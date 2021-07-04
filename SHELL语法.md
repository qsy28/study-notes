## SHELL语法

Shell 既是一种**命令语言**，又是一种**程序设计语言**。

Shell 是指一种**应用程序**，这个应用程序提供了一个界面，用户通过这个界面访问操作系统内核的服务。

**Bash** 在日常工作中被广泛使用。同时，Bash 也是大多数Linux 系统默认的 Shell。

### Hello World

```SHELL
# 在文本编辑器里编写代码
#! /bin/bash
echo "Hello world"
```

```SHELL
# 命令行中运行sh脚本
chmod +x ./test.sh  #使脚本具有执行权限
./test.sh  #执行脚本
```

### shell 变量

**变量名和等号之间不能有空格**

```shell
# 定义
your_name1="qsy"
# 使用
echo $your_name1
echo ${your_name1} # {}识别变量边界，建议使用
# 只读变量
readonly your_name1
# 删除变量 unset 命令不能删除只读变量
unset your_name2
# 输入变量
read 
```

- **字符串**

  单引号，双引号

  - 双引号里可以有变量
  - 双引号里可以出现转义字符`\`

- **shell数组**

  数组名=(value1 value2 ...)  【用空格隔开】

### 传递参数

  我们可以在执行 Shell 脚本时，向脚本传递参数，脚本内获取参数的格式为：**$n**。**n** 代表一个数字，1 为执行脚本的第一个参数，2 为执行脚本的第二个参数，以此类推……

### shell运算符

- **算术运算符**

  原生bash不支持简单的数学运算，但是可以通过其他命令来实现，例如 awk 和 expr

  ```SHELL
  #!/bin/bash
  
  val=`expr 2 + 2` #反引号 #表达式和运算符之间要有空格
  echo "两数之和为 : $val"
  
  :<<'
  + - * / % 用expr  乘号(*)前边必须加反斜杠(\)才能实现乘法运算；
  赋值运算与变量定义一样
  == != 要放在方括号里，并且要有空格  [ $a == $b ]
  '
  ```

- **关系运算符**

  ```SHELL
  # 要放在方括号里，并且要有空格[ $a -eq $b ] 
  -eq  #检测两个数是否相等
  -ne  #检测两个数是否不相等
  -gt  #检测左边的数是否大于右边的
  -lt  #检测左边的数是否小于右边的
  -ge  #检测左边的数是否大于等于右边的
  -le  #检测左边的数是否小于等于右边的
  ```

- **布尔运算符**

  ```SHELL
  ！  #非
  -o  #或
  -a  #与
  ```

- **逻辑运算符**

  ```SHELL
  # 放在两个方括号里 [[ $a -lt 100 && $b -gt 100 ]]
  &&  #AND
  ||  #OR
  ```

- **字符串运算符**

  ```SHELL
  =
  !=
  -z  #检测字符串长度是否为0，为0返回 true
  -n  #检测字符串长度是否不为0，不为0返回 true。
  $   #检测字符串是否为空，不为空返回 true。
  ```

- **文件测试运算符**

### echo

默认添加换行，不想换行的话：`\C`

- 显示结果定向至文件

  ```SHELL
  echo "qin is a shuaibi" > myfile
  ```

### printf——格式化输出

```SHELL
printf  format-string  [arguments...]
# format-string: 为格式控制字符串
# arguments: 为参数列表
# %s %c %d %f都是格式替代符
# %-10s 指一个宽度为10个字符（-表示左对齐，没有则表示右对齐）

printf "%-10s %-8s %-4s\n" 姓名 性别 体重kg  
printf "%-10s %-8s %-4.2f\n" 郭靖 男 66.1234 
printf "%-10s %-8s %-4.2f\n" 杨过 男 48.6543 
printf "%-10s %-8s %-4.2f\n" 郭芙 女 47.9876 
# 单双引号、没有引号都可以输出
```

### test

test 命令用于检查某个条件是否成立（跟[]功能有点相似）

### 流程控制

- **if条件判断**

  ```SHELL
  # if else
  if condition
  then
      command1 
      command2
      ...
      commandN
  else
      command
  fi
  # if else-if else
  if condition1
  then
      command1
  elif condition2 
  then 
      command2
  else
      commandN
  fi
  ```

- **for循环**

  ```SHELL
  for var in item1 item2 ... itemN
  do
      command1
      command2
      ...
      commandN
  done
  
  #写成一行
  for var in item1 item2 ... itemN; do command1; command2… done;
  ```

- **while语句**

  ```SHELL
  while condition #condition 一般为条件表达式，如果返回值为 false，跳出循环。
  do
      command
  done
  
  #无线循环
  while :
  do
      command
  done
  ######
  while true
  do
      command
  done
  #######
  for (( ; ; ))
  ```
  
- **until循环**

  ```shell
  until condition  #condition 一般为条件表达式，如果返回值为 false，则继续执行循环体内的语句，否则跳出循环。
  do
      command
  done
  ```

- **case**

  ```SHELL
  case 值 in
  模式1)
      command1
      command2
      ...
      commandN
      ;;
  模式2）
      command1
      command2
      ...
      commandN
      ;;
  esac
  ```

- 跳出循环：break和continue

### 函数定义

```SHELL
[ function ] funname [()]

{

    action;

    [return int;]

}
funname
echo "$?"
# 函数返回值在调用该函数后通过 $? 来获得。
# 在Shell中，调用函数时可以向其传递参数。在函数体内部，通过 $n 的形式来获取参数的值，例如，$1表示第一个参数，$2表示第二个参数...$10 不能获取第十个参数，获取第十个参数需要${10}。当n>=10时，需要使用${n}来获取参数。
funname a b c d 
```

### 文件包含（引用外部脚本）

```SHELL
. 文件路径   # 注意点号(.)和文件名中间有一空格

或

source filename
```

