package eu.trowl.rex.model.implementations;

import java.util.Set;

import eu.trowl.rex.factory.REXDataFactory;
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

	@Override
	public boolean isDefinedBy(REXClassImpl cls) {
		// TODO Auto-generated method stub
		return filler.isDefinedBy(cls);
	}

	@Override
	public boolean isPartialAbsorbable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCompletelyAbsorbable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addToPatialAbsorbable(Set<REXClassExpressionImpl> pas) {
		// TODO Auto-generated method stub
		
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
		REXClassExpressionImpl fillerComp = filler.testComplement();
		if(fillerComp != null)
		{
			complement = prop.alls.get(fillerComp);
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
			complement = rexDataFactory.getREXObjectAllValuesFrom(prop, fillerComp);
			complement.complement = this;
		}
		return complement;
	}
}
