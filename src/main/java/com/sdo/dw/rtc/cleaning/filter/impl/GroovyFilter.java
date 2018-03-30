package com.sdo.dw.rtc.cleaning.filter.impl;

import com.sdo.dw.rtc.cleaning.filter.FilterType;
import com.sdo.dw.rtc.cleaning.filter.ScriptFilter;

/**
 * @author xiejing.kane
 *
 */
@FilterType("groovy")
public class GroovyFilter extends ScriptFilter {
	public GroovyFilter() {
		super("groovy");
	}
}
