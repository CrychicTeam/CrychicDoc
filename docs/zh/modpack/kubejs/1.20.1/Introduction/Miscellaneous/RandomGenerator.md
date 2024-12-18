---
progress: 80
state: preliminary
---
# RandomGenerator

>[!TIP] 提示
>此处介绍Internal.RandomGenerator接口的相关信息。

## 类型定义

- 借助ProbeJS查看其API文档，由于其是一个Interface（接口），它本身只定义方法名，参数，返回值类型，也无法直接使用其定义的方法，方法的具体实现交由接口的实现类来完成，要使用这些方法同样也在其实现类上使用。

```ts
interface RandomGenerator {
  ints(arg0: number, arg1: number): Internal.IntStream;
  nextFloat(arg0: number): number;
  getDefault(): this;
  abstract nextLong(): number;
  nextDouble(): number;
  nextLong(arg0: number, arg1: number): number;
  nextGaussian(): number;
  doubles(arg0: number, arg1: number, arg2: number): Internal.DoubleStream;
  nextDouble(arg0: number): number;
  nextInt(arg0: number): number;
  nextFloat(): number;
  nextLong(arg0: number): number;
  ints(arg0: number, arg1: number, arg2: number): Internal.IntStream;
  nextFloat(arg0: number, arg1: number): number;
  of(arg0: string): this;
  doubles(): Internal.DoubleStream;
  nextInt(): number;
  nextBytes(arg0: number[]): void;
  longs(arg0: number): Internal.LongStream;
  longs(arg0: number, arg1: number, arg2: number): Internal.LongStream;
  nextBoolean(): boolean;
  nextDouble(arg0: number, arg1: number): number;
  nextExponential(): number;
  longs(arg0: number, arg1: number): Internal.LongStream;
  nextInt(arg0: number, arg1: number): number;
  longs(): Internal.LongStream;
  isDeprecated(): boolean;
  doubles(arg0: number): Internal.DoubleStream;
  nextGaussian(arg0: number, arg1: number): number;
  ints(): Internal.IntStream;
  ints(arg0: number): Internal.IntStream;
  doubles(arg0: number, arg1: number): Internal.DoubleStream;
  get "default"(): Internal.RandomGenerator
  get deprecated(): boolean
  (): number;
}
```

## 实例创建

- [Random](Random.md)类是该接口的一个实现类，可以通过[Utils.getRandom()](../GlobalScope/Utils.md#getrandom)来创建一个Random实例。

## 如何使用

>[!WARNING] 提示
>关于返回“整数流”、“浮点数流”等函数的信息需要确切补充。

### ints

- ints(): Internal.IntStream;
- 生成一个整数流。
- ints(arg0: number): Internal.IntStream;
- 生成一个整数流，每个数之间的间隔为arg0。
- inits(arg0: number, arg1: number): Internal.IntStream;
- 生成一个指定范围内的整数流。
- ints(arg0: number, arg1: number, arg2: number): Internal.IntStream;
- 生成一个指定范围内的整数流。

### nextFloat

- nextFloat(): number;
- 生成一个在\[0.0, 1.0\)范围内的随机浮点数。
- nextFloat(arg0: number): number;
- 生成一个在\[0.0, arg0\)范围内的随机浮点数。

### getDefault

- getDefault(): this;
- 返回默认的随机数生成器实例。

### nextLong

- nextLong(): number;
- 生成一个随机长整型数。
- nextLong(arg0: number, arg1: number): number;
- 生成一个在\[arg0, arg1\)范围内的随机长整数。

### nextDouble

- nextDouble(): number;
- 生成一个在\[0.0, 1.0\)范围内的随机双精度浮点数。
- nextDouble(arg0: number): number;
- 生成一个在\[0.0, arg0\)范围内的随机双精度浮点数。
- nextDouble(arg0: number, arg1: number): number;
- 生成一个在\[arg0, arg1\)范围内的随机双精度浮点数。

### nextGaussian

- nextGaussian(): number;
- 生成一个符合高斯分布（正态分布）的随机双精度浮点数。
- nextGaussian(arg0: number, arg1: number): number;
- 生成一个符合指定均值和标准差的高斯分布的随机双精度浮点数。

### doubles

- doubles(arg0: number): Internal.DoubleStream;
- 生成一个双精度浮点数流。
- doubles(arg0: number, arg1: number): Internal.DoubleStream;
- 生成一个在\[arg0, arg1\)之间的双精度浮点数流。
- doubles(arg0: number, arg1: number, arg2: number): Internal.DoubleStream;
- 生成一个指定范围内的双精度浮点数流。

### nextInt

- nextInt(arg0: number): number;
- 生成一个在\[0, arg0\)范围内的随机整数。
- nextInt(arg0: number, arg1: number): number;
- 生成一个在\[arg0, arg1\)范围内的随机整数。

### nextBytes

- nextBytes(arg0: number[]): void;
- 用随机的Byte类型数字填充给定的数组。

### longs

- longs(): Internal.LongStream;
- 返回长整型数流。
- longs(arg0: number): Internal.LongStream;
- 返回长整型数流。
- longs(arg0: number, arg1: number): Internal.LongStream;
- 返回一个在\[arg0, arg1\)范围的长整型数流。
- longs(arg0: number, arg1: number, arg2: number): Internal.LongStream;
- 返回长整型数流。

### nextBoolean

- nextBoolean(): boolean;
- 生成一个随机的布尔值。

### nextExponential

- nextExponential(): number;
- 生成一个符合指数分布的随机双精度浮点数。

### isDeprecated

- isDeprecated(): boolean;
- 检查当前随机数生成器是否已经被弃用。
