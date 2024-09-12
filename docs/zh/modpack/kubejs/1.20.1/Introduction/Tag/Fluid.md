# 流体标签

## 前言

- 原版的流体标签应用例：minecraft:lava 标签中的流体拥有熔岩的特性，文字较多故无法写明，请查看下方链接。

- 原版流体标签应用列表：[minecraft-wiki/标签#流体](https://zh.minecraft.wiki/w/%E6%A0%87%E7%AD%BE#%E6%B5%81%E4%BD%93)

## 添加流体标签

- 事件：ServerEvents.tags('minecraft:fluid', event=>{});

- 语句：event.add(标签, 流体);

```js
ServerEvents.tags('minecraft:fluid', event => {
    /**
     * 要加标签的流体
     * @type {Special.Fluid[]}
     */
    const tagFluids = [

    ]
    for (const fluid of tagFluids) {
        event.add('kubejs:my_fluid_tags', fluid);
    }
})
```

## 删除流体标签

- 事件：ServerEvents.tags('minecraft:fluid', event=>{});

- 语句：event.remove(标签, 流体);

```js
ServerEvents.tags('minecraft:fluid', event => {
    /**
     * 要加标签的流体
     * @type {Special.Fluid[]}
     */
    const tagFluids = [

    ]
    for (const fluid of tagFluids) {
        event.remove('kubejs:my_fluid_tags', fluid);
    }
})
