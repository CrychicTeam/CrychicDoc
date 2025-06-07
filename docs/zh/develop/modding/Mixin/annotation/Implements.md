---
title: '@Implements'
---

# @Implements

`@Implements` 注解用于让目标类实现一个或多个接口。这通常与 `@Unique` 结合使用，以便能够从外部调用添加到目标类中的新方法。

## 用法

`@Implements` 注解需要一个 `value` 属性，它是一个 `@Interface` 注解的数组。每个 `@Interface` 注解指定一个要实现的接口和一个前缀。

### 示例

假设我们有一个接口：
```java
public interface ICustomData {
    int getCustomData();
    void setCustomData(int data);
}
```

我们想让 `Player` 类实现这个接口，并添加相应的字段和方法。

```java
@Mixin(Player.class)
@Implements(@Interface(iface = ICustomData.class, prefix = "custom$"))
public abstract class PlayerMixin {
    @Unique
    private int customData;

    @Unique
    public int custom$getCustomData() {
        return this.customData;
    }

    @Unique
    public void custom$setCustomData(int data) {
        this.customData = data;
    }
}
```

## `prefix` 属性

`prefix` 属性非常重要。它指定了一个前缀，所有实现了接口中方法的 `@Unique` 方法都必须以这个前缀开头。这告诉 Mixin 哪个 `@Unique` 方法对应于接口中的哪个方法。

在上面的例子中：
- `custom$getCustomData()` 实现了 `ICustomData` 中的 `getCustomData()`
- `custom$setCustomData(int data)` 实现了 `ICustomData` 中的 `setCustomData(int data)`

在 `Mixin` 应用之后，你就可以将一个 `Player` 对象转换为 `ICustomData` 类型，并安全地调用 `getCustomData()` 和 `setCustomData()`。

```java
Player player = ...;
ICustomData customPlayer = (ICustomData) player;
customPlayer.setCustomData(10);
int data = customPlayer.getCustomData(); // returns 10
```

这是一种在目标类上添加新功能并暴露 API 的干净方式。 