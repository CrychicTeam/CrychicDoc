# 物品堆 (ItemStack)

## 前言

- **`物品堆 (ItemStack)`** 游戏中最常见的对象，玩家物品栏中的每一格都是一个物品堆，物品堆包含了如下信息：
  物品 (Item)、数量 (Count)、标签/Nbt (Tags)

## 表示

可以使用如下方式来实例化一个物品堆：

::: code-group

```js [字符串]
// 字符串形式常用作参数等
"minecraft:stone"; // 1个石头
```

```js [对象]
Item.of("minecraft:stone"); // 1个石头
Item.of("minecraft:stone", "{test:114}"); // 带有Nbt({test:114})的1个石头
Item.of("minecraft:stone", 60, "{test:114}"); // 带有Nbt({test:114})的60个石头
```

:::

### 表示多个物品

::: code-group

```js [字符串]
// 字符串形式常用作参数等
"1x minecraft:stone"; // 等同于"minecraft:stone"
"64x minecraft:stone"; // 64个石头
```

```js [对象]
Item.of("minecraft:stone", 1); // 1个石头
Item.of("minecraft:stone", 60); // 60个石头
```

:::

### 表示带有 NBT 的物品

```js
// 带有Nbt({test:114})的1个石头
Item.of("minecraft:stone", "{test:666}");

// 带有Nbt({test:114})的60个石头
Item.of("minecraft:stone", 60, "{test:666}");
```

### 表示带附魔的物品

```js
Item.of("minecraft:stone").enchant("minecraft:looting", 1); // 带有1级抢夺的石头
```

### 表示带有自定义名称的物品

名称为[文本组件 (Components)](./Components.md)。

```js
// 名称是金色的石头
Item.of("minecraft:stone").withName(Component.gold("石头"));
```

### 表示带有自定义工具栏提示的物品

工具栏提示为[文本组件 (Components)](./Components.md)。

```js
// 带有金色的说明的石头
Item.of("minecraft:stone").withLore(Component.gold("说明"));
```

## 其它

以上的`Item#of`方法返回值都是`ItemStack`，这意味着接下来你可以继续[链式调用](https://blog.csdn.net/Glengaga/article/details/135189920)：

```js
// 一把叫“圣剑”的木棍，工具栏提示是“By C”，带有抢夺3和NBT。
Item.of("minecraft:stick")
  .withName("圣剑")
  .withLore("By C")
  .enchant("minecraft:looting", 3)
  .withNBT("{test2:115}");
```
