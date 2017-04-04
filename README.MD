# 第四节 SpringBoot采用Mybatis连接数据库
## 1、在Intellij新建项目，并在pom中添加druid依赖，jdbc,druid,mysql.mybatis,...。

```xml
<dependency>
  <groupId>com.alibaba</groupId>
  <artifactId>druid</artifactId>
  <version>1.0.14</version>
</dependency>
.....
```
## 2、数据源
### 方案一 使用Spring Boot默认配置
在application.properties中配置数据源信息<br/>

```
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/springboot_db?useUnicode = true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
spring.datasource.username=root
spring.datasource.password=root

```
### 方案二

## 3、数据库脚本初始化
创建数据库

```sql
CREATE DATABASE /*!32312 IF NOT EXISTS*/`springboot_db` /*!40100 DEFAULT CHARACTER SET utf8 */;
 
USE `springboot_db`;
 
DROP TABLE IF EXISTS `t_author`;
 
CREATE TABLE `t_author` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `real_name` varchar(32) NOT NULL COMMENT '用户名称',
  `nick_name` varchar(32) NOT NULL COMMENT '用户匿名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

```

## 4、Mybatis整合
### 方案一、注解
实体对象

```java

public class Author {
    private Long id;

    @JSONField(name="real_name")
    private String realName;

    @JSONField(name = "nick_name")
    private String nickName;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
```

dao相关

```java
@Repository
@Mapper
public interface AuthorMapper {

    @Insert("insert into t_author(real_name, nick_name) values(#{real_name},#{nick_name})")
    int add(@Param("real_name") String realName, @Param("nick_name") String nickName);

    @Update("update t_author set real_name = #{real_name},nick_name = #{nick_name} where id = #{id}")
    int update(@Param("real_name") String realName, @Param("nick_name") String nickName,@Param("id") Long id);

    @Delete("delete from t_author where id = #{id}")
    int delete(Long id);

    @Select("select id,real_name as realName, nick_name as nickName from t_author where id = #{id}")
    Author findAuthor(@Param("id") Long id);

    @Select("select id,real_name as realName,nick_name as nickName from t_author")
    List<Author> findAuthorList();

}
```
service相关

```java
@Service
public class AuthorService {

    @Autowired
    private AuthorMapper authorMapper;

    public int add(String realName,String nickName){
        return this.authorMapper.add(realName,nickName);
    }

    public int update(String realName,String nickName,Long id){
        return this.authorMapper.update(realName,nickName,id);
    }

    public int delete(Long id){
        return this.authorMapper.delete(id);
    }

    public Author findAuthor(Long id){
        return this.authorMapper.findAuthor(id);
    }

    public List<Author> findAuthorList(){
        return this.authorMapper.findAuthorList();
    }
}

```

controller相关

```java
@RestController
@RequestMapping(value="/data/mybatis/author")
@MapperScan("com.example")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    /**
     * 查询用户列表
     */
    @RequestMapping(method = RequestMethod.GET)
    public Map<String,Object> getAuthoList(HttpServletRequest request){
        List<Author> authorList = this.authorService.findAuthorList();
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("total",authorList.size());
        param.put("rows",authorList);
        return param;
    }

    /**
     * 查询用户信息
     */
    @RequestMapping(value = "/{userId:\\d+}",method = RequestMethod.GET)
    public Author getAuthor(@PathVariable Long userId,HttpServletRequest request){
        Author author = this.authorService.findAuthor(userId);
        if (author == null){
            throw new RuntimeException("查询错误");
        }

        return author;
    }

    /**
     * 新增方法
     */
    @RequestMapping(method = RequestMethod.POST)
    public void add(@RequestBody JSONObject jsonObject){
        String user_id = jsonObject.getString("user_id");
        String real_name = jsonObject.getString("real_name");
        String nick_name = jsonObject.getString("nick_name");

        try {
            this.authorService.add(real_name,nick_name);
        } catch (Exception e) {
            throw new RuntimeException("新增错误");
        }

    }


    /**
     * 更新方法
     */
    @RequestMapping(value = "/{userId:\\d+}",method = RequestMethod.PUT)
    public void update(@PathVariable Long userId, @RequestBody JSONObject jsonObject){
        Author author = this.authorService.findAuthor(userId);
        String realName = jsonObject.getString("real_name");
        String nickName = jsonObject.getString("nick_name");

        try {
            this.authorService.update(realName,nickName,author.getId());
        } catch (Exception e) {
            throw new RuntimeException("更新错误");
        }
    }

    /**
     * 删除方法
     */
    @RequestMapping(value = "/{userId:\\d+}",method = RequestMethod.DELETE)
    public void delete(@PathVariable Long userId){
        try {
            this.authorService.delete(userId);
        } catch (Exception e) {
            throw new RuntimeException("删除错误");
        }
    }

}

```
