---
title: 47 命令
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 47 命令 相关教程
image: ./covers/5e1fe9e0ebc3649e3cf2c94f4b8673f31c545ea3.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考

https://boson.v2mcdev.com/command/command.html

## 命令

命令相对于旧版本的变动并不是很大，我们这里也是参考之前的boson的教程的内容，讲解下命令的添加。

下面是我们添加了一个命令，这个命令是输入 /Examplemod test 然后执行在聊天栏输入cmd.example.hello，你可以去做些其他的事情，不过这里就让他输出了hello。
```java
        @SubscribeEvent
        public static void onServerStaring(RegisterCommandsEvent event) {
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
            LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
                    Commands.literal(ExampleMod.MODID).then(
                            Commands.literal("test")
                                    .requires((commandSourceStack -> commandSourceStack.hasPermission(2)))
                                    .executes(ExampleCommand.INSTANCE)
                    )
            );
        }

```

我们分别看看这个代码中的内容

```java
// 首先我们要使用的事件是RegisterCommandsEvent，位于forge总线中，
@Mod.EventBusSubscriber(modid = ExampleMod.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
// 下面是关于注册的部分
// 我们要获得对于的分发器dispatcher，获得方法就是通过event获得
CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
// 之后通过dispatcher添加新的command，我们使用分发器的register方法进行注册
// 该方法要求要求一个LiteralArgumentBuilder作为参数，其中的S泛型为CommandSourceStack
public LiteralCommandNode<S> register(LiteralArgumentBuilder<S> command) {
// 对于的获得LiteralArgumentBuilder，我们是通过Commands的静态方法literal获得的
// 其中的string 参数就是注册的命令名字，你在调用时候也需要输入的这个名字
public static LiteralArgumentBuilder<CommandSourceStack> literal(String pName) {
// 在我们的代码中这个名称是ExampleMod.MODID，所以我们可以直接使用Commands.literal(ExampleMod.MODID)
Commands.literal(ExampleMod.MODID)
// 之后通过then或者executes方法添加子命令，这里我们使用then方法，其中的参数是一个LiteralArgumentBuilder，
// then 表示该命令没有结束 需要添加子命令
public T then(ArgumentBuilder<S, ?> argument) {
// 你也可以给你的命令添加一些限制处理，比如权限限制，这里我们使用requires方法，其中的参数是一个Predicate<CommandSourceStack>，
// Predicate是一个函数式接口，其中的参数为CommandSourceStack，返回值为boolean，表示是否满足条件
public T requires(Predicate<S> requirement) {
// 之后我们通过executes方法添加执行的命令，其中的参数是一个Command，
// 这里的command就是命令执行的程序，这个程序由我们来写。
public T executes(Command<S> command) {
```

现在让我们来看看.executes(ExampleCommand.INSTANCE)中的ExampleCommand是怎么操作的。


```java
// 首先我们需要一个Command类，我们实现Command接口，并实现其中的run方法，该方法就是命令满足条件时候的调用方法
public class ExampleCommand implements Command<CommandSourceStack> {
    // 这里定义的INSTANCE就是我们在注册命令时候要求传入的Command实例
    public static final Command<CommandSourceStack> INSTANCE = new ExampleCommand();
    @Override
    public int run(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
            // 这里就是给发送命令的人发了一条消息。
        commandContext.getSource().sendSystemMessage(Component.literal("cmd.example.hello"));
        return 0;
    }
}
```

原版的命令位于该包下：package net.minecraft.server.commands;
大家可以参考学习
