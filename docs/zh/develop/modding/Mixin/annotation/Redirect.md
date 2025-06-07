---
title: '@Redirect'
---

# @Redirect

`@Redirect` 注解用于将一个方法调用重定向到你的方法。这允许你完全替换一个方法调用的行为。

## 用法

`@Redirect` 注解的方法应该有和你想要重定向的方法调用几乎相同的签名。区别在于，`@Redirect` 方法的第一个参数应该是拥有被调用方法的对象实例。

**示例:**

假设目标代码如下：
```java
public class Player {
    private Inventory inventory;

    public void dropAllItems() {
        this.inventory.clear(); // We want to redirect this call
    }
}
```

我们可以重定向 `inventory.clear()` 调用：
```java
@Mixin(Player.class)
public class PlayerMixin {
    @Redirect(
        method = "dropAllItems",
        at = @At(
            value = "INVOKE",
            target = "Lnet/example/Inventory;clear()V"
        )
    )
    private void onClear(Inventory inventory) {
        // Instead of clearing the inventory, we do something else.
        System.out.println("Inventory clear redirected!");
    }
}
```
在这个例子中，当 `dropAllItems` 方法执行时，对 `inventory.clear()` 的调用会被重定向到 `onClear` 方法。原始的 `clear()` 方法将不会被执行。`onClear` 方法的第一个参数 `inventory` 是调用 `clear()` 方法的对象实例。

## 重定向静态方法调用

如果你重定向一个静态方法的调用，那么你的 `@Redirect` 方法也应该是静态的，并且第一个参数应该是被调用方法的第一个参数。

## 与 `@Inject` 的区别

- `@Inject` 是在方法调用 *之前* 或 *之后* 添加代码，原始的方法调用仍然会发生。
- `@Redirect` 是 *替换* 整个方法调用。原始的方法调用不会发生。

`@Redirect` 是一种更强大的注入方式，但有时也更危险，因为它完全改变了原始逻辑。 

::: danger 警告！·
无论何时使用`@Overwrite`与`@Redirect`都是不推荐的行为！
:::