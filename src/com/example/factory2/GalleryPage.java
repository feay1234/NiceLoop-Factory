package com.example.factory2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import adapter.Building;
import adapter.GalleryAdapter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class GalleryPage extends ActionBarActivity {
	private File[] listFile;	
	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
	private GalleryAdapter adapter;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_gallery_page);

//	    GridView gridview = (GridView) findViewById(R.id.gridview);
//	    gridview.setAdapter(new ImageAdapter(this, null));
//
//	    gridview.setOnItemClickListener(new OnItemClickListener() {
//	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//	            Toast.makeText(GalleryPage.this, "" + position, Toast.LENGTH_SHORT).show();
//	        }
//	    });
	    
	    
	    GridView gridView = (GridView)findViewById(android.R.id.list);
		List<Building> buildings = new ArrayList<Building>();
		
		Resources res = this.getResources();
		TypedArray icons = res.obtainTypedArray(R.array.photos);
		
		
		for (int i = 0; i < icons.length(); ++i) {
			buildings.add(new Building("name: "+i, icons.getDrawable(i)));
        }
		
		adapter = new GalleryAdapter(savedInstanceState, buildings, this);
		adapter.setAdapterView(gridView);
		adapter.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		        Toast.makeText(getApplicationContext(), "Item click: " + adapter.getItem(position).name, Toast.LENGTH_SHORT).show();
		    }
		});
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gallery_page, menu);
		return true;
	}
	
	

}
