package eu.trowl.rex.reasoner;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import eu.trowl.rex.factory.REXDataFactory;
import eu.trowl.rex.model.implementations.REXClassExpressionImpl;
import eu.trowl.rex.model.implementations.REXClassImpl;
import eu.trowl.rex.model.implementations.REXIndividualImpl;
import eu.trowl.rex.model.implementations.REXObjectPropertyExpressionImpl;
import eu.trowl.rex.model.implementations.REXSubClassOfImpl;
import eu.trowl.rex.util.REXReasonerConfiguration;
import eu.trowl.rex.util.Timer;
import eu.trowl.rex.workers.ClassificationWorker;

public class Reasoner {

	ConcurrentLinkedQueue<REXClassExpressionImpl> activecontexts = new ConcurrentLinkedQueue<REXClassExpressionImpl>();

	OWLOntology onto;

	Timer timer = new Timer("REL");

	public REXDataFactory factory = new REXDataFactory();

	int AxiomNum = 0;
	
	int cNum = 0;

	HashSet<ClassificationWorker> workers = new HashSet<ClassificationWorker>(6);

	int threadNum;
	Thread[] threads;
	int WIPNum;

	Collection<REXObjectPropertyExpressionImpl> roles;

	public Reasoner(int threadNum)
	{
		this.threadNum = threadNum;
		threads = new Thread[threadNum];
		WIPNum = threadNum;
	}
	
	public Reasoner()
	{
		this.threadNum = 0;
		WIPNum = 0;
	}

	public void load(OWLOntology onto){
//		timer.start();
		cNum = onto.getClassesInSignature().size();
		if(!onto.getClassesInSignature().contains(onto.getOWLOntologyManager().getOWLDataFactory().getOWLThing()))
			cNum++;
		if(!onto.getClassesInSignature().contains(onto.getOWLOntologyManager().getOWLDataFactory().getOWLNothing()))
			cNum++;
		factory.initialise(onto.getOWLOntologyManager().getOWLDataFactory());
		if(onto.getClassesInSignature().size() > REXReasonerConfiguration.largeTThreshold)
			factory.smallT = false;
		if(onto.getIndividualsInSignature().size() > REXReasonerConfiguration.largeAThreshold)
			factory.smallA = false;
		for(OWLLogicalAxiom axiom:onto.getLogicalAxioms())
		{
			factory.initialiseAxiom(axiom);
		}
		roles = new ArrayList<REXObjectPropertyExpressionImpl>(factory.roles.values());
		roles.addAll(factory.rolechainNames.values());
		completeRoles();

		this.onto = onto;
//		timer.stop();
	}

	public void classify(boolean TBox, boolean ABox) throws InterruptedException{

//		timer.start();
		if(TBox)
		for(OWLClass clazz:onto.getClassesInSignature())
			addActiveContext(factory.getREL2ClassExpression(clazz));
		
		if(ABox)
			for(OWLNamedIndividual indi:onto.getIndividualsInSignature())
				addActiveContext(factory.getREL2Individual(indi));
		
//		if(false)
//		if(TBox || ABox)
//			addActiveContext(factory.top);

		if(threadNum != 0)
		{
		for(int j = 0;j<threads.length;j++)
		{
			threads[j] = new Thread((Runnable)new ClassificationWorker(this, activecontexts, factory));
			threads[j].start();
		}
		for(int j = 0;j<threads.length;j++)
			threads[j].join();
		}
		else
		{
			ClassificationWorker worker = new ClassificationWorker(this,activecontexts,factory);
			worker.process();
		}
		finishTBox();
	}


	public void finishTBox() throws InterruptedException{

		output();
	}


	public void output()
	{
//		timer.stop();
		int num = 0;
		int Cnum = 0;
		System.out.println("Total Indi: "+onto.getIndividualsInSignature().size());

		for(OWLClass cls:onto.getClassesInSignature())
		{
			if(!cls.equals(onto.getOWLOntologyManager().getOWLDataFactory().getOWLThing())&&!cls.equals(onto.getOWLOntologyManager().getOWLDataFactory().getOWLNothing()))
			{
				int x = 0;
				REXClassImpl concept = (REXClassImpl) factory.getREL2ClassExpression(cls);
				if(!concept.satisfiable)
					x = cNum;
				else
				{
				concept.superClasses.add(factory.top);
				for(REXClassExpressionImpl sup:concept.superClasses)
				{
					if(sup instanceof REXClassImpl && ((REXClassImpl) sup).original && ((REXClassImpl) sup).getIRI() != null)
					{
						x++;
//											if(cls.getIRI().getFragment().equals("UnitedKingdomIsland"))
//											System.out.println(((REL2ClassImpl) sup).getIRI().getFragment());
					}
				}
				}
//				System.out.println(cls.getIRI().getFragment()+" "+x);
				num+=x;
			}
		}

		for(OWLNamedIndividual indi:onto.getIndividualsInSignature())
		{
			int x = 0;
			REXIndividualImpl individual = factory.getREL2Individual(indi);
			individual.superClasses.add(factory.top);
			for(REXClassExpressionImpl sup:individual.superClasses)
				if(sup instanceof REXClassImpl && ((REXClassImpl)sup).original && ((REXClassImpl) sup).getIRI() != null)
					x++;
			Cnum+=x;
		}

		System.out.print(num+" "+Cnum+" ");
		//		System.out.println(CNum);
		//		System.out.println(RNum);
//		System.out.println(timer.getTotal());

	}

