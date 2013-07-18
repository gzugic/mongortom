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

package com.daprota.m2.util.db;

import com.mongodb.MongoClient;


public class DbConnection implements java.io.Serializable
{

    private MongoClient _mongoClient;
	private String _dbName;
	
      //----------------/
     //- Constructors -/
    //----------------/

    public DbConnection()
    {}


    public DbConnection(MongoClient mongoClient,
	                    String dbName)
    {
        this._mongoClient = mongoClient;
		this._dbName = dbName;          
    }

      //-----------/
     //- Methods -/
    //-----------/
    /**
     * Returns the value of field '_mongoClient'.
     *
     * @return String
     * @return the value of field '_mongoClient'.
     */
    public MongoClient getMongoClient()
    {
        return this._mongoClient;
    }


    /**
     * Returns the value of field '_dbName'.
     *
     * @return String
     * @return the value of field '_dbName'.
     */
    public String getDbName()
    {
        return this._dbName;
    }


	/**
     * Sets the value of field '_mongoClient'.
     *
     * @param _mongoClient the value of field '_mongoClient'.
     */
    public void setMongoClient(MongoClient mongoClient)
    {
        this._mongoClient = mongoClient;
    }


    /**
     * Sets the value of field '_dbName'.
     *
     * @param _dbName the value of field '_dbName'.
     */
    public void setDbName(String dbName)
    {
        this._dbName = dbName;
    }	

}

