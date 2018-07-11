package utility.core;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import utility.ConfigUtil;
import utility.OtherUtil;

public class DatabaseManager {
	
	private MongoDatabase db = ConfigUtil.getDatabase();
	private String collection;
	
	public DatabaseManager(String coll) {
		collection = coll;
	}
	
	public MongoCollection<Document> getDocument() {
		try {
			MongoCollection<Document> collection = db.getCollection(getCollection());
	        return collection;
		}catch (Exception ex) {
            OtherUtil.getWebhookError(ex, DatabaseManager.class.getName(), null);
			return null;
		}
	}
	
	
	private String getCollection() {
		return collection;
	}
	
	/**
	 * <i> Can be used to remove a certain collection <i>
	 * 
	 * @param field
	 * 		The key to find the collection
	 * @param id
	 * 		Parameter used to find the specified key
	 */
	public void removeCollection(String field, String id) {
		getDocument().findOneAndDelete(new Document(field, id));
	}
	
	
	/**
	 * <i> Can be used to search for a user or check on a object within the user's collection or check for a boolean <i>
	 * 
	 * @param field
	 * 		The key to find the collection
	 * @param id
	 * 		Parameter used to find the specified key
	 * @param var
	 * 		Parameter used to find a different object within the collection
	 * @return
	 * 		The object's variable
	 */
	public Object getField(String field, String id, Object var) {
		Document found = (Document) getDocument().find(new Document(field, id)).first();
    	if(found != null) {
    		return found.get(var);
    	}else{
    		return null;
    	}
    }
	
	/**
	 * <i> Can be used for setting any part of a user's collection <i>
	 * 
	 * @param field
	 * 		The key to find the collection
	 * @param id
	 * 		Parameter used to find the specified key
	 * @param var
	 * 		The object you wish to update
	 * @param obj
	 * 		The object's value
	 */
	public void setField(String field, String id, String var, Object obj) {
    	Bson updatedvalue = new Document(var, obj);
		getDocument().findOneAndUpdate(new Document(field, id), new Document("$set", updatedvalue));
    }
}
