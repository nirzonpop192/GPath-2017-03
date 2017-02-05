package com.siddiquinoor.restclient.views.spinner;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.siddiquinoor.restclient.R;
import com.siddiquinoor.restclient.manager.SQLiteHandler;
import com.siddiquinoor.restclient.manager.sqlsyntax.SQLiteQuery;
import com.siddiquinoor.restclient.utils.UtilClass;
import com.siddiquinoor.restclient.views.adapters.DynamicDataIndexDataModel;
import com.siddiquinoor.restclient.views.adapters.DynamicTableQuesDataModel;
import com.siddiquinoor.restclient.views.helper.SpinnerHelper;

import java.util.ArrayList;
import java.util.List;

import static com.siddiquinoor.restclient.manager.SQLiteHandler.AWARD_CODE_COL;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.COUNTRY_CODE_COL;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.DONOR_CODE_COL;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.GROUP_CAT_CODE_COL;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.PROGRAM_CODE_COL;

/**
 * Created by pop
 *
 * @since 1/11/2017.
 * This class is use for Load values in Spinner .
 * if  data exist in data base get the value set to the spinner value
 */

public class SpinnerLoader {
    private static String TAG = SpinnerLoader.class.getSimpleName();

    /***
     * @param context      refer to the activity which will invoke this method.
     * @param sqlH         database reference
     * @param spGroupCat   spinner view
     * @param cCode        country code
     * @param donorCode    donor code
     * @param awardCode    award code
     * @param progCode     program code
     * @param groupCatCode Group categories code
     * @param strGroupCat  Group categories name(values)
     *                     <p>
     *                     This methods load the Group Categories Code and Name to the Spinner From data base
     *                     following to the Country Code , Donor Code , Award Code , Program Code.
     *                     If data exits for certain member , Assume that member 'xyz' has group categories code 03
     *                     and it value I mean group categories name 'Producer Group Categories ' the spinner will be selected the
     *                     'Producer Group' and it's value otherwise the default value would be select .</p>
     */

    public static void loadGroupCatLoader(Context context, SQLiteHandler sqlH, Spinner spGroupCat, String cCode, String donorCode, String awardCode, String progCode, String groupCatCode, String strGroupCat) {
        int position = 0;
        String criteria = " WHERE " + COUNTRY_CODE_COL + " = '" + cCode + "' "
                + " AND " + DONOR_CODE_COL + " = '" + donorCode + "' "
                + " AND " + AWARD_CODE_COL + " = '" + awardCode + "' "
                + " AND " + PROGRAM_CODE_COL + " = '" + progCode + "' "
                + " GROUP BY  " + GROUP_CAT_CODE_COL;


        List<com.siddiquinoor.restclient.views.helper.SpinnerHelper> listAward = sqlH.getListAndID(SQLiteHandler.COMMUNITY_GROUP_CATEGORY_TABLE, criteria, null, false);
        ArrayAdapter<com.siddiquinoor.restclient.views.helper.SpinnerHelper> dataAdapter = new ArrayAdapter<com.siddiquinoor.restclient.views.helper.SpinnerHelper>(context, R.layout.spinner_layout, listAward);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spGroupCat.setAdapter(dataAdapter);


        if (groupCatCode != null) {
            for (int i = 0; i < spGroupCat.getCount(); i++) {
                String groupCategory = spGroupCat.getItemAtPosition(i).toString();
                if (groupCategory.equals(strGroupCat)) {
                    position = i;
                }
            }
            spGroupCat.setSelection(position);
        }
    }

    public static void loadActiveStatusLoader(Context context, Spinner spActive, String idActive) {
        int pos = 0;

        ArrayAdapter<CharSequence> adptMartial = ArrayAdapter.createFromResource(context, R.array.arrActive, R.layout.spinner_layout);

        adptMartial.setDropDownViewResource(R.layout.spinner_layout);
        spActive.setAdapter(adptMartial);


        if (idActive != null) {
            if (idActive.equals("Y"))
                pos = 0;
            else
                pos = 1;

            spActive.setSelection(pos);
        }
    }

