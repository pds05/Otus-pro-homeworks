package ru.otus.java.pro.homeworks.pattern1.simple;

import java.util.Iterator;

public final class SimpleBox {
    private final SimpleMatryoshka red;
    private final SimpleMatryoshka green;
    private final SimpleMatryoshka blue;
    private final SimpleMatryoshka magenta;

    public SimpleBox() {
        this.red = new SimpleMatryoshka("red");
        this.green = new SimpleMatryoshka("green");
        this.blue = new SimpleMatryoshka("blue");
        this.magenta = new SimpleMatryoshka("magenta");
    }

    public Iterator<String> getSmallFirstIterator() {
        return new SimpleSmallFirstIterator(red, green, blue, magenta);
    }

    public Iterator<String> getColorFirstIterator() {
        return new SimpleColorFirstIterator(red, green, blue, magenta);
    }
}
