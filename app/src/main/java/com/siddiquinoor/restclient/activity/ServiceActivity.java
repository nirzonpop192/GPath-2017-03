package com.siddiquinoor.restclient.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.siddiquinoor.restclient.R;
import com.siddiquinoor.restclient.activity.sub_activity.service_sub.ServiceRecord;
import com.siddiquinoor.restclient.activity.sub_activity.service_sub.ServiceSpecification;
import com.siddiquinoor.restclient.activity.sub_activity.service_sub.ServiceVoucherDetails;
import com.siddiquinoor.restclient.fragments.BaseActivity;
import com.siddiquinoor.restclient.manager.SQLiteHandler;
import com.siddiquinoor.restclient.manager.sqlsyntax.SQLServerSyntaxGenerator;
import com.siddiquinoor.restclient.manager.sqlsyntax.SQLiteQuery;
import com.siddiquinoor.restclient.utils.KEY;
import com.siddiquinoor.restclient.utils.UtilClass;
import com.siddiquinoor.restclient.views.adapters.ServiceDataModel;
import com.siddiquinoor.restclient.views.adapters.VouItemServiceExtDataModel;
import com.siddiquinoor.restclient.views.helper.SpinnerHelper;
import com.siddiquinoor.restclient.views.notifications.ADNotificationManager;
import com.siddiquinoor.restclient.views.spinner.SpinnerLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.siddiquinoor.restclient.manager.SQLiteHandler.AWARD_CODE_COL;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.COUNTRY_CODE_COL;
import static com.siddiquinoor.restclient.manager.SQLiteHandler.DONOR_CODE_COL;


