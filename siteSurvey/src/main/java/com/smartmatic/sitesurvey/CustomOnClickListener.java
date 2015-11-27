package com.smartmatic.sitesurvey;

import android.view.View;
import android.view.View.OnClickListener;

public class CustomOnClickListener implements OnClickListener
{

  int position;
  public CustomOnClickListener(int p) {
       this.position = p;
  }

  @Override
  public void onClick(View v)
  {
      //read your lovely variable
  }

}