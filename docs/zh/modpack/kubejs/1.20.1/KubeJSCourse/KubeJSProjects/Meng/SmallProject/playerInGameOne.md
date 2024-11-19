# 检测玩家第一次进游戏
本章主要涉及内容：玩家进入游戏事件，游戏阶段，本章所有代码部分都在`server_scripts`里

涉及模组及版本:
1. jei-1.20.1-forge-15.3.0.4
2. rhino-forge-2001.2.2-build.18
3. architectury-9.2.14-forge
4. kubejs-forge-2001.6.5-build.14
5. probejs-6.0.1-forge

## 完整代码
```js
PlayerEvents.loggedIn(event=>{
    const player = event.player;
    if(!player.stages.has("oneInGame")){
        player.tell("欢迎第一次进入游戏")
        player.stages.add("oneInGame")
    }
})
```
该代码是检测玩家第一次进入游戏，然后给玩家发送消息，使用kjs内置的阶段来记录玩家进入游戏