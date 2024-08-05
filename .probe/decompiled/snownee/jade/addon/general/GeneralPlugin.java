package snownee.jade.addon.general;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IWailaConfig;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypePreset;

@WailaPlugin("jadeaddons")
public class GeneralPlugin implements IWailaPlugin {

    public static final String ID = "jadeaddons";

    public static final ResourceLocation EQUIPMENT_REQUIREMENT = new ResourceLocation("jadeaddons", "equipment_requirement");

    static IWailaClientRegistration client;

    public static BiPredicate<Player, TagKey<Item>> EQUIPMENT_CHECK_PREDICATE = (player, tag) -> player.m_21205_().is(tag) || player.m_21206_().is(tag) || player.getItemBySlot(EquipmentSlot.HEAD).is(tag);

    public TagKey<Item> requirementTag;

    @OnlyIn(Dist.CLIENT)
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        client = registration;
        registration.addConfig(EQUIPMENT_REQUIREMENT, "", ResourceLocation::m_135830_);
        registration.addConfigListener(EQUIPMENT_REQUIREMENT, id -> this.refreshTag(id, $ -> this.requirementTag = $));
        registration.addRayTraceCallback(10000, this::override);
        if (ModList.get().isLoaded("curios")) {
            EQUIPMENT_CHECK_PREDICATE = EQUIPMENT_CHECK_PREDICATE.or((player, tag) -> CuriosApi.getCuriosHelper().findCurios(player, SlotTypePreset.HEAD.getIdentifier()).stream().filter(result -> !result.slotContext().cosmetic()).anyMatch(result -> result.stack().is(tag)));
        }
        MinecraftForge.EVENT_BUS.addListener(this::tagsUpdated);
    }

    private void refreshTags() {
        this.refreshTag(EQUIPMENT_REQUIREMENT, $ -> this.requirementTag = $);
    }

    private void refreshTag(ResourceLocation id, Consumer<TagKey<Item>> setter) {
        String s = IWailaConfig.get().getPlugin().getString(id);
        if (s.isBlank()) {
            setter.accept(null);
        } else {
            setter.accept(TagKey.create(Registries.ITEM, new ResourceLocation(s)));
        }
    }

    private void tagsUpdated(TagsUpdatedEvent event) {
        if (event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED) {
            this.refreshTags();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public Accessor<?> override(HitResult hitResult, @Nullable Accessor<?> accessor, @Nullable Accessor<?> originalAccessor) {
        if (accessor != null) {
            Player player = accessor.getPlayer();
            if (this.requirementTag != null && !EQUIPMENT_CHECK_PREDICATE.test(player, this.requirementTag)) {
                return null;
            }
        }
        return accessor;
    }
}