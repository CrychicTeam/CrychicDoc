---
title: '@Slice'
---

# @Slice

`@Slice` 注解用于在目标方法中定义一个"切片"，也就是一个特定的代码区域。当 `@Inject` 等注入器注解与 `@Slice` 结合使用时，注入器将只在由 `@Slice` 定义的这个切片内部查找其注入点 (`@At`)。

这对于在大型方法中精确定位注入点，或者当存在多个相同的潜在注入点时，非常有用。

## 用法

`@Slice` 注解被用在 `@Inject` 的 `slice` 属性中。

```java
@Inject(
    method = "veryLargeMethod",
    at = @At(...),
    slice = @Slice(...)
)
private void onSomethingInSlice(CallbackInfo ci) {
    // ...
}
```

## 定义切片

一个切片由一个起点和一个可选的终点定义。

### `from` 属性

`from` 属性使用一个 `@At` 注解来标记切片的开始位置（包含）。

### `to` 属性

`to` 属性使用一个 `@At` 注解来标记切片的结束位置（包含）。

**示例:**

假设有一个方法：
```java
public void process() {
    // A
    setup();
    // B
    runLoop();
    // C
    cleanup();
    // D
}
```
我们只想在 `runLoop()` 和 `cleanup()` 之间的代码区域（C区域）注入。我们可以这样定义一个切片：

```java
@Inject(
    method = "process",
    at = @At("TAIL"), // 在切片的末尾注入
    slice = @Slice(
        from = @At(value = "INVOKE", target = "Lnet/example/MyClass;runLoop()V"),
        to = @At(value = "INVOKE", target = "Lnet/example/MyClass;cleanup()V")
    )
)
private void afterRunLoop(CallbackInfo ci) {
    // This code will be injected right after runLoop() is called,
    // because the @At("TAIL") is now relative to the slice.
}
```

## `id` 属性

你可以为一个 `slice` 指定一个唯一的 `id`。这在以下两种情况中有用：
1.  **可读性**: 给切片一个有意义的名字。
2.  **共享**: 多个注入器可以通过 `id` 引用同一个在 `@Slice` 注解中定义的切片，避免重复定义。

要定义一个可共享的切片，你可以在 `Mixin` 类上使用 `@Slice` 注解，并为其提供 `id`。

```java
@Mixin(Example.class)
@Slice(id = "mySlice", from = @At(...), to = @At(...))
public abstract class ExampleMixin {

    @Inject(method = "example", at = @At(...), slice = @Slice(id = "mySlice"))
    private void injector1(...) { ... }

    @ModifyVariable(method = "example", at = @At(...), slice = @Slice(id = "mySlice"))
    private int injector2(...) { ... }
}
```
现在，`injector1` 和 `injector2` 都在 `mySlice` 定义的范围内查找它们的注入点。 