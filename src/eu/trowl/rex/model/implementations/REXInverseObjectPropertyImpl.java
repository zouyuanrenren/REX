package eu.trowl.rex.model.implementations;

import eu.trowl.rex.model.interfaces.REXInverseObjectProperty;

public class REXInverseObjectPropertyImpl extends REXObjectPropertyExpressionImpl
		implements REXInverseObjectProperty {

	protected REXObjectPropertyImpl inverse = null;

	public REXInverseObjectPropertyImpl(REXObjectPropertyImpl prop) {
		// TODO Auto-generated constructor stub
		inverse = prop;
		prop.setInverse(this);
	}

	@Override
	public REXObjectPropertyExpressionImpl getInversePropertyExpression() {
		// TODO Auto-generated method stub
		return inverse;
	}

	@Override
	public void LHS() {
		// TODO Auto-generated method stub
		this.onLHS = true;
//		this.getInversePropertyExpression().related = true;
	}
	
	@Override
	public String toString() {
		return "(INV "+ inverse.toString()+")";
	}

}
