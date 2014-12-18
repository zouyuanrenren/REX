package eu.trowl.rex.absorption.test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import eu.trowl.rex.factory.REXDataFactory;
import eu.trowl.rex.factory.REXOntologyBuilder;
import eu.trowl.rex.model.implementations.REXClassExpressionImpl;
import eu.trowl.rex.model.implementations.REXClassImpl;

public class AbsorptionTest {

	public static void main(String[] args) throws OWLOntologyCreationException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File file = new File("ontology/eu.trowl.rex.absorption.test/wine.owl");
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
		REXOntologyBuilder builder = new REXOntologyBuilder();
		builder.buildOntology(ontology);
		REXDataFactory factory = builder.getFactory();
		int unfoldableSize = 0;
		int generalSize = 0;
		System.out.println("===========Unfoldable TBox===========");
		for(REXClassImpl exp:factory.concepts.values())
		{
			for(REXClassExpressionImpl sup:exp.unfoldableSuperClasses)
				System.out.println(exp+" subClassOf "+sup);
			unfoldableSize+= exp.unfoldableSuperClasses.size();
			for(REXClassExpressionImpl eq:exp.unfoldableDefinition)
				System.out.println(exp+" equivalentClassOf "+eq);
			unfoldableSize += exp.unfoldableDefinition.size();
		}
		for(REXClassImpl exp:factory.freshConcepts)
		{
			for(REXClassExpressionImpl sup:exp.unfoldableSuperClasses)
				System.out.println(exp+" subClassOf "+sup);
			unfoldableSize+= exp.unfoldableSuperClasses.size();
			for(REXClassExpressionImpl eq:exp.unfoldableDefinition)
				System.out.println(exp+" equivalentClassOf "+eq);
			unfoldableSize += exp.unfoldableDefinition.size();
		}
		for(REXClassExpressionImpl exp:factory.binaryUnfoldableSuperClass.keySet())
		{
			for(REXClassExpressionImpl sup:factory.binaryUnfoldableSuperClass.get(exp))
				System.out.println(exp+" subClassOf "+sup);
			unfoldableSize += factory.binaryUnfoldableSuperClass.get(exp).size();
		}
		System.out.println("=============General TBox=============");
		for(REXClassExpressionImpl exp:factory.globalConstraints)
			System.out.println("Top subclassOf "+exp);
		generalSize = factory.globalConstraints.size();
		
		System.out.println("======================================");
		System.out.println("Number of total axioms: "+(unfoldableSize+generalSize));
		System.out.println("Number of unfodalbe axioms: "+unfoldableSize);
		System.out.println("Number of general axioms: "+generalSize);
		
	}

}
