/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.mqtt.v3.client;

import com.hivemq.client.mqtt.*;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.lifecycle.MqttClientAutoReconnect;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3ClientBuilder;
import com.hivemq.client.mqtt.mqtt3.message.connect.Mqtt3Connect;
import com.hivemq.client.mqtt.mqtt3.message.connect.Mqtt3ConnectBuilder;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.exceptions.BeanInstantiationException;
import io.micronaut.core.util.StringUtils;
import io.micronaut.mqtt.exception.MqttClientException;
import io.micronaut.mqtt.ssl.*;
import io.micronaut.mqtt.v3.config.MqttClientConfigurationProperties;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * A factory to create an MQTT client.
 *
 * @author James Kleeh
 * @author Sven Kobow
 * @since 1.0.0
 */
@Factory
public final class MqttClientFactory implements KeyManagerFactoryProvider, TrustManagerFactoryProvider {

    private static final Logger LOG = LoggerFactory.getLogger(MqttClientFactory.class);

    @Singleton
    @Bean(preDestroy = "disconnect")
    Mqtt3AsyncClient mqttClient(MqttClientConfigurationProperties configuration) {

        final Mqtt3ClientBuilder clientBuilder = MqttClient.builder()
            .useMqttVersion3()
            .identifier(configuration.getClientId())
            .transportConfig(buildTransportConfig(configuration));

        if (configuration.isAutomaticReconnect()) {
            clientBuilder.automaticReconnect()
                .initialDelay(MqttClientAutoReconnect.DEFAULT_START_DELAY_S, TimeUnit.SECONDS)
                .maxDelay(configuration.getMaxReconnectDelay(), TimeUnit.SECONDS)
                .applyAutomaticReconnect();
        }

        final Mqtt3ConnectBuilder connectBuilder = Mqtt3Connect.builder()
            .cleanSession(configuration.isCleanSession())
            .keepAlive(configuration.getKeepAliveInterval());

        if (StringUtils.isNotEmpty(configuration.getUserName())) {
            connectBuilder.simpleAuth()
                .username(configuration.getUserName())
                .password(configuration.getPassword())
                .applySimpleAuth();
        }

        if (configuration.getWillMessage() != null) {
            var willMessage = configuration.getWillMessage();

            connectBuilder.willPublish()
                .topic(willMessage.getTopic())
                .payload(willMessage.getPayload())
                .qos(Objects.requireNonNull(MqttQos.fromCode(willMessage.getQos())))
                .retain(willMessage.isRetained());
        }

        final var client = clientBuilder.buildAsync();

        client.connect(connectBuilder.build())
            .whenComplete((mqtt3ConnAck, throwable) -> {
                if (throwable != null) {
                    throw new MqttClientException("Error connecting mqtt client");
                }
            })
            .join();

        return client;
    }

    private MqttClientTransportConfig buildTransportConfig(final MqttClientConfigurationProperties configuration) {
        final URI serverUri = URI.create(configuration.getServerUri());
        if (LOG.isTraceEnabled()) {
            LOG.trace("Connecting to {} on port {}", serverUri.getHost(), serverUri.getPort());
        }

        final MqttClientTransportConfigBuilder transportConfigBuilder = MqttClientTransportConfig.builder()
            .serverHost(serverUri.getHost())
            .serverPort(serverUri.getPort())
            .mqttConnectTimeout(configuration.getConnectionTimeout().toMillis(), TimeUnit.MILLISECONDS);

        if (configuration.getCertificateConfiguration() != null && "ssl".equals(serverUri.getScheme())) {
            final MqttCertificateConfiguration certConfiguration = configuration.getCertificateConfiguration();
            final MqttClientSslConfigBuilder sslConfigBuilder = MqttClientSslConfig.builder();
            if (configuration.isHttpsHostnameVerificationEnabled()) {
                sslConfigBuilder.hostnameVerifier(configuration.getSSLHostnameVerifier());
            }

            try {
                sslConfigBuilder
                    .keyManagerFactory(getKeyManagerFactory(certConfiguration))
                    .trustManagerFactory(getTrustManagerFactory(certConfiguration));

            } catch (KeyManagerFactoryCreationException | TrustManagerFactoryCreationException e) {
                LOG.error(e.getMessage(), e);
                throw new BeanInstantiationException(e.getMessage(), e);
            }

            if (configuration.isHttpsHostnameVerificationEnabled()) {
                sslConfigBuilder.hostnameVerifier(configuration.getSSLHostnameVerifier());
            }
        }

        return transportConfigBuilder.build();
    }
}
