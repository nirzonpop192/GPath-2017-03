package com.siddiquinoor.restclient.activity.sub_activity.summary_sub;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.siddiquinoor.restclient.R;
import com.siddiquinoor.restclient.activity.AllSummaryActivity;
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


public class IdListInGroupSummary extends BaseActivity implements AdapterView.OnItemClickListener {

    private Button btn_back, btn_home;
    private final Context mContext = IdListInGroupSummary.this;

    private String idCountry;
    private SQLiteHandler sqlH;
    private ADNotificationManager dialog;
    private ListView listView;
    SummaryGroupListDataModel groupData;
    private SummaryIdListInGroupAdapter adapter;
    private ArrayList<SummaryIdListInGroupDataModel> groupLisArray=new ArrayList<SummaryIdListInGroupDataModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_list_in_group_summary);
        initial();
        Intent intent= getIntent();
        groupData = intent.getParcelableExtra(KEY.COMMUNITY_GRP_DATA_OBJECT_KEY);

        loadIdInGroupuList(groupData.getcCode(),groupData.getDonorCode(),groupData.getAwardCode(),groupData.getProgramCode(),groupData.getGroupCode());

/*        Log.d("Mor",
                " getcCode: "+groupData.getcCode()
                +" getAwardCode:"+groupData.getAwardCode()
                +" getDonorCode:"+groupData.getDonorCode()
                +" getGroupCatCode:"+groupData.getGroupCatCode()
                +" getGroupCode:"+groupData.getGroupCode()
                +" getProgramCode:"+groupData.getProgramCode()


                );*/

        setAllListener();

    }

    private void setAllListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent iSummary = new Intent(mContext, GroupSummary.class);
                iSummary.putExtra(KEY.COUNTRY_ID, groupData.getcCode());

                startActivity(iSummary);
            }
        });
        btn_home.setOnClickListener(new View.OnClickListener() {
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
    }

    private void viewReference() {
        btn_back = (Button) findViewById(R.id.btnRegisterFooter);
        btn_home = (Button) findViewById(R.id.btnHomeFooter);
        listView = (ListView) findViewById(R.id.list_group_id_records);

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
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
            listView.setFocusableInTouchMode(true);

        } else {
            dialog.showInfromDialog(mContext, "No Data", "");
        }




    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
