# SQL基础教程

## 第2章 查询基础

### 2-1 SELECT语句基础

- 基础语法

  ```sql
  SELECT column_name[,...]
  FROM table_name;
  ```

- 查询所有列

  ```sql
  SELECT *
  FROM table_name;
  ```

- 列别名

  ```sql
  SELECT column_name AS new_column_name
  FROM table_name;
  ```

- 常数查询

  ```sql
  SELECT '字符' AS string, 3 AS number, '2020-10-08' AS date, column_name[,...]
  FROM table_name;
  ```

- 删除重复行

  ```sql
  SELECT DISTINCT column_name[,...]
  FROM table_name;
  ```

- WHERE对**行**选择

  ```sql
  SELECT column_name[,...]
  FROM table_name
  WHERE Logical expression;
  ```

- 注释

  ```SQL
  -- 单行注释
  
  /* 多行注释
  多行注释 */
  ```

### 2-2 算术运算符和比较运算符

- 算术运算符

  `+`、`-`、 `*`、 `/`

- 所有包含`NULL`的计算，结果也是`NULL`

- 比较运算符

  `=`、 `<>`、 `>=`、 `<=`、 `>`、 `<` 

- 字符串类型的数据原则上按照字典顺序进行排序，不能与数字的大小顺序混淆。

- 使用比较运算符选择不出来`NULL`值的行，对`NULL`值处理利用谓词`IS NULL`或者`IS NOT NULL`

### 2-3 逻辑运算符

- `NOT`运算符

- `AND` 、`OR` （利用括号改变优先级）

- 逻辑就是对**真值**进行操作的意思。

- SQL特有的**三值逻辑**：真、假、不确定

  

## 第3章 聚合与排序

### 3-1 对表进行聚合查询

- COUNT()

  ```SQL
  COUNT(*) --计算表中数据的行数
  COUNT(column_name) --计算NULL之外的数据的行数
  ```

- SUM()

- AVG()

- MAX()、MIN()

  > 聚合函数除了`COUNT(*)`都不会考虑NULL
  >
  > SUM/AVG只能对数值类型的列使用，而MAX/MIN原则上适用于任何数据类型的列

- 在聚合函数的参数中使用DISTINCT，删除重复数据。

### 3-2 对表进行分组

```sql
SELECT column_name[,...],[聚合函数]
FROM table_name
WHERE Logical expression
GROUP BY column_name[,...];
```

- GROUP BY 子句中指定的列称为**聚合键**或者**分组列**
- 聚合键中包含NULL时，在结果中会以“不确定”行（空行）的形式表现出来。
- 与聚合函数和GROUP BY 子句有关的常见错误：
  - 使用聚合函数/GROUP BY 子句时，SELECT子句只能存在：常数、聚合函数、GROUP BY 子句中指定的列名（聚合键）
  - GROUP BY 子句不能使用SELECT子句中定义的列名。
  - GROUP BY 子句结果显示的结果是无序的。
  - **WHERE 子句中不可以使用聚合函数！**【分组数据的筛选-HAVING子句】
  - **只有SELECT子句、HAVING子句以及ORDER BY 子句中可以使用聚合函数**

### 3-3 为聚合结果指定条件

```sql
SELECT column_name[,...],[聚合函数]
FROM table_name
WHERE Logical expression
GROUP BY column_name[,...]
HAVING Logical expression’;
```

- HAVING 子句中能够使用的三种元素：常数、聚合函数、~~GROUP BY 子句中指定的列名（聚合键）~~（放在WHERE子句中）
- **把汇总后的结果（GROUP BY之后的视图）作为HAVING 子句起始点去理解**
- 聚合键对应的条件不应该书写在HAVING 子句，而应该放在WHERE子句中

### 3-4 对查询结果进行排序

```sql
SELECT column_name[,...],[聚合函数]
FROM table_name
WHERE Logical expression
GROUP BY column_name[,...]
HAVING Logical expression’
ORDER BY 排序基准列[1...];
```

