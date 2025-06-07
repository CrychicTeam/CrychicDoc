---
title: '@Intrinsic'
---

# @Intrinsic

`@Intrinsic` 是一个高级注解，它告诉 Mixin 一个被注解的方法将由目标类 *固有地* 提供实现。这允许你将一个 `Mixin` 的行为与目标类的现有行为连接起来，而不需要使用 `@Shadow` 或 `@Overwrite`。

## 用法

`@Intrinsic` 通常用于这样的场景：你有一个 `Mixin`，它在某些目标类中需要覆盖一个方法，但在其他目标类中，该方法已经存在并且行为是你想要的。

**示例:**

假设你有一个 `Mixin` 应用于 `ClassA` 和 `ClassB`。
- `ClassA` 有一个方法 `doSomething()`。
- `ClassB` 没有 `doSomething()` 方法。

你的 `Mixin` 想要确保 `doSomething()` 存在。

```java
@Mixin({ClassA.class, ClassB.class})
public abstract class MyMixin {

    @Intrinsic
    public void doSomething() {
        // In ClassA, Mixin will see that doSomething() already exists
        // and will not apply this Mixin method. It assumes the existing
        // method is the "intrinsic" implementation.

        // In ClassB, doSomething() does not exist, so this method *will* be
        // applied, effectively adding the method to ClassB.
        System.out.println("Default implementation of doSomething()");
    }
}
```

## 与 `@Overwrite` 的区别

- `@Overwrite` 会**总是**尝试覆盖目标方法。如果目标方法不存在，或者签名不匹配，就会出错。如果多个 `Mixin` 覆盖同一个方法，也会出错。
- `@Intrinsic` 是一种"如果不存在，则添加"的行为。它首先检查目标类是否已经有了这个方法。如果有，`@Intrinsic` 方法就不会被应用。如果没有，它就会被作为新方法添加。

## `displace` 属性

`@Intrinsic` 有一个布尔属性 `displace`。
- `displace = false` (默认): 如果目标方法存在，则不应用 `Mixin` 方法。
- `displace = true`: 如果目标方法存在，`Mixin` 方法会**替换**它，类似于 `@Overwrite`。

```java
@Intrinsic(displace = true)
public void doSomething() {
    // This will now overwrite doSomething() if it exists,
    // or add it if it doesn't.
}
```
`@Intrinsic(displace = true)` 的行为实际上非常接近 `@Overwrite`，但它在某些复杂的 `Mixin` 继承场景中能解决一些 `@Overwrite` 无法处理的冲突。

对于大多数用例，`@Intrinsic` 是一个不常用的高级功能。 