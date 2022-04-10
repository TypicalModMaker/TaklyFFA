package dev.isnow.ffa.utils.type;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

@SuppressWarnings("ALL")
public class Option<T> implements Iterable<T>, Serializable
{
    private static final Option<?> NONE;
    protected T value;

    protected Option(final T value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return (this.value != null) ? this.value.hashCode() : 0;
    }

    @Override
    public boolean equals(final Object to) {
        return to instanceof Option && Objects.equals(this.value, ((Option)to).value);
    }

    @Override
    public Iterator<T> iterator() {
        return this.isDefined() ? new SingletonIterator<T>(this.value) : Collections.emptyIterator();
    }

    public T orElseGet(final T elseValue) {
        return this.isDefined() ? this.value : elseValue;
    }

    public boolean isDefined() {
        return this.value != null;
    }


    public static <T> Option<T> none() {
        return cast(Option.NONE);
    }

    public static <T> T cast(final Object object) {
        return (T)object;
    }

    public static <T> Option<T> of(final T value) {
        return (value != null) ? new Option<T>(value) : none();
    }

    static {
        NONE = new Option<>(null);
    }
}