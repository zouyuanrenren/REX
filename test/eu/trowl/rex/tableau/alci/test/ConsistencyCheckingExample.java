package eu.trowl.rex.tableau.alci.test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import eu.trowl.rex.tableau.alci.ALCIReasoner;

public class ConsistencyCheckingExample {
	
	public static void main(String[] args) throws IOException, OWLOntologyCreationException, CloneNotSupportedException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File file = new File("ontology/eu.trowl.rex.tableau.test/ConsistencyCheckingExample2.owl");
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
		ALCIReasoner reasoner = new ALCIReasoner(manager, ontology);
		
		if(reasoner.isConsistent())
		System.out.println("The ontology is consistent!");
		else
			System.out.println("The ontology is inconsistent!");
		
		
	}

}
