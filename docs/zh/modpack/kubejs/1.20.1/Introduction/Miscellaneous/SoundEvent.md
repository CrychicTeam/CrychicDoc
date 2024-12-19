---
progress: 50
state: unfinished
---
# SoundEvent

::: tip 提示
此处介绍Internal.SoundEvent的相关信息。
:::

## 类型定义

- SoundEvent是一个Minecraft自身的类，它表示声音资源。

```ts
class SoundEvent {
  getLocation(): ResourceLocation;
  writeToNetwork(arg0: Internal.FriendlyByteBuf_): void;
  static createFixedRangeEvent(arg0: ResourceLocation_, arg1: number): Internal.SoundEvent;
  getRange(arg0: number): number;
  static readFromNetwork(arg0: Internal.FriendlyByteBuf_): Internal.SoundEvent;
  static createVariableRangeEvent(arg0: ResourceLocation_): Internal.SoundEvent;
  static readonly DIRECT_CODEC: Internal.Codec<Internal.SoundEvent>;
  static readonly CODEC: Internal.Codec<Internal.Holder<Internal.SoundEvent>>;
}
```

::: center
这里省略了用不到的函数与属性
:::

## 常用方法

>[!WARNING] 注意
>此条目信息需要补充。

### getLocation

- getLocation(): ResourceLocation;
- 该方法返回当前SoundEvent实例的ResourceLocation。

### writeToNetwork

- writeToNetwork(arg0: Internal.FriendlyByteBuf_): void;
- 需要补充。

### getRange

- getRange(arg0: number): number;
- 输入音量大小（默认为1.0）返回范围。
- 计算方式如下：

```java
public float getRange(float pVolume) {
  if (this.newSystem) {
      return this.range;
  } else {
      return pVolume > 1.0F ? 16.0F * pVolume : 16.0F;
  }
}
```

::: center
小于等于1.0的音量大小会返回16的范围，大于1.0的音量大小返回16与音量大小的倍数
:::

### createFixedRangeEvent

- createFixedRangeEvent(arg0: [ResourceLocation_](../GlobalScope/Classes/ResourceLocation.md), arg1: number): Internal.SoundEvent;
- 该方法是一个static方法，需通过Java.loadClass获取SoundEvent类使用，用于创建具有固定播放范围的SoundEvent实例，第二个参数为播放范围，在原版中通常使用16。

### createVariableRangeEvent

- createVariableRangeEvent(arg0: [ResourceLocation_](../GlobalScope/Classes/ResourceLocation.md)): Internal.SoundEvent;
- 创建一个可以根据声音大小改变范围的SoundEvent实例。

### readFromNetwork

>[!WARNING] 注意
>此条目信息需要补充。

- readFromNetwork(arg0: Internal.FriendlyByteBuf_): Internal.SoundEvent;

## 实例创建

### 引用原版资源

- 可以通过[Utils.getSound()](../GlobalScope/Utils.md#getSound)函数来引用原版中存在的声音资源。

```js [KubeJS]
let sound = Utils.getSound("entity.player.levelup");
```

::: center
玩家升级音效
:::

### 创建新的实例

- 通过反射获取SoundEvent类。

```js [KubeJS]
let SoundEvent = Java.loadClass("net.minecraft.sounds.SoundEvent");
let sound = SoundEvent.createVariableRangeEvent("kubejs:demo_sound");
```

## 实际应用

::: warning 注意
此条目信息需要补充。
:::
