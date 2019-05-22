package com.taxi.taxidriver.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taxi.taxidriver.R;
import com.taxi.taxidriver.constant.Constant;
import com.taxi.taxidriver.modal.main_category_modal.Subcategory;
import com.taxi.taxidriver.modal.main_category_modal.TaxiMainCategoryModal;
import com.taxi.taxidriver.modal.main_category_modal.Vehicle;
import com.taxi.taxidriver.retrofit_provider.RetrofitService;
import com.taxi.taxidriver.retrofit_provider.WebResponse;
import com.taxi.taxidriver.ui.MainHomeActivity;
import com.taxi.taxidriver.utils.Alerts;
import com.taxi.taxidriver.utils.BaseFragment;
import com.taxi.taxidriver.utils.ConnectionDirector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.taxi.taxidriver.ui.activity.LoginActivity.loginfragmentManager;

public class SignUpFragment extends BaseFragment implements View.OnClickListener {
    private static final int LOAD_IMAGE_GALLERY = 120;
    private static final int LOAD_IMAGE_GALLERY1 = 121;
    private static final int LOAD_IMAGE_GALLERY2 = 122;
    private static final int LOAD_IMAGE_GALLERY3 = 123;
    private static final int LOAD_IMAGE_GALLERY4 = 124;
    private static int PICK_IMAGE_CAMERA = 210;
    private static int PICK_IMAGE_CAMERA1 = 211;
    private static int PICK_IMAGE_CAMERA2 = 212;
    private static int PICK_IMAGE_CAMERA3 = 213;
    private static int PICK_IMAGE_CAMERA4 = 214;
    private static int PERMISSION_REQUEST_CODE = 454;
    /*   private static int PERMISSION_REQUEST_CODE1 = 455;
       private static int PERMISSION_REQUEST_CODE2 = 456;
       private static int PERMISSION_REQUEST_CODE3 = 457;
       private static int PERMISSION_REQUEST_CODE4 = 458;*/
    private ArrayAdapter categoryAdapter, subcategoryAdapter;
    private int defaultSelection = 0;
    private List<Vehicle> categoryList = new ArrayList<>();
    List<Subcategory> subcategoryList = new ArrayList<>();
    private String categoryId, subcategoryId;
    private File finalFile = null, imageProfile = null;
    private String strMedicineImage;
    private View rootview;
    private Button btn_signUp;
    private TextView tvSignIn;
    private TextView tvLogin;
    private String strGender = "";
    private CircleImageView ivInsurance, ivDrivingLIcence;
    private String strAadharImage = "", strVehicleImage = "", strDrivingLIImage = "", strInsuImage = "";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_signup_layout, container, false);
        activity = getActivity();
        mContext = getActivity();
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();

        if (checkPermission()) {
            Alerts.show(mContext, "Permission granted");
        } else {
            requestPermission();
        }
        init();
        return rootview;
    }

    private void init() {
        tvSignIn = rootview.findViewById(R.id.tvSignIn);
        ivInsurance = rootview.findViewById(R.id.ivInsurance);
        ((RelativeLayout) rootview.findViewById(R.id.rlProfileImage)).setOnClickListener(this);
        ((RelativeLayout) rootview.findViewById(R.id.rlAadhar)).setOnClickListener(this);
        ((RelativeLayout) rootview.findViewById(R.id.rVehicle)).setOnClickListener(this);
        ((RelativeLayout) rootview.findViewById(R.id.rlDrivingLicence)).setOnClickListener(this);
        ((RelativeLayout) rootview.findViewById(R.id.rlInsurance)).setOnClickListener(this);
        tvLogin = rootview.findViewById(R.id.tvLogin);
        tvSignIn.setOnClickListener(this);
        tvLogin.setOnClickListener(this);

        RadioGroup rgGender = rootview.findViewById(R.id.rgGender);

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
// Toast.makeText(mContext, rb.getText(), Toast.LENGTH_SHORT).show();
                    strGender = rb.getText().toString();
                    if (strGender.equals("Male")) {
                        strGender = "1";
                    } else if (strGender.equals("Female")) {
                        strGender = "2";
                    } else {
                        strGender = "3";
                    }
                }
            }
        });

        spinnerData();
        categoryApi();
    }

    private void startFragment(String tag, Fragment fragment) {
        loginfragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.login_frame, fragment, tag).commit();
    }

    private void spinnerData() {
        Spinner spnCategory = rootview.findViewById(R.id.spnCategory);
        categoryAdapter = new ArrayAdapter(mContext, R.layout.row_spn_normal, categoryList);
        spnCategory.setAdapter(categoryAdapter);
        spnCategory.setSelection(defaultSelection);
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryId = categoryList.get(position).getCategoryId();
                subcategoryList.addAll(categoryList.get(position).getSubcategory());
                subcategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categoryAdapter.notifyDataSetChanged();

        /************************************************
         * Subcategory Adapter
         * ***********************************************/
        Spinner spnSubCategory = rootview.findViewById(R.id.spnSubCategory);
        subcategoryAdapter = new ArrayAdapter(mContext, R.layout.row_spn_normal, subcategoryList);
        spnSubCategory.setAdapter(subcategoryAdapter);
        spnSubCategory.setSelection(defaultSelection);
        spnSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subcategoryId = subcategoryList.get(position).getSubcategoryId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        subcategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSignIn:
                driverRagistrationApi();
                break;
            case R.id.tvLogin:
                startFragment(Constant.SignIn, new LoginFragment());
                break;
            case R.id.rlProfileImage:
                try {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, LOAD_IMAGE_GALLERY);
                    } else {
                        selectImage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rlAadhar:
                try {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, LOAD_IMAGE_GALLERY1);
                    } else {
                        selectImage1();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rVehicle:
                try {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, LOAD_IMAGE_GALLERY2);
                    } else {
                        selectImage2();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rlDrivingLicence:
                try {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, LOAD_IMAGE_GALLERY3);
                    } else {
                        selectImage3();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rlInsurance:
                try {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, LOAD_IMAGE_GALLERY4);
                    } else {
                        selectImage4();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOAD_IMAGE_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    Alerts.show(mContext, "Please give permission");
                }
                break;
            case LOAD_IMAGE_GALLERY1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    Alerts.show(mContext, "Please give permission");
                }
                break;
            case LOAD_IMAGE_GALLERY2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    Alerts.show(mContext, "Please give permission");
                }
                break;
            case LOAD_IMAGE_GALLERY3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    Alerts.show(mContext, "Please give permission");
                }
                break;
            case LOAD_IMAGE_GALLERY4:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    Alerts.show(mContext, "Please give permission");
                }
                break;
        }
    }


    private void selectImage() {
        try {
            PackageManager pm = mContext.getPackageManager();
            int permission = pm.checkPermission(Manifest.permission.CAMERA, mContext.getPackageName());
            if (permission == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] choose = {"Pick From Camera", "Choose From Gallery", "Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                builder.setTitle("Select Option");
                builder.setItems(choose, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (choose[which].equals("Pick From Camera")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (choose[which].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, LOAD_IMAGE_GALLERY);
                        } else if (choose[which].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(mContext, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(mContext, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void selectImage1() {
        try {
            PackageManager pm = mContext.getPackageManager();
            int permission = pm.checkPermission(Manifest.permission.CAMERA, mContext.getPackageName());
            if (permission == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] choose = {"Pick From Camera", "Choose From Gallery", "Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                builder.setTitle("Select Option");
                builder.setItems(choose, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (choose[which].equals("Pick From Camera")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA1);
                        } else if (choose[which].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, LOAD_IMAGE_GALLERY1);
                        } else if (choose[which].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(mContext, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(mContext, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void selectImage2() {
        try {
            PackageManager pm = mContext.getPackageManager();
            int permission = pm.checkPermission(Manifest.permission.CAMERA, mContext.getPackageName());
            if (permission == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] choose = {"Pick From Camera", "Choose From Gallery", "Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                builder.setTitle("Select Option");
                builder.setItems(choose, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (choose[which].equals("Pick From Camera")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA2);
                        } else if (choose[which].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, LOAD_IMAGE_GALLERY2);
                        } else if (choose[which].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(mContext, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(mContext, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void selectImage3() {
        try {
            PackageManager pm = mContext.getPackageManager();
            int permission = pm.checkPermission(Manifest.permission.CAMERA, mContext.getPackageName());
            if (permission == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] choose = {"Pick From Camera", "Choose From Gallery", "Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                builder.setTitle("Select Option");
                builder.setItems(choose, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (choose[which].equals("Pick From Camera")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA3);
                        } else if (choose[which].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, LOAD_IMAGE_GALLERY3);
                        } else if (choose[which].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(mContext, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(mContext, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void selectImage4() {
        try {
            PackageManager pm = mContext.getPackageManager();
            int permission = pm.checkPermission(Manifest.permission.CAMERA, mContext.getPackageName());
            if (permission == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] choose = {"Pick From Camera", "Choose From Gallery", "Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                builder.setTitle("Select Option");
                builder.setItems(choose, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (choose[which].equals("Pick From Camera")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA4);
                        } else if (choose[which].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, LOAD_IMAGE_GALLERY4);
                        } else if (choose[which].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(mContext, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(mContext, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Alerts.show(mContext, "Permission not granted");
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void requestPermission() {
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (mContext.getContentResolver() != null) {
            Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ((CircleImageView) rootview.findViewById(R.id.imgProfileImage)).setImageBitmap(photo);
                Uri tempUri = getImageUri(mContext, photo);
                imageProfile = new File(getRealPathFromURI(tempUri));
                //api hit

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == LOAD_IMAGE_GALLERY && resultCode == RESULT_OK && null != data) {
            final Uri uriImage = data.getData();
            final InputStream inputStream;
            try {
                inputStream = mContext.getContentResolver().openInputStream(uriImage);
                final Bitmap imageMap = BitmapFactory.decodeStream(inputStream);
                ((CircleImageView) rootview.findViewById(R.id.imgProfileImage)).setImageBitmap(imageMap);

                String imagePath2 = getPath(uriImage);
                imageProfile = new File(imagePath2);


                //api hit
            } catch (FileNotFoundException e) {
                Toast.makeText(mContext, "Image not found", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_CAMERA1) {
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ((CircleImageView) rootview.findViewById(R.id.ivAadhar)).setImageBitmap(photo);
                Uri tempUri = getImageUri(mContext, photo);
                finalFile = new File(getRealPathFromURI(tempUri));
                strAadharImage = convertToBase64(finalFile.getAbsolutePath());


                //api hit

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == LOAD_IMAGE_GALLERY1 && resultCode == RESULT_OK && null != data) {
            final Uri uriImage = data.getData();
            final InputStream inputStream;
            try {
                inputStream = mContext.getContentResolver().openInputStream(uriImage);
                final Bitmap imageMap = BitmapFactory.decodeStream(inputStream);
                ((CircleImageView) rootview.findViewById(R.id.ivAadhar)).setImageBitmap(imageMap);

                String imagePath2 = getPath(uriImage);
                File imageFile = new File(imagePath2);

                strAadharImage = convertToBase64(imageFile.getAbsolutePath());


                //api hit
            } catch (FileNotFoundException e) {
                Toast.makeText(mContext, "Image not found", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
        }
        if (requestCode == PICK_IMAGE_CAMERA2) {
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ((CircleImageView) rootview.findViewById(R.id.ivVehicle)).setImageBitmap(photo);
                Uri tempUri = getImageUri(mContext, photo);
                File finalFile = new File(getRealPathFromURI(tempUri));
                strVehicleImage = convertToBase64(finalFile.getAbsolutePath());


                //api hit

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == LOAD_IMAGE_GALLERY2 && resultCode == RESULT_OK && null != data) {
            final Uri uriImage = data.getData();
            final InputStream inputStream;
            try {
                inputStream = mContext.getContentResolver().openInputStream(uriImage);
                final Bitmap imageMap = BitmapFactory.decodeStream(inputStream);
                ((CircleImageView) rootview.findViewById(R.id.ivVehicle)).setImageBitmap(imageMap);

                String imagePath2 = getPath(uriImage);
                File imageFile = new File(imagePath2);
                strVehicleImage = convertToBase64(imageFile.getAbsolutePath());


                //api hit
            } catch (FileNotFoundException e) {
                Toast.makeText(mContext, "Image not found", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {

        }
        if (requestCode == PICK_IMAGE_CAMERA3) {
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ((CircleImageView) rootview.findViewById(R.id.ivDrivingLIcence)).setImageBitmap(photo);
                Uri tempUri = getImageUri(mContext, photo);
                File finalFile = new File(getRealPathFromURI(tempUri));

                strDrivingLIImage = convertToBase64(finalFile.getAbsolutePath());
                Log.e("img12", strDrivingLIImage);
                //api hit

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == LOAD_IMAGE_GALLERY3 && resultCode == RESULT_OK && null != data) {
            final Uri uriImage = data.getData();
            final InputStream inputStream;
            try {
                inputStream = mContext.getContentResolver().openInputStream(uriImage);
                final Bitmap imageMap = BitmapFactory.decodeStream(inputStream);
                ((CircleImageView) rootview.findViewById(R.id.ivDrivingLIcence)).setImageBitmap(imageMap);

                String imagePath2 = getPath(uriImage);
                File imageFile = new File(imagePath2);

                strDrivingLIImage = convertToBase64(imageFile.getAbsolutePath());
                Log.e("img12", strDrivingLIImage);

                //api hit
            } catch (FileNotFoundException e) {
                Toast.makeText(mContext, "Image not found", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_CAMERA4) {
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ((CircleImageView) rootview.findViewById(R.id.ivInsurance)).setImageBitmap(photo);
                Uri tempUri = getImageUri(mContext, photo);
                File finalFile = new File(getRealPathFromURI(tempUri));
                strInsuImage = convertToBase64(finalFile.getAbsolutePath());
                //api hit

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == LOAD_IMAGE_GALLERY4 && resultCode == RESULT_OK && null != data) {
            final Uri uriImage = data.getData();
            final InputStream inputStream;
            try {
                inputStream = mContext.getContentResolver().openInputStream(uriImage);
                final Bitmap imageMap = BitmapFactory.decodeStream(inputStream);
                ((CircleImageView) rootview.findViewById(R.id.ivInsurance)).setImageBitmap(imageMap);

                String imagePath2 = getPath(uriImage);
                File imageFile = new File(imagePath2);
                strInsuImage = convertToBase64(imageFile.getAbsolutePath());
                Log.e("img123", strInsuImage);

                //api hit
            } catch (FileNotFoundException e) {
                Toast.makeText(mContext, "Image not found", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String strPath = cursor.getString(column_index);
        cursor.close();
        return strPath;
    }

    private String convertToBase64(String path) {

        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encodedImage;
    }


    private void driverRagistrationApi() {
        if (cd.isNetWorkAvailable()) {
            String strDriverName = ((EditText) rootview.findViewById(R.id.driverName)).getText().toString();
            String strDriverPhone = ((EditText) rootview.findViewById(R.id.driverPhone)).getText().toString();
            String strEmail = ((EditText) rootview.findViewById(R.id.emailAddress)).getText().toString();
            String strPassword = ((EditText) rootview.findViewById(R.id.password)).getText().toString();
            String strCountry = ((EditText) rootview.findViewById(R.id.country)).getText().toString();
            String strState = ((EditText) rootview.findViewById(R.id.state)).getText().toString();
            String strCity = ((EditText) rootview.findViewById(R.id.city)).getText().toString();
            String strAadharNumber = ((EditText) rootview.findViewById(R.id.aadharNumber)).getText().toString();
            String strVehicleNumnber = ((EditText) rootview.findViewById(R.id.vehicleNumber)).getText().toString();
            String strVehicleModaNumber = ((EditText) rootview.findViewById(R.id.vehicleModalNumber)).getText().toString();
            String strDrivingNumber = ((EditText) rootview.findViewById(R.id.drivingLIcenceNumber)).getText().toString();
            String strInsuranseNumber = ((EditText) rootview.findViewById(R.id.ivInsuranceCaries)).getText().toString();
            String strInsuranceExpireDate = ((EditText) rootview.findViewById(R.id.ivInsuranceExpire)).getText().toString();


            RequestBody driverName = RequestBody.create(MediaType.parse("text/plain"), strDriverName);
            RequestBody driverPhone = RequestBody.create(MediaType.parse("text/plain"), strDriverPhone);
            RequestBody driverEmail = RequestBody.create(MediaType.parse("text/plain"), strEmail);
            RequestBody driverPassword = RequestBody.create(MediaType.parse("text/plain"), strPassword);
            RequestBody country = RequestBody.create(MediaType.parse("text/plain"), strCountry);
            RequestBody state = RequestBody.create(MediaType.parse("text/plain"), strState);
            RequestBody city = RequestBody.create(MediaType.parse("text/plain"), strCity);
            RequestBody aadhar = RequestBody.create(MediaType.parse("text/plain"), strAadharNumber);
            RequestBody vehicleNumber = RequestBody.create(MediaType.parse("text/plain"), strVehicleNumnber);
            RequestBody vehicleModalNumber = RequestBody.create(MediaType.parse("text/plain"), strVehicleModaNumber);
            RequestBody drivingLicenceNumber = RequestBody.create(MediaType.parse("text/plain"), strDrivingNumber);
            RequestBody insuranceNumber = RequestBody.create(MediaType.parse("text/plain"), strInsuranseNumber);
            RequestBody insuranceExpireDate = RequestBody.create(MediaType.parse("text/plain"), strInsuranceExpireDate);
            RequestBody catId = RequestBody.create(MediaType.parse("text/plain"), categoryId);
            RequestBody subId = RequestBody.create(MediaType.parse("text/plain"), subcategoryId);
            RequestBody aadharImage = RequestBody.create(MediaType.parse("text/plain"), strAadharImage);
            RequestBody insuranceImamge = RequestBody.create(MediaType.parse("text/plain"), strInsuImage);
            RequestBody vehicleImage = RequestBody.create(MediaType.parse("text/plain"), strVehicleImage);
            RequestBody drivingliImage = RequestBody.create(MediaType.parse("text/plain"), strDrivingLIImage);
            RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), strGender);

            RequestBody imageBodyFile = RequestBody.create(MediaType.parse("image/*"), imageProfile);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("avtar_img", imageProfile.getName(), imageBodyFile);


            RetrofitService.getServerResponce(new Dialog(mContext), retrofitApiClient.updateProfile(driverName, driverPassword,
                    driverPhone, driverEmail, gender, city, state, country, catId, subId, aadhar, drivingLicenceNumber
                    , insuranceExpireDate, vehicleModalNumber, vehicleNumber, drivingliImage,
                    insuranceImamge, aadharImage, vehicleImage, fileToUpload), new WebResponse() {

                @Override
                public void onResponseSuccess(Response<?> result) throws JSONException {
                    ResponseBody responseBody = (ResponseBody) result.body();
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody.string());
                        if (jsonObject.getBoolean("1")) {
                          //  startFragment(Constant.OtpFragment, new OtpFragment());
                            Alerts.show(mContext, jsonObject.getString("message"));
                        } else {
                            Alerts.show(mContext, jsonObject.getString("message"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onResponseFailed(String error) {
                    Alerts.show(mContext,error);

                }
            });

        } else {
            cd.show(mContext);
        }

    }


    private void categoryApi() {
        if (cd.isNetWorkAvailable()) {


            RetrofitService.getCategoryData(new Dialog(mContext), retrofitApiClient.mainCategoryData(), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    categoryList.clear();
                    TaxiMainCategoryModal mainCategoryModal = (TaxiMainCategoryModal) result.body();

                    if (mainCategoryModal.getStatus() == 1) {
                        categoryList.addAll(mainCategoryModal.getVehicle());
                        if (categoryList.size() > 0) {
                            categoryAdapter.notifyDataSetChanged();
                        } else {
                            Alerts.show(mContext, mainCategoryModal.getMessage());
                        }
                    }
                }

                @Override
                public void onResponseFailed(String error) {
                    Alerts.show(mContext, error);
                }
            });
        } else {
            cd.show(mContext);
        }
    }

}