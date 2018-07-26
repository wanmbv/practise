package com.software.redis;

import com.software.redis.Buyer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author wanmbv
 * @Date 2018/7/25 11:22
 * @Description
 * @Version 1.0
 */
@Component("manager")
public class PanicBuyingManager {
    @Autowired
    @Qualifier("redisTemplate")
    StringRedisTemplate redisTemplate;

    String[] products = {"Hadoop practise", "Mapreduce hook", "Postgresql practise", "Oracle illustrate", "git book"};

    @PostConstruct
    private void init(){
        for(String product: products){
            redisTemplate.opsForValue().set(product, 10 + "");
        }
    }

    public void panicBuying(){
        int buyerNum = 500;
        ExecutorService fixedExecutor = Executors.newFixedThreadPool(buyerNum);
        Random random = new Random();
        for(int i = 0; i < buyerNum; i++){
            int index = random.nextInt(5);
            fixedExecutor.execute(new Buyer(redisTemplate, products[index]));
        }
    }
}