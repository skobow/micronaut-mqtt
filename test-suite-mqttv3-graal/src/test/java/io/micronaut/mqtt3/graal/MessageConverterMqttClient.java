package io.micronaut.mqtt3.graal;

import io.micronaut.mqtt.annotation.Topic;

@MqttPublisher
public interface MessageConverterMqttClient {

    @Topic("message")
    void sendMessage(MessageRequest messageRequest);

}
