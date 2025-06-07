---
title: '@Invoker'
---

# @Invoker

`@Invoker` 注解用于让你能够调用目标类中的私有（private）或保护（protected）方法。

## 用法

与 `@Accessor` 类似，`@Invoker` 注解在一个抽象方法上，Mixin 会在编译时自动实现它，使其调用目标方法。

**示例:**

目标类：
```java
public class Minecraft {
    private void doRender() {
        // ...
    }
}
```

Mixin:
```java
@Mixin(Minecraft.class)
public interface MinecraftInvoker {
    @Invoker("doRender")
    void callDoRender();
}
```
现在，你可以将 `Minecraft` 对象转换为 `MinecraftInvoker` 接口，并调用 `callDoRender()` 方法，这实际上会调用原始的 `doRender()` 方法。

## `value` 属性

`value` 属性是可选的。如果你不指定 `value`，Mixin 会尝试根据你声明的方法名来匹配目标方法（例如，`callDoRender` -> `doRender`，`invokeDoRender` -> `doRender`）。如果你的方法名和目标方法名不匹配，或者有歧义，你需要明确指定 `value`。

**示例:**
```java
// 目标方法是 "renderInternal"，但我们想让调用器名为 "render"
@Invoker("renderInternal")
void render();
```

## 静态方法

`@Invoker` 也可以用于调用静态方法。你只需要确保你的调用器方法也是 `static` 的。

目标类：
```java
public class Util {
    private static void helper() {
        // ...
    }
}
```

Mixin:
```java
@Mixin(Util.class)
public interface UtilInvoker {
    @Invoker("helper")
    static void callHelper();
}
```

## 与 `@Shadow` 的区别

- `@Shadow` 是在你自己的 `Mixin` 代码中 *内部* 使用，用来访问目标类成员。
- `@Invoker` (和 `@Accessor`) 通常用于创建公共接口，让 *外部* 代码可以访问目标类的私有成员。它们通过接口转换的方式，提供了一种更清晰的 API。 