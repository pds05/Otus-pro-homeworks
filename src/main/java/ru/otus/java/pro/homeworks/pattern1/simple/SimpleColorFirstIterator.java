package ru.otus.java.pro.homeworks.pattern1.simple;

public class SimpleColorFirstIterator extends SimpleMatryoshkaAbstractIterator {

    public SimpleColorFirstIterator(SimpleMatryoshka... items) {
        super(items);
    }

    @Override
    public boolean hasNext() {
        return positionWidth < arr.length && arr[positionWidth] != null;
    }

    @Override
    public String next() {
        String result = arr[positionWidth].getItems().get(positionDepth);
        if (positionDepth < arr[positionWidth].getItems().size() - 1) {
            positionDepth++;
        } else {
            positionDepth = 0;
            positionWidth++;
        }
        return result;
    }
}
