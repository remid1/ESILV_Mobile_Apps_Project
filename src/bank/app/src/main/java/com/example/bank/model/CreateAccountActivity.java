package com.example.bank.model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.bank.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class CreateAccountActivity extends AppCompatActivity {
    EditText editTextAccountName;
    EditText editTextAmount;
    EditText editTextIban;
    EditText editTextCurrency;
    String url;
    Button buttonCreateAccount;

    public static native String baseUrlFromJNI();

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        url =  baseUrlFromJNI()+"/accounts";
        editTextAccountName = (EditText)findViewById(R.id.editTextAccountName);
        editTextAmount = (EditText)findViewById(R.id.editTextAmount);
        editTextIban = (EditText)findViewById(R.id.editTextIban);
        editTextCurrency = (EditText)findViewById(R.id.editTextCurrency);
        buttonCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CreateAccountActivity.PostAPITask().execute(url);
            }
        });
    }

    public class PostAPITask extends AsyncTask<String,Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String accountName= editTextAccountName.getText().toString();
            String amount = editTextAmount.getText().toString();
            String iban = editTextIban.getText().toString();
            String currency=editTextCurrency.getText().toString();
            String result="";
            if(accountName.equals("")==false && amount.equals("")==false && iban.equals("")==false && currency.equals("")==false)
            {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("accountName",accountName);
                    obj.put("amount",amount);
                    obj.put("iban",iban);
                    obj.put("currency",currency);
                    result=obj.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(!result.equals("")){
                HttpsURLConnection connection=null;
                BufferedReader reader =null;
                try{
                    URL url =new URL(params[0]);
                    connection=(HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json; utf-8");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setDoOutput(true);
                    try(OutputStream os = connection.getOutputStream()) {
                        byte[] input = result.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    result = "success";
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(!result.equals("")){
                Toast.makeText(getBaseContext(), "Request send", Toast.LENGTH_SHORT).show();
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
                Intent activityIntent = new Intent(getApplicationContext(), DisplayAccounts.class);
                startActivity(activityIntent);
            }
            else{
                Toast.makeText(getBaseContext(), "Request failed", Toast.LENGTH_SHORT).show();
                Intent activityIntent = new Intent(getApplicationContext(), DisplayAccounts.class);
                startActivity(activityIntent);
            }
        }
    }
}