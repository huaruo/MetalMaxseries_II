package com.dana.startappII;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dana.modulII.OnViewChangeListener;
import com.dana.modulII.ScrollLayoutWeiXin;

public class WeiXinSimulation extends Activity implements OnViewChangeListener
{
	//Debug
	private final static String TAG = "WeiXinSimulation";
	
	private ScrollLayoutWeiXin mScrollLayout;
	private ImageView[] imgs;
	private int count;
	private int currentItem;
	private Button startbtn;
	private RelativeLayout mainRLayout;
	private LinearLayout pointLLayout, leftLLayout, rightLLayout, animLLayout;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		//…Ë÷√»´∆¡
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weixinsimulation);
		initView();
	}
	
	private void initView()
	{
		mScrollLayout = (ScrollLayoutWeiXin) findViewById(R.id.wxs_scroll);
		pointLLayout =(LinearLayout) findViewById(R.id.wxs_ll);
		mainRLayout = (RelativeLayout)findViewById(R.id.wxs_rl);
		animLLayout = (LinearLayout) findViewById(R.id.wxs_anim_ll);
		leftLLayout = (LinearLayout) findViewById(R.id.wxs_left_ll);
		rightLLayout = (LinearLayout) findViewById(R.id.wxs_right_ll);
		
		startbtn = (Button) findViewById(R.id.wxs_startbtn);
		startbtn.setOnClickListener(listener);
		
		count = mScrollLayout.getChildCount();
		imgs = new ImageView[count];
		for(int i =0; i<count; i++)
		{
			imgs[i] = (ImageView) pointLLayout.getChildAt(i);
			imgs[i].setEnabled(true);
			imgs[i].setTag(i);
		}
		currentItem = 0;
		imgs[currentItem].setEnabled(false);
		mScrollLayout.SetOnViewChangeListener(this);	
	}
	
	private OnClickListener listener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			switch(v.getId())
			{
				case R.id.wxs_startbtn:
					mScrollLayout.setVisibility(View.GONE);
					pointLLayout.setVisibility(View.GONE);
					animLLayout.setVisibility(View.VISIBLE);
					mainRLayout.setBackgroundResource(R.drawable.whatsnew_bg);
					Animation leftOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left);
					Animation rightOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right);
					
					leftLLayout.setAnimation(leftOutAnimation);
					rightLLayout.setAnimation(rightOutAnimation);
					
					leftOutAnimation.setAnimationListener(animationListener);
					break;
				default:
					break;
					
			}
		}
	};
	
	private AnimationListener animationListener = new AnimationListener()
	{
		@Override
		public void onAnimationStart(Animation animation)
		{
			mainRLayout.setBackgroundColor(R.color.black);
		}
		
		@Override
		public void onAnimationRepeat(Animation animation)
		{
		}
		@Override
		public void onAnimationEnd(Animation animation)
		{
			leftLLayout.setVisibility(View.GONE);
			rightLLayout.setVisibility(View.GONE);
			Intent intent = new Intent(WeiXinSimulation.this, PaintSurfaceView.class);
			WeiXinSimulation.this.startActivity(intent);
			WeiXinSimulation.this.finish();
			overridePendingTransition(R.anim.zoom_out_enter, R.anim.zoom_out_exit);
		}
	};
	
	@Override
	public void OnViewChange(int position)
	{
		setCurrentPoint(position);
	}
	
	private void setCurrentPoint(int position)
	{
		if(position < 0 || position> count -1 || currentItem == position)
		{
			return;
		}
		imgs[currentItem].setEnabled(true);
		imgs[position].setEnabled(false);
		currentItem = position;
	}
}