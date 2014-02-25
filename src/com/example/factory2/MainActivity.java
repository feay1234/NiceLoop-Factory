package com.example.factory2;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private static final int CAMERA_REQUEST = 1888; 
	private static final int REQUEST_TAKE_PHOTO = 1;
	private static final int CAMERA_PIC_REQUEST = 1111;
	private static final int ACTION_TAKE_PHOTO_B = 1;
	
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
	
	private ImageView mImage;
	public String mCurrentPhotoPath;
	private Intent intent;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mImage = (ImageView) findViewById(R.id.camera_image);
		WebView myWebView = (WebView) findViewById(R.id.webview);
		myWebView.loadUrl("file:///android_asset/index.html");
		
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		myWebView.addJavascriptInterface(this, "Android");
		
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}
	}
	
	
	
	@JavascriptInterface
    public void displayFeedActivity() {
    	intent = new Intent(this, GalleryPage.class);
    	startActivity(intent);
    }
	
	@JavascriptInterface
    public void displayManageActivity() {
    	intent = new Intent(this, ManageActivity.class);
    	startActivity(intent);
    }
	
	@JavascriptInterface
	public void displayCamera() {
		dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);

    	
	}
	
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
	{
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // create a matrix for the manipulation
	    Matrix matrix = new Matrix();
	    // resize the bit map
	    matrix.postScale(scaleWidth, scaleHeight);
	    // recreate the new Bitmap
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		
		
		// Get the source image's dimensions
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, options);
		int srcWidth = options.outWidth;
		int srcHeight = options.outHeight;
		int desiredWidth = 400;
		// Only scale if the source is big enough. This code is just trying to fit a image into a certain width.
		if(desiredWidth  > srcWidth)
		    desiredWidth = srcWidth;



		// Calculate the correct inSampleSize/scale value. This helps reduce memory use. It should be a power of 2
		// from: http://stackoverflow.com/questions/477572/android-strange-out-of-memory-issue/823966#823966
		int inSampleSize = 1;
		while(srcWidth / 2 > desiredWidth){
		    srcWidth /= 2;
		    srcHeight /= 2;
		    inSampleSize *= 2;
		}

		float desiredScale = (float) desiredWidth / srcWidth;

		// Decode with inSampleSize
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inSampleSize = inSampleSize;
		options.inScaled = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap sampledSrcBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);

		// Resize
		Matrix matrix = new Matrix();
		matrix.postScale(desiredScale, desiredScale);
		Bitmap scaledBitmap = Bitmap.createBitmap(sampledSrcBitmap, 0, 0, sampledSrcBitmap.getWidth(), sampledSrcBitmap.getHeight(), matrix, true);
		sampledSrcBitmap = null;

		// Save
		File file;
		try {
			file = createImageFile();
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file.getAbsolutePath());
			scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
			scaledBitmap = null;
			file = new File(mCurrentPhotoPath);
			file.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

//		if (requestCode == -1) {
            //2
//            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");  
//            mImage.setImageBitmap(thumbnail);
//            
//            
//            Bitmap resized = Bitmap.createScaledBitmap(thumbnail, 400, 400, true);
//            //3
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            //4
////            thumbnail.compress(Bitmap.CompressFormat.PNG, 90, bytes);
//
//            resized.compress(Bitmap.CompressFormat.PNG, 100, bytes);
//            /* Write bitmap to file using JPEG and 80% quality hint for JPEG. */
//            
//            try {
//            	 File file = createImageFile();
//                file.createNewFile();
//                FileOutputStream fo = new FileOutputStream(file);
//                //5
//                fo.write(bytes.toByteArray());
//                fo.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//		if (resultCode == -1) {
//			handleBigCameraPhoto();
//			dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
//		}
		
	}
	
	private void handleBigCameraPhoto() {

		if (mCurrentPhotoPath != null) {
			setPic();
			galleryAddPic();
			mCurrentPhotoPath = null;
		}

	}
	
	private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		int targetW = mImage.getWidth();
		int targetH = mImage.getHeight();

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		
		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);	
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		bitmap.compress(CompressFormat.JPEG, 10, bos);
		/* Associate the Bitmap to the ImageView */
		mImage.setImageBitmap(bitmap);
	}

	private void galleryAddPic() {
		    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
			File f = new File(mCurrentPhotoPath);
		    Uri contentUri = Uri.fromFile(f);
		    mediaScanIntent.setData(contentUri);
		    this.sendBroadcast(mediaScanIntent);
	}
	
	/* Photo album for this application */
	private String getAlbumName() {
		return getString(R.string.album_name);
	}
	
	private File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			
			storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (! storageDir.mkdirs()) {
					if (! storageDir.exists()){
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}
			
		} else {
			Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}
		
		return storageDir;
	}
	
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}
	
	private File setUpPhotoFile() throws IOException {
		
		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();
		
		return f;
	}
	
	private void dispatchTakePictureIntent(int actionCode) {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		switch(actionCode) {
		case ACTION_TAKE_PHOTO_B:
			File f = null;
			
			try {
				f = setUpPhotoFile();
				mCurrentPhotoPath = f.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
				
				
				
//				BufferedImage originalImage = ImageIO.read(f);
//				BufferedImage scaledImg = Scalr.resize(originalImage, 400);	
//				ImageIO.write(scaledImg, "png", new File(mCurrentPhotoPath)); 
			} catch (IOException e) {
				e.printStackTrace();
				f = null;
				mCurrentPhotoPath = null;
			}
			break;

		default:
			break;			
		} // switch
		Log.e("test","dispatch check");
		startActivityForResult(takePictureIntent, actionCode);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
