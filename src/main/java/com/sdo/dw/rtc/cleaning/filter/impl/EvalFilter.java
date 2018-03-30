package com.sdo.dw.rtc.cleaning.filter.impl;

import java.text.MessageFormat;
import java.util.Map.Entry;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.sdo.dw.rtc.cleaning.filter.Filter;
import com.sdo.dw.rtc.cleaning.filter.FilterType;

/**
 * @author xiejing.kane
 *
 */
@FilterType("eval")
public class EvalFilter implements Filter {
	private static Logger LOGGER = LoggerFactory.getLogger(EvalFilter.class);
	private ScriptEngine engine;
	private String field;
	private String expr;

	@Override
	public void init(JSONObject config) {
		field = config.getString("field");
		expr = config.getString("expr");
		ScriptEngineManager mgr = new ScriptEngineManager();
		engine = mgr.getEngineByName("JavaScript");
		LOGGER.info(MessageFormat.format("field = {0}, expr = {1}", field, expr));
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
