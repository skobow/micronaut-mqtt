plugins {
    id("io.micronaut.build.internal.mqtt-module")
}

dependencies {
    api(projects.micronautMqttSsl)
    api(libs.managed.hivemq.client)
}

micronautBuild {
    binaryCompatibility {
        enabled.set(false)
    }
}
