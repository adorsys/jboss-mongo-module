package de.adorsys.oss.mongo.jndi.example;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.mongodb.DB;

/**
 * @author Christoph Dietze
 * @author Marco Obermeyer
 */

public class MongoDBJndiProvider {
	public static DB getDB(String logicalDbName) {
		try {
			String jndiName = "java:global/mongoDB/" + logicalDbName;
			DB db = (DB) InitialContext.doLookup(jndiName);
			checkNotNull(db, "No Mongo DB found in JNDI at " + jndiName);
			return db;
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

}
