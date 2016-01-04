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
package fr.paris.lutece.plugins.exportuserpreferences.utils;

import au.com.bytecode.opencsv.CSVWriter;

import fr.paris.lutece.plugins.exportuserpreferences.business.KeyHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.commons.beanutils.BeanUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;


public class CsvUtils
{
    private static final String PROPERTY_RESOURCES_LIBRARY_CSV_PROPERTIES;
    private static final char PROPERTY_SEPARATEUR_CSV;
    private static final String PROPERTY_ENCODING_CSV = "UTF-8";
    private static final String PARAMETER_HEADER_CSV = ".entete";
    private static final String PARAMETER_SPLIT_CSV = ",";
    private static final String PARAMETER_FIELD_CSV = ".champs";

    static
    {
        PROPERTY_RESOURCES_LIBRARY_CSV_PROPERTIES = AppPropertiesService.getProperty( 
                "exportuserpreferences.csv.configuration.path" );
        PROPERTY_SEPARATEUR_CSV = AppPropertiesService.getProperty( "exportuserpreferences.csv.separator", ";" )
                                                      .charAt( 0 );
    }

    public static Map<String, Integer> getHeaderLineOrder( String cle )
    {
        String nomDTO = cle;
        Map<String, Integer> headers = new HashMap<String, Integer>(  );

        //Chargement du fichier de properties
        InputStream isProprieteCsv = CsvUtils.class.getClassLoader(  )
                                                   .getResourceAsStream( PROPERTY_RESOURCES_LIBRARY_CSV_PROPERTIES );

        if ( isProprieteCsv == null )
        {
            throw new RuntimeException( "Fichier " + PROPERTY_RESOURCES_LIBRARY_CSV_PROPERTIES + " non trouvé." );
        }
        else
        {
            try
            {
                ArrayList<String> keysToExportList = (ArrayList<String>) KeyHome.getToExportKeysList(  );
                Properties proprieteCsv = new Properties(  );
                proprieteCsv.load( isProprieteCsv );
                ;

                //Récupération des libellés des champs
                String sListeChamps = proprieteCsv.getProperty( nomDTO + PARAMETER_FIELD_CSV );
                String[] listeChamps = sListeChamps.split( PARAMETER_SPLIT_CSV );

                int i = -1;

                // the first field is the user ID, not a pref_key
                // the user ID is handled separately
                for ( String field : listeChamps )
                {
                    if ( keysToExportList.contains( field ) )
                    {
                        headers.put( field, i );
                    }

                    ++i;
                }
            }
            catch ( IOException e )
            {
                throw new RuntimeException( "Problème lors de l'édition du fichier CSV : " + e.getMessage(  ), e );
            }
        }

        return headers;
    }

    /**
     * Ecrit sur la sortie passée en paramètre, les lignes de csv.
     * Attention : ne gère pas l'écriture des en-têtes (content type par
     * exemple) ni la fermeture du flux de sortie.
     * @param cle
     * @param <R> type du DTO résultat
     * @param businessDTO
     * @param listeResultat
     * @param out
     */
    public static void ecrireCsv( String cle, Map<String, ArrayList<String>> listeResultat, OutputStream out,
        Locale locale )
    {
        if ( listeResultat != null )
        {
            String nomDTO = cle;

            //Chargement du fichier de properties
            InputStream isProprieteCsv = CsvUtils.class.getClassLoader(  )
                                                       .getResourceAsStream( PROPERTY_RESOURCES_LIBRARY_CSV_PROPERTIES );

            if ( isProprieteCsv == null )
            {
                throw new RuntimeException( "Fichier " + PROPERTY_RESOURCES_LIBRARY_CSV_PROPERTIES + " non trouvé." );
            }
            else
            {
                try
                {
                    Properties proprieteCsv = new Properties(  );
                    proprieteCsv.load( isProprieteCsv );

                    CSVWriter csvWriter = new CSVWriter( new OutputStreamWriter( out, PROPERTY_ENCODING_CSV ),
                            PROPERTY_SEPARATEUR_CSV );

                    //Récupération des libellés d'en-tête
                    String sListeEntete = proprieteCsv.getProperty( nomDTO + PARAMETER_HEADER_CSV );
                    String[] listeEntete = sListeEntete.split( PARAMETER_SPLIT_CSV );

                    for ( int i = 0; i < listeEntete.length; i++ )
                    {
                        listeEntete[i] = I18nService.getLocalizedString( listeEntete[i], locale );
                    }

                    csvWriter.writeNext( listeEntete );

                    if ( !listeResultat.isEmpty(  ) )
                    {
                        //Récupération des libellés des champs
                        String sListeChamps = proprieteCsv.getProperty( nomDTO + PARAMETER_FIELD_CSV );
                        String[] listeChamps = sListeChamps.split( PARAMETER_SPLIT_CSV );

                        for ( Map.Entry<String, ArrayList<String>> entry : listeResultat.entrySet(  ) )
                        {
                            String[] ligneCsv = new String[listeChamps.length];

                            if ( listeChamps.length > 0 )
                            {
                                ligneCsv[0] = entry.getKey(  );
                            }

                            ArrayList<String> values = entry.getValue(  );

                            for ( int i = 0; i < values.size(  ); i++ )
                            {
                                ligneCsv[i + 1] = entry.getValue(  ).get( i );
                            }

                            csvWriter.writeNext( ligneCsv );
                        }
                    }

                    csvWriter.flush(  );
                    csvWriter.close(  );
                }
                catch ( IOException e )
                {
                    throw new RuntimeException( "Problème lors de l'édition du fichier CSV : " + e.getMessage(  ), e );
                }
            }
        }
    }
}
