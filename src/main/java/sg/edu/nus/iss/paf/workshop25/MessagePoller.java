package sg.edu.nus.iss.paf.workshop25;

import java.time.Duration;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MessagePoller {
    @Autowired @Qualifier("myredis")
    public RedisTemplate<String, String> redisTemplate;

    @Async
    public void start(){
        Runnable run = () -> {
            while(true){
                String message = redisTemplate.opsForList().rightPop("sales", Duration.ofSeconds(10));
                if(message != null){
                    System.out.println("Message received: " + message);
                }
            }
        };

        ExecutorService executor = 
                java.util.concurrent.Executors.newFixedThreadPool(1);
        executor.submit(run);
    }
}   