- 降序 `DESC`

- 含有NULL的列作为排序键时，NULL会在结果的开头或末尾显示

- ORDER BY 子句中可以使用列别名

  > 执行顺序：**<font color = 'red'>FROM --> WHERE --> GROUP BY --> HAVING --> SELECT --> ORDER BY </font>**

- ORDER BY 子句可以使用SELECT中未使用的列和聚合函数。



## 第5章 复杂查询

### 5-1 视图

- 表种保存的是**实际数据**；视图中保存的是**SELECT语句**，使用视图时会执行SELECT语句并创建一张临时表。

- 经常使用的SELECT语句应该做成视图。

  > Redash中query创建的就是视图，在该视图上再写filter sql创建图表。【爱奇艺实习】


```sql
CREATE VIEW view_name (column_name,...)
AS
<SELECT 语句>
--------------
DROP VIEW view_name
```

- 避免在视图上创建视图。
- 视图的限制：
  - 不能使用ORDER BY 语句（视图的数据行也是没有顺序的）
  - 对视图的更新会影响原表，因此更新是否成功与原表结构及约束有关（例如汇总得到的视图无法进行更新等）

### 5-2 子查询

- 子查询就是将用来定义的SELECT语句直接用于FROM子句中（“一次性视图”）
- 子查询作为内层查询会首先执行。
- 同理，避免多层嵌套的子查询。

### 5-3 标量子查询

- **标量子查询必须而且只能返回1行1列的结果**（返回单一值的子查询）

- 能够使用常数或者列名的地方，无论是SELECT子句、GROUP BY子句、HAVING子句，还是OEDER BY子句，几乎所有地方都可以使用标量子查询。

  > 使用举例：查找比平均值高的记录

### 5-4 关联子查询【HIVE 不支持关联子查询】

```sql
-- 按照商品种类与平均销售单价进行比较
SELECT product_type, product_name, sale_price
FROM Product AS P1
WHERE sale_price > ( SELECT AVG(sale_price)
                     FROM Product AS P2
                     WHERE P1.product_type = P2.product_type --最重要的条件！！！
                     GROUP BY product_type --可不写
                   );
```



- 在细分的组内进行比较时，需要使用关联子查询。

- 结合条件要写在子查询中（子查询结束只会留下执行结果，到外层查询时P2已经不在了，P2只在子查询中有效）

  > 使用举例：查找比分组平均值高的记录

## 第6章 函数、谓词、CASE表达式

### 6-1 各种函数

算术函数、字符串函数、日期函数、转换函数、聚合函数

- 算术函数

  ```SQL
  -- 算术运算符
  + - * /
  -- 取绝对值
  ABS(数值) 
  -- 取余数
  MOD(被除数，除数) --SQL SERVER 不支持，使用 %
  -- 四舍五入
  ROUND(对象数值，保留小数的位数)
  ```

- 字符串函数

  ```sql
  -- 拼接
  字符串1 || 字符串2 || 字符串3 || ... --在SQL SERVER 和 MYSQL 不支持，使用 + ，SQL SERVER也可以使用CONCAT函数：CONCAT(str1,...)
  -- 字符串长度
  LENGTH(字符串) --在SQL SERVER不支持，使用LEN()
  -- 大小写转换
  LOWER() UPPER()
  -- 字符串替换
  REPLACE(对象字符串，要替换的字符串，用来替换的字符串)
  -- 字符串截取（数据库不同，语法不一样）
  SUBSTRING(对象字符串，截取起始位置，截取的字符数)
  ```

- 日期函数

  日期函数DBMS不同，差距比较大，可查阅各个DBMS的手册

- 转换函数

  ```SQL
  -- 类型转换
  CAST(转换前的值 AS 想要转换的数据类型)
  -- 值的转换
  COALESCE(数据1，数据2，...) --返回可变参数中左侧开始第一个不是NULL的值
  -- NVL(数据1，数据2，...)
  ```

