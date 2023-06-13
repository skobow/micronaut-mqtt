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
package io.micronaut.mqtt.v5.intercept;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscribe;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscription;
import io.micronaut.context.BeanContext;
import io.micronaut.mqtt.bind.*;
import io.micronaut.mqtt.exception.MqttSubscriberException;
import io.micronaut.mqtt.exception.MqttSubscriberExceptionHandler;
import io.micronaut.mqtt.intercept.AbstractMqttSubscriberAdvice;
import io.micronaut.mqtt.v5.bind.MqttV5BindingContext;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * The MQTT v5 implementation of {@link AbstractMqttSubscriberAdvice}.
 *
 * @author James Kleeh
 * @since 1.0.0
 */
@Singleton
public class MqttSubscriberAdvice extends AbstractMqttSubscriberAdvice<MqttMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(MqttSubscriberAdvice.class);
    private final Mqtt5AsyncClient mqttAsyncClient;

    public MqttSubscriberAdvice(BeanContext beanContext,
                                MqttBinderRegistry binderRegistry,
                                MqttSubscriberExceptionHandler exceptionHandler,
                                Mqtt5AsyncClient mqttAsyncClient) {
        super(beanContext, binderRegistry, exceptionHandler);
        this.mqttAsyncClient = mqttAsyncClient;
    }

    @Override
    public void subscribe(String[] topics, int[] qos, Consumer<MqttBindingContext<MqttMessage>> callback) {
//        try {
//            //workaround for https://github.com/eclipse/paho.mqtt.java/issues/826
//            final MqttProperties props = new MqttProperties();
//            props.setSubscriptionIdentifiers(Arrays.asList(new Integer[] { 0 }));
//
//            MqttSubscription[] subscriptions = new MqttSubscription[topics.length];
//            for (int i = 0; i < topics.length; i++) {
//                subscriptions[i] = new MqttSubscription(topics[i], qos[i]);
//            }
//            mqttAsyncClient.subscribe(subscriptions, null, null, (actualTopic, message) -> {
//                MqttV5BindingContext context = new MqttV5BindingContext(mqttAsyncClient, message);
//                context.setTopic(actualTopic);
//                callback.accept(context);
//            }, props);
//        } catch (MqttException e) {
//            throw new MqttSubscriberException(String.format("Failed to subscribe to the topics: %s", (Object) topics), e);
//        }
    }

    @Override
    public void subscribe(final Map<String, Integer> topicMap, final Consumer<MqttBindingContext<MqttMessage>> callback) {

        final Mqtt5Subscribe mqttSubscribe = Mqtt5Subscribe.builder()
            .addSubscriptions(
                topicMap.entrySet().stream().map(topicEntry -> Mqtt5Subscription.builder()
                    .topicFilter(topicEntry.getKey())
                    .qos(MqttQos.fromCode(topicEntry.getValue()))
                    .build())
            ).build();

        mqttAsyncClient
            .subscribe(mqttSubscribe, mqtt5Publish -> {
                LOG.trace("Reveived message: {}", new String(mqtt5Publish.getPayloadAsBytes()));

                // TODO: populate MqttMessage properly
                final MqttMessage mqttMessage = new MqttMessage(mqtt5Publish.getPayloadAsBytes());
                mqttMessage.setQos(mqtt5Publish.getQos().getCode());

                final MqttProperties properties = new MqttProperties();
                mqtt5Publish.getUserProperties().asList().forEach((prop) -> {
                    properties.getUserProperties().add(new UserProperty(prop.getName().toString(), prop.getValue().toString()));
                });
                mqttMessage.setProperties(properties);

                final MqttV5BindingContext context = new MqttV5BindingContext(mqttAsyncClient, mqttMessage);
                context.setTopic(mqtt5Publish.getTopic().toString());

                callback.accept(context);
            })
            .whenComplete((mqtt5SubAck, throwable) -> {
                if (throwable != null) {
                    throw new MqttSubscriberException(String.format("Failed to subscribe to the topics: %s", throwable.getMessage()), throwable);
                }
            });
    }

    @Override
    public void unsubscribe(Set<String> topics) {
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
