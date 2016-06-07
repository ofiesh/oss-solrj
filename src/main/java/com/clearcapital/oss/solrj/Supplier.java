package com.clearcapital.oss.solrj;

/**
 * A functional interface for the {@link RoundRobinSupplier}.
 */
public interface Supplier<T> {
    T get(String core);
}
