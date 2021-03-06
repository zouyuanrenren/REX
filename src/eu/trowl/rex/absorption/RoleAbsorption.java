package eu.trowl.rex.absorption;

import eu.trowl.rex.factory.REXDataFactory;
import eu.trowl.rex.model.implementations.REXClassExpressionImpl;
import eu.trowl.rex.model.implementations.REXObjectAllValuesFromImpl;
import eu.trowl.rex.model.implementations.REXObjectSomeValuesFromImpl;
import eu.trowl.rex.model.implementations.REXObjectUnionOfImpl;

public class RoleAbsorption extends ConceptAbsorption {

	public RoleAbsorption(REXDataFactory factory) {
		super(factory);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean absorbLHS(REXClassExpressionImpl lhs, REXClassExpressionImpl rhs) {
		if(lhs instanceof REXObjectSomeValuesFromImpl)
		{
			REXObjectSomeValuesFromImpl some = (REXObjectSomeValuesFromImpl) lhs;
			return absorbLHS(some.getFiller(), factory.getREXObjectAllValuesFrom(factory.getREXInverseObjectProperty(some.getProperty()), rhs));
		}
		else
			return super.absorbLHS(lhs, rhs);
	}
	
	@Override
	public boolean absorbRHS(REXClassExpressionImpl lhscomp, REXClassExpressionImpl rhs) {
		if(rhs instanceof REXObjectAllValuesFromImpl)
		{			
			REXObjectAllValuesFromImpl all = (REXObjectAllValuesFromImpl) rhs;
			return absorbRHS(factory.getREXObjectAllValuesFrom(factory.getREXInverseObjectProperty(all.getProperty()), lhscomp), all.getFiller());
		}
		else
			return super.absorbRHS(lhscomp, rhs);
	}

	@Override
	protected void addToGeneralTBox(REXClassExpressionImpl lhsComp,
			REXClassExpressionImpl rhs) {
		// TODO Auto-generated method stub
		if(lhsComp instanceof REXObjectAllValuesFromImpl)
		{
			REXObjectAllValuesFromImpl all = (REXObjectAllValuesFromImpl) lhsComp;
			addToUnfoldableTBox(factory.top, factory.getREXObjectAllValuesFrom(factory.getREXInverseObjectProperty(all.getProperty()), rhs));
		}
		else
		factory.globalConstraints.add((REXObjectUnionOfImpl) factory.getREXObjectUnionOf(lhsComp,rhs));

	}

}
