package com.xie.rtc.cleaning.filter.impl;

import java.util.Map.Entry;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.alibaba.fastjson.JSONObject;
import com.xie.rtc.cleaning.filter.Filter;
import com.xie.rtc.cleaning.filter.FilterType;
import com.xie.rtc.cleaning.util.JSONUtils;

/**
 * @author xiejing.kane
 *
 */
@FilterType("eval")
public class EvalFilter implements Filter {
	private ScriptEngine engine;
	private String field;
	private String expr;

	@Override
	public void init(JSONObject config) {
		field = JSONUtils.getRequiredString(config, "field");
		expr = JSONUtils.getRequiredString(config, "expr");
		ScriptEngineManager mgr = new ScriptEngineManager();
		engine = mgr.getEngineByName("JavaScript");
	}

	@Override
	public JSONObject filter(JSONObject source) throws ScriptException {
		for (Entry<String, Object> entry : source.entrySet()) {
			engine.put(entry.getKey(), entry.getValue());
		}
		source.put(field, engine.eval(expr));
		return source;
	}
}
