package dev.xkmc.modulargolems.compat.misc;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.ConvertMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemGarageKit;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import dev.xkmc.modulargolems.content.entity.humanoid.skin.ClientSkinDispatch;
import dev.xkmc.modulargolems.content.entity.humanoid.skin.SpecialRenderSkin;
import dev.xkmc.modulargolems.events.event.HumanoidSkinEvent;
import dev.xkmc.modulargolems.init.ModularGolems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MaidCompat {

    private static EntityMaidRenderer RENDERER;

    @SubscribeEvent
    public static void onMaidConvert(ConvertMaidEvent event) {
        if (event.getEntity() instanceof HumanoidGolemEntity golem) {
            if (ClientSkinDispatch.get(golem) instanceof MaidCompat.MaidSkin skin) {
                event.setMaid(new MaidCompat.MaidWrapper(golem, skin.id));
            }
        }
    }

    @SubscribeEvent
    public static void onHumanoidSkin(HumanoidSkinEvent event) {
        ItemStack stack = event.getStack();
        if (stack.is((Item) InitItems.GARAGE_KIT.get())) {
            String id = ItemGarageKit.getMaidData(stack).getString("ModelId");
            event.setSkin(new MaidCompat.MaidSkin(id));
        }
    }

    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        RENDERER = new EntityMaidRenderer(event.getContext());
    }

    private static record MaidSkin(String id) implements SpecialRenderSkin {

        @Override
        public void render(HumanoidGolemEntity entity, float f1, float f2, PoseStack stack, MultiBufferSource source, int i) {
            if (MaidCompat.RENDERER != null) {
                try {
                    MaidCompat.RENDERER.m_7392_(entity, f1, f2, stack, source, i);
                } catch (Exception var8) {
                    ModularGolems.LOGGER.debug("Error rendering golem with TLM skin", var8);
                }
            }
        }
    }

    private static record MaidWrapper(Mob mob, String id) implements IMaid {

        public String getModelId() {
            return this.id;
        }

        public Mob asEntity() {
            return this.mob;
        }
    }
}