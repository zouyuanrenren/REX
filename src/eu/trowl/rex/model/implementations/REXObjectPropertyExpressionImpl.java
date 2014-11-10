package eu.trowl.rex.model.implementations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eu.trowl.rex.model.interfaces.REXObjectPropertyExpression;

abstract public class REXObjectPropertyExpressionImpl implements
		REXObjectPropertyExpression {

	protected Set<REXObjectPropertyExpressionImpl> superProperties = new HashSet<REXObjectPropertyExpressionImpl>();
	
	protected Set<REXObjectPropertyExpressionImpl> subProperties = new HashSet<REXObjectPropertyExpressionImpl>();
//
	public Map<REXObjectPropertyExpressionImpl, Set<REXObjectPropertyExpressionImpl>> chains = new HashMap<REXObjectPropertyExpressionImpl, Set<REXObjectPropertyExpressionImpl>>();
	
	public Map<REXClassExpressionImpl, REXObjectSomeValuesFromImpl> somes = new HashMap<REXClassExpressionImpl, REXObjectSomeValuesFromImpl>();
	
	public Map<REXClassExpressionImpl, REXObjectAllValuesFromImpl> alls = new HashMap<REXClassExpressionImpl, REXObjectAllValuesFromImpl>();
	
//	public Map<REL2ClassExpressionImpl, REL2ObjectMax1CardinalityImpl> max1s = new HashMap<REL2ClassExpressionImpl, REL2ObjectMax1CardinalityImpl>();
	
//	public Map<Integer, HashMap<REL2ClassExpressionImpl, REL2ObjectMaxCardinalityImpl>> maxs = new HashMap<Integer, HashMap<REL2ClassExpressionImpl, REL2ObjectMaxCardinalityImpl>>();
//
//	public Map<Integer, HashMap<REL2ClassExpressionImpl, REL2ObjectMinCardinalityImpl>> mins = new HashMap<Integer, HashMap<REL2ClassExpressionImpl, REL2ObjectMinCardinalityImpl>>();

	public Set<REXClassExpressionImpl> ranges = new HashSet<REXClassExpressionImpl>();
	
	public Set<REXObjectPropertyExpressionImpl> disjoints = new HashSet<REXObjectPropertyExpressionImpl>();
	
	public boolean onLHS = false;
	
	public boolean onRHSAllValuesFrom = false;
	
	public boolean onMax1Cardinality = false;
	
	public boolean irrflexive = false;
	
	public boolean functional = false;

	@Override
	public abstract REXObjectPropertyExpressionImpl getInversePropertyExpression();

	public boolean addSuperRole(REXObjectPropertyExpressionImpl superRole) {
		// TODO Auto-generated method stub
		return superProperties.add(superRole);
	}
	
	public boolean addSuperRoles(Set<REXObjectPropertyExpressionImpl> superRoles){
		return superProperties.addAll(superRoles);
	}
	
	public Set<REXObjectPropertyExpressionImpl> getSuperRoles(){
		return superProperties;
	}
	
	public abstract void LHS();

	public boolean addSuberRole(REXObjectPropertyExpressionImpl subRole) {
		// TODO Auto-generated method stub
		return subProperties.add(subRole);
	}

}
