---
authors:
  - Gu-meng
editor: Gu-meng
---
# 物品配方合成返回物品
这一章理论来说应该在物品属性里，但是可修改的物品属性太多了，所以把这一个单独拿出来

在篇章[配方合成进阶](./AdvancedRecipe.md)中提到过，合成返回物品

在原版中本身就有类似的操作，比如牛奶桶在参与合成时返回桶，还有岩浆在成为输入燃料时，也会返回桶

他们的逻辑并不是在*配方合成进阶*中提到的`replaceIngredient`(替换材料)，而是这个物品的一个属性`craftingRemainder`来实现的

## 示例
```js
Item.of('minecraft:bedrock').item.craftingRemainder = Item.of('minecraft:water_bucket').item;
```
(这里写在server里了，虽然但是不建议这么做)

在上面代码中我们设置了物品的`craftingRemainder`(直译过来是制作剩余)属性

将**基岩**在参与合成时会返回**水桶**

这个剩余材料不仅仅在合成，如果输入燃料为有**craftingRemainder**属性的物品，则会返回这个物品，可以参考原版的熔岩桶在作为燃料时返回桶，这里我们也可以写类似的操作

```js
Item.of('minecraft:bedrock').item.burnTime = 20000
```
这里我们设置了物品为燃料且可以燃烧2wtick

当我们将物品作为燃料放入熔炉时，当燃料被使用，在基岩的位置就会返回一个水桶

如果你希望做到**合成保留物品**我们只需要将返回的物品设置为物品本身就可以了

### 标准示例(该示例在startup里)
```js
ItemEvents.modification(event=>{
    event.modify('minecraft:bedrock',item=>{
        item.craftingRemainder = Item.of('minecraft:water_bucket').item;
        item.burnTime = 20000;
    });
})
```
这样写会更正规一点，建议使用这种

## 注意事项
优点：
* 这样写会很通用，在大部分的合成配方中都是可以的，比如机械动力的动力合成就会返回这个制作后剩余材料

缺点：
* 太过通用导致无法精准控制在什么时候返回
