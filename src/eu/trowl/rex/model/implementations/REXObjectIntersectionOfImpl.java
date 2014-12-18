package eu.trowl.rex.model.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import eu.trowl.rex.factory.REXDataFactory;
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
	public boolean isDefinedBy(REXClassImpl cls) {
		// TODO Auto-generated method stub
		for(REXClassExpressionImpl conjunct:intersects)
			if(conjunct.isDefinedBy(cls))
				return true;
		return false;
	}

	@Override
	public boolean isPartialAbsorbable() {
		// TODO Auto-generated method stub
		for(REXClassExpressionImpl ex:intersects)
			if(!ex.isPartialAbsorbable())
				return false;
		return true;
	}

	@Override
	public boolean isCompletelyAbsorbable() {
		// TODO Auto-generated method stub
		for(REXClassExpressionImpl ex:intersects)
			if(!ex.isCompletelyAbsorbable())
				return false;
		return true;
	}

	@Override
	public void addToPatialAbsorbable(Set<REXClassExpressionImpl> pas) {
		// TODO Auto-generated method stub
		if(isPartialAbsorbable())
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
		REXClassExpressionImpl firstComp = intersects.get(0).testComplement();
		REXClassExpressionImpl secondComp = intersects.get(1).testComplement();
		if(firstComp != null && secondComp != null)
		{
			complement = firstComp.Ors.get(secondComp);
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
			REXClassExpressionImpl firstComp = intersects.get(0).getComplement(rexDataFactory);
			REXClassExpressionImpl secondComp = intersects.get(1).getComplement(rexDataFactory);
			complement = rexDataFactory.getREXObjectUnionOf(firstComp, secondComp);
			complement.complement = this;
		}
		return complement;
	}

}
