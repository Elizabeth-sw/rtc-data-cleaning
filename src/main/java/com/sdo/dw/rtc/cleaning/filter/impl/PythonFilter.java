package com.sdo.dw.rtc.cleaning.filter.impl;

import java.text.MessageFormat;

import javax.script.ScriptException;

import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sdo.dw.rtc.cleaning.filter.Filter;
import com.sdo.dw.rtc.cleaning.filter.FilterType;

/**
 * @author xiejing.kane
 *
 */
@FilterType("python")
public class PythonFilter implements Filter {
	private static Logger LOGGER = LoggerFactory.getLogger(PythonFilter.class);
	private PythonInterpreter interpreter;
	private String expr;
	private JSONObject config;

	@Override
	public void init(JSONObject config) {
		this.config = config;
		expr = config.getString("expr");
		interpreter = new PythonInterpreter();
		LOGGER.info(MessageFormat.format("expr = {0}", expr));
	}

	@Override
	public JSONObject filter(JSONObject source) throws ScriptException {
		interpreter.cleanup();
		interpreter.set("source", source);
		interpreter.set("config", config);
		return JSON.parseObject(interpreter.eval(expr).asString());
	}
}
