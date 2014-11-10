package eu.trowl.rex.workers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import eu.trowl.rex.factory.REXDataFactory;
import eu.trowl.rex.model.implementations.REXClassExpressionImpl;
import eu.trowl.rex.model.implementations.REXIndividualImpl;
import eu.trowl.rex.model.implementations.REXObjectIntersectionOfImpl;
import eu.trowl.rex.model.implementations.REXObjectMax1CardinalityImpl;
import eu.trowl.rex.model.implementations.REXObjectMinCardinalityImpl;
import eu.trowl.rex.model.implementations.REXObjectPropertyExpressionImpl;
import eu.trowl.rex.model.implementations.REXObjectSomeValuesFromImpl;
import eu.trowl.rex.model.implementations.REXSubClassOfImpl;
import eu.trowl.rex.model.interfaces.REXIndividual;
import eu.trowl.rex.reasoner.Reasoner;
import eu.trowl.rex.util.REXReasonerConfiguration;
import eu.trowl.rex.util.Timer;

public class ClassificationWorker implements Runnable{

	Reasoner reasoner;
	ConcurrentLinkedQueue<REXClassExpressionImpl> activecontexts;
	REXDataFactory factory;

	//	Concept context;
	REXClassExpressionImpl context;


	REXClassExpressionImpl LHS;
	REXObjectPropertyExpressionImpl RHSrole;
	REXClassExpressionImpl RHSFiller;
	REXClassExpressionImpl RHS;

	REXSubClassOfImpl axiom;

		Timer timer = new Timer("Classification Worker");


	public ClassificationWorker(Reasoner reasoner, ConcurrentLinkedQueue<REXClassExpressionImpl> activecontexts, REXDataFactory factory)
	{
		this.reasoner = reasoner;
		this.activecontexts = activecontexts;
		this.factory = factory;
	}


	private void inferC_sub_D(REXClassExpressionImpl sub, REXClassExpressionImpl sup){
		// this is only called when C \sub D is inferred;		
		if(sub instanceof REXIndividualImpl && sup == factory.bottom)
		{
			factory.consistency = false;
			return;
		}
		if(sub == factory.top && sup == factory.bottom)
		{
			factory.consistency = false;
			return;
		}
		if(!sub.superClasses.contains(sup))
		{
			REXSubClassOfImpl newAxiom = factory.getREL2SubClassOf(sub, sup);
			if(newAxiom.tested)
				return;
			newAxiom.tested = true;
			if(sup instanceof REXObjectSomeValuesFromImpl)
			{
				newAxiom.link = true;
				REXObjectSomeValuesFromImpl some = (REXObjectSomeValuesFromImpl) sup;
//				if(some.getFiller() == context)
//					context.localSchedule.add(newAxiom);
//				else
				{
					some.getFiller().scheduleAdd(newAxiom);
					reasoner.addActiveContext(some.getFiller());
				}
			}
//			if(sub == context)
//				context.localSchedule.add(newAxiom);
//			else
//			if(newAxiom.LHSQueue.compareAndSet(false, true))
			{
				sub.scheduleAdd(newAxiom);
				reasoner.addActiveContext(sub);
			}
//			if(sup == context)
//				context.localSchedule.add(newAxiom);
//			else
//			{
//				sup.scheduleAdd(newAxiom);
//				reasoner.addActiveContext(sup);
//			}
		}
	}

	private void inferNewC_sub_r_D(REXClassExpressionImpl C, REXObjectPropertyExpressionImpl role, REXClassExpressionImpl D, boolean subsumption)
	{
		if(!D.predecessors.containsKey(role) || !D.predecessors.get(role).contains(C))
		{
			REXSubClassOfImpl newAxiom = factory.getREL2SubClassOfSomeValuesFrom(C, role, D);
			if(newAxiom.tested)
				return;
			newAxiom.subsumption = subsumption;
			newAxiom.link = true;
			newAxiom.tested = true;
//			if(C == context)
//				context.localSchedule.add(newAxiom);
//			else
//			if(newAxiom.LHSQueue.compareAndSet(false, true))
			{
				C.scheduleAdd(newAxiom);
				reasoner.addActiveContext(C);
			}
//			if(D == context)
//				context.localSchedule.add(newAxiom);
//			else
//			if(newAxiom.RHSFillerQueue.compareAndSet(false, true))
			{
				D.scheduleAdd(newAxiom);
				reasoner.addActiveContext(D);
			}
//			REL2ClassExpressionImpl some = factory.testREL2ObjectSomeValuesFrom(role, D);
//			if(subsumption && some != null)
//			{
//				if(some == context)
//					context.localSchedule.add(newAxiom);
//				else
//				{
//					some.scheduleAdd(newAxiom);
//					reasoner.addActiveContext(some);
//				}
//			}
		}
	}

