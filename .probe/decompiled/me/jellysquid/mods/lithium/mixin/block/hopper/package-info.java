@MixinConfigOption(description = "Reduces hopper lag using caching, notification systems and BlockEntity sleeping", depends = { @MixinConfigDependency(dependencyPath = "mixin.util.entity_movement_tracking"), @MixinConfigDependency(dependencyPath = "mixin.util.block_entity_retrieval"), @MixinConfigDependency(dependencyPath = "mixin.util.inventory_change_listening") }, enabled = false)
package me.jellysquid.mods.lithium.mixin.block.hopper;

import net.caffeinemc.gradle.MixinConfigDependency;
import net.caffeinemc.gradle.MixinConfigOption;