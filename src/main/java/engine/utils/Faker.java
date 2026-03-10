package engine.utils;

public class Faker {
private Faker(){}
    public static final String USERNAME= com.github.javafaker.Faker.instance().name().firstName();
    public static final String PASS= com.github.javafaker.Faker.instance().address().streetAddress();
}
