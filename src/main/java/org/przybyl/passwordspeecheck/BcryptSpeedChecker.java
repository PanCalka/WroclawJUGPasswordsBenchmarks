package org.przybyl.passwordspeecheck;

import java.time.*;

import org.mindrot.jbcrypt.*;

/**
 * The purpose of this class is to check how fast bcrypt passwords are encoded and checked at current machine.
 * Before running, please read http://stackoverflow.com/a/513259/1271372
 */
public class BcryptSpeedChecker {

	private static final int TEST_ROUNDS = 50;

	public static void main(String[] args) {

		Duration passwordCheckDuration = Duration.ZERO;


		String plainTextPassword = getPasswordFromArgsOrDefault(args);

		for (int currentRounds = 4; takesLessThanOneSec(passwordCheckDuration); currentRounds++) {

			String salt = BCrypt.gensalt(currentRounds);
			String passwordHash = BCrypt.hashpw(plainTextPassword, salt);

			passwordCheckDuration = howLongCheckingPasswordTakesPerRound(plainTextPassword, passwordHash);

			outputResults(passwordCheckDuration, plainTextPassword, currentRounds);
		}
	}

	private static Duration howLongCheckingPasswordTakesPerRound(String plainTextPassword, String hashed) {
		Instant start, stop;

		//warm up
		for (int i = 0; i < 10; i++) {
			BCrypt.checkpw(plainTextPassword, hashed);
		}

		//actual check
		start = Instant.now();
		for (int i = 0; i < TEST_ROUNDS; i++) {
			BCrypt.checkpw(plainTextPassword, hashed);
		}
		stop = Instant.now();

		return Duration.between(start, stop).dividedBy(TEST_ROUNDS);
	}

	private static boolean takesLessThanOneSec(Duration passwordCheckDuration) {
		return passwordCheckDuration.compareTo(Duration.ofSeconds(1)) < 0;
	}

	private static String getPasswordFromArgsOrDefault(String[] args) {
		String password = "P@ssw0rd!";
		if (args.length > 0) {
			password = args[0];
		}
		return password;
	}

	private static void outputResults(Duration passwordCheckDuration, String plainTextPassword, int currentRounds) {
		System.out.println(String.format("Checking a [%s] password takes ~ %4d ms with %2d rounds.",
			plainTextPassword, passwordCheckDuration.toMillis(), currentRounds));
	}

}
