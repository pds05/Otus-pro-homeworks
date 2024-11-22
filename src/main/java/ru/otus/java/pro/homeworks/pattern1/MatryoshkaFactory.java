package ru.otus.java.pro.homeworks.pattern1;

public class MatryoshkaFactory {

    public static Matryoshka create(Matryoshka.Color color, int size) {
        Matryoshka matryoshka = new Matryoshka(color, size);
        while (size > 0) {
            matryoshka.put(create(color, --size));
            return matryoshka;
        }
        return matryoshka;
    }
}
