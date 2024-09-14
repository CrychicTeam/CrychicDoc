# 实体交互事件

## 前言

**`回调函数`** 进阶知识，不了解不影响使用事件，请查看（一个新挖的坑，没填）。

## 示例

- 语句：ItemEvents.entityInteracted(物品id，回调);

- 语句：ItemEvents.entityInteracted(回调);

::: code-group

```js [指定物品]
ItemEvents.entityInteracted('minecraft:stick', event => {

});
```

```js [不指定]
ItemEvents.entityInteracted('minecraft:stick', event => {

});
```

:::
