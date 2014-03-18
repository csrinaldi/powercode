package com.workingflow.common.tuples;

import com.google.common.collect.Iterators;
import java.util.Iterator;
//import org.jboss.errai.common.client.api.annotations.Portable;


//@Portable
public class Unit implements CartesianProduct {

    public Unit() {
    }

    @Override
    public int getArity() {
        return 0;
    }

    @Override
    public Object get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Object> iterator() {
        return Iterators.emptyIterator();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Unit;
    }
}
