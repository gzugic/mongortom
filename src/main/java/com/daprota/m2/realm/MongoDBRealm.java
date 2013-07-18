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
 
 
package com.daprota.m2.realm;

import java.security.Principal;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import org.apache.catalina.realm.GenericPrincipal;
import org.apache.catalina.realm.RealmBase;
import org.apache.catalina.LifecycleException;
import com.daprota.m2.entity.User;
import com.daprota.m2.entity.Role;
import com.daprota.m2.manager.MongoManager;
import com.daprota.m2.M2Exception;
import com.daprota.m2.util.db.DbConnection;
 
public class MongoDBRealm extends RealmBase {

    /**
     * The connection username to use when trying to connect to the database.
     */
    protected String connectionName = null;


    /**
     * The connection URL to use when trying to connect to the database.
     */
    protected String connectionPassword = null;


      /**
     * The connection URL to use when trying to connect to the database.
     */
    protected String connectionURL = null;	
	
	
    /**
     * Descriptive information about this Realm implementation.
     */
    protected static final String info =
        "com.daprota.m2.realm.MongoDBRealm/1.0";


    /**
     * Descriptive information about this Realm implementation.
     */
    protected static final String name = "MongoDBRealm";	

	
    /**
     * Return the username to use to connect to the database.
     *
     */
    public String getConnectionName() {
        return connectionName;
    }

    /**
     * Set the username to use to connect to the database.
     *
     * @param connectionName Username
     */
    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    /**
     * Return the password to use to connect to the database.
     *
     */
    public String getConnectionPassword() {
        return connectionPassword;
    }

    /**
     * Set the password to use to connect to the database.
     *
     * @param connectionPassword User password
     */
    public void setConnectionPassword(String connectionPassword) {
        this.connectionPassword = connectionPassword;
    }	
	
	
    /**
     * Return the URL to use to connect to the database.
     *
     */
    public String getConnectionURL() {
        return connectionURL;
    }

    /**
     * Set the URL to use to connect to the database.
     *
     * @param connectionURL The new connection URL
     */
    public void setConnectionURL( String connectionURL ) {
      this.connectionURL = connectionURL;
    }
	
    /**
     * Return descriptive information about this Realm implementation and
     * the corresponding version number, in the format
     * <code>&lt;description&gt;/&lt;version&gt;</code>.
     */
    @Override
    public String getInfo() {
        return info;
    }	



    /**
     * Return the Principal associated with the specified username and
     * credentials, if there is one; otherwise return <code>null</code>.
     *
     * If there are any errors with the JDBC connection, executing
     * the query or anything we return null (don't authenticate). This
     * event is also logged, and the connection will be closed so that
     * a subsequent request will automatically re-open it.
     *
     *
     * @param username Username of the Principal to look up
     * @param credentials Password or other credentials to use in
     *  authenticating this username
     */
    @Override
    public synchronized Principal authenticate(String username, String credentials) {

	    // No user or no credentials
        // Can't possibly authenticate, don't bother the database then
        if (username == null || credentials == null) {
            return null;
        }
        
        // Open database connection
        DbConnection dbConn = open();
			
        if (dbConn == null) {
            // If the db connection open fails, return "not authenticated"
            return null;
        }

        // Acquire a Principal object for this user
        Principal principal = authenticate(dbConn, username, credentials);

        close(dbConn);
			
        // Return the Principal (if any)
        return (principal);
        
    }    

	
    /**
     * Return the Principal associated with the specified username and
     * credentials, if there is one; otherwise return <code>null</code>.
     *
     * @param dbConn MongoDB database connection	 
     * @param username Username of the Principal to look up
     * @param credentials Password or other credentials to use in
     *  authenticating this username
     */
    public synchronized Principal authenticate(DbConnection dbConn,
                                               String username,
                                               String credentials) {
											   
        // Look up the user's credentials
        String dbCredentials = getPassword(dbConn,username);		

        // Validate the user's credentials
        boolean validated = false;
        if (hasMessageDigest()) {	
            // Hex hashes should be compared case-insensitive
            validated = (digest(credentials).equalsIgnoreCase(dbCredentials));			
        } else {			
            validated = (digest(credentials).equals(dbCredentials));		
        }

        if (validated) {	
            if (containerLog.isTraceEnabled())
                containerLog.trace(sm.getString("MongoDBRealm.authenticateSuccess",
                                                username));
        } else {	
            if (containerLog.isTraceEnabled())
                containerLog.trace(sm.getString("MongoDBRealm.authenticateFailure",
                                                username));
            return (null);
        }

        ArrayList<String> roles = getRoles(dbConn,username);
        
        // Create and return a suitable Principal for this user
        return (new GenericPrincipal(username, credentials, roles));

    }	



