package eu.trowl.rex.model.implementations;

import eu.trowl.rex.factory.REXDataFactory;
import eu.trowl.rex.model.interfaces.REXObjectMinCardinality;

public class REXObjectMinCardinalityImpl extends REXObjectSomeValuesFromImpl
		implements REXObjectMinCardinality {

//	REXClassExpressionImpl filler;
//	REXObjectPropertyExpressionImpl prop;
	int cardinalityValue;

	public REXObjectMinCardinalityImpl(int cardinality,
			REXObjectPropertyExpressionImpl role,
			REXClassExpressionImpl filler) {
		// TODO Auto-generated constructor stub
		super(role, new REXCardinalityAtomic(filler, cardinality));
		this.cardinalityValue = cardinality;
//		this.prop = role;
//		this.filler = filler;
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
		return "("+prop.toString()+" >= "+cardinalityValue+" "+filler.toString()+")";
	}
	
	@Override
	public void RHS() {
		// TODO Auto-generated method stub
		filler.RHS();
	}
	
	@Override
	public REXClassExpressionImpl testComplement() {
		// TODO Auto-generated method stub
		if(complement != null)
			return complement;
		REXClassExpressionImpl fillerFiller = ((REXCardinalityAtomic)filler).filler;
		if(fillerFiller.maxs.containsKey(cardinalityValue-1))
		{
			complement = fillerFiller.maxs.get(cardinalityValue-1).get(prop);
			if(complement != null)
				complement.complement = this;
		}
		return complement;
	}
	
	@Override
	public REXClassExpressionImpl getComplement(REXDataFactory rexDataFactory){
		if(complement == null)
		{
			REXClassExpressionImpl fillerFiller = ((REXCardinalityAtomic)filler).filler;
			if(fillerFiller.maxs.containsKey(cardinalityValue-1))
				complement = fillerFiller.maxs.get(cardinalityValue-1).get(prop);
			if(complement == null)
				complement = rexDataFactory.getREXObjectMaxCardinality(cardinalityValue-1, prop, fillerFiller);
			complement.complement = this;
		}
		return complement;
	}


}
