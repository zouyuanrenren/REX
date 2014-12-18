package eu.trowl.rex.absorption;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.trowl.rex.factory.REXDataFactory;
import eu.trowl.rex.model.implementations.REXClassExpressionImpl;
import eu.trowl.rex.model.implementations.REXClassImpl;
import eu.trowl.rex.model.implementations.REXObjectComplementOfImpl;
import eu.trowl.rex.model.implementations.REXObjectIntersectionOfImpl;
import eu.trowl.rex.model.implementations.REXObjectSomeValuesFromImpl;
import eu.trowl.rex.model.implementations.REXObjectUnionOfImpl;

public class BinaryAbsorption extends RoleAbsorption {

	public BinaryAbsorption(REXDataFactory factory) {
		super(factory);
		// TODO Auto-generated constructor stub
	}
	
//	@Override
//	public boolean absorbLHS(REXClassExpressionImpl lhs, REXClassExpressionImpl rhs) {
//		addToGeneralTBox(factory.getREXObjectComplementOf(lhs),rhs);
//		return false;
//	}

	
	protected void addToGeneralTBox(REXClassExpressionImpl lhsComp,
			REXClassExpressionImpl rhs) {
		// TODO Auto-generated method stub
		Set<REXClassImpl> newlhs = new HashSet<REXClassImpl>();
//		Set<REXClassExpressionImpl> newLHSComp = new HashSet<REXClassExpressionImpl>();
		Set<REXClassExpressionImpl> newRHS = new HashSet<REXClassExpressionImpl>();
		separate(lhsComp, newlhs, newRHS);
		separate(rhs, newlhs, newRHS);
		if(newlhs.size() != 0 &&	addNewBinaryUnfoldableSuperClass(newlhs, newRHS))
				;
		else
			super.addToGeneralTBox(lhsComp, rhs);

	}

	private boolean addNewBinaryUnfoldableSuperClass(Set<REXClassImpl> newlhs,
			Set<REXClassExpressionImpl> newRHS) {
		// TODO Auto-generated method stub
		List<REXClassImpl> finalLhs = new ArrayList<REXClassImpl>();
		for(REXClassImpl lhs:newlhs)
			if(!lhs.isDefined())
				finalLhs.add(lhs);
		if(finalLhs.size() > 0)
		{
			REXClassExpressionImpl inter = factory.getREXObjectIntersectionOf(finalLhs);
			newRHS.addAll(newlhs);
			newRHS.remove(finalLhs);
			List<REXClassExpressionImpl> finalRhs = new ArrayList<REXClassExpressionImpl>(newRHS);;
			REXClassExpressionImpl union = factory.getREXObjectUnionOf(finalRhs);
			if(inter instanceof REXClassImpl)
				addToUnfoldableTBox(inter.asREXClassImpl(), union);
			else
			{
			HashSet<REXClassExpressionImpl> unions = factory.binaryUnfoldableSuperClass.get(inter);
			if(unions == null)
			{
				unions = new HashSet<REXClassExpressionImpl>();
				factory.binaryUnfoldableSuperClass.put(inter, unions);
			}
			unions.add(union);
			}
			return true;
		}
		return false;
	}

	private void separate(REXClassExpressionImpl rhs,
			Set<REXClassImpl> newlhs, Set<REXClassExpressionImpl> newRHS) {
		// TODO Auto-generated method stub
		if(rhs instanceof REXObjectUnionOfImpl){
			REXObjectUnionOfImpl union = (REXObjectUnionOfImpl) rhs;
			for(REXClassExpressionImpl	disjunct:union.getEntities())
				if(disjunct instanceof REXObjectComplementOfImpl && disjunct.complement instanceof REXClassImpl)
					newlhs.add(disjunct.complement.asREXClassImpl());
				else
					separate(disjunct, newlhs, newRHS);
		}
		else if(rhs instanceof REXObjectComplementOfImpl)
			if(rhs.complement instanceof REXClassImpl)
				newlhs.add(rhs.complement.asREXClassImpl());
			else if(rhs.complement instanceof REXObjectIntersectionOfImpl){
				REXObjectIntersectionOfImpl and = (REXObjectIntersectionOfImpl) rhs.complement;
				for(REXClassExpressionImpl conjunct:and.getEntities())
					if(conjunct instanceof REXClassImpl)
						newlhs.add(conjunct.asREXClassImpl());
					else
						separate(factory.getREXObjectComplementOf(conjunct), newlhs, newRHS);
			}
			else
				newRHS.add(rhs);
		else
			newRHS.add(rhs);

	}

}
