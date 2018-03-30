package com.sdo.dw.rtc.cleaning.filter;

import java.text.MessageFormat;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author xiejing.kane
 *
 */
public class ScriptFilter implements Filter {
	private static Logger LOGGER = LoggerFactory.getLogger(ScriptFilter.class);
	protected ScriptEngine engine;
	protected String expr;
	private String engineName;

	protected ScriptFilter(String engineName) {
		this.engineName = engineName;
	}

	@Override
	public void init(JSONObject config) {
		expr = config.getString("expr");
		engine = new ScriptEngineManager().getEngineByName(engineName);
		engine.getBindings(ScriptContext.GLOBAL_SCOPE).put("config", config);
		LOGGER.info(MessageFormat.format("engineName = {0}, expr = {2}", engineName, expr));
	}

	@Override
	public JSONObject filter(JSONObject source) throws ScriptException {
		engine.getBindings(ScriptContext.ENGINE_SCOPE).clear();
		engine.put("source", source);
		return JSON.parseObject(engine.eval(expr).toString());
	}
}
