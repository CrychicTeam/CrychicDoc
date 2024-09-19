# 战利品表

## 前言

- 本条目作为战利品章节所需必备知识的页面，你可以先阅读战利品章节的其他页面，遇到不理解的名词再折返来查看解释。

- **`战利品表`** 战利品用于决定在何种情况下生成何种物品。比如自然生成的容器和可疑的方块内容物、破坏方块时的掉落物、杀死实体时的掉落物、钓鱼时可以钓上的物品、猪灵的以物易物——引用自[minecraft-wiki/战利品表](https://zh.minecraft.wiki/w/%E6%88%98%E5%88%A9%E5%93%81%E8%A1%A8)，战利品触发时，将测试所持有的所有战利品池的谓词，过滤出条件通过的战利品池，对这些战利品池中的每个战利品项测试战利品项谓词，随后应用战利品修饰，如果战利品修饰也有谓词，则也需要判断谓词是否通过。

- **`战利品谓词`** 战利品谓词是应用于战利品池、战利品项、战利品修饰的条件组，当用于战利品池时，谓词的通过与否决定该池是否参与抽取；当应用于战利品项时，谓词的通过与否影响该战利品项的掉落；当应用于战利品修饰时，谓词的通过与否影响该战利品修饰的执行。

- **`战利品修饰器`** 战利品修饰是应用于战利品池或战利品项的函数，当用于战利品池时，整个战利品池的战利品项都会尝试应用战利品修饰，当用于战利品项时，该项掉落时会尝试应用战利品修饰。

<!-- ## 认识战利品表

- **`战利品表`** 每个游戏事物如方块，有且仅有1个战利品表，战利品表内描述该战利品表的类型，修饰符，战利品池，随机序列，参见[minecraft-wiki/战利品表#基本格式](https://zh.minecraft.wiki/w/%E6%88%98%E5%88%A9%E5%93%81%E8%A1%A8#%E5%9F%BA%E6%9C%AC%E6%A0%BC%E5%BC%8F)

```json
{
    "type": "", // 描述战利品类型 事实上是战利品上下文的参数集类型
    "functions":[], // 可选项 对于该战利品表应用的物品修饰器
    "pools":[], // 可选项 其中包含1个或多个战利品池 每个池子都会独立抽取战利品
    "random_sequence":"" // 可选项 随机序列，
}
``` -->

## 步骤导图

::: stepper
@tab 战利品表类型
[方块类型战利品表](Block.md)

[实体类型战利品表](Entity.md)

[钓鱼类型战利品表](Fish.md)

[礼物类型战利品表](Gift.md)

[箱子类型战利品表](Chest.md)
@tab 战利品池
根据你的需求选择替换、添加战利品池。
@tab [可选]战利品谓词
根据你的需要决定
@tab [可选]战利品修饰器
根据你的需要决定
:::

## 战利品表类型

## 战利品池

- 存放许多战利品项的数组，当触发战利品时将从战利品池按抽取次数抽取若干次战利品项。

### 抽取次数

- 决定尝试抽取多少次战利品项，使用1个语句设置即可。

- 语句：pool.rolls = 数字提供器; 请查看[数字提供器](../MiscellaneousKnowledge/NumberProvider.md)

- 语句：pool.setBinomialRolls(重复次数, 成功概率); 设置符合二项分布的抽取次数

- 语句：pool.setUniformRolls(最小值, 最大值); 设置抽取次数区间

### 战利品项

- 作为战利品池的元素而存在。

#### 添加物品类型战利品项

- 向战利品池中添加物品类型的战利品项。

- 语句：pool.addItem(args); 具有3个方法重载。

::: code-group

```js [简单]
// 向战利品池中添加一个物品类型战利品
ServerEvents.entityLootTables(event => {
    event.modifyEntity('minecraft:husk', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:golden_apple')
        })
    })
})
```

```js [权重]
// 向战利品池中添加一个物品类型战利品并设置权重
ServerEvents.entityLootTables(event => {
    event.modifyEntity('minecraft:husk', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:golden_apple', 5)
        })
    })
})
```

```js [物品个数]
// 向战利品池中添加一个物品类型战利品并设置权重与个数，个数由数字提供器决定
ServerEvents.entityLootTables(event => {
    event.modifyEntity('minecraft:husk', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:golden_apple', 5)
        })
    })
})
```

:::

#### 添加空类型战利品项

- 空的战利品项，抽到它什么都不会掉。

- 语句：pool.addEmpty(权重);

- 示例：尸壳死亡时在该战利品池中将有5的权重什么都不掉。

```js
ServerEvents.entityLootTables(event => {
    event.modifyEntity('minecraft:husk', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:golden_apple', 5)
            pool.addEmpty(5)
        })
    })
})
```

#### 添加标签类型战利品项

- 创建一个物品标签类型战利品项，将物品标签的全部或单个物品作为战利品。

- 语句：pool.addTag(标签, 是否从中抽取1个物品); 如果为否，则生成标签中全部物品作为战利品。

:::: warning **注意**
::: justify
从标签中抽取物品为true时无法应用战利品修饰，原因：漏洞。目前该漏洞仍在 1.21.1 和 24w33a 中，见[漏洞追踪](https://bugs.mojang.com/browse/MC-212671)
:::
::::

- 示例：尸壳死亡掉落1个猪灵喜爱的物品。

```js
ServerEvents.entityLootTables(event => {
    event.modifyEntity('minecraft:husk', loot => {
        loot.addPool(pool => {
            pool.addTag('minecraft:piglin_loved', true)
            
        })
    })
})
```
