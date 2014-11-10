package eu.trowl.rex.model.interfaces;

import java.util.List;

public interface REXClassCollectionAxiom extends REXClassAxiom {

	public List<? extends REXClassExpression> getClassExpressions();
}
