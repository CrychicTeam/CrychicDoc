declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/pnc/$CapabilityAir" {
import {$CapabilityAir$ItemStackBuilderCustom, $CapabilityAir$ItemStackBuilderCustom$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/pnc/$CapabilityAir$ItemStackBuilderCustom"
import {$CapabilityAir$ItemStackBuilder, $CapabilityAir$ItemStackBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/pnc/$CapabilityAir$ItemStackBuilder"

export class $CapabilityAir {
static readonly "AIR_TAG": string

constructor()

public "itemStack"(capacity: integer, maxPressure: float): $CapabilityAir$ItemStackBuilder
public "itemStackCustom"(): $CapabilityAir$ItemStackBuilderCustom
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityAir$Type = ($CapabilityAir);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityAir_ = $CapabilityAir$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/curios/$RendererCurios" {
import {$HumanoidModel, $HumanoidModel$Type} from "packages/net/minecraft/client/model/$HumanoidModel"
import {$RenderLayerParent, $RenderLayerParent$Type} from "packages/net/minecraft/client/renderer/entity/$RenderLayerParent"
import {$ICurioRenderer, $ICurioRenderer$Type} from "packages/top/theillusivec4/curios/api/client/$ICurioRenderer"
import {$RendererCurios$RenderContext, $RendererCurios$RenderContext$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/curios/$RendererCurios$RenderContext"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$EntityModel, $EntityModel$Type} from "packages/net/minecraft/client/model/$EntityModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"

export class $RendererCurios implements $ICurioRenderer {

constructor(renderer: $Consumer$Type<($RendererCurios$RenderContext$Type)>)

public "render"<T extends $LivingEntity, M extends $EntityModel<(T)>>(stack: $ItemStack$Type, slotContext: $SlotContext$Type, matrixStack: $PoseStack$Type, renderLayerParent: $RenderLayerParent$Type<(T), (M)>, renderTypeBuffer: $MultiBufferSource$Type, light: integer, limbSwing: float, limbSwingAmount: float, partialTicks: float, ageInTicks: float, netHeadYaw: float, headPitch: float): void
public static "followHeadRotations"(arg0: $LivingEntity$Type, ...arg1: ($ModelPart$Type)[]): void
public static "translateIfSneaking"(arg0: $PoseStack$Type, arg1: $LivingEntity$Type): void
public static "followBodyRotations"(arg0: $LivingEntity$Type, ...arg1: ($HumanoidModel$Type<($LivingEntity$Type)>)[]): void
public static "rotateIfSneaking"(arg0: $PoseStack$Type, arg1: $LivingEntity$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RendererCurios$Type = ($RendererCurios);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RendererCurios_ = $RendererCurios$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/chemical/$CapabilityPigment" {
import {$CapabilityPigment$BlockEntityBuilder, $CapabilityPigment$BlockEntityBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/chemical/$CapabilityPigment$BlockEntityBuilder"
import {$CapabilityPigment$ItemStackBuilder, $CapabilityPigment$ItemStackBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/chemical/$CapabilityPigment$ItemStackBuilder"

export class $CapabilityPigment {

constructor()

public "blockEntity"(): $CapabilityPigment$BlockEntityBuilder
public "itemStack"(): $CapabilityPigment$ItemStackBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityPigment$Type = ($CapabilityPigment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityPigment_ = $CapabilityPigment$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityFluid$CustomTankBuilderItemStack" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$IFluidHandlerItem, $IFluidHandlerItem$Type} from "packages/net/minecraftforge/fluids/capability/$IFluidHandlerItem"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$CapabilityFluid$FluidIOItemStack, $CapabilityFluid$FluidIOItemStack$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityFluid$FluidIOItemStack"
import {$CapabilityBuilderForge, $CapabilityBuilderForge$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityBuilderForge"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$FluidStackJS, $FluidStackJS$Type} from "packages/dev/latvian/mods/kubejs/fluid/$FluidStackJS"

export class $CapabilityFluid$CustomTankBuilderItemStack extends $CapabilityBuilderForge<($ItemStack), ($IFluidHandlerItem)> {

constructor()

public "getCapacity"(getCapacity: $ToIntFunction$Type<($ItemStack$Type)>): $CapabilityFluid$CustomTankBuilderItemStack
public "onFill"(onFill: $CapabilityFluid$FluidIOItemStack$Type): $CapabilityFluid$CustomTankBuilderItemStack
public "withCapacity"(capacity: integer): $CapabilityFluid$CustomTankBuilderItemStack
public "isFluidGood"(isFluidGood: $BiPredicate$Type<($ItemStack$Type), ($FluidStackJS$Type)>): $CapabilityFluid$CustomTankBuilderItemStack
public "onDrain"(onDrain: $CapabilityFluid$FluidIOItemStack$Type): $CapabilityFluid$CustomTankBuilderItemStack
public "acceptFluid"(fluid: $Fluid$Type): $CapabilityFluid$CustomTankBuilderItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityFluid$CustomTankBuilderItemStack$Type = ($CapabilityFluid$CustomTankBuilderItemStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityFluid$CustomTankBuilderItemStack_ = $CapabilityFluid$CustomTankBuilderItemStack$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityForgeEnergy$ItemStorageBuilder" {
import {$IEnergyStorage, $IEnergyStorage$Type} from "packages/net/minecraftforge/energy/$IEnergyStorage"
import {$CapabilityBuilderForge, $CapabilityBuilderForge$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityBuilderForge"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $CapabilityForgeEnergy$ItemStorageBuilder extends $CapabilityBuilderForge<($ItemStack), ($IEnergyStorage)> {

constructor(capacity: integer, canExtract: boolean, canReceive: boolean)

public "getCapability"(instance: $ItemStack$Type): $IEnergyStorage
public "receiveRate"(receiveRate: integer): $CapabilityForgeEnergy$ItemStorageBuilder
public "extractRate"(extractRate: integer): $CapabilityForgeEnergy$ItemStorageBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityForgeEnergy$ItemStorageBuilder$Type = ($CapabilityForgeEnergy$ItemStorageBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityForgeEnergy$ItemStorageBuilder_ = $CapabilityForgeEnergy$ItemStorageBuilder$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/curios/$CapabilityCurios$ShouldDrop" {
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $CapabilityCurios$ShouldDrop {

 "test"(arg0: $ItemStack$Type, arg1: $SlotContext$Type, arg2: $DamageSource$Type, arg3: integer, arg4: boolean): boolean

(arg0: $ItemStack$Type, arg1: $SlotContext$Type, arg2: $DamageSource$Type, arg3: integer, arg4: boolean): boolean
}

export namespace $CapabilityCurios$ShouldDrop {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityCurios$ShouldDrop$Type = ($CapabilityCurios$ShouldDrop);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityCurios$ShouldDrop_ = $CapabilityCurios$ShouldDrop$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/chemical/$CapabilityChemical" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CapabilityChemical {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityChemical$Type = ($CapabilityChemical);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityChemical_ = $CapabilityChemical$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/events/$BlockEntityCapEventJS" {
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$CapabilityBuilder, $CapabilityBuilder$Type} from "packages/com/prunoideae/powerfuljs/$CapabilityBuilder"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"

export class $BlockEntityCapEventJS extends $EventJS {

constructor()

public "attach"(entityType: $BlockEntityType$Type<(any)>, builder: $CapabilityBuilder$Type<($BlockEntity$Type), (any), (any)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityCapEventJS$Type = ($BlockEntityCapEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityCapEventJS_ = $BlockEntityCapEventJS$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/botania/$CapabilityMana" {
import {$CapabilityMana$ItemStackBuilder, $CapabilityMana$ItemStackBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/botania/$CapabilityMana$ItemStackBuilder"
import {$CapabilityMana$BlockEntityBuilder, $CapabilityMana$BlockEntityBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/botania/$CapabilityMana$BlockEntityBuilder"

export class $CapabilityMana {

constructor()

public "blockEntity"(): $CapabilityMana$BlockEntityBuilder
public "itemStack"(): $CapabilityMana$ItemStackBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityMana$Type = ($CapabilityMana);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityMana_ = $CapabilityMana$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityFluid" {
import {$CapabilityFluid$CustomTankBuilderItemStack, $CapabilityFluid$CustomTankBuilderItemStack$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityFluid$CustomTankBuilderItemStack"
import {$CapabilityFluid$CustomTankBuilderBlockEntity, $CapabilityFluid$CustomTankBuilderBlockEntity$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityFluid$CustomTankBuilderBlockEntity"
import {$CapabilityFluid$NormalTankBuilderItemStack, $CapabilityFluid$NormalTankBuilderItemStack$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityFluid$NormalTankBuilderItemStack"

export class $CapabilityFluid {

constructor()

public "customBlockEntity"(): $CapabilityFluid$CustomTankBuilderBlockEntity
public "customItemStack"(): $CapabilityFluid$CustomTankBuilderItemStack
public "itemStack"(capacity: integer): $CapabilityFluid$NormalTankBuilderItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityFluid$Type = ($CapabilityFluid);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityFluid_ = $CapabilityFluid$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/curios/$CapabilityCurios$ItemStackBuilder" {
import {$ICurio, $ICurio$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio"
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$CapabilityCurios$AttributeModificationContext, $CapabilityCurios$AttributeModificationContext$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/curios/$CapabilityCurios$AttributeModificationContext"
import {$CapabilityCurios$EquipCallback, $CapabilityCurios$EquipCallback$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/curios/$CapabilityCurios$EquipCallback"
import {$CapabilityBuilderForge, $CapabilityBuilderForge$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityBuilderForge"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$CapabilityCurios$ShouldDrop, $CapabilityCurios$ShouldDrop$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/curios/$CapabilityCurios$ShouldDrop"

export class $CapabilityCurios$ItemStackBuilder extends $CapabilityBuilderForge<($ItemStack), ($ICurio)> {

constructor()

public "modifyAttribute"(attribute: $ResourceLocation$Type, identifier: string, d: double, operation: $AttributeModifier$Operation$Type): $CapabilityCurios$ItemStackBuilder
public "onEquip"(onEquip: $CapabilityCurios$EquipCallback$Type): $CapabilityCurios$ItemStackBuilder
public "curioTick"(curioTick: $BiConsumer$Type<($ItemStack$Type), ($SlotContext$Type)>): $CapabilityCurios$ItemStackBuilder
public "canUnequip"(canUnequip: $BiPredicate$Type<($ItemStack$Type), ($SlotContext$Type)>): $CapabilityCurios$ItemStackBuilder
public "getDropRule"(getDropRule: $CapabilityCurios$ShouldDrop$Type): $CapabilityCurios$ItemStackBuilder
public "onUnequip"(onUnequip: $CapabilityCurios$EquipCallback$Type): $CapabilityCurios$ItemStackBuilder
public "canEquip"(canEquip: $BiPredicate$Type<($ItemStack$Type), ($SlotContext$Type)>): $CapabilityCurios$ItemStackBuilder
public "getCapability"(instance: $ItemStack$Type): $ICurio
public "dynamicAttribute"(context: $Consumer$Type<($CapabilityCurios$AttributeModificationContext$Type)>): $CapabilityCurios$ItemStackBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityCurios$ItemStackBuilder$Type = ($CapabilityCurios$ItemStackBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityCurios$ItemStackBuilder_ = $CapabilityCurios$ItemStackBuilder$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityItem" {
import {$CapabilityItem$BlockEntityBuilder, $CapabilityItem$BlockEntityBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityItem$BlockEntityBuilder"

export class $CapabilityItem {

constructor()

public "blockEntity"(): $CapabilityItem$BlockEntityBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityItem$Type = ($CapabilityItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityItem_ = $CapabilityItem$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityItem$InsertItem" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $CapabilityItem$InsertItem {

 "insert"(arg0: $BlockEntity$Type, arg1: integer, arg2: $ItemStack$Type, arg3: boolean): $ItemStack

(arg0: $BlockEntity$Type, arg1: integer, arg2: $ItemStack$Type, arg3: boolean): $ItemStack
}

export namespace $CapabilityItem$InsertItem {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityItem$InsertItem$Type = ($CapabilityItem$InsertItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityItem$InsertItem_ = $CapabilityItem$InsertItem$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/immersive/$CapabilitiesIE" {
import {$CapabilityExternalHeatable, $CapabilityExternalHeatable$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/immersive/$CapabilityExternalHeatable"
import {$CapabilityRotationAcceptor, $CapabilityRotationAcceptor$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/immersive/$CapabilityRotationAcceptor"

export interface $CapabilitiesIE {

}

export namespace $CapabilitiesIE {
const ROTATION_ACCEPTOR: $CapabilityRotationAcceptor
const EXTERNAL_HEATABLE: $CapabilityExternalHeatable
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilitiesIE$Type = ($CapabilitiesIE);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilitiesIE_ = $CapabilitiesIE$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/forge/$CapabilityServiceForge" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$CapabilityBuilderForge, $CapabilityBuilderForge$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityBuilderForge"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $CapabilityServiceForge {
static readonly "INSTANCE": $CapabilityServiceForge

constructor()

public "getCapabilitiesFor"(stack: $ItemStack$Type): $Stream<($CapabilityBuilderForge<($ItemStack), (any)>)>
public "getCapabilitiesFor"(entity: $Entity$Type): $Stream<($CapabilityBuilderForge<($Entity), (any)>)>
public "getCapabilitiesFor"(be: $BlockEntity$Type): $Stream<($CapabilityBuilderForge<($BlockEntity), (any)>)>
public "addEntity"(predicate: $Predicate$Type<($Entity$Type)>, builder: $CapabilityBuilderForge$Type<($Entity$Type), (any)>): void
public "addBE"(predicate: $Predicate$Type<($BlockEntity$Type)>, builder: $CapabilityBuilderForge$Type<($BlockEntity$Type), (any)>): void
public "addItem"(predicate: $Predicate$Type<($ItemStack$Type)>, builder: $CapabilityBuilderForge$Type<($ItemStack$Type), (any)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityServiceForge$Type = ($CapabilityServiceForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityServiceForge_ = $CapabilityServiceForge$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/chemical/$CapabilitySlurry" {
import {$CapabilitySlurry$BlockEntityBuilder, $CapabilitySlurry$BlockEntityBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/chemical/$CapabilitySlurry$BlockEntityBuilder"
import {$CapabilitySlurry$ItemStackBuilder, $CapabilitySlurry$ItemStackBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/chemical/$CapabilitySlurry$ItemStackBuilder"

export class $CapabilitySlurry {

constructor()

public "blockEntity"(): $CapabilitySlurry$BlockEntityBuilder
public "itemStack"(): $CapabilitySlurry$ItemStackBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilitySlurry$Type = ($CapabilitySlurry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilitySlurry_ = $CapabilitySlurry$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/curios/$EventCurios" {
import {$EventHandler, $EventHandler$Type} from "packages/dev/latvian/mods/kubejs/event/$EventHandler"
import {$EventGroup, $EventGroup$Type} from "packages/dev/latvian/mods/kubejs/event/$EventGroup"

export interface $EventCurios {

}

export namespace $EventCurios {
const GROUP: $EventGroup
const REGISTER_RENDERER: $EventHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventCurios$Type = ($EventCurios);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventCurios_ = $EventCurios$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/$CapabilityProtection" {
import {$CapabilityProtection$LaserItemStackBuilder, $CapabilityProtection$LaserItemStackBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/$CapabilityProtection$LaserItemStackBuilder"
import {$CapabilityProtection$RadiationItemStackBuilder, $CapabilityProtection$RadiationItemStackBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/$CapabilityProtection$RadiationItemStackBuilder"

export class $CapabilityProtection {

constructor()

public "itemStackLaserProtection"(): $CapabilityProtection$LaserItemStackBuilder
public "itemStackRadiationProtection"(): $CapabilityProtection$RadiationItemStackBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityProtection$Type = ($CapabilityProtection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityProtection_ = $CapabilityProtection$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityBuilderForge" {
import {$CapabilityProvider, $CapabilityProvider$Type} from "packages/net/minecraftforge/common/capabilities/$CapabilityProvider"
import {$CapabilityBuilder, $CapabilityBuilder$Type} from "packages/com/prunoideae/powerfuljs/$CapabilityBuilder"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"

export class $CapabilityBuilderForge<I extends $CapabilityProvider<(I)>, T> extends $CapabilityBuilder<(I), (T), ($Capability<(T)>)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityBuilderForge$Type<I, T> = ($CapabilityBuilderForge<(I), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityBuilderForge_<I, T> = $CapabilityBuilderForge$Type<(I), (T)>;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityForgeEnergy$EnergyIOBlockEntity" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"

export interface $CapabilityForgeEnergy$EnergyIOBlockEntity {

 "transfer"(arg0: $BlockEntity$Type, arg1: integer, arg2: boolean): integer

(arg0: $BlockEntity$Type, arg1: integer, arg2: boolean): integer
}

export namespace $CapabilityForgeEnergy$EnergyIOBlockEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityForgeEnergy$EnergyIOBlockEntity$Type = ($CapabilityForgeEnergy$EnergyIOBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityForgeEnergy$EnergyIOBlockEntity_ = $CapabilityForgeEnergy$EnergyIOBlockEntity$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/immersive/$CapabilityExternalHeatable" {
import {$CapabilityExternalHeatable$BlockEntityBuilder, $CapabilityExternalHeatable$BlockEntityBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/immersive/$CapabilityExternalHeatable$BlockEntityBuilder"

export class $CapabilityExternalHeatable {

constructor()

public "blockEntity"(): $CapabilityExternalHeatable$BlockEntityBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityExternalHeatable$Type = ($CapabilityExternalHeatable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityExternalHeatable_ = $CapabilityExternalHeatable$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/curios/$CapabilityCurios$EquipCallback" {
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $CapabilityCurios$EquipCallback {

 "changed"(arg0: $ItemStack$Type, arg1: $SlotContext$Type, arg2: $ItemStack$Type): void

(arg0: $ItemStack$Type, arg1: $SlotContext$Type, arg2: $ItemStack$Type): void
}

export namespace $CapabilityCurios$EquipCallback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityCurios$EquipCallback$Type = ($CapabilityCurios$EquipCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityCurios$EquipCallback_ = $CapabilityCurios$EquipCallback$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/$PowerfulJSPlugin" {
import {$KubeJSPlugin, $KubeJSPlugin$Type} from "packages/dev/latvian/mods/kubejs/$KubeJSPlugin"
import {$EventHandler, $EventHandler$Type} from "packages/dev/latvian/mods/kubejs/event/$EventHandler"
import {$EventGroup, $EventGroup$Type} from "packages/dev/latvian/mods/kubejs/event/$EventGroup"

export class $PowerfulJSPlugin extends $KubeJSPlugin {
static readonly "GROUP": $EventGroup
static readonly "REGISTER_BE_CAP": $EventHandler
static readonly "REGISTER_ENTITY_CAP": $EventHandler

constructor()

public "afterInit"(): void
public "registerEvents"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PowerfulJSPlugin$Type = ($PowerfulJSPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PowerfulJSPlugin_ = $PowerfulJSPlugin$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/immersive/$CapabilityRotationAcceptor" {
import {$CapabilityRotationAcceptor$BlockEntityBuilder, $CapabilityRotationAcceptor$BlockEntityBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/immersive/$CapabilityRotationAcceptor$BlockEntityBuilder"

export class $CapabilityRotationAcceptor {

constructor()

public "blockEntity"(): $CapabilityRotationAcceptor$BlockEntityBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityRotationAcceptor$Type = ($CapabilityRotationAcceptor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityRotationAcceptor_ = $CapabilityRotationAcceptor$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/pnc/$CapabilitiesPneumatic" {
import {$CapabilityAir, $CapabilityAir$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/pnc/$CapabilityAir"

export interface $CapabilitiesPneumatic {

}

export namespace $CapabilitiesPneumatic {
const AIR: $CapabilityAir
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilitiesPneumatic$Type = ($CapabilitiesPneumatic);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilitiesPneumatic_ = $CapabilitiesPneumatic$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/$CapabilityEvaporationTower" {
import {$CapabilityEvaporationTower$BlockEntityBuilder, $CapabilityEvaporationTower$BlockEntityBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/$CapabilityEvaporationTower$BlockEntityBuilder"

export class $CapabilityEvaporationTower {

constructor()

public "blockEntity"(): $CapabilityEvaporationTower$BlockEntityBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityEvaporationTower$Type = ($CapabilityEvaporationTower);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityEvaporationTower_ = $CapabilityEvaporationTower$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/events/forge/$DynamicItemStackEventJS" {
import {$DynamicAttachEventJS, $DynamicAttachEventJS$Type} from "packages/com/prunoideae/powerfuljs/events/forge/$DynamicAttachEventJS"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$CapabilityBuilderForge, $CapabilityBuilderForge$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityBuilderForge"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $DynamicItemStackEventJS extends $DynamicAttachEventJS<($ItemStack)> {

constructor()

public "add"(predicate: $Predicate$Type<($ItemStack$Type)>, provider: $CapabilityBuilderForge$Type<($ItemStack$Type), (any)>): $DynamicAttachEventJS<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicItemStackEventJS$Type = ($DynamicItemStackEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicItemStackEventJS_ = $DynamicItemStackEventJS$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/events/forge/$DynamicBEEventJS" {
import {$DynamicAttachEventJS, $DynamicAttachEventJS$Type} from "packages/com/prunoideae/powerfuljs/events/forge/$DynamicAttachEventJS"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$CapabilityBuilderForge, $CapabilityBuilderForge$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityBuilderForge"

export class $DynamicBEEventJS extends $DynamicAttachEventJS<($BlockEntity)> {

constructor()

public "add"(predicate: $Predicate$Type<($BlockEntity$Type)>, provider: $CapabilityBuilderForge$Type<($BlockEntity$Type), (any)>): $DynamicAttachEventJS<($BlockEntity)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicBEEventJS$Type = ($DynamicBEEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicBEEventJS_ = $DynamicBEEventJS$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/$CapabilityBuilder" {
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"

export class $CapabilityBuilder<I, T, K> {

constructor()

public "side"(direction: $Direction$Type): $CapabilityBuilder<(I), (T), (K)>
public "availableOn"(isAvailable: $BiPredicate$Type<(I), ($Direction$Type)>): $CapabilityBuilder<(I), (T), (K)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityBuilder$Type<I, T, K> = ($CapabilityBuilder<(I), (T), (K)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityBuilder_<I, T, K> = $CapabilityBuilder$Type<(I), (T), (K)>;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/curios/$CapabilitiesCurios" {
import {$CapabilityCurios, $CapabilityCurios$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/curios/$CapabilityCurios"

export interface $CapabilitiesCurios {

}

export namespace $CapabilitiesCurios {
const CURIOS: $CapabilityCurios
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilitiesCurios$Type = ($CapabilitiesCurios);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilitiesCurios_ = $CapabilitiesCurios$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilitiesForge" {
import {$CapabilityItem, $CapabilityItem$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityItem"
import {$CapabilityForgeEnergy, $CapabilityForgeEnergy$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityForgeEnergy"
import {$CapabilityFluid, $CapabilityFluid$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityFluid"

export interface $CapabilitiesForge {

}

export namespace $CapabilitiesForge {
const ENERGY: $CapabilityForgeEnergy
const FLUID: $CapabilityFluid
const ITEM: $CapabilityItem
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilitiesForge$Type = ($CapabilitiesForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilitiesForge_ = $CapabilitiesForge$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/chemical/$CapabilityGas" {
import {$CapabilityGas$ItemStackBuilder, $CapabilityGas$ItemStackBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/chemical/$CapabilityGas$ItemStackBuilder"
import {$CapabilityGas$BlockEntityBuilder, $CapabilityGas$BlockEntityBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/chemical/$CapabilityGas$BlockEntityBuilder"

export class $CapabilityGas {

constructor()

public "blockEntity"(): $CapabilityGas$BlockEntityBuilder
public "itemStack"(): $CapabilityGas$ItemStackBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityGas$Type = ($CapabilityGas);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityGas_ = $CapabilityGas$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/proxy/$PowerfulJSClient" {
import {$PowerfulJSCommon, $PowerfulJSCommon$Type} from "packages/com/prunoideae/powerfuljs/proxy/$PowerfulJSCommon"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $PowerfulJSClient extends $PowerfulJSCommon {

constructor()

public "runOnClient"(run: $Runnable$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PowerfulJSClient$Type = ($PowerfulJSClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PowerfulJSClient_ = $PowerfulJSClient$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/botania/$CapabilityBlockProvider" {
import {$CapabilityBlockProvider$BuilderItemStack, $CapabilityBlockProvider$BuilderItemStack$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/botania/$CapabilityBlockProvider$BuilderItemStack"

export class $CapabilityBlockProvider {

constructor()

public "blockProvider"(): $CapabilityBlockProvider$BuilderItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityBlockProvider$Type = ($CapabilityBlockProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityBlockProvider_ = $CapabilityBlockProvider$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/curios/$CapabilityCurios" {
import {$CapabilityCurios$ItemStackBuilder, $CapabilityCurios$ItemStackBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/curios/$CapabilityCurios$ItemStackBuilder"

export class $CapabilityCurios {

constructor()

public "itemStack"(): $CapabilityCurios$ItemStackBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityCurios$Type = ($CapabilityCurios);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityCurios_ = $CapabilityCurios$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/botania/$CapabilityExoflame" {
import {$CapabilityExoflame$BuilderBlockEntity, $CapabilityExoflame$BuilderBlockEntity$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/botania/$CapabilityExoflame$BuilderBlockEntity"

export class $CapabilityExoflame {

constructor()

public "tileEntity"(): $CapabilityExoflame$BuilderBlockEntity
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityExoflame$Type = ($CapabilityExoflame);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityExoflame_ = $CapabilityExoflame$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/chemical/$CapabilityInfusion" {
import {$CapabilityInfusion$BlockEntityBuilder, $CapabilityInfusion$BlockEntityBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/chemical/$CapabilityInfusion$BlockEntityBuilder"
import {$CapabilityInfusion$ItemStackBuilder, $CapabilityInfusion$ItemStackBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/chemical/$CapabilityInfusion$ItemStackBuilder"

export class $CapabilityInfusion {

constructor()

public "blockEntity"(): $CapabilityInfusion$BlockEntityBuilder
public "itemStack"(): $CapabilityInfusion$ItemStackBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityInfusion$Type = ($CapabilityInfusion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityInfusion_ = $CapabilityInfusion$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/events/forge/$DynamicEntityEventJS" {
import {$DynamicAttachEventJS, $DynamicAttachEventJS$Type} from "packages/com/prunoideae/powerfuljs/events/forge/$DynamicAttachEventJS"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$CapabilityBuilderForge, $CapabilityBuilderForge$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityBuilderForge"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $DynamicEntityEventJS extends $DynamicAttachEventJS<($Entity)> {

constructor()

public "add"(predicate: $Predicate$Type<($Entity$Type)>, provider: $CapabilityBuilderForge$Type<($Entity$Type), (any)>): $DynamicAttachEventJS<($Entity)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicEntityEventJS$Type = ($DynamicEntityEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicEntityEventJS_ = $DynamicEntityEventJS$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityItem$BlockEntityBuilder" {
import {$CapabilityItem$IsItemValid, $CapabilityItem$IsItemValid$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityItem$IsItemValid"
import {$CapabilityItem$InsertItem, $CapabilityItem$InsertItem$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityItem$InsertItem"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$CapabilityBuilderForge, $CapabilityBuilderForge$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityBuilderForge"
import {$CapabilityItem$ExtractItem, $CapabilityItem$ExtractItem$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityItem$ExtractItem"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IItemHandler, $IItemHandler$Type} from "packages/net/minecraftforge/items/$IItemHandler"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$ToIntBiFunction, $ToIntBiFunction$Type} from "packages/java/util/function/$ToIntBiFunction"

export class $CapabilityItem$BlockEntityBuilder extends $CapabilityBuilderForge<($BlockEntity), ($IItemHandler)> {

constructor()

public "getSlots"(getSlots: $ToIntFunction$Type<($BlockEntity$Type)>): $CapabilityItem$BlockEntityBuilder
public "getStackInSlot"(getStackInSlot: $BiFunction$Type<($BlockEntity$Type), (integer), ($ItemStack$Type)>): $CapabilityItem$BlockEntityBuilder
public "insertItem"(insertItem: $CapabilityItem$InsertItem$Type): $CapabilityItem$BlockEntityBuilder
public "getSlotLimit"(getSlotLimit: $ToIntBiFunction$Type<($BlockEntity$Type), (integer)>): $CapabilityItem$BlockEntityBuilder
public "extractItem"(extractItem: $CapabilityItem$ExtractItem$Type): $CapabilityItem$BlockEntityBuilder
public "isItemValid"(isItemValid: $CapabilityItem$IsItemValid$Type): $CapabilityItem$BlockEntityBuilder
public "getCapability"(instance: $BlockEntity$Type): $IItemHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityItem$BlockEntityBuilder$Type = ($CapabilityItem$BlockEntityBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityItem$BlockEntityBuilder_ = $CapabilityItem$BlockEntityBuilder$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityItem$IsItemValid" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $CapabilityItem$IsItemValid {

 "isValid"(arg0: $BlockEntity$Type, arg1: integer, arg2: $ItemStack$Type): boolean

(arg0: $BlockEntity$Type, arg1: integer, arg2: $ItemStack$Type): boolean
}

export namespace $CapabilityItem$IsItemValid {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityItem$IsItemValid$Type = ($CapabilityItem$IsItemValid);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityItem$IsItemValid_ = $CapabilityItem$IsItemValid$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityForgeEnergy$ItemStackBuilder" {
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$IEnergyStorage, $IEnergyStorage$Type} from "packages/net/minecraftforge/energy/$IEnergyStorage"
import {$CapabilityBuilderForge, $CapabilityBuilderForge$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityBuilderForge"
import {$CapabilityForgeEnergy$EnergyIOItemStack, $CapabilityForgeEnergy$EnergyIOItemStack$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityForgeEnergy$EnergyIOItemStack"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $CapabilityForgeEnergy$ItemStackBuilder extends $CapabilityBuilderForge<($ItemStack), ($IEnergyStorage)> {

constructor()

public "getEnergyStored"(getEnergyStored: $ToIntFunction$Type<($ItemStack$Type)>): $CapabilityForgeEnergy$ItemStackBuilder
public "getMaxEnergyStored"(getMaxEnergyStored: $ToIntFunction$Type<($ItemStack$Type)>): $CapabilityForgeEnergy$ItemStackBuilder
public "canExtract"(canExtract: $Predicate$Type<($ItemStack$Type)>): $CapabilityForgeEnergy$ItemStackBuilder
public "receiveEnergy"(receiveEnergy: $CapabilityForgeEnergy$EnergyIOItemStack$Type): $CapabilityForgeEnergy$ItemStackBuilder
public "canReceive"(canReceive: $Predicate$Type<($ItemStack$Type)>): $CapabilityForgeEnergy$ItemStackBuilder
public "extractEnergy"(extractEnergy: $CapabilityForgeEnergy$EnergyIOItemStack$Type): $CapabilityForgeEnergy$ItemStackBuilder
public "withCapacity"(capacity: integer): $CapabilityForgeEnergy$ItemStackBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityForgeEnergy$ItemStackBuilder$Type = ($CapabilityForgeEnergy$ItemStackBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityForgeEnergy$ItemStackBuilder_ = $CapabilityForgeEnergy$ItemStackBuilder$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/events/forge/$DynamicAttachEventJS" {
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$CapabilityProvider, $CapabilityProvider$Type} from "packages/net/minecraftforge/common/capabilities/$CapabilityProvider"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$CapabilityBuilderForge, $CapabilityBuilderForge$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityBuilderForge"

export class $DynamicAttachEventJS<T extends $CapabilityProvider<(T)>> extends $EventJS {

constructor()

public "add"(arg0: $Predicate$Type<(T)>, arg1: $CapabilityBuilderForge$Type<(T), (any)>): $DynamicAttachEventJS<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicAttachEventJS$Type<T> = ($DynamicAttachEventJS<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicAttachEventJS_<T> = $DynamicAttachEventJS$Type<(T)>;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityForgeEnergy$EnergyIOItemStack" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $CapabilityForgeEnergy$EnergyIOItemStack {

 "transfer"(arg0: $ItemStack$Type, arg1: integer, arg2: boolean): integer

(arg0: $ItemStack$Type, arg1: integer, arg2: boolean): integer
}

export namespace $CapabilityForgeEnergy$EnergyIOItemStack {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityForgeEnergy$EnergyIOItemStack$Type = ($CapabilityForgeEnergy$EnergyIOItemStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityForgeEnergy$EnergyIOItemStack_ = $CapabilityForgeEnergy$EnergyIOItemStack$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/$CapabilityLaser" {
import {$CapabilityLaser$BlockEntityBuilder, $CapabilityLaser$BlockEntityBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/$CapabilityLaser$BlockEntityBuilder"

export class $CapabilityLaser {

constructor()

public "blockEntity"(): $CapabilityLaser$BlockEntityBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityLaser$Type = ($CapabilityLaser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityLaser_ = $CapabilityLaser$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/botania/$CapabilityRelic" {
import {$CapabilityRelic$CustomRelicBuilder, $CapabilityRelic$CustomRelicBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/botania/$CapabilityRelic$CustomRelicBuilder"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CapabilityRelic$NormalRelicBuilder, $CapabilityRelic$NormalRelicBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/botania/$CapabilityRelic$NormalRelicBuilder"

export class $CapabilityRelic {

constructor()

public "addRelicTooltipForItem"(stack: $ItemStack$Type, tooltip: $List$Type<($Component$Type)>): void
public "normalRelic"(): $CapabilityRelic$NormalRelicBuilder
public "normalRelic"(advancement: $ResourceLocation$Type): $CapabilityRelic$NormalRelicBuilder
public "customRelic"(): $CapabilityRelic$CustomRelicBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityRelic$Type = ($CapabilityRelic);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityRelic_ = $CapabilityRelic$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/$CapabilitiesMekanism" {
import {$CapabilityPigment, $CapabilityPigment$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/chemical/$CapabilityPigment"
import {$CapabilityLaser, $CapabilityLaser$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/$CapabilityLaser"
import {$CapabilityEvaporationTower, $CapabilityEvaporationTower$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/$CapabilityEvaporationTower"
import {$CapabilitySlurry, $CapabilitySlurry$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/chemical/$CapabilitySlurry"
import {$CapabilityConfigurable, $CapabilityConfigurable$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/$CapabilityConfigurable"
import {$CapabilityInfusion, $CapabilityInfusion$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/chemical/$CapabilityInfusion"
import {$CapabilityGas, $CapabilityGas$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/chemical/$CapabilityGas"
import {$CapabilityProtection, $CapabilityProtection$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/$CapabilityProtection"
import {$CapabilityAlloyInteractable, $CapabilityAlloyInteractable$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/$CapabilityAlloyInteractable"

export interface $CapabilitiesMekanism {

}

export namespace $CapabilitiesMekanism {
const GAS: $CapabilityGas
const INFUSION: $CapabilityInfusion
const PIGMENT: $CapabilityPigment
const SLURRY: $CapabilitySlurry
const PROTECTION: $CapabilityProtection
const ALLOY_INTERACTABLE: $CapabilityAlloyInteractable
const CONFIGURABLE: $CapabilityConfigurable
const EVAPORATION_TOWER: $CapabilityEvaporationTower
const LASER: $CapabilityLaser
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilitiesMekanism$Type = ($CapabilitiesMekanism);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilitiesMekanism_ = $CapabilitiesMekanism$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/proxy/$PowerfulJSCommon" {
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $PowerfulJSCommon {

constructor()

public "runOnClient"(run: $Runnable$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PowerfulJSCommon$Type = ($PowerfulJSCommon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PowerfulJSCommon_ = $PowerfulJSCommon$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/$PowerfulJS" {
import {$PowerfulJSCommon, $PowerfulJSCommon$Type} from "packages/com/prunoideae/powerfuljs/proxy/$PowerfulJSCommon"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $PowerfulJS {
static readonly "MOD_ID": string
static "PROXY": $Supplier<($PowerfulJSCommon)>

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PowerfulJS$Type = ($PowerfulJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PowerfulJS_ = $PowerfulJS$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityProvider" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ICapabilityProvider, $ICapabilityProvider$Type} from "packages/net/minecraftforge/common/capabilities/$ICapabilityProvider"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"

export class $CapabilityProvider implements $ICapabilityProvider {

constructor(capability: $Capability$Type<(any)>, instance: $LazyOptional$Type<(any)>, available: $Predicate$Type<($Direction$Type)>)

public static "of"(capability: any, instance: any, available: $Predicate$Type<($Direction$Type)>): $CapabilityProvider
public "getCapability"<C>(capability: $Capability$Type<(C)>, arg: $Direction$Type): $LazyOptional<(C)>
public "getCapability"<T>(arg0: $Capability$Type<(T)>): $LazyOptional<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityProvider$Type = ($CapabilityProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityProvider_ = $CapabilityProvider$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/forge/$PowerfulJSForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PowerfulJSForge {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PowerfulJSForge$Type = ($PowerfulJSForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PowerfulJSForge_ = $PowerfulJSForge$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/forge/$PowerfulJSPluginForge" {
import {$PowerfulJSPlugin, $PowerfulJSPlugin$Type} from "packages/com/prunoideae/powerfuljs/$PowerfulJSPlugin"
import {$BindingsEvent, $BindingsEvent$Type} from "packages/dev/latvian/mods/kubejs/script/$BindingsEvent"
import {$EventHandler, $EventHandler$Type} from "packages/dev/latvian/mods/kubejs/event/$EventHandler"
import {$EventGroup, $EventGroup$Type} from "packages/dev/latvian/mods/kubejs/event/$EventGroup"

export class $PowerfulJSPluginForge extends $PowerfulJSPlugin {
static readonly "DYNAMIC_ATTACH_ITEMSTACK_CAP": $EventHandler
static readonly "DYNAMIC_ATTACH_ENTITY_CAP": $EventHandler
static readonly "DYNAMIC_ATTACH_BLOCK_ENTITY_CAP": $EventHandler
static readonly "GROUP": $EventGroup
static readonly "REGISTER_BE_CAP": $EventHandler
static readonly "REGISTER_ENTITY_CAP": $EventHandler

constructor()

public "init"(): void
public "registerBindings"(event: $BindingsEvent$Type): void
public "afterInit"(): void
public "registerEvents"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PowerfulJSPluginForge$Type = ($PowerfulJSPluginForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PowerfulJSPluginForge_ = $PowerfulJSPluginForge$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityFluid$FluidIOItemStack" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$FluidStackJS, $FluidStackJS$Type} from "packages/dev/latvian/mods/kubejs/fluid/$FluidStackJS"

export interface $CapabilityFluid$FluidIOItemStack {

 "transfer"(arg0: $ItemStack$Type, arg1: $FluidStackJS$Type, arg2: boolean): integer

(arg0: $ItemStack$Type, arg1: $FluidStackJS$Type, arg2: boolean): integer
}

export namespace $CapabilityFluid$FluidIOItemStack {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityFluid$FluidIOItemStack$Type = ($CapabilityFluid$FluidIOItemStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityFluid$FluidIOItemStack_ = $CapabilityFluid$FluidIOItemStack$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/botania/$CapabilityAvatar" {
import {$CapabilityAvatar$AvatarBehaviorBuilder, $CapabilityAvatar$AvatarBehaviorBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/botania/$CapabilityAvatar$AvatarBehaviorBuilder"

export class $CapabilityAvatar {

constructor()

public "wieldable"(): $CapabilityAvatar$AvatarBehaviorBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityAvatar$Type = ($CapabilityAvatar);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityAvatar_ = $CapabilityAvatar$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityItem$ExtractItem" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $CapabilityItem$ExtractItem {

 "extract"(arg0: $BlockEntity$Type, arg1: integer, arg2: integer, arg3: boolean): $ItemStack

(arg0: $BlockEntity$Type, arg1: integer, arg2: integer, arg3: boolean): $ItemStack
}

export namespace $CapabilityItem$ExtractItem {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityItem$ExtractItem$Type = ($CapabilityItem$ExtractItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityItem$ExtractItem_ = $CapabilityItem$ExtractItem$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/curios/$CapabilityCurios$AttributeModificationContext" {
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $CapabilityCurios$AttributeModificationContext {

constructor(stack: $ItemStack$Type, context: $SlotContext$Type, uuid: $UUID$Type, modifiers: $Multimap$Type<($Attribute$Type), ($AttributeModifier$Type)>)

public "remove"(attribute: $Attribute$Type, identifier: string): $CapabilityCurios$AttributeModificationContext
public "getContext"(): $SlotContext
public "getStack"(): $ItemStack
public "modify"(attribute: $Attribute$Type, identifier: string, amount: double, operation: $AttributeModifier$Operation$Type): $CapabilityCurios$AttributeModificationContext
public "getUUID"(): $UUID
get "context"(): $SlotContext
get "stack"(): $ItemStack
get "uUID"(): $UUID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityCurios$AttributeModificationContext$Type = ($CapabilityCurios$AttributeModificationContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityCurios$AttributeModificationContext_ = $CapabilityCurios$AttributeModificationContext$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityForgeEnergy$BlockEntityBuilder" {
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$IEnergyStorage, $IEnergyStorage$Type} from "packages/net/minecraftforge/energy/$IEnergyStorage"
import {$CapabilityBuilderForge, $CapabilityBuilderForge$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityBuilderForge"
import {$CapabilityForgeEnergy$EnergyIOBlockEntity, $CapabilityForgeEnergy$EnergyIOBlockEntity$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityForgeEnergy$EnergyIOBlockEntity"

export class $CapabilityForgeEnergy$BlockEntityBuilder extends $CapabilityBuilderForge<($BlockEntity), ($IEnergyStorage)> {

constructor()

public "getEnergyStored"(getEnergyStored: $ToIntFunction$Type<($BlockEntity$Type)>): $CapabilityForgeEnergy$BlockEntityBuilder
public "getMaxEnergyStored"(getMaxEnergyStored: $ToIntFunction$Type<($BlockEntity$Type)>): $CapabilityForgeEnergy$BlockEntityBuilder
public "canExtract"(canExtract: $Predicate$Type<($BlockEntity$Type)>): $CapabilityForgeEnergy$BlockEntityBuilder
public "receiveEnergy"(receiveEnergy: $CapabilityForgeEnergy$EnergyIOBlockEntity$Type): $CapabilityForgeEnergy$BlockEntityBuilder
public "canReceive"(canReceive: $Predicate$Type<($BlockEntity$Type)>): $CapabilityForgeEnergy$BlockEntityBuilder
public "extractEnergy"(extractEnergy: $CapabilityForgeEnergy$EnergyIOBlockEntity$Type): $CapabilityForgeEnergy$BlockEntityBuilder
public "withCapacity"(capacity: integer): $CapabilityForgeEnergy$BlockEntityBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityForgeEnergy$BlockEntityBuilder$Type = ($CapabilityForgeEnergy$BlockEntityBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityForgeEnergy$BlockEntityBuilder_ = $CapabilityForgeEnergy$BlockEntityBuilder$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/forge/$PowerfulJSEvents" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$AttachCapabilitiesEvent, $AttachCapabilitiesEvent$Type} from "packages/net/minecraftforge/event/$AttachCapabilitiesEvent"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $PowerfulJSEvents {

constructor()

public static "attachBECapabilities"(event: $AttachCapabilitiesEvent$Type<($BlockEntity$Type)>): void
public static "attachItemCapabilities"(event: $AttachCapabilitiesEvent$Type<($ItemStack$Type)>): void
public static "attachEntityCapabilities"(event: $AttachCapabilitiesEvent$Type<($Entity$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PowerfulJSEvents$Type = ($PowerfulJSEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PowerfulJSEvents_ = $PowerfulJSEvents$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/$CapabilityService" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$BlockEntityInfo, $BlockEntityInfo$Type} from "packages/dev/latvian/mods/kubejs/block/entity/$BlockEntityInfo"
import {$CapabilityBuilder, $CapabilityBuilder$Type} from "packages/com/prunoideae/powerfuljs/$CapabilityBuilder"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ItemBuilder, $ItemBuilder$Type} from "packages/dev/latvian/mods/kubejs/item/$ItemBuilder"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $CapabilityService {
static readonly "INSTANCE": $CapabilityService

constructor()

public "getCapabilitiesFor"(entity: $Entity$Type): $Optional<($List<($CapabilityBuilder<($Entity), (any), (any)>)>)>
public "getCapabilitiesFor"(itemStack: $ItemStack$Type): $Optional<($List<($CapabilityBuilder<($ItemStack), (any), (any)>)>)>
public "getCapabilitiesFor"(blockEntity: $BlockEntity$Type): $Optional<($List<($CapabilityBuilder<($BlockEntity), (any), (any)>)>)>
public "loadBuilders"(): void
public "addBECapability"(type: $BlockEntityType$Type<(any)>, capabilityBuilder: $CapabilityBuilder$Type<($BlockEntity$Type), (any), (any)>): void
public "addItemCapability"(item: $Item$Type, capabilityBuilder: $CapabilityBuilder$Type<($ItemStack$Type), (any), (any)>): void
public "addLazyBECapability"(info: $BlockEntityInfo$Type, capabilityBuilder: $CapabilityBuilder$Type<($BlockEntity$Type), (any), (any)>): void
public "resolveLazyBECapabilities"(): void
public "addEntityCapability"(type: $EntityType$Type<(any)>, capabilityBuilder: $CapabilityBuilder$Type<($Entity$Type), (any), (any)>): void
public "addBuilderCapability"(builder: $ItemBuilder$Type, capabilityBuilder: $CapabilityBuilder$Type<($ItemStack$Type), (any), (any)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityService$Type = ($CapabilityService);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityService_ = $CapabilityService$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityFluid$CustomTankBuilderBlockEntity" {
import {$IFluidHandler, $IFluidHandler$Type} from "packages/net/minecraftforge/fluids/capability/$IFluidHandler"
import {$CapabilityFluid$FluidIOBlockEntity, $CapabilityFluid$FluidIOBlockEntity$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityFluid$FluidIOBlockEntity"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$CapabilityBuilderForge, $CapabilityBuilderForge$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityBuilderForge"
import {$FluidStackJS, $FluidStackJS$Type} from "packages/dev/latvian/mods/kubejs/fluid/$FluidStackJS"

export class $CapabilityFluid$CustomTankBuilderBlockEntity extends $CapabilityBuilderForge<($BlockEntity), ($IFluidHandler)> {

constructor()

public "getCapacity"(getCapacity: $ToIntFunction$Type<($BlockEntity$Type)>): $CapabilityFluid$CustomTankBuilderBlockEntity
public "getCapability"(instance: $BlockEntity$Type): $IFluidHandler
public "onFill"(onFill: $CapabilityFluid$FluidIOBlockEntity$Type): $CapabilityFluid$CustomTankBuilderBlockEntity
public "getFluid"(getFluid: $Function$Type<($BlockEntity$Type), ($FluidStackJS$Type)>): $CapabilityFluid$CustomTankBuilderBlockEntity
public "isFluidGood"(isFluidGood: $BiPredicate$Type<($BlockEntity$Type), ($FluidStackJS$Type)>): $CapabilityFluid$CustomTankBuilderBlockEntity
public "onDrain"(onDrain: $CapabilityFluid$FluidIOBlockEntity$Type): $CapabilityFluid$CustomTankBuilderBlockEntity
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityFluid$CustomTankBuilderBlockEntity$Type = ($CapabilityFluid$CustomTankBuilderBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityFluid$CustomTankBuilderBlockEntity_ = $CapabilityFluid$CustomTankBuilderBlockEntity$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityForgeEnergy" {
import {$CapabilityForgeEnergy$BlockEntityBuilder, $CapabilityForgeEnergy$BlockEntityBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityForgeEnergy$BlockEntityBuilder"
import {$CapabilityForgeEnergy$ItemStorageBuilder, $CapabilityForgeEnergy$ItemStorageBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityForgeEnergy$ItemStorageBuilder"
import {$CapabilityForgeEnergy$ItemStackBuilder, $CapabilityForgeEnergy$ItemStackBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityForgeEnergy$ItemStackBuilder"

export class $CapabilityForgeEnergy {

constructor()

public "customBlockEntity"(): $CapabilityForgeEnergy$BlockEntityBuilder
public "customItemStack"(): $CapabilityForgeEnergy$ItemStackBuilder
public "normalItemStack"(capacity: integer, canExtract: boolean, canReceive: boolean): $CapabilityForgeEnergy$ItemStorageBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityForgeEnergy$Type = ($CapabilityForgeEnergy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityForgeEnergy_ = $CapabilityForgeEnergy$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/curios/$RegisterCuriosRendererEventJS" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$RendererCurios$RenderContext, $RendererCurios$RenderContext$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/curios/$RendererCurios$RenderContext"

export class $RegisterCuriosRendererEventJS extends $EventJS {

constructor()

public "register"(item: $Item$Type, renderer: $Consumer$Type<($RendererCurios$RenderContext$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegisterCuriosRendererEventJS$Type = ($RegisterCuriosRendererEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegisterCuriosRendererEventJS_ = $RegisterCuriosRendererEventJS$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/curios/$RendererCurios$RenderContext" {
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$RenderLayerParent, $RenderLayerParent$Type} from "packages/net/minecraft/client/renderer/entity/$RenderLayerParent"
import {$EntityModel, $EntityModel$Type} from "packages/net/minecraft/client/model/$EntityModel"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $RendererCurios$RenderContext {
readonly "stack": $ItemStack
readonly "slotContext": $SlotContext
readonly "matrixStack": $PoseStack
readonly "renderLayerParent": $RenderLayerParent<($LivingEntity), ($EntityModel<($LivingEntity)>)>
readonly "renderTypeBuffer": $MultiBufferSource
readonly "light": integer
readonly "limbSwing": float
readonly "limbSwingAmount": float
readonly "partialTicks": float
readonly "ageInTicks": float
readonly "netHeadYaw": float
readonly "netHeadPitch": float

constructor(stack: $ItemStack$Type, slotContext: $SlotContext$Type, matrixStack: $PoseStack$Type, renderLayerParent: $RenderLayerParent$Type<($LivingEntity$Type), ($EntityModel$Type<($LivingEntity$Type)>)>, renderTypeBuffer: $MultiBufferSource$Type, light: integer, limbSwing: float, limbSwingAmount: float, partialTicks: float, ageInTicks: float, netHeadYaw: float, netHeadPitch: float)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RendererCurios$RenderContext$Type = ($RendererCurios$RenderContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RendererCurios$RenderContext_ = $RendererCurios$RenderContext$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityFluid$NormalTankBuilderItemStack" {
import {$IFluidHandlerItem, $IFluidHandlerItem$Type} from "packages/net/minecraftforge/fluids/capability/$IFluidHandlerItem"
import {$CapabilityBuilderForge, $CapabilityBuilderForge$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityBuilderForge"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $CapabilityFluid$NormalTankBuilderItemStack extends $CapabilityBuilderForge<($ItemStack), ($IFluidHandlerItem)> {


public "getCapability"(instance: $ItemStack$Type): $IFluidHandlerItem
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityFluid$NormalTankBuilderItemStack$Type = ($CapabilityFluid$NormalTankBuilderItemStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityFluid$NormalTankBuilderItemStack_ = $CapabilityFluid$NormalTankBuilderItemStack$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/botania/$CapabilitiesBotania" {
import {$CapabilityAvatar, $CapabilityAvatar$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/botania/$CapabilityAvatar"
import {$CapabilityBlockProvider, $CapabilityBlockProvider$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/botania/$CapabilityBlockProvider"
import {$CapabilityRelic, $CapabilityRelic$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/botania/$CapabilityRelic"
import {$CapabilityExoflame, $CapabilityExoflame$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/botania/$CapabilityExoflame"
import {$CapabilityMana, $CapabilityMana$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/botania/$CapabilityMana"

export interface $CapabilitiesBotania {

}

export namespace $CapabilitiesBotania {
const MANA: $CapabilityMana
const AVATAR: $CapabilityAvatar
const BLOCK_PROVIDER: $CapabilityBlockProvider
const RELIC: $CapabilityRelic
const EXOFLAME: $CapabilityExoflame
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilitiesBotania$Type = ($CapabilitiesBotania);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilitiesBotania_ = $CapabilitiesBotania$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/events/$EntityCapEventJS" {
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$CapabilityBuilder, $CapabilityBuilder$Type} from "packages/com/prunoideae/powerfuljs/$CapabilityBuilder"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EntityCapEventJS extends $EventJS {

constructor()

public "attach"(entityType: $EntityType$Type<(any)>, capabilityBuilder: $CapabilityBuilder$Type<($Entity$Type), (any), (any)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityCapEventJS$Type = ($EntityCapEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityCapEventJS_ = $EntityCapEventJS$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/$CapabilityAlloyInteractable" {
import {$CapabilityAlloyInteractable$BlockEntityBuilder, $CapabilityAlloyInteractable$BlockEntityBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/$CapabilityAlloyInteractable$BlockEntityBuilder"

export class $CapabilityAlloyInteractable {

constructor()

public "blockEntity"(): $CapabilityAlloyInteractable$BlockEntityBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityAlloyInteractable$Type = ($CapabilityAlloyInteractable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityAlloyInteractable_ = $CapabilityAlloyInteractable$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/$CapabilityConfigurable" {
import {$CapabilityConfigurable$BlockEntityBuilder, $CapabilityConfigurable$BlockEntityBuilder$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/mekanism/$CapabilityConfigurable$BlockEntityBuilder"

export class $CapabilityConfigurable {

constructor()

public "blockEntity"(): $CapabilityConfigurable$BlockEntityBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityConfigurable$Type = ($CapabilityConfigurable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityConfigurable_ = $CapabilityConfigurable$Type;
}}
declare module "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilityFluid$FluidIOBlockEntity" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$FluidStackJS, $FluidStackJS$Type} from "packages/dev/latvian/mods/kubejs/fluid/$FluidStackJS"

export interface $CapabilityFluid$FluidIOBlockEntity {

 "transfer"(arg0: $BlockEntity$Type, arg1: $FluidStackJS$Type, arg2: boolean): integer

(arg0: $BlockEntity$Type, arg1: $FluidStackJS$Type, arg2: boolean): integer
}

export namespace $CapabilityFluid$FluidIOBlockEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityFluid$FluidIOBlockEntity$Type = ($CapabilityFluid$FluidIOBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityFluid$FluidIOBlockEntity_ = $CapabilityFluid$FluidIOBlockEntity$Type;
}}
