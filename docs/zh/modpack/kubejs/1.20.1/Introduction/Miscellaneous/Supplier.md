# Supplier

>[!TIP] 提示
>此处介绍类型别名为Internal.Supplier_\<T\>的相关信息。

## 什么是Supplier

1. Supplier意为“供应商”，\<T\>表示泛型，意为这个“供应商”提供怎样类型的值，例如Supplier\<number\>它表示它会提供一个number类型的值。
2. Supplier常见于延迟求值，在Supplier被调用求值前，值还未被初始化。

## 在KubeJS中如何表示

- 示例：numberSupplier = () => 0; 它表示一个Supplier\<number\>
- 可以看到它只是一个箭头函数，Supplier\<number\>在这里通过一个返回值为number类型的箭头函数表示。
- 当需要求值时，就像调用普通函数那样。

```js [KubeJS]
let numberSupplier = () => 666;
let num = numberSupplier();
```

- 以Utils.lazy()函数为例，该函数接受一个Supplier\<any\>，这里传递了一个Supplier\<ResourceLocation\>，只有调用lazyLocation的get()方法时，new ResourceLocation("kubejs:demo")才会真正被执行并返回它的值。

```js [KubeJS]
let lazyLocation = Utils.lazy(() => new ResourceLocation("kubejs:demo"));
```

## 在Java中

- 在Java中，Supplier是一个函数式接口，该接口有一个get()方法用于求值，假如现有一个实现了Supplier接口的对象numberSupplier，要从这个对象上获取值则调用numberSupplier.get();
- 在Java中，使用与JavaScript箭头函数相似的lambda表达式创建Supplier实例。

```java
Supplier<String> lazyStr = () -> "Hello World!";

System.out.println(lazyStr.get());
```

>[!IMPORTANT] 小结
>明白在KubeJS中表示一个 Internal.Supplier_\<any\> 类型对象使用箭头函数 () => any
