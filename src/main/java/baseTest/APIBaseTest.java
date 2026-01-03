package baseTest;

import engine.listeners.TestNgListener;
import engine.listeners.TransformListener;
import org.testng.annotations.Listeners;

@Listeners({TestNgListener.class, TransformListener.class})
public class APIBaseTest {
}
