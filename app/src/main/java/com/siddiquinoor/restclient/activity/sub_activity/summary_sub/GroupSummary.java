package com.siddiquinoor.restclient.activity.sub_activity.summary_sub;
/**
 * Created by Faisal on 9/5/2016.
 * This Activity show the Group Summary List
 * Group Name	Category (Short Name)	Short Name (SrvCode)	Count
 */

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
import android.widget.TextView;

import com.siddiquinoor.restclient.R;
import com.siddiquinoor.restclient.fragments.BaseActivity;
import com.siddiquinoor.restclient.manager.SQLiteHandler;
import com.siddiquinoor.restclient.utils.KEY;
import com.siddiquinoor.restclient.views.adapters.SummaryGroupListAdapter;
import com.siddiquinoor.restclient.views.adapters.SummaryGroupListDataModel;
import com.siddiquinoor.restclient.views.notifications.ADNotificationManager;

import java.util.ArrayList;
import java.util.List;

public class GroupSummary extends BaseActivity /*implements AdapterView.OnItemClickListener*/ {
    private Button btnBack, btnHome;
    private ListView listView;
    private TextView tv_lay3Title;
    private final Context mContext = GroupSummary.this;

    private String idCountry;
    private SQLiteHandler sqlH;
    private SummaryGroupListAdapter adapter;
    private ArrayList<SummaryGroupListDataModel> groupLisArray = new ArrayList<SummaryGroupListDataModel>();

    private ADNotificationManager dialog;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_summary);
        initial();

        idCountry = sqlH.getSelectedCountryCode();
        tv_lay3Title.setText(sqlH.getLayerLabel(idCountry,"3"));
        setAllListener();

        new LoadListView(idCountry).execute();

    }

    private void initial() {
        viewReference();
        sqlH = new SQLiteHandler(mContext);
        dialog = new ADNotificationManager();
    }

    private void setAllListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSummaryActivity((Activity) mContext, idCountry);

            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity((Activity) mContext);

            }
        });
    }

    /**
     * refer the non java object into java object
     */
    private void viewReference() {
        btnBack = (Button) findViewById(R.id.btnRegisterFooter);
        btnHome = (Button) findViewById(R.id.btnHomeFooter);
        listView = (ListView) findViewById(R.id.list_group_records);
        tv_lay3Title = (TextView) findViewById(R.id.list_title_LayR3Name);
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

    /**
     * : 2016-10-17
     * : Faisal Mohammad
     * description: LOAD :: Criteria in list view
     */


    public void loadGroupList(String cCode) {


        // use variable to like operation
        List<SummaryGroupListDataModel> assignList = sqlH.getGroupSummaryList(cCode);
        if (assignList.size() != 0) {
            groupLisArray.clear();
            for (SummaryGroupListDataModel data : assignList) {
                // add contacts data in arrayList
                groupLisArray.add(data);
            }

            adapter = new SummaryGroupListAdapter((Activity) mContext, groupLisArray);
            // leave it for test purpose
//            adapter.notifyDataSetChanged();
//            listView.setAdapter(adapter);
//            listView.setOnItemClickListener(this);
//            listView.setFocusableInTouchMode(true);

        } else {
            dialog.showInfromDialog(mContext, "No Data", "");
        }
        //hidePDialog();
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        SummaryGroupListDataModel data = (SummaryGroupListDataModel) adapter.getItem(position);
//
//        Intent intent = new Intent(mContext, GroupMemberSummary.class);
//        intent.putExtra(KEY.COMMUNITY_GRP_DATA_OBJECT_KEY, data);
//        finish();
//        startActivity(intent);
//    }

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


    private class LoadListView extends AsyncTask<Void, Integer, String> {
        String temCountryCode;

        public LoadListView(String temCountryCode) {
            this.temCountryCode = temCountryCode;
        }

        @Override
        protected String doInBackground(Void... params) {
            loadGroupList(temCountryCode);
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


            if (adapter != null) {
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SummaryGroupListDataModel data = (SummaryGroupListDataModel) adapter.getItem(position);

                        Intent intent = new Intent(mContext, GroupMemberSummary.class);
                        intent.putExtra(KEY.COMMUNITY_GRP_DATA_OBJECT_KEY, data);
                        finish();
                        startActivity(intent);
                    }
                });
                listView.setFocusableInTouchMode(true);

            } else {
                Log.d("MAL", "Adapter Is Empty ");

            }
        }
    }
}
