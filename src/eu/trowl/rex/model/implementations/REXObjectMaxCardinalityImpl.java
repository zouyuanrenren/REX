package eu.trowl.rex.model.implementations;

import eu.trowl.rex.model.interfaces.REXObjectMaxCardinality;

public class REXObjectMaxCardinalityImpl extends REXClassExpressionImpl
		implements REXObjectMaxCardinality {

	REXClassExpressionImpl filler;
	REXObjectPropertyExpressionImpl prop;
	int cardinalityValue;

	public REXObjectMaxCardinalityImpl(int cardinality,
			REXObjectPropertyExpressionImpl role,
			REXClassExpressionImpl filler) {
		// TODO Auto-generated constructor stub
		this.cardinalityValue = cardinality;
		this.prop = role;
		this.filler = filler;
	}

	@Override
	public int getCardinalityValue() {
		// TODO Auto-generated method stub
		return cardinalityValue;
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
		return "("+prop.toString()+" <= "+cardinalityValue+" "+filler.toString()+")";
	}

	@Override
	public void RHS() {
		// TODO Auto-generated method stub
		if(cardinalityValue == 1)
			prop.onMax1Cardinality = true;
		filler.RHS();
	}


}
