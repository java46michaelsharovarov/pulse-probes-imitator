package telran.monitoring;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import telran.monititoring.model.PulseProbe;

@Component
public class PulseProbesImitatorImpl implements PulseProbesImitator {

	private static final int MIN_PROBABILITY = 0;
	private static final int MAX_PROBABILITY = 100;
	
	Map<Long, Integer> patientPulses = new HashMap<>();
	
	private static AtomicInteger seqNumber = new AtomicInteger(1);
	
	@Value("${app.patients.amount}")
	private int numberOfPatients;
	
	@Value("${app.patients.id.min}")
	private long minId;
	
	@Value("${app.pulse.value.min}")
	private int minPulse;
	
	@Value("${app.pulse.value.max}")
	private int maxPulse;
	
	@Value("${app.jump.probability}")
	private int probabilityOfJump;
	
	@Value("${app.increas.probability}")
	private int probabilityOfIncrease;
	
	@Value("${app.jump.multiplier}")
	private double multiplierForJump;
	
	@Value("${app.no-jump.multiplier}")
	private double multiplierForNoJump;
	
	
	@Override
	public PulseProbe nextProbe() {
		long maxId = minId + numberOfPatients;
		long id = generateRandomId(minId, maxId);
		updatePatientPulses(id);
		return new PulseProbe(id, System.currentTimeMillis(), seqNumber.getAndIncrement(), patientPulses.get(id));
	}

	private void updatePatientPulses(long id) {
		patientPulses.merge(id, generateRandomPulse(minPulse, maxPulse), (i, val) -> updatePulseValue(val));
	}

	private long generateRandomId(long minId, long maxId) {
		return getRandom().nextLong(minId, maxId + 1);
	}

	private int generateRandomPulse(int minPulse, int maxPulse) {
		return getRandom().nextInt(minPulse, maxPulse + 1);
	}
	
	private int updatePulseValue(int pulse) {
		boolean isIncrease = getRandomBoolean(probabilityOfIncrease);
		boolean isJump = getRandomBoolean(probabilityOfJump);
		double multiplier = isJump ? multiplierForJump : multiplierForNoJump;
		return (int) (isIncrease ? pulse * multiplier : pulse / multiplier);
	}

	private ThreadLocalRandom getRandom() {
		return ThreadLocalRandom.current();
	}

	private boolean getRandomBoolean(int probability) {
		return getRandom().nextInt(MIN_PROBABILITY, MAX_PROBABILITY + 1) <= probability;
	}

}
