package telran.monitoring;

import java.util.stream.IntStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PulseProbesImitatorApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ac = SpringApplication.run(PulseProbesImitatorApplication.class, args);
		PulseProbesImitatorImpl imitator = ac.getBean(PulseProbesImitatorImpl.class);
		IntStream.range(0, 10)
		.forEach(i -> System.out.println(imitator.nextProbe()));
	}

}
