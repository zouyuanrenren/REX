package eu.trowl.rex.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataComplementOf;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;

import eu.trowl.rex.model.implementations.REXClassExpressionImpl;
import eu.trowl.rex.model.implementations.REXClassImpl;
import eu.trowl.rex.model.implementations.REXDataPropertyImpl;
import eu.trowl.rex.model.implementations.REXDatatypeImpl;
import eu.trowl.rex.model.implementations.REXIndividualImpl;
import eu.trowl.rex.model.implementations.REXInverseObjectPropertyImpl;
import eu.trowl.rex.model.implementations.REXLiteralImpl;
import eu.trowl.rex.model.implementations.REXObjectAllValuesFromImpl;
import eu.trowl.rex.model.implementations.REXObjectComplementOfImpl;
import eu.trowl.rex.model.implementations.REXObjectIntersectionOfImpl;
import eu.trowl.rex.model.implementations.REXObjectMax1CardinalityImpl;
import eu.trowl.rex.model.implementations.REXObjectMaxCardinalityImpl;
import eu.trowl.rex.model.implementations.REXObjectMinCardinalityImpl;
import eu.trowl.rex.model.implementations.REXObjectPropertyExpressionImpl;
import eu.trowl.rex.model.implementations.REXObjectPropertyImpl;
import eu.trowl.rex.model.implementations.REXObjectSomeValuesFromImpl;
import eu.trowl.rex.model.implementations.REXObjectUnionOfImpl;
import eu.trowl.rex.model.implementations.REXSubClassOfImpl;

public class REXDataFactory {
//	 public List<REL2ClassExpressionImpl> classExpressions = new ArrayList<REL2ClassExpressionImpl>();
//	 public int conceptID = 1;
	
	 public HashMap<OWLClassExpression, REXClassImpl> concepts;
	 public HashMap<OWLDataRange, REXClassImpl> datatypes;
	 public HashMap<OWLIndividual, REXIndividualImpl> individuals;
	 public HashMap<OWLLiteral, REXLiteralImpl> literals;
	 public HashSet<REXClassExpressionImpl> contexts;

	 public HashMap<List<OWLObjectPropertyExpression>, REXObjectPropertyExpressionImpl> rolechainNames;
//	 public int roleID = 1;
	
	 public HashMap<OWLPropertyExpression, REXObjectPropertyExpressionImpl> roles;
	 
	 public boolean smallT = true;
	 public boolean smallA = true;
	 
	 public REXClassImpl top;
	 public REXClassImpl bottom;
	 
	 private boolean debug = false;
	 
	 public boolean TBox_Classified = false;
	 public boolean ABox_Classified = false;
	
	private  REXClassExpressionImpl getREL2ApproximationName(
			OWLClassExpression exp) {
		// TODO Auto-generated method stub
		REXClassImpl cls = concepts.get(exp);
		if(cls == null)
		{
			cls = new REXClassImpl();
			cls.original = false;
			concepts.put(exp, cls);
		}
		return cls;
	}
	
	private  REXClassImpl getREL2Class(OWLClass clazz){
		REXClassImpl newclazz = concepts.get(clazz);
		if(newclazz == null)
		{
			newclazz = new REXClassImpl(clazz);
			concepts.put(clazz, newclazz);
//			classExpressions.add(newclazz);
		}
//		 CONFIGURATION!
//		getREL2ObjectComplementOf(newclazz);
		return newclazz;		
	}

	public  REXClassExpressionImpl getREL2ClassExpression(OWLClassExpression clazz){
		OWLClassExpression exp = clazz.getNNF();
		REXClassExpressionImpl cls;
		if(exp instanceof OWLClass)
			cls = getREL2Class((OWLClass)exp);
		else if(exp instanceof OWLObjectIntersectionOf)
			cls = getREL2ObjectIntersectionOf((OWLObjectIntersectionOf)exp);
		else if(exp instanceof OWLObjectSomeValuesFrom)
			cls = getREL2ObjectSomeValuesFrom((OWLObjectSomeValuesFrom)exp);
		else if(exp instanceof OWLObjectAllValuesFrom)
			cls = getREL2ObjectAllValuesFrom((OWLObjectAllValuesFrom)exp);
		else if(exp instanceof OWLObjectMaxCardinality)
			cls = getREL2ObjectMaxCardinality((OWLObjectMaxCardinality)exp);
		else if(exp instanceof OWLObjectUnionOf)
			cls = getREL2ObjectUnionOf((OWLObjectUnionOf)exp);
		else if(exp instanceof OWLObjectComplementOf)
			cls = getREL2ObjectComplementOf((OWLObjectComplementOf)exp);
		else if(exp instanceof OWLObjectHasValue)
			cls = getREL2ObjectHasValue((OWLObjectHasValue)exp);
		else if(exp instanceof OWLObjectOneOf)
			cls = getREL2ObjectOneOf((OWLObjectOneOf)exp);
		else if(exp instanceof OWLObjectMinCardinality)
			cls = getREL2ObjectMinCardinality((OWLObjectMinCardinality)exp);
		else if(exp instanceof OWLObjectExactCardinality)
			cls = getREL2ObjectExactCardinality((OWLObjectExactCardinality)exp);
		else if(exp instanceof OWLDataSomeValuesFrom)
			cls = getREL2DataSomeValuesFrom((OWLDataSomeValuesFrom)exp);
		else if(exp instanceof OWLDataAllValuesFrom)
			cls = getREL2DataAllValuesFrom((OWLDataAllValuesFrom)exp);
		else if(exp instanceof OWLDataMinCardinality)
			cls = getREL2DataMinCardinality((OWLDataMinCardinality)exp);
		else if(exp instanceof OWLDataMaxCardinality)
			cls = getREL2DataMaxCardinality((OWLDataMaxCardinality)exp);
		else if(exp instanceof OWLDataExactCardinality)
			cls = getREL2DataExactCardinality((OWLDataExactCardinality)exp);
		else
		{
			if(debug)
			System.out.println("wrong class expression types:" + exp);
			cls = getREL2ApproximationName(exp);
		}
		if(smallT && cls.complement == null)
			getREL2ObjectComplementOf(cls);
		return cls;
	}

	private REXClassExpressionImpl getREL2DataExactCardinality(
			OWLDataExactCardinality exp) {
		// TODO Auto-generated method stub
		return getREL2ObjectIntersectionOf((OWLObjectIntersectionOf)exp.asIntersectionOfMinMax());
	}

	private REXClassExpressionImpl getREL2DataMaxCardinality(
			OWLDataMaxCardinality exp) {
		// TODO Auto-generated method stub
		return getREL2ObjectMaxCardinality(exp.getCardinality(), getREL2DataPropertyExpression(exp.getProperty()), getREL2DataRange(exp.getFiller()));
	}

