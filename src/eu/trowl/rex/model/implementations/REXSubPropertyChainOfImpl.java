package eu.trowl.rex.model.implementations;

import eu.trowl.rex.model.interfaces.REXObjectPropertyExpression;
import eu.trowl.rex.model.interfaces.REXSubObjectPropertyExpression;
import eu.trowl.rex.model.interfaces.REXSubObjectPropertyOf;

public class REXSubPropertyChainOfImpl implements REXSubObjectPropertyOf {

	protected REXPropertyExpressionChainImpl chain;
	protected REXObjectPropertyExpressionImpl superRole;
	
	@Override
	public REXSubObjectPropertyExpression getSubProperty() {
		// TODO Auto-generated method stub
		return chain;
	}

	@Override
	public REXObjectPropertyExpression getSuperProperty() {
		// TODO Auto-generated method stub
		return superRole;
	}

	@Override
	public void initialise() {
		// TODO Auto-generated method stub
		
	}

}
