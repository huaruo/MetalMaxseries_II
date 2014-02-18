package com.dana.util;

import com.dana.startappII.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;

public class General
{
	//Debug
	private final static String TAG = "General";
	
	/**
	 * ����ProgressDialog
	 * @param context
	 * @param strDispMsg
	 * @return
	 */
	public static ProgressDialog processDialog(final Context context, final String strDispMsg)
	{
		ProgressDialog mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setMessage(strDispMsg);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.show();
		return mProgressDialog;
	}
	
	/**
	 * ����Dialog��ʾ��Ϣ
	 * @param context
	 * @param strTitle
	 * @param strDispMsg
	 */
	public static void msgDialog(final Context context, final String strTitle, final String strDispMsg)
	{
		final AlertDialog.Builder builder= new AlertDialog.Builder(context);
		builder.setTitle(strTitle);
		builder.setMessage(strDispMsg);
//		builder.setItems(new String[]{"1","2"}, new DialogInterface.OnClickListener()
//		{
//			public void onClick(DialogInterface dialog, int item)
//			{
//				dialog.dismiss();
//			}
//		});
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int item)
			{
				dialog.dismiss();
			}
		});
		AlertDialog alert= builder.create();
		alert.setCancelable(true);
		alert.setCanceledOnTouchOutside(true);
		alert.show();
	}
	
	/**
	 * ������ť��AlertDialog
	 * @param context
	 */
	public static void appExitDialog(final Context context)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setMessage("Are You Sure?");
		builder.setIcon(General.getIcon(context));
		builder.setTitle("Exit Program?");
		builder.setCancelable(false);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				((Activity) context).finish();
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});

		AlertDialog ad = builder.create();
		ad.show();
		//����home���� android2.3֮��汾�ز�֧��
//		ad.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
	}
	
	/**
	 * ��ȡͼ����Դ
	 * @param context
	 * @return
	 */
	public static int getIcon(Context context)
	{

		int bancIconId;
		SharedPreferences settings = context.getSharedPreferences("AppSetting", 0);
		bancIconId = settings.getInt("banckIconID", 0);

		switch (bancIconId)
		{
		case 0:
			return R.drawable.ic_launcher;
		case 1:
			return R.drawable.ic_launcher;
		default:
			return R.drawable.ic_launcher;
		}
	}
	
//	private View getViewById(String id)
//	{
//		Solo solo;
//		if(id == "")
//		{
//			return null;
//		}
//		Activity act = solo.getCurrentActivity();
//		int idt = act.getResources().getIdentifier(id, "id", act.getPackageName());//��ȡid
//		View view = solo.getView(idt);
//		return view;
//	
//	//
//	//	ͨ��id��ȡ�ؼ�
//	//	EditText recvet = (EditText) solo.getCurrentActivity().findViewById(com.paxdata.mpos.R.id.cm_receivetimeout_et);
//	//
//	//	ͨ��getview��ȡ��ͼ
//	//	int selectIndex = 0;
//	//	ArrayList<GridView> gridList  = solo.getCurrentViews(GridView.class);
//	//	View itemView = gridList.get(selectIndex);
//	//	solo.waitForView(itemView, sleepTime,true);
//	//	solo.clickOnView(itemView);
	
//	}

}