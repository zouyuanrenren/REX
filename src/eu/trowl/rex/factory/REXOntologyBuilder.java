package eu.trowl.rex.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import eu.trowl.rex.model.implementations.REXObjectPropertyExpressionImpl;
import eu.trowl.rex.util.REXReasonerConfiguration;

public class REXOntologyBuilder {
	REXDataFactory rex_factory;
	public ArrayList<REXObjectPropertyExpressionImpl> roles;
	public int cNum;
	
	public REXOntologyBuilder() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		rex_factory = new REXDataFactory();
	}

	public void buildOntology(OWLOntology onto) {
		// TODO Auto-generated method stub
		rex_factory.onto = onto;
		cNum = onto.getClassesInSignature().size();
		if(!onto.getClassesInSignature().contains(onto.getOWLOntologyManager().getOWLDataFactory().getOWLThing()))
			cNum++;
		if(!onto.getClassesInSignature().contains(onto.getOWLOntologyManager().getOWLDataFactory().getOWLNothing()))
			cNum++;
		rex_factory.initialise(onto.getOWLOntologyManager().getOWLDataFactory());
		if(rex_factory.concepts.size() > REXReasonerConfiguration.largeTThreshold)
			rex_factory.smallT = false;
		if(rex_factory.individuals.size() > REXReasonerConfiguration.largeAThreshold)
			rex_factory.smallA = false;
		for(OWLOntology ontology:onto.getImportsClosure())
		for(OWLLogicalAxiom axiom:ontology.getLogicalAxioms())
		{
			rex_factory.initialiseAxiom(axiom);
		}
		roles = new ArrayList<REXObjectPropertyExpressionImpl>(rex_factory.roles.values());
		roles.addAll(rex_factory.rolechainNames.values());

	}

	public REXDataFactory getFactory() {
		// TODO Auto-generated method stub
		return rex_factory;
	}
	
}
