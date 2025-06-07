---
root: true
title: 目录
priority: 10000000
collapsed: false
state: unfinished
progress: 15
---

# 概要 {#Summary}

`Mixin`意为混入('Mix in')，允许使用者在**在指定位置混入自己的代码**，这意味着使用着可以修改原版与其他模组的行为与配置，以实现自己的需求。

`Mixin`使用大量[注解](https://www.runoob.com/w3cnote/java-annotation.html),并允许通过不同的注解与对注解不同的配置，实现不同位置与不同方式的插入与修改代码。

::: danger 请注意
通常`Mixin`极容易导致代码冲突并致使游戏崩溃，请谨慎使用`Mixin`，尽量尝试使用Loader提供的钩子与API来实现- [概要 {#Summary}](#概要-summary)
需求。

但如果你的需求需要`Mixin`来实现，请不要过于恐惧`Mixin`可能引发的问题，并积极向社区寻求相关的帮助。
:::

通常来说，如果你的需求符合以下特征，可以在设计时将`Mixin`作为实现需求的方案之一:

1. 需要修改原版代码的逻辑。
2. 需要对他人的模组进行修改/修复。
3. 需要在原版或代码中加入自己的变量与方法。
4. 需要访问访问控制修饰符为`private`或`protect`的变量或方法。
5. 为其他模组做兼容性的修改。