package commands.family;

import java.util.ArrayList;

import org.bson.Document;

import utility.core.DatabaseManager;

public class Children {
	
	private static final DatabaseManager db = new DatabaseManager("beta_child");
	
	public void addChild(String userid, String parentA, String parentB, boolean st) {
		Document doc = new Document("id", userid)
				.append("isAdopted", st)
				.append("parentA", parentA)
				.append("parentB", parentB);
		db.getDocument().insertOne(doc);
	}
	
	public ArrayList<Document> getChildren() { 
		return db.getDocument().find().into(new ArrayList<>());
	}
	
	public void removeChild(String id) {
		db.removeCollection("id", id);
	}
	
	public String getParentA(String id) {
		try {
			return db.getField("id", id, "parentA").toString();
		}catch (NullPointerException ex) {
			return null;
		}
	}
	
	public String getParentB(String id) {
		try {
			return db.getField("id", id, "parentB").toString();
		}catch (NullPointerException ex) {
			return null;
		}
	}
	
	public String getChild(String id) {
		try {
			return db.getField("id", id, "id").toString();
		}catch (NullPointerException ex) {
			return null;
		}
	}
	
	public boolean isAdopted(String id) {
		try {
			return Boolean.valueOf(db.getField("id", id, "isAdopted").toString());
		}catch (NullPointerException ex) {
			return false;
		}
	}
	
	public void setAdoption(String id, boolean st) {
		db.setField("id", id, "isAdopted", st);
	}
	
	public boolean isParent(String id, String parent) {
		if(parent.equals(getParentA(id))) {
			return true;
		}else return parent.equals(getParentB(id));
	}
}
