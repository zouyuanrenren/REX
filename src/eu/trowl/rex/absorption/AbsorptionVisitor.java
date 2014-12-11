package eu.trowl.rex.absorption;

import eu.trowl.rex.factory.REXDataFactory;
import eu.trowl.rex.model.implementations.REXClassExpressionImpl;
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
}
