package eu.trowl.rex.tableau.alci.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import eu.trowl.rex.tableau.alci.ALCIReasoner;

public class AssessmentTest {
	
	public static void main(String[] args) throws IOException, OWLOntologyCreationException, CloneNotSupportedException {
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File file = new File("ontology/eu.trowl.rex.tableau.test/bookstoreTest3.owl");
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
		ALCIReasoner reasoner = new ALCIReasoner(manager, ontology);
		
		OWLDataFactory factory = manager.getOWLDataFactory();

		OWLObjectProperty hasWritten = factory.getOWLObjectProperty(IRI.create("http://www.cs4021/bookstoreTest1.owl#hasWritten"));
		
		for(OWLNamedIndividual subject:ontology.getIndividualsInSignature())
			for(OWLNamedIndividual object:ontology.getIndividualsInSignature())
		{
			if(reasoner.hasRelation(subject, hasWritten, object))
				System.out.println(subject.getIRI().getFragment()+" hasWritten "+object.getIRI().getFragment());
		}
	}

}
