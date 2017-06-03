package kaizendata.tec.project.technical;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DababaseInteractions {

	private static DababaseInteractions instance;
	private static final String DATABASE_NAME = "kaizendata";
	private static final String SERVER = "localhost";
	private static final Integer PORT_NUMBER = 27017;
	private MongoDatabase mgDB;
	private MongoClient mgClient;

	private DababaseInteractions() {

		mgClient = new MongoClient(SERVER, PORT_NUMBER);
		mgDB = mgClient.getDatabase(DATABASE_NAME);
	}

	public static DababaseInteractions getInstance() {

		if (instance == null) {
			instance = new DababaseInteractions();

		}

		return instance;
	}

	public MongoDatabase getMgDB() {
		return mgDB;
	}

	public void setMgDB(MongoDatabase mgDB) {
		this.mgDB = mgDB;
	}

}
