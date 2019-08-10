package com.xie.rtc.cleaning.decoder.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.xie.rtc.cleaning.Context;
import com.xie.rtc.cleaning.decoder.Decoder;
import com.xie.rtc.cleaning.decoder.DecoderType;
import com.xie.rtc.cleaning.exception.InvalidParameterException;

/**
 * @author xiejing.kane
 *
 */
@DecoderType("json")
public class JSONDecoder implements Decoder {
	private static final Logger LOGGER = LoggerFactory.getLogger(JSONDecoder.class);

	@Override
	public void init(Context decoderContext) throws InvalidParameterException {
	}

	@Override
	public JSONObject decode(String source) {
		JSONObject json = JSONObject.parseObject(source);
		LOGGER.trace("decode result = " + json);
		return json;
	}

}
