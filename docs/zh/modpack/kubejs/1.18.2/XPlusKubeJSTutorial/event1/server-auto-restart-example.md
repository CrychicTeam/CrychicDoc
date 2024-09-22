---
authors: ['Wudji']
---

# 9 计划重启

***

:::info
**Warning:** This page may be outdated. Please verify the details with the latest updates or documentation.
:::

当服务器启动时，你可以为其计划执行各种内容。你可以使用 callback.reschedule(时间) 来在指定时间内重复这个事件(括号内留空即为使用之前设定的时间)。



```
onEvent('server.load', function (event) {
  event.server.schedule(117 * MINUTE, event.server, function (callback) {
    callback.data.tell('服务器将在3分钟后重启!')
    //callback.reschedule(2 * MINUTE) //两分钟以后再通知一次
  })
  
  event.server.schedule(120 * MINUTE, event.server, function (callback) {
    callback.data.runCommand('/stop')//这里后边不用加重复事件，因为服务端重启时会自动加载本段代码
  })
})
```