	private REXClassExpressionImpl getREL2DataMinCardinality(
			OWLDataMinCardinality exp) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl role = getREL2DataPropertyExpression(exp.getProperty());
		REXClassExpressionImpl	filler = getREL2DataRange(exp.getFiller());

		return getREL2ObjectMinCardinality(exp.getCardinality(), role, filler);
	}

	private REXClassExpressionImpl getREL2DataAllValuesFrom(
			OWLDataAllValuesFrom exp) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl some = getREL2DataSomeValuesFrom((OWLDataSomeValuesFrom) exp.getComplementNNF());
		REXClassExpressionImpl all = some.complement;
		if(all == null)
		{
			all = getREL2ObjectComplementOf(some);
//		some.LHS();
		}
		return all;
	}

	private REXClassExpressionImpl getREL2DataSomeValuesFrom(
			OWLDataSomeValuesFrom clazz) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl	filler = getREL2DataRange(clazz.getFiller());
		if(filler == bottom)
			return bottom;
		REXDataPropertyImpl role = getREL2DataPropertyExpression(clazz.getProperty());

		return getREL2ObjectSomeValuesFrom(role, filler);
	}

	private REXClassExpressionImpl getREL2DataRange(OWLDataRange dataRange) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl cls = datatypes.get(dataRange);
		if(cls != null)
			return cls;
		else if(dataRange.isDatatype())
			cls = getREL2DataType((OWLDatatype)dataRange.asOWLDatatype());
		else if(dataRange instanceof OWLDataOneOf)
			cls = getREL2DataOneOf((OWLDataOneOf)dataRange);
		else if(dataRange instanceof OWLDataComplementOf)
			cls = getREL2DataComplementOf((OWLDataComplementOf)dataRange);
		else
		{
			if(debug)
			System.out.println("wrong data range types:" + dataRange);
			cls = getREL2ApproximationName(dataRange);
		}
//		if(smallT && cls.complement == null)
//			getREL2ObjectComplementOf(cls);
		return cls;
		}

	private REXClassExpressionImpl getREL2DataComplementOf(
			OWLDataComplementOf dataRange) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl complement = getREL2DataRange(dataRange.getDataRange());
		// do I need to do this?
		// a very simple normalisation step;
		return getREL2ObjectComplementOf(complement);

	}

	private REXClassExpressionImpl getREL2DataOneOf(OWLDataOneOf dataRange) {
		// TODO Auto-generated method stub
		if(dataRange.getValues().size() == 1)
		{
			OWLLiteral literal = dataRange.getValues().iterator().next();
			return getREL2Literal(literal);
		}
		else
		{
			List<OWLDataRange> comps = new ArrayList<OWLDataRange>();
			for(OWLLiteral literal:dataRange.getValues())
				comps.add(factory.getOWLDataComplementOf(factory.getOWLDataOneOf(literal)));
			REXClassExpressionImpl intersection = getREL2DataIntersectionOf(comps);
			REXClassExpressionImpl union = intersection.complement;
			if(union == null)
			{
				union = getREL2ObjectComplementOf(intersection);

			}
			return union;
		}
	}

	private REXClassExpressionImpl getREL2DataIntersectionOf(
			List<OWLDataRange> operands) {
		// TODO Auto-generated method stub
		if(operands.size() == 1)
			return getREL2DataRange(operands.get(0));
		
		REXClassExpressionImpl leftConjunct = getREL2DataRange(operands.remove(0));
		REXClassExpressionImpl rightConjunct = getREL2DataIntersectionOf(operands);
		
		return getREL2ObjectIntersectionOf(leftConjunct, rightConjunct);
	}

	private REXClassExpressionImpl getREL2Literal(OWLLiteral literal) {
		// TODO Auto-generated method stub
		REXLiteralImpl newclazz = literals.get(literal);
		if(newclazz == null)
		{
			newclazz = new REXLiteralImpl(literal);
			newclazz.original = false;
			REXClassExpressionImpl type = getREL2DataType(literal.getDatatype());
			newclazz.superClasses.add(type);
			newclazz.addOriginalSuperClasses(type);
			literals.put(literal, newclazz);
//			classExpressions.add(newclazz);
		}
		return newclazz;
	}

	private REXClassExpressionImpl getREL2ApproximationName(
			OWLDataRange dataRange) {
		// TODO Auto-generated method stub
		REXClassImpl cls = datatypes.get(dataRange);
		if(cls == null)
		{
			cls = new REXDatatypeImpl();
			cls.original = false;
			datatypes.put(dataRange, cls);
		}
		return cls;
	}

	private REXClassExpressionImpl getREL2DataType(OWLDatatype datatype) {
		// TODO Auto-generated method stub
		REXClassImpl newclazz = datatypes.get(datatype);
		if(newclazz == null)
		{
			newclazz = new REXDatatypeImpl(datatype);
			newclazz.original = false;
			datatypes.put(datatype, newclazz);
//			classExpressions.add(newclazz);
		}
//		 CONFIGURATION!
//		getREL2ObjectComplementOf(newclazz);
		return newclazz;		
	}

	private  REXObjectPropertyExpressionImpl getREL2Name4RoleChain(List<OWLObjectPropertyExpression> subs){
		
		if(subs.size() == 1)
			return getREL2ObjectPropertyExpression(subs.get(0));
		
		REXObjectPropertyExpressionImpl newName = rolechainNames.get(subs);
		if(newName == null)
		{
			newName = new REXObjectPropertyImpl();
			rolechainNames.put(subs, newName);
		}
		REXObjectPropertyExpressionImpl newSub1 = getREL2ObjectPropertyExpression(subs.get(0));
		REXObjectPropertyExpressionImpl newSub2 = getREL2Name4RoleChain(subs.subList(1, subs.size()));
		initialiseREL2SubPropertyChainOf(newSub1, newSub2, newName);
		return newName;
	}

	public  REXClassExpressionImpl getREL2ObjectAllValuesFrom(
			OWLObjectAllValuesFrom clazz) {
		// TODO Auto-generated method stub
//		REL2ClassExpressionImpl	filler = getREL2ClassExpression(clazz.getFiller());
//		if(filler == REL2PredefinedClassExpressions.top)
//			return REL2PredefinedClassExpressions.top;
//		REL2ObjectPropertyExpressionImpl role = getREL2ObjectPropertyExpression(clazz.getProperty());
//
//		REL2ClassExpressionImpl all = getREL2ObjectAllValuesFrom(role, filler);
		REXClassExpressionImpl some = getREL2ObjectSomeValuesFrom((OWLObjectSomeValuesFrom) clazz.getComplementNNF());
		REXClassExpressionImpl all = some.complement;
		if(all == null)
		{
//			all = getREL2ApproximationName(clazz);
			all = getREL2ObjectComplementOf(some);
//		some.complement = all;
//		all.complement = some;
		some.LHS();
		}
		return all;
		}

	private  REXObjectAllValuesFromImpl getREL2ObjectAllValuesFrom(
			REXObjectPropertyExpressionImpl role, REXClassExpressionImpl filler) {
		// TODO Auto-generated method stub
		REXObjectAllValuesFromImpl newAll = role.alls.get(filler);
		if(newAll == null)
		{
			newAll = new REXObjectAllValuesFromImpl(role, filler);
			role.alls.put(filler,newAll);
//			classExpressions.add(newAll);
		}
		return newAll;
	}

	private  REXClassExpressionImpl getREL2ObjectComplementOf(
			OWLObjectComplementOf exp) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl complement = getREL2ClassExpression(exp.getOperand());
		// do I need to do this?
		// a very simple normalisation step;
		return getREL2ObjectComplementOf(complement);

	}

	private REXClassExpressionImpl getREL2ObjectComplementOf(
			REXClassExpressionImpl complement) {
		// TODO Auto-generated method stub
		if(complement instanceof REXObjectComplementOfImpl)
			return ((REXObjectComplementOfImpl)complement).getClassExpression();
		REXClassExpressionImpl cls = complement.complement;
		if(cls == null)
		{
			cls = new REXObjectComplementOfImpl(complement);
		}
		return cls;
	}

	private REXClassExpressionImpl getREL2ObjectExactCardinality(
			OWLObjectExactCardinality exp) {
		// TODO Auto-generated method stub
		return getREL2ObjectIntersectionOf((OWLObjectIntersectionOf)exp.asIntersectionOfMinMax());
	}

	private REXClassExpressionImpl getREL2ObjectHasValue(OWLObjectHasValue exp) {
		// TODO Auto-generated method stub
		return getREL2ObjectSomeValuesFrom((OWLObjectSomeValuesFrom) exp.asSomeValuesFrom());
	}

	public  REXClassExpressionImpl getREL2ObjectIntersectionOf(List<OWLClassExpression> operands){
		
		if(operands.size() == 1)
			return getREL2ClassExpression(operands.get(0));
		
		REXClassExpressionImpl leftConjunct = getREL2ClassExpression(operands.remove(0));
		REXClassExpressionImpl rightConjunct = getREL2ObjectIntersectionOf(operands);
		
		return getREL2ObjectIntersectionOf(leftConjunct, rightConjunct);
		
	}

	public  REXClassExpressionImpl getREL2ObjectIntersectionOf(OWLObjectIntersectionOf clazz){
		
		return getREL2ObjectIntersectionOf(clazz.getOperandsAsList());
	}

	private  REXObjectIntersectionOfImpl getREL2ObjectIntersectionOf(REXClassExpressionImpl ... conjuncts){
//		if(conjuncts.length>2)
//			System.out.println("Incorrect number of conjuncts!");
		REXObjectIntersectionOfImpl newAnd = conjuncts[0].Ands.get(conjuncts[1]);
				
				if(newAnd == null)
				{
					newAnd = new REXObjectIntersectionOfImpl(conjuncts[0], conjuncts[1]);
					conjuncts[0].Ands.put(conjuncts[1], newAnd);
					conjuncts[1].Ands.put(conjuncts[0], newAnd);
//					classExpressions.add(newAnd);
				}
				return newAnd;
	}

