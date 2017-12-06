# LogRecordHelper


# 功能描述：

将Logcat日志存储到手机文件中

-------------------------------------------------------------------

## Glide:
```
compile 'acffo.xqx.xlogrecordlib:xLogRecordHelper:1.0.0'
```


## Maven:
```
<dependency>
  <groupId>acffo.xqx.xlogrecordlib</groupId>
  <artifactId>xLogRecordHelper</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```
<br><br>
# 使用方法：

### // 声明
 
 XLogRecordHelper xLogRecordHelper;
 
  ---------

### // 参数一：上下文
 
### // 参数二：文件夹名，若为多层，则为"xdirx/xxdirx"
 
### // 参数三：文件名，要加文件格式 ，比如 .txt
 
 xLogRecordHelper = XLogRecordHelper.getInstance(this, "xdirx", "aaaa.txt"); 
 
 ---------
 
### // 设置过滤TAG，只显示该TAG的日志
 
 xLogRecordHelper.setFilterStr("xqxinfo");  
 
  ---------

 
### // 开始将日志写入文件
 
 xLogRecordHelper.start();
 
  ---------

 
## // 结束将日志写入文件
 
 xLogRecordHelper.stop();
