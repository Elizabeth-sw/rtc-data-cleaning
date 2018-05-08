package com.sdo.dw.rtc.cleaning;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.sdo.dw.rtc.cleaning.exception.InvalidParameterException;

/**
 * The context is a key-value store used to pass configuration information
 * throughout the Formatter.
 * 
 * @author xiejing.kane
 *
 */
public class Context {

	private JSONObject parameters;

	public Context() {
		parameters = new JSONObject();
	}

	public Context(JSONObject json) {
		this.parameters = JSON.parseObject(json.toJSONString());
	}

	/**
	 * Gets a copy of the backing json structure.
	 * 
	 * @return immutable copy of backing json structure
	 */
	public JSONObject getParameters() {
		return JSON.parseObject(parameters.toJSONString());
	}

	/**
	 * Get properties which start with a prefix. When a property is returned, the
	 * prefix is removed the from name. For example, if this method is called with a
	 * parameter &quot;hdfs.&quot; and the context contains: <code>
	 * { hdfs.key = value, otherKey = otherValue }
	 * </code> this method will return a map containing: <code>
	 * { key = value}
	 * </code>
	 *
	 * <b>Note:</b> The <tt>prefix</tt> must end with a period character. If not
	 * this method will raise an IllegalArgumentException.
	 *
	 * @param prefix
	 *            key prefix to find and remove from keys in resulting map
	 * @return map with keys which matched prefix with prefix removed from keys in
	 *         resulting map. If no keys are matched, the returned map is empty
	 * @throws IllegalArguemntException
	 *             if the given prefix does not end with a period character.
	 */
	public JSONObject getSubProperties(String prefix) {
		Preconditions.checkArgument(prefix.endsWith("."),
				"The given prefix does not end with a period (" + prefix + ")");
		JSONObject result = new JSONObject();
		JSONObject paraCopy = getParameters();
		for (String key : paraCopy.keySet()) {
			if (key.startsWith(prefix)) {
				String name = key.substring(prefix.length());
				result.put(name, paraCopy.get(key));
			}
		}
		return result;
	}

	/**
	 * Associates all of the given map's keys and values in the Context.
	 */
	public void putAll(Map<String, String> map) {
		parameters.putAll(map);
	}

	/**
	 * Associates the specified value with the specified key in this context. If the
	 * context previously contained a mapping for the key, the old value is replaced
	 * by the specified value.
	 * 
	 * @param key
	 *            key with which the specified value is to be associated
	 * @param value
	 *            to be associated with the specified key
	 */
	public void put(String key, String value) {
		parameters.put(key, value);
	}

	/**
	 * Returns true if this Context contains a mapping for key. Otherwise, returns
	 * false.
	 */
	public boolean containsKey(String key) {
		return parameters.containsKey(key);
	}

	/**
	 * Gets value mapped to key, returning null if unmapped.
	 * <p>
	 * Note that this method returns an object as opposed to a primitive. The
	 * configuration key requested may not be mapped to a value and by returning the
	 * primitive object wrapper we can return null. If the key does not exist the
	 * return value of this method is assigned directly to a primitive, a
	 * {@link NullPointerException} will be thrown.
	 * </p>
	 * 
	 * @param key
	 *            to be found
	 * @return value associated with key or null if unmapped
	 */
	public Boolean getBoolean(String key) {
		return parameters.getBoolean(key);
	}

	/**
	 * Gets value mapped to key, returning null if unmapped.
	 * <p>
	 * Note that this method returns an object as opposed to a primitive. The
	 * configuration key requested may not be mapped to a value and by returning the
	 * primitive object wrapper we can return null. If the key does not exist the
	 * return value of this method is assigned directly to a primitive, a
	 * {@link NullPointerException} will be thrown.
	 * </p>
	 * 
	 * @param key
	 *            to be found
	 * @return value associated with key or null if unmapped
	 */
	public Integer getInteger(String key) {
		return parameters.getInteger(key);
	}

	/**
	 * Gets value mapped to key, returning null if unmapped.
	 * <p>
	 * Note that this method returns an object as opposed to a primitive. The
	 * configuration key requested may not be mapped to a value and by returning the
	 * primitive object wrapper we can return null. If the key does not exist the
	 * return value of this method is assigned directly to a primitive, a
	 * {@link NullPointerException} will be thrown.
	 * </p>
	 * 
	 * @param key
	 *            to be found
	 * @return value associated with key or null if unmapped
	 */
	public Long getLong(String key) {
		return parameters.getLong(key);
	}

	/**
	 * Gets value mapped to key, returning defaultValue if unmapped.
	 * 
	 * @param key
	 *            to be found
	 * @param defaultValue
	 *            returned if key is unmapped
	 * @return value associated with key
	 */
	public String getString(String key, String defaultValue) {
		if (containsKey(key)) {
			return parameters.getString(key);
		}
		return defaultValue;
	}

	/**
	 * Gets value mapped to key, returning null if unmapped.
	 * 
	 * @param key
	 *            to be found
	 * @return value associated with key or null if unmapped
	 */
	public String getString(String key) {
		return parameters.getString(key);
	}

	/**
	 * Gets value mapped to key, throw InvalidFormatException if unmapped.
	 * 
	 * @param key
	 *            to be found
	 * @return value associated with key or null if unmapped
	 * @throws InvalidParameterException
	 */
	public JSONArray getJSONArray(String key) throws InvalidParameterException {
		return parameters.getJSONArray(key);
	}

	/**
	 * Gets value mapped to key, throw InvalidFormatException if unmapped.
	 * 
	 * @param key
	 *            to be found
	 * @return value associated with key or null if unmapped
	 * @throws InvalidParameterException
	 */
	public JSONObject getJSONObject(String key) throws InvalidParameterException {
		if (containsKey(key)) {
			try {
				return parameters.getJSONObject(key);
			} catch (Exception e) {
				throw new InvalidParameterException("Invalid JSONObject format: " + parameters.get(key), e);
			}
		}
		throw new InvalidParameterException("unmapped key: " + key);
	}

	/**
	 * Gets value mapped to key, throw InvalidFormatException if unmapped.
	 * 
	 * @param key
	 *            to be found
	 * @return value associated with key or null if unmapped
	 * @throws InvalidParameterException
	 */
	public Context getSubContext(String key) throws InvalidParameterException {
		if (containsKey(key)) {
			try {
				return new Context(getJSONObject(key));
			} catch (Exception e) {
				throw new InvalidParameterException("Invalid Context format: " + get(key), e);
			}
		}
		throw new InvalidParameterException("unmapped key: " + key);
	}

	public Object get(String key, Object defaultValue) {
		Object result = parameters.get(key);
		if (result != null) {
			return result;
		}
		return defaultValue;
	}

	public Object get(String key) {
		return get(key, null);
	}

	@Override
	public String toString() {
		return "{ parameters:" + parameters + " }";
	}
}
