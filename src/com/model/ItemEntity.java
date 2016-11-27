/**
 * 
 */
package com.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
// ===========================================================
// Inner and Anonymous Classes
// ===========================================================
public class ItemEntity implements Serializable{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private String	classname;
	private String	id;
	private String	title;
	private String	price;
	private float	rate;
	private String	picAddr;
	private int itemCount;

	public int getItemCount(){
		return itemCount;
	}

	public void setItemCount(int itemCount){
		this.itemCount = itemCount;
	}

	public ItemEntity(String classname, String id,
			String title, String price, float rate,
			String picAddr, int count){
		super();
		this.classname = classname;
		this.id = id;
		this.title = title;
		this.price = price;
		this.rate = rate;
		this.picAddr = picAddr;
		this.itemCount = count;
	}

	/**
	 * @return the classname
	 */
	public String getClassname(){
		return classname;
	}

	/**
	 * @param classname
	 *            the classname to set
	 */
	public void setClassname(String classname){
		this.classname = classname;
	}

	/**
	 * @return the id
	 */
	public String getId(){
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id){
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle(){
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title){
		this.title = title;
	}

	/**
	 * @return the price
	 */
	public String getPrice(){
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(String price){
		this.price = price;
	}

	/**
	 * @return the rate
	 */
	public float getRate(){
		return rate;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(float rate){
		this.rate = rate;
	}

	/**
	 * @return the picAddr
	 */
	public String getPicAddr(){
		return picAddr;
	}

	/**
	 * @param picAddr
	 *            the picAddr to set
	 */
	public void setPicAddr(String picAddr){
		this.picAddr = picAddr;
	}

	@Override
	public boolean equals(Object o){
		// TODO Auto-generated method stub
		if(o instanceof ItemEntity){
			ItemEntity u = (ItemEntity) o; 
			return this.title.equals(u.title)   
                    && this.id.equals(id) && this.classname.equals(classname);   
		}
		return super.equals(o);
	}

	
}