    /**
     * semi universal method
     *
     * @param context     refer to the activity which will invoke this method.
     * @param sqlH        database reference
     * @param spGroup     spinner view
     * @param cCode       country code
     * @param donorCode   donor code
     * @param awardCode   award code
     * @param progCode    program code
     * @param grpCateCode Group categories code
     * @param groupCode   group Code
     * @param strGroup    group name
     *                    <p>
     *                    This methods load the Group  Code and Name to the Spinner From data base
     *                    following to the Country Code , Donor Code , Award Code , Program Code & Group Categories Code .
     *                    If data exits for certain member , Assume that member 'xyz' has group  code 0003
     *                    and it value I mean group categories name 'Tamana Group, Balaka ' the spinner will be selected the
     *                    'Tamana Group Balaka' and it's value otherwise the default value would be select .</p>
     */
    public static void loadGroupLoader(Context context, SQLiteHandler sqlH, Spinner spGroup, String cCode, String donorCode, String awardCode, String progCode, String grpCateCode, String groupCode, String strGroup) {
        int position = 0;
        String criteria = " WHERE " + SQLiteHandler.COUNTRY_CODE_COL + " = '" + cCode + "' "
                + " AND " + SQLiteHandler.DONOR_CODE_COL + " = '" + donorCode + "' "
                + " AND " + SQLiteHandler.AWARD_CODE_COL + " = '" + awardCode + "' "
                + " AND " + SQLiteHandler.PROGRAM_CODE_COL + " = '" + progCode + "' "
                + " AND " + SQLiteHandler.GROUP_CAT_CODE_COL + " = '" + grpCateCode + "' "
                + " ORDER BY  " + SQLiteHandler.GROUP_NAME_COL;


        List<SpinnerHelper> listAward = sqlH.getListAndID(SQLiteHandler.COMMUNITY_GROUP_TABLE, criteria, null, false);
        ArrayAdapter<SpinnerHelper> dataAdapter = new ArrayAdapter<SpinnerHelper>(context, R.layout.spinner_layout, listAward);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spGroup.setAdapter(dataAdapter);


        if (groupCode != null) {
            for (int i = 0; i < spGroup.getCount(); i++) {
                String groupCategory = spGroup.getItemAtPosition(i).toString();
                if (groupCategory.equals(strGroup)) {
                    position = i;
                }
            }
            spGroup.setSelection(position);
        }
    }

    /**
     * universal method
     *
     * @param context   refer to the activity which will invoke this method.
     * @param sqlH      database reference
     * @param spAward   spinner view
     * @param cCode     country Code
     * @param awardCode award code
     * @param strAward  award Name
     *                  This method load the Award Name .
     */
    public static void loadAwardLoader(Context context, SQLiteHandler sqlH, Spinner spAward, String cCode, String awardCode, String strAward) {
        int position = 0;
        String criteria = " WHERE " + SQLiteHandler.ADM_COUNTRY_AWARD_TABLE + "." + SQLiteHandler.COUNTRY_CODE_COL + "='" + cCode + "'";

        List<SpinnerHelper> listAward = sqlH.getListAndID(SQLiteHandler.ADM_COUNTRY_AWARD_TABLE, criteria, null, false);
        ArrayAdapter<SpinnerHelper> dataAdapter = new ArrayAdapter<SpinnerHelper>(context, R.layout.spinner_layout, listAward);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spAward.setAdapter(dataAdapter);


        if (awardCode != null) {
            for (int i = 0; i < spAward.getCount(); i++) {
                String award = spAward.getItemAtPosition(i).toString();
                if (award.equals(strAward)) {
                    position = i;
                }
            }
            spAward.setSelection(position);
        }

    }

