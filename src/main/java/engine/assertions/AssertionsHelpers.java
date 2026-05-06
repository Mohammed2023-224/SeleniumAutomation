package engine.assertions;

import engine.reporters.Loggers;
import org.testng.Assert;

import java.util.function.Supplier;

public class AssertionsHelpers {
    public static void assertTrueWithRetry(Supplier<Boolean> fn,
                                           String assertionMessage) {
        boolean flag = false;
        int limit = 1;
        int count = 5;
        while (!flag &&limit<=count) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            flag=  fn.get();
            if (flag) {
                Assert.assertTrue(flag, assertionMessage);
                Loggers.logInfo(assertionMessage);
            } else {
                limit++;
            }
            if (count == limit) {
                Assert.assertTrue(
                        false,
                        "Tried asserting 5 times. no results. " + assertionMessage
                );
            }
        }
    }
}
