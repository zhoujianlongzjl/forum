## 清风逐月论坛

## 资料
[Spring 文档](https://spring.io/guides)

[Spring web](https://spring.io/guides/gs/serving-web-content/)

[Github OAuth](https://developer.github.com/apps/building-github-apps/creating-a-github-app/)

[Mysql 菜鸟教程](https://www.runoob.com/mysql/mysql-tutorial.html)

[Bootstrap](https://v3.bootcss.com/)

[Markdown 插件](https://pandao.github.io/editor.md/)
## 工具
[Git](https://git-scm.com/downloads)

[Maven](https://mvnrepository.com/)

[Lombok](https://projectlombok.org/)

## 脚本
```sql
create table user
(
    id           bigint auto_increment
        primary key,
    name         varchar(50)  null comment '用户名',
    password     varchar(50)  null comment '密码',
    token        varchar(50)  null,
    gmt_create   bigint       null comment '创建时间',
    gmt_modified bigint       null comment '修改时间',
    avatar_url   varchar(100) null comment '头像',
    bio          varchar(256) null comment '个人简介'
);
```
```text
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```

