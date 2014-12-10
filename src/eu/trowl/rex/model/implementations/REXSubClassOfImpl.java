package eu.trowl.rex.model.implementations;

import eu.trowl.rex.absorption.AbsorptionVisitor;
import eu.trowl.rex.model.interfaces.REXClassExpression;
import eu.trowl.rex.model.interfaces.REXSubClassOf;

public class REXSubClassOfImpl implements REXSubClassOf {

	public REXClassExpressionImpl lhs;
	public REXObjectPropertyExpressionImpl rhsRole = null;
	public REXClassExpressionImpl rhsFiller;
//	REL2ClassExpressionImpl rhs;
	
	public boolean subsumption = true;
	public boolean link = false;
//	public boolean RuleSome = false;
//	public AtomicBoolean LHSQueue = new AtomicBoolean(false);
//	public AtomicBoolean RHSFillerQueue = new AtomicBoolean(false);
	public boolean tested = false;
	
	public REXSubClassOfImpl(REXClassExpressionImpl lhs,
			REXClassExpressionImpl rhs) {
		// TODO Auto-generated constructor stub
		this.lhs = lhs;
		this.rhsFiller = rhs;
	}
	
	public REXSubClassOfImpl(REXClassExpressionImpl lhs, REXObjectPropertyExpressionImpl rhsRole, REXClassExpressionImpl rhsFiller){
		this.lhs = lhs;
		this.rhsRole = rhsRole;
		this.rhsFiller = rhsFiller;
	}

	@Override
	public REXClassExpression getLHSContext() {
		// TODO Auto-generated method stub
		return lhs;
	}

	@Override
	public REXClassExpression getRHSContext() {
		// TODO Auto-generated method stub
		if(rhsRole != null)
		return rhsFiller;
//		if(rhs instanceof REL2ObjectSomeValuesFromImpl)
//			return ((REL2ObjectSomeValuesFromImpl)rhs).getFiller();
		return null;
	}

	public void addToContext() {
		// TODO Auto-generated method stub
		if(this.tested)
			return;
		tested = true;
		REXClassExpressionImpl context = (REXClassExpressionImpl) getLHSContext();
//		if(!(context instanceof REL2DataRange))
			context.scheduleAdd(this);
		if(getRHSContext() != null)
		{
			context = (REXClassExpressionImpl) getRHSContext();
//			if(!(context instanceof REL2DataRange))
			context.scheduleAdd(this);
		}
	}


	@Override
	public String toString() {
		String str = lhs.toString();
		if(rhsRole == null)
			str+=" SubClassOf "+rhsFiller.toString();
		else
			str+=" SubClassOf (" +rhsRole.toString()+" SOME "+rhsFiller.toString()+")";
		return str;
	}

	@Override
	public void initialise() {
		// TODO Auto-generated method stub
		addToContext();
		lhs.LHS();
		if(rhsRole == null)
			lhs.addOriginalSuperClasses(rhsFiller);
		else
			if(rhsRole.somes.containsKey(rhsFiller))
				lhs.addOriginalSuperClasses(rhsRole.somes.get(rhsFiller));
			else
				System.out.println();
		
	}
	
	public void accept(AbsorptionVisitor visitor){
		visitor.absorb(this);
	}


	
	

}
