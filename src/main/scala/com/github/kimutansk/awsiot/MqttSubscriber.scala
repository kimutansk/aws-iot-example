package com.github.kimutansk.awsiot

import org.eclipse.paho.client.mqttv3.{MqttClient, MqttConnectOptions}

/** MQTT Subscribe Test Class */
object MqttSubscriber {
  def main(args: Array[String]) {
    // Connect Target
    val brokerURI:String = "ssl://******.iot.ap-northeast-1.amazonaws.com:8883"

    // SocketFactoryGenerate
    val socketFactory = SocketFactoryGenerator.generateFromFilePath("/etc/cert/rootCA.pem", "/etc/cert/cert.pem", "/etc/cert/private.pem")

    // MQTT Client generate
    val client:MqttClient = new MqttClient(brokerURI, "mqtt-subscriber")
    client.setCallback(new SubscribeMqttCallback)
    val options:MqttConnectOptions = new MqttConnectOptions()
    options.setSocketFactory(socketFactory)
    client.connect(options)

    client.subscribe("test-topic")
    Thread.sleep(60000)
  }
}
