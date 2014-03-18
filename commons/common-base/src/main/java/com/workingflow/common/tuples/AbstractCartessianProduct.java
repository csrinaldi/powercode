package com.workingflow.common.tuples;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;

import java.util.Arrays;
import java.util.Iterator;
import org.codehaus.jackson.annotate.JsonIgnore;

abstract class AbstractCartessianProduct implements CartesianProduct {

    private static final Joiner JOINER = Joiner.on(", ");

    protected Object[] members;

    public AbstractCartessianProduct(Object[] members) {
        this.members = members;
    }

    @Override
    public int getArity() {
        return members.length;
    }

    @SuppressWarnings("unchecked")
    protected <T> T getMember(int index) {
        return (T) get(index);
    }

    @Override
    public Object get(int index) {
        return members[index];
    }

    protected void set(int index, Object value) {
        members[index] = value;
    }

    @Override
    public Iterator<Object> iterator() {
        return Arrays.asList(members).iterator();
    }

    protected boolean equalMembers(CartesianProduct other) {
        for (int i = 0; i < getArity(); i++) {
            if (!Objects.equal(get(i), other.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(members);
    }

    public String toString() {
        return "(" + JOINER.join(members) + ")";
    }

    @JsonIgnore
    public Object[] getMembers() {
        return members;
    }

    public void setMembers(Object[] members) {
        this.members = members;
    }
}
