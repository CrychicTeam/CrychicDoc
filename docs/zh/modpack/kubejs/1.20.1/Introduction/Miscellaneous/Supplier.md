---
progress: 100
state: preliminary
---

# Supplier

::: tip 提示
此处介绍Internal.Supplier_\<T\>的相关信息。
:::

## 文字释义

1. Supplier意为“供应商”，\<T\>表示泛型，意为这个“供应商”提供怎样类型的值，例如Supplier\<number\>它表示它会提供一个number类型的值。
2. Supplier常见于延迟求值，在Supplier被调用求值前，值还未被初始化。

## 类型定义

- 借助ProbeJS，查看其API文档。

```ts
type Supplier_<T> = Supplier<T> | (()=> T);
```

::: center
显然，该类型别名中引用了两个类型，在这里我们使用第二个即可
:::

## 实例创建

- 在KubeJS中使用无参数有返回值的箭头函数来表示一个Internal.Supplier_\<T\>类型。
- 示例：numberSupplier = () =\> 0; 它表示一个Supplier\<number\>类型对象。
- 当需要求值时，就像调用普通函数那样写上括号()即可。

```js [KubeJS]
let numberSupplier = () => 666;
```

- 以Utils.lazy()函数为例，该函数接受一个Supplier\<any\>，这里传递了一个Supplier\<ResourceLocation\>，只有调用lazyLocation的get()方法时，new ResourceLocation("kubejs:demo")才会真正被执行并返回它的值。

```js [KubeJS]
let lazyLocation = Utils.lazy(() => new ResourceLocation("kubejs:demo"));
```

## 如何使用

- 在KubeJS中，对于我们自己以箭头函数方式声明的Supplier，就像调用普通函数那样调用即可得到结果。

```js [KubeJS]
let numberSupplier = () => 666;
let num = numberSupplier();
```

::: center
这里num即为求出的值。
:::

## 相关信息

- 在Java中，Supplier是一个函数式接口，该接口有一个get()方法用于求值，假如现有一个实现了Supplier接口的对象numberSupplier，要从这个对象上获取值则调用numberSupplier.get();
- 在Java中，使用与JavaScript箭头函数相似的lambda表达式创建Supplier实例。

```java
Supplier<String> lazyStr = () -> "Hello World!";

System.out.println(lazyStr.get());
```

>[!IMPORTANT] 小结
>知晓在KubeJS中表示一个Internal.Supplier_\<any\>类型对象使用箭头函数 () => any  
>知晓如何对声明出的Supplier求值。
