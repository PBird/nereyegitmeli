package com.hghgh.sergen.nereye_gitmeli;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class GMap extends AppCompatActivity {

    TextView tv_otel,tv_gez,tv_tar,tv_res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmap);

        tv_otel = findViewById(R.id.tv_otel);
        tv_gez = findViewById(R.id.tv_gez);
        tv_tar = findViewById(R.id.tv_tar);
        tv_res = findViewById(R.id.tv_res);

       Bundle extras = getIntent().getExtras();
       //Oteller

       ArrayList<String> positionsOtel =  extras.getStringArrayList("secilenOtelPositions");
       ArrayList<String> isimOtel =  extras.getStringArrayList("secilenOtelIsim");

       for(int i=0;i<positionsOtel.size();i++)
        tv_otel.append((i+1)+" "+ isimOtel.get(i) + " " +positionsOtel.get(i)+'\n');

       //Restoranlar

        ArrayList<String> positionsRes =  extras.getStringArrayList("secilenResPositions");
        ArrayList<String> isimRes =  extras.getStringArrayList("secilenResIsim");

        for(int i=0;i<positionsRes.size();i++)
            tv_res.append((i+1)+" "+ isimRes.get(i) + " " +positionsRes.get(i)+'\n');

        //Gezilecek Yerler

        ArrayList<String> positionsGez =  extras.getStringArrayList("secilenGezPositions");
        ArrayList<String> isimGez =  extras.getStringArrayList("secilenGezIsim");

        for(int i=0;i<positionsGez.size();i++)
            tv_gez.append((i+1)+" "+ isimGez.get(i) + " " +positionsGez.get(i)+'\n');

        //Tarihi Yerler

        ArrayList<String> positionsTar =  extras.getStringArrayList("secilenTarPositions");
        ArrayList<String> isimTar =  extras.getStringArrayList("secilenTarIsim");

        for(int i=0;i<positionsTar.size();i++)
            tv_tar.append((i+1)+" "+ isimTar.get(i) + " " +positionsTar.get(i)+'\n');


    }
}
