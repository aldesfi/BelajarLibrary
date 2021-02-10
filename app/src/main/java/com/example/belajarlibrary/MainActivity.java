package com.example.belajarlibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URISyntaxException;

import id.aldes.uploaddoc.*;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    TextView URI,tvURL,ERROR;
    String objectUrl = "https://aldes.id/upload/do.php";
    String domain = "https://aldes.id/uploads/";

    public static final int PERMISSION_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //PERMISSION

        aldes.cekIzinBacaStorage(this,PERMISSION_EXTERNAL_STORAGE);

        //FILECHOOSER
        Button button = findViewById(R.id.btnPilihFile);
        URI = findViewById(R.id.filePath);
        ERROR = findViewById(R.id.errorPrint);
        tvURL = findViewById(R.id.tvURL);
        button.setOnClickListener(v ->
        {
            showFileChooser();
        });


    }

    private static final int FILE_SELECT_CODE = 0;

    private void showFileChooser()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
                    startActivityForResult(
                    Intent.createChooser(intent, "Pilih File"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex)
        {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "File Manager Tidak Ditemukan.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == FILE_SELECT_CODE) {
            if (resultCode == RESULT_OK) {
                // Get the Uri of the selected file
                Uri uri = data.getData();
                Log.d(TAG, "File Uri: " + uri.toString());
                // Get the path
                String path = null;
                String filename = null;
                try
                {
                    path = getPath(this, uri);

                }
                catch (URISyntaxException e)
                {
                    e.printStackTrace();
                    ERROR.setText(e.getMessage());
                }

                if(!(path==null))
                {
                    String txtPath = "File Path: " + path;
                    URI.setText(txtPath);

                    int SDK_INT = android.os.Build.VERSION.SDK_INT;

                                if (SDK_INT > 8)
                                {
                                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                            .permitAll().build();
                                    StrictMode.setThreadPolicy(policy);

                                    boolean objectHasil = aldes.doUpload(
                                            aldes.fileDeclaration(path),
                                            path,
                                            objectUrl,
                                            aldes.fileDeclaration(path).getName()
                                    );


                                    TextView textView1 = findViewById(R.id.TextView);
                                    aldes.printHasil(
                                            objectHasil,
                                            textView1
                                    );

                                    if (objectHasil)
                                    {
                                        tvURL.setText(domain + aldes.fileDeclaration(path).getName());
                                        ERROR.setText("");
                                    }

                                }

                    }
                else
                {
                    ERROR.setText("File Manager Not Supported");
                }
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @SuppressLint("Recycle")
    public static String getPath(Context context, Uri uri) throws URISyntaxException
    {
        if ("content".equalsIgnoreCase(uri.getScheme()))
        {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try
            {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst())
                {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
}