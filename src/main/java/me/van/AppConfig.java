package me.van;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean(autowire = Autowire.BY_NAME, value = "producer")
    MQProducer producer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer();
        initProducer(producer, "a.io:9876;b.io:9876");
        return producer;
    }


    @Bean(autowire = Autowire.BY_NAME, value = "producer_sandbox1")
    MQProducer producerSandbox1() throws MQClientException, SandboxCannotCreateObjectException {
        DefaultMQProducer producer = createProducerInSandbox();
        initProducer(producer, "x.io:9876;y.io:9876");
        return producer;
    }


    @Bean(autowire = Autowire.BY_NAME, value = "producer_sandbox2")
    MQProducer producerSandbox2() throws MQClientException, SandboxCannotCreateObjectException {
        DefaultMQProducer producer = createProducerInSandbox();
        initProducer(producer, "1.io:9876;2.io:9876");
        return producer;
    }

    private DefaultMQProducer createProducerInSandbox() throws SandboxCannotCreateObjectException {
        Sandbox sandbox = new Sandbox("org.apache.rocketmq.client");
        return sandbox.createObject(DefaultMQProducer.class);
    }

    private void initProducer(DefaultMQProducer producer, String namesrvAddr) throws MQClientException {
        producer.setNamesrvAddr(namesrvAddr);
        producer.setProducerGroup("test-group");
        producer.setRetryAnotherBrokerWhenNotStoreOK(true);
        producer.start();
    }
}