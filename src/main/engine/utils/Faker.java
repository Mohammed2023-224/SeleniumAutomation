package engine.utils;

public class Faker {

    public static String userName= com.github.javafaker.Faker.instance().name().firstName();
    public static String pass= com.github.javafaker.Faker.instance().address().streetAddress();
}
