package com.workingflow.common.tuples;

//import org.jboss.errai.common.client.api.annotations.Portable;

//@Portable
public class Pair<M0, M1> extends AbstractCartessianProduct implements CartesianProduct {

    Pair(M0 m0, M1 m1) {
        super(new Object[]{m0, m1});
    }

    public Pair() {
        super(new Object[2]);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair<?, ?>)) {
            return false;
        }
        Pair<?, ?> other = (Pair<?, ?>) o;
        return equalMembers(other);
    }

    public M0 getM0() {
        return getMember(0);
    }

    public M1 getM1() {
        return getMember(1);
    }

    public void setM0(M0 value) {
        set(0, value);
    }

    public void setM1(M1 value) {
        set(1, value);
    }
}
