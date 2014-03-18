package com.workingflow.common.tuples;

import javax.annotation.Nullable;

public interface CartesianProduct extends Iterable<Object> {

  int getArity();

  @Nullable
  Object get(int index);

}
