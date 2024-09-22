---
authors: ['Wudji']
---

# 8 聊天事件

***

:::info
**Warning:** This page may be outdated. Please verify the details with the latest updates or documentation.
:::

一个基础的例子：当有人在发送‘kubejs教程’时回复'请访问[https://www.mcbbs.net/thread-1207772-1-1.html'](https://www.mcbbs.net/thread-1207772-1-1.html)

```
onEvent('player.chat', function (event) {
     // 检测如果聊天内容为“kubejs教程” 执行命令, 忽略大小写
  if (event.message.trim().equalsIgnoreCase('kubejs教程')) {
     // 将事件推迟1刻，否则服务器信息将会显示在玩家信息之前
    event.server.scheduleInTicks(1, event.server, function (callback) {
      // 对每个人说以下内容，颜色为绿色。聊天信息为[Server]
      callback.data.tell(text.green('请访问h t  tp s://www.mcbbs.net/thread-1207772-1-1.html'[/url]))
      // 下面的这种表述方法设置了聊天信息
      callback.data.tell([Text.red('[Test]'),text.green('请访问h t  tp s://www.mcbbs.net/thread-1207772-1-1.html')])
    })
  }
})
```

另一个例子：监测到聊天信息时执行对应指令

```
onEvent('player.chat', function (event) {
  if (event.message.startsWith('test')) {
    event.server.runCommandSilent('kick '+event.player.name+' test ')
    event.server.runCommandSilent(`say 已踢出玩家${event.player.name}`)
    event.cancel()//取消该事件，也就是说玩家的聊天信息不会显示
  }
})
```

利用取消的这一特性，你甚至还可以做到"伪造"聊天

下面这个例子在检测到发送消息的玩家带有 `rankexample` 时在其聊天信息前加上rank标识

```
onEvent('player.chat',function (event){
	let input = event.message.trim();//获取聊天信息
	if(event.player.stages.has("rankexample")){
        event.server.tell([Text.blue('[MVP--]').bold(), `<${event.player.name}> ${input}`]);
        event.cancel();
    }
})
```

注：更多关于gamestage的操作详见11-4章节，关于玩家、世界的操作详见第15章