    /**
     * Open (if necessary) and return a database connection for use by
     * this Realm.
     *
     * @exception M2Exception if a database error occurs
     */
    protected DbConnection open() {
        if (!((connectionURL.substring(0,7)).equals("mongodb")))
        {
            containerLog.error(sm.getString("MongoDBRealm.exception: connectionURL not properly set up"));
        }
 		
		String dbName = null;
		
        String connectionURL2 = connectionURL.substring(10);	
		if (connectionURL2.indexOf(',') == -1)
		{
		    if (connectionURL2.indexOf("/") == -1) 
			{
			    containerLog.error("M2 ConnectionManager cannot be initialized. Database name is not provide.");			    
			}
			else {		
		        String host = connectionURL2.substring(0,connectionURL2.indexOf(":"));			
			    String port = connectionURL2.substring(connectionURL2.indexOf(":")+1,connectionURL2.indexOf("/"));						
                dbName = connectionURL2.substring(connectionURL2.indexOf("/")+1);	
			
  		        try {	
                    MongoClient mongoClient = new MongoClient(host,Integer.parseInt(port)); 			            	
                    return (new DbConnection(mongoClient,dbName));							
		        }
	            catch (UnknownHostException e) {
		            containerLog.error(sm.getString("MongoDBRealm.exception"),e);  	
                }
            }				
		}
		else 
		{
		    String[] hostArray = null;			
			
		    if (connectionURL2.indexOf('/') == -1)
			{
			    containerLog.error("M2 ConnectionManager cannot be initialized. Database name is not provide.");				
			}
			else
			{
			    dbName = connectionURL2.substring(connectionURL2.indexOf("/")+1);
			    String hostString = connectionURL2.substring(0,connectionURL2.indexOf('/'));
				hostArray = hostString.split(",");
			}
		    		
		    int length = hostArray.length;
		    int idx = 0;
			List<ServerAddress> saList = new ArrayList<ServerAddress>();
			try 
			{
		        while (idx < length)
		        {
		            String host = hostArray[idx].substring(0,hostArray[idx].indexOf(':'));	
		            String port = hostArray[idx].substring(hostArray[idx].indexOf(':')+1);
                    saList.add(new ServerAddress(host,Integer.parseInt(port)));
					++idx;
                }
   		    	
                MongoClient mongoClient = new MongoClient(saList); 			            	
                return (new DbConnection(mongoClient,dbName));							
		    }
	        catch (MongoException e) {
		        containerLog.error(sm.getString("MongoDBRealm.exception"),e);  	
            }
            catch (UnknownHostException e) {
		        containerLog.error(sm.getString("MongoDBRealm.exception"),e);  	
            }				
		}
		
        return null;

    }	

