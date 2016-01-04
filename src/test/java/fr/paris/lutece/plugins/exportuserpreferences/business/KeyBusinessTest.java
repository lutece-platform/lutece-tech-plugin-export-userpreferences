/*
 * Copyright (c) 2002-2015, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.exportuserpreferences.business;

import fr.paris.lutece.test.LuteceTestCase;


public class KeyBusinessTest extends LuteceTestCase
{
    private final static String PREFKEY1 = "PrefKey1";
    private final static String PREFKEY2 = "PrefKey2";
	private final static boolean TOEXPORT1 = true;
    private final static boolean TOEXPORT2 = false;

    public void testBusiness(  )
    {
        // Initialize an object
        Key key = new Key();
        key.setPrefKey( PREFKEY1 );
        key.setToExport( TOEXPORT1 );

        // Create test
        KeyHome.create( key );
        Key keyStored = KeyHome.findByPrimaryKey( key.getPrefKey( ) );
        assertEquals( keyStored.getToExport() , key.getToExport( ) );

        // Update test
        key.setPrefKey( PREFKEY2 );
        key.setToExport( TOEXPORT2 );
        KeyHome.update( key );
        keyStored = KeyHome.findByPrimaryKey( key.getPrefKey( ) );
        assertEquals( keyStored.getToExport() , key.getToExport( ) );

        // List test
        KeyHome.getKeysList();

        // Delete test
        KeyHome.remove( key.getPrefKey( ) );
        keyStored = KeyHome.findByPrimaryKey( key.getPrefKey( ) );
        assertNull( keyStored );
        
    }

}