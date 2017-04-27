package com.siddiquinoor.restclient.activity.sub_activity.summary_sub;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.siddiquinoor.restclient.activity.SummaryMenuActivity;
import com.siddiquinoor.restclient.activity.MainActivity;
import com.siddiquinoor.restclient.R;
import com.siddiquinoor.restclient.fragments.BaseActivity;
import com.siddiquinoor.restclient.manager.SQLiteHandler;
import com.siddiquinoor.restclient.manager.sqlsyntax.SQLiteQuery;
import com.siddiquinoor.restclient.utils.KEY;
import com.siddiquinoor.restclient.views.adapters.SummaryCriteriaModel;
import com.siddiquinoor.restclient.views.adapters.SummaryCriteriaListAdapter;
import com.siddiquinoor.restclient.views.helper.SpinnerHelper;
import com.siddiquinoor.restclient.views.notifications.ADNotificationManager;
import com.siddiquinoor.restclient.views.spinner.SpinnerLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @date: 2015-10-15
 */

public class SummaryAssignCriteria extends BaseActivity  {
    private static final String TAG = SummaryAssignCriteria.class.getSimpleName();
    private Button btnHome;

    private Button btnSummary;

    private Context mContext;
    private SQLiteHandler sqlH;
    private Spinner spProgram;
    private String idProgram;
    private String strProgram;
    private Spinner spVillage;
    private String idVillage;
    private String strVillage;
    private String idCountry, idLayR1Code, idLayR2Code, idLayR3Code;

    private ArrayList<SummaryCriteriaModel> criteriaArray = new ArrayList<>();
    private SummaryCriteriaListAdapter mAdapter;
    private ListView lv_CriteriaSummary;