    /**
     * Close the specified database connection.
     *
     * @param dbConn The connection to be closed
     */
    protected void close(DbConnection dbConn) {

        // Do nothing if the database connection is already closed
        if (dbConn == null)
            return;

        // Close this database connection, and log any errors
		MongoClient mongoClient = dbConn.getMongoClient();
        mongoClient.close();
        containerLog.warn("MongoDBRealm.close"); // Just log it here

    }
	
	
    /**
     * Return a short name for this Realm implementation.
     */
    @Override
    protected String getName() {

        return (name);

    }	

	
    /**
     * Return the password associated with the given principal's user name.
     */
    @Override
    protected String getPassword(String username) {

        DbConnection dbConn = null;

        // Ensure that we have an open database connection
        dbConn = open();
        if (dbConn == null) {
            return null;
        }

        try {
            return getPassword(dbConn, username);            
        } finally {
            close(dbConn);
        }
	}

    /**
     * Return the password associated with the given principal's user name.
     * @param dbConn The database connection to be used
     * @param username Username for which password should be retrieved
     */
    protected String getPassword(DbConnection dbConn,
                                 String username) {		
	
        // Look up the user's credentials
        String dbCredentials = null;
		User user = null;        
		try {
		    MongoManager mongoMgr = new MongoManager(dbConn);
		    user = mongoMgr.findUserByUserName(username);
		}
        catch (M2Exception e) {                
            // Log the problem for posterity
			containerLog.error(sm.getString("MongoDBRealm.getPassword.exception",
                               username), e);                
        }				
        catch (MongoException e) {                
            // Log the problem for posterity
			containerLog.error(sm.getString("MongoDBRealm.getPassword.exception",
                               username), e);                
        }		
	    if (user != null)
            return user.getPassword();
	    else
	        return null;	
        				
    }
 
    /**
     * Return the Principal associated with the given user name.
     */
    @Override
    protected Principal getPrincipal(String username) {
        DbConnection dbConn = open();
        if (dbConn == null) {
            return new GenericPrincipal(username, null, null);
        }
        try {
            return (new GenericPrincipal(username,
                    getPassword(dbConn, username),
                    getRoles(dbConn, username)));
        } finally {
            close(dbConn);
        }
    }

	
	/**
     * Return the roles associated with the gven user name.
     */
    protected ArrayList<String> getRoles(String username) {

        // Ensure that we have an open database connection
        DbConnection dbConn = open();
        if (dbConn == null) {
            return null;
        }

        try {
            return getRoles(dbConn, username);
        } finally {
            close(dbConn);
        }	
	
	}

    /**
     * Return the roles associated with the given user name
     * @param dbConn The database connection to be used
     * @param username Username for which roles should be retrieved
     */
    protected ArrayList<String> getRoles(DbConnection dbConn,
                                         String username) {	
        if (allRolesMode != AllRolesMode.STRICT_MODE) {
            // Using an authentication only configuration and no role store has
            // been defined so don't spend cycles looking
            return null;
        }
		
		ArrayList<String> roles = new ArrayList<String>();
		User user = null;		
		try {
            MongoManager mongoMgr = new MongoManager(dbConn);
			user = mongoMgr.findUserByUserName(username);
        } catch (M2Exception e) {                
            // Log the problem for posterity
			containerLog.error(sm.getString("MongoDBRealm.getRoles.exception",
                               username), e);                
        }				
        catch (MongoException e) {                
            // Log the problem for posterity
			containerLog.error(sm.getString("MongoDBRealm.getRoles.exception",
                               username), e);                
        }				
        if (user != null)
        {				
            ArrayList<Role> m2Roles = user.getRoles();
	        for (Role role : m2Roles) 
	        {
	            roles.add(role.getName()); 
	        }	
            return roles;					
		}	            
	    else
	        return null;
        		            
    }
	
	
    // ------------------------------------------------------ Lifecycle Methods

    /**
     * Prepare for the beginning of active use of the public methods of this
     * component and implement the requirements of
     * {@link org.apache.catalina.util.LifecycleBase#startInternal()}.
     *
     * @exception LifecycleException if this component detects a fatal error
     *  that prevents this component from being used
     */
    @Override
    protected void startInternal() throws LifecycleException {

        super.startInternal();
    }
	
	
}  
