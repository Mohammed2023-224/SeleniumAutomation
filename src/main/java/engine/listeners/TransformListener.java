package engine.listeners;

import engine.reporters.Loggers;
import engine.utils.PropertyReader;
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
        if(PropertyReader.get("file_control", Boolean.class)) {
            if (testMethod == null) return;
            String className = testClass != null ? testClass.getSimpleName() : testMethod.getDeclaringClass().getSimpleName();
            String testSignature = (className + "." + testMethod.getName()).trim().toLowerCase();
            if (!runningTests.contains(testSignature.toLowerCase())) {
                annotation.setEnabled(false);
                Loggers.getLogger().info("⛔ Skipping: " + testSignature);
            } else {
                annotation.setRetryAnalyzer(RetryListener.class);
                Loggers.getLogger().info("✅ Executing: " + testSignature);
            }
        }
    }
}
