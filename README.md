在ETL中的"T"或者是实时流处理中我们经常需要针对每个用户需求开发一个类或者代码块来完成数据清洗的工作

使用这个类库，你只需要写一个YAML配置，就能轻松完成需求，节省大量开发，测试，发布和维护成本

Cleaner的输入是一个String，最终输出是一个JSON。这里借鉴了Logstash里的filter的概念，但这里为两类，decoder和filter。decoder负责将一个String解析成JSON；filter负责将一个JSON转化成另一个(也可以是同一个)JSON，最终形成一个清洗链

* [Getting Started](./README.md#user-content-getting-started)
* [Sample Config](./README.md#user-content-sample-config)
	* [Decoder](./README.md#user-content-decoder): [json](./README.md#user-content-json) [grok](./README.md#user-content-grok)
	* [Filters](./README.md#user-content-filters): [rename](./README.md#user-content-rename) [remove](./README.md#user-content-remove) [keep](./README.md#user-content-keep) [underline](./README.md#user-content-underline) [iptolong](./README.md#user-content-iptolong) [add](./README.md#user-content-add) [date](./README.md#user-content-date) [trim](./README.md#user-content-trim) [replaceall](./README.md#user-content-replaceall) [bool](./README.md#user-content-bool) [json](./README.md#user-content-json) [split](./README.md#user-content-split) [grok](./README.md#user-content-grok) [eval](./README.md#user-content-eval) [java](./README.md#user-content-java) [math](./README.md#user-content-math)
* [自定义插件](./README.md#user-content-自定义插件)


# Getting Started
* 使用非常的简单，代码如下：

```java
String srcData = "2018-02-09 17:14:04	INFO";
Cleaner cleaner = Cleaner.create(<YourClass>.class.getClassLoader().getResourceAsStream("<path>/test.yml"));
Result result = cleaner.process(srcData);
System.out.println(JSON.toJSONString(result.getPayload(), true));
```

* test.yml是清洗的配置文件，具体配置见下一章节

```
decoder:
  grok_patterns: {TESTLOG: "%{DATA:eventTime}\t%{GREEDYDATA:level}"}
  grok_entry: TESTLOG
  type: grok
filters:
- type: date
  params: {field: eventTime, source: 'yyyy-MM-dd HH:mm:ss', target: yyyyMMdd HHmmss}
- type: underline
  params:
    fields: [eventTime]
```

* 这里做了三步处理:
1. 从日志中解析出eventTime字段和level字段
2. 将eventTime字段的格式由yyyy-MM-dd HH:mm:ss改变为yyyyMMdd HHmmss
3. 将字段名eventTime的格式由驼峰表达式转化为下划线表达式event_time

* 最终得到清洗后的数据
```
{
	"level":"INFO",
	"event_time":"20180209 171404"
}

```

# Sample Config
具体用法可以参考[测试代码](./src/test/java/com/sdo/dw/rtc/cleaning/Test.java)

## Decoder
### json
* 描述
```
将source string直接解析为jsonobject
```
* 范例
```
decoder:
  type: json
```

### grok
* 描述
```
通过正则表达式解析source string为jsonobject,语法参考logstash grok
```
* 参数
```
grok_patterns：正则表达式，默认会加载default_patterns中的所有正则表达式，如果entry已存在，则覆盖
grok_patterns_file：正则表达式文件
grok_entry：正则入口
```
* 范例
```
decoder:
  type: grok
  grok_patterns:
    YEAR: (?>\d\d){1,2}
    MONTHNUM: (?:0?[1-9]|1[0-2])
    DATE_CN: '%{YEAR:year}[/-]%{MONTHNUM:monthnum}'
  grok_patterns_file: src\main\resources\default_patterns
  grok_entry: DATE_CN
```
		
## Filters
### rename
* 描述
```
对key进行重命名
```
* 参数
```
fields：重命名的keys
```
* 范例
```
type: rename
params:
  fields:
    gameId: game_id
    monthnum: MONTH
```

### remove
* 描述
```
删除指定的key
```
* 参数
```
fields：需要删除的keys
```
* 范例
```
type: remove
params:
  fields:
  - abc
  - def
```

### keep
* 描述
```
与remove相反，保留指定的key，其他的删除
```
* 参数
```
fields：需要保留的keys
```
* 范例
```
type: keep
params:
  fields:
  - messageType
  - settleTime
  - ptId
```		

### underline
* 描述
```
对key进行格式化，由驼峰表达式转为下划线表达式
```
* 参数
```
fields：需要格式化的keys
```
* 范例
```
type: underline
params:
  fields:
  - messageType
```

### iptolong
* 描述
```
将指定的值由ip格式转为long
```
* 参数
```
field: 需要格式化的字段对应的key
newField: 格式化之后写入的新字段的key
```
* 范例
```
type: iptolong
params:
  field: ip
  new_field: ip_long
```

### add
* 描述
```
添加静态key value
```
* 参数
```
fields：需要添加的kv list
preserve_existing：如果与原有的key冲突，是否保留原有数据
```
* 范例
```
type: add
params:
  preserve_existing: true
  fields:
    newf2: v2
    newf1: v1
```

### date
* 描述
```
对日志类型的值进行格式化
```
* 参数
```
field：需要格式化的key
source：源格式
target：目标格式
```
* 范例
```
type: date
params:
  field: event_time
  source: yyyyMMdd
  target: yyyy-MM-dd
```		

### trim
* 描述
```
去掉value前后的所有space
```
* 参数
```
需要trim的keys
```
* 范例
```
type: trim
params:
  fields:
  - abc
  - def
```

### replaceall
* 描述
```
对value做正则替换
```
* 参数
```
field：需要做替换的key
regex：需要匹配的正则表达式
repl：用于替换的字符串
```
* 范例
```
type: replaceall
params:
  regex: abc
  field: user_name
  repl: def
```

### bool
* 描述
```
根据条件过滤，只有满足条件的数据才会被保留
```
* 参数
```
conditions：条件表达式，语法同sql的where字句，只支持比较运算符 > >= = != < <= ,空值判断is null，is not null,以及布尔运算符and or
```
* 范例
```
type: bool
params:
  conditions: ' (a!=''qq'' or b!=123.1) and c !=1'
```

### json
* 描述
```
将某个value解析为json并提取到原数据中
```
* 参数
```
field：要提取的value
discard_existing：是否丢弃原数据
preserve_existing：如果key冲突，是否保留原数据
append_prefix：是否给解析出来的key添加前缀
```
* 范例
```
type: json
params: 
	append_prefix: false
	discard_existing: false
	field: abc
	preserve_existing: true
```

### split
* 描述
```
将某个value按照分割符拆分
```
* 参数
```
field：要提取的value
discard_existing：是否丢弃原数据
preserve_existing：如果key冲突，是否保留原数据
append_prefix：是否给解析出来的key添加前缀
delimiter:分割符
assigner:赋值符
```
* 范例
```
type: split
params:
  append_prefix: false
  discard_existing: false
  field: abc
  delimiter: ','
  preserve_existing: true
  assigner: ':'
```

### grok
* 描述
```
将某个value用正则表达式解析并提取到原数据中
```
* 参数
```
field：要提取的value
discard_existing：是否丢弃原数据
preserve_existing：如果key冲突，是否保留原数据
append_prefix：是否给解析出来的key添加前缀
patterns：用于解析的正则表达式，默认会加载default_patterns中的所有正则表达式，如果entry已存在，则覆盖
entry：正则解析入口
```
* 范例
```
type: grok
params:
  append_prefix: false
  discard_existing: false
  entry: SPLIT_DATA
  field: abc
  preserve_existing: true
  patterns:
    SPLIT_DATA: '%{DATA:f1}|%{GREEDYDATA:f2}'
```

### eval
* 描述
```
对数据进行重新运算
```
* 参数
```
field：计算结果保存的key
expr：计算表达式
```
* 范例
```
type: eval
params:
  field: new_calc
  expr: (price/2 + count*5)
```

### java
* 描述
```
使用java代码片段进行数据处理。
```
* 参数
```
code：代码
code_file：代码片段文件，如果同时指定了code和code_file，code优先.注意:代码中不允许出现双斜杠注释
import：代码需要引入的类，如果pom里并没有指定相应的dependency，则需要事先提交给管理员
```
* 范例
```
type: java
params:
  code_file: /tmp/code
  import:
  - com.google.common.collect.Lists
```

### math
* 描述
```
使用java.lang.Math函数
```
* 参数
```
method：函数名
args: 参与计算的key
new_field：计算结果写入的新字段
```
* 范例
```
type: math
params:
  method: max
  args:
  - field_a
  - field_b
  new_field: max_a_b
```

# 自定义插件
开发和使用自定义的decoder和filter也很简单，以decoder为例

* 在`com.sdo.dw.rtc.cleaning.decoder.impl`包中新建`Decoder`接口的实现类`MyDecoder`，并且加上`@DecoderType("my")`注解，注解的值即为该decoder的id
```
package test;

@DecoderType("my")
public class MyDecoder implements Decoder {
	private static final Logger LOGGER = LoggerFactory.getLogger(MyDecoder.class);

	@Override
	public void init(Context decoderContext) throws InvalidParameterException {
	}

	@Override
	public JSONObject decode(String source) {
		// construct json from source
		return json;
	}

}
```
* 然后就可以在配置中通过id调用该decoder
```
decoder:
  type: my
  my_param1: abc
  my_param2: 123
```
其中`my_param1`和`my_param2`可以通过`decoderContext`获取

或者也可以直接通过类名直接调用decoder
```
decoder:
  type: test.MyDecoder
  my_param1: abc
  my_param2: 123
```

filter的自定义同理