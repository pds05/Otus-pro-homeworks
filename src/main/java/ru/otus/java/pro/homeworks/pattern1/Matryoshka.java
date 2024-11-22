package ru.otus.java.pro.homeworks.pattern1;

public class Matryoshka implements Assemble<Matryoshka> {
    Color color;
    int size;
    Matryoshka inner;

    public Matryoshka(Color color, int size) {
        this.size = size;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    @Override
    public void put(Matryoshka item) {
        int diff = size - item.getSize();
        if (diff < 1) {
            throw new IllegalArgumentException("Матрешка слишком большая");
        }
        if (diff > 1) {
            throw new IllegalArgumentException("Матрешка слишком маленькая");
        } else {
            this.inner = (Matryoshka) item;
        }
    }

    @Override
    public Matryoshka get() {
        return inner;
    }

    @Override
    public Matryoshka get(int innerSize) {
        if (innerSize > getSize()) {
            throw new IndexOutOfBoundsException("Отсутсвуюет вложенная матрешка с указанным индексом");
        }
        Matryoshka result = this;
        while (innerSize < result.getSize()) {
            result = result.get();
        }
        return result;
    }

    @Override
    public int amount() {
        return 1 + (inner != null ? inner.amount() : 0);
    }

    @Override
    public String toString() {
        return "Matryoshka{" +
                "color=" + color +
                ", size=" + size +
                ", inner=" + inner +
                '}';
    }

    public enum Color {
        RED, GREEN, BLUE, MAGENTA
    }
}
