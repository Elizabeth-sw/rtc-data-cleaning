package com.xie.rtc.cleaning.filter.impl;

import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;
import com.xie.rtc.cleaning.filter.Filter;
import com.xie.rtc.cleaning.filter.FilterType;
import com.xie.rtc.cleaning.util.JSONUtils;

/**
 * @author xiejing.kane
 *
 */
@FilterType("rename")
public class RenameFilter implements Filter {
	private JSONObject fields;

	@Override
	public void init(JSONObject config) {
		fields = JSONUtils.<JSONObject>getRequired(config, "fields");
	}

	@Override
	public JSONObject filter(JSONObject source) {
		for (Entry<String, Object> entry : fields.entrySet()) {
			String oldName = entry.getKey();
			String newName = entry.getValue().toString();
			if (source.containsKey(oldName)) {
				source.put(newName, source.remove(oldName));
			}
		}
		return source;
	}

}
