---
authors: ['Wudji']
---

# 11.4 KubeJs内置游戏阶段 (类似于GameStage)

***

| 功能          | 表达格式                   | 返回值  |
| ----------- | ---------------------- | ---- |
| 禁用一个阶段状态的更新 | .addNoUpdate('阶段名')    | 布尔型  |
| 启用一个阶段状态的更新 | .removeNoUpdate('阶段名') | 布尔型  |
| 玩家是否具有阶段    | .has('阶段名')            | 布尔型  |
| 为玩家添加阶段     | .add('阶段名')            | 布尔型  |
| 为玩家移除阶段     | .remove('阶段名')         | 布尔型  |
| 为玩家设置阶段     | .set('阶段名', 布尔值)       | 布尔型  |
| 切换阶段状态      | .toggle('阶段名')         | 布尔型  |
| 同步阶段        | .sync()                | void |
| 替换阶段//该功能存疑 | replace('阶段名')         | void |

通常以上内容都接在event.player.stages后，例如`event.player.stages.add('XPlusUser')`

下面是一个实例

```
onEvent('player.logged_in',event=>{
    let username = e.player.name
    if (!event.player.stages.has('0') && username == "Wudji_Notfound") {
    	//0代表离线，1代表在线
        event.player.stages.add('0');
        event.player.stages.remove('1');
        event.server.runCommand(`say 无敌鸡加入了游戏！`);
    }
})

onEvent('player.logged_out',event=>{
    let username = e.player.name
    if (!event.player.stages.has('1') && username == "Wudji_Notfound") {
        event.player.stages.add('1');   
        event.player.stages.remove('0');
        event.server.runCommand(`say 无敌鸡离开了游戏！`);
    }
})
```

你还可以将游戏阶段用于任务设置，详见本教程后半部分
