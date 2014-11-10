package eu.trowl.rex.model.interfaces;

public interface REXCardinality<P, F> extends
		REXPropertyQualifiedRestriction<P, F> {

	public int getCardinalityValue();
}
