package com.xie.rtc.cleaning.filter.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.xie.rtc.cleaning.filter.Filter;
import com.xie.rtc.cleaning.filter.FilterType;
import com.xie.rtc.cleaning.util.JSONUtils;

/**
 * @author xiejing.kane
 *
 */
@FilterType("iptolong")
public class IPToLongFilter implements Filter {
	private String field;
	private String newField;

	@Override
	public void init(JSONObject config) {
		field = JSONUtils.getRequiredString(config, "field");
		newField = JSONUtils.getRequiredString(config, "new_field");
	}

	@Override
	public JSONObject filter(JSONObject source) {
		String ip = source.getString(field);
		if (!Strings.isNullOrEmpty(ip)) {
			source.put(newField, ipToLong(ip));
		}
		return source;
	}

	private long ipToLong(String ip) {
		String[] addrArray = ip.split("\\.");
		long num = 0;
		for (int i = 0; i < addrArray.length; i++) {
			int power = 3 - i;
			num += ((Integer.parseInt(addrArray[i]) % 256 * Math.pow(256, power)));
		}
		return num;
	}
}
