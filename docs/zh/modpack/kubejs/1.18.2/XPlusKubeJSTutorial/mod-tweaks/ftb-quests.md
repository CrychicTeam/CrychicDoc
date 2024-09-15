# 11.3 FTB Quest相关修改

***

FTB Quest任务模组内置对KubeJS的支持，你可以通过KubeJS便捷地修改FTB Quest任务的进度/自定义任务等

FTB Quest自身提供了 `ftbquests.completed.[任务ID]` (任务完成事件)， `ftbquests.custom_reward.<奖励ID>` (自定义任务奖励)和 `ftbquests.custom_task.<任务ID>` 三个事件可供监听，你也可以在其他事件中对玩家游戏进度进行修改。

本节将通过任务示例“村民挚友”来讲解如何通过KubeJS修改FTB Quest相关内容\*\*(推荐先阅读第15章内容)\*\*

![](https://m1.miaomc.cn/uploads/20220509\_ed5c881d5875b.png)

## **1、于其他事件中直接修改进度**

例子1：当玩家攻击村民时重置其任务进度

```
ID为onEvent('entity.attack', event => {
	if(event.source && event.source.actual && event.source.actual.player && event.entity.type == 'minecraft:villager'){// 检测玩家攻击村民，防止为空报错
        event.source.actual.tell("你攻击了村民，村民挚友任务失败！");
        event.source.actual.data.ftbquests.changeProgress("4425171B4650EB54", event => {
            event.reset = true;// 重设ID为4425171B4650EB54的任务的进度
        })
    }
})
// 另一种写法
onEvent('entity.attack', event => {
	if(event.source.actual.player!=null && event.entity.type == 'minecraft:villager'){
        event.source.actual.tell("你攻击了村民，村民挚友任务失败！");
        event.source.actual.data.ftbquests.reset("4425171B4650EB54的");// 重设ID为4425171B4650EB54的任务的进度
    }
})
```

例子2：玩家攻击掠夺兽时添加进度

```
onEvent('entity.death', event => {
    	if(event.source && event.source.actual && event.source.actual.player && event.entity.type == 'minecraft:ravager'){
            event.source.actual.tell("你击杀了劫掠受，等价于击杀三个唤魔者！");
            event.source.actual.data.ftbquests.addProgress("6B2089D4683E7907", 3);// 为ID为6B2089D4683E7907的任务添加进度值3
        }
})
```

## 2、自定义奖励

例子：玩家完成村民挚友任务时给予村庄英雄效果

```
onEvent('ftbquests.custom_reward.4883F7BD04E2C597', event => {// 设置ID为4883F7BD04E2C597的自定义奖励
    // 给予玩家药水效果奖励(村庄英雄)
    event.player.potionEffects.add('minecraft:hero_of_the_village', 120000, 5, false, false);
})
```

## 3、自定义任务

例子：检测玩家是否完成进度`minecraft:adventure/trade`

```
onEvent('ftbquests.custom_task.76AA10188A48B51F', event => {// 设置ID为76AA10188A48B51F的自定义任务
    event.checkTimer = 1;// 定义多久检测一次
    event.maxProgress = 1;// 定义最大进度
    event.check = (task, player) => {
        if (player.isAdvancementDone("minecraft:adventure/trade")) {// 检测进度是否完成
            player.tell("done");
            task.progress++;// 添加进度
        }
    }
})
```

## 4、监听任务完成事件

```
onEvent('ftbquests.completed.4425171B4650EB54', event => {// 检测ID为4425171B4650EB54的任务是否完成
    console.log("村民挚友任务已被完成");
})
```

**注：你可以通过右击任务图标并点击复制ID来获取指定任务/奖励的ID**

![](https://m1.miaomc.cn/uploads/20220509\_5f8ec1de2bc00.png)

附本节用到的FTB Quest配置文件：https://wwu.lanzouw.com/iTone04k5arc
