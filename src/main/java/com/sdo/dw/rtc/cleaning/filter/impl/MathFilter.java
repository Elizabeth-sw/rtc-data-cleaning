package com.sdo.dw.rtc.cleaning.filter.impl;

import java.lang.reflect.Method;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.sdo.dw.rtc.cleaning.filter.Filter;
import com.sdo.dw.rtc.cleaning.filter.FilterType;
import com.sdo.dw.rtc.cleaning.util.JSONUtils;

/**
 * @author xiejing.kane
 *
 */
@FilterType("math")
public class MathFilter implements Filter {
	private String newField;
	private String method;
	private List<String> args = Lists.newArrayList();

	@Override
	public void init(JSONObject config) {
		newField = JSONUtils.getRequiredString(config, "new_field");
		method = JSONUtils.getRequiredString(config, "method");
		for (Object field : config.getJSONArray("args")) {
			args.add(field.toString());
		}
	}

	@Override
	public JSONObject filter(JSONObject source) throws Exception {
		List<Class<?>> paramTypes = Lists.newArrayList();
		List<Object> params = Lists.newArrayList();
		for (String field : args) {
			Object val = source.get(field);
			params.add(val);
			paramTypes.add((Class<?>) val.getClass().getField("TYPE").get(null));
		}
		Method m = Math.class.getDeclaredMethod(method, paramTypes.toArray(new Class<?>[] {}));
		source.put(newField, m.invoke(null, params.toArray(new Object[] {})));
		return source;
	}

}
