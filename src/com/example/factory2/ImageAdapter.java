package com.example.factory2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.example.factory2.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
	private List<File> list;

    public ImageAdapter(Context c, List<File> list) {
        mContext = c;
        this.list = list;
    }

    public int getCount() {
       return list.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        File imgFile = new  File(list.get(position).getAbsolutePath());
		if(imgFile.exists()){
			BitmapFactory.Options options = new BitmapFactory.Options();

			 options.inSampleSize = 8;
//		    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			Bitmap myBitmap = decodeFile(imgFile);

		    imageView.setImageBitmap(myBitmap);

		}
        return imageView;
    }

    static Bitmap decodeFile(File f){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_SIZE=70;

            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
    

	
}