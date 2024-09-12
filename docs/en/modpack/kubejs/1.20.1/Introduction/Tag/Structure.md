# 结构标签

## 前言

- 原版的结构标签应用例：eye_of_ender_located 末影之眼指向最近的拥有此标签的结构。

- 原版结构标签列表：[minecraft-wiki/标签#结构](https://zh.minecraft.wiki/w/%E6%A0%87%E7%AD%BE#%E7%BB%93%E6%9E%84)

## 添加结构标签

- 事件：ServerEvents.tags('minecraft:structure', event=>{});

- 语句：event.add(标签, 结构);

```js
ServerEvents.tags('minecraft:structure', event => {
    /**
     * 要删除标签的物品
     * @type {Special.Structure[]}
     */
    const tagStructures = [
        
    ]
    for (const structure of tagStructures) {
        event.add('kubejs:my_structure_tags', structure);
    }
})
```

## 删除结构标签

- 事件：ServerEvents.tags('minecraft:structure', event=>{});

- 语句：event.remove(标签, 结构);

```js
ServerEvents.tags('minecraft:structure', event => {
    /**
     * 要删除标签的物品
     * @type {Special.Structure[]}
     */
    const tagStructures = [
        
    ]
    for (const structure of tagStructures) {
        event.remove('kubejs:my_structure_tags', structure);
    }
})
```
