---
authors:
  - Gu-meng
  - Qi-Month
editor: Gu-meng
---
# 关于方块
此处介绍其他关于方块的函数

## 放置方块(怎么又是你)

根据 [scene_world_function.md](../Internal/SceneWorldFunction) 我们可知放置方块有两种函数

```js
setBlocks(arg0: Internal.Selection_, arg1: boolean_, arg2: Internal.BlockState_): void_;
setBlocks(arg0: Internal.Selection_, arg1: Internal.BlockState_): void_;
setBlocks(arg0: Internal.Selection_, arg1: Internal.BlockState_, arg2: boolean_): void_;

setBlock(arg0: BlockPos_, arg1: Internal.BlockState_, arg2: boolean_): void_;
```

**setBlocks** 已经于 [Ponder](../Ponder.md) 介绍过, 此处不多赘述, 此处着重介绍 **setBlock**

我们可以发现 **setBlocks** 与 **setBlock** 的第一个参数种类是不同的

其差别在于 **setBlocks** 可以使用 [3, 1, 1, 1, 1, 3] 来一次性放置大量方块, **setBlock** 一次只能放置一个

```js
// 在 [2, 1, 2] 放置铁块方块, 若该位置原本有方块, 则破坏该原本方块
scene.world.setBlock([2, 1, 2], "minecraft:iron_block", true);
```

## 破坏方块

首先来看范例

```js
// 破坏在 [2, 1, 2] 的方块
scene.world.destroyBlock([2, 1, 2]);
```
若是该位置无方块, 则不显示破坏方块时的粒子效果

> 以下代码节录自 [scene_world_function.md](../Internal/SceneWorldFunction)

```js
destroyBlock(arg0: BlockPos_): void_;
```

我们可以看到第一个参数种类与 setBlock 相同, 即`只能填入单格, 不能范围破坏`

## 替换方块

首先来看范例

```js
// 将 [2, 1, 2] 的方块替换为铁块方块, 并显示破坏方块时的粒子效果
scene.world.replaceBlocks([2, 1, 2], "minecraft:iron_block", true);

// 将以 [2, 1, 3] 及 [3, 1, 3] 为对角组成的矩形区域内的方块全部替换为铁块方块, 并不显示破坏方块时的粒子效果
scene.world.replaceBlocks([2, 1, 3, 3, 1, 3], "minecraft:iron_block", false);
```
若是该位置无方块, 则不替换方块, 也不显示破坏方块时的粒子效果

> 以下代码节录自 [scene_world_function.md](../Internal/SceneWorldFunction)

```js
replaceBlocks(arg0: Internal.Selection_, arg1: Internal.BlockState_, arg2: boolean_): void_;
```

我们可以看到第一个参数种类与 setBlocks 相同, 即`可以范围替换`

## 设定方块状态

首先来看范例

```js
scene.world.setBlocks([2, 1, 2], 'create:cogwheel', true);

// 将 [2, 1, 2] 的方块 设定 axis 为 x, waterlogged 为 true, 并不显示破坏方块时的粒子效果
scene.world.modifyBlock([2, 1, 2], state => state.with("axis", "x").with("waterlogged", "true"), false);

// 将 [2, 1, 2] 的方块 设定为橡木活板门, open 为 true, 并显示破坏方块时的粒子效果
scene.world.modifyBlock([2, 1, 2], () => Block.id("minecraft:oak_trapdoor").with("type", "top"), true);

// 将以 [2, 1, 3] 及 [3, 1, 3] 为对角组成的矩形区域内的方块 设定为橡木活板门, open 为 true, 并显示破坏方块时的粒子效果
scene.world.modifyBlocks([2, 1, 3, 3, 1, 3], () => Block.id("minecraft:oak_trapdoor").with("type", "top"), true);
```

> 以下代码节录自 [scene_world_function.md](../Internal/SceneWorldFunction)

```js
modifyBlocks(arg0: Internal.Selection_, arg1: Internal.BlockStateFunction_, arg2: boolean_): void_;
modifyBlocks(arg0: Internal.Selection_, arg1: boolean_, arg2: Internal.BlockStateFunction_): void_;
modifyBlocks(arg0: Internal.Selection_, arg1: Internal.BlockStateFunction_): void_;

modifyBlock(arg0: BlockPos_, arg1: Internal.BlockStateFunction_, arg2: boolean_): void_;
```

## 设定方块NBT

首先来看范例

```js
scene.world.setBlocks([2, 1, 2], 'create:cogwheel', true);

// 将 [2, 1, 2] 的方块的 NBT 中的 Speed 设定为 16
scene.world.modifyBlockEntityNBT([2, 1, 2], nbt => { nbt.Speed = 16 });

// 同上, 目前第二个参数的用途不明
scene.world.modifyBlockEntityNBT([2, 1, 2], true, nbt => { nbt.Speed = 16 });
```

> 以下代码节录自 [scene_world_function.md](../Internal/SceneWorldFunction)

```js
modifyBlockEntityNBT(arg0: Internal.Selection_, arg1: boolean_, arg2: Internal.Consumer_<Internal.CompoundTag>): void_;
modifyBlockEntityNBT(arg0: Internal.Selection_, arg1: Internal.Consumer_<Internal.CompoundTag>): void_;
```

## 其余待做函数

等待大佬编辑

> 以下代码节录自 [scene_world_function.md](../Internal/SceneWorldFunction)

```js
modifyBlockEntity<T extends Internal.BlockEntity>(arg0: BlockPos_, arg1: T, arg2: Internal.Consumer_<T>): void_;
restoreBlocks(arg0: Internal.Selection_): void_;
glueBlockOnto(arg0: BlockPos_, arg1: Internal.Direction_, arg2: Internal.ElementLink_<Internal.WorldSectionElement>): void_;
cycleBlockProperty(arg0: BlockPos_, arg1: Internal.Property_<any>): void_;
incrementBlockBreakingProgress(arg0: BlockPos_): void_;
```
