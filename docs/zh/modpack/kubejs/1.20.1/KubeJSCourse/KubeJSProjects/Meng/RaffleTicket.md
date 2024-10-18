---
authors: ['Gu-meng']
---
# 抽奖券
本章涉及内容：AttachedData、物品注册、自定义战利品、自定义战利品生成、运用nbt，本章代码除了注册在`startup_scripts`其他都在`server_scripts`内
涉及模组及版本:
1. rhino-forge-2001.2.2-build.18
2. architectury-9.2.14-forge
3. kubejs-forge-2001.6.5-build.14
4. probejs-6.0.1-forge

## 注册抽奖券
```js
StartupEvents.registry("item",event=>{
    event.create("meng:raffle_ticket");
})
```

## 抽奖券的公共文件
```js
// priority: 5

// 抽奖券基础物品列表
const basalItemList = [];
// 抽奖券奖池列表(作为临时抽奖存储用)
let poolItemList = [];
// 抽奖券的类型对象
let ticketTypeObject = {
    basal: basalItemList
};
```
如果需要添加其他的战利品，可以像下面这样添加
```js
const testItemList = [];
let ticketTypeObject = {
    basal: basalItemList,
    addTest : testItemList
};
```

## 抽奖券战利品添加
```js
function addBasalItem(itemId, weight) {
    basalItemList.push({
        itemId: itemId,
        weight: weight
    })
}

addBasalItem('minecraft:diamond', 3);
addBasalItem('minecraft:lapis_lazuli', 5)
addBasalItem('minecraft:emerald', 2)
addBasalItem('minecraft:gold_ingot', 8)
addBasalItem('minecraft:copper_ingot', 20)
addBasalItem('minecraft:iron_ingot', 20)
addBasalItem('minecraft:netherite_ingot', 1)
```

## 抽奖券战利品
```js
ServerEvents.genericLootTables(event => {
    for (const key in ticketTypeObject) {
        event.addGeneric("meng:raffle_ticket/" + key,loot=>{
            loot.addPool(pool => {
                ticketTypeObject[key].forEach(value => {
                    pool.addItem(value.itemId).weight(value.weight);
                });
            });
        })
    }
})
```

## 开始抽奖
```js
ItemEvents.firstRightClicked("meng:raffle_ticket", event => {
    const player = event.getPlayer();
    const playerAttachedData = player.getData();
    if (playerAttachedData.get("lotteryState")) {
        player.tell(Text.translate("text.meng.lottery_state"))
        // playerAttachedData.put("lotteryState",false);
        return;
    }
    const item = event.getItem();
    const itemNbt = item.getNbt();
    let ticketType;
    try {
        ticketType = itemNbt.getString("ticketType");
    } catch (e) {
        ticketType = "basal";
    }
    let itemList = ticketTypeObject[ticketType];
    if (itemList == undefined) {
        itemList = ticketTypeObject.basal;
    }
    let uuid = player.getUuid()
    let obj = {}
    obj[uuid] = MengUtils.ArrUtil.randArr(addItemList(itemList))
    poolItemList.push(obj)

    let lootData = event.getServer().getLootData();
    let itemLoot = lootData
        .getLootTable("meng:raffle_ticket/basal")
        .getRandomItems(
            new $LootParams(event.getLevel())
                .create($LootContextParamSets.EMPTY)
        )[0];
    let poolList = poolItemList.find(value => Object.keys(value).includes(uuid.toString()))[uuid];
    poolList[poolList.length - 2] = itemLoot.id;
    
    playerAttachedData.add("lotteryState", true);
    playerAttachedData.add("poolCountMax", poolList.length);
    playerAttachedData.add("count", 0);
    item.count--;
})

function addItemList(itemList) {
    let list = []
    itemList.forEach(value => {
        for (let index = 0; index < value.weight; index++) {
            list.push(value.itemId);
        }
    });
    let listTemp = list;

    while (true) {
        if (list.length >= raffleTicketConfig.maxCount) {
            break;
        }
        list = list.concat(MengUtils.ArrUtil.randArr(listTemp));
    }
    return list;
}
```