	private void inferstrictC_sub_D(REXClassExpressionImpl sub,
			REXClassExpressionImpl sup) {
		// TODO Auto-generated method stub
		if(sub instanceof REXIndividualImpl && sup == factory.bottom)
		{
			factory.consistency = false;
			return;
		}
		if(sub == factory.top && sup == factory.bottom)
		{
			factory.consistency = false;
			return;
		}
		if(!sub.superClasses.contains(sup))
		{
			REXSubClassOfImpl newAxiom = factory.getstrictREL2SubClassOf(sub, sup);
			if(newAxiom.tested)
				return;
			newAxiom.tested = true;
//			if(sub == context)
//				sub.localSchedule.add(newAxiom);
//			else
//			if(newAxiom.LHSQueue.compareAndSet(false, true))
			{
				sub.scheduleAdd(newAxiom);
				reasoner.addActiveContext(sub);
			}
//			if(sup == context)
//				context.localSchedule.add(newAxiom);
//			else
//			{
//				sup.scheduleAdd(newAxiom);
//				reasoner.addActiveContext(sup);
//			}
		}
	}

	void proceedTBox()
	{
//				if(LHS.toString().equals("(part_of SOME plant)"))
//					System.out.println();
		if(RHS != null && axiom.subsumption && context == LHS)
			// in this case, we have C \sub D
		{
//			axiom.LHSQueue.set(false);
			if(LHS.superClasses.add(RHS))
			{
				ruleR_neg();
				ruleR_bot_2();
				ruleR_and_minus();
				ruleR_and_plus();
				ruleR_or();
				ruleR_some_2();

				// generalised ELK rule
				if(factory.smallT)
				ruleR_sub_1();
				ruleR_sub_2();

				// non-EL++ rule
				ruleR_func_2();
//								ruleR_forall_2();
				ruleR_comp();
				ruleR_cardin();
				if(false)
				{
				if(LHS == factory.top)
					ruleR_top();
				
				ruleR_min_2();
				ruleR_min_dis();
				}
				
				// individual inverse rule
				ruleR_some_I_2();
						
				if(RHS.complement != null && RHS.complement instanceof REXObjectSomeValuesFromImpl)
					reasoner.addActiveContext(RHS.complement);
				if(RHS.complement != null && RHS.complement instanceof REXObjectIntersectionOfImpl)
					reasoner.addActiveContext(RHS.complement);
			}
		}
//		if(RHS != null && axiom.subsumption && context == RHS)
//			// in this case, we havd D \sub C
//		{
//			if(RHS.subClasses.add(LHS))
//			{
//				ruleR_sub_1();
//			}
//		}
		if(axiom.link && RHSFiller == context)
			// in this case, we have D ->r.C
		{
//			axiom.RHSFillerQueue.set(false);
			Set<REXClassExpressionImpl> predecessors = RHSFiller.predecessors.get(RHSrole);
			if(predecessors == null)
			{
				predecessors = new HashSet<REXClassExpressionImpl>();
				RHSFiller.predecessors.put(RHSrole, predecessors);
			}
			if(predecessors.add(LHS))
			{
				ruleR_bot_1();
				ruleR_some_1();
				ruleR_o_1();

				if(RHSFiller.isContext.get() == false)
					reasoner.addActiveContext(RHSFiller);				
			}
		}
		if(axiom.link && LHS == context)
			// in this case, we have C ->r.D
		{			
//			axiom.LHSQueue.set(true);
			Set<REXClassExpressionImpl> imps = LHS.implications.get(RHSrole);		
			if(imps == null)
			{
				imps = new HashSet<REXClassExpressionImpl>();
				LHS.implications.put(RHSrole, imps);
			}
			if(imps.add(RHSFiller))
			{
				ruleR_o_2();

				// non-EL rules
				if(false)
								ruleR_forall_1();
				ruleR_func_1();
				
				if(false)
				ruleR_min_1();
				
				// individual inverse rule
				ruleR_some_I_1();

				if(RHSFiller.isContext.get() == false)
					reasoner.addActiveContext(RHSFiller);	
			}

		}


	}


	private void ruleR_min_dis() {
		// TODO Auto-generated method stub
		if(RHS.complement != null)
			for(REXObjectPropertyExpressionImpl role:LHS.predecessors.keySet())
				for(REXClassExpressionImpl pred:LHS.predecessors.get(role))
					if(pred.implications.containsKey(role) && pred.implications.get(role).contains(LHS) && pred.implications.get(role).contains(RHS.complement))
						cardinTestSimple(pred, role,LHS);
	}


	private void ruleR_min_2() {
		// TODO Auto-generated method stub
		for(REXObjectPropertyExpressionImpl role:LHS.predecessors.keySet())
			for(REXClassExpressionImpl pred:LHS.predecessors.get(role))
				if(pred.implications.containsKey(role) && pred.implications.get(role).contains(LHS))
		cardinTestSimple2(pred, role, LHS, RHS);
	}

