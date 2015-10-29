package com.github.kimutansk.awsiot

import org.eclipse.paho.client.mqttv3.{IMqttDeliveryToken, MqttCallback, MqttMessage}

/** Subscribe MqttCallBack */
class SubscribeMqttCallback extends MqttCallback{
  // Nop
  override def deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken): Unit = ???

  override def messageArrived(s: String, mqttMessage: MqttMessage): Unit = {
    System.out.println("Message received. : Topic=" + s + ", Payload=" + mqttMessage.toString)
  }

  override def connectionLost(throwable: Throwable): Unit = ???
}
