---
authors:
  - Gu-meng
editor: Gu-meng
---
# tag介绍

在[mcwiki](https://zh.minecraft.wiki/w/%E6%A0%87%E7%AD%BE?variant=zh-cn)中已经做出了相对官方的解释，所以本文就简要介绍一下tag在魔改过程中的作用

## tag的作用
在原版中有很多tag都会相对应的处理事件，这样可以极大程度的减少代码量

在我们魔改过程中个物品设置tag也可以极大程度的减少代码量，比如在合成时候，可以用任意木板合成木棍，如果给每一个木板都加上木棍的配方是比较繁琐的事情

原版就将木板设置为木板的tag，然后在合成时选择木板tag作为输入配方，这样来减少重复代码的操作

在机械动力，我们可以用扳手拆下原版的红石机器，也是机械动力给红石机器的方块里添加了一个名为`create:wrench_pickup`的tag，在扳手去拆下机器时，只需要判断该方块是否含有`create:wrench_pickup`，然后将其拆除

在原版也有类似的，比如方块挖掘等级就是靠tag，这里在[mcwiki](https://zh.minecraft.wiki/w/%E5%B7%A5%E5%85%B7%E6%9D%90%E6%96%99)有讲到
