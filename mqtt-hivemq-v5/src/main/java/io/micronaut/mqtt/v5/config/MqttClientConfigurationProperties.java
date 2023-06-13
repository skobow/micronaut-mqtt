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
package io.micronaut.mqtt.v5.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.mqtt.config.MqttSSLConfiguration;
import jakarta.validation.constraints.NotNull;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import java.time.Duration;
import java.util.Properties;

/**
 * Configuration for the MQTT client.
 *
 * @author James Kleeh
 * @author Sven Kobow
 * @since 1.0.0
 */
@ConfigurationProperties("mqtt.client")
public class MqttClientConfigurationProperties implements MqttSSLConfiguration {

    private String serverUri;
    private String clientId;
    private Duration connectionTimeout = Duration.ofSeconds(3);
    private Boolean manualAcks;
    private SocketFactory socketFactory;
    private Properties sslProperties;
    private boolean isHttpsHostnameVerificationEnabled;
    private HostnameVerifier sslHostnameVerifier;
    private WillMessage willMessage;

//    @ConfigurationBuilder(excludes = {"socketFactory", "SSLProperties", "httpsHostnameVerificationEnabled", "SSLHostnameVerifier"})
//    private final MqttConnectionOptions connectOptions = new MqttConnectionOptions();

    public MqttClientConfigurationProperties(WillMessage willMessage) {
        if (willMessage.getTopic() != null) {
            this.willMessage = willMessage;
        }
    }

    /**
     * @return The connection options
     */
//    public MqttConnectionOptions getConnectOptions() {
//        return connectOptions;
//    }

    /**
     * @return The server URI
     */
    @NotNull
    public String getServerUri() {
        return serverUri;
    }

    /**
     * @param serverUri The server URI
     */
    public void setServerUri(String serverUri) {
        this.serverUri = serverUri;
    }

    /**
     * @return The client id
     */
    @NotNull
    public String getClientId() {
        return clientId;
    }

    /**
     * @param clientId The client ID
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * @return The connection timeout
     */
    public Duration getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * @param connectionTimeout How long to wait for a connection
     */
    public void setConnectionTimeout(Duration connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    @Override
    public SocketFactory getSocketFactory() {
        return this.socketFactory;
    }

    @Override
    public void setSocketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    @Override
    public Properties getSSLProperties() {
        return this.sslProperties;
    }

    @Override
    public void setSSLProperties(Properties props) {
        this.sslProperties = props;
    }

    @Override
    public boolean isHttpsHostnameVerificationEnabled() {
        return this.isHttpsHostnameVerificationEnabled;
    }

    @Override
    public void setHttpsHostnameVerificationEnabled(boolean httpsHostnameVerificationEnabled) {
        this.isHttpsHostnameVerificationEnabled = httpsHostnameVerificationEnabled;
    }

    @Override
    public HostnameVerifier getSSLHostnameVerifier() {
        return this.sslHostnameVerifier;
    }

    @Override
    public void setSSLHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.sslHostnameVerifier = hostnameVerifier;
    }

    @ConfigurationProperties("will-message")
    static class WillMessage {

        private String topic;
        private byte[] payload;
        private int qos;
        private boolean retained;

        /**
         * @return The topic to publish to
         */
        public String getTopic() {
            return topic;
        }

        /**
         * @param topic The topic to publish to
         */
        public void setTopic(String topic) {
            this.topic = topic;
        }

        /**
         * @return The message payload
         */
        public byte[] getPayload() {
            return payload;
        }

        /**
         * @param payload The message payload
         */
        public void setPayload(byte[] payload) {
            this.payload = payload;
        }

        /**
         * @return The message qos
         */
        public int getQos() {
            return qos;
        }

        /**
         * @param qos The message qos
         */
        public void setQos(int qos) {
            this.qos = qos;
        }

        /**
         * @return True if the message should be retained
         */
        public boolean isRetained() {
            return retained;
        }

        /**
         * @param retained If the message should be retained
         */
        public void setRetained(boolean retained) {
            this.retained = retained;
        }
    }
}
