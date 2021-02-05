# chinasoft-framework

基于welink的服务端项目项目骨架。对welink开放平台接口进行了封装，对数据库访问进行了封装。如有新的需求，请联系我。

------
[Welink开放平台服务端](https://open.welink.huaweicloud.com/docs/) 接口调用封装
# 使用方法
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
Rest接口统一返回json数据，格式定义在ResultVO类中，
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
数据库封装了分页查询、动态表明等功能。
封装了mybatis-plus的自动生成数据库mapper、实体类entity、service等代码的功能。
数据库代码自动生成方法参考：[mybatis-plus代码自动生成](https://mp.baomidou.com/guide/generator.html)

数据库生成代码执行方法在CodeGenerator类中