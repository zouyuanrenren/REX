package eu.trowl.owl.rex.owlapi.tests;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TBoxScript {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		ExecutorService executor = Executors.newSingleThreadExecutor();
//		String path = "/home/ktgroup/workspace/Benchmark/ORE2013/ore2013-ontologies-offline/functional/dl";
//		String path = "C:/Users/zouyuanrenren/Eclipseworkspace/Benchmarks/AIJEvaluation/";
		String path = "C:/Users/zouyuanrenren/Dropbox/KTShare/Ontologies/test-runner/ontologies/rl/";
		
		File directory = new File(path);
		int i = 0;
		
		for(File file:directory.listFiles())
		{
			i++;
            System.out.print(i+" "+file.getName()+" ");
//			if(i>14 && file.getName().toLowerCase().endsWith("owl"))
//			if(file.length() > 100000000 && file.length() <200000000)
            if(i>2)
			{
		        Future<String> future = executor.submit(new TBoxClassification(file));

	        try {
//	            System.out.println(future.get(600, TimeUnit.SECONDS));
	            future.get(300,TimeUnit.SECONDS);
//	            System.out.println("Finished!");
	        } catch (TimeoutException e) {
	            System.out.println("TimeOut ");
	            future.cancel(true);
	        }
			}
			else
				System.out.println();

		}


        executor.shutdownNow();
	}

}
