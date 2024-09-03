# 生物掉落
在下面将会使用kubejs的生物战利品最基础的写法格式、可调用参数
## 基础写法
```js
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:pig", loot => {
        loot.addPool(pool => {
            pool.addItem("diamond").weight(1).count([3, 6])
            pool.addItem("iron_ingot").weight(2)
        })
    })
})
```
在上面代码中，我们将猪的掉落物(战利品,盖章后面统称为掉落物),修改为掉落3~6个钻石或者掉落一个铁锭，其中铁锭的权重占比是2，钻石的权重占比是1([权重的概念](../../ti-wai-hua/quan-zhong.md))

这里`pool.addItem("diamond").weight(1).count([3, 6])`是将权重占比和出现数量单独进行添加的，我们也可以写在一起，像这样`pool.addItem("diamond",1,[3,6])`

如果我们需要在原有的生物掉落中添加新的掉落我们可以这样写
```js
ServerEvents.entityLootTables(event => {
    event.modifyEntity("minecraft:pig", loot => {
        loot.addPool(pool => {
            pool.addItem("pig_spawn_egg")
        })
})
```

我们可以发现除了调用的方法不同以外，里面的调用参数都是一样的，下面简单的为大家列举了常用方法和例子
## 常用方法和示例
下面为大家举出常用的方法和对应的示例加上文本解释，所有代码的顶层代码都为
```js
ServerEvents.entityLootTables(event => {
    event.modifyEntity("minecraft:pig", loot => {
        loot.addPool(pool => {
            //code
        }
    }
})
```
code为代码内容
### setUniformRolls
随机从奖池抽取次数
```js
pool.setUniformRolls(1,3)
```
参数1:最小抽取次数

参数2:最大抽取次数
### enchantRandomly
使掉落物携带附魔属性，等级为随机的
```js
pool.enchantRandomly("minecraft:smite")
```
参数:附魔类型
### lootingEnchant
使物品掉落受抢夺影响，并设置数值
```js
pool.lootingEnchant(2,10)
```
参数1:抢夺每增加一级额外掉落增加几个 

参数2:最大掉落个数
### killedByPlayer
该掉落物必须为玩家击杀才会掉落
```js
pool.killedByPlayer()
```
### addEmpty
设置掉落物为空的权重占比
```js
pool.addEmpty(2)
```
参数: 权重占比
### furnaceSmelt
被熔炼的物品(类似熔炉燃烧,圆石掉落成石头)
```js
pool.furnaceSmelt()
```
### randomChanceWithLooting
随机掉落受抢夺影响 
```js
pool.randomChanceWithLooting(0.1,0.2)
```
参数1:没有抢夺的概率

参数2:每一层抢夺增加多少概率
### entityProperties
匹配实体属性，如果匹配则掉落奖池物品
```js
pool.entityProperties("killer", {
    type:"minecraft:player",
    equipment: {
            mainhand: {
                items: [
                    "minecraft:diamond_sword"
                ],
            predicates:{
                enchantments:{
                    enchantments:["minecraft:silk_touch"]
                }
            } 
        }
    }
})
```
检测击杀猪的是否为玩家，获取击杀猪的武器是否为钻石剑且附魔为精准采集

参数1: 实体类型(可选："[this](../../ti-wai-hua/zhan-li-pin-shang-xia-wen.md#this)","[killer_player](../../ti-wai-hua/zhan-li-pin-shang-xia-wen.md#killer_player)","[killer](../../ti-wai-hua/zhan-li-pin-shang-xia-wen.md#killer)","[direct_killer](../../ti-wai-hua/zhan-li-pin-shang-xia-wen.md#direct_killer)")

参数2: json格式的战利品表谓词里的entity_properties下的predicate
### addTag
添加tag掉落物
```js
pool.addTag("minecraft:beds",true)
```
参数1:tag标签名

参数2:是否随机抽取一个物品
### rolls
奖池抽奖次数
```js
pool.rolls = 3
```