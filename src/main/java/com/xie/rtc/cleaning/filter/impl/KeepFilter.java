package com.xie.rtc.cleaning.filter.impl;

import java.util.List;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.xie.rtc.cleaning.filter.Filter;
import com.xie.rtc.cleaning.filter.FilterType;
import com.xie.rtc.cleaning.util.JSONUtils;

/**
 * @author xiejing.kane
 *
 */
@FilterType("keep")
public class KeepFilter implements Filter {
	private List<String> fields;

	@Override
	public void init(JSONObject config) {
		fields = JSONUtils.getRequiredList(config, "fields");
	}

	@Override
	public JSONObject filter(JSONObject source) {
		for (Entry<String, Object> entry : Sets.newHashSet(source.entrySet())) {
			if (!fields.contains(entry.getKey())) {
				source.remove(entry.getKey());
			}
		}
		return source;
	}
}
