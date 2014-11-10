package eu.trowl.rex.model.interfaces;

public interface REXSubPropertyOf<S, P> extends REXPropertyAxiom<P> {

	public S getSubProperty();
	public P getSuperProperty();
}
