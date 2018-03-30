package com.sdo.dw.rtc.cleaning.filter.impl;

import com.sdo.dw.rtc.cleaning.filter.FilterType;
import com.sdo.dw.rtc.cleaning.filter.ScriptFilter;

/**
 * @author xiejing.kane
 *
 */
@FilterType("ruby")
public class RubyFilter extends ScriptFilter {
	public RubyFilter() {
		super("groovy");
	}
}
