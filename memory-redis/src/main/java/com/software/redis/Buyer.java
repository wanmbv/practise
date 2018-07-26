package com.software.redis;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author wanmbv
 * @Date 2018/7/25 13:44
 * @Description
 * @Version 1.0
 */
public class Buyer implements Runnable {

    private StringRedisTemplate redisTemplate;

    private String panicBuyedProduct;

    public Buyer(StringRedisTemplate redisTemplate, String panicBuyedProduct){
        this.redisTemplate = redisTemplate;
        this.panicBuyedProduct = panicBuyedProduct;
    }

    private long getExecTime(){
        String line = "2018-07-25 15:21:00";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            return format.parse(line).getTime();
        }catch(Exception e){
            return new Date().getTime();
        }
    }

    @Override
    public void run() {
        long interval = getExecTime() - new Date().getTime();
        if(interval > 0) {
            try {
                Thread.sleep(interval);
            } catch(Exception e) {
                System.out.println("line设置有误");
            }
        }
        while(true){
            String productV = redisTemplate.opsForValue().get(panicBuyedProduct);
            int left = Integer.parseInt(productV);
            if(left < 0){
                System.out.println(String.format(Thread.currentThread().getName() +"未能抢购成功，商品%s售罄", panicBuyedProduct));
                break;
            }
            long result = redisTemplate.opsForValue().increment(this.panicBuyedProduct, -1l);
            if(result >= 0 ){
                System.out.println(String.format(Thread.currentThread().getName()+"抢购商品%s成功", panicBuyedProduct));
                break;
            }else{
                System.out.println(String.format(Thread.currentThread().getName()+"抢购商品%s失败",panicBuyedProduct));
                break;
            }
        }
    }
}