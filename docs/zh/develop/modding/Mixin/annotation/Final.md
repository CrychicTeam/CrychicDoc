---
title: '@Final'
---

# @Final

`@Final` 本身不是一个 Mixin 注解，而是 Java 的一个关键字。但在 Mixin 的上下文中，它与 `@Shadow` 结合使用时有特殊的意义。

## @Shadow a final field

当你需要 `shadow` 一个 `final` 字段时，你必须在你的 `@Shadow` 声明中也包含 `final` 关键字。

**示例:**

目标类：
```java
public class Block {
    private final String translationKey;
}
```

Mixin:
```java
@Mixin(Block.class)
public class BlockMixin {
    @Shadow
    private final String translationKey;

    @Inject(method = "onUse", at = @At("HEAD"))
    private void onUse(CallbackInfo ci) {
        if (this.translationKey.equals("block.minecraft.chest")) {
            // ...
        }
    }
}
```

## @Mutable

如果你想要修改一个 `final` 字段的值，你需要额外使用 `@Mutable` 注解。

```java
@Mixin(Block.class)
public abstract class BlockMixin {
    @Mutable
    @Shadow
    private final String translationKey;

    public void setTranslationKey(String newKey) {
        this.translationKey = newKey;
    }
}
```

没有 `@Mutable` 注解，尝试给一个 `final` 的 `@Shadow` 字段赋值会导致编译错误。 