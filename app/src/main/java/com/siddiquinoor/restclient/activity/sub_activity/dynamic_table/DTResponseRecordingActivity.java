package com.siddiquinoor.restclient.activity.sub_activity.dynamic_table;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.siddiquinoor.restclient.R;
import com.siddiquinoor.restclient.data_model.DTQResModeDataModel;
import com.siddiquinoor.restclient.data_model.DTResponseTableDataModel;
import com.siddiquinoor.restclient.data_model.DT_ATableDataModel;
import com.siddiquinoor.restclient.fragments.BaseActivity;
import com.siddiquinoor.restclient.manager.SQLiteHandler;
import com.siddiquinoor.restclient.manager.sqlsyntax.SQLServerSyntaxGenerator;
import com.siddiquinoor.restclient.utils.KEY;
import com.siddiquinoor.restclient.views.adapters.DynamicDataIndexDataModel;
import com.siddiquinoor.restclient.views.adapters.DynamicTableQuesDataModel;
import com.siddiquinoor.restclient.views.helper.SpinnerHelper;
import com.siddiquinoor.restclient.views.notifications.ADNotificationManager;
import com.siddiquinoor.restclient.views.spinner.SpinnerLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class DTResponseRecordingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    public static final String TEXT = "Text";
    public static final String NUMBER = "Number";
    public static final String Date = "Date";
    public static final String Date_OR_Time = "Datetime";
    public static final String Textbox = "Textbox";
    public static final String COMBO_BOX = "Combobox";
    public static final String GEO_LAYER_3 = "Geo Layer 3";

    public static final String GEO_LAYER_2 = "Geo Layer 2";
    public static final String GEO_LAYER_1 = "Geo Layer 1";
    public static final String GEO_LAYER_4 = "Geo Layer 4";
    public static final String GEO_LAYER_ADDRESS = "Geo Layer Address";
    public static final String SERVICE_SITE = "Service Site";
    public static final String DISTRIBUTION_POINT = "Distribution Point";
    public static final String COMMUNITY_GROUP = "Community Group";
    public static final String ORGANIZATION_LIST = "Organization List";
    public static final String CHECK_BOX = "Checkbox";
    public static final String RADIO_BUTTON = "Radio Button";
    public static final String DATE_TIME = "Datetime";
    public static final String PHOTO = "Photo";
    public static final String DATE = "Date";
    public static final String RADIO_BUTTON_N_TEXTBOX = "Radio Button, Textbox";
    public static final String CHECKBOX_N_TEXTBOX = "Checkbox, Textbox";
    public static final String LOOKUP_LIST = "Lookup List";
    /**
     * Database helper
     */
    private SQLiteHandler sqlH;
    /**
     * alert Dialog
     */
    private ADNotificationManager dialog;
    private final Context mContext = DTResponseRecordingActivity.this;
    private DynamicDataIndexDataModel dyIndex;
    private int totalQuestion;
    private TextView tv_DtQuestion;
    private Button btnNextQues;
    private Button btnHome;
    private DynamicTableQuesDataModel mDTQ;
    private Button btnPreviousQus;


    int mQusIndex;
    /**
     * For Date time picker
     */
    private SimpleDateFormat mFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
    private Calendar calendar = Calendar.getInstance();
    /**
     * Dynamic view
     */
    private Spinner dt_spinner;
    private EditText dt_edt;
    private TextView _dt_tv_DatePicker;
    private RadioGroup radioGroup;
//    private RadioButton rdbtn;
    /**
     * To determined the either any Check box is Selected or nor
     */
    private int countChecked = 0;

    private String idSpinner;
    private String strSpinner;


    /**
     * DTQResMode
     */
    DTQResModeDataModel mDTQResMode;

    /**
     * #mDTATable is Deliminator of Check Box item &  value
     * it is assigned by {@link #displayQuestion(DynamicTableQuesDataModel)} method
     */
    List<DT_ATableDataModel> mDTATableList;
    /**
     * List for Dynamic
     */

    private List<RadioButton> mRadioButton_List = new ArrayList<RadioButton>();

    private List<CheckBox> mCheckBox_List = new ArrayList<CheckBox>();

    /**
     * we use global for
     */
    SQLServerSyntaxGenerator mSyntaxGenerator = new SQLServerSyntaxGenerator();

    private RadioGroup radioGroupForRadioAndEditText;
    // private LinearLayout llRadioGroupAndEditText;
    private List<RadioButton> mRadioButtonForRadioAndEdit_List = new ArrayList<RadioButton>();
    private List<EditText> mEditTextForRadioAndEdit_List = new ArrayList<EditText>();
    private List<EditText> mEditTextForCheckBoxAndEdit_List = new ArrayList<EditText>();
    private List<CheckBox> mCheckBoxForCheckBoxAndEdit_List = new ArrayList<CheckBox>();

    private LinearLayout dt_layout_Radio_N_EditText;


    /**
     * Layout
     */
    private LinearLayout parent_layout_onlyFor_CB;
    private LinearLayout parent_layout_FOR_CB_N_ET;
    /**
     * This layout is child of
     * {@link #parent_layout_FOR_CB_N_ET}
     */
    private LinearLayout subParent_CB_layout_FOR_CB_N_ET;
    /**
     * This layout is child of
     * {@link #parent_layout_FOR_CB_N_ET}
     */
    private LinearLayout subParent_ET_layout_FOR_CB_N_ET;

    private LinearLayout ll_editText;
