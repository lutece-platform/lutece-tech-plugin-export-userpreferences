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
package fr.paris.lutece.plugins.exportuserpreferences.web;

import fr.paris.lutece.plugins.exportuserpreferences.business.Key;
import fr.paris.lutece.plugins.exportuserpreferences.business.KeyHome;
import fr.paris.lutece.plugins.exportuserpreferences.utils.CsvUtils;
import fr.paris.lutece.plugins.exportuserpreferences.utils.export.UserPreferencesExportUtils;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

import java.io.IOException;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * This class provides the user interface to manage Key features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageKeys.jsp", controllerPath = "jsp/admin/plugins/exportuserpreferences/", right = "EXPORTUSERPREFERENCES_MANAGEMENT" )
public class KeyJspBean extends ManageExportuserpreferencesJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_KEYS = "/admin/plugins/exportuserpreferences/manage_keys.html";
    private static final String TEMPLATE_CREATE_KEY = "/admin/plugins/exportuserpreferences/create_key.html";
    private static final String TEMPLATE_MODIFY_KEY = "/admin/plugins/exportuserpreferences/modify_key.html";

    // Parameters
    private static final String PARAMETER_PREF_KEY = "pref_key";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_KEYS = "exportuserpreferences.manage_keys.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_KEY = "exportuserpreferences.modify_key.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_KEY = "exportuserpreferences.create_key.pageTitle";

    // Markers
    private static final String MARK_KEY_LIST = "key_list";
    private static final String MARK_KEY = "key";
    private static final String MARK_LIST_AVAILABLE_KEYS = "list_available_keys";
    private static final String JSP_MANAGE_KEYS = "jsp/admin/plugins/exportuserpreferences/ManageKeys.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_KEY = "exportuserpreferences.message.confirmRemoveKey";
    private static final String PROPERTY_DEFAULT_LIST_KEY_PER_PAGE = "exportuserpreferences.listKeys.itemsPerPage";
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "exportuserpreferences.model.entity.key.attribute.";

    // Views
    private static final String VIEW_MANAGE_KEYS = "manageKeys";
    private static final String VIEW_CREATE_KEY = "createKey";
    private static final String VIEW_MODIFY_KEY = "modifyKey";

    // Actions
    private static final String ACTION_CREATE_KEY = "createKey";
    private static final String ACTION_MODIFY_KEY = "modifyKey";
    private static final String ACTION_REMOVE_KEY = "removeKey";
    private static final String ACTION_CONFIRM_REMOVE_KEY = "confirmRemoveKey";

    // Infos
    private static final String INFO_KEY_CREATED = "exportuserpreferences.info.key.created";
    private static final String INFO_KEY_UPDATED = "exportuserpreferences.info.key.updated";
    private static final String INFO_KEY_REMOVED = "exportuserpreferences.info.key.removed";
    private static final String PROPERTY_CSV_EXTENSION = "exportuserpreferences.csv.extension";
    private static final String PROPERTY_CSV_FILE_NAME = "exportuserpreferences.csv.file.name";
    public static final String MARK_USERPREFERENCES = "USERPREFERENCES";

    // Session variable to store working values
    private Key _key;

    @View( value = VIEW_MANAGE_KEYS, defaultView = true )
    public String getManageKeys( HttpServletRequest request )
    {
        _key = null;

        List<Key> listKeys = (List<Key>) KeyHome.getKeysList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_KEY_LIST, listKeys, JSP_MANAGE_KEYS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_KEYS, TEMPLATE_MANAGE_KEYS, model );
    }

    /**
     * Returns the form to create a key
     *
     * @param request The Http request
     * @return the html code of the key form
     */
    @View( VIEW_CREATE_KEY )
    public String getCreateKey( HttpServletRequest request )
    {
        _key = ( _key != null ) ? _key : new Key(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_KEY, _key );

        List<String> listAvailableKeys = (List<String>) KeyHome.getAvailableKeysList(  );
        model.put( MARK_LIST_AVAILABLE_KEYS, listAvailableKeys );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_KEY, TEMPLATE_CREATE_KEY, model );
    }

    /**
     * Process the data capture form of a new key
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_KEY )
    public String doCreateKey( HttpServletRequest request )
    {
        populate( _key, request );

        // Check constraints
        if ( !validateBean( _key, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_KEY );
        }

        KeyHome.create( _key );
        addInfo( INFO_KEY_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_KEYS );
    }

    /**
     * Manages the removal form of a key whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_KEY )
    public String getConfirmRemoveKey( HttpServletRequest request )
    {
        String strPrefKey = String.valueOf( request.getParameter( PARAMETER_PREF_KEY ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_KEY ) );
        url.addParameter( PARAMETER_PREF_KEY, strPrefKey );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_KEY, url.getUrl(  ),
                AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a key
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage keys
     */
    @Action( ACTION_REMOVE_KEY )
    public String doRemoveKey( HttpServletRequest request )
    {
        String strPrefKey = String.valueOf( request.getParameter( PARAMETER_PREF_KEY ) );
        KeyHome.remove( strPrefKey );
        addInfo( INFO_KEY_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_KEYS );
    }

    /**
     * Returns the form to update info about a key
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_KEY )
    public String getModifyKey( HttpServletRequest request )
    {
        String strPrefKey = String.valueOf( request.getParameter( PARAMETER_PREF_KEY ) );

        if ( ( _key == null ) || ( _key.getPrefKey(  ) != strPrefKey ) )
        {
            _key = KeyHome.findByPrimaryKey( strPrefKey );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_KEY, _key );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_KEY, TEMPLATE_MODIFY_KEY, model );
    }

    /**
     * Process the change form of a key
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_KEY )
    public String doModifyKey( HttpServletRequest request )
    {
        populate( _key, request );

        // Check constraints
        if ( !validateBean( _key, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            Map<String, String> mapParameters = new HashMap<String, String>(  );
            mapParameters.put( PARAMETER_PREF_KEY, _key.getPrefKey(  ) );

            return redirect( request, VIEW_MODIFY_KEY, mapParameters );
        }

        KeyHome.update( _key );
        addInfo( INFO_KEY_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_KEYS );
    }

    /**
     * Export the values from core_user_preferences into csv file
     * @param request The Http request
     * @param response The Http response
     */
    public void doExportCSV( HttpServletRequest request, HttpServletResponse response )
    {
        Map<String, ArrayList<String>> listValues = (Map<String, ArrayList<String>>) KeyHome.getValuesList(  );

        try
        {
            //Génère le CSV
            String strFormatExtension = AppPropertiesService.getProperty( PROPERTY_CSV_EXTENSION );
            String strFileName = AppPropertiesService.getProperty( PROPERTY_CSV_FILE_NAME ) + "." + strFormatExtension;
            UserPreferencesExportUtils.addHeaderResponse( request, response, strFileName, strFormatExtension );

            OutputStream os = response.getOutputStream(  );

            //say how to decode the csv file, with utf8
            byte[] bom = new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }; // BOM values
            os.write( bom ); // adds BOM 
            CsvUtils.ecrireCsv( MARK_USERPREFERENCES, listValues, os, getLocale(  ) );

            os.flush(  );
            os.close(  );
        }
        catch ( IOException e )
        {
            AppLogService.error( e );
        }
    }
}
