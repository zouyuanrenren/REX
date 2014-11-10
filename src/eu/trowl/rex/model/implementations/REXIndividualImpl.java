package eu.trowl.rex.model.implementations;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import eu.trowl.rex.model.interfaces.REXIndividual;

public class REXIndividualImpl extends REXClassImpl implements REXIndividual {

	public Set<REXIndividualImpl> differentIndividuals = new HashSet<REXIndividualImpl>();
	
	public REXIndividualImpl(OWLIndividual indi) {
		// TODO Auto-generated constructor stub
		if(indi instanceof OWLNamedIndividual)
		iri = indi.asOWLNamedIndividual().getIRI();
	}
	
	public REXIndividualImpl(){
		iri = IRI.create("http://trowl.eu/APPROXI");
	}

}
