package com.github.kimutansk.awsiot

import org.eclipse.paho.client.mqttv3.{MqttMessage, MqttConnectOptions, MqttClient}

/** MQTT Publish Test Class */
object MqttPublisher {
  def main(args: Array[String]) {
    // Connect Target
    val brokerURI:String = "ssl://******.iot.ap-northeast-1.amazonaws.com:8883"

    // SocketFactoryGenerate
    val socketFactory = SocketFactoryGenerator.generateFromFilePath("/etc/cert/rootCA.pem", "/etc/cert/cert.pem", "/etc/cert/private.pem", "password")

    // MQTT Client generate
    val client:MqttClient = new MqttClient(brokerURI, "mqtt-publisher")
    client.setCallback(new PublishMqttCallback)
    val options:MqttConnectOptions = new MqttConnectOptions()
    options.setSocketFactory(socketFactory)
    client.connect(options)


    val message:MqttMessage = new MqttMessage("Test Message".getBytes("UTF-8"))
    client.publish("test-topic", message)
  }
}
