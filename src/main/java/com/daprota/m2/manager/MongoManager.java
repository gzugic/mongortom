/*
 *
 * Copyright (C) 2013 Goran Zugic
 *
 * Goran Zugic licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.daprota.m2.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.daprota.m2.entity.User;
import com.daprota.m2.entity.Role;
import com.daprota.m2.util.db.DbConnection;
import com.daprota.m2.M2Exception;

public class MongoManager
{
    final static Logger logger = LoggerFactory.getLogger(MongoManager.class);
	
    private DB _db = null;

	public MongoManager(DbConnection dbConn)
	    throws M2Exception
    {
	    if (dbConn != null) 
		{
		    try {
			    MongoClient mongoClient = dbConn.getMongoClient();
				if (mongoClient != null)
				{
				    String dbName = dbConn.getDbName();
			        if (dbName != null)
				    {
		                _db = mongoClient.getDB(dbName);
				    }
				    else
				    {
				        logger.error("MongoManager constructor failed. Database name is not provided.");
		                throw new M2Exception("MongoManager constructor failed. Database name is not provided.");
				    }
				}
				else 
				{
				    logger.error("MongoManager constructor failed. MongoClient is not provided.");
		            throw new M2Exception("MongoManager constructor failed. MongoClient is not provided.");				
				}
			}
			catch (MongoException ex) 
			{
			    logger.error("MongoManager constructor failed. Cannot connect to database.\n" + ex.toString());
		        throw new M2Exception("MongoManager constructor failed. Cannot connect to database.");
			}
		}
		else 
		{
		    logger.error("MongoManager constructor failed. Database connection object is null.");
		    throw new M2Exception("MongoManager constructor failed. Database connection object is null.");
		}
    }


    /**
     * Retrieves user via spceified userName.
     *
     * @param userName A user's userName.
	 * @return user if exists. Otherwise returns null.
     */
    public User findUserByUserName(String userName)
	    throws MongoException
    {
	    DBCursor dbCur = null;
		
	    try {
            // get a user collection
            DBCollection coll = _db.getCollection("user");
		    DBObject query = new BasicDBObject("userName", userName);
		    dbCur = coll.find(query);
		    boolean hasNext;		
		    try {
		        logger.debug("Call dbCur.hasNext().");
		        hasNext = dbCur.hasNext();		
		    }
		    catch (MongoException ex) {
		        logger.error("Search for user {} failed.\n" + ex.toString(), userName);
		        throw new MongoException("Search for user " + userName + " failed.");
		    }		
		    if (hasNext) 
		    {
                DBObject user = dbCur.next();
			    ArrayList<BasicDBObject> roles = (ArrayList<BasicDBObject>)user.get("roles");
			    ArrayList<Role> roles2 = new ArrayList<Role>();
                if (roles != null) 
				{				
                    for (BasicDBObject role : roles) 
                    {
			            Role role3 = new Role(role.get("_id").toString(),(String)role.get("name"),(String)role.get("description"));
				        roles2.add(role3);       
                    } 
				    logger.debug("User {} roles added.",userName);
				}			    			
			    return (new User(user.get("_id").toString(),(String)user.get("userName"),(String)user.get("password"),roles2));
		    }	
		    else
		    {
		        logger.warn("User {} could not be found", userName);
		        return null;
		    }
		}
		catch (MongoException ex) 
	    {	    
            logger.error("Search for user {} failed.\n" + ex.toString(),userName);			
	        throw new MongoException("Search for user " + userName + " failed.");
	    }
		finally {
            dbCur.close();
        }	
    }
	
}