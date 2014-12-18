package eu.trowl.rex.model.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import eu.trowl.rex.factory.REXDataFactory;
import eu.trowl.rex.model.interfaces.REXObjectUnionOf;

public class REXObjectUnionOfImpl extends REXClassExpressionImpl
 implements REXObjectUnionOf {

//	Set<REL2ClassExpressionImpl> operands;

	protected List<REXClassExpressionImpl> operands;

	public REXObjectUnionOfImpl(List<REXClassExpressionImpl> operands2) {
		// TODO Auto-generated constructor stub
		operands = operands2;
	}

	public REXObjectUnionOfImpl(
			REXClassExpressionImpl leftConcept,
			REXClassExpressionImpl rightConcept) {
		// TODO Auto-generated constructor stub
		operands = new ArrayList<REXClassExpressionImpl>();
		operands.add(leftConcept);
		operands.add(rightConcept);
	}

	@Override
	public List<REXClassExpressionImpl> getEntities() {
		// TODO Auto-generated method stub
		return operands;
	}

	@Override
	public void LHS() {
		// TODO Auto-generated method stub
		originalLHS = true;
		for(REXClassExpressionImpl operand:operands)
			operand.LHS();
	}

	@Override
	public String toString() {
		String string = "("+operands.get(0).toString();		
		for(int i =1;i<operands.size();i++)
			string+= " OR "+operands.get(i);
		string+=")";
		return string;
	}

	@Override
	public void RHS() {
		// TODO Auto-generated method stub
		for(REXClassExpressionImpl operand:operands)
			operand.RHS();
	}
	
	@Override
	public boolean isDefinedBy(REXClassImpl cls) {
		// TODO Auto-generated method stub
		for(REXClassExpressionImpl disjunct:operands)
			if(disjunct.isDefinedBy(cls))
				return true;
		return false;
	}

	@Override
	public boolean isPartialAbsorbable() {
		// TODO Auto-generated method stub
		for(REXClassExpressionImpl exp:operands)
			if(exp.isPartialAbsorbable())
				return true;
		return false;
	}

	@Override
	public boolean isCompletelyAbsorbable() {
		// TODO Auto-generated method stub
		for(REXClassExpressionImpl exp:operands)
			if(!exp.isCompletelyAbsorbable())
				return false;
		return true;
	}

	@Override
	public void addToPatialAbsorbable(Set<REXClassExpressionImpl> pas) {
		// TODO Auto-generated method stub
		for(REXClassExpressionImpl exp:operands)
			exp.addToPatialAbsorbable(pas);
	}

	@Override
	public void addToNotCompletelyAbsorbable(Set<REXClassExpressionImpl> nCA) {
		// TODO Auto-generated method stub
		for(REXClassExpressionImpl exp:operands)
			exp.addToNotCompletelyAbsorbable(nCA);
	}

	@Override
	public REXClassExpressionImpl testComplement() {
		// TODO Auto-generated method stub
		if(complement != null)
			return complement;
		REXClassExpressionImpl firstComp = operands.get(0).testComplement();
		REXClassExpressionImpl secondComp = operands.get(1).testComplement();
		if(firstComp != null && secondComp != null)
		{
			complement = firstComp.Ands.get(secondComp);
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
			REXClassExpressionImpl firstComp = operands.get(0).getComplement(rexDataFactory);
			REXClassExpressionImpl secondComp = operands.get(1).getComplement(rexDataFactory);
			complement = rexDataFactory.getREXObjectIntersectionOf(firstComp, secondComp);
			complement.complement = this;
		}
		return complement;
	}

}