## 抽奖动画
```js
PlayerEvents.tick(event => {
    let player = event.getPlayer();
    let playerAttachedData = player.getData();
    let tickCount = event.getServer().getTickCount();
    if (playerAttachedData.get("lotteryState")) {
        let countMax = playerAttachedData.get("poolCountMax");
        let count = playerAttachedData.get("count");
        let itemShow = itemShowFunc(player,count,countMax);
        if (countMax - count <= 3) {
            if (tickCount % 10 == 0){
                if (count >= countMax){
                    playerAttachedData.add("lotteryState",false);
                    let poolList = poolItemList.find(value => Object.keys(value).includes(player.getUuid().toString()))[player.getUuid()];
                    player.give(Item.of(poolList[count-2]));
                    player.playNotifySound("minecraft:entity.player.levelup","players",1,1)
                    paintHide(player)
                    poolList = [];
                }else{
                    paintShow(player,itemShow.oneItemId,itemShow.twoItemId,itemShow.threeItemId);
                    playerAttachedData.add("count", count + 1)
                    player.playNotifySound("minecraft:entity.experience_orb.pickup","players",1,1)
                }
            }
        } else if (countMax - count <= 10) {
            if (tickCount % 5 == 0){
                paintShow(player,itemShow.oneItemId,itemShow.twoItemId,itemShow.threeItemId);
                playerAttachedData.add("count", count + 1)
                player.playNotifySound("minecraft:entity.experience_orb.pickup","players",1,2)
            }
        } else {
                paintShow(player,itemShow.oneItemId,itemShow.twoItemId,itemShow.threeItemId);
                playerAttachedData.add("count", count + 1)
                player.playNotifySound("minecraft:entity.experience_orb.pickup","players",1,2)
        }
        
    }
})

function itemShowFunc(player,count,max) {
    let uuid = player.getUuid();
    let poolList = poolItemList.find(value => Object.keys(value).includes(uuid.toString()))[uuid];
    let two_item = poolList[Number(count)];
    let one_item = poolList[count - 1 == -1 ? max - 1 : count - 1];
    let three_item;
    if (count - 2 == -2) {
        three_item = poolList[max - 2]
    } else if (count - 2 == -1) {
        three_item = poolList[max - 1]
    } else if (count - 2 >= 0) {
        three_item = poolList[count - 2]
    }
    return {
        oneItemId:one_item,
        twoItemId:two_item,
        threeItemId: three_item
    }
}

function paintHide(player) {
    player.paint({
        item_one: {
            visible: false
        }, item_two: {
            visible: false
        }, item_three: {
            visible: false
        }
    })
}

function paintShow(player, one_item, two_item, three_item) {
    player.paint({
        item_one: {
            type: "item",
            item: two_item,
            visible: true,
            w: raffleTicketConfig.sideItemShowSize,
            h: raffleTicketConfig.sideItemShowSize,
            x: "$screenW/2-" + raffleTicketConfig.itemShowInterval,
            y: "$screenH/2+sin(time()*0.5)*$screenH/32",
        },
        item_two: {
            type: "item",
            item: one_item,
            visible: true,
            w: raffleTicketConfig.middleItemShowSize,
            h: raffleTicketConfig.middleItemShowSize,
            x: "$screenW/2",
            y: "$screenH/2+sin(time()*0.5)*$screenH/32",
        },
        item_three: {
            type: "item",
            item: three_item,
            visible: true,
            w: raffleTicketConfig.sideItemShowSize,
            h: raffleTicketConfig.sideItemShowSize,
            x: "$screenW/2+" + raffleTicketConfig.itemShowInterval,
            y: "$screenH/2+sin(time()*0.5)*$screenH/32",
        }
    })
}
```

## 一些注意事项
1. 战利品通过不同的物品携带的nbt里的`ticketType`不同来进行不同的奖池抽奖，做了简单的防止找不到nbt所以有一个默认的奖池为basalItemList，如果不需要添加其他奖池的可以直接修改`抽奖券战利品添加`里的代码
2. 该项目只是作为示例，很多地方并不是最优解，可自行进行解决
3. 如果对该项目代码部分不满可以将修改好的代码上传至[gitee项目仓库](https://gitee.com/gumengmengs/kubejs-course)