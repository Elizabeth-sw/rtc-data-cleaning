package com.sdo.dw.rtc.cleaning.filter.impl;

import com.alibaba.fastjson.JSONObject;
import com.sdo.dw.rtc.cleaning.filter.Filter;
import com.sdo.dw.rtc.cleaning.filter.FilterType;
import com.sdo.dw.rtc.cleaning.util.CommonUtils;
import com.sdo.dw.rtc.cleaning.util.ExtractCallable;
import com.sdo.dw.rtc.cleaning.util.JSONUtils;

/**
 * @author xiejing.kane
 *
 */
@FilterType("split")
public class SplitFilter implements Filter {
	private static final boolean DEFAULT_DISCARD_EXISTING = false;
	private static final boolean DEFAULT_PRESERVE_EXISTING = true;
	private static final boolean DEFAULT_APPEND_PREFIX = false;

	private String field;
	private boolean discardExisting;
	private boolean preserveExisting;
	private String delimiter;
	private String assigner;

	/**
	 * 注意：设置appendPrefix=true的话，后续的filter使用字段时要带上前缀，否则将会找不到field
	 */
	private boolean appendPrefix;

	@Override
	public void init(JSONObject config) {
		field = JSONUtils.getRequiredString(config, "field");
		delimiter = JSONUtils.getRequiredString(config, "delimiter");
		assigner = JSONUtils.getRequiredString(config, "assigner");
		
		discardExisting = (boolean) config.getOrDefault("discard_existing", DEFAULT_DISCARD_EXISTING);
		preserveExisting = (boolean) config.getOrDefault("preserve_existing", DEFAULT_PRESERVE_EXISTING);
		appendPrefix = (boolean) config.getOrDefault("append_prefix", DEFAULT_APPEND_PREFIX);
		
	}

	@Override
	public JSONObject filter(JSONObject source) throws Exception {
		return CommonUtils.extract(source, field, discardExisting, preserveExisting, appendPrefix,
				new ExtractCallable() {
					@Override
					public JSONObject call() throws Exception {
						JSONObject result = new JSONObject();
						for (String segment : getSource().split(delimiter)) {
							String[] keyValue = segment.split(assigner);
							result.put(keyValue[0], keyValue[1]);
						}
						return result;
					}
				});
	}
}
