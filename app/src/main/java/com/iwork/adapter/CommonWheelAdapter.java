/**
 * description:
 * @author: "zhengtao"
 * @create: 2015年4月13日
 */
package com.iwork.adapter;

import java.util.List;

/**
 * @description:
 */
public class CommonWheelAdapter<T> extends WheelAdapter {

	private List<T> mDataList;
	
	public CommonWheelAdapter(List<T> dataList) {
		mDataList = dataList;
    }
	
	@Override
	public int getCount() {
		int count = 0;
		if(mDataList != null){
			count = mDataList.size();
		}
		return count;
	}

	@Override
	public String getItem(int index) {
		// TODO Auto-generated method stub
		return mDataList.get(index).toString();
	}
	
	public T getDataAt(int position){
		if (position < 0 || position >= mDataList.size())
			return null;
		return mDataList.get(position);
	}
	
	public int indexOf(T obj){
		if(mDataList != null){
			int size = mDataList.size();
    		for(int i = 0; i < size; i++){
    			if(mDataList.get(i).equals(obj)){
    				return i;
    			}
    		}
    	}
    	return -1;
	}

	@Override
	public int getValue(int index) {
		// TODO Auto-generated method stub
		return index;
	}

	@Override
	public int getStartValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getEndValue() {
		// TODO Auto-generated method stub
		return this.getCount() - 1;
	}

	@Override
	public int getInterval() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getValueIndex(int value) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public void setStartValue(int value) {
		// TODO Auto-generated method stub
		
	}

}
