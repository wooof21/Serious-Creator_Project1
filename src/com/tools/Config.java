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
public class Config{

	/**
	 * 域名地址
	 * */
	public static String		DOMAIN							=
																		"http://192.168.200.13:818";

	/**
	 * 菜系接口地址
	 * */
	public static String		cxUrl							=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_cx";
	/**
	 * 商圈接口地址
	 * */
	public static String		sqUrl							=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_qy";

	/**
	 * 包房分类接口地址
	 * */
	public static String		bfUrl							=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_bffl";
	/**
	 * 包房列表接口地址
	 * */
	public static String		BF_MAIN_LIST_URL				=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_bf_list";
	/**
	 * 包房预定页接口地址
	 * */
	public static String		BF_RESERVE_MAIN_URL				=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_baofang_yd&uid=";
	/**
	 * 包房信息列表接口地址
	 * */
	public static String		BF_INFO_LIST_URL				=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_baofang_ydlist&uid=";
	/**
	 * 包房预定提交接口地址
	 * */
	public static String		BF_RESERVE_SUBMIT_URL				=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_baofang_ydtj";

	/**
	 * 搜餐厅 --- 饭店接口地址
	 * */
	public static String		CT_MAIN_LIST_URL				=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_ct_list";
	/**
	 * 饭店主页接口地址
	 * */
	public static String		FD_MAIN_LIST_URL				=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_fandian_xx&uid=";
	/**
	 * 饭店主页推荐菜接口地址
	 * */
	public static String		FD_MAIN_TUIJIAN_LIST_URL		=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_fandian_tjcaipin&uid=";
	/**
	 * 饭店主页评论接口地址
	 * */
	public static String		FD_MAIN_COMMEND_LIST_URL		=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_fandian_pj&uid=";

	/**
	 * 首页， 热门推荐接口地址
	 * */
	public static String		MAIN_REC_LIST_URL				=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_index_tjlist";

	/**
	 * 首页， 热门搜索接口地址
	 * */
	public static String		MAIN_SEARCH_LIST_URL			=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_index_rmss";

	/**
	 * 美食主页列表接口地址
	 * */
	public static String		MS_MAIN_LIST_URL				=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_meishi_list";
	/**
	 * 美食主页热门搜索接口地址
	 * */
	public static String		MS_MAIN_HOT_SEARCH_LIST_URL		=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_meishi_rmtj";
	/**
	 * 美食菜品主页接口地址
	 * */
	public static String		MS_DISH_COMENT_MAIN_URL			=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_cai_view&id=";
	/**
	 * 美食菜品评价接口地址
	 * */
	public static String		MS_DISH_COMENT_LIST_URL			=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_cai_pj&uid=";

	/**
	 * 点菜接口地址
	 * */
	public static String		DISH_ORDER_LIST_URL				=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_fandian_cd&uid=";
	/**
	 * 提交订单地址
	 * */
	public static String		DISH_SUBMIT_ORDER_URL	=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_order";
	
	/**
	 * 饭店详情收藏状态接口地址
	 * */
	public static String		FD_DETAIL_CLECT_STATUS_URL		=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_fandian_sc&uid=";
	/**
	 * 饭店详情收藏，取消收藏接口地址
	 * */
	public static String		FD_DETAIL_CLECT_OR_NOT_URL		=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_user_sc&uid=";
	/**
	 * 我的订单接口地址
	 * */
	public static String		MY_ORDER_URL					=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_user_order&userid=";
	/**
	 * 我的订单删除接口地址
	 * */
	public static String		MY_ORDER_DELETE_URL				=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_user_dddel&id=";
	/**
	 * 包房预定记录接口地址
	 * */
	public static String		BF_ORDER_RECORD_URL				=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_user_bfxx&userid=";
	/**
	 * 会员中心主页接口地址
	 * */
	public static String		VIP_CENTER_MAIN_DETAIL_URL		=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_user&userid=";

	/**
	 * weixin app id
	 * */
	public static final String	APP_ID							=
																		"wx8683e0c0fef883c3";
	/**
	 * weixin app secret
	 * */
	public static final String	APP_SECRET						=
																		"cf11a4400848050d0a945ba49b715f62";

	/**
	 * 微信获取access token链接
	 * */
	public static String		WEIXIN_GET_TOKEN_URL			=
																		"https://api.weixin.qq.com/sns/oauth2/access_token?appid=";
	/**
	 * 微信获取refresh token链接
	 * */
	public static String		WEIXIN_GET_REFRESH_TOKEN_URL	=
																		"https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=";
	/**
	 * 获取用户个人信息（UnionID机制）
	 * */
	public static String		WEIXIN_GET_PERSONAL_INFO_URL	=
																		"https://api.weixin.qq.com/sns/userinfo?access_token=";
	
	/**
	 * 订单2维码地址
	 * */
	public static String		ORDER_NUM_QR_CODE_URL	=
																		"http://192.168.200.13:818/chihuoshijian/ewm_view.php?num=";
	/**
	 * 评价提交接口
	 * */
	public static String		COMMENT_SUBMIT_URL	=
																		"http://192.168.200.13:818/chihuoshijian/api/api_java.php?act=api_pj_add&uid=";

	
	public static int			GPS								=
																		100;

	public static int			NOGPS							=
																		101;

	public static int			WIRELESS_SETTING				=
																		1001;

	public static int			GPS_SETTING						=
																		1002;
	public static int			RESULT_FALIURE					=
																		5000;
	public static int			RESULT_SUCCESS					=
																		5001;

}
