jboss-mongo-module
==================

A jboss module to configure mongo-databases per jndi


```xml
<subsystem xmlns="urn:jboss:domain:naming:1.3">

    <bindings>
    
        <object-factory name="java:global/mongoClient" 
                        module="de.adorsys.oss.mongo-jndi" 
                        class="de.adorsys.oss.mongo.jndi.MongoClientObjectFactory">
            <environment>
                <property name="mongoURI" value="${mongo.uri:mongodb://localhost:27017}"/>
            </environment>
        </object-factory>
    
        <object-factory name="java:global/mongoDB/myDB" 
                        module="de.adorsys.oss.mongo-jndi" 
                        class="de.adorsys.oss.mongo.jndi.MongoDBObjectFactory">
            <environment>
                 <property name="mongoClientRef" value="java:global/mongoClient"/>
                 <property name="dbName" value="myDB"/>
            </environment>
        </object-factory>
    
    </bindings>

</subsystem>
```
