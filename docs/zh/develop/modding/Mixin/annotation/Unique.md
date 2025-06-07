---
title: '@Unique'
---

# @Unique

`@Unique` 注解用于标记一个字段或方法在 `Mixin` 中是唯一的。这意味着这个成员不应该存在于目标类中。当 `Mixin` 应用时，这个唯一的成员会被添加到目标类中。

## 用法

`@Unique` 确保你添加的成员不会意外地覆盖目标类中已有的成员。如果目标类中已经存在一个同名的成员，`Mixin` 在应用时会抛出一个错误。

### 添加新字段

你可以在 `Mixin` 中添加新的字段，并在目标类中使用它们。

**示例:**
```java
@Mixin(Player.class)
public class PlayerMixin {
    @Unique
    private int customTimer;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        this.customTimer++;
    }
}
```
这个例子向 `Player` 类添加了一个 `customTimer` 字段，并在 `tick` 方法中更新它。

### 添加新方法

同样地，你也可以添加新的方法。
```java
@Mixin(Player.class)
public abstract class PlayerMixin implements IPlayerWithCustomLogic {
    @Unique
    private int customData;

    @Unique
    public void doCustomLogic() {
        this.customData++;
        // ...
    }
}
```
通常，为了能从外部调用这些新添加的方法，你会让 `Mixin` 实现一个接口（例如 `IPlayerWithCustomLogic`），然后在需要的地方将目标对象（`Player`）转换成该接口类型来调用新方法。

## `silent` 属性

默认情况下，如果 `@Unique` 成员与目标类中的成员冲突，Mixin 会抛出错误。你可以设置 `silent = true` 来抑制这个错误。这在某些高级用例中可能有用，但通常不推荐，因为它可能隐藏潜在的问题。

```java
@Unique(silent = true)
private void possiblyClashingMethod() {
    // ...
}
```

## 总结

- `@Unique` 用于向目标类安全地添加新成员。
- 它能防止意外的方法或字段覆盖。
- 是实现自定义逻辑和存储自定义数据的常用方式。 