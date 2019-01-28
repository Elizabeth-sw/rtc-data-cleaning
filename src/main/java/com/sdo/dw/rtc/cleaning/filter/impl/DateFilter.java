package com.sdo.dw.rtc.cleaning.filter.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.sdo.dw.rtc.cleaning.filter.Filter;
import com.sdo.dw.rtc.cleaning.filter.FilterType;

/**
 * @author xiejing.kane
 *
 */
@FilterType("date")
public class DateFilter implements Filter {
	private String field;
	private ThreadLocal<SimpleDateFormat> sourceSdf;
	private ThreadLocal<SimpleDateFormat> targetSdf;

	@Override
	public void init(JSONObject config) {
		field = config.getString("field");
		String sourceFormat = config.getString("source");
		String targetFormat = config.getString("target");
		sourceSdf = new ThreadLocal<SimpleDateFormat>() {
			@Override
			protected SimpleDateFormat initialValue() {
				return new SimpleDateFormat(sourceFormat);
			}
		};
		targetSdf = new ThreadLocal<SimpleDateFormat>() {
			@Override
			protected SimpleDateFormat initialValue() {
				return new SimpleDateFormat(targetFormat);
			}
		};
	}

	@Override
	public JSONObject filter(JSONObject source) throws ParseException {
		if (source.containsKey(field)) {
			Date sourceDate = sourceSdf.get().parse(source.getString(field));
			String targetDate = targetSdf.get().format(sourceDate);
			source.put(field, targetDate);
		}
		return source;
	}
}
