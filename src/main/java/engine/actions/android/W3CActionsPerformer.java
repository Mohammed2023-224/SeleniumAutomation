package engine.actions.android;

import engine.reporters.Loggers;
import io.appium.java_client.AppiumDriver;

import java.util.Arrays;

public class W3CActionsPerformer {


    public static void perform(
            AppiumDriver driver,
            W3CTouchActions... fingers
    ) {

        driver.perform(
                Arrays.stream(fingers)
                        .map(W3CTouchActions::getSequence)
                        .toList()
        );
        Loggers.logInfo("Start performing all saved actions");
    }
}
