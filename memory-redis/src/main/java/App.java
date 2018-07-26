import com.software.redis.PanicBuyingManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author wanmbv
 * @Date 2018/7/25 14:48
 * @Description
 * @Version 1.0
 */
public class App {
    public static void main(String[] args){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:ApplicationContext.xml");
        PanicBuyingManager manager = (PanicBuyingManager) context.getBean("manager");
        manager.panicBuying();
    }
}