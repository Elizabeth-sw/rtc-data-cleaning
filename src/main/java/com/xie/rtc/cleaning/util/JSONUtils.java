package com.xie.rtc.cleaning.util;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class JSONUtils {
	public static String getRequiredString(JSONObject json, String key) {
		String value = json.getString(key);
		Preconditions.checkNotNull(value, key);
		return value;
	}

	public static List<String> getRequiredList(JSONObject json, String key) {
		JSONArray value = json.getJSONArray(key);
		Preconditions.checkNotNull(value, key);
		return Lists.newArrayList(value.toArray(new String[] {}));
	}

	public static <T> T getRequired(JSONObject json, String key) {
		@SuppressWarnings("unchecked")
		T value = (T) json.get(key);
		Preconditions.checkNotNull(value, key);
		return value;
	}
}
