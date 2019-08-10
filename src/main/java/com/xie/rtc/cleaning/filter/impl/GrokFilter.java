package com.xie.rtc.cleaning.filter.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xie.rtc.cleaning.filter.Filter;
import com.xie.rtc.cleaning.filter.FilterType;
import com.xie.rtc.cleaning.util.CommonUtils;
import com.xie.rtc.cleaning.util.ExtractCallable;
import com.xie.rtc.cleaning.util.JSONUtils;

import io.thekraken.grok.api.Grok;
import io.thekraken.grok.api.Match;

/**
 * @author xiejing.kane
 *
 */
@FilterType("grok")
public class GrokFilter implements Filter {
	private static Logger LOGGER = LoggerFactory.getLogger(GrokFilter.class);
	private static final boolean DEFAULT_DISCARD_EXISTING = false;
	private static final boolean DEFAULT_PRESERVE_EXISTING = true;
	private static final boolean DEFAULT_APPEND_PREFIX = false;

	private String field;
	private boolean discardExisting;
	private boolean preserveExisting;
	/**
	 * 注意：设置appendPrefix=true的话，后续的filter使用字段时要带上前缀，否则将会找不到field
	 */
	private boolean appendPrefix;
	private Grok grok;

	@Override
	public void init(JSONObject config) throws Exception {
		field = JSONUtils.getRequiredString(config, "field");
		discardExisting = (boolean) config.getOrDefault("discard_existing", DEFAULT_DISCARD_EXISTING);
		preserveExisting = (boolean) config.getOrDefault("preserve_existing", DEFAULT_PRESERVE_EXISTING);
		appendPrefix = (boolean) config.getOrDefault("append_prefix", DEFAULT_APPEND_PREFIX);
		initGrok(config);
	}

	private void initGrok(JSONObject config) throws Exception {
		String name = config.getString("entry");
		JSONObject patterns = config.getJSONObject("patterns");
		grok = CommonUtils.initGrok(name, patterns, null);
	}

	@Override
	public JSONObject filter(JSONObject source) throws Exception {
		return CommonUtils.extract(source, field, discardExisting, preserveExisting, appendPrefix,
				new ExtractCallable() {
					@Override
					public JSONObject call() throws Exception {
						Match gm = grok.match(getSource());
						gm.captures();
						JSONObject json = JSON.parseObject(gm.toJson());
						LOGGER.trace("decode result = " + json);
						return json;
					}
				});
	}
}
