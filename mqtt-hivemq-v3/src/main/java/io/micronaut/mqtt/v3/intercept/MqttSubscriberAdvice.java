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

import com.hivemq.client.internal.mqtt.message.MqttMessage;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.datatypes.MqttTopicFilter;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3RxClient;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3Subscribe;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3SubscribeBuilder;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3Subscription;
import com.hivemq.client.mqtt.mqtt3.message.unsubscribe.Mqtt3Unsubscribe;
import io.micronaut.context.BeanContext;
import io.micronaut.mqtt.bind.MqttBinderRegistry;
import io.micronaut.mqtt.bind.MqttBindingContext;
import io.micronaut.mqtt.exception.MqttSubscriberException;
import io.micronaut.mqtt.exception.MqttSubscriberExceptionHandler;
import io.micronaut.mqtt.intercept.AbstractMqttSubscriberAdvice;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
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

    public MqttSubscriberAdvice(BeanContext beanContext,
                                MqttBinderRegistry binderRegistry,
                                MqttSubscriberExceptionHandler exceptionHandler,
                                Mqtt3AsyncClient mqttAsyncClient) {
        super(beanContext, binderRegistry, exceptionHandler);
        this.mqttAsyncClient = mqttAsyncClient;
    }

    @Override
    public void subscribe(String[] topics, int[] qos, Consumer<MqttBindingContext<MqttMessage>> callback) {
        // Deprecated
//        try {
//            IMqttMessageListener messageListener = (actualTopic, message) -> {
//                MqttV3BindingContext context = new MqttV3BindingContext(mqttAsyncClient, message);
//                context.setTopic(actualTopic);
//                callback.accept(context);
//            };
//            IMqttMessageListener[] listeners = new IMqttMessageListener[topics.length];
//            Arrays.fill(listeners, messageListener);
//            mqttAsyncClient.subscribe(topics, qos, listeners);
//        } catch (MqttException e) {
//            throw new MqttSubscriberException(String.format("Failed to subscribe to the topics: %s", (Object) topics), e);
//        }
    }

    @Override
    public void subscribe(final Map<String, Integer> topicMap, final Consumer<MqttBindingContext<MqttMessage>> callback) {

        final Mqtt3Subscribe mqttSubscribe = Mqtt3Subscribe.builder()
            .addSubscriptions(
                topicMap.entrySet().stream().map(topicEntry -> {
                    return Mqtt3Subscription.builder()
                        .topicFilter(topicEntry.getKey())
                        .qos(MqttQos.fromCode(topicEntry.getValue()))
                        .build();
                })
            ).build();

        mqttAsyncClient
            .subscribe(mqttSubscribe)
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
//        try {
//            IMqttToken token = mqttAsyncClient.unsubscribe(topics.toArray(new String[]{}));
//            token.waitForCompletion();
//        } catch (MqttException e) {
//            if (LOG.isWarnEnabled()) {
//                LOG.warn("Failed to unsubscribe from the subscribed topics", e);
//            }
//        }
    }
}
