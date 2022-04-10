package dev.isnow.ffa.utils.type;

import java.util.Iterator;

public class SingletonIterator<T> implements Iterator<T>
{
    private final T element;
    private int count;

    public SingletonIterator(final T element) {
        this.element = element;
    }

    @Override
    public boolean hasNext() {
        return this.count == 0;
    }

    @Override
    public T next() {
        ++this.count;
        return this.element;
    }
}