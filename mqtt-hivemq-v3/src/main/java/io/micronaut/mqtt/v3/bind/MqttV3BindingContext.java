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
package io.micronaut.mqtt.v3.bind;

import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import io.micronaut.core.annotation.Internal;
import io.micronaut.mqtt.bind.MqttBindingContext;
import io.micronaut.mqtt.bind.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A binding context for MQTT v3 messages.
 *
 * @author James Kleeh
 * @since 1.0.0
 */
@Internal
public final class MqttV3BindingContext implements MqttBindingContext<MqttMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(MqttV3BindingContext.class);

    private final Mqtt3AsyncClient client;
    private final MqttMessage message;
    private String topic;
    private boolean manualAcks = false;
    private Mqtt3Publish mqtt3Publish;

    /**
     * @param client The client
     * @param message The message
     */
    public MqttV3BindingContext(Mqtt3AsyncClient client, MqttMessage message) {
        this.client = client;
        this.message = message;
    }

    @Override
    public byte[] getPayload() {
        return message.getPayload();
    }

    @Override
    public void setPayload(byte[] payload) {
        message.setPayload(payload);
    }

    @Override
    public boolean isRetained() {
        return message.isRetained();
    }

    @Override
    public void setRetained(boolean retained) {
        message.setRetained(retained);
    }

    @Override
    public int getQos() {
        return message.getQos();
    }

    @Override
    public void setQos(int qos) {
        message.setQos(qos);
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public int getId() {
        return message.getId();
    }

    @Override
    public void acknowlege() {
        if (mqtt3Publish != null && manualAcks) {
            mqtt3Publish.acknowledge();
        }
    }

    @Override
    public MqttMessage getNativeMessage() {
        return message;
    }

    public void setManualAcks(final boolean manualAcks) {
        this.manualAcks = manualAcks;
    }

    public boolean getManualAcks() {
        return manualAcks;
    }

    public void setMqtt3Publish(final Mqtt3Publish mqtt3Publish) {
        this.mqtt3Publish = mqtt3Publish;
    }

    public Mqtt3Publish getMqtt3Publish() {
        return mqtt3Publish;
    }
}
