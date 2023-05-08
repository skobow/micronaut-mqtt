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
package io.micronaut.mqtt.bind;

/**
 * A MQTT message holds the payload and options
 * for message delivery.
 *
 * @author Sven Kobow
 */
public class MqttMessage {

    private boolean mutable = true;
    private byte[] payload;
    private int qos = 1;
    private boolean retained = false;
    private boolean dup = false;
    private int messageId;

    public MqttMessage() {
        setPayload(new byte[]{});
    }

    public MqttMessage(final byte[] payload) {
        setPayload(payload);
    }

    public boolean getMutable() {
        return mutable;
    }

    public void setMutable(final boolean mutable) {
        this.mutable = mutable;
    }

    /**
     * Returns the payload as byte array.
     *
     * @return payload The payload byte array
     */
    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(final byte[] payload) {
        this.payload = payload;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(final int qos) {
        this.qos = qos;
    }

    public boolean getRetained() {
        return retained;
    }

    public void setRetained(final boolean retained) {
        this.retained = retained;
    }

    public boolean getDup() {
        return dup;
    }

    public void setDup(final boolean dup) {
        this.dup = dup;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(final int messageId) {
        this.messageId = messageId;
    }
}
