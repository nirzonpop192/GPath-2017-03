package com.siddiquinoor.restclient.controller;

/**
 * This class is to define basic Application configuration
 *
 * @author Siddiqui Noor
 * @version 1.3.0
 * @desc Technical Director, TechnoDhaka.
 * @link www.SiddiquiNoor.com
 * @since 1.0
 */

public class AppConfig {


    /***********************************************************************
     * UAT
     * //Windows Server Azure VM Live Server
     */
    public static final String API_LINK = "http://pciapp.cloudapp.net/datacraft/api/";
    public static final String API_LINK_ENU = "http://pciapp.cloudapp.net/datacraft/api/index.php?enu";
    //LIVE LINK
    //  public static final String API_LINK = "http://pciapp.cloudapp.net/apilive/";
    /************************************************************************/

    /***********************************************************************
     * //  Localhost
     */
  //  public static final String API_LINK = "http://192.168.49.1/api/local/";
    /************************************************************************/
    /***********************************************************************
     * //  Localhost out side ngrok
     */
    //  public static final String API_LINK = "http://83cb7db6.ngrok.io/api/";
    /************************************************************************/
    // Application developments  Environment
    public static Boolean DEV_ENVIRONMENT = false; // false / true


}
