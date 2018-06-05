package com.hghgh.sergen.nereye_gitmeli;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GMap extends AppCompatActivity {

    TextView tv_otel,tv_gez,tv_tar,tv_res;
    int toplam=0;
    double[] longitude,latitude;
    String[] isimler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmap);


        tv_otel = findViewById(R.id.tv_otel);
        tv_gez = findViewById(R.id.tv_gez);
        tv_tar = findViewById(R.id.tv_tar);
        tv_res = findViewById(R.id.tv_res);

       Bundle extras = getIntent().getExtras();
        toplam = extras.getInt("toplam");
        Log.i("toplam ", " "+toplam);
        longitude = new double[toplam];
        latitude =  new double[toplam];
        isimler = new String[toplam];

       //Oteller

       ArrayList<String> positionsOtel =  extras.getStringArrayList("secilenOtelPositions");
       ArrayList<String> isimOtel =  extras.getStringArrayList("secilenOtelIsim");

       //Restoranlar
        ArrayList<String> positionsRes =  extras.getStringArrayList("secilenResPositions");
        ArrayList<String> isimRes =  extras.getStringArrayList("secilenResIsim");

        //Gezilecek Yerler

        ArrayList<String> positionsGez =  extras.getStringArrayList("secilenGezPositions");
        ArrayList<String> isimGez =  extras.getStringArrayList("secilenGezIsim");

        //Tarihi Yerler

        ArrayList<String> positionsTar =  extras.getStringArrayList("secilenTarPositions");
        ArrayList<String> isimTar =  extras.getStringArrayList("secilenTarIsim");

        // Double cevirme
        int say = 0;
        for(int i =0;i<positionsOtel.size();i++) {
            convertPositions(positionsOtel.get(i), say,isimOtel.get(i));
            say++;
        }
        for(int i =0;i<positionsGez.size();i++) {
            convertPositions(positionsGez.get(i), say,isimGez.get(i));
            say++;
        }
        for(int i =0;i<positionsTar.size();i++) {
            convertPositions(positionsTar.get(i), say,isimTar.get(i));
            say++;
        }
        for(int i =0;i<positionsRes.size();i++) {
            convertPositions(positionsRes.get(i), say,isimRes.get(i));
            say++;
        }

        Log.i("longtitude", Arrays.toString(longitude));
        Log.i("latitude", Arrays.toString(latitude));
        Log.i("isimler", Arrays.toString(isimler));
    }
    private void convertPositions(String position,int i,String isim){
        String p_longitude = "\\((.*?)\\,"
                ,p_latitude="\\,(.*?)\\)";
        Pattern p_lo = Pattern.compile(p_longitude);
        Pattern p_la = Pattern.compile(p_latitude);

        Matcher m_lo = p_lo.matcher(position);
        if(m_lo.find()) {
             longitude[i] = Double.parseDouble(m_lo.group(1));
             isimler[i] = isim ;
        }

        Matcher m_la = p_la.matcher(position);
        if(m_la.find()) {
            latitude[i] = Double.parseDouble(m_la.group(1));
        }

    }
}
