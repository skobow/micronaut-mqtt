package micronaut.mqtt.config

import io.micronaut.context.ApplicationContext
import io.micronaut.core.io.Readable
import io.micronaut.mqtt.v5.config.Mqtt5ClientConfigurationProperties
import spock.lang.Specification

import javax.net.ssl.HostnameVerifier
import java.time.Duration

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

class V5ClientConfigurationSpec extends Specification {

    final String SERVER_HOST = "localhost"
    final Integer SERVER_PORT = 1883
    final String CLIENT_ID = "client-id"
    final Duration CONNECTION_TIMEOUT = Duration.ofMillis(1000)
    final boolean MANUAL_ACKS = false
    final String USER_NAME = "username"
    final byte[] PASSWORD = "password".bytes
    final boolean CLEAN_START = false
    final int KEEP_ALIVE_INTERVAL = 5
    final long MAX_RECONNECT_DELAY = 10
    final List<String> SERVER_URIS = ["server1", "server2"]
    final boolean AUTOMATIC_RECONNECT = false
    final Long SESSION_EXPIRY_INTERVAL = 10
    final Integer RECEIVE_MAXIMUM = 10
    final Long MAXIMUM_PACKET_SIZE = 10
    final Integer TOPIC_ALIAS_MAXIMUM = 2
    final boolean REQUEST_RESPONSE_INFO = false
    final boolean REQUEST_PROBLEM_INFO = false
    final Map<String, String> USER_PROPERTIES = ["up1": "value1", "up2": "value2"]
    final String AUTH_METHOD = "enhanced"
    final byte[] AUTH_DATA = []
    final boolean USE_SUBSCRIPTION_IDENTIFIERS = false
    final Map<String, String> CUSTOM_WEB_SOCKET_HEADERS = ["header1": "value1", "header2": "value2"]
    final boolean SEND_REASON_MESSAGES = false
    final boolean HOSTNAME_VERIFICATION_ENABLED = false
    final HostnameVerifier HOSTNAME_VERIFIER = null

    final boolean SSL_ENABLED = false
    final Readable SSL_CERTIFICATE_AUTHORITY = null
    final Readable SSL_CERTIFICATE = null
    final Readable SSL_PRIVATE_KEY = null
    final char[] SSL_PASSWORD = "password".bytes

    void "test client configuration"() {
        ApplicationContext applicationContext = startContext()

        when:
        def config = applicationContext.getBean(Mqtt5ClientConfigurationProperties)

        then:
        config.serverHost == SERVER_HOST
        config.serverPort == SERVER_PORT
        config.clientId == CLIENT_ID
        config.connectionTimeout == CONNECTION_TIMEOUT
        config.manualAcks == MANUAL_ACKS
        config.userName == USER_NAME
        config.password == PASSWORD
        config.cleanStart == CLEAN_START
        config.keepAliveInterval == KEEP_ALIVE_INTERVAL
        config.maxReconnectDelay == MAX_RECONNECT_DELAY
        config.automaticReconnect == AUTOMATIC_RECONNECT
        config.sessionExpiryInterval == SESSION_EXPIRY_INTERVAL
        config.receiveMaximum == RECEIVE_MAXIMUM
        config.maximumPacketSize == MAXIMUM_PACKET_SIZE
        config.topicAliasMaximum == TOPIC_ALIAS_MAXIMUM
        config.requestResponseInfo == REQUEST_RESPONSE_INFO
        config.requestProblemInfo == REQUEST_PROBLEM_INFO
        config.userProperties == USER_PROPERTIES
        config.authMethod == AUTH_METHOD
        config.authData == AUTH_DATA
        config.useSubscriptionIdentifiers == USE_SUBSCRIPTION_IDENTIFIERS
        config.customWebSocketHeaders == CUSTOM_WEB_SOCKET_HEADERS
        config.sendReasonMessages == SEND_REASON_MESSAGES

        // SSL configuration properties
        config.httpsHostnameVerificationEnabled == HOSTNAME_VERIFICATION_ENABLED
        config.SSLHostnameVerifier == HOSTNAME_VERIFIER

        def sslConfig = config.getSslConfiguration()
        sslConfig != null
        !sslConfig.enabled
        sslConfig.certificateAuthority == SSL_CERTIFICATE_AUTHORITY
        sslConfig.certificate == SSL_CERTIFICATE
        sslConfig.privateKey == SSL_PRIVATE_KEY
        sslConfig.password == SSL_PASSWORD

        cleanup:
        applicationContext.close()
    }

    ApplicationContext startContext() {
        ApplicationContext.run(
                [
                        "mqtt.client.server-host"                        : SERVER_HOST,
                        "mqtt.client.server-port"                        : SERVER_PORT,
                        "mqtt.client.client-id"                          : CLIENT_ID,
                        "mqtt.client.connection-timeout"                 : CONNECTION_TIMEOUT,
                        "mqtt.client.manual-acks"                        : MANUAL_ACKS,
                        "mqtt.client.user-name"                          : USER_NAME,
                        "mqtt.client.password"                           : PASSWORD,
                        "mqtt.client.clean-start"                        : CLEAN_START,
                        "mqtt.client.keep-alive-interval"                : KEEP_ALIVE_INTERVAL,
                        "mqtt.client.max-reconnect-delay"                : MAX_RECONNECT_DELAY,
                        "mqtt.client.automatic-reconnect"                : AUTOMATIC_RECONNECT,
                        "mqtt.client.session-expiry-interval"            : SESSION_EXPIRY_INTERVAL,
                        "mqtt.client.receive-maximum"                    : RECEIVE_MAXIMUM,
                        "mqtt.client.maximum-packet-size"                : MAXIMUM_PACKET_SIZE,
                        "mqtt.client.topic-alias-maximum"                : TOPIC_ALIAS_MAXIMUM,
                        "mqtt.client.request-response-info"              : REQUEST_RESPONSE_INFO,
                        "mqtt.client.request-problem-info"               : REQUEST_PROBLEM_INFO,
                        "mqtt.client.user-properties"                    : USER_PROPERTIES,
                        "mqtt.client.auth-method"                        : AUTH_METHOD,
                        "mqtt.client.auth-data"                          : AUTH_DATA,
                        "mqtt.client.use-subscription-identifiers"       : USE_SUBSCRIPTION_IDENTIFIERS,
                        "mqtt.client.custom-web-socket-headers"          : CUSTOM_WEB_SOCKET_HEADERS,
                        "mqtt.client.send-reason-messages"               : SEND_REASON_MESSAGES,
                        "mqtt.client.https-hostname-verification-enabled": HOSTNAME_VERIFICATION_ENABLED,
                        "mqtt.client.sslhostname-verifier"               : HOSTNAME_VERIFIER,
                        "mqtt.client.ssl.enabled"                        : SSL_ENABLED,
                        "mqtt.client.ssl.certificate-authority"          : SSL_CERTIFICATE_AUTHORITY,
                        "mqtt.client.ssl.certificate"                    : SSL_CERTIFICATE,
                        "mqtt.client.ssl.private-key"                    : SSL_PRIVATE_KEY,
                        "mqtt.client.ssl.password"                       : SSL_PASSWORD,
                        "spec.name"                                      : getClass().simpleName
                ], "test")
    }
}
