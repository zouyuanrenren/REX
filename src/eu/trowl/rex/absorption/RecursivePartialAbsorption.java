package eu.trowl.rex.absorption;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.trowl.rex.factory.REXDataFactory;
import eu.trowl.rex.model.implementations.REXClassExpressionImpl;
import eu.trowl.rex.model.implementations.REXClassImpl;
import eu.trowl.rex.model.implementations.REXIndividualImpl;
import eu.trowl.rex.model.implementations.REXObjectAllValuesFromImpl;
import eu.trowl.rex.model.implementations.REXObjectComplementOfImpl;
import eu.trowl.rex.model.implementations.REXObjectIntersectionOfImpl;
import eu.trowl.rex.model.implementations.REXObjectMaxCardinalityImpl;
import eu.trowl.rex.model.implementations.REXObjectUnionOfImpl;
import eu.trowl.rex.model.implementations.REXSubClassOfImpl;

public class RecursivePartialAbsorption extends AbsorptionVisitor {

	public RecursivePartialAbsorption(REXDataFactory factory) {
		super(factory);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void absorb(REXSubClassOfImpl axiom) {
		// TODO Auto-generated method stub
		if(axiom.rhsRole == null)
			absorbRHS(factory.getREXObjectComplementOf(axiom.lhs),axiom.rhsFiller);
		else
			absorbRHS(factory.getREXObjectComplementOf(axiom.lhs), axiom.rhsRole.somes.get(axiom.rhsFiller));
	}

	@Override
	public boolean absorbLHS(REXClassExpressionImpl lhs,
			REXClassExpressionImpl rhs) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean absorbRHS(REXClassExpressionImpl lhsComp,
			REXClassExpressionImpl rhs) {
		// TODO Auto-generated method stub
		Set<REXClassExpressionImpl> pas = new HashSet<REXClassExpressionImpl>();
		PA(pas, lhsComp);
		PA(pas, rhs);
		REXClassImpl A = Absorb(pas);
		Set<REXClassExpressionImpl> nCA = new HashSet<REXClassExpressionImpl>();
		notCA(nCA, lhsComp);
		notCA(nCA, rhs);
		addToUnfoldableTBox(A, factory.getREXObjectUnionOf(new ArrayList<REXClassExpressionImpl>(nCA)));
		return false;
	}


	private REXClassImpl Absorb(Set<REXClassExpressionImpl> pas) {
		// TODO Auto-generated method stub
		List<REXClassImpl> S = new ArrayList<REXClassImpl>();
		for(REXClassExpressionImpl pa:pas)
			S.add(recursiveAbsorb(pa));
		REXClassImpl A = join(S);
		
		return A;
	}

	private REXClassImpl join(List<REXClassImpl> s) {
		// TODO Auto-generated method stub
		if(s.size() == 0)
			return factory.top;
		if(s.size() == 1)
			return s.get(0);
		else
		{
			REXClassImpl A1 = s.get(0);
			REXClassImpl A2;
			for(int i = 1;i< s.size();i++)
			{
				A2 = s.get(i);
				REXClassExpressionImpl inter = factory.getREXObjectIntersectionOf(A1, A2);
				REXClassImpl T = factory.getREXApproximationName(inter);
				HashSet<REXClassExpressionImpl> unions = factory.binaryUnfoldableSuperClass.get(inter);
				if(unions == null)
				{
					unions = new HashSet<REXClassExpressionImpl>();
					factory.binaryUnfoldableSuperClass.put(inter, unions);
				}
				unions.add(T);
				A1 = T;
			}
			return A1;
		}
	}
	
	private REXClassImpl recursiveAbsorb(REXClassExpressionImpl pa) {
		// TODO Auto-generated method stub
		if(pa instanceof REXObjectComplementOfImpl && pa.complement instanceof REXClassImpl && !(pa.complement instanceof REXIndividualImpl))
			return pa.complement.asREXClassImpl();
		REXClassImpl T = factory.getREXApproximationName(pa);
		if(pa instanceof REXObjectComplementOfImpl && pa.complement instanceof REXIndividualImpl)
		{
			REXSubClassOfImpl newAxiom = factory.getREXSubClassOf(pa.complement,T);
			newAxiom.initialise();
		}
		else if(pa instanceof REXObjectAllValuesFromImpl)
		{
			REXObjectAllValuesFromImpl all = (REXObjectAllValuesFromImpl) pa;
			
			Set<REXClassExpressionImpl> Dprimes= new HashSet<REXClassExpressionImpl>();
			PA(Dprimes, all.getFiller());
			REXClassImpl A = Absorb(Dprimes);
			addToUnfoldableTBox(A, factory.getREXObjectAllValuesFrom(factory.getREXInverseObjectProperty(all.getProperty()), T));
		}
		else if(pa instanceof REXObjectMaxCardinalityImpl)
		{
			REXObjectMaxCardinalityImpl max = (REXObjectMaxCardinalityImpl) pa;
			Set<REXClassExpressionImpl> Dprimes = new HashSet<REXClassExpressionImpl>();
			PA(Dprimes, factory.getREXObjectComplementOf(max.getFiller()));
			REXClassImpl A = Absorb(Dprimes);
			addToUnfoldableTBox(A, factory.getREXObjectAllValuesFrom(factory.getREXInverseObjectProperty(max.getProperty()), T));
		}
		else if(pa instanceof REXObjectIntersectionOfImpl)
		{
			REXObjectIntersectionOfImpl and = (REXObjectIntersectionOfImpl) pa;
			for(REXClassExpressionImpl Ci :and.getEntities())
			{
				Set<REXClassExpressionImpl> Ds = new HashSet<REXClassExpressionImpl>();
				PA(Ds, Ci);
				REXClassImpl A = Absorb(Ds);
				addToUnfoldableTBox(A, T);
			}
		}
		return T;
	}

	private void PA(Set<REXClassExpressionImpl> pas,
			REXClassExpressionImpl exp) {
		// TODO Auto-generated method stub
		exp.addToPatialAbsorbable(pas);
	}

	private void notCA(Set<REXClassExpressionImpl> nCA,
			REXClassExpressionImpl exp) {
		// TODO Auto-generated method stub
		exp.addToNotCompletelyAbsorbable(nCA);
	}

	@Override
	public void absorbEQLHS(REXClassExpressionImpl lhs,
			REXClassExpressionImpl rhs) {
		// TODO Auto-generated method stub
		if(lhs instanceof REXClassImpl)
			absorbEQ(lhs.asREXClassImpl(), rhs);
		else
			absorbEQRHS(lhs, rhs);

	}

	@Override
	public boolean absorbEQRHS(REXClassExpressionImpl lhs,
			REXClassExpressionImpl rhs) {
		// TODO Auto-generated method stub
		if(rhs instanceof REXClassImpl)
			absorbEQ(rhs.asREXClassImpl(), lhs);
		else
		{
			absorbLHS(lhs, rhs);
			absorbLHS(rhs, lhs);
		}
		return false;
	}

	private void absorbEQ(REXClassImpl A, REXClassExpressionImpl C) {
		// TODO Auto-generated method stub
		Set<REXClassExpressionImpl> PAs = new HashSet<REXClassExpressionImpl>();
		REXClassExpressionImpl nC = factory.getREXObjectComplementOf(C); 
		PA(PAs, nC);
		REXClassImpl TA = Absorb(PAs);
		addToUnfoldableTBox(TA, factory.getREXApproximationName(TA));
		if(nC.isCompletelyAbsorbable())
		{
			addToUnfoldableTBox(A, C);
			addToUnfoldableTBox(TA, A);
		}
//		else if(A.unfoldableDefinition.size() > 0 || A.unfoldableSuperClasses.size() > 0)
		else if(!factory.testUniqueness(A))
		{
			addToUnfoldableTBox(A,C);
			addToUnfoldableTBox(TA, factory.getREXObjectUnionOf(nC, A));
		}
		else
		{
			A.unfoldableDefinition.add(C);
		}
	}

//	private void addToTBox(REXClassImpl A,
//			REXClassExpressionImpl exp) {
//		// TODO Auto-generated method stub
//		if(A == factory.top && exp instanceof REXObjectUnionOfImpl)
//			factory.globalConstraints.add((REXObjectUnionOfImpl) exp);
//		else
//			addToUnfoldableTBox(A, exp);
//	}

}
