package eu.trowl.rex.model.interfaces;

import java.util.List;

public interface REXPropertyExpressionChain extends
		REXSubObjectPropertyExpression {

	public List<? extends REXObjectPropertyExpression> getProperties();
}
