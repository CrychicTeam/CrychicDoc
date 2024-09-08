# 为实体添加药水效果

```js
// 反射 务必放在事件外边
const $MobEffectInstance = Java.loadClass('net.minecraft.world.effect.MobEffectInstance');
// 使用实体交互事件演示
ItemEvents.entityInteracted(event => {
    // 在本事件中 entity：事件中的玩家，target：交互的实体，hand：交互的手，主副手均会发生一次
    const { entity, target, hand, server, level } = event;
    if (hand !== 'main_hand') return;
    // 药水效果的属性
    let tickTime = 200; // 持续时间，单位：游戏刻
    let effectLevel = 0; // 药水效果等级，最低级从0开始
    let isAmbient = false; // 是否为环境效果，目前不知有什么用
    let isVisible = false; // 是否有粒子效果
    let showIcon = false;// 是否在屏幕上显示图标
    // 新建$MobEffectInstance实例
    let mobEffectInstance = new $MobEffectInstance('minecraft:night_vision', tickTime, effectLevel, isAmbient, isVisible, showIcon);
    // 添加到实体
    target.addEffect(mobEffectInstance);
})
```
