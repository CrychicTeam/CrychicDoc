# 14 方块和物品属性修改

***

KubeJs允许你修改一些已经存在的物品/方块的属性

## 1、物品属性修改

```
onEvent('item.modification', event => {
  event.modify('minecraft:ender_pearl', item => {
    item.maxStackSize = 64
    item.fireResistant = true
  })
})
```

该段示例脚本将末影珍珠每组最大数量调整为了64，并且不会被烧毁

以下是可以使用的方法（a = b）：

* maxStackSize // 每组数量
* maxDamage // 最大耐久值
* burnTime // 燃烧时间（燃料）
* craftingReminder // 合成后剩余物品（字符串）
* fireResistant // 是否抗火
* rarity // 稀有度（如Rarity.COMMON）
* tier = tierOptions => { // 物品等级
  * uses // 耐久
  * speed // 挖掘速度
  * attackDamageBonus // 攻击伤害
  * level // 工具等级
  * enchantmentValue // 附魔值
  * repairIngredient // 铁砧修复需要使用的物品，格式如Ingredient.of('物品注册名')
* }
* foodProperties = food => { // 食物设置。注: 使用函数而不是 a = b
  * hunger(int) // 饱食度
  * saturation(float) // 饱和度
  * meat(boolean) // 是否为肉食
  * alwaysEdible(boolean) // 是否总是可食用(无论饱食度)
  * fastToEat(boolean) // 是否能被快速吃掉
  * effect(效果ID(字符串), 时间(整形), 效果等级(整形), 获得倍率(浮点型)) // 吃掉后给予的效果
  * removeEffect(String effectId) // 吃掉后移除的效果
* }

## 2、方块属性修改

```
onEvent('block.modification', event => {
  event.modify('minecraft:stone', block => {
    block.destroySpeed = 0.1
    block.hasCollision = false
  })
})
```

该段示例脚本将石头的破坏速度调整为0.1, 并且使其无碰撞箱

以下是可使用的方法（a = b）：

* material // 材质
* boolean hasCollision // 是否有碰撞箱（布尔型）
* destroySpeed // 破坏速度（浮点型）
* explosionResistance // 爆炸抗性（浮点型）
* randomlyTicking // 是否接受随机刻控制
* soundType // 声音类型
* friction // 摩擦（浮点型）
* speedFactor // 速度倍率
* jumpFactor // 跳跃倍率
* lightEmission // 发光强度
* requiredTool // 是否需要工具掉落
