package com.tools;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class Tools {

	public String getURL(String urlStr) {
		HttpURLConnection httpcon = null;
		InputStream in = null;
		String data = "";

		try {
			URL url = new URL(urlStr);
			httpcon = (HttpURLConnection) url.openConnection();
			httpcon.setDoInput(true);
			httpcon.setDoOutput(true);
			httpcon.setUseCaches(false);
			httpcon.setRequestMethod("GET");
			in = httpcon.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader bufferReader = new BufferedReader(isr);
			String input = "";
			while ((input = bufferReader.readLine()) != null) {
				data = input + data;
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (httpcon != null) {
				httpcon.disconnect();
			}
		}

		return data;

	}
	public void scaleInAnimation(View v){
		ScaleAnimation sa = new ScaleAnimation(1.0f, 0.95f, 1.0f, 0.95f,  
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,  
                0.5f);  
		sa.setRepeatCount(0);
		sa.setFillAfter(false);
		sa.setDuration(100);
		v.startAnimation(sa);
	}
	public void upDown(View v){
		TranslateAnimation ta = new TranslateAnimation(0, 0, 0, 5);
		ta.setDuration(500);
		ta.setInterpolator(new CycleInterpolator(5));
		v.startAnimation(ta);
	}
	
	public String doPostData(String urlStr) {
		HttpURLConnection httpcon = null;
		InputStream in = null;
		String data = "";
		
		try {
			URL url = new URL(urlStr);
			httpcon = (HttpURLConnection) url.openConnection();
			httpcon.setDoInput(true);
			
			httpcon.setDoOutput(true);
			httpcon.setUseCaches(false);
			httpcon.setRequestMethod("POST");
			in = httpcon.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader bufferReader = new BufferedReader(isr);
			String input = "";
			while ((input = bufferReader.readLine()) != null) {
				data = input + data;
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (httpcon != null) {
				httpcon.disconnect();
			}
		}

		return data;
		
	}
	
	public String doPostData(String urlStr, String data) throws IOException {
		String result = "";
		byte[] xmlbyte = data.getBytes("UTF-8");
		Log.e("post接口上传 格式的内容---utf8---", data);
		
		URL url = new URL(urlStr);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setDoOutput(true);// 允许输出
		conn.setDoInput(true);
		conn.setUseCaches(false);// 不使用缓存
		conn.setRequestMethod("POST");
		conn.getOutputStream().write(xmlbyte);
		conn.getOutputStream().flush();
		conn.getOutputStream().close();
		
		Log.e("conn.getResponseCode()----", "" + conn.getResponseCode());
		
		if (conn.getResponseCode() != 200)
			throw new RuntimeException("请求url失败");
		int codeOrder = conn.getResponseCode();
		
		if (codeOrder == 200) {

			InputStream inStream = conn.getInputStream();// 获取返回数据

			// 使用输出流来输出字符(可选)
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while ((len = inStream.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			result = out.toString("UTF-8");						
			Log.e("post返回结果--------", "" + result);
			out.close();
			
		} else {
			
		}
		
		return result;
	}
	public boolean getInternet(Context context) {
		ConnectivityManager con = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
		boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnectedOrConnecting();
		if (wifi || internet) {
			return true;
		} else {
			return false;
		}
	}
	
	/** 
     * 对double数据进行取精度. 
     * <p> 
     * For example: <br> 
     * double value = 100.345678; <br> 
     * double ret = round(value,4,BigDecimal.ROUND_HALF_UP); <br> 
     * ret为100.3457 <br> 
     *  
     * @param value 
     *            double数据. 
     * @param scale 
     *            精度位数(保留的小数位数). 
     * @param roundingMode 
     *            精度取值方式. 
     * @return 精度计算后的数据. 
     */  
    public static double round(double value, int scale, int roundingMode) {  
        BigDecimal bd = new BigDecimal(value);  
        bd = bd.setScale(scale, roundingMode);  
        double d = bd.doubleValue();  
        bd = null;  
        return d;  
    }  
    
	public double getDistance(double lat1, double myLat, double lon1, double myLon){
		if(myLat == 0 && myLat == 0){
			return -1;
		}else{
			LatLng ll1 = new LatLng(lat1, lon1);
			LatLng ll2 = new LatLng(myLat, myLon);
			double distance = DistanceUtil.getDistance(ll1, ll2);
			return distance;
		}
		
	}
}
