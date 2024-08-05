package yesman.epicfight.client.events.engine;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.settings.KeyMappingLookup;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.lwjgl.glfw.GLFW;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.client.gui.screen.IngameConfigurationScreen;
import yesman.epicfight.client.gui.screen.SkillEditScreen;
import yesman.epicfight.client.input.EpicFightKeyMappings;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.skill.ChargeableSkill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlot;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.entity.eventlistener.MovementInputEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import yesman.epicfight.world.entity.eventlistener.SkillExecuteEvent;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

@OnlyIn(Dist.CLIENT)
public class ControllEngine {

    private final Map<KeyMapping, BiFunction<KeyMapping, Integer, Boolean>> keyFunctions = Maps.newHashMap();

    private final Set<Object> packets = Sets.newHashSet();

    private final Minecraft minecraft;

    private LocalPlayer player;

    private LocalPlayerPatch playerpatch;

    private KeyMappingLookup keyHash;

    private int weaponInnatePressCounter = 0;

    private int sneakPressCounter = 0;

    private int moverPressCounter = 0;

    private int lastHotbarLockedTime;

    private boolean weaponInnatePressToggle = false;

    private boolean sneakPressToggle = false;

    private boolean moverPressToggle = false;

    private boolean attackLightPressToggle = false;

    private boolean hotbarLocked;

    private boolean chargeKeyUnpressed;

    private int reserveCounter;

    private KeyMapping reservedKey;

    private SkillSlot reservedOrChargingSkillSlot;

    private KeyMapping currentChargingKey;

    public Options options;

    public ControllEngine() {
        ControllEngine.Events.controllEngine = this;
        this.minecraft = Minecraft.getInstance();
        this.options = this.minecraft.options;
        this.keyFunctions.put(EpicFightKeyMappings.ATTACK, this::attackKeyPressed);
        this.keyFunctions.put(this.options.keySwapOffhand, this::swapHandKeyPressed);
        this.keyFunctions.put(EpicFightKeyMappings.SWITCH_MODE, this::switchModeKeyPressed);
        this.keyFunctions.put(EpicFightKeyMappings.DODGE, this::dodgeKeyPressed);
        this.keyFunctions.put(EpicFightKeyMappings.GUARD, this::guardPressed);
        this.keyFunctions.put(EpicFightKeyMappings.WEAPON_INNATE_SKILL, this::weaponInnateSkillKeyPressed);
        this.keyFunctions.put(EpicFightKeyMappings.MOVER_SKILL, this::moverKeyPressed);
        this.keyFunctions.put(EpicFightKeyMappings.LOCK_ON, this::lockonPressed);
        try {
            this.keyHash = (KeyMappingLookup) ObfuscationReflectionHelper.findField(KeyMapping.class, "f_90810_").get(null);
        } catch (IllegalAccessException | IllegalArgumentException var2) {
            var2.printStackTrace();
        }
    }

    public void setPlayerPatch(LocalPlayerPatch playerpatch) {
        this.weaponInnatePressCounter = 0;
        this.weaponInnatePressToggle = false;
        this.sneakPressCounter = 0;
        this.sneakPressToggle = false;
        this.attackLightPressToggle = false;
        this.player = playerpatch.getOriginal();
        this.playerpatch = playerpatch;
    }

    public LocalPlayerPatch getPlayerPatch() {
        return this.playerpatch;
    }

    public boolean canPlayerMove(EntityState playerState) {
        return !playerState.movementLocked() || this.player.jumpableVehicle() != null;
    }

    public boolean canPlayerRotate(EntityState playerState) {
        return !playerState.turningLocked() || this.player.jumpableVehicle() != null;
    }

