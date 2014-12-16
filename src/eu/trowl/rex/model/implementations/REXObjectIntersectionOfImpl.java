package eu.trowl.rex.model.implementations;

import java.util.ArrayList;
import java.util.List;

import eu.trowl.rex.model.interfaces.REXObjectIntersectionOf;

public class REXObjectIntersectionOfImpl extends REXClassExpressionImpl
		implements REXObjectIntersectionOf {

	protected List<REXClassExpressionImpl> intersects;

	public REXObjectIntersectionOfImpl(List<REXClassExpressionImpl> conjuncts) {
		// TODO Auto-generated constructor stub
		intersects = conjuncts;
	}

	public REXObjectIntersectionOfImpl(REXClassExpressionImpl leftConjunct,
			REXClassExpressionImpl rightConjunct) {
		// TODO Auto-generated constructor stub
		intersects = new ArrayList<REXClassExpressionImpl>();
		intersects.add(leftConjunct);
		intersects.add(rightConjunct);
	}

	@Override
	public List<? extends REXClassExpressionImpl> getEntities() {
		// TODO Auto-generated method stub
		return intersects;
	}

	@Override
	public void LHS() {
		// TODO Auto-generated method stub
		originalLHS = true;
		for(REXClassExpressionImpl conjunct:intersects)
			conjunct.LHS();
	}

	@Override
	public String toString() {
		String string = "("+intersects.get(0).toString();		
		for(int i =1;i<intersects.size();i++)
			string+= " AND "+intersects.get(i);
		string+=")";
		return string;
	}

	@Override
	public void RHS() {
		// TODO Auto-generated method stub
		for(REXClassExpressionImpl conjunct:intersects)
			conjunct.RHS();
	}

	@Override
	public REXClassExpressionImpl testComplement() {
		// TODO Auto-generated method stub
		if(complement != null)
			return complement;
		REXClassExpressionImpl firstComp = intersects.get(0).testComplement();
		REXClassExpressionImpl secondComp = intersects.get(1).testComplement();
		if(firstComp != null && secondComp != null)
			complement = firstComp.Ors.get(secondComp);
		return complement;
	}

	@Override
	public boolean isDefinedBy(REXClassImpl cls) {
		// TODO Auto-generated method stub
		for(REXClassExpressionImpl conjunct:intersects)
			if(conjunct.isDefinedBy(cls))
				return true;
		return false;
	}

}
