package engine.utils;

public class Faker {


    public final String userName= com.github.javafaker.Faker.instance().name().firstName();
    public final String pass= com.github.javafaker.Faker.instance().address().streetAddress();
}