    private boolean attackKeyPressed(KeyMapping key, int action) {
        if (action == 1 && this.playerpatch.isBattleMode() && this.currentChargingKey != key && !Minecraft.getInstance().isPaused()) {
            if (EpicFightKeyMappings.ATTACK.getKey().equals(EpicFightKeyMappings.WEAPON_INNATE_SKILL.getKey())) {
                if (!this.weaponInnatePressToggle) {
                    this.weaponInnatePressToggle = true;
                }
            } else {
                SkillSlot slot = !this.player.m_20096_() && !this.player.m_20069_() && this.player.m_20184_().y > 0.05 ? SkillSlots.AIR_ATTACK : SkillSlots.BASIC_ATTACK;
                if (this.playerpatch.getSkill(slot).sendExecuteRequest(this.playerpatch, this).isExecutable()) {
                    this.player.m_36334_();
                    this.attackLightPressToggle = false;
                    this.releaseAllServedKeys();
                } else if (!this.player.m_5833_() && slot == SkillSlots.BASIC_ATTACK) {
                    this.reserveKey(slot, EpicFightKeyMappings.ATTACK);
                }
                this.lockHotkeys();
                this.attackLightPressToggle = false;
                this.weaponInnatePressToggle = false;
                this.weaponInnatePressCounter = 0;
            }
            if (this.options.keyAttack.getKey() == EpicFightKeyMappings.ATTACK.getKey()) {
                return true;
            }
        }
        return false;
    }

    private boolean dodgeKeyPressed(KeyMapping key, int action) {
        if (action == 1 && this.playerpatch.isBattleMode() && this.currentChargingKey != key) {
            if (key.getKey().getValue() == this.options.keyShift.getKey().getValue()) {
                if (this.player.m_20202_() == null && !this.sneakPressToggle) {
                    this.sneakPressToggle = true;
                }
            } else {
                SkillSlot skillCategory = this.playerpatch.getEntityState().knockDown() ? SkillSlots.KNOCKDOWN_WAKEUP : SkillSlots.DODGE;
                SkillContainer skill = this.playerpatch.getSkill(skillCategory);
                if (skill.sendExecuteRequest(this.playerpatch, this).shouldReserverKey()) {
                    this.reserveKey(SkillSlots.DODGE, key);
                }
            }
        }
        return false;
    }

    private boolean guardPressed(KeyMapping key, int action) {
        if (action == 1) {
        }
        return false;
    }

