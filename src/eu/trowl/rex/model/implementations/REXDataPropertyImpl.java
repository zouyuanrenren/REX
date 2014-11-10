package eu.trowl.rex.model.implementations;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataProperty;

import eu.trowl.rex.model.interfaces.REXDataProperty;

public class REXDataPropertyImpl extends REXObjectPropertyImpl
		implements REXDataProperty {

	public REXDataPropertyImpl(OWLDataProperty prop) {
		// TODO Auto-generated constructor stub
		this.iri = prop.getIRI();
		this.superProperties.add(this);
	}

	@Override
	public IRI getIRI() {
		// TODO Auto-generated method stub
		return null;
	}

}
