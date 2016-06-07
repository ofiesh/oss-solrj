package com.clearcapital.oss.solrj;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;

/**
 * The {@link RoundRobinSupplier} manages a collection of {@link Supplier}s and supplys them in a round robin fashion.
 * {@link Supplier}s can be added and removed. When added and removed, the cycle is restarted from an arbitrary point.
 *
 * @param <T> The type of {@link Supplier<T>}. Probably always a
 * {@link org.apache.solr.client.solrj.impl.HttpSolrServer}, however a generic makes it easier to test and {@code <T>}
 * could be another type of Http Client.
 */
public class RoundRobinSupplier<T> implements Supplier<T> {

    private final Object lock = new Object();
    private Set<Supplier<T>> suppliers;
    private Iterator<Supplier<T>> iterator;

    /**
     *
     * @param suppliers The initial {@link Collection} of {@link Supplier<T>}.
     */
    public RoundRobinSupplier(final Collection<? extends Supplier<T>> suppliers) {
        if (suppliers != null) {
            this.suppliers = new HashSet<>(suppliers);
        } else {
            this.suppliers = new HashSet<>();
        }
        cycle();
    }

    /**
     * Gets the {@code T} supplied by the next {@link Supplier} in the round robin cycle.
     * @param core The core the supplied {@code <T>} should be configured against.
     * @return Returns the next {@code <T>} in the round robin cycle configured against the core.
     */
    @Override
    public T get(final String core) {
        Supplier<T> supplier;
        if (iterator == null) {
            return null;
        }
        synchronized (lock) {
            supplier = iterator.next();
        }
        return supplier == null ? null : supplier.get(core);
    }

    /**
     * Adds a {@link Supplier} to the round robin cycle.
     * @param supplier {@link Supplier} to add to the cycle.
     */
    public void add(final Supplier<T> supplier) {
        synchronized (lock) {
            suppliers.add(supplier);
            cycle();
        }
    }

    /**
     * Removes a {@link Supplier} from the cycle
     * @param predicate The {@link RemovePredicate} that matches against {@code <T>}s that should be removed from the
     * cycle.
     * @return Returns true if a {@link Supplier was removed}, otherwise false.
     */
    public boolean remove(final RemovePredicate<T> predicate) {
        boolean removed = false;
        synchronized (lock) {
            final Iterator<Supplier<T>> iterator = suppliers.iterator();
            while (iterator.hasNext()) {
                final Supplier<T> next = iterator.next();
                if (predicate.matches(next)) {
                    removed = true;
                    iterator.remove();
                }
            }
            if(removed) {
                cycle();
            }
        }
        return removed;
    }

    private void cycle() {
        if (!suppliers.isEmpty()) {
            iterator = Iterators.cycle(ImmutableSet.copyOf(suppliers));
        } else {
            iterator = null;
        }
    }
}
