package com.workingflow.common.tuples;

//import org.jboss.errai.common.client.api.annotations.Portable;

//@Portable
public class Simple<M0> extends AbstractCartessianProduct implements CartesianProduct {

    Simple(M0 m0) {
        super(new Object[]{m0});;
    }

    public Simple() {
        super(new Object[1]);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Simple<?>)) {
            return false;
        }
        Simple<?> other = (Simple<?>) o;
        return equalMembers(other);
    }

    public M0 getM0() {
        return getMember(0);
    }

    public void setM0(M0 value) {
        set(0, value);
    }


}
