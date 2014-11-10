package eu.trowl.rex.model.interfaces;

public interface REXPropertyHasValue<P, V> extends REXPropertyRestriction<P> {

	public V getValue();
}
