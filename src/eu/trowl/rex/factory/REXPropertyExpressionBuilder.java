package eu.trowl.rex.factory;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLPropertyExpressionVisitorEx;

import eu.trowl.rex.model.implementations.REXDataPropertyImpl;
import eu.trowl.rex.model.implementations.REXInverseObjectPropertyImpl;
import eu.trowl.rex.model.implementations.REXObjectPropertyExpressionImpl;
import eu.trowl.rex.model.implementations.REXObjectPropertyImpl;

public class REXPropertyExpressionBuilder implements
		OWLPropertyExpressionVisitorEx<REXObjectPropertyExpressionImpl> {
	
	REXDataFactory factory;

	public REXPropertyExpressionBuilder(REXDataFactory rexDataFactory) {
		// TODO Auto-generated constructor stub
		factory = rexDataFactory;
	}

	@Override
	public REXObjectPropertyExpressionImpl visit(OWLObjectProperty prop) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl newRole = factory.roles.get(prop);
		if(newRole == null)
		{
			newRole = new REXObjectPropertyImpl(prop);
			factory.roles.put(prop, newRole);
		}
		return newRole;
	}

	@Override
	public REXObjectPropertyExpressionImpl visit(OWLObjectInverseOf prop) {
		// TODO Auto-generated method stub
		OWLObjectInverseOf newrole = (OWLObjectInverseOf) prop;
		REXObjectPropertyImpl inverse = (REXObjectPropertyImpl) newrole.getInverse().asOWLObjectProperty().accept(this); 
		REXObjectPropertyExpressionImpl role = inverse.getInversePropertyExpression();
		if(role == null)
			role = new REXInverseObjectPropertyImpl(inverse);
		factory.roles.put(prop, role);
		return role;
	}

	@Override
	public REXObjectPropertyExpressionImpl visit(OWLDataProperty prop) {
		// TODO Auto-generated method stub
		REXDataPropertyImpl newRole = (REXDataPropertyImpl) factory.roles.get(prop);
		if(newRole == null)
		{
			newRole = new REXDataPropertyImpl(prop);
			factory.roles.put(prop, newRole);
		}
		return newRole;
	}

	@Override
	public REXObjectPropertyExpressionImpl visit(OWLAnnotationProperty arg0) {
		// TODO Auto-generated method stub
		if(factory.debug)
			System.out.println("Ignored Property Expression: "+arg0);
		return null;
	}

}
