package eu.trowl.rex.factory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLRule;

import eu.trowl.rex.model.implementations.REXClassExpressionImpl;
import eu.trowl.rex.model.implementations.REXDataPropertyImpl;
import eu.trowl.rex.model.implementations.REXIndividualImpl;
import eu.trowl.rex.model.implementations.REXObjectIntersectionOfImpl;
import eu.trowl.rex.model.implementations.REXObjectPropertyExpressionImpl;
import eu.trowl.rex.model.implementations.REXObjectSomeValuesFromImpl;
import eu.trowl.rex.model.implementations.REXSubClassOfImpl;
import eu.trowl.rex.model.interfaces.REXObjectSomeValuesFrom;

public class OWL2DLAxiomVisitor implements OWLAxiomVisitor {

	REXDataFactory factory;
	REXOntologyBuilder builder;
	
	public OWL2DLAxiomVisitor(REXDataFactory factory) {
		// TODO Auto-generated constructor stub
		this.factory = factory;
	}

	@Override
	public void visit(OWLAnnotationAssertionAxiom ax) {
		// TODO Auto-generated method stub
		if(factory.debug)
			System.out.print("Ignore axiom: "+ax);
	}

	@Override
	public void visit(OWLSubAnnotationPropertyOfAxiom ax) {
		// TODO Auto-generated method stub
		if(factory.debug)
			System.out.print("Ignore axiom: "+ax);

	}

	@Override
	public void visit(OWLAnnotationPropertyDomainAxiom ax) {
		// TODO Auto-generated method stub
		if(factory.debug)
			System.out.print("Ignore axiom: "+ax);

	}

	@Override
	public void visit(OWLAnnotationPropertyRangeAxiom ax) {
		// TODO Auto-generated method stub
		if(factory.debug)
			System.out.print("Ignore axiom: "+ax);

	}

	@Override
	public void visit(OWLDeclarationAxiom ax) {
		// TODO Auto-generated method stub
		if(factory.debug)
			System.out.print("Ignore axiom: "+ax);

	}

	@Override
	public void visit(OWLSubClassOfAxiom ax) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		REXClassExpressionImpl lhs = factory.getREXClassExpression(ax.getSubClass());
		REXClassExpressionImpl rhs = factory.getREXClassExpression(ax.getSuperClass());
		
