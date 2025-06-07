---
title: '@Overwrite'
---

# @Overwrite

`@Overwrite` 注解用于完全覆盖目标方法。使用 `@Overwrite` 的方法将会替换掉整个目标方法。

## 用法

`@Overwrite` 注解的方法必须和目标方法有完全相同的签名（方法名、返回类型、参数）。

**示例:**

目标代码：
```java
public class Example {
    public String getMessage() {
        return "Original Message";
    }
}
```

使用 `@Overwrite` 覆盖 `getMessage` 方法：
```java
@Mixin(Example.class)
public class ExampleMixin {
    /**
     * @author YourName
     * @reason A brief explanation of why you are overwriting.
     */
    @Overwrite
    public String getMessage() {
        return "Overwritten Message";
    }
}
```
现在，当代码调用 `Example` 实例的 `getMessage` 方法时，它将返回 `"Overwritten Message"`。

## Javadoc 注释

当使用 `@Overwrite` 时，强烈建议添加 Javadoc 注释，并包含 `@author` 和 `@reason` 标签。

- `@author`: 你的名字或ID。
- `@reason`: 解释你为什么要覆盖这个方法。

这有助于其他开发者理解你的意图，并且在出现冲突时更容易调试。

## 警告

`@Overwrite` 是一种非常具有侵入性的注入方式。它会完全删除原始方法的内容。这使得它非常容易与其他 `Mixin` 产生冲突。如果多个 `Mixin` 都尝试覆盖同一个方法，只有一个能成功，其他的都会失败并导致游戏崩溃。

**请优先考虑使用 `@Inject`、`@Redirect` 或其他侵入性较小的注解。`@Overwrite` 应该是你的最后选择。** 

::: danger 警告！·
无论何时使用`@Overwrite`与`@Redirect`都是不推荐的行为！
:::