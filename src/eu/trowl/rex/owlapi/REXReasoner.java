package eu.trowl.rex.owlapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.AxiomNotInProfileException;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.ClassExpressionNotInProfileException;
import org.semanticweb.owlapi.reasoner.FreshEntitiesException;
import org.semanticweb.owlapi.reasoner.FreshEntityPolicy;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;
import org.semanticweb.owlapi.reasoner.IndividualNodeSetPolicy;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.TimeOutException;
import org.semanticweb.owlapi.reasoner.UnsupportedEntailmentTypeException;
import org.semanticweb.owlapi.reasoner.impl.OWLClassNode;
import org.semanticweb.owlapi.reasoner.impl.OWLClassNodeSet;
import org.semanticweb.owlapi.reasoner.impl.OWLDataPropertyNode;
import org.semanticweb.owlapi.reasoner.impl.OWLDataPropertyNodeSet;
import org.semanticweb.owlapi.reasoner.impl.OWLNamedIndividualNode;
import org.semanticweb.owlapi.reasoner.impl.OWLNamedIndividualNodeSet;
import org.semanticweb.owlapi.reasoner.impl.OWLObjectPropertyNode;
import org.semanticweb.owlapi.reasoner.impl.OWLObjectPropertyNodeSet;
import org.semanticweb.owlapi.util.Version;

import eu.trowl.rex.factory.REXDataFactory;
import eu.trowl.rex.model.implementations.REXClassExpressionImpl;
import eu.trowl.rex.model.implementations.REXClassImpl;
import eu.trowl.rex.model.implementations.REXIndividualImpl;
import eu.trowl.rex.reasoner.Reasoner;

/** 
 * @author Yuan Ren
 * @version 2013-07-17:
 */
public class REXReasoner implements OWLReasoner, OWLOntologyChangeListener{
/**
 * @version 2013-07-17:
 */
	
	protected static REXDataFactory rel2Factory = null;
	boolean TBox_Classified = false;
	boolean ABox_Classified = false;

	// whether a buffered reasoner
	public boolean bufferred = true;

	// configuration
	public OWLReasonerConfiguration configuration = null;
	public ArrayList<OWLNamedIndividual> inconsistentIndividuals = new ArrayList<OWLNamedIndividual>();
	public final OWLOntologyManager manager;
//OWL-BGP
	protected boolean bgp = false;
	protected List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
//	protected CombinedClassifier classifier;
	// Set Disjointness
	protected boolean disjoint = false;
//	protected Ontology elcontology;
	protected final OWLDataFactory factory;
	protected HashSet<REXClassImpl> leaf = new HashSet<REXClassImpl>();
	protected OWLClassNodeSet leaves = new OWLClassNodeSet();
	// Meta On
	protected boolean MetaOn = false;
	protected OWLOntology ontology = null;
	
	protected OWLClassNodeSet satisfiable = new OWLClassNodeSet();
	// buffers
	protected Set<OWLAxiom> toadd = new HashSet<OWLAxiom>();
	
	protected Set<OWLAxiom> toremove = new HashSet<OWLAxiom>();
	
	protected OWLClassNode unsats = new OWLClassNode();
	
	protected Version version = new Version(0,8,0,0);
	
	Set<InferenceType> current_Precompute = new HashSet<InferenceType>();
	HashSet<REXClassImpl> satis = new HashSet<REXClassImpl>();
	Set<InferenceType> supported_Precompute = new HashSet<InferenceType>();
	Set<InferenceType> supported_TBox_Precompute = new HashSet<InferenceType>();

	// ROIQ
	public REXReasoner(OWLOntologyManager manager, OWLOntology ontology, boolean bgp, boolean MetaOn, boolean disjoint) {
		super();
//		timer.start();
		this.manager = manager;
		this.factory = manager.getOWLDataFactory();
        this.ontology = ontology;
        this.bgp = bgp;
        this.MetaOn = MetaOn;
        this.disjoint = disjoint;
//        nominalfree = NF;
        supported_Precompute.add(InferenceType.CLASS_HIERARCHY);
        supported_Precompute.add(InferenceType.DATA_PROPERTY_HIERARCHY);
        supported_Precompute.add(InferenceType.DISJOINT_CLASSES);
        supported_Precompute.add(InferenceType.OBJECT_PROPERTY_HIERARCHY);
        supported_Precompute.add(InferenceType.CLASS_ASSERTIONS);
        supported_Precompute.add(InferenceType.DIFFERENT_INDIVIDUALS);
        supported_Precompute.add(InferenceType.OBJECT_PROPERTY_ASSERTIONS);
        supported_Precompute.add(InferenceType.SAME_INDIVIDUAL);
        
        supported_TBox_Precompute.add(InferenceType.CLASS_HIERARCHY);
        supported_TBox_Precompute.add(InferenceType.DATA_PROPERTY_HIERARCHY);
        supported_TBox_Precompute.add(InferenceType.DISJOINT_CLASSES);
        supported_TBox_Precompute.add(InferenceType.OBJECT_PROPERTY_HIERARCHY);
        
        if(bgp)
        	current_Precompute.add(InferenceType.CLASS_ASSERTIONS);

//        initialiseClassifier();
        loadOntology();

    }

	public void classify(boolean TBox, boolean ABox) throws InterruptedException{
			// TODO Auto-generated method stub
//		classifier.ontology = elcontology;
		if(!TBox && !ABox)
			return;
		rel2Factory.consistency = true;
//			classifier.completion(TBox, ABox);
		reasoner.classify(TBox,ABox);
//			if(Thread.currentThread().isInterrupted())
//				return;
//		System.out.println("classified!");
			if(!rel2Factory.consistency)
			{
//				System.out.println("Ontology is be inconsistent!");
				TBox_Classified = true;
				ABox_Classified = true;

//				return;
				throw new InconsistentOntologyException();
				
			}
			
			if(TBox)
				reasoner.TBoxPostprocessing();
			if(ABox)
				reasoner.ABoxPostprocessing();
			
//			System.out.println("post-processed!");
	
	//		System.out.println("Materialised.");
			
			// only perform the below if classified TBox the first time
			if(TBox && !TBox_Classified)
			{
				for(REXClassImpl cls:rel2Factory.concepts.values())
					if(cls.original && cls.satisfiable && !satis.contains(cls))
						satis.addAll(cls.equivalence);
//			satis = new HashSet<REL2ClassImpl>(rel2Factory.concepts.values());
			REXClassImpl bot = rel2Factory.bottom;
//			satis.removeAll(bot.equivalence);
			
			leaves = new OWLClassNodeSet();
			satisfiable = new OWLClassNodeSet();
			
			leaf = new HashSet<REXClassImpl>(satis);
			
			
			for(REXClassImpl atom:satis)
			{
				if(!leaf.contains(atom))
					continue;
//				REL2ClassImpl atom = (REL2ClassImpl) REL2ClassImpl;
				for(REXClassExpressionImpl sub:atom.superClasses)
				{
				if(sub instanceof REXClassImpl && !atom.equivalence.contains(sub) && leaf.contains(sub))
				{
					leaf.removeAll(sub.asREXClassImpl().equivalence);
				}
				}
			}
			
	//		OWLClassNode botnode = new OWLClassNode();
	//		Timer timer = new Timer("Bot equivalence construction");
	//		timer.start();
			for(REXClassImpl bsc:bot.equivalence)
			{
				if(bsc.original)
					unsats.add(factory.getOWLClass(bsc.getIRI()));
			}
	//		timer.stop();
	//		System.out.println(timer);
			
			while(satis.size()>0)
			{
				REXClassImpl sub = satis.iterator().next();
				satis.removeAll(sub.equivalence);
				OWLClassNode ancestor = new OWLClassNode();
				for(REXClassImpl eq:sub.equivalence)
					if(eq.original)
						ancestor.add(factory.getOWLClass(eq.getIRI()));							
				if(ancestor.getSize() > 0)
					satisfiable.addNode(ancestor);
			}
	
			while(leaf.size()>0)
			{
				REXClassImpl sub = leaf.iterator().next();
				leaf.removeAll(sub.equivalence);
				OWLClassNode ancestor = new OWLClassNode();
				for(REXClassImpl eq:sub.equivalence)
					if(eq.original)
						ancestor.add(factory.getOWLClass(eq.getIRI()));							
				if(ancestor.getSize() > 0)
					leaves.addNode(ancestor);
			}
	
			TBox_Classified = true;
			}
			if(ABox)
				ABox_Classified = true;
		}

//	public int countersubsumers(){
//		return elcontology.countsubsumers();
//	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		manager.removeOntologyChangeListener(this);
	}

