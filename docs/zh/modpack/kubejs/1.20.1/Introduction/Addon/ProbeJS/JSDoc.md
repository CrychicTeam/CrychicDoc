# JSDoc
## 类型补全问题 {#Summary}
::: justify
在开发过程中，有时会遇到类型补全的问题。这个问题并非由 ProbeJS 直接导致，而是由于 KubeJS 各个事件返回的 参数 与 JavaScript 的 弱类型 特性所引发。通过合理使用 JSDoc 标记，可以在一定程度上优化这一问题。
:::

例如在[这个文档](../../Entity/PotionEffects.md)中，虽然可以使用 potionEffects 来修改实体的药水效果，但在尝试获取时可能会遇到问题：

```js twoslash
ItemEvents.entityInteracted(event => {
    const { entity, target, hand, server, level } = event;
    if (hand !== 'main_hand') return;

    // 将 potionEffects 显式声明为 any 类型
    entity.potionEffects.add('minecraft:night_vision', 200, 0, false, true);
//               ^?
})
```

可以看到，`potionEffects`并没有正确显示`Twoslash`提供的类型提示。

这是因为`potionEffects`是`Internal.LivingEntity`的方法，而 `ItemEvents.entityInteracted` 提供的参数类型为 `Entity`。由于`ProbeJS`生成的类型文件无法正确体现类的继承关系，这种问题在`KubeJS`的魔改中较为常见。

然而，通过使用 JSDoc 标记变量类型可以缓解这一问题：

```js twoslash
ItemEvents.entityInteracted(event => {
    const { entity, target, hand, server, level } = event;
    /**
     * @type {Internal.LivingEntity}
     */
    let livingentity = entity;
    entity.potionEffects.add('minecraft:night_vision', 200, 0, false, true);
    livingentity.potionEffects.add('minecraft:night_vision', 200, 0, false, true);
    //               ^?
})
```
<br>

可以看到，现在potionsEffects可以正常地获取到应
该方式能够让 potionEffects 的类型补全更加准确，从而提升开发体验。

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

通过@template标签，你可以为函数指定泛型类型，使得代码更具通用性。

## this上下文 {#This}

在某些场景下，尤其是在`面向对象`的编程中，this的上下文可能不够明确。通过JSDoc，你可以为this标注类型(尽管KubeJS只有阉割的类)：

```js twoslash
/**
 * @this {Internal.Player}
 */
function logUsername() {
    console.log(this.username);
}
```

[^T]: 泛型（Generic）是指在编程语言中可以在编写代码时不确定具体数据类型，而在实际使用时才明确具体类型的编程技术。<br>这种机制允许函数、类或接口在处理不同类型的数据时保持通用性，避免重复编写同样逻辑但只适用于特定类型的代码。