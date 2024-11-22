package ru.otus.java.pro.homeworks.pattern1.simple;

import java.util.ArrayList;
import java.util.List;

public final class SimpleMatryoshka {
    private final List<String> items;

    public List<String> getItems() {
        return items;
    }

    public SimpleMatryoshka(String name) {
        this.items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add(name + i);
        }
    }
}
