package eu.trowl.rex.tableau.alci.test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import eu.trowl.rex.tableau.alci.ALCIReasoner;

public class GCIExample {
	
	public static void main(String[] args) throws IOException, OWLOntologyCreationException, CloneNotSupportedException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File file = new File("ontology/eu.trowl.rex.tableau.test/GCIExample.owl");
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
		ALCIReasoner reasoner = new ALCIReasoner(manager, ontology);
		
		OWLDataFactory factory = manager.getOWLDataFactory();

		OWLNamedIndividual Bill = factory.getOWLNamedIndividual(IRI.create("http://www.abdn.ac.uk/cs3019/GCIExample.owl#Bill"));
		OWLClass Person = factory.getOWLClass(IRI.create("http://www.abdn.ac.uk/cs3019/GCIExample.owl#Person"));
		OWLClassAssertionAxiom axiom = factory.getOWLClassAssertionAxiom(Person, Bill);
		if(reasoner.isEntailed(axiom))
		System.out.println("The class asseretion is entailed!");
		else
			System.out.println("The class assertion is not entailed!");
	}

}
