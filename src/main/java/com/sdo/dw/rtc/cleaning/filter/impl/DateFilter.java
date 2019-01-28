package com.sdo.dw.rtc.cleaning.filter.impl;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;

import com.alibaba.fastjson.JSONObject;
import com.sdo.dw.rtc.cleaning.filter.Filter;
import com.sdo.dw.rtc.cleaning.filter.FilterType;
import com.sdo.dw.rtc.cleaning.util.JSONUtils;

/**
 * @author xiejing.kane
 *
 */
@FilterType("date")
public class DateFilter implements Filter {
	private String field;
	private DateTimeFormatter sourceFormatter;
	private DateTimeFormatter targetFormatter;

	@Override
	public void init(JSONObject config) {
		field = config.getString("field");
		String sourceFormat = JSONUtils.getRequiredString(config, "source");
		String targetFormat = JSONUtils.getRequiredString(config, "target");

		sourceFormatter = DateTimeFormatter.ofPattern(sourceFormat);
		targetFormatter = DateTimeFormatter.ofPattern(targetFormat);
	}

	@Override
	public JSONObject filter(JSONObject source) throws ParseException {
		if (source.containsKey(field)) {
			source.put(field, targetFormatter.format(sourceFormatter.parse(source.getString(field))));
		}
		return source;
	}
}
