/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.exportuserpreferences.utils.export;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * The Class UserPreferencesExportUtils.
 */
public final class UserPreferencesExportUtils
{
    public static final String ERROR_NOTHING_TO_EXPORT = "exportuserpreferences.message.csv.error.nothingToExport";
    private static final String CSV = "csv";
    private static final String APPLICATION_CSV = "application/csv";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String ATTACHMENT = "attachment ;filename=\"";
    private static final String DOUBLE_QUOTE = "\"";
    private static final String APPLICATION_OCTET = "application/octet-stream";
    private static final String PRAGMA = "Pragma";
    private static final String PUBLIC_STRING = "public";
    private static final String EXPIRES = "Expires";
    private static final String ZERO = "0";
    private static final String CACHE_CONTROL = "Cache-Control";
    private static final String CACHE_CONTROL_VALUE = "must-revalidate,post-check=0,pre-check=0";

    /**
     *
     * Creates a new UserPreferencesExportUtils.java object.
     */
    private UserPreferencesExportUtils(  )
    {
    }

    /**
    * Writes the http header in the response
    * @param request the httpServletRequest
    * @param response the http response
    * @param strFileName the name of the file who must insert in the response
    * @param strFileExtension the file extension
    */
    public static void addHeaderResponse( HttpServletRequest request, HttpServletResponse response, String strFileName,
        String strFileExtension )
    {
        response.setHeader( CONTENT_DISPOSITION, ATTACHMENT + strFileName + DOUBLE_QUOTE );

        if ( strFileExtension.equals( CSV ) )
        {
            response.setContentType( APPLICATION_CSV );
        }
        else
        {
            String strMimeType = request.getSession(  ).getServletContext(  ).getMimeType( strFileName );

            if ( strMimeType != null )
            {
                response.setContentType( strMimeType );
            }
            else
            {
                response.setContentType( APPLICATION_OCTET );
            }
        }

        response.setHeader( PRAGMA, PUBLIC_STRING );
        response.setHeader( EXPIRES, ZERO );
        response.setHeader( CACHE_CONTROL, CACHE_CONTROL_VALUE );
    }
}
