package eu.trowl.rex.model.implementations;

import java.util.Set;

import eu.trowl.rex.factory.REXDataFactory;
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
		return filler.isCompletelyAbsorbable();
	}

	@Override
	public void addToPatialAbsorbable(Set<REXClassExpressionImpl> pas) {
		// TODO Auto-generated method stub
		pas.add(this);
	}

	@Override
	public void addToNotCompletelyAbsorbable(Set<REXClassExpressionImpl> nCA) {
		// TODO Auto-generated method stub
		if(!isCompletelyAbsorbable())
			nCA.add(this);
	}

	@Override
	public REXClassExpressionImpl testComplement() {
		// TODO Auto-generated method stub
		if(complement != null)
			return complement;
		REXClassExpressionImpl fillerComp = filler.testComplement();
		if(fillerComp != null)
		{
			complement = prop.somes.get(fillerComp);
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
			REXClassExpressionImpl fillerComp = filler.getComplement(rexDataFactory);
			complement = rexDataFactory.getREXObjectSomeValuesFrom(prop, fillerComp);
			complement.complement = this;
		}
		return complement;
	}
}
