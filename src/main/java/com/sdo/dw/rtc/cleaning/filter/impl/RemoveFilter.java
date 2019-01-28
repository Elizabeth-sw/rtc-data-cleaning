package com.sdo.dw.rtc.cleaning.filter.impl;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.sdo.dw.rtc.cleaning.filter.Filter;
import com.sdo.dw.rtc.cleaning.filter.FilterType;

/**
 * @author xiejing.kane
 *
 */
@FilterType("remove")
public class RemoveFilter implements Filter {
	private List<String> fields;

	@Override
	public void init(JSONObject config) {
		fields = Lists.newArrayList(config.getJSONArray("fields").toArray(new String[] {}));
	}

	@Override
	public JSONObject filter(JSONObject source) {
		for (String field : fields) {
			source.remove(field);
		}
		return source;
	}
}
