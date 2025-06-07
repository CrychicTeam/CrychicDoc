---
title: 'Mixin 优先级'
---

# Mixin 优先级

Mixin 优先级允许你控制你的 Mixin 在其他模组的 Mixin 之前或之后应用。当多个模组都尝试修改同一个目标类的同一个方法时，优先级系统就显得至关重要。

## `priority` 属性

优先级是通过 `@Mixin` 注解的 `priority` 属性来设置的。

```java
@Mixin(value = ExampleClass.class, priority = 900)
public class HighPriorityMixin {
    // ...
}

@Mixin(value = ExampleClass.class, priority = 1100)
public class LowPriorityMixin {
    // ...
}
```

-   `priority` 是一个整数。
-   **数字越小，优先级越高。**
-   默认的优先级是 **1000**。

## 优先级如何工作

当多个 Mixin 应用于同一个目标时，它们会根据优先级排序。优先级最高的（`priority` 值最小的）会首先被应用。

-   如果你想让你的修改在大多数其他 Mixin **之前** 生效，你应该设置一个**低于 1000** 的优先级（例如 `900`）。
-   如果你想让你的修改在其他 Mixin **之后** 生效（例如，为了兼容性，需要依赖于其他 Mixin 的修改），你应该设置一个**高于 1000** 的优先级（例如 `1100`）。

## 常见的优先级范围

虽然你可以选择任何整数，但在社区中有一些非官方的约定：

| 优先级范围 | 描述                                       |
| ---------- | ------------------------------------------ |
| 0-999      | **高优先级 (High Priority)**。用于需要尽早执行的核心修改或修复。 |
| 1000       | **默认优先级 (Default Priority)**。大多数 Mixin 使用这个级别。     |
| 1001-2000  | **低优先级 (Low Priority)**。用于需要在其他 Mixin 修改之后再应用的修改。 |
| > 2000     | **非常低的优先级 (Very Low Priority)**。通常为兼容性补丁保留。     |

## 为什么重要？

- **`@Overwrite`**: 如果多个 Mixin 都尝试 `@Overwrite` 同一个方法，只有优先级最高的那个会成功，其他的都会失败并导致游戏崩溃。
- **`@Inject`**: 注入的顺序会影响最终的行为。一个高优先级的 `@Inject` (at `HEAD`) 会在一个低优先级的 `@Inject` (at `HEAD`) 之前运行。
- **`@Redirect` 和 `@ModifyArg`**: 修改的顺序可能很重要。一个低优先级的 `@ModifyArg` 会在已经经过高优先级 Mixin 修改后的值的基础上再进行修改。

谨慎选择你的优先级。除非你有明确的理由需要你的 Mixin 在其他 Mixin 之前或之后运行，否则坚持使用默认的 `1000` 优先级。 