    /**
     * have to change  the logic
     */
    String idAward;
    String idDonor;
    private boolean isComingFromAssign;
    private ADNotificationManager dialog;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_summary_assign_criteria);
        mContext = SummaryAssignCriteria.this;
        dialog = new ADNotificationManager();
        sqlH = new SQLiteHandler(mContext);
        viewReference();
        setAllListener();

        boolean redir;
        Intent intent = getIntent();
        idCountry = intent.getStringExtra(KEY.COUNTRY_ID);

        isComingFromAssign = intent.getBooleanExtra("COME_FROM_ASSIGN", false);
        redir = intent.getBooleanExtra("ASS_SUMM_DIR", false);
        if (redir) {
            idProgram = intent.getStringExtra(KEY.PROGRAM_CODE);
            strProgram = intent.getStringExtra(KEY.PROGRAM_NAME);
            idVillage = intent.getStringExtra(KEY.VILLAGE_CODE);
            strVillage = intent.getStringExtra(KEY.VILLAGE_NAME);
        }
        loadProgram(idCountry);


    }

    private void setAllListener() {
        btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent iSummary = new Intent(mContext, SummaryMenuActivity.class);
                iSummary.putExtra(KEY.COUNTRY_ID, idCountry);

                startActivity(iSummary);
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent iHome = new Intent(mContext, MainActivity.class);
                startActivity(iHome);
            }
        });
    }

    private void viewReference() {
        btnHome = (Button) findViewById(R.id.btnHomeFooter);
        btnSummary = (Button) findViewById(R.id.btnRegisterFooter);
        spProgram = (Spinner) findViewById(R.id.sp_programAssgnSummary);
        spVillage = (Spinner) findViewById(R.id.sp_villageAssgnSummary);

        lv_CriteriaSummary = (ListView) findViewById(R.id.lv_AssignSumCriteria);
        //  btnSummary.setText("SumRegLay4TotalHHRecords");


    }

    /**
     * calling getWidth() and getHeight() too early:
     * When  the UI has not been sized and laid out on the screen yet..
     *
     * @param hasFocus the value will be true when UI is focus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        setUpGoToAssgnButton();
//        setUpHomeButton();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setUpGoToAssgnButton() {
        btnSummary.setText("");
        Drawable backImage = getResources().getDrawable(R.drawable.goto_back);
        btnSummary.setCompoundDrawablesRelativeWithIntrinsicBounds(backImage, null, null, null);
        setPaddingButton(mContext, backImage, btnSummary);
    }


    /**
     * LOAD :: Program
     */
    private void loadProgram(String cCode) {

        int position = 0;

        String criteria = " WHERE " + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + SQLiteHandler.COUNTRY_CODE + "= '" + idCountry + "'";
        // Spinner Drop down elements for District
        List<SpinnerHelper> listProgram = sqlH.getListAndID(SQLiteHandler.ASSIGN_SUMMARY_PROGRAM_DETAILS, criteria, null, false);

        // Creating mAdapter for spinner
        ArrayAdapter<SpinnerHelper> dataAdapter = new ArrayAdapter<SpinnerHelper>(this, R.layout.spinner_layout, listProgram);
        // Drop down layout style
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        // attaching data mAdapter to spinner
        spProgram.setAdapter(dataAdapter);


        if (idProgram != null) {
            for (int i = 0; i < spProgram.getCount(); i++) {
                String prog = spProgram.getItemAtPosition(i).toString();
                if (prog.equals(strProgram)) {
                    position = i;
                }
            }
            spProgram.setSelection(position);
        }


        spProgram.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strProgram = ((SpinnerHelper) spProgram.getSelectedItem()).getValue();
                idProgram = ((SpinnerHelper) spProgram.getSelectedItem()).getId();
                if (idProgram.length() > 2) {
                    Log.d(TAG, "load Program data " + idProgram);
                    idDonor = idProgram.substring(0, 2);
                    idAward = idProgram.substring(2, 4);
                    idProgram = idProgram.substring(4);

                 /*   Log.d(TAG, "load Donor data " + idDonor);
                    Log.d(TAG, "load Award data " + idAward);*/

                    loadLayR4List(idCountry);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    } // end Load Spinner

    /**
     * LOAD :: Village
     */
    private void loadLayR4List(String cCode) {
        SpinnerLoader.loadLayR4ListLoader(mContext, sqlH, spVillage, idVillage, strVillage, SQLiteQuery.loadVillageInAssignSummary_sql(getCountryCode()));


        spVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strVillage = ((SpinnerHelper) spVillage.getSelectedItem()).getValue();
                idVillage = ((SpinnerHelper) spVillage.getSelectedItem()).getId();
                Log.d(TAG, "village id :" + idVillage);
                if (Integer.parseInt(idVillage) > 0) {

                    idLayR1Code = idVillage.substring(4, 6);
                    idLayR2Code = idVillage.substring(6, 8);
                    idLayR3Code = idVillage.substring(8, 10);
                    idVillage = idVillage.substring(10);

                    //  for test purpose
//                    loadAssignSummaryCriteriaList(idCountry, idLayR1Code, idLayR2Code, idLayR3Code, idVillage, idDonor, idAward, idProgram);

                    LoadListView loadListView= new LoadListView(idCountry, idLayR1Code, idLayR2Code, idLayR3Code, idVillage, idDonor, idAward, idProgram);
                    loadListView.execute();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private class LoadListView extends AsyncTask<Void, Integer, String> {

       private String temCCode;
       private String temLayR1Code;
       private String temLayR2Code;
       private String temLayR3Code;
       private String temLayR4Code;
       private String temDonorCode;
       private String temAwardCode;
       private String temProgCode;


        private LoadListView(final String temCCode, final String temLayR1Code, final String temLayR2Code, final String temLayR3Code, final String temLayR4Code, final String temDonorCode, final String temAwardCode, final String temProgCode) {

            this.temCCode = temCCode;
            this.temLayR1Code = temLayR1Code;
            this.temLayR2Code = temLayR2Code;
            this.temLayR3Code = temLayR3Code;
            this.temLayR4Code = temLayR4Code;
            this.temDonorCode = temDonorCode;
            this.temAwardCode = temAwardCode;
            this.temProgCode = temProgCode;

        }

        @Override
        protected String doInBackground(Void... params) {
            loadAssignSummaryCriteriaList(temCCode, temLayR1Code, temLayR2Code, temLayR3Code, temLayR4Code, temDonorCode, temAwardCode,temProgCode);
            return "successes";
        }

        /**
         * Initiate the dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startProgressBar("Data is Loading");

        }


        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideProgressBar();


            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
                lv_CriteriaSummary.setAdapter(mAdapter);
                lv_CriteriaSummary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        SummaryCriteriaModel criteriaS = (SummaryCriteriaModel) mAdapter.getItem(position);
                        Intent iAssignSummary = new Intent(mContext, SummaryAssignBaseCriteria.class);


                        iAssignSummary.putExtra(KEY.COUNTRY_ID, idCountry);
                        iAssignSummary.putExtra(KEY.DONOR_CODE, idDonor);
                        iAssignSummary.putExtra(KEY.AWARD_CODE, idAward);
                        /** criteria is summary id */
                        iAssignSummary.putExtra("Assign_SumCRITERIA_ID", criteriaS.getCriteria_id());
                        iAssignSummary.putExtra("Assign_SumCRITERIA_STR", criteriaS.getCriteria_name());

                        iAssignSummary.putExtra(KEY.PROGRAM_CODE, idProgram);
                        iAssignSummary.putExtra(KEY.PROGRAM_NAME, strProgram);
                        iAssignSummary.putExtra(KEY.VILLAGE_CODE, idVillage);
                        iAssignSummary.putExtra(KEY.VILLAGE_NAME, strVillage);
                        iAssignSummary.putExtra(KEY.DISTRICT_CODE, idLayR1Code);
                        iAssignSummary.putExtra(KEY.UPAZILLA_CODE, idLayR2Code);
                        iAssignSummary.putExtra(KEY.UNIT_CODE, idLayR3Code);

                        finish();
                        startActivity(iAssignSummary);
                    }
                });
                lv_CriteriaSummary.setFocusableInTouchMode(true);

            } else {
                Log.d(TAG, "Adapter Is Empty ");
                dialog.showInfromDialog(mContext, "No Data Found", "There is no data in this program");
            }

        }
    }

    private void hideProgressBar() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    /**
     * @param msg text massage
     */
    private void startProgressBar(String msg) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(msg);
        pDialog.setCancelable(true);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.show();
    }

    /**
     * : 2015-10-16
     * <p>
     * LOAD :: Criteria in list view
     */
    public void loadAssignSummaryCriteriaList(String cCode, String disCode, String upCode, String unCode, String vCode,
                                              String donorCode, String awardCode, String progCode) {
//        Log.d(TAG, "In load service List ");


        // use veriable to like operation
        List<SummaryCriteriaModel> AssignCriteriaList = sqlH.getAssignCriteriaList(cCode, disCode, upCode, unCode, vCode, donorCode, awardCode, progCode);//, idOpMonth);//SQHandler 783:Line
        if (AssignCriteriaList.size() != 0) {
            criteriaArray.clear();
            for (SummaryCriteriaModel cdata : AssignCriteriaList) {
                // add contacts data in arrayList
                criteriaArray.add(cdata);
            }

            mAdapter = new SummaryCriteriaListAdapter(this, criteriaArray, cCode, donorCode, awardCode);
            mAdapter.notifyDataSetChanged();
//            lv_CriteriaSummary.setAdapter(mAdapter);
//            lv_CriteriaSummary.setOnItemClickListener(this);
//            lv_CriteriaSummary.setFocusableInTouchMode(true);

        } else {


            // this statements clear the list view
            criteriaArray.clear();
//            mAdapter = new SummaryCriteriaListAdapter(this, criteriaArray, cCode, donorCode, awardCode);
//            lv_CriteriaSummary.setAdapter(mAdapter);
//            mAdapter.notifyDataSetChanged();
//
//            dialog.showInfromDialog(mContext, "No Data Found", "There is no data in this program");
        }
    }



    @Override
    public void onBackPressed() {
//        if (isComingFromAssign) {
//            super.onBackPressed();
//        }
    }
}
