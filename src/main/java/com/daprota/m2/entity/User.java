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

package com.daprota.m2.entity;

import java.util.ArrayList;


public class User implements java.io.Serializable, Comparable<User>
{

    private String _id;
    private String _userName;  
	private String _password;  
	private ArrayList<Role> _roles;		

      //----------------/
     //- Constructors -/
    //----------------/

    public User()
    {}


    public User(String id,
	            String userName,
				String password,				
                ArrayList<Role> roles)
    {
        this._id = id;
		this._userName = userName;
		this._password = password;		
		this._roles = roles;		
    }

      //-----------/
     //- Methods -/
    //-----------/
    /**
     * Returns the value of field '_id'.
     *
     * @return String
     * @return the value of field '_id'.
     */
    public String getId()
    {
        return this._id;
    }

	
    /**
     * Returns the value of field '_userName'.
     *
     * @return String
     * @return the value of field '_userName'.
     */
    public String getUserName()
    {
        return this._userName;
    }
	
	
    /**
     * Returns the value of field '_password'.
     *
     * @return String
     * @return the value of field '_password'.
     */
    public String getPassword()
    {
        return this._password;
    }	
	
	
	/**
     * Returns the value of field '_roles'
     *
     * @return List of roles
     * @return the value of field '_roles'.
     */
    public ArrayList<Role> getRoles()
    {
        return this._roles;
    }
	

	/**
     * Sets the value of field 'id'.
     *
     * @param name the value of field '_id'.
     */
    public void setId(String id)
    {
        this._id = id;
    }

	

    /**
     * Sets the value of field '_userName'.
     *
     * @param userName the value of field '_userName'.
     */
    public void setUserName(String userName)
    {
        this._userName = userName;
    }
	
	
    /**
     * Sets the value of field '_password'.
     *
     * @param password the value of field '_password'.
     */
    public void setPassword(String password)
    {
        this._password = password;
    }	

	
    /**
     * Sets the value of field '_roles'.
     *
     * @param roles the value of field '_roles'.
     */
    public void setRoles(ArrayList<Role> roles)
    {
        this._roles = roles;
    }	

	
   /**
     * Adds a role to roles
     *
     * @param role A role
     */
    public void addRole(Role role)
    {
        this._roles.add(role);
    }
	

   /**
     * Compares this user with the specified user for order
     *
     * @param user A user
	 * @return a negative integer, zero, or a positive integer as this user is 
	 * less than, equal to, or greater than the specified user. 
     */
	
	public int compareTo(User user) {
        return (_userName.compareTo(user.getUserName()));
    }

}