	public void cardinTestSimple2(REXClassExpressionImpl subject, REXObjectPropertyExpressionImpl role, REXClassExpressionImpl b, REXClassExpressionImpl atomic){
		for(int len:atomic.mins.keySet())
			if(len <= REXReasonerConfiguration.cardinThreshold)
			{
				REXObjectMinCardinalityImpl target = atomic.mins.get(len).get(role);
		if(target != null && !subject.superClasses.contains(target) && (!subject.subAxioms.containsKey(target) || !subject.subAxioms.get(target).tested))
		{
			{
//				if(a.toString().equals("Mushroom"))
//					System.out.println();
				ArrayList<REXClassExpressionImpl> completeList =  subject.completeLists.get(target);
				if(completeList == null)
				{
					completeList = new ArrayList<REXClassExpressionImpl>();
					subject.completeLists.put(target, completeList);
					subject.totalWeight.put(target, 0);
				}
				completeList.add(b);
		        int size = subject.totalWeight.get(target);
		        size+=b.cardin;
		        subject.totalWeight.put(target, size);
//		        for(REL2ClassExpressionImpl obj:objs.keySet())
//				{
//					completeList.add(obj);
////					cardinList.add(objs.get(obj));
//					size += objs.get(obj);
//				}
//				ArrayList<REL2ClassExpressionImpl> completeList = LHS.completeLists.get(target).keySet();
//				if(completeList == null)
//				{
//					completeList = new ArrayList<REL2ClassExpressionImpl>();
//					LHS.completeLists.put(target, completeList);
//					LHS.totalWeight.put(target, 0);
//				}
//				int size = LHS.totalWeight.get(target);
//		        completeList.add(b);
//		        size+=b.cardin;
				subject.totalWeight.put(target, size);
		        ArrayList<REXClassExpressionImpl> comb = new ArrayList<REXClassExpressionImpl>();
		        if(size >= len && testCombSimple(completeList, 0, len, size, comb))
		        	inferstrictC_sub_D(subject, target);
			}
		}
	}

}

	private void ruleR_min_1() {
		// TODO Auto-generated method stub
		cardinTestSimple(LHS, RHSrole,RHSFiller);

	}
	
	public void cardinTestSimple(REXClassExpressionImpl subject, REXObjectPropertyExpressionImpl role, REXClassExpressionImpl b){
//		boolean toprocess = false;
		for(REXClassExpressionImpl atomic:b.superClasses)
		for(int len:atomic.mins.keySet())
			if(len <= REXReasonerConfiguration.cardinThreshold)
			{
				REXObjectMinCardinalityImpl target = atomic.mins.get(len).get(role);
				if(target != null && !subject.superClasses.contains(target) && (!subject.subAxioms.containsKey(target) || !subject.subAxioms.get(target).tested ))
				{
//					if(subject.toString().equals("Mushroom"))
//						System.out.println();
					ArrayList<REXClassExpressionImpl> completeList =  subject.completeLists.get(target);
					if(completeList == null)
					{
						completeList = new ArrayList<REXClassExpressionImpl>();
						subject.completeLists.put(target, completeList);
						subject.totalWeight.put(target, 0);
					}
			        int size = subject.totalWeight.get(target);
			        if(!completeList.contains(b))
			        {
						completeList.add(b);
				        size+=b.cardin;
				        subject.totalWeight.put(target, size);
			        }
//			        for(REL2ClassExpressionImpl obj:objs.keySet())
//					{
//						completeList.add(obj);
////						cardinList.add(objs.get(obj));
//						size += objs.get(obj);
//					}
//					ArrayList<REL2ClassExpressionImpl> completeList = LHS.completeLists.get(target).keySet();
//					if(completeList == null)
//					{
//						completeList = new ArrayList<REL2ClassExpressionImpl>();
//						LHS.completeLists.put(target, completeList);
//						LHS.totalWeight.put(target, 0);
//					}
//					int size = LHS.totalWeight.get(target);
//			        completeList.add(b);
//			        size+=b.cardin;
					subject.totalWeight.put(target, size);
			        ArrayList<REXClassExpressionImpl> comb = new ArrayList<REXClassExpressionImpl>();
			        if(size >= len && testCombSimple(completeList, 0, len, size, comb))
			        	inferstrictC_sub_D(subject, target);
				}
			}
	}

	boolean testCombSimple(ArrayList<REXClassExpressionImpl> completeList, int startid, int numLeft, int sizeLeft, ArrayList<REXClassExpressionImpl> comb)
	{
		{
			for(int i = startid;i < completeList.size();i++)
			{
//				comb.add(completeList.get(i));
				sizeLeft-=completeList.get(i).cardin;
				if(testSimple(comb, completeList.get(i)))
//				if(test(comb))
				{
					if(numLeft <=completeList.get(i).cardin)
						return true;
					comb.add(completeList.get(i));
					if(testCombSimple(completeList, i+1,numLeft-completeList.get(i).cardin,sizeLeft, comb))
						return true;
					comb.remove(completeList.get(i));
				}
				
//				comb.remove(completeList.get(i));
				
				if(sizeLeft < numLeft)
					break;
			}
		}
		return false;
	}
	
