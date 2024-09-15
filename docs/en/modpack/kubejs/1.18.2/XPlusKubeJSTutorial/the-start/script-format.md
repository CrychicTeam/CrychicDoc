# 1.3 脚本基础

***

## 代码基础格式

如JavaScript一样，所有的脚本后缀名都应为.js。在对应的文件夹下，你可以使用以下方式监听事件：

```
onEvent('事件名称', event => {

  //在此处编写代码 <- 这是一行注释

})
```

例如，以下例子会在玩家加入服务器时将信息打印到控制台。由于`player.logged_in`为Server类型脚本，使用时你应当将其放于`server_scripts`文件夹下：

`kubejs\server_scripts\firstscript.js`

```
onEvent('player.logged_in', event => {//监听玩家登入事件player.logged_in
  let name = event.player.name
  console.info("Hello world!")
  console.info("玩家 " + name + " 加入了游戏")
})
```

## JavaScript语法

你可以通过以下页面快速了解JavaScript的语法。

[JavaScript语法](https://www.w3school.com.cn/js/js\_syntax.asp)

[if 和 if else](https://www.w3school.com.cn/js/js\_if\_else.asp)

[for循环](https://www.w3school.com.cn/js/js\_loop\_for.asp)

[while循环](https://www.w3school.com.cn/js/js\_loop\_while.asp)

[函数](https://www.w3school.com.cn/js/js\_functions.asp)

[箭头函数](https://www.w3school.com.cn/js/js\_arrow\_function.asp)
