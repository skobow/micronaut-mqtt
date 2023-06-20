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
package io.micronaut.mqtt.v3.intercept;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.datatypes.MqttTopicFilter;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3Subscribe;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3Subscription;
import com.hivemq.client.mqtt.mqtt3.message.unsubscribe.Mqtt3Unsubscribe;
import io.micronaut.context.BeanContext;
import io.micronaut.mqtt.bind.MqttBinderRegistry;
import io.micronaut.mqtt.bind.MqttBindingContext;
import io.micronaut.mqtt.bind.MqttMessage;
import io.micronaut.mqtt.exception.MqttSubscriberException;
import io.micronaut.mqtt.exception.MqttSubscriberExceptionHandler;
import io.micronaut.mqtt.intercept.AbstractMqttSubscriberAdvice;
import io.micronaut.mqtt.v3.bind.MqttV3BindingContext;
import io.micronaut.mqtt.v3.config.MqttClientConfigurationProperties;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * The MQTT v3 implementation of {@link AbstractMqttSubscriberAdvice}.
 *
 * @author James Kleeh
 * @author Sven Kobow
 * @since 1.0.0
 */
@Singleton
public class MqttSubscriberAdvice extends AbstractMqttSubscriberAdvice<MqttMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(MqttSubscriberAdvice.class);
    private final Mqtt3AsyncClient mqttAsyncClient;
    private final MqttClientConfigurationProperties configurationProperties;

    public MqttSubscriberAdvice(BeanContext beanContext,
                                MqttBinderRegistry binderRegistry,
                                MqttSubscriberExceptionHandler exceptionHandler,
                                Mqtt3AsyncClient mqttAsyncClient,
                                MqttClientConfigurationProperties configurationProperties) {
        super(beanContext, binderRegistry, exceptionHandler);
        this.configurationProperties = configurationProperties;
        this.mqttAsyncClient = mqttAsyncClient;
    }

    @Override
    public void subscribe(final Map<String, Integer> topicMap, final Consumer<MqttBindingContext<MqttMessage>> callback) {

        final Mqtt3Subscribe mqttSubscribe = Mqtt3Subscribe.builder()
            .addSubscriptions(
                topicMap.entrySet().stream().map(topicEntry -> Mqtt3Subscription.builder()
                    .topicFilter(topicEntry.getKey())
                    .qos(Objects.requireNonNull(MqttQos.fromCode(topicEntry.getValue())))
                    .build())
            ).build();

        mqttAsyncClient
            .subscribe(mqttSubscribe, mqtt3Publish -> {
                LOG.trace("Received message: {}", new String(mqtt3Publish.getPayloadAsBytes()));

                // TODO: populate MqttMessage properly
                final MqttMessage mqttMessage = new MqttMessage(mqtt3Publish.getPayloadAsBytes());
                mqttMessage.setQos(mqtt3Publish.getQos().getCode());

                final MqttV3BindingContext context = new MqttV3BindingContext(mqttAsyncClient, mqttMessage);
                context.setTopic(mqtt3Publish.getTopic().toString());
                context.setMqtt3Publish(mqtt3Publish);

                configurationProperties.getManualAcks().ifPresent(context::setManualAcks);
                callback.accept(context);
            }, configurationProperties.getManualAcks().orElse(false))
            .whenComplete((mqtt3SubAck, throwable) -> {
                if (throwable != null) {
                    throw new MqttSubscriberException(String.format("Failed to subscribe to the topics: %s", throwable.getMessage()), throwable);
                }
            });
    }

    @Override
    public void unsubscribe(Set<String> topics) {
        final Mqtt3Unsubscribe mqtt3Unsubscribe = Mqtt3Unsubscribe.builder()
            .addTopicFilters(
                topics.stream().map(MqttTopicFilter::of)
            ).build();

        mqttAsyncClient.unsubscribe(mqtt3Unsubscribe);
    }
}
