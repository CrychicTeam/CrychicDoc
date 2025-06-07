---
title: '@ModifyVariable'
---

# @ModifyVariable

`@ModifyVariable` 注解用于修改方法中的局部变量。

## 属性

### `method`

`method` 属性指定了包含要修改局部变量的方法。

### `at`

`at` 属性使用 `@At` 注解来指定在何处修改变量。

### `index` / `ordinal`

你可以使用 `index` (或 `ldc` / `var` / `arg`) 或 `ordinal` 来指定要修改的变量。

- `index`: 局部变量表中的索引。
- `ordinal`: 如果有多个相同类型的变量，`ordinal` 用于选择第几个（从0开始）。

### `name`

你也可以通过 `name` 属性来指定变量名。这通常比使用索引更清晰。

## 用法

`@ModifyVariable` 注解的方法应该返回与要修改的变量相同的类型，并接受一个同类型的参数（原始变量值）。

**示例:**

假设目标方法如下：
```java
public void applyDamage(int damage) {
    int finalDamage = damage * 2;
    // ... deal finalDamage
}
```

我们可以修改 `finalDamage` 变量：
```java
@Mixin(Example.class)
public class ExampleMixin {
    @ModifyVariable(
        method = "applyDamage",
        at = @At("STORE"), // after the variable is stored
        name = "finalDamage"
    )
    private int modifyFinalDamage(int originalDamage) {
        return originalDamage / 2;
    }
}
```
`@At("STORE")` 表示在变量被赋值后立即修改它。在这个例子中，`finalDamage` 的值将从 `damage * 2` 变为 `(damage * 2) / 2`，也就是 `damage`。

## 修改方法参数

`@ModifyVariable` 也可以用来修改方法的参数，因为它们也被看作是局部变量。

**示例:**
```java
@Mixin(Example.class)
public class ExampleMixin {
    @ModifyVariable(
        method = "applyDamage",
        at = @At("HEAD"),
        argsOnly = true // only look at arguments
    )
    private int modifyDamageArgument(int damage) {
        return 0; // Negate all damage
    }
}
```
这里 `argsOnly = true` 是一个好习惯，它告诉 Mixin 只在方法的参数中查找要修改的变量。`at = @At("HEAD")` 表示在方法开始时就修改参数。 