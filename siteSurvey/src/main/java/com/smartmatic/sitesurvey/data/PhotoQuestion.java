package com.smartmatic.sitesurvey.data;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import com.smartmatic.sitesurvey.PhotoIntentActivity;
import com.smartmatic.sitesurvey.R;
import com.smartmatic.sitesurvey.SurveyActivity;
import com.smartmatic.sitesurvey.SurveyAdapter;
import com.smartmatic.sitesurvey.core.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class PhotoQuestion extends Question implements Cloneable {

	private static final int ACTION_TAKE_PHOTO_B = 1;
	private ImageView mImageView;

	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";

	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
	
	private SurveyActivity parentActivity = null;
	
	public PhotoQuestion(String questionName, String questionLabel, String questionAnswer,
			String _fontSize, String _color) {
		super(questionName, questionLabel, questionAnswer, _fontSize, _color);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}
		
		space= 6;
	}
	
	private View currentView;
	@Override
	public View GetView(LayoutInflater inflater, ViewGroup container, SurveyActivity activityRef, SurveyAdapter surveyAdapter, int parentId){
		
		parentActivity = activityRef;
		parentActivity.addOnResultListener(new SurveyActivity.OnResultListener() {
			
			@Override
			public void onResult(int requestCode, int resultCode, Intent data) {
				switch (requestCode) {
				case ACTION_TAKE_PHOTO_B: {
					if (resultCode == Activity.RESULT_OK) {
						//answer = data.getExtras().getString("FILE_PATH");
						handleBigCameraPhoto();
					}
					break;
				}
				}
			}
		});
				
		currentView =  inflater.inflate(R.layout.photo_question,container, false);
				
		TextView label = (TextView)currentView.findViewById(R.id.description);
    	label.setText(this.label);
    	label.setTextSize(fontSize);

    	Typeface tf = Typeface.createFromAsset(activityRef.getAssets(), "fonts/OpenSans-Regular.ttf");
    	label.setTypeface(tf);
    	try{
    		label.setTextColor(Color.parseColor(color));
    	} catch (Exception e) {}
    	
    	mImageView = (ImageView) currentView.findViewById(R.id.picturePreview);
		Button button = (Button)currentView.findViewById(R.id.button1);
    	button.setOnClickListener(new Button.OnClickListener() {
    	    public void onClick(View v) {
    	    	dispatchTakePictureIntent(v.getContext());
    	    }
    	});
    	
    	if(answer != null && !answer.equals("")){
    		setPic();
    	}
		return currentView;	
	}
		
	/* Photo album for this application */
	private String getAlbumName() {
		return "SiteSurvey";
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
		answer = f.getAbsolutePath();
		
		return f;
	}

	private void dispatchTakePictureIntent(Context context) {
		
		Intent takePictureIntent =new Intent(context, PhotoIntentActivity.class);
		
		if(answer == null || answer.equals("")){
			File f = null;
			try {
				f = setUpPhotoFile();
				//answer = f.getAbsolutePath();
			} catch (IOException e) {
				e.printStackTrace();
				f = null;
				answer = null;
			}
		}
		
		Bundle b = new Bundle();
		b.putString("FILE_PATH", answer); 
		takePictureIntent.putExtras(b); 
		
		((Activity) context).startActivityForResult(takePictureIntent,ACTION_TAKE_PHOTO_B);
	}
	
	private void handleBigCameraPhoto() {

		if (answer != null && !answer.equals("")) {
			setPic();
			galleryAddPic();
		}

	}
	
	private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		int targetW = mImageView.getWidth();
		int targetH = mImageView.getHeight();

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(answer, bmOptions);
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
		Bitmap bitmap = BitmapFactory.decodeFile(answer, bmOptions);
		
		/* Associate the Bitmap to the ImageView */
		mImageView.setImageBitmap(bitmap);
		mImageView.setVisibility(View.VISIBLE);
		//mVideoView.setVisibility(View.INVISIBLE);
	}

	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(answer);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    parentActivity.sendBroadcast(mediaScanIntent);
	}

	public Question clone() {
		PhotoQuestion q= new PhotoQuestion(name, label, "", String.valueOf(fontSize), color);
		q.page = page;
		return q;
	}

	@Override
	public void setAnsweredListener(AnsweredListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	public static Question CreateFromXML(XmlPullParser parser) {
				
		String name = parser.getAttributeValue(null, "name");
		String label = parser.getAttributeValue(null, "label");
		String fontSize = parser.getAttributeValue(null, "font-size");
		String color = parser.getAttributeValue(null, "font-color");
		    
		return new PhotoQuestion(name, label, "", fontSize, color);
	}
}
