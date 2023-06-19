/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.mqtt.v5.client;

import com.hivemq.client.mqtt.*;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.mqtt.exception.MqttClientException;
import io.micronaut.mqtt.ssl.CertificateReader;
import io.micronaut.mqtt.ssl.MqttCertificateConfiguration;
import io.micronaut.mqtt.ssl.PrivateKeyReader;
import io.micronaut.mqtt.v5.config.MqttClientConfigurationProperties;
import io.micronaut.scheduling.TaskExecutors;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.net.URI;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A factory to create an MQTT client.
 *
 * @author James Kleeh
 * @author Sven Kobow
 * @since 1.0.0
 */
@Factory
public final class MqttClientFactory {

    private static final Logger LOG = LoggerFactory.getLogger(MqttClientFactory.class);

    @Singleton
    @Bean(preDestroy = "disconnect")
    Mqtt5AsyncClient mqttClient(final MqttClientConfigurationProperties configuration,
                                @Named(TaskExecutors.MESSAGE_CONSUMER) final ExecutorService executorService) {
        final ScheduledExecutorService consumerExecutor = (ScheduledExecutorService) executorService;

        final URI serverUri = URI.create(configuration.getServerUri());
        if (LOG.isTraceEnabled()) {
            LOG.trace("Connecting to {} on port {}", serverUri.getHost(), serverUri.getPort());
        }

        final MqttClientTransportConfigBuilder transportConfigBuilder = MqttClientTransportConfig.builder()
            .mqttConnectTimeout(configuration.getConnectionTimeout().toMillis(), TimeUnit.MILLISECONDS)
            .serverHost(serverUri.getHost())
            .serverPort(serverUri.getPort());

        if ("ssl".equals(serverUri.getScheme())) {
            final MqttCertificateConfiguration certConfiguration = configuration.getCertificateConfiguration();

            final MqttClientSslConfigBuilder sslConfigBuilder = MqttClientSslConfig.builder();

            sslConfigBuilder.keyManagerFactory(buildKeyManagerFactory(certConfiguration));
            sslConfigBuilder.trustManagerFactory(buildTrustManagerFactory(certConfiguration));

            if (configuration.isHttpsHostnameVerificationEnabled()) {
                sslConfigBuilder.hostnameVerifier(configuration.getSSLHostnameVerifier());
            }

            transportConfigBuilder.sslConfig(sslConfigBuilder.build());
        }

        final Mqtt5AsyncClient client = MqttClient.builder()
            .executorConfig(MqttClientExecutorConfig.builder().nettyExecutor(consumerExecutor).build())
            .useMqttVersion5()
            .identifier(configuration.getClientId())
            .transportConfig(transportConfigBuilder.build())
            .buildAsync();

        client.connect()
            .whenComplete((mqtt3ConnAck, throwable) -> {
                if (throwable != null) {
                    throw new MqttClientException("Error connecting mqtt client");
                }
            })
            .join();

        return client;
    }

    private KeyManagerFactory buildKeyManagerFactory(final MqttCertificateConfiguration certConfiguration) {
        try {
            final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            final Certificate certificate = CertificateReader.readCertificate(certConfiguration.getCertificate());

            final PrivateKey key = PrivateKeyReader.getPrivateKey(certConfiguration.getPrivateKey(), certConfiguration.getPassword());

            keyStore.load(null, null);
            keyStore.setCertificateEntry("certificate", certificate);
            keyStore.setKeyEntry("private-key", key, certConfiguration.getPassword(), new Certificate[]{certificate});

            kmf.init(keyStore, certConfiguration.getPassword());

            return kmf;
        } catch (NoSuchAlgorithmException | KeyStoreException | IOException | CertificateException |
                 UnrecoverableKeyException e) {
            LOG.error("Error creating KeyManagerFactory: {}", e.getMessage(), e);
        }

        return null;
    }

    private TrustManagerFactory buildTrustManagerFactory(final MqttCertificateConfiguration certConfiguration) {
        try {
            final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            final Certificate certificate = CertificateReader.readCertificate(certConfiguration.getCertificateAuthority());

            keyStore.load(null);
            keyStore.setCertificateEntry("ca-certificate", certificate);

            tmf.init(keyStore);

            return tmf;
        } catch (NoSuchAlgorithmException | KeyStoreException | IOException | CertificateException e) {
            LOG.error("Error creating TrustManagerFactory: {}", e.getMessage(), e);
        }

        return null;
    }
}