- 聚合函数

  ```sql
  COUNT() SUM() AVG() MAX() MIN()
  ```

- 绝大多数函数对于NULL值都返回NULL（COALESCE转换函数除外）

- **总结一下工作中常用的，也就是HIVE支持的函数**【更具体地可见HIVE的笔记】

  ```SQL
  -- 取绝对值
  ABS(数值) 
  -- 取余数
  MOD(被除数，除数) 
  -- 四舍五入
  ROUND(对象数值，保留小数的位数)
  
  -- 拼接
  CONCAT(str1,...)
  -- 字符串截取
  SUBSTR(对象字符串，截取起始位置，截取的字符数)
  
  -- 日期函数（待补充）
  
  -- 类型转换
  CAST(转换前的值 AS 想要转换的数据类型)
  -- 值的转换
  COALESCE(数据1，数据2，...) --返回可变参数中左侧开始第一个不是NULL的值
  NVL(数据1，数据2，...)
  ```

### 6-2 谓词

- 谓词是返回值为真值（TRUE/FALSE/UNKNOWN）的函数

- LIKE —— 字符串的**部分一致**查询

  - 前方一致
  - 中间一致
  - 后方一致

  ```SQL
  -- 通配符
  %  0字符以上任意字符串
  _  任意一个字符
  ```

- BETWEEN —— **范围查询**

  - BETWEEN 会包含临界值
  
- IS NULL、IS NOT NULL

- IN —— OR 的简单用法

  - IN 、NOT IN 无法选取出NULL值数据
  - 具有其他谓词所没有的用法，可以用子查询（表/视图）作为其参数
  - 其参数不能是NULL值，因此子查询的返回值也不能有NULL值

- EXISTS —— 判断是否存在满足某种条件的记录，其主语是”记录“ 【具体使用见高级教程】

### 6-3 CASE表达式【工作中常用！】

- 简单CASE表达式和**搜索表达式**（几乎只用这个）
- 不要省略ELSE
- END 必须要

```SQL
CASE WHEN <求值表达式> THEN <表达式>
     WHEN <求值表达式> THEN <表达式>
     ELSE <求值表达式>
END
```

- 可以写在任何地方，包括WHERE子句中

- CASE表达式将SELECT语句的结果中的行列进行转换

  举例：计算各分类下的总数，但展示如下

| 分类1总数 | 分类2总数 | 分类3总数 |
| :----------: | :-------- | ----: |
| x | y | z |

```SQL
SELECT SUM(CASE WHEN type = 分类1 THEN number ELSE 0 END) AS 分类1总数,
       SUM(CASE WHEN type = 分类2 THEN number ELSE 0 END) AS 分类2总数,
       SUM(CASE WHEN type = 分类3 THEN number ELSE 0 END) AS 分类3总数
FROM table
```



## 第7章 集合运算（表联结）



## 第8章 SQL高级处理

### 8-1 窗口函数

```SQL
<窗口函数> OVER ( [PARTITION BY <列清单> ] ORDER BY <排序列清单> ) --HIVE里order by不是必须的
```

- 窗口函数兼具分组和排序两种功能，且不会对分组项进行聚合，会保留原来的记录数。
- 通过 PARTITION BY 分组后的记录集合称为”窗口“。
- 能够作为窗口函数使用的函数：

```SQL
-- 能够作为窗口函数的聚合函数（所有聚合函数都可以）
SUM AVG COUNT MAX MIN
-- 专用窗口函数
RANK DENSE_RANK ROW_NUMBER (排序函数)【面试常考】
RANK() --如果存在相同位次的记录，会跳过之后的位次（1，1，1，4）
DENSE_RANK() --即使存在相同位次的记录，也不会跳过后面的位次（1，1，1，2）
ROW_NUMBER() --赋予唯一的连续位次（1，2，3，4）
```

