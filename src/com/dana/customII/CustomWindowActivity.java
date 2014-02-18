package com.dana.customII;

import java.util.ArrayList;

import com.dana.customII.CustomMenu.OnMenuItemSelectedListener;
import com.dana.startappII.R;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

public class CustomWindowActivity extends Activity implements OnMenuItemSelectedListener
{
	//Debug
	private static final String TAG = "CustomWindowActivity";
	
	protected TextView cwa_title;
	final protected Activity cwa_activity = this;
	
	private CustomMenu mMenu;
	
	public static final int MENU_ITEM_1 = 1;
	public static final int MENU_ITEM_2 = 2;
	public static final int MENU_ITEM_3 = 3;
	public static final int MENU_ITEM_4 = 4;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_custom_window);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		cwa_title=(TextView)findViewById(R.id.wt_customwindowtitle);
		
		mMenu = new CustomMenu(this,this,getLayoutInflater());
		mMenu.setHideOnSelect(true);
		mMenu.setItemsPerLineInPortraitOrientation(4);
		mMenu.setItemsPerLineInLandscapeOrientation(8);
		//load the menu items
		loadMenuItems();	
	}
	/**
	 * Snarf the menu key.
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_MENU)
		{
			doMenu();
			return true;//always eat it!
		}
		return super.onKeyDown(keyCode, event);
	}
	/**
	 * Load up our menu.
	 */
	private void loadMenuItems()
	{
		//This is kind of a tedious way to load up the menu items.
		//Am sure there is room for improvement.
		ArrayList<CustomMenuItem> menuItems = new ArrayList<CustomMenuItem>();
		CustomMenuItem cmi = new CustomMenuItem();
		cmi.setCaption("电子\n现金");
		cmi.setImageResourceId(R.drawable.icon1);
		cmi.setId(MENU_ITEM_1);
		menuItems.add(cmi);
		cmi = new CustomMenuItem();
		cmi.setCaption("余额\n查询");
		cmi.setImageResourceId(R.drawable.icon2);
		cmi.setId(MENU_ITEM_2);
		menuItems.add(cmi);
		cmi = new CustomMenuItem();
		cmi.setCaption("其他\n交易");
		cmi.setImageResourceId(R.drawable.icon3);
		cmi.setId(MENU_ITEM_3);
		menuItems.add(cmi);
		cmi = new CustomMenuItem();
		cmi.setCaption("商户\n管理");
		cmi.setImageResourceId(R.drawable.icon4);
		cmi.setId(MENU_ITEM_4);
		menuItems.add(cmi);
		if(!mMenu.isShowing())
		{
			try
			{
				mMenu.setMenuItems(menuItems);
			}
			catch(Exception e)
			{
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Egads!");
				alert.setMessage(e.getMessage());
				alert.show();
			}
		}
	}
	
	/**
	 * Toggle our menu on user pressing the menu key.
	 */
	private void doMenu()
	{
		if(mMenu.isShowing())
		{
			mMenu.hide();
		}
		else
		{
			//Note it doesn't matter what widget you send the menu as long as it gets view.
			mMenu.show(findViewById(R.id.wt_customwindowtitle));
		}	
	}
	/**
	 * For the  demo just toast the item selected.
	 */
	@Override
	public void MenuItemSelectedEvent(CustomMenuItem selection)
	{
		Intent intent;
		switch(selection.getId())
		{
			case 1:
				Toast.makeText(this, "This is 1", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(this, "This is 3", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(this, "This is 4", Toast.LENGTH_SHORT).show();
				break;
		}
	}
	
}