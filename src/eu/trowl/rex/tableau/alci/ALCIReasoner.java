package eu.trowl.rex.tableau.alci;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;


/** 
 * @author Yuan Ren
 * @version 2014-11-29: 
 * ALCI version;
 */
public class ALCIReasoner {

	protected Set<OWLOntology> importClosure;
	public final OWLOntologyManager manager;
	protected final OWLDataFactory factory;
	

	public ALCIReasoner(OWLOntologyManager manager, OWLOntology ontology) throws CloneNotSupportedException {
		this.manager = manager;
		this.factory = manager.getOWLDataFactory();
        this.importClosure = ontology.getImportsClosure();
	}
	
	public boolean isConsistent() throws CloneNotSupportedException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		Tableau tableau = new Tableau(importClosure, factory);
		return tableau.check();
	}

	public boolean isSatisfiable(OWLClassExpression arg0) throws CloneNotSupportedException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		
		Tableau tableau = new Tableau(importClosure, factory);
		
		tableau.add(factory.getOWLAnonymousIndividual(), arg0);

		return tableau.check();
	}
	
	public boolean isEntailed(OWLSubClassOfAxiom axiom) throws CloneNotSupportedException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Tableau tableau = new Tableau(importClosure, factory);
		tableau.add(factory.getOWLAnonymousIndividual(), factory.getOWLObjectIntersectionOf(axiom.getSubClass(),axiom.getSuperClass().getComplementNNF()));
		return !tableau.check();
	}
	
	public boolean isEntailed(OWLClassAssertionAxiom axiom) throws CloneNotSupportedException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Tableau tableau = new Tableau(importClosure, factory);
		tableau.add(axiom.getIndividual(), axiom.getClassExpression().getComplementNNF());
		return !tableau.check();
		
	}

	public boolean hasRelation(OWLNamedIndividual subject,
			OWLObjectPropertyExpression property, OWLNamedIndividual object) throws CloneNotSupportedException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		Tableau tableau = new Tableau(importClosure, factory);
		tableau.check();
		return tableau.hasRelation(subject, property, object);
//		if(tableau.edges.containsKey(subject) && tableau.edges.get(subject).containsKey(property) && tableau.edges.get(subject).get(property).contains(object))
//			return true;
//		return false;
	}

}
