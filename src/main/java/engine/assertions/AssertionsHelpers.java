package engine.assertions;

import engine.reporters.Loggers;
import org.testng.Assert;

public class AssertionsHelpers {
    public static void assertTrueWithRetry(boolean flagMethod, String assertionMessage, String log ){
        boolean flag=false;
        int limit=0;
        int count=5;
        while(!flag) {
            if(flagMethod){
                flag=true;
                Assert.assertTrue(
                        flagMethod,assertionMessage);
                Loggers.logInfo(log);
            }
            else {
                limit++;
            }
            if (count==limit){
                Assert.assertTrue(
                        false,"Tried asserting 5 times. no results.  " +assertionMessage);
                break;
            }
        }
    }
}
