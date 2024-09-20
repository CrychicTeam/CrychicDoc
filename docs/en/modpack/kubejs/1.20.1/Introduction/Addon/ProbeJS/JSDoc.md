---
progress: 100
---
# JSDoc
## 类型补全问题 {#Summary}
::: justify
在开发过程中，有时会遇到类型补全的问题。这个问题并非由`ProbeJS`直接导致，而是由于`KubeJS`各个事件返回的`参数`与`JavaScript`的`弱类型`特性所引发。通过合理使用[`JSDoc`](https://jsdoc.app/)标记，可以在一定程度上优化这一问题。

如果你是想要更深入地学习`JSDoc`，可以在文档内撰写好相关内容前查看[这一文档](https://jsdoc.app/)。
:::

例如在[这个文档](../../Entity/PotionEffects.md)中，虽然可以使用 potionEffects 来修改实体的药水效果，但在尝试获取时可能会遇到问题：

```js twoslash
ItemEvents.entityInteracted(event => {
    const { entity, target, hand, server, level } = event;
    if (hand !== 'main_hand') return;
    entity.potionEffects.add('minecraft:night_vision', 200, 0, false, true);
//                ^?
})
```

::: justify
可以看到，`potionEffects`并没有正确显示`Twoslash`提供的类型提示。

这是因为`potionEffects`是`Internal.LivingEntity`的方法，同时

 `ItemEvents.entityInteracted` 提供的参数类型为 `Entity`，便导致了无法正确为`event.entity`补全`potionEffects`的情况。
 
 由于`KubeJS`的事件设计，这种问题在`KubeJS`的魔改中较为常见。

> 更具体的内容可以参照[类篇章](../../../Upgrade/GlobalScope/Classes/Catalogue)

然而，通过使用 JSDoc 标记变量类型可以缓解这一问题：
:::

```js twoslash
ItemEvents.entityInteracted(event => {
    const { entity, target, hand, server, level } = event;
    /**
     * @type {Internal.LivingEntity} 这是一个改变了类型的变量。
     */
    let livingentity = entity;
    entity.potionEffects.add('minecraft:night_vision', 200, 0, false, true);
    livingentity.potionEffects.add('minecraft:night_vision', 200, 0, false, true);
    //               ^?
})
```
<br>

可以看到，现在`potionsEffects`的类型可以正常显示。

该方式能够让类型补全更加准确，从而改进开发体验。

## 基础JSDoc用法 {#BasicJSDoc}

### 标注变量类型 {#VariableType}

在JavaScript中，由于其动态类型特性，变量的类型并不会在代码中明确声明。但通过`JSDoc`，你可以为变量显式声明类型，从而获得更好的类型提示支持。其基本语法如下：

```js twoslash
/**
 * @type {string}
 */
let myString = 'Hello World';
```

通过这种方式，编辑器会将myString识别为string类型，并提供相应的代码补全提示。

通常来说，这种方法能够适用于大部分KubeJS开发中遇到的类型问题。

## 标注函数参数与返回值 {#ParamAndReturn}

同样，JSDoc还可以用于为函数的参数和返回值标注类型。例如：

```js twoslash
/**
 * 计算两个数的和
 * @param {number} a - 第一个数字
 * @param {number} b - 第二个数字
 * @returns {number} 两数之和
 */
function add(a, b) {
    return a + b;
}
```

通过这种方式，`JSDoc`可以为你定义更加复杂的类型结构，能为函数规范格式，便于复杂的开发。

## 进阶JSDoc用法 {#AdvancedJSDoc}

### 泛型类型 {#T}

如果你在处理泛型类型[^T]时，需要使用JSDoc来描述这些结构。例如，对于一个可以返回T类型的函数，可以这样定义：


```js twoslash
/**
 * 返回数组中的第一个元素
 * @template T
 * @param {T[]} arr - 传入的数组
 * @returns {T} 数组的第一个元素
 */
function first(arr) {
    return arr[0];
}
```

通过`@template`标签，你可以为函数指定泛型类型，使得代码更具通用性。

## this上下文 {#This}

在某些场景下，尤其是在`面向对象`的编程中，`this`的上下文可能不够明确。通过JSDoc，你可以为`this`标注类型(尽管KubeJS只有阉割的类)：

```js twoslash
/**
 * @this {Internal.Player}
 */
function logUsername() {
    console.log(this.username);
}
```

[^T]: 泛型（Generic）是指在编程语言中可以在编写代码时不确定具体数据类型，而在实际使用时才明确具体类型的编程技术。<br>这种机制允许函数、类或接口在处理不同类型的数据时保持通用性，避免重复编写同样逻辑但只适用于特定类型的代码。<br>KubeJS的开发一般用不到这一部分。

## JSDoc实际应用 {#Examples}

这一章节将简要列举一些具体应用来帮助各位理解JSDoc起到的关键作用。

### Param {#param}

这种情况通常出现在需要为`箭头函数`的参数指定具体类型时。一般来说，箭头函数的参数类型往往被定义为各种类的`父类`，这可能导致在编写代码时，得到的类型提示和补全**少于预期的情况**。

你还不需要知道父类意味着什么，只需知道为了解决这个问题，我们可以使用 JSDoc 注释，将默认的`Internal.Item`类型显式地标注为更具体的`Internal.SwordItem`。

这样不仅可以获得更加精准的类型提示，还能提升代码的可读性和可维护性。

::: code-group
```js [kubejs] twoslash
ItemEvents.modification(event=>{
    event.modify("diamond_sword", item=>{
        item.getTier()
    })
})
```
```js [kubejs] twoslash
ItemEvents.modification(event=>{
    event.modify("diamond_sword",/**@param {Internal.SwordItem} item */ item=>{
        item.getTier()
    })
})
```
:::

### Type {#Type}

在某些情况下，`JavaScript`的类型推断可能不足以提供准确/明确的类型提示。

这时候可以通过使用 `@type`注释来明确指定变量的类型，从而获得更精确的类型信息和代码提示。

::: code-group
```js [kubejs] twoslash
ItemEvents.entityInteracted(event => {
    const { entity, target, hand, server, level } = event;
    if (hand !== 'main_hand') return;
    entity.potionEffects.add('minecraft:night_vision', 200, 0, false, true);
})
```
```js [kubejs] twoslash
ItemEvents.entityInteracted(event => {
    const { entity, target, hand, server, level } = event;
    /**
     * @type {Internal.LivingEntity} 这是一个改变了类型的变量。
     */
    let livingentity = entity;
    entity.potionEffects.add('minecraft:night_vision', 200, 0, false, true);
    livingentity.potionEffects.add('minecraft:night_vision', 200, 0, false, true);
})
```
:::

## Q&A {#Q&A}

- Q: JSDoc能改变一个变量/参数具体的类型吗？

::: justify
答案是否定的，JSDoc所起到的只是**标记作用**，这意味着你必须明确自己要标记的参数的类型，且在该事件中的确作为所标记的类型存在，才能够使用JSDoc来重新标记类型。JSDoc 的作用仅仅是为开发工具提供类型提示，而不会改变 JavaScript 运行时的实际类型。
:::

- Q: JSDoc 可以自动推断类型吗？

::: justify
不可以，JSDoc 只能提供你手动指定的类型信息，不能自动推断变量或参数的类型。你需要明确使用`@type`、`@param`、`@returns`等标签来告知开发工具具体的类型。对于推断类型的功能，TypeScript 提供了更好的支持。
:::