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

import com.hivemq.client.mqtt.MqttClientTransportConfig;
import com.hivemq.client.mqtt.lifecycle.MqttClientAutoReconnect;
import com.hivemq.client.mqtt.mqtt5.message.connect.Mqtt5Connect;
import com.hivemq.client.mqtt.mqtt5.message.connect.Mqtt5ConnectRestrictions;
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
    private Duration connectionTimeout = Duration.ofMillis(MqttClientTransportConfig.DEFAULT_MQTT_CONNECT_TIMEOUT_MS);
    private Boolean manualAcks = false;
    private String userName = null;
    private byte[] password = null;
    private boolean cleanStart = Mqtt5Connect.DEFAULT_CLEAN_START;
    private Integer keepAliveInterval = Mqtt5Connect.DEFAULT_KEEP_ALIVE;
    private Long maxReconnectDelay = MqttClientAutoReconnect.DEFAULT_MAX_DELAY_S;
    private boolean automaticReconnect = false;
    private Long sessionExpiryInterval = Mqtt5Connect.DEFAULT_SESSION_EXPIRY_INTERVAL;
    private Integer receiveMaximum = Mqtt5ConnectRestrictions.DEFAULT_RECEIVE_MAXIMUM;
    private Integer maximumPacketSize = Mqtt5ConnectRestrictions.DEFAULT_MAXIMUM_PACKET_SIZE;
    private Integer topicAliasMaximum = Mqtt5ConnectRestrictions.DEFAULT_TOPIC_ALIAS_MAXIMUM;
    private boolean requestResponseInfo = Mqtt5ConnectRestrictions.DEFAULT_REQUEST_RESPONSE_INFORMATION;
    private boolean requestProblemInfo = Mqtt5ConnectRestrictions.DEFAULT_REQUEST_PROBLEM_INFORMATION;
    private Map<String, String> userProperties;
    @Deprecated
    private String authMethod;
    @Deprecated
    private byte[] authData;
    @Deprecated
    private boolean useSubscriptionIdentifiers;
    private Map<String, String> customWebSocketHeaders = null;
    @Deprecated
    private boolean sendReasonMessages;
    private boolean isHttpsHostnameVerificationEnabled = false;
    private HostnameVerifier sslHostnameVerifier = null;
    private WillMessage willMessage = null;
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
    public void setServerUri(final String serverUri) {
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
    public void setClientId(final String clientId) {
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
    public void setConnectionTimeout(final Duration connectionTimeout) {
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
    public void setManualAcks(final Boolean manualAcks) {
        this.manualAcks = manualAcks;
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
     * @return The password to use for MQTT connections.
     */
    public byte[] getPassword() {
        return this.password;
    }

    /**
     * @param password The password to use for MQTT connections.
     */
    public void setPassword(final byte[] password) {
        this.password = password;
    }

    /**
     * @return True if a new session should be started for connection.
     */
    public boolean isCleanStart() {
        return this.cleanStart;
    }

    /**
     * @param cleanStart If connection should start a new session.
     */
    public void setCleanStart(final boolean cleanStart) {
        this.cleanStart = cleanStart;
    }

    /**
     * @return The keep alive interval.
     */
    public Integer getKeepAliveInterval() {
        return this.keepAliveInterval;
    }

    /**
     * @param keepAliveInterval The keep alive interval.
     */
    public void setKeepAliveInterval(final Integer keepAliveInterval) {
        this.keepAliveInterval = keepAliveInterval;
    }

    /**
     * @return The maximal delay for reconnecting.
     */
    public Long getMaxReconnectDelay() {
        return this.maxReconnectDelay;
    }

    /**
     * @param maxReconnectDelay The maximum delay for reconnecting.
     */
    public void setMaxReconnectDelay(final Long maxReconnectDelay) {
        this.maxReconnectDelay = maxReconnectDelay;
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
     * @return The session expiry interval.
     */
    public Long getSessionExpiryInterval() {
        return this.sessionExpiryInterval;
    }

    /**
     * @param sessionExpiryInterval The session expiry interval.
     */
    public void setSessionExpiryInterval(final Long sessionExpiryInterval) {
        this.sessionExpiryInterval = sessionExpiryInterval;
    }

    /**
     * @return The receive maximum.
     */
    public Integer getReceiveMaximum() {
        return this.receiveMaximum;
    }

    /**
     * @param receiveMaximum The receive maximum.
     */
    public void setReceiveMaximum(final Integer receiveMaximum) {
        this.receiveMaximum = receiveMaximum;
    }

    /**
     * @return The maximum paket size.
     */
    public Integer getMaximumPacketSize() {
        return this.maximumPacketSize;
    }

    /**
     * @param maximumPacketSize The maximum paket size.
     */
    public void setMaximumPacketSize(final Integer maximumPacketSize) {
        this.maximumPacketSize = maximumPacketSize;
    }

    /**
     * @return The topic alias maximum.
     */
    public Integer getTopicAliasMaximum() {
        return this.topicAliasMaximum;
    }

    /**
     * @param topicAliasMaximum The topic alias maximum.
     */
    public void setTopicAliasMaximum(final Integer topicAliasMaximum) {
        this.topicAliasMaximum = topicAliasMaximum;
    }

    /**
     * @return True if the server should return response information.
     */
    public boolean isRequestResponseInfo() {
        return this.requestResponseInfo;
    }

    /**
     * @param requestResponseInfo If the server should return response information.
     */
    public void setRequestResponseInfo(final boolean requestResponseInfo) {
        this.requestResponseInfo = requestResponseInfo;
    }

    /**
     * @return True if reason string or user properties are sent in case of failure.
     */
    public boolean isRequestProblemInfo() {
        return this.requestProblemInfo;
    }

    /**
     * @param requestProblemInfo If reason string or user properties are sent in case of failure.
     */
    public void setRequestProblemInfo(final boolean requestProblemInfo) {
        this.requestProblemInfo = requestProblemInfo;
    }

    /**
     * @return The user defined properties.
     */
    public Map<String, String> getUserProperties() {
        return this.userProperties;
    }

    /**
     * @param userProperties The user defined properties.
     */
    public void setUserProperties(final Map<String, String> userProperties) {
        this.userProperties = userProperties;
    }

    /**
     * @return The name of the authentication method.
     */
    public String getAuthMethod() {
        return this.authMethod;
    }

    /**
     * @param authMethod The name of the authentication method.
     */
    public void setAuthMethod(final String authMethod) {
        this.authMethod = authMethod;
    }

    /**
     * @return The authentication data.
     */
    public byte[] getAuthData() {
        return this.authData;
    }

    /**
     * @param authData The authentication data.
     */
    public void setAuthData(final byte[] authData) {
        this.authData = authData;
    }

    /**
     * @return True if subscription identifiers should be used.
     */
    public boolean isUseSubscriptionIdentifiers() {
        return this.useSubscriptionIdentifiers;
    }

    /**
     * @param useSubscriptionIdentifiers If subscription identifiers should be used.
     */
    public void setUseSubscriptionIdentifiers(final boolean useSubscriptionIdentifiers) {
        this.useSubscriptionIdentifiers = useSubscriptionIdentifiers;
    }

    /**
     * @return The custom headers that should be sent with web socket connections.
     */
    public Map<String, String> getCustomWebSocketHeaders() {
        return this.customWebSocketHeaders;
    }

    /**
     * @param customWebSocketHeaders The custom headers that should be sent with web socket connections.
     */
    public void setCustomWebSocketHeaders(final Map<String, String> customWebSocketHeaders) {
        this.customWebSocketHeaders = customWebSocketHeaders;
    }

    /**
     * @return True if reason messages should be sent.
     */
    public boolean isSendReasonMessages() {
        return this.sendReasonMessages;
    }

    /**
     * @param sendReasonMessages True if reason messages should be sent.
     */
    public void setSendReasonMessages(final boolean sendReasonMessages) {
        this.sendReasonMessages = sendReasonMessages;
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
    public void setHttpsHostnameVerificationEnabled(final boolean httpsHostnameVerificationEnabled) {
        this.isHttpsHostnameVerificationEnabled = httpsHostnameVerificationEnabled;
    }

    /**
     * @return The hostname verifier to use for hostname verification.
     */
    public HostnameVerifier getSSLHostnameVerifier() {
        return this.sslHostnameVerifier;
    }

    /**
     * @param hostnameVerifier The hostname verifier to use for hostname verification.
     */
    public void setSSLHostnameVerifier(final Optional<HostnameVerifier> hostnameVerifier) {
        hostnameVerifier.ifPresent(hnv -> this.sslHostnameVerifier = hnv);
    }

    /**
     * @return The last will message that should be sent on ungraceful disconnects.
     */
    public WillMessage getWillMessage() {
        return this.willMessage;
    }

    /**
     * @return The SSL certificate configuration with CA and client certificates.
     */
    public MqttCertificateConfiguration getCertificateConfiguration() {
        return certificateConfiguration;
    }

    @ConfigurationProperties("will-message")
    public static class WillMessage {

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
