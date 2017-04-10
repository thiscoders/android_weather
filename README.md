## Android天气客户端
### 一.数据来源:和风天气

    1.和风天气的个人认证key请登录< http://www.heweather.com/ > 网站
### 二.相关包的含义
    |包名|介绍|类型|
    |----|----|----|
    |activity|存放activity|文件夹|
    |utils|存放工具类|文件夹|
    |domain|一些必须的javabean|文件夹|
    |cvs|自定义控件类|文件夹|



### 三.数据表的创建代码
1.创建天气状态的sql语句
``` sql
    create table wstatus(
        wsid varchar(20)  primary key, --代码
        chin varchar(20),  --中文
        engl varchar(20),  --英文
        icon varchar(100)  --图标地址
    );
```

2.创建城市列表的sql语句
``` sql
    create table china_cities(``
        cityId varchar(30) primary key,   --城市/地区编码
        cityEn varchar(30),  --英文
        cityZh varchar(30),  --中文
        countryCode varchar(30),  --国家代码
        countryEn varchar(30),  --国家英文
        countryZh varchar(30),  --国家中文
        provinceEn varchar(30),  --省英文
        provinceZh varchar(30),  --省中文
        leaderEn varchar(30),  --所属上级市英文
        leaderZh varchar(30),  --所属上级市中文
        lat varchar(30),  --纬度
        lon varchar(30)  --经度
    );
```
## 3. xiaoshayu修改了readme.md文件
