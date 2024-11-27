package ru.otus.java.pro.homeworks.pattern1.iterator;

import ru.otus.java.pro.homeworks.pattern1.Assemble;

public class ColorFirstIterator<T extends Assemble<T>> extends MatryoshkaAbstractIterator<T> {
    @SafeVarargs
    public ColorFirstIterator(T... items) {
        super(items);
    }

    @Override
    public boolean hasNext() {
        return positionWidth < arr.length && arr[positionWidth] != null;
    }

    @Override
    public T next() {
        T result = arr[positionWidth].get(positionDepth++);
        if (positionDepth >= arr[positionWidth].amount()) {
            positionDepth = 0;
            positionWidth++;
        }
        return result;
    }
}
