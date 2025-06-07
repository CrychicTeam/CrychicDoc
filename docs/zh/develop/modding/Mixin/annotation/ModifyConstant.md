---
title: '@ModifyConstant'
---

# @ModifyConstant

`@ModifyConstant` 注解用于修改在方法中加载的常量值。

::: v-warning 注意
`@ModifyConstant` 在新版本的 Mixin 中已被弃用，因为它在处理某些常量（如 `ldc`) 时存在问题。

推荐使用 `@ModifyArg`，`@Redirect` 或者带有 `@At(value = "CONSTANT")` 的 `@Inject` 来实现类似的功能。
:::

## 用法

`@ModifyConstant` 注解的方法应该返回与要修改的常量相同的类型，并可以接受一个同类型的参数（原始常量值）。

**示例 (旧版用法):**

目标代码：
```java
public class Example {
    public void printMessage() {
        System.out.println("Hello World");
    }
}
```

Mixin:
```java
@Mixin(Example.class)
public class ExampleMixin {
    @ModifyConstant(method = "printMessage", constant = @Constant(stringValue = "Hello World"))
    private String modifyMessage(String constant) {
        return "Goodbye World";
    }
}
```
这个 Mixin 会将 `printMessage` 方法中的 "Hello World" 字符串常量修改为 "Goodbye World"。

由于它的不可靠性，请尽量避免使用此注解。 