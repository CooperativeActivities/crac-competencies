package crac.competencies;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Neo4JConnector {

	private GraphDatabaseService  graphDb;
	
	private static enum RelTypes implements RelationshipType
	{
	    KNOWS
	}
	
	@SuppressWarnings("deprecation")
	public void init(String path){
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(path);
		registerShutdownHook( graphDb );
	}
	
	
	private static void registerShutdownHook( final GraphDatabaseService graphDb )
	{
	    // Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running application).
	    Runtime.getRuntime().addShutdownHook( new Thread()
	    {
	        @Override
	        public void run()
	        {
	            graphDb.shutdown();
	        }
	    } );
	}
	
	public void helloWorldExample(){
		try ( Transaction tx = graphDb.beginTx() )
		{
			Node firstNode = graphDb.createNode();
			firstNode.setProperty( "message", "Hello, " );
			Node secondNode = graphDb.createNode();
			secondNode.setProperty( "message", "World!" );

			Relationship relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS );
			relationship.setProperty( "message", "brave Neo4j " );
		    tx.success();
		}
	}
}
