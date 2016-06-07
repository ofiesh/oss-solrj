package com.clearcapital.oss.solrj;

/**
 * Generic predicate to remove {@link Supplier}s from a {@link RoundRobinSupplier}
 * @param <T> The {@code <T>} of the {@link RoundRobinSupplier}
 */
public interface RemovePredicate<T> {

    boolean matches(Supplier<T> t);
}
