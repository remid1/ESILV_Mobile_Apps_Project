package com.example.bank.model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bank.Database.AppDatabase;
import com.example.bank.Entity.Login;
import com.example.bank.R;
import com.example.bank.dao.LoginDAO;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText editTextLogin;
    Button buttonLogin;


    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextLogin = (EditText)findViewById(R.id.editTextLogin);
        buttonLogin=(Button)findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginActivity.RequestAsyncTask().execute();
            }
        });
    }


    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }
    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] textBytes = text.getBytes("iso-8859-1");
        md.update(textBytes, 0, textBytes.length);
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }


    public class RequestAsyncTask extends AsyncTask<Void,String,String> {
        @Override
        protected String doInBackground(Void... params) {
            String hash="";
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
            LoginDAO lDao = db.loginDao();
            List<Login> l = lDao.getAll();
            if(l.isEmpty()){
                try {
                    lDao.insert(new Login(SHA1(editTextLogin.getText().toString())));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            };
            l = lDao.getAll();
            for(Login a: l){
                hash=a.getHash();
            }
            return hash;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new CheckAsyncTask().execute(result);
        }
    }
    public class CheckAsyncTask extends AsyncTask<String,Void, Boolean> {

        private Boolean checkPassword(String hash) throws UnsupportedEncodingException, NoSuchAlgorithmException {
            String password= editTextLogin.getText().toString();
            String passwordhash=SHA1(password);
            return passwordhash.equals(hash);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean result = Boolean.FALSE;
            try {
                result = checkPassword(params[0]);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                Toast.makeText(getBaseContext(), "authentification success", Toast.LENGTH_SHORT).show();
                Intent activity2Intent = new Intent(getApplicationContext(), DisplayAccounts.class);
                startActivity(activity2Intent);
            }
            else{
                Toast.makeText(getBaseContext(), "wrong password", Toast.LENGTH_SHORT).show();
            }

        }
    }



}