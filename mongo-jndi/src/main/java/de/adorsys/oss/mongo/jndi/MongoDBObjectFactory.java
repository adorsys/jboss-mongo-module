package de.adorsys.oss.mongo.jndi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 * Provides {@link DB} instances.
 * 
 * @author Christoph Dietze
 * @author Marco Obermeyer
 */
public class MongoDBObjectFactory implements ObjectFactory {

	private static final Logger LOG = LoggerFactory.getLogger(MongoDBObjectFactory.class);

	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
		LOG.info("MongoDB JNDI lookup, obj: {}, env: {}", obj, environment);

		String mongoClientRef = (String) environment.get("mongoClientRef");
		checkNotNull(mongoClientRef, "Environment parameter 'mongoClientRef' must be specified");

		String dbName = (String) environment.get("dbName");
		checkNotNull(dbName, "Environment parameter 'dbName' must be specified");

		MongoClient mongoClient = (MongoClient) InitialContext.doLookup(mongoClientRef);
		checkNotNull(mongoClient, "No MongoClient instance found at JNDI ref '" + mongoClientRef + "'");

		DB db = mongoClient.getDB(dbName);
		return db;
	}
}
