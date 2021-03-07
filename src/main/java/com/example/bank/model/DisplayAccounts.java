package com.example.bank.model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank.Database.AppDatabase;
import com.example.bank.Entity.Account;
import com.example.bank.R;
import com.example.bank.dao.AccountDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static java.lang.Integer.parseInt;

public class DisplayAccounts extends AppCompatActivity {


    public static native String baseUrlFromJNI();

    static {
        System.loadLibrary("native-lib");
    }
    private AppDatabase db;

    private TextView txt;
    private TextView txt1;
    private Button refreshButton;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_accounts);
        txt=(TextView)findViewById(R.id.textView2);
        txt.setMovementMethod(new ScrollingMovementMethod());
        String url = baseUrlFromJNI()+"/accounts";

        new APITask().execute(url); //requete API et rempli accounts si requete echoue recupere dans la bdd room
        refreshButton = (Button)findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new APITask().execute(url);
            }
        });
        createButton = (Button)findViewById(R.id.buttonCreate);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityIntent = new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivity(activityIntent);
            }
        });
        String url2 = baseUrlFromJNI()+"/config/1";
        txt1 = (TextView)findViewById(R.id.textView);
        new ConfigTask().execute(url2);
    }

    public class ConfigTask extends AsyncTask<String,Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpsURLConnection connection=null;
            BufferedReader reader =null;
            String result ="";
            try{
                URL url =new URL(params[0]);
                connection=(HttpsURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader((stream)));
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line=reader.readLine()) != null){
                    buffer.append(line);
                }
                //READ JSON
                JSONObject config = new  JSONObject(buffer.toString());
                result= "id: "+ config.getString("id") + "\nName: "+ config.getString("name")+"\nlastname: "+config.getString("lastname");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null){
                    connection.disconnect();
                }
                try{
                    if(reader != null){
                        reader.close();
                    }
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
                txt1.setText(result);
            }

        }
    }


    public class APITask extends AsyncTask<String,String, List<Account>> {

        @Override
        protected List<Account> doInBackground(String... params) {
            List<Account> accounts = new ArrayList<Account>();;
            HttpsURLConnection connection=null;
            BufferedReader reader =null;
            try{
                URL url =new URL(params[0]);
                connection=(HttpsURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader((stream)));
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line=reader.readLine()) != null){
                    buffer.append(line);
                }
                //READ JSON
                JSONArray jsonarray =new JSONArray(buffer.toString());
                for(int i=0; i<jsonarray.length(); i++){
                    JSONObject account = jsonarray.getJSONObject(i);
                    int id = parseInt(account.getString("id"));
                    String accountName = account.getString("accountName");
                    String amount = account.getString("amount");
                    String iban = account.getString("iban");
                    String currency = account.getString("currency");
                    accounts.add(new Account(id,accountName,amount,iban,currency));
                }
                return accounts;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null){
                    connection.disconnect();
                }
                try{
                    if(reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return accounts;
        }

        @Override
        protected void onPostExecute(List<Account> result) {
            super.onPostExecute(result);
            if(result.isEmpty()){
                Toast.makeText(getBaseContext(), "Request fail", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getBaseContext(), "Request success", Toast.LENGTH_SHORT).show();
            }
            new AccountAsyncTask().execute(result);
        }
    }

    public class AccountAsyncTask extends AsyncTask<List<Account>,Void,List<Account>> {

        @Override
        protected List<Account> doInBackground(List<Account>... params) {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
            AccountDao aDao = db.accountDao();
            if(!params[0].isEmpty())
            {
                aDao.delete();
                for(Account a: params[0]){
                    aDao.insert(a);
                }
            }
            return aDao.getAll();
        }

        @Override
        protected void onPostExecute(List<Account> result) {
            super.onPostExecute(result);
            String chain="";
            for(Account a: result){
                chain = chain+a.toString()+"\n\n";
            }
            txt.setText(chain);
        }
    }
}