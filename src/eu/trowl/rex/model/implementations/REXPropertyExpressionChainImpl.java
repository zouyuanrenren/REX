package eu.trowl.rex.model.implementations;

import java.util.List;

import eu.trowl.rex.model.interfaces.REXObjectPropertyExpression;
import eu.trowl.rex.model.interfaces.REXPropertyExpressionChain;

public class REXPropertyExpressionChainImpl implements
		REXPropertyExpressionChain {

	List<REXObjectPropertyExpressionImpl> properties;
	
	@Override
	public List<? extends REXObjectPropertyExpression> getProperties() {
		// TODO Auto-generated method stub
		return properties;
	}

}
