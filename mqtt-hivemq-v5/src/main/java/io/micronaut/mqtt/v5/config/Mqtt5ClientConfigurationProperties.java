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

import com.hivemq.client.mqtt.mqtt5.message.connect.Mqtt5Connect;
import com.hivemq.client.mqtt.mqtt5.message.connect.Mqtt5ConnectRestrictions;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.mqtt.config.MqttClientConfigurationProperties;
import io.micronaut.mqtt.ssl.MqttSslConfiguration;

import java.net.URI;
import java.util.Map;

/**
 * Configuration for the MQTT client.
 *
 * @author James Kleeh
 * @author Sven Kobow
 * @since 1.0.0
 */
@ConfigurationProperties(value = "mqtt.client")
public class Mqtt5ClientConfigurationProperties extends MqttClientConfigurationProperties {

    private boolean cleanStart = Mqtt5Connect.DEFAULT_CLEAN_START;
    private Long sessionExpiryInterval = Mqtt5Connect.DEFAULT_SESSION_EXPIRY_INTERVAL;
    private Integer receiveMaximum = Mqtt5ConnectRestrictions.DEFAULT_RECEIVE_MAXIMUM;
    private Integer maximumPacketSize = Mqtt5ConnectRestrictions.DEFAULT_MAXIMUM_PACKET_SIZE;
    private Integer topicAliasMaximum = Mqtt5ConnectRestrictions.DEFAULT_TOPIC_ALIAS_MAXIMUM;
    private boolean requestResponseInfo = Mqtt5ConnectRestrictions.DEFAULT_REQUEST_RESPONSE_INFORMATION;
    private boolean requestProblemInfo = Mqtt5ConnectRestrictions.DEFAULT_REQUEST_PROBLEM_INFORMATION;
    private Map<String, String> userProperties;

    private MqttSslConfiguration sslConfiguration;

    /**
     * @param willMessage An optional last will message
     * @param sslConfiguration An optional configuration for SSL certificates
     */
    public Mqtt5ClientConfigurationProperties(
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
     * @return The SSL configuration options.
     */
    public MqttSslConfiguration getSslConfiguration() {
        if (sslConfiguration == null) {
            sslConfiguration = new MqttSslConfiguration();
        }
        return sslConfiguration;
    }


}
