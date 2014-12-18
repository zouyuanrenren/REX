package eu.trowl.rex.absorption;

import eu.trowl.rex.factory.REXDataFactory;
import eu.trowl.rex.model.implementations.REXClassExpressionImpl;
import eu.trowl.rex.model.implementations.REXClassImpl;
import eu.trowl.rex.model.implementations.REXObjectUnionOfImpl;
import eu.trowl.rex.model.implementations.REXSubClassOfImpl;

public abstract class AbsorptionVisitor {

	public REXDataFactory factory;
	
	public AbsorptionVisitor(REXDataFactory factory){
		this.factory = factory;
	}
	
	public abstract void absorb(REXSubClassOfImpl axiom);
	
	public abstract boolean absorbLHS(REXClassExpressionImpl lhs, REXClassExpressionImpl rhs);

	public abstract boolean absorbRHS(REXClassExpressionImpl lhs, REXClassExpressionImpl rhs);

	public abstract void absorbEQLHS(REXClassExpressionImpl lhs, REXClassExpressionImpl rhs);

	public abstract boolean absorbEQRHS(REXClassExpressionImpl lhs, REXClassExpressionImpl rhs);
	
	protected void addToUnfoldableTBox(REXClassImpl lhs,
			REXClassExpressionImpl rhs) {
		// TODO Auto-generated method stub
		if(lhs == factory.top && rhs instanceof REXObjectUnionOfImpl)
			factory.globalConstraints.add((REXObjectUnionOfImpl) rhs);
		else
			lhs.unfoldableSuperClasses.add(rhs);
	}

	protected void addToGeneralTBox(REXClassExpressionImpl lhsComp,
			REXClassExpressionImpl rhs) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl global = factory.getREXObjectUnionOf(lhsComp, rhs);
		if(global instanceof REXObjectUnionOfImpl)
		factory.globalConstraints.add((REXObjectUnionOfImpl) factory.getREXObjectUnionOf(lhsComp,rhs));
		else
			factory.top.unfoldableSuperClasses.add(global);

	}


}
