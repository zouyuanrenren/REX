package eu.trowl.rex.absorption;

import eu.trowl.rex.factory.REXDataFactory;
import eu.trowl.rex.model.implementations.REXClassExpressionImpl;
import eu.trowl.rex.model.implementations.REXClassImpl;
import eu.trowl.rex.model.implementations.REXObjectComplementOfImpl;
import eu.trowl.rex.model.implementations.REXObjectIntersectionOfImpl;
import eu.trowl.rex.model.implementations.REXObjectUnionOfImpl;
import eu.trowl.rex.model.implementations.REXSubClassOfImpl;

public class ConceptAbsorption extends AbsorptionVisitor {

	public ConceptAbsorption(REXDataFactory factory) {
		super(factory);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void absorb(REXSubClassOfImpl axiom) {
		// TODO Auto-generated method stub
		if(axiom.rhsRole == null)
			absorbLHS(axiom.lhs, axiom.rhsFiller);
		else
			absorbLHS(axiom.lhs, axiom.rhsRole.somes.get(axiom.rhsFiller));
	}

	@Override
	public boolean absorbLHS(REXClassExpressionImpl lhs, REXClassExpressionImpl rhs) {
		// TODO Auto-generated method stub
		if(lhs instanceof REXClassImpl)
		{
//			need to examine if the lhs is absorbable
			if(lhs.asREXClassImpl().isDefined())
			{
				// unabsorbable class has to be internalised
//				addToGeneralTBox(lhs, rhs);
//				return false;
				return absorbRHS(factory.getREXObjectComplementOf(lhs), rhs);
			}
			else
			{
				REXSubClassOfImpl axiom = factory.getREXSubClassOf(lhs, rhs);
				axiom.initialise();
				addToUnfoldableTBox(lhs.asREXClassImpl(), rhs);
				return true;
			}
		}
		else if(lhs instanceof REXObjectIntersectionOfImpl)
		{
			REXObjectIntersectionOfImpl inter = (REXObjectIntersectionOfImpl) lhs;
			REXClassExpressionImpl head = inter.getEntities().get(0);
			REXClassExpressionImpl tail = inter.getEntities().get(1);
			if(head instanceof REXClassImpl)
			{
				if(head.asREXClassImpl().isDefined())
					return absorbLHS(tail, factory.getREXObjectUnionOf(factory.getREXObjectComplementOf(head), rhs));
				else
					// can be absorbed
				{
					REXSubClassOfImpl axiom = factory.getREXSubClassOf(head, factory.getREXObjectUnionOf(factory.getREXObjectComplementOf(tail), rhs));
					axiom.initialise();
					addToUnfoldableTBox(head.asREXClassImpl(), factory.getREXObjectUnionOf(factory.getREXObjectComplementOf(tail), rhs));
					return true;
				}
				
			}
			else
				return absorbLHS(tail, factory.getREXObjectUnionOf(factory.getREXObjectComplementOf(head), rhs));
		}
		else
			return absorbRHS(factory.getREXObjectComplementOf(lhs), rhs);

	}

	@Override
	public boolean absorbRHS(REXClassExpressionImpl lhsComp,
			REXClassExpressionImpl rhs) {
		// TODO Auto-generated method stub
		if(rhs instanceof REXObjectComplementOfImpl)
		{
			REXObjectComplementOfImpl comp = (REXObjectComplementOfImpl) rhs;
			if(comp.complement instanceof REXClassImpl && !comp.complement.asREXClassImpl().isDefined())
				{
					REXSubClassOfImpl axiom = factory.getREXSubClassOf(comp.complement, lhsComp);
					axiom.initialise();
					addToUnfoldableTBox(comp.complement.asREXClassImpl(), lhsComp);
					return true;
				}
			else
			{
				addToGeneralTBox(lhsComp,rhs);
				return false;
			}
		}
		else if (rhs instanceof REXObjectUnionOfImpl)
		{
			REXObjectUnionOfImpl union = (REXObjectUnionOfImpl) rhs;
			REXClassExpressionImpl head = union.getEntities().get(0);
			REXClassExpressionImpl tail = union.getEntities().get(1);
			if(head instanceof REXObjectComplementOfImpl && head.complement instanceof REXClassImpl && !head.complement.asREXClassImpl().isDefined())
					{
						REXClassExpressionImpl newUnion = factory.getREXObjectUnionOf(lhsComp, tail);
						REXSubClassOfImpl axiom = factory.getREXSubClassOf(head.complement, newUnion);
						axiom.initialise();
						addToUnfoldableTBox(head.complement.asREXClassImpl(), newUnion);
						return true;
					}
					else
					{
						return absorbRHS(factory.getREXObjectUnionOf(lhsComp, head), tail);
					}
		}
		else
		{
			addToGeneralTBox(lhsComp, rhs);
			return false;
		}
	}

	@Override
	public void absorbEQLHS(REXClassExpressionImpl lhs, REXClassExpressionImpl rhs) {
		// TODO Auto-generated method stub
		if(lhs instanceof REXClassImpl && lhs.asREXClassImpl().unfoldableSuperClasses.size() == 0 && lhs.asREXClassImpl().canBeDefinedBy(rhs))
		{
			lhs.asREXClassImpl().unfoldableDefinition.add(rhs);
//			return true;
		}
		else if(lhs instanceof REXObjectComplementOfImpl && lhs.complement instanceof REXClassImpl && lhs.complement.asREXClassImpl().unfoldableSuperClasses.size() == 0 && lhs.complement.asREXClassImpl().canBeDefinedBy(factory.getREXObjectComplementOf(rhs)))
		{
			lhs.complement.asREXClassImpl().unfoldableDefinition.add(factory.getREXObjectComplementOf(rhs));
//			return true;
		}
		if (absorbEQRHS(lhs, rhs) == false)
		{
			absorbLHS(lhs, rhs);
			absorbLHS(rhs, lhs);
		}
	}

	@Override
	public boolean absorbEQRHS(REXClassExpressionImpl lhs,
			REXClassExpressionImpl rhs) {
		// TODO Auto-generated method stub
		if(rhs instanceof REXClassImpl && rhs.asREXClassImpl().unfoldableSuperClasses.size() == 0 && rhs.asREXClassImpl().canBeDefinedBy(lhs))
		{
			rhs.asREXClassImpl().unfoldableDefinition.add(lhs);
			return true;
		}
		else if(rhs instanceof REXObjectComplementOfImpl && rhs.complement instanceof REXClassImpl && rhs.complement.asREXClassImpl().unfoldableSuperClasses.size() == 0 && rhs.complement.asREXClassImpl().canBeDefinedBy(factory.getREXObjectComplementOf(lhs)))
		{
			rhs.complement.asREXClassImpl().unfoldableDefinition.add(factory.getREXObjectComplementOf(lhs));
			return true;
		}
//		addToGeneralTBox(factory.getREXObjectComplementOf(lhs),rhs);
//		addToGeneralTBox(factory.getREXObjectComplementOf(rhs),lhs);
		return false;
	}

}
