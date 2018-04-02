package com.sdo.dw.rtc.cleaning.filter.impl;

import java.text.MessageFormat;

import javax.script.ScriptContext;
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
@FilterType("script")
public class ScriptFilter implements Filter {
	private static Logger LOGGER = LoggerFactory.getLogger(ScriptFilter.class);
	protected ScriptEngine engine;
	protected String script;
	private String engineName;

	@Override
	public void init(JSONObject config) {
		script = config.getString("script");
		engineName = config.getString("engine");
		engine = new ScriptEngineManager().getEngineByName(engineName);
		engine.getBindings(ScriptContext.GLOBAL_SCOPE).put("config", config);
		LOGGER.info(MessageFormat.format("engineName = {0}, script = {1}", engineName, script));
	}

	@Override
	public JSONObject filter(JSONObject source) throws ScriptException {
		engine.getBindings(ScriptContext.ENGINE_SCOPE).clear();
		engine.getBindings(ScriptContext.ENGINE_SCOPE).put("source", source);
		engine.put("source", source);
		engine.eval(script);
		return source;
	}
}
