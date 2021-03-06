package eu.trowl.rex.factory;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitorEx;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;

import eu.trowl.rex.model.implementations.REXClassExpressionImpl;
import eu.trowl.rex.model.implementations.REXClassImpl;
import eu.trowl.rex.model.implementations.REXDataPropertyImpl;
import eu.trowl.rex.model.implementations.REXObjectPropertyExpressionImpl;

public class REXClassExpressionBuilder implements
		OWLClassExpressionVisitorEx<REXClassExpressionImpl> {
	
	REXDataFactory factory;

	public REXClassExpressionBuilder(REXDataFactory rexDataFactory) {
		// TODO Auto-generated constructor stub
		factory = rexDataFactory;
	}

	@Override
	public REXClassExpressionImpl visit(OWLClass clazz) {
		// TODO Auto-generated method stub
		REXClassImpl newclazz = factory.concepts.get(clazz);
		if(newclazz == null)
		{
			newclazz = new REXClassImpl(clazz);
			factory.concepts.put(clazz, newclazz);
//			classExpressions.add(newclazz);
		}
//		 CONFIGURATION!
//		getREL2ObjectComplementOf(newclazz);
		return newclazz;		
	}

	@Override
	public REXClassExpressionImpl visit(OWLObjectIntersectionOf clazz) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl exp = factory.getREXObjectIntersectionOf(clazz.getOperandsAsList(),0);
		exp.testComplement();
		return exp;
	}

	@Override
	public REXClassExpressionImpl visit(OWLObjectUnionOf clazz) {
		// TODO Auto-generated method stub
//		REXClassExpressionImpl intersection =  factory.getREXClassExpression(clazz.getComplementNNF());
//		REXClassExpressionImpl union = intersection.complement;
//		if(union == null)
//		{
////			union = getREL2ApproximationName(exp);
//			union = factory.getREXObjectComplementOf(intersection);
////		union.complement = intersection;
////		intersection.complement = union;
//		intersection.LHS();
//		}
		REXClassExpressionImpl union = factory.getREXObjectUnionOf(clazz.getOperandsAsList(),0);
		union.testComplement();
		return union;
	}

	@Override
	public REXClassExpressionImpl visit(OWLObjectComplementOf clazz) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl complement = factory.getREXClassExpression(clazz.getOperand());
		// do I need to do this?
		// a very simple normalisation step;
		complement.testComplement();
		return factory.getREXObjectComplementOf(complement);
	}

	@Override
	public REXClassExpressionImpl visit(OWLObjectSomeValuesFrom clazz) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl some = factory.getREXObjectSomeValuesFrom(clazz.getProperty(), clazz.getFiller());
		some.testComplement();
		return some;
	}

	@Override
	public REXClassExpressionImpl visit(OWLObjectAllValuesFrom clazz) {
		// TODO Auto-generated method stub
//		REXClassExpressionImpl some = factory.getREXClassExpression(clazz.getComplementNNF());
//		REXClassExpressionImpl all = some.complement;
//		if(all == null)
//		{
////			all = getREL2ApproximationName(clazz);
//			all = factory.getREXObjectComplementOf(some);
////		some.complement = all;
////		all.complement = some;
//		some.LHS();
//		}
		REXClassExpressionImpl all = factory.getREXObjectAllValuesFrom(clazz.getProperty(), clazz.getFiller());
		all.testComplement();
		return all;
	}

	@Override
	public REXClassExpressionImpl visit(OWLObjectHasValue clazz) {
		// TODO Auto-generated method stub
		return clazz.asSomeValuesFrom().accept(this);
	}

	@Override
	public REXClassExpressionImpl visit(OWLObjectMinCardinality clazz) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl role = factory.getREXObjectPropertyExpression(clazz.getProperty());
		REXClassExpressionImpl	filler = factory.getREXClassExpression(clazz.getFiller());

		REXClassExpressionImpl min = factory.getREXObjectMinCardinality(clazz.getCardinality(), role, filler);
		min.testComplement();
		return min;
	}

	@Override
	public REXClassExpressionImpl visit(OWLObjectExactCardinality clazz) {
		// TODO Auto-generated method stub
		return clazz.asIntersectionOfMinMax().accept(this);
	}

	@Override
	public REXClassExpressionImpl visit(OWLObjectMaxCardinality clazz) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl max =  factory.getREXObjectMaxCardinality(clazz.getCardinality(), clazz.getProperty(), clazz.getFiller());
		max.testComplement();
		return max;
	}

	@Override
	public REXClassExpressionImpl visit(OWLObjectHasSelf clazz) {
		// TODO Auto-generated method stub
		if(factory.debug)
			System.out.println("Unsupported Class Expression: "+clazz);
		return null;
	}

	@Override
	public REXClassExpressionImpl visit(OWLObjectOneOf clazz) {
		// TODO Auto-generated method stub
		if(clazz.getIndividuals().size() == 1)
		{
			OWLIndividual indi = clazz.getIndividuals().iterator().next();
			return factory.getREXIndividual(indi);
		}
		else
			return clazz.asObjectUnionOf().accept(this);
	}

	@Override
	public REXClassExpressionImpl visit(OWLDataSomeValuesFrom clazz) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl	filler = factory.getREXDataRange(clazz.getFiller());
		if(filler == factory.bottom)
			return factory.bottom;
		REXDataPropertyImpl role = factory.getREXDataPropertyExpression(clazz.getProperty());

		REXClassExpressionImpl some = factory.getREXObjectSomeValuesFrom(role, filler);
		some.testComplement();
		return some;
	}

	@Override
	public REXClassExpressionImpl visit(OWLDataAllValuesFrom clazz) {
		// TODO Auto-generated method stub
//		REXClassExpressionImpl some = clazz.getComplementNNF().accept(this);
//		REXClassExpressionImpl all = some.complement;
//		if(all == null)
//		{
//			all = factory.getREXObjectComplementOf(some);
////		some.LHS();
//		}
//		return all;
		REXClassExpressionImpl all = factory.getREXObjectAllValuesFrom(factory.getREXDataPropertyExpression(clazz.getProperty()), factory.getREXDataRange(clazz.getFiller()));
		all.testComplement();
		return all;
	}

	@Override
	public REXClassExpressionImpl visit(OWLDataHasValue clazz) {
		// TODO Auto-generated method stub
		if(factory.debug)
			System.out.println("Unsupported Class Expression: "+clazz);
		return null;
	}

	@Override
	public REXClassExpressionImpl visit(OWLDataMinCardinality clazz) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl role = factory.getREXDataPropertyExpression(clazz.getProperty());
		REXClassExpressionImpl	filler = factory.getREXDataRange(clazz.getFiller());

		REXClassExpressionImpl min = factory.getREXObjectMinCardinality(clazz.getCardinality(), role, filler);
		min.testComplement();
		return min;
	}

	@Override
	public REXClassExpressionImpl visit(OWLDataExactCardinality clazz) {
		// TODO Auto-generated method stub
		return clazz.asIntersectionOfMinMax().accept(this);
	}

	@Override
	public REXClassExpressionImpl visit(OWLDataMaxCardinality clazz) {
		// TODO Auto-generated method stub
		if(clazz.getCardinality() < 0)
			return factory.bottom;
//		if(clazz.getCardinality() == 0)
//			return getREXObjectAllValuesFrom(clazz.getProperty(), clazz.getFiller().filler.getComplementNNF());
		REXClassExpressionImpl max = factory.getREXObjectMaxCardinality(clazz.getCardinality(), factory.getREXDataPropertyExpression(clazz.getProperty()), factory.getREXDataRange(clazz.getFiller()));
		max.testComplement();
		return max;
	}

}
