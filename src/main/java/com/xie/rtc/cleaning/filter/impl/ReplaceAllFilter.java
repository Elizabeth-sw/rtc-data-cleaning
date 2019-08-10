package com.sdo.dw.rtc.cleaning.filter.impl;

import com.alibaba.fastjson.JSONObject;
import com.sdo.dw.rtc.cleaning.filter.Filter;
import com.sdo.dw.rtc.cleaning.filter.FilterType;
import com.sdo.dw.rtc.cleaning.util.JSONUtils;

/**
 * @author xiejing.kane
 *
 */
@FilterType("replaceall")
public class ReplaceAllFilter implements Filter {
	private String field;

	private String regex;
	private String repl;

	@Override
	public void init(JSONObject config) {
		field = JSONUtils.getRequiredString(config, "field");
		regex = JSONUtils.getRequiredString(config, "regex");
		repl = JSONUtils.getRequiredString(config, "repl");
	}

	@Override
	public JSONObject filter(JSONObject source) {
		if (source.containsKey(field)) {
			String value = source.getString(field).replaceAll(regex, repl);
			source.put(field, value);
		}
		return source;
	}
}
