# AABB

## 介绍

AABB 全称 axis-aligned bounding box，译为轴对齐包围盒。
[包围盒](https://zh.wikipedia.org/wiki/%E5%8C%85%E5%9B%B4%E4%BD%93)就是将物体组合完全包容起来的一个封闭空间。
轴对齐就是指这个空间的边与轴（X、Y、Z）对齐，即为一个立方体。

在 MC 中，也可以粗略地看作**碰撞箱**。

### 构造

原版所有碰撞检测，包含检测都用到了 AABB，所以先从构造开始介绍。

::: code-group

```js [of]
/*
你可以直接使用 new 来构造一个 AABB
x1, y1, z1为立方体一个顶点的坐标；x2, y2, z2为立方体另一个顶点的坐标
这两个顶点在立方体的对角线上
类似地，start和end是一个长度为3的向量，作用与上面一样，都是指两个顶点的坐标
*/
AABB.of(double x1, double y1, double z1,double x2, double y2, double z2);    // KJS
new $AABB(double x1, double y1, double z1, double x2, double y2, double z2); // 原版
new $AABB(Vec3 start, Vec3 end);

```

```js [ofBlock(s)]

// pos是一个BlockPos，基于此构造的AABB是这个坐标下完整方块的碰撞箱
AABB.ofBlock(BlockPos pos); // KJS
new $AABB(BlockPos pos);    // 原版

// KJS还提供了一个用于构造两个方块间AABB的方法
AABB.ofBlocks(BlockPos startPos, BlockPos endPos);
// 等效于原版的:
$AABB.encapsulatingFullBlocks(BlockPos startPos, BlockPos endPos);

```

```js [ofSize]

// 构造指定边长，指定中心的AABB
AABB.ofSize(Vec3 center, double xSize, double ySize, double zSize);
// 等效于下面
new AABB(
    center.x - xSize / 2.0,
    center.y - ySize / 2.0,
    center.z - zSize / 2.0,
    center.x + xSize / 2.0,
    center.y + ySize / 2.0,
    center.z + zSize / 2.0
);

```

:::

此外，由于 KJS 的 Wrapper，你可以直接使用`BlockPos`和数组代替 AABB。

```js
[] -> AABB.EMPTY; // 即AABB.of(0, 0, 0, 0, 0, 0)
[2, 3, 4] -> AABB.ofSize(0, 2, 3, 4);
[2, 3, 4, 6, 7, 8] -> AABB.of(2, 3, 4, 6, 7, 8);
```

## 变化

通常来说，AABB 可能是需要随时变动的，这里介绍几种变化方法。

::: code-group

```js [收缩]
/*
(不常用，不作介绍)
返回缩小指定量的AABB，正值使最大值减小，负值使最小值增大。
如果收缩量大于边长，则边将被包裹。
*/
AABB.contract(double x, double y, double z);
```

```js [扩展]
/*
返回扩展指定量的AABB。正值使最大值增大，负值使最小值减小。
返回的AABB永远大于等于原AABB。
简单地说就是向各个轴增加长度。
*/
AABB.expandTowards(double x, double y, double z);

// 例子：
let aabb = new AABB(0, 0, 0, 1, 1, 1);
aabb.expandTowards(2, 2, 2) -> [0, 0, 0, 3, 3, 3]
aabb.expandTowards(-2, -2, -2) -> [-2, -2, -2, 3, 3, 3]
aabb.expandTowards(0, 1, -1) -> [0, 0, -1, 3, 2, 3]
```

```js [扩大]
/*
返回扩大指定量的AABB，负值会使AABB缩小。
由于最小值和最大值都发生了变化，边长将增加指定量的2倍。
如果扩大的值大于边长，则边将被包裹。
简单地说就是以中心使各个轴的长度增加/减小，类似于上一个expandTowards。
*/
AABB.inflate(double x, double y, double z);
AABB.inflate(double value); // 等效于AABB.inflate(value, value, value);

AABB.deflate(double x, double y, double z); // 等效于AABB.inflate(-value, -value, -value);
AABB.deflate(double value); // 等效于AABB.inflate(-value);

// 例子：
new AABB(0, 0, 0, 1, 1, 1).inflate(2, 2, 2) -> [-2, -2, -2, 3, 3, 3]
new AABB(0, 0, 0, 6, 6, 6).inflate(-2, -2, -2) -> [2, 2, 2, 4, 4, 4]
```

```js [移动]
// 移动该AABB
AABB.move(double x, double y, double z);
AABB.move(Vec3 vec3);
```

```js [其它]
// 取AABB的边长
AABB.getSize(); // 三边边长平均数
AABB.getXSize();
AABB.getYSize();
AABB.getZSize();

// 取AABB的中心点
AABB.getCenter();

// 取两个AABB相交的部分
AABB.intersect(AABB other);
// 判断两AABB是否相交
AABB.intersects(AABB other);
AABB.intersects(double x1, double y1, double z1, double x2, double y2, double z2);
AABB.intersects(Vec3 min, Vec3 max);

// 以两个AABB的顶点作为新的AABB
AABB.minmax(AABB other);

// 判断一个坐标是否包含于AABB中
AABB.contains(Vec3 vec);
AABB.contains(double x, double y, double z);
```

:::

## 其它用法

这里介绍几种常用的关于 AABB 的用法。

::: code-group

```js [取范围内实体]
/*
第一个参数为排除的实体，返回的实体中不会包含它，可以为null。
第二个参数为AABB，也就是会返回这个范围内的实体。
第三个参数为谓词，用于选定指定的实体，可以为null。
*/
Level.getEntities(Entity entity, AABB aabb, Predicate predicate); // 原版
Level.getEntities(Entity entity, AABB aabb); // 原版
// 类似于原版，但没有谓词匹配和实体
Level.getEntitiesWithin(AABB aabb); // KJS
```

:::
