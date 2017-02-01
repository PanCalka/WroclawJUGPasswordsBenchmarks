package org.przybyl.passwordspeecheck;

import java.time.*;

import org.mindrot.jbcrypt.*;

/**
 * The purpose of this class is to check how fast bcrypt passwords are encoded and checked at current machine.
 * Before running, please read http://stackoverflow.com/a/513259/1271372
 */
public class PasswordSpeedChecker {

    public static void main(String[] args) {
        String pass = "P@ssword1";
        int hashCost = getArgumentFromPositionOrDefault(args, 1, 10);
        int testRounds = getArgumentFromPositionOrDefault(args, 2, 100);


        for (int i = 0; i < testRounds / 10; i++) {
            BCrypt.hashpw(pass, BCrypt.gensalt(hashCost));
        }

        Instant start = Instant.now();

        for (int i = 0; i < testRounds ; i++) {
            BCrypt.hashpw(pass, BCrypt.gensalt(hashCost));
        }
        Instant stop = Instant.now();

        for (int i = 0; i < testRounds / 10; i++) {
            BCrypt.hashpw(pass, BCrypt.gensalt(hashCost));
        }
        long millis = Duration.between(start, stop).toMillis();

        System.out.println(String.format("Computing password %d times with %d rounds took %d seconds, one took %d ms.", testRounds, hashCost, millis/1000, millis / testRounds));

        String hashed = BCrypt.hashpw(pass, BCrypt.gensalt(hashCost));

        for (int i = 0; i < testRounds / 10; i++) {
            BCrypt.checkpw(pass, hashed);
        }

        Instant start2 = Instant.now();

        for (int i = 0; i < testRounds ; i++) {
            BCrypt.checkpw(pass, hashed);
        }
        Instant stop2 = Instant.now();

        for (int i = 0; i < testRounds / 10; i++) {
            BCrypt.checkpw(pass, hashed);
        }
        long millis2 = Duration.between(start2, stop2).toMillis();

        System.out.println(String.format("Checking password %d times with %d rounds took %d seconds, one took %d ms.", testRounds, hashCost, millis2/1000, millis2 / testRounds));



    }

    static int getArgumentFromPositionOrDefault(String[] args, int position, int defaultValue) {
        if (args.length > position) {
             return Integer.parseInt(args[position]);
        }
        return defaultValue;
    }

}
