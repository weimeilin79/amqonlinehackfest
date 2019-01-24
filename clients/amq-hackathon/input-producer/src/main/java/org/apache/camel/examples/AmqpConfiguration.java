/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.examples;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "amqp")
public class AmqpConfiguration {
  
  private long timerPeriod = 1000L;
  private int minMessageSize = 1;
  private int maxMessageSize = 1024;
  private String clientId;
  private String producerQueue;
  private String sslTruststorePath;
  private String sslTruststorePassword;
  private String sslCertAlias = "broker";

  public long getTimerPeriod() {
    return timerPeriod;
  }

  public void setTimerPeriod(long timerPeriod) {
    this.timerPeriod = timerPeriod;
  }

  public int getMinMessageSize() {
    return minMessageSize;
  }

  public void setMinMessageSize(int minMessageSize) {
    this.minMessageSize = minMessageSize;
  }

  public int getMaxMessageSize() {
    return maxMessageSize;
  }

  public void setMaxMessageSize(int maxMessageSize) {
    this.maxMessageSize = maxMessageSize;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getProducerQueue() {
    return producerQueue;
  }

  public void setProducerQueue(String producerQueue) {
    this.producerQueue = producerQueue;
  }

  public String getSslTruststorePath() {
    return sslTruststorePath;
  }

  public void setSslTruststorePath(String sslTruststorePath) {
    this.sslTruststorePath = sslTruststorePath;
  }

  public String getSslTruststorePassword() {
    return sslTruststorePassword;
  }

  public void setSslTruststorePassword(String sslTruststorePassword) {
    this.sslTruststorePassword = sslTruststorePassword;
  }

  public String getSslCertAlias() {
    return sslCertAlias;
  }

  public void setSslCertAlias(String sslCertAlias) {
    this.sslCertAlias = sslCertAlias;
  }
  
}
