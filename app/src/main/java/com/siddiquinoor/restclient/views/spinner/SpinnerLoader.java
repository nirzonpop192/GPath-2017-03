package com.siddiquinoor.restclient.views.spinner;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.siddiquinoor.restclient.R;
import com.siddiquinoor.restclient.activity.ServiceActivity;
import com.siddiquinoor.restclient.manager.SQLiteHandler;
import com.siddiquinoor.restclient.manager.sqlsyntax.SQLiteQuery;
import com.siddiquinoor.restclient.utils.UtilClass;
import com.siddiquinoor.restclient.views.helper.SpinnerHelper;

import java.util.List;

import static com.siddiquinoor.restclient.manager.SQLiteHandler.AWARD_CODE_COL;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.COUNTRY_CODE_COL;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.DONOR_CODE_COL;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.FDP_CODE_COL;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.GROUP_CAT_CODE_COL;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.PROGRAM_CODE_COL;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.SELECTED_SERVICE_CENTER_TABLE;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.SERVICE_CENTER_CODE_COL;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.SERVICE_CENTER_NAME_COL;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.SERVICE_CENTER_TABLE;

/**
 * Created by pop on 1/11/2017.
 * This class is use for Load values in Spinner .
 * if  data exist in data base get the value set to the spinner value
 */

public class SpinnerLoader {

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
        String criteria = " WHERE " + SQLiteHandler.ADM_AWARD_TABLE + "." + SQLiteHandler.COUNTRY_CODE_COL + "='" + cCode + "'";

        List<SpinnerHelper> listAward = sqlH.getListAndID(SQLiteHandler.ADM_AWARD_TABLE, criteria, null, false);
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
        String criteria = "";
        //   myImageColumn WHEN NULL THEN 0 ELSE 1 END
        int operationMode = UtilClass.getAppOperationMode((Activity) context);
        switch (operationMode) {
            case UtilClass.SERVICE_OPERATION_MODE:
                criteria = "SELECT  CASE " + FDP_CODE_COL + " WHEN  'null'  THEN '000'  ELSE " + FDP_CODE_COL + " END " + " || '' || " + SERVICE_CENTER_CODE_COL + " , " +
                        SERVICE_CENTER_NAME_COL + " FROM " + SERVICE_CENTER_TABLE
                        + " WHERE " + SERVICE_CENTER_TABLE + "." + SERVICE_CENTER_CODE_COL + " || '' || "
                        + SERVICE_CENTER_TABLE + "." + COUNTRY_CODE_COL
                        + " IN ( SELECT "
                        + SELECTED_SERVICE_CENTER_TABLE + "." + SERVICE_CENTER_CODE_COL + " || '' || "
                        + SELECTED_SERVICE_CENTER_TABLE + "." + SQLiteHandler.COUNTRY_CODE_COL + " from " + SELECTED_SERVICE_CENTER_TABLE + ")"
                        + " AND " + FDP_CODE_COL + " != 'null' "
                        + " GROUP BY " + SERVICE_CENTER_TABLE + "." + SERVICE_CENTER_CODE_COL;


                break;
            default:
                criteria = "SELECT  CASE " + FDP_CODE_COL + " WHEN  'null'  THEN '000'  ELSE " + FDP_CODE_COL + " END " + " || '' || " + SERVICE_CENTER_CODE_COL + " , " +
                        SERVICE_CENTER_NAME_COL + " FROM " + SERVICE_CENTER_TABLE;
                break;
        }


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
}