		initialiseREXSubClassOf(lhs,rhs);
		

		
//		lhs = getREL2ClassExpression(ax.getSuperClass().getComplementNNF());
//		rhs = getREL2ClassExpression(ax.getSubClass().getComplementNNF());
//		initialiseREL2SubClassOf(lhs,rhs);

	}

	private void initialiseREXSubClassOf(REXClassExpressionImpl lhs,
			REXClassExpressionImpl rhs) {
		// TODO Auto-generated method stub
		REXSubClassOfImpl newAxiom = factory.getREXSubClassOf(lhs,rhs);
		newAxiom.initialise();
		if(false)
		if(lhs.complement != null && lhs.complement instanceof REXObjectIntersectionOfImpl)
		{
			REXObjectIntersectionOfImpl and = (REXObjectIntersectionOfImpl) lhs.complement;
			for(REXClassExpressionImpl conjunct:and.getEntities())
				if(conjunct.complement != null)
				{
					factory.getREXSubClassOf(conjunct.complement, rhs);
					conjunct.complement.addOriginalSuperClasses(rhs);
				}
		}

	}

	@Override
	public void visit(OWLNegativeObjectPropertyAssertionAxiom ax) {
		// TODO Auto-generated method stub
		if(factory.debug)
			System.out.print("Unsupported axiom: "+ax);

	}

	@Override
	public void visit(OWLAsymmetricObjectPropertyAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl role = factory.getREXObjectPropertyExpression(ax.getProperty());
		REXObjectPropertyExpressionImpl irole = factory.getREXObjectPropertyExpression(ax.getProperty().getInverseProperty());
		role.disjoints.add(irole);
		irole.disjoints.add(role);

	}

	@Override
	public void visit(OWLReflexiveObjectPropertyAxiom ax) {
		// TODO Auto-generated method stub
		if(factory.debug)
			System.out.print("Unsupported axiom: "+ax);

	}

	@Override
	public void visit(OWLDisjointClassesAxiom ax) {
		// TODO Auto-generated method stub
		if(factory.smallT && false)
			for(OWLClassExpression cls1:ax.getClassExpressions())
				for(OWLClassExpression cls2:ax.getClassExpressionsMinus(cls1))
				{
					REXClassExpressionImpl lhs = factory.getREXClassExpression(cls1);
					REXClassExpressionImpl rhs = factory.getREXClassExpression(cls2.getComplementNNF());
					initialiseREXSubClassOf(lhs,rhs);
				}
		else
		for(OWLClassExpression cls1:ax.getClassExpressions())
			for(OWLClassExpression cls2:ax.getClassExpressionsMinus(cls1))
			{
				REXClassExpressionImpl lhs = factory.getREXObjectIntersectionOf(factory.getREXClassExpression(cls1),factory.getREXClassExpression(cls2));
				REXSubClassOfImpl newAxiom = factory.getREXSubClassOf(lhs, factory.bottom);
//				newAxiom.addToContext();
//				lhs.LHS();
//				lhs.addOriginalSuperClasses(factory.bottom);
				newAxiom.initialise();
			}

	}

	@Override
	public void visit(OWLDataPropertyDomainAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl role = factory.getREXDataPropertyExpression(ax.getProperty());
		REXClassExpressionImpl domain = factory.getREXClassExpression(ax.getDomain());
		REXClassExpressionImpl lhs = factory.getREXObjectSomeValuesFrom(role, factory.top);
		initialiseREXSubClassOf(lhs, domain);

	}

	@Override
	public void visit(OWLObjectPropertyDomainAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl role = factory.getREXObjectPropertyExpression(ax.getProperty());
		REXClassExpressionImpl domain = factory.getREXClassExpression(ax.getDomain());
		REXClassExpressionImpl lhs = factory.getREXObjectSomeValuesFrom(role, factory.top);
		initialiseREXSubClassOf(lhs, domain);

	}

	@Override
	public void visit(OWLEquivalentObjectPropertiesAxiom ax) {
		// TODO Auto-generated method stub
		List<REXObjectPropertyExpressionImpl> props = new ArrayList<REXObjectPropertyExpressionImpl>();
		for(OWLObjectPropertyExpression p:ax.getProperties())
			props.add(factory.getREXObjectPropertyExpression(p));
		for(REXObjectPropertyExpressionImpl subRole:props)
			for(REXObjectPropertyExpressionImpl superRole:props)
				if(subRole != superRole)
					subRole.addSuperRole(superRole);
	}

	@Override
	public void visit(OWLNegativeDataPropertyAssertionAxiom ax) {
		// TODO Auto-generated method stub
		if(factory.debug)
			System.out.print("Unsupported axiom: "+ax);

	}

	@Override
	public void visit(OWLDifferentIndividualsAxiom ax) {
		// TODO Auto-generated method stub
		for(OWLIndividual indi1:ax.getIndividuals())
			for(OWLIndividual indi2:ax.getIndividuals())
				if(indi1 != indi2)
			{
					REXIndividualImpl i1 = factory.getREXIndividual(indi1);
					REXIndividualImpl i2 = factory.getREXIndividual(indi2);
//				REL2ClassExpressionImpl lhs = getREL2ObjectIntersectionOf(i1,i2);
//				REL2SubClassOfImpl newAxiom = getREL2SubClassOf(lhs, bottom);
//				newAxiom.addToContext();
//				lhs.LHS();
//				lhs.originalSuperClasses.add(bottom);
					i1.differentIndividuals.add(i2);
					i2.differentIndividuals.add(i1);
			}

	}

	@Override
	public void visit(OWLDisjointDataPropertiesAxiom ax) {
		// TODO Auto-generated method stub
		if(factory.debug)
			System.out.print("Unsupported axiom: "+ax);

	}

	@Override
	public void visit(OWLDisjointObjectPropertiesAxiom ax) {
		// TODO Auto-generated method stub
		Set<REXObjectPropertyExpressionImpl> roles = new HashSet<REXObjectPropertyExpressionImpl>();
		for(OWLObjectPropertyExpression prop1:ax.getProperties())
		{
			REXObjectPropertyExpressionImpl role1 = factory.getREXObjectPropertyExpression(prop1);
			for(REXObjectPropertyExpressionImpl role2:roles)
				if(!role1.equals(role2))
				{
					role1.disjoints.add(role2);
					role2.disjoints.add(role1);
				}
			roles.add(role1);
		}

	}

	@Override
	public void visit(OWLObjectPropertyRangeAxiom ax) {
		// TODO Auto-generated method stub
//		OWLObjectSomeValuesFrom some = factory.factory.getOWLObjectSomeValuesFrom(ax.getProperty(), ax.getRange().getComplementNNF());
//		initialiseREL2SubClassOf(factory.getREL2ClassExpression(some), factory.bottom);
		REXClassExpressionImpl some = factory.getREXObjectSomeValuesFrom(ax.getProperty(),ax.getRange().getComplementNNF());
		initialiseREXSubClassOf(some,factory.bottom);
		if(factory.smallT && false)
		{
//		REL2ObjectPropertyExpressionImpl role = getREL2ObjectPropertyExpression(ax.getProperty());
//		REL2ClassExpressionImpl range = getREL2ClassExpression(ax.getRange());
		REXClassExpressionImpl rhs = factory.getREXObjectComplementOf(some);
		initialiseREXSubClassOf(factory.top, rhs);
		}
		some = factory.getREXObjectSomeValuesFrom(factory.getREXObjectPropertyExpression(ax.getProperty().getInverseProperty()), factory.top);
		initialiseREXSubClassOf(some,factory.getREXClassExpression(ax.getRange()));
//		some = factory.factory.getOWLObjectSomeValuesFrom(ax.getProperty().getInverseProperty(), factory.factory.getOWLThing());
//		initialiseREL2SubClassOf(factory.getREL2ClassExpression(some), factory.getREL2ClassExpression(ax.getRange()));
//		role.ranges.add(range);

	}

	@Override
	public void visit(OWLObjectPropertyAssertionAxiom ax) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl sub = factory.getREXIndividual(ax.getSubject());
		REXClassExpressionImpl obj = factory.getREXIndividual(ax.getObject());
		REXObjectPropertyExpressionImpl role = factory.getREXObjectPropertyExpression(ax.getProperty());
		REXClassExpressionImpl some = factory.getREXObjectSomeValuesFrom(role, obj);
		initialiseREXSubClassOf(sub,some);

	}

	@Override
	public void visit(OWLFunctionalObjectPropertyAxiom ax) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl lhs = factory.getREXObjectMinCardinality(2,factory.getREXObjectPropertyExpression(ax.getProperty()), factory.top);
		initialiseREXSubClassOf(lhs,factory.bottom);
		factory.getREXObjectPropertyExpression(ax.getProperty()).functional = true;

	}

	@Override
	public void visit(OWLSubObjectPropertyOfAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl subRole = factory.getREXObjectPropertyExpression(ax.getSubProperty());
		REXObjectPropertyExpressionImpl superRole = factory.getREXObjectPropertyExpression(ax.getSuperProperty());
		subRole.addSuperRole(superRole);

	}

	@Override
	public void visit(OWLDisjointUnionAxiom ax) {
		// TODO Auto-generated method stub
		if(factory.debug)
			System.out.print("Unsupported axiom: "+ax);

	}

	@Override
	public void visit(OWLSymmetricObjectPropertyAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl role = factory.getREXObjectPropertyExpression(ax.getProperty());
		REXObjectPropertyExpressionImpl irole = factory.getREXObjectPropertyExpression(ax.getProperty().getInverseProperty());
		role.addSuperRole(irole);
		irole.addSuperRole(role);

	}

	@Override
	public void visit(OWLDataPropertyRangeAxiom ax) {
		// TODO Auto-generated method stub
		factory.getREXDataPropertyExpression(ax.getProperty()).ranges.add(factory.getREXDataRange(ax.getRange()));

	}

	@Override
	public void visit(OWLFunctionalDataPropertyAxiom ax) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl lhs = factory.getREXObjectMinCardinality(2,factory.getREXDataPropertyExpression(ax.getProperty()), factory.top);
		if(false)
			initialiseREXSubClassOf(lhs,factory.bottom);
		factory.getREXDataPropertyExpression(ax.getProperty()).functional = true;

	}

	@Override
	public void visit(OWLEquivalentDataPropertiesAxiom ax) {
		// TODO Auto-generated method stub
		List<REXDataPropertyImpl> props = new ArrayList<REXDataPropertyImpl>();
		for(OWLDataPropertyExpression p:ax.getProperties())
			props.add(factory.getREXDataPropertyExpression(p));
		for(REXDataPropertyImpl subRole:props)
			for(REXDataPropertyImpl superRole:props)
				if(subRole != superRole)
					subRole.addSuperRole(superRole);

	}

	@Override
	public void visit(OWLClassAssertionAxiom ax) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl indi = factory.getREXIndividual(ax.getIndividual());
		REXClassExpressionImpl clazz = factory.getREXClassExpression(ax.getClassExpression());
		initialiseREXSubClassOf(indi,clazz);

	}

	@Override
	public void visit(OWLEquivalentClassesAxiom ax) {
		// TODO Auto-generated method stub
		List<REXClassExpressionImpl> clss = new ArrayList<REXClassExpressionImpl>();
		for(OWLClassExpression c:ax.getClassExpressions())
		{
			clss.add(factory.getREXClassExpression(c));
		}
		for(REXClassExpressionImpl lhs:clss)
		{
			for(REXClassExpressionImpl rhs:clss)
				if(lhs!=rhs)
				{
					initialiseREXSubClassOf(lhs, rhs);
				}
			lhs.LHS();
		}

	}

	@Override
	public void visit(OWLDataPropertyAssertionAxiom ax) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl sub = factory.getREXIndividual(ax.getSubject());
		REXClassExpressionImpl obj = factory.getREXLiteral(ax.getObject());
		REXObjectPropertyExpressionImpl role = factory.getREXDataPropertyExpression(ax.getProperty());
		REXClassExpressionImpl some = factory.getREXObjectSomeValuesFrom(role, obj);
		initialiseREXSubClassOf(sub,some);

	}

	@Override
	public void visit(OWLTransitiveObjectPropertyAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl prop = factory.getREXObjectPropertyExpression(ax.getProperty());
		factory.initialiseREXSubPropertyChainOf(prop, prop, prop);

	}
	

	@Override
	public void visit(OWLIrreflexiveObjectPropertyAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl role = factory.getREXObjectPropertyExpression(ax.getProperty());
		role.irrflexive = true;

	}

	@Override
	public void visit(OWLSubDataPropertyOfAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl subRole = factory.getREXDataPropertyExpression(ax.getSubProperty());
		REXObjectPropertyExpressionImpl superRole = factory.getREXDataPropertyExpression(ax.getSuperProperty());
		subRole.addSuperRole(superRole);

	}

	@Override
	public void visit(OWLInverseFunctionalObjectPropertyAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl inverse = factory.getREXObjectPropertyExpression(ax.getProperty().getInverseProperty());
		REXClassExpressionImpl lhs = factory.getREXObjectMaxCardinality(1,inverse, factory.top);
		initialiseREXSubClassOf(lhs, factory.top);

	}

	@Override
	public void visit(OWLSameIndividualAxiom ax) {
		// TODO Auto-generated method stub
		List<REXIndividualImpl> indis = new ArrayList<REXIndividualImpl>();
		for(OWLIndividual i:ax.getIndividuals())
		{
			indis.add(factory.getREXIndividual(i));
		}
		for(REXClassExpressionImpl lhs:indis)
		{
			for(REXClassExpressionImpl rhs:indis)
				if(lhs!=rhs)
				{
					initialiseREXSubClassOf(lhs, rhs);
				}
			lhs.LHS();
		}

	}

	@Override
	public void visit(OWLSubPropertyChainOfAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl superRole = factory.getREXObjectPropertyExpression(ax.getSuperProperty());
		List<OWLObjectPropertyExpression> subs = ax.getPropertyChain();
		if(subs.size() == 1)
		{
			REXObjectPropertyExpressionImpl subRole = factory.getREXObjectPropertyExpression(subs.get(0));
			subRole.addSuperRole(superRole);
		}
		else
		{
			REXObjectPropertyExpressionImpl sub1 = factory.getREXObjectPropertyExpression(subs.remove(0));
			REXObjectPropertyExpressionImpl sub2 = factory.getREXName4RoleChain(subs);
			factory.initialiseREXSubPropertyChainOf(sub1, sub2, superRole);
		}

	}

	@Override
	public void visit(OWLInverseObjectPropertiesAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl prop1 = factory.getREXObjectPropertyExpression(ax.getFirstProperty());
		REXObjectPropertyExpressionImpl prop2 = factory.getREXObjectPropertyExpression(ax.getSecondProperty());
		
		REXObjectPropertyExpressionImpl p1inverse = factory.getREXObjectPropertyExpression(ax.getFirstProperty().getInverseProperty());
		REXObjectPropertyExpressionImpl p2inverse = factory.getREXObjectPropertyExpression(ax.getSecondProperty().getInverseProperty());
		
		prop1.addSuperRole(p2inverse);
		prop2.addSuperRole(p1inverse);
		p1inverse.addSuperRole(prop2);
		p2inverse.addSuperRole(prop1);

	}

	@Override
	public void visit(OWLHasKeyAxiom ax) {
		// TODO Auto-generated method stub
		if(factory.debug)
			System.out.print("Unsupported axiom: "+ax);

	}

	@Override
	public void visit(OWLDatatypeDefinitionAxiom ax) {
		// TODO Auto-generated method stub
		if(factory.debug)
			System.out.print("Unsupported axiom: "+ax);

	}

	@Override
	public void visit(SWRLRule ax) {
		// TODO Auto-generated method stub
		if(factory.debug)
			System.out.print("Unsupported axiom: "+ax);

	}

}
