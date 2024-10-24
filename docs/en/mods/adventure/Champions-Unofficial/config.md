---
Layout: doc
title: Config
order: 2
prev:
  text: Wiki
  link: /en/mods\adventure\Champions-Unofficial\wiki
authors:
  - M1hono
next:
  text: Introduction
  link: /en/mods\adventure\Champions-Unofficial\index
---

#

## Example: Dropping an Item with Specific NBT

```json
{
  "type": "minecraft:entity",
  "pools": [
    {
      "rolls": 1,
      "entries": [
        {
          "type": "minecraft:item",
          "name": "minecraft:diamond_sword",
          "conditions": [
            {
              "condition": "champions:entity_champion",
              "entity": "this",
              "minTier": 4,
            }
          ]
          "functions": [
            {
              "function": "minecraft:set_attributes",
              "modifiers": [
                {
                  "attribute": "generic.attack_damage",
                  "name": "Bonus Damage",
                  "amount": 5,
                  "operation": "addition",
                  "id": "12345678-1234-1234-1234-1234567890ab",
                  "slot": "mainhand"
                }
              ]
            },
            {
              "function": "minecraft:set_nbt",
              "tag": "{display:{Name:'{\"text\":\"Champion Sword\",\"color\":\"gold\"}'}}"
            }
          ]
        }
      ]
    }
  ]
}
```