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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents MQTT v5 Properties including user properties.
 */
public class MqttProperties {

    private byte[] correlationData;
    private List<UserProperty> userProperties = new ArrayList<>();

    /**
     * User properties for MQTT v5 messages.
     * @return a list of user defined properties
     */
    public List<UserProperty> getUserProperties() {
        return this.userProperties;
    }

    /**
     * MQTT message correlation data.
     * @param value Correlation data as byte array
     */
    public void setCorrelationData(final byte[] value) {
        this.correlationData = value;
    }

    /**
     * MQTT message correlation data.
     * @return correlation data as byte array
     */
    public byte[] getCorrelationData() {
        return this.correlationData;
    }
}
