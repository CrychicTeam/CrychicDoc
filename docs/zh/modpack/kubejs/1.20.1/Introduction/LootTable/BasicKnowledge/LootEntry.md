# 抽取项

- 抽取项存在于每个随机池的抽取项列表中。

## 抽取项类型

|   抽取项类型    |   作用    |   语句    |
|:------------:|:---------:|:---------:|
|   选择    |   从中掉落第一个满足条件的战利品   |   -   |
|   动态    |   用于潜影盒与纹饰陶罐   |   -   |
|   空    |   什么都不掉的战利品   |   addEmpty()   |
|   物品    |   掉落一个物品   |   addItem()   |
|   组    |   掉落一组物品   |   -   |
|   战利品表    |   从另一个战利品表决定掉落什么   |   -   |
|   序列    |   按依次掉落，直到某一项谓词不通过   |   -   |
|   物品标签    |   掉落标签中1个或全部物品   |   -   |

## 添加抽取项

- 在addPool(pool=>{})中使用;

- 示例：

```js
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:diamond')// [!code ++]
        })
    })
})

```
