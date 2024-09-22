---
authors: ['Wudji']
---

# 6.1 作物方块和种子

***

## 一、基础信息

在注册方块的时候，传入参数`"crop"`可以将方块类型指定为作物方块。

默认情况下，KubeJS会同时注册对应种子物品，其ID为`作物方块ID` + `_seed`。

`BlockBuilder`下的`noItem()`方法可以使KubeJS不再注册种子物品

```js
StartupEvents.registry('block', event => {
	// 自动注册对应种子，ID为example_crop_seed
    event.create("example_crop","crop");
})
```

## 二、CropBlockBuilder

| 方法                                        | 描述             | 对应属性默认值 |
| ------------------------------------------- | ---------------- | -------------- |
| age(int 生长时间)                           | 设置生长时间     | 7              |
| crop(Object 产出物品)                       | 额外添加掉落物品 | -              |
| crop(Object 产出物品,double 概率)           | 额外添加掉落物品 | -              |
| dropSeed(布尔值 是否掉落种子)               | 设置掉落种子     | true           |
| bonemeal(ToIntFunction 使用骨粉回调函数)    | 设置使用骨粉事件 | null           |
| survive(SurviveCallback surviveCallback)    | 设置生长条件事件 | null           |
| growTick(ToDoubleFunction 生长速度回调函数) | 随机刻选中事件   | null           |

值得注意的是，`BlockBuilder`中的`randomTick(RandomTickCallbackJS 随机刻回调函数)`在`CropBlockBuilder`中被override了，你需要使用growTick方法替代它。

## 三、示例

```js
StartupEvents.registry('block', event => {
    // ^(*￣(oo)￣)^ 作物
    event.create("pig_plant","crop")
        .age(3)// 从 0 开始
        .crop(Item.of("minecraft:porkchop").withCount(2),0.3)
        .crop(Item.of("minecraft:porkchop"))
        .texture("0","minecraft:block/nether_wart_stage0") // 阶段0纹理
        .texture("1","minecraft:block/nether_wart_stage1") // 阶段1纹理
        .texture("2","minecraft:block/nether_wart_stage1") // 阶段2纹理
        .texture("3","minecraft:block/nether_wart_stage2") // 阶段3纹理
        .bonemeal(cb =>{
            // 使用骨粉时生成一只猪
            let b = cb.block;
            let entity = cb.getLevel().createEntity("minecraft:pig")
            entity.setPosition(b.getX(),b.getY() + 1,b.getZ())
            entity.spawn()
            return 0;
        })
        .item(s =>{
            // 设置种子纹理
            s.texture("minecraft:item/spawn_egg");
        })

    // 末影人(小黑子)作物
    event.create("enderman_plant","crop")
        .survive((blockState,level,blockPos) =>{
            // 注1：这里的第一个形参和第三个形参指的都是作物方块的对应属性，而不是被种植的方块。
            // 注2：.survive的回调不止是决定作物是否生长，它更决定了作物是否能存活。
            //  在玩家使用种子种植作物，以及随机刻触发作物生长前都会触发一次survive的回调。
            //  如果返回false，则作物无法被玩家种植，或者作物会被破坏并掉落它的种子（类似于在原版没有光照的环境下种植作物）。（TRAYER）
            
            // 检测天气状态（非雨天）和被种植的方块ID。
            let id = level.getBlockState(blockPos.below()).getBlock().getId()
            return (!level.isRaining()) && id == "minecraft:sand";

        }).growTick((blockState,level,blockPos) => {
            // 注：与.survive不同，.growTick的回调则决定了作物在一次随机刻触发的生长中会有多少概率生长。
            //  如果返回的数字大于0，则生长的概率=1 / (25 / 返回值 + 1)，如果返回数字小于等于0，则会使用原版的计算方式。（TRAYER）
            
            // 被随机刻选中时有1/2的概率生长。
            return 25;
        }).item(s =>{
            // 设置种子纹理
            s.texture("minecraft:item/spawn_egg");
        })
})
```