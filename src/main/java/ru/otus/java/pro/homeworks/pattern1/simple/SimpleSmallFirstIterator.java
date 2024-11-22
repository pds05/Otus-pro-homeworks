package ru.otus.java.pro.homeworks.pattern1.simple;

public class SimpleSmallFirstIterator extends SimpleMatryoshkaAbstractIterator {

    public SimpleSmallFirstIterator(SimpleMatryoshka... items) {
        super(items);
    }

    @Override
    public boolean hasNext() {
        if (positionWidth >= arr.length || arr[positionWidth] == null) {
            return false;
        }
        if (arr[positionWidth].getItems().size() > positionDepth) {
            return true;
        }
        positionWidth++;
        return hasNext();
    }

    @Override
    public String next() {
        String result = arr[positionWidth].getItems().get(positionDepth);
        if (positionWidth < arr.length - 1) {
            positionWidth++;
        } else {
            positionWidth = 0;
            positionDepth++;
        }
        return result;
    }
}
