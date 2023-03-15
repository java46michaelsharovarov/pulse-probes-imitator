package telran.monitoring;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import telran.monitoring.model.PulseProbe;
import telran.monitoring.service.PulseProbesImitatorImpl;

@SpringBootApplication
public class PulseProbesImitatorApplication {

	@Autowired
	PulseProbesImitatorImpl imitator;
	
	public static void main(String[] args) {
		SpringApplication.run(PulseProbesImitatorApplication.class, args);
	}
	
	@Bean
	Supplier<PulseProbe> pulseProbSupplier() {
		return imitator::nextProbe;		
	}

}
