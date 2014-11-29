package eu.trowl.rex.model.implementations;

import eu.trowl.rex.model.interfaces.REXObjectSomeValuesFrom;

public class REXObjectSomeValuesFromImpl extends REXClassExpressionImpl
		implements REXObjectSomeValuesFrom {

	protected REXClassExpressionImpl filler;
	protected REXObjectPropertyExpressionImpl prop;
	
	public REXObjectSomeValuesFromImpl(REXObjectPropertyExpressionImpl role,
			REXClassExpressionImpl filler2) {
		// TODO Auto-generated constructor stub
		prop = role;
		filler = filler2;
		this.addOriginalSuperClasses(this);
	}

	@Override
	public REXClassExpressionImpl getFiller() {
		// TODO Auto-generated method stub
		return filler;
	}

	@Override
	public REXObjectPropertyExpressionImpl getProperty() {
		// TODO Auto-generated method stub
		return prop;
	}

	@Override
	public void LHS() {
		// TODO Auto-generated method stub
		originalLHS = true;
		filler.LHS();
		prop.LHS();
		prop.onRHSAllValuesFrom = true;
	}
	

	@Override
	public String toString() {
		return "("+prop.toString()+" SOME "+filler.toString()+")";
	}

	@Override
	public void RHS() {
		// TODO Auto-generated method stub
		filler.RHS();
	}
}