//    private TextView tv_ErrorDisplay;

    /**
     * mDTResponse Sequence  DTRSeq
     */
    private int mDTRSeq;
    private int surveyNumber;


    /**
     * @param sIState savedInstanceState
     */

    @Override
    protected void onCreate(Bundle sIState) {
        super.onCreate(sIState);
        setContentView(R.layout.activity_dt__qustion);
        inti();

        setListener();


    }


    private void setListener() {
        btnNextQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProcessValidation();
            }
        });
        btnPreviousQus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousQuestion();
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHomeWithDialog();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void addIconHomeButton() {
        btnHome.setText("");
        Drawable imageHome = getResources().getDrawable(R.drawable.home_b);
        btnHome.setCompoundDrawablesRelativeWithIntrinsicBounds(imageHome, null, null, null);
        setPaddingButton(mContext, imageHome, btnHome);


    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void addNextOrPreviousButton(Button button) {
        button.setText("");
        Drawable imageHome;
        if (button == btnNextQues)
            imageHome = getResources().getDrawable(R.drawable.goto_forward);
        else
            imageHome = getResources().getDrawable(R.drawable.goto_back);

        button.setCompoundDrawablesRelativeWithIntrinsicBounds(imageHome, null, null, null);
        setPaddingButton(mContext, imageHome, button);


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
        addIconHomeButton();
        addNextOrPreviousButton(btnNextQues);
        addNextOrPreviousButton(btnPreviousQus);
    }

    /**
     * {@link #deleteFromResponseTable()}
     */
    private void goToHomeWithDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        /**
         *  in unfinished condition if anyone press home button
         *  Setting Dialog Title
         */

        alertDialog.setTitle("Home");

        String massage;
        if (mQusIndex < totalQuestion) {

            massage = "Your response is incomplete.\nDo you want to quit ?";
            // On pressing Settings button
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    confirmationDialog();

                }
            });

        } else {

            massage = " Do you want to go to Home page ?";


            // On pressing Settings button
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    goToMainActivity((Activity) mContext);

                }
            });

        }

        // Setting Dialog Message
        alertDialog.setMessage(massage);

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();


    }


    private void confirmationDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        /**
         *  in unfinished condition if anyone press home button
         *  Setting Dialog Title
         */

        alertDialog.setTitle("Home");

        String massage;


        massage = "Incomplete response will be deleted!!";
        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteFromResponseTable();
                goToMainActivity((Activity) mContext);

            }
        });


        // Setting Dialog Message
        alertDialog.setMessage(massage);

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();


    }

    /**
     * Change The Color of Question  to Indicate the Eror
     */

    private void errorIndicator() {
        tv_DtQuestion.setTextColor(getResources().getColor(R.color.red));
    }

    private void normalIndicator() {
        tv_DtQuestion.setTextColor(getResources().getColor(R.color.blue_dark));
    }

    /**
     * Check All type of Validation For
     */

    private void saveProcessValidation() {

        int i = 0;
        String responseControl = mDTQResMode.getDtResponseValueControl();


        if (mDTQ.getAllowNullFlag().equals("N")) {

            switch (responseControl) {
                case Textbox:

                    String edtInput = dt_edt.getText().toString();

                    if (edtInput.equals("Text") || edtInput.equals("Number") || edtInput.length() == 0) {

                        errorIndicator();
                        displayError("Insert  Text");


                    } else {
                        normalIndicator();
                        saveData(edtInput, mDTATableList.get(0));
                        // NEXT QUESTION

                        nextQuestion();

                    }

                    break;


                case Date_OR_Time:

                    if (_dt_tv_DatePicker.getText().toString().equals("Click for Date")) {

                        errorIndicator();
                        displayError("Set Date First");

                    } else {
                        normalIndicator();

                        /**                         * mDTATableList.get(0) wil be single                         */
                        saveData(_dt_tv_DatePicker.getText().toString(), mDTATableList.get(0));
                        nextQuestion();
                    }


                    break;
                case COMBO_BOX:
                    /** here it get null point reference if spinner get no values */
                    if (idSpinner != null) {
                        if (idSpinner.equals("00")) {

                            errorIndicator();
                            displayError("Select Item");

                        } else {
                            normalIndicator();
                            /**                             * {@link DTResponseRecordingActivity#saveData(String, DT_ATableDataModel)}                             */
                            saveData(strSpinner, mDTATableList.get(0));

                            /**                         * NEXT QUESTION                         */
                            nextQuestion();
                        }
                    }


                    break;
                case CHECK_BOX:

                    if (countChecked <= 0) {

                        errorIndicator();
                        displayError("Select a option.");

                    } else {
                        normalIndicator();

                        i = 0;
                        for (CheckBox cb : mCheckBox_List) {
                            if (cb.isChecked()) {

                                saveData("", mDTATableList.get(i));
                            }// end of if condition
                            i++;
                        }// end of for each loop
                    }// end of else

                    nextQuestion();
                    break;

                case RADIO_BUTTON:
                    i = 0;
                    for (RadioButton rb : mRadioButton_List) {
                        if (rb.isChecked()) {

                            saveData("", mDTATableList.get(i));
                        }
                        i++;
                    }
                    nextQuestion();
                    break;


                case RADIO_BUTTON_N_TEXTBOX:

                    boolean error = false;

                    i = 0;
                    for (RadioButton rb : mRadioButtonForRadioAndEdit_List) {
                        if (rb.isChecked()) {

                            if (mEditTextForRadioAndEdit_List.get(i).getText().length() == 0) {
                                errorIndicator();
                                displayError("Insert value for Selected Option");
                                error = true;
                                break;

                            } else {
                                normalIndicator();

                                saveData(mEditTextForRadioAndEdit_List.get(i).getText().toString(), mDTATableList.get(i));
                            }


                        }
                        i++;
                    }

                    if (!error)
                        nextQuestion();
                    break;

                case CHECKBOX_N_TEXTBOX:


                    normalIndicator();
                    int k = 0;
                    for (CheckBox cb : mCheckBoxForCheckBoxAndEdit_List) {
                        if (cb.isChecked()) {
                            Toast.makeText(mContext, "Radio Button no:" + (k + 1) + " is checked"
                                    + " the value of the : " + mEditTextForCheckBoxAndEdit_List.get(k).getText(), Toast.LENGTH_SHORT).show();
                            if (mEditTextForCheckBoxAndEdit_List.get(k).getText().length() == 0) {
                                errorIndicator();
                                displayError("Insert value for Selected Option");
                                break;
                            } else {
                                normalIndicator();


                                saveData(mEditTextForCheckBoxAndEdit_List.get(k).getText().toString(), mDTATableList.get(k));
                            }


                        }
                        k++;
                    }
                    nextQuestion();
                    break;


            }// end of switch

        }// end of the AllowNullFlag
        else {


            /**
             *  TODO: 9/29/2016  save method & update method
             */
//                    saveData("");


            /**
             * NEXT QUESTION
             */
            nextQuestion();


        }// end of else where ans is not magneto


    }


    /**
     * @param errorMsg Massage In valid
     */

    private void displayError(String errorMsg) {


        dialog.showWarningDialog(mContext, errorMsg);


    }

    private void saveData(String ansValue, DT_ATableDataModel dtATable) {
        Log.e("SAVE_DATA", ansValue + "  " + dtATable.getDt_AValue());
        saveOnResponseTable(ansValue, dtATable);
    }

    /**
     * @param ansValue user input
     * @param dtATable DTA Table
     */

    private void saveOnResponseTable(String ansValue, DT_ATableDataModel dtATable) {


        String DTBasic = dyIndex.getDtBasicCode();
        String AdmCountryCode = dyIndex.getcCode();
        String AdmDonorCode = dyIndex.getDonorCode();
        String AdmAwardCode = dyIndex.getAwardCode();
        String AdmProgCode = dyIndex.getProgramCode();
        String DTEnuID = getStaffID();

        String DTQCode = mDTQ.getDtQCode();
        String DTQText = mDTQ.getqText();
        String DTACode = dtATable.getDt_ACode();
        /**    DTRSeq is user input serial no         */
        int DTRSeq = mDTRSeq;
        String DTAValue = null;
        String ProgActivityCode = dyIndex.getProgramActivityCode();
        String DTTimeString = null;
        try {
            DTTimeString = getDateTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String OpMode = dyIndex.getOpMode();
        String OpMonthCode = dyIndex.getOpMonthCode();
        /**
         * todo : set the  DT Q data type
         */
        String DataType = dtATable.getDataType();


        DTAValue = dtATable.getDt_AValue().equals("null") || dtATable.getDt_AValue().length() == 0 ? ansValue : dtATable.getDt_AValue();


      /*  Log.d("MOR_1", " for save process"
                + "\n DTBasic          : " + DTBasic
                + "\n AdmCountryCode   : " + AdmCountryCode
                + "\n AdmDonorCode     : " + AdmDonorCode
                + "\n AdmAwardCode     : " + AdmAwardCode
                + "\n AdmProgCode      : " + AdmProgCode
                + "\n DTEnuID          : " + DTEnuID
                + "\n DTQCode          : " + DTQCode
                + "\n DTACode          : " + DTACode
                + "\n DTRSeq           : " + DTRSeq
                + "\n DTAValue         : " + DTAValue
                + "\n ProgActivityCode : " + ProgActivityCode
                + "\n DTTimeString     : " + DTTimeString
                + "\n OpMode           : " + OpMode
                + "\n OpMonthCode      : " + OpMonthCode
                + "\n DataType         : " + DataType
                + "\n DTQText          : " + DTQText
                + "\n SurveyNum        : " + surveyNumber
        );*/

/**
 * todo set upload syntax variable here
 */


        mSyntaxGenerator.setDTBasic(DTBasic);
        mSyntaxGenerator.setAdmCountryCode(AdmCountryCode);
        mSyntaxGenerator.setAdmDonorCode(AdmDonorCode);
        mSyntaxGenerator.setAdmAwardCode(AdmAwardCode);
        mSyntaxGenerator.setAdmProgCode(AdmProgCode);
        mSyntaxGenerator.setDTEnuID(DTEnuID);
        mSyntaxGenerator.setDTQCode(DTQCode);
        mSyntaxGenerator.setDTACode(DTACode);
        mSyntaxGenerator.setDTRSeq(String.valueOf(mDTRSeq));
        mSyntaxGenerator.setDTAValue(DTAValue);
        mSyntaxGenerator.setProgActivityCode(ProgActivityCode);
        mSyntaxGenerator.setDTTimeString(DTTimeString);
        mSyntaxGenerator.setOpMode(OpMode);
        mSyntaxGenerator.setOpMonthCode(OpMonthCode);
        mSyntaxGenerator.setDataType(DataType);
        mSyntaxGenerator.setCompleteness("Y");


        /**
         * main execute
         * Insert or update operation
         */
        if (sqlH.isDataExitsInDTAResponse_Table(DTBasic, AdmCountryCode, AdmDonorCode, AdmAwardCode, AdmProgCode, DTEnuID, DTQCode, DTACode, mDTRSeq)) {
            sqlH.updateIntoDTResponseTable(DTBasic, AdmCountryCode, AdmDonorCode, AdmAwardCode, AdmProgCode, DTEnuID, DTQCode, DTACode,
                    String.valueOf(DTRSeq), DTAValue, ProgActivityCode, DTTimeString, OpMode, OpMonthCode, DataType);
            sqlH.updateIntoDTSurveyTable(DTBasic, AdmCountryCode, AdmDonorCode, AdmAwardCode, AdmProgCode, DTEnuID, DTQCode, DTACode,
                    String.valueOf(DTRSeq), DTAValue, ProgActivityCode, DTTimeString, OpMode, OpMonthCode, DataType, DTQText, surveyNumber);
            /**
             *  upload syntax
             */
            sqlH.insertIntoUploadTable(mSyntaxGenerator.updateIntoDTResponseTable());
        } else {

            sqlH.addIntoDTResponseTable(DTBasic, AdmCountryCode, AdmDonorCode, AdmAwardCode, AdmProgCode, DTEnuID, DTQCode, DTACode,
                    String.valueOf(DTRSeq), DTAValue, ProgActivityCode, DTTimeString, OpMode, OpMonthCode, DataType);
            sqlH.addIntoDTSurveyTable(DTBasic, AdmCountryCode, AdmDonorCode, AdmAwardCode, AdmProgCode, DTEnuID, DTQCode, DTACode,
                    String.valueOf(DTRSeq), DTAValue, ProgActivityCode, DTTimeString, OpMode, OpMonthCode, DataType, DTQText, surveyNumber);
            /**
             *  upload syntax
             */

            sqlH.insertIntoUploadTable(mSyntaxGenerator.insertIntoDTResponseTable());
        }


    }


    /**
     * delete the unfinished data form the
     */

    private void deleteFromResponseTable() {


        String DTBasic = dyIndex.getDtBasicCode();
        String AdmCountryCode = dyIndex.getcCode();
        String AdmDonorCode = dyIndex.getDonorCode();
        String AdmAwardCode = dyIndex.getAwardCode();
        String AdmProgCode = dyIndex.getProgramCode();
        String OpMode = dyIndex.getOpMode();
        String getOpMonthCode = dyIndex.getOpMonthCode();
        String DTEnuID = getStaffID();
        int DTRSeq = mDTRSeq;
        /**
         *  total Question no is less then index no
         *  the Delete Syntax in  the {@link SQLiteHandler#deleteFromDTResponseTable(String, String, String, String, String, String, int, String, String)}
         *
         */


        if (DTBasic != null && AdmCountryCode != null && AdmDonorCode != null && AdmAwardCode != null
                && AdmProgCode != null && DTEnuID != null && DTRSeq != 0) {
            sqlH.deleteFromDTResponseTable(DTBasic, AdmCountryCode, AdmDonorCode, AdmAwardCode, AdmProgCode, DTEnuID, DTRSeq, OpMode, getOpMonthCode);
        }
    }

    /**
     * Load the next Question
     */

    private void nextQuestion() {
        ++mQusIndex;

        //to check does index exceed the max value

        if (mQusIndex != totalQuestion)
            hideViews();


        if (mQusIndex < totalQuestion) {
            DynamicTableQuesDataModel nextQus = loadNextQuestion(dyIndex.getDtBasicCode(), mQusIndex);

            displayQuestion(nextQus);
            // set arrow icon instead of stop icon . set previous  state
            removeStopIconNextButton(btnPreviousQus);
            // no need to delete bellow code
            //   if (mQusIndex == totalQuestion - 1) {
            //   addSaveIconButton(btnNextQues);
            //   }


        } else if (mQusIndex == totalQuestion) {

            mSyntaxGenerator.setDtShortName(dyIndex.getDtShortName());
            Log.d("sql", mSyntaxGenerator.sqlSpDTShortName_Save());

            sqlH.insertIntoUploadTable(mSyntaxGenerator.sqlSpDTShortName_Save());
            Toast.makeText(mContext, "Saved Successfully", Toast.LENGTH_SHORT).show();

            /// Bellow Code end the
            addStopIconButton(btnNextQues);
      /*      if (surveyCompletness) {
                surveyCompletness
            }
*/

        } else if (mQusIndex > totalQuestion) {
            // new set of ans .set icon next button
            continueDialog();

        }
    }

    private void continueDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        /**
         *  in unfinished condition if anyone press home button
         *  Setting Dialog Title
         */

        alertDialog.setTitle("Continue !!");

        String massage;


        massage = "Do you want to continue ?";
        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                removeStopIconNextButton(btnNextQues);
                initialWithFirstQues();


            }
        });


        // Setting Dialog Message
        alertDialog.setMessage(massage);

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                goToMainActivity((Activity) mContext);
            }
        });

        // Showing Alert Message
        alertDialog.show();


    }

    /**
     * load previous Question
     */

    private void previousQuestion() {
        --mQusIndex;
        hideViews();
        /**
         * to check does index exceed the min value
         */
        if (mQusIndex >= 0) {
            DynamicTableQuesDataModel nextQus = loadPreviousQuestion(dyIndex.getDtBasicCode(), mQusIndex);

            displayQuestion(nextQus);


            if (mQusIndex == 0)
                addStopIconButton(btnPreviousQus);

        } else if (mQusIndex < 0) {
            mQusIndex = 0;

        }
    }

    /**
     * @param qusObject DTQTable object
     *                  {@link #mDTATableList} must be assigned before invoking {@link #loadDT_QResMode(String)}
     *                  {@link #mDTATableList} needed in {@link #saveData(String, DT_ATableDataModel)} (String)} method
     */

    private void displayQuestion(DynamicTableQuesDataModel qusObject) {
        tv_DtQuestion.setText(qusObject.getqText());
        mDTATableList = sqlH.getDTA_Table(qusObject.getDtBasicCode(), qusObject.getDtQCode());
        /**
         * {@link #mDTATableList} if it's size is zero than there will be IndexOutOfBoundsException
         * occur
         * the poxy data prevent to occur that Exception
         */
        if (mDTATableList.size() == 0) {
            DT_ATableDataModel proxyDATA_data = new DT_ATableDataModel(mDTQ.getDtBasicCode(), mDTQ.getDtQCode(), "null", "No Recoded in DB", "null", "null", "null", "null", "null", "null", "N", 0, 0, "Text", "null");
            mDTATableList.add(proxyDATA_data);
        }


        loadDT_QResMode(qusObject.getqResModeCode());


    }

    /**
     * initial state
     * also views refer
     *
     * @see :{@link #viewReference()}
     */

    private void inti() {
        viewReference();
        sqlH = new SQLiteHandler(mContext);

        dialog = new ADNotificationManager();
        Intent intent = getIntent();
        dyIndex = intent.getParcelableExtra(KEY.DYNAMIC_INDEX_DATA_OBJECT_KEY);
        totalQuestion = intent.getIntExtra(KEY.DYNAMIC_T_QUES_SIZE, 0);

        initialWithFirstQues();


    }

    private void initialWithFirstQues() {
        surveyNumber = sqlH.getSurveyNumber(dyIndex.getDtBasicCode(), dyIndex.getcCode(), dyIndex.getDonorCode(), dyIndex.getAwardCode(), dyIndex.getProgramCode(), getStaffID());
        mQusIndex = 0;
        mDTRSeq = sqlH.getNextDTResponseSequence(dyIndex.getDtBasicCode(), dyIndex.getcCode(), dyIndex.getDonorCode(), dyIndex.getAwardCode(), dyIndex.getProgramCode(), getStaffID());
        Log.d("RES", "DTRSec for next mDTRSeq: " + mDTRSeq);
        hideViews();

        DynamicTableQuesDataModel qus = fistQuestion(dyIndex.getDtBasicCode());
        displayQuestion(qus);
    }

    /**
     * Hide the view
     */

    private void hideViews() {
        _dt_tv_DatePicker.setVisibility(View.GONE);
        dt_edt.setVisibility(View.GONE);
        dt_spinner.setVisibility(View.GONE);
        parent_layout_onlyFor_CB.setVisibility(View.GONE);
        radioGroup.setVisibility(View.GONE);
        dt_layout_Radio_N_EditText.setVisibility(View.GONE);
        parent_layout_FOR_CB_N_ET.setVisibility(View.GONE);


    }

    /**
     * Refer the all the necessary view in java object
     */
    private void viewReference() {
        tv_DtQuestion = (TextView) findViewById(R.id.tv_DtQuestion);
        //set up home button
        btnHome = (Button) findViewById(R.id.btnHomeFooter);
        Button btnGone = (Button) findViewById(R.id.btnRegisterFooter);
        btnGone.setVisibility(View.GONE);
        // next & preview button
        btnNextQues = (Button) findViewById(R.id.btn_dt_next);
        btnPreviousQus = (Button) findViewById(R.id.btn_dt_preview);
        // btnNextQues.setText("Next");
        //  btnPreviousQus.setText("Previous");
        // dynamic view reference


        parent_layout_onlyFor_CB = (LinearLayout) findViewById(R.id.ll_checkBox);
        _dt_tv_DatePicker = (TextView) findViewById(R.id.tv_dtTimePicker);
        dt_edt = (EditText) findViewById(R.id.edt_dt);
        dt_spinner = (Spinner) findViewById(R.id.dt_sp);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        /**
         * todo re name
         */
        ll_editText = (LinearLayout) findViewById(R.id.llEditText);

        radioGroupForRadioAndEditText = (RadioGroup) findViewById(R.id.radioGroupForRadioAndEdit);

        //CheckBox and EditText

        parent_layout_FOR_CB_N_ET = (LinearLayout) findViewById(R.id.ll_CheckBoxAndEditTextParent);
        subParent_CB_layout_FOR_CB_N_ET = (LinearLayout) findViewById(R.id.ll_checkBoxAndEditTextCheckbox);
        subParent_ET_layout_FOR_CB_N_ET = (LinearLayout) findViewById(R.id.et_CheckBoxAndEditText);
        dt_layout_Radio_N_EditText = (LinearLayout) findViewById(R.id.ll_radioGroupAndEditText);


        // Error label

//        tv_ErrorDisplay = (TextView) findViewById(R.id.tv_dt_errorLable);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void addStopIconButton(Button button) {
        Log.d("ICON", "in icon set icon  ");

        button.setText("");
        Drawable imageHome = getResources().getDrawable(R.drawable.stop);
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(imageHome, null, null, null);
        setPaddingButton(mContext, imageHome, button);


    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void addSaveIconButton(Button button) {
        // Log.d("ICON", "in icon set icon  ");

        button.setText("");
        Drawable imageHome = getResources().getDrawable(R.drawable.save_b);
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(imageHome, null, null, null);
        setPaddingButton(mContext, imageHome, button);


    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void removeStopIconNextButton(Button button) {
        button.setText("");
        Drawable imageHome;
        if (button == btnPreviousQus)
            imageHome = getResources().getDrawable(R.drawable.goto_back);
        else
            imageHome = getResources().getDrawable(R.drawable.goto_forward);

        button.setCompoundDrawablesRelativeWithIntrinsicBounds(imageHome, null, null, null);
        setPaddingButton(mContext, imageHome, button);


    }


    /**
     * the method invoked once  in {@link  #onCreate(Bundle)}
     *
     * @param dtBasic dtBasic code as primary key
     * @return Ques object  of first index
     */

    private DynamicTableQuesDataModel fistQuestion(final String dtBasic) {
        return loadQuestion(dtBasic, 0);
    }

    /**
     * invoking in {@link #btnNextQues}
     *
     * @param dtBasic  dtBasic code as primary key
     * @param qusIndex Qus  index
     * @return Ques object  of given index
     */
    private DynamicTableQuesDataModel loadNextQuestion(final String dtBasic, final int qusIndex) {

        return loadQuestion(dtBasic, qusIndex);
    }

    /**
     * invoking in {@link #btnPreviousQus}
     *
     * @param dtBasic  dtBasic code as primary key
     * @param qusIndex Qus  index
     * @return Ques object  of given index
     */

    private DynamicTableQuesDataModel loadPreviousQuestion(final String dtBasic, final int qusIndex) {

        return loadQuestion(dtBasic, qusIndex);
    }


    public DynamicTableQuesDataModel loadQuestion(final String dtBasic, final int qusIndex) {
        mDTQ = sqlH.getSingleDynamicQuestion(dtBasic, qusIndex);
        return mDTQ;
    }

    /**
     * loadDT_QResMode(String) is equivalent to ans view loader
     *
     * @param resMode repose Mode
     */
    private void loadDT_QResMode(String resMode) {

/**
 *  the {@link #mDTQResMode} is needed in the save process in {@link #saveProcessValidation()}
 */
        mDTQResMode = sqlH.getDT_QResMode(resMode);
        String responseControl = mDTQResMode.getDtResponseValueControl();
        String dataType = mDTQResMode.getDtDataType();

        String resLupText = mDTQResMode.getDtQResLupText();
        Log.d("Nir", "responseControl :" + responseControl + "\n dataType:" + dataType + " \n resLupText :" + resLupText);
/**
 * Resort Data if Data exits
 */
        DTResponseTableDataModel loadAns = sqlH.getDTResponseTableData(dyIndex.getDtBasicCode(), dyIndex.getcCode(), dyIndex.getDonorCode(), dyIndex.getAwardCode(), dyIndex.getProgramCode(), getStaffID(), mDTQ.getDtQCode(), mDTATableList.get(0).getDt_ACode(), mDTRSeq);

        countChecked = 0;
        if (dataType != null) {
            switch (responseControl) {
                case Textbox:

                    dt_edt.setVisibility(View.VISIBLE);
                    /**
                     * if data exit show data
                     */
                    if (loadAns != null)
                        dt_edt.setText(loadAns.getDtaValue());
                    else
                        dt_edt.setText("");

                    switch (dataType) {
                        case TEXT:
                            dt_edt.setHint("Text");
                            dt_edt.setInputType(InputType.TYPE_CLASS_TEXT);
                            break;
                        case NUMBER:

                            dt_edt.setHint("Number");
                            if (resLupText.equals("Number (not decimal)"))
                                dt_edt.setInputType(InputType.TYPE_CLASS_NUMBER);
                            else
                                dt_edt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                            break;

                    }// end of switch

                    break;


                case Date_OR_Time:

                    _dt_tv_DatePicker.setVisibility(View.VISIBLE);

                    /**
                     * if data exit show data
                     */
                    if (loadAns != null)
                        _dt_tv_DatePicker.setText(loadAns.getDtaValue());
                    else
                        _dt_tv_DatePicker.setText("Select Date");

                    switch (dataType) {
                        case DATE_TIME:
                            getTimeStamp(_dt_tv_DatePicker);
                            break;
                        case DATE:

                            _dt_tv_DatePicker.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    setDate();
                                }
                            });
                            break;
                    }


                    break;
                case COMBO_BOX:

                    dt_spinner.setVisibility(View.VISIBLE);
                    /**
                     * if data exist get the Spinner String
                     * set position •
                     */
                    if (loadAns != null)
                        strSpinner = loadAns.getDtaValue();

                    else
                        strSpinner = null;

                    loadDynamicSpinnerList(dyIndex.getcCode(), resLupText);


                    break;
                case CHECK_BOX:

                    parent_layout_onlyFor_CB.setVisibility(View.VISIBLE);
                    if (mDTATableList.size() > 0)
                        loadDynamicCheckBox(mDTATableList);


                    break;
                case RADIO_BUTTON:
                    radioGroup.setVisibility(View.VISIBLE);
                    if (mDTATableList.size() > 0)
                        loadRadioButtons(mDTATableList);

                    break;


                case RADIO_BUTTON_N_TEXTBOX:
                    dt_layout_Radio_N_EditText.setVisibility(View.VISIBLE);

                    if (mDTATableList.size() > 0)
                        loadRadioButtonAndEditText(mDTATableList, dataType);

                    break;
                case CHECKBOX_N_TEXTBOX:
                    parent_layout_FOR_CB_N_ET.setVisibility(View.VISIBLE);

                    if (mDTATableList.size() > 0)
                        loadDynamicCheckBoxAndEditText(mDTATableList, dataType);
                    break;


            }// end of switch
        }// end of if

    }//  end of loadDT_QResMode


    /**
     * Shuvo vai
     *
     * @param dtA_Table_Data ans Mode
     */

    private void loadDynamicCheckBox(List<DT_ATableDataModel> dtA_Table_Data) {
        /**
         * If there are any Children in layout Container it will reMove
         * And the list of the Check Box {@link #mCheckBox_List} clear
         *
         */
        if (parent_layout_onlyFor_CB.getChildCount() > 0) {
            mCheckBox_List.clear();
            parent_layout_onlyFor_CB.removeAllViews();
        }


        for (int i = 0; i < dtA_Table_Data.size(); i++) {
            TableRow row = new TableRow(this);
            row.setId(i);
            LinearLayout.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(layoutParams);
            CheckBox checkBox = new CheckBox(this);
            checkBox.setOnCheckedChangeListener(DTResponseRecordingActivity.this);
            checkBox.setId(i);
            /**
             * set Text label
             */
            checkBox.setText(dtA_Table_Data.get(i).getDt_ALabel());
            /**
             * set check box is checked or not
             */

            DTResponseTableDataModel loadAns = sqlH.getDTResponseTableData(dyIndex.getDtBasicCode(), dyIndex.getcCode(), dyIndex.getDonorCode(), dyIndex.getAwardCode(), dyIndex.getProgramCode(), getStaffID(), mDTQ.getDtQCode(), dtA_Table_Data.get(i).getDt_ACode(), mDTRSeq);
            if (loadAns != null) {
                if (loadAns.getDtaValue().equals("Y")) {
                    checkBox.setChecked(true);
                }

            }

            row.addView(checkBox);
            /**             * {@link #btnNextQues} needed             */
            mCheckBox_List.add(checkBox);
            parent_layout_onlyFor_CB.addView(row);
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {


        if (isChecked) {
            /**             * increase number of Selected Check box             */
            countChecked++;
        } else {
            /**             * decrease number of  Selected  Check box             */
            countChecked--;
        }


    }


    /**
     * Date & time Session
     */
    public void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        String strDate = mFormat.format(calendar.getTime());
        displayDate(strDate);
    }

    private void displayDate(String strDate) {
        _dt_tv_DatePicker.setText(strDate);
    }

    public void setDate() {
        new DatePickerDialog(mContext, datePickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * date Time picker Listener
     * The Date Listener invoke in {@link #setDate()}
     */

    DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        }
    };

    public void updateDate() {
        displayDate(mFormat.format(calendar.getTime()));

    }


    /**
     * this method load spinner value for different dt basic table dynamicly
     *
     * @param cCode      Country Code
     * @param resLupText res lup
     */
    private void loadDynamicSpinnerList(final String cCode, final String resLupText) {

        SpinnerLoader.loadDynamicSpinnerListLoader(mContext, sqlH, dt_spinner, cCode, resLupText, strSpinner, mDTQ,dyIndex);


        dt_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSpinner = ((SpinnerHelper) dt_spinner.getSelectedItem()).getValue();
                idSpinner = ((SpinnerHelper) dt_spinner.getSelectedItem()).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void loadRadioButtons(List<DT_ATableDataModel> radioButtonItemName) {


        if (radioGroup.getChildCount() > 0) {
            mRadioButton_List.clear();
            radioGroup.removeAllViews();
        }


        for (int i = 0; i < radioButtonItemName.size(); i++) {
            RadioButton rdbtn = new RadioButton(this);
            rdbtn.setId(i);
            rdbtn.setTextSize(24); // set text size

            rdbtn.setPadding(0, 10, 0, 10);     // set padding

            rdbtn.setText(radioButtonItemName.get(i).getDt_ALabel()); // set label
// jodi nicher line ta na execute  kori tahole  ager qus er jei redio button ta select kori pore quesion ta korte pari na
            if (i == 0)
                rdbtn.setChecked(true);


            radioGroup.addView(rdbtn);

            mRadioButton_List.add(rdbtn);

        }// end of for loop

    }
    /**
     * Radio - EditText & CheckBox - EditText
     */

    /**
     * @param List_DtATable
     */

    public void loadRadioButtonAndEditText(List<DT_ATableDataModel> List_DtATable, String dataType) {

        if (ll_editText.getChildCount() > 0) {
            mRadioButtonForRadioAndEdit_List.clear();
            mEditTextForRadioAndEdit_List.clear();
            radioGroupForRadioAndEditText.removeAllViews();
            ll_editText.removeAllViews();
        }


        for (int i = 0; i < List_DtATable.size(); i++) {
            String label = List_DtATable.get(i).getDt_ALabel();
            RadioButton rdbtn = new RadioButton(this);

            rdbtn.setId(i);
            rdbtn.setText(label); // set label
            // jodi nicher line ta na execute  kori tahole  ager qus er jei radio button ta select kori pore question ta korte pari na
            if (i == 0)
                rdbtn.setChecked(true);

            rdbtn.setTextSize(24); // set text size
            rdbtn.setPadding(0, 10, 0, 10);     // set padding
            rdbtn.setOnCheckedChangeListener(DTResponseRecordingActivity.this);


            EditText et = new EditText(this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 5, 0, 5);
            et.setLayoutParams(params);
            et.setHint(label);
            et.setId(i);
            /**
             * sof keyboard type
             */
            if (dataType.equals(NUMBER)) {
                et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            }
            et.setBackgroundColor(Color.WHITE);


/**
 *
 * todo aad index after set DTRespose Sequn {@link #saveOnResponseTable(String, DT_ATableDataModel)}
 */
            DTResponseTableDataModel loadAns = sqlH.getDTResponseTableData(dyIndex.getDtBasicCode(), dyIndex.getcCode(), dyIndex.getDonorCode(), dyIndex.getAwardCode(), dyIndex.getProgramCode(), getStaffID(), mDTQ.getDtQCode(), List_DtATable.get(i).getDt_ACode(), mDTRSeq);
            if (loadAns != null) {
                rdbtn.setChecked(true);
                String value = loadAns.getDtaValue();
                et.setText(value);
            }


            radioGroupForRadioAndEditText.addView(rdbtn);
            mRadioButtonForRadioAndEdit_List.add(rdbtn);

            ll_editText.addView(et);
            mEditTextForRadioAndEdit_List.add(et);

        }


    }

    /**
     * If there are any Children in layout Container it will reMove
     * And the list of the Check Box {@link #mEditTextForCheckBoxAndEdit_List}
     * and {@link #mCheckBoxForCheckBoxAndEdit_List }
     * clear
     */

    private void loadDynamicCheckBoxAndEditText(List<DT_ATableDataModel> List_DtATable, String dataType) {

        if (subParent_CB_layout_FOR_CB_N_ET.getChildCount() > 0) {
            subParent_ET_layout_FOR_CB_N_ET.removeAllViews();
            subParent_CB_layout_FOR_CB_N_ET.removeAllViews();
            mCheckBoxForCheckBoxAndEdit_List.clear();
            mEditTextForCheckBoxAndEdit_List.clear();
        }


        for (int i = 0; i < List_DtATable.size(); i++) {

            String label = List_DtATable.get(i).getDt_ALabel();
            TableRow row = new TableRow(this);
            row.setId(i);
            LinearLayout.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(layoutParams);
            CheckBox checkBox = new CheckBox(this);
            checkBox.setOnCheckedChangeListener(DTResponseRecordingActivity.this);
            checkBox.setId(i);
            checkBox.setText(label); //  set Text label
            row.addView(checkBox);

            EditText et = new EditText(this);
            et.setHint(label);
            et.setId(i);

            /**
             * sof keyboard type
             */
            if (dataType.equals(NUMBER)) {
                et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            }
            et.setBackgroundColor(Color.WHITE);

            /**
             * This snippets work for Check Box Well  but not for the radio button
             * todo aad index after set DTRespose Sequn {@link #saveOnResponseTable(String, DT_ATableDataModel)}
             */
            DTResponseTableDataModel loadAns = sqlH.getDTResponseTableData(dyIndex.getDtBasicCode(), dyIndex.getcCode(), dyIndex.getDonorCode(), dyIndex.getAwardCode(), dyIndex.getProgramCode(), getStaffID(), mDTQ.getDtQCode(), List_DtATable.get(i).getDt_ACode(), mDTRSeq);
            if (loadAns != null) {
                checkBox.setChecked(true);
                String value = loadAns.getDtaValue();
                et.setText(value);
            }

            subParent_ET_layout_FOR_CB_N_ET.addView(et);
            /**
             * {@link #btnNextQues} needed
             *
             */

            mEditTextForCheckBoxAndEdit_List.add(et);
            subParent_CB_layout_FOR_CB_N_ET.addView(row);
            mCheckBoxForCheckBoxAndEdit_List.add(checkBox);
        }


    }

    /**
     * Shuvo
     * this method only show the System Current Time
     *
     * @param tv Text view For Show
     */

    private void getTimeStamp(TextView tv) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DATE);
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        String am_pm = (hourOfDay < 12) ? "AM" : "PM";
        String timeStamp = year + "/" + month + "/" + day + "  " + hourOfDay + ":" + minute + " " + am_pm;

        tv.setText(timeStamp);
    }

}

