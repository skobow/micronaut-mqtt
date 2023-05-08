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
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3RxClient;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import io.micronaut.aop.InterceptorBean;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.mqtt.annotation.v3.MqttPublisher;
import io.micronaut.mqtt.bind.MqttBinderRegistry;
import io.micronaut.mqtt.bind.MqttBindingContext;
import io.micronaut.mqtt.bind.MqttMessage;
import io.micronaut.mqtt.exception.MqttClientException;
import io.micronaut.mqtt.intercept.AbstractMqttIntroductionAdvice;
import io.micronaut.mqtt.v3.bind.MqttV3BindingContext;
import jakarta.inject.Singleton;

import java.lang.annotation.Annotation;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The MQTT v3 implementation of {@link AbstractMqttIntroductionAdvice}.
 *
 * @author James Kleeh
 * @author Sven Kobow
 * @since 1.0.0
 */
@Singleton
@InterceptorBean(MqttPublisher.class)
public class MqttIntroductionAdvice extends AbstractMqttIntroductionAdvice<BiConsumer<Mqtt3Publish, Throwable>, MqttMessage> {

    private final Mqtt3AsyncClient mqtt3AsyncClient;

    public MqttIntroductionAdvice(Mqtt3AsyncClient mqtt3AsyncClient,
                                  MqttBinderRegistry binderRegistry) {
        super(binderRegistry);
        this.mqtt3AsyncClient = mqtt3AsyncClient;
    }

    @Override
    public MqttBindingContext<MqttMessage> createBindingContext(MethodInvocationContext<Object, Object> context) {
        return new MqttV3BindingContext(mqtt3AsyncClient, new MqttMessage());
    }

    @Override
    public Object publish(String topic, MqttMessage message, BiConsumer<Mqtt3Publish, Throwable> listener) {

        final CompletableFuture<Mqtt3Publish> publishFuture = mqtt3AsyncClient.publishWith()
            .topic(topic)
            .payload(message.getPayload())
            .qos(MqttQos.fromCode(message.getQos()))
            .retain(message.getRetained())
            .send();
        publishFuture.whenComplete(listener);

        return null;

//        try {
//            return mqttRxClient.publish(topic, message, null, listener);
//        } catch (MqttException e) {
//            throw new MqttClientException("Failed to publish the message", e);
//        }
    }

    @Override
    public BiConsumer<Mqtt3Publish, Throwable> createListener(Runnable onSuccess, Consumer<Throwable> onError) {
        return (mqtt3Publish, throwable) -> {
            if (throwable != null) {
                onError.accept(throwable);
            } else {
                onSuccess.run();
            }
        };
//        return new BiConsumer<Mqtt3Publish, Throwable>() {
//            @Override
//            public void onSuccess(IMqttToken asyncActionToken) {
//                onSuccess.run();
//            }
//
//            @Override
//            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                onError.accept(exception);
//            }
//        };
    }

    @Override
    public Class<? extends Annotation> getRequiredAnnotation() {
        return MqttPublisher.class;
    }

}
