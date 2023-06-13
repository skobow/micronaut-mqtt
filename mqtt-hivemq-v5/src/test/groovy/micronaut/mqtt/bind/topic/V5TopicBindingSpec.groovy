package micronaut.mqtt.bind.topic

import io.micronaut.context.annotation.Requires
import io.micronaut.mqtt.annotation.v5.MqttPublisher
import io.micronaut.mqtt.test.bind.topic.TopicBindingClient
import io.micronaut.mqtt.test.bind.topic.TopicBindingSpec

class V5TopicBindingSpec extends TopicBindingSpec {

    @Override
    Class<? extends TopicBindingClient> getClient() {
        return MyClient.class
    }

    @Requires(property = "spec.name", value = "V5TopicBindingSpec")
    @MqttPublisher
    static interface MyClient extends TopicBindingClient {}
}
