---
title: '@Mixin'
---

# @Mixin

`@Mixin`注解用于声明一个类为`Mixin`类，并指定要注入的目标类。

## 属性

### `value`

`value`属性用于指定一个或多个目标类。目标类是`Mixin`将要应用修改的类。

**示例:**

```java
@Mixin(ExampleClass.class)
public class ExampleMixin {
    // ...
}
```

可以指定多个目标类：

```java
@Mixin({ExampleClass1.class, ExampleClass2.class})
public class ExampleMixin {
    // ...
}
```

### `priority`

`priority`属性用于指定`Mixin`的优先级。优先级是一个整数，值越小，优先级越高。默认优先级是`1000`。在多个`Mixin`修改同一个方法时，优先级高的会先应用。

**示例:**

```java
@Mixin(value = ExampleClass.class, priority = 900)
public class HighPriorityMixin {
    // ...
}
```

关于优先级的更详细说明：

`Mixin`的优先级系统允许开发者控制他们的`Mixin`在其他`Mixin`之前或之后应用。这在多个模组修改同一个目标类时尤其重要。

默认优先级是 `1000`。如果你想让你的`Mixin`在大多数其他`Mixin`之前应用，你可以设置一个较低的`priority`值（例如`900`）。如果你希望在其他`Mixin`之后应用，可以设置一个较高的值（例如`1100`）。

Fabric和Forge有一些默认的优先级范围，但通常你只需要关心比默认值高还是低。

### `remap`

`remap`属性是一个布尔值，用于指定是否对`Mixin`中的成员（方法、字段）名称进行重映射。在开发环境中，我们使用可读的名称（如`net.minecraft.client.Minecraft`），但在生产环境中，这些名称会被混淆（如`net.minecraft.client.d`）。

默认情况下，`remap`为`true`，这意味着`Mixin`会尝试将你使用的开发环境中的名称映射到混淆后的名称。在大多数情况下，你应该保持`remap = true`。只有在你需要直接操作混淆后的代码或者目标不是一个需要被映射的类时（例如其他模组的类），才将其设置为`false`。

::: warning 注意
如果你需要`Mixin`其他模组，那么通常必须将`remap`设为`false`。
:::

**示例:**

```java
@Mixin(value = ExampleClass.class, remap = false)
public class ExampleMixin {
    // ...
}
``` 