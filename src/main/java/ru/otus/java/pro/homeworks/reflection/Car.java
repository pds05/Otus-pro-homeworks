package ru.otus.java.pro.homeworks.reflection;

public class Car {
    private String brand;
    private String color;
    private int averageExpense;
    private int fuel;
    private Driver driver;
    private boolean isEngine;

    public Car(String brand, String color, int averageExpense) {
        this.brand = brand;
        this.averageExpense = averageExpense;
        this.color = color;
    }

    public void ride(int distance) {
        if (!isEngine) {
            throw new CarException("Press engine first!");
        }
        int remainingFuel = distance / averageExpense;
        if (remainingFuel > fuel) {
            throw new CarException("Fuel low level currentFuel=" + fuel + ", remainingFuel=" + remainingFuel);
        }
        System.out.println("Riding on " + distance + "km " + this);
        fuel = fuel - remainingFuel;
    }

    public void turn(int speed) {
        if (speed > 100 && driver == Driver.BEGINNER) {
            throw new CarException("Very high speed=" + speed + "!");
        }
        System.out.println("Turn passed! speed=" + speed + " ");
    }

    public boolean engine() {
        if (fuel >= 0 && !isEngine) {
            isEngine = true;
            System.out.println("Engine ON " + this);
            return true;
        }
        isEngine = false;
        System.out.println("Engine OFF " + this);
        return false;
    }

    public String getBrand() {
        return brand;
    }

    public int getAverageExpense() {
        return averageExpense;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public int getFuel() {
        return fuel;
    }

    public boolean isEngine() {
        return isEngine;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", color='" + color + '\'' +
                ", averageExpense=" + averageExpense +
                ", fuel=" + fuel +
                ", driver=" + driver +
                ", isEngine=" + isEngine +
                '}';
    }
}
