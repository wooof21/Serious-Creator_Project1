/**
 * 
 */
package com.chihuoshijian.wxapi;


import org.json.JSONException;
import org.json.JSONObject;

import com.chihuoshijian.R;
import com.main.membercenter.MemberCenterMainActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelbiz.AddCardToWXCardPackage.Resp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tools.Config;
import com.tools.Tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
public class WXEntryActivity extends Activity implements
		IWXAPIEventHandler{

	private IWXAPI				api;

	private String				code;

	private String				token;

	private String				refreshToken;

	private String				openId;

	private ProgressDialog		progressDialog;

	private SharedPreferences	sharedPre;

	private Editor				editor;

	public String	unionid;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.login);
		regToWx();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tencent.mm.sdk.openapi.IWXAPIEventHandler#onReq(com.tencent.mm.sdk
	 * .modelbase.BaseReq)
	 */
	@Override
	public void onReq(BaseReq arg0){
		// TODO Auto-generated method stub
		finish();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tencent.mm.sdk.openapi.IWXAPIEventHandler#onResp(com.tencent.mm.sdk
	 * .modelbase.BaseResp)
	 */
	@Override
	public void onResp(BaseResp resp){
		// TODO Auto-generated method stub
		System.out.println("onResp");
		String result = "";
		if(resp != null){
			resp = resp;
		}
		switch(resp.errCode){
			case BaseResp.ErrCode.ERR_OK:
				result = "发送成功";
				code = ((SendAuth.Resp) resp).code;
				Log.e("code", code);

				new TokenAsync().execute(Config.APP_ID,
						Config.APP_SECRET, code);
			break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = "发送取消";
			break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = "发送被拒绝";
			break;
			default:
				result = "发送返回";
			break;
		}

		Toast.makeText(this, result, Toast.LENGTH_LONG)
				.show();
		finish();
	}

	class TokenAsync extends
			AsyncTask<Object, Void, String>{

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Object... params){
			// TODO Auto-generated method stub
			String url =
					Config.WEIXIN_GET_TOKEN_URL
							+ params[0]
							+ "&secret="
							+ params[1]
							+ "&code="
							+ params[2]
							+ "&grant_type=authorization_code";
			Log.e("url", url);
			String data = new Tools().getURL(url);
			System.out.println(data);
			try{
				JSONObject job = new JSONObject(data);
				token = job.getString("access_token");
				refreshToken =
						job.getString("refresh_token");
				openId = job.getString("openid");
				unionid = job.getString("unionid");
				saveLoginInfo(getApplicationContext(), "", "", openId, unionid, code, token, refreshToken);
			}catch(JSONException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return openId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result){
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result != null){
				Intent intent = new Intent(WXEntryActivity.this, MemberCenterMainActivity.class);
				intent.putExtra("openId", result);
				intent.putExtra("token", token);
				startActivity(intent);
			}
		}

	}

	private void saveLoginInfo(Context context,
			String name, String psw, String openId, String unionid,
			String code, String token, String refreshToken){
		sharedPre =
				context.getSharedPreferences("config",
						Context.MODE_PRIVATE);
		editor = sharedPre.edit();
		editor.putString("username", name);
		editor.putString("password", psw);
		editor.putString("openId", openId);
		editor.putString("code", code);
		editor.putString("token", token);
		editor.putString("refreshToken", refreshToken);
		editor.putString("unionid", unionid);
		editor.commit();
	}

	private void clearLoginInfo(){
    	if(editor != null){
    		editor.clear();  
            editor.commit();
    	}
    }
	
	private void regToWx(){
		api =
				WXAPIFactory.createWXAPI(this,
						Config.APP_ID, true);
		api.registerApp(Config.APP_ID);
		api.handleIntent(getIntent(), this);
	}

}
