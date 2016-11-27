/**
 * 
 */
package com.model;

import java.io.Serializable;

/**
 * @author Liming Chu
 *
 * @param
 * @return
 */
public class OrderItem implements Serializable{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private String name;
	private String count;
	private String price;
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getCount(){
		return count;
	}
	public void setCount(String count){
		this.count = count;
	}
	public String getPrice(){
		return price;
	}
	public void setPrice(String price){
		this.price = price;
	}
}
