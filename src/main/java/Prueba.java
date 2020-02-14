import java.net.UnknownHostException;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

public class Prueba {
    /**
     * Main del proyecto.
     * @param args
     * 
     */
	static MongoDatabase database;
    public static void main(String[] args) {
        System.out.println("Prueba conexión MongoDB");
        MongoClient mongo = crearConexion();
 
        if (mongo != null) {
            System.out.println("Lista de bases de datos: ");
            printDatabases(mongo);
          //Accessing the database 
            database = mongo.getDatabase("tienda");  
            
            createCollection("Consolas");
            createCollection("Juegos");
            createDocument();
        } else {
            System.out.println("Error: Conexión no establecida");
        }
        
    }
 
    public static void createDocument() {
    	 MongoCollection<Document> collection = database.getCollection("Consolas"); 
         System.out.println("Coleccion selecionada correctamente");

         Document document = new Document("title", "MongoDB") 
         .append("id", 1)
         .append("description", "database") 
         .append("likes", 100) 
         .append("url", "http://www.tutorialspoint.com/mongodb/") 
         .append("by", "tutorials point");  
         collection.insertOne(document); 
         System.out.println("Documento insertado correctamente");
	}

	public static void createCollection(String string) {
    	 try { 
    		 database.createCollection(string);
             System.out.println("Coleccion creada"); 
    	 } catch(Exception e) {
    		 System.out.println("Coleccion ya creada");
    	 }

	}

	/**
     * Clase para crear una conexión a MongoDB.
     * @return MongoClient conexión
     */
    private static MongoClient crearConexion() {
        MongoClient mongo = null;
        
      //  mongo = new MongoClient("localhost", 27017);
    	MongoClientURI uri = new MongoClientURI("mongodb+srv://admin:Passw0rd@cluster0-otfa2.mongodb.net/test?retryWrites=true&w=majority");

        mongo = new MongoClient(uri);

        return mongo;
    }
 
    /**
     * Clase que imprime por pantalla todas las bases de datos MongoDB.
     * @param mongo conexión a MongoDB
     */
    private static void printDatabases(MongoClient mongo) {
    	MongoClientURI uri = new MongoClientURI("mongodb+srv://admin:Passw0rd@cluster0-otfa2.mongodb.net/test?retryWrites=true&w=majority");

    	try(MongoClient mongoClient = new MongoClient(uri))
    	{
    		MongoDatabase database = mongoClient.getDatabase("prueba");
    		MongoCollection<Document> collection = database.getCollection("prueba");
    		
    		FindIterable<Document> docs = collection.find();
    		
    		for (Document doc: docs) {
    			System.out.println(doc);
    		}
    		
    	}
    	
        List<String> dbs = mongo.getDatabaseNames();
        for (String db : dbs) {
            System.out.println(" - " + db);
        }
    }
}
