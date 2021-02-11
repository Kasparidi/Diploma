package ru.netology.data;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Random;


public class DataGenerator {
    public static Faker faker = new Faker(new Locale("en"));

    public static String approveNumber() {
        return "4444444444444441";
    }

    public static String declineNumber() {
        return "4444444444444442";
    }

    public static String invalidNumber() {
        return "444444444444";
    }

    public static String currentMonth(){
        LocalDate currentDate = LocalDate.now();
        int month = currentDate.getMonthValue();
        return String.format("%02d", month);
    }

    public static String pastMonth() {
        LocalDate currentDate = LocalDate.now();
        int month = currentDate.getMonthValue() - 1;
        return String.format("%02d", month);
    }

    public static String anyMonth() {
        Random random = new Random();
        int month = random.nextInt(12) + 1;
        return String.format("%02d", month);
    }

    public static String currentYear() {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear()-2000;
        return String.format("%02d", year);
    }

    public static String yearPlusFive() {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear()-2000+5;
        return String.format("%02d", year);
    }

    public static String pastYear() {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear() - 1;
        return String.format("%02d", year);
    }

    public static String validYear() {
        Random random = new Random();
        int y = random.nextInt(5) + 1;
        LocalDate futureYear = LocalDate.now().plusYears(y);
        int year = futureYear.getYear() - 2000;
        return String.format("%02d", year);
    }

    public static String invalidYear() {
        Random random = new Random();
        int y = random.nextInt(25) + 6;
        LocalDate futureYear = LocalDate.now().plusYears(y);
        int year = futureYear.getYear() - 2000;
        return String.format("%02d", year);
    }

    public static String codeCVC() {
        FakeValuesService fake = new FakeValuesService(
                new Locale("en"), new RandomService());
        return fake.regexify("[1-999]{3}");
    }

    public static String name() {
        return faker.name().firstName().toUpperCase() + " " + faker.name().lastName().toUpperCase();
    }

    public static String longName() {
        FakeValuesService fake = new FakeValuesService(
                new Locale("en"), new RandomService());
        return fake.regexify("[a-z]{65}");
    }

}
