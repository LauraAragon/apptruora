package com.example.lauri.truoraapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ServersActivity extends AppCompatActivity {

    private EditText dominio, serversInfo;
    private Button go;
    private RequestQueue colaPeticionesHTTP;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servers);
        setwidgets();
        setGoButton();
    }

    private void setwidgets() {
        dominio = findViewById(R.id.editText_Dominio);
        serversInfo = findViewById(R.id.editText_ServerInfo);
        go = findViewById(R.id.button_Go);
        colaPeticionesHTTP = Volley.newRequestQueue(this);
    }

    public void launchMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void setGoButton(){
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoDominio = dominio.getText().toString();
                if (Patterns.WEB_URL.matcher(textoDominio).matches()){
                    searchForDomain();
                } else {
                    Toast.makeText(getApplicationContext(),"Type a Valid Domain!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void searchForDomain() {
        String textoDominio = dominio.getText().toString();
        //System.out.println("-------------"+textoDominio+"-------------");
        String url = "http://144.217.243.174:3002/servers/" + textoDominio;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    progressDialog.dismiss();
                    serversInfo.setText(response.toString(2));
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error","Error: "+error.getLocalizedMessage());
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Couldn't find any info :(",Toast.LENGTH_LONG).show();
                serversInfo.setText("");
            }
        });

        colaPeticionesHTTP.add(request);
        progressDialog = ProgressDialog.show(ServersActivity.this, "Searching", "Domain info is coming", true);

    }
}
