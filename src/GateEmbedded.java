import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.SwingUtilities;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.ProcessingResource;
import gate.SimpleAnnotationSet;
import gate.creole.ANNIEConstants;
import gate.creole.SerialAnalyserController;
import gate.gui.MainFrame;
import gate.util.GateException;


public class GateEmbedded {

	/** The Corpus Pipeline application to contain ANNIE */
	  private SerialAnalyserController annieController;

	  /**
	   * Initialise the ANNIE system. This creates a "corpus pipeline"
	   * application that can be used to run sets of documents through
	   * the extraction system.
	   */
	  public void initAnnie() throws GateException {
	    System.out.println("Initialising ANNIE...");

	    // create a serial analyser controller to run ANNIE with
	    annieController =
	      (SerialAnalyserController) Factory.createResource(
	        "gate.creole.SerialAnalyserController", Factory.newFeatureMap(),
	        Factory.newFeatureMap(), "ANNIE_" + Gate.genSym()
	      );

	    // load each PR as defined in ANNIEConstants
	    for(int i = 0; i < ANNIEConstants.PR_NAMES.length; i++) {
	      FeatureMap params = Factory.newFeatureMap(); // use default parameters
	      ProcessingResource pr = (ProcessingResource)
	        Factory.createResource(ANNIEConstants.PR_NAMES[i], params);

	      // add the PR to the pipeline controller
	      annieController.add(pr);
	    } // for each ANNIE PR

	    System.out.println("...ANNIE loaded");
	  } // initAnnie()
	  
	  /** Tell ANNIE's controller about the corpus you want to run on */
	  public void setCorpus(Corpus corpus) {
	    annieController.setCorpus(corpus);
	  } 
	  
	  /** Run ANNIE */
	  public void execute() throws GateException {
	    System.out.println("Running ANNIE...");
	    annieController.execute();
	    System.out.println("...ANNIE complete");
	  } // execute()
	  

	  /**
	   * Run from the command-line, with a list of URLs as argument.
	   * <P><B>NOTE:</B><BR>
	   * This code will run with all the documents in memory - if you
	   * want to unload each from memory after use, add code to store
	   * the corpus in a DataStore.
	   */
	  public static void main(String args[]) throws GateException, IOException {
	    // initialise the GATE library
	    System.out.println("Initialising GATE...");
	    URL gHome = GateEmbedded.class.getClassLoader().getResource("GATE");
	    File gateHome;
		try {
			gateHome = new File(gHome.toURI());
			Gate.setGateHome( gateHome );
		    
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        Gate.init();
	    
	    System.out.println("...GATE initialised"); 

	    URL url = GateEmbedded.class.getClassLoader().getResource("GATE/plugins/Annie/plugins/ANNIE");
	    Gate.getCreoleRegister().registerDirectories(url);
	    GateEmbedded annie = new GateEmbedded();
	    annie.initAnnie();
	    Document myDoc = Factory.newDocument("My name is John.");
	    Corpus corpus = (Corpus) Factory.createResource("gate.corpora.CorpusImpl");
	    corpus.add(myDoc);
	    
	    
	    annie.setCorpus(corpus);
	    annie.execute();
	    
	    Iterator<Document> iter = corpus.iterator();

	    while(iter.hasNext()) {
	        Document doc = (Document) iter.next();
	        AnnotationSet defaultAnnotSet = doc.getAnnotations();
	        Set<String> annotTypesRequired = new HashSet<String>();
	        annotTypesRequired.add("Token");
	        Set<Annotation> tokensSet = new HashSet<Annotation>(defaultAnnotSet.get(annotTypesRequired));
	        Iterator<Annotation> it = tokensSet.iterator();
	        
	        while(it.hasNext()) {
	        	Annotation currAnnot = (Annotation) it.next();
	            System.out.println(currAnnot.getFeatures().get("string").toString());
	        }
	    }
	    
	  }
}
