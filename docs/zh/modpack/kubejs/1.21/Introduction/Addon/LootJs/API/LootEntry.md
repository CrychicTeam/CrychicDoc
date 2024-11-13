# LootEntry

战利品抽取项用于在战利品表中创建结果物品。它们被分为不同类型的抽取项。

- Simple Entries:
  
  - `ItemEntry`: 向随机池中添加物品
  - `EmptyEntry`: 向随机池中添加空抽取项
  - `TagEntry`: 将标签中的物品添加到随机池中
  - `TableReferenceEntry`: 引用其他表中的物品
  - `DynamicEntry`: 在Minecraft中用于潜影盒。

- Composite Entries:

  - `AlternativeEntry`: 仅添加满足谓词的第一个抽取项。这可以用来作为简单的 if-else 判断
  - `SequenceEntry`: 添加所有抽取项，但是遇到不满足的抽取项后面的不会再添加进去
  - `GroupEntry`: 将分组中的抽取项全部加入随机池中
  
- Methods:

  - `.getType()`
  - `.isSimple()`
  - `.isItem()`
  - `.isTag()`
  - `.isReference()`
  - `.isComposite()`
  - `.isAlternative()`
  - `.isSequence()`
  - `.isGroup()`
  - `.getConditions(): LootConditionsList`
  - `.when((conditions: LootConditionsList) => { ... })`

## Simple Entries

单一抽取项可以包含 [`LootFunctions`](LootFunctions.md)，并且会在抽取项被抽取时执行。

- Methods:

  - `.getFunctions(): LootFunctionsList`
  - `.apply((functions: LootFunctionsList) => { ... })`
  - `.getWeight()`
  - `.setWeight(weight: number)`
  - `.withWeight(weight: number)`
  - `.getQuality()`
  - `.setQuality(quality: number)`
  - `.withQuality(quality: number)`

Weight: 相对其他项的选中此项的权重。计算公式为：`weight / sum(weights of all entries)`。

Quality: 根据 `killer_entity` 的属性 `generic.luck` 修改抽取项的权重。计算方式为 `floor(weight + (quality * generic.luck))`

```js
// `withWeight` 和 `withQuality` 将会返回他们自身,
// 因此我们很轻松的可使用链式结构调用他们
const entry = LootEntry.of("minecraft:stick")
                       .withWeight(42)
                       .withQuality(3)
```

### ItemEntry

语法：

- `LootEntry.of(item)`
- `LootEntry.of(item, count: NumberProvider)`

方法：

- `.getItem()`
- `.setItem(item: Item)`
- `.test(filter: ItemFilter)`

```js
LootEntry.of("minecraft:diamond")

LootEntry.of("minecraft:diamond", [5, 10])
```

```js
const entry = LootEntry.of("minecraft:diamond_sword")

if (entry.test(ItemFilter.SWORD)) {

}
```

### EmptyEntry

语法：

- `LootEntry.empty()`

### TagEntry

语法：

- `LootEntry.tag(tag: string)`
- `LootEntry.tag(tag: string, expanded: boolean)`

方法：

- `.getTag()`
- `.setTag(tag: string)`
- `.setExpanded(expanded: boolean)`
- `.getExpanded()`
- `.isTag(tag: string): boolean`

```js
LootEntry.tag("minecraft:pickaxes")

LootEntry.tag("minecraft:pickaxes", true)
```

### TableReferenceEntry

语法：

- `LootEntry.reference(table: ResourceLocation | string)`

方法：

- `.getLocation()`
- `.setLocation(table: string)`

```js
LootEntry.reference("minecraft:chests/abandoned_mineshaft")
```

## Composite Entries

语法：

- `LootEntry.alternative(...entries: LootEntry[])`
- `LootEntry.sequence(...entries: LootEntry[])`
- `LootEntry.group(...entries: LootEntry[])`

方法:

- `.getEntries(): LootEntryList`
- `.entries((entries: LootEntryList) => { ... })`

```js
/**
 * 当抽取项被滚动时:
 * 如果随机到钻石, 那么就添加钻石, 剩下的抽取项会被跳过.
 */
LootEntry.alternative(
    LootEntry.of("minecraft:diamond").when(c => c.randomChance(0.5)),
    LootEntry.of("minecraft:emerald").when(c => c.randomChance(0.5)),
    LootEntry.of("minecraft:iron_ingot")
)
```
