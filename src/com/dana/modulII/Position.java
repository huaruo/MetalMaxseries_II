package com.dana.modulII;

public class Position
{
	private int row, column;
	
	public Position()
	{
		row = -1;
		column = -1;
	}
	
	/***/
	public Position(int row, int column)
	{
		this.row = row;
		this.column = column;
	}
	/**获取横坐标*/
	public int getRow()
	{
		return row;
	}
	/**设置横坐标*/
	public int setRow(int row)
	{
		return row;
	}
	/**获取纵坐标*/
	public int getColumn()
	{
		return column;
	}
	/**设置纵坐标*/
	public void setColumn(int column)
	{
		this.column = column;
	}
	/**获取空位置*/
	public static Position getEmptyPosition()
	{
		return new Position(-1,-1);
	}
	
	@Override
	public String toString()
	{
		return "pso -> (" +this.row+" , " + this.column + ")";
	}
}