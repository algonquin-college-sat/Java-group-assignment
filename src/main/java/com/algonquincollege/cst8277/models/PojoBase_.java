package com.algonquincollege.cst8277.models;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-12-06T14:40:05.797-0500")
@StaticMetamodel(PojoBase.class)
public class PojoBase_ {
	public static volatile SingularAttribute<PojoBase, Integer> id;
	public static volatile SingularAttribute<PojoBase, LocalDateTime> createdDate;
	public static volatile SingularAttribute<PojoBase, LocalDateTime> updatedDate;
	public static volatile SingularAttribute<PojoBase, Integer> version;
}
