/**
 * Created by apatterson on 19/11/2015.
 */
package persistence;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import org.lightcouch.Response;


public class CouchDB implements DB {

    private CouchDbClient dbClient;

    public CouchDB(String db, String host, Integer port, String protocol){
        CouchDbProperties properties = new CouchDbProperties()
                .setDbName(db)
                .setCreateDbIfNotExist(true)
                .setProtocol(protocol)
                .setHost(host)
                .setPort(port);

        dbClient = new CouchDbClient(properties);
    }

    //takes in a JSON string and saves it to the DB. return true if success.
    public boolean save(Object o) {
        if (o instanceof String) {

            JsonParser parser = new JsonParser();

            JsonElement jsonElement = null;
            String jsonString = (String) o;

            try{
                while(!jsonString.startsWith("{")){
                    jsonString = jsonString.substring(1, jsonString.length()-2);
                }

                jsonElement = parser.parse(jsonString);
                dbClient.save(jsonElement.getAsJsonObject());
                return true;

            }catch(Exception e){
                e.printStackTrace();
                return false;
            }

        }

        return false;
    }
}