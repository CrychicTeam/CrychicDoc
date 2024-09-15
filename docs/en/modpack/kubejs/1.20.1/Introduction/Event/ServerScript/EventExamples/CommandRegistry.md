# 命令注册事件

## 示例

- 效果：/command \<string\> 服务器会发送字符串参数消息。

```js
ServerEvents.commandRegistry(event => {
    const { commands, arguments } = event;
    event.register(
        // 命令的起始字符串：/command
        commands.literal('command')
            // 命令运行需要的条件：执行源必须有2级及以上权限（管理员）也可以没有条件或者其他条件。
            .requires(commandSourceStack => commandSourceStack.hasPermission(2))
            // 然后命令需要什么参数：参数名称，参数类型（传入事件本身）
            .then(commands.argument('string', arguments.STRING.create(event))
                // 命令执行时会发生什么
                .executes(commandSourceStack => {
                    // 获取了命令中的参数
                    const arg = arguments.STRING.getResult(commandSourceStack, 'arg');
                    if (arg === null) {
                        return 0;
                    }
                    // 命令逻辑：使服务器发送了这条消息
                    commandSourceStack.getSource().getServer().tell(arg);
                    return 1;
                })
            )

    )
})
```
