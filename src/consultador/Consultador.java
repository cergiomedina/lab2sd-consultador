
package consultador;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Consultador {

    static String doc_para_indexar;
    static String BaseDeDatos;
    static String ColeccionDocumentos;
    static String ColeccionIndice;
    
    public static void main(String[] args) throws IOException {
        DB db = null;
        try{   
         cargarParametros();
         MongoClient mongoClient = new MongoClient();
         db = mongoClient.getDB(BaseDeDatos);
         //System.out.println("Connect to database successfully");
         
       }catch(Exception e){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
       }
        DBCollection documentos = db.getCollection(ColeccionIndice);
        while(true){
            BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
            String consulta = lector.readLine().toLowerCase();
            BasicDBObject palabra = new BasicDBObject("palabra",consulta);
            DBCursor cursor;
            cursor = documentos.find(palabra);
            if(cursor.count()==0){
                System.out.println("No se encuentra resultado");
            }else{
                int cantidad_respuestas = cursor.count();
                BasicDBObject respuesta = (BasicDBObject) cursor.one().get("documentos");
                for (int i = 0; i < respuesta.size(); i++) {
                    System.out.println("http://es.wikipedia.org/wiki/"+respuesta.get("Documento-"+Integer.toString(i)));
                }
                
            }
        }
    }
    
    private static void cargarParametros() throws FileNotFoundException, IOException {
        // cargo archivo para indexar
        
        BufferedReader entrada = new BufferedReader(new FileReader("parametros.ini"));
        String linea;
        try {
            doc_para_indexar = entrada.readLine();  
            BaseDeDatos = entrada.readLine();
            ColeccionDocumentos = entrada.readLine();
            ColeccionIndice = entrada.readLine();
            entrada.close();
        } catch (IOException ex) {
            System.out.println("No se ha podido cargar los datos de parametros.ini");
        }
        
    }
    
}
