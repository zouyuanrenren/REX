package eu.trowl.rex.owlapi;

import java.util.List;

import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeBroadcastStrategy;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;

/** 
 * @author Yuan Ren
 * @version 2014-05-05
 */
public class REXChangeBroadcastStrategy implements
		OWLOntologyChangeBroadcastStrategy {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void broadcastChanges(OWLOntologyChangeListener arg0,
			List<? extends OWLOntologyChange> arg1) throws OWLException {
		// TODO Auto-generated method stub
		arg0.ontologiesChanged(arg1);
	}

}
