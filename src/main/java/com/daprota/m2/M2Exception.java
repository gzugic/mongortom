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
 
 
package com.daprota.m2;

public class M2Exception extends RuntimeException 
{

    public M2Exception( String msg ){
        super( msg );
        _code = -3;
    }

    public M2Exception( int code , String msg )
	{
        super( msg );
        _code = code;
    }
	

    public int getCode(){
        return _code;
    }
	
    final int _code;
}
