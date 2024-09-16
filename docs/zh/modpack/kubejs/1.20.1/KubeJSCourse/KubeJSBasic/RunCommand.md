# 执行游戏内指令
在kubejs当中提供了一个服务端方法，可以直接调用到mc里的指令

可以使用全局静态类`Utils`里的`server`属性，然后调用`runCommand`这个方法

可以像下面这样写
```js
Utils.server.runCommand('kill @e[type="item"]');
```
这样写每次执行到这里就会在游戏内执行该指令，清除所有的物品

但是这样写会在游戏聊天框显示服务器执行了xxx，有时候我们不想让玩家看到这些信息，我们可以这样写

```js
Utils.server.runCommandSilent('kill @e[type="item"]');
```

这样在执行时就不会看到这些信息了

这里简单说明一下，如果可以从事件中获取到服务端就从获取的服务端里去运行`runCommandSilent`或者`runCommand`，减少调用`Utils`下的`server`，防止出现一些不必要的不好排查的错误