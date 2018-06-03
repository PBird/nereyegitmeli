package com.hghgh.sergen.nereye_gitmeli;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YerSecimi extends AppCompatActivity {

    String url = "http://nereye.ekobalik.xyz/getJson/sehirler/";

    Button btn_Secimler[],btn_start;
    ArrayList<String>[][]  mekanlar =  new ArrayList[4][6];
    private ProgressDialog pdialog;
    boolean[] filled = new boolean[]{false,false, false ,false};

    String sehirId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yer_secimi);

        final Bundle extra = getIntent().getExtras();
        sehirId = extra.getString("sehirId");

        btn_Secimler = new Button[4];
        btn_Secimler[0] = findViewById(R.id.btn_tar);
        btn_Secimler[1] = findViewById(R.id.btn_otel);
        btn_Secimler[2] = findViewById(R.id.btn_gez);
        btn_Secimler[3] = findViewById(R.id.btn_res);
        btn_start = findViewById(R.id.btn_start);

        for(int i = 0; i < 4; i++)  {
            for (int j = 0;j<6;j++)
                mekanlar[i][j] = new ArrayList<>();
        }
        btnSecimlerInit();

        btn_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YerSecimi.this,GMap.class);

                Bundle extras = new Bundle();
                // Secilen Tarihi Yerler
                int CountTar = mekanlar[0][3].size();
                ArrayList<String> secilenTarPositions = new ArrayList<>();
                ArrayList<String> secilenTarIsim = new ArrayList<>();
                for(int i=0;i<CountTar;i++)
                  if(mekanlar[0][5].get(i) == "1")
                  {
                      secilenTarPositions.add(mekanlar[0][3].get(i)); // position
                      secilenTarIsim.add(mekanlar[0][1].get(i)); // isim
                  }

                extras.putStringArrayList("secilenTarPositions",secilenTarPositions);
                extras.putStringArrayList("secilenTarIsim",secilenTarIsim);

                  //Secilen Oteller

                int CountOtel = mekanlar[1][3].size();
                ArrayList<String> secilenOtelPositions = new ArrayList<>();
                ArrayList<String> secilenOtelIsim = new ArrayList<>();
                for(int i=0;i<CountOtel;i++)
                    if(mekanlar[1][5].get(i) == "1")
                    {
                        secilenOtelPositions.add(mekanlar[1][3].get(i)); // position
                        secilenOtelIsim.add(mekanlar[1][1].get(i)); // isim
                    }

                extras.putStringArrayList("secilenOtelPositions",secilenOtelPositions);
                extras.putStringArrayList("secilenOtelIsim",secilenOtelIsim);

                // Gezilecek yerler

                int CountGez = mekanlar[2][3].size();
                ArrayList<String> secilenGezPositions = new ArrayList<>();
                ArrayList<String> secilenGezIsim = new ArrayList<>();
                for(int i=0;i<CountGez;i++)
                    if(mekanlar[2][5].get(i) == "1")
                    {
                        secilenGezPositions.add(mekanlar[2][3].get(i)); // position
                        secilenGezIsim.add(mekanlar[2][1].get(i)); // isim
                    }

                extras.putStringArrayList("secilenGezPositions",secilenGezPositions);
                extras.putStringArrayList("secilenGezIsim",secilenGezIsim);

                // Restoranlar

                int CountRes = mekanlar[3][3].size();
                ArrayList<String> secilenResPositions = new ArrayList<>();
                ArrayList<String> secilenResIsim = new ArrayList<>();
                for(int i=0;i<CountRes;i++)
                    if(mekanlar[3][5].get(i) == "1")
                    {
                        secilenResPositions.add(mekanlar[3][3].get(i)); // position
                        secilenResIsim.add(mekanlar[3][1].get(i)); // isim
                    }

                extras.putStringArrayList("secilenResPositions",secilenResPositions);
                extras.putStringArrayList("secilenResIsim",secilenResIsim);





                intent.putExtras(extras);
                startActivity(intent);

            }
        });

    }
    private void btnSecimlerInit() {
        btn_Secimler[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pdialog = new ProgressDialog(YerSecimi.this);
                pdialog.setMessage("Loading....");
                pdialog.show();

                fillMekanJson("tarihiyer","Tarihi Yer",0);
            }
        });

        btn_Secimler[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pdialog = new ProgressDialog(YerSecimi.this);
                pdialog.setMessage("Loading....");
                pdialog.show();

                fillMekanJson("otel","Otel",1);
            }
        });


        btn_Secimler[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pdialog = new ProgressDialog(YerSecimi.this);
                pdialog.setMessage("Loading....");
                pdialog.show();

                fillMekanJson("gezilecekyer","Gezilecek Yer",2);
            }
        });


        btn_Secimler[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pdialog = new ProgressDialog(YerSecimi.this);
                pdialog.setMessage("Loading....");
                pdialog.show();

                fillMekanJson("restoran","Restoran",3);
            }
        });


    }

    private void fillMekanJson(String mekan,String title,int filledNo) {

        final String t= title;
        final int no =filledNo;
        mekan = url  + sehirId +'/'+ mekan;
        Log.i("url",url);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonsehirler = new JsonArrayRequest(
                Request.Method.GET,
                mekan,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON

                        try{
                            if(!filled[no])
                            {

                                filled[no]=true;
                                // Loop through the array elements
                                for(int i=0;i<response.length();i++){
                                    // Get current json object
                                    JSONObject sehir = response.getJSONObject(i);

                                    // Get the current  (json object) data
                                    String id = sehir.getString("id");
                                    String isim = sehir.getString("isim");
                                    String aciklama = sehir.getString("kisaaciklama");
                                    String position = sehir.getString("position");
                                    String adres = sehir.getString("markerAddress");

                                    // Display the formatted json data in text view
//

                                    mekanlar[no][0].add(id);
                                    mekanlar[no][1].add(isim);
                                    mekanlar[no][2].add(aciklama);
                                    mekanlar[no][3].add(position);
                                    mekanlar[no][4].add(adres);
                                    mekanlar[no][5].add("0");


                                    Log.i("say " , mekanlar[no][1].size()+ "");


                                }
                                pdialog.dismiss();

                            }


                            // start

                            AlertDialog.Builder builder = new AlertDialog.Builder(YerSecimi.this);

                            //Log.i("mekan",mekanlar[5].get(0));

                            final boolean[] checked =  new boolean[mekanlar[no][5].size()];
                            for(int i = 0;i<mekanlar[no][5].size();i++){
                                if(mekanlar[no][5].get(i) == "1")
                                    checked[i] = true;
                                else
                                    checked[i]=false;
                            }

                            String[] mekans = new String[mekanlar[no][1].size()];

                            for(int i = 0;i<mekanlar[no][1].size();i++){
                                mekans[i] = mekanlar[no][1].get(i);
                            }



                            builder.setMultiChoiceItems(mekans,checked, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                                    // Update the current focused item's checked status
                                    checked[which] = isChecked;
                                    if(isChecked)
                                        mekanlar[no][5].set(which,"1") ;
                                    else
                                        mekanlar[no][5].set(which,"0");


                                }
                            });

                            builder.setTitle(t+" Seçin?");

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do something when click positive button

                                }
                            });

                            AlertDialog dialog = builder.create();
                            // Display the alert dialog on interface
                            dialog.show();
                            pdialog.dismiss();

                            //end




                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Toast.makeText(getApplicationContext(), "Hata oluştu İnternet Bağlantınızı kontrol edin!!(sehir)", Toast.LENGTH_SHORT).show();
                        pdialog.dismiss();
                    }
                }
        );

        requestQueue.add(jsonsehirler);
    }


}