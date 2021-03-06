package commands.family;

import org.bson.Document;

import utility.core.DatabaseManager;

import java.util.ArrayList;

public class Marriage {
	
	private static final DatabaseManager db = new DatabaseManager("beta_marriage");
	
	public enum STATUS {
		PROPOSER,
		PROPOSEDTO,
	}
	
	public void addUser(String userid, String partnername, boolean married, String date, STATUS marriagestatus, boolean forced) {
    	Document doc = new Document("id", userid)
                .append("partner", partnername)
                .append("married", married)
                .append("status", marriagestatus.toString())
                .append("forced", forced)
                .append("date", date);
		db.getDocument().insertOne(doc);
    }

	public ArrayList<Document> getMarriages() {
		return db.getDocument().find().into(new ArrayList<>());
	}

	public void removeUser(String id) {
		db.removeCollection("id", id);
	}
	
	public String getUser(String id) {
		try {
			return db.getField("id", id, "id").toString();
		}catch (NullPointerException ex) {
			return null;
		}
	}
	
	public String getPartner(String id) {
		try {
			return db.getField("id", id, "partner").toString();
		}catch (NullPointerException ex) {
			return null;
		}
	}
	
	public void setMarried(String id, boolean married) {
		db.setField("id", id, "married", married);
	}
	
	public boolean isMarried(String id) {
		try {
			return Boolean.valueOf(db.getField("id", id, "married").toString());
		}catch (NullPointerException ex) {
			return false;
		}
	}
	
	public boolean isForced(String id) {
		try {
			return Boolean.valueOf(db.getField("id", id, "forced").toString());
		}catch (NullPointerException ex) {
			return false;
		}
	}
	
	public boolean isProposing(String id) {
		try {
			String value = db.getField("id", id, "status").toString();
            return value.equals(STATUS.PROPOSER.toString());
		}catch (NullPointerException ex) {
			return false;
		}
	}
	
	public boolean isProposedTo(String id) {
		try {
			String value = db.getField("id", id, "status").toString();
            return value.equals(STATUS.PROPOSEDTO.toString());
		}catch (NullPointerException ex) {
			return false;
		}
	}
	
	public String getMarriageDate(String id) {
		return db.getField("id", id, "date").toString();
	}
	
	public void setMarriageDate(String id, String date) {
		db.setField("id", id, "date", date);
	}
	
// --Commented out by Inspection START (8/6/2018 4:03 PM):
// --Commented out by Inspection START (8/6/2018 4:03 PM):
////	public String getMarriageStatus(String id) {
////		return db.getField("id", id, "status").toString();
////	}
//// --Commented out by Inspection STOP (8/6/2018 4:03 PM)
// --Commented out by Inspection STOP (8/6/2018 4:03 PM)

	public void setMarriageStatus(String id, STATUS status) {
		db.setField("id", id, "status", status);
	}
}
