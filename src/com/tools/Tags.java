/**
 * 
 */
package com.tools;

/**
 * @author Liming Chu
 *
 * @param
 * @return
 */
public class Tags{

	private String[] tags;

	private static Tags instance;

	public Tags(){
		super();
		// TODO Auto-generated constructor stub
	}
	public static Tags getInstance(){
		if(instance == null){
			instance = new Tags();
		}
		return instance;
	}

	public String[] getTags(){
		return tags;
	}

	public void setTags(String[] tags){
		this.tags = tags;
		System.out.println("1" + tags);
	}
	
	
}
