package eu.trowl.owl.rex.owlapi.tests;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;

import eu.trowl.rex.owlapi.REXReasoner;
import eu.trowl.rex.owlapi.REXReasonerFactory;
import eu.trowl.rex.util.Timer;

public class TBoxClassification implements Callable<String> {

	/**
	 * @param args
	 * @throws OWLOntologyCreationException 
	 * @throws OWLReasonerException 
	 * @throws OWLOntologyCreationException 
	 * @throws IOException 
	 */
	File filename;
	TBoxScript master;
	public TBoxClassification(File name){
		filename = name;
//		master = script;
	}
	public void classify() throws OWLOntologyCreationException, IOException{
		// TODO Auto-generated method stub


//		IRI physicalURI = IRI.create(filename);
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		Timer time = new Timer("Loading");
		time.start();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(filename);
		time.stop();
//		System.out.println(time);

		REXReasonerFactory relfactory = new REXReasonerFactory();
		Timer timer = new Timer("REL Materialisation");
		timer.start();
		REXReasoner reasoner = relfactory.createReasoner(ontology);
//		System.out.println("Retrieveing...");
			reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
			if(Thread.currentThread().isInterrupted())
				return;
			if(reasoner.isConsistent() != true)
			{
				timer.stop();
				System.out.println("Inconsistency "+timer.getTotal());
				return;
			}
		OWLDataFactory factory = manager.getOWLDataFactory();
		
		OWLClass thing = factory.getOWLThing();
		OWLClass nothing = factory.getOWLNothing();
		int num = 0;
		for(OWLClass cls:ontology.getClassesInSignature())
		{
			if(Thread.currentThread().isInterrupted())
				return;
			if(!cls.equals(thing)&&!cls.equals(nothing))
			{
			num+=reasoner.getSuperClasses(cls, true).getFlattened().size();
			num+=reasoner.getEquivalentClasses(cls).getSize();
			}
		}
//		System.out.println("Number of named concept subsumptions:"+num);
		System.out.print(num+" ");
		timer.stop();
//		System.out.println("Done.");
		System.out.println(timer.getTotal());
		
	}

	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		classify();
		return null;
	}

}
