package com.smartmatic.sitesurvey;

import com.smartmatic.sitesurvey.PendingListFragment.PSAdapter;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;

@SuppressLint("ValidFragment")
public class MarkerDialogFragment extends DialogFragment {

	private int position = 1;
	public MarkerDialogFragment(int _position) {
		super();
		position = _position;
	}
		
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        /*LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_marker, null);
        builder.setView(view);
        
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        ((TextView)view.findViewById(R.id.title)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.title)).setText(PendingListFragment.psArray.get(position).title);
        */
        builder.setTitle(PendingListFragment.psArray.get(position).title);
        builder.setItems(new CharSequence[]
        		{getActivity().getText(R.string.drive),getActivity().getText(R.string.survey)}
        		, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            	switch (which) {
				case 0:
					PSAdapter.startDrive(position);
					break;
				case 1:
					PSAdapter.startSurvey(position);
					break;

				default:
					break;
				}
        }});
        return builder.create();
    }
    
}