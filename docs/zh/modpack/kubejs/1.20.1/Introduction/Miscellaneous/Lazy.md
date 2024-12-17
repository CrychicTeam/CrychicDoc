---
progress: 100
state: preliminary
---
# Lazy

>[!TIP] 提示
>此处介绍Internal.Lazy\<T\>的相关信息。

## 文字释义

- “Lazy”一词多见于惰性求值等，假设现有一个类型为lazy\<string\>的对象，它表示可以提供一个类型是string的值，但该值在对该lazy对象首次调用get方法进行求值前都还未被创建。

## 类型定义

- 借助ProbeJS，得以查看其API文档，它实现了[Internal.Supplier](../Miscellaneous/Supplier.md)接口，这意味着它拥有该接口定义的方法，用于提供某个值。

```ts
class Lazy<T> implements Internal.Supplier<T> {
  static serviceLoader<T>(type: T): Internal.Lazy<T>;
  getClass(): typeof any;
  toString(): string;
  notifyAll(): void;
  notify(): void;
  static of<T>(supplier: Internal.Supplier_<T>): Internal.Lazy<T>;
  wait(arg0: number, arg1: number): void;
  hashCode(): number;
  wait(): void;
  forget(): void;
  wait(arg0: number): void;
  static of<T>(supplier: Internal.Supplier_<T>, expiresInMs: number): Internal.Lazy<T>;
  get(): T;
  equals(arg0: any): boolean;
  get class(): typeof any
}    
```

## 实例创建

- 可使用[Utils.lazy(param)](../GlobalScope/Utils.md#lazy)函数创建一个lazy对象。
- 示例：创建了一个延迟提供number类型值的lazy对象。

```js [KubeJS]
let lazyNum = Utils.lazy(() => 123);
```

## 使用方法

### get

- get(): T
- 该函数用于获取其内部持有的值。

```js [KubeJS]
let lazyNum = Utils.lazy(() => 123);
let num = lazyNum.get();
```

::: center
通过get对lazy对象进行求值
:::

### forget

- forget()
- 该函数使lazy对象丢弃内部已经创建的值，当下次调用get方法时，它会创建新值。

```js
let lazy = Utils.lazy(() => { 
  console.log("Supplier: Message"); 
  return "Message";
});
lazy.get();
lazy.forget();
lazy.get();
```

::: center
这里第二次调用get时依然会打印"Supplier: Message" 这证明内部原有的值被丢弃了
:::

### 相关信息

- 与[Internal.Supplier\<T\>](../Miscellaneous/Supplier.md)不同的是，Supplier每次求值都是获取一个新实例，而lazy对象只会在第一次求值时执行内部的Supplier求值，之后的求值均返回同一个实例。

```js
let lazy = Utils.lazy(() => { 
  console.log("Supplier: Message"); 
  return "Message";
});
```

::: center
只有第一次调用get方法才会打印"Supplier: Message" 这证明内部的Supplier只执行了一次
:::

>[!IMPORTANT] 小结
>知晓在KubeJS中创建一个lazy对象使用Utils.lazy方法。  
>知晓lazy的get与forget方法。  
>知晓lazy与Supplier的不同。
