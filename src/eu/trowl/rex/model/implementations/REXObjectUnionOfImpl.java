package eu.trowl.rex.model.implementations;

import java.util.ArrayList;
import java.util.List;

import eu.trowl.rex.model.interfaces.REXObjectUnionOf;

public class REXObjectUnionOfImpl extends REXClassExpressionImpl
 implements REXObjectUnionOf {

//	Set<REL2ClassExpressionImpl> operands;

	List<REXClassExpressionImpl> operands;

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
}
