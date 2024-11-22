package ru.otus.java.pro.homeworks.pattern1.iterator;

import ru.otus.java.pro.homeworks.pattern1.Assemble;

public class SmallFirstIterator<T extends Assemble<T>> extends MatryoshkaAbstractIterator<T> {

    @SafeVarargs
    public SmallFirstIterator(T... items) {
        super(items);
    }

    @Override
    public boolean hasNext() {
        if (positionWidth >= arr.length || arr[positionWidth] == null) {
            return false;
        }
        if(arr[positionWidth].amount() > positionDepth){
            return true;
        }
        positionWidth++;
        return hasNext();
    }

    @Override
    public T next() {
        T result = arr[positionWidth].get(positionDepth);
        if (positionWidth < arr.length - 1) {
            positionWidth++;
        } else {
            positionWidth = 0;
            positionDepth++;
        }
        return result;
    }
}
