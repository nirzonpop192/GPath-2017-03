package com.siddiquinoor.restclient.activity.sub_activity.training;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.siddiquinoor.restclient.R;
import com.siddiquinoor.restclient.data_model.adapters.AssignDataModel;
import com.siddiquinoor.restclient.data_model.adapters.TrainingNActivityIndexDataModel;
import com.siddiquinoor.restclient.fragments.BaseActivity;
import com.siddiquinoor.restclient.manager.SQLiteHandler;
import com.siddiquinoor.restclient.utils.KEY;
import com.siddiquinoor.restclient.views.adapters.AssignDataModelAdapter;
import com.siddiquinoor.restclient.views.notifications.ADNotificationManager;

import java.util.ArrayList;
import java.util.List;

public class TABeneficiaryCardListActivity extends BaseActivity {
    private TextView tv_taTitle, tv_startNEndDate, tv_venue, tv_Address;

    private TrainingNActivityIndexDataModel mTAMasterData;
    private Button btnHome, btnNextPage, btnPreview;
    private Context mContext;

    private ADNotificationManager mDialog;


    private ListView listView;
    private static ProgressDialog pDialog;
    private EditText edtEventName;
    private SQLiteHandler sqlH;
    private AssignDataModelAdapter adapter;

    private void viewReference() {

        tv_taTitle = (TextView) findViewById(R.id.ta_index_row_tv_taTitle);
        tv_startNEndDate = (TextView) findViewById(R.id.ta_index_row_tv_StartEndDate);
        tv_venue = (TextView) findViewById(R.id.ta_index_row_tv_Venue);
        tv_Address = (TextView) findViewById(R.id.ta_index_row_tv_Address);
        btnNextPage = (Button) findViewById(R.id.btn_dt_next);
        btnPreview = (Button) findViewById(R.id.btn_dt_preview);

        listView = (ListView) findViewById(R.id.lv_eligibleBeni);
        btnHome = (Button) findViewById(R.id.btnHomeFooter);
        Button button = (Button) findViewById(R.id.btnRegisterFooter);
        button.setVisibility(View.GONE);
    }

    private void initial() {
        mContext = TABeneficiaryCardListActivity.this;
        mDialog = new ADNotificationManager();
        sqlH = new SQLiteHandler(mContext);
        Intent intent = getIntent();
        mTAMasterData = intent.getParcelableExtra(KEY.EVENT_INDEX_DATA_OBJECT_KEY);
        viewReference();
    }


    private void setText() {
        tv_taTitle.setText(mTAMasterData.getEventTittle());
        tv_startNEndDate.setText(mTAMasterData.getStartDate() + "  to  " + mTAMasterData.getEndDate());

        tv_venue.setText("" + "Venue     : " + mTAMasterData.getVenueName().trim());
        tv_Address.setText("" + "Address : " + mTAMasterData.getAddressName().trim());
    }

    private void setListener() {
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity((Activity) mContext);
            }
        });
        btnNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddParticipants();
            }
        });
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPreviousPage();
            }
        });
    }

    private void goToPreviousPage() {
        finish();
        Intent intent = new Intent(mContext, IdTypeSelection.class);
//        intent.putExtra(KEY.EVENT_INDEX_DATA_OBJECT_KEY, data);
        startActivity(intent);
    }

    private void goToAddParticipants() {
    }


    private void loadEligibleTrainingAcitMemList(final String cCode, final String memberString) {
//        List<TrainingNActivityIndexDataModel> dataList = sqlH.getTrainingActivityIndexList(cCode, eventNameSearch);
        String awardCode = "01";
        String strAward = "01";
        String progCode = "01";
        String strProgram = "01";
        String srvCode = "01";
        ArrayList<AssignDataModel> dataArray = new ArrayList<>();

//        if (dataList.size() != 0) {
//            dataArray.clear();
//
//            for (TrainingNActivityIndexDataModel data : dataList) {
//                dataArray.add(data);                                                                 //add contacts data in arrayList
//            }
//            adapter = new TrainingNActivityIndexAdapter((Activity) mContext, dataArray);                  //Assign the Adapter in list
//        }

        //listViewAss.setAdapter(null);
        List<AssignDataModel> assDatalist = sqlH.getEligibleTrainingAcitMemList(cCode, memberString);
        // listViewAss.setAdapter(null);
        if (assDatalist.size() != 0) {
            dataArray.clear();
            for (AssignDataModel asdata : assDatalist) {
                // add contacts data in arrayList

                dataArray.add(asdata);
            }


            adapter = new AssignDataModelAdapter((Activity) TABeneficiaryCardListActivity.this,
                    dataArray, awardCode, strAward, progCode, strProgram, srvCode, "", "", "",
                    "", "", "", "");


            if ( adapter.getCount() > 0) {
                if (adapter.getCount() != 0) {
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                } else {
                    new ADNotificationManager().showInfromDialog(mContext, "NO Data", "No data Found");
                }
            }

        }
    }

 /*   private void hideProgressBar() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void startProgressBar(String msg) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(msg);
        pDialog.setCancelable(true);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.show();
    }*/

    private class LoadListView extends AsyncTask<Void, Integer, String> {
        private String temCCode;
        private String memberID;


        LoadListView(String temCCode, String memberID) {
            this.memberID = memberID;
            this.temCCode = temCCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            startProgressBar("Data is Loading");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            hideProgressBar();

//            if (adapter != null && adapter.getCount() > 0) {
//                if (adapter.getCount() != 0) {
//                    adapter.notifyDataSetChanged();
//                    listView.setAdapter(adapter);
//                } else {
//                    new ADNotificationManager().showInfromDialog(mContext, "NO Data", "No data Found");
//                }
//            }
        }

        @Override
        protected String doInBackground(Void... params) {
//            loadEligibleTrainingAcitMemList(temCCode, memberID);
            return "success";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabeneficiary_card_list);
        initial();
        setText();
        setListener();

        loadEligibleTrainingAcitMemList(mTAMasterData.getcCode(),"");
//        LoadListView loadList= new LoadListView(mTAMasterData.getcCode(),"");
//        loadList.execute();
    }
}
