package team.lodestar.lodestone.network;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.systems.network.LodestoneClientPacket;

public class TotemOfUndyingEffectPacket extends LodestoneClientPacket {

    private final int entityId;

    private final ItemStack stack;

    public TotemOfUndyingEffectPacket(Entity entity, ItemStack stack) {
        this.entityId = entity.getId();
        this.stack = stack;
    }

    public TotemOfUndyingEffectPacket(int entityId, ItemStack stack) {
        this.entityId = entityId;
        this.stack = stack;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeItem(this.stack);
    }

    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level.getEntity(this.entityId) instanceof LivingEntity livingEntity) {
            minecraft.particleEngine.createTrackingEmitter(livingEntity, ParticleTypes.TOTEM_OF_UNDYING, 30);
            minecraft.level.playLocalSound(livingEntity.m_20185_(), livingEntity.m_20186_(), livingEntity.m_20189_(), SoundEvents.TOTEM_USE, livingEntity.m_5720_(), 1.0F, 1.0F, false);
            if (livingEntity == minecraft.player) {
                minecraft.gameRenderer.displayItemActivation(this.stack);
            }
        }
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, TotemOfUndyingEffectPacket.class, TotemOfUndyingEffectPacket::encode, TotemOfUndyingEffectPacket::decode, LodestoneClientPacket::handle);
    }

    public static TotemOfUndyingEffectPacket decode(FriendlyByteBuf buf) {
        return new TotemOfUndyingEffectPacket(buf.readInt(), buf.readItem());
    }
}