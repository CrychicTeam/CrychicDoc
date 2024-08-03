package dev.latvian.mods.kubejs.core.mixin.common;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.core.InventoryKJS;
import dev.latvian.mods.kubejs.core.PlayerKJS;
import dev.latvian.mods.kubejs.player.KubeJSInventoryListener;
import dev.latvian.mods.kubejs.stages.Stages;
import dev.latvian.mods.kubejs.util.AttachedData;
import dev.latvian.mods.kubejs.util.KubeJSPlugins;
import dev.latvian.mods.rhino.util.RemapForJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@RemapPrefixForJS("kjs$")
@Mixin(value = { Player.class }, priority = 1001)
public abstract class PlayerMixin implements PlayerKJS {

    private Stages kjs$stages;

    private AttachedData<Player> kjs$attachedData;

    private KubeJSInventoryListener kjs$inventoryChangeListener;

    @Override
    public Stages kjs$getStages() {
        if (this.kjs$stages == null) {
            this.kjs$stages = Stages.create(this.kjs$self());
        }
        return this.kjs$stages;
    }

    @Override
    public InventoryKJS kjs$getInventory() {
        return this.kjs$self().getInventory();
    }

    @Override
    public InventoryKJS kjs$getCraftingGrid() {
        return this.kjs$self().inventoryMenu.getCraftSlots();
    }

    @Override
    public AttachedData<Player> kjs$getData() {
        if (this.kjs$attachedData == null) {
            this.kjs$attachedData = new AttachedData<>(this.kjs$self());
            KubeJSPlugins.forEachPlugin(this.kjs$attachedData, KubeJSPlugin::attachPlayerData);
        }
        return this.kjs$attachedData;
    }

    @Shadow
    @RemapForJS("closeMenu")
    public abstract void closeContainer();

    @Override
    public KubeJSInventoryListener kjs$getInventoryChangeListener() {
        if (this.kjs$inventoryChangeListener == null) {
            this.kjs$inventoryChangeListener = new KubeJSInventoryListener((Player) this);
        }
        return this.kjs$inventoryChangeListener;
    }
}