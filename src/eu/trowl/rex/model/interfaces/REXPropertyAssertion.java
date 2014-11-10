package eu.trowl.rex.model.interfaces;

public interface REXPropertyAssertion<P, O> extends REXAssertion {

	public REXIndividual getSubject();
	public P getProperty();
	public O getObject();
}
