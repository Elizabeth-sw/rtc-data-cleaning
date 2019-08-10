package com.xie.rtc.cleaning.filter.impl;

import javax.script.ScriptContext;
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
@FilterType("script")
public class ScriptFilter implements Filter {
	protected ScriptEngine engine;
	protected String script;
	private String engineName;

	@Override
	public void init(JSONObject config) {
		script = JSONUtils.getRequiredString(config, "script");
		engineName = JSONUtils.getRequiredString(config, "engine");
		engine = new ScriptEngineManager().getEngineByName(engineName);
		engine.getBindings(ScriptContext.GLOBAL_SCOPE).put("config", config);
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