//	private  REL2ObjectMax1CardinalityImpl getREL2ObjectMax1Cardinality(
//			REL2ObjectPropertyExpressionImpl role,
//			REL2ClassExpressionImpl filler) {
//		// TODO Auto-generated method stub
//		HashMap<REL2ObjectPropertyExpressionImpl, REL2ObjectMaxCardinalityImpl> max1s = filler.maxs.get(1);
//		if(max1s == null)
//		{
//			max1s = new HashMap<REL2ObjectPropertyExpressionImpl, REL2ObjectMaxCardinalityImpl>();
//			filler.maxs.put(1, max1s);
//		}
//		REL2ObjectMax1CardinalityImpl newMax = (REL2ObjectMax1CardinalityImpl) max1s.get(role);
//		if(newMax == null)
//		{
//			newMax = new REL2ObjectMax1CardinalityImpl(role, filler);
//			max1s.put(role, newMax);
////			classExpressions.add(newMax);
//		}
//		return newMax;
//	}

//	private  REL2ClassExpressionImpl getREL2ObjectMaxCardinality(
//			int cardinality, REL2ObjectPropertyExpressionImpl role,
//			REL2ClassExpressionImpl filler) {
//		// TODO Auto-generated method stub
//		// TODO special case where cardinality == 0;
//		if(cardinality < 0)
//			return bottom;
//		if(cardinality == 0)
//			return getREL2ObjectAllValuesFrom(role, getREL2ObjectComplementOf(filler));
//		if(cardinality == 1)
//			return getREL2ObjectMax1Cardinality(role,filler);
//		HashMap<REL2ObjectPropertyExpressionImpl, REL2ObjectMaxCardinalityImpl> maxs = filler.maxs.get(cardinality);
//		if(maxs == null)
//		{
//			maxs = new HashMap<REL2ObjectPropertyExpressionImpl, REL2ObjectMaxCardinalityImpl>();
//			filler.maxs.put(cardinality, maxs);
//		}
//		REL2ObjectMaxCardinalityImpl newMax = maxs.get(role);
//		if(newMax == null)
//		{
//			newMax = new REL2ObjectMaxCardinalityImpl(cardinality, role, filler);
//			maxs.put(role, newMax);
////			classExpressions.add(newMax);
//		}
//		return newMax;
//	}
	
	public  REXClassExpressionImpl getREL2ObjectMaxCardinality(
			OWLObjectMaxCardinality exp) {
		// TODO Auto-generated method stub
		
//		REL2ObjectPropertyExpressionImpl role = getREL2ObjectPropertyExpression(exp.getProperty());
//		REL2ClassExpressionImpl	filler = getREL2ClassExpression(exp.getFiller());

//		REL2ClassExpressionImpl max = getREL2ObjectMaxCardinality(exp.getCardinality(), role, filler);
//		REL2ClassExpressionImpl min = getREL2ObjectMinCardinality((OWLObjectMinCardinality)exp.getComplementNNF());
//		REL2ClassExpressionImpl max = min.complement;
//		if(max == null)
//		{
////			max = getREL2ApproximationName(exp);
//			max = getREL2ObjectComplementOf(min);
////		max.complement = min;
////		min.complement = max;
//		min.LHS();
//		}
//		return max;
		return getREL2ObjectMaxCardinality(exp.getCardinality(), getREL2ObjectPropertyExpression(exp.getProperty()), getREL2ClassExpression(exp.getFiller()));
	}
	
	private REXClassExpressionImpl getREL2ObjectMaxCardinality(
			int cardinality,
			REXObjectPropertyExpressionImpl role,
			REXClassExpressionImpl filler) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl min = getREL2ObjectMinCardinality(cardinality+1,role,filler);
		REXClassExpressionImpl max = min.complement;
		if(max == null)
		{
			max = getREL2ObjectComplementOf(min);
			min.LHS();
		}
		return max;
	}

	private REXClassExpressionImpl getREL2ObjectMinCardinality(
			int cardinality, REXObjectPropertyExpressionImpl role,
			REXClassExpressionImpl filler) {
		// TODO Auto-generated method stub
		if(cardinality <= 0)
			return top;
		if(cardinality == 1)
			return getREL2ObjectSomeValuesFrom(role, filler);
		HashMap<REXObjectPropertyExpressionImpl, REXObjectMinCardinalityImpl> mins = filler.mins.get(cardinality);
		if(mins == null)
		{
			mins = new HashMap<REXObjectPropertyExpressionImpl, REXObjectMinCardinalityImpl>();
			filler.mins.put(cardinality, mins);
		}
		REXObjectMinCardinalityImpl newMin = mins.get(role);
		if(newMin == null)
		{
			newMin = new REXObjectMinCardinalityImpl(cardinality, role, filler);
			mins.put(role, newMin);
//			classExpressions.add(newMin);
		}
		return newMin;
	}

	private REXClassExpressionImpl getREL2ObjectMinCardinality(
			OWLObjectMinCardinality exp) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl role = getREL2ObjectPropertyExpression(exp.getProperty());
		REXClassExpressionImpl	filler = getREL2ClassExpression(exp.getFiller());

		return getREL2ObjectMinCardinality(exp.getCardinality(), role, filler);
	}
	
	private REXClassExpressionImpl getREL2ObjectOneOf(OWLObjectOneOf exp) {
		// TODO Auto-generated method stub
		if(exp.getIndividuals().size() == 1)
		{
			OWLIndividual indi = exp.getIndividuals().iterator().next();
			return getREL2Individual(indi);
		}
		else
			return getREL2ObjectUnionOf((OWLObjectUnionOf) exp.asObjectUnionOf());
	}
	
	private  REXObjectPropertyExpressionImpl getREL2ObjectProperty(OWLObjectProperty prop){
		REXObjectPropertyExpressionImpl newRole = roles.get(prop);
		if(newRole == null)
		{
			newRole = new REXObjectPropertyImpl(prop);
			roles.put(prop, newRole);
		}
		return newRole;

	}
	
	public  REXObjectPropertyExpressionImpl getREL2ObjectPropertyExpression(OWLObjectPropertyExpression prop){
		REXObjectPropertyExpressionImpl role;
		if(prop.getSimplified() instanceof OWLObjectProperty)
		{
			role =  getREL2ObjectProperty(prop.getSimplified().asOWLObjectProperty());
		}
		else
		{
			OWLObjectInverseOf newrole = (OWLObjectInverseOf) prop.getSimplified();
			REXObjectPropertyImpl inverse = (REXObjectPropertyImpl) getREL2ObjectProperty(newrole.getInverse().asOWLObjectProperty()); 
			role = inverse.getInversePropertyExpression();
			if(role == null)
			role = new REXInverseObjectPropertyImpl(inverse);
			roles.put(prop, role);
		}
		return role;
	}

	public  REXClassExpressionImpl getREL2ObjectSomeValuesFrom(OWLObjectSomeValuesFrom clazz){
		REXClassExpressionImpl	filler = getREL2ClassExpression(clazz.getFiller());
		if(filler == bottom)
			return bottom;
		REXObjectPropertyExpressionImpl role = getREL2ObjectPropertyExpression(clazz.getProperty());

		return getREL2ObjectSomeValuesFrom(role, filler);
	}
	
	private  REXObjectSomeValuesFromImpl getREL2ObjectSomeValuesFrom(REXObjectPropertyExpressionImpl role, REXClassExpressionImpl filler){
		
		REXObjectSomeValuesFromImpl newSome = role.somes.get(filler);
		if(newSome == null)
		{
			newSome = new REXObjectSomeValuesFromImpl(role, filler);
			role.somes.put(filler, newSome);
//			classExpressions.add(newSome);
		}
		return newSome;

	}
	

	private  REXClassExpressionImpl getREL2ObjectUnionOf(
			List<OWLClassExpression> operands) {
		// TODO Auto-generated method stub
		if(operands.size() == 1)
			return getREL2ClassExpression(operands.get(0));
		
		REXClassExpressionImpl leftConcept = getREL2ClassExpression(operands.remove(0));
		REXClassExpressionImpl rightConcept = getREL2ObjectUnionOf(operands);
		
		return getREL2ObjectUnionOf(leftConcept, rightConcept);
	}
	
	private  REXClassExpressionImpl getREL2ObjectUnionOf(
			OWLObjectUnionOf exp) {
		// TODO Auto-generated method stub
//		REL2ClassExpressionImpl union = getREL2ObjectUnionOf(exp.getOperandsAsList());
		REXClassExpressionImpl intersection =  getREL2ObjectIntersectionOf((OWLObjectIntersectionOf)exp.getComplementNNF());
		REXClassExpressionImpl union = intersection.complement;
		if(union == null)
		{
//			union = getREL2ApproximationName(exp);
			union = getREL2ObjectComplementOf(intersection);
//		union.complement = intersection;
//		intersection.complement = union;
		intersection.LHS();
		}
		return union;
	}
	
	private  REXClassExpressionImpl getREL2ObjectUnionOf(REXClassExpressionImpl ... concepts){
//		if(concepts.length>2)
//			System.out.println("Incorrect number of union concepts!");
		REXObjectUnionOfImpl newOr = concepts[0].Ors.get(concepts[1]);
				
				if(newOr == null)
				{
					newOr = new REXObjectUnionOfImpl(concepts[0], concepts[1]);
					concepts[0].Ors.put(concepts[1], newOr);
					concepts[1].Ors.put(concepts[0], newOr);
//					classExpressions.add(newOr);
					newOr.LHS();
				}
				return newOr;
	}

	public  REXSubClassOfImpl getREL2SubClassOf(
				REXClassExpressionImpl lhs, REXClassExpressionImpl rhs) {
			// TODO Auto-generated method stub
			REXSubClassOfImpl axiom = null;
			if(rhs instanceof REXObjectSomeValuesFromImpl)
			{
				REXObjectSomeValuesFromImpl some = (REXObjectSomeValuesFromImpl) rhs;
				REXObjectPropertyExpressionImpl role = some.getProperty();
				REXClassExpressionImpl filler = some.getFiller();
				axiom = getREL2SubClassOfSomeValuesFrom(lhs, role, filler);
				axiom.link = true;
			}
//			else if(rhs instanceof REL2ObjectAllValuesFromImpl)
//			{
//				axiom = getREL2SubClassOfAllValuesFrom(lhs, rhs);
//			}
//			else if(rhs instanceof REL2ObjectMax1CardinalityImpl)
//			{
//				axiom = getREL2SubClassOfMax1Cardinality(lhs, rhs);
//			}
			else
			{
				lhs.subAxioms.putIfAbsent(rhs, new REXSubClassOfImpl(lhs, rhs));
				axiom = lhs.subAxioms.get(rhs);
			}
			
			return axiom;
		}

	private  REXSubClassOfImpl getREL2SubClassOfAllValuesFrom(
			REXClassExpressionImpl C,
			REXClassExpressionImpl D) {
		// TODO Auto-generated method stub
		C.subAllAxioms.putIfAbsent(D, new REXSubClassOfImpl(C,D));
		D.RHS();
		return C.subAllAxioms.get(D);
	}

	private  REXSubClassOfImpl getREL2SubClassOfMax1Cardinality(
			REXClassExpressionImpl C, REXClassExpressionImpl D) {
		// TODO Auto-generated method stub
		C.subMax1Axioms.putIfAbsent(D, new REXSubClassOfImpl(C,D));
		D.RHS();
		return C.subMax1Axioms.get(D);
	}

	public  REXSubClassOfImpl getREL2SubClassOfSomeValuesFrom(
			REXClassExpressionImpl C, REXObjectPropertyExpressionImpl role,
			REXClassExpressionImpl D) {
		// TODO Auto-generated method stub
		REXSubClassOfImpl axiom = null;
			C.subSomeAxioms.putIfAbsent(role, new ConcurrentHashMap<REXClassExpressionImpl, REXSubClassOfImpl>());
			ConcurrentHashMap<REXClassExpressionImpl, REXSubClassOfImpl> axioms = C.subSomeAxioms.get(role);
			axioms.putIfAbsent(D, new REXSubClassOfImpl(C, role, D));
			axiom = axioms.get(D);
		
		return axiom;
	}

	public  REXSubClassOfImpl getstrictREL2SubClassOf(
			REXClassExpressionImpl lhs, REXClassExpressionImpl rhs) {
		// TODO Auto-generated method stub
		lhs.subAxioms.putIfAbsent(rhs, new REXSubClassOfImpl(lhs, rhs));
		return lhs.subAxioms.get(rhs);
	}
	
	OWLDataFactory factory;
	public boolean consistency = true;

	public  void initialise(OWLDataFactory datafactory){
		concepts = new HashMap<OWLClassExpression, REXClassImpl>();
		contexts = new HashSet<REXClassExpressionImpl>();
		datatypes = new HashMap<OWLDataRange, REXClassImpl>();
		individuals = new HashMap<OWLIndividual, REXIndividualImpl>();
		literals = new HashMap<OWLLiteral, REXLiteralImpl>();
		roles = new HashMap<OWLPropertyExpression, REXObjectPropertyExpressionImpl>();
		rolechainNames = new HashMap<List<OWLObjectPropertyExpression>, REXObjectPropertyExpressionImpl>();
		factory = datafactory;
//		concepts.put(datafactory.getOWLThing(), REL2PredefinedClassExpressions.top);
//		concepts.put(datafactory.getOWLNothing(), REL2PredefinedClassExpressions.bottom);
		top = getREL2Class(datafactory.getOWLThing());
//		top.original = true;
		bottom = getREL2Class(datafactory.getOWLNothing());
		top.complement = bottom;
		bottom.complement = top;
//		bottom.original = true;
		bottom.satisfiable = false;
//		classExpressions.add(REL2PredefinedClassExpressions.top);
	}

	public  void initialiseAxiom(OWLLogicalAxiom ax) {
		// TODO Auto-generated method stub
		if(ax instanceof OWLSubObjectPropertyOfAxiom)
			initialiseSubObjectPropertyOf((OWLSubObjectPropertyOfAxiom) ax);
		else if(ax instanceof OWLObjectPropertyDomainAxiom)
			initialiseREL2ObjectPropertyDomain((OWLObjectPropertyDomainAxiom)ax);
		else if(ax instanceof OWLEquivalentClassesAxiom)
			initialiseREL2EquivalentClasses((OWLEquivalentClassesAxiom)ax);
		else if(ax instanceof OWLEquivalentObjectPropertiesAxiom)
			initialiseREL2EquivalentObjectProperties((OWLEquivalentObjectPropertiesAxiom)ax);
		else if(ax instanceof OWLTransitiveObjectPropertyAxiom)
			initialiseREL2TransitiveObjectProperty((OWLTransitiveObjectPropertyAxiom)ax);
		else if(ax instanceof OWLSubPropertyChainOfAxiom)
			initialiseREL2SubPropertyChainOf((OWLSubPropertyChainOfAxiom)ax);
		else if(ax instanceof OWLSubClassOfAxiom)
			initialiseREL2SubClassOf((OWLSubClassOfAxiom)ax);
		else if(ax instanceof OWLDisjointClassesAxiom)
			initialiseREL2DisjointClasses((OWLDisjointClassesAxiom) ax);
		else if(ax instanceof OWLObjectPropertyRangeAxiom)
			initialiseREL2ObjectPropertyRange((OWLObjectPropertyRangeAxiom)ax);
		else if(ax instanceof OWLInverseObjectPropertiesAxiom)
			initialiseREL2InverseObjectProperties((OWLInverseObjectPropertiesAxiom)ax);
		else if(ax instanceof OWLDisjointObjectPropertiesAxiom)
			initialiseREL2DisjointObjectPropertiesAxiom((OWLDisjointObjectPropertiesAxiom)ax);
		else if(ax instanceof OWLIrreflexiveObjectPropertyAxiom)
			initialiseREL2IrreflexiveObjectProperty((OWLIrreflexiveObjectPropertyAxiom)ax);
		else if(ax instanceof OWLFunctionalObjectPropertyAxiom)
			initialiseREL2FunctionalObjectProperty((OWLFunctionalObjectPropertyAxiom)ax);
		else if(ax instanceof OWLInverseFunctionalObjectPropertyAxiom)
			initialiseREL2InverseFunctionalProperty((OWLInverseFunctionalObjectPropertyAxiom)ax);
		else if(ax instanceof OWLSymmetricObjectPropertyAxiom)
			initialiseREL2SymmetricObjectPropertyAxiom((OWLSymmetricObjectPropertyAxiom)ax);
		else if(ax instanceof OWLAsymmetricObjectPropertyAxiom)
			initialiseREL2AsymmetricObjectPropertyAxiom((OWLAsymmetricObjectPropertyAxiom)ax);
		else if(ax instanceof OWLClassAssertionAxiom)
			initialiseREL2ClassAssertionAxiom((OWLClassAssertionAxiom)ax);
		else if(ax instanceof OWLObjectPropertyAssertionAxiom)
			initialiseREL2ObjectPropertyAssertion((OWLObjectPropertyAssertionAxiom)ax);
		else if(ax instanceof OWLFunctionalDataPropertyAxiom)
			initialiseREL2FunctionaDataProperty((OWLFunctionalDataPropertyAxiom)ax);
		else if(ax instanceof OWLDataPropertyAssertionAxiom)
			initialiseREL2DataPropertyAssertion((OWLDataPropertyAssertionAxiom)ax);
		else if(ax instanceof OWLDataPropertyRangeAxiom)
			initialiseREL2DataPropertyRange((OWLDataPropertyRangeAxiom)ax);
		else if(ax instanceof OWLDataPropertyDomainAxiom)
			initialiseREL2DataPropertyDomain((OWLDataPropertyDomainAxiom)ax);
		else if(ax instanceof OWLEquivalentDataPropertiesAxiom)
			initialiseREL2EquivalentDataProperties((OWLEquivalentDataPropertiesAxiom)ax);
		else if(ax instanceof OWLSameIndividualAxiom)
			initialiseREL2SameIndividual((OWLSameIndividualAxiom)ax);
		else if(ax instanceof OWLDifferentIndividualsAxiom)
			initialiseREl2DifferentIndividuals((OWLDifferentIndividualsAxiom)ax);
		else
		{
			if(debug)
			System.out.println("Wrong Axiom Type: "+ax);
		}
	}

	private void initialiseREl2DifferentIndividuals(
			OWLDifferentIndividualsAxiom ax) {
		// TODO Auto-generated method stub
		for(OWLIndividual indi1:ax.getIndividuals())
			for(OWLIndividual indi2:ax.getIndividuals())
				if(indi1 != indi2)
			{
					REXIndividualImpl i1 = getREL2Individual(indi1);
					REXIndividualImpl i2 = getREL2Individual(indi2);
//				REL2ClassExpressionImpl lhs = getREL2ObjectIntersectionOf(i1,i2);
//				REL2SubClassOfImpl newAxiom = getREL2SubClassOf(lhs, bottom);
//				newAxiom.addToContext();
//				lhs.LHS();
//				lhs.originalSuperClasses.add(bottom);
					i1.differentIndividuals.add(i2);
					i2.differentIndividuals.add(i1);
			}

	}

	private void initialiseREL2SameIndividual(OWLSameIndividualAxiom ax) {
		// TODO Auto-generated method stub
		List<REXIndividualImpl> indis = new ArrayList<REXIndividualImpl>();
		for(OWLIndividual i:ax.getIndividuals())
		{
			indis.add(getREL2Individual(i));
		}
		for(REXClassExpressionImpl lhs:indis)
		{
			for(REXClassExpressionImpl rhs:indis)
				if(lhs!=rhs)
				{
					initialiseREL2SubClassOf(lhs, rhs);
				}
			lhs.LHS();
		}

	}

	private void initialiseREL2EquivalentDataProperties(
			OWLEquivalentDataPropertiesAxiom ax) {
		// TODO Auto-generated method stub
		List<REXDataPropertyImpl> props = new ArrayList<REXDataPropertyImpl>();
		for(OWLDataPropertyExpression p:ax.getProperties())
			props.add(getREL2DataPropertyExpression(p));
		for(REXDataPropertyImpl subRole:props)
			for(REXDataPropertyImpl superRole:props)
				if(subRole != superRole)
					subRole.addSuperRole(superRole);

	}

	private void initialiseREL2DataPropertyDomain(OWLDataPropertyDomainAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl role = getREL2DataPropertyExpression(ax.getProperty());
		REXClassExpressionImpl domain = getREL2ClassExpression(ax.getDomain());
		REXClassExpressionImpl lhs = getREL2ObjectSomeValuesFrom(role, top);
		initialiseREL2SubClassOf(lhs, domain);

	}

	private void initialiseREL2DataPropertyRange(OWLDataPropertyRangeAxiom ax) {
		// TODO Auto-generated method stub
//		OWLDataSomeValuesFrom some = factory.getOWLDataSomeValuesFrom(ax.getProperty(), ax.getRange().getComplementNNF());
//		initialiseREL2SubClassOf(getREL2ClassExpression(some), bottom);
//		some = factory.getOWLObjectSomeValuesFrom(ax.getProperty().getInverseProperty(), factory.getOWLThing());
//		initialiseREL2SubClassOf(getREL2ClassExpression(some), getREL2ClassExpression(ax.getRange()));
		getREL2DataPropertyExpression(ax.getProperty()).ranges.add(getREL2DataRange(ax.getRange()));
	}

	private void initialiseREL2DataPropertyAssertion(
			OWLDataPropertyAssertionAxiom ax) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl sub = getREL2Individual(ax.getSubject());
		REXClassExpressionImpl obj = getREL2Literal(ax.getObject());
		REXObjectPropertyExpressionImpl role = getREL2DataPropertyExpression(ax.getProperty());
		REXClassExpressionImpl some = getREL2ObjectSomeValuesFrom(role, obj);
		initialiseREL2SubClassOf(sub,some);

	}

	private void initialiseREL2FunctionaDataProperty(
			OWLFunctionalDataPropertyAxiom ax) {
		// TODO Auto-generated method stub
//		REL2ClassExpressionImpl rhs = getREL2ObjectMaxCardinality(1,getREL2DataPropertyExpression(ax.getProperty()), top);
//		initialiseREL2SubClassOf(top, rhs);
		REXClassExpressionImpl lhs = getREL2ObjectMinCardinality(2,getREL2DataPropertyExpression(ax.getProperty()), top);
		if(false)
			initialiseREL2SubClassOf(lhs,bottom);
		getREL2DataPropertyExpression(ax.getProperty()).functional = true;

	}

	public REXDataPropertyImpl getREL2DataPropertyExpression(
			OWLDataPropertyExpression prop) {
		// TODO Auto-generated method stub
		REXDataPropertyImpl role;
//		if(prop.getSimplified() instanceof OWLObjectProperty)
//		{
//			role =  getREL2ObjectProperty(prop.getSimplified().asOWLObjectProperty());
//		}
//		else
//		{
//			OWLObjectInverseOf newrole = (OWLObjectInverseOf) prop.getSimplified();
//			REL2ObjectPropertyImpl inverse = (REL2ObjectPropertyImpl) getREL2ObjectProperty(newrole.getInverse().asOWLObjectProperty()); 
//			role = inverse.getInversePropertyExpression();
//			if(role == null)
//			role = new REL2InverseObjectPropertyImpl(inverse);
//			roles.put(prop, role);
//		}
		role = getREL2DataProperty(prop.asOWLDataProperty());
		return role;
	}

	private REXDataPropertyImpl getREL2DataProperty(
			OWLDataProperty prop) {
		// TODO Auto-generated method stub
		REXDataPropertyImpl newRole = (REXDataPropertyImpl) roles.get(prop);
		if(newRole == null)
		{
			newRole = new REXDataPropertyImpl(prop);
			roles.put(prop, newRole);
		}
		return newRole;
	}

	private void initialiseREL2AsymmetricObjectPropertyAxiom(
			OWLAsymmetricObjectPropertyAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl role = getREL2ObjectPropertyExpression(ax.getProperty());
		REXObjectPropertyExpressionImpl irole = getREL2ObjectPropertyExpression(ax.getProperty().getInverseProperty());
		role.disjoints.add(irole);
		irole.disjoints.add(role);
	}

	private void initialiseREL2ObjectPropertyAssertion(
			OWLObjectPropertyAssertionAxiom ax) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl sub = getREL2Individual(ax.getSubject());
		REXClassExpressionImpl obj = getREL2Individual(ax.getObject());
		REXObjectPropertyExpressionImpl role = getREL2ObjectPropertyExpression(ax.getProperty());
		REXClassExpressionImpl some = getREL2ObjectSomeValuesFrom(role, obj);
		initialiseREL2SubClassOf(sub,some);
	}

	private void initialiseREL2ClassAssertionAxiom(OWLClassAssertionAxiom ax) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl indi = getREL2Individual(ax.getIndividual());
		REXClassExpressionImpl clazz = getREL2ClassExpression(ax.getClassExpression());
		initialiseREL2SubClassOf(indi,clazz);
	}

	public REXIndividualImpl getREL2Individual(OWLIndividual individual) {
		// TODO Auto-generated method stub
		REXIndividualImpl newclazz = individuals.get(individual);
		if(newclazz == null)
		{
			newclazz = new REXIndividualImpl(individual);
			newclazz.original = false;
			individuals.put(individual, newclazz);
//			classExpressions.add(newclazz);
		}
		return newclazz;
	}

	private  void initialiseREL2DisjointClasses(OWLDisjointClassesAxiom ax) {
		// TODO Auto-generated method stub
		if(smallT && false)
			for(OWLClassExpression cls1:ax.getClassExpressions())
				for(OWLClassExpression cls2:ax.getClassExpressionsMinus(cls1))
				{
					REXClassExpressionImpl lhs = getREL2ClassExpression(cls1);
					REXClassExpressionImpl rhs = getREL2ClassExpression(cls2.getComplementNNF());
					initialiseREL2SubClassOf(lhs,rhs);
				}
		else
		for(OWLClassExpression cls1:ax.getClassExpressions())
			for(OWLClassExpression cls2:ax.getClassExpressionsMinus(cls1))
			{
				REXClassExpressionImpl lhs = getREL2ObjectIntersectionOf(factory.getOWLObjectIntersectionOf(cls1,cls2));
				REXSubClassOfImpl newAxiom = getREL2SubClassOf(lhs, bottom);
				newAxiom.addToContext();
				lhs.LHS();
				lhs.addOriginalSuperClasses(bottom);
			}
	}

	private  void initialiseREL2DisjointObjectPropertiesAxiom(
			OWLDisjointObjectPropertiesAxiom ax) {
		// TODO Auto-generated method stub
		Set<REXObjectPropertyExpressionImpl> roles = new HashSet<REXObjectPropertyExpressionImpl>();
		for(OWLObjectPropertyExpression prop1:ax.getProperties())
		{
			REXObjectPropertyExpressionImpl role1 = getREL2ObjectPropertyExpression(prop1);
			for(REXObjectPropertyExpressionImpl role2:roles)
				if(!role1.equals(role2))
				{
					role1.disjoints.add(role2);
					role2.disjoints.add(role1);
				}
			roles.add(role1);
		}
	}


	private  void initialiseREL2EquivalentClasses(
			OWLEquivalentClassesAxiom ax) {
		// TODO Auto-generated method stub
		List<REXClassExpressionImpl> clss = new ArrayList<REXClassExpressionImpl>();
		for(OWLClassExpression c:ax.getClassExpressions())
		{
			clss.add(getREL2ClassExpression(c));
		}
		for(REXClassExpressionImpl lhs:clss)
		{
			for(REXClassExpressionImpl rhs:clss)
				if(lhs!=rhs)
				{
					initialiseREL2SubClassOf(lhs, rhs);
				}
			lhs.LHS();
		}
			
	}

	private  void initialiseREL2EquivalentObjectProperties(
			OWLEquivalentObjectPropertiesAxiom ax) {
		// TODO Auto-generated method stub
		List<REXObjectPropertyExpressionImpl> props = new ArrayList<REXObjectPropertyExpressionImpl>();
		for(OWLObjectPropertyExpression p:ax.getProperties())
			props.add(getREL2ObjectPropertyExpression(p));
		for(REXObjectPropertyExpressionImpl subRole:props)
			for(REXObjectPropertyExpressionImpl superRole:props)
				if(subRole != superRole)
					subRole.addSuperRole(superRole);
	}
	
	private  void initialiseREL2FunctionalObjectProperty(
			OWLFunctionalObjectPropertyAxiom ax) {
		// TODO Auto-generated method stub
//		REL2ClassExpressionImpl rhs = getREL2ObjectMaxCardinality(1,getREL2ObjectPropertyExpression(ax.getProperty()), top);
//		initialiseREL2SubClassOf(top, rhs);
		REXClassExpressionImpl lhs = getREL2ObjectMinCardinality(2,getREL2ObjectPropertyExpression(ax.getProperty()), top);
		initialiseREL2SubClassOf(lhs,bottom);
		getREL2ObjectPropertyExpression(ax.getProperty()).functional = true;
	}

	private  void initialiseREL2InverseFunctionalProperty(
			OWLInverseFunctionalObjectPropertyAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl inverse = getREL2ObjectPropertyExpression(ax.getProperty().getInverseProperty());
		REXClassExpressionImpl lhs = getREL2ObjectMaxCardinality(1,inverse, top);
		initialiseREL2SubClassOf(lhs, top);
		
	}
	
	private  void initialiseREL2InverseObjectProperties(
			OWLInverseObjectPropertiesAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl prop1 = getREL2ObjectPropertyExpression(ax.getFirstProperty());
		REXObjectPropertyExpressionImpl prop2 = getREL2ObjectPropertyExpression(ax.getSecondProperty());
		
		REXObjectPropertyExpressionImpl p1inverse = getREL2ObjectPropertyExpression(ax.getFirstProperty().getInverseProperty());
		REXObjectPropertyExpressionImpl p2inverse = getREL2ObjectPropertyExpression(ax.getSecondProperty().getInverseProperty());
		
		prop1.addSuperRole(p2inverse);
		prop2.addSuperRole(p1inverse);
		p1inverse.addSuperRole(prop2);
		p2inverse.addSuperRole(prop1);
	}
	
	private  void initialiseREL2IrreflexiveObjectProperty(
			OWLIrreflexiveObjectPropertyAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl role = getREL2ObjectPropertyExpression(ax.getProperty());
		role.irrflexive = true;
	}

	private  void initialiseREL2ObjectPropertyDomain(
			OWLObjectPropertyDomainAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl role = getREL2ObjectPropertyExpression(ax.getProperty());
		REXClassExpressionImpl domain = getREL2ClassExpression(ax.getDomain());
		REXClassExpressionImpl lhs = getREL2ObjectSomeValuesFrom(role, top);
		initialiseREL2SubClassOf(lhs, domain);
	}

	private  void initialiseREL2ObjectPropertyRange(
			OWLObjectPropertyRangeAxiom ax) {
		// TODO Auto-generated method stub
		OWLObjectSomeValuesFrom some = factory.getOWLObjectSomeValuesFrom(ax.getProperty(), ax.getRange().getComplementNNF());
		initialiseREL2SubClassOf(getREL2ClassExpression(some), bottom);
		if(smallT && false)
		{
//		REL2ObjectPropertyExpressionImpl role = getREL2ObjectPropertyExpression(ax.getProperty());
//		REL2ClassExpressionImpl range = getREL2ClassExpression(ax.getRange());
		REXClassExpressionImpl rhs = getREL2ObjectComplementOf(getREL2ClassExpression(some));
		initialiseREL2SubClassOf(top, rhs);
		}
		some = factory.getOWLObjectSomeValuesFrom(ax.getProperty().getInverseProperty(), factory.getOWLThing());
		initialiseREL2SubClassOf(getREL2ClassExpression(some), getREL2ClassExpression(ax.getRange()));
//		role.ranges.add(range);
	}

	private  void initialiseREL2SubClassOf(OWLSubClassOfAxiom ax) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl lhs = getREL2ClassExpression(ax.getSubClass());
		REXClassExpressionImpl rhs = getREL2ClassExpression(ax.getSuperClass());
		initialiseREL2SubClassOf(lhs, rhs);