//	protected boolean entail(OWLClassAssertionAxiom axiom){
////		REL2ClassExpressionImpl sub = getDescription(axiom.getSubClass());
////		REL2ClassExpressionImpl sup = getDescription(axiom.getSuperClass());
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.aBox_Classified)
//			precomputeInferences(InferenceType.CLASS_ASSERTIONS);
//		REL2IndividualImpl indi = getIndividual(axiom.getIndividual());
//		REL2ClassExpressionImpl sup = getDescription(axiom.getClassExpression());
//		REL2ClassImpl bot = (REL2ClassImpl) elcontology.descriptions.get(0);
//
//		//		ELCClassifier classifier = new ELCClassifier();
//		//		classifier.ontology = elcontology;
//		boolean answer = false;
//		
//		boolean duo = false;
////		if(indi == null || !(sub instanceof REL2ClassImpl))
////		{
////			// apply duo classification approach
////			duo = true;
////			// add an additional axiom tempC \sub sub
////			OWLClass tempclass = factory.getOWLClass(IRI.create("http://trowl.eu/REL#tempC"));
////			OWLSubClassOfAxiom tempaxiom = factory.getOWLSubClassOfAxiom(tempclass, axiom.getSubClass());
////			Entry<REL2ClassImpl, REL2ClassImpl> entry = elcfactory.loadAxiom(tempaxiom);
////
////			// incrementally classify the new ontology
////			CombinedClassifier classifier = new CombinedClassifier();
////			classifier.ontology = elcontology;
////			classifier.completion_duo();
////
////			sub = (REL2ClassImpl) entry.getKey();
////
////		}
//
//		if(sup == null || !(sup instanceof REL2ClassImpl))
//		{
//			// apply duo classification approach
//			duo = true;
//			// add an additional axiom sup \sub tempD
//			OWLClass tempclass = factory.getOWLClass(IRI.create("http://trowl.eu/REL#tempD"));
//			OWLSubClassOfAxiom tempaxiom = factory.getOWLSubClassOfAxiom(axiom.getClassExpression(),tempclass);
//			Entry<REL2ClassImpl, REL2ClassImpl> entry = rel2Factory.loadAxiom(tempaxiom);
//
//			// incrementally classify the new ontology
//			CombinedClassifier classifier = new CombinedClassifier();
//			classifier.ontology = elcontology;
//			classifier.completion_duo(elcontology.aBox_Classified);
//
//			sup = (REL2ClassImpl) entry.getValue();
//
//		}
//		
//		
//		
////		REL2ClassImpl bsub = (REL2ClassImpl) sub;
//		REL2ClassImpl bsup = (REL2ClassImpl) sup;
//		answer = (indi.superClasses.contains(bsup) || indi.superClasses.contains(bot) || indi.tempSubsumers.contains(bsup) || indi.tempSubsumers.contains(bot));
////		answer = (bsub.superClasses.contains(bsup) || bsub.superClasses.contains(bot));
//		
//		// clean the classifier, factory, ontology, etc for duo-classification.
//		if(duo)
//			rel2Factory.clean();
//
//		return answer;
//	}
//
//	protected boolean entail(OWLDisjointClassesAxiom axiom)
//	{
//		List<OWLClassExpression> list = axiom.getClassExpressionsAsList();
//		for(int i = 0;i<list.size()-1;i++)
//			for(int j = i+1;j<list.size();j++)
//			{
//				if(!entail(factory.getOWLSubClassOfAxiom(list.get(i), factory.getOWLObjectComplementOf(list.get(j)))))
//					return false;
//			}
//		return true;
//	}
//
//	protected boolean entail(OWLFunctionalDataPropertyAxiom axiom)
//	{
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.tBox_Classified)
//			precomputeInferences(InferenceType.DATA_PROPERTY_HIERARCHY);
//		Role role = getRole(axiom.getProperty());
//		// might be incomplete
//		for(Role sup:role.superClasses)
//		if(sup.functional)
//			return true;
//		return false;
//	}
//
//	protected boolean entail(OWLFunctionalObjectPropertyAxiom axiom)
//	{
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.tBox_Classified)
//			precomputeInferences(InferenceType.OBJECT_PROPERTY_HIERARCHY);
//		Role role = getRole(axiom.getProperty());
//		// might be incomplete
//		for(Role sup:role.superClasses)
//		if(sup.functional)
//			return true;
//		return false;
//	}
//
//	protected boolean entail(OWLSubClassOfAxiom axiom){
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.tBox_Classified)
//			precomputeInferences(InferenceType.CLASS_HIERARCHY);
//		REL2ClassExpressionImpl sub = getDescription(axiom.getSubClass());
//		REL2ClassExpressionImpl sup = getDescription(axiom.getSuperClass());
//		REL2ClassImpl bot = (REL2ClassImpl) elcontology.descriptions.get(0);
//
//		//		ELCClassifier classifier = new ELCClassifier();
//		//		classifier.ontology = elcontology;
//		boolean answer = false;
//		
//		boolean duo = false;
//		if(sub == null || !(sub instanceof REL2ClassImpl))
//		{
//			// apply duo classification approach
//			duo = true;
//			// add an additional axiom tempC \sub sub
//			OWLClass tempclass = factory.getOWLClass(IRI.create("http://trowl.eu/REL#tempC"));
//			OWLSubClassOfAxiom tempaxiom = factory.getOWLSubClassOfAxiom(tempclass, axiom.getSubClass());
//			Entry<REL2ClassImpl, REL2ClassImpl> entry = rel2Factory.loadAxiom(tempaxiom);
//
//			// incrementally classify the new ontology
//			CombinedClassifier classifier = new CombinedClassifier();
//			classifier.ontology = elcontology;
//			classifier.completion_duo(elcontology.aBox_Classified);
//
//			sub = (REL2ClassImpl) entry.getKey();
//
//		}
//
//		if(sup == null || !(sup instanceof REL2ClassImpl))
//		{
//			// apply duo classification approach
//			duo = true;
//			// add an additional axiom sup \sub tempD
//			OWLClass tempclass = factory.getOWLClass(IRI.create("http://trowl.eu/REL#tempD"));
//			OWLSubClassOfAxiom tempaxiom = factory.getOWLSubClassOfAxiom(axiom.getSuperClass(),tempclass);
//			Entry<REL2ClassImpl, REL2ClassImpl> entry = rel2Factory.loadAxiom(tempaxiom);
//
//			// incrementally classify the new ontology
//			CombinedClassifier classifier = new CombinedClassifier();
//			classifier.ontology = elcontology;
//			classifier.completion_duo(elcontology.aBox_Classified);
//
//			sup = (REL2ClassImpl) entry.getValue();
//
//		}
//		
//		
//		
//		REL2ClassImpl bsub = (REL2ClassImpl) sub;
//		REL2ClassImpl bsup = (REL2ClassImpl) sup;
//		answer = (bsub.superClasses.contains(bsup) || bsub.superClasses.contains(bot) || bsub.tempSubsumers.contains(bsup) || bsub.tempSubsumers.contains(bot));
////		answer = (bsub.superClasses.contains(bsup) || bsub.superClasses.contains(bot));
//		
//		// clean the classifier, factory, ontology, etc for duo-classification.
//		if(duo)
//			rel2Factory.clean();
//
//		return answer;
//	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		toadd = new HashSet<OWLAxiom>();
		toremove = new HashSet<OWLAxiom>();
		loadOntology();
		// if current_Precompute is un-empty, compute it;
		// otherwise, classify none;
		TBox_Classified = false;
		ABox_Classified = false;
		precomputeInferences();
	}

	@Override
	public Node<OWLClass> getBottomClassNode() {
		// TODO Auto-generated method stub
		return getEquivalentClasses(factory.getOWLNothing());
	}

	@Override
	public Node<OWLDataProperty> getBottomDataPropertyNode() {
		// TODO Auto-generated method stub
		// todo
		// not supported yet
		return new OWLDataPropertyNode();
	}

	@Override
	public Node<OWLObjectPropertyExpression> getBottomObjectPropertyNode() {
		// TODO Auto-generated method stub
		// todo
		// not supported yet
		return new OWLObjectPropertyNode();
	}

	@Override
	public BufferingMode getBufferingMode() {
		// TODO Auto-generated method stub
//		System.out.println("getBufferingMode is not supported yet.");
		if(this.bufferred == true)
		return BufferingMode.BUFFERING;
		else
			return BufferingMode.NON_BUFFERING;
	}

//	ArrayList<REL2ClassImpl> getcls(REL2IndividualImpl indi, boolean arg1){
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.tBox_Classified)
//			precomputeInferences(InferenceType.DATA_PROPERTY_HIERARCHY);
//		REL2ClassImpl bot = (REL2ClassImpl) elcontology.descriptions.get(0);
//		ArrayList<REL2ClassImpl> classes = new ArrayList<REL2ClassImpl>();
//		if(indi.superClasses.contains(bot)){
//			classes = new ArrayList<REL2ClassImpl>(elcontology.allconcepts);
//		}
//		else
//			for(REL2ClassImpl cls:indi.superClasses)
//				if(cls instanceof REL2ClassImpl && cls.original)
//					classes.add(cls);
//		if(arg1 == true)
//		{
//			ArrayList<REL2ClassImpl> toremove = new ArrayList<REL2ClassImpl>(classes);
//			for(REL2ClassImpl REL2ClassImpl:toremove)
//			{
//				for(REL2ClassImpl basic2:toremove)
//				{
//					if(REL2ClassImpl.superClasses.contains(basic2) && !REL2ClassImpl.equivalence.contains(basic2))
//						classes.removeAll(basic2.equivalence);
//				}
//			}
//		}
//		return classes;
//	}

	
	
	
	
	// EL
	
	
	// ELP
	
	@Override
	public NodeSet<OWLClass> getDataPropertyDomains(OWLDataProperty arg0,
			boolean arg1){
		// TODO Auto-generated method stub
		// todo
		// not supported yet
		System.out.print("getDataPropertyDomains method is not supported yet!");
		return new OWLClassNodeSet();
	}
	
	@Override
	public Set<OWLLiteral> getDataPropertyValues(OWLNamedIndividual arg0,
			OWLDataProperty arg1) {
		// TODO Auto-generated method stub
		// todo
		// unsupported yet
		System.out.println("getDataPropertyValues method is not supported yet!");
		return new HashSet<OWLLiteral>();
	}

