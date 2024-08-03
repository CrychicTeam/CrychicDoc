package dev.xkmc.modulargolems.events.event;

import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import dev.xkmc.modulargolems.content.entity.humanoid.skin.SpecialRenderSkin;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

public class HumanoidSkinEvent extends Event {

    private final HumanoidGolemEntity golem;

    private final ItemStack stack;

    private SpecialRenderSkin skin;

    public HumanoidSkinEvent(HumanoidGolemEntity golem, ItemStack stack) {
        this.golem = golem;
        this.stack = stack;
    }

    public HumanoidGolemEntity getGolem() {
        return this.golem;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public void setSkin(@Nullable SpecialRenderSkin skin) {
        this.skin = skin;
    }

    @Nullable
    public SpecialRenderSkin getSkin() {
        return this.skin;
    }
}