---
authors: ['Wudji']
---

# 2.2 配方的修改和删除

***

## 1、配方的移除

| 例子                                                                                       | 用途解释                                                |
| ---------------------------------------------------------------------------------------- | --------------------------------------------------- |
| `event.remove({})`                                                                       | 删除所有配方                                              |
| `event.remove({id: '配方ID'})`                                                             | 移除指定配方ID的配方                                         |
| `event.remove({input: '#forge:dusts/redstone'})`                                         | 移除所有以带有#forge:dusts/redstone标签为输入物品的配方 ' '内也可以填物品ID |
| `event.remove({output: '#minecraft:wool'})`                                              | 移除所有以带有#minecraft:wool标签为输出物品的配方 ' '内也可以填物品ID       |
| `event.remove({mod: 'fabricexamplemod'})`                                                | 移除所有id为fabricexamplemod的mod添加的配方                    |
| `event.remove({type: 'minecraft:campfire_cooking'})`                                     | 移除以营火为合成方式的配方                                       |
| `event.remove({output: 'minecraft:cooked_chicken', type: 'minecraft:campfire_cooking'})` | (叠加不同修改逻辑的示例) 移除用营火烤鸡肉的配方                           |

## 2、配方的修改

```
  // 在所有无序配方中，将任何木板替换为minecraft:gold_nugget
  event.replaceInput({type: 'minecraft:crafting_shapeless'}, '#minecraft:planks', 'minecraft:gold_nugget')
  // {}内可以填写配方类别(和上述配方的移除写法一样)
  
  // 在所有配方中，将输出物品中的minecraft:stick替换为minecraft:oak_sapling
  event.replaceOutput({}, 'minecraft:stick', 'minecraft:oak_sapling')
```