//	protected REL2ClassExpressionImpl getDescription(OWLClassExpression desc){
//		if(elcontology.classID.get(desc)!=null)
//			return elcontology.descriptions.get(elcontology.classID.get(desc));
//		return null;
//	}

	@Override
	public NodeSet<OWLNamedIndividual> getDifferentIndividuals(
			OWLNamedIndividual arg0) throws InconsistentOntologyException,
			FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		// TODO Auto-generated method stub
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.aBox_Classified)
//			precomputeInferences(InferenceType.DIFFERENT_INDIVIDUALS);
		OWLNamedIndividualNodeSet indis = new OWLNamedIndividualNodeSet();
//		REL2IndividualImpl indi = getIndividual(arg0);
//		if(indi != null)
//		{
//			for(REL2IndividualImpl diff:indi.differentIndividuals)
//			{
//				OWLNamedIndividualNode node = new OWLNamedIndividualNode();
//				for(REL2ClassImpl equa:diff.equivalence)
//					if(equa.original && equa instanceof REL2IndividualImpl)
//						node.add(factory.getOWLNamedIndividual(equa.asSingleton().uri));
//				if(node.getSize()>0)
//					indis.addNode(node);
//			}
//		}
		return indis;
	}

	@Override
	public NodeSet<OWLClass> getDisjointClasses(OWLClassExpression concept)
	throws ReasonerInterruptedException, TimeOutException,
	FreshEntitiesException, InconsistentOntologyException {
		// TODO Auto-generated method stub
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.tBox_Classified)
//			precomputeInferences(InferenceType.DISJOINT_CLASSES);
		OWLClassNodeSet	disjoints = new OWLClassNodeSet();
//		REL2ClassExpressionImpl desc = getDescription(concept);
//		REL2ClassImpl bot = (REL2ClassImpl) elcontology.descriptions.get(0);
//
////		boolean duo = false;
////		if(desc == null || !(desc instanceof REL2ClassImpl))
////		{
////			// apply duo classification approach
////			duo = true;
////			// add an additional axiom tempC \sub X
////			OWLClass tempclass = factory.getOWLClass(IRI.create("http://trowl.eu/REL#tempC"));
////			OWLSubClassOfAxiom axiom = factory.getOWLSubClassOfAxiom(tempclass, concept);
////			Entry<REL2ClassImpl, REL2ClassImpl> entry = elcfactory.loadAxiom(axiom);
////
////			// incrementally classify the new ontology
////			CombinedClassifier classifier = new CombinedClassifier();
////			classifier.ontology = elcontology;
////			classifier.classify_duo();
////
////			desc = (REL2ClassImpl) entry.getKey();
////
////		}
////		
////		
////		
////				REL2ClassImpl atom = (REL2ClassImpl)desc;
////				HashSet<REL2ClassImpl> superClasses = new HashSet<REL2ClassImpl>();
////				if(atom.equivalence.contains(bot))
////					return satisfiable;
//////				if(atom.equivalence.contains(top))
//////					return 
////				else
////				{
////					// todo: update EL complement part
////					for(REL2ClassImpl sub:atom.superClasses)
////					{
////						if(sub.complement.original && !superClasses.contains(sub.complement))
////						{
////							superClasses.addAll(sub.complement.equivalence);
////						}
////					}					
////					for(REL2ClassImpl sub:atom.tempSubsumers)
////					{
////						if(sub.complement.original && !superClasses.contains(sub.complement))
////						{
////							superClasses.addAll(sub.complement.equivalence);
////						}
////					}					
////				}
////
////
////				while(superClasses.size()>0)
////				{
////					REL2ClassImpl sub = superClasses.iterator().next();
////					superClasses.removeAll(sub.equivalence);
////					OWLClassNode ancestor = new OWLClassNode();
////					for(REL2ClassImpl eq:sub.equivalence)
////						if(eq instanceof REL2ClassImpl && ((REL2ClassImpl)eq).original)
////							ancestor.add(factory.getOWLClass(((REL2ClassImpl)eq).uri));							
////					if(ancestor.getSize() > 0)
////						disjoints.addNode(ancestor);
////				}
////
////				// clean the classifier, factory, ontology, etc for duo-classification.
////				if(duo)
////					elcfactory.clean();

		System.out.println("getDisjointClasses method is not supported yet!");
		return disjoints;
	}

	@Override
	public NodeSet<OWLDataProperty> getDisjointDataProperties(
			OWLDataPropertyExpression arg0)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		// TODO Auto-generated method stub
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.tBox_Classified)
//			precomputeInferences(InferenceType.DATA_PROPERTY_HIERARCHY);
		OWLDataPropertyNodeSet props = new OWLDataPropertyNodeSet();
//		Role role = getRole(arg0);
//		if(role != null)
//		{
//			for(Role diff:role.disjoints)
//			{
//				OWLDataPropertyNode node = new OWLDataPropertyNode();
//				for(Role equa:diff.equivalence)
//					if(equa.original)
//						node.add(factory.getOWLDataProperty(equa.uri));
//				if(node.getSize()>0)
//					props.addNode(node);
//			}
//		}
		return props;
	}

	@Override
	public NodeSet<OWLObjectPropertyExpression> getDisjointObjectProperties(
			OWLObjectPropertyExpression arg0)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {		
		// TODO Auto-generated method stub
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.tBox_Classified)
//			precomputeInferences(InferenceType.OBJECT_PROPERTY_HIERARCHY);
		OWLObjectPropertyNodeSet props = new OWLObjectPropertyNodeSet();
//		Role role = getRole(arg0);
//		if(role != null)
//		{
//			for(Role diff:role.disjoints)
//			{
//				OWLObjectPropertyNode node = new OWLObjectPropertyNode();
//				for(Role equa:diff.equivalence)
//					if(equa.original)
//						node.add(factory.getOWLObjectProperty(equa.uri));
//				if(node.getSize()>0)
//					props.addNode(node);
//			}
//		}
		return props;
	}

	@Override
	public Node<OWLClass> getEquivalentClasses(OWLClassExpression concept) {
		// TODO Auto-generated method stub
		if(!isConsistent())
		{
			throw(new InconsistentOntologyException());
		}
		if(!TBox_Classified)
			precomputeInferences(InferenceType.CLASS_HIERARCHY);
		OWLClassNode equivalence = new OWLClassNode();
		REXClassExpressionImpl desc = rel2Factory.getREXClassExpression(concept);
		REXClassImpl bot = rel2Factory.bottom;
//		boolean duo = false;
		if(desc == null || !(desc instanceof REXClassImpl))
		{
//			// apply duo classification approach
//			duo = true;
//			// add an additional axiom X \equiv tempC
//			OWLClass tempclass = factory.getOWLClass(IRI.create("http://trowl.eu/REL#tempC"));
//			OWLSubClassOfAxiom axiom = factory.getOWLSubClassOfAxiom(concept, tempclass);
//			Entry<REL2ClassImpl, REL2ClassImpl> entry = rel2Factory.loadAxiom(axiom);
//			// add an additional axiom tempC \equiv X
//			axiom = factory.getOWLSubClassOfAxiom(tempclass, concept);
//			rel2Factory.loadAxiom(axiom);
//
//			// incrementally classify the new ontology
//			CombinedClassifier classifier = new CombinedClassifier();
//			classifier.ontology = elcontology;
//			classifier.completion_duo(elcontology.aBox_Classified);
//
//			desc = (REL2ClassImpl) entry.getValue();

		}

		// todo
		// the tempC equivalence needs to be post-processed after reasoning
		
		REXClassImpl REL2ClassImpl = (REXClassImpl) desc;
		if(!REL2ClassImpl.satisfiable && REL2ClassImpl != bot)
			return getEquivalentClasses(factory.getOWLNothing());
		if(REL2ClassImpl == bot)
			return unsats;
		for(REXClassExpressionImpl eq:REL2ClassImpl.superClasses)
			if(eq instanceof REXClassImpl && eq.superClasses.contains(REL2ClassImpl) && eq.asREXClassImpl().original)
				equivalence.add(factory.getOWLClass(eq.asREXClassImpl().getIRI()));

//		 clean the classifier, factory, ontology, etc for duo-classification.
//		if(duo)
//			rel2Factory.clean();

		return equivalence;
	}

	@Override
	public Node<OWLDataProperty> getEquivalentDataProperties(
			OWLDataProperty arg0) throws InconsistentOntologyException,
			FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		// TODO Auto-generated method stub
		// todo
		// not supported yet
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.tBox_Classified)
//			precomputeInferences(InferenceType.DATA_PROPERTY_HIERARCHY);
		OWLDataPropertyNode equivalence = new OWLDataPropertyNode();
//		Role desc = getRole(arg0);
//		if(desc != null)
//		{
//			for(Role eq:desc.equivalence)
//				if(eq.original)
//					equivalence.add(factory.getOWLDataProperty(eq.uri));
//		}
		return equivalence;
	}

	@Override
	public Node<OWLObjectPropertyExpression> getEquivalentObjectProperties(
			OWLObjectPropertyExpression arg0)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		// TODO Auto-generated method stub
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.tBox_Classified)
//			precomputeInferences(InferenceType.OBJECT_PROPERTY_HIERARCHY);
		OWLObjectPropertyNode equivalence = new OWLObjectPropertyNode();
//		Role desc = getRole(arg0);
//		if(desc != null)
//		{
//			for(Role eq:desc.equivalence)
//				if(eq.original)
//					equivalence.add(factory.getOWLObjectProperty(eq.uri));
//		}
		return equivalence;
	}

	@Override
	public FreshEntityPolicy getFreshEntityPolicy() {
		// TODO Auto-generated method stub
		// todo
		// not supported yet
//		System.out.println("getFreshEntityPolicy is not supported yet.");
		return this.configuration.getFreshEntityPolicy();
	}

