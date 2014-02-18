package com.dana.modulII;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.util.Log;

public class ExcelOperation
{
	//Debug
	private final static String TAG = "ExcelOperation";
	
	/**
	 * ��Excel�ļ��д���sheet
	 * @param filename
	 * @param sheetname
	 * @param sheetnum
	 * @throws IOException
	 */
	public static void CreateExcelSheet(String filename, String sheetname, int sheetnum) throws IOException
	{
		if(!filename.endsWith("xls"))
			filename+=".xls";
		File file = new File(filename);
		try {
			//��Excel�ļ�
			WritableWorkbook book = Workbook.createWorkbook(file);
			//���ɹ�����
//			if(book.getSheet(sheetname).isHidden())
			book.createSheet(sheetname, sheetnum);
			book.write();
			book.close();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i(TAG, e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	/**
	 * д��Excel
	 * @param filename
	 * @param sheetname
	 * @param row ����
	 * @param col ����
	 * @param value
	 */
	public static void WriteExcel(String filename, String sheetname, int row, int col, String value)
	{
		Workbook wb = null;
		WritableWorkbook book = null;
		WritableSheet sheet = null;
		
		File file = new File(filename);
		try {
			//���Excel�ļ�
			wb = Workbook.getWorkbook(file);
			//��һ���ļ��ĸ���������ָ������д�ص�ԭ�ļ�
			book = Workbook.createWorkbook(file, wb);
			sheet = book.getSheet(sheetname);
			//��Label����Ĺ�����ָ����Ԫ���λ�ü���Ԫ������
			Label label = new Label(col, row, value);
			//������õĵ�Ԫ����ӵ���������
			sheet.addCell(label);
			//д�����ݲ��ر��ļ�
			book.write();
			book.close();			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, e.getMessage());
			e.printStackTrace();
			try {
				book.close();
			} catch (WriteException e1) {
				// TODO Auto-generated catch block
				Log.i(TAG, e1.getMessage());
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Log.i(TAG, e1.getMessage());
				e1.printStackTrace();
			}
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, e.getMessage());
			e.printStackTrace();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, e.getMessage());
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, e.getMessage());
			e.printStackTrace();
		}
	}
	
