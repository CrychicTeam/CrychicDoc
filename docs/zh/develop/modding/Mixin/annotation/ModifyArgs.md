---
title: '@ModifyArgs'
---

# @ModifyArgs

`@ModifyArgs` 注解用于一次性修改方法调用的多个参数。它比多次使用 `@ModifyArg` 更高效。

## 用法

`@ModifyArgs` 注解的方法需要一个 `Args` 类型的参数。`Args` 对象包含了目标方法调用的所有参数，你可以通过 `get` 和 `set` 方法来访问和修改它们。

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

我们可以使用 `@ModifyArgs` 来同时修改 `drawString` 的 `x` 和 `y` 坐标：
```java
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Gui.class)
public class GuiMixin {
    @ModifyArgs(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/example/Gui;drawString(Ljava/lang/String;III)V"
        )
    )
    private void modifyCoordinates(Args args) {
        int x = args.get(1); // get original x
        int y = args.get(2); // get original y
        args.set(1, x + 50); // set new x
        args.set(2, y + 20); // set new y
    }
}
```

在这个例子中，`drawString` 方法的 `x` 参数将变为 `60`，`y` 参数将变为 `30`。

## `Args` 类

`Args` 类提供了以下方法：

- `get(int index)`: 获取指定索引的参数。
- `set(int index, Object value)`: 设置指定索引的参数。
- `setAll(Object... values)`: 一次性设置所有参数。
- `size()`: 获取参数的数量。

使用 `setAll` 的示例：
```java
@ModifyArgs(
    method = "render",
    at = @At(
        value = "INVOKE",
        target = "Lnet/example/Gui;drawString(Ljava/lang/String;III)V"
    )
)
private void modifyAll(Args args) {
    args.setAll("New string", 100, 100, 0xFF00FF);
}
```
这会替换 `drawString` 的所有参数。 