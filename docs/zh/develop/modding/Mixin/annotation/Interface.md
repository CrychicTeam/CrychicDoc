---
title: '@Interface'
---

# @Interface

`@Interface` 注解有两个主要用途：

1.  作为 `@Implements` 注解的一部分，声明要实现的接口和方法前缀。
2.  作为独立的注解，用于"软实现"一个接口。

## 在 `@Implements` 中使用

这是 `@Interface` 最常见的用法。它告诉 Mixin 目标类应该实现哪个接口，以及实现方法的 `prefix` 是什么。

```java
@Implements(@Interface(iface = ICustomData.class, prefix = "custom$"))
```
更多信息请参考 [`@Implements` 的文档](./Implements.md)。

## 软实现 (Soft Implementation)

你可以直接在 `Mixin` 类上使用 `@Interface` 注解。这被称为"软实现"。它声称目标类实现了指定的接口，但**不**强制你提供实现。

**示例:**
```java
@Mixin(Block.class)
@Interface(iface = IBreakable.class, prefix = "breakable$")
public class BlockMixin {
    // No methods prefixed with "breakable$" are required here
}
```

### 用途

软实现的主要用途是，当你知道目标类 *已经* 拥有符合接口规范的方法时，你可以用 `@Interface` 来形式化地建立这种关系，而无需重新实现这些方法。

例如，假设 `IBreakable` 接口有一个 `breakBlock()` 方法。如果 `Block` 类已经有一个 `breakBlock()` 方法（或者一个可以被重映射为 `breakBlock` 的方法），Mixin 就会将这个现有的方法视为接口实现，而不需要你在 `Mixin` 中提供一个 `breakable$breakBlock()` 方法。

### `remap` 属性

`@Interface` 注解有一个 `remap` 属性（默认为 `true`），它控制是否对接口中的方法名进行重映射。这在处理混淆环境时非常重要。 