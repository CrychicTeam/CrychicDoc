---
progress: 100
state: preliminary
---
# Java

::: tip 提示
此条目信息为KubeJS提供的全局对象Java。
:::

## 对象结构

```ts
class JavaWrapper {
  loadClass(className: string): Internal.any;    
  tryLoadClass(className: string): any;
}
```

::: center
这里省略了用不到的函数与属性
:::

## 方法描述

### loadClass

- loadClass(className: string): Internal.any;
- 根据提供的完全限定类名字符串（可以理解为路径，但并不是系统文件中的路径），通过反射获取类对象，如果类不存在则抛出错误。
- 例：反射Entity类。

```js [KubeJS]
const Entity = Java.loadClass('net.minecraft.world.entity.Entity');
```

::: center
这样就可以如同普通的JavaScript对象一般使用这个类了
:::

### tryLoadClass

- tryLoadClass(className: string): any;
- 根据提供的完全限定类名字符串（可以理解为路径，但并不是系统文件中的路径），通过反射获取类对象，如果类不存在则返回null，不会抛出错误。

## 注意事项

::: warning 注意
KubeJS内无法继承（extends）类，无法实现（implements）接口。  
在本例中，Entity是一个抽象类（abstract class）我们无法继承它来创建其子类。  
:::
