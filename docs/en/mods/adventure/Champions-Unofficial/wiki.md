---
Layout: doc
title: Wiki
prev:
  text: 'Intro'
  link: './'
next:
  text: 'Config'
  link: './config'
order: 1
authors:
  - M1hono
---

## Introduction to Champions

**Champions** is a mod that brings more challenge to gameplay by adding elite mobs with different levels of rarity. Each hostile mob now has a chance to be given a level and become a champion. These champions are tougher, have extra abilities, and drop better loot that matches their increased power.

The mod comes with a configuration file that offers plenty of options to tweak the difficulty and variety to suit your playstyle.

> **Champions-Unofficial** carries over all the settings from the original mod and aims to further improve and expand on it.

<ClientOnly>
<DamageChart 
  :incomingDamage="50"
  :armorToughness="0"
  :minDamage="0"
  :maxDamage="60"
  :maxArmorPoints="100"
  :isJavaEdition="true"
/>
</ClientOnly>

The stats of hostile mobs are an important part of this mod, so here's a dynamic chart that shows how damage changes based on armor value and armor toughness. This can be helpful for modders looking to fine-tune their setups.

You can adjust the values in the chart to see how different levels of attacks affect enemies with various armor levels.

## Calculation Formula

To help modders figure out how to balance their game, here’s the formula used in the Champions mod to calculate attribute growth. This applies to health, attack damage, armor, armor toughness, and knockback resistance:

$$
\text{Attribute} = \text{Base Attribute} \times (1 + \text{growthFactor} \times \text{growth.health})
$$

### Example

Let’s say the base health is `100`, the `growthFactor`[^1] is `15`, and `growth.health`[^2] is set to the default of `2`. The resulting health would be:

$$
\text{Health} = 100 \times (1 + 15 \times 2) = 100 \times 31 = 3100
$$

### Applicable Attributes

- **Max Health**
- **Attack Damage**
- **Armor Value**
- **Armor Toughness**
- **Knockback Resistance**

This formula works for all five of these attributes.

## List of Affixes

1. **Adaptable** - Takes less damage from consecutive attacks of the same type.

2. **Arctic** - Fires homing projectiles that slow down targets.

3. **Enkindling** - Fires homing projectiles that cause explosions and set targets on fire.

4. **Dampening** - Reduces damage from indirect attacks, like arrows.

5. **Desecrating** - Periodically spawns a harmful cloud under its target.

6. **Hasty** - Greatly increases movement speed.

7. **Infested** - Spawns silverfish when hit or attacking.

8. **Knocking** - Increases knockback and slows targets for a short time.

9. **Lively** - Regenerates 1 health point per second, or 5 per second when not in combat.

10. **Molten** - Enhances attack damage and armor penetration, and grants fire resistance.

11. **Plagued** - Infects nearby creatures with a Poison effect.

12. **Reflective** - Reflects a small portion of damage back to the attacker.

13. **Wounding** - Attacks have a chance to reduce healing and increase damage taken.

14. **Shielding** - Periodically becomes immune to all damage.

15. **Magnetic** - Periodically pulls targets towards itself.

16. **Paralyzing** - Occasionally immobilizes targets, preventing them from moving.

17. **Jailer** - Has a chance to imprison the target with each attack.

> **This text is adapted from** [the original github page.](https://github.com/TheIllusiveC4/Champions)

[^1]: Defined in `champions-ranks.toml`, this attribute represents the growth factor influenced by the level.

[^2]: Defined in `champions-server.toml`, this attribute has a default value of 2 and represents the growth coefficient for the corresponding attribute.
