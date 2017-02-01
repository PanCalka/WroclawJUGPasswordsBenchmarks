package org.przybyl.passwordspeecheck;

import static org.przybyl.passwordspeecheck.PasswordSpeedChecker.getArgumentFromPositionOrDefault;

import java.lang.reflect.*;
import java.time.*;

import org.apache.commons.codec.digest.*;

/**
 * The purpose of this class is to check how fast SHA512-CRYPT passwords are encoded and checked at current machine.
 * Before running, please read http://stackoverflow.com/a/513259/1271372
 */
public class PasswordShaSpeedChecker {
	public static final int SHA_ROUNDS = 50_000;

	public static void main(String[] args) {
		String pass = "P@ssword1";
		int hashCost = getArgumentFromPositionOrDefault(args, 0, 50_000);
		int testRounds = getArgumentFromPositionOrDefault(args, 1, 100);
		int saltLength = getArgumentFromPositionOrDefault(args, 2, 16);

		Instant start, stop;


		//warm up
		for (int i = 0; i < testRounds / 10; i++) {
			Crypt.crypt(pass, createSha512CryptSalt(hashCost, saltLength));
		}

		start = Instant.now();

		//hash
		for (int i = 0; i < testRounds; i++) {
			Crypt.crypt(pass, createSha512CryptSalt(hashCost, saltLength));
		}
		stop = Instant.now();

		final Duration hashingDuration = Duration.between(start, stop);



		String hashed = Crypt.crypt(pass, createSha512CryptSalt(hashCost, saltLength));

		//warm up
		for (int i = 0; i < testRounds / 10; i++) {
			Crypt.crypt(pass, hashed);
		}

		start = Instant.now();

		//hash
		for (int i = 0; i < testRounds; i++) {
			Crypt.crypt(pass, hashed);
		}
		stop = Instant.now();


		final Duration checkingDuration = Duration.between(start, stop);

		System.out.println(String.format("Computing password %d times with %d rounds took %d seconds, one took %d ms.", testRounds, hashCost, hashingDuration.toMillis()/1000, hashingDuration.dividedBy(testRounds).toMillis()));

		System.out.println(String.format("Checking password %d times with %d rounds took %d seconds, one took %d ms.", testRounds, hashCost, checkingDuration.toMillis()/1000, checkingDuration.dividedBy(testRounds).toMillis()));

	}

	private static String createSha512CryptSalt(int hashRounds, int saltLength) {
		try {
			Class<?> b64 = Class.forName("org.apache.commons.codec.digest.B64");
			Method getRandomSalt = b64.getDeclaredMethod("getRandomSalt", Integer.TYPE);
			getRandomSalt.setAccessible(true);
			Object salt = getRandomSalt.invoke(null, saltLength);
			return "$6$rounds=" + hashRounds + "$" + salt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
