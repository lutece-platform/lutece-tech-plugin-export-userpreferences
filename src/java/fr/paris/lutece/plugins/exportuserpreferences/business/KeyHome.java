/*
 * Copyright (c) 2002-2015, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *         and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *         and the following disclaimer in the documentation and/or other materials
 *         provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *         contributors may be used to endorse or promote products derived from
 *         this software without specific prior written permission.
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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


/**
 * This class provides instances management methods (create, find, ...) for Key objects
 */
public final class KeyHome
{
    // Static variable pointed at the DAO instance
    private static IKeyDAO _dao = SpringContextService.getBean( "exportuserpreferences.keyDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "exportuserpreferences" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private KeyHome(  )
    {
    }

    /**
     * Create an instance of the key class
     * @param key The instance of the Key which contains the informations to store
     * @return The  instance of key which has been created with its primary key.
     */
    public static Key create( Key key )
    {
        _dao.insert( key, _plugin );

        return key;
    }

    /**
     * Update of the key which is specified in parameter
     * @param key The instance of the Key which contains the data to store
     * @return The instance of the  key which has been updated
     */
    public static Key update( Key key )
    {
        _dao.store( key, _plugin );

        return key;
    }

    /**
     * Remove the key whose identifier is specified in parameter
     * @param strPrefKey The key
     */
    public static void remove( String strPrefKey )
    {
        _dao.delete( strPrefKey, _plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a key whose identifier is specified in parameter
     * @param strPrefKey The key primary key
     * @return an instance of Key
     */
    public static Key findByPrimaryKey( String strPrefKey )
    {
        return _dao.load( strPrefKey, _plugin );
    }

    /**
     * Load the data of all the key objects and returns them in form of a collection
     * @return the collection which contains the data of all the key objects
     */
    public static Collection<Key> getKeysList(  )
    {
        return _dao.selectKeysList( _plugin );
    }

    /**
     * Load the pref_key of all the key objects and returns them in form of a collection
     * @return the collection which contains the pref_key of all the key objects
     */
    public static Collection<String> getPrefKeysList(  )
    {
        return _dao.selectPrefKeysList( _plugin );
    }

    /**
     * Load the pref_key of all the key objects which are exported and returns them in form of a collection
     * @return the collection which contains the pref_key of all the key objects
     */
    public static Collection<String> getToExportKeysList(  )
    {
        return _dao.selectToExportKeysList( _plugin );
    }

    /**
     * Load the values which are exported and returns them in form of a map
     * @return the map which contains the (user_id -> values of its keys) of all the key objects to export
     */
    public static Map<String, ArrayList<String>> getValuesList(  )
    {
        return _dao.getValuesList( _plugin );
    }

    /**
     * Load all the pref_key which are added in the create_key form and returns them in form of a collection
     * @return the collection which contains the pref_key of all the key objects
     */
    public static Collection<String> getAvailableKeysList(  )
    {
        return _dao.selectAvailableKeysList( _plugin );
    }
}
