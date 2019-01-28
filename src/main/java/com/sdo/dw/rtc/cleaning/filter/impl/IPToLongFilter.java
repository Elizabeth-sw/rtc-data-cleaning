package com.sdo.dw.rtc.cleaning.filter.impl;

import com.alibaba.fastjson.JSONObject;
import com.sdo.dw.rtc.cleaning.filter.Filter;
import com.sdo.dw.rtc.cleaning.filter.FilterType;
import com.sdo.dw.rtc.cleaning.util.JSONUtils;

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
		if (source.containsKey(field)) {
			String ip = source.getString(field);
			if (!ip.isEmpty()) {
				source.put(newField, ipToLong(ip));
			}
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
