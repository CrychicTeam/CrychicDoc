# global全局变量
在KubeJS中，提供了一个可以在`server`、`startup`、`client`三端调用的一个全局变量->`global`

## 如何快速理解
我们先在`server`的脚本文件夹下创建一个`GlobalTest.js`文件，然后在里面写上以下内容
```js
global.testLog = (message) =>{
    console.log(message)
}
global.testNumber = 10;
global.testMessage = "hello,meng"
global.testList = [
    1,2,3,4
]
global.testObj = {
    h : 1,
    b : 2
}
```

写完之后我们进入到`client`脚本文件下面写上以下内容
```js
global.testLog(global.testMessage)
console.log(global.testMessage)
```

再在`startup`里编写以下脚本
```js
global.testList.forEach(value=>{
    global.testLog(value)
});
console.log(global.testObj.h)
console.log(global.testObj.b)
```

接下来我们按照这个顺序在游戏内输入指令
1. /kjs reload server_scripts
因为我们的全局变量是写在`server`里的，所以有限加载这个文件夹，让全局变量加载进kubejs里
2. /kjs reload client_scripts
我们输入完这个指令之后，进入文件路径为`/logs/kubejs/server.log`和`/logs/kubejs/client.log`里，可以看到

在`server.log`里有一行输出文本为"hello,meng" 这里就是我们调用的`global.testLog(global.testMessage)`起了作用

在`client.log`里也有同样的一行输出文本为"hello,meng" 这个地方是调用的`console.log(global.testMessage)`起了作用
3. /kjs reload startup_scripts
输入完成指令之后我们进入到`/logs/kubejs/server.log`和`/logs/kubejs/startup.log`里

在`server.log`里输出了数组里的数字`1``2``3``4` 说明是可以成功访问的

在`startup.log`里也有两个数字分别是`1`和`2`，这也是因为我们读取到了全局变量里对象的值

## 小结
在正常情况下，我们无法做到这种跨文件的方法变量的调用

但是使用了global之后，就可以在全局进行调用

## 用处
在项目[祭坛合成](../KubeJSProjects/Meng/AltarComposition.md)中就有使用到全局变量