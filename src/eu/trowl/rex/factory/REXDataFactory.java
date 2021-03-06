package eu.trowl.rex.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLPropertyExpression;

import eu.trowl.rex.absorption.AbsorptionVisitor;
import eu.trowl.rex.absorption.ConceptAbsorption;
import eu.trowl.rex.absorption.RoleAbsorption;
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
import eu.trowl.rex.model.implementations.REXObjectMaxCardinalityImpl;
import eu.trowl.rex.model.implementations.REXObjectMinCardinalityImpl;
import eu.trowl.rex.model.implementations.REXObjectPropertyExpressionImpl;
import eu.trowl.rex.model.implementations.REXObjectPropertyImpl;
import eu.trowl.rex.model.implementations.REXObjectSomeValuesFromImpl;
import eu.trowl.rex.model.implementations.REXObjectUnionOfImpl;
import eu.trowl.rex.model.implementations.REXSubClassOfImpl;
import eu.trowl.rex.util.REXReasonerConfiguration;

public class REXDataFactory {
	//	 public List<REL2ClassExpressionImpl> classExpressions = new ArrayList<REL2ClassExpressionImpl>();
	//	 public int conceptID = 1;
	
	int FCid = 1;

	OWL2DLAxiomVisitor axVisitor = new OWL2DLAxiomVisitor(this);
	REXClassExpressionBuilder cEBuilder = new REXClassExpressionBuilder(this);
	REXDataRangeBuilder dRBuilder = new REXDataRangeBuilder(this);
	REXPropertyExpressionBuilder pEBuilder = new REXPropertyExpressionBuilder(this);
	public AbsorptionVisitor absorber;

	public HashMap<OWLClassExpression, REXClassImpl> concepts;
	public HashSet<REXClassImpl> freshConcepts;
	public HashMap<OWLDataRange, REXDatatypeImpl> datatypes;
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

	public boolean debug = false;


	OWLDataFactory factory;

	public boolean consistency = true;
	
	public HashSet<REXObjectUnionOfImpl> globalConstraints = new HashSet<REXObjectUnionOfImpl>();
	public HashMap<REXClassExpressionImpl, HashSet<REXClassExpressionImpl>> binaryUnfoldableSuperClass = new HashMap<REXClassExpressionImpl, HashSet<REXClassExpressionImpl>>();

	public OWLOntology onto;

