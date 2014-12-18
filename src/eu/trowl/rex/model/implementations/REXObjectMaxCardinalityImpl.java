package eu.trowl.rex.model.implementations;

import java.util.Set;

import eu.trowl.rex.factory.REXDataFactory;
import eu.trowl.rex.model.interfaces.REXObjectMaxCardinality;

public class REXObjectMaxCardinalityImpl extends REXClassExpressionImpl
		implements REXObjectMaxCardinality {

	protected REXClassExpressionImpl filler;
	protected REXObjectPropertyExpressionImpl prop;
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

	@Override
	public boolean isDefinedBy(REXClassImpl cls) {
		// TODO Auto-generated method stub
		return filler.isDefinedBy(cls);
	}

	@Override
	public boolean isPartialAbsorbable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCompletelyAbsorbable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addToPatialAbsorbable(Set<REXClassExpressionImpl> pas) {
		// TODO Auto-generated method stub
		pas.add(this);
	}

	@Override
	public void addToNotCompletelyAbsorbable(Set<REXClassExpressionImpl> nCA) {
		// TODO Auto-generated method stub
		nCA.add(this);
	}

	@Override
	public REXClassExpressionImpl testComplement() {
		// TODO Auto-generated method stub
		if(complement != null)
			return complement;
		if(filler.mins.containsKey(cardinalityValue+1))
		{
			complement = filler.mins.get(cardinalityValue+1).get(prop);
			if(complement != null)
				complement.complement = this;
		}
		return complement;
	}

	@Override
	public REXClassExpressionImpl getComplement(REXDataFactory rexDataFactory) {
		// TODO Auto-generated method stub
		if(complement == null)
		{
			if(filler.mins.containsKey(cardinalityValue+1))
				complement = filler.mins.get(cardinalityValue+1).get(prop);
			if(complement == null)
				complement = rexDataFactory.getREXObjectMinCardinality(cardinalityValue+1, prop, filler);
			complement.complement = this;
		}
		return complement;
	}


}
