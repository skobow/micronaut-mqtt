package io.micronaut.mqtt.ssl

import io.micronaut.context.annotation.Requires
import io.micronaut.mqtt.annotation.Topic
import io.micronaut.mqtt.v5.annotation.MqttPublisher

@Requires(property = "spec.name", value = "SslAuthenticationSpec")
@MqttPublisher
interface ProductClient {

    @Topic("product")
    void send(byte[] data)
}
