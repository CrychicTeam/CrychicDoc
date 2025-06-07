---
title: '@At'
---

# @At

`@At` 注解用于指定注入点（Injection Point），它决定了代码注入到目标方法中的确切位置。`@At` 通常作为 `@Inject`、`@Redirect` 等注解的参数使用。

## `value` 属性

`value` 属性是一个字符串，用于指定注入点的类型。下面是一些常用的注入点类型：

### `HEAD`

`HEAD` 将代码注入到目标方法的开头。这是最简单和最常用的注入点之一。

**示例:**
```java
@Inject(method = "update", at = @At("HEAD"))
private void onUpdate(CallbackInfo ci) {
    // This code runs at the start of the update() method
}
```

### `TAIL`

`TAIL` 将代码注入到目标方法的末尾，紧接在 `RETURN` 指令之前。如果方法有多个返回点，`TAIL` 会在所有返回点之前注入。

**示例:**
```java
@Inject(method = "update", at = @At("TAIL"))
private void afterUpdate(CallbackInfo ci) {
    // This code runs just before the update() method returns
}
```

### `RETURN`

`RETURN` 与 `TAIL` 类似，但它会在每个 `RETURN` 指令之前注入。如果方法有返回值，注入方法的最后一个参数应该是 `CallbackInfoReturnable`。

### `INVOKE`

`INVOKE` 用于在方法调用指令之前注入代码。你需要使用 `target` 参数来指定目标方法。

**`target` 参数:**

`target` 参数是一个字符串，用于指定目标方法的签名。签名格式为 `Lowner/class;methodName(Larg/type;)Lreturn/type;`。

**示例:**
```java
// Target code:
public class Game {
    private Logger logger;

    public void start() {
        this.logger.info("Game starting!"); // We want to inject before this line
    }
}

// Mixin:
@Mixin(Game.class)
public class GameMixin {
    @Inject(
        method = "start",
        at = @At(
            value = "INVOKE",
            target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V"
        )
    )
    private void onLoggerInfo(CallbackInfo ci) {
        System.out.println("The logger is about to be used!");
    }
}
```

### `FIELD`

`FIELD` 用于在字段访问（获取或设置）指令之前注入代码。你需要使用 `target` 参数来指定目标字段。

**示例:**
```java
@Inject(
    method = "someMethod",
    at = @At(
        value = "FIELD",
        target = "Lnet/minecraft/world/World;isRemote:Z",
        opcode = Opcodes.GETFIELD
    )
)
```

### `NEW`

`NEW` 用于在 `new` 一个新对象时注入代码。

### `CONSTANT`

`CONSTANT` 用于在加载一个常量时注入。这对于修改方法中使用的硬编码值很有用。

## `shift` 属性

`shift` 属性用于微调注入点的位置。它可以是 `BY`, `BEFORE`, 或 `AFTER`。

- `shift = At.Shift.BY`：按指定的字节码指令数量移动注入点。需要 `by` 参数。
- `shift = At.Shift.BEFORE`：将注入点移动到目标之前。
- `shift = At.Shift.AFTER`：将注入点移动到目标之后。

**示例:**
```java
@Inject(
    method = "example",
    at = @At(
        value = "INVOKE",
        target = "...",
        shift = At.Shift.AFTER
    )
)
```

这会将代码注入到方法调用 *之后*，而不是之前。 