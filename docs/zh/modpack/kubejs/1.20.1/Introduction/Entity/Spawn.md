# 实体生成

## 前言

## 指定的生成

- 借聊天事件为例，生成尸壳。

```js
PlayerEvents.chat(event => {
    const { message, player, player: { block, block: { x, y, z, } }, server, level } = event;
    if (message !== 'test') return;
    // 新建一个尸壳实体
    const husk = level.createEntity('minecraft:husk');
    // 设置位置
    husk.setPosition(x, y, z);
    // 设置了显示名字
    husk.setCustomName(Component.of('僵尸测试员'));
    // 设置nbt
    husk.mergeNbt({ NoAI: true });
    // 生成 不调用此函数实体不生成
    husk.spawn();
})
```