//	protected REL2IndividualImpl getIndividual(OWLIndividual indi){
//		if(elcontology.individualID.get(indi)!=null)
//			return (REL2IndividualImpl) elcontology.descriptions.get(elcontology.individualID.get(indi));
//		return null;
//	}

	@Override
	public IndividualNodeSetPolicy getIndividualNodeSetPolicy() {
		// TODO Auto-generated method stub
		// todo
		// not supported yet
//		System.out.println("getIndividualNodePolicy is not supported yet.");
		return this.configuration.getIndividualNodeSetPolicy();
	}

//	protected NodeSet<OWLNamedIndividual> getIndividuals(OWLClassExpression concept){
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.aBox_Classified)
//			precomputeInferences(InferenceType.CLASS_ASSERTIONS);
//			OWLNamedIndividualNodeSet individuals = new OWLNamedIndividualNodeSet();
//			HashSet<REL2IndividualImpl> indis = new HashSet<REL2IndividualImpl>();
//			boolean duo = false;
//			REL2ClassImpl elpconcept = null;
//			REL2ClassImpl bot = (REL2ClassImpl) elcontology.descriptions.get(0);
//			// check if the concept does not already exist
//			if(elcontology.classID.get(concept) == null || !(elcontology.descriptions.get(elcontology.classID.get(concept)) instanceof REL2ClassImpl))
//			{
//				// apply duo classification approach
//				duo = true;
//				// add an additional axiom X \sub tempC
//				OWLClass tempclass = factory.getOWLClass(IRI.create("http://trowl.eu/REL#tempC"));
//				OWLSubClassOfAxiom axiom = factory.getOWLSubClassOfAxiom(concept, tempclass);
//				Entry<REL2ClassImpl, REL2ClassImpl> entry = rel2Factory.loadAxiom(axiom);
//	
//				// incrementally classify the new ontology
//				CombinedClassifier classifier = new CombinedClassifier();
//				classifier.ontology = elcontology;
//				classifier.completion_duo(elcontology.aBox_Classified);
//				
//				elpconcept = (REL2ClassImpl) entry.getValue();
//			}
//			else
//				elpconcept = (REL2ClassImpl) elcontology.descriptions.get(elcontology.classID.get(concept));
//			for(REL2ClassExpressionImpl bsc:elcontology.descriptions.values())
//			{
//				if(bsc instanceof REL2IndividualImpl)
//				{
//					REL2IndividualImpl indi = (REL2IndividualImpl) bsc;
//				if(indi.superClasses.contains(bot) || indi.superClasses.contains(elpconcept)||indi.tempSubsumers.contains(elpconcept))
//	//			if(indi.superClasses.contains(bot) || indi.superClasses.contains(elpconcept))
//					indis.add(indi);
//				}
//			}
//	//		indis = new HashSet<Individual>(elpconcept.instances);
//			while(indis.size()>0)
//			{
//				REL2IndividualImpl head = indis.iterator().next();
//				indis.removeAll(head.equivalence);
//				OWLNamedIndividualNode	node = new OWLNamedIndividualNode();
//				for(REL2ClassImpl same:head.equivalence)
//					if(same instanceof REL2IndividualImpl && ((REL2IndividualImpl)same).original)
//					node.add(factory.getOWLNamedIndividual(same.asSingleton().uri));
//				individuals.addNode(node);
//			}
//			// clean the classifier, factory, ontology, etc for duo-classification.
//			if(duo)
//				rel2Factory.clean();
//			return individuals;
//		}

	@Override
	public NodeSet<OWLNamedIndividual> getInstances(OWLClassExpression arg0, boolean arg1) {
		// TODO Auto-generated method stub
		// now only returns indirect instances
		// todo
//		return getIndividuals(arg0);
		return null;
	}

	@Override
	public Node<OWLObjectPropertyExpression> getInverseObjectProperties(
			OWLObjectPropertyExpression arg0)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		// TODO Auto-generated method stub
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.tBox_Classified)
//			precomputeInferences(InferenceType.OBJECT_PROPERTY_HIERARCHY);
//		Role role = getRole(arg0);
//		if(role != null)
//		{
//			OWLObjectPropertyNode inverse = new OWLObjectPropertyNode();
//			for(Role invrole:role.inverse.equivalence)
//				if(invrole.original)
//					inverse.add(factory.getOWLObjectProperty(invrole.uri));
//			return inverse;
//		}
		return new OWLObjectPropertyNode();
	}

	@Override
	public NodeSet<OWLClass> getObjectPropertyDomains(
			OWLObjectPropertyExpression arg0, boolean arg1)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		// TODO Auto-generated method stub
		OWLClassExpression exp = factory.getOWLObjectSomeValuesFrom(arg0, factory.getOWLThing());
		return getSuperClasses(exp, arg1);
	}

	public Map<OWLNamedIndividual, Set<OWLNamedIndividual>> getObjectPropertyInstances(
			OWLObjectProperty owlOP) {
		// TODO Auto-generated method stub
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.aBox_Classified)
//			precomputeInferences(InferenceType.OBJECT_PROPERTY_ASSERTIONS);
//		Role role = getRole(owlOP);
		Map<OWLNamedIndividual, Set<OWLNamedIndividual>> insts = new HashMap<OWLNamedIndividual, Set<OWLNamedIndividual>>();
//		for(REL2IndividualImpl indi:role.subjects)
//		{
//			if(!indi.original)
//				continue;
//			OWLNamedIndividual sub = factory.getOWLNamedIndividual(indi.uri);
//			HashSet<OWLNamedIndividual> objs = new HashSet<OWLNamedIndividual>();
//			for(REL2ClassImpl obj:role.Relations.get(indi))
//			{
//				if(obj.original && obj instanceof REL2IndividualImpl)
//				{
//					objs.add(factory.getOWLNamedIndividual(obj.uri));
//				}
//			}
//			if(objs.size()>0)
//				insts.put(sub, objs);
//		}
		return insts;
	}

	@Override
	public NodeSet<OWLClass> getObjectPropertyRanges(
			OWLObjectPropertyExpression arg0, boolean arg1)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		// TODO Auto-generated method stub
		OWLClassExpression exp = factory.getOWLObjectSomeValuesFrom(arg0.getInverseProperty(), factory.getOWLThing());
		return getSuperClasses(exp, arg1);
	}

	@Override
	public NodeSet<OWLNamedIndividual> getObjectPropertyValues(OWLNamedIndividual arg0,
			OWLObjectPropertyExpression arg1) {
		// TODO Auto-generated method stub
		// todo
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.aBox_Classified)
//			precomputeInferences(InferenceType.OBJECT_PROPERTY_ASSERTIONS);
		OWLNamedIndividualNodeSet toreturn = new OWLNamedIndividualNodeSet();
//		REL2IndividualImpl indi = getIndividual(arg0);
//		Role role0 = getRole(arg1);
//		if(indi != null && role0 != null)
//		{
//			for(Role role:role0.subroles)
//			{
////				if(role == role0)
//			if(role.Relations.get(indi)!=null)
//			{
//				HashSet<REL2ClassImpl> indi2s = new HashSet<REL2ClassImpl>(role.Relations.get(indi));
//				while(!indi2s.isEmpty())
//				{
//					REL2ClassImpl bsc = indi2s.iterator().next();
//					if(bsc instanceof REL2IndividualImpl)
//					{
//						REL2IndividualImpl indi2 = (REL2IndividualImpl)bsc;
//						OWLNamedIndividualNode node = new OWLNamedIndividualNode();
//						for(REL2ClassImpl equa:indi2.equivalence)
//						{
////							if(equa.original)
//							if(equa instanceof REL2IndividualImpl)
//								node.add(factory.getOWLNamedIndividual(equa.asSingleton().uri));
//						}
//						if(node.getSize() > 0)
//							toreturn.addNode(node);
//						indi2s.removeAll(indi2.equivalence);
//					}
//					indi2s.remove(bsc);
//					indi2s.removeAll(bsc.equivalence);
//				}
//			}
//				
//			}
//			for(RoleConcept rc:indi.LeftConnection)
//			{
//				if(rc.role.inverse.superClasses.contains(role0) && rc.concept instanceof REL2IndividualImpl)
////				if(rc.role.inverse == role0 && rc.concept instanceof REL2IndividualImpl)
//				{
//					OWLNamedIndividualNode node = new OWLNamedIndividualNode();
//					for(REL2ClassImpl equa:rc.concept.equivalence)
//					{
//						if(equa.original)
//						if(equa instanceof REL2IndividualImpl)
//							node.add(factory.getOWLNamedIndividual(equa.asSingleton().uri));
//					}
//					if(node.getSize() > 0)
//						toreturn.addNode(node);					
//				}
//			}
//
////			for(Role role:role0.inverse.subroles)
////			{
////				for(Entry<REL2ClassImpl, HashSet<REL2ClassImpl>> entry:role.Relations.entrySet())
////					if(entry.getValue().contains(indi) && entry.getKey() instanceof REL2IndividualImpl)
////					{
////						OWLNamedIndividualNode node = new OWLNamedIndividualNode();
////						for(REL2ClassImpl equa:entry.getKey().equivalence)
////						{
//////							if(equa.original)
////							if(equa instanceof REL2IndividualImpl)
////								node.add(factory.getOWLNamedIndividual(equa.asSingleton().uri));
////						}
////						if(node.getSize() > 0)
////							toreturn.addNode(node);
////
////					}
////						
////			}
//		}
////			if(indi.relations.get(role)!=null)
////			{
////				HashSet<Individual> indi2s = new HashSet<Individual>(indi.relations.get(role));
////				while(!indi2s.isEmpty())
////				{
////					Individual indi2 = indi2s.iterator().next();
////					OWLNamedIndividualNode node = new OWLNamedIndividualNode();
////					for(Individual equa:indi2.sameIndividuals)
////					{
////						if(equa.original)
////							node.add(factory.getOWLNamedIndividual(equa.uri));
////					}
////					if(node.getSize() > 0)
////						toreturn.addNode(node);
////					indi2s.removeAll(indi2.sameIndividuals);
////				}
////			}
		return toreturn;
	}
	
	@Override
	public Set<OWLAxiom> getPendingAxiomAdditions() {
		// TODO Auto-generated method stub
		// todo
		// not supported yet
		return toadd;
	}

	@Override
	public Set<OWLAxiom> getPendingAxiomRemovals() {
		// TODO Auto-generated method stub
		// todo
		// not supported yet
		return toremove;
	}

	@Override
	public List<OWLOntologyChange> getPendingChanges() {
		// TODO Auto-generated method stub
		return changes;
	}

	@Override
	public Set<InferenceType> getPrecomputableInferenceTypes() {
		// TODO Auto-generated method stub
		return supported_Precompute;
	}

	@Override
	public String getReasonerName() {
		// TODO Auto-generated method stub
		return "REL";
	}

	@Override
	public Version getReasonerVersion() {
		// TODO Auto-generated method stub
		return version;
	}

