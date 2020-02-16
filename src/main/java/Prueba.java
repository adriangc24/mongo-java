import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.LogManager;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import java.util.logging.Logger;
import java.util.logging.Level;

public class Prueba {

	static MongoClient mongo = new MongoClient();
	static MongoDatabase database;
	static MongoCollection<Document> consolas;
	static MongoCollection<Document> juegos;
	static Scanner sc = new Scanner(System.in);
	static int opcion = 0;
	static String opcion2 = null;
	static String aux, aux3;
	static Float aux2;
	static String nombre, plataforma;
	static Float precio;

	public static void main(String[] args) {

		// Deshabilitaremos los logs para mejor experiencia interactiva con la shell
		disableLogs();
		crearConexionAtlas();

		// Descomentar para cargar datos de ejemplo

		/*
		 * createDocumentJuegos("sonic", 34f, "switch"); createDocumetConsolas("switch",
		 * 350f); createDocumentJuegos("resident evil 3", 50f, "ps2");
		 * createDocumetConsolas("ps2", 100f); createDocumentJuegos("call of duty mw3",
		 * 100f, "ps3"); createDocumetConsolas("ps3", 500f);
		 */

		menu();
		mongo.close();

		/**
		 * Conexion local
		 * 
		 * System.out.println("Prueba conexión MongoDB"); MongoClient mongo =
		 * crearConexion();
		 * 
		 * if (mongo != null) { System.out.println("Lista de bases de datos: ");
		 * printDatabases(mongo); // Accessing the database database =
		 * mongo.getDatabase("tienda");
		 * 
		 * createCollection("consolas"); createCollection("juegos");
		 * createDocumentsEjemplo(); menu();
		 * 
		 * } else { System.out.println("Error: Conexión no establecida"); }
		 **/

	}

