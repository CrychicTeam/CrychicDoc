---
editor: Gu-meng
---
# 聊天消息+1
本章主要涉及内容：Component，聊天事件，本章所有代码部分都在`server_scripts`里

涉及模组及版本:
1. jei-1.20.1-forge-15.3.0.4
2. rhino-forge-2001.2.2-build.18
3. architectury-9.2.14-forge
4. kubejs-forge-2001.6.5-build.14
5. probejs-6.0.1-forge

## 完整代码
```js
PlayerEvents.decorateChat(event=>{
    const msg = event.getMessage();
    event.setMessage(Text.of(msg).clickSuggestCommand(msg).hover(Text.of("+1")))
})
```
玩家发送消息后，会进行消息处理，重新设置消息属性，让他可以被点击后将消息直接复制粘贴到玩家的发送消息栏和玩家鼠标放在消息上提示会显示+1的文本
