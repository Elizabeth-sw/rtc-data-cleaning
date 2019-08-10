package com.xie.rtc.cleaning;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptException;

import org.reflections.Reflections;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.xie.rtc.cleaning.Cleaner;
import com.xie.rtc.cleaning.Result;
import com.xie.rtc.cleaning.filter.Filter;
import com.xie.rtc.cleaning.filter.FilterType;
import com.xie.rtc.cleaning.filter.impl.BoolFilter;
import com.xie.rtc.cleaning.filter.impl.DateFilter;
import com.xie.rtc.cleaning.filter.impl.EvalFilter;
import com.xie.rtc.cleaning.filter.impl.GrokFilter;
import com.xie.rtc.cleaning.filter.impl.IPToLongFilter;
import com.xie.rtc.cleaning.filter.impl.JSONFilter;
import com.xie.rtc.cleaning.filter.impl.JavaFilter;
import com.xie.rtc.cleaning.filter.impl.KeepFilter;
import com.xie.rtc.cleaning.filter.impl.MathFilter;
import com.xie.rtc.cleaning.filter.impl.RemoveFilter;
import com.xie.rtc.cleaning.filter.impl.RenameFilter;
import com.xie.rtc.cleaning.filter.impl.ReplaceAllFilter;
import com.xie.rtc.cleaning.filter.impl.ScriptFilter;
import com.xie.rtc.cleaning.filter.impl.SplitFilter;
import com.xie.rtc.cleaning.filter.impl.TrimFilter;
import com.xie.rtc.cleaning.filter.impl.UnderlineFilter;

import net.sf.jsqlparser.JSQLParserException;

public class Test {
	public static void main(String[] args) throws Exception {
		testMain();
		// testRenameFilter();
		// testAnnotatedFilters();
		// testJavaDynamicFilter();
		// testEvalFilter();
		// testRenameFilter();
		// testDateFilter();
		// testIPToLongFilter();
		// testRemoveFilter();
		// testAddFilter();
		// testTrimFilter();
		// testReplaceAllFilter();
		// testKeepFilter();
		// testJsonFilter();
		// testSplitFilter();
		// testBoolFilter();
		// testUnderlineFilter();
		// testGrokFilter();
		// testScriptFilter();
		// testMathFilter();
	}

	public static void testMain() throws Exception {
		String srcData = "2018-02-09 17:14:04	INFO";
		Cleaner cleaner = Cleaner.create(Test.class.getClassLoader().getResourceAsStream("test.yml"));
		Result result = cleaner.process(srcData);
		System.out.println(JSON.toJSONString(result.getPayload(), true));
	}

	public static void testAnnotatedFilters() throws Exception {
		Map<String, String> annotatedFilters = Maps.newHashMap();
		Reflections reflections = new Reflections(Filter.class.getPackage().getName() + ".impl");
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(FilterType.class);
		for (Class<?> filter : annotated) {
			String annotation = filter.getAnnotation(FilterType.class).value();
			if (annotatedFilters.containsKey(annotation)) {
				throw new Exception(
						MessageFormat.format("Duplicated declaration of Annotation: {0}, classes = [{1}, {2}]",
								annotation, annotatedFilters.get(annotation), filter.getName()));
			}
			annotatedFilters.put(annotation, filter.getName());
		}
		System.out.println(annotatedFilters.keySet());
	}

	public static void testJavaDynamicFilter() throws Exception {
		JavaFilter filter = new JavaFilter();
		JSONObject config = new JSONObject();
		config.put("code_file", Test.class.getClassLoader().getResource("test_code").getPath());
		JSONArray array = new JSONArray();
		array.add("com.google.common.collect.Lists");
		array.add("java.util.List");
		config.put("import", array);
		filter.init(config);
		System.out.println(filter.filter(new JSONObject()));
	}

	public static void testEvalFilter() throws ScriptException {
		// 实现ip to long
		JSONObject config = JSON.parseObject(
				"{\"field\":\"new_calc\", \"expr\":\"var ipl=0;ip.split('.').forEach(function( octet ) {ipl<<=8;ipl+=parseInt(octet);});(ipl >>>0)\"}");
		EvalFilter filter = new EvalFilter();
		filter.init(config);
		JSONObject data = new JSONObject();
		data.put("ip", "70.30.65.9");
		data.put("b", "2");
		System.out.println(filter.filter(data).getLong("new_calc"));
	}

	public static void testScriptFilter() throws ScriptException {
		JSONObject config = JSON.parseObject("{\"field\":\"new_calc\", \"engine\":\"python\", \"script\":\"source\"}");
		ScriptFilter filter = new ScriptFilter();
		filter.init(config);
		JSONObject data = new JSONObject();
		data.put("val", 1.1);
		System.out.println(filter.filter(data));
	}

	public static void testRenameFilter() {
		JSONObject config = JSON.parseObject("{\"fields\":{\"gameId\":\"game_id\",\"monthnum\":\"MONTH\"}}");
		RenameFilter filter = new RenameFilter();
		filter.init(config);
		JSONObject data = new JSONObject();
		data.put("gameId", "123");
		data.put("areaId", "456");
		data.put("vaLue", "789");
		System.out.println(filter.filter(data));
	}

	public static void testDateFilter() throws ParseException {
		JSONObject config = JSON
				.parseObject("{\"field\":\"event_time\",\"source\":\"yyyyMMdd\",\"target\":\"yyyy-MM-dd\"}");
		DateFilter filter = new DateFilter();
		filter.init(config);
		JSONObject data = new JSONObject();
		data.put("event_time", "20180131");
		System.out.println(filter.filter(data));
	}

