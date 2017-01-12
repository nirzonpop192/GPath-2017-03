package com.siddiquinoor.restclient.views.spinner;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.siddiquinoor.restclient.R;
import com.siddiquinoor.restclient.manager.SQLiteHandler;
import com.siddiquinoor.restclient.views.helper.SpinnerHelper;

import java.util.List;

import static com.siddiquinoor.restclient.manager.SQLiteHandler.AWARD_CODE_COL;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.COUNTRY_CODE_COL;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.DONOR_CODE_COL;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.GROUP_CAT_CODE_COL;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.PROGRAM_CODE_COL;

/**
 * Created by pop on 1/11/2017.
 * This class is use for Load values in Spinner .
 * if  data exist in data base get the value set to the spinner value
 */

public class SpinnerLoader {

    /***
     * @param context     refer to the activity which will invoke this method.
     * @param sqlH        database reference
     * @param spGroupCat  spinner view
     * @param cCode       country code
     * @param donorCode   donor code
     * @param awardCode   award code
     * @param progCode    program code
     * @param idGroupCat  Group categories code
     * @param strGroupCat Group categories name(values)
     *                    <p>
     *                    This methods load the Group Categories Code and Name to the Spinner From data base
     *                    following to the Country Code , Donor Code , Award Code , Program Code.
     *                    If data exits for certain member , Assume that member 'xyz' has group categories code 03
     *                    and it value I mean group categories name 'Producer Group Categories ' the spinner will be selected the
     *                    'Producer Group' and it's value otherwise the default value would be select .</p>
     */

    public static void loadGroupCatLoader(Context context, SQLiteHandler sqlH, Spinner spGroupCat, String cCode, String donorCode, String awardCode, String progCode, String idGroupCat, String strGroupCat) {
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


        if (idGroupCat != null) {
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
     * @param context     refer to the activity which will invoke this method.
     * @param sqlH        database reference
     * @param spGroup     spinner view
     * @param cCode       country code
     * @param donorCode   donor code
     * @param awardCode   award code
     * @param progCode    program code
     * @param grpCateCode Group categories code
     * @param idGroup     group Code
     * @param strGroup    group name
     *                    <p>
     *                    This methods load the Group  Code and Name to the Spinner From data base
     *                    following to the Country Code , Donor Code , Award Code , Program Code & Group Categories Code .
     *                    If data exits for certain member , Assume that member 'xyz' has group  code 0003
     *                    and it value I mean group categories name 'Tamana Group, Balaka ' the spinner will be selected the
     *                    'Tamana Group Balaka' and it's value otherwise the default value would be select .</p>
     */
    public static void loadGroupLoader(Context context, SQLiteHandler sqlH, Spinner spGroup, String cCode, String donorCode, String awardCode, String progCode, String grpCateCode, String idGroup, String strGroup) {
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


        if (idGroup != null) {
            for (int i = 0; i < spGroup.getCount(); i++) {
                String groupCategory = spGroup.getItemAtPosition(i).toString();
                if (groupCategory.equals(strGroup)) {
                    position = i;
                }
            }
            spGroup.setSelection(position);
        }
    }
}