//	protected Role getRole(OWLDataPropertyExpression role){
//		OWLObjectPropertyExpression objrole = factory.getOWLObjectProperty(role.asOWLDataProperty().getIRI());
//		if(elcontology.roleID.get(objrole)!=null)
//			return elcontology.roles.get(elcontology.roleID.get(objrole));
//		return null;
//	}
//
//	protected Role getRole(OWLObjectPropertyExpression role){
//		if(elcontology.roleID.get(role)!=null)
//			return elcontology.roles.get(elcontology.roleID.get(role));
//		return null;
//	}

	@Override
	public OWLOntology getRootOntology() {
		// TODO Auto-generated method stub
		return ontology;
	}

	@Override
	public Node<OWLNamedIndividual> getSameIndividuals(OWLNamedIndividual arg0)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		// TODO Auto-generated method stub
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.aBox_Classified)
//			precomputeInferences(InferenceType.SAME_INDIVIDUAL);
		OWLNamedIndividualNode node = new OWLNamedIndividualNode();
//		REL2IndividualImpl indi = getIndividual(arg0);
//		if(indi != null)
//		{
//			for(REL2ClassImpl equa:indi.equivalence)
//			{
//					if(equa.original && equa instanceof REL2IndividualImpl)
//						node.add(factory.getOWLNamedIndividual(((REL2IndividualImpl)equa).uri));
//			}
//		}
		return node;
	}

	@Override
	public NodeSet<OWLClass> getSubClasses(OWLClassExpression concept, boolean direct) {
		// TODO Auto-generated method stub
		if(!isConsistent())
		{
			throw(new InconsistentOntologyException());
		}
		if(!TBox_Classified)
			precomputeInferences(InferenceType.CLASS_HIERARCHY);
		OWLClassNodeSet	descendants = new OWLClassNodeSet();
		REXClassExpressionImpl desc = rel2Factory.getREXClassExpression(concept);
		REXClassImpl bot = rel2Factory.bottom;
//		boolean duo = false;
//		if(desc == null || !(desc instanceof REL2ClassImpl))
//		{
//			// apply duo classification approach
//			duo = true;
//			// add an additional axiom X \sub tempC
//			OWLClass tempclass = factory.getOWLClass(IRI.create("http://trowl.eu/REL#tempC"));
//			OWLSubClassOfAxiom axiom = factory.getOWLSubClassOfAxiom(concept, tempclass);
//			Entry<REL2ClassImpl, REL2ClassImpl> entry = rel2Factory.loadAxiom(axiom);
//
//			// incrementally classify the new ontology
//			CombinedClassifier classifier = new CombinedClassifier();
//			classifier.ontology = elcontology;
//			classifier.completion_duo(elcontology.aBox_Classified);
//
//			desc = (REL2ClassImpl) entry.getValue();
//
//		}

		REXClassImpl atom = (REXClassImpl) desc;
		if(atom.superClasses.contains(bot))
			return descendants;
		HashSet<REXClassImpl> subsumees = new HashSet<REXClassImpl>();
		if(direct)
		{
			for(REXClassImpl sub:rel2Factory.concepts.values())
			{
				if(sub.satisfiable && sub.original &&  !atom.equivalence.contains(sub) && !subsumees.contains(sub) && sub.superClasses.contains(atom))
				{
					boolean toadd = true;
					for(REXClassExpressionImpl subsub:sub.superClasses)
						if(subsub instanceof REXClassImpl && subsub.asREXClassImpl().original && !sub.equivalence.contains(subsub) && !atom.equivalence.contains(subsub) && subsub.superClasses.contains(atom) )
						{
							toadd = false;
							break;
						}
//					for(REL2ClassImpl subsub:sub.tempSubsumers)
//						if(subsub instanceof REL2ClassImpl && subsub.original && !sub.equivalence.contains(subsub) && !atom.equivalence.contains(subsub) && (subsub.superClasses.contains(atom) || subsub.tempSubsumers.contains(atom)))
//						{
//							toadd = false;
//							break;
//						}
					
					if(toadd)
						subsumees.addAll(sub.equivalence);
				}
			}
//			if(subsumees.size() == 0)
//				subsumees.addAll(bot.equivalence);					
		}
		else
		{
			for(REXClassImpl sub:rel2Factory.concepts.values())
			{
				if(sub.superClasses.contains(atom) && !sub.equivalence.contains(atom))
					subsumees.add(sub);
			}
//			subsumees.addAll(bot.equivalence);
		}
		while(subsumees.size()>0)
		{
			REXClassImpl sub = subsumees.iterator().next();
			subsumees.removeAll(sub.equivalence);
			OWLClassNode descendant = new OWLClassNode();
			for(REXClassImpl eq:sub.equivalence)
				if(eq instanceof REXClassImpl && ((REXClassImpl)eq).original)
					descendant.add(factory.getOWLClass(((REXClassImpl)eq).getIRI()));							
			if(descendant.getSize() > 0)
				descendants.addNode(descendant);
		}
		if(!direct || descendants.getFlattened().size() == 0)
			descendants.addNode(unsats);

//		// clean the classifier, factory, ontology, etc for duo-classification.
//		if(duo)
//			rel2Factory.clean();

		return descendants;
	}

	@Override
	public NodeSet<OWLDataProperty> getSubDataProperties(OWLDataProperty arg0,
			boolean arg1) throws InconsistentOntologyException,
			FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		// TODO Auto-generated method stub
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.tBox_Classified)
//			precomputeInferences(InferenceType.DATA_PROPERTY_HIERARCHY);
		OWLDataPropertyNodeSet	descendants = new OWLDataPropertyNodeSet();
//		Role role = getRole(arg0);
//		if(role != null)
//		{
//			HashSet<Role> subsumees = new HashSet<Role>();
//			if(arg1)
//			for(Role sub:elcontology.roles.values())
//			{
//				if(!role.equivalence.contains(sub) && !subsumees.contains(sub) && sub.superClasses.contains(role))
//				{
//					boolean toadd = true;
//					for(Role subsub:sub.superClasses)
//						if(subsub.original && !sub.equivalence.contains(subsub) && !role.equivalence.contains(subsub) && subsub.superClasses.contains(role))
//						{
//							toadd = false;
//							break;
//						}
//					if(toadd)
//					subsumees.addAll(sub.equivalence);
//				}
//			}
//			else
//				for(Role sub:elcontology.roles.values())
//					if(sub.superClasses.contains(role))
//						subsumees.add(sub);
//			while(subsumees.size()>0)
//			{
//				Role sub = subsumees.iterator().next();
//				subsumees.removeAll(sub.equivalence);
//				OWLDataPropertyNode descendant = new OWLDataPropertyNode();
//				for(Role eq:sub.equivalence)
//					if(eq.original)
//						descendant.add(factory.getOWLDataProperty(eq.uri));							
//				if(descendant.getSize() > 0)
//					descendants.addNode(descendant);
//			}
//		}
		return descendants;	
		
	}

	@Override
	public NodeSet<OWLObjectPropertyExpression> getSubObjectProperties(
			OWLObjectPropertyExpression arg0, boolean arg1)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		// TODO Auto-generated method stub
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.tBox_Classified)
//			precomputeInferences(InferenceType.OBJECT_PROPERTY_HIERARCHY);
		OWLObjectPropertyNodeSet	descendants = new OWLObjectPropertyNodeSet();
//		Role role = getRole(arg0);
//		if(role != null)
//		{
//			HashSet<Role> subsumees = new HashSet<Role>();
//			if(arg1)
//			for(Role sub:elcontology.roles.values())
//			{
//				if(!role.equivalence.contains(sub) && !subsumees.contains(sub) && sub.superClasses.contains(role))
//				{
//					boolean toadd = true;
//					for(Role subsub:sub.superClasses)
//						if(subsub.original && !sub.equivalence.contains(subsub) && !role.equivalence.contains(subsub) && subsub.superClasses.contains(role))
//						{
//							toadd = false;
//							break;
//						}
//					if(toadd)
//					subsumees.addAll(sub.equivalence);
//				}
//			}
//			else
//				for(Role sub:elcontology.roles.values())
//					if(sub.superClasses.contains(role))
//						subsumees.add(sub);
//			while(subsumees.size()>0)
//			{
//				Role sub = subsumees.iterator().next();
//				subsumees.removeAll(sub.equivalence);
//				OWLObjectPropertyNode descendant = new OWLObjectPropertyNode();
//				for(Role eq:sub.equivalence)
//					if(eq.original)
//						descendant.add(factory.getOWLObjectProperty(eq.uri));							
//				if(descendant.getSize() > 0)
//					descendants.addNode(descendant);
//			}
//		}
		return descendants;
	}
	
