package eu.trowl.rex.tableau.alci;

import java.lang.reflect.InvocationTargetException;
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

import eu.trowl.rex.factory.REXDataFactory;
import eu.trowl.rex.model.implementations.REXClassExpressionImpl;
import eu.trowl.rex.model.implementations.REXClassImpl;
import eu.trowl.rex.model.implementations.REXIndividualImpl;
import eu.trowl.rex.model.implementations.REXObjectAllValuesFromImpl;
import eu.trowl.rex.model.implementations.REXObjectIntersectionOfImpl;
import eu.trowl.rex.model.implementations.REXObjectPropertyExpressionImpl;
import eu.trowl.rex.model.implementations.REXObjectSomeValuesFromImpl;
import eu.trowl.rex.model.implementations.REXObjectUnionOfImpl;
import eu.trowl.rex.model.interfaces.REXObjectSomeValuesFrom;

public class Tableau {
//	Set<OWLOntology> ontologies;
	OWLDataFactory factory;
	REXDataFactory rFactory;
	ArrayList<REXIndividualImpl> nodes = new ArrayList<REXIndividualImpl>();
//	HashSet<REXIndividualImpl> blocked = new HashSet<REXIndividualImpl>();
	
//	HashMap<OWLIndividual, HashSet<OWLClassExpression>> nodeLabels = new HashMap<OWLIndividual, HashSet<OWLClassExpression>>();
//	HashMap<OWLIndividual, HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>>> edges = new HashMap<OWLIndividual, HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>>>();
	HashSet<REXClassExpressionImpl> globalConstraints = new HashSet<REXClassExpressionImpl>();
	
	public Tableau() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		rFactory = new REXDataFactory();
	}
	
	public Tableau(Set<OWLOntology> ontologies, OWLDataFactory factory) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		rFactory = new REXDataFactory();
