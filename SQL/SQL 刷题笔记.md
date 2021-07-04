# SQL 刷题笔记

### 0311

#### limit
查询第二高的工资
```sql
# n是偏移量，也就是说从第几个开始选取（0表示第一条记录）
# m是返回记录的条数
limit n,m 
limit 后不能接算术运算符
limit 必须在查询语句的最后
```
####  排序函数
对工资进行排序
```sql
DENSE_RANK() #如果使用 DENSE_RANK() 进行排名会得到：1，1，2，3，4。
RANK() #如果使用 RANK() 进行排名会得到：1，1，3，4，5。
ROW_NUMBER() #如果使用 ROW_NUMBER() 进行排名会得到：1，2，3，4，5。
```
####  lead/lag() over()
```sql
#两个函数可以在一次查询中取出同一字段的前N行的数据(lag)和后N行的数据(lead)作为独立的列
#返回字段（expression）前offset的值
LAG(<expression>[,offset[, default_value]]) OVER (PARTITION BY (expr) ORDER BY (expr) [ASC|DESC]) 
#返回字段（expression）后offset的值
LEAD(<expression>[,offset[, default_value]]) OVER (PARTITION BY (expr) ORDER BY (expr) [ASC|DESC])

lead(字段, 偏移量, 没有符合条件的默认值) over(partition by ... order by ...)
```

### 0312
#### 日期函数
```sql
"datediff(time1,time2),time1以及time2是可以被解析为时间戳的日期字段，符合常用的时间格式即可，类似于yyyy-MM-dd HH:mm:ss 。并且该内置函数所计算得到的日期差距的最小颗粒度为天"

datediff(date1,date2) #返回date1-date2 计算两个日期天数差距

"unix_timestamp(time, ‘yyyy-MM-dd HH:mm’)该函数将时间字段time转换为精确到秒的时间戳，两个这样的时间戳相减即可得到日期之间的秒数差距，然后除以60即可得到分钟差距。"
unix_timestamp(time1, ‘yyyy-MM-dd HH:mm’) - unix_timestamp(time2, ‘yyyy-MM-dd HH:mm’) #计算两个日期秒数差距
```

#### 异或操作

相邻的学生交换座位

```sql
# 我的思路用if进行判断，对id进行奇偶变换
select
if(id%2=1,if(id=(select max(id) from seat),id,id+1),id-1) as id,student
from seat order by id
# 大神思路利用异或操作对id进行奇偶变换
select rank() over(order by (id-1)^1) as id,student from seat
```

#### 增删改操作

```sql
# 删除

# 更新
UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值 #set后的表达式可以使用函数
```


### 0313
#### 取中位数

sum() over() 倒序、正序计算累计频率，两者累计频率>=总数/2



#### all() any()

any 是任意一个 
all 是所有
any表示有任何一个满足就返回true，all表示全部都满足才返回true 

select * from student where 班级=’01’ and age > all (select age from student where 班级=’02’); 
就是说，查询出01班中，年龄大于 02班所有人的同学 
相当于 
select * from student where 班级=’01’ and age > (select max(age) from student where 班级=’02’);
而 
select * from student where 班级=’01’ and age > any (select age from student where 班级=’02’); 
就是说，查询出01班中，年龄大于 02班任意一个 的 同学 
相当于 
select * from student where 班级=’01’ and age > (select min(age) from student where 班级=’02’);



#### ifnull()，null值与其他任何值进行的计算都是null值

```sql
ifnull(x1, x2)
--如果x1是null值，则x2
-- null值与其他任何值进行的计算都是null值
```




### 0314
#### subdate(date,n)
针对连续问题，利用原始某列与生成的rank相减，若连续则减数相同

#### 笛卡尔积



#### where子句多变量时加括号

```sql
select product_id, year first_year, quantity, price
from sales
where (product_id, year) in
(select product_id, min(year)
from sales 
group by product_id)
```



### 0326

#### 查找雇员最多的项目

```sql
# 窗口函数与group by 和聚合函数的使用
select project_id
from
(select project_id, rank() over(order by count(employee_id) desc) as ranking
from project
group by project_id) a
where a.ranking = 1
```



#### sum()函数里可以添加逻辑表达式，计算某条件成立时的sum

```sql
# 买了s8，但没有买iphone的人
select s.buyer_id
from product p, sales s
where p.product_id = s.product_id
group by s.buyer_id
having sum(p.product_name='S8') > 0 and sum(p.product_name='iphone') < 1
```



### 0424

#### 截取字符串 substr(str, start, end)

```sql
substr(order_date, 1, 7) as month
```



