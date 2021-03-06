/*
 * Licensed to the Sakai Foundation (SF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The SF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.sakaiproject.nakamura.activemq;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;


/**
 *
 */
public class ActivateTest  {

    @Mock private BundleContext bundleContext;
    @Mock private ComponentContext componentContext;

     public ActivateTest() {
       MockitoAnnotations.initMocks(this);
     }
     
    @Test
    public void testActivate() throws Exception {
      Random r = new Random();
      int i = r.nextInt(100) + 61700;
      System.setProperty("activemq.broker.port", String.valueOf(i));
      Activator activator = new Activator();
      activator.start(bundleContext);
      ActiveMQConnectionFactoryService service = new ActiveMQConnectionFactoryService();
      Dictionary<String, Object> properties = new Hashtable<String, Object>();
      properties.put(ActiveMQConnectionFactoryService.BROKER_URL, "vm://localhost:"+i);
      Mockito.when(componentContext.getProperties()).thenReturn(properties);
      service.activate(componentContext);
      ConnectionFactory cf = service.getDefaultConnectionFactory();
      Connection c = cf.createConnection();
      Session clientSession = c.createSession(false, 1);
      String topic = "testTopic";
      Topic emailTopic = clientSession.createTopic(topic);
      MessageProducer client = clientSession.createProducer(emailTopic);
      Message msg = clientSession.createMessage();
      // may need to set a delivery mode eg persistent for certain types of messages.
      // this should be specified in the OSGi event.
      msg.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
      msg.setJMSType(topic);
      msg.setStringProperty("testing", "testingvalue");
      client.send(msg);
      clientSession.close();
      c.close();
      service.deactivate(componentContext);
      activator.stop(bundleContext);
     
    }
}
