package ru.otus.java.pro.homeworks.reflection;

import ru.otus.java.pro.homeworks.reflection.api.AfterSuite;
import ru.otus.java.pro.homeworks.reflection.api.BeforeSuite;
import ru.otus.java.pro.homeworks.reflection.api.Test;

public class TestCar {
    private Car car;

    @BeforeSuite
    public void prepareCar() {
        car = new Car("lada", "red", 10);
        car.setFuel(30);
        car.setDriver(Driver.BEGINNER);
        car.engine();
    }

    @Test(priority = 10)
    public void drive() {
        car.ride(300);
    }

    @Test(priority = 5)
    public void bend() {
        car.turn(110);
    }

    @AfterSuite
    public void parkCar() {
        car.setDriver(null);
        car.engine();
    }
}
