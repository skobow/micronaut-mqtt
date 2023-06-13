package micronaut.mqtt.intercept

import io.micronaut.context.annotation.Requires
import io.micronaut.mqtt.annotation.v5.MqttPublisher
import io.micronaut.mqtt.test.intercept.SimplePubSubClient
import io.micronaut.mqtt.test.intercept.SimplePubSubSpec

class SimpleV5PubSubSpec extends SimplePubSubSpec {

    @Override
    Class<? extends SimplePubSubClient> getClient() {
        return SimpleV5PubSubClient.class
    }

    @Requires(property = "spec.name", value = "SimpleV5PubSubSpec")
    @MqttPublisher
    static interface SimpleV5PubSubClient extends SimplePubSubClient {}
}
