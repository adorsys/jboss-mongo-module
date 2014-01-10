package de.adorsys.oss.mongo.jndi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.concurrent.Callable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * Provides {@link MongoClient} instances.
 * <p>
 * An ObjectFactory gets called for every lookup and usually a new object instance is created. However, we cache each {@link MongoClient}
 * instance keyed by its JNDI-name, so that subsequent lookups of the same JNDI-name return the same MongoClient instance. Usually you want
 * only one MongoClient per JVM. The cache works also only JVM-wide (via a static field).
 * 
 * @author Christoph Dietze
 * @author Marco Obermeyer
 */
public class MongoClientObjectFactory implements ObjectFactory {

	private static final Logger LOG = LoggerFactory.getLogger(MongoClientObjectFactory.class);
	private static final Cache<String, MongoClient> CACHE = CacheBuilder.newBuilder().build();

	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, final Hashtable<?, ?> environment) throws Exception {
		String jndiName = (String) obj;
		MongoClient mongoClient = CACHE.get(jndiName, new Callable<MongoClient>() {
			@Override
			public MongoClient call() throws Exception {
				return createMongoClient(environment);
			}
		});
		LOG.info("MongoClient JNDI lookup, obj: {}, env: {}, mongoClient: {}", obj, environment, mongoClient.hashCode());
		return mongoClient;
	}

	private MongoClient createMongoClient(Hashtable<?, ?> environment) throws UnknownHostException {
		String mongoUriString = (String) environment.get("mongoURI");
		checkNotNull(mongoUriString, "Environment parameter 'mongoURI' must be specified");
		MongoClientURI mongoURI = new MongoClientURI(mongoUriString);
		return new MongoClient(mongoURI);
	}
}
