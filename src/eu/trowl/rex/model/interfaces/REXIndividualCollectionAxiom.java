package eu.trowl.rex.model.interfaces;

import java.util.List;

public interface REXIndividualCollectionAxiom extends REXAssertion {

	public List<? extends REXIndividual> getIndividuals();
}
