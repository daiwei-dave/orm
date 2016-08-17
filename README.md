本次更新较大，约有四次改动，并且已经将心中想要的功能基本完成了，暂定为版本1.1。考虑到内容越来越多，决定做一个阶段性总结，方便自己实时查阅。

* annotation 注解包
 * Operation：作于dao层方法上，方便后续处理sql语句与result类型
* config 配置包
 * DataSouce：数据库信息
 * Environment：存有配置信息中的dao方法与resultMap（同MyBatis）以及DataSource
 * XMLConfigBuilder：读取与解析配置信息，并返回处理后的Environment
* io 读写流包
 * ClassLoaderWrapper：通过多种ClassLoader尝试读取资源与类，提高改进空间
* mapper 映射包
 * MappedMethod
 * MappedProxy
 * MappedResult
 * SqlCommandType