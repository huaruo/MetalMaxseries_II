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
	/**��ȡ������*/
	public int getRow()
	{
		return row;
	}
	/**���ú�����*/
	public int setRow(int row)
	{
		return row;
	}
	/**��ȡ������*/
	public int getColumn()
	{
		return column;
	}
	/**����������*/
	public void setColumn(int column)
	{
		this.column = column;
	}
	/**��ȡ��λ��*/
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