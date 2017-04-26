package com.siddiquinoor.restclient.activity.sub_activity.summary_sub;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.siddiquinoor.restclient.activity.MainActivity;
import com.siddiquinoor.restclient.R;
import com.siddiquinoor.restclient.fragments.BaseActivity;
import com.siddiquinoor.restclient.manager.SQLiteHandler;
import com.siddiquinoor.restclient.utils.KEY;
import com.siddiquinoor.restclient.views.adapters.SummaryOfMemberAssignedListModel;
import com.siddiquinoor.restclient.views.adapters.SummaryOfMemberAssignedListAdapter;
import com.siddiquinoor.restclient.views.notifications.ADNotificationManager;

import java.util.ArrayList;
import java.util.List;

public class SummaryAssignBaseCriteria extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = SummaryAssignBaseCriteria.class.getSimpleName();
    private TextView tvProgram, tvVillage;

    private TextView tvCriteria;
    private String strCriteria;
    private String idCriteria;
    private String idVillage;
    private String strVillage;
    private String idProgram;
    private String strProgram;
    private String idCountry;
    private String idAward;
    private String idService;
    private ListView lv_assignList;
    private SQLiteHandler sqlH;
    private Context mContext;
    private ArrayList<SummaryOfMemberAssignedListModel> assignLisArray = new ArrayList<SummaryOfMemberAssignedListModel>();
    private SummaryOfMemberAssignedListAdapter mAdapter;
    private String idDonor;
    private String idDistrict;
    private String idUpazila;
    private String idUnit;
    private Button btnHome;
    private Button btnAssignSummary;
    private ADNotificationManager dialog;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_summary_assign_base_criteria);
        viewReference();
        mContext = this;
        dialog = new ADNotificationManager();

        sqlH = new SQLiteHandler(mContext);
        getDataFromIntent();
        tvProgram.setText(strProgram);

        tvVillage.setText(strVillage);
        tvCriteria.setText(strCriteria);
        // for test purpose
//        loadAssignSummaryList(idCountry, idDistrict, idUpazila, idUnit, idVillage, idDonor, idAward, idProgram, idService);

        LoadListView loadListView = new LoadListView(idCountry, idDistrict, idUpazila, idUnit, idVillage, idDonor, idAward, idProgram, idService);
        loadListView.execute();
        setAllListener();


    }

    private void setAllListener() {
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(mContext, MainActivity.class));
            }
        });
        btnAssignSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent iAssSummayCri = new Intent(mContext, SummaryAssignCriteria.class);

                iAssSummayCri.putExtra(KEY.COUNTRY_ID, idCountry);
                iAssSummayCri.putExtra("ASS_SUMM_DIR", true);
                iAssSummayCri.putExtra(KEY.PROGRAM_CODE, idProgram);
                iAssSummayCri.putExtra(KEY.PROGRAM_NAME, strProgram);
                iAssSummayCri.putExtra(KEY.VILLAGE_CODE, idVillage);
                iAssSummayCri.putExtra(KEY.VILLAGE_NAME, strVillage);
                startActivity(iAssSummayCri);
            }
        });
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        idCriteria = intent.getStringExtra("Assign_SumCRITERIA_ID");
        /** @description: IdCriteria = Program Code[***] + Service Code [**] */
        idService = idCriteria.substring(3, 5);
        strCriteria = intent.getStringExtra("Assign_SumCRITERIA_STR");

        idVillage = intent.getStringExtra(KEY.VILLAGE_CODE);
        strVillage = intent.getStringExtra(KEY.VILLAGE_NAME);

        idProgram = intent.getStringExtra(KEY.PROGRAM_CODE);
        strProgram = intent.getStringExtra(KEY.PROGRAM_NAME);

        idCountry = intent.getStringExtra(KEY.COUNTRY_ID);
        idDonor = intent.getStringExtra(KEY.DONOR_CODE);
        idAward = intent.getStringExtra(KEY.AWARD_CODE);
        idDistrict = intent.getStringExtra(KEY.DISTRICT_CODE);
        idUpazila = intent.getStringExtra(KEY.UPAZILLA_CODE);
        idUnit = intent.getStringExtra(KEY.UNIT_CODE);
