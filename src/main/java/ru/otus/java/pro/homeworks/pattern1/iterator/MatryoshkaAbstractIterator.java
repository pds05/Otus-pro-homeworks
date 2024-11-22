package ru.otus.java.pro.homeworks.pattern1.iterator;

import ru.otus.java.pro.homeworks.pattern1.Assemble;
import ru.otus.java.pro.homeworks.pattern1.Matryoshka;

import java.util.Iterator;

public abstract class MatryoshkaAbstractIterator<T extends Assemble<T>> implements Iterator<T> {
    protected final T[] arr;
    protected int positionWidth;
    protected int positionDepth;

    public MatryoshkaAbstractIterator(T... items) {
        this.arr = items;
    }
}
