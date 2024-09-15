# 8 流体注册

***

## 一、流体注册

### 1、事件监听

通过向`StartupEvents.registry`事件传入不同的参数，其可用于在启动脚本中注册游戏中的新元素，如方块、物品、声音、药效等，在这里，我们传入`"fluid"`来进行与流体有关的修改：

```js
StartupEvents.registry("fluid",event=>{
    // code here
})
```

### 2、事件方法

| 方法                  | 描述     | 返回值       |
| --------------------- | -------- | ------------ |
| create(字符串 流体ID) | 注册流体 | FluidBuilder |

### 3、FluidBuilder

| 方法                                | 描述                                  | 备注                         |
| ----------------------------------- | ------------------------------------- | ---------------------------- |
| builtinTextures()                   | 使用内置贴图                          | -                            |
| flowingTexture(ResourceLocation id) | 设置流动贴图                          | -                            |
| bucketColor(Color 颜色)             | 设置流体桶的颜色                      | 可使用16进制，形如`0x24acf2` |
| noBucket()                          | 设置默认不生成桶                      | -                            |
| luminosity(整形 发光度)             | 设置流体亮度                          | 默认值0                      |
| gaseous()                           | 将当前流体转为气体                    | -                            |
| stillTexture(ResourceLocation id)   | 设置静止贴图                          | -                            |
| viscosity(整形 粘稠度)              | 设置粘稠度                            | 默认值为1000                 |
| displayName(字符串 名称)            | 设置流体名称                          | -                            |
| createAttributes()                  | 创建并返回ArchitecturyFluidAttributes | 食用教程见下                 |
| thickTexture(Color 颜色)            | 设置流体颜色                          | 可使用16进制，形如`0x24acf2` |
| rarity(Rarity 稀有度)               | 设置流体稀有度                        | -                            |
| color(Color 颜色)                   | 设置流体颜色（厚）                    | 可使用16进制，形如`0x24acf2` |
| density(整形 密度)                  | 设置流体密度                          | 默认值1000                   |
| noBlock()                           | 设置默认不注册流体方块                | -                            |
| thinTexture(Color 颜色)             | 设置流体颜色（薄）                    | 可使用16进制，形如`0x24acf2` |
| temperature(整形 温度)              | 设置流体温度                          | 默认值300                    |

### 4、ArchitecturyFluidAttributes

通过ArchitecturyFluidAttributes，你可以快捷修改流体属性

| 方法                                | 描述                 | 备注                              | 默认值        |
| ----------------------------------- | -------------------- | --------------------------------- | ------------- |
| dropOff(整形 衰减值)                | 设置流体衰减值       | 根据与源头方块距离降低流体level值 | 1             |
| tickDelay(整形 延迟刻数)            | 设置扩散延迟         | -                                 | 5             |
| explosionResistance(float 爆炸抗性) | 设置流体爆炸抗性     | -                                 | 100.0F        |
| color(Color 颜色)                   | 设置流体颜色         | 可使用16进制，形如`0x24acf2`      | 0xffffff      |
| luminosity(整形 发光度)             | 设置流体亮度         | -                                 | 0             |
| density(整形 密度)                  | 设置流体密度         | -                                 | 1000          |
| temperature(整形 温度)              | 设置流体温度         | -                                 | 300           |
| viscosity(整形 粘稠度)              | 设置粘稠度           | -                                 | 1000          |
| lighterThanAir(布尔值 是否比空气轻) | 设置流体质量         | 为true时流体将不会向下流动        | false         |
| rarity(Rarity 稀有度)               | 设置流体稀有度       | -                                 | Rarity.COMMON |
| fillSound(fillSound: SoundEvent)    | 设置流体填充容器声音 | -                                 | -             |
| emptySound(emptySound: SoundEvent)  | 设置容器倒空流体声音 | -                                 | -             |

## 二、示例

```js
StartupEvents.registry("fluid",event=>{
    let example_fluid = event.create("example_fluid").thinTexture(0xcc3e44).thickTexture(0xcc3e44).temperature(2000).bucketColor(0xcc3e44).displayName("Wudji");

    let exampleAttributes = example_fluid.createAttributes();
    
    exampleAttributes.dropOff(2);
    exampleAttributes.tickDelay(20);

    example_fluid.attributes = exampleAttributes;
})
```