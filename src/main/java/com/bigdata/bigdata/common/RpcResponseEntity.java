package com.bigdata.bigdata.common;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RpcResponseEntity{
	public static final int DEFAULT_CODE_SUCCESS = 200;
	public static final String DEFAULT_MSG_SUCCESS = "成功";
	
	public static final int DEFAULT_CODE_400 = 400;
	public static final String DEFAULT_MSG_400 = "参数有误";
	
	public static final int DEFAULT_CODE_403 = 403;
	public static final String DEFAULT_MSG_403 = "禁止访问";
	
	public static final int DEFAULT_CODE_500 = 500;
	public static final String DEFAULT_MSG_500 = "服务器的内部错误";
	
	
	public static Map<Integer, String> restMap = new HashMap<Integer, String>();
	
	static {
		restMap.put(DEFAULT_CODE_SUCCESS, DEFAULT_MSG_SUCCESS);
		restMap.put(DEFAULT_CODE_400, DEFAULT_MSG_400);
		restMap.put(DEFAULT_CODE_403, DEFAULT_MSG_403);
		restMap.put(DEFAULT_CODE_500, DEFAULT_MSG_500);
	}
	
	public JSONObject restResponse(int code) {
		return restResponse(code,restMap.get(code));
	}
	
	public JSONObject restResponse(int code, String msg) {
		return restResponse(code, msg, null);
	}
	
	public JSONObject restResponse(int code, String msg, Object returnParm) {
		JSONObject returnJSON = new JSONObject();
		returnJSON.put("invokeResultCode", code);
		returnJSON.put("invokeResultMessage", msg);
		if(returnParm == null) {
			return returnJSON;
		}
		
		if(returnParm instanceof java.util.List) {
			returnJSON.put("data", JSONArray.fromObject(returnParm));
		}else {
			returnJSON.put("data", JSONObject.fromObject(returnParm));
		}
		return returnJSON;
	}
}
