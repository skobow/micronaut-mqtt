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

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.mqtt.ssl.MqttCertificateConfiguration;
import jakarta.validation.constraints.NotNull;

import javax.net.ssl.HostnameVerifier;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

/**
 * Configuration for the MQTT client.
 *
 * @author James Kleeh
 * @author Sven Kobow
 * @since 1.0.0
 */
@ConfigurationProperties("mqtt.client")
public class MqttClientConfigurationProperties {

    private String serverUri;
    private String clientId;
    private Duration connectionTimeout = Duration.ofSeconds(3);
    private Boolean manualAcks;
    private byte[] password;
    private String userName;
    private int maxReconnectDelay;
    private int keepAliveInterval;
    private int maxInflight;
    private boolean cleanSession;
    private boolean automaticReconnect;
    private Map<String, String> customWebSocketHeaders;
    private boolean isHttpsHostnameVerificationEnabled;
    private HostnameVerifier sslHostnameVerifier;
    private WillMessage willMessage;
    private MqttCertificateConfiguration certificateConfiguration;

    public MqttClientConfigurationProperties(
        @Nullable final WillMessage willMessage,
        @Nullable final MqttCertificateConfiguration certificateConfiguration) {
        this.certificateConfiguration = certificateConfiguration;
        if (willMessage.getTopic() != null) {
            this.willMessage = willMessage;
        }
    }

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

    /**
     * @return An optional boolean to set the client in manual acknowledge mode
     */
    public Optional<Boolean> getManualAcks() {
        return Optional.ofNullable(manualAcks);
    }

    /**
     * @param manualAcks Set to true if you wish to manually acknowledge messages
     */
    public void setManualAcks(Boolean manualAcks) {
        this.manualAcks = manualAcks;
    }

    /**
     * @return The password to use for MQTT connections.
     */
    public byte[] getPassword() {
        return password;
    }

    /**
     * @param password The password to use for MQTT connections.
     */
    public void setPassword(final byte[] password) {
        this.password = password;
    }

    /**
     * @return The username to use for MQTT connections.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName The username to use for MQTT connections.
     */
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * @return The maximal delay for reconnecting.
     */
    public int getMaxReconnectDelay() {
        return maxReconnectDelay;
    }

    /**
     * @param maxReconnectDelay The maximum delay for reconnecting.
     */
    public void setMaxReconnectDelay(final int maxReconnectDelay) {
        this.maxReconnectDelay = maxReconnectDelay;
    }

    /**
     * @return The keep alive interval.
     */
    public int getKeepAliveInterval() {
        return keepAliveInterval;
    }

    /**
     * @param keepAliveInterval The keep alive interval.
     */
    public void setKeepAliveInterval(final int keepAliveInterval) {
        this.keepAliveInterval = keepAliveInterval;
    }

    /**
     * @return The maximal inflight.
     */
    public int getMaxInflight() {
        return maxInflight;
    }

    /**
     * @param maxInflight The maximal inflight.
     */
    public void setMaxInflight(final int maxInflight) {
        this.maxInflight = maxInflight;
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
     * @return True is automatic reconnect should be performed.
     */
    public boolean isAutomaticReconnect() {
        return automaticReconnect;
    }

    /**
     * @param automaticReconnect If an automatic reconnect should be performed.
     */
    public void setAutomaticReconnect(final boolean automaticReconnect) {
        this.automaticReconnect = automaticReconnect;
    }

    /**
     * @return The custom headers that should be sent with web socket connections.
     */
    public Map<String, String> getCustomWebSocketHeaders() {
        return customWebSocketHeaders;
    }

    /**
     * @param customWebSocketHeaders The custom headers that should be sent with web socket connections.
     */
    public void setCustomWebSocketHeaders(final Map<String, String> customWebSocketHeaders) {
        this.customWebSocketHeaders = customWebSocketHeaders;
    }

    /**
     * @return True if hostname verification should be used.
     */
    public boolean isHttpsHostnameVerificationEnabled() {
        return this.isHttpsHostnameVerificationEnabled;
    }

    /**
     * @param httpsHostnameVerificationEnabled True if hostname verification should be used.
     */
    public void setHttpsHostnameVerificationEnabled(boolean httpsHostnameVerificationEnabled) {
        this.isHttpsHostnameVerificationEnabled = httpsHostnameVerificationEnabled;
    }

    /**
     * @return The hostname verifier to use for hostname verification.
     */
    public HostnameVerifier getSSLHostnameVerifier() {
        return sslHostnameVerifier;
    }

    /**
     * @param hostnameVerifier The hostname verifier to use for hostname verification.
     */
    public void setSSLHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.sslHostnameVerifier = hostnameVerifier;
    }

    /**
     * @return The last will message that should be sent on ungraceful disconnects.
     */
    public WillMessage getWillMessage() {
        return willMessage;
    }

    /**
     * @return The SSL certificate configuration with CA and client certificates.
     */
    public MqttCertificateConfiguration getCertificateConfiguration() {
        return certificateConfiguration;
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
