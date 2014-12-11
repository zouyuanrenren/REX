package eu.trowl.rex.tableau.alci.test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import eu.trowl.rex.tableau.alci.ALCIReasoner;

public class DisjunctionExample {
	
	public static void main(String[] args) throws IOException, OWLOntologyCreationException, CloneNotSupportedException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File file = new File("ontology/eu.trowl.rex.tableau.test/SubsumptionCheckingExample.owl");
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
		ALCIReasoner reasoner = new ALCIReasoner(manager, ontology);
		
		OWLDataFactory factory = manager.getOWLDataFactory();

		OWLClass Confucian = factory.getOWLClass(IRI.create("http://www.abdn.ac.uk/cs3019/subsumptionCheckingExample.owl#Confucian"));
		OWLClass Person = factory.getOWLClass(IRI.create("http://www.abdn.ac.uk/cs3019/subsumptionCheckingExample.owl#Person"));
		OWLClass English = factory.getOWLClass(IRI.create("http://www.abdn.ac.uk/cs3019/subsumptionCheckingExample.owl#English"));
		OWLObjectIntersectionOf EnglishPerson = factory.getOWLObjectIntersectionOf(Person, English); 
		OWLSubClassOfAxiom axiom = factory.getOWLSubClassOfAxiom(Confucian, EnglishPerson);

		if(reasoner.isEntailed(axiom))
		System.out.println("The subsumption is entailed!");
		else
			System.out.println("The subsumption is not entailed!");
		
		
	}

}
