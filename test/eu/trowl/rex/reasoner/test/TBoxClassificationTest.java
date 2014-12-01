package eu.trowl.rex.reasoner.test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;

import eu.trowl.rex.owlapi.REXReasoner;
import eu.trowl.rex.owlapi.REXReasonerFactory;
import eu.trowl.rex.reasoner.Reasoner;
import eu.trowl.rex.util.Timer;


public class TBoxClassificationTest {

	/**
	 * @param args
	 * @throws OWLOntologyCreationException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws OWLOntologyCreationException, InterruptedException {
		// TODO Auto-generated method stub
//		String path = "file:///C:/Users/zouyuanrenren/Eclipseworkspace/Benchmarks/AIJEvaluation/";
		String path = "file:///C:/Users/zouyuanrenren/Eclipseworkspace/Benchmarks/ORE2013/DL/";
		
//		String f = path+"6a2afaf6-ea7f-4ce2-936b-62b283e589a8_lscale.owl";
//		String f = path+"6785a156-f582-4a75-823d-65102e344a45_tology.owl";
//		String f = path+"2614d330-3144-4e74-b943-ced1dd906212_mmyard.owl";
//		String f = path+"a26b6246-205a-427b-b90b-2d19ae6c0070_rument.owl";
//		String f = path+"72db2148-edfc-4c63-a142-52d2a0eb0b9b_inst15.owl";
//		String f = path+"GALEN-Full-Union_ALCHOI(D).owl";
//		String f = path+"7e1c9977-4d4b-49ab-808f-1ca185be99c5_BCR.owl";
//		String f = path+"00c21813-4923-4e1a-9c0e-b6163accae5f_t8.rdf.owl";
//		String f = path+"326b8b3d-1f49-42cd-8b1e-dd693bf3547d_Unified.owl";
//		String f = path+"a5ac64cb-3d75-48e2-8106-b05277c9e16e_lscale.owl";
//		String f = path+"0cd30725-3ae5-4c41-820f-c6a16240e802_-OKB_2.owl";
//		String f = path+"ed702658-c8e0-48bb-af79-34c625fb8039_tology.owl";
//		String f = path+"5043d1d7-86d8-4e78-af21-f5eca30cab3f_tology.owl";
//		String f = path+"240a6af0-3a59-41b6-afe6-b00648b43315__inst1.owl";
//		String f = path+"63153319-5b25-43ed-86b2-f0714c0acd7c_O-full.owl";
//		String f = path+"645a3ff8-698d-4904-9cff-9bf134ff6a0c_tology.owl";
//		String f = path+"71134246-639b-4ed8-aff8-2261cb41cd4e_tp3.owl";
//		String f = path+"8f298886-1bb7-41b6-810b-ec036ad75fee_2Fchemical";
//		String f = path+"60fb4605-bf17-45a4-9d3f-8e70a871c013_dation.owl";
//		String f = path+"f11a9684-6c48-43c5-a343-2923cf4247bc_Unified.owl";
//		String f = path+"125bc127-6fa8-4dd3-add7-485cd3b93965_Turtle.owl";
//		String f = path+"39ab6f44-4c3a-427c-b716-29819c966181_2Fctxmatch";
//		String f = path+"ca3f25c3-c60d-4bf3-b691-3282c541bf39_people.owl";
//		String f = path+"00793.owl";
//		String f = path+"53f9f8e8-9463-4612-b082-45508fb37137_chains.owl";
//		String f = path+"fa1ae516-9f0b-42f1-a66a-708d5a9cf9a0_ets-v1.owl";
//		String f = path+"2691dbdf-ec4d-4d3a-bc5f-e2980f60853e_amo3.owl";
//		String f = path+"844f7541-1f1f-4004-ae32-42e5cae21f4a_people.owl";
//		String f = path+"9aa706f1-eaa9-40c2-9028-46072f45ed71_amo.owl";
//		String f = path+"777a6585-c41f-44bb-b6ca-079c83c1bde3_BpetsA.owl";
//		String f = path+"aee636cb-4238-41af-a3d6-541d30f2e7ed_spills.owl";
//		String f = path+"51e460e9-619a-4683-b9ad-ec261e4bf06d_7-0342.owl";
//		String f = path+"8a74f1e5-2162-435f-a7a5-4258d2571681_ndPets.owl";
//		String f = path+"578811f0-41fd-4e88-a580-0cd6fe989d6c_1615.owl";
//		String f = path+"761b8a30-9fe4-40e2-9b91-45f2d65a6e44_mint.owl";
//		String f = path+"lipid-ontology.1183.owl";
//		String f = path+"cell-culture-ontology.3108.owl.xml";
//		String f = path+"b4885109-03d6-4e14-a89d-bc05a64b31df_pizza.owl";
//		String f = path+"bb3945c9-a7fb-476b-87dc-1ce8e69a28eb_dbpedia.owl";
		String f = path+"87800dc9-355e-4d61-b761-dbe9d7db4526_111201.owl";
		
//		String f = path+"wine_nodatatype.owl";
//		String f= path+"BoC_big5_0.owl";
//		String f = path+"Travel4_7.owl";
//		String f = path+"Travel.owl";
//		String f = path+"NCIt.owl";
		
		String path2 = "file:///C:/Users/zouyuanrenren/Eclipseworkspace/Benchmarks/Test/";
		
//		String f = path2+"test.owl";

//		String f = path+"T_biological_process.owl";
//		String f = path+"T_cellular_component_xp_go.imports-local.owl";
//		String f = path+"T_go_xp_regulation.imports-local.owl";
//		String f = path + "T_cyc.owl";
//		String f = path+"T_FMA_Constitutional.owl";
		// problematic, containing unsuppoed class expressions
//		String f = path+"T_tambis-full.owl";
//		String f = path+"T_wine.owl";
		// problematic, containing unsuppoed class expressions
//		String f = path+"T_DLP.owl";
//		String f = path+"galen-ians-full-undoctored.owl";
//		String f = path+"fma_lite.owl";
		
//		String f = path+"EFO.owl";
//		String f = path+"OBI.owl";
// //		String f = path+"NCIt.owl";
//		String f = path+"ChEBI.owl";
		
//		String f = path+"SEMANTEC.owl";
//		String f = path+"vicodi_nodatatype.owl";
//		String f = path+"wine_nodatatype.owl";
//		String f = path+"http___sweet.jpl.nasa.gov_sweet_numerics.owl";
//		String f = path+"http___mged.sourceforge.net_ontologies_MGEDOntology.owl";
//		String f = path+"BoC_small.owl";				
//		String f = path+"BoC_middle.owl";
//		String f = path+"BoC_big.owl";
		
//		String f = path+"NPOntology01_vunknown.owl";
//		String f = path+"sopharm_v2.1.2.owl";
//		String f = path+"gaz_v$Revision-_1.512_$.obo";
//		String f = path3+"biomodels-21.owl";
//		String f = path4+"BioModelsT4_0.owl";
		
//		String f = path2+"FMA_2_0.owl";
//		String f = path2+"FMA-FULLR-DLR-TBOX.owl";
//		String f = path2+"FMA-FULLR.owl";
//		String f = path2+"FMA-DLR.owl";
//		String f = path2+"FMA-DLR-M1-meta.owl";
//		String f = path2+"fmaOwlDlComponent_2_0.owl";
//		String f = path2+"FMA_3.0_noMTC_100702.owl";
		
//		String f = path+"pizza.owl";
		
		// 33476
//		String f = path5+"not-galen.owl";
		// 499992
//		String f = path5+"full-galen.owl";
		// 205673
//		String f = path5+ "go.owl";
		// 307823
//		String f = path5+"nciOntology.owl";
		
		
//		String f = path6+"test.owl";
		
//		IRI physicalURI = IRI.create("file:"+args[1]);
		IRI physicalURI = IRI.create(f);
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		Timer time = new Timer("Load");
		time.start();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(physicalURI);
		System.out.println(1+" workers, "+ontology.getIndividualsInSignature().size()+" individuals, "+(ontology.getAxiomCount(AxiomType.CLASS_ASSERTION)+ontology.getAxiomCount(AxiomType.OBJECT_PROPERTY_ASSERTION))+" ABox axioms.");
		time.stop();
		System.out.println(time);
//		Reasoner reasoner = new Reasoner(1);
//		reasoner.load(ontology);
//		reasoner.classify();
//		reasoner.output();
		Timer timer = new Timer("REL 2 DL");
		timer.start();
		REXReasonerFactory factory = new REXReasonerFactory();
		REXReasoner reasoner = factory.createReasoner(ontology);
//		System.out.println(reasoner.isConsistent());
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		OWLClass thing = manager.getOWLDataFactory().getOWLThing();
		OWLClass nothing = manager.getOWLDataFactory().getOWLNothing();
		int num = 0;
		for(OWLClass cls:ontology.getClassesInSignature())
		{
			if(Thread.currentThread().isInterrupted())
				return;
			int x = 0;
//			if(cls.getIRI().getFragment().equals("Mushroom"))
			if(!cls.equals(thing)&&!cls.equals(nothing))
			{
				x+=reasoner.getSuperClasses(cls, false).getFlattened().size();
				x+=reasoner.getEquivalentClasses(cls).getSize();
//				System.out.println(reasoner.getEquivalentClasses(cls));

//				System.out.println(reasoner.getSuperClasses(cls,false).getFlattened());
//				System.out.println(cls.getIRI().getFragment()+" "+x);
			}
			num+=x;
		}
		timer.stop();
		System.out.print(num+" ");
		System.out.println(timer);


	}

}
