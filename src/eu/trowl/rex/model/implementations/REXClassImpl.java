package eu.trowl.rex.model.implementations;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;

import eu.trowl.rex.model.interfaces.REXClass;

public class REXClassImpl extends REXClassExpressionImpl implements REXClass {

	protected IRI iri;
	public boolean original = true;
	
	public Set<REXClassImpl> equivalence = new HashSet<REXClassImpl>();
	
	public REXClassImpl(OWLClass clazz) {
		// TODO Auto-generated constructor stub
		iri = clazz.getIRI();
	}

	public REXClassImpl(IRI iri) {
		// TODO Auto-generated constructor stub
		this.iri = iri;
	}

	public REXClassImpl() {
		// TODO Auto-generated constructor stub
		this.iri = IRI.create("http://trowl.eu/APPROXC");
	}

	@Override
	public IRI getIRI() {
		// TODO Auto-generated method stub
		return iri;
	}
	
	@Override
	public void LHS() {
		// TODO Auto-generated method stub
		originalLHS = true;
	}

	@Override
	public String toString() {
		return iri.getFragment();
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

}
