package eu.trowl.rex.model.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import eu.trowl.rex.model.interfaces.REXClassExpression;

abstract public class REXClassExpressionImpl implements REXClassExpression {
	
	public AtomicBoolean isContext = new AtomicBoolean(false);
	public AtomicBoolean isActiveContext = new AtomicBoolean(false);
	public boolean originalLHS = false;
	public boolean satisfiable = true;
	protected Queue<REXSubClassOfImpl> schedule = new ConcurrentLinkedQueue<REXSubClassOfImpl>();
//	public Queue<REL2SubClassOfImpl> localSchedule = new ConcurrentLinkedQueue<REL2SubClassOfImpl>();
	public Set<REXClassExpressionImpl> superClasses = new HashSet<REXClassExpressionImpl>();
//	public Set<REL2ClassExpressionImpl> subClasses = new HashSet<REL2ClassExpressionImpl>();
	private Set<REXClassExpressionImpl> originalSuperClasses = new HashSet<REXClassExpressionImpl>();
//	public Set<REL2ClassExpressionImpl> equivalentClasses = new HashSet<REL2ClassExpressionImpl>();
	public Map<REXObjectPropertyExpressionImpl, Set<REXClassExpressionImpl>> predecessors = new HashMap<REXObjectPropertyExpressionImpl,Set<REXClassExpressionImpl>>();
	public Map<REXObjectPropertyExpressionImpl, Set<REXClassExpressionImpl>> implications = new HashMap<REXObjectPropertyExpressionImpl, Set<REXClassExpressionImpl>>();
//	public Map<REL2ObjectPropertyExpressionImpl, Set<REL2ClassExpressionImpl>> leastFillers = new HashMap<REL2ObjectPropertyExpressionImpl, Set<REL2ClassExpressionImpl>>();
//	public Map<REL2ObjectPropertyExpressionImpl, Set<REL2ClassExpressionImpl>> RelationFillers = new HashMap<REL2ObjectPropertyExpressionImpl, Set<REL2ClassExpressionImpl>>();
//	public Map<REL2ObjectPropertyExpressionImpl, Set<REL2ClassExpressionImpl>> predFillers = new HashMap<REL2ObjectPropertyExpressionImpl, Set<REL2ClassExpressionImpl>>();
	
	public Map<REXClassExpressionImpl, REXObjectIntersectionOfImpl> Ands = new HashMap<REXClassExpressionImpl, REXObjectIntersectionOfImpl>();
	public Map<REXClassExpressionImpl, REXObjectUnionOfImpl> Ors = new HashMap<REXClassExpressionImpl, REXObjectUnionOfImpl>();
	public REXClassExpressionImpl	complement = null;
	public Map<Integer, HashMap<REXObjectPropertyExpressionImpl, REXObjectMinCardinalityImpl>> mins = new HashMap<Integer, HashMap<REXObjectPropertyExpressionImpl, REXObjectMinCardinalityImpl>>();
	public Map<Integer, HashMap<REXObjectPropertyExpressionImpl, REXObjectMaxCardinalityImpl>> maxs = new HashMap<Integer, HashMap<REXObjectPropertyExpressionImpl, REXObjectMaxCardinalityImpl>>();
	

	public ConcurrentHashMap<REXClassExpressionImpl, REXSubClassOfImpl> subAxioms = new ConcurrentHashMap<REXClassExpressionImpl, REXSubClassOfImpl>();
	public ConcurrentHashMap<REXObjectPropertyExpressionImpl, ConcurrentHashMap<REXClassExpressionImpl, REXSubClassOfImpl>> subSomeAxioms = new ConcurrentHashMap<REXObjectPropertyExpressionImpl, ConcurrentHashMap<REXClassExpressionImpl, REXSubClassOfImpl>>();
	public ConcurrentHashMap<REXClassExpressionImpl, REXSubClassOfImpl> subAllAxioms = new ConcurrentHashMap<REXClassExpressionImpl, REXSubClassOfImpl>();
	public ConcurrentHashMap<REXClassExpressionImpl, REXSubClassOfImpl> subMax1Axioms = new ConcurrentHashMap<REXClassExpressionImpl, REXSubClassOfImpl>();
	
	public int cardin = 1;
	
	public abstract void LHS();
	
	public abstract void RHS();

	public void printSchedule() {
		// TODO Auto-generated method stub
		System.out.println(schedule);
	}

	public boolean emptySchedule() {
		// TODO Auto-generated method stub
		return schedule.isEmpty();
	}

	public REXSubClassOfImpl schedulePoll() {
		// TODO Auto-generated method stub
		return schedule.poll();
	}

	public void scheduleAdd(REXSubClassOfImpl newAxiom) {
		// TODO Auto-generated method stub
		schedule.add(newAxiom);
	}

	public void clearSchedule() {
		// TODO Auto-generated method stub
		schedule.clear();
	}
	
	public boolean hasMax(int n, REXObjectPropertyExpressionImpl role)
	{
		if(maxs.containsKey(n) && maxs.get(n).containsKey(role))
			return true;
		if(mins.containsKey(n+1) && mins.get(n+1).containsKey(role) && mins.get(n+1).get(role).complement != null)
			return true;
		return false;
	}
	
	public REXClassExpressionImpl getMax(int n, REXObjectPropertyExpressionImpl role){
		
		if(maxs.containsKey(n) && maxs.get(n).containsKey(role))
			return maxs.get(n).get(role);
		if(mins.containsKey(n+1) && mins.get(n+1).containsKey(role) && mins.get(n+1).get(role).complement != null)
			return mins.get(n+1).get(role).complement;
		return null;
	}

	public REXClassImpl asREXClassImpl() {
		// TODO Auto-generated method stub
		return (REXClassImpl) this;
	}
	

	public void addOriginalSuperClasses(REXClassExpressionImpl OSC) {
		// TODO Auto-generated method stub
		originalSuperClasses.add(OSC);
	}
	
	public Set<REXClassExpressionImpl> getOriginalSuperClasses() {
		return originalSuperClasses;
	}

	Map<REXObjectPropertyExpressionImpl, Integer> RelationSize = new HashMap<REXObjectPropertyExpressionImpl, Integer>();
	
//	public boolean addRelation(REL2ObjectPropertyExpressionImpl role,
//			REL2ClassExpressionImpl object) {
//		// TODO Auto-generated method stub
//		Set<REL2ClassExpressionImpl> imps = implications.get(role);
//		int size;		
//		if(imps == null)
//		{
//			imps = new HashSet<REL2ClassExpressionImpl>();
//			implications.put(role, imps);
//			size = 0;
//		}
//		else
//			size = RelationSize.get(role);
//		boolean success = imps.add(object);
//		
//		if(object instanceof REL2ObjectMinCardinalityImpl)
//			size+=((REL2ObjectMinCardinalityImpl)object).cardinalityValue;
//		else
//			size+=object.cardin;
//		RelationSize.put(role, size);
////		cardinTestSimple(role,object);
//		
//		return success;
//	}
	
	public Map<REXObjectMinCardinalityImpl, ArrayList<REXClassExpressionImpl>> completeLists = new HashMap<REXObjectMinCardinalityImpl, ArrayList<REXClassExpressionImpl>>();
	public Map<REXObjectMinCardinalityImpl, Integer> totalWeight = new HashMap<REXObjectMinCardinalityImpl, Integer>();

	public abstract REXClassExpressionImpl testComplement();

	public abstract boolean specifiedBy(REXClassImpl cls);
	
	
	
	
}
