package id.aldes.uploaddoc;

import android.Manifest;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import okhttp3.*;
//import okhttp3.MediaType.Companion.toMediaTypeOrNull;
//import okhttp3.RequestBody.Companion.asRequestBody;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


public class aldes
{
    
    public static String HaiDunia()
    {
        return "Hai Dunia";
    }

    public static String BerhasilUpload()
    {
        return "Berhasil Upload";
    }

    public static String GagalUpload()
    {
        return "Gagal Upload";
    }

    public static void printHasil(boolean operandHasil, TextView operandTextView1)
    {
        if(operandHasil)
        {
            operandTextView1.setText(BerhasilUpload());
        }
        else
        {
            operandTextView1.setText(GagalUpload());
        }

    }

    public static void cekIzinBacaStorage(Activity operandActivity, int PERMISSION_EXTERNAL_STORAGE)
    {
        ActivityCompat.requestPermissions(operandActivity,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_EXTERNAL_STORAGE);
    }

    public static File fileDeclaration(String imagePath)
    {
        return new File(imagePath);
    }

    public static boolean doUpload( File sourceFile1,
                                    String imagePath1,
                                    String uploadURL1,
                                    String fileName1
    )
    {
        Log.e("aldesResponse", "init :"+ imagePath1+" uploadURL1:"+uploadURL1+" fileName1:"+fileName1);
        if (!sourceFile1.isFile())
        {
            Log.e("aldesResponse", "Source File not exist :"+ imagePath1);
            return false;
        }
        else
        {
            try
            {
                //declaration-part
                int bytesAvailable;
                int bufferSize;
                byte[] buffer;
                int bytesRead;
                int serverResponseCode;
                DataOutputStream DataOutputStream1;
                HttpURLConnection Connection1 = null;
                int maxBufferSize = 1024 * 1024;
                String  boundary1 = "*****" ,
                        twoHyphens1 =  "--",
                        lineEnd1 = "\r\n";

                URL url = new URL(uploadURL1);
                FileInputStream fileInputStream = new FileInputStream(sourceFile1);
                Connection1 = (HttpURLConnection) url.openConnection();

                // Open a HTTP  connection to  the URL
                Connection1.setDoInput(true);
                Connection1.setDoOutput(true); // Allow Outputs
                Connection1.setUseCaches(false); // Don't use a Cached Copy
                Connection1.setRequestMethod("POST");
                Connection1.setRequestProperty("Connection", "Keep-Alive");
                Connection1.setRequestProperty("ENCTYPE", "multipart/form-data");
                Connection1.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary1);
                Connection1.setRequestProperty("uploaded_file", fileName1);

                DataOutputStream1 = new DataOutputStream(Connection1.getOutputStream());
                DataOutputStream1.writeBytes(twoHyphens1 + boundary1 + lineEnd1);
                DataOutputStream1.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\""
                        + fileName1 + "\"" + lineEnd1);
                DataOutputStream1.writeBytes(lineEnd1);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0)
                {
                    DataOutputStream1.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                DataOutputStream1.writeBytes(lineEnd1);
                DataOutputStream1.writeBytes(twoHyphens1 + boundary1 + twoHyphens1 + lineEnd1);
                // Responses from the server (code and message)
                serverResponseCode = Connection1.getResponseCode();
                String serverResponseMessage = Connection1.getResponseMessage();
                Log.i(  "aldesResponse", 
                        "HTTP Response is : "
                        + serverResponseMessage 
                        + ": " 
                        + serverResponseCode);
                
                fileInputStream.close();
                DataOutputStream1.flush();
                DataOutputStream1.close();
                return true;
            }
            catch (MalformedURLException ex)
            {
                ex.printStackTrace();
                Log.e("aldesResponse", "error: " + ex.getMessage(), ex);
                return false;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        } // End else block

    }

}
