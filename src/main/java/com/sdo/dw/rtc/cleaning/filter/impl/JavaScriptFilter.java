package com.sdo.dw.rtc.cleaning.filter.impl;

import com.sdo.dw.rtc.cleaning.filter.FilterType;
import com.sdo.dw.rtc.cleaning.filter.ScriptFilter;

/**
 * @author xiejing.kane
 *
 */
@FilterType("js")
public class JavaScriptFilter extends ScriptFilter {
	public JavaScriptFilter() {
		super("js");
	}
}