	private static void disableLogs() {
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE);
		mongoLogger.getLogger("org.mongodb.driver.connection").setLevel(Level.OFF);
		mongoLogger.getLogger("org.mongodb.driver.management").setLevel(Level.OFF);
		mongoLogger.getLogger("org.mongodb.driver.cluster").setLevel(Level.OFF);
		mongoLogger.getLogger("org.mongodb.driver.protocol.insert").setLevel(Level.OFF);
		mongoLogger.getLogger("org.mongodb.driver.protocol.query").setLevel(Level.OFF);
		mongoLogger.getLogger("org.mongodb.driver.protocol.update").setLevel(Level.OFF);
	}

	public static void createDocumetConsolas(String nombre, Float precio) {
		try {

			System.out.println("Coleccion selecionada correctamente");
			Document document = new Document("nombre", nombre).append("precio", precio);
			consolas.insertOne(document);
			System.out.println("Documento insertado correctamente");
		} catch (Exception e) {
			System.out.println("Error al insertar el document");
		}
	}

	public static void crearConexionAtlas() {
		MongoClientURI uri = new MongoClientURI(
				"mongodb+srv://admin:Passw0rd@cluster0-otfa2.mongodb.net/test?retryWrites=true&w=majority");
		try {
			mongo = new MongoClient(uri);
			MongoDatabase database = mongo.getDatabase("tienda");
			consolas = database.getCollection("consolas");
			juegos = database.getCollection("juegos");
			database.getCollection("prueba");

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public static void menu() {
		database = mongo.getDatabase("tienda");

		while (true) {
			System.out.println(
					"Que quieres hacer?\n1- Insertar datos\n2- Modificar datos\n3- Elminar datos\n4- Busqueda sencilla\n5- Busqueda compleja\n6- Salir");
			if (sc.hasNextInt()) {
				opcion = sc.nextInt();
				switch (opcion) {
				case 1:
					System.out.println("Sobre que coleccion desea insertar datos?");
					printCollections();

					opcion2 = sc.next();
					if (opcion2.toLowerCase().equals("consolas")) {
						sc = new Scanner(System.in);
						System.out.println("Introduce el nombre de la consola");
						aux = sc.nextLine();
						System.out.println("Introduce el precio de la consola ej: 99,58");
						if (sc.hasNextFloat()) {
							aux2 = sc.nextFloat();
							createDocumetConsolas(aux, aux2);
						} else {
							System.out.println("Precio incorrecto");
						}

					} else if (opcion2.toLowerCase().equals("juegos")) {
						sc = new Scanner(System.in);
						System.out.println("Introduce el nombre del juego");
						aux = sc.nextLine();
						System.out.println("Introduce el precio del juego ej: 99,58");
						if (sc.hasNextFloat()) {
							aux2 = sc.nextFloat();
						} else {
							System.out.println("Precio incorrecto");
						}
						sc = new Scanner(System.in);
						System.out.println("Introduce la plataforma del juego");
						aux3 = sc.nextLine();
						createDocumentJuegos(aux, aux2, aux3);

					} else {
						System.out.println("Opcion incorrecta\n");
					}
				case 2:
					System.out.println("Sobre que coleccion desea modificar datos?");
					printCollections();

					opcion2 = sc.next();
					if (opcion2.toLowerCase().equals("consolas")) {
						modifyDocumentConsolas();
						break;
					} else if (opcion2.toLowerCase().equals("juegos")) {
						modifyDocumentJuegos();
						break;
					} else {
						System.out.println("Opcion incorrecta\n");
					}
				case 3:
					System.out.println("Sobre que coleccion desea eliminar datos?");
					printCollections();

					opcion2 = sc.next();
					if (opcion2.toLowerCase().equals("consolas")) {
						deleteDocumentConsolas();
						break;
					} else if (opcion2.toLowerCase().equals("juegos")) {
						deleteDocumentJuegos();
						break;
					} else {
						System.out.println("Opcion incorrecta\n");
					}
				case 4:
					sc = new Scanner(System.in);
					System.out.println("Sobre que coleccion desea buscar datos?");
					printCollections();

					opcion2 = sc.next();
					if (opcion2.toLowerCase().equals("consolas")) {
						printCollection(consolas);
						sc = new Scanner(System.in);
						System.out.println("Nombre a buscar");
						nombre = sc.nextLine();
						simpleSearch(consolas,nombre);
						break;

					} else if (opcion2.toLowerCase().equals("juegos")) {
						printCollection(juegos);
						sc = new Scanner(System.in);
						System.out.println("Nombre a buscar");
						nombre = sc.nextLine();
						simpleSearch(juegos,nombre);
						break;
					} else {
						System.out.println("Opcion incorrecta\n");
					}
				case 5:
					sc = new Scanner(System.in);
					System.out.println("Sobre que coleccion desea buscar datos?");
					printCollections();

					opcion2 = sc.next();
					if (opcion2.toLowerCase().equals("consolas")) {
						printCollection(consolas);
						sc = new Scanner(System.in);
						System.out.println("Nombre a buscar");
						nombre = sc.nextLine();
						System.out.println("Precio a buscar");
						precio = sc.nextFloat();
						complexSearch(consolas,nombre,precio);
						break;

					} else if (opcion2.toLowerCase().equals("juegos")) {
						printCollection(juegos);
						sc = new Scanner(System.in);
						System.out.println("Nombre a buscar");
						nombre = sc.nextLine();
						System.out.println("Precio a buscar");
						precio = sc.nextFloat();
						complexSearch(juegos,nombre,precio);
						break;
					} else {
						System.out.println("Opcion incorrecta\n");
					}
				case 6:
					System.out.println("Hasta luego !\n");
					break;
				}
				if (opcion == 6) {
					break;
				}
			} else {
				sc = new Scanner(System.in);
			}
		}
	}

	private static void complexSearch(MongoCollection<Document> collection, String nombre, Float precio) {
		Document findDocument = new Document("nombre", nombre).append("precio", precio);
		MongoCursor<Document> resultDocument = collection.find(findDocument).iterator();
		Document result;

		while (resultDocument.hasNext()) {
			result = resultDocument.next();
			if(collection.equals(juegos)) {
				System.out.println("Nombre: " + result.getString("nombre") + " Precio:  " + result.getDouble("precio")+ " Plataforma:  " + result.getString("plataforma"));
			}
			else {
				System.out.println("Nombre: " + result.getString("nombre") + " Precio:  " + result.getDouble("precio"));

			}		}
	}

	private static void simpleSearch(MongoCollection<Document> collection, String nombre) {
		Document findDocument = new Document("nombre", nombre);
		MongoCursor<Document> resultDocument = collection.find(findDocument).iterator();
		Document result;

		while (resultDocument.hasNext()) {
			result = resultDocument.next();
			if(collection.equals(juegos)) {
				System.out.println("Nombre: " + result.getString("nombre") + " Precio:  " + result.getDouble("precio")+ " Plataforma:  " + result.getString("plataforma"));
			}
			else {
				System.out.println("Nombre: " + result.getString("nombre") + " Precio:  " + result.getDouble("precio"));

			}
		}
	}

	private static void printCollections() {
		MongoIterable<String> colls = database.listCollectionNames();
		for (String col : colls) {
			System.out.println(col);
		}
	}

	public static void createDocumentJuegos(String nombre, Float precio, String plataforma) {
		try {
			System.out.println("Coleccion selecionada correctamente");
			Document document = new Document("nombre", nombre).append("precio", precio).append("plataforma",
					plataforma);
			juegos.insertOne(document);
			System.out.println("Documento insertado correctamente");

		} catch (Exception e) {
			System.out.println("Error al insertar el document");
		}
	}

	public static void modifyDocumentJuegos() {

		try {
			sc = new Scanner(System.in);
			printCollection(juegos);
			System.out.print("Nombre del juego a actualizar: ");
			nombre = sc.nextLine();
			System.out.print("Precio: ");
			precio = sc.nextFloat();
			Document documentActualizado = new Document("$set", new Document("precio", precio));
			juegos.findOneAndUpdate(new Document("nombre", nombre), documentActualizado);

		} catch (Exception e) {
			System.out.println("Error al modificar el document");
		}
	}

	public static void deleteDocumentJuegos() {

		try {
			sc = new Scanner(System.in);
			printCollection(juegos);
			System.out.print("Nombre del juego a borrar: ");
			nombre = sc.nextLine();
			juegos.deleteOne(Filters.eq("nombre", nombre));

		} catch (Exception e) {
			System.out.println("Error al borrar el document");
		}
	}

	public static void deleteDocumentConsolas() {

		try {
			sc = new Scanner(System.in);
			printCollection(consolas);
			System.out.print("Nombre de la consola a borrar: ");
			nombre = sc.nextLine();
			juegos.deleteOne(Filters.eq("nombre", nombre));

		} catch (Exception e) {
			System.out.println("Error al borrar el document");
		}
	}

	public static void modifyDocumentConsolas() {

		try {
			sc = new Scanner(System.in);
			printCollection(consolas);
			System.out.print("Nombre de consola a actualizar: ");
			nombre = sc.nextLine();
			System.out.print("Precio: ");
			precio = sc.nextFloat();
			Document documentActualizado = new Document("$set", new Document("precio", precio));
			consolas.findOneAndUpdate(new Document("nombre", nombre), documentActualizado);

		} catch (Exception e) {
			System.out.println("Error al modificar el document");
		}
	}

	public static void createCollection(String string) {
		try {
			MongoDatabase database = mongo.getDatabase("tienda");
			database.createCollection(string.toLowerCase());
			System.out.println("Coleccion creada");
		} catch (Exception e) {
			System.out.println("Coleccion ya creada");
		}

	}

	/**
	 * Clase para crear una conexión a MongoDB.
	 * 
	 * @return MongoClient conexión
	 */
	private static MongoClient crearConexion() {
		MongoClient mongo = null;
		mongo = new MongoClient("localhost", 27017);

		return mongo;
	}

	/**
	 * Clase que imprime por pantalla todas las bases de datos MongoDB.
	 * 
	 * @param mongo conexión a MongoDB
	 */
	private static void printDatabases(MongoClient mongo) {

		List<String> dbs = mongo.getDatabaseNames();
		for (String db : dbs) {
			System.out.println(" - " + db);
		}

	}

	public static void printCollection(MongoCollection<Document> collection) {
		int i = 1;
		FindIterable<Document> iterDoc = collection.find();

		Iterator it = iterDoc.iterator();

		while (it.hasNext()) {
			System.out.println(it.next());
			i++;
		}
	}
}