- **专用窗口函数无需参数，因此括号里通常是空的，与能够作为窗口函数的聚合函数不一样！！**
- **原则上窗口函数只能在SELECT子句中使用**
- 聚合函数用于窗口函数的用法：（聚合函数用作窗口函数时，会以当前记录为基准决定汇总范围）

```SQL
-- 累加值
SUM(column_name) OVER (ORDER BY column_name[2]) AS current_sum  --按照column_name[2]排序的累加值
-- 累计平均
AVG(column_name) OVER (ORDER BY column_name[2]) AS current_avg  --按照column_name[2]排序的累计平均

-- 移动平均
窗口函数还能指定更加详细的汇总范围的备选功能选项，该备选功能中的汇总范围成为框架
如何指定框架（汇总范围）：
AVG(column_name) OVER (ORDER BY column_name[2] ROWS 2 PRECEDING) AS moving_avg_1  --按照column_name[2]排序的移动平均，计算当前记录及其前两条记录的平均值
AVG(column_name) OVER (ORDER BY column_name[2] ROWS 2 FOLLOWING) AS moving_avg_2  --计算当前记录及其后两条记录的平均值
AVG(column_name) OVER (ORDER BY column_name[2] ROWS BETWEEN 1 PRECEDING AND 1 FOLLOWING) AS moving_avg_3 --计算当前记录及其上一条记录和下一条记录的平均值
```

- 窗口函数之**框架**
- **OVER子句中的 ORDER BY 只能用来决定窗口函数按照什么顺序进行计算，对结果排序顺序没有影响。**

### 8-2 GROUPING 运算符

```SQL
ROLLUP
CUBE
GROUPING SETS
```

- **ROLLUP**——同时计算合计与小计【感觉工作中不常用】

  ```SQL
  SELECT product_type,regist_time,SUM(sale_price) AS sum_price
  FROM Product
  GROUP BY ROLLUP(product_type,regist_time); --可多个聚合键
  -- GROUP BY product_type,regist_time WITH ROLLUP (MYSQL)
  等价于
  GROUP BY ()
  UNION ALL
  GROUP BY product_type
  UNION ALL
  GROUP BY product_type,regist_time
  ```

  - 对于合计行会默认使用NULL作为键值。

- 给小计、合计行的键值赋值——条件判断+**GROUPING()**函数

   `GROUPING()`函数：该函数在其参数列的值为超级分组记录所产生的NULL时返回1，其他情况返回0——可识别出原始数据中的NULL和超级分组的NULL

  ```SQL
  SELECT 
  if(GROUPING(product_type)=1,'商品种类 合计',product_type) AS product_type
  , if(GROUPING(regist_time)=1,'商品种类 合计',CAST(regist_time AS string)) AS regist_time
  , SUM(sale_price) AS sum_price
  FROM Product
  GROUP BY ROLLUP(product_type,regist_time); 
  ```

- **CUBE**

  ```SQL
  SELECT product_type,regist_time,SUM(sale_price) AS sum_price
  FROM Product
  GROUP BY CUBE(product_type,regist_time); --可多个聚合键
  
  等价于
  GROUP BY ()
  UNION ALL
  GROUP BY product_type
  UNION ALL
  GROUP BY regist_time
  UNION ALL
  GROUP BY product_type,regist_time
  ```
  
- **GROUPING SETS**——用于从ROLLUP、CUBE的结果中取出部分记录

   ```SQL
   SELECT product_type,regist_time,SUM(sale_price) AS sum_price
   FROM Product
   GROUP BY GROUPING SETS (product_type,regist_time); --可多个聚合键
   
   等价于
   GROUP BY product_type
   UNION ALL
   GROUP BY regist_time
   ```

   

### WITH AS 子句

- （像创建视图），with as 创建的子查询部分（subquery factoring），是用来定义一个SQL片断，该SQL片断会被整个SQL语句所用到。

```SQL
with tab1 as
(
    select 查询语句
), tab2 as (select 查询语句), tabs as (select 查询语句)
```



