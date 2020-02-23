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
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import com.praxello.smartquiz.adapter.ViewQuestionAdapter;
import com.praxello.smartquiz.model.allquestion.QuizBO;
import com.praxello.smartquiz.services.ApiRequestHelper;
import com.praxello.smartquiz.services.SmartQuiz;
import com.praxello.smartquiz.widget.slidingitemrecyclerview.SlidingItemMenuRecyclerView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ViewQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.rv_create_quiz_question)
    SlidingItemMenuRecyclerView rvCreateQuizQuestion;

    private static String TAG="ViewQuestionActivity";

    public static SmartQuiz smartQuiz;
    @BindView(R.id.btn_addquizquestion)
    AppCompatButton btnAddQuizQuestion;
    QuizBO quizBO;
    @BindView(R.id.ll_nodata)
    LinearLayout llNoData;
    private int GALLERY = 1, CAMERA = 2;
    String imageBase64String;
    public static String  selectedImagePath;
    public static ViewQuestionAdapter viewQuestionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);
        smartQuiz = (SmartQuiz) getApplication();
        ButterKnife.bind(this);

        if (getIntent().getParcelableExtra("data") != null) {
            //questionBO=getIntent().getParcelableArrayListExtra("data");
            quizBO = getIntent().getParcelableExtra("data");
        }

        //basic intialisation..
        initViews();
    }

    private void initViews(){
        Toolbar toolbar=findViewById(R.id.toolbar_create_question);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Question");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        //button intialisation..
        btnAddQuizQuestion.setOnClickListener(this);
        rvCreateQuizQuestion.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        //Setting data to adapter.....
        if(getIntent().getParcelableExtra("data")!=null){
            if(quizBO.getQuestions()!=null){
                viewQuestionAdapter=new ViewQuestionAdapter(ViewQuestionActivity.this,quizBO.getQuestions());
                rvCreateQuizQuestion.setAdapter(viewQuestionAdapter);
            }else{
                llNoData.setVisibility(View.VISIBLE);
                rvCreateQuizQuestion.setVisibility(View.GONE);
            }
        }else{
            llNoData.setVisibility(View.VISIBLE);
            rvCreateQuizQuestion.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_addquizquestion:
                Intent intent=new Intent(ViewQuestionActivity.this, AddNewQuestionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("data",quizBO);
                //intent.putExtra("quizid",getIntent().getStringExtra("quizid"));
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(AddNewQuestionActivity.questionBOArrayList!=null){
            ViewQuestionAdapter viewQuestionAdapter=new ViewQuestionAdapter(ViewQuestionActivity.this,AddNewQuestionActivity.questionBOArrayList);
            rvCreateQuizQuestion.setAdapter(viewQuestionAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    public void onClickCalled() {
        //first check for permissions then uploading....
        requestPermissions();

        // Call another acitivty here and pass some arguments to it.
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
                    //ViewQuestionAdapter.ViewQuestionViewHolder.etMediaLink.setText("Demo");

                    //Toast.makeText(DashBoardActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ViewQuestionActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
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
            Uri tempUri = getImageUri(ViewQuestionActivity.this, thumbnail);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tempUri));
            imageBase64String = Base64.encodeToString(byteArray, Base64.DEFAULT);
            selectedImagePath=finalFile.getAbsolutePath();
            //ViewQuestionAdapter.ViewQuestionViewHolder.etMediaLink.setText("Demo");

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
        Cursor cursor = ViewQuestionActivity.this.getContentResolver().query(uri, projection, null, null, null);
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
        Cursor cursor = ViewQuestionActivity.this.getContentResolver().query(uri, null, null, null, null);
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
            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public void uploadImageRetrofit(){

        String filePath=selectedImagePath;
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
        map.put("imageName", createPartFromString("questionpics/"+ CommonMethods.getPrefrence(ViewQuestionActivity.this, AllKeys.USER_ID)+".jpg"));
        map.put("angle", createPartFromString("0"));
        map.put("imageData", createPartFromString(imageBase64String));

        smartQuiz.getApiRequestHelper().uploadimage(map, new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                Toast.makeText(ViewQuestionActivity.this, "image updated successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String apiResponse) {
                Toast.makeText(ViewQuestionActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MultipartBody.FORM, descriptionString);
    }
}
