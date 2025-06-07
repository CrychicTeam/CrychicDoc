---
authors:
  - Gu-meng
editor: Gu-meng
---
# LoadClass使用
> 孤梦注：千万不要玩Java.loadClass啊，这东西比较废人

在使用Java.loadClass时，孤梦建议是你有一定的Java基础，至少得知道类、类路径、静态、对象、访问修饰符等概念

在绝大多数时候我们都用不到loadClass

loadclass就是kjs提供给我们用来加载java类的

这使得KubeJS的可玩性非常高，比如[注册AE存储元件和对应的组件](../KubeJSProjects/Meng/RegComponent&Storage.md)就使用到了非常多的loadClass

## 关于使用
```js
let class = Java.loadClass("类路径");
```
在使用ProbeJS时，在绝大多数时候都会为你补齐类路径，但是ProbeJS并不是所有的类都有对应的路径，所以这个时候你可以去翻一下对应模组的GitHub开源链接，查找到你需要的类，并将其路径复制过来

在你使用`Java.loadClass`class时候，你可以直接去调用到里面的`public static`的方法或者变量常量等

但是有些内容调用不到，你就可以和写java一样去`new`这个类

沿用上面的内容
```js
let newClass = new class();
```
这里的`new class()`括号里可能会需要传参有的也不需要，这里根据对应类提供的源码来写

## 总结
总的来说你如果需要更好的使用`Java.loadClass`你是需要有一定的Java基础，这不是靠一个篇章能说明白的事情
