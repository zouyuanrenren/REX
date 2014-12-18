package eu.trowl.rex.model.implementations;

import java.util.Set;

import eu.trowl.rex.factory.REXDataFactory;
import eu.trowl.rex.model.interfaces.REXObjectComplementOf;

public class REXObjectComplementOfImpl extends REXClassExpressionImpl
		implements REXObjectComplementOf {

	public REXObjectComplementOfImpl(REXClassExpressionImpl complement) {
		// TODO Auto-generated constructor stub
		this.complement = complement;
		complement.complement = this;
	}

	@Override
	public REXClassExpressionImpl getClassExpression() {
		// TODO Auto-generated method stub
		return complement;
	}

	@Override
	public void LHS() {
		// TODO Auto-generated method stub
		// how to set the LHS() of a negation?
//		System.out.println("LHS() of a complement?");
	}

	@Override
	public String toString() {
		return "(not "+complement.toString()+")";
	}

	@Override
	public void RHS() {
		// TODO Auto-generated method stub
		complement.LHS();
	}

	@Override
	public boolean isDefinedBy(REXClassImpl cls) {
		// TODO Auto-generated method stub
		return complement.isDefinedBy(cls);
	}

	@Override
	public void addToPatialAbsorbable(Set<REXClassExpressionImpl> pas) {
		// TODO Auto-generated method stub
		if(isPartialAbsorbable())
			pas.add(this);
	}

	@Override
	public boolean isPartialAbsorbable() {
		// TODO Auto-generated method stub
		return isCompletelyAbsorbable();
	}

	@Override
	public boolean isCompletelyAbsorbable() {
		// TODO Auto-generated method stub
		if(complement instanceof REXClassImpl)
			return true;
		return false;
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
		return getClassExpression();
	}

	@Override
	public REXClassExpressionImpl getComplement(REXDataFactory rexDataFactory) {
		// TODO Auto-generated method stub
		return getClassExpression();
	}
}
