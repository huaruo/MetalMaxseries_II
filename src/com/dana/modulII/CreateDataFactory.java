package com.dana.modulII;

import java.util.ArrayList;
import java.util.List;

public class CreateDataFactory
{
	public static List<String> createUpdateData(int currentPage, int pageSize)
	{
		List<String> list = new ArrayList<String>();
		int i;
		int preSize = (currentPage -1)*pageSize;
		int curSize = currentPage*pageSize;
		for(i=preSize; i<curSize; i++)
		{
			list.add((i+1)+"¸ÜÉÏ¿ª»¨");
		}
		return list;
	}
}