package com.clearcapital.oss.solrj;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.junit.Test;

public class RoundRobinSupplierTest {

    public class IntegerSupplier implements Supplier<Integer> {

        private final Integer integer;
        public IntegerSupplier(Integer integer) {
            this.integer = integer;
        }

        @Override
        public Integer get(final String core) {
            return integer;
        }
    }

    @Test
    public void testConcurrentModificationSafe() {
        final List<Supplier<Integer>> suppliers = new ArrayList<>();
        suppliers.add(new IntegerSupplier(1));
        final RoundRobinSupplier<Integer> supplier = new RoundRobinSupplier<>(suppliers);
        supplier.get("foo");
        suppliers.add(new IntegerSupplier(2));
        try {
            supplier.get("foo");
        } catch (ConcurrentModificationException e) {
            fail("Adding an object to suppliers caused a concurrent modification exception");
        }
    }

    @Test
    public void testRoundRobin() {
        final List<Supplier<Integer>> suppliers = new ArrayList<>();
        suppliers.add(new IntegerSupplier(1));
        suppliers.add(new IntegerSupplier(2));
        suppliers.add(new IntegerSupplier(3));
        final RoundRobinSupplier<Integer> supplier = new RoundRobinSupplier<>(suppliers);
        Integer first = supplier.get("foo");
        Integer second = supplier.get("foo");
        Integer third = supplier.get("foo");
        assertNotEquals("first and second should not be equal", first, second);
        assertNotEquals("first and third should not be equal", first, third);
        assertNotEquals("second and third should not be equal", second, third);
        assertEquals("first not as expected", first, supplier.get("foo"));
        assertEquals("second not as expected", second, supplier.get("foo"));
        assertEquals("third not as expected", third, supplier.get("foo"));
    }

    @Test
    public void testRoundRobinAdd() {
        final List<Supplier<Integer>> suppliers = new ArrayList<>();
        suppliers.add(new IntegerSupplier(1));
        final RoundRobinSupplier<Integer> objectRoundRobinSupplier = new RoundRobinSupplier<>(suppliers);
        objectRoundRobinSupplier.get("foo");
        objectRoundRobinSupplier.add(new IntegerSupplier(2));
        Integer first = objectRoundRobinSupplier.get("foo");
        Integer second = objectRoundRobinSupplier.get("foo");
        assertNotEquals("first and second should not be equal", first, second);
        assertEquals(first, objectRoundRobinSupplier.get("foo"));
        assertEquals(second, objectRoundRobinSupplier.get("foo"));
    }

    @Test
    public void testRoundRobinRemove() {
        final List<IntegerSupplier> suppliers = new ArrayList<>();
        suppliers.add(new IntegerSupplier(2));
        suppliers.add(new IntegerSupplier(2));
        final RoundRobinSupplier<Integer> supplier = new RoundRobinSupplier<>(suppliers);
        supplier.remove(new RemovePredicate<Integer>() {

            @Override
            public boolean matches(final Supplier<Integer> t) {
                return ((IntegerSupplier) t).integer == 1;
            }
        });
        assertEquals(new Integer(2), supplier.get("foo"));
        assertEquals(new Integer(2), supplier.get("foo"));
    }

    @Test
    public void testNullSuppliers() {
        assertNull(new RoundRobinSupplier<>(null).get("foo"));
    }

    @Test
    public void testNoSuppliers() {
        assertNull(new RoundRobinSupplier<>(Collections.<Supplier<Object>>emptyList()).get("foo"));
    }

    @Test
    public void testAddSuppliers() {
        final RoundRobinSupplier<Integer> supplier = new RoundRobinSupplier<>(null);
        supplier.add(new IntegerSupplier(2));
        assertEquals(new Integer(2), supplier.get("foo"));
    }

    @Test
    public void testSolrRemovePredicate() {
        HttpSolrServerFactory factory = new HttpSolrServerFactory();
        List<HttpSolrServerManager> managers = Arrays.asList(new HttpSolrServerManager("http://db1:8983/solr", factory),
                new HttpSolrServerManager("https://db1:4564/solr", factory));
        final RoundRobinSupplier<HttpSolrServer> supplier = new RoundRobinSupplier<>(managers);
        supplier.remove(new HttpSolrServerManagerRemovePredicate("http://db1:8983/solr"));
        assertEquals("https://db1:4564/solr/core", supplier.get("core").getBaseURL());
        assertEquals("https://db1:4564/solr/core", supplier.get("core").getBaseURL());
    }
}
