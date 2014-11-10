package eu.trowl.rex.model.implementations;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;

import eu.trowl.rex.model.interfaces.REXLiteral;

public class REXLiteralImpl extends REXIndividualImpl implements REXLiteral {

	public REXLiteralImpl(OWLLiteral literal) {
		// TODO Auto-generated constructor stub
//		iri = IRI.create(literal.getLiteral());
		iri = IRI.create("http://trowl.eu/APPROXR");
	}


}
