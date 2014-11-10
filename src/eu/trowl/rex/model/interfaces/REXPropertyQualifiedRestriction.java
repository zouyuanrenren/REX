package eu.trowl.rex.model.interfaces;

public interface REXPropertyQualifiedRestriction<P, F> extends
		REXPropertyRestriction<P> {

	public F getFiller();
}
