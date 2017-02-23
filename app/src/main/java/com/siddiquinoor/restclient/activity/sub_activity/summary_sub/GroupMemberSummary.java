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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.siddiquinoor.restclient.R;
import com.siddiquinoor.restclient.activity.MainActivity;
import com.siddiquinoor.restclient.fragments.BaseActivity;
import com.siddiquinoor.restclient.manager.SQLiteHandler;
import com.siddiquinoor.restclient.utils.KEY;
import com.siddiquinoor.restclient.views.adapters.SummaryGroupListDataModel;
import com.siddiquinoor.restclient.views.adapters.SummaryIdListInGroupAdapter;
import com.siddiquinoor.restclient.views.adapters.SummaryIdListInGroupDataModel;
import com.siddiquinoor.restclient.views.notifications.ADNotificationManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  Faisal
 * This class show a list of member id And inserted in specifice Group
 */


public class GroupMemberSummary extends BaseActivity /*implements AdapterView.OnItemClickListener*/ {

    private Button btnBack, btnHome;
    private final Context mContext=GroupMemberSummary.this;
    private SQLiteHandler sqlH;
    private ADNotificationManager dialog;
    private ListView listView;
    SummaryGroupListDataModel groupData;
    private SummaryIdListInGroupAdapter adapter;
    private ArrayList<SummaryIdListInGroupDataModel> groupLisArray=new ArrayList<SummaryIdListInGroupDataModel>();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_summary);
        initial();
        Intent intent= getIntent();
        groupData = intent.getParcelableExtra(KEY.COMMUNITY_GRP_DATA_OBJECT_KEY);

      //  loadIdInGroupuList(groupData.getcCode(),groupData.getDonorCode(),groupData.getAwardCode(),groupData.getProgramCode(),groupData.getGroupCode());
        new LoadListView(groupData.getcCode(), groupData.getDonorCode(), groupData.getAwardCode(), groupData.getProgramCode(), groupData.getGroupCode()).execute();
        setAllListener();

    }

    private void setAllListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent iSummary = new Intent(mContext, GroupSummary.class);
                iSummary.putExtra(KEY.COUNTRY_ID, groupData.getcCode());

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

    /**
     * This method invoke in @see #onCreate(Bundle) to initiate necessary variable
     */

    private void initial() {
        viewReference();
        sqlH=new SQLiteHandler(mContext);
        dialog=new ADNotificationManager();
       // Context mContext = GroupMemberSummary.this;
    }

    private void viewReference() {
        btnBack = (Button) findViewById(R.id.btnRegisterFooter);
        btnHome = (Button) findViewById(R.id.btnHomeFooter);
        listView = (ListView) findViewById(R.id.list_group_id_records);


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
        setUpHomeButton();
        setUpBackPressButton();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setUpBackPressButton() {
        btnBack.setText("");
        Drawable backImage = getResources().getDrawable(R.drawable.goto_back);
        btnBack.setCompoundDrawablesRelativeWithIntrinsicBounds(backImage, null, null, null);
        setPaddingButton(mContext, backImage, btnBack);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setUpHomeButton() {
        btnHome.setText("");
        Drawable imageHome = getResources().getDrawable(R.drawable.home_b);
        btnHome.setCompoundDrawablesRelativeWithIntrinsicBounds(imageHome, null, null, null);
        setPaddingButton(mContext, imageHome, btnHome);
    }

    public void loadIdInGroupuList(String cCode, String donorCode, String awardCode, String prgCode,String groupCode) {



        // use variable to like operation
        List<SummaryIdListInGroupDataModel> assignList = sqlH.getIdListInGroupInGroupSummary(cCode,donorCode,awardCode ,prgCode,groupCode);
        if (assignList.size() != 0) {
            groupLisArray.clear();
            for (SummaryIdListInGroupDataModel data : assignList) {
                // add contacts data in arrayList
                groupLisArray.add(data);
            }

            adapter = new SummaryIdListInGroupAdapter((Activity) mContext, groupLisArray);
//            adapter.notifyDataSetChanged();
//            listView.setAdapter(adapter);
//            listView.setOnItemClickListener(this);
//            listView.setFocusableInTouchMode(true);

        } /*else {
            dialog.showInfromDialog(mContext, "No Data", "");
        }*/




    }

 /*   @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }*/


    /**
     * @param msg text massage
     */

    private void startProgressBar(String msg) {
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(msg);
        pDialog.setCancelable(true);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.show();
    }


    private class LoadListView extends AsyncTask<Void, Integer, String> {
        private String tem_cCode, tem_donorCode, tem_awardCode, tem_prgCode, tem_groupCode;

        public LoadListView(String tem_cCode, String tem_donorCode , String tem_awardCode, String tem_prgCode, String tem_groupCode) {
            this.tem_awardCode = tem_awardCode;
            this.tem_cCode = tem_cCode;
            this.tem_donorCode = tem_donorCode;
            this.tem_groupCode = tem_groupCode;
            this.tem_prgCode = tem_prgCode;
        }

        @Override
        protected String doInBackground(Void... params) {
            loadIdInGroupuList(tem_cCode, tem_donorCode, tem_awardCode, tem_prgCode, tem_groupCode);
            return "success";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startProgressBar("Data is Loading");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (adapter != null && adapter.getCount() > 0) {
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
                listView.setFocusableInTouchMode(true);

            } else {
              //  Log.d("MAL", "Adapter Is Empty ");
                //  dialog.showInfromDialog(mContext, "No Data", "");
            }
        }
    }
}
