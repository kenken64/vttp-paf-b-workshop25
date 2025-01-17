package sg.edu.nus.iss.paf.workshop25;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class SalesSubscribe implements MessageListener{
    @Override
    public void onMessage(org.springframework.data.redis.connection.Message message, byte[] pattern) {
        System.out.println("Message received: " + message.toString());
        String txt = new String(message.getBody());
        System.out.println("Message received: " + txt);
    }
}
