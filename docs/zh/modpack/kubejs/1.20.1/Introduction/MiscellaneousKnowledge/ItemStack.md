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
"1x minecraft:stone"; // 等同于上一行
"64x minecraft:stone"; // 64个石头
```

```js [对象]
Item.of("minecraft:stone"); // 1个石头
Item.of("minecraft:stone", 1); // 等同于上一行
Item.of("minecraft:stone", 24); // 24个石头

Item.of("minecraft:stone", "{test:114}"); // 带有Nbt({test:114})的1个石头
Item.of("minecraft:stone", 60, "{test:114}"); // 带有Nbt({test:114})的60个石头
```

:::

以上的`Item#of`方法返回值都是`ItemStack`，这意味着接下来你可以继续[链式调用](https://blog.csdn.net/Glengaga/article/details/135189920)：

```js
Item.of("minecraft:stick").withName("圣剑"); // 一把叫“圣剑”的木棍
Item.of("minecraft:stick").withNBT("{test2:115}"); // 附加Nbt
// 一把叫“圣剑”的木棍，同时还有“By C”的Tooltip
Item.of("minecraft:stick").withName("圣剑").withLore("By C");
Item.of("minecraft:stick").enchant("minecraft:looting", 3); // 带有3级抢夺的木棍
```

对于`ItemStack#withName`和`ItemStack#withLore`两个方法的参数，
更高级的用法可以参见[文本组件 (Components)](./Components.md)
