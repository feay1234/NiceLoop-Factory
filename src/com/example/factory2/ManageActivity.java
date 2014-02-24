package com.example.factory2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ManageActivity extends Activity {

	private Intent intent;
	private WebView myWebView;
	
	private File[] listFile;
	private List<File> list = new ArrayList<File>();
	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage);
		
		
//		myWebView = (WebView) findViewById(R.id.webview);
//		
//		myWebView.loadUrl("file:///android_asset/manage.html");
//		
//		WebSettings webSettings = myWebView.getSettings();
//		webSettings.setJavaScriptEnabled(true);
//		
//		myWebView.addJavascriptInterface(this, "Android");
//		
//		
//		 
//		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}
		getImages();
		GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new ImageAdapter(this, list));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//	            Toast.makeText(ManageActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        	intent = new Intent(ManageActivity.this, CommentActivity.class);
	        	intent.putExtra("url", list.get(position).getAbsolutePath());
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
