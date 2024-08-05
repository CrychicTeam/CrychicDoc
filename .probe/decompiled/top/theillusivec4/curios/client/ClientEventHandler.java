package top.theillusivec4.curios.client;

import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotAttribute;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.ICuriosMenu;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketOpenCurios;

public class ClientEventHandler {

    private static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");

    private static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent evt) {
        if (evt.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            if (KeyRegistry.openCurios.consumeClick() && mc.isWindowActive()) {
                NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CPacketOpenCurios(ItemStack.EMPTY));
            }
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key evt) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer localPlayer = mc.player;
        if (localPlayer != null && localPlayer.m_242612_() && !(localPlayer.f_36096_ instanceof ICuriosMenu) && evt.getKey() == KeyRegistry.openCurios.getKey().getValue() && evt.getAction() == 1) {
            localPlayer.closeContainer();
        }
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent evt) {
        ItemStack stack = evt.getItemStack();
        Player player = evt.getEntity();
        if (!stack.isEmpty()) {
            List<Component> tooltip = evt.getToolTip();
            for (int i = 0; i < tooltip.size(); i++) {
                Component component = (Component) tooltip.get(i);
                ComponentContents curioTags = component.getContents();
                if (curioTags instanceof TranslatableContents) {
                    TranslatableContents contents = (TranslatableContents) curioTags;
                    boolean replace = false;
                    Object[] args = contents.getArgs();
                    if (args != null) {
                        for (int i1 = 0; i1 < args.length; i1++) {
                            Object arg = args[i1];
                            if (arg instanceof MutableComponent) {
                                MutableComponent mutableComponent = (MutableComponent) arg;
                                ComponentContents type = mutableComponent.getContents();
                                if (type instanceof TranslatableContents) {
                                    TranslatableContents contents1 = (TranslatableContents) type;
                                    if (contents1.getKey().startsWith("curios.slot.")) {
                                        String actualKey = contents1.getKey().replace(".slot.", ".identifier.");
                                        contents.getArgs()[i1] = Component.translatable(actualKey, contents1.getArgs());
                                        replace = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (replace) {
                        tooltip.set(i, Component.translatable(contents.getKey().replace("attribute.modifier.", "curios.modifiers.slots."), contents.getArgs()).withStyle(component.getStyle()));
                    }
                }
            }
            CompoundTag tag = stack.getTag();
            int ix = 0;
            if (tag != null && tag.contains("HideFlags", 99)) {
                ix = tag.getInt("HideFlags");
            }
            Map<String, ISlotType> map = player != null ? CuriosApi.getItemStackSlots(stack, player) : CuriosApi.getItemStackSlots(stack, FMLLoader.getDist() == Dist.CLIENT);
            Set<String> curioTags = Set.copyOf(map.keySet());
            if (curioTags.contains("curio")) {
                curioTags = Set.of("curio");
            }
            List<String> slots = new ArrayList(curioTags);
            if (!slots.isEmpty()) {
                List<Component> tagTooltips = new ArrayList();
                MutableComponent slotsTooltip = Component.translatable("curios.tooltip.slot").append(" ").withStyle(ChatFormatting.GOLD);
                for (int j = 0; j < slots.size(); j++) {
                    String key = "curios.identifier." + (String) slots.get(j);
                    MutableComponent type = Component.translatable(key);
                    if (j < slots.size() - 1) {
                        type = type.append(", ");
                    }
                    type = type.withStyle(ChatFormatting.YELLOW);
                    slotsTooltip.append(type);
                }
                tagTooltips.add(slotsTooltip);
                LazyOptional<ICurio> optionalCurio = CuriosApi.getCurio(stack);
                optionalCurio.ifPresent(curio -> {
                    List<Component> actualSlotsTooltip = curio.getSlotsTooltip(tagTooltips);
                    if (!actualSlotsTooltip.isEmpty()) {
                        tooltip.addAll(1, actualSlotsTooltip);
                    }
                });
                if (!optionalCurio.isPresent()) {
                    tooltip.addAll(1, tagTooltips);
                }
                List<Component> attributeTooltip = new ArrayList();
                for (String identifier : slots) {
                    Multimap<Attribute, AttributeModifier> multimap = CuriosApi.getAttributeModifiers(new SlotContext(identifier, player, 0, false, true), UUID.randomUUID(), stack);
                    if (!multimap.isEmpty() && (ix & 2) == 0) {
                        boolean init = false;
                        for (Entry<Attribute, AttributeModifier> entry : multimap.entries()) {
                            if (entry.getKey() != null) {
                                if (!init) {
                                    attributeTooltip.add(Component.empty());
                                    attributeTooltip.add(Component.translatable("curios.modifiers." + identifier).withStyle(ChatFormatting.GOLD));
                                    init = true;
                                }
                                AttributeModifier attributemodifier = (AttributeModifier) entry.getValue();
                                double amount = attributemodifier.getAmount();
                                boolean flag = false;
                                if (player != null) {
                                    if (attributemodifier.getId() == ATTACK_DAMAGE_MODIFIER) {
                                        AttributeInstance att = player.m_21051_(Attributes.ATTACK_DAMAGE);
                                        if (att != null) {
                                            amount += att.getBaseValue();
                                        }
                                        amount += (double) EnchantmentHelper.getDamageBonus(stack, MobType.UNDEFINED);
                                        flag = true;
                                    } else if (attributemodifier.getId() == ATTACK_SPEED_MODIFIER) {
                                        AttributeInstance att = player.m_21051_(Attributes.ATTACK_SPEED);
                                        if (att != null) {
                                            amount += att.getBaseValue();
                                        }
                                        flag = true;
                                    }
                                    double d1;
                                    if (attributemodifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE || attributemodifier.getOperation() == AttributeModifier.Operation.MULTIPLY_TOTAL) {
                                        d1 = amount * 100.0;
                                    } else if (((Attribute) entry.getKey()).equals(Attributes.KNOCKBACK_RESISTANCE)) {
                                        d1 = amount * 10.0;
                                    } else {
                                        d1 = amount;
                                    }
                                    Object var27 = entry.getKey();
                                    if (var27 instanceof SlotAttribute) {
                                        SlotAttribute slotAttribute = (SlotAttribute) var27;
                                        if (amount > 0.0) {
                                            attributeTooltip.add(Component.translatable("curios.modifiers.slots.plus." + attributemodifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable("curios.identifier." + slotAttribute.getIdentifier())).withStyle(ChatFormatting.BLUE));
                                        } else {
                                            d1 *= -1.0;
                                            attributeTooltip.add(Component.translatable("curios.modifiers.slots.take." + attributemodifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable("curios.identifier." + slotAttribute.getIdentifier())).withStyle(ChatFormatting.RED));
                                        }
                                    } else if (flag) {
                                        attributeTooltip.add(Component.literal(" ").append(Component.translatable("attribute.modifier.equals." + attributemodifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(((Attribute) entry.getKey()).getDescriptionId()))).withStyle(ChatFormatting.DARK_GREEN));
                                    } else if (amount > 0.0) {
                                        attributeTooltip.add(Component.translatable("attribute.modifier.plus." + attributemodifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(((Attribute) entry.getKey()).getDescriptionId())).withStyle(ChatFormatting.BLUE));
                                    } else if (amount < 0.0) {
                                        d1 *= -1.0;
                                        attributeTooltip.add(Component.translatable("attribute.modifier.take." + attributemodifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(((Attribute) entry.getKey()).getDescriptionId())).withStyle(ChatFormatting.RED));
                                    }
                                }
                            }
                        }
                    }
                }
                optionalCurio.ifPresent(curio -> {
                    List<Component> actualAttributeTooltips = curio.getAttributesTooltip(attributeTooltip);
                    if (!actualAttributeTooltips.isEmpty()) {
                        tooltip.addAll(actualAttributeTooltips);
                    }
                });
                if (!optionalCurio.isPresent()) {
                    tooltip.addAll(attributeTooltip);
                }
            }
        }
    }
}