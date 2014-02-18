package com.dana.modulII;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

public class ClsUtils
{
	//Debug
	private final static String TAG = "ClsUtils";
	public static BluetoothDevice remoteDevice = null;
	
	/** 
     * ���豸��� �ο�Դ�룺platform/packages/apps/Settings.git 
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java 
     */
	public static boolean createBond(Class btClass, BluetoothDevice btDevice) throws Exception
	{
		Method createBondMethod = btClass.getMethod("createBond");
		Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}
	
	/** 
     * ���豸������ �ο�Դ�룺platform/packages/apps/Settings.git 
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java 
     */
	public static boolean removeBond(Class btClass, BluetoothDevice btDevice) throws Exception
	{
		Method removeBondMethod = btClass.getMethod("removeBond");
		Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}
	
	public static boolean SetPin(Class btClass, BluetoothDevice btDevice, 
            String str) throws Exception 
    { 
        try
        { 
            Method removeBondMethod = btClass.getDeclaredMethod("setPin", 
                    new Class[] 
                    {byte[].class}); 
            Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice, 
                    new Object[] 
                    {str.getBytes()}); 
            Log.d("returnValue", "setPin is success " +btDevice.getAddress()+ returnValue.booleanValue());
        } 
        catch (SecurityException e) 
        { 
            // throw new RuntimeException(e.getMessage()); 
            e.printStackTrace(); 
        } 
        catch (IllegalArgumentException e) 
        { 
            // throw new RuntimeException(e.getMessage()); 
            e.printStackTrace(); 
        } 
        catch (Exception e) 
        { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } 
        return true; 
  
    } 
  
    // ȡ���û����� 
	public static boolean cancelPairingUserInput(Class btClass, 
            BluetoothDevice device) 
    throws Exception 
    { 
        Method createBondMethod = btClass.getMethod("cancelPairingUserInput"); 
        // cancelBondProcess() 
        Boolean returnValue = (Boolean) createBondMethod.invoke(device); 
        Log.d("returnValue", "cancelPairingUserInput is success " + returnValue.booleanValue());
        return returnValue.booleanValue(); 
    } 
  
    // ȡ����� 
    public static boolean cancelBondProcess(Class btClass, 
            BluetoothDevice device) 
  
    throws Exception 
    { 
        Method createBondMethod = btClass.getMethod("cancelBondProcess"); 
        Boolean returnValue = (Boolean) createBondMethod.invoke(device); 
        return returnValue.booleanValue(); 
    } 
  
    /** 
     *  
     * @param clsShow 
     */
    public static void printAllInform(Class clsShow) 
    { 
        try
        { 
            // ȡ�����з��� 
            Method[] hideMethod = clsShow.getMethods(); 
            int i = 0; 
            for (; i < hideMethod.length; i++) 
            { 
                //Log.e("method name", hideMethod.getName() + ";and the i is:"
                  //      + i); 
            } 
            // ȡ�����г��� 
            Field[] allFields = clsShow.getFields(); 
            for (i = 0; i < allFields.length; i++) 
            { 
                //Log.e("Field name", allFields.getName()); 
            } 
        } 
        catch (SecurityException e) 
        { 
            // throw new RuntimeException(e.getMessage()); 
            e.printStackTrace(); 
        } 
        catch (IllegalArgumentException e) 
        { 
            // throw new RuntimeException(e.getMessage()); 
            e.printStackTrace(); 
        } 
        catch (Exception e) 
        { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } 
    } 
}