package eu.trowl.rex.reasoner.test;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import eu.trowl.rex.reasoner.Reasoner;
import eu.trowl.rex.util.Timer;


public class TBoxClassificationTestBatch {

	/**
	 * @param args
	 * @throws OWLOntologyCreationException 
	 * @throws InterruptedException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws OWLOntologyCreationException, InterruptedException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		String path = "C:/Users/zouyuanrenren/Eclipseworkspace/Benchmarks/AIJEvaluation/";

		File directory = new File(path);
		int i = 0;

		for(File file:directory.listFiles())
		{
			i++;
			System.out.print(i+" "+file.getName()+" ");
			if(file.getName().toLowerCase().endsWith("owl"))
				//			if(file.length() <= 100000000)
			{

				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
				Reasoner reasoner = new Reasoner(1);
				reasoner.load(ontology);
				reasoner.classify();
			}
			else
				System.out.println();
		}
	}

}
