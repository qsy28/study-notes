## 1. LINUX常用命令

LINUX 命令大全：https://www.runoob.com/linux/linux-command-manual.html

####  终端命令帮助信息

```shell
man command
```

#### 清屏

```SHELL
clear
```

#### 切换用户（实习：root用户禁止使用hive命令）

```shell
su - cupid_dp
```

#### 查看当前目录下文件夹

```shell
ls
ls -l #文件夹及文件的详细信息
```

#### 打印当前工作目录

```shell
pwd
```

#### 转移所在的目录

```shell
cd . #当前目录
cd .. #上一级目录
cd 目录路径 #到特定的文件夹下
```

#### 建立一个自己的文件夹（便于上传sql及输出查询结果）

```shell
mkdir home/cupid_dp/qinsiyao_sx
```

#### 给文件添加执行权限

```SHELL
chmod +x test.sh
```

#### 统计文件行数

```SHELL
wc -l 文件路径
```

#### cat -- 打印文件



#### 输出重定向

```SHELL
command > file # 将输出重定向到 file。
command >> file # 将输出以追加的方式重定向到 file。
```

#### hive的三种运行方式

- 进行hive终端

  ```shell
  $ hive
  hive > 查询语句
  # 退出
  hive > quit;
  ```

- **运行sql文件**

  ```shell
  hive -f sql文件路径 > 查询结果输出路径.txt
  # 传递参数
  hive -hiveconf var='xxx' -f sql文件路径 > 查询结果输出路径.txt
  ```

- hive -e

  ```shell
  hive -e '查询语句'  > 查询结果输出路径.txt
  ```


#### kill掉自己的job（不要kill错了）

```shell
hadoop job -list
hadoop job -kill job_number
```

## 2. LINUX VIM

文本编辑器

## 3. LINUX awk命令

> AWK 是一种处理文本文件的语言，是一个强大的文本分析工具。

```SHELL
awk [选项参数] 'script' var=value file(s)
或
awk [选项参数] -f scriptfile var=value file(s)
```

```SHELL
# 每行按空格或TAB分割，输出文本中的1、4项
awk '{print $1,$4}' log.txt
awk '$1>2' log.txt
     脚本   文本文件
# 指定分隔符：先使用空格分割，然后对分割结果再使用","分割
awk -F '[ ,]' '{print $1,$2}' log.txt
    参数  分隔符
# 设置变量
awk -va=1 -vb=s '{print $1,$1+a,$1b}' log.txt
    v参数 a,b为变量分别赋值1,'s'
# 运行awk脚本
awk -f {awk脚本(后缀为.awk)} {文件名}

关于 awk 脚本，我们需要注意两个关键词 BEGIN 和 END。
BEGIN{ 这里面放的是执行前的语句 }
{这里面放的是处理每一行时要执行的语句}
END {这里面放的是处理完所有的行后要执行的语句 }
```

- 内置变量

  > 变量：分为内置变量和自定义变量;输入分隔符FS和输出分隔符OFS都属于内置变量。
  >
  > 内置变量：就是awk预定义好的、内置在awk内部的变量，而自定义变量就是用户定义的变量。
  
  ```SHELL
  # 只记录遇到的内置变量，具体可查官方手册
  $n	当前记录的第n个字段，字段间由FS分隔
  $0	完整的输入记录
  NF	一条记录的字段的数目
  FS	字段分隔符(默认是任何空格)
  NR	已经读出的记录数，就是行号，从1开始
  ```

