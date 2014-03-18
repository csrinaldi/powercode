package com.workingflow.common.tuples;

import java.util.Arrays;
import java.util.Iterator;
//import org.jboss.errai.common.client.api.annotations.Portable;

//@Portable
public class Triple<M0, M1, M2> implements CartesianProduct {

    private M0 m0;
    private M1 m1;
    private M2 m2;

    public Triple(M0 m0, M1 m1, M2 m2) {
        this.m0 = m0;
        this.m1 = m1;
        this.m2 = m2;
    }

    public Triple() {
        this(null, null, null);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.m0 != null ? this.m0.hashCode() : 0);
        hash = 71 * hash + (this.m1 != null ? this.m1.hashCode() : 0);
        hash = 71 * hash + (this.m2 != null ? this.m2.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Triple<M0, M1, M2> other = (Triple<M0, M1, M2>) obj;
        if (this.m0 != other.m0 && (this.m0 == null || !this.m0.equals(other.m0))) {
            return false;
        }
        if (this.m1 != other.m1 && (this.m1 == null || !this.m1.equals(other.m1))) {
            return false;
        }
        if (this.m2 != other.m2 && (this.m2 == null || !this.m2.equals(other.m2))) {
            return false;
        }
        return true;
    }

    public M0 getM0() {
        return m0;
    }

    public M1 getM1() {
        return m1;
    }

    public M2 getM2() {
        return m2;
    }

    public void setM0(M0 m0) {
        this.m0 = m0;
    }

    public void setM1(M1 m1) {
        this.m1 = m1;
    }

    public void setM2(M2 m2) {
        this.m2 = m2;
    }

    @Override
    public int getArity() {
        return 3;
    }

    @Override
    public Object get(int index) {
        switch (index) {
            case 0:
                return m0;
            case 1:
                return m1;
            case 2:
                return m2;
            default: 
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public Iterator<Object> iterator() {
        return Arrays.asList(m0,m1,m2).iterator();
    }
}
