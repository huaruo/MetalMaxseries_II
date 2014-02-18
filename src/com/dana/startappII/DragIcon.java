package com.dana.startappII;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.dana.modulII.Position;

public class DragIcon extends Activity
{
	//Debug
	private final static String TAG = "DragIcon";
	
	private final static int COLUMN = 2;
	private final static int ROW =3;
	
	private int containerWidth, containerHeight, row_width, column_height, unit_width, unit_height;
	
	private RelativeLayout dragRLayout;
	private Map<Position, View> dragViewsMap;
	private List<Position> positions;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		//设置全票
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dragicon);
		
		DisplayMetrics dm = getResources().getDisplayMetrics();
		containerWidth = dm.widthPixels;
		containerHeight = dm.heightPixels - 70;
		
		row_width = containerWidth/COLUMN;
		column_height=containerHeight/ROW;
		
		Log.i(TAG, "container width = " + containerWidth);
		Log.i(TAG, "container height = " + containerHeight);
		Log.i(TAG, "row width = " + row_width);
		Log.i(TAG, "column height = " + column_height);
		
		dragRLayout =(RelativeLayout) findViewById(R.id.dragicon_scenes_rl);
		initDragViews();
		locateViews();
		
	}
	
	private void locateViews()
	{
		for (Position pos: positions)
		{
			locateView(null,pos);
		}
	}
	
	private void initDragViews() {
		positions = new ArrayList<Position>();
		List<Bitmap> bmps = new ArrayList<Bitmap>();
		bmps.add(((BitmapDrawable) (getResources().getDrawable(R.drawable.scene_bedroom))).getBitmap());
		bmps.add(((BitmapDrawable) (getResources().getDrawable(R.drawable.scene_bathroom))).getBitmap());
		bmps.add(((BitmapDrawable) (getResources().getDrawable(R.drawable.scene_diningroom))).getBitmap());
		bmps.add(((BitmapDrawable) (getResources().getDrawable(R.drawable.scene_guestroom))).getBitmap());
		bmps.add(((BitmapDrawable) (getResources().getDrawable(R.drawable.scene_kidsroom))).getBitmap());
		bmps.add(((BitmapDrawable) (getResources().getDrawable(R.drawable.scene_kitchenroom))).getBitmap());
		unit_height = bmps.get(0).getHeight() + 90;
		unit_width = bmps.get(0).getWidth();
		
		Log.d(TAG, "unit_height = " + unit_height);
		Log.d(TAG, "unit_width = " + unit_width);

		dragViewsMap = new HashMap<Position, View>();
		for (int i = 0; i < bmps.size(); i++) {
			Bitmap bmp = bmps.get(i);
			int row = i / COLUMN;
			int Column = i % COLUMN;
			Position pos = new Position(row, Column);

			final RelativeLayout unit_layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.scene_unit_layout,null);
			ImageView drag_img = (ImageView) unit_layout.findViewById(R.id.sul_iv);
			drag_img.setImageBitmap(bmp);

			TextView drag_txt = (TextView) unit_layout.findViewById(R.id.sul_tv);
			drag_txt.setText("drag test " + i);
			
			unit_layout.setOnTouchListener(new OnTouchListener() {

				int lastX, lastY;
				Position originPos=Position.getEmptyPosition();

				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					int action = event.getAction();
					Log.i("TAG", "Touch:" + action);

					// Toast.makeText(DraftTest.this, "位置："+x+","+y,
					// Toast.LENGTH_SHORT).show();

					if (action == MotionEvent.ACTION_DOWN)
					{
						Log.v("Drag", "MotionEvent.ACTION_DOWN");
						lastX = (int) event.getRawX();
						lastY = (int) event.getRawY();
						
						originPos=getTouchPos(lastX, lastY);
						originPos=mappingPosition(originPos);
						
						Log.v("Drag", "lastX = " + lastX + " lastY = " + lastY);

						// v.layout(lastX, lastX/2, lastY, lastY/2);
					}
					/**
					 * layout(l,t,r,b) l Left position, relative to parent t Top
					 * position, relative to parent r Right position, relative
					 * to parent b Bottom position, relative to parent
					 * */
					else if(action== MotionEvent.ACTION_MOVE)
					{
						Log.v("Drag", "MotionEvent.ACTION_MOVE");
						int dx = (int) event.getRawX() - lastX;
						int dy = (int) event.getRawY() - lastY;
						Log.v("Drag", "dx = " + dx + " dy = " + dy);

						int left = v.getLeft() + dx;
						int top = v.getTop() + dy;
						int right = v.getRight() + dx;
						int bottom = v.getBottom() + dy;

						if (left < 0) {
							left = 0;
							right = left + v.getWidth();
						}

						if (right > containerWidth) {
							right = containerWidth;
							left = right - v.getWidth();
						}

						if (top < 0) {
							top = 0;
							bottom = top + v.getHeight();
						}

						if (bottom > containerHeight) {
							bottom = containerHeight;
							top = bottom - v.getHeight();
						}

						v.layout(left, top, right, bottom);

						Log.v("Drag", "position：" + left + ", " + top + ", "
								+ right + ", " + bottom);

						lastX = (int) event.getRawX();
						lastY = (int) event.getRawY();

					}
					else if(action== MotionEvent.ACTION_UP)
					{
						Position pos = getTouchPos(lastX, lastY);
						move(unit_layout, originPos, pos);
					}
					return true;
				}
			});
			positions.add(pos);
			Log.i(TAG, "put "+unit_layout.toString()+" in the dragViewsMap with the position : "+pos.toString());
			dragViewsMap.put(pos, unit_layout);
		}
	}
	
	
    /**根据点击位置获取点击的控件坐标*/
	private Position getTouchPos(int tx,int ty)
	{
		
		int pos_row=ty/column_height;
		int pos_Column=tx/row_width;
		if(pos_row >= ROW-1)
		{
			pos_row=ROW-1;
		}
		if(pos_Column>=COLUMN-1)
		{
			pos_Column=COLUMN-1;
		}
		
		return new Position(pos_row, pos_Column);
	}
	
	/**
	 * 控件换位
	 * */
	private void move(View view,Position fromPos,Position toPos)
	{
		Log.v(TAG, "move().....topos : "+toPos.toString());
		toPos=mappingPosition(toPos);
		
		//判断换位顺序：是往上换位还是往下换位
		int from_index=-1;
		int to_index=-1;
		for(int i=0;i<positions.size();i++)
		{
			Position pos = positions.get(i);
			if(pos.getRow()==fromPos.getRow()&&pos.getColumn()==fromPos.getColumn())
			{
				from_index=i;
			}
			
			if(pos.getRow()==toPos.getRow()&&pos.getColumn()==toPos.getColumn())
			{
				to_index=i;
			}
		}
		if(to_index<from_index) //往上交换
		{
			for(int i=from_index-1;i>=to_index;i--)
			{
				View moving_view = dragViewsMap.get(positions.get(i));
				locateView(moving_view, positions.get(i+1));
				dragViewsMap.put(positions.get(i+1), moving_view);
			}
			//将拖动的控件放到目标坐标位置
			locateView(view, toPos);
			dragViewsMap.put(toPos, view);
		}
		else   //往下交换
		{
			for(int i=from_index+1;i<=to_index;i++)
			{
				View moving_view = dragViewsMap.get(positions.get(i));
				locateView(moving_view, positions.get(i-1));
				dragViewsMap.put(positions.get(i-1), moving_view);
			}
			//将拖动的控件放到目标坐标位置
			locateView(view, toPos);
			dragViewsMap.put(toPos, view);
		}
	}
	
	private Position mappingPosition(Position position)
	{
		for(Position pos : positions)
		{
			if(pos.getRow()==position.getRow()&&pos.getColumn()==position.getColumn())
			{
				return pos;
			}
		}
		return null;
	}
	
	/**定位指定的控件*/
	private void locateView(View view,Position pos)
	{
		Log.v(TAG, "locatView()..."+pos.toString());
		
		boolean isInit=false;
		RelativeLayout.LayoutParams layout_params=null;
		if(view==null)
		{
			isInit=true;
			view = dragViewsMap.get(pos);
			layout_params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		else
		{
			layout_params = (LayoutParams) view.getLayoutParams();
		}
		
		int left_margin=pos.getColumn()*row_width+row_width/2-unit_width/2;
		int top_margin =pos.getRow()*column_height+column_height/2-unit_height/2;
		
		Log.d(TAG, "left_margin = "+left_margin);
		Log.d(TAG, "top_margin = "+top_margin);
		
		layout_params.setMargins(left_margin, top_margin, 0, 0);
		view.setLayoutParams(layout_params);
		if(isInit)
		{
			dragRLayout.addView(view);
		}
	}
}