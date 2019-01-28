package com.sdo.dw.rtc.cleaning.filter.impl;

import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;
import com.sdo.dw.rtc.cleaning.filter.Filter;
import com.sdo.dw.rtc.cleaning.filter.FilterType;
import com.sdo.dw.rtc.cleaning.util.JSONUtils;

/**
 * @author xiejing.kane
 *
 */
@FilterType("add")
public class AddFilter implements Filter {
	private JSONObject fields;
	private boolean preserveExisting = true;

	@Override
	public void init(JSONObject config) {
		fields = JSONUtils.<JSONObject>getRequired(config, "fields");
		preserveExisting = config.getBooleanValue("preserve_existing");
	}

	@Override
	public JSONObject filter(JSONObject source) {
		for (Entry<String, Object> entry : fields.entrySet()) {
			if (source.containsKey(entry.getKey()) && preserveExisting) {
				continue;
			}
			source.put(entry.getKey(), entry.getValue());
		}
		return source;
	}
}
