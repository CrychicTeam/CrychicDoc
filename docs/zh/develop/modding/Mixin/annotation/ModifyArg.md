---
title: '@ModifyArg'
---

# @ModifyArg

`@ModifyArg` 注解用于修改传递给方法的参数。它允许你在方法调用发生之前更改参数的值。

## 属性

### `method`

`method` 属性指定了包含要修改参数的方法调用的方法。

### `at`

`at` 属性使用 `@At` 注解来定位到进行参数修改的方法调用。`@At` 的 `value` 通常是 `INVOKE`。

### `index`

`index` 属性指定要修改的参数的索引（从0开始）。

## 用法

`@ModifyArg` 注解的方法应该返回与要修改的参数相同的类型，并接受与原始参数相同类型的参数。

**示例:**

假设有以下目标代码：
```java
public class Gui {
    public void drawString(String s, int x, int y, int color) {
        // draws a string
    }

    public void render() {
        this.drawString("Hello", 10, 10, 0xFFFFFF);
    }
}
```

我们可以使用 `@ModifyArg` 来修改传递给 `drawString` 的字符串：
```java
@Mixin(Gui.class)
public class GuiMixin {
    @ModifyArg(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/example/Gui;drawString(Ljava/lang/String;III)V"
        ),
        index = 0
    )
    private String modifyText(String originalText) {
        return "Modified Text";
    }
}
```

在这个例子中，当 `render` 方法调用 `drawString` 时，第一个参数（索引为0）`"Hello"` 将被替换为 `"Modified Text"`。

你也可以修改其他索引的参数，比如修改颜色：
```java
@ModifyArg(
    method = "render",
    at = @At(
        value = "INVOKE",
        target = "Lnet/example/Gui;drawString(Ljava/lang/String;III)V"
    ),
    index = 3
)
private int modifyColor(int originalColor) {
    return 0xFF0000; // Red color
}
```

这会将文本颜色修改为红色。 