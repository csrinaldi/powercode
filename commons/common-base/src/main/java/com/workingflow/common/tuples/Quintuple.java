package com.workingflow.common.tuples;

//import org.jboss.errai.common.client.api.annotations.Portable;

//@Portable
public class Quintuple<M0, M1, M2, M3, M4> extends AbstractCartessianProduct implements CartesianProduct {

    Quintuple(M0 m0, M1 m1, M2 m2, M3 m3, M4 m4) {
        super(new Object[]{m0, m1, m2, m3, m4});
    }

    public Quintuple() {
        super(new Object[5]);
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

    public M4 getM4() {
        return getMember(4);
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

    public void setM4(M4 value) {
        set(4, value);
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Quintuple<?, ?, ?, ?, ?>)) {
            return false;
        }
        Quintuple<?, ?, ?, ?, ?> other = (Quintuple<?, ?, ?, ?, ?>) o;
        return equalMembers(other);
    }
}
