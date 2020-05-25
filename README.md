## 清风逐月论坛

## 资料
[Spring 文档](https://spring.io/guides)

[Spring web](https://spring.io/guides/gs/serving-web-content/)

[Github OAuth](https://developer.github.com/apps/building-github-apps/creating-a-github-app/)

[Mysql 菜鸟教程](https://www.runoob.com/mysql/mysql-tutorial.html)

[Vue](https://cn.vuejs.org/v2/guide/)

[Markdown 插件](https://pandao.github.io/editor.md/)
## 工具
[Git](https://git-scm.com/downloads)

[Maven](https://mvnrepository.com/)

[Lombok](https://projectlombok.org/)

## 脚本
```sql
create table comment
(
    id            bigint auto_increment
        primary key,
    parent_id     bigint           not null comment '父类ID',
    type          int              not null comment '父类类型',
    commentator   bigint           not null comment '评论人ID',
    gmt_create    bigint           not null comment '创建时间',
    gmt_modified  bigint           not null comment '更新时间',
    content       varchar(1024)    null,
    comment_count bigint default 0 null comment '回复数'
);
```
```sql
create table question
(
    id            bigint auto_increment
        primary key,
    title         varchar(50)   null comment '标题',
    description   text          null comment '内容',
    gmt_create    bigint        null comment '创建时间',
    gmt_modified  bigint        null comment '修改时间',
    creator       bigint        null comment '创建人id',
    comment_count int default 0 null comment '评论数',
    view_count    int default 0 null comment '浏览数',
    like_count    int default 0 null comment '点赞数',
    tag           varchar(256)  null comment '标签',
    shield        int default 0 null comment '是否屏蔽：0不屏蔽，1屏蔽',
    examine       int default 0 null comment '审核：0未审核，1通过，2不通过'
);
```
```sql
create table tag
(
    id           bigint auto_increment
        primary key,
    name         varchar(50)  null comment '标签名',
    description  varchar(256) null comment '描述',
    gmt_create   bigint       null comment '创建时间',
    gmt_modified bigint       null comment '修改时间'
);
```
```sql
create table user
(
    id           bigint auto_increment
        primary key,
    name         varchar(50)       null comment '用户名',
    password     varchar(50)       null comment '密码',
    token        varchar(50)       null,
    gmt_create   bigint            null comment '创建时间',
    gmt_modified bigint            null comment '修改时间',
    avatar_url   varchar(100)      null comment '头像',
    bio          varchar(256)      null comment '个人简介',
    type         int(20) default 0 null comment '用户类型，0普通用户，1管理员'
);
```

```text
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```

