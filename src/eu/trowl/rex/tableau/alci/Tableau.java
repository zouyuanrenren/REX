package eu.trowl.rex.tableau.alci;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class Tableau {
	Set<OWLOntology> ontologies;
	OWLDataFactory factory;
	ArrayList<OWLIndividual> nodes = new ArrayList<OWLIndividual>();
	HashSet<OWLIndividual> blocked = new HashSet<OWLIndividual>();
	
	HashMap<OWLIndividual, HashSet<OWLClassExpression>> nodeLabels = new HashMap<OWLIndividual, HashSet<OWLClassExpression>>();
	HashMap<OWLIndividual, HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>>> edges = new HashMap<OWLIndividual, HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>>>();
	HashSet<OWLClassExpression> globalConstraints = new HashSet<OWLClassExpression>();
	
	public Tableau()
	{
		
	}
	
	public Tableau(Set<OWLOntology> ontologies, OWLDataFactory factory)
	{
		this.ontologies = ontologies;
		this.factory = factory;
		for(OWLOntology ontology:ontologies)
			for(OWLLogicalAxiom axiom:ontology.getLogicalAxioms())
				if(axiom instanceof OWLEquivalentClassesAxiom)
				{
					OWLEquivalentClassesAxiom eq = (OWLEquivalentClassesAxiom) axiom;
					for(OWLClassExpression exp1:eq.getClassExpressions())
						for(OWLClassExpression exp2:eq.getClassExpressionsMinus(exp1))
							globalConstraints.add(factory.getOWLObjectUnionOf(factory.getOWLObjectComplementOf(exp1),exp2));
							
				}
		for(OWLOntology ontology:ontologies)
		{
			for(OWLNamedIndividual indi:ontology.getIndividualsInSignature())
			{
				HashSet<OWLClassExpression> labels = nodeLabels.get(indi);
				if(labels == null)
				{
					labels = new HashSet<OWLClassExpression>();
					nodes.add(indi);
					labels.addAll(globalConstraints);
					nodeLabels.put(indi, labels);
					for(OWLOntology onto:ontologies)
						for(OWLClassAssertionAxiom ax:onto.getClassAssertionAxioms(indi))
//					for(OWLClassExpression exp:indi.getTypes(ontologies))
						labels.add(ax.getClassExpression().getNNF());
					HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>> relations = new HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>>();
					edges.put(indi, relations);
					for(OWLOntology onto:ontologies)
						for(OWLObjectPropertyAssertionAxiom ax:onto.getObjectPropertyAssertionAxioms(indi))
//					for(Entry<OWLObjectPropertyExpression, Set<OWLIndividual>> entry:indi.getObjectPropertyValues(onto).entrySet())
					{
						HashSet<OWLIndividual> objects = relations.get(ax.getProperty());
						if(objects == null)
						{
							objects = new HashSet<OWLIndividual>();
							relations.put(ax.getProperty(), objects);
						}
						objects.add(ax.getObject());
					}
					

				}
				
				
			}
		}
	}
	
	public boolean check() throws CloneNotSupportedException{
		boolean changed = true;
		while(changed)
		{
			int index = 0;
		OWLIndividual next;
		changed = false;
		do
		{
			next = nodes.get(index++);
			if(blocked.contains(next))
				continue;
				
			// SUB rule
			if(subRule(next))
				changed = true;
			
			// AND rule
			if(andRule(next))
				changed = true;

			// FORALL rule
			if(forallRule(next))
				changed = true;
			
			// INV rule
			if(invRule(next))
				changed = true;
			
			// detect clash
			if(clash(next))
			{
				return false;				
			}
			
			// EXISTS rule
			if(existsRule(next))
				changed = true;
		
			// block offspring nodes
			if(!(next instanceof OWLNamedIndividual))
				checkBlock(next, next);
						
			
		}
		while(index<nodes.size());
		
		}
		
		// OR rule
		
		return orRule();
	}

	private boolean invRule(OWLIndividual node) {
		// TODO Auto-generated method stub
		boolean changed = false;
		HashMap<OWLIndividual, HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>>> newedges = new HashMap<OWLIndividual, HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>>>();
		if(edges.containsKey(node))
			for(OWLObjectPropertyExpression edge:edges.get(node).keySet())
				for(OWLOntology onto:ontologies)
					for(OWLInverseObjectPropertiesAxiom ax:onto.getInverseObjectPropertyAxioms(edge))
						for(OWLObjectPropertyExpression inverse:ax.getPropertiesMinus(edge))
//				for(OWLObjectPropertyExpression inverse:edge.getInverses(ontologies))
					for(OWLIndividual object:edges.get(node).get(edge))
						if(!edges.containsKey(object) || !edges.get(object).containsKey(inverse) || !edges.get(object).get(inverse).contains(node))
						{
							HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>> newedge = newedges.get(object);
							if(newedge == null)
							{
								newedge = new HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>>();
								newedges.put(object, newedge);
							}
							HashSet<OWLIndividual> objects = newedge.get(inverse);
							if(objects == null)
							{
								objects = new HashSet<OWLIndividual>();
								newedge.put(inverse, objects);
							}
							if(!objects.contains(node))
							{
								changed = true;
								objects.add(node);
							}
						}
		for(OWLIndividual subject:newedges.keySet())
		{
			HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>> edge = edges.get(subject);
			if(edge == null)
			{
				edge = new HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>>();
				edges.put(subject, edge);
			}
			for(OWLObjectPropertyExpression relation:newedges.get(subject).keySet())
			{
				HashSet<OWLIndividual> objects = edge.get(relation);
				if(objects == null)
				{
					objects = new HashSet<OWLIndividual>();
					edge.put(relation, objects);
				}
				objects.addAll(newedges.get(subject).get(relation));
			}
		}
		return changed;
	}

	// check offspring nodes for blocking
	void checkBlock(OWLIndividual blockingNode, OWLIndividual blockedParent){
		if(edges.containsKey(blockedParent))
			for(OWLObjectPropertyExpression role:edges.get(blockedParent).keySet())
				for(OWLIndividual child:edges.get(blockedParent).get(role))
					if(!blocked.contains(child) && nodeLabels.get(blockingNode).containsAll(nodeLabels.get(child)))
						block(child);
					else checkBlock(blockingNode, child);
	}
	
	// blocking offspring nodes
	void block(OWLIndividual node)
	{
		blocked.add(node);
		for(Entry<OWLObjectPropertyExpression, HashSet<OWLIndividual>> objects:edges.get(node).entrySet())
			for(OWLIndividual child:objects.getValue())
				if(!blocked.add(node))
					block(child);
	}
	
	// OR rule
	
	boolean orRule() throws CloneNotSupportedException
	{
		for(OWLIndividual node:nodes)
		{
			HashSet<OWLClassExpression> labels = nodeLabels.get(node);
			for(OWLClassExpression exp:labels)
				if(exp instanceof OWLObjectUnionOf)
				{
					OWLObjectUnionOf union = (OWLObjectUnionOf) exp;
					boolean foundOperand = false;
					for(OWLClassExpression operand:union.getOperands())
						if(labels.contains(operand))
						{
							foundOperand = true;
							break;
						}
					if(!foundOperand)
					{
						for(OWLClassExpression operand:union.getOperands())
						{
							Tableau newTableau = this.clone();
							newTableau.add(node, operand);
							if(newTableau.check())
								return true;
						}
						return false;
					}
				}
		}
		return true;
	}
	
	void add(OWLIndividual indi, OWLClassExpression exp)
	{
		HashSet<OWLClassExpression> labels = nodeLabels.get(indi);
		if(labels == null)
		{
			labels = new HashSet<OWLClassExpression>();
			labels.addAll(globalConstraints);
			nodes.add(indi);
			nodeLabels.put(indi, labels);
			HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>> relations = new HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>>();
			edges.put(indi, relations);
		}
		labels.add(exp.getNNF());

			
	}
	
	// EXISTS rule
	
	boolean existsRule(OWLIndividual node)
	{
		boolean changed = false;
		for(OWLClassExpression exp:nodeLabels.get(node))
			if(exp instanceof OWLObjectSomeValuesFrom)
			{
				OWLObjectSomeValuesFrom some = (OWLObjectSomeValuesFrom) exp;
				OWLObjectPropertyExpression role = some.getProperty();
				OWLClassExpression filler = some.getFiller();
				HashSet<OWLIndividual> objects = edges.get(node).get(role);
				if(objects == null)
				{
					changed = true;
					OWLIndividual newindi = factory.getOWLAnonymousIndividual();
					nodes.add(newindi);
					HashSet<OWLClassExpression> labels = new HashSet<OWLClassExpression>();
					labels.add(filler.getNNF());
					labels.addAll(globalConstraints);
					nodeLabels.put(newindi, labels);
					HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>> relations = new HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>>();
					edges.put(newindi, relations);
					objects = new HashSet<OWLIndividual>();
					objects.add(newindi);
					edges.get(node).put(role.asOWLObjectProperty(), objects);
					
				}
			}
		return changed;
	}
	
	// FORALL rule	
	boolean forallRule(OWLIndividual node)
	{
		boolean changed = false;
		HashMap<OWLIndividual, HashSet<OWLClassExpression>> toadd = new HashMap<OWLIndividual, HashSet<OWLClassExpression>>();
		for(OWLClassExpression label:nodeLabels.get(node))
			if(label instanceof OWLObjectAllValuesFrom)
			{
				OWLObjectAllValuesFrom all = (OWLObjectAllValuesFrom) label;
				if(edges.get(node) !=null && edges.get(node).get(all.getProperty()) != null)
					for(OWLIndividual object:edges.get(node).get(all.getProperty()))
						if(!nodeLabels.get(object).contains(all.getFiller()))
						{
							changed = true;
							HashSet<OWLClassExpression> newlabels = toadd.get(object);
							if(newlabels == null)
							{
								newlabels = new HashSet<OWLClassExpression>();
								toadd.put(object, newlabels);
							}
							newlabels.add(all.getFiller());
						}
			}
		for(OWLIndividual object:toadd.keySet())
			nodeLabels.get(object).addAll(toadd.get(object));
		return changed;
	}
	
	// SUB rule
	boolean subRule(OWLIndividual node)
	{
		boolean changed = false;
		HashSet<OWLClassExpression> labels = nodeLabels.get(node);
		
		HashSet<OWLClassExpression> toAdd = new HashSet<OWLClassExpression>();
		for(OWLClassExpression exp:labels)
			if(exp  instanceof OWLClass)
			{
				OWLClass atomic = (OWLClass) exp;
				for(OWLOntology onto:ontologies)
					for(OWLSubClassOfAxiom ax:onto.getSubClassAxiomsForSubClass(atomic))
//				for(OWLClassExpression superClass:atomic.getSuperClasses(ontologies))
				{
					if(!labels.contains(ax.getSuperClass().getNNF()))
					{
						toAdd.add(ax.getSuperClass().getNNF());
						changed = true;
					}
				}
			}
		
		labels.addAll(toAdd);
		
		return changed;		
	}
	
	// detect clash
	boolean clash(OWLIndividual node)
	{
		HashSet<OWLClassExpression> labels = nodeLabels.get(node);
		if(labels.contains(factory.getOWLNothing()))
			return true;
		for(OWLClassExpression exp:labels)
			if(labels.contains(exp.getComplementNNF()))
				return true;
		return false;
	}
	
	// And rule
	boolean andRule(OWLIndividual node){
		HashSet<OWLClassExpression> toadd = new HashSet<OWLClassExpression>();
		HashSet<OWLClassExpression> labels = nodeLabels.get(node);
		for(OWLClassExpression label:labels)
			if(label instanceof OWLObjectIntersectionOf)
			{
				OWLObjectIntersectionOf and = (OWLObjectIntersectionOf) label;
				for(OWLClassExpression operand:and.getOperands())
					if(!labels.contains(operand))
						toadd.add(operand);
			}
		labels.addAll(toadd);
		if(toadd.size()>0)
			return true;
		return false;
	}

	@Override
	protected Tableau clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Tableau clone = new Tableau();
		clone.ontologies = this.ontologies;
		clone.factory = this.factory;
		clone.nodes = new ArrayList<OWLIndividual>(this.nodes);
		clone.blocked = new HashSet<OWLIndividual>(this.blocked);
		clone.nodeLabels = new HashMap<OWLIndividual, HashSet<OWLClassExpression>>();
		for(Entry<OWLIndividual, HashSet<OWLClassExpression>> entry:this.nodeLabels.entrySet())
		{
			HashSet<OWLClassExpression> labels = new HashSet<OWLClassExpression>(entry.getValue());
			clone.nodeLabels.put(entry.getKey(), labels);
		}
		for(Entry<OWLIndividual, HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>>> entry:this.edges.entrySet())
		{
			HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>> relations = new HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>>();
			clone.edges.put(entry.getKey(), relations);
			for(Entry<OWLObjectPropertyExpression, HashSet<OWLIndividual>> relationEntry:entry.getValue().entrySet())
			{
				HashSet<OWLIndividual> objects = new HashSet<OWLIndividual>(relationEntry.getValue());
				relations.put(relationEntry.getKey(), objects);
			}
		}
		return clone;
	}

	
}
