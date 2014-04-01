package com.example.factory2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import adapter.Building;
import adapter.GalleryAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import database.DatabaseHandler;
import database.Image;

public class ManageActivity extends ActionBarActivity {

	private Intent intent;
	private WebView myWebView;
	
	private File[] listFile;
	private List<File> list = new ArrayList<File>();
	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
	private GalleryAdapter adapter;
	private DatabaseHandler db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage);
		
		db = new DatabaseHandler(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}
		
		
		for (Image img : db.getAllImages()) {
            String log = "Name: "+img.get_name()+" ,Comment: " + img.get_comment() + " ,Upload: " + img.get_upload();
            Log.d("Name: ", log);

        }
		
		getImages();
//		GridView gridview = (GridView) findViewById(R.id.gridview);
//	    gridview.setAdapter(new ImageAdapter(this, list));
//
//	    gridview.setOnItemClickListener(new OnItemClickListener() {
//	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//	        	intent = new Intent(ManageActivity.this, CommentActivity.class);
//	        	intent.putExtra("url", list.get(position).getAbsolutePath());
//	        	startActivity(intent);
//	        }
//	    });
	    
//	    new
	    GridView gridView = (GridView)findViewById(android.R.id.list);
		List<Building> buildings = new ArrayList<Building>();
		
		Resources res = this.getResources();
		TypedArray icons = res.obtainTypedArray(R.array.photos);
		
		for(int i=0;i<list.size();i++){
			File imgFile = new  File(list.get(i).getAbsolutePath());
			db.addImage(new Image(list.get(i).getAbsolutePath()));
			if(imgFile.exists()){
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 8;
				Bitmap myBitmap = ImageAdapter.decodeFile(imgFile);
				buildings.add(new Building(list.get(i).getAbsolutePath(), new BitmapDrawable(getResources(),myBitmap)));
			}
		}
		
		adapter = new GalleryAdapter(savedInstanceState, buildings, this);
		adapter.setAdapterView(gridView);
		adapter.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		    	intent = new Intent(ManageActivity.this, CommentActivity.class);
		    	
	        	intent.putExtra("url", list.get(position).getAbsolutePath());
	        	Image g = db.getImage(list.get(position).getAbsolutePath());
	        	intent.putExtra("comment", g.get_comment());
	        	startActivity(intent);
		    }
		});
	    
		
	}
	
	private String getAlbumName() {
		return getString(R.string.album_name);
	}

	@SuppressLint("NewApi")
	public void getImages()
	{
	    File file= mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

	        if (file.isDirectory())
	        {
	            listFile = file.listFiles();
	            
	            for(int i=0; i<listFile.length; i++){
	            	
	            	if(listFile[i].getAbsoluteFile().length() != 0){
	            		list.add(listFile[i]);
	            	}
	            	else{
	            		listFile[i].getAbsoluteFile().delete();
	            	}
	            }
	        }
	        
	}

	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage, menu);
		return true;
	}
	
	
	

}
