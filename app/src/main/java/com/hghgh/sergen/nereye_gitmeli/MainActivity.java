package com.hghgh.sergen.nereye_gitmeli;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btn_hazirla;
    Spinner sp_sehirler;
    ImageView img_resim;
    TextView tx_vv;
    ArrayList<String>[] sehirler;
    ArrayList resimler;
    ArrayAdapter sehirAdapter;
    String secilenSehirId;

    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_hazirla = findViewById(R.id.btn_hazirla);
        sp_sehirler = findViewById(R.id.sp_sehirler);
        tx_vv = findViewById(R.id.tx_ss);
        img_resim = findViewById(R.id.img_resim);


        btn_hazirla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,YerSecimi.class);
                intent.putExtra("sehirId",secilenSehirId);
                startActivity(intent);
            }
        });

        sehirler = new ArrayList[3];
        sehirler[0] = new ArrayList<String>(); // id
        sehirler[1] = new ArrayList<String>(); // isim
        sehirler[2] = new ArrayList<String>(); // kisa aciklama

        final String sehirUrl = "http://nereye.ekobalik.xyz/public/getJson/sehirler";
        final String resimUrl = "http://nereye.ekobalik.xyz/public/getimg/sehirler";

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();
        sehirAdapter =new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, sehirler[1]);
        sehirAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_sehirler.setAdapter(sehirAdapter);


        String[] test=new String[]{"test1","test2"};

        fillSehirlerJson(sehirUrl); // sehirleri siteden çeker spinera doldurur


        sp_sehirler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fillImageArray(resimUrl,position); // sehire ait olan resimleri çeker sadece ilk resmi gösterir
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void fillSehirlerJson(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonsehirler = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject sehir = response.getJSONObject(i);

                                // Get the current  (json object) data
                                String id = sehir.getString("id");
                                String isim = sehir.getString("isim");
                                String aciklama = sehir.getString("aciklama");

                                // Display the formatted json data in text view
//

                                sehirler[0].add(id);
                                sehirler[1].add(isim);
                                sehirler[2].add(aciklama);
                                dialog.dismiss();
                                sehirAdapter.notifyDataSetChanged();
                            }
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
                        dialog.dismiss();
                    }
                }
        );

        requestQueue.add(jsonsehirler);
    }

    private void fillImageArray(String resimUrl,int id) {
        secilenSehirId = sehirler[0].get(id);
        resimUrl = resimUrl + '/' + secilenSehirId  ;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonresimler = new StringRequest(
                resimUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String string) {
                        // Do something with response
                        try {
                            JSONObject object = new JSONObject(string);
                            JSONArray imageArray = object.getJSONArray("links");
                            ArrayList resimler = new ArrayList();

                            for(int i = 0; i < imageArray.length(); ++i) {
                                resimler.add(imageArray.getString(i));
                            }
                            // resimler.get(2)

                            new DownloadImageTask(img_resim)
                                    .execute(resimler.get(0)+""); // resmi indirir



                            //

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Toast.makeText(getApplicationContext(), "Hata oluştu İnternet Bağlantınızı kontrol edin(img)!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
        );
        requestQueue.add(jsonresimler);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }



}
