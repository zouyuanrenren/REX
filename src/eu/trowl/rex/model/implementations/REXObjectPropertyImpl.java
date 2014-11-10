package eu.trowl.rex.model.implementations;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import eu.trowl.rex.model.interfaces.REXObjectProperty;

public class REXObjectPropertyImpl extends REXObjectPropertyExpressionImpl implements
		REXObjectProperty {

	IRI iri;
	
	REXInverseObjectPropertyImpl inverse = null;
	
	public REXObjectPropertyImpl(OWLObjectProperty prop) {
		// TODO Auto-generated constructor stub
		iri = prop.getIRI();
		superProperties.add(this);
	}

	public REXObjectPropertyImpl() {
		// TODO Auto-generated constructor stub
		iri = null;
		superProperties.add(this);
	}

	@Override
	public IRI getIRI() {
		// TODO Auto-generated method stub
		return iri;
	}
	
//	Set<REL2ObjectPropertyImpl> superProperties = new HashSet<REL2ObjectPropertyImpl>();
	
	@Override
	public String toString() {
		return iri.getFragment();
	}

	@Override
	public REXObjectPropertyExpressionImpl getInversePropertyExpression() {
		// TODO Auto-generated method stub
		return inverse;
	}
	
	public void setInverse(REXInverseObjectPropertyImpl inverse){
		this.inverse = inverse;
	}

	@Override
	public void LHS() {
		// TODO Auto-generated method stub
		this.onLHS = true;
//		this.getInversePropertyExpression().related = true;
	}
	
}
