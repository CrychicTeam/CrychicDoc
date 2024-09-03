# 添加食物
在本章中将会介绍如何使用物品添加事件去添加食物，本章所有js代码都是在文件夹`startup_scripts`下

## 基础写法
```js
StartupEvents.registry("item", event => {
    event.create("meng:my_food")
        .food(foodBuilder=>{})
})
```
这是最基础的写法，将该物品设置为食物，我们可以为食物设置一些参数

## 食物参数方法
|                 方法名                 |         参数          |              作用              |           备注           |
| :------------------------------------: | :-------------------: | :----------------------------: | :----------------------: |
|           saturation(float)            |        饱和度         |  设置食物给玩家带来的饱和倍率  |   数值*饱食度 = 饱和度   |
|            hunger(integer)             |        饱食度         |   设置食物给玩家带来的饱食度   |   直接食用带来的"鸡腿"   |
|                 meat()                 |           -           |            设置为肉            |       用于被狼食用       |
|             alwaysEdible()             |           -           |        设置为随时可食用        | 不需要消耗饱食度就可食用 |
|              fastToEat()               |           -           |         设置为快速食用         |          吃得快          |
|        removeEffect(MobEffect)         |       药水效果        |    食用后清除某一种药水效果    |            -             |
|    eaten(Consumer\<FoodEatenEvent\>)     |      食用后事件       |               -                |            -             |
| effect(ResourceLocation,int,int,float) | [effect参数](#effect) |     设置食用后给予药水效果     |            -             |
|                build()                 |           -           | 返回该食物的`FoodProperties`类 |            -             |

[关于饱和度和饱食度的区别](../../../ti-wai-hua/saturation-hunger)

### effect
```
effect(
    ResourceLocation, 药水效果id
    int, 持续tick
    int, 药水等级(n+1为实际等级)
    float 药水生效概率(1=100%)
)
```

## 药水效果设置
### 添加药水效果
```js
StartupEvents.registry("item", event => {
    event.create("meng:my_food")
        .food(foodBuilder=>{
            foodBuilder.effect("minecraft:speed",20*20,0,0.5)
        })
})
```
当玩家食用该物品后会给玩家一个20秒的1级速度效果，生效概率为50%

### 移除药水效果
```js
StartupEvents.registry("item", event => {
    event.create("meng:my_food2")
        .food(foodBuilder=>{
            foodBuilder.removeEffect("speed")
        })
})
```
虽然但是好像移除失败了（；´д｀）ゞ

## 使用后事件
```js
const { $Player } = require("packages/net/minecraft/world/entity/player/$Player")

StartupEvents.registry("item", event => {
    event.create("meng:my_food3")
        .food(foodBuilder=>{
            foodBuilder.eaten(foodEatenEvent=>{
                /**
                 * @type {$Player}
                 */
                let player = foodEatenEvent.getPlayer()
                if (foodEatenEvent.getPlayer() != null){
                    player.give("bowl")
                }
            })
        })
})
```
如果食用该食物的是玩家，则给玩家返回一个碗(这里判断了一下是怕食用者不是玩家)

### 事件里可调用的方法
|   方法名    |    返回类型     |
| :---------: | :-------------: |
|  getItem()  |    ItemStack    |
| getEntity() |     Entity      |
| getPlayer() |     Player      |
| getLevel()  |      Level      |
|  getServer  | MinecraftServer |

## 简单示例
```js
event.create("meng:my_food4")
        .food(foodBuilder=>{
            foodBuilder.hunger(10) //设置恢复5个“鸡腿”
            foodBuilder.saturation(0.5) // 饱和度设置为10*0.5 = 5
            foodBuilder.meat() // 设置食物属性为肉，可以被狗食用
            foodBuilder.alwaysEdible() //设置为无需消耗饱食度即可使用
            foodBuilder.fastToEat() //设置该食物为快速食用类
        })
```