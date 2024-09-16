# 定时任务
在很多时候我们希望一个事件在一秒或者n秒后执行，而不是立马执行

这个时候我们就可以使用到定时任务

定时任务是在server类下面的一个方法`scheduleInTicks`

他的调用也非常简单，如果你获取到了一个`server`对象，就可以像下面这样写

```js
Utils.server.scheduleInTicks(20 * 5, () => {
    Utils.server.tell("hello -- 定时任务触发了")
})
```
这个代码会在五秒，让服务端发送一段文本

这里我直接使用的`Utils`下面的`server`，如果你能够在事件中获取到`server`就在事件里获取，确实获取不到的也可以使用上面的方法获取到`server`