package engine.listeners;

import engine.reporters.Loggers;
import engine.utils.ReadExecutionFlow;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

public class TransformListener implements IAnnotationTransformer {
    Set<String> runningTests = new ReadExecutionFlow().readExecutionControl();

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (testMethod == null) return;
        String className = testClass != null ? testClass.getSimpleName() : testMethod.getDeclaringClass().getSimpleName();
        String testSignature = (className + "." + testMethod.getName()).trim().toLowerCase();
        if (!runningTests.contains(testSignature.toLowerCase())) {
            annotation.setEnabled(false);
            Loggers.log.info("⛔ Skipping: " + testSignature);
        } else {
            annotation.setRetryAnalyzer(RetryListener.class);
            Loggers.log.info("✅ Executing: " + testSignature);
        }
    }
}
