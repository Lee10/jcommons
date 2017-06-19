package org.lee.coderepo.list;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lee on 2017/3/31.
 */
public class ListUtils {
	
	public static <T> List<List<T>> split(List<T> list, int returnSize) {
		
		List<List<T>> returnList = new ArrayList<List<T>>();
		int listSize = list.size();
		int size = listSize / (returnSize - 1);
		size = size == 0 ? 1 : size;
		int num = listSize % size == 0 ? listSize / size : (listSize / size + 1);
		int start = 0;
		int end = 0;
		for (int i = 1; i <= num; i++) {
			start = (i - 1) * size;
			end = i * size > listSize ? listSize : i * size;
			returnList.add(list.subList(start, end));
		}
		return returnList;
	}
	
	public static <T> List<List<T>> split2(List<T> list, int size) {
		
		List<List<T>> returnList = new ArrayList<List<T>>();
		int listSize = list.size();
		int num = listSize % size == 0 ? listSize / size : (listSize / size + 1);
		int start = 0;
		int end = 0;
		for (int i = 1; i <= num; i++) {
			start = (i - 1) * size;
			end = i * size > listSize ? listSize : i * size;
			returnList.add(list.subList(start, end));
		}
		return returnList;
	}
	
	public static <T> List<T> distinct(List<T> list){
		return new ArrayList<T>(new HashSet<T>(list));
	}
	
	public static <T> List<T> retainAll(List<T> list, List<T> anotherList){
		Set<T> set = new HashSet<T>(list);
		set.retainAll(new HashSet<T>(anotherList));
		return new ArrayList<T>(set);
	}
}
