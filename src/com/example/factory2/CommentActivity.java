package com.example.factory2;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import database.DatabaseHandler;
import database.Image;

public class CommentActivity extends Activity {
	
	private WebView myWebView;
	private ImageView imageView;
	private EditText editView;
	private Button uploadBtn;
	private Context context;
	private File imgFile;
	private DatabaseHandler db;
	
	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
		db = new DatabaseHandler(this);
		imageView = (ImageView) findViewById(R.id.image);
		editView = (EditText) findViewById(R.id.text);
		Button uploadBtn = (Button) findViewById(R.id.uploadBtn);
		
		Intent intent = getIntent();
		String url = intent.getExtras().getString("url");
		String comment = intent.getExtras().getString("comment");
		editView.setText(comment);

		imgFile = new  File(url);
		if(imgFile.exists()){

		    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

		    imageView.setImageBitmap(myBitmap);

		}
		Log.e("ImageFile", imgFile.getAbsolutePath());
		context = this;
		
		uploadBtn.setOnClickListener(new View.OnClickListener() {
        	
            @Override
            public void onClick(View v) {
//            	new RetreiveFeedTask(context).execute();
            	
            	new AlertDialog.Builder(CommentActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm To Complete")
                .setMessage("Do you really want to save this comment?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    	Image img = new Image(imgFile.getAbsolutePath());
                    	img.set_comment(editView.getText().toString());
                        db.updateComment(img);
                        finish();    
                    }

                })
                .setNegativeButton("no", null)
                .show();
            	
            	
            	
            	
            }
        });

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment, menu);
		return true;
	}
	
	

}
