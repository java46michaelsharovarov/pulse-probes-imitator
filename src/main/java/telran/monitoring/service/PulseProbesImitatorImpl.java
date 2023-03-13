package telran.monitoring.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import telran.monititoring.model.PulseProbe;

@Service
public class PulseProbesImitatorImpl implements PulseProbesImitator {

	private static final int MIN_PROBABILITY = 0;
	private static final int MAX_PROBABILITY = 100;
	
	Map<Long, Integer> patientPulses = new HashMap<>();
	
	private static long seqNumber = 1;
	
	@Value("${app.patients.amount:10}")
	private int numberOfPatients;
	
	@Value("${app.patients.id.min:100000000}")
	private long minId;
	
	@Value("${app.pulse.value.min:70}")
	private int minPulse;
	
	@Value("${app.pulse.value.max:150}")
	private int maxPulse;
	
	@Value("${app.jump.probability:10}")
	private int probabilityOfJump;
	
	@Value("${app.increas.probability:70}")
	private int probabilityOfIncrease;
	
	@Value("${app.jump.multiplier:1.4}")
	private double multiplierForJump;
	
	@Value("${app.no-jump.multiplier:1.05}")
	private double multiplierForNoJump;
	
	
	@Override
	public PulseProbe nextProbe() {
		long maxId = minId + numberOfPatients;
		long id = generateRandomId(minId, maxId);
		updatePatientPulses(id);
		return new PulseProbe(id, seqNumber++, patientPulses.get(id));
	}

	private void updatePatientPulses(long id) {
		patientPulses.compute(id, (k, v) -> v == null? generateRandomPulse(minPulse, maxPulse) : updatePulseValue(v));
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

	private boolean getRandomBoolean(int probability) {
		return getRandom().nextInt(MIN_PROBABILITY, MAX_PROBABILITY + 1) <= probability;
	}
	
	private ThreadLocalRandom getRandom() {
		return ThreadLocalRandom.current();
	}

}
