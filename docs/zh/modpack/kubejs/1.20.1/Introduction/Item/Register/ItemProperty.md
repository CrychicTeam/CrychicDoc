# 物品属性

- 为物品增加功能，根据物品类型的不同，有不同的可以设置的属性。
- `通用`基础物品的属性、函数是全类型物品通用的。

## 基础物品

- 在注册物品中，已经有了形如下方的代码，这个物品还没有任何功能，需要通过调用其提供的函数为它设置属性。
- 因为create函数返回的是`Internal.BasicItemJS$Builder`对象，因此调用的函数也是来源于它，后续许多函数都同样的返回该对象，这使得代码可以链式调用。
- 如果上一个函数返回的值不再是这个对象，就不能再使用链式调用了，可以调整语句顺序或将返回的对象存储在变量里，以便于对其操作。

::: code-group

```js [注册物品]
StartupEvents.registry("minecraft:item", event => {
  let basicItem = event.create("basic_item")
})
```

```js [链式调用]
StartupEvents.registry("minecraft:item", event => {
  let basicItem = event.create("basic_item")
      .barColor((itemStack) => 0xFFFFFFFF)
      .barWidth((item) => 10)
})
```

:::

### 耐久条颜色

- barColor接受一个Internal.Function_\<T, R\>对象，意为接受T类型变量，返回R类型变量，在这里是一个接受ItemStack返回Color的函数，在原版中，判断物品耐久度高于一定比例是绿色，低于一定比例变成黄色或红色。

```js [KubeJS]
StartupEvents.registry("minecraft:item", event => {
  let basicItem = event.create("basic_item").barColor((itemStack) => 0xFFFFFFFF)
})
```

### 耐久条宽度

- barWidth函数接受一个Internal.ToIntFunction_\<Internal.ItemStack\>对象，意为接受ItemStack，返回int数字的函数

```js [KubeJS]
StartupEvents.registry("minecraft:item", event => {
  let basicItem = event.create("basic_item").barWidth((item) => 10)
})
```

### 纹理上色

- 此处尚不能参考，需要补充。
- color函数接受一个纹理的索引number，与Internal.ItemTintFunction_对象，该对象是接受Item与number，返回color的函数。

```js
StartupEvents.registry("minecraft:item", event => {
  let basicItem = event.create("basic_item").color()
})
```

### 设置容器

- 用于配方，在原版中，牛奶桶的容器是桶。
- containerItem函数接受一个物品id

```js [KubeJS]
StartupEvents.registry("minecraft:item", event => {
  let basicItem = event.create("basic_item").containerItem("minecraft:bucket")
})
```

### 设置显示名

- 例：使用翻译键来代替硬编码的物品名称
- displayName函数接受一个Component

```js [KubeJS]
StartupEvents.registry("minecraft:item", event => {
  let basicItem = event.create("basic_item");
  basicItem.displayName(Component.translatable("item.kubejs.basic_item"))
})
```

### 覆盖语言文件的显示名

- 该函数使物品的显示名不能被语言文件覆盖

::: code-group

```js [KubeJS]
StartupEvents.registry("minecraft:item", event => {
  let basicItem = event.create("basic_item");
  basicItem.displayName(Component.translatable("item.kubejs.basic_item")).formattedDisplayName()
})
```

```js [KubeJS]
StartupEvents.registry("minecraft:item", event => {
  let basicItem = event.create("basic_item");
  basicItem.formattedDisplayName(Component.translatable("item.kubejs.basic_item"))
})
```

:::

### 燃烧时间

- 设置物品在熔炉内作为燃料的燃烧时间，默认为0，即不能作为燃料。
- burnTime(number)

```js [KubeJS]
StartupEvents.registry("minecraft:item", event => {
  let basicItem = event.create("basic_item");

  basicItem.burnTime(400);
})
```

### 设置ItemProperties

- 在createItemProperties()函数后，返回值类型是
- 在食物、耐久度等都是通过设置ItemProperties来实现。

```js [KubeJS]
StartupEvents.registry("minecraft:item", event => {
  let basicItem = event.create("basic_item");
  let itemProperties = basicItem.createItemProperties();
})
```

#### 耐久度

- 设置物品的耐久度 durability(number)
- 设置物品的默认耐久度 defaultDurability(number)

```js [KubeJS]
StartupEvents.registry("minecraft:item", event => {
  let basicItem = event.create("basic_item");
  let itemProperties = basicItem.createItemProperties();

  itemProperties.durability(100).defaultDurability(100);
})
```

#### 抗火性

```js [KubeJS]
StartupEvents.registry("minecraft:item", event => {
  let basicItem = event.create("basic_item");
  let itemProperties = basicItem.createItemProperties();

})
```

#### 稀有度

- 设置物品稀有度，默认为常见common

::: details 稀有度

|   描述    |   值    |
|:---------:|:---------:|
|   常见    |    'common'    |
|   稀有    |    'uncommon'    |
|   珍贵    |    'rare'    |
|   史诗    |    'epic'    |

:::

```js [KubeJS]
StartupEvents.registry("minecraft:item", event => {
  let basicItem = event.create("basic_item");
  let itemProperties = basicItem.createItemProperties();

  itemProperties.rarity('uncommon')

})
```

#### 堆叠上限

- stacksTo(number) 默认堆叠上限是64

```js [KubeJS]
StartupEvents.registry("minecraft:item", event => {
  let basicItem = event.create("basic_item");
  let itemProperties = basicItem.createItemProperties();
  itemProperties.stacksTo(16);
})
```

#### 不可修理

- 需要测试

```js [KubeJS]
StartupEvents.registry("minecraft:item", event => {
  let basicItem = event.create("basic_item");
  let itemProperties = basicItem.createItemProperties();

  itemProperties.setNoRepair();
})
```

#### 食物

- 需要补充
- food()接受Internal.FoodProperties_对象

```js [KubeJS]
StartupEvents.registry("minecraft:item", event => {
  let basicItem = event.create("basic_item");
  let itemProperties = basicItem.createItemProperties();

  itemProperties.food();
})
```
