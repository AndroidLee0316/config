## 封闭获取配置系统模块（集网络请求及本地存储为一体）

### 使用说明

   ConfigRepository为入口类

### 更新日志

### 2018年8月15日
#### 因为dbflow在多个module出现时，需要编译然后在App统一初始化，耦合性太高，因此删除对dbflow的依赖，替换为原生SQLite;

### 2018年10月20日
#### SQLite涉及多线程并发问题，内部实现修改为通过ContentProvider封装SQLite。
#   c o n f i g  
 