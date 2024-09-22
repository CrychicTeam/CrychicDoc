---
authors: ['Wudji']
---

# 2.4 配方ID

本节由@[Qi-Month (柒月)](https://github.com/Qi-Month)编写。

* **关于Recipe ID(配方ID)**
  * 配方ID往往是配方文件的所在目录,在开发中用于查看配方类型以及写法时显得尤为重要
  * 常见的例子有删除配方,覆盖配方还有使用`custom`
  * 在添加`JEI`后游戏内`F3+H`开启`高级提示框`后便可查看\
    ![debug.png](https://i1.mcobj.com/imgb/u18prz/20240705\_66880e45a0358.png)
  * 在JEI内随便打开一个配方文件将光标放在输出上便可查看\
    ![id-view.png](https://i1.mcobj.com/imgb/u18prz/20240705\_66880e45ad7eb.png)
  * 根据上图浇筑铁锭的配方所示,根据配方ID我们可以得知配方文件在 `ModFile.jar/data/tconstruct/recipes/smeltery/casting/metal/iron/ingot_gold_cast.json`
  * JEI给我们提供了复制配方ID的按键,我们自行配置即可(默认没有绑定任何按键,按照自己的习惯绑定即可)\
    ![key-binding.png](https://i1.mcobj.com/imgb/u18prz/20240705\_66880e45a6fb0.png)
  * `REI`和`EMI`其实也可以,但是本教程只教`JEI`,使用REI或EMI的朋友请自行在设置内寻找相关按键
*   **使用**

    * 在写配方时后面加上`.id`方法时如果填入了原配方的ID会将原配方直接覆盖

    ```js
    ServerEvents.recipes((event) => {
          event.recipes.kubejs.shaped("minecraft:furnace", [
            "AAA",
            "AAA",
          	"AAA"
          ], {
            A: "#forge:ingots/iron"
        }).id("minecraft:furnace") // 注意这不是写输出,是写配方ID
    })
    ```

    * 如果每个配方都写一个json会很麻烦且不好管理,所以同时也可以根据配方ID找到配方文件后直接复制里面的内容供`custom`使用,典型的例子就是

    ```js
    ServerEvents.recipes((event) => {
          event.custom({
      	    "type": "createmetallurgy:casting_in_table",
          	"ingredients": [
      		    { "fluid": "createmetallurgy:molten_gold", "amount": 90 },
      		    { "tag": "forge:plates" },
      	    ],
      	    "results": [{ "item": "createmetallurgy:graphite_plate_mold" }],
    	    "processingTime": 90
        })
    })
    ```

    * 提一嘴`custom`也是可以使用配方ID进行配方覆盖的