	boolean testSimple(ArrayList<REXClassExpressionImpl> comb, REXClassExpressionImpl bsc2){
		for(REXClassExpressionImpl bsc1:comb)
//			for(REL2ClassExpressionImpl bsc2:comb)
//				if(bsc1 != bsc2)
					if(bsc1 instanceof REXIndividualImpl && bsc2 instanceof REXIndividual)
					{
						if(!((REXIndividualImpl)bsc1).differentIndividuals.contains(bsc2))
							return false;
					}
					else if(bsc2.complement == null ||(bsc2.complement != null && !bsc1.superClasses.contains(bsc2.complement)))
					if(bsc1.complement == null || (bsc1.complement != null && !bsc2.superClasses.contains(bsc1.complement)))
						return false;
		return true;
	}


	private void ruleR_top() {
		// TODO Auto-generated method stub
//		if(RHS instanceof REL2ClassImpl && RHS.asREL2ClassImpl().getIRI().toString().equals("Generic_Goal"))
//			System.out.println();
		if(!LHS.getOriginalSuperClasses().contains(RHS))
			for(REXClassExpressionImpl context:factory.contexts)
				if(context.satisfiable)
				inferC_sub_D(context, RHS);
	}


	private void ruleR_some_I_2() {
		// TODO Auto-generated method stub
		// individual and inverse specific rules
		if(context instanceof REXIndividualImpl)
		for(REXObjectPropertyExpressionImpl role:context.implications.keySet())
			if(role.getInversePropertyExpression() != null)
			for(REXObjectPropertyExpressionImpl s:role.getInversePropertyExpression().getSuperRoles())
				if(s.onLHS)
				{
					REXClassExpressionImpl newSome = factory.testREL2ObjectSomeValuesFrom(s, RHS);
					if(newSome != null && newSome.originalLHS)
						for(REXClassExpressionImpl newContext:context.implications.get(role))
							if(newContext instanceof REXIndividualImpl)
						{
							inferstrictC_sub_D(newContext,newSome);
						}
					// the <=1 rule
					// when D \sub \exists R.LHS
					// LHS \sub RHS
					// if there are some
					// R \sub S
					// D \sub <=1 S.RHS
					// and there are some
					// LHS \sub sup s.t. \forall S.sup occurs in the ontology
					// then D \sub \forall S.sup

					//  we first test the existence of <=1 S.RHS
//					if(RHS.maxs.containsKey(1) && RHS.maxs.get(1).containsKey(s))
					if(RHS.hasMax(1, s))
					{
						HashSet<REXClassExpressionImpl> supalls = new HashSet<REXClassExpressionImpl>();
						for(REXClassExpressionImpl sup:LHS.superClasses)
						{
							// now we test the existence of \forall S.sup
							REXClassExpressionImpl all = factory.testREL2ObjectAllValuesFrom(s, sup);
							if(all != null)
								supalls.add(all);
						}
						if(supalls.size() > 0)
						for(REXClassExpressionImpl newcontext:context.implications.get(role))
							// then we test if D \sub <=1 S.RHS
//							if(newcontext.superClasses.contains(RHS.maxs.get(1).get(s)))
							if(newcontext instanceof REXIndividualImpl && newcontext.superClasses.contains(RHS.getMax(1, s)))
								for(REXClassExpressionImpl supall:supalls)
									inferstrictC_sub_D(newcontext,supall);
					}
					// the <=1 rule again
					REXClassExpressionImpl all = factory.testREL2ObjectAllValuesFrom(s, RHS);
					if(all != null && context.predecessors.get(role) != null)
					{
						for(REXClassExpressionImpl newcontext:context.predecessors.get(role))
							for(REXClassExpressionImpl sup:LHS.superClasses)
								// then we test the existence of <=1 S.sup
								// if one has already been found, then we don't need to test again
//								if(sup.maxs.containsKey(1) && sup.maxs.get(1).containsKey(s) && newcontext.superClasses.contains(sup.maxs.get(1).get(s)))
								if(sup.hasMax(1, s) && newcontext.superClasses.contains(sup.getMax(1, s)))
								{
									inferstrictC_sub_D(newcontext,all);
									break;
								}
					}
				}
		}


