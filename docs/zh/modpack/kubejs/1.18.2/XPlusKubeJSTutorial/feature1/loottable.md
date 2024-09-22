---
authors: ['Wudji']
---

# 4 自定义Loot Table

***

(本章参考了[Minecraft 原版模组入门教程 - 战利品表(作者ruhuasiyu)](https://zhangshenxing.gitee.io/vanillamodtutorial/#%E6%88%98%E5%88%A9%E5%93%81%E8%A1%A8))

KubeJS目前能修改全局、方块、实体、猫或村民礼物(村庄英雄Buff)、钓鱼、宝箱战利品表

基本的方法名称如下

| 事件ID                 | 覆盖原有战利品表的方法名称 | 修改战利品表的方法名称  |
| -------------------- | ------------- | ------------ |
| generic.loot\_tables | addGeneric    | modify       |
| block.loot\_tables   | addBlock      | modifyBlock  |
| entity.loot\_tables  | addEntity     | modifyEntity |
| gift.loot\_tables    | addGift       | modify       |
| fishing.loot\_tables | addFishing    | modify       |
| chest.loot\_tables   | addChest      | modify       |

下面就下面5种的战利品表进行讲解。

***

## 0、基本格式

```
onEvent('事件ID', event => {
  event.覆盖/修改原有战利品表方法名称('物品/实体注册名', table => {
  	//修改内容
  }
  
  //table.clearPools()//清空所有随机池
  //table.clearConditions()//清空条件
  //table.clearFunctions()//清空函数
})
```

## 1、方块战利品表(block.loot\_tables)

针对方块战利品表，KubeJS提供了很简单的添加单方块掉落物的方法。

```
onEvent('block.loot_tables', event => {
  event.addSimpleBlock('minecraft:dirt', 'minecraft:red_sand')//覆盖泥土的战利品表并使泥土掉落红沙
  event.addSimpleBlock('minecraft:oak_leaves')//覆盖橡树树叶的战利品表并使橡树树叶在破坏时掉落自身
})
```

如果你想要实现更复杂的修改，你需要使用随机池。KubeJS提供了一些简写方法，具体见下方例子。

```
onEvent('block.loot_tables', event => {
  event.addBlock('minecraft:dirt', table => {
    table.addPool(pool => {//新建随机池(覆盖原有的)
      pool.rolls = 1 // 固定值
      // pool.rolls = [4, 6] // 也可写为 {min: 4, max: 6} // 抽奖次数∈[4,6]
      // pool.rolls = {n: 4, p: 0.3} // 二项分布
      //以上两种值的表达方式为值提供器
      pool.addItem('minecraft:dirt')
      //pool.addItem('minecraft:dirt', 40) // 此处40为权重
      //pool.addItem('minecraft:dirt', 40, [4, 8]) // 物品个数∈[4,8]
      // pool.addCondition({json}) //json格式，抽取该物品需要满足的条件，原版数据包格式（具体使用情景详见14.1）
      // pool.addEntry({json})//json格式，项目类别
    })
  })
})
```

注：修改方块战利品表(而不是覆盖)的示例详见14.1

## 2、实体战利品表(entity.loot\_tables)

```
onEvent('entity.loot_tables', event => {
  // 覆盖僵尸的战利品表，掉落5个物品(25%概率为胡萝卜，75%为苹果)
  event.addEntity('minecraft:zombie', table => {
    table.addPool(pool => {
      pool.rolls = 5//抽5次奖
      pool.addItem('minecraft:carrot', 1)
      pool.addItem('minecraft:apple', 3)
    })
  })
  
  event.modifyEntity('minecraft:pig', table => {
    table.addPool(pool => {
      // 修改猪的战利品表，使在被玩家杀死时多掉落一个泥土
      // 注：只是修改战利品表。并不会覆盖原有的掉落生/熟猪肉的战利品表
      pool.addItem('minecraft:dirt')
      pool.killedByPlayer()
    })
  })
})
```

注：对于`gift.loot_tables`、`fishing.loot_tables`、`chest.loot_tables`，只能使用`addJson(物品注册名, json原版格式)`来进行修改

## 3、常用表达方式

```
      /*
      	在新建/修改随机池时，还有以下表达方式可用：
      	以下值提供器与上述的表示方法相同
      	pool.enchantRandomly(附魔注册名)// 随机附魔
      	pool.enchantWithLevels(附魔等级(值提供器), 是否为宝藏(布尔值))// 指定等级和是否为宝藏的随机附魔
      	pool.name(name as Text)// 指定名称
      	pool.damage(掉落物耐久值(值提供器))// 修改掉落物耐久值
      	pool.randomChance(掉落概率(double型))// 修改掉落概率
      	pool.randomChanceWithLooting(掉落概率(double型), 倍率(double型))// 使用该表达方式时，抢夺或时运会对掉落物有影响
      	pool.furnaceSmelt()// 使当前随机池掉落的物品掉落形式为经过熔炉烧炼(自动烧炼)
      	pool.killedByPlayer()// 添加条件：实体被玩家杀死才会掉落
      	pool.survivesExplosion()// 添加条件：方块被爆炸破坏时依旧掉落
      	//以上方法也可以用于对Loot Table直接修改（例如table.name(name as Text)），具体使用见14.1
      */
```