//	OWLSubClassOfAxiom CAtoGCI(OWLClassAssertionAxiom axiom){
//		factory.getOWLSubClassOfAxiom(factory.getOWLObjectOneOf(axiom.a), arg1)
//	}

//	ArrayList<REL2ClassImpl> getsubsumers(REL2ClassImpl desc, boolean arg1){
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.tBox_Classified)
//			precomputeInferences(InferenceType.CLASS_HIERARCHY);
//		REL2ClassImpl bot = (REL2ClassImpl) elcontology.descriptions.get(0);
//		ArrayList<REL2ClassImpl> superClasses = new ArrayList<REL2ClassImpl>();
//				REL2ClassImpl atom = (REL2ClassImpl)desc;
//				if(arg1)
//				{
//					if(atom.equivalence.contains(bot))
//						return new ArrayList<REL2ClassImpl>(leaf);
//					for(REL2ClassImpl sub:atom.superClasses)
//					{
//						if(sub.original && sub instanceof REL2ClassImpl && !superClasses.contains(sub))
//						{
//							boolean toadd = true;
//							for(REL2ClassImpl sub2:atom.superClasses)
//								if(sub2.original && !atom.equivalence.contains(sub2) && !sub.equivalence.contains(sub2) && sub2.superClasses.contains(sub))
//								{
//									toadd =false;
//									break;
//								}
//							for(REL2ClassImpl sub2:atom.tempSubsumers)
//								if(sub2.original && !atom.equivalence.contains(sub2) && !sub.equivalence.contains(sub2) && sub2.superClasses.contains(sub))
//								{
//									toadd =false;
//									break;
//								}
//							if(toadd)
////								superClasses.addAll(sub.equivalence);
//								superClasses.add(sub);
//						}
//					}
////					for(REL2ClassImpl sub:atom.tempSubsumers)
////					{
////						if(sub.original && !atom.equivalence.contains(sub) && !superClasses.contains(sub))
////						{
////							boolean toadd = true;
////							for(REL2ClassImpl sub2:atom.superClasses)
////								if(sub2.original && !atom.equivalence.contains(sub2) && !sub.equivalence.contains(sub2) && (sub2.superClasses.contains(sub) || sub2.tempSubsumers.contains(sub)))
////								{
////									toadd =false;
////									break;
////								}
////							for(REL2ClassImpl sub2:atom.tempSubsumers)
////								if(sub2.original && !atom.equivalence.contains(sub2) && !sub.equivalence.contains(sub2) && (sub2.superClasses.contains(sub) || sub2.tempSubsumers.contains(sub)))
////								{
////									toadd =false;
////									break;
////								}
////							if(toadd)
////								superClasses.addAll(sub.equivalence);
////						}
////					}
//				}
//				else
//				{
//					if(atom.equivalence.contains(bot))
//						return new ArrayList<REL2ClassImpl>(satis);
//					for(REL2ClassImpl sub:atom.superClasses)
//						if(sub.original)
//							superClasses.add(sub);
////					for(REL2ClassImpl sub:atom.tempSubsumers)
////						if(!atom.equivalence.contains(sub) && sub.original)
////							superClasses.add(sub);
//				}
//				return superClasses;
//	}

	@Override
	public NodeSet<OWLClass> getSuperClasses(OWLClassExpression concept,
			boolean direct) throws InconsistentOntologyException,
			ClassExpressionNotInProfileException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		// TODO Auto-generated method stub
		if(!isConsistent())
		{
			throw(new InconsistentOntologyException());
		}
		if(!TBox_Classified)
			precomputeInferences(InferenceType.CLASS_HIERARCHY);
		OWLClassNodeSet	ancestors = new OWLClassNodeSet();
		REXClassExpressionImpl desc = rel2Factory.getREXClassExpression(concept);
//		REL2ClassImpl bot = rel2Factory.bottom;
//		boolean duo = false;
//		if(desc == null || !(desc instanceof REL2ClassImpl))
//		{
//			// apply duo classification approach
//			duo = true;
//			// add an additional axiom tempC \sub X
//			OWLClass tempclass = factory.getOWLClass(IRI.create("http://trowl.eu/REL#tempC"));
//			OWLSubClassOfAxiom axiom = factory.getOWLSubClassOfAxiom(tempclass, concept);
//			Entry<REL2ClassImpl, REL2ClassImpl> entry = rel2Factory.loadAxiom(axiom);
//
//			// incrementally classify the new ontology
//			CombinedClassifier classifier = new CombinedClassifier();
//			classifier.ontology = elcontology;
//			classifier.completion_duo(elcontology.aBox_Classified);
//
//			desc = (REL2ClassImpl) entry.getKey();
//
//		}
		
				REXClassImpl atom = (REXClassImpl)desc;
				HashSet<REXClassImpl> superClasses = new HashSet<REXClassImpl>();
				if(direct)
				{
					if(!atom.satisfiable)
						return leaves;
					for(REXClassExpressionImpl sub:atom.superClasses)
					{
						if(sub instanceof REXClassImpl && sub.asREXClassImpl().original && !atom.equivalence.contains(sub) && !superClasses.contains(sub))
						{
							boolean toadd = true;
							for(REXClassExpressionImpl sub2:atom.superClasses)
								if(sub2 instanceof REXClassImpl && sub2.asREXClassImpl().original && !atom.equivalence.contains(sub2) && !sub.asREXClassImpl().equivalence.contains(sub2) && sub2.superClasses.contains(sub))
								{
									toadd =false;
									break;
								}
//							for(REL2ClassImpl sub2:atom.tempSubsumers)
//								if(sub2 instanceof REL2ClassImpl && sub2.original && !atom.equivalence.contains(sub2) && !sub.equivalence.contains(sub2) && (sub2.superClasses.contains(sub) || sub2.tempSubsumers.contains(sub)))
//								{
//									toadd =false;
//									break;
//								}
							if(toadd)
								superClasses.addAll(sub.asREXClassImpl().equivalence);
						}
					}
//					for(REL2ClassImpl sub:atom.tempSubsumers)
//					{
//						if(sub instanceof REL2ClassImpl && sub.original && !atom.equivalence.contains(sub) && !superClasses.contains(sub))
//						{
//							boolean toadd = true;
//							for(REL2ClassImpl sub2:atom.superClasses)
//								if(sub2 instanceof REL2ClassImpl && sub2.original && !atom.equivalence.contains(sub2) && !sub.equivalence.contains(sub2) && (sub2.superClasses.contains(sub) || sub2.tempSubsumers.contains(sub)))
//								{
//									toadd =false;
//									break;
//								}
//							for(REL2ClassImpl sub2:atom.tempSubsumers)
//								if(sub2 instanceof REL2ClassImpl && sub2.original && !atom.equivalence.contains(sub2) && !sub.equivalence.contains(sub2) && (sub2.superClasses.contains(sub) || sub2.tempSubsumers.contains(sub)))
//								{
//									toadd =false;
//									break;
//								}
//							if(toadd)
//								superClasses.addAll(sub.equivalence);
//						}
//					}
				}
				else
				{
					if(!atom.satisfiable)
						return satisfiable;
					for(REXClassExpressionImpl sub:atom.superClasses)
						if(sub instanceof REXClassImpl && sub.asREXClassImpl().original && !atom.equivalence.contains(sub))
							superClasses.add(sub.asREXClassImpl());
//					for(REL2ClassImpl sub:atom.tempSubsumers)
//						if(sub instanceof REL2ClassImpl && !atom.equivalence.contains(sub) && sub.original)
//							superClasses.add(sub);
				}
				while(superClasses.size()>0)
				{
					REXClassImpl sub = superClasses.iterator().next();
					superClasses.removeAll(sub.equivalence);
					OWLClassNode ancestor = new OWLClassNode();
					for(REXClassImpl eq:sub.equivalence)
						if(eq instanceof REXClassImpl && ((REXClassImpl)eq).original)
							ancestor.add(factory.getOWLClass(((REXClassImpl)eq).getIRI()));							
					if(ancestor.getSize() > 0)
						ancestors.addNode(ancestor);
				}

//				// clean the classifier, factory, ontology, etc for duo-classification.
//				if(duo)
//					rel2Factory.clean();

				return ancestors;
	}

//	@Override
//	public void prepareReasoner() throws ReasonerInterruptedException,
//			TimeOutException {
//		// TODO Auto-generated method stub
//		
//	}


	// ELPI
	
	@Override
	public NodeSet<OWLDataProperty> getSuperDataProperties(
			OWLDataProperty arg0, boolean arg1)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		// TODO Auto-generated method stub
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.tBox_Classified)
//			precomputeInferences(InferenceType.DATA_PROPERTY_HIERARCHY);
		OWLDataPropertyNodeSet	ancestors = new OWLDataPropertyNodeSet();