	private void ruleR_some_I_1() {
		// TODO Auto-generated method stub
		
		// individual and inverse role specific rules
		if(context instanceof REXIndividualImpl && RHSFiller instanceof REXIndividualImpl && RHSrole.getInversePropertyExpression() != null)
			for(REXObjectPropertyExpressionImpl s:RHSrole.getInversePropertyExpression().getSuperRoles())
				if(s.onLHS)
				{
					boolean leq = true;
					for(REXClassExpressionImpl sup:context.superClasses)
					{
						REXClassExpressionImpl newSome = factory.testREL2ObjectSomeValuesFrom(s, sup);
						if(newSome != null && newSome.originalLHS)
							inferstrictC_sub_D(RHSFiller,newSome);
//						if(leq && sup.maxs.containsKey(1) && sup.maxs.get(1).containsKey(s))
						REXClassExpressionImpl max1 = sup.getMax(1, s);
						if(leq && max1 != null)
						{
//							REL2ClassExpressionImpl max1 = sup.maxs.get(1).get(s);
							if(RHSFiller.superClasses.contains(max1))
							{
								for(REXClassExpressionImpl sup2:context.superClasses)
								{
									REXClassExpressionImpl all = factory.testREL2ObjectAllValuesFrom(s, sup2);
									if(all != null)
										inferstrictC_sub_D(RHSFiller,all);
								}
								leq = false;
							}
						}
					}
					//				for(REL2ClassExpressionImpl sup:context.superClasses)
				}	
	}


	public void process(){
//				timer.start();
		while(factory.consistency)
		{
			context = activecontexts.poll();
			if(context == null)
				break;
			while(factory.consistency && context.satisfiable)
			{
				//				if(context.toString().equals("SauvignonBlancGrape"))
				//					System.out.println();
//				if(!context.localSchedule.isEmpty())
//					axiom = context.localSchedule.poll();
//				else
					axiom = context.schedulePoll();
				if(axiom == null)
					break;
				processAxiom();
			}
			reasoner.deactivateContext(context);
		}
//				timer.stop();
//				System.out.println(timer);
	}


	void processAxiom(){
		LHS = axiom.lhs;
		RHSrole = axiom.rhsRole;
		RHSFiller = axiom.rhsFiller;
		if(RHSrole != null)
			RHS = factory.testREL2ObjectSomeValuesFrom(RHSrole, RHSFiller);
		else
			RHS = RHSFiller;

//		if(LHS.toString().equals("Image") && RHSFiller.toString().contains("has_image_format"))
//			System.out.println();
//		if(LHS.toString().equals("ImageFormat") && RHSFiller.toString().equals("Thing"))
//			System.out.println();
//		if(LHS.toString().contains("(has_image_format SOME Thing)") && RHSFiller.toString().equals("PhysicalBioAssay"))
//			System.out.println();
//		if(LHS.toString().contains("has_image_format") && RHSFiller.toString().contains("PhysicalBioAssay"))
//			System.out.println();
//		if(LHS.toString().equals("PhysicalBioAssay") && RHSFiller.toString().equals("BioAssay"))
//			System.out.println();
		proceedTBox();
	}


	private void ruleR_and_minus() {
		// TODO Auto-generated method stub
		// C \sub D1 \and D2 => C \sub D1, C \sub D2
		// context is the same as local context = LHS;
		if(RHS instanceof REXObjectIntersectionOfImpl)
		{
			REXObjectIntersectionOfImpl and = (REXObjectIntersectionOfImpl) RHS;
			for(REXClassExpressionImpl conjunct:and.getEntities())
				inferC_sub_D(LHS, conjunct);				
		}
	}


	private void ruleR_and_plus() {
		// TODO Auto-generated method stub
		for(REXClassExpressionImpl conjunct:RHS.Ands.keySet())
		{
			REXClassExpressionImpl and = RHS.Ands.get(conjunct);
			if(LHS.superClasses.contains(conjunct) && and.originalLHS)
				inferC_sub_D(LHS, and);
		}
	}


	private void ruleR_bot_1() {
		// TODO Auto-generated method stub
		if(RHSFiller.superClasses.contains(factory.bottom))
			inferC_sub_D(LHS,factory.bottom);
	}


	private void ruleR_bot_2() {
		// TODO Auto-generated method stub
		if(RHS == factory.bottom)
			for(REXObjectPropertyExpressionImpl role:context.predecessors.keySet())
				for(REXClassExpressionImpl predecessor:context.predecessors.get(role))
				{
					inferC_sub_D(predecessor,factory.bottom);
				}
	}


	private void ruleR_cardin() {
		// TODO Auto-generated method stub
		// C \sub D
		// =>
		// >=n r.C \sub >=m s.D
		for(Integer i:LHS.mins.keySet())
		{
			for(Integer j:RHS.mins.keySet())
				if(i >= j)
					for(REXObjectPropertyExpressionImpl r:LHS.mins.get(i).keySet())
						for(REXObjectPropertyExpressionImpl s:r.getSuperRoles())
						{
							REXObjectMinCardinalityImpl Y = RHS.mins.get(j).get(s);
							if(Y!=null)
							{
								REXObjectMinCardinalityImpl X = LHS.mins.get(i).get(r);
								inferstrictC_sub_D(X,Y);
							}
						}
			if(false)
			for(REXObjectPropertyExpressionImpl r:LHS.mins.get(i).keySet())
				for(REXObjectPropertyExpressionImpl s:r.getSuperRoles())
				{
					REXClassExpressionImpl newSome = factory.testREL2ObjectSomeValuesFrom(s, RHS);
					if(newSome != null && newSome.originalLHS)
					{
						REXObjectMinCardinalityImpl X = LHS.mins.get(i).get(r);
						inferstrictC_sub_D(X,newSome);
					}
				}

		}
	}


