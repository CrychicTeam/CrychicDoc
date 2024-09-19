# 物品修饰器

## 前言

- 作用：用于对物品施加单个或多个操作，例如使空地图变为指向某个标签中结构的寻宝地图。

<!-- - 数据格式：\{condition:"谓词类型", ...谓词的内容, function:"物品修饰器类型", ...物品修饰器的内容\} 参见：[minecraft-wiki/物品修饰器#数据格式](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#%E6%95%B0%E6%8D%AE%E6%A0%BC%E5%BC%8F)

- 参见：[minecraft-wiki/物品修饰器](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8) -->

> [!WARNING] 注意
> 由于大部分物品修饰器并不被KubeJS原生支持，只能以Json传入，本文已尝试给出示例但观感极差，因此建议在[minecraft-wiki/物品修饰器#数据格式](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#%E6%95%B0%E6%8D%AE%E6%A0%BC%E5%BC%8F)了解某个修饰器的作用后使用[数据包生成器#物品修饰器](https://misode.github.io/item-modifier/)来快速书写物品修饰器。

## 物品修饰器类型

<!-- ## 战利品修饰器

- 用于修改战利品表中物品栈的函数。

- 战利品修饰器或战利品表物品函数就是[物品修饰器](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8) -->

### 应用奖励公式

### 复制方块实体显示名

### 复制NBT

### 复制方块状态

### 随机附魔

- 作用：将对战利品项从附魔列表中随机附魔。

<!-- - 语句：enchantRandomly(附魔id数组);

- 示例：掉落了保护1的金苹果。

```js
ServerEvents.entityLootTables(event => {
    event.modifyEntity('minecraft:husk', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:golden_apple', 5, 1)
            .enchantRandomly(['minecraft:protection'])
        })
    })
})
``` -->

### 给予等价于经验等级的随机魔咒

- 作用：对战利品项执行一次数字提供器返回的等级的附魔。

<!-- - 语句：.enchantWithLevels(数字提供器, 是否包含宝藏附魔);

- 示例：尸壳掉落一把30级附魔的铁剑。

```js
ServerEvents.entityLootTables(event => {
    event.modifyEntity('minecraft:husk', loot => {
        loot.addPool(pool => {
            pool.addItem(Item.of('minecraft:iron_sword', '{Damage:0}'), 5, 1)
            .enchantWithLevels(30, true)
        })
    })
})
``` -->

### 设置探险家地图

### 爆炸损耗

### 填充玩家头颅

### 熔炉熔炼

- 作用：得到物品放入熔炉冶炼后的产物。

<!-- - 语句：furnaceSmelt()

- 示例：尸壳死亡掉落橡木的熔炉冶炼产物。

```js
ServerEvents.entityLootTables(event => {
    event.modifyEntity('minecraft:husk', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:oak_wood', 5, 1).furnaceSmelt()
            
        })
    })
})
``` -->

### 限制堆叠数量

作用：

### 根据魔咒调整物品数量

<!-- - 语句：lootingEnchant(每级抢夺掉落数-数字提供器, 战利品项总计最大掉落数);

- 示例：尸壳掉落金苹果，每多1级抢夺额外掉落1-2个，总共最多掉落6个。

```js
ServerEvents.entityLootTables(event => {
    event.modifyEntity('minecraft:husk', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:golden_apple', 5, 1)
            .lootingEnchant([1, 2], 6) 
        })
    })
})
``` -->

### 引用物品修饰器

### 设置属性

### 设置旗帜图案

### 设置内容物

### 设置物品数量

### 设置损伤值

### 设置魔咒

### 设置乐器

### 设置战利品表

### 设置物品描述

### 设置物品名

### 设置NBT

### 设置药水

### 设置迷之炖菜状态效果
