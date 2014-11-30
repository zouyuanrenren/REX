package eu.trowl.rex.model.implementations;

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
	public REXClassExpressionImpl testComplement() {
		// TODO Auto-generated method stub
		return getClassExpression();
	}
}
