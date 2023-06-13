package io.micronaut.mqtt.health

import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import io.micronaut.context.ApplicationContext
import io.micronaut.health.HealthStatus
import io.micronaut.management.health.indicator.HealthResult
import io.micronaut.mqtt.test.AbstractMQTTTest
import io.micronaut.mqtt.v3.client.health.MqttHealthIndicator
import reactor.core.publisher.Flux

class V3HealthIndicatorSpec extends AbstractMQTTTest {

    void "mqtt v3 client health indicator"() {
        ApplicationContext ctx = startContext()
        def mqttClient = ctx.getBean(Mqtt3AsyncClient.class)

        when:
        def indicator = ctx.getBean(MqttHealthIndicator.class)
        HealthResult result = Flux.from(indicator.getResult()).blockFirst()

        then:
        result.status == HealthStatus.UP
        result.details['clientId'] != null

        when:
        mqttClient.disconnect().join()
        result = Flux.from(indicator.getResult()).blockFirst()

        then:
        result.status == HealthStatus.DOWN
    }
}
