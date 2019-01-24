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

import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.jms.ConnectionFactory;
import javax.net.ssl.SSLContext;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.camel.spi.ComponentCustomizer;
import org.apache.camel.util.jsse.KeyStoreParameters;
import org.apache.camel.util.jsse.SSLContextParameters;
import org.apache.camel.util.jsse.TrustManagersParameters;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CamelConfiguration extends RouteBuilder {

  private static final Logger log = LoggerFactory.getLogger(CamelConfiguration.class);
  
  @Autowired
  AmqpConfiguration amqpProperties;
  
  
  @Bean
  KeyStoreParameters trustStore(AmqpConfiguration amqpProperties, CamelContext camelContext) {
    KeyStoreParameters trustStore = new KeyStoreParameters();
    trustStore.setCamelContext(camelContext);
    trustStore.setResource(amqpProperties.getSslTruststorePath());
    trustStore.setPassword(amqpProperties.getSslTruststorePassword());
    return trustStore;
  }
  
  @Bean
  TrustManagersParameters trustManagers(KeyStoreParameters trustStore) {
    TrustManagersParameters trustManagers = new TrustManagersParameters();
    trustManagers.setKeyStore(trustStore);
    return trustManagers;
  }
  
  @Bean
  SSLContextParameters sslContextParameters(AmqpConfiguration amqpProperties, TrustManagersParameters trustManagers) {
    SSLContextParameters sslContextParameters = new SSLContextParameters();
    sslContextParameters.setTrustManagers(trustManagers);
    sslContextParameters.setCertAlias(amqpProperties.getSslCertAlias());
    return sslContextParameters;
  }
  
  @Bean
  SSLContext sslContext(CamelContext camelContext, SSLContextParameters sslContextParameters) throws GeneralSecurityException, IOException {
    SSLContext sslContext = sslContextParameters.createSSLContext(camelContext);
    return sslContext;
  }
  
  @Bean
  ComponentCustomizer<AMQPComponent> amqpComponentCustomizer(ConnectionFactory jmsConnectionFactory, SSLContext sslContext) {
    return (component) -> {
      component.setConnectionFactory(jmsConnectionFactory);
      if (jmsConnectionFactory instanceof JmsConnectionFactory) {
        ((JmsConnectionFactory) jmsConnectionFactory).setSslContext(sslContext);
      } else if (jmsConnectionFactory instanceof JmsPoolConnectionFactory) {
        ((JmsConnectionFactory) ((JmsPoolConnectionFactory) jmsConnectionFactory).getConnectionFactory()).setSslContext(sslContext);
      }
    };
  }
  
  @Override
  public void configure() throws Exception {
    fromF("amqp:queue:%s", amqpProperties.getConsumerQueue())
      .log(LoggingLevel.DEBUG, log, "ClientID: ${header.ClientID}, Message: ${bodyAs(java.lang.String).length()}")
    ;
    
    from("amqp:topic:mytopic")
      .log(LoggingLevel.DEBUG, log, "Alert: ${body}")
    ;
  }
}