	public REXDataFactory() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class cl = Class.forName(REXReasonerConfiguration.absorption);
		Constructor con = cl.getConstructor(REXDataFactory.class);
		absorber = (AbsorptionVisitor) con.newInstance(this);

	}

	public  REXClassExpressionImpl getREXClassExpression(OWLClassExpression clazz){
		REXClassExpressionImpl cls = clazz.getNNF().accept(cEBuilder);
		if(smallT && cls.complement == null)
			getREXObjectComplementOf(cls);
		return cls;
	}


	public  REXObjectPropertyExpressionImpl getREXObjectPropertyExpression(OWLObjectPropertyExpression prop){
		return prop.getSimplified().accept(pEBuilder);
	}


	public REXDataPropertyImpl getREXDataPropertyExpression(
			OWLDataPropertyExpression prop) {
		return (REXDataPropertyImpl) prop.accept(pEBuilder);
		// TODO Auto-generated method stub
	}

	public REXIndividualImpl getREXIndividual(OWLIndividual individual) {
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

	public  REXClassExpressionImpl getREXObjectIntersectionOf(List<OWLClassExpression> operands, int next){

		if(next == operands.size() -1)
			return getREXClassExpression(operands.get(next));

		REXClassExpressionImpl leftConjunct = getREXClassExpression(operands.get(next));
		REXClassExpressionImpl rightConjunct = getREXObjectIntersectionOf(operands, next+1);
		
		if(leftConjunct == bottom || rightConjunct == bottom)
			return bottom;
		if(leftConjunct == top)
			return rightConjunct;
		if(rightConjunct == top)
			return leftConjunct;

		return getREXObjectIntersectionOf(leftConjunct, rightConjunct);

	}

	public REXClassExpressionImpl getREXObjectUnionOf(
			List<OWLClassExpression> operands, int next) {
		// TODO Auto-generated method stub
		if(next == operands.size() -1)
			return getREXClassExpression(operands.get(next));
		
		
		
		REXClassExpressionImpl leftDisjunct = getREXClassExpression(operands.get(next));
		REXClassExpressionImpl rightDisjunct = getREXObjectUnionOf(operands, next+1);

		if(leftDisjunct == bottom)
			return rightDisjunct;
		if(rightDisjunct == bottom)
			return leftDisjunct;
		if(leftDisjunct == top || rightDisjunct == top)
			return top;
		return getREXObjectUnionOf(leftDisjunct, rightDisjunct);
	}

	public  REXClassExpressionImpl getREXObjectSomeValuesFrom(OWLObjectPropertyExpression property, OWLClassExpression clazz){
		REXClassExpressionImpl	filler = getREXClassExpression(clazz);
		if(filler == bottom)
			return bottom;
		REXObjectPropertyExpressionImpl role = getREXObjectPropertyExpression(property);

		return getREXObjectSomeValuesFrom(role, filler);
	}



	public REXClassExpressionImpl getREXObjectAllValuesFrom(
			OWLObjectPropertyExpression property, OWLClassExpression clazz) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl filler = getREXClassExpression(clazz);
		if(filler == top)
			return top;
		REXObjectPropertyExpressionImpl role = getREXObjectPropertyExpression(property);
		
		return getREXObjectAllValuesFrom(role, filler);
	}

	public REXClassExpressionImpl getREXObjectMaxCardinality(int cardinality,
			OWLObjectPropertyExpression property, OWLClassExpression filler) {
		// TODO Auto-generated method stub
		if(cardinality < 0)
			return bottom;
		if(cardinality == 0)
			return getREXObjectAllValuesFrom(property, filler.getComplementNNF());
		return getREXObjectMaxCardinality(cardinality, getREXObjectPropertyExpression(property), getREXClassExpression(filler));
	}


	public  REXSubClassOfImpl getREXSubClassOf(
			REXClassExpressionImpl lhs, REXClassExpressionImpl rhs) {
		// TODO Auto-generated method stub
		REXSubClassOfImpl axiom = null;
		if(rhs instanceof REXObjectSomeValuesFromImpl)
		{
			REXObjectSomeValuesFromImpl some = (REXObjectSomeValuesFromImpl) rhs;
			REXObjectPropertyExpressionImpl role = some.getProperty();
			REXClassExpressionImpl filler = some.getFiller();
			axiom = getREXSubClassOfSomeValuesFrom(lhs, role, filler);
			axiom.link = true;
		}
		else
		{
			lhs.subAxioms.putIfAbsent(rhs, new REXSubClassOfImpl(lhs, rhs));
			axiom = lhs.subAxioms.get(rhs);
		}

		return axiom;
	}


	public  REXSubClassOfImpl getREXSubClassOfSomeValuesFrom(
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

	public  REXSubClassOfImpl getstrictREXSubClassOf(
			REXClassExpressionImpl lhs, REXClassExpressionImpl rhs) {
		// TODO Auto-generated method stub
		lhs.subAxioms.putIfAbsent(rhs, new REXSubClassOfImpl(lhs, rhs));
		return lhs.subAxioms.get(rhs);
	}

	public  void initialise(OWLDataFactory datafactory){
		concepts = new HashMap<OWLClassExpression, REXClassImpl>();
		freshConcepts = new HashSet<REXClassImpl>();
		contexts = new HashSet<REXClassExpressionImpl>();
		datatypes = new HashMap<OWLDataRange, REXDatatypeImpl>();
		individuals = new HashMap<OWLIndividual, REXIndividualImpl>();
		literals = new HashMap<OWLLiteral, REXLiteralImpl>();
		roles = new HashMap<OWLPropertyExpression, REXObjectPropertyExpressionImpl>();
		rolechainNames = new HashMap<List<OWLObjectPropertyExpression>, REXObjectPropertyExpressionImpl>();
		factory = datafactory;
		//		concepts.put(datafactory.getOWLThing(), REL2PredefinedClassExpressions.top);
		//		concepts.put(datafactory.getOWLNothing(), REL2PredefinedClassExpressions.bottom);
		top = (REXClassImpl) getREXClassExpression(datafactory.getOWLThing());
		//		top.original = true;
		bottom = (REXClassImpl) getREXClassExpression(datafactory.getOWLNothing());
		top.complement = bottom;
		bottom.complement = top;
		//		bottom.original = true;
		bottom.satisfiable = false;
		//		classExpressions.add(REL2PredefinedClassExpressions.top);
	}


	public  void initialiseAxiom(OWLLogicalAxiom ax) {
		// TODO Auto-generated method stub
		ax.accept(axVisitor);
	}

	public void orderCardin() {
		// TODO Auto-generated method stub

	}


	public REXClassExpressionImpl testREXObjectAllValuesFrom(
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

	public  REXObjectSomeValuesFromImpl testREXObjectSomeValuesFrom(
			REXObjectPropertyExpressionImpl role,
			REXClassExpressionImpl filler) {
		// TODO Auto-generated method stub
		return role.somes.get(filler);
	}


	REXClassExpressionImpl getREXDataIntersectionOf(
			List<OWLDataRange> operands) {
		// TODO Auto-generated method stub
		if(operands.size() == 1)
			return getREXDataRange(operands.get(0));

		REXClassExpressionImpl leftConjunct = getREXDataRange(operands.remove(0));
		REXClassExpressionImpl rightConjunct = getREXDataIntersectionOf(operands);

		if(leftConjunct == bottom || rightConjunct == bottom)
			return bottom;
		if(leftConjunct == top)
			return rightConjunct;
		if(rightConjunct == top)
			return leftConjunct;

		return getREXObjectIntersectionOf(leftConjunct, rightConjunct);
	}

	REXClassExpressionImpl getREXDataRange(OWLDataRange dataRange) {
		// TODO Auto-generated method stub
		return dataRange.accept(dRBuilder);
	}


	REXClassExpressionImpl getREXLiteral(OWLLiteral literal) {
		// TODO Auto-generated method stub
		REXLiteralImpl newclazz = literals.get(literal);
		if(newclazz == null)
		{
			newclazz = new REXLiteralImpl(literal);
			newclazz.original = false;
			REXClassExpressionImpl type = getREXDataRange(literal.getDatatype());
			newclazz.superClasses.add(type);
			newclazz.addOriginalSuperClasses(type);
			literals.put(literal, newclazz);
			//			classExpressions.add(newclazz);
		}
		return newclazz;
	}


	REXObjectPropertyExpressionImpl getREXName4RoleChain(List<OWLObjectPropertyExpression> subs){

		if(subs.size() == 1)
			return getREXObjectPropertyExpression(subs.get(0));

		REXObjectPropertyExpressionImpl newName = rolechainNames.get(subs);
		if(newName == null)
		{
			newName = new REXObjectPropertyImpl();
			rolechainNames.put(subs, newName);
		}
		REXObjectPropertyExpressionImpl newSub1 = getREXObjectPropertyExpression(subs.get(0));
		REXObjectPropertyExpressionImpl newSub2 = getREXName4RoleChain(subs.subList(1, subs.size()));
		initialiseREXSubPropertyChainOf(newSub1, newSub2, newName);
		return newName;
	}

	public REXClassExpressionImpl getREXObjectComplementOf(
			REXClassExpressionImpl complement) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl cls = complement.complement;
		if(cls == null)
		{
			cls = complement.getComplement(this);
		}
//		if(complement instanceof REXObjectComplementOfImpl)
//			return ((REXObjectComplementOfImpl)complement).getClassExpression();
//		REXClassExpressionImpl cls = complement.complement;
//		if(cls == null)
//		{
//			cls = new REXObjectComplementOfImpl(complement);
//		}
		return cls;
	}

	public REXClassExpressionImpl getREXObjectIntersectionOf(REXClassExpressionImpl ... conjuncts){
		//		if(conjuncts.length>2)
		//			System.out.println("Incorrect number of conjuncts!");
		if(conjuncts[0] == bottom || conjuncts[1] == bottom)
			return bottom;
		if(conjuncts[0] == top)
			return conjuncts[1];
		if(conjuncts[1] == top)
			return conjuncts[0];

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

	public REXClassExpressionImpl getREXObjectMinCardinality(
			int cardinality, REXObjectPropertyExpressionImpl role,
			REXClassExpressionImpl filler) {
		// TODO Auto-generated method stub
		if(cardinality <= 0)
			return top;
		if(cardinality == 1)
			return getREXObjectSomeValuesFrom(role, filler);
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
			newMin.getProperty().somes.put(newMin.getFiller(), newMin);
			//			classExpressions.add(newMin);
		}
		return newMin;
	}

	public REXObjectSomeValuesFromImpl getREXObjectSomeValuesFrom(REXObjectPropertyExpressionImpl role, REXClassExpressionImpl filler){

		REXObjectSomeValuesFromImpl newSome = role.somes.get(filler);
		if(newSome == null)
		{
			newSome = new REXObjectSomeValuesFromImpl(role, filler);
			role.somes.put(filler, newSome);
			//			classExpressions.add(newSome);
		}
		return newSome;

	}

	void initialiseREXSubPropertyChainOf(REXObjectPropertyExpressionImpl ... roles){
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
	private  REXClassExpressionImpl getREXApproximationName(
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
	
	public REXClassImpl getREXApproximationName(REXClassExpressionImpl exp){
		REXClassImpl cls = new REXClassImpl(IRI.create("http://rex/C"+FCid));
		FCid++;
		cls.original = false;
		freshConcepts.add(cls);
		return cls;
	}

	private REXClassExpressionImpl getREXApproximationName(
			OWLDataRange dataRange) {
		// TODO Auto-generated method stub
		REXDatatypeImpl cls = datatypes.get(dataRange);
		if(cls == null)
		{
			cls = new REXDatatypeImpl();
			cls.original = false;
			datatypes.put(dataRange, cls);
		}
		return cls;
	}

//	private REXDataPropertyImpl getREL2DataProperty(
//			OWLDataProperty prop) {
//		// TODO Auto-generated method stub
//		REXDataPropertyImpl newRole = (REXDataPropertyImpl) roles.get(prop);
//		if(newRole == null)
//		{
//			newRole = new REXDataPropertyImpl(prop);
//			roles.put(prop, newRole);
//		}
//		return newRole;
//	}


	public REXObjectAllValuesFromImpl getREXObjectAllValuesFrom(
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

//	private  REXObjectPropertyExpressionImpl getREL2ObjectProperty(OWLObjectProperty prop){
//		REXObjectPropertyExpressionImpl newRole = roles.get(prop);
//		if(newRole == null)
//		{
//			newRole = new REXObjectPropertyImpl(prop);
//			roles.put(prop, newRole);
//		}
//		return newRole;
//
//	}


//	REXClassExpressionImpl getREXObjectUnionOf(
//			List<OWLClassExpression> operands) {
//		// TODO Auto-generated method stub
//		if(operands.size() == 1)
//			return getREXClassExpression(operands.get(0));
//
//		REXClassExpressionImpl leftConcept = getREXClassExpression(operands.remove(0));
//		REXClassExpressionImpl rightConcept = getREXObjectUnionOf(operands);
//
//		return getREXObjectUnionOf(leftConcept, rightConcept);
//	}


	public  REXClassExpressionImpl getREXObjectUnionOf(REXClassExpressionImpl ... concepts){
		//		if(concepts.length>2)
		//			System.out.println("Incorrect number of union concepts!");
		if(concepts[0] == bottom)
			return concepts[1];
		if(concepts[1] == bottom)
			return concepts[0];
		if(concepts[0] == top || concepts[1] == top)
			return top;

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

	public REXClassExpressionImpl getREXObjectMaxCardinality(
				int cardinality,
				REXObjectPropertyExpressionImpl role,
				REXClassExpressionImpl filler) {
			// TODO Auto-generated method stub
	//		REXClassExpressionImpl min = getREXObjectMinCardinality(cardinality+1,role,filler);
	//		REXClassExpressionImpl max = min.complement;
	//		if(max == null)
	//		{
	//			max = getREXObjectComplementOf(min);
	//			min.LHS();
	//		}
			HashMap<REXObjectPropertyExpressionImpl, REXObjectMaxCardinalityImpl> maxs = filler.maxs.get(cardinality);
			if(maxs == null)
			{
				maxs = new HashMap<REXObjectPropertyExpressionImpl, REXObjectMaxCardinalityImpl>();
				filler.maxs.put(cardinality, maxs);
			}
			REXObjectMaxCardinalityImpl newMax = maxs.get(role);
			if(newMax == null)
			{
				newMax = new REXObjectMaxCardinalityImpl(cardinality, role, filler);
				maxs.put(role, newMax);
//				newMax.getProperty().somes.put(newMax.getFiller(), newMax);
				//			classExpressions.add(newMin);
			}
			return newMax;
		}


	private  REXSubClassOfImpl getREXSubClassOfAllValuesFrom(
			REXClassExpressionImpl C,
			REXClassExpressionImpl D) {
		// TODO Auto-generated method stub
		C.subAllAxioms.putIfAbsent(D, new REXSubClassOfImpl(C,D));
		D.RHS();
		return C.subAllAxioms.get(D);
	}

	private  REXSubClassOfImpl getREXSubClassOfMax1Cardinality(
			REXClassExpressionImpl C, REXClassExpressionImpl D) {
		// TODO Auto-generated method stub
		C.subMax1Axioms.putIfAbsent(D, new REXSubClassOfImpl(C,D));
		D.RHS();
		return C.subMax1Axioms.get(D);
	}


	public REXObjectPropertyExpressionImpl getREXInverseObjectProperty(
			REXObjectPropertyExpressionImpl role) {
		// TODO Auto-generated method stub
		REXObjectPropertyExpressionImpl inverse = role.getInversePropertyExpression();
		if(inverse == null)
			inverse = new REXInverseObjectPropertyImpl((REXObjectPropertyImpl) role);
		return inverse;
	}

	public REXClassExpressionImpl getREXObjectIntersectionOf(List<REXClassImpl> clss) {
		// TODO Auto-generated method stub
		if(clss.size() == 1)
			return clss.get(0);
		REXClassExpressionImpl head = clss.remove(0);
		REXClassExpressionImpl tail = getREXObjectIntersectionOf(clss);
		
		if(head == bottom || tail == bottom)
			return bottom;
		if(head == top)
			return tail;
		if(tail == top)
			return head;
		
		return getREXObjectIntersectionOf(head, tail);
	}

	public REXClassExpressionImpl getREXObjectUnionOf(List<REXClassExpressionImpl> clss) {
		// TODO Auto-generated method stub
		if(clss.size() == 1)
			return clss.get(0);
		REXClassExpressionImpl head = clss.remove(0);
		REXClassExpressionImpl tail = getREXObjectUnionOf(clss);
	
		if(head == bottom)
			return tail;
		if(tail == bottom)
			return head;
		if(head == top || tail == top)
			return top;

		return getREXObjectUnionOf(head, tail);
	}

	public boolean testUniqueness(REXClassImpl A) {
		// TODO Auto-generated method stub
		if(A.original)
		{
			OWLClass cls = factory.getOWLClass(A.getIRI());
			if(onto.getSubClassAxiomsForSubClass(cls).size() + onto.getEquivalentClassesAxioms(cls).size() >1)
				return false;
		}
		return true;
	}



}
