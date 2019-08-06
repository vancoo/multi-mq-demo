package me.van;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    MQProducer producer;
    @Autowired
    MQProducer producer_sandbox1;
    @Autowired
    MQProducer producer_sandbox2;

    @GetMapping("/")
    public String hello(){
        return "hello world";
    }

    @GetMapping("/send")
    public String send(String msg){
        if(null == msg) return "msg is null";

        String returnMsg = "";
        Message message = new Message("topic-test-multi-mq-client", msg.getBytes());
        try {
            producer.send(message);
            returnMsg += "原生producer发送完成<br/>";

            producer_sandbox1.send(message);
            returnMsg += "第一个沙箱内producer发送完成<br/>";

            producer_sandbox2.send(message);
            returnMsg += "第二个沙箱内producer发送完成<br/>";
        } catch (MQClientException | InterruptedException | RemotingException | MQBrokerException e) {
            returnMsg += "发送过程出现异常：" + e.getMessage();
        }
        return returnMsg;
    }
}
