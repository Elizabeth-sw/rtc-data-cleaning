package com.xie.rtc.cleaning.filter.impl;

import java.util.List;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.xie.rtc.cleaning.filter.Filter;
import com.xie.rtc.cleaning.filter.FilterType;
import com.xie.rtc.cleaning.util.CommonUtils;
import com.xie.rtc.cleaning.util.JSONUtils;

/**
 * 将key由驼峰表达式替换成下划线表达式
 * <p>
 * gameId -> game_id
 * 
 * @author xiejing.kane
 *
 */
@FilterType("underline")
public class UnderlineFilter implements Filter {
	private List<String> fields;

	@Override
	public void init(JSONObject config) {
		fields = JSONUtils.getRequiredList(config, "fields");
	}

	@Override
	public JSONObject filter(JSONObject source) {
		for (Entry<String, Object> entry : Sets.newHashSet(source.entrySet())) {
			String key = entry.getKey();
			if (fields.contains(key) || fields.contains("*")) {
				source.put(CommonUtils.camel2Underline(key), source.remove(key));
			}
		}
		return source;
	}
}
