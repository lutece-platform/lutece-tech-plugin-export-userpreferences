/*
 * Copyright (c) 2002-2015, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *	 and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *	 and the following disclaimer in the documentation and/or other materials
 *	 provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *	 contributors may be used to endorse or promote products derived from
 *	 this software without specific prior written permission.
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

import fr.paris.lutece.plugins.exportuserpreferences.utils.CsvUtils;
import fr.paris.lutece.plugins.exportuserpreferences.web.KeyJspBean;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides Data Access methods for Key objects
 */

public final class KeyDAO implements IKeyDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT pref_key, to_export FROM exportuserpreferences_key WHERE pref_key = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO exportuserpreferences_key ( pref_key, to_export ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM exportuserpreferences_key WHERE pref_key = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE exportuserpreferences_key SET pref_key = ?, to_export = ? WHERE pref_key = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT pref_key, to_export FROM exportuserpreferences_key";
    private static final String SQL_QUERY_SELECTALL_PREFKEY = "SELECT pref_key FROM exportuserpreferences_key";

    private static final String SQL_QUERY_SELECT_TOEXPORT = "SELECT pref_key FROM exportuserpreferences_key WHERE to_export = 1";
    private static final String SQL_SELECT_VALUES = "SELECT DISTINCT "
            + "core_user_preferences.id_user, core_user_preferences.pref_key, core_user_preferences.pref_value "
            + "FROM core_user_preferences "
            + "LEFT JOIN exportuserpreferences_key ON core_user_preferences.pref_key LIKE exportuserpreferences_key.pref_key "
            + "WHERE exportuserpreferences_key.to_export = 1";
    private static final String SQL_QUERY_SELECT_AVAILABLEKEYS = "SELECT DISTINCT pref_key FROM core_user_preferences cup " 
            + "WHERE NOT EXISTS (SELECT pref_key FROM exportuserpreferences_key ek WHERE cup.pref_key = ek.pref_key)";
    

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Key key, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        daoUtil.setString( 1, key.getPrefKey( ) );
        daoUtil.setBoolean( 2, key.getToExport( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Key load( String strPrefKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setString( 1 , strPrefKey );
        daoUtil.executeQuery( );

        Key key = null;

        if ( daoUtil.next( ) )
        {
            key = new Key();
            key.setPrefKey( daoUtil.getString( 1 ) );
            key.setToExport( daoUtil.getBoolean( 2 ) );
        }

        daoUtil.free( );
        return key;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( String strPrefKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setString( 1 , strPrefKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Key key, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        
        daoUtil.setString( 1, key.getPrefKey( ) );
        daoUtil.setBoolean( 2, key.getToExport( ) );
        daoUtil.setString( 3, key.getPrefKey( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Key> selectKeysList( Plugin plugin )
    {
        Collection<Key> keyList = new ArrayList<Key>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Key key = new Key(  );
            
            key.setPrefKey( daoUtil.getString( 1 ) );
            key.setToExport( daoUtil.getBoolean( 2 ) );

            keyList.add( key );
        }

        daoUtil.free( );
        return keyList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<String> selectPrefKeysList( Plugin plugin )
    {
            Collection<String> keyList = new ArrayList<String>( );
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_PREFKEY, plugin );
            daoUtil.executeQuery(  );

            while ( daoUtil.next(  ) )
            {
                keyList.add( daoUtil.getString( 1 ) );
            }

            daoUtil.free( );
            return keyList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<String> selectToExportKeysList( Plugin plugin )
    {
        Collection<String> keyList = new ArrayList<String>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_TOEXPORT, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            keyList.add( daoUtil.getString( 1 ) );
        }

        daoUtil.free( );
        return keyList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Map<String, ArrayList<String>> getValuesList( Plugin plugin )
    {
        Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>(  );
        Map<String, Integer> headers = CsvUtils.getHeaderLineOrder( KeyJspBean.MARK_USERPREFERENCES );
        DAOUtil daoUtil = new DAOUtil( SQL_SELECT_VALUES, plugin );
        daoUtil.executeQuery(  );
        //ArrayList<String> keysToExportList = (ArrayList<String>) selectToExportKeysList( plugin );
        
        while ( daoUtil.next(  ) )
        {
            ArrayList<String> values = map.get( daoUtil.getString( 1 ) );
            String prefValue = daoUtil.getString( 3 );
            String prefKey = daoUtil.getString( 2 );
            Integer valuePosition = headers.get( prefKey );
            if ( values == null )
            {
                values = new ArrayList<String>( headers.size() );
                
                for ( int n = 0; n < valuePosition; n++ )
                {
                    values.add( "" );
                }
                values.add( valuePosition, prefValue );
                map.put( daoUtil.getString( 1 ), values );
            }
            else
            {
                for ( int n = values.size(); n < valuePosition; n++ )
                {
                    values.add( "" );
                }
                values.add( valuePosition, prefValue );
            }
        }

        daoUtil.free(  );

        return map;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<String> selectAvailableKeysList( Plugin plugin )
    {
        Collection<String> keyList = new ArrayList<String>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_AVAILABLEKEYS, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            keyList.add( daoUtil.getString( 1 ) );
        }

        daoUtil.free( );
        return keyList;
    }
}