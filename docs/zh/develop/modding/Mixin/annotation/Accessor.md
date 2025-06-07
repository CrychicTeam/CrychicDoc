---
title: '@Accessor'
---

# @Accessor

`@Accessor` 注解用于自动生成目标类中字段的 `getter` 和 `setter` 方法。这使得你可以访问和修改私有（private）字段。

## 用法

`@Accessor` 注解在一个抽象方法上，Mixin 会在编译时自动实现这个方法。

### 生成 Getter

要为一个字段生成 `getter`，你需要声明一个无参数的方法，其名称遵循 Java Bean 规范（例如，`getX` 或 `isX`）。返回类型必须与字段类型匹配。

**示例:**

目标类：
```java
public class Entity {
    private int entityId;
}
```

Mixin:
```java
@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("entityId")
    int getEntityId();
}
```
现在你可以将 `Entity` 对象转换为 `EntityAccessor` 接口，并调用 `getEntityId()` 来获取 `entityId` 字段的值。

### 生成 Setter

要生成 `setter`，你需要声明一个带一个参数的方法，名称以 `set` 开头。返回类型为 `void`。

**示例:**
```java
@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("entityId")
    void setEntityId(int id);
}
```
这将允许你修改 `entityId` 字段。

## `value` 属性

`value` 属性是可选的。如果你不指定 `value`，Mixin 会尝试根据方法名推断目标字段名（例如 `getEntityId` -> `entityId`）。如果字段名与方法名不匹配，或者有歧义，你就需要明确指定 `value`。

**示例:**
```java
// 目标字段名是 "internalId"，但我们想让 getter 名为 "getId"
@Accessor("internalId")
int getId();
```

## 使用类而不是接口

你也可以在 `Mixin` 类（而不是接口）中使用 `@Accessor`。
```java
@Mixin(Entity.class)
public abstract class EntityMixin {
    @Accessor("entityId")
    abstract int getEntityId();
}
```
在这种情况下，方法需要是 `abstract` 的。 