package eu.trowl.rex.model.implementations;

import java.util.Set;

import eu.trowl.rex.factory.REXDataFactory;

public class REXCardinalityAtomic extends REXClassExpressionImpl {

	public REXClassExpressionImpl filler;
	public REXCardinalityAtomic(REXClassExpressionImpl filler, int cardinality) {
		// TODO Auto-generated constructor stub
		this.filler = filler;
		cardin = cardinality;
	}
	@Override
	public void LHS() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void RHS() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public REXClassExpressionImpl testComplement() {
		// TODO Auto-generated method stub
		return complement;
	}
	@Override
	public boolean isDefinedBy(REXClassImpl cls) {
		// TODO Auto-generated method stub
		return filler.isDefinedBy(cls);
	}
	@Override
	public boolean isPartialAbsorbable() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isCompletelyAbsorbable() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void addToPatialAbsorbable(Set<REXClassExpressionImpl> pas) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addToNotCompletelyAbsorbable(Set<REXClassExpressionImpl> nCA) {
		// TODO Auto-generated method stub
		nCA.add(this);
	}
	@Override
	public String toString() {
		return filler.toString()+"_>="+cardin;
	}
	@Override
	public REXClassExpressionImpl getComplement(REXDataFactory rexDataFactory) {
		// TODO Auto-generated method stub
		System.out.println("Cannot get complement of a cardinality atomic");
		return null;
	}

}
