package com.siddiquinoor.restclient.activity.sub_activity.assign_program.ddr;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.siddiquinoor.restclient.R;
import com.siddiquinoor.restclient.fragments.BaseActivity;

/**
 * Created by USER on 8/8/2016.
 */
public class AssignForDDRMalwaiVUL extends BaseActivity {
    TextView tv_MemberID, tv_MemberName,tv_HHName,tv_Criteria,tv_Date;
    Spinner sp_category,sp_Group,sp_disable,sp_active;
    RadioGroup radioGroup_mal_DDR;
    RadioButton rb_1,rb_2,rb_3,rb_4,rb_5,rb_6,rb_7;

    Button btnSave, btnSummary, btnHome,btnBackToAssign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_for_ddr_malwai);
    }

    private void viewReference()
    {
        //TEXT_VIEW

        tv_MemberID =(TextView)findViewById(R.id.txt_assign_mal_MemberID_ddr);
        tv_MemberName = (TextView)findViewById(R.id.txt_assign_mal_MemberName_ddr);
        tv_HHName =(TextView)findViewById(R.id.txt_assign_mal_HHName_ddr);
        tv_Criteria = (TextView)findViewById(R.id.txt_assign_mal_Criteria_ddr);
        tv_Date =(TextView)findViewById(R.id.txt_assign_mal_Reg_Date_ddr);

        //Spinner

        sp_category =(Spinner)findViewById(R.id.sp_ass_GroupCategories_mal_ddr);
        sp_Group =(Spinner)findViewById(R.id.sp_ass_GroupCode_mal_ddr);
        sp_active =(Spinner)findViewById(R.id.sp_ass_Active_mal_ddr);
        sp_disable =(Spinner)findViewById(R.id.sp_ass_disable_mal_ddr);
        //sp_disable.setVisibility(View.GONE);

        //Radio Group

        radioGroup_mal_DDR = (RadioGroup) findViewById(R.id.radio_grp_mal_ddr);
        radioGroup_mal_DDR.setVisibility(View.GONE);

        //radio Button

        rb_1 =(RadioButton)findViewById(R.id.rb_ass_mal_ddr_1);
        rb_1.setVisibility(View.GONE);
        rb_2=(RadioButton)findViewById(R.id.rb_ass_mal_ddr_2);
        rb_2.setVisibility(View.GONE);
        rb_3=(RadioButton)findViewById(R.id.rb_ass_mal_ddr_3);
        rb_3.setVisibility(View.GONE);
        rb_4=(RadioButton)findViewById(R.id.rb_ass_mal_ddr_4);
        rb_4.setVisibility(View.GONE);
        rb_5=(RadioButton)findViewById(R.id.rb_ass_mal_ddr_5);
        rb_5.setVisibility(View.GONE);
        rb_6=(RadioButton)findViewById(R.id.rb_ass_mal_ddr_6);
        rb_6.setVisibility(View.GONE);
        rb_7=(RadioButton)findViewById(R.id.rb_ass_mal_ddr_7);
        rb_7.setVisibility(View.GONE);


        //Button
        btnHome =(Button)findViewById(R.id.btnHomeFooter);
        btnSummary =(Button)findViewById(R.id.btnRegisterFooter);
        btnBackToAssign =(Button)findViewById(R.id.btn_mal_goAssignePage_ddr);
        btnSave =(Button)findViewById(R.id.btn_assign_mal_save_ddr);

        setUpHomeButton();
        setUpSummaryButton();
        setUpSaveButton();
        setUpGoToAssgnButton();


    }

    private void setUpHomeButton() {

        btnHome.setText("");
        Drawable imageHome = getResources().getDrawable(R.drawable.home_b);
        btnHome.setCompoundDrawablesRelativeWithIntrinsicBounds(imageHome, null, null, null);
        btnHome.setPadding(180, 10, 180, 10);
    }


    private void setUpSummaryButton() {
        btnSummary.setText("");
        Drawable summeryImage = getResources().getDrawable(R.drawable.summession_b);
        btnSummary.setCompoundDrawablesRelativeWithIntrinsicBounds(summeryImage, null, null, null);
        btnSummary.setPadding(180, 10, 180, 10);
    }

    private void setUpSaveButton() {
        btnSave.setText("");
        Drawable saveImage = getResources().getDrawable(R.drawable.save_b);
        btnSave.setCompoundDrawablesRelativeWithIntrinsicBounds(saveImage, null, null, null);
        btnSave.setPadding(180, 10, 180, 10);
    }

    private void setUpGoToAssgnButton() {
        btnBackToAssign.setText("");
        Drawable saveImage = getResources().getDrawable(R.drawable.goto_back);
        btnBackToAssign.setCompoundDrawablesRelativeWithIntrinsicBounds(saveImage, null, null, null);
        btnBackToAssign.setPadding(180, 10, 180, 10);
    }

}