    /**
     * @param context           refer to the activity which will invoke this method.
     * @param sqlH              database reference
     * @param spCriteria        spinner view
     * @param cCode             country code
     * @param donorCode         donor code
     * @param awardCode         award code
     * @param foodFlagTypeQuery sqlite query An "and condition" will  dynamically added  to load criteria on spinner .Assume program MCHN has
     *                          four service PW,LM,CA2,CU2. CA2 & CU2 have Food type service but PW and LM
     *                          have Cash type service .if user select food type then pw and lm will not appeared
     * @param criteriaCode      criteria Code (programCode + service Code)
     * @param strCriteria       criteria Name (programShortName + serviceShortName )
     *                          <p>
     *                          This methods load the Criteria Code (Program Code+ Service Code ) and Name (Program Name + service Name ) to the Spinner From data base
     *                          following to the Country Code , Donor Code , Award Code , foodFlagTypeQuery .</p>
     */
    public static void loadServiceRecodeCriteriaLoader(Context context, SQLiteHandler sqlH, Spinner spCriteria, String cCode, String donorCode, String awardCode, String foodFlagTypeQuery, String criteriaCode, String strCriteria) {

        int position = 0;
        String criteria = SQLiteQuery.loadServiceRecodeCriteria(cCode, donorCode, awardCode, foodFlagTypeQuery);


        List<SpinnerHelper> listCriteria = sqlH.getListAndID(SQLiteHandler.CUSTOM_QUERY, criteria, null, false);
        ArrayAdapter<SpinnerHelper> dataAdapter = new ArrayAdapter<SpinnerHelper>(context, R.layout.spinner_layout, listCriteria);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spCriteria.setAdapter(dataAdapter);


        if (criteriaCode != null) {
            for (int i = 0; i < spCriteria.getCount(); i++) {
                String award = spCriteria.getItemAtPosition(i).toString();
                if (award.equals(strCriteria)) {
                    position = i;
                }
            }
            spCriteria.setSelection(position);
        }

    }

    public static void loadServiceCenterLoader(Context context, SQLiteHandler sqlH, Spinner spServiceCenter, String idSrvCenter, String strServiceCenter) {
        int position = 0;


        int operationMode = UtilClass.getAppOperationMode((Activity) context);
        String criteria = SQLiteQuery.loadServiceCenter_sql(operationMode);


        List<SpinnerHelper> listAward = sqlH.getListAndID(SQLiteHandler.CUSTOM_QUERY, criteria, null, false);
        ArrayAdapter<SpinnerHelper> dataAdapter = new ArrayAdapter<SpinnerHelper>(context, R.layout.spinner_layout, listAward);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spServiceCenter.setAdapter(dataAdapter);


        if (idSrvCenter != null) {
            for (int i = 0; i < spServiceCenter.getCount(); i++) {
                String serviceCenterName = spServiceCenter.getItemAtPosition(i).toString();

                if (serviceCenterName.equals(strServiceCenter)) {
                    position = i;
                }
            }
            spServiceCenter.setSelection(position);
        }

    }

    /**
     * todo: describe it
     *
     * @param context refer to the activity which will invoke this method.
     * @param sqlH    database reference
     * @param spOrg   spinner view
     * @param cCode   country code
     * @param idOrg   organization Code
     * @param strOrg  organization Code
     * @param query   load spinner query
     */
    public static void loadOrganizationLoader(Context context, SQLiteHandler sqlH, Spinner spOrg, String cCode, String idOrg, String strOrg, String query) {

        int position = 0;

        List<SpinnerHelper> listUpazilla = sqlH.getListAndID(SQLiteHandler.CUSTOM_QUERY, query, cCode, false);
        ArrayAdapter<SpinnerHelper> dataAdapter = new ArrayAdapter<SpinnerHelper>(context, R.layout.spinner_layout, listUpazilla);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spOrg.setAdapter(dataAdapter);

        if (idOrg != null) {

            for (int i = 0; i < spOrg.getCount(); i++) {
                String orgation = spOrg.getItemAtPosition(i).toString();
                if (orgation.equals(strOrg)) {
                    position = i;
                }
            }
            spOrg.setSelection(position);
        }
    }