	private void ruleR_comp() {
		// TODO Auto-generated method stub
		if(RHS.complement!=null &&LHS.complement != null)
			inferstrictC_sub_D(RHS.complement,LHS.complement);
	}


	private void ruleR_func_1() {
		// TODO Auto-generated method stub
		for(REXObjectPropertyExpressionImpl s:RHSrole.getSuperRoles())
			if(s.onLHS)
			{
				for(REXClassExpressionImpl sup:context.superClasses)
				{
					if(sup instanceof REXObjectMax1CardinalityImpl)
					{
						REXObjectMax1CardinalityImpl max = (REXObjectMax1CardinalityImpl) sup;
						if(max.getProperty() == s && RHSFiller.superClasses.contains(max.getFiller()))
							for(REXClassExpressionImpl sup2:s.alls.keySet())
								if(RHSFiller.superClasses.contains(sup2))
								{
//									REL2ClassExpressionImpl all = factory.testREL2ObjectAllValuesFrom(s, sup2);
//									if(all != null)
										inferstrictC_sub_D(LHS,factory.testREL2ObjectAllValuesFrom(s, sup2));
								}
					}
					// alternative
					if(sup.complement != null && sup.complement instanceof REXObjectMinCardinalityImpl)
					{
						REXObjectMinCardinalityImpl min = (REXObjectMinCardinalityImpl) sup.complement;
						if(min.getCardinalityValue() == 2 && min.getProperty() == s && min.getFiller().complement != null && RHSFiller.superClasses.contains(min.getFiller().complement))
							for(REXClassExpressionImpl sup2:s.alls.keySet())
								if(RHSFiller.superClasses.contains(sup2))
								{
//									REL2ClassExpressionImpl all = factory.testREL2ObjectAllValuesFrom(s, sup2);
//									if(all != null)
										inferstrictC_sub_D(LHS,factory.testREL2ObjectAllValuesFrom(s, sup2));
								}
					}
				}
			}
	}


	private void ruleR_func_2() {
		// TODO Auto-generated method stub
		if(RHS instanceof REXObjectMax1CardinalityImpl)
		{
			REXObjectMax1CardinalityImpl max = (REXObjectMax1CardinalityImpl) RHS;
			if(max.getProperty().onLHS)
			for(REXObjectPropertyExpressionImpl r:LHS.implications.keySet())
				if(r.getSuperRoles().contains(max.getProperty()))
					for(REXClassExpressionImpl filler:LHS.implications.get(r))
						if(filler.superClasses.contains(max.getFiller()))
						{
							for(REXClassExpressionImpl sup2:max.getProperty().alls.keySet())
								if(filler.superClasses.contains(sup2))
								{
//									REL2ClassExpressionImpl all = factory.testREL2ObjectAllValuesFrom(max.getProperty(), sup2);
//									if(all != null)
										inferstrictC_sub_D(LHS,factory.testREL2ObjectAllValuesFrom(max.getProperty(), sup2));
								}
							for(REXClassExpressionImpl supC2:max.getProperty().somes.keySet())
								if(supC2.complement != null && filler.superClasses.contains(supC2.complement) && supC2.complement.hasMax(1, max.getProperty()))
									inferstrictC_sub_D(LHS,factory.testREL2ObjectAllValuesFrom(max.getProperty(), supC2.complement));
						}
		}
		// alternative
		if(RHS.complement != null && RHS.complement instanceof REXObjectMinCardinalityImpl)
		{
			REXObjectMinCardinalityImpl min = (REXObjectMinCardinalityImpl) RHS.complement;
			if(min.getProperty().onLHS && min.getFiller().complement != null)
				for(REXObjectPropertyExpressionImpl r:LHS.implications.keySet())
					if(r.getSuperRoles().contains(min.getProperty()))
						for(REXClassExpressionImpl filler:LHS.implications.get(r))
							if(filler.superClasses.contains(min.getFiller().complement))
							{
								for(REXClassExpressionImpl sup2:min.getProperty().alls.keySet())
									if(filler.superClasses.contains(sup2))
									{
//										REL2ClassExpressionImpl all = factory.testREL2ObjectAllValuesFrom(max.getProperty(), sup2);
//										if(all != null)
											inferstrictC_sub_D(LHS,factory.testREL2ObjectAllValuesFrom(min.getProperty(), sup2));
									}
								for(REXClassExpressionImpl supC2:min.getProperty().somes.keySet())
									if(supC2.complement != null && filler.superClasses.contains(supC2.complement) && supC2.complement.hasMax(1, min.getProperty()))
										inferstrictC_sub_D(LHS,factory.testREL2ObjectAllValuesFrom(min.getProperty(), supC2.complement));
							}
				
		}
	}


