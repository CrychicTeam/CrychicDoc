---
title: 注解
priority: -1000
---

# 注解 {#annotation}

Mixin 使用一系列注解来实现其强大的代码修改功能。下面是主要注解的列表，点击可以查看每个注解的详细用法和示例。

## 主要注解

### 核心
- [**`@Mixin`**](./Mixin.md) - 声明一个类为 Mixin 类，并指定目标。
- [**`@Shadow`**](./Shadow.md) - 引用目标类中已有的成员（字段或方法）。
- [**`@Unique`**](./Unique.md) - 声明一个成员是 Mixin 独有的，将其添加到目标类。

### 注入器 (Injectors)
- [**`@Inject`**](./Inject.md) - 在目标方法的特定位置注入代码。
- [**`@ModifyArg`**](./ModifyArg.md) - 修改方法调用时的单个参数。
- [**`@ModifyArgs`**](./ModifyArgs.md) - 修改方法调用时的多个参数。
- [**`@ModifyVariable`**](./ModifyVariable.md) - 修改作用域内的局部变量。
- [**`@Redirect`**](./Redirect.md) - 重定向一个方法调用到另一个方法。
- [**`@Overwrite`**](./Overwrite.md) - 完全覆盖一个目标方法。
- [**`@ModifyConstant`**](./ModifyConstant.md) - (已弃用) 修改常量值。

### 访问器 (Accessors)
- [**`@Accessor`**](./Accessor.md) - 自动生成字段的 getter/setter。
- [**`@Invoker`**](./Invoker.md) - 允许调用私有或保护方法。

### 接口与元注解
- [**`@Implements`**](./Implements.md) - 让目标类实现一个接口。
- [**`@Interface`**](./Interface.md) - 与 `@Implements` 配合使用或用于"软实现"。
- [**`@At`**](./At.md) - 指定注入点 (Injection Point)，用于注入器注解中。
- [**`@Final`**](./Final.md) - 与`@Shadow`连用以访问final成员。
- [**`@Intrinsic`**](./Intrinsic.md) - 声明一个方法的实现是"固有"的。

### 高级概念
- [**`@Slice`**](./Slice.md) - 在方法中定义一个用于查找注入点的"切片"。
- [**可选 Mixin**](./OptionalMixin.md) - 如何让 Mixin 只在目标类存在时应用。
- [**Mixin 优先级**](./Priority.md) - 控制 Mixin 的应用顺序。


::: info 注意
本文档内容很大程度上参考了 [Mixin Cheatsheet](https://github.com/2xsaiko/mixin-cheatsheet) 项目，并结合了 SpongePowered Mixin Wiki 的信息进行了补充和说明。
:::

