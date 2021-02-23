# chinasoft-framework

基于welink的服务端项目项目骨架。对welink开放平台接口进行了封装，对数据库访问,redis缓存进行了封装。如有新的需求，请联系我。
[后端开发须知](http://192.168.11.111:8090/x/jgAF)
------
### [Welink开放平台服务端](https://open.welink.huaweicloud.com/docs/) 接口调用封装
在springboot项目的application.properties中添加welink We码或者轻应用的clientId和clientSecretKey。

```properties
# clientId
welink.openapi.client-id=20201231090249131354656
# clientSecretKey
welink.openapi.client-secret=a19af76f-23fdf-43fe-fd32-fdf54gf54

```
在代码中注入自动生成的AuthRes对象和OpenAPI对象。
```java
@Autowired
private OpenAPI openAPI;
@Autowired
private AuthRes authRes;

void test(){
    //获取租户信息
    TenantInfoRes   tenantInfoRes = openAPI.getTenantInfo(authRes.getAccess_token());
}
```
更多welink开放平台接口封装在openAPI类中。

-----
### Rest接口统一返回json数据，格式定义在ResultVO类中，
```java
public class ResultVO {
    public final static int SUCCESS_CODE = 0;//返回码0，意为操作成功
    public final static int FAIL_CODE = 1;//返回码1，意为操作失败，msg字段保存具体错误信息
    public final static int AUTH_FAIL_OR_EXPIRED = 2; // 返回码2，认证失败或者认证已过期

    private int code; //返回码
    private String msg; //返回的操作信息，主要是错误信息
    private Object data;// 返回给前端的数据对象
}
```
----
### 数据库封装了crud代码自动生成、分页查询、动态表名等功能。
封装了mybatis-plus的自动生成数据库mapper、实体类entity、service等代码的功能。
数据库代码自动生成方法参考：[mybatis-plus代码自动生成](https://mp.baomidou.com/guide/generator.html)

数据库生成代码执行方法在CodeGenerator类中
### Redis缓存封装
Redis缓存用户信息、租户信息的操作封装在RedisService类中，如需缓存其他数据，添加相应方法即可。
### 接口调用次数限制
接口调用次数限制注解@RequestLimit ，防止接口被频繁调用。
```java
@GetMapping("welink")
@RequestLimit(maxCount = 10, timeout = 60)
public TenantInfoRes welink(){
     TenantInfoRes tenantInfoRes = openAPI.getTenantInfo(authRes.getAccess_token());
     return tenantInfoRes;
}
```
上边的接口调用限制为60秒钟最多调用10次。
### 统一异常返回封装
所有类型的异常统一处理，异常处理代码封装在GlobalExceptionHandler类中，项目中需要用到的异常，直接抛出throw即可，会封装成统一的ResultVO返回前端。
自定义的异常需要继承CommonException类，异常编号（code）需要在ResultCode类中定义。