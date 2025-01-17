package sg.edu.nus.iss.paf.workshop25;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class Appconfig {
    
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Integer redisPort;

    @Value("${spring.redis.username}")
    private String redisUsername;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value("${spring.redis.database}")
    private Integer redisDatabase;


    // Register ---> Subscriber sales class
    @Autowired
    private SalesSubscribe salesSubscribe;


    private JedisConnectionFactory createFactory(){
        RedisStandaloneConfiguration config = 
                            new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort);
        config.setDatabase(redisDatabase);
        if(!"NOT_SET".equals(redisUsername)){
            config.setUsername(redisUsername);
            config.setPassword(redisPassword);
        }

        JedisClientConfiguration jedisClient = 
                        JedisClientConfiguration.builder().build();

        JedisConnectionFactory jedisFac = 
                    new JedisConnectionFactory(config, jedisClient);

        jedisFac.afterPropertiesSet();
        return jedisFac;
    }

    @Bean
    public RedisMessageListenerContainer createListenerContainer(){
        JedisConnectionFactory jedisFac = createFactory();
        RedisMessageListenerContainer listenerContainer = 
                    new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(jedisFac);
        listenerContainer.addMessageListener(salesSubscribe, 
                    ChannelTopic.of("sales"));

        return listenerContainer;
    }

    @Bean("myredis")
    public RedisTemplate<String, String> createRedisConnection(){
        JedisConnectionFactory jedisFac = createFactory();
        RedisTemplate<String, String> redisTemplate = 
                    new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisFac);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        
        return redisTemplate;
    }
}