//	/**���Excel*/  
//	public static void writeExcel(OutputStream os)   
//	{
//		try{
//			/** ֻ��ͨ��API�ṩ�� ��������������Workbook��������ʹ��WritableWorkbook�Ĺ��캯������Ϊ��WritableWorkbook�Ĺ��캯��Ϊ protected���ͣ�����һ��ֱ�Ӵ�Ŀ���ļ��ж�ȡ WritableWorkbook wwb = Workbook.createWorkbook(new File(targetfile));���� ��������ʵ����ʾ ��WritableWorkbookֱ��д�뵽�����*/  
//			WritableWorkbook wwb = Workbook.createWorkbook(os);   
//			 //����Excel������ ָ�����ƺ�λ��   
//			WritableSheet ws = wwb.createSheet("Test Sheet 1",0);   
//			/**************�����������������*****************/  
//			//1.���Label����   
//			Label label = new Label(0,0,"����");   
//			ws.addCell(label);   
//			//��Ӵ�������Formatting����   
//			WritableFont wf = new WritableFont(WritableFont.TIMES,18,WritableFont.BOLD,true);   
//			WritableCellFormat wcf = new WritableCellFormat(wf);   
//			Label labelcf = new Label(1,0,"this is a label test",wcf);   
//			ws.addCell(labelcf);   
//			//��Ӵ���������ɫ��Formatting����   
//			WritableFont wfc = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD,false,   
//			UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.DARK_YELLOW);   
//			WritableCellFormat wcfFC = new WritableCellFormat(wfc);   
//			Label labelCF = new Label(1,0,"Ok",wcfFC);   
//			ws.addCell(labelCF);
//			
//			//2.���Number����   
//			Number labelN = new Number(0,1,3.1415926);   
//			ws.addCell(labelN);   
//			//��Ӵ���formatting��Number����   
//			NumberFormat nf = new NumberFormat("#.##");   
//			WritableCellFormat wcfN = new WritableCellFormat(nf);   
//			Number labelNF = new Number(1,1,3.1415926,wcfN);   
//			ws.addCell(labelNF);   
//			//3.���Boolean����   
//			jxl.write.Boolean labelB = new jxl.write.Boolean(0,2,true);   
//			ws.addCell(labelB);   
//			jxl.write.Boolean labelB1 = new jxl.write.Boolean(1,2,false);   
//			ws.addCell(labelB1);             
//			//4.���DateTime����   
//			jxl.write.DateTime labelDT = new jxl.write.DateTime(0,3,new java.util.Date());   
//			ws.addCell(labelDT);   
//			//5.��Ӵ���formatting��DateFormat����   
//			DateFormat df = new DateFormat("dd MM yyyy hh:mm:ss");   
//			WritableCellFormat wcfDF = new WritableCellFormat(df);   
//			DateTime labelDTF = new DateTime(1,3,new java.util.Date(),wcfDF);   
//			ws.addCell(labelDTF);   
//			//6.���ͼƬ����,jxlֻ֧��png��ʽͼƬ   
//			File image = new File("f:\\1.png");   
//			WritableImage wimage = new WritableImage(0,4,6,17,image);   
//			ws.addImage(wimage);   
//			//7.д�빤����   
//			wwb.write();   
//			wwb.close();   
//		} catch(Exception e)   
//		{   
//			e.printStackTrace();   
//		}   
//	}   

	/**
	 * ��Excel��Ԫ����д������
	 * @param filename
	 * @param sheetname
	 * @param row ����
	 * @param col ����
	 * @param value ����
	 */
	public static void WriteExcelNumber(String filename, String sheetname, int row, int col, double value)
	{
		Workbook wb = null;
		WritableWorkbook book = null;
		WritableSheet sheet = null;
		
		File file = new File(filename);
		try {
			//���Excel�ļ�
			wb = Workbook.getWorkbook(file);
			//��һ���ļ��ĸ���������ָ������д�ص�ԭ�ļ�
			book = Workbook.createWorkbook(file, wb);
			sheet = book.getSheet(sheetname);
			//����һ���������ֵĵ�Ԫ�� ����ʹ��Number��������·�����������﷨����
			Number number = new Number(col, row, value);
			//������õĵ�Ԫ����ӵ���������
			sheet.addCell(number);
			//д�����ݲ��ر��ļ�
			book.write();
			book.close();			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, e.getMessage());
			e.printStackTrace();
			try {
				book.close();
			} catch (WriteException e1) {
				// TODO Auto-generated catch block
				Log.i(TAG, e1.getMessage());
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Log.i(TAG, e1.getMessage());
				e1.printStackTrace();
			}
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, e.getMessage());
			e.printStackTrace();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, e.getMessage());
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡExcel�ļ�
	 * @param filename
	 * @param sheetname
	 * @param row
	 * @param col
	 * @return
	 */
	public static String ReadExcel(String filename, String sheetname, int row, int col)
	{
		Workbook book = null;
		Sheet sheet = null;
		
		File file = new File(filename);
		try {
			book = Workbook.getWorkbook(file);
			//��ù�����sheet
			sheet = book.getSheet(sheetname);
			//�õ���Ԫ��
			Cell cell = sheet.getCell(col, row);
			String result = cell.getContents();
			book.close();
			return result;
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String getExcelSheet(String filename, String sheetname)
	{
		Workbook book = null;
		Sheet sheet = null;
		String result = "";
		
		File file = new File(filename);
		try {
			book = Workbook.getWorkbook(file);
			sheet = book.getSheet(sheetname);
			//�õ�����
			int row = sheet.getRows();
			//�õ�����
			int col = sheet.getColumns();
			//��д��Ԫ��
			for(int i = 0; i< row; i++)
			{
				for(int j = 0; j < col; j++)
				{
					Cell cell = sheet.getCell(j,i);
					result += (String.valueOf(i+1) + "��" + String.valueOf(j+1) + "��: ");
					result += cell.getContents();
					result +="\n";//\t
				}
			}
			book.close();
			return result;
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}