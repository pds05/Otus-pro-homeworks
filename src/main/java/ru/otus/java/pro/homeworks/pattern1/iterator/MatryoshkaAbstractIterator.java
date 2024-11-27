package ru.otus.java.pro.homeworks.pattern1.iterator;

import ru.otus.java.pro.homeworks.pattern1.Assemble;
import ru.otus.java.pro.homeworks.pattern1.Matryoshka;

import java.util.Arrays;
import java.util.Iterator;

public abstract class MatryoshkaAbstractIterator<T extends Assemble<T>> implements Iterator<T> {
    protected final T[] arr;
    protected int positionWidth;
    protected int positionDepth;

    public MatryoshkaAbstractIterator(T... items) {
        this.arr = items;
        bubbleSort(arr);
    }

    private void swap(int dex1, int dex2) {
        T temp = arr[dex1];
        arr[dex1] = arr[dex2];
        arr[dex2] = temp;
    }

    public void bubbleSort(T[] array) {
        for (int i = array.length - 1; i > 1; i--) {
            for (int j = 0; j < i; j++) {
                int next = j + 1;
                if (array[j] != null && array[next] != null && array[j].amount() > array[next].amount()) {
                    swap(j, next);
                }
            }
        }
    }
}
