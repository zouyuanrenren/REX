package eu.trowl.rex.tableau.alci.test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import eu.trowl.rex.tableau.alci.ALCIReasoner;

public class BlockingExample {
	
	public static void main(String[] args) throws IOException, OWLOntologyCreationException, CloneNotSupportedException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File file = new File("ontology/eu.trowl.rex.tableau.test/BlockingExample.owl");
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
		ALCIReasoner reasoner = new ALCIReasoner(manager, ontology);

		OWLClass Person = manager.getOWLDataFactory().getOWLClass(IRI.create("http://www.abdn.ac.uk/cs3019/blockingExample.owl#Person"));
		if(reasoner.isSatisfiable(Person))
		System.out.println("The concept is satisfiable!");
		else
			System.out.println("The concept is unsatisfiable!");
	}

}
