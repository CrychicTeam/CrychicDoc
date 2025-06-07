---
title: '@Inject'
---

# @Inject

`@Inject`注解用于将代码注入到目标方法的特定位置。这是最常用的`Mixin`注解之一。

## 属性

### `method`

`method`属性指定了目标方法的名称和描述符。这是必须的属性，用于定位要注入代码的方法。

**示例:**

```java
@Inject(method = "render", at = @At("HEAD"))
private void onRender(CallbackInfo ci) {
    // a hook at the beginning of render()
}
```

### `at`

`at`属性使用`@At`注解来指定注入点（Injection Point）。注入点决定了你的代码在目标方法中的确切位置。`@At`有很多可能的取值，下面是一些常用的：

-   `"HEAD"`: 注入到方法的开头。
-   `"TAIL"`: 注入到方法的结尾，在所有`RETURN`操作码之前。
-   `"RETURN"`: 在每个`RETURN`操作码之前注入。
-   `"INVOKE"`: 在方法调用之前注入。需要和`target`一起使用来指定目标调用。

关于`@At`的更详细信息，请参考[@At文档](./At.md)。

### `slice`

`slice`属性用于更精确地指定注入点的位置，特别是当有多个匹配的注入点时。

### `cancellable`

当`cancellable`属性设置为`true`时，你可以在注入的代码中取消目标方法的执行。这对于阻止原版方法的逻辑执行很有用。

当`cancellable = true`时，你的注入方法必须使用`CallbackInfo`作为参数。你可以调用`ci.cancel()`来取消目标方法。

**示例:**

```java
@Inject(method = "example", at = @At("HEAD"), cancellable = true)
private void onExample(CallbackInfo ci) {
    // some condition
    if (shouldCancel) {
        ci.cancel(); // Cancels the target method
    }
}
```

### `locals`

`locals`属性允许你捕获注入点作用域内的局部变量。你需要使用`org.spongepowered.asm.mixin.injection.callback.LocalCapture`枚举来指定如何捕获局部变量。

-   `LocalCapture.PRINT`：打印所有可用的局部变量到控制台。用于调试。
-   `LocalCapture.CAPTURE_FAILHARD`：捕获局部变量。如果失败则抛出异常。
-   `LocalCapture.CAPTURE_FAILSOFT`：捕獲局部變量。如果失敗，則記錄一個警告。

捕获的局部变量会作为参数传递给你的注入方法，在`CallbackInfo`参数之后。

**示例:**
目标方法：
```java
public void example(int i) {
    String s = "hello";
    double d = 1.0;
    // some code
}
```

Mixin:
```java
@Inject(method = "example", at = @At(...), locals = LocalCapture.CAPTURE_FAILHARD)
private void onExample(CallbackInfo ci, int i, String s, double d) {
    // You can now use i, s, and d
}
```

## 回调信息 (CallbackInfo)

注入方法通常需要一个`CallbackInfo`类型的参数。它提供了关于注入的信息，并且在`cancellable`时用于取消方法。

如果目标方法有返回值，你应该使用`CallbackInfoReturnable<R>`，其中`R`是返回类型。你可以使用`cir.setReturnValue()`来修改返回值。

**示例:**

```java
@Inject(method = "getValue", at = @At("HEAD"), cancellable = true)
private void onGetValue(CallbackInfoReturnable<String> cir) {
    cir.setReturnValue("new value");
}
``` 