package com.praxello.smartquiz.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.praxello.smartquiz.AllKeys;
import com.praxello.smartquiz.CommonMethods;
import com.praxello.smartquiz.R;
import com.praxello.smartquiz.model.CreateQuestionResponse;
import com.praxello.smartquiz.model.allquestion.QuestionBO;
import com.praxello.smartquiz.model.allquestion.QuizBO;
import com.praxello.smartquiz.model.quiz.UserData;
import com.praxello.smartquiz.services.ApiRequestHelper;
import com.praxello.smartquiz.services.SmartQuiz;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddNewQuestionActivity extends AppCompatActivity implements View.OnClickListener{

    private static String TAG="AddNewQuestionActivity";
    public static SmartQuiz smartQuiz;

    @BindView(R.id.spin_category)
    Spinner spinCategory;
    @BindView(R.id.et_question)
    EditText etQuestion;
    @BindView(R.id.et_option1)
    EditText etOption1;
    @BindView(R.id.et_option2)
    EditText etOption2;
    @BindView(R.id.et_option3)
    EditText etOption3;
    @BindView(R.id.et_option4)
    EditText etOption4;
    @BindView(R.id.et_medialink)
    EditText etMediaLink;
    @BindView(R.id.et_details)
    EditText etDetails;
    @BindView(R.id.btn_createquestion)
    AppCompatButton btnCreateQuestion;
    @BindView(R.id.btn_medialink)
    AppCompatButton btnMediaLink;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.radioButton1)
    RadioButton radioButton1;
    @BindView(R.id.radioButton2)
    RadioButton radioButton2;
    @BindView(R.id.radioButton3)
    RadioButton radioButton3;
    @BindView(R.id.radioButton4)
    RadioButton radioBuuton4;
    @BindView(R.id.ll_medialink)
    LinearLayout llMediaLink;
    QuizBO quizBO;
    private int stCategoryId=0;
    private int selectedRadioBtn=0;
    private int GALLERY = 1, CAMERA = 2;
    String imageBase64String;
    String selectedImagePath;
    public static ArrayList<QuestionBO> questionBOArrayList;
    ArrayList<QuestionBO> questionBOSArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_question);
        smartQuiz = (SmartQuiz) getApplication();
        ButterKnife.bind(this);

        if (getIntent().getParcelableExtra("data") != null) {
            //questionBO=getIntent().getParcelableArrayListExtra("data");
            quizBO = getIntent().getParcelableExtra("data");
            Log.e(TAG, "onCreate: "+quizBO.toString() );
        }

        //basic intialisation..
        initViews();

        //set Data to adapter spin category
        setDataToAdapter();

        //getRadio button selected data....
        getRadioButtonData();
    }

    private void initViews(){
        questionBOSArrayList=new ArrayList<>();

        Toolbar toolbar=findViewById(R.id.toolbar_addnewquestion);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("New Question");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        //button intialisation..
        btnCreateQuestion.setOnClickListener(this);
        btnMediaLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_createquestion:
                if(isValidated()){
                    createQuestion();
                }
                break;

            case R.id.btn_medialink:
                //first check for permissions then uploading....
                requestPermissions();
                break;
        }
    }

    private void setDataToAdapter(){
        String[] arraySpinner = new String[] {"Normal","Image","Video"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(adapter);

        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stCategoryId=position+1;
                if(stCategoryId==2){
                    llMediaLink.setVisibility(View.VISIBLE);
                    etMediaLink.setVisibility(View.VISIBLE);
                    btnMediaLink.setVisibility(View.VISIBLE);
                    etMediaLink.setEnabled(false);
                }else if(stCategoryId==3){
                    llMediaLink.setVisibility(View.VISIBLE);
                    etMediaLink.setVisibility(View.VISIBLE);
                    btnMediaLink.setVisibility(View.GONE);
                    etMediaLink.setEnabled(true);
                }else{
                    llMediaLink.setVisibility(View.GONE);
                }
                //Toast.makeText(AddNewQuestionActivity.this, "Category Id"+stCategoryId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getRadioButtonData(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radioButton1) {
                    selectedRadioBtn=1;
                    //Toast.makeText(getApplicationContext(), selectedRadioBtn+"", Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.radioButton2) {
                    selectedRadioBtn=2;
                    //Toast.makeText(getApplicationContext(), selectedRadioBtn+"", Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.radioButton3) {
                    selectedRadioBtn=3;
                   // Toast.makeText(getApplicationContext(), selectedRadioBtn+"", Toast.LENGTH_SHORT).show();
                }else if(checkedId == R.id.radioButton4) {
                    selectedRadioBtn=4;
                   // Toast.makeText(getApplicationContext(), selectedRadioBtn+"", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestPermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            /* New Handler to start the Menu-Activity
                             * and close this Splash-Screen after some seconds.*/
                                showPictureDialog();
                            // Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void createQuestion(){
        Map<String,String> params=new HashMap<>();
        params.put("questionType",String.valueOf(stCategoryId));
        params.put("quizId",String.valueOf(quizBO.getQuizId()));
        params.put("question",etQuestion.getText().toString());
        params.put("option1",etOption1.getText().toString());
        params.put("option2",etOption2.getText().toString());
        params.put("option3",etOption3.getText().toString());
        params.put("option4",etOption4.getText().toString());
        params.put("answer",String.valueOf(selectedRadioBtn));
        params.put("answerDetails",etDetails.getText().toString());
        params.put("mediaUrl","http://103.127.146.5/~tailor/smartquiz/questionpics/"+CommonMethods.getPrefrence(AddNewQuestionActivity.this,AllKeys.USER_ID)+".jpg");

        Log.e(TAG, "createQuestion: "+params );

        smartQuiz.getApiRequestHelper().createquizquestion(params,new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                CreateQuestionResponse createQuestionResponse=(CreateQuestionResponse) object;

                Log.e(TAG, "onSuccess: "+createQuestionResponse.getResponsecode());
                Log.e(TAG, "onSuccess: "+createQuestionResponse.getMessage());

                if(createQuestionResponse.getResponsecode()==200){
                    AddNewQuestionActivity.questionBOArrayList=createQuestionResponse.getNewRecord();
                    Toast.makeText(AddNewQuestionActivity.this, createQuestionResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    QuestionBO questionBO=new QuestionBO(createQuestionResponse.getNewRecord().get(0).getQuestionId(),
                            createQuestionResponse.getNewRecord().get(0).getQuestionType(),
                            createQuestionResponse.getNewRecord().get(0).getQuizId(),
                            createQuestionResponse.getNewRecord().get(0).getQuestion(),
                            createQuestionResponse.getNewRecord().get(0).getOption1(),
                            createQuestionResponse.getNewRecord().get(0).getOption2(),
                            createQuestionResponse.getNewRecord().get(0).getOption3(),
                            createQuestionResponse.getNewRecord().get(0).getOption4(),
                            createQuestionResponse.getNewRecord().get(0).getAnswer(),
                            createQuestionResponse.getNewRecord().get(0).getAnswerDetails(),
                            createQuestionResponse.getNewRecord().get(0).getIsActive(),
                            createQuestionResponse.getNewRecord().get(0).getMediaUrl()
                    );

                    MyQuizActivity.mainQuizBO.getQuestions().add(questionBO);
                    ViewQuestionActivity.viewQuestionAdapter.notifyDataSetChanged();
                    MyQuizActivity.myQuizAdapter.notifyDataSetChanged();

                    //uploading image data..
                    if(selectedImagePath!=null){
                        uploadImageRetrofit(selectedImagePath);
                    }
                    //clear all fields data
                    resetAllData();

                }else{
                    Toast.makeText(AddNewQuestionActivity.this, createQuestionResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String apiResponse) {
                Toast.makeText(AddNewQuestionActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetAllData(){
        etQuestion.setText("");
        etOption1.setText("");
        etOption2.setText("");
        etOption3.setText("");
        etOption4.setText("");
        etMediaLink.setText("");
        etDetails.setText("");
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {"Capture photo from camera",
                "Select photo from gallery"
        };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                takePhotoFromCamera();
                                break;
                            case 1:
                                choosePhotoFromGallary();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (requestCode == GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    //String path = saveImage(bitmap);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,50,out);

                    byte[] byteArray = out.toByteArray();
                    Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                    //ivProfilePic.setImageBitmap(compressedBitmap);

                    selectedImagePath =  getRealPathFromURIForGallery(contentURI);
                    etMediaLink.setText(selectedImagePath);

                    //Toast.makeText(DashBoardActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddNewQuestionActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA && resultCode == RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, out);
            Bitmap decodedImage= BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            //ivProfilePic.setImageBitmap(decodedImage);

            byte[] byteArray = out .toByteArray();

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(AddNewQuestionActivity.this, thumbnail);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tempUri));
            imageBase64String = Base64.encodeToString(byteArray, Base64.DEFAULT);
            selectedImagePath=finalFile.getAbsolutePath();
            etMediaLink.setText(selectedImagePath);

            //uploadImageRetrofit(finalFile.getAbsolutePath());

            // Log.e(TAG, "onActivityResult:decoded bitmap "+imageBase64String );

            //Toast.makeText(DashBoardActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String getRealPathFromURIForGallery(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = AddNewQuestionActivity.this.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    public static Bitmap decodeSampledBitmapFromResource(String strPath, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
//        if (Build.VERSION.SDK_INT < 21) {
//            options.inPurgeable = true;
//        }else {
//            options.inBitmap= true;
//        }
        BitmapFactory.decodeFile(strPath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(strPath, options);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = AddNewQuestionActivity.this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of welcomeone and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public void uploadImageRetrofit(String filePath){

        Bitmap bm;
        File file = new File(filePath);
        long fileSizeInBytes = 0;
        if (file.length() > 0) {
            fileSizeInBytes = file.length();
        }
        long fileSizeInKB = 0;
        if (fileSizeInBytes > 1024) {
            fileSizeInKB = fileSizeInBytes / 1024;
        }
        if (fileSizeInKB > 500) {
            bm = decodeSampledBitmapFromResource(filePath, 400, 200);
        } else {
            bm = BitmapFactory.decodeFile(filePath);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        imageBase64String= Base64.encodeToString(b, Base64.DEFAULT);

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("imageName", createPartFromString("questionpics/"+ CommonMethods.getPrefrence(AddNewQuestionActivity.this, AllKeys.USER_ID)+".jpg"));
        map.put("angle", createPartFromString("0"));
        map.put("imageData", createPartFromString(imageBase64String));

        smartQuiz.getApiRequestHelper().uploadimage(map, new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                Toast.makeText(AddNewQuestionActivity.this, "image updated successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String apiResponse) {
                Toast.makeText(AddNewQuestionActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MultipartBody.FORM, descriptionString);
    }

    private boolean isValidated(){
        if(etQuestion.getText().toString().isEmpty()){
            Toast.makeText(smartQuiz, "Question required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(etOption1.getText().toString().isEmpty()){
            Toast.makeText(smartQuiz, "Option1 required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(etOption2.getText().toString().isEmpty()){
            Toast.makeText(smartQuiz, "Option2 required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(etOption3.getText().toString().isEmpty()){
            Toast.makeText(smartQuiz, "Option3 required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(etOption4.getText().toString().isEmpty()){
            Toast.makeText(smartQuiz, "Option4 required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(stCategoryId==2 || stCategoryId==3){
            if(etMediaLink.getText().toString().isEmpty()){
                Toast.makeText(smartQuiz, "Media link required!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if(etDetails.getText().toString().isEmpty()){
            Toast.makeText(smartQuiz, "Details required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (radioGroup.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(smartQuiz, "Please select one right option!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
    }

}