	private void ruleR_neg() {
		// TODO Auto-generated method stub
		if(RHS.complement != null && LHS.superClasses.contains(RHS.complement))
			inferC_sub_D(LHS,factory.bottom);
	}


	private void ruleR_o_1() {
		// TODO Auto-generated method stub
		// A \sub \some r.C
		// C \sub \some s.B
		// r \sub r'
		// s \sub s'
		// r' o s' \sub t'
		// t' \sub t
		// t.related
		// =>
		// A \sub \some r.B
		for(REXObjectPropertyExpressionImpl rprime:RHSrole.getSuperRoles())
			for(REXObjectPropertyExpressionImpl s:context.implications.keySet())
				for(REXObjectPropertyExpressionImpl sprime:s.getSuperRoles())
					if(rprime.chains.get(sprime) != null)
						for(REXObjectPropertyExpressionImpl tprime:rprime.chains.get(sprime))
							for(REXObjectPropertyExpressionImpl t:tprime.getSuperRoles())
								if(t.onLHS)
									for(REXClassExpressionImpl B:context.implications.get(s))
										if(!LHS.implications.containsKey(t) || !LHS.implications.get(t).contains(B))
											inferNewC_sub_r_D(LHS,t,B,false);
	}

	private void ruleR_o_2() {
		// TODO Auto-generated method stub
		// A \sub \some r.C
		// C \sub \some s.B
		// r \sub r'
		// s \sub s'
		// r' o s' \sub t'
		// t' \sub t
		// t.related
		// =>
		// A \sub \some r.B
		for(REXObjectPropertyExpressionImpl sprime:RHSrole.getSuperRoles())
			for(REXObjectPropertyExpressionImpl r:context.predecessors.keySet())
				for(REXObjectPropertyExpressionImpl rprime:r.getSuperRoles())
					if(rprime.chains.get(sprime) != null)
						for(REXObjectPropertyExpressionImpl tprime:rprime.chains.get(sprime))
							for(REXObjectPropertyExpressionImpl t:tprime.getSuperRoles())
								if(t.onLHS)
									for(REXClassExpressionImpl A:context.predecessors.get(r))
										if(!A.implications.containsKey(t) || !A.implications.get(t).contains(RHSFiller))
											inferNewC_sub_r_D(A,t,RHSFiller, false);

	}

	private void ruleR_or() {
		// TODO Auto-generated method stub
		for(REXClassExpressionImpl or:RHS.Ors.values())
		{
			if(!LHS.superClasses.contains(or) && or.originalLHS)
				inferC_sub_D(LHS, or);
		}
	}

	private void ruleR_some_1() {
		// TODO Auto-generated method stub
		for(REXObjectPropertyExpressionImpl s:RHSrole.getSuperRoles())
			if(s.onLHS)
			{
				boolean leq = true;
				for(REXClassExpressionImpl sup:context.superClasses)
				{
					REXClassExpressionImpl newSome = factory.testREL2ObjectSomeValuesFrom(s, sup);
					if(newSome != null && newSome.originalLHS)
						inferstrictC_sub_D(LHS,newSome);
//					if(leq && sup.maxs.containsKey(1) && sup.maxs.get(1).containsKey(s))
					REXClassExpressionImpl max1 = sup.getMax(1, s);
					if(leq && max1 != null)
					{
//						REL2ClassExpressionImpl max1 = sup.maxs.get(1).get(s);
						if(LHS.superClasses.contains(max1))
						{
							for(REXClassExpressionImpl sup2:context.superClasses)
							{
								REXClassExpressionImpl all = factory.testREL2ObjectAllValuesFrom(s, sup2);
								if(all != null)
									inferstrictC_sub_D(LHS,all);
							}
							leq = false;
						}
					}
				}
				//				for(REL2ClassExpressionImpl sup:context.superClasses)
			}
	}

