package com.siddiquinoor.restclient.activity.sub_activity.training;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.siddiquinoor.restclient.R;
import com.siddiquinoor.restclient.data_model.adapters.TrainingNActivityIndexDataModel;
import com.siddiquinoor.restclient.fragments.BaseActivity;
import com.siddiquinoor.restclient.utils.KEY;
import com.siddiquinoor.restclient.views.notifications.ADNotificationManager;

public class IdTypeSelection extends BaseActivity {

    public static final int NO_RADIO_BUTTON_SELECTED = -1;
    private TextView tv_taTitle, tv_startNEndDate, tv_venue, tv_Address;

    private TrainingNActivityIndexDataModel mTAMasterData;
    private Button btnHome,btnNextPage,btnPreview;
    private Context mContext;
    private RadioButton rbtnBeneficiary_card,rbtnNational_id_card,rbtnEmail_address,rbtnCell_phone,rbtnLicence;
    private RadioGroup rbtGroup;
    private ADNotificationManager mDialog;

    private void viewReference() {
        rbtGroup = (RadioGroup) findViewById(R.id.rdGrp);
        rbtnBeneficiary_card = (RadioButton) findViewById(R.id.rbtnBeneficiary_card);
        rbtnNational_id_card = (RadioButton) findViewById(R.id.rbtnNational_id_card);
        rbtnEmail_address = (RadioButton) findViewById(R.id.rbtnEmail_address);
        rbtnCell_phone = (RadioButton) findViewById(R.id.rbtnCell_phone);
        rbtnLicence = (RadioButton) findViewById(R.id.rbtnLicence);
        tv_taTitle = (TextView) findViewById(R.id.ta_index_row_tv_taTitle);
        tv_startNEndDate = (TextView) findViewById(R.id.ta_index_row_tv_StartEndDate);
        tv_venue = (TextView) findViewById(R.id.ta_index_row_tv_Venue);
        tv_Address = (TextView) findViewById(R.id.ta_index_row_tv_Address);
        btnPreview = (Button) findViewById(R.id.btn_dt_preview);
        btnNextPage = (Button) findViewById(R.id.btn_dt_next);
        btnHome = (Button) findViewById(R.id.btnHomeFooter);
        Button button = (Button) findViewById(R.id.btnRegisterFooter);
        button.setVisibility(View.GONE);
        btnPreview.setVisibility(View.INVISIBLE);
    }

    private void initial() {
        mContext=IdTypeSelection.this;
        mDialog= new ADNotificationManager();
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
    }

    private void goToAddParticipants() {

        if (rbtGroup.getCheckedRadioButtonId()== NO_RADIO_BUTTON_SELECTED){
            mDialog.showErrorDialog(mContext,"Select ID type");
        }else{
            switch (rbtGroup.getCheckedRadioButtonId()){
                case R.id.rbtnBeneficiary_card:

                    Intent intent = new Intent(mContext, TABeneficiaryCardListActivity.class);
                    intent.putExtra(KEY.EVENT_INDEX_DATA_OBJECT_KEY, mTAMasterData);
                    startActivity(intent);
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_type_selection);

        initial();
        setText();
        setListener();
    }





}
