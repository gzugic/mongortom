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


public class Role implements java.io.Serializable, Comparable<Role>
{

    private String _id;
    private String _name;   
	private String _description;
	
      //----------------/
     //- Constructors -/
    //----------------/

    public Role()
    {}


    public Role(String id,
	            String name,
				String description)
    {
        this._id = id;
		this._name = name; 
        this._description = description;		
    }

      //-----------/
     //- Methods -/
    //-----------/
    /**
     * Returns the value of field 'id'.
     *
     * @return String
     * @return the value of field 'id'.
     */
    public String getId()
    {
        return this._id;
    }

	
    /**
     * Returns the value of field 'name'.
     *
     * @return String
     * @return the value of field 'name'.
     */
    public String getName()
    {
        return this._name;
    }
	
	
    /**
     * Returns the value of field '_description'.
     *
     * @return String
     * @return the value of field '_description'.
     */
    public String getDescription()
    {
        return this._description;
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
     * Sets the value of field 'name'.
     *
     * @param name the value of field '_name'.
     */
    public void setName(String name)
    {
        this._name = name;
    }
	
	
    /**
     * Sets the value of field '_description'.
     *
     * @param description the value of field '_description'.
     */
    public void setDescription(String description)
    {
        this._description = description;
    }	

  
    /**
     * Compares this role with the specified role for order
     *
     * @param role A role
	 * @return a negative integer, zero, or a positive integer as this role is 
	 * less than, equal to, or greater than the specified role. 
     */
	
	public int compareTo(Role role) {
        return (_name.compareTo(role.getName()));
    }

}

