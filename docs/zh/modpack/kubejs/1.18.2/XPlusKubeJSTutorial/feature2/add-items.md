---
authors: ['Wudji']
---

# 13 自定义物品和物品ToolTip

***

要使用KubeJS来添加物品，你需要使用事件`item.registry`。该事件属于StartUp事件。

## 一、事件监听

你可以使用`event.create("自定义物品ID")`语句来创建物品。

```
onEvent("item.registry", event => {
  event.create("自定义物品ID")
})
```

物品的材质应被放于`kubejs/assets/kubejs/textures/item/物品ID.png`

如果你想使用自定义的物品模型，你可以使用 Blockbench 制作然后把它放于`kubejs/assets/kubejs/models/item/物品ID.json`

此外，你还可以手动定义材质路径，详见下文。

**注意：1.18.2中事件监听方式有修改，详见1.18.2 方块 / 物品注册修改 章节**

## 二、ItemBuilder

你可以使用以下方法设定物品属性。

| 方法                                              | 描述                | 属性默认值                | 返回值         |
| ----------------------------------------------- | ----------------- | -------------------- | ----------- |
| type(ItemType 类型)                               | 设置物品类型\[1]（仅1.16） | basic                | ItemBuilder |
| tier(字符串 类型)                                    | 设置物品Tier\[2]      | -                    | ItemBuilder |
| modifyTier(tier => ...)                         | 自定义物品tier         | -                    | ItemBuilder |
| maxStackSize(整形 数量)                             | 设置物品单组最大数量        | 64                   | ItemBuilder |
| maxDamage(整形 耐久值)                               | 设置物品最大耐久值         | 0                    | ItemBuilder |
| burnTime(整形 燃烧时间)                               | （燃料）设置物品燃烧时间      | 0                    | ItemBuilder |
| containerItem(字符串 物品ID)                         | 设置物品容器ID\[3]      | "minecraft:air"      | ItemBuilder |
| subtypes(Function\<ItemStackJS, Collection> fn) | 设置物品的SubTypes     | null                 | ItemBuilder |
| tool(ToolType 类型, 整形 等级)                        | 设置物品工具属性          | new HashMap<>()      | ItemBuilder |
| miningSpeed(浮点型 速度)                             | （工具）设置挖掘速度        | 1.0F                 | ItemBuilder |
| attackDamage(浮点型 伤害)                            | （武器）设置攻击伤害        | -                    | ItemBuilder |
| attackSpeed(浮点型 速度)                             | （武器）设置攻击速度        | -                    | ItemBuilder |
| rarity(RarityWrapper 稀有度)                       | 设置物品稀有度           | RarityWrapper.COMMON | ItemBuilder |
| glow(布尔值 是否发光)                                  | 设置物品是否发光          | false                | ItemBuilder |
| tooltip(Component 文本)                           | 设置物品的ToolTip      | new ArrayList<>()    | ItemBuilder |
| group(字符串 物品组)                                  | 设置物品的创造物品栏位\[4]   | KubeJS.tab           | ItemBuilder |
| color(整形 index, 整形 c)                           | 设置物品颜色            | 0xFFFFFFFF           | ItemBuilder |
| texture(字符串 材质路径)                               | 手动设置物品材质          | 空字符串                 | ItemBuilder |
| parentModel(字符串 模型路径)                           | 手动设置物品模型          | 空字符串                 | ItemBuilder |
| food(Consumer b)                                | （转换为食物）设置食物属性     | null                 | ItemBuilder |

\[1] 可用值有：basic（默认值）、sword、pickaxe、axe、shovel、hoe、helmet、chestplate、leggings、boots。

* **注意：1.18.2中该方法有修改，详见1.18.2 方块 / 物品注册修改 章节**

\[2] 工具类的tier类型有：wood、stone、iron、gold、diamond、netherite；护甲类的tier类型有：leather、chainmail、iron、gold、diamond、turtle、netherite

\[3] 支持的方法同新建tier

\[4] 即类似于岩浆桶和空桶之间的关系。

\[5] 可用值有：search、buildingBlocks、decorations、redstone、transportation、misc、food、tools、combat、brewing

**注：以上方法中`attackDamage(浮点型 伤害)`、`attackSpeed(浮点型 速度)`在1.16.5版本中可能无法生效，截至编辑本节时仍未修复。**

以下为示例：