    /***
     * the dynamic spinner loader in Dynamic Response
     *
     * @param context    refer to the activity which will invoke this method.
     * @param sqlH       database reference
     * @param dt_spinner spinner view
     * @param cCode      country code
     * @param resLupText response Look up text
     * @param strSpinner spinner T7ext
     * @param mDTQ       Dynamic Table Question
     */
    public static void loadDynamicSpinnerListLoader(Context context, SQLiteHandler sqlH, Spinner dt_spinner, String cCode, String resLupText, String strSpinner, DynamicTableQuesDataModel mDTQ, DynamicDataIndexDataModel dyBasic) {

        int position = 0;
        List<SpinnerHelper> list = new ArrayList<SpinnerHelper>();

        String udf = SQLiteQuery.loadDynamicSpinnerListLoader_sql(cCode, resLupText, mDTQ.getLup_TableName(),dyBasic);
        Log.d(TAG, " resLupText:" + resLupText);


        list.clear();
        list = sqlH.getListAndID(SQLiteHandler.CUSTOM_QUERY, udf, cCode, false);

        ArrayAdapter<SpinnerHelper> dataAdapter = new ArrayAdapter<SpinnerHelper>(context, R.layout.spinner_layout, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);

        dt_spinner.setAdapter(dataAdapter);

        /**      Retrieving Code for previous button         */
        if (strSpinner != null) {
            for (int i = 0; i < dt_spinner.getCount(); i++) {
                String union = dt_spinner.getItemAtPosition(i).toString();
                if (union.equals(strSpinner)) {
                    position = i;
                }
            }
            dt_spinner.setSelection(position);
        }

    }

    public static void loadLocationLoader(Context context, SQLiteHandler sqlH, Spinner spLocation, String cCode, String idLocation, String strLocation) {

        int position = 0;
        String criteria = SQLiteQuery.loadLocationLoader_sql(cCode);


        List<SpinnerHelper> listLocation = sqlH.getListAndID(SQLiteHandler.CUSTOM_QUERY, criteria, null, false);
        ArrayAdapter<SpinnerHelper> dataAdapter = new ArrayAdapter<SpinnerHelper>(context, R.layout.spinner_layout, listLocation);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spLocation.setAdapter(dataAdapter);


        if (idLocation != null) {
            for (int i = 0; i < spLocation.getCount(); i++) {
                String locationName = spLocation.getItemAtPosition(i).toString();
                if (locationName.equals(strLocation)) {
                    position = i;
                }
            }
            spLocation.setSelection(position);
        }
    }

    /**
     * this method load dynamic survey  active month.
     *
     * @param context   refer to the activity which will invoke this method.
     * @param sqlH      database reference
     * @param spDtMonth spinner view
     * @param cCode     country code
     * @param opCode    operation code for dynamic table is 5
     * @param idMonth   op month code
     * @param strMonth  op month name
     */
    public static void loadDtMonthLoader(Context context, SQLiteHandler sqlH, Spinner spDtMonth, String cCode, String opCode, String idMonth, String strMonth) {
        int position = 0;
        String criteria = SQLiteQuery.loadDtMonth_sql(cCode, opCode);


        List<SpinnerHelper> listProgram = sqlH.getListAndID(SQLiteHandler.CUSTOM_QUERY, criteria, null, false);
        /**         *  remove select value         */
        listProgram.remove(0);

        ArrayAdapter<SpinnerHelper> dataAdapter = new ArrayAdapter<SpinnerHelper>(context, R.layout.spinner_layout, listProgram);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);

        spDtMonth.setAdapter(dataAdapter);


        if (idMonth != null) {
            for (int i = 0; i < spDtMonth.getCount(); i++) {
                String monthName = spDtMonth.getItemAtPosition(i).toString();
                if (monthName.equals(strMonth)) {
                    position = i;
                }
            }
            spDtMonth.setSelection(position);

        }

    }

    public static void loadCountryLoader(Context context, SQLiteHandler sqlH, Spinner spCountry, int operationMode, String idCountry, String strCountry) {

        int position = 0;
        String criteria = "";
        Log.d(TAG, "operation mode : " + operationMode);
        criteria = SQLiteQuery.loadCountry_sql(operationMode, sqlH.isMultipleCountryAccessUser());


        List<SpinnerHelper> listCountry = sqlH.getListAndID(SQLiteHandler.COUNTRY_TABLE, criteria, null, true);
        ArrayAdapter<SpinnerHelper> dataAdapter = new ArrayAdapter<SpinnerHelper>(context, R.layout.spinner_layout, listCountry);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);

        spCountry.setAdapter(dataAdapter);


        if (idCountry != null) {
            for (int i = 0; i < spCountry.getCount(); i++) {
                String district = spCountry.getItemAtPosition(i).toString();
                if (district.equals(strCountry)) {
                    position = i;
                }
            }
            spCountry.setSelection(position);
        }
    }
}