	void completeRoles(){
		boolean roleclosure = true;

		// initialize role transitivity?
		while(roleclosure)
		{
			roleclosure = false;
			for(REXObjectPropertyExpressionImpl r:roles)
			{
				HashSet<REXObjectPropertyExpressionImpl> tempset1 = new HashSet<REXObjectPropertyExpressionImpl>(r.getSuperRoles());
				tempset1.remove(r);
				for(REXObjectPropertyExpressionImpl j:tempset1)
				{
					if(j.onLHS)
					{
						r.onLHS = true;
					}
					// R17
					roleclosure = r.addSuperRoles(j.getSuperRoles())? true:roleclosure;
				}
				for(REXObjectPropertyExpressionImpl s:r.chains.keySet())
					if(!r.onLHS || !s.onLHS)
						for(REXObjectPropertyExpressionImpl t:r.chains.get(s))
						{
							if(t.onLHS)
							{
								roleclosure = true;
								r.onLHS = true;
								s.onLHS = true;
							}
						}
			}
			for(REXObjectPropertyExpressionImpl r:roles)
				for(REXObjectPropertyExpressionImpl s:r.getSuperRoles())
				{
					r.onRHSAllValuesFrom = r.onRHSAllValuesFrom || s.onRHSAllValuesFrom;
					r.onMax1Cardinality = r.onMax1Cardinality || s.onMax1Cardinality;
					s.addSuberRole(r);
				}
		}
		for(REXObjectPropertyExpressionImpl role:roles )
		{
			HashSet<REXObjectPropertyExpressionImpl> supers = new HashSet<REXObjectPropertyExpressionImpl>(role.getSuperRoles());
			for(REXObjectPropertyExpressionImpl s:supers)
				if(s.getInversePropertyExpression() != null && role.getInversePropertyExpression() != null)
					role.getInversePropertyExpression().addSuperRole(s.getInversePropertyExpression());
			for(REXObjectPropertyExpressionImpl s:role.chains.keySet())
			{
				if(s.getInversePropertyExpression() == null)
					continue;
				if(role.getInversePropertyExpression() == null)
					continue;
				Set<REXObjectPropertyExpressionImpl> rs = s.getInversePropertyExpression().chains.get(role.getInversePropertyExpression());
				if(rs == null)
				{
					rs = new HashSet<REXObjectPropertyExpressionImpl>();
					s.getInversePropertyExpression().chains.put(role.getInversePropertyExpression(), rs);
				}
				for(REXObjectPropertyExpressionImpl t:role.chains.get(s))
					if(t.getInversePropertyExpression() != null)
					rs.add(t.getInversePropertyExpression());
			}
			for(REXObjectPropertyExpressionImpl s:role.disjoints)
			{
				if(s.getInversePropertyExpression() != null && role.getInversePropertyExpression() != null)
				{
				s.getInversePropertyExpression().disjoints.add(role.getInversePropertyExpression());
				role.getInversePropertyExpression().disjoints.add(s.getInversePropertyExpression());
				}
			}
		}
	}

	public void addActiveContext(REXClassExpressionImpl context){
//		if(context instanceof REL2DataRange)
//			return;
		if(context.isActiveContext.compareAndSet(false, true))
		{
			activecontexts.add(context);
			if(context.isContext.compareAndSet(false, true))
			{
				factory.contexts.add(context);
				REXSubClassOfImpl	axiom = factory.getREL2SubClassOf(context, context);
//				if(axiom.LHSQueue.compareAndSet(false, true))
				{
				addToActiveContext(axiom);
				if(factory.top.originalLHS)
				{
					axiom = factory.getREL2SubClassOf(context, factory.top);
					addToActiveContext(axiom);
				}
				}
			}
		}
	}


	private void addToActiveContext(REXSubClassOfImpl axiom) {
		// TODO Auto-generated method stub
		REXClassExpressionImpl context;
		if(axiom.tested)
			return;
		axiom.tested = true;
		if(axiom.getRHSContext() != null)
		{
			axiom.link = true;
			context = (REXClassExpressionImpl) axiom.getRHSContext();
			context.scheduleAdd(axiom);
			addActiveContext(context);
		}
		context = (REXClassExpressionImpl) axiom.getLHSContext();
		context.scheduleAdd(axiom);
//		if(axiom.subsumption)
//		{
//			if(axiom.rhsRole != null)
//				context = factory.testREL2ObjectSomeValuesFrom(axiom.rhsRole, axiom.rhsFiller);
//			else
//				context = axiom.rhsFiller;
//			if(context != null)
//			{
//				context.scheduleAdd(axiom);
//				addActiveContext(context);
//			}
//			else
//				System.out.println("Missing context in subsumption axiom: "+axiom);
//		}
	}

	public void deactivateContext(REXClassExpressionImpl context){
		context.isActiveContext.set(false);
		if(context.satisfiable && !context.emptySchedule())
		{
			addActiveContext(context);
		}
//		else if(context.satisfiable && !context.localSchedule.isEmpty())
//			addActiveContext(context);
	}

	public void TBoxPostprocessing() {
		// TODO Auto-generated method stub
		factory.top.superClasses.add(factory.top);
		factory.top.equivalence.add(factory.top);
		factory.bottom.superClasses.add(factory.bottom);
		factory.bottom.equivalence.add(factory.bottom);
		for(REXClassImpl cls:factory.concepts.values())
			if(cls.original)
			{
				if(!cls.satisfiable)
					factory.bottom.equivalence.add(cls);
				else
				{
					cls.superClasses.add(factory.top);
				for(REXClassExpressionImpl sup:cls.superClasses)
					if(sup instanceof REXClassImpl && sup.asREL2ClassImpl().original && sup.superClasses.contains(cls))
					{
						cls.equivalence.add(sup.asREL2ClassImpl());
//						sup.asREL2ClassImpl().equivalence.add(cls);
					}
				}
			}
	}
	
	public void ABoxPostprocessing(){
		
	}

	public void classify() throws InterruptedException {
		// TODO Auto-generated method stub
		classify(true,false);
	}
}
