package ru.otus.java.pro.homeworks.pattern1;

import ru.otus.java.pro.homeworks.pattern1.simple.SimpleBox;

import java.util.Iterator;

public class MatryoshkaApp {
    public static void main(String[] args) {
        Box box = new Box(4);
        box.put(MatryoshkaFactory.create(Matryoshka.Color.RED, 9));
        box.put(MatryoshkaFactory.create(Matryoshka.Color.BLUE, 9));
        box.put(MatryoshkaFactory.create(Matryoshka.Color.GREEN, 9));
        box.put(MatryoshkaFactory.create(Matryoshka.Color.MAGENTA, 9));

        System.out.println("\r\nColor first result:");
        Iterator<Matryoshka> iterator2 = box.getColorFirstIterator();
        while (iterator2.hasNext()) {
            System.out.println(iterator2.next());
        }

        System.out.println("Small first result:");
        Iterator<Matryoshka> iterator = box.getSmallFirstIterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        System.out.println("Simple Matryoshka color first result:");
        SimpleBox simpleBox = new SimpleBox();
        Iterator<String> iterator3 = simpleBox.getColorFirstIterator();
        print(iterator3);

        System.out.println("Simple Matryoshka small first result:");
        Iterator<String> iterator4 = simpleBox.getSmallFirstIterator();
        print(iterator4);

    }

    private static void print(Iterator<String> iterator) {
        StringBuilder builder = new StringBuilder();
        while (iterator.hasNext()) {
            builder.append(iterator.next() + ", ");
        }
        builder.setLength(builder.length() - 2);
        System.out.println(builder.toString());
    }
}
