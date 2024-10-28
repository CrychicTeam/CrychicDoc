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

# Champions Mod Configuration Guide

The Champions mod enhances the challenge in Minecraft by adding elite monsters. Here is a detailed description of the configuration files in the `config/` folder to help you properly configure the game.

## HUD Display Settings

```toml
[hud]
# The x-offset for the champion HUD
# Range: -1000 ~ 1000
hudXOffset = 0

# The y-offset for the champion HUD
# Range: -1000 ~ 1000
hudYOffset = 0

# The distance, in blocks, from which the champion HUD can be seen
# Range: 0 ~ 1000
hudRange = 50

# Set to true to move the WAILA overlay underneath the champion HUD
enableWailaIntegration = true
```

These settings are used to adjust the position and visibility of the HUD (Head-Up Display) in the game.

## General Configuration

```toml
[general]
# The range from an active beacon where no champions will spawn (0 to disable)
# Range: 0 ~ 1000
beaconProtectionRange = 64

# Set to true to enable champions from mob spawners
championSpawners = false

# The minimum tier of champions that will have death messages sent out upon defeat (0 to disable)
# Range: > 0
deathMessageTier = 0

# A list of dimension names that are blacklisted/whitelisted for champions
dimensionList = []

# Set whether the dimension list is a blacklist or whitelist
# Allowed Values: BLACKLIST, WHITELIST
dimensionPermission = "BLACKLIST"

# A list of entities that are blacklisted/whitelisted for champions
entitiesList = []

# Set whether the entities list is a blacklist or whitelist
# Allowed Values: BLACKLIST, WHITELIST
entitiesPermission = "BLACKLIST"

# Set to true to show HUD display for champions, including health, affixes, and tier
showHud = true

# Set entity id (for example, ['minecraft:end_dragon', 'minecraft:creeper']) to hidden HUD display for champions, including health, affixes, and tier
bossBarBlackList = []

# Set to true to have champions generate a colored particle effect indicating their rank
showParticles = true

# Set to true to show champion tier and affixes in The One Probe overlay
enableTOPIntegration = true
```

These general configuration options allow you to customize the behavior and display of elite monsters in the game to suit different players' needs.

By properly configuring these options, you can make the game experience richer and more personalized.