//		Role role = getRole(arg0);
//		if(role != null)
//		{
//			HashSet<Role> superClasses = new HashSet<Role>();
//			if(arg1)
//			{
//			for(Role sub:role.superClasses)
//				if(sub.original && !role.equivalence.contains(sub) && !superClasses.contains(sub))
//				{
//					boolean toadd = true;
//					for(Role sub2:role.superClasses)
//						if(sub2.original && !role.equivalence.contains(sub2) && !sub.equivalence.contains(sub2) && sub2.superClasses.contains(sub))
//						{
//							toadd =false;
//							break;
//						}
//					if(toadd)
//						superClasses.addAll(sub.equivalence);
//				}
//			}
//			else
//				for(Role sub:role.superClasses)
//					superClasses.add(sub);
//
//			while(superClasses.size()>0)
//			{
//				Role sub = superClasses.iterator().next();
//				superClasses.removeAll(sub.equivalence);
//				OWLDataPropertyNode ancestor = new OWLDataPropertyNode();
//				for(Role eq:sub.equivalence)
//					if(eq.original)
//						ancestor.add(factory.getOWLDataProperty(eq.uri));							
//				if(ancestor.getSize() > 0)
//					ancestors.addNode(ancestor);
//			}
//		}
		return ancestors;
	}

	
	@Override
	public NodeSet<OWLObjectPropertyExpression> getSuperObjectProperties(
			OWLObjectPropertyExpression arg0, boolean arg1)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		// TODO Auto-generated method stub
//		if(!isConsistent())
//		{
//			throw(new InconsistentOntologyException());
//		}
//		if(!elcontology.tBox_Classified)
//			precomputeInferences(InferenceType.OBJECT_PROPERTY_HIERARCHY);
		OWLObjectPropertyNodeSet	ancestors = new OWLObjectPropertyNodeSet();
//		Role role = getRole(arg0);
//		if(role != null)
//		{
//			HashSet<Role> superClasses = new HashSet<Role>();
//			if(arg1)
//			{
//			for(Role sub:role.superClasses)
//				if(sub.original && !role.equivalence.contains(sub) && !superClasses.contains(sub))
//				{
//					boolean toadd = true;
//					for(Role sub2:role.superClasses)
//						if(sub2.original && !role.equivalence.contains(sub2) && !sub.equivalence.contains(sub2) && sub2.superClasses.contains(sub))
//						{
//							toadd =false;
//							break;
//						}
//					if(toadd)
//						superClasses.addAll(sub.equivalence);
//				}
//			}
//			else
//				for(Role sub:role.superClasses)
//					superClasses.add(sub);
//
//			while(superClasses.size()>0)
//			{
//				Role sub = superClasses.iterator().next();
//				superClasses.removeAll(sub.equivalence);
//				OWLObjectPropertyNode ancestor = new OWLObjectPropertyNode();
//				for(Role eq:sub.equivalence)
//					if(eq.original)
//						ancestor.add(factory.getOWLObjectProperty(eq.uri));							
//				if(ancestor.getSize() > 0)
//					ancestors.addNode(ancestor);
//			}
//		}
		return ancestors;
	}

	@Override
	public long getTimeOut() {
		// TODO Auto-generated method stub
		// todo
		// not supported yet
//		System.out.println("getTimeOut is not supported yet.");
		return this.configuration.getTimeOut();
	}

	@Override
	public Node<OWLClass> getTopClassNode() {
		// TODO Auto-generated method stub
		return getEquivalentClasses(factory.getOWLThing());
	}

	@Override
	public Node<OWLDataProperty> getTopDataPropertyNode() {
		// TODO Auto-generated method stub
		// todo
		// not supported yet
		System.out.println("getTopDataPropertyNode is not supported yet.");
		return new OWLDataPropertyNode();
	}
	
	@Override
	public Node<OWLObjectPropertyExpression> getTopObjectPropertyNode() {
		// TODO Auto-generated method stub
		// todo
		// not supported yet
		System.out.print("getTopObjectPropertyNode is not supported yet.");
		return new OWLObjectPropertyNode();
	}

	
	@Override
	public NodeSet<OWLClass> getTypes(OWLNamedIndividual arg0, boolean arg1) {
		// TODO Auto-generated method stub
		if(!isConsistent())
		{
			throw(new InconsistentOntologyException());
		}
		if(!ABox_Classified)
			precomputeInferences(InferenceType.CLASS_ASSERTIONS);
		OWLClassNodeSet types = new OWLClassNodeSet();
		REXIndividualImpl indi = rel2Factory.getREXIndividual(arg0);
		if(indi == null)
			return types;
//		REL2ClassImpl bot = rel2Factory.bottom;
		HashSet<REXClassImpl> classes = new HashSet<REXClassImpl>();
		if(!indi.satisfiable){
//			classes = elcontology.allconcepts;
			throw(new InconsistentOntologyException());
		}
		else
			for(REXClassExpressionImpl cls:indi.superClasses)
				if(cls instanceof REXClassImpl && ! (cls instanceof REXIndividualImpl) && cls.asREXClassImpl().original)
					classes.add((REXClassImpl) cls);
		if(arg1 == true)
		{
			HashSet<REXClassImpl> toremove = new HashSet<REXClassImpl>(classes);
			for(REXClassImpl basic1:toremove)
			{
				for(REXClassImpl basic2:toremove)
				{
					if(basic1.superClasses.contains(basic2) && !basic1.equivalence.contains(basic2))
						classes.removeAll(basic2.equivalence);
				}
			}
		}

		while(classes.size()>0)
		{
			REXClassImpl sub = classes.iterator().next();
			classes.removeAll(sub.equivalence);
			OWLClassNode type = new OWLClassNode();
			for(REXClassImpl eq:sub.equivalence)
				if(eq instanceof REXClassImpl && ((REXClassImpl)eq).original)
					type.add(factory.getOWLClass(((REXClassImpl)eq).getIRI()));							
			if(type.getSize() > 0)
				types.addNode(type);
		}
		return types;
	}

	@Override
	public Node<OWLClass> getUnsatisfiableClasses() {
		// TODO Auto-generated method stub
//		OWLClassNode toreturn = new OWLClassNode();
//		REL2ClassImpl bot = (REL2ClassImpl) elcontology.descriptions.get(0);
//		for(REL2ClassImpl concept:bot.equivalence)
//			if(concept instanceof REL2ClassImpl)
//				toreturn.add(factory.getOWLClass(((REL2ClassImpl)concept).uri));
//		return toreturn;
		if(!isConsistent())
		{
			throw(new InconsistentOntologyException());
		}
		if(!TBox_Classified)
			precomputeInferences(InferenceType.CLASS_HIERARCHY);
		return unsats;
	}
	
	// ELPPI

	public boolean hasObjectPropertyRelationship(OWLNamedIndividual ind1,
			OWLObjectProperty ope, OWLNamedIndividual ind2) {
		// TODO Auto-generated method stub
//		REL2IndividualImpl sub = getIndividual(ind1);
//		Role role = getRole(ope);
//		REL2IndividualImpl obj = getIndividual(ind2);
//		
//		if(sub != null && role != null && role.subjects.contains(sub))
//			return role.Relations.get(sub).contains(obj);
//		
		return false;
	}
	
//	protected void initialiseClassifier()
//		{
//	//        if(nominalfree)
//	//			System.out.println("This configuration will treat this ontology as containing nominal!");
//	       	classifier = new CombinedClassifier();
//	
//		}
	
	@Override
	public void interrupt() {
		// TODO Auto-generated method stub
		// todo
		// not supported yet
		System.out.println("interrupt is not supported yet.");
	}
	
	@Override
	public boolean isConsistent() {
		// TODO Auto-generated method stub
		if(!ABox_Classified)
			precomputeInferences(InferenceType.CLASS_ASSERTIONS);
		return rel2Factory.consistency;
	}

	@Override
	public boolean isEntailed(OWLAxiom arg0)
			throws ReasonerInterruptedException,
			UnsupportedEntailmentTypeException, TimeOutException,
			AxiomNotInProfileException, FreshEntitiesException {
		// TODO Auto-generated method stub
		// todo
		// only support subclass checking
//		if(arg0 instanceof OWLSubClassOfAxiom)
//			return entail((OWLSubClassOfAxiom)arg0);
//		else if(arg0 instanceof OWLClassAssertionAxiom)
//			return entail((OWLClassAssertionAxiom)arg0);
//		else if(arg0 instanceof OWLDisjointClassesAxiom)
//			return entail((OWLDisjointClassesAxiom)arg0);
//		else if(arg0 instanceof OWLFunctionalDataPropertyAxiom)
//			return entail((OWLFunctionalDataPropertyAxiom)arg0);
//		else if(arg0 instanceof OWLFunctionalObjectPropertyAxiom)
//			return entail((OWLFunctionalObjectPropertyAxiom)arg0);
		System.out.println("This type of entailment checking is not supported yet");
		throw new UnsupportedEntailmentTypeException(arg0);
	}

	@Override
	public boolean isEntailed(Set<? extends OWLAxiom> arg0)
			throws ReasonerInterruptedException,
			UnsupportedEntailmentTypeException, TimeOutException,
			AxiomNotInProfileException, FreshEntitiesException {
		// TODO Auto-generated method stub
		// todo
		// only support subclass checking
		for(OWLAxiom axiom:arg0)
		{
			if(!isEntailed(axiom))
				return false;
		}
		return true;
	}

	@Override
	public boolean isEntailmentCheckingSupported(AxiomType<?> arg0) {
		// TODO Auto-generated method stub
		// todo
		if(arg0.equals(AxiomType.CLASS_ASSERTION) 
				|| arg0.equals(AxiomType.SUBCLASS_OF)
				|| arg0.equals(AxiomType.DISJOINT_CLASSES)
				|| arg0.equals(AxiomType.FUNCTIONAL_DATA_PROPERTY)
				|| arg0.equals(AxiomType.FUNCTIONAL_OBJECT_PROPERTY))
			return true;
		return false;
	}

