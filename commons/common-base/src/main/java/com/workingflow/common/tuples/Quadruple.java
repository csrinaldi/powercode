package com.workingflow.common.tuples;

//import org.jboss.errai.common.client.api.annotations.Portable;

//@Portable
public class Quadruple<M0, M1, M2, M3> extends AbstractCartessianProduct implements CartesianProduct {

    Quadruple(M0 m0, M1 m1, M2 m2, M3 m3) {
        super(new Object[]{m0, m1, m2, m3});
    }

    public Quadruple() {
        super(new Object[4]);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Quadruple<?, ?, ?, ?>)) {
            return false;
        }
        Quadruple<?, ?, ?, ?> other = (Quadruple<?, ?, ?, ?>) o;
        return equalMembers(other);
    }

    public M0 getM0() {
        return getMember(0);
    }

    public M1 getM1() {
        return getMember(1);
    }

    public M2 getM2() {
        return getMember(2);
    }

    public M3 getM3() {
        return getMember(3);
    }

    public void setM0(M0 value) {
        set(0, value);
    }

    public void setM1(M1 value) {
        set(1, value);
    }

    public void setM2(M2 value) {
        set(2, value);
    }

    public void setM3(M3 value) {
        set(3, value);
    }
}
