package com.github.kimutansk.awsiot

import org.eclipse.paho.client.mqttv3.{IMqttDeliveryToken, MqttMessage, MqttCallback}

/** Publish MqttCallBack */
class PublishMqttCallback extends MqttCallback{
  // Nop
  override def deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken): Unit = ???

  override def messageArrived(s: String, mqttMessage: MqttMessage): Unit = ???

  override def connectionLost(throwable: Throwable): Unit = ???
}