	public static void testIPToLongFilter() throws ParseException {
		JSONObject config = JSON.parseObject("{\"field\":\"ip\",\"new_field\":\"ip_long\"}");
		IPToLongFilter filter = new IPToLongFilter();
		filter.init(config);
		JSONObject data = new JSONObject();
		data.put("ip", "70.30.65.9");
		System.out.println(filter.filter(data));
	}

	public static void testRemoveFilter() {
		JSONObject config = JSON.parseObject("{\"fields\":[\"abc\"]}");
		RemoveFilter filter = new RemoveFilter();
		filter.init(config);
		JSONObject data = new JSONObject();
		data.put("event_time", "20180131");
		data.put("abc", "def");
		System.out.println(filter.filter(data));
	}

	public static void testAddFilter() throws Exception {
		JSONObject config = JSON
				.parseObject("{\"fields\":{\"newf1\":\"v1\",\"newf2\":\"v2\"}, \"preserve_existing\":false}");
		Filter filter = (Filter) Class.forName("com.xie.rtc.cleaning.filter.impl.AddFilter").newInstance();
		filter.init(config);
		JSONObject data = new JSONObject();
		data.put("newf1", "20180131");
		System.out.println(filter.filter(data));
	}

	public static void testTrimFilter() {
		JSONObject config = JSON.parseObject("{\"fields\":[\"abc\"]}");
		TrimFilter filter = new TrimFilter();
		filter.init(config);
		JSONObject data = new JSONObject();
		data.put("event_time", "20180131  ");
		data.put("abc", "  def  ");
		System.out.println(filter.filter(data));
	}

	public static void testReplaceAllFilter() {
		JSONObject config = JSON.parseObject("{\"field\":\"event_time\",\"regex\":\"a*c\",\"repl\":\"def\"}");
		ReplaceAllFilter filter = new ReplaceAllFilter();
		filter.init(config);
		JSONObject data = new JSONObject();
		data.put("event_time", "adc");
		System.out.println(filter.filter(data));
	}

	public static void testKeepFilter() {
		JSONObject config = JSON.parseObject("{\"fields\":[\"abc\"]}");
		KeepFilter filter = new KeepFilter();
		filter.init(config);
		JSONObject data = new JSONObject();
		data.put("event_time", "20180131");
		data.put("abc", "def");
		System.out.println(filter.filter(data));
	}

	public static void testJsonFilter() throws Exception {
		JSONObject config = JSON.parseObject(
				"{\"field\":\"abc\", \"discard_existing\":false, \"preserve_existing\":false, \"append_prefix\":true}");
		JSONFilter filter = new JSONFilter();
		filter.init(config);
		JSONObject data = new JSONObject();
		data.put("gameId", "ttt");
		data.put("abc", "{\"gameId\":\"game_id\",\"monthnum\":\"MONTH\"}");
		System.out.println(filter.filter(data));
	}

	public static void testSplitFilter() throws Exception {
		JSONObject config = JSON.parseObject(
				"{\"field\":\"abc\", \"discard_existing\":false, \"preserve_existing\":true, \"append_prefix\":false, \"delimiter\":\"\\\\^_\\\\^\", \"assigner\":\":\"}");
		SplitFilter filter = new SplitFilter();
		filter.init(config);
		JSONObject data = new JSONObject();
		data.put("gameId", "ttt");
		data.put("abc", "gameId:123^_^area_id:9");
		System.out.println(filter.filter(data));
	}

	public static void testBoolFilter() throws JSQLParserException {
		JSONObject config = JSON.parseObject("{\"conditions\":\" (a!='qq2' or b!=123.1) or c is null \"}");
		BoolFilter filter = new BoolFilter();
		filter.init(config);
		JSONObject data = new JSONObject();
		data.put("a", "qq1");
		data.put("b", "123.1");
		data.put("c", "1");
		System.out.println(filter.filter(data));
	}

	public static void testUnderlineFilter() {
		JSONObject config = JSON.parseObject("{\"fields\":[\"*\"]}");
		UnderlineFilter filter = new UnderlineFilter();
		filter.init(config);
		JSONObject data = new JSONObject();
		data.put("event_time", "20180131");
		data.put("GameId", "def");
		System.out.println(filter.filter(data));
	}

	public static void testGrokFilter() throws Exception {
		JSONObject config = JSON.parseObject(
				"{\"field\":\"abc\", \"discard_existing\":false, \"preserve_existing\":true, \"append_prefix\":false, \"entry\":\"SPLIT_DATA\", \"patterns\":{\"SPLIT_DATA\":\"%{DATA:f1}\\\\|%{DATA:f2}\"}}");
		System.out.println(JSON.toJSONString(config, true));
		GrokFilter filter = new GrokFilter();
		filter.init(config);
		JSONObject data = new JSONObject();
		data.put("event_time", "20180131");
		data.put("abc", "12|er");
		System.out.println(filter.filter(data));
	}

	public static void testMathFilter() throws Exception {
		JSONObject config = new JSONObject();
		config.put("new_field", "x");
		config.put("method", "max");
		config.put("args", JSON.parseArray("[ \"a\", \"b\"]"));
		MathFilter filter = new MathFilter();
		filter.init(config);
		JSONObject data = new JSONObject();
		data.put("a", 1);
		data.put("b", -2);
		System.out.println(filter.filter(data));
	}
}
