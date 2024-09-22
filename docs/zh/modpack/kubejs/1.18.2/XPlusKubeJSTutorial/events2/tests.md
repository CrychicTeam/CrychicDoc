---
authors: ['Wudji']
---

# 15.6 测试用例

**一、防止玩家开创造**

```
onEvent("player.tick", event => {// 监听player.tick事件
    if(event.player.creativeMode || event.player.creativeMode.spectator){// 判断玩家游戏模式（创造或观察者）
        // 上述判断通过即通过服务器静默执行指令将玩家调为生存模式
        event.server.runCommandSilent(`gamemode survival ${event.player.name}`);
    }
})
```

**二、玩家佩戴下界合金头盔时给予缓慢效果**

```
onEvent("player.tick", event => {// 监听player.tick事件
    if(event.player.headArmorItem.id == "minecraft:netherite_helmet"){// 判断玩家头盔是不是下界合金头盔
        // 上述判断通过即给予玩家1 tick的缓慢I效果
        event.player.add("minecraft:slowness", 1, 1, false, false);
    }
})
```

**三、禁止非创造玩家捡起基岩**

```
onEvent("item.pickup", event => {// 监听item.pickup事件
    if(event.item.id == "minecraft:bedrock" && !event.player.creativeMode){// 判断物品是不是基岩
        // 判断通过则取消事件防止被捡起
        event.cancel();
    }
})
```

**四、防止烟花多次触发**

```
onEvent("item.right_click", event => {// 监听item.right_click事件
    if(event.item.id == "minecraft:firework_rocket" && event.player.onGround){// 判断右键点击的物品是不是烟花且玩家在地面上
        event.player.addItemCooldown("minecraft:firework_rocket", 200);
    }
})
```

**五、消耗铁加速玩家鞘翅飞行**

```
onEvent("item.right_click", event => {// 监听item.right_click事件
    if(event.item.id == "minecraft:iron" && !event.player.onGround && event.player.chestArmorItem.id == "minecraft:elytra"){// 判断物品是不是钻石且玩家不在地面上且玩家穿着鞘翅
        // 消耗一个铁加速玩家鞘翅飞行
        event.player.boostElytraFlight();
        event.item.count--;
        event.player.addItemCooldown("minecraft:iron", 60);
    }
})
```