//		lhs = getREL2ClassExpression(ax.getSuperClass().getComplementNNF());
//		rhs = getREL2ClassExpression(ax.getSubClass().getComplementNNF());
//		initialiseREL2SubClassOf(lhs,rhs);
	}

	private  void initialiseREL2SubClassOf(REXClassExpressionImpl lhs, REXClassExpressionImpl rhs){
		REXSubClassOfImpl newAxiom = getREL2SubClassOf(lhs,rhs);
		newAxiom.addToContext();
		lhs.LHS();
		lhs.addOriginalSuperClasses(rhs);
		if(false)
		if(lhs.complement != null && lhs.complement instanceof REXObjectIntersectionOfImpl)
		{
			REXObjectIntersectionOfImpl and = (REXObjectIntersectionOfImpl) lhs.complement;
			for(REXClassExpressionImpl conjunct:and.getEntities())
				if(conjunct.complement != null)
				{
					getREL2SubClassOf(conjunct.complement, rhs);
					conjunct.complement.addOriginalSuperClasses(rhs);
				}
		}
	}

	private  void initialiseREL2SubPropertyChainOf(
			OWLSubPropertyChainOfAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl superRole = getREL2ObjectPropertyExpression(ax.getSuperProperty());
		List<OWLObjectPropertyExpression> subs = ax.getPropertyChain();
		if(subs.size() == 1)
		{
			REXObjectPropertyExpressionImpl subRole = getREL2ObjectPropertyExpression(subs.get(0));
			subRole.addSuperRole(superRole);
		}
		else
		{
			REXObjectPropertyExpressionImpl sub1 = getREL2ObjectPropertyExpression(subs.remove(0));
			REXObjectPropertyExpressionImpl sub2 = getREL2Name4RoleChain(subs);
			initialiseREL2SubPropertyChainOf(sub1, sub2, superRole);
		}
	}

	private  void initialiseREL2SubPropertyChainOf(REXObjectPropertyExpressionImpl ... roles){
//		if(roles.length > 3)
//			System.out.println("incorrect size of role chain!!!");
		Set<REXObjectPropertyExpressionImpl> chains = roles[0].chains.get(roles[1]);
		if(chains == null)
		{
			chains = new HashSet<REXObjectPropertyExpressionImpl>();
			roles[0].chains.put(roles[1], chains);
		}
		chains.add(roles[2]);
	}

	private void initialiseREL2SymmetricObjectPropertyAxiom(
			OWLSymmetricObjectPropertyAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl role = getREL2ObjectPropertyExpression(ax.getProperty());
		REXObjectPropertyExpressionImpl irole = getREL2ObjectPropertyExpression(ax.getProperty().getInverseProperty());
		role.addSuperRole(irole);
		irole.addSuperRole(role);
	}

	private  void initialiseREL2TransitiveObjectProperty(
			OWLTransitiveObjectPropertyAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl prop = getREL2ObjectPropertyExpression(ax.getProperty());
		initialiseREL2SubPropertyChainOf(prop, prop, prop);
	}

	private  void initialiseSubObjectPropertyOf(
			OWLSubObjectPropertyOfAxiom ax) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl subRole = getREL2ObjectPropertyExpression(ax.getSubProperty());
		REXObjectPropertyExpressionImpl superRole = getREL2ObjectPropertyExpression(ax.getSuperProperty());
		subRole.addSuperRole(superRole);
	}

	public  REXObjectSomeValuesFromImpl testREL2ObjectSomeValuesFrom(
			REXObjectPropertyExpressionImpl role,
			REXClassExpressionImpl filler) {
		// TODO Auto-generated method stub
		return role.somes.get(filler);
	}

	public REXClassExpressionImpl testREL2ObjectAllValuesFrom(
			REXObjectPropertyExpressionImpl role,
			REXClassExpressionImpl filler) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl all = role.alls.get(filler);
		if(all == null && filler.complement != null)
			all = role.somes.get(filler.complement);
		if(all != null)
			return all.complement;
		else
			return null;
	}

	public void orderCardin() {
		// TODO Auto-generated method stub
		
	}

}
