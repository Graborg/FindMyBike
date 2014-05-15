package com.example.findmybike.app;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;


public class MapActivity extends Activity {

    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        Intent intent = getIntent();
        LatLng BikePosition = intent.getParcelableExtra("BikePosition");
        final View mapView = findViewById(R.id.map);
        final View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        Marker bikePosition = map.addMarker(new MarkerOptions()
                .position(BikePosition)
                .title("Your bike")
                .snippet("Nice bike")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.maplogo)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(BikePosition, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
        map.setMyLocationEnabled(true);
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0,0,30,300);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    public void openDir(View v){
        Intent intent = new Intent(this, DirectionActivity.class);
        startActivity(intent);
    }



}
