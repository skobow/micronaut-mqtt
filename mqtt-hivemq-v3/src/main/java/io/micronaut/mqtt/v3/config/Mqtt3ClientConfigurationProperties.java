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
package io.micronaut.mqtt.v3.config;

import com.hivemq.client.mqtt.mqtt3.message.connect.Mqtt3Connect;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.mqtt.config.MqttClientConfigurationProperties;
import io.micronaut.mqtt.ssl.MqttSslConfiguration;

import java.net.URI;

/**
 * Configuration for the MQTT client.
 *
 * @author James Kleeh
 * @author Sven Kobow
 * @since 1.0.0
 */
@ConfigurationProperties(value = "mqtt.client")
public class Mqtt3ClientConfigurationProperties extends MqttClientConfigurationProperties {

    private boolean cleanSession = Mqtt3Connect.DEFAULT_CLEAN_SESSION;
    private MqttSslConfiguration sslConfiguration;

    /**
     * @param willMessage An optional last will message
     * @param sslConfiguration An optional configuration for SSL certificates
     */
    public Mqtt3ClientConfigurationProperties(
        @Nullable final WillMessage willMessage,
        @Nullable final MqttSslConfiguration sslConfiguration) {
        super(willMessage);
        this.sslConfiguration = sslConfiguration;
    }

    @Override
    protected boolean isSSL() {
        return sslConfiguration != null && sslConfiguration.isEnabled();
    }

    @Override
    public void onChangeServerUri(final URI serverUri) {
        if (serverUri.getScheme().equals("ssl")) {
            getSslConfiguration().setEnabled(true);
        }
    }

    /**
     * @param cleanSession True if a new session should be started for connection.
     */
    public void setCleanSession(final boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    /**
     * @return If connection should start a new session.
     */
    public boolean isCleanSession() {
        return this.cleanSession;
    }

    /**
     * @return The SSL configuration options.
     */
    public MqttSslConfiguration getSslConfiguration() {
        if (sslConfiguration == null) {
            sslConfiguration = new MqttSslConfiguration();
        }
        return sslConfiguration;
    }
}
