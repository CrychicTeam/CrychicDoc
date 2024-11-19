::: carousels#{"cycle": false, "interval": 2800, "undelimiters": true}
@tab
![EmendatusEnigmaticaJS](/imgs/EmendatusEnigmaticaJS/eejs_1.png)
@tab
![EmendatusEnigmaticaJS](/imgs/EmendatusEnigmaticaJS/eejs_2.png)
:::

[[TOC]]

## 关于 EmendatusEnigmaticaJS

EmendatusEnigmaticaJS最初来自于整合包[Omniworld](https://n-wither.github.io/projects/omniworld/)，但是Omniworld目前任然停留在1.20.1，于是我修改了部分代码，将其带到1.21。

需要更多资源请查看[我的仓库](https://github.com/QiHuang02/StellarisSpace/tree/main/kubejs/assets/emendatusenigmatica)

> [!important]
>
> 目前仍然处于 ==WIP== 状态
>
> ToDoList：
>
> - [x] 修复方块类型的模型和纹理问题
> - [x] 添加自动创建矿物方块类型的LootTable文件集成
> - [x] 添加锭、宝石、粒、板、棒、齿轮等物品的注册
> - [x] 添加对Mekanism的集成（仅完成物品的注册，化学品相关暂缓）
> - [x] 支持使用自定义纹理
> - [ ] 添加液体类型的集成
> - [ ] 添加本地化集成
> - [ ] 添加配方集成
> - [ ] 自动将注册的物品放入自定义的创造模式物品栏中
> - [ ] ~~添加工具类型（稿、斧、锹、剑）的注册~~
> - [ ] ~~添加装备类型（头盔、胸甲、护腿、鞋子）的注册~~
> - [ ] ~~添加矿物生成~~
> - [ ] 优化代码结构

## 如何使用

在这里可以添加定义材料

### 参数

一些必须的参数: `name`、`type`、`processedTypes`

一些可选的参数: `color`、`texture`、`drop`、`burnTime`、`gemTemplate`、`strata`、`harvestLevel`

### 参数解释
- `name`: 定义材料的名称
- `type`: 定义材料的类型,可选的有`metal`,`alloy`,`gem`,`special`
  - `metal`: 不支持 `gem`
  - `alloy`: 不支持 `gem`、`ore`、`raw`
  - `gem`: 不支持 `ingot`, `nugget`
  - `special`: 都支持，但是纹理需要自己准备
- `processedTypes`: 定义了材料可以有哪些形态
  - 全部的可支持的形态有: `ore`, `raw`, `gem`, `ingot`, `nugget`, `dust`, `plate`, `gear`, `rod`, `storage_block`, `mekanism`, `bloodmagic`, `crushed`
- `color`: 因为是使用的灰度模板纹理、所以需要使用该属性为其上颜色
- `texture`: 使用自定义的纹理
- `drop`: 当设置改属性后，会自动生成该材料的矿物方块的战利品表，当加工形态中有`ore`时必须设置
- `burnTime`: 定义该材料的燃烧值
- `gemTemplate`: 定义了该材料使用的宝石纹理的模版，当`type`为`gem`时必须设置
- `strata`: 定义了该材料的容矿物，当加工形态中有`ore`时必须设置
- `harvestLevel`: 定义了该材料的方块形态的加工形态的挖掘等级

## 源代码

### Strata.js

这一部分代码主要定义了矿物的地层，也是指容纳矿物的岩石。

```JS
// Strata.js
// priority: 200

let EE_STRATAS = {
    stone: {
        name: 'stone',
        texture: 'minecraft:block/stone',
        fill: 'minecraft:stone',
        hardness: 1.5,
        resistance: 6,
        tool: 'pickaxe'
    },
    andesite: {
        name: 'andesite',
        texture: 'minecraft:block/andesite',
        fill: 'minecraft:andesite',
        hardness: 1.5,
        resistance: 6,
        tool: 'pickaxe'
    },
    diorite: {
        name: 'diorite',
        texture: 'minecraft:block/diorite',
        fill: 'minecraft:diorite',
        hardness: 1.5,
        resistance: 6,
        tool: 'pickaxe'
    },
    granite: {
        name: 'granite',
        texture: 'minecraft:block/granite',
        fill: 'minecraft:granite',
        hardness: 1.5,
        resistance: 6,
        tool: 'pickaxe'
    },
    deepslate: {
        name: 'deepslate',
        texture: 'minecraft:block/deepslate',
        fill: 'minecraft:deepslate',
        hardness: 3,
        resistance: 6,
        tool: 'pickaxe'
    },
    netherrack: {
        name: 'netherrack',
        texture: 'minecraft:block/netherrack',
        fill: 'minecraft:netherrack',
        hardness: 0.4,
        resistance: 0.4,
        tool: 'pickaxe'
    },
    end_stone: {
        name: 'end_stone',
        texture: 'minecraft:block/end_stone',
        fill: 'minecraft:end_stone',
        hardness: 3,
        resistance: 9,
        tool: 'pickaxe'
    }
};
```

### Global.js

这一部分主要用于定义一些全局的设定，例如各种路径、各种Json模板。

```JS
// Global.js
// priority: 199

global.modid = 'emendatusenigmatica';
Platform.setModName(`${global.modid}`, 'Emendatus Enigmatica');

const assetspath = `./kubejs/assets/${global.modid}`;
const datapath = `./kubejs/data/${global.modid}`;

const paths = {
    models: {
        block: `${assetspath}/models/block/`,
    },
    textures: {
        block: `${assetspath}/textures/blocks/templates`,
        item: `${assetspath}/textures/items/templates`
    },
    loots: {
        block: `${datapath}/loot_table/blocks/`,
    },
    recipes: {
        recipe: `${datapath}/recipe/`
    }
};

const OreLootJson = (block, item, sequence, min, max) => ({
    "type": "minecraft:block",
    "pools": [
        {
            "bonus_rolls": 0.0,
            "entries": [
                {
                    "type": "minecraft:alternatives",
                    "children": [
                        {
                            "type": "minecraft:item",
                            "conditions": [
                                {
                                    "condition": "minecraft:match_tool",
                                    "predicate": {
                                        "predicates": {
                                            "minecraft:enchantments": [
                                                {
                                                    "enchantments": "minecraft:silk_touch",
                                                    "levels": {
                                                        "min": 1
                                                    }
                                                }
                                            ]
                                        }
                                    }
                                }
                            ],
                            "name": block
                        },
                        {
                            "type": "minecraft:item",
                            "functions": [
                                {
                                    "add": false,
                                    "count": {
                                        "type": "minecraft:uniform",
                                        "max": max,
                                        "min": min
                                    },
                                    "function": "minecraft:set_count"
                                },
                                {
                                    "enchantment": "minecraft:fortune",
                                    "formula": "minecraft:ore_drops",
                                    "function": "minecraft:apply_bonus"
                                },
                                {
                                    "function": "minecraft:explosion_decay"
                                }
                            ],
                            "name": item
                        }
                    ]
                }
            ],
            "rolls": 1.0
        }
    ],
    "random_sequence": sequence
});

function createLootOre(name, strata, drop) {
    let loot = JsonIO.read(`${paths.loots.block}${name}_ore_${strata}.json`) || {};
    if (loot.type === undefined) {
        console.log(`No block loot table found, creating new: ${name}_ore_${strata}.json`);
        let min = parseInt(drop.min);
        let max = parseInt(drop.max);
        JsonIO.write(
            `${paths.loots.block}${name}_ore_${strata}.json`,
            OreLootJson(
                `emendatusenigmatica:${name}_ore_${strata}`,
                `${drop.item}`,
                `emendatusenigmatica:blocks/${name}_ore_${strata}`,
                min,
                max
            )
        )
    }
};
```

### EmendatusEnigmatica.js

这一部分是EmendatusEnigmaticaJS最主要的一个部分，几乎所有的注册逻辑都在此。

```JS
// EmendatusEnigmatica.js
// priority: 198

/**
 * @typedef {Object} EEConfig
 * @property {string} name - The name of the material.
 * @property {string} type - The type of the material (e.g., 'metal', 'special').
 * @property {string[]} [processedTypes] - A list of processed types for the material.
 * @property {string[]} [color] - A list of color codes.
 * @property {number} [burnTime] - Burn time in ticks, if applicable.
 * @property {Object} [drop] - Information about drops (optional).
 * @property {string} [drop.item] - The item dropped by this material.
 * @property {number} [drop.min] - Minimum amount of items dropped.
 * @property {number} [drop.max] - Maximum amount of items dropped.
 * @property {string[]} [strata] - Strata levels applicable to this material.
 * @property {string} [harvestLevel] - Required harvest tool level.
 * @property {number} [gemTemplate] - Gem template index (optional).
 * @property {Object} [texture] - Custom textures (optional).
 * @property {Object} [texture.item]
 * @property {string} [texture.item.ingot]
 * @property {string} [texture.item.nugget]
 * @property {string} [texture.item.gem]
 * @property {string} [texture.item.dust]
 * @property {string} [texture.item.plate]
 * @property {string} [texture.item.rod]
 * @property {string} [texture.item.gear]
 * @property {string} [texture.item.raw]
 * @property {Object} [texture.block]
 * @property {string} [texture.block.parent]
 * @property {string} [texture.block.ore]
 * @property {string} [texture.block.storage_block]
 */

/**
 * Class to handle Emendatus Enigmatica materials.
 * 
 * @param {EEConfig} config - Configuration object for the material.
 */
function EmendatusEnigmaticaJS(config) {
    this.name = config.name;
    this.type = config.type;
    this.harvestLevel = config.harvestLevel || null;
    this.processedTypes = config.processedTypes || [];
    this.strata = config.strata || [];
    this.color = config.color || [];
    this.burnTime = config.burnTime || 0;
    this.gemTemplate = config.gemTemplate || -1;
    this.drop = config.drop || null;
    this.texture = config.texture || {};
};

EmendatusEnigmaticaJS.prototype = {
    /**
     * Registers all processed types for the material.
     */
    registry() {
        let {
            name,
            type,
            harvestLevel,
            processedTypes,
            strata,
            color,
            burnTime,
            gemTemplate,
            drop,
            texture
        } = this;

        // 定义一个映射对象，将每个 processedType 与相应的函数映射起来
        let typeRegistryMap = {
            ore: () => registryOre(name, strata, harvestLevel, color, type, drop),
            raw: () => registryRaw(name, color),
            storage_block: () => registrySBlock(name, type, burnTime, color, texture),
            mekanism: () => registryMek(name, color),
            bloodmagic: () => registryBloodMagic(name, color),
            crush: () => registryCrush(name, color),
            ingot: () => registryIngot(name, burnTime, color, texture),
            nugget: () => registryNugget(name, burnTime, color),
            gem: () => registryGem(name, burnTime, color, gemTemplate),
            dust: () => registryDust(name, burnTime, color, texture),
            gear: () => registryGear(name, burnTime, color, texture),
            plate: () => registryPlate(name, burnTime, color, texture),
            rod: () => registryRod(name, burnTime, color, texture)
        };

        processedTypes.forEach((ptypes) => {
            typeRegistryMap[ptypes]();
        });
    }
};

function registryOre(name, strata, harvestLevel, color, type, drop) {
    let layerNames = ['layer0', 'layer1', 'layer2', 'layer3', 'layer4'];
    let texturePaths = layerNames.map((layer, index) => `${global.modid}:block/templates/ore/${type}/0${index}`);

    strata.forEach((s) => {
        StartupEvents.registry('block', (event) => {
            let builder = event.create(`${global.modid}:${name}_ore_${s}`);

            builder.renderType('cutout')
                .hardness(EE_STRATAS[s].resistance)
                .soundType(SoundType.STONE)
                .requiresTool(true)
                .tagBoth('c:ores')
                .tagBoth(`c:ores/${name}`)
                .tagBoth(`c:ore_rates/singular`)
                .tagBlock(`minecraft:mineable/${EE_STRATAS[s].tool}`)
                .tagBlock(`c:mineable/paxel`)
                .tagBlock(`minecraft:needs_${harvestLevel}_tool`)

            if (color) {
                builder.modelGenerator((model) => {
                    model.parent(`${EE_STRATAS[s].texture}`);
                    model.texture(s, `${EE_STRATAS[s].texture}`);
                    layerNames.forEach((layer, index) => {
                        model.texture(layer, texturePaths[index]);
                    });
                    model.element((element) => {
                        element.allFaces((face) => {
                            face.uv(0, 0, 16, 16).tex(s);
                        });
                    });
                    layerNames.forEach((layer, index) => {
                        model.element((element) => {
                            element.allFaces((face) => {
                                face.uv(0, 0, 16, 16).tex(layer).tintindex(index);
                            });
                        });
                    })
                });

                color.forEach((colorValue, index) => {
                    builder.color(index, colorValue);
                    builder.item((item) => {
                        item.color(index, colorValue);
                    });
                });
            };
        });
        createLootOreJson(name, s, drop);
    });
};

function registryRaw(name, color) {
    let layerNames = ['layer0', 'layer1', 'layer2', 'layer3', 'layer4'];
    let texturePaths = layerNames.map((layer, index) => `${global.modid}:block/templates/raw_block/0${index}`);

    StartupEvents.registry('item', (event) => {
        let builder = event.create(`${global.modid}:raw_${name}`)
            .tag('c:raw_materials')
            .tag(`c:raw_materials/${name}`)

        if (color) {
            for (let i = 0; i < color.length; i++) {
                builder.texture(`layer${i}`, `${global.modid}:item/templates/raw/0${i}`)
                    .color(i, color[i]);
            }
        }
    });
    StartupEvents.registry('block', (event) => {
        let builder = event.create(`${global.modid}:raw_${name}_block`)

        builder.modelGenerator((model) => {
            model.parent('minecraft:block/raw_iron_block')

            layerNames.forEach((layer, index) => {
                model.texture(layer, texturePaths[index]);
                model.element((element) => {
                    element.allFaces((face) => {
                        face.uv(0, 0, 16, 16).tex(layer).tintindex(index);
                    })
                })
            });
        })

        builder.renderType('cutout')
            .tagBoth('c:storage_blocks')
            .tagBoth(`c:storage_blocks/raw_${name}`)
            .tagBlock('minecraft:mineable/pickaxe')
            .soundType(SoundType.METAL)
            .requiresTool(true)
            .hardness(3)
            .resistance(3);

        if (color) {
            color.forEach((colorValue, index) => {
                builder.color(index, colorValue);
                builder.item((item) => {
                    item.color(index, colorValue);
                });
            });
        }
    });
};

function registrySBlock(name, type, burnTime, color) {
    let layerNames = ['layer0', 'layer1', 'layer2', 'layer3', 'layer4'];
    let metalTexturePaths = layerNames.map((layer, index) => `${global.modid}:block/templates/block/metal/0${index}`);
    let gemTexturePaths = layerNames.map((layer, index) => `${global.modid}:block/templates/block/gem/0${index}`);

    StartupEvents.registry('block', (event) => {
        let builder = event.create(`${global.modid}:${name}_block`);

        builder.renderType('cutout')
            .tagBoth('c:storage_blocks')
            .tagBoth(`c:storage_blocks/${name}`)
            .tagBlock('minecraft:mineable/pickaxe')
            .requiresTool(true)
            .hardness(3)
            .resistance(3)

        if (burnTime) {
            builder.item(i => {
                i.burnTime(burnTime * 10)
            })
            builder.tagBoth('fuelgoeshere:forced_fuels')
        }

        if (EE_MATERIALS[name].texture?.block?.parent && EE_MATERIALS[name].texture?.block?.storage_block) {
            builder.modelGenerator((model) => {
                model.parent(`${EE_MATERIALS[name].texture?.block?.parent}`)
                model.texture('layer0', `${global.modid}:${EE_MATERIALS[name].texture?.block?.storage_block}`)
                model.element((element) => {
                    element.allFaces((face) => {
                        face.uv(0, 0, 16, 16).tex('layer0')
                    })
                })
            })
            return
        }

        if (color) {
            builder.modelGenerator((model) => {
                model.parent('minecraft:block/iron_block')
                if (type === 'metal' | type === 'alloy' | type === 'special') {
                    layerNames.forEach((layer, index) => {
                        model.texture(layer, metalTexturePaths[index]);
                    });
                } else if (type === 'gem') {
                    layerNames.forEach((layer, index) => {
                        model.texture(layer, gemTexturePaths[index]);
                    })
                }

                layerNames.forEach((layer, index) => {
                    model.element((element) => {
                        element.allFaces((face) => {
                            face.uv(0, 0, 16, 16).tex(layer).tintindex(index);
                        })
                    })
                });
            })

            color.forEach((colorValue, index) => {
                builder.color(index, colorValue);
                builder.item((item) => {
                    item.color(index, colorValue);
                });
            });
        }
    });
}

function registryIngot(name, burnTime, color, texture) {
    StartupEvents.registry('item', (event) => {
        let builder = event.create(`${global.modid}:${name}_ingot`)
            .tag('c:ingots')
            .tag(`c:ingots/${name}`);

        if (builder) {
            builder.burnTime(burnTime)
            builder.tag('fuelgoeshere:forced_fuels')
        };

        if (EE_MATERIALS[name].texture?.item?.ingot) {
            builder.texture(`${global.modid}:${EE_MATERIALS[name].texture?.item?.ingot}`)
            return
        }

        if (color) {
            for (let i = 0; i < color.length; i++) {
                builder.texture(`layer${i}`, `${global.modid}:item/templates/ingot/0${i}`)
                    .color(i, color[i]);
            }
        }
    })
};

function registryNugget(name, burnTime, color, texture) {
    StartupEvents.registry('item', (event) => {
        let builder = event.create(`${global.modid}:${name}_nugget`)
            .tag('c:nuggets')
            .tag(`c:nuggets/${name}`);

        if (builder) {
            builder.burnTime(burnTime)
            builder.tag('fuelgoeshere:forced_fuels')
        };

        if (EE_MATERIALS[name].texture?.item?.nugget) {
            builder.texture(`${global.modid}:${EE_MATERIALS[name].texture?.item?.nugget}`)
            return
        }

        if (color) {
            for (let i = 0; i < color.length; i++) {
                builder.texture(`layer${i}`, `${global.modid}:item/templates/nugget/0${i}`)
                    .color(i, color[i]);
            }
        }
    })
};

function registryGem(name, burnTime, color, gemTemplate, texture) {
    StartupEvents.registry('item', (event) => {
        let builder = event.create(`${global.modid}:${name}_gem`)
            .tag('c:gems')
            .tag(`c:gems/${name}`);

        if (builder) {
            builder.burnTime(burnTime)
            builder.tag('fuelgoeshere:forced_fuels')
        };

        if (EE_MATERIALS[name].texture?.item?.gem) {
            builder.texture(`${global.modid}:${EE_MATERIALS[name].texture?.item?.gem}`)
            return
        };

        if (color) {
            for (let i = 0; i < color.length; i++) {
                builder.texture(`layer${i}`, `${global.modid}:item/templates/gem/template_${gemTemplate}/0${i}`)
                    .color(i, color[i])
            }
        }
    })
}

function registryDust(name, burnTime, color, texture) {
    StartupEvents.registry('item', (event) => {
        let builder = event.create(`${global.modid}:${name}_dust`)
            .tag('c:dusts')
            .tag(`c:dusts/${name}`);

        if (builder) {
            builder.burnTime(burnTime)
            builder.tag('fuelgoeshere:forced_fuels')
        };

        if (EE_MATERIALS[name].texture?.item?.dust) {
            builder.texture(`${global.modid}:${EE_MATERIALS[name].texture?.item?.dust}`)
            return
        };

        if (color) {
            for (let i = 0; i < color.length; i++) {
                builder.texture(`layer${i}`, `${global.modid}:item/templates/dust/0${i}`)
                    .color(i, color[i])
            }
        }
    })
}

function registryGear(name, burnTime, color, texture) {
    StartupEvents.registry('item', (event) => {
        let builder = event.create(`${global.modid}:${name}_gear`)
            .tag('c:gears')
            .tag(`c:gears/${name}`);

        if (builder) {
            builder.burnTime(burnTime)
            builder.tag('fuelgoeshere:forced_fuels')
        };

        if (EE_MATERIALS[name].texture?.item?.gear) {
            builder.texture(`${global.modid}:${EE_MATERIALS[name].texture?.item?.gear}`)
            return
        };

        if (color) {
            for (let i = 0; i < color.length; i++) {
                builder.texture(`layer${i}`, `${global.modid}:item/templates/gear/0${i}`)
                    .color(i, color[i])
            }
        }
    })
}

function registryPlate(name, burnTime, color, texture) {
    StartupEvents.registry('item', (event) => {
        let builder = event.create(`${global.modid}:${name}_plate`)
            .tag('c:plates')
            .tag(`c:plates/${name}`);

        if (builder) {
            builder.burnTime(burnTime)
            builder.tag('fuelgoeshere:forced_fuels')
        };

        if (EE_MATERIALS[name].texture?.item?.plate) {
            builder.texture(`${global.modid}:${EE_MATERIALS[name].texture?.item?.plate}`)
            return
        };

        if (color) {
            for (let i = 0; i < color.length; i++) {
                builder.texture(`layer${i}`, `${global.modid}:item/templates/plate/0${i}`)
                    .color(i, color[i])
            }
        }
    })
}

function registryRod(name, burnTime, color) {
    StartupEvents.registry('item', (event) => {
        let builder = event.create(`${global.modid}:${name}_rod`)
            .tag('c:rods')
            .tag(`c:rods/${name}`);

        if (builder) {
            builder.burnTime(burnTime)
            builder.tag('fuelgoeshere:forced_fuels')
        };

        if (EE_MATERIALS[name].texture?.item?.rod) {
            builder.texture(`${global.modid}:${EE_MATERIALS[name].texture?.item?.rod}`)
            return
        };

        if (color) {
            for (let i = 0; i < color.length; i++) {
                builder.texture(`layer${i}`, `${global.modid}:item/templates/rod/0${i}`)
                    .color(i, color[i])
            }
        }
    })
}

function registryMek(name, color) {
    StartupEvents.registry('item', (event) => {
        let crystal = event.create(`${global.modid}:${name}_crystal`).tag('mekanism:crystals').tag(`mekanism:crystals/${name}`)
        let shard = event.create(`${global.modid}:${name}_shard`).tag('mekanism:shards').tag(`mekanism:shards/${name}`)
        let clump = event.create(`${global.modid}:${name}_clump`).tag('mekanism:clumps').tag(`mekanism:clumps/${name}`)
        let dirtyDust = event.create(`${global.modid}:${name}_dirty_dust`).tag('mekanism:dirty_dusts').tag(`mekanism:dirty_dusts/${name}`)

        if (color) {
            for (let i = 0; i < color.length; i++) {
                crystal.texture(`layer${i}`, `${global.modid}:item/templates/crystal/0${i}`)
                    .color(i, color[i]);
                shard.texture(`layer${i}`, `${global.modid}:item/templates/shard/0${i}`)
                    .color(i, color[i]);
                clump.texture(`layer${i}`, `${global.modid}:item/templates/clump/0${i}`)
                    .color(i, color[i]);
                dirtyDust.texture(`layer${i}`, `${global.modid}:item/templates/dirty_dust/0${i}`)
                    .color(i, color[i]);
            }
        }
    });
};

function registryBloodMagic(name, color) {
    StartupEvents.registry('item', (event) => {
        let fragment = event.create(`${global.modid}:${name}_fragment`).tag('bloodmagic:fragments').tag(`bloodmagic:fragments/${name}`);
        let gravel = event.create(`${global.modid}:${name}_gravel`).tag('bloodmagic:gravels').tag(`bloodmagic:gravels/${name}`);

        if (color) {
            for (let i = 0; i < color.length; i++) {
                fragment.texture(`layer${i}`, `${global.modid}:item/templates/fragment/0${i}`)
                    .color(i, color[i]);
                gravel.texture(`layer${i}`, `${global.modid}:item/templates/gravel/0${i}`)
                    .color(i, color[i]);
            }
        }
    });
};

function registryCrush(name, color) {
    StartupEvents.registry('item', (event) => {
        let builder = event.create(`${global.modid}:${name}_crushed_ore`)
            .tag('create:crushed_raw_materials')
            .tag(`create:crushed_raw_materials/${name}`)

        if (this.color) {
            for (let i = 0; i < color.length; i++) {
                builder.texture(`layer${i}`, `${global.modid}:item/templates/crushed_ore/0${i}`)
                    .color(i, color[i]);
            };
        }
    });
};
```

### Material.js

这一部分主要用于材料的定义。

```JS
// Material.js
// priority: 197

let commonStratas = ['stone', 'andesite', 'diorite', 'granite', 'deepslate', 'netherrack', 'end_stone'];
let vanillaComplementStratas = ['netherrack', 'end_stone'];

/**
 * @type {EEConfig[]}
 */
let EE_MATERIALS = {
    // Vanilla
    // Coal
    coal: {
        name: 'coal',
        type: 'special',
        processedTypes: ['dust'],
        color: ['#393e46', '#2e2e2e', '#261e24', '#1f1721', '#1c1c1e'],
        burnTime: 1600
    },
    // Iron
    iron: {
        name: 'iron',
        type: 'metal',
        processedTypes: ['ore', 'dust', 'gear', 'plate', 'rod'],
        color: ['#ffffff', '#c9c9c9', '#828282', '#5e5e5e', '#353535'],
        strata: vanillaComplementStratas,
        drop: {
            item: 'minecraft:raw_iron',
            min: 1,
            max: 1
        },
        harvestLevel: 'stone'
    },
    // Copper
    copper: {
        name: 'copper',
        type: 'metal',
        processedTypes: ['ore', 'dust', 'gear', 'plate', 'rod'],
        color: ['#f7e6b7', '#f8b18d', '#cc6d51', '#a1383f', '#781c22'],
        strata: vanillaComplementStratas,
        drop: {
            item: 'minecraft:raw_copper',
            min: 2,
            max: 5
        },
        harvestLevel: 'stone'
    },
    // Gold
    gold: {
        name: 'gold',
        type: 'metal',
        processedTypes: ['ore', 'dust', 'gear', 'plate', 'rod'],
        color: ['#ffffff', '#fcf8a7', '#fad64a', '#dc9613', '#b26411'],
        strata: ['end_stone'],
        drop: {
            item: 'minecraft:raw_gold',
            min: 1,
            max: 1
        },
        harvestLevel: 'iron'
    },
    // Netherite
    netherite: {
        name: 'netherite',
        type: 'metal',
        processedTypes: ['nugget', 'dust', 'gear', 'plate', 'rod'],
        color: ['#737173', '#4d494d', '#443d3f', '#31292a', '#271c1d']
    },
    // Diamond
    diamond: {
        name: 'diamond',
        type: 'gem',
        processedTypes: ['ore', 'dust', 'gear', 'plate', 'rod'],
        color: ['#f2fffc', '#a1fbe8', '#20c5b5', '#1aaaa7', '#1c919a'],
        strata: vanillaComplementStratas,
        drop: {
            item: 'minecraft:diamond',
            min: 1,
            max: 1
        },
        harvestLevel: 'iron'
    },
    // Emerald
    emerald: {
        name: 'emerald',
        type: 'gem',
        processedTypes: ['ore', 'dust', 'gear', 'plate', 'rod'],
        color: ['#e6fcee', '#41f384', '#00aa2c', '#009529', '#007b18'],
        strata: vanillaComplementStratas,
        drop: {
            item: 'minecraft:emerald',
            min: 1,
            max: 1
        },
        harvestLevel: 'iron'
    },
    // Amethyst
    amethyst: {
        name: 'amethyst',
        type: 'gem',
        processedTypes: ['dust', 'gear', 'plate', 'rod'],
        color: ['#fcfad2', '#fbc9e3', '#b18cf0', '#8b69ca', '#6e4ea9']
    },
    // Quartz
    quartz: {
        name: 'quartz',
        type: 'gem',
        processedTypes: ['dust', 'gear', 'plate', 'rod'],
        color: ['#ffffff', '#eae5de', '#d4caba', '#b6a48e', '#897b73']
    },
    // Lapis
    lapis: {
        name: 'lapis',
        type: 'gem',
        processedTypes: ['dust', 'gear'],
        color: ['#9db5ed', '#5981e1', '#1d54a9', '#1f4294', '#173782']
    },
    // Universal Modded Metals
    // Aluminum
    aluminum: {
        name: 'aluminum',
        type: 'metal',
        processedTypes: ['ore', 'raw', 'ingot', 'nugget', 'dust', 'plate', 'gear', 'rod', 'storage_block', 'bloodmagic'],
        harvestLevel: 'stone',
        strata: commonStratas,
        color: ['#f2fafc', '#dfedf2', '#c5dbed', '#9da8c9', '#7a80a8'],
        smallStorageBlock: false,
        drop: {
            item: 'emendatusenigmatica:raw_aluminum',
            min: 1,
            max: 1
        }
    },
    // Osmium
    osmium: {
        name: 'osmium',
        type: 'metal',
        processedTypes: ['ore', 'raw', 'ingot', 'nugget', 'dust', 'plate', 'gear', 'rod', 'storage_block', 'bloodmagic'],
        harvestLevel: 'stone',
        strata: commonStratas,
        color: ['#e6ede9', '#abd1ca', '#83b0bd', '#3d5680', '#2c3766'],
        smallStorageBlock: false,
        drop: {
            item: 'emendatusenigmatica:raw_osmium',
            min: 1,
            max: 1
        }
    },
    // Lead
    lead: {
        name: 'lead',
        type: 'metal',
        processedTypes: ['ore', 'raw', 'ingot', 'nugget', 'dust', 'plate', 'gear', 'rod', 'storage_block', 'bloodmagic'],
        harvestLevel: 'iron',
        strata: commonStratas,
        color: ['#aebcbf', '#707e8a', '#414a6a', '#28254d', '#1f1d47'],
        smallStorageBlock: false,
        drop: {
            item: 'emendatusenigmatica:raw_lead',
            min: 1,
            max: 1
        }
    },
    // Nickel
    nickel: {
        name: 'nickel',
        type: 'metal',
        processedTypes: ['ore', 'raw', 'ingot', 'nugget', 'dust', 'plate', 'gear', 'rod', 'storage_block', 'mekanism', 'bloodmagic'],
        harvestLevel: 'stone',
        strata: commonStratas,
        color: ['#f6f7f0', '#b0b59f', '#869071', '#5a5c57', '#40423f'],
        smallStorageBlock: false,
        drop: {
            item: 'emendatusenigmatica:raw_nickel',
            min: 1,
            max: 1
        }
    },
    // Silver
    silver: {
        name: 'silver',
        type: 'metal',
        processedTypes: ['ore', 'raw', 'ingot', 'nugget', 'dust', 'plate', 'gear', 'rod', 'storage_block', 'mekanism', 'bloodmagic'],
        harvestLevel: 'iron',
        strata: commonStratas,
        color: ['#ffffff', '#d8ecff', '#baccff', '#7b85d9', '#646db4'],
        smallStorageBlock: false,
        drop: {
            item: 'emendatusenigmatica:raw_silver',
            min: 1,
            max: 1
        }
    },
    // Tin
    tin: {
        name: 'tin',
        type: 'metal',
        processedTypes: ['ore', 'raw', 'ingot', 'nugget', 'dust', 'plate', 'gear', 'rod', 'storage_block', 'bloodmagic'],
        harvestLevel: 'stone',
        strata: commonStratas,
        color: ['#ebfaf9', '#bcd7e5', '#a1a6d3', '#74609e', '#473b61'],
        smallStorageBlock: false,
        drop: {
            item: 'emendatusenigmatica:raw_tin',
            min: 1,
            max: 1
        }
    },
    // Uranium
    uranium: {
        name: 'uranium',
        type: 'metal',
        processedTypes: ['ore', 'raw', 'ingot', 'nugget', 'dust', 'plate', 'gear', 'rod', 'storage_block', 'bloodmagic'],
        harvestLevel: 'stone',
        strata: commonStratas,
        color: ['#ebe06a', '#98b350', '#43692f', '#113817', '#072411'],
        smallStorageBlock: false,
        drop: {
            item: 'emendatusenigmatica:raw_uranium',
            min: 1,
            max: 1
        }
    },
    // Zinc
    zinc: {
        name: 'zinc',
        type: 'metal',
        processedTypes: ['ore', 'raw', 'ingot', 'nugget', 'dust', 'plate', 'gear', 'rod', 'storage_block', 'mekanism', 'bloodmagic'],
        harvestLevel: 'iron',
        strata: commonStratas,
        color: ['#efece6', '#d1d1a5', '#9ca67b', '#54714c', '#3c5a3b'],
        smallStorageBlock: false,
        drop: {
            item: 'emendatusenigmatica:raw_zinc',
            min: 1,
            max: 1
        }
    },
    // Universal Modded Gems
    // Sulfur
    sulfur: {
        name: 'sulfur',
        type: 'gem',
        processedTypes: ['ore', 'gem', 'dust', 'storage_block'],
        harvestLevel: 'stone',
        strata: commonStratas,
        color: ['#fff8e5', '#ffea47', '#ded531', '#bdc43b', '#a0ad3b'],
        drop: {
            item: 'emendatusenigmatica:sulfur_gem',
            min: 1,
            max: 1
        },
        gemTemplate: 8
    },
    // Niter
    niter: {
        name: 'niter',
        type: 'gem',
        processedTypes: ['ore', 'gem', 'dust', 'storage_block'],
        harvestLevel: 'stone',
        strata: commonStratas,
        color: ['#ffffff', '#e0dde4', '#aea5b8', '#8b7f9a', '#716978'],
        drop: {
            item: 'emendatusenigmatica:niter_gem',
            min: 1,
            max: 1
        },
        gemTemplate: 8
    },
    // Fluorite
    fluorite: {
        name: 'fluorite',
        type: 'gem',
        processedTypes: ['ore', 'gem', 'dust', 'storage_block'],
        harvestLevel: 'stone',
        strata: commonStratas,
        texture: {
            item: {
                gem: 'item/fluorite_gem',
                dust: 'item/fluorite_dust'
            },
            block: {
                parent: 'minecraft:block/iron_block',
                storage_block: 'block/fluorite_block'
            }
        },
        drop: {
            item: 'emendatusenigmatica:fluorite_gem',
            min: 2,
            max: 4
        }
    },
    // Ruby
    ruby: {
        name: 'ruby',
        type: 'gem',
        baseItem: 'gem',
        processedTypes: ['gem', 'dust', 'gear', 'plate', 'rod', 'storage_block'],
        color: ['#fcd1cc', '#fb7b71', '#e93e43', '#c41735', '#780526'],
        gemTemplate: 1
    },
    // Sapphire
    sapphire: {
        name: 'sapphire',
        type: 'gem',
        baseItem: 'gem',
        processedTypes: ['gem', 'dust', 'gear', 'plate', 'rod', 'storage_block'],
        color: ['#fcfcfc', '#bde5fc', '#76c6fc', '#246be9', '#121d73'],
        gemTemplate: 3
    },
    // Misc
    // Wood
    wood: {
        name: 'wood',
        type: 'special',
        processedTypes: ['dust', 'storage_block'],
        color: ['#b8945f', '#987849', '#745a36', '#5f4a2b', '#4c3d26']
    },
    // Ender Pearl
    ender_pearl: {
        name: 'ender_pearl',
        type: 'special',
        processedTypes: ['dust', 'storage_block'],
        color: ['#8cf4e2', '#349988', '#0c3730', '#0b4d42', '#063931']
    },
    // Coal Coke
    coal_coke: {
        name: 'coal_coke',
        type: 'special',
        processedTypes: ['gem', 'dust', 'storage_block'],
        color: ['#819da6', '#2e4049', '#1c1c1e', '#252525', '#1a2a36'],
        texture: {
            item: {
                gem: 'item/coal_coke_gem',
            },
            block: {
                parent: 'minecraft:block/sand',
                storage_block: 'block/coal_coke_block'
            }
        },
        burnTime: 3200
    },
    // Silicon
    silicon: {
        name: 'silicon',
        type: 'special',
        processedTypes: ['gem'],
        texture: {
            item: {
                gem: 'item/silicon_gem'
            }
        }
    },
    // Alloys
    // Electrum
    electrum: {
        name: 'electrum',
        type: 'alloy',
        processedTypes: ['dust', 'gear', 'ingot', 'nugget', 'plate', 'rod', 'storage_block'],
        color: ['#f4f7eb', '#eded91', '#e5b840', '#a85d1b', '#8c3a0e']
    },
    // Invar
    invar: {
        name: 'invar',
        type: 'alloy',
        processedTypes: ['dust', 'gear', 'ingot', 'nugget', 'plate', 'rod', 'storage_block'],
        color: ['#ffffff', '#b8c4bf', '#8d9f96', '#5b7669', '#495e57']
    },
    // Constantan
    constantan: {
        name: 'constantan',
        type: 'alloy',
        processedTypes: ['dust', 'gear', 'ingot', 'nugget', 'plate', 'rod', 'storage_block'],
        color: ['#f0e8d8', '#e5c09e', '#d8876b', '#943a38', '#781e24']
    },
    // Bronze
    bronze: {
        name: 'bronze',
        type: 'alloy',
        processedTypes: ['dust', 'gear', 'ingot', 'nugget', 'plate', 'rod', 'storage_block'],
        color: ['#ebe9be', '#ebd288', '#d38c53', '#ba5b2f', '#9c3a27']
    },
    // Signalum
    signalum: {
        name: 'signalum',
        type: 'alloy',
        processedTypes: ['dust', 'gear', 'ingot', 'nugget', 'plate', 'rod', 'storage_block'],
        color: ['#ffe4c9', '#fc8638', '#e55c17', '#993d0f', '#82260d']
    },
    // Lumium
    lumium: {
        name: 'lumium',
        type: 'alloy',
        processedTypes: ['dust', 'gear', 'ingot', 'nugget', 'plate', 'rod', 'storage_block'],
        color: ['#fdfff7', '#e5f3b5', '#dcd56b', '#bf8c39', '#a87132']
    },
    // Enderium
    enderium: {
        name: 'enderium',
        type: 'alloy',
        processedTypes: ['dust', 'gear', 'ingot', 'nugget', 'plate', 'rod', 'storage_block'],
        color: ['#5de8cc', '#289799', '#1c5961', '#0b2e47', '#0f1e36']
    },
    // Brass
    brass: {
        name: 'brass',
        type: 'alloy',
        processedTypes: ['dust', 'gear', 'ingot', 'nugget', 'plate', 'rod', 'storage_block'],
        color: ['#dfedcc', '#c7d477', '#b5a642', '#8c6322', '#6b3c0d']
    },
    // Steel
    steel: {
        name: 'steel',
        type: 'alloy',
        processedTypes: ['dust', 'gear', 'ingot', 'nugget', 'plate', 'rod', 'storage_block'],
        color: ['#e4e6eb', '#9ea0a3', '#818185', '#454552', '#31313b']
    },
};

Object.entries(EE_MATERIALS).forEach(
    ([key, material]) => {
        new EmendatusEnigmaticaJS(material).registry();
    }
);
```
