package eu.trowl.rex.model.interfaces;

import java.util.Collection;

public interface REXCollectionExpression<P> extends REXClassExpression {

	public Collection<? extends P> getEntities();
}
