package com.siddiquinoor.restclient.activity.sub_activity.summary_sub;
/**
 * Created by Faisal on 9/5/2016.
 * This Activity show the Group Summary List
 * Group Name	Category (Short Name)	Short Name (SrvCode)	Count
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.siddiquinoor.restclient.R;
import com.siddiquinoor.restclient.activity.AllSummaryActivity;
import com.siddiquinoor.restclient.activity.MainActivity;
import com.siddiquinoor.restclient.fragments.BaseActivity;
import com.siddiquinoor.restclient.manager.SQLiteHandler;
import com.siddiquinoor.restclient.utils.KEY;
import com.siddiquinoor.restclient.views.adapters.SummaryGroupListAdapter;
import com.siddiquinoor.restclient.views.adapters.SummaryGroupListDataModel;
import com.siddiquinoor.restclient.views.notifications.ADNotificationManager;

import java.util.ArrayList;
import java.util.List;

public class GroupSummary extends BaseActivity implements AdapterView.OnItemClickListener {
    private Button btn_back, btn_home;
    private ListView lv_group;
    private final Context mContext = GroupSummary.this;

    private String idCountry;
    private SQLiteHandler sqlH;
    private SummaryGroupListAdapter adapter;
   private ArrayList<SummaryGroupListDataModel>groupLisArray=new ArrayList<SummaryGroupListDataModel>() ;

    private ADNotificationManager dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_summary);
        initial();
        Intent intent = getIntent();
        idCountry = intent.getStringExtra(KEY.COUNTRY_ID);

        setAllListener();

        loadGroupList(idCountry);
    }

    private void initial() {
        viewReference();
        sqlH=new SQLiteHandler(mContext);
        dialog=new ADNotificationManager();
    }

    private void setAllListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToSummaryActivity((Activity) mContext, idCountry);

            }
        });
        btn_home.setOnClickListener(new View.OnClickListener() {
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
        btn_back = (Button) findViewById(R.id.btnRegisterFooter);
        btn_home = (Button) findViewById(R.id.btnHomeFooter);
        lv_group = (ListView) findViewById(R.id.list_group_records);
        setUpBackPressButton();
        setUpHomeButton();
    }


    private void setUpBackPressButton() {
        btn_back.setText("");
        Drawable saveImage = getResources().getDrawable(R.drawable.goto_back);
        btn_back.setCompoundDrawablesRelativeWithIntrinsicBounds(saveImage, null, null, null);
        btn_back.setPadding(180, 10, 180, 10);
    }

    private void setUpHomeButton() {
        btn_home.setText("");
        Drawable imageHome = getResources().getDrawable(R.drawable.home_b);
        btn_home.setCompoundDrawablesRelativeWithIntrinsicBounds(imageHome, null, null, null);
        btn_home.setPadding(180, 10, 180, 10);
    }

    /**
     * : 2016-10-17
     * : Faisal Mohammad
     * description: LOAD :: Criteria in list view
     */



    public void loadGroupList(String cCode) {



        // use variable to like operation
        List<SummaryGroupListDataModel> assignList = sqlH.getGroupSummaryList(cCode );
        if (assignList.size() != 0) {
            groupLisArray.clear();
            for (SummaryGroupListDataModel data : assignList) {
                // add contacts data in arrayList
                groupLisArray.add(data);
            }

            adapter = new SummaryGroupListAdapter((Activity) mContext, groupLisArray);
            adapter.notifyDataSetChanged();
            lv_group.setAdapter(adapter);
            lv_group.setOnItemClickListener(this);
            lv_group.setFocusableInTouchMode(true);

        } else {
            dialog.showInfromDialog(mContext, "No Data", "");
        }
        //hidePDialog();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SummaryGroupListDataModel data = (SummaryGroupListDataModel) adapter.getItem(position);

        Intent intent = new Intent(mContext, IdListInGroupSummary.class);
        intent.putExtra(KEY.COMMUNITY_GRP_DATA_OBJECT_KEY,data);
        finish();
        startActivity(intent);
    }
}