```
onEvent("item.registry", event => {
  // 基础示例
  event.create("can_empty").displayName("空罐头盒").maxStackSize(32);
  // 注册头盔
  event.create("another_helmet").displayName("另一个头盔").type("helmet").tier("netherite");
  // 食物注册
  event.create("magic_steak").food(food => {// FoodBuilder
		food
    		.hunger(6)// 饱食度
    		.saturation(6)// 饱和度
      		.effect("speed", 600, 0, 1)// 食用后给予的效果，接受值：效果ID, 持续时间, 等级, 概率(0~1)
      		.removeEffect("poison")// 食用后移除的效果
      		.alwaysEdible()// 无视饱食度依旧可以食用（类似金苹果）
      		.fastToEat()// 缩短食用事件（类似干海带）
      		.meat()// 设置为肉类（狗可食用）
      		.eaten(ctx => {// 当使用时执行...（ItemFoodEatenEventJS）
      			// 该方法可用于部分替代item.food_eaten事件
        		ctx.player.tell(`${ctx.item.id} 真好吃~`)
        	})
	})
})
```

另请参阅：[ItemBuilder](https://github.com/KubeJS-Mods/KubeJS/blob/eol/1.16/common/src/main/java/dev/latvian/kubejs/item/ItemBuilder.java)、[FoodBuilder](https://github.com/KubeJS-Mods/KubeJS/blob/eol/1.16/common/src/main/java/dev/latvian/kubejs/item/FoodBuilder.java)、[ItemFoodEatenEventJS](https://github.com/KubeJS-Mods/KubeJS/blob/eol/1.16/common/src/main/java/dev/latvian/kubejs/item/ItemFoodEatenEventJS.java)

## 三、新建Tier

通过新建Tier你可以将一套属性快速应用于多个物品上。

```
// 注：以下数据均为默认值（对应tier:iron）

// 注册工具tier
onEvent('item.registry.tool_tiers', event => {
  event.add('tier_id', tier => {
    tier.uses = 250 // 耐久
    tier.speed = 6.0 // 挖掘速度
    tier.attackDamageBonus = 2.0 //伤害加成
    tier.level = 2 // 等级
    tier.enchantmentValue = 14 // 附魔
    tier.repairIngredient = '#forge:ingots/iron'// 铁砧修复用物品
  })
})

// 注册护甲tier
onEvent('item.registry.armor_tiers', event => {
  // 栏位顺序对应 [靴子, 护腿, 胸甲, 头盔]
  event.add('tier_id', tier => {
    tier.durabilityMultiplier = 15 // 耐久倍率，每个栏位对应[13, 15, 16, 11]
    tier.slotProtections = [2, 5, 6, 2]// 保护倍率
    tier.enchantmentValue = 9 // 附魔
    tier.equipSound = 'minecraft:item.armor.equip_iron' // 装备音效
    tier.repairIngredient = '#forge:ingots/iron' // 铁砧修复用物品
    tier.toughness = 0.0 // 护甲韧性，钻石为2.0，下界合金为3.0
    tier.knockbackResistance = 0.0
  })
})
```

在添加完毕以上tier后，你可以在`type(ItemType 类型)`中传入你自己添加的`tier_id`来应用它。

## 四、ToolTip

以下为部分示例：

```
onEvent('item.tooltip', tooltip => {
  // 批量添加tooltip
  tooltip.add(['minecraft:diorite', 'minecraft:granite', 'minecraft:andesite'], '三 大 废 岩')
  // 你可以使用除#tag以外的其他任何表述方法
  tooltip.add(/refinedstorage:red_/, 'Can be any color')
  // 你可以使用数组来新建多行tooltip. 你可以使用其他转义符来避开'这个符号
  tooltip.add('thermal:latex_bucket', ["Not equivalent to Industrial Foregoing's Latex", 'Line 2'])
  
  tooltip.addAdvanced('thermal:latex_bucket', (item, advanced, text) => {
    text.add(0/*index*/, Text.of('Hello')) // 当index为0时，这种表示方法 会替换掉物品名称. 如果你想要在第二行添加tooltip，index必须为1，以此类推.
  })
})
```

另请参阅：[ItemTooltipEventJS](https://github.com/KubeJS-Mods/KubeJS/blob/eol/1.16/common/src/main/java/dev/latvian/kubejs/item/ItemTooltipEventJS.java)