public class ServiceActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String FFA = "FFA";
    private static final String C1 = "C1";
    private static final String C2 = "C2";
    private static final String C3 = "C3";
    private static final String DRR = "DRR";
    private static final String UCT = "UCT";
    private static final String CFWS = "CFWS";
    private static final String CFWU = "CFWU";
    // for log  tag
    private final String TAG = ServiceActivity.class.getName();

    AlertDialog goToDialog;
    Intent intent;

    private Spinner spAward, spCriteria, spServiceCenter, spServiceMonth;

    private String strServiceCenter;
    private SQLiteHandler sqlH;
    private String strAward, strCriteria, strSrvMonth, strGroupCat, strGroup;
    private String idCountry, idAward, idDonor, idProgram, idCriteria, idService, idOpCode, idOpMonthCode,
            idSrvCenter, idFdpCode, idMemberSearch, idServiceMonth, idGroupCat, idGroup;
    // private String serviceMonthCode;
    private String strOpMonthLabel = null;
    private TextView tv_srvDate;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
    private Button btnHome, btnSummary;

    private Button btnSave;
    private ServiceDataListAdapter adapter;
    private ArrayList<ServiceDataModel> serviceArray = new ArrayList<ServiceDataModel>();
    ListView mListView;
    private Button btn_search;
    private EditText edt_srvMMSerach;
    private String opMonthLable;
    HashMap<String, String> dateRange;
    private Context mContext;
    private ADNotificationManager erroDialog = new ADNotificationManager();
    public static ProgressDialog pDialog;
    private CheckBox checkBox_header;
    // private static boolean isNotAdded = true;

    /**
     * To save checked items, and re-add while scrolling.
     */
    SparseBooleanArray mChecked = new SparseBooleanArray();

    private static int count = 0;

    private Spinner spGroup;
    private Spinner spGroupCategories;
    private TextView tv_srvTitleCount;
    private boolean fromQR = false;
    private Spinner spDistributionType;
    private String strDistType;
    private String idDistributionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        initialize();


        setAllListener();


        Intent intent = getIntent();
        String countryId;
        String dir = intent.getStringExtra(KEY.DIR_CLASS_NAME_KEY);
        if (dir.equals("ServiceRecord")) {


            countryId = intent.getStringExtra(KEY.COUNTRY_ID);

            idAward = intent.getStringExtra(KEY.AWARD_CODE);
            strAward = intent.getStringExtra(KEY.AWARD_NAME);

            idDonor = intent.getStringExtra(KEY.DONOR_CODE);
            strCriteria = intent.getStringExtra(KEY.CRITERIA_NAME);
            idCriteria = intent.getStringExtra(KEY.CRITERIA_CODE);
            idSrvCenter = intent.getStringExtra(KEY.SERVICE_CENTER_CODE);
            strServiceCenter = intent.getStringExtra(KEY.SERVICE_CENTER_NAME);
            String srDate = intent.getStringExtra(KEY.SERVICE_DATE);
            opMonthLable = intent.getStringExtra(KEY.OP_MONTH_LABLE);
            idOpMonthCode = intent.getStringExtra(KEY.OP_MONTH_CODE);
            idOpCode = intent.getStringExtra(KEY.OP_CODE);

            idServiceMonth = intent.getStringExtra(KEY.SERVICE_MONTH_CODE);
            strSrvMonth = intent.getStringExtra(KEY.SERVICE_MONTH_NAME);

            tv_srvDate.setText(srDate);

            idProgram = idCriteria.substring(0, 3);
            idService = idCriteria.substring(3);

            idGroup = intent.getStringExtra(KEY.GROUP_CODE);
            strGroup = intent.getStringExtra(KEY.GROUP_NAME);
            idGroupCat = intent.getStringExtra(KEY.GROUP_CATEGORY_CODE);
            strGroupCat = intent.getStringExtra(KEY.GROUP_CATEGORY_NAME);

            Log.d("NIR0", "idGroupCat :" + idGroupCat + "strGroupCat: " + strGroupCat + " idGroup :" + idGroup + " strGroup" + strGroup);

            loadindingLog(countryId, srDate);


            String memSearchId = "";
            loadAward(countryId);

        } else if (dir.equals("ServiceVoucherDetails")) {

            countryId = intent.getStringExtra(KEY.COUNTRY_ID);

            idAward = intent.getStringExtra(KEY.AWARD_CODE);
            strAward = intent.getStringExtra(KEY.AWARD_NAME);

            idDonor = intent.getStringExtra(KEY.DONOR_CODE);
            strCriteria = intent.getStringExtra(KEY.CRITERIA_NAME);
            idCriteria = intent.getStringExtra(KEY.CRITERIA_CODE);
            idSrvCenter = intent.getStringExtra(KEY.SERVICE_CENTER_CODE);
            strServiceCenter = intent.getStringExtra(KEY.SERVICE_CENTER_NAME);
            String srDate = intent.getStringExtra(KEY.SERVICE_DATE);
            opMonthLable = intent.getStringExtra(KEY.OP_MONTH_LABLE);
            idOpMonthCode = intent.getStringExtra(KEY.OP_MONTH_CODE);
            idOpCode = intent.getStringExtra(KEY.OP_CODE);

            idServiceMonth = intent.getStringExtra(KEY.SERVICE_MONTH_CODE);
            strSrvMonth = intent.getStringExtra(KEY.SERVICE_MONTH_NAME);

            tv_srvDate.setText(srDate);

            idProgram = idCriteria.substring(0, 3);
            idService = idCriteria.substring(3);

            idGroup = intent.getStringExtra(KEY.GROUP_CODE);
            strGroup = intent.getStringExtra(KEY.GROUP_NAME);
            idGroupCat = intent.getStringExtra(KEY.GROUP_CATEGORY_CODE);
            strGroupCat = intent.getStringExtra(KEY.GROUP_CATEGORY_NAME);

            Log.d("NIR0", "idGroupCat :" + idGroupCat + "strGroupCat: " + strGroupCat + " idGroup :" + idGroup + " strGroup" + strGroup);

            loadAward(countryId);
            loadindingLog(countryId, srDate);

            String memSearchId = "";


        } else if (dir.equals("ServiceSpecification")) {

            countryId = intent.getStringExtra(KEY.COUNTRY_ID);
            idAward = intent.getStringExtra(KEY.AWARD_CODE);
            strAward = intent.getStringExtra(KEY.AWARD_NAME);

            idDonor = intent.getStringExtra(KEY.DONOR_CODE);
            strCriteria = intent.getStringExtra(KEY.CRITERIA_NAME);
            idCriteria = intent.getStringExtra(KEY.CRITERIA_CODE);
            idSrvCenter = intent.getStringExtra(KEY.SERVICE_CENTER_CODE);
            strServiceCenter = intent.getStringExtra(KEY.SERVICE_CENTER_NAME);
            String srDate = intent.getStringExtra(KEY.SERVICE_DATE);


//            opMonthLable = intent.getStringExtra(KEY.OP_MONTH_LABLE);
//            idOpMonthCode = intent.getStringExtra(KEY.OP_MONTH_CODE);
//            idOpCode = intent.getStringExtra(KEY.OP_CODE);

            idServiceMonth = intent.getStringExtra(KEY.SERVICE_MONTH_CODE);
            strSrvMonth = intent.getStringExtra(KEY.SERVICE_MONTH_NAME);
            tv_srvDate.setText(srDate);

            idProgram = idCriteria.substring(0, 3);
            idService = idCriteria.substring(3);


            idGroup = intent.getStringExtra(KEY.GROUP_CODE);
            strGroup = intent.getStringExtra(KEY.GROUP_NAME);
            idGroupCat = intent.getStringExtra(KEY.GROUP_CATEGORY_CODE);
            strGroupCat = intent.getStringExtra(KEY.GROUP_CATEGORY_NAME);

            Log.d("NIR0", "idGroupCat :" + idGroupCat + "strGroupCat: " + strGroupCat + " idGroup :" + idGroup + " strGroup" + strGroup);

            loadAward(countryId);

            testLogD(countryId, srDate, "ServiceSpecification");
        } else {


            countryId = intent.getStringExtra(KEY.COUNTRY_ID);
            // String strCountry = intent.getStringExtra("STR_COUNTRY");
            Log.d(TAG, "ID_COUNTRY:" + countryId);
            loadAward(countryId);
        }



            /*
             * Select All / None DO NOT USE "setOnCheckedChangeListener" here.
             */
        checkBox_header.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    /*
                     * Set all the checkbox to True/False
                     */
                for (int i = 0; i < count; i++) {
                    mChecked.put(i, checkBox_header.isChecked());
                }

                    /*
                     * Update View
                     */
                adapter.notifyDataSetChanged();

            }
        });

            /*
             * Add Header to ListView
             */

        fromQR = false;
        Button btn_service_qr_reader = (Button) findViewById(R.id.btn_service_qr);
        btn_service_qr_reader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(ServiceActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                // QR Code Capture Activity Orientation Set : degree unit
                integrator.setOrientation(90);
                // Scan View finder size : pixel unit
                integrator.addExtra(Intents.Scan.HEIGHT, 300);
                integrator.addExtra(Intents.Scan.WIDTH, 300);
                // Capture View Start
                integrator.initiateScan();
                fromQR = true;
            }
        });

        edt_srvMMSerach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (fromQR) {
                    String temId = edt_srvMMSerach.getText().toString().trim();
                    String serviceDate = tv_srvDate.getText().toString();

                    loadServiceListView(idCountry, idDonor, idAward, idProgram, idService, temId, idOpMonthCode, strOpMonthLabel, idOpMonthCode, serviceDate, idSrvCenter, idGroup);
                    fromQR = false;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void loadindingLog(String countryId, String srDate) {
        Log.d("NIR2", "From redrirect from service Record\n"
                + "countryId : " + countryId + " idAward : " + idAward
                + " strAward :" + strAward + " idDonor :" + idDonor
                + " strCriteria: " + strCriteria + " idCriteria : " + idCriteria
                + " idSrvCenter: " + idSrvCenter + " strServiceCenter : " + strServiceCenter
                + " serviceDate: " + srDate + " opMonthLable : " + opMonthLable
                + " serviceDate: " + idOpMonthCode + " idOpCode : " + idOpCode
                + " idServiceMonth: " + idServiceMonth + " strSrvMonth : " + strSrvMonth
                + " idGroup: " + idGroup + " strGroup : " + strGroup
                + " idGroupCat: " + idGroupCat + " strGroupCat : " + strGroupCat
        );
    }

    private void testLogD(String countryId, String srDate, String pageName) {
        Log.d("NIR1", "From redir from " + pageName + "\n"
                + "countryId : " + countryId + " idAward : " + idAward
                + " strAward :" + strAward + " idDonor :" + idDonor
                + " strCriteria: " + strCriteria + " idCriteria : " + idCriteria
                + " idSrvCenter: " + idSrvCenter + " strServiceCenter : " + strServiceCenter
                + " serviceDate: " + srDate + " idServiceMonth : " + idServiceMonth
                + " strSrvMonth: " + strSrvMonth
        );
    }

    private void initialize() {
        sqlH = new SQLiteHandler(this);
        mContext = ServiceActivity.this;
        viewReference();
        pDialog = new ProgressDialog(mContext);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    String qr_content = data.getStringExtra(Intents.Scan.RESULT);
//qr_reader_result_textView.setText(qr_content);
                    edt_srvMMSerach.setText(qr_content);
                } else {
                    Log.d("TAG", "Result Not Ok");
                }
                break;
            default:
                Log.d("TAG", "No Result Code");
                break;
        }
    }

    private void setAllListener() {

        btnHome.setOnClickListener(this);
        tv_srvDate.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMemberData();
            }
        });
        btnSummary.setOnClickListener(this);

    }

    private void searchMemberData() {

        String temId = edt_srvMMSerach.getText().toString().trim();

        String serviceDate = tv_srvDate.getText().toString();
        if (temId.isEmpty() || temId.equals("") || temId.length() > 0) {

            idMemberSearch = temId;
            if (idDonor.equals("00") || idAward.equals("")) {
                erroDialog.showErrorDialog(mContext, "Select Award");
            } else if (idSrvCenter == null || idSrvCenter.equals("00")) {
                erroDialog.showErrorDialog(mContext, "Select Service Center");
            } else if (idServiceMonth == null || idServiceMonth.equals("00")) {
                erroDialog.showErrorDialog(mContext, "Select Service Month");
            } else if (serviceDate.equals("") || serviceDate.equals("yyyy-mm-dd") || serviceDate.equals("Date")) {

                erroDialog.showErrorDialog(mContext, "Select a Date");
            } else if (idProgram == null || idService == null) {
                erroDialog.showErrorDialog(mContext, "Select Criteria");
            } else {

                try {
                    dateRange = sqlH.getDateRangeForService(idCountry, idOpMonthCode);
                    String start_date = dateRange.get("sdate");
                    String end_date = dateRange.get("edate");


                    idOpCode = "2";
                    idOpMonthCode = idServiceMonth.substring(8);
                    strOpMonthLabel = strSrvMonth;


                    if (serviceDate != null && start_date != null && end_date != null) {
                        if (!getValidDateRangeUSAFormat(serviceDate, start_date, end_date)) {

                            erroDialog.showErrorDialog(mContext, "Service date is not within the valid range.");

                        } else {

                         /*   LoadingList loadlist = new LoadingList(idCountry, idDonor, idAward,
                                    idProgram, idService, idMemberSearch, idOpMonthCode,
                                    strOpMonthLabel, idOpMonthCode, serviceDate, idSrvCenter, idGroup);
                            loadlist.execute();*/

                            loadServiceListView(idCountry, idDonor, idAward, idProgram, idService, idMemberSearch, idOpMonthCode, strOpMonthLabel, idOpMonthCode, serviceDate, idSrvCenter, idGroup);
                        }

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    /**
     * this method will convert XML view into the Java View Object  .
     */

    private void viewReference() {
        spAward = (Spinner) findViewById(R.id.sp_awardList);
        spCriteria = (Spinner) findViewById(R.id.spCriteria);
        spServiceCenter = (Spinner) findViewById(R.id.spServiceCenter);
        spServiceMonth = (Spinner) findViewById(R.id.spServiceMonth);
        spGroupCategories = (Spinner) findViewById(R.id.sp_srvGroupCategories);
        spGroup = (Spinner) findViewById(R.id.sp_srvGroup);

        tv_srvDate = (TextView) findViewById(R.id.tv_srvDate);
        btnHome = (Button) findViewById(R.id.btnHomeFooter);

        btnSave = (Button) findViewById(R.id.btn_service_save);
        btnSummary = (Button) findViewById(R.id.btnRegisterFooter);
        mListView = (ListView) findViewById(R.id.lv_assign);
        btn_search = (Button) findViewById(R.id.btn_service_search);
        edt_srvMMSerach = (EditText) findViewById(R.id.edt_service_memberSearch);

        spDistributionType = (Spinner) findViewById(R.id.sp_srv_dist_Type);

        setUpSummaryButton();
//        setUpGotoButton();
        addIconHomeButton();
        setUpSaveButton();
        /**
         * Header Check Box
         */

        final View headerView = getLayoutInflater().inflate(R.layout.title_service_listview_header,
                mListView, false);

        checkBox_header = (CheckBox) headerView.findViewById(R.id.cb_ServiceCheckedAll);
        mListView.addHeaderView(headerView);


        tv_srvTitleCount = (TextView) findViewById(R.id.tv_srvTitleCount);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)

    private void setUpSummaryButton() {
        btnSummary.setText("");
        Drawable summeryImage = getResources().getDrawable(R.drawable.summession_b);
        btnSummary.setCompoundDrawablesRelativeWithIntrinsicBounds(summeryImage, null, null, null);
        btnSummary.setPadding(180, 10, 180, 10);
    }

    /**
     * Icon set by the method
     */
 /*   private void setUpGotoButton() {
        btnHome.setText("");
        Drawable imageGoto = getResources().getDrawable(R.drawable.goto_forward);
        btnHome.setCompoundDrawablesRelativeWithIntrinsicBounds(imageGoto, null, null, null);
        btnHome.setPadding(180, 10, 180, 10);
    }
*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void addIconHomeButton() {

        btnHome.setText("");
        Drawable imageHome = getResources().getDrawable(R.drawable.home_b);
        btnHome.setCompoundDrawablesRelativeWithIntrinsicBounds(imageHome, null, null, null);


        btnHome.setPadding(180, 5, 180, 5);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setUpSaveButton() {
        btnSave.setText("");
        Drawable saveImage = getResources().getDrawable(R.drawable.save_b);
        btnSave.setCompoundDrawablesRelativeWithIntrinsicBounds(saveImage, null, null, null);
        btnSave.setPadding(380, 10, 380, 10);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_srvDate:
                setDate();
                break;
            case R.id.btn_service_save:

                saveServicedData();

                break;

            case R.id.btnHomeFooter:

                //    goToAlert();
                goToMainActivity((Activity) mContext);


                break;
            case R.id.btnRegisterFooter:
                finish();
                Intent iSummary = new Intent(mContext, AllSummaryActivity.class);
                iSummary.putExtra("ID_COUNTRY", idCountry);
                startActivity(iSummary);
                break;
        }
    }

    /**
     * Save  data
     */

    private void saveServicedData() {
        String serviceDate = tv_srvDate.getText().toString();
        Log.d(TAG, " service date :" + serviceDate);

        if (serviceDate.equals("") || serviceDate.equals("yyyy-mm-dd") || serviceDate.equals("Date")) {


            erroDialog.showErrorDialog(mContext, "Please select a Date ");
        } else if (idSrvCenter.equals("00")) {
            erroDialog.showErrorDialog(mContext, "Please select Service Center ");
        } else try {

            dateRange = sqlH.getDateRangeForService(idCountry, idOpMonthCode);
            String start_date = dateRange.get("sdate");
            String end_date = dateRange.get("edate");
            idOpCode = dateRange.get("opCode");

            strOpMonthLabel = dateRange.get("opMonthLable");//"opMCode"

//      for check the value      Log.d(TAG, " idOpMonthCode  :" + idOpMonthCode + " idOpCode  :" + idOpCode + " idOpMonthCode : " + idOpMonthCode + " strOpMonthLabel : " + strOpMonthLabel);

            if (start_date != null && end_date != null) {
                if (!getValidDateRangeUSAFormat(serviceDate, start_date, end_date)) {

                    erroDialog.showErrorDialog(mContext, "Service date is not within the valid range. Save attempt denied");

                } else if (adapter.isArrayListNull()) {

                    erroDialog.showErrorDialog(mContext, "No records selected to save.");

                } else {

                    ArrayList<ServiceDataModel> alist = new ArrayList<ServiceDataModel>();
                    alist = adapter.getArrayList();

                    String srvName;
                    String progName;
                    srvName = sqlH.getServiceShortName(idProgram, idService);
                    progName = sqlH.getProgramShortName(idAward, idDonor, idProgram);

                    String wd = null;
                    Log.d(TAG, "In Save Method idAward:" + idAward + " idProgram:" + idProgram + " idService: " + idService);
                    switch (srvName) {

                        case FFA:
                        case C1:
                        case C2:
                        case C3:
                            switch (progName) {

                                case DRR:
                                    switch (idDistributionType) {
                                        case DistributionActivity.NONE:
                                            break;
                                        case DistributionActivity.FOOD_TYPE:
                                            wd = sqlH.get_ProgSrvDefaultDays(idCountry, idDonor, idAward, idProgram, idService, "FoodFlag");
                                            break;
                                        case DistributionActivity.NON_FOOD_TYPE:
                                            wd = sqlH.get_ProgSrvDefaultDays(idCountry, idDonor, idAward, idProgram, idService, "NFoodFlag");
                                            break;
                                        case DistributionActivity.CASH_TYPE:
                                            wd = sqlH.get_ProgSrvDefaultDays(idCountry, idDonor, idAward, idProgram, idService, "CashFlag");
                                            break;
                                        case DistributionActivity.VOUCHER_TYPE:
                                            wd = sqlH.get_ProgSrvDefaultDays(idCountry, idDonor, idAward, idProgram, idService, "VOFlag");
                                            break;
                                    }


                                    Log.d("SAVE", "wd:" + wd);
                                    break;
                                case UCT:
                                    wd = sqlH.get_ProgSrvDefaultDays(idCountry, idDonor, idAward, idProgram, idService, "CashFlag");
                                    break;
                            }
                            break;
                    }


                    try {
                        String EntryBy = getStaffID();
                        String EntryDate = getDateTime();


                        for (ServiceDataModel srvMemData : alist) {
                            srvMemData.setOpCode(idOpCode);
                            srvMemData.setOpMontheCode(idOpMonthCode);
                            srvMemData.setWorkingDay(wd);
//                            Log.d("SAVE", "Working  Daya setWorkingDay:" + srvMemData.getWorkingDay());


                            srvMemData.setServiceSLCode(srvMemData.getServiceSLCode());
                            srvMemData.setServiceDTCode(serviceDate);
                            srvMemData.setServiceStatusCode("O");
                            srvMemData.setServiceCenterCode(idSrvCenter);
                            srvMemData.setFPDCode(idFdpCode);

                            srvMemData.setDistFlag(idDistributionType);


                            /**
                             * get last Serviced Date
                             */
                            String lastServicedDate = sqlH.getLastServiceDate(srvMemData);

                            long dayDifference = 0;
                            SimpleDateFormat myFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
                            SQLServerSyntaxGenerator sqlServerSyntax = new SQLServerSyntaxGenerator();

                            sqlServerSyntax.setAdmCountryCode(srvMemData.getC_code());
                            sqlServerSyntax.setAdmDonorCode(srvMemData.getDonor_code());
                            sqlServerSyntax.setAdmAwardCode(srvMemData.getAward_code());
                            sqlServerSyntax.setLayR1ListCode(srvMemData.getDistrictCode());
                            sqlServerSyntax.setLayR2ListCode(srvMemData.getUpazillaCode());
                            sqlServerSyntax.setLayR3ListCode(srvMemData.getUnitCode());
                            sqlServerSyntax.setLayR4ListCode(srvMemData.getVillageCode());
                            sqlServerSyntax.setHHID(srvMemData.getHHID());
                            sqlServerSyntax.setMemID(srvMemData.getMemberId());
                            sqlServerSyntax.setProgCode(srvMemData.getProgram_code());
                            sqlServerSyntax.setSrvCode(srvMemData.getService_code());
                            sqlServerSyntax.setOpCode(srvMemData.getOpCode());
                            sqlServerSyntax.setOpMonthCode(srvMemData.getOpMontheCode());
                            sqlServerSyntax.setSrvSL(srvMemData.getServiceSLCode());
                            sqlServerSyntax.setSrvCenterCode(srvMemData.getServiceCenterCode());
                            sqlServerSyntax.setSrvDT(srvMemData.getServiceDTCode());
                            sqlServerSyntax.setSrvStatus(srvMemData.getServiceStatusCode());
                            sqlServerSyntax.setFDPCode(idFdpCode);
                            sqlServerSyntax.setWD(srvMemData.getWorkingDay());
                            sqlServerSyntax.setEntryBy(EntryBy);
                            sqlServerSyntax.setEntryDate(EntryDate);
                            sqlServerSyntax.setDistFlag(idDistributionType);
                            /**
                             * if the man get service more than one time
                             */
                            if (!lastServicedDate.equals("")) {
                                try {
                                    Date date1 = myFormat.parse(serviceDate);
                                    Date date2 = myFormat.parse(lastServicedDate);
                                    dayDifference = date2.getTime() - date1.getTime();

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                /**
                                 * if the last serviced Date & present Service date are not Same
                                 * than the data will be inserted
                                 * A man cannot get 2 service in the same day
                                 */
                                if (dayDifference != 0) {
                                    // insert for local device
                                    sqlH.addMemberIntoServiceTable(srvMemData, EntryBy, EntryDate);
                                    // insert for upload in Sync process
                                    sqlH.insertIntoUploadTable(sqlServerSyntax.insertInToSrvTable());
                                    /**
                                     * min Srv Date
                                     */
                                    saveServiceMinumDate(srvMemData, serviceDate, sqlServerSyntax);
                                    /**
                                     * max date
                                     */
                                    saveServiceMaxDate(srvMemData, serviceDate, sqlServerSyntax);


                                    /**  if it is none food than save automatically Service Extended table fgf*/


                                    /**
                                     * none fodd flag
                                     */
                                    saveNoneFoodProgram(srvMemData, sqlServerSyntax, EntryBy, EntryDate);
                                }
                            } /** if the man get service for first time */
                            else {
                                sqlH.addMemberIntoServiceTable(srvMemData, EntryBy, EntryDate);
                                // insert for upload in Sync process
                                sqlH.insertIntoUploadTable(sqlServerSyntax.insertInToSrvTable());
                                /**
                                 * SrvMinDate
                                 */
                                saveServiceMinumDate(srvMemData, serviceDate, sqlServerSyntax);

                                /**
                                 * max date
                                 */
                                saveServiceMaxDate(srvMemData, serviceDate, sqlServerSyntax);


                                /**
                                 * none fodd flag
                                 */
                                saveNoneFoodProgram(srvMemData, sqlServerSyntax, EntryBy, EntryDate);


                            }// end of the else


                        }// end of for

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    idMemberSearch = "";
                    Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_LONG).show();

                 /*   LoadingList loadList = new LoadingList(idCountry, idDonor, idAward, idProgram, idService, idMemberSearch, idOpMonthCode, strOpMonthLabel, idOpMonthCode, idSrvCenter, serviceDate, idGroup);
                    loadList.execute();*/
                    loadServiceListView(idCountry, idDonor, idAward, idProgram, idService, idMemberSearch, idOpMonthCode, strOpMonthLabel, idOpMonthCode, serviceDate, idSrvCenter, idGroup);
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void saveNoneFoodProgram(ServiceDataModel srv_memData, SQLServerSyntaxGenerator syntax, String entryBy, String entryDate) {

        if (sqlH.checkAdmCountryProgramsNoneFoodFlag(idCountry, idDonor, idAward, idProgram, idService)) {

            Log.d("NIr", "In none food flag");
            List<VouItemServiceExtDataModel> listVou = sqlH.getDefaultVoucherItemRespectToProgram(idCountry, idDonor, idAward, idProgram, idService);

            for (VouItemServiceExtDataModel data : listVou) {
                // add contacts data in arrayList


                String voRefNo = "";
                Log.d("NIr", "data.getVoItmSpec(" + data.getVoItmSpec());
                sqlH.addServiceExtendedTable(srv_memData.getC_code(), srv_memData.getDistrictCode(), srv_memData.getUpazillaCode(), srv_memData.getUnitCode(),
                        srv_memData.getVillageCode(), srv_memData.getHHID(), srv_memData.getMemberId(), srv_memData.getDonor_code(), srv_memData.getAward_code(),
                        srv_memData.getProgram_code(), srv_memData.getService_code(), srv_memData.getOpCode(), srv_memData.getOpMontheCode(),
                        data.getVoItmSpec() /*vOItmSpec*/, data.getVoItmUnit()/* Unite Code */, voRefNo, data.getVoItemCost(), idDistributionType, entryBy, entryDate, "0");
                /** set the variable than insert  upload Table*/


                syntax.setvOItmUnit(data.getVoItmUnit());
                syntax.setvOItmSpec(data.getVoItmSpec());
                syntax.setvOItmCost(data.getVoItemCost());

                sqlH.insertIntoUploadTable(syntax.insertIntoSrvExtendedTable());


            }

        }
    }

    public void saveServiceMinumDate(ServiceDataModel data, String serviceDate, SQLServerSyntaxGenerator sqlServerSyntax) {
        String srvMinimumDate = sqlH.get_MemberMinSrvDate(data.getC_code(), data.getDonor_code(), data.getAward_code(), data.getDistrictCode(), data.getUpazillaCode(), data.getUnitCode(),
                data.getVillageCode(), data.getHHID(), data.getMemberId(), data.getProgram_code(), data.getService_code());
        if (srvMinimumDate.length() > 0) {

            SimpleDateFormat myFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            Date date = null;
            Date mini = null;
            try {
                date = myFormat.parse(serviceDate);
                mini = myFormat.parse(srvMinimumDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (mini != null && date != null) {
                if (mini.getTime() > date.getTime()) {
                    sqlH.updateSrvMinDate(data.getC_code(), data.getDonor_code(), data.getAward_code(), data.getDistrictCode(), data.getUpazillaCode(), data.getUnitCode(),
                            data.getVillageCode(), data.getHHID(), data.getMemberId(), data.getProgram_code(), data.getService_code(), serviceDate);
                    sqlH.insertIntoUploadTable(sqlServerSyntax.update_SrvMinDate());
                }
            }

        } else {
            sqlH.updateSrvMinDate(data.getC_code(), data.getDonor_code(), data.getAward_code(), data.getDistrictCode(), data.getUpazillaCode(), data.getUnitCode(),
                    data.getVillageCode(), data.getHHID(), data.getMemberId(), data.getProgram_code(), data.getService_code(), serviceDate);
            sqlH.insertIntoUploadTable(sqlServerSyntax.update_SrvMinDate());
        }
    }


    public void saveServiceMaxDate(ServiceDataModel data, String serviceDate, SQLServerSyntaxGenerator sqlServerSyntax) {
        String srvMaximumDate = sqlH.get_MemberMaxSrvDate(data.getC_code(), data.getDonor_code(), data.getAward_code(), data.getDistrictCode(), data.getUpazillaCode(), data.getUnitCode(),
                data.getVillageCode(), data.getHHID(), data.getMemberId(), data.getProgram_code(), data.getService_code());
        if (srvMaximumDate.length() > 0) {

            SimpleDateFormat myFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            Date date = null;
            Date max = null;
            try {
                date = myFormat.parse(serviceDate);
                max = myFormat.parse(srvMaximumDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (max != null && date != null) {
                if (max.getTime() < date.getTime()) {
                    sqlH.updateSrvMaxDate(data.getC_code(), data.getDonor_code(), data.getAward_code(), data.getDistrictCode(), data.getUpazillaCode(), data.getUnitCode(),
                            data.getVillageCode(), data.getHHID(), data.getMemberId(), data.getProgram_code(), data.getService_code(), serviceDate);
                    sqlH.insertIntoUploadTable(sqlServerSyntax.update_SrvMaxDate());
                }
            }

        } else {
            sqlH.updateSrvMinDate(data.getC_code(), data.getDonor_code(), data.getAward_code(), data.getDistrictCode(), data.getUpazillaCode(), data.getUnitCode(),
                    data.getVillageCode(), data.getHHID(), data.getMemberId(), data.getProgram_code(), data.getService_code(), serviceDate);
            sqlH.insertIntoUploadTable(sqlServerSyntax.update_SrvMinDate());
        }
    }


    /**
     * This method Convert SlNo from String to integer Than Increments by 1
     * again convert into String to save
     *
     * @param sl Service Serial No
     * @return next Service Serial No
     */

    private String padding(String sl) {

        int tem = Integer.parseInt(sl);
//        if(tem>1)
        tem = tem + 1;

        String convertedValue = String.valueOf(tem);
        if (convertedValue.length() < 2)
            convertedValue = "0" + convertedValue;
        //  Log.d(TAG,"Service SL :"+convertedValue);
        return convertedValue;

    }

    public void setDate() {
        new DatePickerDialog(ServiceActivity.this, datepickerD, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener datepickerD = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        }
    };

    public void updateDate() {
        tv_srvDate.setText(format.format(calendar.getTime()));
    }


    /**
     * LOAD :: Group
     * This method lode the data from table to spinner
     *
     * @param cCode       Country Code
     * @param donorCode   Donor Code
     * @param awardCode   award Code
     * @param progCode    Program Code
     * @param grpCateCode Group Categories Code
     */
    private void loadGroup(final String cCode, final String donorCode, final String awardCode
            , final String progCode, final String grpCateCode, final String strSrvDate, final String srvCenterCode) {

        int position = 0;
        String criteria = " WHERE " + SQLiteHandler.COUNTRY_CODE_COL + " = '" + cCode + "' "
                + " AND " + SQLiteHandler.DONOR_CODE_COL + " = '" + donorCode + "' "
                + " AND " + AWARD_CODE_COL + " = '" + awardCode + "' "
                + " AND " + SQLiteHandler.PROGRAM_CODE_COL + " = '" + progCode + "' "
                + " AND " + SQLiteHandler.GROUP_CAT_CODE_COL + " = '" + grpCateCode + "' "
                + " AND " + SQLiteHandler.SERVICE_CENTER_CODE_COL + " = '" + srvCenterCode + "' ";
        Log.d("MOR", criteria);

        // Spinner Drop down elements for District
        List<SpinnerHelper> listAward = sqlH.getListAndID(SQLiteHandler.COMMUNITY_GROUP_TABLE, criteria, null, false);

        // Creating adapter for spinner
        ArrayAdapter<SpinnerHelper> dataAdapter = new ArrayAdapter<SpinnerHelper>(this, R.layout.spinner_layout, listAward);
        // Drop down layout style
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        // attaching data adapter to spinner
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


        spGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strGroup = ((SpinnerHelper) spGroup.getSelectedItem()).getValue();
                idGroup = ((SpinnerHelper) spGroup.getSelectedItem()).getId();
                Log.d("HEO", "Group  ,idGroup:" + idGroup + " strGroup : " + strGroup);
                if (idGroup.length() > 2) {
                    /**   working*/
              /*      LoadingList loadlist = new LoadingList(idCountry, idDonor, idAward, idProgram, idService, idMemberSearch, idOpMonthCode, strOpMonthLabel, idOpMonthCode, strSrvDate, idSrvCenter, idGroup);
                    loadlist.execute();*/

                    //  for test query
                    loadServiceListView(idCountry, idDonor, idAward, idProgram, idService, idMemberSearch, idOpMonthCode, strOpMonthLabel, idOpMonthCode, strSrvDate, idSrvCenter, idGroup);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    } // end Load Spinner


    /**
     * LOAD :: GroupCategory
     *
     * @param cCode     Adm Country Code
     * @param donorCode Adm Donor Code
     * @param awardCode Adm Award Code
     * @param progCode  Adm Program Cod e
     */
    private void loadGroupCategory(final String cCode, String donorCode, String awardCode,
                                   final String progCode, final String strSrvDate, final String srvCenterCode) {

        int position = 0;
        String criteria = " WHERE " + SQLiteHandler.COUNTRY_CODE_COL + " = '" + cCode + "' "
                + " AND " + SQLiteHandler.DONOR_CODE_COL + " = '" + donorCode + "' "
                + " AND " + AWARD_CODE_COL + " = '" + awardCode + "' "
                + " AND " + SQLiteHandler.PROGRAM_CODE_COL + " = '" + progCode + "' " +
                " GROUP BY " + SQLiteHandler.GROUP_CAT_CODE_COL;


        // Spinner Drop down elements for District
        List<SpinnerHelper> listAward = sqlH.getListAndID(SQLiteHandler.COMMUNITY_GROUP_CATEGORY_TABLE, criteria, null, false);

        // Creating adapter for spinner
        ArrayAdapter<SpinnerHelper> dataAdapter = new ArrayAdapter<SpinnerHelper>(this, R.layout.spinner_layout, listAward);
        // Drop down layout style
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        // attaching data adapter to spinner
        spGroupCategories.setAdapter(dataAdapter);


        if (idGroupCat != null) {
            for (int i = 0; i < spGroupCategories.getCount(); i++) {
                String groupCategory = spGroupCategories.getItemAtPosition(i).toString();
                if (groupCategory.equals(strGroupCat)) {
                    position = i;
                }
            }
            spGroupCategories.setSelection(position);
        }


        spGroupCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strGroupCat = ((SpinnerHelper) spGroupCategories.getSelectedItem()).getValue();
                idGroupCat = ((SpinnerHelper) spGroupCategories.getSelectedItem()).getId();

                if (idGroupCat.length() > 2)
                    loadGroup(idCountry, idDonor, idAward, progCode, idGroupCat, strSrvDate, srvCenterCode);

                Log.d(TAG, "Group Category ,idGroupCat:" + idGroupCat + " strGroupCat : " + strGroupCat);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    } // end Load Spinner


    /**
     * LOAD :: Award
     *
     * @param cCode Country Code
     */
    private void loadAward(final String cCode) {

        SpinnerLoader.loadAwardLoader(mContext, sqlH, spAward, cCode, idAward, strAward);


        spAward.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strAward = ((SpinnerHelper) spAward.getSelectedItem()).getValue();
                String awardID = ((SpinnerHelper) spAward.getSelectedItem()).getId();

                if (awardID.length() > 2) {
                    idAward = awardID.substring(2);
                    idDonor = awardID.substring(0, 2);
                    idCountry = cCode;

                    loadServiceCenter(cCode, idDonor, idAward);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    } // end Load Spinner

    /**
     * LOAD :: Service Center
     */
    private void loadServiceCenter(final String cCode, final String donorCode, final String awardCode) {

        int position = 0;
        String criteria = "";

        int operationMode = UtilClass.getAppOperationMode(ServiceActivity.this);
        switch (operationMode) {
            case UtilClass.SERVICE_OPERATION_MODE:
                criteria = "SELECT " + SQLiteHandler.FDP_CODE_COL + " || '' || " + SQLiteHandler.SERVICE_CENTER_CODE_COL + " , " +
                        SQLiteHandler.SERVICE_CENTER_NAME_COL + " FROM " + SQLiteHandler.SERVICE_CENTER_TABLE
                        + " WHERE " + SQLiteHandler.SERVICE_CENTER_TABLE + "." + SQLiteHandler.SERVICE_CENTER_CODE_COL + " || '' || "
                        + SQLiteHandler.SERVICE_CENTER_TABLE + "." + SQLiteHandler.COUNTRY_CODE_COL
                        + " IN ( SELECT "
                        + SQLiteHandler.SELECTED_SERVICE_CENTER_TABLE + "." + SQLiteHandler.SERVICE_CENTER_CODE_COL + " || '' || "
                        + SQLiteHandler.SELECTED_SERVICE_CENTER_TABLE + "." + SQLiteHandler.COUNTRY_CODE_COL + " from " + SQLiteHandler.SELECTED_SERVICE_CENTER_TABLE + ")" +
                        " GROUP BY " + SQLiteHandler.SERVICE_CENTER_TABLE + "." + SQLiteHandler.SERVICE_CENTER_CODE_COL;


                /**
                 *  todo:  where set the where  condition
                 */

                break;
            default:
                criteria = "SELECT " + SQLiteHandler.FDP_CODE_COL + " || '' || " + SQLiteHandler.SERVICE_CENTER_CODE_COL + " , " +
                        SQLiteHandler.SERVICE_CENTER_NAME_COL + " FROM " + SQLiteHandler.SERVICE_CENTER_TABLE;
                break;
        }


        // Spinner Drop down elements for District
        List<SpinnerHelper> listAward = sqlH.getListAndID(SQLiteHandler.CUSTOM_QUERY, criteria, null, false);


        ArrayAdapter<SpinnerHelper> dataAdapter = new ArrayAdapter<SpinnerHelper>(this, R.layout.spinner_layout, listAward);

        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);

        spServiceCenter.setAdapter(dataAdapter);


        if (idSrvCenter != null) {
            for (int i = 0; i < spServiceCenter.getCount(); i++) {
                String serviceCenterName = spServiceCenter.getItemAtPosition(i).toString();

                if (serviceCenterName.equals(strServiceCenter)) {
                    position = i;
                }
            }
            spServiceCenter.setSelection(position);
        }


        spServiceCenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strServiceCenter = ((SpinnerHelper) spServiceCenter.getSelectedItem()).getValue();
                String fdpWithSrvCenterCode = ((SpinnerHelper) spServiceCenter.getSelectedItem()).getId();

                if (fdpWithSrvCenterCode.length() > 2) {
                    idFdpCode = fdpWithSrvCenterCode.substring(0, 3);
                    idSrvCenter = fdpWithSrvCenterCode.substring(3);


                    loadServiceMonth(cCode, donorCode, awardCode, idSrvCenter, idFdpCode);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    } // end Load Spinner


    /**
     * LOAD :: load Service Month
     */
    private void loadServiceMonth(final String cCode, final String donorCode, final String awardCode, final String SrvCenterCode, final String fdpCode) {

        int position = 0;
        String criteria;

        criteria = SQLiteQuery.getServiceMonths_WHERE_Service_Open_Condition(cCode);

        List<SpinnerHelper> listMonth = sqlH.getListAndID(SQLiteHandler.OP_MONTH_TABLE, criteria, null, false);
        listMonth.remove(0);

        ArrayAdapter<SpinnerHelper> dataAdapter = new ArrayAdapter<SpinnerHelper>(this, R.layout.spinner_layout, listMonth);

        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);

        spServiceMonth.setAdapter(dataAdapter);


        if (idServiceMonth != null) {
            Log.d("InSrv", "In Service moth spinner \n" + "idServiceMonth:" + idServiceMonth);
            for (int i = 0; i < spServiceMonth.getCount(); i++) {
                String month = spServiceMonth.getItemAtPosition(i).toString();
                if (month.equals(strSrvMonth)) {

                    position = i;
                    spServiceMonth.setSelection(position);

                    Log.d("InSrv", "In Service moth spinner \n" + "strSrvMonth:" + strSrvMonth +
                            " position :");


                }
            }

        }


        spServiceMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSrvMonth = ((SpinnerHelper) spServiceMonth.getSelectedItem()).getValue();
                idServiceMonth = ((SpinnerHelper) spServiceMonth.getSelectedItem()).getId();
                if (idServiceMonth.length() > 2) {

                    /** I don't want change the query Code that whys use sub string
                     *                   idCountry = idServiceMonth.substring(0, 4);
                     *                   donorId = idServiceMonth.substring(4, 6);
                     *                    awardId = idServiceMonth.substring(6, 8);
                     * */
                    idOpMonthCode = idServiceMonth.substring(8);
                    Log.d("In Service", " In the service month the fdpCode : " + fdpCode +
                            "\n private global veriable : idFdpCode :" + idFdpCode);
                    //loadDistributionType(idCountry, idDonor, idAward);
                    loadDistributionType(cCode, donorCode, awardCode, SrvCenterCode, idFdpCode, idOpMonthCode);

                }

                Log.d(TAG, "idServiceMonth : " + idServiceMonth + " strSrvMonth :" + strSrvMonth);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    } // end Load Spinner


    /**
     * ** LOAD:: DistributionType
     */
    private void loadDistributionType(final String cCode, final String donorCode, final String awardCode, final String srvCenterCode, final String fdpCode, final String srvMonthCode) {
        int position = 0;

        ArrayAdapter<CharSequence> adptMartial = ArrayAdapter.createFromResource(
                this, R.array.arrDistributionType, R.layout.spinner_layout);

        adptMartial.setDropDownViewResource(R.layout.spinner_layout);
        spDistributionType.setAdapter(adptMartial);


        if (idDistributionType != null) {
            for (int i = 0; i < spDistributionType.getCount(); i++) {
                String type = spDistributionType.getItemAtPosition(i).toString();
                if (type.equals(strDistType)) {
                    position = i;
                }
            }
            spDistributionType.setSelection(position);
        }


        /*** Experiments*/

        spDistributionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String type = parent.getItemAtPosition(position).toString();
                String foodFlagQuary = "";

                switch (type) {
                    case "None":
                        strDistType = "None";
                        idDistributionType = DistributionActivity.NONE;
                        foodFlagQuary = " AND " + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + SQLiteHandler.FOOD_FLAG + "= '0' "
                                + " AND " + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + SQLiteHandler.NON_FOOD_FLAG + "= '0' "
                                + " AND " + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + SQLiteHandler.CASH_FLAG + "= '0' "
                                + " AND " + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + SQLiteHandler.VOUCHER_FLAG + "= '0' ";
                        break;
                    case "Food":
                        strDistType = "Food";
                        idDistributionType = DistributionActivity.FOOD_TYPE;
                        foodFlagQuary = " AND " + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + SQLiteHandler.FOOD_FLAG + "= '1' ";
                        break;
                    case "Non Food":

                        strDistType = "Non Food";
                        idDistributionType = DistributionActivity.NON_FOOD_TYPE;
                        foodFlagQuary = " AND " + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + SQLiteHandler.NON_FOOD_FLAG + "= '1' ";
                        break;

                    case "Cash":
                        strDistType = "Cash";
                        idDistributionType = DistributionActivity.CASH_TYPE;
                        foodFlagQuary = " AND " + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + SQLiteHandler.CASH_FLAG + "= '1' ";
                        break;
                    case "Voucher":
                        strDistType = "Voucher";
                        idDistributionType = DistributionActivity.VOUCHER_TYPE;
                        foodFlagQuary = " AND " + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + SQLiteHandler.VOUCHER_FLAG + "= '1' ";
                        break;
                }

                loadServiceRecodeCriteria(cCode, donorCode, awardCode, srvCenterCode, idFdpCode, idOpMonthCode, foodFlagQuary);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * todo : Sending over the Spinner helper page
     * LOAD :: Criteria
     *
     * @param cCode         Country Code
     * @param donorCode     Donor Code
     * @param awardCode     award Code
     * @param srvCenterCode Service Center Code
     * @param fdpCode       food distribution point
     * @param srvMonthCode  service month Code
     */
    private void loadServiceRecodeCriteria(final String cCode, final String donorCode, final String awardCode, final String srvCenterCode, final String fdpCode, final String srvMonthCode, final String foodFlagTypeQuery) {

        int position = 0;
        String criteria = " SELECT " + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + SQLiteHandler.PROGRAM_CODE_COL
                + " ||  '-' || " + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + SQLiteHandler.SERVICE_CODE_COL +
                " AS criteriaId "
                + ", " + SQLiteHandler.ADM_PROGRAM_MASTER_TABLE + "." + SQLiteHandler.PROGRAM_SHORT_NAME_COL
                + " || '-' ||  " + SQLiteHandler.SERVICE_MASTER_TABLE + "." + SQLiteHandler.SERVICE_SHORT_NAME_COL + " AS Criteria"
                + " FROM " + SQLiteHandler.COUNTRY_PROGRAM_TABLE
                + " INNER JOIN " + SQLiteHandler.ADM_PROGRAM_MASTER_TABLE + " ON "
                + SQLiteHandler.ADM_PROGRAM_MASTER_TABLE + "." + SQLiteHandler.PROGRAM_CODE_COL + " = " + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + SQLiteHandler.PROGRAM_CODE_COL
                + " AND " + SQLiteHandler.ADM_PROGRAM_MASTER_TABLE + "." + SQLiteHandler.PROGRAM_CODE_COL + " = " + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + SQLiteHandler.PROGRAM_CODE_COL
                + " AND " + SQLiteHandler.ADM_PROGRAM_MASTER_TABLE + "." + SQLiteHandler.DONOR_CODE_COL + " = " + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + SQLiteHandler.DONOR_CODE_COL
                + " AND " + SQLiteHandler.ADM_PROGRAM_MASTER_TABLE + "." + AWARD_CODE_COL + " = " + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + AWARD_CODE_COL
                + " INNER JOIN " + SQLiteHandler.SERVICE_MASTER_TABLE + " ON "
                + SQLiteHandler.SERVICE_MASTER_TABLE + "." + SQLiteHandler.PROGRAM_CODE_COL + " = " + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + SQLiteHandler.PROGRAM_CODE_COL
                + " AND " + SQLiteHandler.SERVICE_MASTER_TABLE + "." + SQLiteHandler.SERVICE_CODE_COL + " = " + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + SQLiteHandler.SERVICE_CODE_COL
                + " WHERE "
                + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + COUNTRY_CODE_COL + " = '" + cCode + "' "
                +" AND "+ SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + DONOR_CODE_COL + " = '" + donorCode + "' "
                + " AND " + SQLiteHandler.COUNTRY_PROGRAM_TABLE + "." + AWARD_CODE_COL + " = '" + awardCode + "'"
                + foodFlagTypeQuery
                + " ORDER BY Criteria ";


        List<SpinnerHelper> listCriteria = sqlH.getListAndID(SQLiteHandler.CUSTOM_QUERY, criteria, null, false);
        ArrayAdapter<SpinnerHelper> dataAdapter = new ArrayAdapter<SpinnerHelper>(this, R.layout.spinner_layout, listCriteria);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spCriteria.setAdapter(dataAdapter);


        if (idCriteria != null) {
            for (int i = 0; i < spCriteria.getCount(); i++) {
                String award = spCriteria.getItemAtPosition(i).toString();
                if (award.equals(strCriteria)) {
                    position = i;
                }
            }
            spCriteria.setSelection(position);
        }


        spCriteria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strCriteria = ((SpinnerHelper) spCriteria.getSelectedItem()).getValue();
                idCriteria = ((SpinnerHelper) spCriteria.getSelectedItem()).getId();
                if (idCriteria.length() > 2) {
                    Log.d(TAG, "load servece data " + idCriteria);

                    idCountry = cCode;
                    idAward = awardCode;
                    idDonor = donorCode;
                    idProgram = idCriteria.substring(0, 3);
                    idService = idCriteria.substring(3);
                    idMemberSearch = "";

                    HashMap<String, String> mdateRange = sqlH.getDateRangeForService(idCountry, idOpMonthCode);
                    dateRange = sqlH.getDateRangeForService(idCountry, srvMonthCode);

                    strOpMonthLabel = mdateRange.get("opMonthLable");
                    idOpCode = mdateRange.get("opCode");


                    /**
                     * if the the voucher program  than service date & service center code must needed*/
                    String serviceDate = tv_srvDate.getText().toString();
                    if (serviceDate.equals("") || serviceDate.equals("yyyy-mm-dd") || serviceDate.equals("Date")) {


                        erroDialog.showErrorDialog(mContext, "Please select a Date ");
                    } else try {
                        dateRange = sqlH.getDateRangeForService(idCountry, idOpMonthCode);
                        String start_date = dateRange.get("sdate");
                        String end_date = dateRange.get("edate");
                        idOpCode = dateRange.get("opCode");//"opMCode"

                        strOpMonthLabel = dateRange.get("opMonthLable");//"opMCode"


                        if (serviceDate != null && start_date != null && end_date != null) {
                            if (!getValidDateRangeUSAFormat(serviceDate, start_date, end_date)) {

                                erroDialog.showErrorDialog(mContext, "Service date is not within the valid range. Save attempt denied");

                            } else {

                                loadGroupCategory(idCountry, idDonor, idAward, idProgram, serviceDate, srvCenterCode);

                            /*    LoadingList loadlist = new LoadingList(idCountry, idDonor, idAward, idProgram, idService, idMemberSearch, idOpMonthCode, strOpMonthLabel, idOpMonthCode, serviceDate, idSrvCenter);
                                loadlist.execute();*/
                            }

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    } // end Load Spinner

    /**
     * @param cCode         country Code
     * @param donorCode     Donor Code
     * @param awardCode     award Code
     * @param prgCode       Program Code
     * @param srvCode       Service Code
     * @param memSearchId   member Id for Search Version
     * @param opMonthLable  Service Operation Month Code
     * @param opCode        operation code
     * @param opMCode       operation month code
     * @param srvDate       service date
     * @param srvCenterCode Service Center Code
     * @param grpCode       GroupCod e
     *                      <p> this method </p>
     *                      todo:09-08-2016 implements graduatio date
     */


    public void loadServiceListView(final String cCode, String donorCode, String awardCode, String prgCode, String srvCode, String memSearchId, String opMonthLable,
                                    String opCode, String opMCode, String srvDate, String srvCenterCode
            , String grpCode) {

        List<ServiceDataModel> srvMemberList = null;
        String srvName;
        String progName;
        srvName = sqlH.getServiceShortName(prgCode, srvCode);
        progName = sqlH.getProgramShortName(awardCode, donorCode, prgCode);


        Log.d(TAG, "In load service List SrvName :" + srvName + " progName: " + progName);
        switch (srvName) {
            case C1:
            case C2:
            case C3:
            case FFA:
                switch (progName) {
                    case CFWS:
                    case CFWU:
                    case DRR:
                        tv_srvTitleCount.setText(R.string.wd);
                        srvMemberList = sqlH.getFFAMemberListForService(cCode, donorCode, awardCode, prgCode, srvCode, memSearchId, opCode, opMCode, grpCode, idDistributionType);
                        break;

                }
                break;
            default:
                // use variable to like operation
                srvMemberList = sqlH.getMemberListForService(cCode, donorCode, awardCode, prgCode, srvCode, memSearchId, opCode, opMCode, grpCode, idDistributionType);

                break;
        }

        if (srvMemberList != null) {
            Log.d(TAG, "srvMemberList size : " + srvMemberList.size());
            if (srvMemberList.size() != 0) {
                serviceArray.clear();
                for (ServiceDataModel data : srvMemberList) {
                    // add contacts data in arrayList
                    serviceArray.add(data);
                }
                Log.d(TAG, "serviceArray size : " + serviceArray.size() + "");
                adapter = new ServiceDataListAdapter(this, serviceArray, strAward, idCriteria, strCriteria, opMonthLable, opCode, opMCode, srvDate, srvCenterCode, progName, srvName);
         /*       *//**
                 * for the test perpose
                 *//*
                mListView.setAdapter(adapter);*/
            } else {
                // this statements clear the list view
                serviceArray.clear();
                adapter = new ServiceDataListAdapter(this, serviceArray, strAward, idCriteria, strCriteria, opMonthLable, opCode, opMCode, srvDate, srvCenterCode, progName, srvName);
            /*    *//**
                 * for the test perpose
                 *//*
                mListView.setAdapter(adapter);*/
            }
        } else {
            serviceArray.clear();
            adapter = new ServiceDataListAdapter(this, serviceArray, strAward, idCriteria, strCriteria, opMonthLable, opCode, opMCode, srvDate, srvCenterCode, progName, srvName);

        }

/**
 *  set adpater
 */

        if (adapter != null) {
            adapter.notifyDataSetChanged();
            mListView.setAdapter(adapter);
            /**
             * Notify the use no data available
             */
            if (adapter.getCount() == 0) {
                erroDialog.showInfromDialog(mContext, "No Data Found", "No Data found");
            }


        } else {
            erroDialog.showInfromDialog(mContext, "No Data Found", "No Data found");
        }


    }

    /**
     * This is a Back Ground Thread Class
     */
    private class LoadingList extends AsyncTask<Void, Integer, String> {
        private String countryCode, donorCode, awardCode, programCode,
                serviceCode, searchID, opMonthLabel, opCode, opMonthCode, srvCenterCode, tserviceDate, groupCode;

        public LoadingList(String countryCode, String donorCode, String awardCode,
                           String programCode, String serviceCode, String searchID, String opMonthLabel,
                           String opCode, String opMonthCode, String srvCenterCode, String tserviceDate, String groupCode) {
            this.countryCode = countryCode;
            this.donorCode = donorCode;
            this.awardCode = awardCode;
            this.programCode = programCode;
            this.serviceCode = serviceCode;
            this.searchID = searchID;
            this.opMonthLabel = opMonthLabel;
            this.opCode = opCode;
            this.opMonthCode = opMonthCode;
            this.srvCenterCode = srvCenterCode;
            this.tserviceDate = tserviceDate;
            this.groupCode = groupCode;


        }

        @Override
        protected String doInBackground(Void... params) {
            try {


                loadServiceListView(countryCode, donorCode, awardCode, programCode
                        , serviceCode, searchID, idOpMonthCode, opMonthLabel, opMonthCode
                        , tserviceDate, srvCenterCode, groupCode);

            } catch (Exception e) {
                pDialog.dismiss();
                return "UNKNOWN";
            }
            return "sucess";


            //return "";
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startProgressBar("Data is Loading.");

        }


        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            pDialog.dismiss();

            // if the selected program is  for voucher tha this view will gone
            if (sqlH.checkAdmCountryProgramsVoucherFlag(idCountry, idDonor, idAward, idProgram)) {
                btnSave.setVisibility(View.GONE);
            } else {
                btnSave.setVisibility(View.VISIBLE);
            }

            //   mListView.setFocusableInTouchMode(true);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
                mListView.setAdapter(adapter);
                /**
                 * Notify the use no data available
                 */
                if (adapter.getCount() == 0) {
                    erroDialog.showInfromDialog(mContext, "No Data Found", "No Data found");
                }


            } else {
                erroDialog.showInfromDialog(mContext, "No Data Found", "No Data found");
            }


        }
    }

    private void startProgressBar(String msg) {


        pDialog.setMessage(msg);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        pDialog.show();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * Here  is the sub class  Adapter class
     */

    public class ServiceDataListAdapter extends BaseAdapter {
        private Activity activity;
        /**
         * inflater Convert XmL into Java Object
         */

        private LayoutInflater inflater;
        ArrayList<ServiceDataModel> servicedData;// = new ArrayList<ServiceDataModel>();
        private SQLiteHandler sqlH = null;
        private final String TAG = "ServiceDataListAdapter";

        private String awardName;
        private String villageName; // todo:remove the Village  Name
        private String CriteriaName;
        private String criteriaId;
        private String opMonthStr;
        private HashMap<String, String> dateRange;

        private String opCode;
        private String opMonthCode;
        private String srvCenterCode;
        private String srvDate;
        /**
         * To Control the rows of List need the Program Short name
         */
        private String progShortName;
        private String srvShortName;

        /**
         * @param activity      The Parent Activity
         * @param servicedData  Service Date
         * @param awardName     Awaer Name
         * @param criteriaId    Criteria Id (Program Code+ Service Code )
         * @param CriteriaName  Criteria Name (Program Name+ Service Name )
         * @param opMonthStr    Service Operation Month String
         * @param opCode        operation Code
         * @param opMonthCode   Service Operation Mode
         * @param srvDate       Service Date
         * @param srvCenterCode Service Center Code
         */


        public ServiceDataListAdapter(Activity activity, ArrayList<ServiceDataModel> servicedData, String awardName, String criteriaId,
                                      String CriteriaName, String opMonthStr, String opCode, String opMonthCode, String srvDate, String srvCenterCode, String progShortName, String srvShortName) {
            this.activity = activity;
            this.servicedData = servicedData;
            sqlH = new SQLiteHandler(activity);
            dateRange = sqlH.getDateRangeForService(idCountry, idServiceMonth);
            this.awardName = awardName;
            this.CriteriaName = CriteriaName;
            this.criteriaId = criteriaId;
            this.opMonthStr = opMonthStr;
            this.opCode = opCode;
            this.opMonthCode = opMonthCode;
            this.srvDate = srvDate;
            this.srvCenterCode = srvCenterCode;
            this.progShortName = progShortName;
            this.srvShortName = srvShortName;


        }


        @Override
        public int getCount() {

            count = servicedData.size();
            return count;

        }

        /**
         * @param position the Index of the row in list
         * @return the Service Object
         */

        @Override
        public Object getItem(int position) {
            return servicedData.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        viewHolder holder;

        /**
         * @param position    index
         * @param convertView
         * @param parent
         * @return the Custom View of row
         */

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ServiceDataModel personToBeServiced = servicedData.get(position);

            View row = convertView;


            if (inflater == null)
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            if (convertView == null) {
                row = inflater.inflate(R.layout.list_row_service, null);
                holder = new viewHolder();
                holder.tv_newMemId = (TextView) row.findViewById(R.id.srv_row_newId);
                holder.tv_memName = (TextView) row.findViewById(R.id.srv_row_member_name);
                holder.tv_countORwd = (TextView) row.findViewById(R.id.srv_row_tv_criteriaView);
                holder.imgEdit = (ImageButton) row.findViewById(R.id.ibtn_edit_service_holder);
                holder.imgVoucher = (ImageButton) row.findViewById(R.id.ibtn_voucher_service_holder);
                holder.imgSrvSpecific = (ImageButton) row.findViewById(R.id.ibtn_service_spacific_holder);

                row.setTag(holder);

            } else {
                holder = (viewHolder) row.getTag();
            }


            String criteria = SQLiteQuery.getVillageNameWHERECondition(personToBeServiced.getC_code(), personToBeServiced.getDistrictCode(), personToBeServiced.getUpazillaCode(), personToBeServiced.getUnitCode(), personToBeServiced.getVillageCode());


            villageName = sqlH.getVillageName(criteria);
            personToBeServiced.setVillageName(villageName);
            personToBeServiced.setAwardName(awardName);
            personToBeServiced.setCriteriaName(CriteriaName);
            personToBeServiced.setOpMonthStr(opMonthStr);
            personToBeServiced.setServiceSLCode(padding(personToBeServiced.getGetSrvMemCount()));

            personToBeServiced.setOpCode("2"); // set opCode to service
            personToBeServiced.setOpMontheCode(opMonthCode);
            personToBeServiced.setCriteriaId(criteriaId);
            personToBeServiced.setFPDCode(idFdpCode);
            Log.d("Adapter", "in Adapter the idFdpCode :" + idFdpCode);

            /* **************ADDING CONTENTS**************** */

            holder.tv_newMemId.setText(personToBeServiced.getNewID());  // Registration ID or Holding ID
            //  holder.mmID.setText(personToBeServiced.getMemberId());
            holder.tv_memName.setText(personToBeServiced.getHh_mm_name());
            switch (srvShortName) {
                case C1:
                case C2:
                case C3:
                case FFA:
                    switch (progShortName) {
                        case CFWS:
                        case "CFWU":
                        case DRR:
                            holder.tv_countORwd.setText(personToBeServiced.getWorkingDay());
                            break;

                    }
                    break;
                default:
                    int c = Integer.parseInt(personToBeServiced.getGetSrvMemCount());
                    holder.tv_countORwd.setText(String.valueOf(c));
                    break;
            }


            // check box reference is define here
            CheckBox cbId_holder = (CheckBox) row.findViewById(R.id.cb_srv_id_holde);
            // set checked change listener
            cbId_holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    /**
                     *  Saving Checked Position
                     */
                    mChecked.put(position, isChecked);

                    /**
                     * Find if all the check boxes are true
                     */
                    if (isAllValuesChecked()) {

                                    /*
                                     * set HeaderCheck box to true
                                     */
                        checkBox_header.setChecked(isChecked);
                    }

                    /** get the object of sepecific row & set the value of the
                     * save the chekbox of that particular state
                     */
                    getServicedPerson((Integer) buttonView.getTag()).setCheckBox(isChecked);
                    /** if the selected program is  for voucher tha staff will not save the
                     *  benificary service normally by check box
                     *
                     */

                    /** old state*/
                    addDataToArrayList(getServicedPerson((Integer) buttonView.getTag()),
                            getServicedPerson((Integer) buttonView.getTag()).isCheckBox());


                }
            });
            // set the sate of particular positioned check box
            cbId_holder.setTag(position);
            // than set the checked sate

            /**
             * Set CheckBox "TRUE" or "FALSE" if mChecked == true
             */
            cbId_holder.setChecked((mChecked.get(position)));
//            Log.d(TAG, " position " + position + " the check box  is svaved " + cbId_holder.isChecked());


            /**
             * necessary setups
             */
            personToBeServiced.setServiceCenterCode(srvCenterCode);
            // // TODO: jekono ekta komate hobe
            personToBeServiced.setServiceDTCode(srvDate);
            personToBeServiced.setTemServiceDate(srvDate);
            personToBeServiced.setTemServiceCenterName(strServiceCenter);
            personToBeServiced.setServiceCenterCode(idSrvCenter);
            personToBeServiced.setTemServiceDate(tv_srvDate.getText().toString());
            personToBeServiced.setOpMonthStr(strOpMonthLabel);
            personToBeServiced.setTemIdServiceMonth(idOpMonthCode);
            personToBeServiced.setTemStrSrvMonth(strSrvMonth);
            personToBeServiced.setTemIdGroup(idGroup);
            personToBeServiced.setTemStrGroup(strGroup);
            personToBeServiced.setTemIdGroupCat(idGroupCat);
            personToBeServiced.setTemStrGroupCat(strGroupCat);
            personToBeServiced.setDistFlag(idDistributionType);

/**
 * This Button bring To Service Record Page
 */
            holder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                    Intent intent = new Intent(activity, ServiceRecord.class);
                    personToBeServiced.setTemServiceCenterName(strServiceCenter);
                    personToBeServiced.setServiceCenterCode(idSrvCenter);
                    personToBeServiced.setTemServiceDate(tv_srvDate.getText().toString());
                    personToBeServiced.setOpMonthStr(strOpMonthLabel);
                    intent.putExtra(KEY.SERVICE_DATA_OBJECT_KEY, personToBeServiced);
                    activity.startActivity(intent);
                }
            });


            // if the selected program is not for voucher tha this view will gone
            // todo
            if (!sqlH.checkAdmCountryProgramsVoucherFlag(idCountry, idDonor, idAward, idProgram)) {
                holder.imgVoucher.setVisibility(View.GONE);
            }

            if (!sqlH.checkCriteriaServiceSpecificFlag(idCountry, idDonor, idAward, idProgram, idService)) {
                holder.imgSrvSpecific.setVisibility(View.GONE);
            }

            holder.imgVoucher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(activity, ServiceVoucherDetails.class);

                    // send the Service data object to the ServiceVoucherDetails
                    intent.putExtra(KEY.SERVICE_DATA_OBJECT_KEY, personToBeServiced);
                    activity.finish();
                    activity.startActivity(intent);
                }
            });


            holder.imgSrvSpecific.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent iSrvSpec = new Intent(activity, ServiceSpecification.class);
                    //activity.finish(); if any change needed in ServiceActivity

                    iSrvSpec.putExtra(KEY.SERVICE_DATA_OBJECT_KEY, personToBeServiced);

                    activity.finish();
                    activity.startActivity(iSrvSpec);
                }
            });


            if (position % 2 == 0) {
                row.setBackgroundColor(Color.WHITE);
                changeTextColor(activity.getResources().getColor(R.color.blue));
            } else {
                row.setBackgroundColor(activity.getResources().getColor(R.color.list_divider));
                changeTextColor(activity.getResources().getColor(R.color.black));
            }

            return row;
        }

        private void changeTextColor(int color) {

            holder.tv_newMemId.setTextColor(color);
            holder.tv_memName.setTextColor(color);
            holder.tv_countORwd.setTextColor(color);
        }


        ServiceDataModel getServicedPerson(int position) {
            return (ServiceDataModel) getItem(position);
        }


        public boolean isArrayListNull() {
            if (listOFWant2Save.isEmpty())
                return true;
            else
                return false;


        }

        private ArrayList<ServiceDataModel> listOFWant2Save = new ArrayList<ServiceDataModel>();

        private void addDataToArrayList(ServiceDataModel s, boolean chackBoxStatus) {
            if (chackBoxStatus) {
                listOFWant2Save.add(s);
            } else {
                if (listOFWant2Save.contains(s)) {// first check the data is exits ing or not
                    listOFWant2Save.remove(s);
                }

            }

        }

        public ArrayList<ServiceDataModel> getArrayList() {
            return listOFWant2Save;
        }


        class viewHolder {
            TextView tv_newMemId;
            TextView tv_memName;
            /**
             * tv_countORwd will show the no of Service that Member Get And no of Working Days
             * depend upon Program
             */
            TextView tv_countORwd;
            ImageButton imgEdit;
            ImageButton imgVoucher;
            ImageButton imgSrvSpecific;

        }

        /*
         * Find if all values are checked.
         */
        protected boolean isAllValuesChecked() {

            for (int i = 0; i < count; i++) {
                if (!mChecked.get(i)) {
                    return false;
                }
            }

            return true;
        }


    }// end of  adapter


}
