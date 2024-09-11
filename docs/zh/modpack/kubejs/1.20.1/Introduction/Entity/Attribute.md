# 属性

- 一个属性（Attribute）有基础值（base）修饰符（Modifiers）总值（Total）。

## 前言

- 便捷查看属性注册名。

```js
/**
 * 在Probejs生成时会一并生成属性注册名的字符串枚举。
 * 这个变量没有任何用处，只是为了能够使这个jsdoc生效。
 * Ctrl左键点击下方这行的Attribute。
 * @type {Special.Attribute}
 */
let specialAttribute;
```

:::: warning **注意**
::: justify
只有“有生命的实体”才能使用属性操作。

反面例子：矿车，不是有生命的实体。

有生命的实体：继承LivingEntity类，不懂没关系，不影响下边的使用。
:::
::::

## 属性基础值

### 获取属性基础值

- 获取属性基础值。

- 语句：livingEntity.getAttribute(属性注册名).getBaseValue();

```js
// 借实体交互事件为例，获取尸壳的最大生命值属性
ItemEvents.entityInteracted(event => {
    const { entity, target, hand, level, server } = event;
    if (hand !== 'main_hand' || target.type !== 'minecraft:husk') return;

    /**
     * 启动编辑器类型提示可以使用jsdoc
     * 标注了livingEntity是Internal.LivingEntity类型
     * @type {Internal.LivingEntity}
     */
    const livingEntity = target;

    // 获取了最大生命值属性
    const maxHealth = livingEntity.getAttribute('generic.max_health').getBaseValue();
    server.tell(`最大生命值：${maxHealth}`);
});
```

### 修改属性基础值

- 修改属性基础值。

- 如果有修改属性的需要更加建议使用属性修饰符。

语句：livingEntity.getAttribute(属性注册名).setBaseValue(值);

```js
ItemEvents.entityInteracted(event => {
    const { entity, target, hand, level, server } = event;
    // 此处使用了僵尸作为实验生物
    // 在实际操作属性时需要确保被操作的实体类型是LivingEntity的子类
    if (hand !== 'main_hand' || target.type !== 'minecraft:husk') return;
    /**
     * 为了使编辑器启动类型提示可以使用jsdoc
     * 因为Attribute是LivingEntity的属性，而target是一个Entity
     * @type {Internal.LivingEntity}
     */
    const livingEntity = target;

    // 太长了不便观看 换行了下
    livingEntity
        .getAttribute('generic.max_health')
        .setBaseValue(30);
    
    const maxHealth = livingEntity
        .getAttribute('generic.max_health')
        .getBaseValue();
    server.tell(`最大生命值：${maxHealth}`);
});
```

## 属性修饰符

### 属性操作符

- 用于指定属性修饰符在属性总值的计算中如何计算。

- 类型："addition" | "multiply_total" | "multiply_base"

- "addition"：修饰符值以加法运算添加到属性总值中。

- "multiply_total"：以修饰符值为乘数，对属性总值进行乘法运算。

- "multiply_base"：以修饰符值为乘数，对属性基础值进行乘法运算。

### 添加属性修饰符

- 通过添加修饰符来修改属性。

- 语句：livingEntity.modifyAttribute(属性注册名, 字符串标识符, 值, 属性操作符);

```js
// 为尸壳添加一个20最大生命上限的属性修饰符
ItemEvents.entityInteracted(event => {
    const { entity, target, hand, level, server } = event;
    if (hand !== 'main_hand' || target.type !== 'minecraft:husk') return;
    /**
     * 为了使编辑器启动类型提示可以使用jsdoc
     * 因为Attribute是LivingEntity的属性，而target是一个Entity
     * @type {Internal.LivingEntity}
     */
    const livingEntity = target;

    // 太长了不便观看 换行了下
    livingEntity
        .modifyAttribute('generic.max_health', 'kubejs:maxHealth', 20, 'addition');
    
    const maxHealth = livingEntity
        .getAttribute('generic.max_health')
        .getBaseValue();
    server.tell(`最大生命值：${maxHealth}`);
});
```

### 获取属性修饰符

- 语句：livingEntity.getAttribute(属性注册名).getModifiers().forEach(attributeModifier=> {});

- 语句：livingEntity.getAttribute(属性注册名).getModifier(属性修饰符Uuid);

::: code-group

```js [通过遍历]
ItemEvents.entityInteracted(event => {
    const { entity, target, hand, level, server } = event;
    // 此处使用了僵尸作为实验生物
    // 在实际操作属性时需要确保被操作的实体类型是LivingEntity的子类
    if (hand !== 'main_hand' || target.type !== 'minecraft:husk') return;
    /**
     * 为了使编辑器启动类型提示可以使用jsdoc
     * 因为Attribute是LivingEntity的属性，而target是一个Entity
     * @type {Internal.LivingEntity}
     */
    const livingEntity = target;
    
    livingEntity.getAttribute('generic.max_health').getModifiers().forEach(
        (attributeModifier) => {
            // 获取修饰符的Uuid
            console.log(attributeModifier.id);
            // 获取修饰符的标识符
            console.log(attributeModifier.name);
            // 获取修饰符的值
            console.log(attributeModifier.amount);
            // 获取修饰符的操作符
            console.log(attributeModifier.operation);
        });
});
```

```js [通过修饰符Uuid]
// 通过属性修饰符Uuid
livingEntity.getAttribute('generic.max_health').getModifier(Uuid)
```

:::
