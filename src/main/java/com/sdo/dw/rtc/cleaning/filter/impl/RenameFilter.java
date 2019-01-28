package com.sdo.dw.rtc.cleaning.filter.impl;

import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;
import com.sdo.dw.rtc.cleaning.filter.Filter;
import com.sdo.dw.rtc.cleaning.filter.FilterType;

/**
 * @author xiejing.kane
 *
 */
@FilterType("rename")
public class RenameFilter implements Filter {
	private JSONObject fields;

	@Override
	public void init(JSONObject config) {
		fields = config.getJSONObject("fields");
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
