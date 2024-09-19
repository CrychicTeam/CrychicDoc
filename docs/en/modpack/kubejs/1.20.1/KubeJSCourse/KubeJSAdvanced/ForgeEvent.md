# ForgeEvent的使用
> 孤梦注：可千万不要玩forge事件啊，玩了你就要长脑子了！！！

`ForgeEvent`是写在**startup**里的，这个一定得注意！

ForgeEvent不仅仅只能调用到forge提供的事件，如果其他模组在源代码（指的Java代码）里直接继承了`Event`类，也是可以被ForgeEvent捕捉下来的，类似的示例：[机械动力修改流体管道流体和流体产生块](../KubejsProjects/Meng/SmallProject/CreatePipeCollision.md)

ForgeEvent是可以搭配着[loadClass](./JavaLoadClass.md)一起使用的

## forge提供的事件
forge本身提供了非常多的事件可以使我们捕捉到游戏内的事情，但是我们一个个去翻又太过麻烦，这里孤梦就推荐使用[crt文档](https://docs.blamejared.com/1.20.1/en)去找对应的事件！

[crt文档](https://docs.blamejared.com/1.20.1/en)的forge栏里的event目录下是非常好的ForgeEvent文档

我们可以现在crt文档里找到对应的事件，然后再去[forge的GitHub](https://github.com/MinecraftForge/MinecraftForge/tree/1.20.1/src/main/java/net/minecraftforge/event)上去找到相应事件的类

## 关于使用
```js
ForgeEvents.onEvent("事件类",event=>{
    //关于事件的code
})
```
因为每个事件给出的方法是有可能不一样的，所以这里无法举例，且提供不了详细的函数使用（这里也可以简单参考一下crt文档提供的事件的使用函数）

## 事件的“热加载”
绝大部分的事件都是可以“热加载”的，但是需要使用一点手段

在前面我们学习了[global全局变量](./GlobalVariable.md)，我们可以在事件里嵌入一个[global全局变量](./GlobalVariable.md)，将事件处理引出来，像下面这样写
```js
ForgeEvents.onEvent("事件类",event=>{
    global.eventTest(event);
})

global.eventTest = event =>{
    //事件处理
}
```
这样我们在游戏里只需要输入 `/kjs reload startup_scripts` 就可以进行事件处理的热加载了

## 注意事项
startup是非常脆弱的，稍不注意就会大退然后报错，说你的“犀牛”有问题，

所以建议在测试的过程中使用`try catch`包裹处理一下报错，能够减少闪退的次数

具体像下面这样写（沿用上面代码）

```js
global.eventTest = event =>{
    try{
        //事件处理
    }catch(err){
        console.error(err);
    }
}
```
这样在报错时，可以在`/logs/kubejs/startup.log`里看见因为什么报错，然后去调整代码，一定不要因为错误被catch了就不管不顾了，除非你的这个错误是在意料之中的，并且出现报错后也有对应的处理，不会导致一直重复报错