    private boolean swapHandKeyPressed(KeyMapping key, int action) {
        if (this.playerpatch.getEntityState().inaction() || !this.playerpatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).canBePlacedOffhand()) {
            while (key.consumeClick()) {
            }
            this.setKeyBind(key, false);
        }
        return false;
    }

    private boolean switchModeKeyPressed(KeyMapping key, int action) {
        if (action == 1 && this.playerpatch.getOriginal().m_9236_().getGameRules().getBoolean(EpicFightGamerules.CAN_SWITCH_COMBAT)) {
            this.playerpatch.toggleMode();
        }
        return false;
    }

    private boolean weaponInnateSkillKeyPressed(KeyMapping key, int action) {
        if (action == 1 && this.playerpatch.isBattleMode() && this.currentChargingKey != key && !EpicFightKeyMappings.ATTACK.getKey().equals(EpicFightKeyMappings.WEAPON_INNATE_SKILL.getKey())) {
            if (this.playerpatch.getSkill(SkillSlots.WEAPON_INNATE).sendExecuteRequest(this.playerpatch, this).shouldReserverKey()) {
                if (!this.player.m_5833_()) {
                    this.reserveKey(SkillSlots.WEAPON_INNATE, key);
                }
            } else {
                this.lockHotkeys();
            }
        }
        return false;
    }

    private boolean moverKeyPressed(KeyMapping key, int action) {
        if (action == 1 && this.playerpatch.isBattleMode() && !this.playerpatch.isChargingSkill()) {
            if (key.getKey().getValue() == this.options.keyJump.getKey().getValue()) {
                SkillContainer skillContainer = this.playerpatch.getSkill(SkillSlots.MOVER);
                SkillExecuteEvent event = new SkillExecuteEvent(this.playerpatch, skillContainer);
                if (skillContainer.canExecute(this.playerpatch, event) && this.player.m_20202_() == null && !this.moverPressToggle) {
                    this.moverPressToggle = true;
                }
            } else {
                SkillContainer skill = this.playerpatch.getSkill(SkillSlots.MOVER);
                skill.sendExecuteRequest(this.playerpatch, this);
            }
        }
        return false;
    }

    private boolean lockonPressed(KeyMapping key, int action) {
        if (action == 1) {
            this.playerpatch.toggleLockOn();
        }
        return false;
    }

    private void inputTick(Input input) {
        if (this.moverPressToggle) {
            if (!this.isKeyDown(this.options.keyJump)) {
                this.moverPressToggle = false;
                this.moverPressCounter = 0;
                if (this.player.m_20096_()) {
                    input.jumping = true;
                }
            } else if (this.moverPressCounter > (Integer) EpicFightMod.CLIENT_CONFIGS.longPressCount.getValue()) {
                SkillContainer skill = this.playerpatch.getSkill(SkillSlots.MOVER);
                skill.sendExecuteRequest(this.playerpatch, this);
                this.moverPressToggle = false;
                this.moverPressCounter = 0;
            } else {
                input.jumping = false;
                this.moverPressCounter++;
            }
        }
        if (!this.canPlayerMove(this.playerpatch.getEntityState())) {
            input.forwardImpulse = 0.0F;
            input.leftImpulse = 0.0F;
            input.up = false;
            input.down = false;
            input.left = false;
            input.right = false;
            input.jumping = false;
            input.shiftKeyDown = false;
            this.player.sprintTriggerTime = -1;
            this.player.m_6858_(false);
        }
        if (this.player.m_6084_()) {
            this.playerpatch.getEventListener().triggerEvents(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, new MovementInputEvent(this.playerpatch, input));
        }
    }

    private void tick() {
        if (EpicFightKeyMappings.SKILL_EDIT.consumeClick() && this.playerpatch.getSkillCapability() != null) {
            Minecraft.getInstance().setScreen(new SkillEditScreen(this.player, this.playerpatch.getSkillCapability()));
        }
        if (EpicFightKeyMappings.CONFIG.consumeClick()) {
            Minecraft.getInstance().setScreen(new IngameConfigurationScreen(this.minecraft, null));
        }
        if (this.playerpatch != null && this.playerpatch.isBattleMode() && !Minecraft.getInstance().isPaused()) {
            if (this.player.f_19797_ - this.lastHotbarLockedTime > 20 && this.hotbarLocked) {
                this.unlockHotkeys();
            }
            if (this.weaponInnatePressToggle) {
                if (!this.isKeyDown(EpicFightKeyMappings.WEAPON_INNATE_SKILL)) {
                    this.attackLightPressToggle = true;
                    this.weaponInnatePressToggle = false;
                    this.weaponInnatePressCounter = 0;
                } else if (EpicFightKeyMappings.WEAPON_INNATE_SKILL.getKey().equals(EpicFightKeyMappings.ATTACK.getKey())) {
                    if (this.weaponInnatePressCounter > (Integer) EpicFightMod.CLIENT_CONFIGS.longPressCount.getValue()) {
                        if (this.minecraft.hitResult.getType() == HitResult.Type.BLOCK && this.playerpatch.getTarget() == null && !EpicFightMod.CLIENT_CONFIGS.noMiningInCombat.getValue()) {
                            this.minecraft.startAttack();
                            this.setKeyBind(EpicFightKeyMappings.ATTACK, true);
                        } else if (this.playerpatch.getSkill(SkillSlots.WEAPON_INNATE).sendExecuteRequest(this.playerpatch, this).shouldReserverKey()) {
                            if (!this.player.m_5833_()) {
                                this.reserveKey(SkillSlots.WEAPON_INNATE, EpicFightKeyMappings.WEAPON_INNATE_SKILL);
                            }
                        } else {
                            this.lockHotkeys();
                        }
                        this.weaponInnatePressToggle = false;
                        this.weaponInnatePressCounter = 0;
                    } else {
                        this.weaponInnatePressCounter++;
                    }
                }
            }
            if (this.attackLightPressToggle) {
                SkillSlot slot = !this.player.m_20096_() && !this.player.m_20069_() && this.player.m_20184_().y > 0.05 ? SkillSlots.AIR_ATTACK : SkillSlots.BASIC_ATTACK;
                if (this.playerpatch.getSkill(slot).sendExecuteRequest(this.playerpatch, this).isExecutable()) {
                    this.player.m_36334_();
                    this.releaseAllServedKeys();
                } else if (!this.player.m_5833_() && slot == SkillSlots.BASIC_ATTACK) {
                    this.reserveKey(slot, EpicFightKeyMappings.ATTACK);
                }
                this.lockHotkeys();
                this.attackLightPressToggle = false;
                this.weaponInnatePressToggle = false;
                this.weaponInnatePressCounter = 0;
            }
            if (this.sneakPressToggle) {
                if (!this.isKeyDown(this.options.keyShift)) {
                    SkillSlot skillSlot = this.playerpatch.getEntityState().knockDown() ? SkillSlots.KNOCKDOWN_WAKEUP : SkillSlots.DODGE;
                    SkillContainer skill = this.playerpatch.getSkill(skillSlot);
                    if (skill.sendExecuteRequest(this.playerpatch, this).shouldReserverKey()) {
                        this.reserveKey(skillSlot, this.options.keyShift);
                    }
                    this.sneakPressToggle = false;
                    this.sneakPressCounter = 0;
                } else if (this.sneakPressCounter > (Integer) EpicFightMod.CLIENT_CONFIGS.longPressCount.getValue()) {
                    this.sneakPressToggle = false;
                    this.sneakPressCounter = 0;
                } else {
                    this.sneakPressCounter++;
                }
            }
            if (this.currentChargingKey != null) {
                SkillContainer skill = this.playerpatch.getSkill(this.reservedOrChargingSkillSlot);
                if (skill.getSkill() instanceof ChargeableSkill chargingSkill) {
                    if (!this.isKeyDown(this.currentChargingKey)) {
                        this.chargeKeyUnpressed = true;
                    }
                    if (this.chargeKeyUnpressed && this.playerpatch.getSkillChargingTicks() > chargingSkill.getMinChargingTicks()) {
                        if (skill.getSkill() != null) {
                            skill.sendExecuteRequest(this.playerpatch, this);
                        }
                        this.releaseAllServedKeys();
                    }
                    if (this.playerpatch.getSkillChargingTicks() >= chargingSkill.getAllowedMaxChargingTicks()) {
                        this.releaseAllServedKeys();
                    }
                } else {
                    this.releaseAllServedKeys();
                }
            }
            if (this.reservedKey != null) {
                if (this.reserveCounter > 0) {
                    SkillContainer skill = this.playerpatch.getSkill(this.reservedOrChargingSkillSlot);
                    this.reserveCounter--;
                    if (skill.getSkill() != null && skill.sendExecuteRequest(this.playerpatch, this).isExecutable()) {
                        this.releaseAllServedKeys();
                        this.lockHotkeys();
                    }
                } else {
                    this.releaseAllServedKeys();
                }
            }
            if (this.playerpatch.getEntityState().inaction() || this.hotbarLocked) {
                for (int i = 0; i < 9; i++) {
                    while (this.options.keyHotbarSlots[i].consumeClick()) {
                    }
                }
            }
        }
    }

    private void reserveKey(SkillSlot slot, KeyMapping keyMapping) {
        this.reservedKey = keyMapping;
        this.reservedOrChargingSkillSlot = slot;
        this.reserveCounter = 8;
    }

    private void releaseAllServedKeys() {
        this.chargeKeyUnpressed = true;
        this.currentChargingKey = null;
        this.reservedOrChargingSkillSlot = null;
        this.reserveCounter = -1;
        this.reservedKey = null;
    }

    public void setChargingKey(SkillSlot chargingSkillSlot, KeyMapping keyMapping) {
        this.chargeKeyUnpressed = false;
        this.currentChargingKey = keyMapping;
        this.reservedOrChargingSkillSlot = chargingSkillSlot;
        this.reserveCounter = -1;
        this.reservedKey = null;
    }

    public boolean isKeyDown(KeyMapping key) {
        if (key.getKey().getType() == InputConstants.Type.KEYSYM) {
            return key.isDown() || GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), key.getKey().getValue()) > 0;
        } else {
            return key.getKey().getType() != InputConstants.Type.MOUSE ? false : key.isDown() || GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), key.getKey().getValue()) > 0;
        }
    }

    public void setKeyBind(KeyMapping key, boolean setter) {
        KeyMapping.set(key.getKey(), setter);
    }

    public void lockHotkeys() {
        this.hotbarLocked = true;
        this.lastHotbarLockedTime = this.player.f_19797_;
        for (int i = 0; i < 9; i++) {
            while (this.options.keyHotbarSlots[i].consumeClick()) {
            }
        }
    }

    public void unlockHotkeys() {
        this.hotbarLocked = false;
    }

    public void addPacketToSend(Object packet) {
        this.packets.add(packet);
    }

    @OnlyIn(Dist.CLIENT)
    @EventBusSubscriber(modid = "epicfight", value = { Dist.CLIENT })
    public static class Events {

        static ControllEngine controllEngine;

        @SubscribeEvent
        public static void mouseEvent(InputEvent.MouseButton.Pre event) {
            if (controllEngine.minecraft.player != null && Minecraft.getInstance().screen == null) {
                InputConstants.Key input = InputConstants.Type.KEYSYM.getOrCreate(event.getButton());
                InputConstants.Key inputMouse = InputConstants.Type.MOUSE.getOrCreate(event.getButton());
                boolean canceled = false;
                for (KeyMapping keyMapping : controllEngine.keyHash.getAll(input)) {
                    if (controllEngine.keyFunctions.containsKey(keyMapping)) {
                        canceled |= ((BiFunction) controllEngine.keyFunctions.get(keyMapping)).apply(keyMapping, event.getAction());
                    }
                }
                for (KeyMapping keyMappingx : controllEngine.keyHash.getAll(inputMouse)) {
                    if (controllEngine.keyFunctions.containsKey(keyMappingx)) {
                        canceled |= ((BiFunction) controllEngine.keyFunctions.get(keyMappingx)).apply(keyMappingx, event.getAction());
                    }
                }
                event.setCanceled(canceled);
            }
        }

        @SubscribeEvent
        public static void keyboardEvent(InputEvent.Key event) {
            if (controllEngine.minecraft.player != null && Minecraft.getInstance().screen == null) {
                InputConstants.Key input = InputConstants.Type.KEYSYM.getOrCreate(event.getKey());
                InputConstants.Key inputMouse = InputConstants.Type.MOUSE.getOrCreate(event.getKey());
                for (KeyMapping keyMapping : controllEngine.keyHash.getAll(input)) {
                    if (controllEngine.keyFunctions.containsKey(keyMapping)) {
                        ((BiFunction) controllEngine.keyFunctions.get(keyMapping)).apply(keyMapping, event.getAction());
                    }
                }
                for (KeyMapping keyMappingx : controllEngine.keyHash.getAll(inputMouse)) {
                    if (controllEngine.keyFunctions.containsKey(keyMappingx)) {
                        ((BiFunction) controllEngine.keyFunctions.get(keyMappingx)).apply(keyMappingx, event.getAction());
                    }
                }
            }
        }

        @SubscribeEvent
        public static void mouseScrollEvent(InputEvent.MouseScrollingEvent event) {
            if (controllEngine.minecraft.player != null && controllEngine.playerpatch != null && controllEngine.playerpatch.getEntityState().inaction() && controllEngine.minecraft.screen == null) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void moveInputEvent(MovementInputUpdateEvent event) {
            if (controllEngine.playerpatch != null) {
                controllEngine.inputTick(event.getInput());
            }
        }

        @SubscribeEvent
        public static void clientTickEndEvent(TickEvent.ClientTickEvent event) {
            if (controllEngine.minecraft.player != null) {
                if (event.phase == TickEvent.Phase.START) {
                    controllEngine.tick();
                } else {
                    for (Object packet : controllEngine.packets) {
                        EpicFightNetworkManager.sendToServer(packet);
                    }
                    controllEngine.packets.clear();
                }
            }
        }
    }
}