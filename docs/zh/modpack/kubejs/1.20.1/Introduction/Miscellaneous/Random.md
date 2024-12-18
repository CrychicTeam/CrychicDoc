---
progress: 80
state: preliminary
---
# Random

>[!TIP] 提示
>此处介绍Internal.Random的相关信息。

## 类型定义

- 通过ProbeJS查看其API文档。
- 其实现RandomGenerator与Serializable接口，意味着其具有生成随机数的功能，Serializable只是一个标记其为可序列化的类的接口，其没有定义任何方法，这里无需关注。

```ts
class Random implements Internal.RandomGenerator, Internal.Serializable {
  constructor()
  constructor(arg0: number)
  getClass(): typeof any;
  ints(arg0: number, arg1: number): Internal.IntStream;
  nextFloat(arg0: number): number;
  static getDefault(): Internal.RandomGenerator;
  nextLong(): number;
  nextDouble(): number;
  nextLong(arg0: number, arg1: number): number;
  notify(): void;
  nextGaussian(): number;
  wait(arg0: number, arg1: number): void;
  doubles(arg0: number, arg1: number, arg2: number): Internal.DoubleStream;
  nextDouble(arg0: number): number;
  nextInt(arg0: number): number;
  nextFloat(): number;
  nextLong(arg0: number): number;
  ints(arg0: number, arg1: number, arg2: number): Internal.IntStream;
  nextFloat(arg0: number, arg1: number): number;
  static of(arg0: string): Internal.RandomGenerator;
  doubles(): Internal.DoubleStream;
  nextInt(): number;
  nextBytes(arg0: number[]): void;
  longs(arg0: number): Internal.LongStream;
  longs(arg0: number, arg1: number, arg2: number): Internal.LongStream;
  toString(): string;
  nextBoolean(): boolean;
  notifyAll(): void;
  nextDouble(arg0: number, arg1: number): number;
  setSeed(arg0: number): void;
  nextExponential(): number;
  longs(arg0: number, arg1: number): Internal.LongStream;
  nextInt(arg0: number, arg1: number): number;
  longs(): Internal.LongStream;
  isDeprecated(): boolean;
  doubles(arg0: number): Internal.DoubleStream;
  nextGaussian(arg0: number, arg1: number): number;
  ints(): Internal.IntStream;
  ints(arg0: number): Internal.IntStream;
  hashCode(): number;
  wait(): void;
  wait(arg0: number): void;
  doubles(arg0: number, arg1: number): Internal.DoubleStream;
  equals(arg0: any): boolean;
  get class(): typeof any
  get "default"(): Internal.RandomGenerator
  set seed(arg0: number)
  get deprecated(): boolean
}
```

## 实例创建

- 通过Utils的getRandom方法获取Random对象，注意该方法始终返回同一个Random对象。

```js [KubeJS]
let random = Utils.getRandom();
```

## 如何使用

### 生成随机数

- 参见[RandomGenerator](../Miscellaneous/RandomGenerator.md)。
