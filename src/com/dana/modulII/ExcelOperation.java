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
	 * 在Excel文件中创建sheet
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
			//打开Excel文件
			WritableWorkbook book = Workbook.createWorkbook(file);
			//生成工作表
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
	 * 写入Excel
	 * @param filename
	 * @param sheetname
	 * @param row 行数
	 * @param col 列数
	 * @param value
	 */
	public static void WriteExcel(String filename, String sheetname, int row, int col, String value)
	{
		Workbook wb = null;
		WritableWorkbook book = null;
		WritableSheet sheet = null;
		
		File file = new File(filename);
		try {
			//获得Excel文件
			wb = Workbook.getWorkbook(file);
			//打开一个文件的副本，并且指定数据写回到原文件
			book = Workbook.createWorkbook(file, wb);
			sheet = book.getSheet(sheetname);
			//在Label对象的构造中指明单元格的位置及单元格内容
			Label label = new Label(col, row, value);
			//将定义好的单元格添加到工作表中
			sheet.addCell(label);
			//写入数据并关闭文件
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
	
//	/**输出Excel*/  
//	public static void writeExcel(OutputStream os)   
//	{
//		try{
//			/** 只能通过API提供的 工厂方法来创建Workbook，而不能使用WritableWorkbook的构造函数，因为类WritableWorkbook的构造函数为 protected类型：方法一：直接从目标文件中读取 WritableWorkbook wwb = Workbook.createWorkbook(new File(targetfile));方法 二：如下实例所示 将WritableWorkbook直接写入到输出流*/  
//			WritableWorkbook wwb = Workbook.createWorkbook(os);   
//			 //创建Excel工作表 指定名称和位置   
//			WritableSheet ws = wwb.createSheet("Test Sheet 1",0);   
//			/**************往工作表中添加数据*****************/  
//			//1.添加Label对象   
//			Label label = new Label(0,0,"测试");   
//			ws.addCell(label);   
//			//添加带有字型Formatting对象   
//			WritableFont wf = new WritableFont(WritableFont.TIMES,18,WritableFont.BOLD,true);   
//			WritableCellFormat wcf = new WritableCellFormat(wf);   
//			Label labelcf = new Label(1,0,"this is a label test",wcf);   
//			ws.addCell(labelcf);   
//			//添加带有字体颜色的Formatting对象   
//			WritableFont wfc = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD,false,   
//			UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.DARK_YELLOW);   
//			WritableCellFormat wcfFC = new WritableCellFormat(wfc);   
//			Label labelCF = new Label(1,0,"Ok",wcfFC);   
//			ws.addCell(labelCF);
//			
//			//2.添加Number对象   
//			Number labelN = new Number(0,1,3.1415926);   
//			ws.addCell(labelN);   
//			//添加带有formatting的Number对象   
//			NumberFormat nf = new NumberFormat("#.##");   
//			WritableCellFormat wcfN = new WritableCellFormat(nf);   
//			Number labelNF = new Number(1,1,3.1415926,wcfN);   
//			ws.addCell(labelNF);   
//			//3.添加Boolean对象   
//			jxl.write.Boolean labelB = new jxl.write.Boolean(0,2,true);   
//			ws.addCell(labelB);   
//			jxl.write.Boolean labelB1 = new jxl.write.Boolean(1,2,false);   
//			ws.addCell(labelB1);             
//			//4.添加DateTime对象   
//			jxl.write.DateTime labelDT = new jxl.write.DateTime(0,3,new java.util.Date());   
//			ws.addCell(labelDT);   
//			//5.添加带有formatting的DateFormat对象   
//			DateFormat df = new DateFormat("dd MM yyyy hh:mm:ss");   
//			WritableCellFormat wcfDF = new WritableCellFormat(df);   
//			DateTime labelDTF = new DateTime(1,3,new java.util.Date(),wcfDF);   
//			ws.addCell(labelDTF);   
//			//6.添加图片对象,jxl只支持png格式图片   
//			File image = new File("f:\\1.png");   
//			WritableImage wimage = new WritableImage(0,4,6,17,image);   
//			ws.addImage(wimage);   
//			//7.写入工作表   
//			wwb.write();   
//			wwb.close();   
//		} catch(Exception e)   
//		{   
//			e.printStackTrace();   
//		}   
//	}   

	/**
	 * 在Excel单元格内写入数据
	 * @param filename
	 * @param sheetname
	 * @param row 行数
	 * @param col 列数
	 * @param value 内容
	 */
	public static void WriteExcelNumber(String filename, String sheetname, int row, int col, double value)
	{
		Workbook wb = null;
		WritableWorkbook book = null;
		WritableSheet sheet = null;
		
		File file = new File(filename);
		try {
			//获得Excel文件
			wb = Workbook.getWorkbook(file);
			//打开一个文件的副本，并且指定数据写回到原文件
			book = Workbook.createWorkbook(file, wb);
			sheet = book.getSheet(sheetname);
			//生成一个保存数字的单元格 必须使用Number的完整包路径，否则有语法歧义
			Number number = new Number(col, row, value);
			//将定义好的单元格添加到工作表中
			sheet.addCell(number);
			//写入数据并关闭文件
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
	 * 读取Excel文件
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
			//获得工作表sheet
			sheet = book.getSheet(sheetname);
			//得到单元格
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
			//得到行数
			int row = sheet.getRows();
			//得到列数
			int col = sheet.getColumns();
			//读写单元格
			for(int i = 0; i< row; i++)
			{
				for(int j = 0; j < col; j++)
				{
					Cell cell = sheet.getCell(j,i);
					result += (String.valueOf(i+1) + "行" + String.valueOf(j+1) + "列: ");
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