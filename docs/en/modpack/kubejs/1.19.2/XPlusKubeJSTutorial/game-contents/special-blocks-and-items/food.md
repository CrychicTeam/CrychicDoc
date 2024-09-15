# 6.2 食物注册

***

## 一、基础信息

借助`ItemBuilder`下的方法`food(Consumer<FoodBuilder>)`，你可以便捷地创建食物物品，例如：

```js
StartupEvents.registry("item",event =>{
    event.create("example_food","basic").food(food=>{
        // 在此设置食物属性
    })
})
```

## 二、FoodBuilder

| 方法                                                                | 描述                         | 备注                                                                                               |
| ------------------------------------------------------------------- | ---------------------------- | -------------------------------------------------------------------------------------------------- |
| hunger(int 饱食度)                                                  | 设置食物提供的饱食度         | 详情见[Minecraft Wiki](https://minecraft.fandom.com/zh/wiki/%E9%A5%A5%E9%A5%BF#%E6%9C%BA%E5%88%B6) |
| saturation(float 饱和度)                                            | 设置食物提供的饱和度倍率\[1] | 详情见[Minecraft Wiki](https://minecraft.fandom.com/zh/wiki/%E9%A5%A5%E9%A5%BF#%E6%9C%BA%E5%88%B6) |
| meat(boolean flag)                                                  | 设置是否为肉食               | 例如原版中作为狼的食物                                                                             |
| meat()                                                              | 设置为肉食                   | 同上                                                                                               |
| alwaysEdible(boolean flag)                                          | 设置是否随时可食用           | 例如金苹果                                                                                         |
| alwaysEdible()                                                      | 设置为随时可食用             | 同上                                                                                               |
| fastToEat(boolean flag)                                             | 设置是否快速食用             | 例如干海带                                                                                         |
| fastToEat()                                                         | 设置为快速食用               | 同上                                                                                               |
| effect(ResourceLocation 药效ID, int 持续时间, int 倍率, float 概率) | 食用后给予药效               | 概率为0\~1                                                                                         |
| removeEffect(MobEffect 药效)                                        | 食用后移除指定药效           | -                                                                                                  |
| eaten(Consumer 事件)                                                | 设置食用完成后事件           | \[1]                                                                                               |

\[1]

`饱和度` = `设置的食物提供饱食度` \* `饱和度倍率`

\[2]

```
FoodEatenEventJS具有以下属性：
player 返回食用玩家
item 返回食用物品
值得注意的是，FoodEatenEventJS是PlayerEventJS的子类，这意味着其具有PlayerEventJS中的方法或属性。
```

## 三、示例

```js
StartupEvents.registry("item",event =>{
    event.create("super_wudji","basic").fireResistant(true).glow(true).food(food=>{
        food.hunger(3)
        food.saturation(2.0)// 提供的饱和度为 3 * 2 = 6
        food.alwaysEdible(true)
        food.fastToEat(true)
        food.meat(true)
        food.eaten(ctx =>{
            let player = ctx.player;
            if (player.getUsername() == "Wudji_NotFound"){
                player.potionEffects.add("minecraft:resistance",23333,5,true,false);
            }else{
                if(Utils.random() > 0.5){
                    player.give(Item.of("kubejs:super_wudji"));
                }
            }
        })
    })
})
```