//		this.ontologies = ontologies;
		this.factory = factory;
		rFactory.initialise(factory);
		for(OWLOntology ontology:ontologies)
			for(OWLLogicalAxiom axiom:ontology.getLogicalAxioms())
			{
				rFactory.initialiseAxiom(axiom);
				if(axiom instanceof OWLEquivalentClassesAxiom)
				{
					OWLEquivalentClassesAxiom eq = (OWLEquivalentClassesAxiom) axiom;
					for(OWLClassExpression exp1:eq.getClassExpressions())
						for(OWLClassExpression exp2:eq.getClassExpressionsMinus(exp1))
							globalConstraints.add(rFactory.getREXClassExpression(factory.getOWLObjectUnionOf(factory.getOWLObjectComplementOf(exp1),exp2)));
							
				}
			}
		for(OWLOntology ontology:ontologies)
		{
			for(OWLNamedIndividual indi:ontology.getIndividualsInSignature())
			{
				REXIndividualImpl node = rFactory.getREXIndividual(indi);
				if(!nodes.contains(node))
				{
					nodes.add(node);
					node.superClasses.addAll(globalConstraints);
//					for(OWLOntology onto:ontologies)
//					{
//						for(OWLClassAssertionAxiom ax:onto.getClassAssertionAxioms(indi))
//							node.superClasses.add(rFactory.getREXClassExpression(ax.getClassExpression().getNNF()));
//						for(OWLObjectPropertyAssertionAxiom ax:onto.getObjectPropertyAssertionAxioms(indi))
//						{
//							REXObjectPropertyExpressionImpl role = rFactory.getREXObjectPropertyExpression(ax.getProperty());
//							Set<REXClassExpressionImpl> objs = node.implications.get(role);
//							if(objs == null)
//							{
//								objs = new HashSet<REXClassExpressionImpl>();
//								node.implications.put(role, objs);
//							}
//							objs.add(rFactory.getREXIndividual(ax.getObject()));
//						}
//					}
					for(REXClassExpressionImpl sup:node.getOriginalSuperClasses())
						if(sup instanceof REXObjectSomeValuesFromImpl && ((REXObjectSomeValuesFromImpl)sup).getFiller() instanceof REXIndividualImpl)
						{
							REXObjectSomeValuesFromImpl some = (REXObjectSomeValuesFromImpl) sup;
							Set<REXClassExpressionImpl> objs = node.implications.get(some.getProperty());
							if(objs == null)
							{
								objs = new HashSet<REXClassExpressionImpl>();
								node.implications.put(some.getProperty(), objs);
							}
							objs.add(some.getFiller());
						}
						else
							node.superClasses.add(sup);
				}				
			}
		}
	}
	
	public boolean check() throws CloneNotSupportedException{
		boolean changed = true;
		while(changed)
		{
			int index = 0;
		REXIndividualImpl next;
		changed = false;
		do
		{
			next = nodes.get(index++);
			if(next.blockedBy != null)
//			if(blocked.contains(next))
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
			if(changed && !(next instanceof OWLNamedIndividual))
				checkBlock(next, next, new HashSet<REXIndividualImpl>());
						
			
		}
		while(index<nodes.size());
		
		}
		
		// OR rule
		
		return orRule();
	}

	private boolean invRule(REXIndividualImpl node) {
		// TODO Auto-generated method stub
		boolean changed = false;
		HashMap<REXIndividualImpl, HashMap<REXObjectPropertyExpressionImpl, HashSet<REXIndividualImpl>>> newedges = new HashMap<REXIndividualImpl, HashMap<REXObjectPropertyExpressionImpl, HashSet<REXIndividualImpl>>>();
//		HashSet<REXObjectPropertyExpressionImpl> inverses = new HashSet<REXObjectPropertyExpressionImpl>();
//		if(edges.containsKey(node))
			for(REXObjectPropertyExpressionImpl prop:node.implications.keySet())
//				for(OWLOntology onto:ontologies)
//					for(OWLInverseObjectPropertiesAxiom ax:onto.getInverseObjectPropertyAxioms(edge))
						for(REXObjectPropertyExpressionImpl superrole:prop.getSuperRoles())
							if(superrole.getInversePropertyExpression() != null)
//								inverses.add(superrole.getInversePropertyExpression());
//				for(OWLObjectPropertyExpression inverse:edge.getInverses(ontologies))
//			for(REXObjectPropertyExpressionImpl prop:node.implications.keySet())
							{
								REXObjectPropertyExpressionImpl inverse = superrole.getInversePropertyExpression();
					for(REXClassExpressionImpl object:node.implications.get(prop))
						if(object instanceof REXIndividualImpl)
//							for(REXObjectPropertyExpressionImpl inverse:inverses)
						if(!object.implications.containsKey(inverse) || !object.implications.get(inverse).contains(node))
						{
							HashMap<REXObjectPropertyExpressionImpl, HashSet<REXIndividualImpl>> newedge = newedges.get(object);
							if(newedge == null)
							{
								newedge = new HashMap<REXObjectPropertyExpressionImpl, HashSet<REXIndividualImpl>>();
								newedges.put((REXIndividualImpl) object, newedge);
							}
							HashSet<REXIndividualImpl> objects = newedge.get(inverse);
							if(objects == null)
							{
								objects = new HashSet<REXIndividualImpl>();
								newedge.put(inverse, objects);
							}
							if(!objects.contains(node))
							{
								changed = true;
								objects.add(node);
							}
						}
	}
		for(REXIndividualImpl subject:newedges.keySet())
		{
			for(REXObjectPropertyExpressionImpl prop:newedges.get(subject).keySet())
			{
				Set<REXClassExpressionImpl> objs = subject.implications.get(prop);
				if(objs == null)
				{
					objs = new HashSet<REXClassExpressionImpl>();
					subject.implications.put(prop, objs);
				}
				objs.addAll(newedges.get(subject).get(prop));
			}
		
//			HashMap<REXObjectPropertyExpressionImpl, HashSet<REXIndividualImpl>> edge = edges.get(REXIndividualImpl);
//			if(edge == null)
//			{
//				edge = new HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>>();
//				edges.put(subject, edge);
//			}
//			for(OWLObjectPropertyExpression relation:newedges.get(subject).keySet())
//			{
//				HashSet<OWLIndividual> objects = edge.get(relation);
//				if(objects == null)
//				{
//					objects = new HashSet<OWLIndividual>();
//					edge.put(relation, objects);
//				}
//				objects.addAll(newedges.get(subject).get(relation));
//			}
		}
		return changed;
	}

	// check offspring nodes for blocking
	void checkBlock(REXIndividualImpl blockingNode, REXIndividualImpl blockedParent, HashSet<REXIndividualImpl> checked){
		for(REXObjectPropertyExpressionImpl role:blockedParent.implications.keySet())
			for(REXClassExpressionImpl child:blockedParent.implications.get(role))
				if(child instanceof REXIndividualImpl && !checked.contains(child) && ((REXIndividualImpl)child).blockedBy == null)
				{
					checked.add((REXIndividualImpl) child);
					if(blockingNode.superClasses.containsAll(child.superClasses))
						block(blockingNode, (REXIndividualImpl) child);
					else checkBlock(blockingNode, (REXIndividualImpl) child, checked);
				}
//		if(edges.containsKey(blockedParent))
//			for(OWLObjectPropertyExpression role:edges.get(blockedParent).keySet())
//				for(OWLIndividual child:edges.get(blockedParent).get(role))
//					if(!blocked.contains(child) && nodeLabels.get(blockingNode).containsAll(nodeLabels.get(child)))
//						block(child);
//					else checkBlock(blockingNode, child);
	}
	
	// blocking offspring nodes
	void block(REXIndividualImpl blockingNode, REXIndividualImpl node)
	{
//		blocked.add(node);
		node.blockedBy = blockingNode;
		for(REXObjectPropertyExpressionImpl prop:node.implications.keySet())
			for(REXClassExpressionImpl obj:node.implications.get(prop))
				if(obj instanceof REXIndividualImpl)
					if(((REXIndividualImpl) obj).blockedBy == null)
						((REXIndividualImpl) obj).blockedBy = blockingNode;
//		for(Entry<OWLObjectPropertyExpression, HashSet<OWLIndividual>> objects:edges.get(node).entrySet())
//			for(OWLIndividual child:objects.getValue())
//				if(!blocked.add(node))
//					block(child);
	}
	
	// OR rule
	
	boolean orRule() throws CloneNotSupportedException
	{
		for(REXIndividualImpl node:nodes)
		{
			for(REXClassExpressionImpl exp:node.superClasses)
			{
				if(exp instanceof REXObjectUnionOfImpl)
				{
					REXObjectUnionOfImpl union = (REXObjectUnionOfImpl) exp;
					boolean foundOperand = false;
					for(REXClassExpressionImpl operand:union.getEntities())
						if(node.superClasses.contains(operand))
						{
							foundOperand = true;
							break;
						}
					if(!foundOperand)
					{
						for(REXClassExpressionImpl operand:union.getEntities())
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
		}
//		for(OWLIndividual node:nodes)
//		{
//			HashSet<OWLClassExpression> labels = nodeLabels.get(node);
//			for(OWLClassExpression exp:labels)
//				if(exp instanceof OWLObjectUnionOf)
//				{
//					OWLObjectUnionOf union = (OWLObjectUnionOf) exp;
//					boolean foundOperand = false;
//					for(OWLClassExpression operand:union.getOperands())
//						if(labels.contains(operand))
//						{
//							foundOperand = true;
//							break;
//						}
//					if(!foundOperand)
//					{
//						for(OWLClassExpression operand:union.getOperands())
//						{
//							Tableau newTableau = this.clone();
//							newTableau.add(node, operand);
//							if(newTableau.check())
//								return true;
//						}
//						return false;
//					}
//				}
//		}
		return true;
	}
	
	public void add(OWLIndividual individual, OWLClassExpression clazz) {
		// TODO Auto-generated method stub
		add(rFactory.getREXIndividual(individual), rFactory.getREXClassExpression(clazz));
	}

	void add(REXIndividualImpl indi, REXClassExpressionImpl exp)
	{
		if(!nodes.contains(indi) || !indi.superClasses.contains(exp))
		{
			nodes.add(indi);
			indi.superClasses.add(exp);
		}
//		HashSet<OWLClassExpression> labels = nodeLabels.get(indi);
//		if(labels == null)
//		{
//			labels = new HashSet<OWLClassExpression>();
//			labels.addAll(globalConstraints);
//			nodes.add(indi);
//			nodeLabels.put(indi, labels);
//			HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>> relations = new HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>>();
//			edges.put(indi, relations);
//		}
//		labels.add(exp.getNNF());

			
	}
	
	// EXISTS rule
	
	boolean existsRule(REXIndividualImpl node)
	{
		boolean changed = false;
		for(REXClassExpressionImpl exp:node.superClasses)
			if(exp instanceof REXObjectSomeValuesFromImpl)
			{
				REXObjectSomeValuesFromImpl some = (REXObjectSomeValuesFromImpl) exp;
				if(!node.implications.containsKey(some.getProperty()))
				{
					changed = true;
				}
				else
				{
					boolean foundObject = false;
					for(REXClassExpressionImpl obj:node.implications.get(some.getProperty()))
						if(obj instanceof REXIndividualImpl)
						{
							foundObject = true;
							break;
						}
					if(!foundObject)
						changed = true;
				}
				if(changed)
				{
					REXIndividualImpl newindi = new REXIndividualImpl();
					nodes.add(newindi);
					newindi.superClasses.add(some.getFiller());
					newindi.superClasses.addAll(globalConstraints);
					HashSet<REXClassExpressionImpl> objs = new HashSet<REXClassExpressionImpl>();
					objs.add(newindi);
					node.implications.put(some.getProperty(), objs);
				}
				
			}
		
//		for(OWLClassExpression exp:nodeLabels.get(node))
//			if(exp instanceof OWLObjectSomeValuesFrom)
//			{
//				OWLObjectSomeValuesFrom some = (OWLObjectSomeValuesFrom) exp;
//				OWLObjectPropertyExpression role = some.getProperty();
//				OWLClassExpression filler = some.getFiller();
//				HashSet<OWLIndividual> objects = edges.get(node).get(role);
//				if(objects == null)
//				{
//					changed = true;
//					OWLIndividual newindi = factory.getOWLAnonymousIndividual();
//					nodes.add(newindi);
//					HashSet<OWLClassExpression> labels = new HashSet<OWLClassExpression>();
//					labels.add(filler.getNNF());
//					labels.addAll(globalConstraints);
//					nodeLabels.put(newindi, labels);
//					HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>> relations = new HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>>();
//					edges.put(newindi, relations);
//					objects = new HashSet<OWLIndividual>();
//					objects.add(newindi);
//					edges.get(node).put(role.asOWLObjectProperty(), objects);
//					
//				}
//			}
		return changed;
	}
	
	// FORALL rule	
	boolean forallRule(REXIndividualImpl node)
	{
		boolean changed = false;
		HashMap<REXClassExpressionImpl, HashSet<REXClassExpressionImpl>> toadd = new HashMap<REXClassExpressionImpl, HashSet<REXClassExpressionImpl>>();
		for(REXClassExpressionImpl label:node.superClasses)
			if(label instanceof REXObjectAllValuesFromImpl)
			{
				REXObjectAllValuesFromImpl all = (REXObjectAllValuesFromImpl) label;
				if(node.implications.containsKey(all.getProperty()))
					for(REXClassExpressionImpl obj:node.implications.get(all.getProperty()))
						if(obj instanceof REXIndividualImpl && !obj.superClasses.contains(all.getFiller()))
						{
							HashSet<REXClassExpressionImpl> newlabels = toadd.get(obj);
							if(newlabels == null)
							{
								newlabels = new HashSet<REXClassExpressionImpl>();
								toadd.put(obj, newlabels);
							}
							newlabels.add(all.getFiller());
						}
//				if(edges.get(node) !=null && edges.get(node).get(all.getProperty()) != null)
//					for(OWLIndividual object:edges.get(node).get(all.getProperty()))
//						if(!nodeLabels.get(object).contains(all.getFiller()))
//						{
//							changed = true;
//							HashSet<OWLClassExpression> newlabels = toadd.get(object);
//							if(newlabels == null)
//							{
//								newlabels = new HashSet<OWLClassExpression>();
//								toadd.put(object, newlabels);
//							}
//							newlabels.add(all.getFiller());
//						}
			}
		for(REXClassExpressionImpl object:toadd.keySet())
			object.superClasses.addAll(toadd.get(object));
//			nodeLabels.get(object).addAll(toadd.get(object));
		return changed;
	}
	
	// SUB rule
	boolean subRule(REXIndividualImpl node)
	{
		boolean changed = false;
//		HashSet<OWLClassExpression> labels = nodeLabels.get(node);
		
		HashSet<REXClassExpressionImpl> toAdd = new HashSet<REXClassExpressionImpl>();
		for(REXClassExpressionImpl exp:node.superClasses)
			if(exp  instanceof REXClassImpl)
			{
//				OWLClass atomic = (OWLClass) exp;
//				for(OWLOntology onto:ontologies)
//					for(OWLSubClassOfAxiom ax:onto.getSubClassAxiomsForSubClass(atomic))
//				for(OWLClassExpression superClass:atomic.getSuperClasses(ontologies))
				for(REXClassExpressionImpl superClass:exp.getOriginalSuperClasses())
				{
					if(!node.superClasses.contains(superClass))
					{
						toAdd.add(superClass);
						changed = true;
					}
				}
			}
		
		node.superClasses.addAll(toAdd);
		
		return changed;		
	}
	
	// detect clash
	boolean clash(REXIndividualImpl node)
	{
		if(node.superClasses.contains(rFactory.bottom))
			return true;
		for(REXClassExpressionImpl exp:node.superClasses)
			if(node.superClasses.contains(exp.complement))
				return true;
		return false;
//		HashSet<OWLClassExpression> labels = nodeLabels.get(node);
//		if(labels.contains(factory.getOWLNothing()))
//			return true;
//		for(OWLClassExpression exp:labels)
//			if(labels.contains(exp.getComplementNNF()))
//				return true;
//		return false;
	}
	
	// And rule
	boolean andRule(REXIndividualImpl node){
		HashSet<REXClassExpressionImpl> toadd = new HashSet<REXClassExpressionImpl>();
//		HashSet<REXClassExpressionImpl> labels = nodeLabels.get(node);
		for(REXClassExpressionImpl label:node.superClasses)
			if(label instanceof REXObjectIntersectionOfImpl)
			{
				REXObjectIntersectionOfImpl and = (REXObjectIntersectionOfImpl) label;
				for(REXClassExpressionImpl operand:and.getEntities())
					if(!node.superClasses.contains(operand))
						toadd.add(operand);
			}
		node.superClasses.addAll(toadd);
		if(toadd.size()>0)
			return true;
		return false;
	}

	@Override
	protected Tableau clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Tableau clone = null;
		try {
			clone = new Tableau();
//			clone.ontologies = this.ontologies;
			clone.factory = this.factory;
			clone.nodes = new ArrayList<REXIndividualImpl>();
			HashMap<REXIndividualImpl, REXIndividualImpl> map = new HashMap<REXIndividualImpl, REXIndividualImpl>();
			for(REXIndividualImpl node:nodes)
			{
				REXIndividualImpl newnode = new REXIndividualImpl(node.getIRI());
				clone.nodes.add(newnode);
				map.put(node, newnode);
			}
			for(REXIndividualImpl node:nodes)
			{
				REXIndividualImpl newnode = map.get(node);
				for(REXClassExpressionImpl cls:node.superClasses)
				{
					if(cls instanceof REXIndividualImpl)
						newnode.superClasses.add(map.get(cls));
					else
						newnode.superClasses.add(cls);
				}
				for(REXObjectPropertyExpressionImpl role:node.implications.keySet())
				{
					Set<REXClassExpressionImpl> objs = newnode.implications.get(role);
					if(objs == null)
					{
						objs = new HashSet<REXClassExpressionImpl>();
						newnode.implications.put(role, objs);
					}
					for(REXClassExpressionImpl obj:node.implications.get(role))
						if(obj instanceof REXIndividualImpl)
							objs.add(map.get(obj));
						else
							objs.add(obj);
				}
			}
//			clone.blocked = new HashSet<OWLIndividual>(this.blocked);
//			clone.nodeLabels = new HashMap<OWLIndividual, HashSet<OWLClassExpression>>();
//			for(Entry<OWLIndividual, HashSet<OWLClassExpression>> entry:this.nodeLabels.entrySet())
//			{
//				HashSet<OWLClassExpression> labels = new HashSet<OWLClassExpression>(entry.getValue());
//				clone.nodeLabels.put(entry.getKey(), labels);
//			}
//			for(Entry<OWLIndividual, HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>>> entry:this.edges.entrySet())
//			{
//				HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>> relations = new HashMap<OWLObjectPropertyExpression, HashSet<OWLIndividual>>();
//				clone.edges.put(entry.getKey(), relations);
//				for(Entry<OWLObjectPropertyExpression, HashSet<OWLIndividual>> relationEntry:entry.getValue().entrySet())
//				{
//					HashSet<OWLIndividual> objects = new HashSet<OWLIndividual>(relationEntry.getValue());
//					relations.put(relationEntry.getKey(), objects);
//				}
//			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clone;
	}

	public boolean hasRelation(OWLNamedIndividual subject,
			OWLObjectPropertyExpression property, OWLNamedIndividual object) {
		// TODO Auto-generated method stub
		REXIndividualImpl sub = rFactory.getREXIndividual(subject);
		REXObjectPropertyExpressionImpl prop = rFactory.getREXObjectPropertyExpression(property);
		if(sub.implications.containsKey(prop) && sub.implications.get(prop).contains(rFactory.getREXIndividual(object)))
			return true;
		return false;
	}

	
}