//        Log.d(TAG, " in idCountry:" + idCountry);


    }

    private void viewReference() {
        tvProgram = (TextView) findViewById(R.id.tv_assignSummarProg);
        tvVillage = (TextView) findViewById(R.id.tv_assignSummarVillage);
        tvCriteria = (TextView) findViewById(R.id.tv_assignSummarCriteria);
        lv_assignList = (ListView) findViewById(R.id.lv_assignSummaryList);
        btnHome = (Button) findViewById(R.id.btnHomeFooter);
        btnAssignSummary = (Button) findViewById(R.id.btnRegisterFooter);
        btnAssignSummary.setText("");


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
        btnAssignSummary.setText("");
        Drawable backImage = getResources().getDrawable(R.drawable.goto_back);
        btnAssignSummary.setCompoundDrawablesRelativeWithIntrinsicBounds(backImage, null, null, null);
        setPaddingButton(mContext, backImage, btnAssignSummary);
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
        private String temSrvCode;


        private LoadListView(final String temCCode, final String temLayR1Code, final String temLayR2Code, final String temLayR3Code, final String temLayR4Code, final String temDonorCode, final String temAwardCode, final String temProgCode, final String temSrvCode) {

            this.temCCode = temCCode;
            this.temLayR1Code = temLayR1Code;
            this.temLayR2Code = temLayR2Code;
            this.temLayR3Code = temLayR3Code;
            this.temLayR4Code = temLayR4Code;
            this.temDonorCode = temDonorCode;
            this.temAwardCode = temAwardCode;
            this.temProgCode = temProgCode;
            this.temSrvCode = temSrvCode;

        }

        @Override
        protected String doInBackground(Void... params) {
//            loadAssignSummaryList(temCCode, temLayR1Code, temLayR2Code, temLayR3Code, temLayR4Code, temDonorCode, temAwardCode,temProgCode);
            loadAssignSummaryList(temCCode, temLayR1Code, temLayR2Code, temLayR3Code, temLayR4Code, temDonorCode, temAwardCode, temProgCode, temSrvCode);
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
                lv_assignList.setAdapter(mAdapter);
                lv_assignList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
                lv_assignList.setFocusableInTouchMode(true);

            } else {
                Log.d(TAG, "Adapter Is Empty ");
                dialog.showInfromDialog(mContext, "No Data Found", "No data for this village");
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
     * : 2015-10-17
     * : Faisal Mohammad
     * description: LOAD :: Criteria in list view
     */
    private void loadAssignSummaryList(String cCode, String disCode, String upCode, String unCode, String donorCode, String vCode, String awardCode, String progCode, String srvCode) {
//        Log.d(TAG, "In load service List ");


        // use variable to like operation
        List<SummaryOfMemberAssignedListModel> assignList = sqlH.getTotalListOfMemberRAssignedSummary(cCode, disCode, upCode, unCode, vCode, donorCode, awardCode, progCode, srvCode);
        if (assignList.size() != 0) {
            assignLisArray.clear();
            for (SummaryOfMemberAssignedListModel data : assignList) {
                // add contacts data in arrayList
                assignLisArray.add(data);
            }

            mAdapter = new SummaryOfMemberAssignedListAdapter((Activity) mContext, assignLisArray);
//            mAdapter.notifyDataSetChanged();
//            lv_assignList.setAdapter(mAdapter);
//            lv_assignList.setOnItemClickListener(this);
//            lv_assignList.setFocusableInTouchMode(true);
//            Toast.makeText(mContext, "Size : " + assignList.size(), Toast.LENGTH_SHORT).show();
        }/* else {
//            dialog.showInfromDialog(mContext, "No Data", " No data for this village");
        }*/

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.d(TAG, "position : " + position);

    }

    /**
     * 2015-10-17
     * Faial Mohammad
     * off the back press button
     */

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


}