//	public boolean isoriginal(OWLClass concept) {
//		// TODO Auto-generated method stub
//		REL2ClassImpl elpconcept = (REL2ClassImpl) getDescription(concept);
//		if(elpconcept != null)
//		return elpconcept.original;
//		return false;
//	}

	@Override
	public boolean isPrecomputed(InferenceType arg0) {
		// TODO Auto-generated method stub
//		System.out.println("REL does not support precomputation yet.");
		return current_Precompute.contains(arg0);
	}
	
	// TrOWL-BGP methods

	@Override
	public boolean isSatisfiable(OWLClassExpression arg0) {
		// TODO Auto-generated method stub

//		OWLSubClassOfAxiom axiom1 = factory.getOWLSubClassOfAxiom(arg0, factory.getOWLNothing());
//		return !entail(axiom1);
		REXClassExpressionImpl cls = rel2Factory.getREXClassExpression(arg0);
		if(cls.isContext.get())
			return cls.satisfiable;
		else
			return true;
//		if(cls instanceof REL2ClassImpl)
//			return cls.satisfiable;
//		else
//			return true;
	}
	
	Reasoner reasoner = new Reasoner();
	
	public void loadOntology()
	{
//		rel2Factory = new OntologyFactory(ontology,bgp,MetaOn,disjoint);
//		rel2Factory.createbuilder();
//		this.elcontology = rel2Factory.createELOntology();	
//		this.elcontology.profile = Profile.OWL_2_EL;
		reasoner.load(ontology);
		rel2Factory = reasoner.factory;
	}

	@Override
	public void ontologiesChanged(List<? extends OWLOntologyChange> arg0)
	throws OWLException {
		// TODO Auto-generated method stub
		if(bufferred)
		{
			for(OWLOntologyChange change:arg0)
			{
				if(change instanceof AddAxiom)
					toadd.add(change.getAxiom());
				else
					toremove.add(change.getAxiom());
			}
			changes.addAll(arg0);
		}
		else
		{
			loadOntology();
			// if current_Precompute is un-empty, compute it;
			// otherwise, classify none;
			TBox_Classified = false;
			ABox_Classified = false;
			precomputeInferences();
		}

	}

	@Override
	public void precomputeInferences(InferenceType... arg0)
	throws ReasonerInterruptedException, TimeOutException,
	InconsistentOntologyException {
		// TODO Auto-generated method stub
		// add into the current_Precompute
		for(InferenceType type:arg0)
		{
			if(!supported_Precompute.contains(type))
				System.out.println("REL does not support precomputation of this inference type yet. It will be ignored.");
			else current_Precompute.add(type);
		}
		boolean TBox = false;
		boolean ABox = false;
		// decide if want to do ABox reasoning
		if(current_Precompute.contains(InferenceType.CLASS_ASSERTIONS)
//				|| current_Precompute.contains(InferenceType.DIFFERENT_INDIVIDUALS)
//				|| current_Precompute.contains(InferenceType.OBJECT_PROPERTY_ASSERTIONS)
//				|| current_Precompute.contains(InferenceType.SAME_INDIVIDUAL)
				)
		{
//			current_Precompute.addAll(supported_Precompute);
			// if TBox not classified and ABox not classified, then classify both;
			// if TBox classified and ABox not, then classify only ABox;
			// if TBox not classified and ABox classified, impossible;
			// if TBox classified and ABox classified, then classify none;
//			classify(false,!rel2Factory.ABox_Classified);
			ABox = !ABox_Classified;
		}
		// decide if want to do TBox reasoning only
		if(current_Precompute.contains(InferenceType.CLASS_HIERARCHY)
				|| current_Precompute.contains(InferenceType.DATA_PROPERTY_HIERARCHY)
				|| current_Precompute.contains(InferenceType.DISJOINT_CLASSES)
				|| current_Precompute.contains(InferenceType.OBJECT_PROPERTY_HIERARCHY))
		{
			current_Precompute.addAll(supported_TBox_Precompute);
			// if TBox not classified, then classify TBox;
			// if TBox classified, then classify none;
//			classify(!rel2Factory.TBox_Classified,false);
			TBox = !TBox_Classified;
		}
		try {
			classify(TBox, ABox);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
//	// OWL-DBC methods
//	public void save(boolean TBox, boolean Type, boolean Relation, boolean subType, boolean clsType,
//			OutputStream out) throws IOException {
//		// TODO Auto-generated method stub
//		String rdfs="<http://www.w3.org/2000/01/rdf-schema#> .\n";
//		String rdf="<http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n";
////		String subcls = "\t<"+rdfs+"subClassOf>\t";
////		String type = "\t<"+rdf+"type>\t";
////		final PrintStream ps= new PrintStream(out);
//		OutputStreamWriter writer = new OutputStreamWriter(out);
//		writer.write("@prefix rdf: "+rdf);
//		writer.write("@prefix rdfs: "+rdfs);
////		int n = 0;
//		if(TBox)
//			for(REL2ClassImpl cls:elcontology.allconcepts)
//			{
//				if(cls instanceof REL2ClassImpl && cls.id > 1)
//				{
//					ArrayList<REL2ClassImpl> sups = getsubsumers(cls,subType);
//					if(sups.size()>0)
//					{
//						writer.write("<"+cls.uri+"> rdfs:subClassOf ");
//					for(int i = 0;i<sups.size()-1;i++)
//					{
////						String subs = "<"+cls.uri+"> rdfs:subClassOf <"+sups.get(i).uri+">\n";
////						System.out.print(subs);
////						ps.println(subs);
//						writer.write("<"+sups.get(i).uri+"> ,\n");
//					}
//					writer.write("<"+sups.get(sups.size()-1)+"> .\n");
////					System.out.print("<"+cls.uri+"> rdfs:subClassOf <"+sups.get(sups.size()-1).uri+">\n");
//					}
////					System.out.println(cls.uri.getFragment()+" "+sups.size());
//				}
//			}
//		
//		if(Type && Relation)
//			for(int id:elcontology.individualID.values())
////			for(REL2IndividualImpl indi:elcontology.individuals.values())
//			{
//				REL2IndividualImpl indi = (REL2IndividualImpl)elcontology.descriptions.get(id);
//				if(indi.original)
//				{
//					ArrayList<REL2ClassImpl> clss = getcls(indi,clsType);
//					if(clss.size()>0)
//					{
//						writer.write("<"+indi.uri+"> a ");
//					for(int i = 0;i<clss.size()-1;i++)
////						ps.println(indi.uri+type+cls.uri);
//						writer.write("<"+clss.get(i).uri+"> ,\n");
//					writer.write("<"+clss.get(clss.size()-1)+"> .\n");
//					}
////					System.out.println(indi.uri.getFragment()+" "+clss.size());
//
//					for(Role role:elcontology.roles.values())
//					{
//						if(role.original && role.Relations.get(indi)!=null)
//						{
//							ArrayList<REL2IndividualImpl> objs = new ArrayList<REL2IndividualImpl>();
//							for(REL2ClassImpl obj:role.Relations.get(indi))
//								if(obj instanceof REL2IndividualImpl && obj.original)
//									objs.add(obj.asSingleton());
//							if(objs.size()>0)
//							{
//							writer.write("<"+indi.uri+"> <"+role.uri+"> ");
//							for(int i = 0;i<objs.size()-1;i++)
//								writer.write("<"+objs.get(i).uri+"> ,\n");
////							for(Individual indi2:indi.relations.get(role))
////								ps.println(indi.uri+"\t"+role.uri+"\t"+indi2.uri);
//							writer.write("<"+objs.get(objs.size() -1)+"> .\n");
//							}
//						}
//					}
//				}
//			}
//		else if(Type)
//			for(int id:elcontology.individualID.values())
////				for(REL2IndividualImpl indi:elcontology.individuals.values())
//				{
//					REL2IndividualImpl indi = (REL2IndividualImpl)elcontology.descriptions.get(id);
//				if(indi.original)
//				{
//					ArrayList<REL2ClassImpl> clss = getcls(indi,clsType);
//					if(clss.size()>0)
//					{
//						writer.write("<"+indi.uri+"> a ");
//					for(int i = 0;i<clss.size()-1;i++)
////						ps.println(indi.uri+type+cls.uri);
//						writer.write("<"+clss.get(i).uri+"> ,");
//					writer.write("<"+clss.get(clss.size()-1)+"> .");
//					}
//				}
//			}
//		else if(Relation)
//			for(int id:elcontology.individualID.values())
////				for(REL2IndividualImpl indi:elcontology.individuals.values())
//				{
//					REL2IndividualImpl indi = (REL2IndividualImpl)elcontology.descriptions.get(id);
//				if(indi.original)
//				{
//					for(Role role:elcontology.roles.values())
//					{
//						if(role.original && role.Relations.get(indi)!=null)
//						{
//							writer.write("<"+indi.uri+"> <"+role.uri+"> ");
//							ArrayList<REL2IndividualImpl> objs = new ArrayList<REL2IndividualImpl>();
//							for(REL2ClassImpl obj:role.Relations.get(indi))
//								if(obj instanceof REL2IndividualImpl && obj.original)
//									objs.add(obj.asSingleton());
//							for(int i = 0;i<objs.size()-1;i++)
//								writer.write("<"+objs.get(i).uri+"> ,");
////							for(Individual indi2:indi.relations.get(role))
////								ps.println(indi.uri+"\t"+role.uri+"\t"+indi2.uri);
//							writer.write("<"+objs.get(objs.size() -1)+"> .");
//						}
//					}
//				}
//			}
//		
//		writer.close();
////		ps.close();
//	}


}
