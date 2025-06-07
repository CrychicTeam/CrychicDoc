---
title: '@Shadow'
---

# @Shadow

`@Shadow` 注解用于在 `Mixin` 类中声明一个对目标类中成员（字段或方法）的引用。这允许你从 `Mixin` 的方法中访问目标类的私有（private）或保护（protected）成员。

## 用法

### 访问字段

要访问目标类中的一个字段，你可以在 `Mixin` 中声明一个带有 `@Shadow` 注解的同名同类型的字段。

**示例:**

目标类：
```java
public class Player {
    private boolean onGround;
}
```

Mixin:
```java
@Mixin(Player.class)
public abstract class PlayerMixin {
    @Shadow private boolean onGround;

    @Inject(method = "jump", at = @At("HEAD"))
    private void onJump(CallbackInfo ci) {
        if (this.onGround) {
            // ...
        }
    }
}
```
现在你可以在 `onJump` 方法中访问 `onGround` 字段了。

### 访问方法

同样地，你也可以用 `@Shadow` 来访问目标类中的方法。

**示例:**

目标类：
```java
public class Block {
    protected void dropItems() {
        // ...
    }
}
```

Mixin:
```java
@Mixin(Block.class)
public abstract class BlockMixin {
    @Shadow protected abstract void dropItems();

    @Inject(method = "destroy", at = @At("TAIL"))
    private void onDestroy(CallbackInfo ci) {
        this.dropItems();
    }
}
```

注意，`@Shadow` 方法必须是 `abstract` 的。

## `prefix` 属性

有时，你的 `Mixin` 类可能需要一个与目标类中字段或方法同名的成员。为了避免命名冲突，你可以使用 `prefix` 属性。Mixin 会自动为你添加上前缀。

**示例:**
```java
@Mixin(Example.class)
public class ExampleMixin {
    @Shadow(prefix = "shadow$")
    private int value; // This will shadow the 'value' field in Example.class

    private int value; // This is a new field specific to the mixin

    public void someMethod() {
        this.value = 1; // Accesses the mixin's own 'value' field
        this.shadow$value = 2; // Accesses the shadowed 'value' field from the target class
    }
}

```

## `final` 字段

如果目标字段是 `final` 的，你需要在 `@Shadow` 注解的字段上也加上 `final` 修饰符。

```java
@Shadow final private int score;
```
如果你想修改一个 `final` 字段，你需要使用 `@Mutable` 注解和 `@Shadow` 一起。
```java
@Mixin(Example.class)
public abstract class ExampleMixin {
    @Mutable
    @Shadow
    private final int score;

    // constructor or other methods to modify score
}
```
现在你就可以在你的 `Mixin` 方法中修改 `score` 的值了。

## 匿名内部类

`@Shadow` 也可以用于匿名内部类，但这通常比较复杂，因为匿名类没有名字。你需要使用特殊的语法来定位它们。 