package com.xie.rtc.cleaning.filter.impl;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.xie.rtc.cleaning.filter.Filter;
import com.xie.rtc.cleaning.filter.FilterType;
import com.xie.rtc.cleaning.util.JSONUtils;

/**
 * @author xiejing.kane
 *
 */
@FilterType("trim")
public class TrimFilter implements Filter {
	private List<String> fields;

	@Override
	public void init(JSONObject config) {
		fields = JSONUtils.getRequiredList(config, "fields");
	}

	@Override
	public JSONObject filter(JSONObject source) {
		for (String field : fields) {
			if (source.containsKey(field)) {
				source.put(field, source.getString(field).trim());
			}
		}
		return source;
	}
}
