# Range

在使用 LootJS 时，我们经常需要指定两个值之间的范围。对于这些情况，我们有 `Range` 。当函数需要 `Range` 时，我们可以通过直接向函数传递 `number` 或 `number[]` 来轻松使用类型包装器。

语法：

- `Range.exactly(value: number)`
- `Range.atLeast(value: number)`
- `Range.atMost(value: number)`
- `Range.between(min: number, max: number)`

```js
const condition = LootCondition.distance(10)
```

```js
const condition = LootCondition.distance([0, 10])
```

```js
const condition = LootCondition.distance(Range.atLeast(20))
```
