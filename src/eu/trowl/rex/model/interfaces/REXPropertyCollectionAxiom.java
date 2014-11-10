package eu.trowl.rex.model.interfaces;

import java.util.List;

public interface REXPropertyCollectionAxiom<P> extends REXPropertyAxiom<P> {

	public List<? extends REXProperty> getProperties();
}
