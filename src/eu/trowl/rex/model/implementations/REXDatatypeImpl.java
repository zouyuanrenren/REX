package eu.trowl.rex.model.implementations;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDatatype;

import eu.trowl.rex.model.interfaces.REXDataRange;

public class REXDatatypeImpl extends REXClassImpl implements REXDataRange {

	public REXDatatypeImpl(OWLDatatype datatype) {
		// TODO Auto-generated constructor stub
		this.iri = datatype.getIRI();
	}

	public REXDatatypeImpl() {
		// TODO Auto-generated constructor stub
		this.iri = IRI.create("http://trowl.eu/APPROXD");
	}

}
