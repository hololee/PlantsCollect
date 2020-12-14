package com.phone.plantscollect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    ProgressDialog dialog;
    SharedPreferences preferences, preferences2;
    Uri mImageCaptureUri;

    static final int TAKE_PICTURE = 1001;
    static final int IMPORT_GALLERY = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                runMain();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            runMain();
        }
    }


    public void runMain() {

        Toast.makeText(this, "permission granted.", Toast.LENGTH_SHORT).show();

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("NOTICE");
        builder.setMessage("리스트의 사진을 클릭시 사진촬영이 시작됩니다.\n\n촬영완료시 촬영된 사진은 바로 \'김형석교수님 연구실\' 서버 컴퓨터로 업로드 되어지니 신중하게 찍어주시기 부탁드립니다.\n\nYou can take a picture by clicking the picture in the list.\n\nWhen the picture is taken, the picture will be uploaded directly to the server computer of \'Professor Kim Hyung-seok\'.\n\nPlease take it carefully.");
        builder.setCancelable(false);
        final EditText et = new EditText(MainActivity.this);
        et.setHint("이름을 입력하세요. Text your name here.");
        et.setTextSize(12f);
        et.setPadding(20, 10, 20, 10);
        et.setBackgroundColor(Color.WHITE);
        builder.setView(et);
        builder.setPositiveButton("알겠습니다(I see)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (et.getText().length() > 0) {
                    preferences2 = getSharedPreferences("info", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences2.edit();
                    editor.putString("name", et.getText().toString().trim());
                    editor.apply();
                } else {
                    Toast.makeText(MainActivity.this, "이름을 입력하고 다시 동의 해주세요.\nAfter text your name, and agree", Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });
        builder.setNegativeButton("동의안함(I do not agree)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();

        final ListView list = findViewById(R.id.list);

        //set adapter.
        ListAdapter adapter = new ListAdapter(MainActivity.this);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Toast.makeText(MainActivity.this, ((listItem) list.getAdapter().getItem(position)).getName(), Toast.LENGTH_LONG).show();

                //save the current item info.
                preferences = getSharedPreferences("data", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("name", ((listItem) list.getAdapter().getItem(position)).getName());
                editor.putString("path", ((listItem) list.getAdapter().getItem(position)).getPath());
                editor.apply();


                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setPositiveButton("카메라로 직접촬영\n(Direct shot with the camera)", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //  take a picture.
                        String url = "tmp_" + System.currentTimeMillis() + ".jpg";
                        mImageCaptureUri = FileProvider.getUriForFile(MainActivity.this, "com.phone.plantscollect.fileprovider", new File(Environment.getExternalStorageDirectory(), url));
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                        startActivityForResult(intent, TAKE_PICTURE);


                    }
                });
                builder1.setNegativeButton("갤러리에서 가져오기\n(Import from gallery)", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // import from gallery

                        try {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent, IMPORT_GALLERY);
                        } catch (ActivityNotFoundException e) {
                            Log.e("tag", "No gallery: " + e);
                        }


                    }
                });
                builder1.setTitle("업로드 방법 선택(SELECT)");
                builder1.show();


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    Uri image = mImageCaptureUri;
//                    Uri image = data.getData();
                    Bitmap img_data = decodeUri(MainActivity.this, image, 1200);
                    File img = saveBitmapToPng(MainActivity.this, preferences.getString("name", "null") + "_" + Calendar.getInstance().getTimeInMillis() + "_" + preferences2.getString("name", "null"), img_data);

                    uploadImage(preferences.getString("path", ""), img);

                }
            } else if (requestCode == IMPORT_GALLERY && resultCode == Activity.RESULT_OK) {
                if (data != null) {

                    Uri image = data.getData();
                    Bitmap img_data = decodeUri(MainActivity.this, image, 1200);
                    File img = saveBitmapToPng(MainActivity.this, preferences.getString("name", "null") + "_" + Calendar.getInstance().getTimeInMillis() + "_" + preferences2.getString("name", "null"), img_data);

                    uploadImage(preferences.getString("path", ""), img);

                }

            } else {
                Toast.makeText(this, "촬영된 사진데이터가 없습니다.(FAIL, retry)", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "알수없는 오류 발생.(FAIL, retry)", Toast.LENGTH_SHORT).show();
        }


    }


    public static File saveBitmapToPng(Context context, String name, Bitmap bitmap) {

        File storage = context.getCacheDir();
        String fileName = name + ".png";

        File tempFile = new File(storage, fileName);

        try {
            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new File(tempFile.getAbsolutePath());
    }

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (width_tmp / 2 >= requiredSize && height_tmp / 2 >= requiredSize) {
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }


    public void uploadImage(final String path, final File file) {
        dialog = ProgressDialog.show(MainActivity.this, "", "Uploading. Please wait...", true);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Toast.makeText(MainActivity.this, "저장 성공.(Image saved)", Toast.LENGTH_SHORT).show();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {

                Looper.prepare();

                SftpUtil sft = new SftpUtil();
                sft.init();

//                InputStream stream = sft.download("/home/user01/Downloads/", "vgg19.ipynb");
//                InputStream stream = sft.download("/data1/LJH/JWA_PFS/dataset/histogram/recur1/", "PFS_9.866666667.png");
//                final Bitmap bmp = BitmapFactory.decodeStream(stream);
                //업로드.
                boolean sc_uploaded = sft.upload(path, file);
                if (sc_uploaded) {
                    dialog.dismiss();
                }

                sft.disconnect();

            }
        }).start();
    }


}
