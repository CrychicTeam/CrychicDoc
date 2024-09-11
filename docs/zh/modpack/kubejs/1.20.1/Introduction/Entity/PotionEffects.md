# 药水效果

## 前言

:::: warning **注意**
::: justify
只有“有生命的实体”才能添加药水效果，并且这个实体不会免疫你要添加的药水效果。

矿车，不是有生命的实体；盔甲架尽管是有生命的实体，但其是特殊的，免疫所有药水效果。

有生命的实体：继承LivingEntity类，不懂没关系，不影响下边的使用。
:::
::::

## 添加到实体

- 语句：entity.potionEffects.add(args); 具有4个方法重载。

- 例：当进行实体交互，玩家获得夜视效果。

::: code-group

```js [示例]
// 使用实体交互事件演示
ItemEvents.entityInteracted(event => {
    const { entity, target, hand, server, level } = event;
    if (hand !== 'main_hand') return;
    /**
     * 药水id，持续时间，等级，是不是环境效果，有没有粒子效果
     */
    entity.potionEffects.add('minecraft:night_vision', 200, 0, false, true);
})

```

```js [示例]
// 使用实体交互事件演示
ItemEvents.entityInteracted(event => {
    const { entity, target, hand, server, level } = event;
    if (hand !== 'main_hand') return;
    /**
     * 药水id，持续时间，等级 - 默认不是环境效果，有粒子效果
     */
    entity.potionEffects.add('minecraft:night_vision', 200, 0);
})
```

```js [示例]
// 使用实体交互事件演示
ItemEvents.entityInteracted(event => {
    const { entity, target, hand, server, level } = event;
    if (hand !== 'main_hand') return;
    /**
     * 药水id，持续时间 -默认0级（游戏内1级），不是环境效果，有粒子效果
     */
    entity.potionEffects.add('minecraft:night_vision', 200);
})
```

```js [示例]
// 使用实体交互事件演示
ItemEvents.entityInteracted(event => {
    const { entity, target, hand, server, level } = event;
    if (hand !== 'main_hand') return;
    /**
     * 药水id - 默认持续10秒，0级（游戏内1级），不是环境效果，有粒子效果
     */
    entity.potionEffects.add('minecraft:night_vision');
})
```

:::

## 实体是否拥有

- 语句：livingEntity.hasEffect(药水效果注册名);

- 例：尸壳是否有夜视。

```js
ItemEvents.entityInteracted(event => {
    const { entity, target, hand, level, server } = event;
    if (hand !== 'main_hand' || target.type !== 'minecraft:husk') return;
    
    /**@type {Internal.LivingEntity} */
    const livingEntity = target;

    const boolean = livingEntity.hasEffect('minecraft:night_vision');

    console.log(`尸壳是否具有夜视药水效果：${boolean}`);
});
```
