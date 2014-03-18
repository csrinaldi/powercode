package com.workingflow.common.tuples;


//import org.jboss.errai.common.client.api.annotations.Portable;

//@Portable
public class Sextuple<M0, M1, M2, M3, M4, M5> extends AbstractCartessianProduct implements CartesianProduct {

    Sextuple(M0 m0, M1 m1, M2 m2, M3 m3, M4 m4, M5 m5) {
        super(new Object[]{m0, m1, m2, m3, m4, m5});
    }

    public Sextuple() {
        super(new Object[6]);
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

    public M5 getM5() {
        return getMember(5);
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

    public void setM5(M5 value) {
        set(5, value);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Sextuple<?, ?, ?, ?, ?, ?>)) {
            return false;
        }
        Sextuple<?, ?, ?, ?, ?, ?> other = (Sextuple<?, ?, ?, ?, ?, ?>) o;
        return equalMembers(other);
    }
}
