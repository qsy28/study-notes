## 常用函数

### 关系运算

- 不等于	`<>`

- RLIKE 

  ```sql
  A RLIKE B
  -- 如果字符串A或者字符串B为NULL，则返回NULL；如果字符串A符合JAVA正则表达式B的正则语法，则为TRUE；否则为FALSE。
  ```

- REGEXP

  ```sql
  A REGEXP B
  -- 功能与RLIKE相同
  ```

### 数学运算

- 取余 `%`

- 位操作

  ```sql
  -- 位与
  A & B --A B数值类型，返回A和B按位进行与操作的结果。结果的数值类型等于A的类型和B的类型的最小父类型（详见数据类型的继承关系）。
  
  -- 位或
  A | B
   
  -- 位异或
  A ^ B
  
  -- 位取反（符号位也取反）
  ~ A
  ```

### 逻辑运算（AND OR NOT）

### 数值计算

- 四舍五入

  ```SQL
  round(double a)
  round(double a, int d) --d保留小数点后位数
  ```

- 向下取整数

### 字符串函数

- 字符串格式化函数:printf

  ```SQL
printf(String format, Obj... args)
  --返回值: string
--说明:将指定对象用 format 格式进行格式化.
  ```
  
- 字符串分割
  
  ```SQL
  split(string,'\\[分隔符]')--当分隔符为 , 时，不需要转义符，其他特殊分隔符需要加
  --返回值为数组
  /*
  当split包含在 "" 之中时 需要加4个\
  如 hive -e "....  split('192.168.0.1','\\\\.') ... "  不然得到的值是null
  */
  ```
- 字符串连接

  ```SQL
  CONCAT(var1,var2,...)
  CONCAT_WS(separator,var1,var2,...)--将每个参数值和第一个参数separator指定的分隔符依次连接到一起组成新的字符串
  ```

  

### 对数组的操作

- 判断数组中是否包含某个元素
  ```SQL
  array_contains(collect_set(tag_id), 599)
                       --数组          --元素
  ```

### 聚合函数

```sql
count(1) --等同于count(*) 用时更少，都不会忽略Null值
count(列名) --会忽略Null值
```



## 联结

- join关键字默认为内连接，返回两张表中都有的信息；

- left join以前面的表作为主表和其他表进行关联，返回的记录数和主表的记录数相同，关联不上的字段用NULL;

- left [outer] join对其无影响；

- right join与left相反，以后面的表为主表，和前面的表做关联，返回的记录数和主表一致，关联不上的字段为NULL;

- full join为全关联，返回两个表记录的并集，关联不上的字段为NULL;

  注意：full join时，hive不会用mapjoin来优化；

- left semi join以关键字前面的表为主表，两个表对on的条件字段做交集，返回前面表的记录
- cross join（笛卡尔积关联）返回两个表的笛卡尔积结果，不需要指定关联键；