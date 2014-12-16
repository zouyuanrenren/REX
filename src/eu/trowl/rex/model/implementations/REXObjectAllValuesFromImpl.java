package eu.trowl.rex.model.implementations;

import eu.trowl.rex.model.interfaces.REXObjectAllValuesFrom;

public class REXObjectAllValuesFromImpl extends REXClassExpressionImpl
		implements REXObjectAllValuesFrom {

	protected REXClassExpressionImpl filler;
	protected REXObjectPropertyExpressionImpl prop;
	
	public REXObjectAllValuesFromImpl(REXObjectPropertyExpressionImpl role,
			REXClassExpressionImpl filler2) {
		// TODO Auto-generated constructor stub
		prop = role;
		filler = filler2;
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
	}

	@Override
	public String toString() {
		return "("+prop.toString()+" All "+filler.toString()+")";
	}

	@Override
	public void RHS() {
		// TODO Auto-generated method stub
		prop.onRHSAllValuesFrom = true;
		filler.RHS();
	}

	@Override
	public REXClassExpressionImpl testComplement() {
		// TODO Auto-generated method stub
		if(complement != null)
			return complement;
		REXClassExpressionImpl fillerComp = filler.testComplement();
		if(fillerComp != null)
			complement = prop.somes.get(fillerComp);
		return complement;
	}

	@Override
	public boolean isDefinedBy(REXClassImpl cls) {
		// TODO Auto-generated method stub
		return filler.isDefinedBy(cls);
	}
}
