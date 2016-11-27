/**
 * 
 */
package com.chihuoshijian.login;

import com.chihuoshijian.R;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tools.Config;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Liming Chu	
 *
 * @param
 * @return
 */
public class LogInActivity extends Activity implements OnClickListener{
	
    private IWXAPI api;
    
    private EditText phone;
    private EditText code;
    private TextView getCode;
    private TextView WXLogin;
    private TextView login;
    
    private String text;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		prepareView();
		
	}
	
	private void prepareView(){
		phone = (EditText) findViewById(R.id.login_phone_et);
		code = (EditText) findViewById(R.id.login_certification_code);
		getCode = (TextView) findViewById(R.id.login_get_code);
		WXLogin = (TextView) findViewById(R.id.login_weixin_login);
		login = (TextView) findViewById(R.id.login_login);
		
		getCode.setOnClickListener(this);
		WXLogin.setOnClickListener(this);
		login.setOnClickListener(this);
	}
	
	private void start(){
		api = WXAPIFactory.createWXAPI(this, Config.APP_ID, true);
		api.registerApp(Config.APP_ID);
		final SendAuth.Req req = new SendAuth.Req();
	    req.scope = "snsapi_userinfo";
	    req.state = "wechat_sdk_demo_test";
	    api.sendReq(req);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed(){
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v){
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.login_get_code:
			
			break;
			case R.id.login_weixin_login:
				start();
				break;
			case R.id.login_login:
				
				break;
			default:
			break;
		}
	}
}
