package ru.otus.java.pro.homeworks.pattern1.simple;

import java.util.Iterator;

public abstract class SimpleMatryoshkaAbstractIterator implements Iterator<String> {
    protected final SimpleMatryoshka[] arr;
    protected int positionWidth = 0;
    protected int positionDepth = 0;

    public SimpleMatryoshkaAbstractIterator(SimpleMatryoshka... arr) {
        this.arr = arr;
    }
}
