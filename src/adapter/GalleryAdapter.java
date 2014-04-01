package adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.factory2.R;
import com.example.factory2.UploadFiles;
import com.manuelpeinado.multichoiceadapter.MultiChoiceBaseAdapter;

import database.DatabaseHandler;
import database.Image;

public class GalleryAdapter extends MultiChoiceBaseAdapter {
    private List<Building> buildings;
    private Context context;
    public GalleryAdapter(Bundle savedInstanceState, List<Building> buildings, Context context) {
        super(savedInstanceState);
        this.buildings = buildings;
        this.context = context;
    }

    @SuppressLint("NewApi")
	@Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.feed, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
    	if (item.getItemId() == R.id.menu_share) {
//    		new RetreiveFeedTask(context).execute();
    		uploadSelectedItems();
            return true;
        }
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public int getCount() {
        return buildings.size();
    }

    @Override
    public Building getItem(int position) {
        return buildings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    protected View getViewImpl(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            int layout = R.layout.activity_gallery_adapter;
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
        }
        ImageView imageView = (ImageView) convertView;
        Building building = getItem(position);
        imageView.setImageDrawable(building.photo);
        return imageView;
    }
    
    
    private void uploadSelectedItems() {
//    	new RetreiveFeedTask(context).execute();
        // http://stackoverflow.com/a/4950905/244576
        List<Long> positions = new ArrayList<Long>(getCheckedItems());
        Collections.sort(positions, Collections.reverseOrder());
        
        List<String> image_url = new ArrayList<String>();
        for (long position : positions) {
        	image_url.add(buildings.get((int) position).name);
        }
        new RetreiveFeedTask(context,image_url).execute();
//        finishActionMode();
    }
    
    class RetreiveFeedTask extends AsyncTask<String, Void, Boolean> {

		private Context context;
		private Exception exception;
		private ProgressDialog dialog;
		private DatabaseHandler db;
		private List<String> image_url;
	    public RetreiveFeedTask(Context context, List<String> image_url){
			this.context = context;
			this.db = new DatabaseHandler(context);
			this.image_url = image_url;
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
	        	for(String url : image_url){
	        		Image img = db.getImage(url);
	        		new UploadFiles().upload(img.get_name(), img.get_comment());
	        	}
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
	    	finishActionMode();
	    }
	}
    
}