	private void ruleR_some_2() {
		// TODO Auto-generated method stub
		for(REXObjectPropertyExpressionImpl role:context.predecessors.keySet())
			for(REXObjectPropertyExpressionImpl s:role.getSuperRoles())
				if(s.onLHS)
				{
					REXClassExpressionImpl newSome = factory.testREL2ObjectSomeValuesFrom(s, RHS);
					if(newSome != null && newSome.originalLHS)
						for(REXClassExpressionImpl newContext:context.predecessors.get(role))
						{
							inferstrictC_sub_D(newContext,newSome);
						}
					// the <=1 rule
					// when D \sub \exists R.LHS
					// LHS \sub RHS
					// if there are some
					// R \sub S
					// D \sub <=1 S.RHS
					// and there are some
					// LHS \sub sup s.t. \forall S.sup occurs in the ontology
					// then D \sub \forall S.sup

					//  we first test the existence of <=1 S.RHS
//					if(RHS.maxs.containsKey(1) && RHS.maxs.get(1).containsKey(s))
					if(RHS.hasMax(1, s))
					{
						HashSet<REXClassExpressionImpl> supalls = new HashSet<REXClassExpressionImpl>();
						for(REXClassExpressionImpl sup:LHS.superClasses)
						{
							// now we test the existence of \forall S.sup
							REXClassExpressionImpl all = factory.testREL2ObjectAllValuesFrom(s, sup);
							if(all != null)
								supalls.add(all);
						}
						if(supalls.size() > 0)
						for(REXClassExpressionImpl newcontext:context.predecessors.get(role))
							// then we test if D \sub <=1 S.RHS
//							if(newcontext.superClasses.contains(RHS.maxs.get(1).get(s)))
							if(newcontext.superClasses.contains(RHS.getMax(1, s)))
								for(REXClassExpressionImpl supall:supalls)
									inferstrictC_sub_D(newcontext,supall);
					}
					// the <=1 rule again
					REXClassExpressionImpl all = factory.testREL2ObjectAllValuesFrom(s, RHS);
					if(all != null && context.predecessors.get(role) != null)
					{
						for(REXClassExpressionImpl newcontext:context.predecessors.get(role))
							for(REXClassExpressionImpl sup:LHS.superClasses)
								// then we test the existence of <=1 S.sup
								// if one has already been found, then we don't need to test again
//								if(sup.maxs.containsKey(1) && sup.maxs.get(1).containsKey(s) && newcontext.superClasses.contains(sup.maxs.get(1).get(s)))
								if(sup.hasMax(1, s) && newcontext.superClasses.contains(sup.getMax(1, s)))
								{
									inferstrictC_sub_D(newcontext,all);
									break;
								}
					}
				}
		
	}

		private void ruleR_forall_1()	{
			// A \sub \some r.B
			// A \sub C
			// => r \sub is, \some s.C \sub D, (B \and D) in O
			// A \sub \some r.(B \and D)
	
			if(RHSFiller == factory.top)
				if(RHSrole.getInversePropertyExpression() != null)
					for(REXObjectPropertyExpressionImpl s:RHSrole.getInversePropertyExpression().getSuperRoles())
						for(REXClassExpressionImpl C:LHS.superClasses)
						{
							REXObjectSomeValuesFromImpl somesC = factory.testREL2ObjectSomeValuesFrom(s, C);
							if(somesC != null)
							{
								for(REXClassExpressionImpl D:somesC.getOriginalSuperClasses())
									if(D != somesC)
										// ruleR_forall_*_1
										inferNewC_sub_r_D(LHS, RHSrole, D, true);
							}
						}
	
		}
	
		private void ruleR_forall_2()	{
			// A is the context
			// A \sub \some r.B
			// A \sub C
			// => r \sub is, \some s.C \sub D, (B \and D) in O
			// A \sub \some r.(B \and D)
	
			for(REXObjectPropertyExpressionImpl r:LHS.implications.keySet())
			{
				if(LHS.implications.get(r).contains(factory.top))
					for(REXObjectPropertyExpressionImpl is:r.getSuperRoles())
					{
						if(is.getInversePropertyExpression() != null)
						{
							REXObjectSomeValuesFromImpl somesC = factory.testREL2ObjectSomeValuesFrom(is.getInversePropertyExpression(), RHS);
							if(somesC != null)
							{
								for(REXClassExpressionImpl D:somesC.getOriginalSuperClasses())
									if(D != somesC)
										// ruleR_forall_*_2
										inferNewC_sub_r_D(LHS, r,D, true);
							}
						}
						for(REXClassExpressionImpl D:is.ranges)
							inferNewC_sub_r_D(LHS,r,D,true);
					}
			}
		}

	private void ruleR_sub_1() {
		// TODO Auto-generated method stub
		// D \sub C, C \sub E = > D \sub E
		// context is the same as local context == RHS;
		if(LHS == factory.bottom)
			return;
		else
			//			for(REL2ClassExpressionImpl E:RHS.originalSuperClasses)
			//				inferC_sub_D(LHS, E);
			for(REXClassExpressionImpl E:RHS.superClasses)
				inferC_sub_D(LHS,E);

	}


	private void ruleR_sub_2() {
		// TODO Auto-generated method stub
		// C \sub D = > C \sub E : D \sub E \in \O
		// context is the same as local context == LHS;
		if(RHS == factory.bottom)
		{
			LHS.satisfiable = false;
		}
//		else if(!factory.smallT)
		else{
						for(REXClassExpressionImpl E:RHS.getOriginalSuperClasses())
							inferC_sub_D(LHS, E);
			
//			for(REL2ClassExpressionImpl E:LHS.subClasses)
//				inferC_sub_D(E,RHS);
			
			if(factory.smallT && LHS.complement != null)
				for(REXClassExpressionImpl sup:LHS.complement.superClasses)
					if(sup.complement != null)
						if(RHS.complement != null)
							inferC_sub_D(RHS.complement, sup);
						else if(sup.complement != null)
							inferC_sub_D(sup.complement, RHS);
		}				
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		process();
	}


}
