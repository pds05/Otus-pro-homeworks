package ru.otus.java.pro.homeworks.pattern1;

import ru.otus.java.pro.homeworks.pattern1.iterator.ColorFirstIterator;
import ru.otus.java.pro.homeworks.pattern1.iterator.SmallFirstIterator;

import java.util.Arrays;
import java.util.Iterator;

public class Box {
    private final Matryoshka[] array;

    public Box(int size) {
        this.array = new Matryoshka[size];
    }

    public void put(Matryoshka item) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                array[i] = item;
                return;
            }
        }
        throw new IllegalStateException("Коробка переполнена");
    }

    public Iterator<Matryoshka> getColorFirstIterator() {
        return new ColorFirstIterator(array);
    }

    public Iterator<Matryoshka> getSmallFirstIterator() {
        return new SmallFirstIterator(array);
    }

    @Override
    public String toString() {
        return "Box{" +
                "array=" + Arrays.toString(array) +
                '}';
    }
}
