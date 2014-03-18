package com.workingflow.common.domain.auditory;


import com.workingflow.common.domain.Persistable;
import java.io.Serializable;
import java.util.Date;

public interface Auditable<S, ID extends Serializable> extends Persistable<ID> {

  Date getCreatedDate();

  void setCreatedDate(Date createdDate);

  S getCreatedBy();

  void setCreatedBy(S createdBy);

  Date getUpdatedDate();

  void setUpdatedDate(Date updatedDate);

  S getUpdatedBy();

  void setUpdatedBy(S updatedBy);
}
