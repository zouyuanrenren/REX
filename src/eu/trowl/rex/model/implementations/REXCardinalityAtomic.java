package eu.trowl.rex.model.implementations;

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
	public boolean specifiedBy(REXClassImpl cls) {
		// TODO Auto-generated method stub
		return filler.specifiedBy(cls);
	}

}
