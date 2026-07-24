package engine.assertions;
import org.testng.asserts.SoftAssert;

public class SoftAssertManager {

    private SoftAssertManager() {}

    private static final ThreadLocal<SoftAssert> SOFT_ASSERT =
            ThreadLocal.withInitial(SoftAssert::new);

    public static void init() {
        SOFT_ASSERT.set(new SoftAssert());
    }

    public static SoftAssert get() {
        SoftAssert softAssert = SOFT_ASSERT.get();
        if (softAssert == null) {
            softAssert = new SoftAssert();
            SOFT_ASSERT.set(softAssert);
        }
        return softAssert;
    }

    public static void assertAllAndClear() {
        try {
            get().assertAll();
        } finally {
            SOFT_ASSERT.remove();
        }
    }

    public static void clear() {
        SOFT_ASSERT.remove();
    }
}