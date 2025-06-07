---
title: '可选 Mixin (Pseudo-Mixins)'
---

# 可选 Mixin (Pseudo-Mixins)

## 概述 {#Summary}

在模组开发中，你可能需要向运行时可能不存在的类（例如，另一个模组的类）注入代码。如果直接在 `@Mixin` 中引用一个不存在的类，游戏会崩溃。

有两种主要的解决方案：
1. 使用 `@Pseudo` 注解（推荐用于简单场景）
2. 使用 Mixin 插件（适用于复杂场景）

## @Pseudo 注解方式 {#Pseudo}

`@Pseudo` 注解允许你创建一个 Mixin，即使目标类在运行时不存在也不会导致崩溃。这在处理可选依赖模组时特别有用。

### 基本用法 {#Pseudo-Basic}

这是一个针对 Fabric API 的简单示例：

```java
@Pseudo
@Mixin(ServerLifecycleEvents.class)
public class FabricApiMixin {
    @Shadow
    @Final
    @Mutable
    public static Event<ServerLifecycleEvents.ServerStarting> SERVER_STARTING;

    static {
        SERVER_STARTING = null;
    }
}
```

在这个例子中：
- `@Pseudo` 表明这是一个可选的 Mixin
- 如果 `ServerLifecycleEvents` 类不存在，Mixin 会被安全地跳过
- 使用 `static` 初始化块来设置字段值

## Mixin 插件方式 {#Plugin}

Mixin 插件提供了更灵活的控制，允许你基于复杂条件决定是否应用 Mixin。

### 创建插件 {#Plugin-Creation}

首先，创建一个实现了 `IMixinConfigPlugin` 接口的类：

```java
package net.example.mixin.plugin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MyMixinPlugin implements IMixinConfigPlugin {
    // 用于缓存类检查结果
    private static final Map<String, Boolean> CLASS_CHECK_CACHE = new HashMap<>();

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // 检查是否是我们的可选 Mixin
        if (mixinClassName.endsWith("OptionalModMixin")) {
            return CLASS_CHECK_CACHE.computeIfAbsent(targetClassName, this::isClassPresent);
        }
        return true;
    }

    private boolean isClassPresent(String className) {
        try {
            // 尝试加载目标类
            Class.forName(className, false, this.getClass().getClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    // 其他必需的方法实现
    @Override public void onLoad(String mixinPackage) {}
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
```

### 配置插件 {#Plugin-Config}

在 Mixin 配置文件 (`mixins.json`) 中指定插件：

```json
{
  "required": true,
  "package": "net.example.mixin",
  "compatibilityLevel": "JAVA_17",
  "plugin": "net.example.mixin.plugin.MyMixinPlugin",
  "mixins": [
    "SomeMixin",
    "optional.OptionalModMixin"
  ],
  "client": [
    "client.SomeClientMixin"
  ],
  "injectors": {
    "defaultRequire": 1
  }
}
```

### 创建 Mixin {#Plugin-Mixin}

创建使用插件控制的 Mixin：

```java
package net.example.mixin.optional;

@Mixin(targets = "net.othermod.SomeClass")
public class OptionalModMixin {
    @Inject(method = "someMethod", at = @At("HEAD"))
    private void onSomeMethod(CallbackInfo ci) {
        // Mixin 逻辑
    }
}
```