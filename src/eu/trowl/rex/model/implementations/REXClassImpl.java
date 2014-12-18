package eu.trowl.rex.model.implementations;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;

import eu.trowl.rex.factory.REXDataFactory;
import eu.trowl.rex.model.interfaces.REXClass;

public class REXClassImpl extends REXClassExpressionImpl implements REXClass {

	protected IRI iri;
	public boolean original = true;
	
	public Set<REXClassImpl> equivalence = new HashSet<REXClassImpl>();
	
//	public Absorbable absorbable = Absorbable.Unknown;
	
	public Set<REXClassExpressionImpl> unfoldableSuperClasses = new HashSet<REXClassExpressionImpl>();
	public Set<REXClassExpressionImpl> unfoldableDefinition = new HashSet<REXClassExpressionImpl>();

	
	public REXClassImpl(OWLClass clazz) {
		// TODO Auto-generated constructor stub
		iri = clazz.getIRI();
	}

	public REXClassImpl(IRI iri) {
		// TODO Auto-generated constructor stub
		this.iri = iri;
	}

	public REXClassImpl() {
		// TODO Auto-generated constructor stub
		this.iri = IRI.create("http://trowl.eu/APPROXC");
	}

	@Override
	public IRI getIRI() {
		// TODO Auto-generated method stub
		return iri;
	}
	
	@Override
	public void LHS() {
		// TODO Auto-generated method stub
		originalLHS = true;
	}

	@Override
	public String toString() {
		return iri.getFragment();
	}

	@Override
	public void RHS() {
		// TODO Auto-generated method stub
		
	}

	public boolean canBeDefinedBy(REXClassExpressionImpl... exps) {
//		for(REXClassExpressionImpl eq:unfoldableDefinition)
//			if(!(eq instanceof REXClassImpl))
		if(isDefined())
				return false;
		for(REXClassExpressionImpl exp:exps)
			if(exp != this && exp.isDefinedBy(this))
				return false;
		return true;
		
	}

	@Override
	public boolean isDefinedBy(REXClassImpl cls) {
		// TODO Auto-generated method stub
//		if(this.toString() == "Full")
//			System.out.println(this);
//		if(cls.toString() == "Thing")
//			return false;
//		if(this.iri.toString().equals("http://www.w3.org/2002/07/owl#Thing") || cls.iri.toString().equals("http://www.w3.org/2002/07/owl#Thing"))
//			return false;
		if(this == cls)
			return true;
//		for(REXClassExpressionImpl sup:unfoldableSuperClasses)
//			if(sup.specifiedBy(cls))
//				return true;
		for(REXClassExpressionImpl eq:unfoldableDefinition)
//			if(!(eq instanceof REXClassImpl))
//				return eq.isDefinedBy(cls);
			if(eq.isDefinedBy(cls))
				return true;
		return false;
	}

//	public boolean canBeAbsorbed(Set<REXClassExpressionImpl> exps) {
//		// TODO Auto-generated method stub
//		for(REXClassExpressionImpl eq:unfoldableDefinition)
//			if(!(eq instanceof REXClassImpl))
//				return false;
//		for(REXClassExpressionImpl exp:exps)
//			if(exp.specifiedBy(this))
//				return false;
//		return true;
//		}

	public boolean isDefined() {
		// TODO Auto-generated method stub
		for(REXClassExpressionImpl def:unfoldableDefinition)
			if(def instanceof REXClassImpl)
			{
				if(def.asREXClassImpl().isDefined())
				return true;
			}
			else
				return true;
		return false;
	}

	@Override
	public boolean isPartialAbsorbable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCompletelyAbsorbable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addToPatialAbsorbable(Set<REXClassExpressionImpl> pas) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addToNotCompletelyAbsorbable(Set<REXClassExpressionImpl> nCA) {
		// TODO Auto-generated method stub
		nCA.add(this);
	}

	@Override
	public REXClassExpressionImpl testComplement() {
		// TODO Auto-generated method stub
		return complement;
	}

	@Override
	public REXClassExpressionImpl getComplement(REXDataFactory rexDataFactory) {
		// TODO Auto-generated method stub
		if(complement == null)
		{
			complement = new REXObjectComplementOfImpl(this);
			complement.complement = this;
		}
		return complement;
	}

}
