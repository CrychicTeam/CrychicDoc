# 通用类型战利品表

::: tip 提示
这是一个未被原版使用的战利品表类型，但在KubeJS中可用于指定战利品表id后进行操作。
:::

## 操作战利品表

- 事件：ServerEvents.genericLootTables(event => \{\});

::: code-group

```js [KubeJS修改战利品表]
ServerEvents.genericLootTables(event => {
    // 修改战利品表event.modify(战利品表id, loot => {})
    event.modify('minecraft:blocks/grass_block', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:grass')// [!code ++]
        })
    })
})
```

```js [KubeJS覆盖战利品表]
ServerEvents.genericLootTables(event => {
    // 覆盖战利品表event.addGeneric(战利品表id, loot => {})
    event.addGeneric('minecraft:blocks/grass_block', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:grass')// [!code ++]
        })
    })
})
```

```js [KubeJS带有谓词与修饰器的]
ServerEvents.genericLootTables(event => {
    // 覆盖战利品表event.addGeneric(战利品表id, loot => {})
    event.addGeneric('minecraft:blocks/grass_block', loot => {
        loot.addPool(pool => {
            // 添加战利品
            pool.addItem('minecraft:grass')// [!code ++]
            // 为战利品添加有条件的物品修饰器
            pool.addItem('minecraft:grass').addConditionalFunction(c=>c.name(Component.green('测试的草')))// [!code ++]
            // 为战利品池添加有条件的物品修饰器
            pool.addConditionalFunction(c=>c.name(Component.green('测试的草')))// [!code ++]
        })
        // 为战利品表添加有条件的物品修饰器
        loot.addConditionalFunction(c=>c.name(Component.green('测试的草')))// [!code ++]
    })
})
```

::: warning 警告
该类型默认不检查所有谓词与修饰器的可用性，谓词与物品修饰器的可用性需根据实际战利品表类型判断。
:::

:::

## 可用谓词

- 该类型在原版没有使用，默认提供全部上下文参数，（有确切上下文参数时）全部谓词可用，但需根据实际战利品表类型来判断一些谓词的可用性，例如本例中操作方块类型战利品表，谓词可用性需参考[方块类型战利品表可用谓词](../Vanilla/Block.md#可用谓词)

## 可用物品修饰器

- 该类型在原版没有使用，默认提供全部上下文参数，（有确切上下文参数时）全部物品修饰器可用，但需根据实际战利品表类型来判断一些谓词的可用性，例如本例中操作方块类型战利品表，物品修饰器可用性需参考[方块类型战利品表可用物品修饰器](../Vanilla/Block.md#可用物品修饰器)
