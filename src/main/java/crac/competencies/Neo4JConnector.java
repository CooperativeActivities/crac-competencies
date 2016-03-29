package crac.competencies;

import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Neo4JConnector {

	private GraphDatabaseService  graphDb;
	
	
	
	@SuppressWarnings("deprecation")
	public void init(String path){
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(path);
		registerShutdownHook( graphDb );
	}
	
	
	private static void registerShutdownHook( final GraphDatabaseService graphDb ){
	    // Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running application).
	    Runtime.getRuntime().addShutdownHook( new Thread() {
	        @Override
	        public void run()
	        {
	            graphDb.shutdown();
	        }
	    } );
	}
	
	public void initializeTBox(OntModel model) {
		ExtendedIterator<OntClass> classes = model.listClasses(); 
		while(classes.hasNext()){
			OntClass cl = classes.next();
			createTNode(cl.getURI());
		}
		ExtendedIterator<ObjectProperty> rels = model.listObjectProperties();
		while(rels.hasNext()){
			ObjectProperty rel = rels.next();
			createTRel(rel);
		}
	}
	
	
	private void createTRel(ObjectProperty rel) {
		OntResource sourceClass = rel.getDomain();
		OntResource targetClass = rel.getRange();
		if(sourceClass != null && targetClass != null){
			try (Transaction tx = graphDb.beginTx()){
				String uri = rel.getURI().substring(rel.getURI().indexOf('#')+1);
				Result result = graphDb.execute("match (s {uri: '"+sourceClass.getURI()+"'})-[r:"+ uri +"]->(t {uri: '"+targetClass.getURI()+"'})return r");
				if(!result.hasNext()) {
					System.out.println("Adding Relationship to TBox");
					Label sourceLabel = DynamicLabel.label(sourceClass.getURI());
					Node sourceNode = graphDb.findNodes(sourceLabel).next();
					Label targetLabel = DynamicLabel.label(targetClass.getURI());
					Node targetNode = graphDb.findNodes(targetLabel).next();
	
					RelationshipType type = DynamicRelationshipType.withName(uri);			
					sourceNode.createRelationshipTo(targetNode, type );
				}
				tx.success();
			}
		}		
	}


	private void createTNode(String uri) {
		try (Transaction tx = graphDb.beginTx()){
			Result result = graphDb.execute("match (n {uri: '"+uri+"'}) return n");
			if(!result.hasNext()) {
				System.out.println("Adding TBox Element");
				Node elem = graphDb.createNode();
				Label label = DynamicLabel.label(uri);
				elem.addLabel(label);
				elem.setProperty("uri", uri);
			}
			tx.success();
		}
	}


	public void storePerson(String personId){
		Node person = graphDb.createNode();
	
		person.setProperty( "type", "Person" );
		person.setProperty( "message", "Hello, " );
	}
}
