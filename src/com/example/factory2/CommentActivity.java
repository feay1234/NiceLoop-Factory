package com.example.factory2;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

public class CommentActivity extends Activity {
	
	private WebView myWebView;
	private ImageView imageView;
	private EditText editView;
	private Button uploadBtn;
	private Context context;
	private File imgFile;
	
	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
		imageView = (ImageView) findViewById(R.id.image);
		editView = (EditText) findViewById(R.id.text);
		Button uploadBtn = (Button) findViewById(R.id.uploadBtn);
		
		Intent intent = getIntent();
		String url = intent.getExtras().getString("url");

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
            	Log.e("comment", "check");
            	new RetreiveFeedTask(context).execute();
            }
        });
//		myWebView = (WebView) findViewById(R.id.webview);
//		myWebView.loadUrl("file:///android_asset/comment.html");
//		
//		WebSettings webSettings = myWebView.getSettings();
//		webSettings.setJavaScriptEnabled(true);
//		
//		myWebView.addJavascriptInterface(this, "Android");
//		
//		
//		Intent intent = getIntent();
//		String url = "displayCommentActivity('http://www.artveroni.com/russian/portrait/id_252_arthur_braginsky_portrait_oil_paintings_Marilyn_Monroe_b.jpg";
//
//		myWebView.setWebViewClient(new WebViewClient() {  
//            @Override  
//            public void onPageFinished(WebView view, String url)  
//            {  
//            	myWebView.loadUrl("javascript:setImage(\""+url+"\")");
//
//            }  
//        }); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment, menu);
		return true;
	}
	
	class RetreiveFeedTask extends AsyncTask<String, Void, Boolean> {

		private Context context;
		private Exception exception;
		private ProgressDialog dialog;
		
	    public RetreiveFeedTask(Context context){
			this.context = context;
		}
	    

		/** progress dialog to show user that the backup is processing. */
		/** application context. */
		@Override
		protected void onPreExecute() {
			this.dialog = new ProgressDialog(context);
			this.dialog.setMessage("Please wait, the app is uploading to the server");
			this.dialog.show();
			this.dialog.setCanceledOnTouchOutside(false);
		}
		
	    protected Boolean doInBackground(String... urls) {
	        try {
	        	new UploadFiles().upload(imgFile.getAbsolutePath(), editView.getText().toString());
//	        	new InsertComment().insert(editView.getText().toString(),imgFile.getName());
	        	
	        } catch (Exception e) {
	            this.exception = e;
	            return false;
	        }
	        return true;
	    }

	    @SuppressLint("ShowToast")
		protected void onPostExecute(Boolean success) {
	        
	    	if (dialog.isShowing()) {
				dialog.dismiss();
			}
	    	if(success){
	    		Toast.makeText(context, "Upload complete", Toast.LENGTH_SHORT).show();
	    	}
	    	else{
	    		Toast.makeText(context, "fail to upload", Toast.LENGTH_SHORT).show();
	    	}
	    }
	}

}
