package eu.trowl.rex.owlapi;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.IllegalConfigurationException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;


/** 
 * @author Yuan Ren
 * @version 2014-05-05
 */
public class REXReasonerFactory implements OWLReasonerFactory{

	public String getReasonerName() {
		// TODO Auto-generated method stub
		return "REL 2";
	}

	@Override
	public REXReasoner createReasoner(OWLOntology ontology) {
		// TODO Auto-generated method stub
		REXReasoner reasoner = new REXReasoner(ontology.getOWLOntologyManager(), ontology, bgp, MetaOn,disjoint);
		reasoner.manager.addOntologyChangeListener(reasoner, new REXChangeBroadcastStrategy());
		return reasoner;
	}
	
	@Override
	public REXReasoner createNonBufferingReasoner(OWLOntology arg0) {
		// TODO Auto-generated method stub
		REXReasoner reasoner = createReasoner(arg0);
		reasoner.bufferred = false;
		return reasoner;
	}

	@Override
	public OWLReasoner createNonBufferingReasoner(OWLOntology arg0,
			OWLReasonerConfiguration arg1) throws IllegalConfigurationException {
		// TODO Auto-generated method stub
		System.out.println("RELm2 does not support OWLReasonerConfiguration yet.");
		return createReasoner(arg0);	
	}


	@Override
	public OWLReasoner createReasoner(OWLOntology arg0,
			OWLReasonerConfiguration arg1) throws IllegalConfigurationException {
		// TODO Auto-generated method stub
		System.out.println("REL 2 does not support OWLReasonerConfiguration yet.");
		return createReasoner(arg0);	
	}

	private boolean bgp = false;
	public void setBGP(boolean bgp)
	{
		this.bgp = bgp;
	}
	private boolean MetaOn = false;
	public void setMetaOn(boolean MetaOn)
	{
		this.MetaOn = MetaOn;
	}
	private boolean disjoint = false;
	public void setDisjoint(boolean disjoint)
	{
		this.disjoint = disjoint;
	}
	
}
