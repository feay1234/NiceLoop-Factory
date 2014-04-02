package niceloop.th.factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import niceloop.th.factory.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

public class ImageActivity extends Activity {
	private ImageView mImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		
		mImage = (ImageView) findViewById(R.id.camera_image);
		Intent intent = getIntent();
		Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
		
		mImage.setImageBitmap(bitmap);
		
		String extr = Environment.getExternalStorageDirectory().toString();
        File mFolder = new File(extr + "/MyApp");

        if (!mFolder.exists()) {
            mFolder.mkdir();
        }
        Log.e("image","create folder");
        String strF = mFolder.getAbsolutePath();
        File mSubFolder = new File(strF + "/MyApp-SubFolder");

        if (!mSubFolder.exists()) {
            mSubFolder.mkdir();
        }

        String s = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+".png";

        File f = new File(mSubFolder.getAbsolutePath(),s);
        
        String strMyImagePath = f.getAbsolutePath();
        FileOutputStream fos = null;
        try {
        	Log.e("image", "check");
            fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG,100, fos);

            fos.flush();
            fos.close();
         //   MediaStore.Images.Media.insertImage(getContentResolver(), b, "Screen", "screen");
        }catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image, menu);
		return true;
	}

}
