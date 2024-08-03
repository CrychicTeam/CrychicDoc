declare module "packages/top/theillusivec4/curios/common/$CuriosHelper$SlotAttributeWrapper" {
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$SlotAttribute, $SlotAttribute$Type} from "packages/top/theillusivec4/curios/api/$SlotAttribute"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

/**
 * 
 * @deprecated
 */
export class $CuriosHelper$SlotAttributeWrapper extends $SlotAttribute {
readonly "identifier": string
static readonly "MAX_NAME_LENGTH": integer

constructor(arg0: string)

public static "isNullOrAddition"(arg0: $AttributeModifier$Operation$Type): boolean
public static "toValueComponent"(arg0: $Attribute$Type, arg1: $AttributeModifier$Operation$Type, arg2: double, arg3: $TooltipFlag$Type): $MutableComponent
public static "toComponent"(arg0: $Attribute$Type, arg1: $AttributeModifier$Type, arg2: $TooltipFlag$Type): $MutableComponent
public static "toBaseComponent"(arg0: $Attribute$Type, arg1: double, arg2: double, arg3: boolean, arg4: $TooltipFlag$Type): $MutableComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosHelper$SlotAttributeWrapper$Type = ($CuriosHelper$SlotAttributeWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosHelper$SlotAttributeWrapper_ = $CuriosHelper$SlotAttributeWrapper$Type;
}}
declare module "packages/top/theillusivec4/curios/api/event/$CurioAttributeModifierEvent" {
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $CurioAttributeModifierEvent extends $Event {

constructor()
constructor(arg0: $ItemStack$Type, arg1: $SlotContext$Type, arg2: $UUID$Type, arg3: $Multimap$Type<($Attribute$Type), ($AttributeModifier$Type)>)

public "getModifiers"(): $Multimap<($Attribute), ($AttributeModifier)>
public "isCancelable"(): boolean
public "clearModifiers"(): void
public "removeModifier"(arg0: $Attribute$Type, arg1: $AttributeModifier$Type): boolean
public "removeAttribute"(arg0: $Attribute$Type): $Collection<($AttributeModifier)>
public "getSlotContext"(): $SlotContext
public "getItemStack"(): $ItemStack
public "getOriginalModifiers"(): $Multimap<($Attribute), ($AttributeModifier)>
public "getUuid"(): $UUID
public "addModifier"(arg0: $Attribute$Type, arg1: $AttributeModifier$Type): boolean
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "modifiers"(): $Multimap<($Attribute), ($AttributeModifier)>
get "cancelable"(): boolean
get "slotContext"(): $SlotContext
get "itemStack"(): $ItemStack
get "originalModifiers"(): $Multimap<($Attribute), ($AttributeModifier)>
get "uuid"(): $UUID
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CurioAttributeModifierEvent$Type = ($CurioAttributeModifierEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CurioAttributeModifierEvent_ = $CurioAttributeModifierEvent$Type;
}}
declare module "packages/top/theillusivec4/curios/api/type/$ICuriosMenu" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ICuriosMenu {

 "resetSlots"(): void

(): void
}

export namespace $ICuriosMenu {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICuriosMenu$Type = ($ICuriosMenu);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICuriosMenu_ = $ICuriosMenu$Type;
}}
declare module "packages/top/theillusivec4/curios/mixin/core/$AccessorEntity" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $AccessorEntity {

 "getFirstTick"(): boolean

(): boolean
}

export namespace $AccessorEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessorEntity$Type = ($AccessorEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessorEntity_ = $AccessorEntity$Type;
}}
declare module "packages/top/theillusivec4/curios/client/$IconHelper" {
import {$IIconHelper, $IIconHelper$Type} from "packages/top/theillusivec4/curios/api/type/util/$IIconHelper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $IconHelper implements $IIconHelper {

constructor()

public "getIcon"(arg0: string): $ResourceLocation
public "addIcon"(arg0: string, arg1: $ResourceLocation$Type): void
public "clearIcons"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IconHelper$Type = ($IconHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IconHelper_ = $IconHelper$Type;
}}
declare module "packages/top/theillusivec4/caelus/mixin/util/$ClientMixinHooks" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export class $ClientMixinHooks {

constructor()

public static "checkFlight"(): boolean
public static "canRenderCape"(arg0: $Player$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientMixinHooks$Type = ($ClientMixinHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientMixinHooks_ = $ClientMixinHooks$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/client/$CPacketOpenCurios" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $CPacketOpenCurios {

constructor(arg0: $ItemStack$Type)

public static "decode"(arg0: $FriendlyByteBuf$Type): $CPacketOpenCurios
public static "encode"(arg0: $CPacketOpenCurios$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $CPacketOpenCurios$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CPacketOpenCurios$Type = ($CPacketOpenCurios);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CPacketOpenCurios_ = $CPacketOpenCurios$Type;
}}
declare module "packages/top/theillusivec4/curios/api/client/$ICurioRenderer" {
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$HumanoidModel, $HumanoidModel$Type} from "packages/net/minecraft/client/model/$HumanoidModel"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$RenderLayerParent, $RenderLayerParent$Type} from "packages/net/minecraft/client/renderer/entity/$RenderLayerParent"
import {$EntityModel, $EntityModel$Type} from "packages/net/minecraft/client/model/$EntityModel"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $ICurioRenderer {

 "render"<T extends $LivingEntity, M extends $EntityModel<(T)>>(arg0: $ItemStack$Type, arg1: $SlotContext$Type, arg2: $PoseStack$Type, arg3: $RenderLayerParent$Type<(T), (M)>, arg4: $MultiBufferSource$Type, arg5: integer, arg6: float, arg7: float, arg8: float, arg9: float, arg10: float, arg11: float): void

(arg0: $LivingEntity$Type, ...arg1: ($ModelPart$Type)[]): void
}

export namespace $ICurioRenderer {
function followHeadRotations(arg0: $LivingEntity$Type, ...arg1: ($ModelPart$Type)[]): void
function translateIfSneaking(arg0: $PoseStack$Type, arg1: $LivingEntity$Type): void
function followBodyRotations(arg0: $LivingEntity$Type, ...arg1: ($HumanoidModel$Type<($LivingEntity$Type)>)[]): void
function rotateIfSneaking(arg0: $PoseStack$Type, arg1: $LivingEntity$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICurioRenderer$Type = ($ICurioRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICurioRenderer_ = $ICurioRenderer$Type;
}}
declare module "packages/top/theillusivec4/curios/common/capability/$CurioItemCapability" {
import {$ICurio, $ICurio$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio"
import {$ICapabilityProvider, $ICapabilityProvider$Type} from "packages/net/minecraftforge/common/capabilities/$ICapabilityProvider"

export class $CurioItemCapability {

constructor()

public static "createProvider"(arg0: $ICurio$Type): $ICapabilityProvider
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CurioItemCapability$Type = ($CurioItemCapability);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CurioItemCapability_ = $CurioItemCapability$Type;
}}
declare module "packages/top/theillusivec4/curios/common/integration/jei/$CuriosContainerV2Handler" {
import {$IClickableIngredient, $IClickableIngredient$Type} from "packages/mezz/jei/api/runtime/$IClickableIngredient"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$CuriosScreenV2, $CuriosScreenV2$Type} from "packages/top/theillusivec4/curios/client/gui/$CuriosScreenV2"
import {$IGuiClickableArea, $IGuiClickableArea$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiClickableArea"
import {$IGuiContainerHandler, $IGuiContainerHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiContainerHandler"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"

export class $CuriosContainerV2Handler implements $IGuiContainerHandler<($CuriosScreenV2)> {

constructor()

public "getGuiExtraAreas"(arg0: $CuriosScreenV2$Type): $List<($Rect2i)>
public "getGuiClickableAreas"(arg0: $CuriosScreenV2$Type, arg1: double, arg2: double): $Collection<($IGuiClickableArea)>
public "getClickableIngredientUnderMouse"(arg0: $CuriosScreenV2$Type, arg1: double, arg2: double): $Optional<($IClickableIngredient<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosContainerV2Handler$Type = ($CuriosContainerV2Handler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosContainerV2Handler_ = $CuriosContainerV2Handler$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/server/$SPacketQuickMove" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $SPacketQuickMove {
readonly "windowId": integer
readonly "moveIndex": integer

constructor(arg0: integer, arg1: integer)

public static "decode"(arg0: $FriendlyByteBuf$Type): $SPacketQuickMove
public static "encode"(arg0: $SPacketQuickMove$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $SPacketQuickMove$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SPacketQuickMove$Type = ($SPacketQuickMove);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SPacketQuickMove_ = $SPacketQuickMove$Type;
}}
declare module "packages/top/theillusivec4/curios/common/slottype/$SlotType" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ICurio$DropRule, $ICurio$DropRule$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio$DropRule"
import {$ISlotType, $ISlotType$Type} from "packages/top/theillusivec4/curios/api/type/$ISlotType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $SlotType implements $ISlotType {


public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "compareTo"(arg0: $ISlotType$Type): integer
public static "from"(arg0: $CompoundTag$Type): $ISlotType
public "getSize"(): integer
public "getIdentifier"(): string
public "getValidators"(): $Set<($ResourceLocation)>
public "writeNbt"(): $CompoundTag
public "useNativeGui"(): boolean
public "getOrder"(): integer
public "canToggleRendering"(): boolean
public "hasCosmetic"(): boolean
public "getIcon"(): $ResourceLocation
public "getDropRule"(): $ICurio$DropRule
/**
 * 
 * @deprecated
 */
public "getPriority"(): integer
/**
 * 
 * @deprecated
 */
public "isLocked"(): boolean
/**
 * 
 * @deprecated
 */
public "isVisible"(): boolean
get "size"(): integer
get "identifier"(): string
get "validators"(): $Set<($ResourceLocation)>
get "order"(): integer
get "icon"(): $ResourceLocation
get "dropRule"(): $ICurio$DropRule
get "priority"(): integer
get "locked"(): boolean
get "visible"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotType$Type = ($SlotType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotType_ = $SlotType$Type;
}}
declare module "packages/top/theillusivec4/curios/api/client/$ICuriosScreen" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ICuriosScreen {

}

export namespace $ICuriosScreen {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICuriosScreen$Type = ($ICuriosScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICuriosScreen_ = $ICuriosScreen$Type;
}}
declare module "packages/top/theillusivec4/curios/client/gui/$CosmeticButton" {
import {$ImageButton, $ImageButton$Type} from "packages/net/minecraft/client/gui/components/$ImageButton"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $CosmeticButton extends $ImageButton {
readonly "resourceLocation": $ResourceLocation
static readonly "SMALL_WIDTH": integer
static readonly "DEFAULT_WIDTH": integer
static readonly "DEFAULT_HEIGHT": integer
 "onPress": $Button$OnPress
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer


public "renderWidget"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CosmeticButton$Type = ($CosmeticButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CosmeticButton_ = $CosmeticButton$Type;
}}
declare module "packages/top/theillusivec4/curios/server/command/$CurioArgumentType" {
import {$Suggestions, $Suggestions$Type} from "packages/com/mojang/brigadier/suggestion/$Suggestions"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$StringReader, $StringReader$Type} from "packages/com/mojang/brigadier/$StringReader"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$SuggestionsBuilder, $SuggestionsBuilder$Type} from "packages/com/mojang/brigadier/suggestion/$SuggestionsBuilder"
import {$ArgumentType, $ArgumentType$Type} from "packages/com/mojang/brigadier/arguments/$ArgumentType"
import {$CommandContext, $CommandContext$Type} from "packages/com/mojang/brigadier/context/$CommandContext"

export class $CurioArgumentType implements $ArgumentType<(string)> {
static "slotIds": $Set<(string)>

constructor()

public static "slot"(): $CurioArgumentType
public static "getSlot"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>, arg1: string): string
public "parse"(arg0: $StringReader$Type): string
public "getExamples"(): $Collection<(string)>
public "listSuggestions"<S>(arg0: $CommandContext$Type<(S)>, arg1: $SuggestionsBuilder$Type): $CompletableFuture<($Suggestions)>
get "examples"(): $Collection<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CurioArgumentType$Type = ($CurioArgumentType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CurioArgumentType_ = $CurioArgumentType$Type;
}}
declare module "packages/top/theillusivec4/curios/api/$SlotAttribute" {
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export class $SlotAttribute extends $Attribute {
static readonly "MAX_NAME_LENGTH": integer


public static "getOrCreate"(arg0: string): $SlotAttribute
public "getIdentifier"(): string
public static "isNullOrAddition"(arg0: $AttributeModifier$Operation$Type): boolean
public static "toValueComponent"(arg0: $Attribute$Type, arg1: $AttributeModifier$Operation$Type, arg2: double, arg3: $TooltipFlag$Type): $MutableComponent
public static "toComponent"(arg0: $Attribute$Type, arg1: $AttributeModifier$Type, arg2: $TooltipFlag$Type): $MutableComponent
public static "toBaseComponent"(arg0: $Attribute$Type, arg1: double, arg2: double, arg3: boolean, arg4: $TooltipFlag$Type): $MutableComponent
get "identifier"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotAttribute$Type = ($SlotAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotAttribute_ = $SlotAttribute$Type;
}}
declare module "packages/top/theillusivec4/curios/api/$CuriosDataProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$DataProvider, $DataProvider$Type} from "packages/net/minecraft/data/$DataProvider"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$IEntitiesData, $IEntitiesData$Type} from "packages/top/theillusivec4/curios/api/type/data/$IEntitiesData"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ISlotData, $ISlotData$Type} from "packages/top/theillusivec4/curios/api/type/data/$ISlotData"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"

export class $CuriosDataProvider implements $DataProvider {

constructor(arg0: string, arg1: $PackOutput$Type, arg2: $ExistingFileHelper$Type, arg3: $CompletableFuture$Type<($HolderLookup$Provider$Type)>)

public "generate"(arg0: $HolderLookup$Provider$Type, arg1: $ExistingFileHelper$Type): void
public "createSlot"(arg0: string): $ISlotData
public "createEntities"(arg0: string): $IEntitiesData
public "copyEntities"(arg0: string, arg1: string): $IEntitiesData
public "copySlot"(arg0: string, arg1: string): $ISlotData
public "run"(arg0: $CachedOutput$Type): $CompletableFuture<(any)>
public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosDataProvider$Type = ($CuriosDataProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosDataProvider_ = $CuriosDataProvider$Type;
}}
declare module "packages/top/theillusivec4/curios/common/inventory/$CurioSlot" {
import {$IDynamicStackHandler, $IDynamicStackHandler$Type} from "packages/top/theillusivec4/curios/api/type/inventory/$IDynamicStackHandler"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$SlotItemHandler, $SlotItemHandler$Type} from "packages/net/minecraftforge/items/$SlotItemHandler"

export class $CurioSlot extends $SlotItemHandler {
readonly "container": $Container
 "index": integer
 "x": integer
 "y": integer

constructor(arg0: $Player$Type, arg1: $IDynamicStackHandler$Type, arg2: integer, arg3: string, arg4: integer, arg5: integer, arg6: $NonNullList$Type<(boolean)>, arg7: boolean, arg8: boolean, arg9: boolean)
constructor(arg0: $Player$Type, arg1: $IDynamicStackHandler$Type, arg2: integer, arg3: string, arg4: integer, arg5: integer, arg6: $NonNullList$Type<(boolean)>, arg7: boolean)

public "getIdentifier"(): string
public "showCosmeticToggle"(): boolean
public "getRenderStatus"(): boolean
public "isCosmetic"(): boolean
public "canToggleRender"(): boolean
public "getSlotName"(): string
public "allowModification"(arg0: $Player$Type): boolean
public "set"(arg0: $ItemStack$Type): void
get "identifier"(): string
get "renderStatus"(): boolean
get "cosmetic"(): boolean
get "slotName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CurioSlot$Type = ($CurioSlot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CurioSlot_ = $CurioSlot$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/server/$SPacketPage" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $SPacketPage {

constructor(arg0: integer, arg1: integer)

public static "decode"(arg0: $FriendlyByteBuf$Type): $SPacketPage
public static "encode"(arg0: $SPacketPage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $SPacketPage$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SPacketPage$Type = ($SPacketPage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SPacketPage_ = $SPacketPage$Type;
}}
declare module "packages/top/theillusivec4/curios/api/$SlotTypeMessage" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

/**
 * 
 * @deprecated
 */
export class $SlotTypeMessage {
static readonly "REGISTER_TYPE": string
static readonly "MODIFY_TYPE": string


/**
 * 
 * @deprecated
 */
public "getPriority"(): integer
/**
 * 
 * @deprecated
 */
public "getSize"(): integer
/**
 * 
 * @deprecated
 */
public "isLocked"(): boolean
/**
 * 
 * @deprecated
 */
public "getIdentifier"(): string
/**
 * 
 * @deprecated
 */
public "isVisible"(): boolean
/**
 * 
 * @deprecated
 */
public "hasCosmetic"(): boolean
/**
 * 
 * @deprecated
 */
public "getIcon"(): $ResourceLocation
get "priority"(): integer
get "size"(): integer
get "locked"(): boolean
get "identifier"(): string
get "visible"(): boolean
get "icon"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotTypeMessage$Type = ($SlotTypeMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotTypeMessage_ = $SlotTypeMessage$Type;
}}
declare module "packages/top/theillusivec4/curios/common/$CuriosRegistry" {
import {$ArgumentTypeInfo, $ArgumentTypeInfo$Type} from "packages/net/minecraft/commands/synchronization/$ArgumentTypeInfo"
import {$CuriosContainer, $CuriosContainer$Type} from "packages/top/theillusivec4/curios/common/inventory/container/$CuriosContainer"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$CuriosContainerV2, $CuriosContainerV2$Type} from "packages/top/theillusivec4/curios/common/inventory/container/$CuriosContainerV2"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $CuriosRegistry {
static readonly "CURIO_SLOT_ARGUMENT": $RegistryObject<($ArgumentTypeInfo<(any), (any)>)>
static readonly "CURIO_MENU": $RegistryObject<($MenuType<($CuriosContainer)>)>
static readonly "CURIO_MENU_NEW": $Supplier<($MenuType<($CuriosContainerV2)>)>

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosRegistry$Type = ($CuriosRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosRegistry_ = $CuriosRegistry$Type;
}}
declare module "packages/top/theillusivec4/curios/common/util/$SetCurioAttributesFunction" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$LootItemFunctionType, $LootItemFunctionType$Type} from "packages/net/minecraft/world/level/storage/loot/functions/$LootItemFunctionType"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$LootItemConditionalFunction, $LootItemConditionalFunction$Type} from "packages/net/minecraft/world/level/storage/loot/functions/$LootItemConditionalFunction"

export class $SetCurioAttributesFunction extends $LootItemConditionalFunction {
static "TYPE": $LootItemFunctionType


public static "register"(): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
public "getType"(): $LootItemFunctionType
public "m_7372_"(arg0: $ItemStack$Type, arg1: $LootContext$Type): $ItemStack
public static "decorate"(arg0: $BiFunction$Type<($ItemStack$Type), ($LootContext$Type), ($ItemStack$Type)>, arg1: $Consumer$Type<($ItemStack$Type)>, arg2: $LootContext$Type): $Consumer<($ItemStack)>
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
get "type"(): $LootItemFunctionType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SetCurioAttributesFunction$Type = ($SetCurioAttributesFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SetCurioAttributesFunction_ = $SetCurioAttributesFunction$Type;
}}
declare module "packages/top/theillusivec4/curios/client/$KeyRegistry" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"

export class $KeyRegistry {
static "openCurios": $KeyMapping

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyRegistry$Type = ($KeyRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyRegistry_ = $KeyRegistry$Type;
}}
declare module "packages/top/theillusivec4/curios/common/event/$CuriosEventHandler" {
import {$PlayerEvent$Clone, $PlayerEvent$Clone$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$Clone"
import {$BlockEvent$BreakEvent, $BlockEvent$BreakEvent$Type} from "packages/net/minecraftforge/event/level/$BlockEvent$BreakEvent"
import {$PlayerEvent$PlayerLoggedInEvent, $PlayerEvent$PlayerLoggedInEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerLoggedInEvent"
import {$PlayerXpEvent$PickupXp, $PlayerXpEvent$PickupXp$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerXpEvent$PickupXp"
import {$LivingEquipmentChangeEvent, $LivingEquipmentChangeEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingEquipmentChangeEvent"
import {$LivingDropsEvent, $LivingDropsEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingDropsEvent"
import {$AttachCapabilitiesEvent, $AttachCapabilitiesEvent$Type} from "packages/net/minecraftforge/event/$AttachCapabilitiesEvent"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$EnderManAngerEvent, $EnderManAngerEvent$Type} from "packages/net/minecraftforge/event/entity/living/$EnderManAngerEvent"
import {$OnDatapackSyncEvent, $OnDatapackSyncEvent$Type} from "packages/net/minecraftforge/event/$OnDatapackSyncEvent"
import {$PlayerEvent$StartTracking, $PlayerEvent$StartTracking$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$StartTracking"
import {$EntityJoinLevelEvent, $EntityJoinLevelEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityJoinLevelEvent"
import {$TickEvent$LevelTickEvent, $TickEvent$LevelTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$LevelTickEvent"
import {$PlayerInteractEvent$RightClickItem, $PlayerInteractEvent$RightClickItem$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$RightClickItem"
import {$LivingEvent$LivingTickEvent, $LivingEvent$LivingTickEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingEvent$LivingTickEvent"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$LootingLevelEvent, $LootingLevelEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LootingLevelEvent"

export class $CuriosEventHandler {
static "dirtyTags": boolean

constructor()

public "tick"(arg0: $LivingEvent$LivingTickEvent$Type): void
public "playerXPPickUp"(arg0: $PlayerXpEvent$PickupXp$Type): void
public "entityJoinWorld"(arg0: $EntityJoinLevelEvent$Type): void
public "curioRightClick"(arg0: $PlayerInteractEvent$RightClickItem$Type): void
public "enderManAnger"(arg0: $EnderManAngerEvent$Type): void
public "onDatapackSync"(arg0: $OnDatapackSyncEvent$Type): void
public "worldTick"(arg0: $TickEvent$LevelTickEvent$Type): void
public "playerDrops"(arg0: $LivingDropsEvent$Type): void
public "playerStartTracking"(arg0: $PlayerEvent$StartTracking$Type): void
public "playerLoggedIn"(arg0: $PlayerEvent$PlayerLoggedInEvent$Type): void
public "attachEntitiesCapabilities"(arg0: $AttachCapabilitiesEvent$Type<($Entity$Type)>): void
public "livingEquipmentChange"(arg0: $LivingEquipmentChangeEvent$Type): void
public "looting"(arg0: $LootingLevelEvent$Type): void
public "playerClone"(arg0: $PlayerEvent$Clone$Type): void
public "onBreakBlock"(arg0: $BlockEvent$BreakEvent$Type): void
public "attachStackCapabilities"(arg0: $AttachCapabilitiesEvent$Type<($ItemStack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosEventHandler$Type = ($CuriosEventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosEventHandler_ = $CuriosEventHandler$Type;
}}
declare module "packages/top/theillusivec4/curios/api/$CuriosTriggers$EquipBuilder" {
import {$ItemPredicate$Builder, $ItemPredicate$Builder$Type} from "packages/net/minecraft/advancements/critereon/$ItemPredicate$Builder"
import {$LocationPredicate$Builder, $LocationPredicate$Builder$Type} from "packages/net/minecraft/advancements/critereon/$LocationPredicate$Builder"
import {$SlotPredicate$Builder, $SlotPredicate$Builder$Type} from "packages/top/theillusivec4/curios/api/$SlotPredicate$Builder"
import {$CriterionTriggerInstance, $CriterionTriggerInstance$Type} from "packages/net/minecraft/advancements/$CriterionTriggerInstance"

export class $CuriosTriggers$EquipBuilder {


public "build"(): $CriterionTriggerInstance
public "withLocation"(arg0: $LocationPredicate$Builder$Type): $CuriosTriggers$EquipBuilder
public "withSlot"(arg0: $SlotPredicate$Builder$Type): $CuriosTriggers$EquipBuilder
public "withItem"(arg0: $ItemPredicate$Builder$Type): $CuriosTriggers$EquipBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosTriggers$EquipBuilder$Type = ($CuriosTriggers$EquipBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosTriggers$EquipBuilder_ = $CuriosTriggers$EquipBuilder$Type;
}}
declare module "packages/top/theillusivec4/curios/common/capability/$CurioInventoryCapability" {
import {$ICapabilityProvider, $ICapabilityProvider$Type} from "packages/net/minecraftforge/common/capabilities/$ICapabilityProvider"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $CurioInventoryCapability {

constructor()

public static "createProvider"(arg0: $LivingEntity$Type): $ICapabilityProvider
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CurioInventoryCapability$Type = ($CurioInventoryCapability);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CurioInventoryCapability_ = $CurioInventoryCapability$Type;
}}
declare module "packages/top/theillusivec4/curios/api/client/$CuriosRendererRegistry" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$ICurioRenderer, $ICurioRenderer$Type} from "packages/top/theillusivec4/curios/api/client/$ICurioRenderer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $CuriosRendererRegistry {

constructor()

public static "load"(): void
public static "register"(arg0: $Item$Type, arg1: $Supplier$Type<($ICurioRenderer$Type)>): void
public static "getRenderer"(arg0: $Item$Type): $Optional<($ICurioRenderer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosRendererRegistry$Type = ($CuriosRendererRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosRendererRegistry_ = $CuriosRendererRegistry$Type;
}}
declare module "packages/top/theillusivec4/curios/api/event/$CurioEquipEvent" {
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$LivingEvent, $LivingEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingEvent"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $CurioEquipEvent extends $LivingEvent {

constructor()
constructor(arg0: $ItemStack$Type, arg1: $SlotContext$Type)

public "getStack"(): $ItemStack
public "getSlotContext"(): $SlotContext
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "stack"(): $ItemStack
get "slotContext"(): $SlotContext
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CurioEquipEvent$Type = ($CurioEquipEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CurioEquipEvent_ = $CurioEquipEvent$Type;
}}
declare module "packages/top/theillusivec4/curios/api/type/capability/$ICurioItem" {
import {$ICurio, $ICurio$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$ICurio$SoundInfo, $ICurio$SoundInfo$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio$SoundInfo"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$EnderMan, $EnderMan$Type} from "packages/net/minecraft/world/entity/monster/$EnderMan"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$ICurio$DropRule, $ICurio$DropRule$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio$DropRule"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"

export interface $ICurioItem {

/**
 * 
 * @deprecated
 */
 "onEquip"(arg0: string, arg1: integer, arg2: $LivingEntity$Type, arg3: $ItemStack$Type): void
 "onEquip"(arg0: $SlotContext$Type, arg1: $ItemStack$Type, arg2: $ItemStack$Type): void
 "getAttributesTooltip"(arg0: $List$Type<($Component$Type)>, arg1: $ItemStack$Type): $List<($Component)>
/**
 * 
 * @deprecated
 */
 "showAttributesTooltip"(arg0: string, arg1: $ItemStack$Type): boolean
/**
 * 
 * @deprecated
 */
 "playRightClickEquipSound"(arg0: $LivingEntity$Type, arg1: $ItemStack$Type): void
 "makesPiglinsNeutral"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): boolean
 "canWalkOnPowderedSnow"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): boolean
/**
 * 
 * @deprecated
 */
 "getAttributeModifiers"(arg0: string, arg1: $ItemStack$Type): $Multimap<($Attribute), ($AttributeModifier)>
 "getAttributeModifiers"(arg0: $SlotContext$Type, arg1: $UUID$Type, arg2: $ItemStack$Type): $Multimap<($Attribute), ($AttributeModifier)>
 "hasCurioCapability"(arg0: $ItemStack$Type): boolean
 "curioTick"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): void
/**
 * 
 * @deprecated
 */
 "curioTick"(arg0: string, arg1: integer, arg2: $LivingEntity$Type, arg3: $ItemStack$Type): void
 "canUnequip"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): boolean
/**
 * 
 * @deprecated
 */
 "canUnequip"(arg0: string, arg1: $LivingEntity$Type, arg2: $ItemStack$Type): boolean
 "curioBreak"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): void
/**
 * 
 * @deprecated
 */
 "curioBreak"(arg0: $ItemStack$Type, arg1: $LivingEntity$Type): void
/**
 * 
 * @deprecated
 */
 "getDropRule"(arg0: $LivingEntity$Type, arg1: $ItemStack$Type): $ICurio$DropRule
 "getDropRule"(arg0: $SlotContext$Type, arg1: $DamageSource$Type, arg2: integer, arg3: boolean, arg4: $ItemStack$Type): $ICurio$DropRule
/**
 * 
 * @deprecated
 */
 "getFortuneBonus"(arg0: string, arg1: $LivingEntity$Type, arg2: $ItemStack$Type, arg3: integer): integer
/**
 * 
 * @deprecated
 */
 "getLootingBonus"(arg0: string, arg1: $LivingEntity$Type, arg2: $ItemStack$Type, arg3: integer): integer
 "canEquipFromUse"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): boolean
 "onEquipFromUse"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): void
 "getEquipSound"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): $ICurio$SoundInfo
 "getSlotsTooltip"(arg0: $List$Type<($Component$Type)>, arg1: $ItemStack$Type): $List<($Component)>
/**
 * 
 * @deprecated
 */
 "readSyncData"(arg0: $CompoundTag$Type, arg1: $ItemStack$Type): void
 "readSyncData"(arg0: $SlotContext$Type, arg1: $CompoundTag$Type, arg2: $ItemStack$Type): void
/**
 * 
 * @deprecated
 */
 "onUnequip"(arg0: string, arg1: integer, arg2: $LivingEntity$Type, arg3: $ItemStack$Type): void
 "onUnequip"(arg0: $SlotContext$Type, arg1: $ItemStack$Type, arg2: $ItemStack$Type): void
/**
 * 
 * @deprecated
 */
 "canRightClickEquip"(arg0: $ItemStack$Type): boolean
/**
 * 
 * @deprecated
 */
 "getTagsTooltip"(arg0: $List$Type<($Component$Type)>, arg1: $ItemStack$Type): $List<($Component)>
/**
 * 
 * @deprecated
 */
 "curioAnimate"(arg0: string, arg1: integer, arg2: $LivingEntity$Type, arg3: $ItemStack$Type): void
 "writeSyncData"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): $CompoundTag
/**
 * 
 * @deprecated
 */
 "writeSyncData"(arg0: $ItemStack$Type): $CompoundTag
 "canSync"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): boolean
/**
 * 
 * @deprecated
 */
 "canSync"(arg0: string, arg1: integer, arg2: $LivingEntity$Type, arg3: $ItemStack$Type): boolean
 "getLootingLevel"(arg0: $SlotContext$Type, arg1: $DamageSource$Type, arg2: $LivingEntity$Type, arg3: integer, arg4: $ItemStack$Type): integer
/**
 * 
 * @deprecated
 */
 "canEquip"(arg0: string, arg1: $LivingEntity$Type, arg2: $ItemStack$Type): boolean
 "canEquip"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): boolean
 "isEnderMask"(arg0: $SlotContext$Type, arg1: $EnderMan$Type, arg2: $ItemStack$Type): boolean
 "getFortuneLevel"(arg0: $SlotContext$Type, arg1: $LootContext$Type, arg2: $ItemStack$Type): integer
}

export namespace $ICurioItem {
const defaultInstance: $ICurio
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICurioItem$Type = ($ICurioItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICurioItem_ = $ICurioItem$Type;
}}
declare module "packages/top/theillusivec4/curios/client/$CuriosClientConfig$Client$ButtonCorner" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $CuriosClientConfig$Client$ButtonCorner extends $Enum<($CuriosClientConfig$Client$ButtonCorner)> {
static readonly "TOP_LEFT": $CuriosClientConfig$Client$ButtonCorner
static readonly "TOP_RIGHT": $CuriosClientConfig$Client$ButtonCorner
static readonly "BOTTOM_LEFT": $CuriosClientConfig$Client$ButtonCorner
static readonly "BOTTOM_RIGHT": $CuriosClientConfig$Client$ButtonCorner


public static "values"(): ($CuriosClientConfig$Client$ButtonCorner)[]
public static "valueOf"(arg0: string): $CuriosClientConfig$Client$ButtonCorner
public "getCreativeXoffset"(): integer
public "getYoffset"(): integer
public "getCreativeYoffset"(): integer
public "getXoffset"(): integer
get "creativeXoffset"(): integer
get "yoffset"(): integer
get "creativeYoffset"(): integer
get "xoffset"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosClientConfig$Client$ButtonCorner$Type = (("top_right") | ("top_left") | ("bottom_right") | ("bottom_left")) | ($CuriosClientConfig$Client$ButtonCorner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosClientConfig$Client$ButtonCorner_ = $CuriosClientConfig$Client$ButtonCorner$Type;
}}
declare module "packages/top/theillusivec4/curios/client/gui/$PageButton$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $PageButton$Type extends $Enum<($PageButton$Type)> {
static readonly "NEXT": $PageButton$Type
static readonly "PREVIOUS": $PageButton$Type


public static "values"(): ($PageButton$Type)[]
public static "valueOf"(arg0: string): $PageButton$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageButton$Type$Type = (("next") | ("previous")) | ($PageButton$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageButton$Type_ = $PageButton$Type$Type;
}}
declare module "packages/top/theillusivec4/curios/api/event/$CurioUnequipEvent" {
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$LivingEvent, $LivingEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingEvent"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $CurioUnequipEvent extends $LivingEvent {

constructor()
constructor(arg0: $ItemStack$Type, arg1: $SlotContext$Type)

public "getStack"(): $ItemStack
public "getSlotContext"(): $SlotContext
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "stack"(): $ItemStack
get "slotContext"(): $SlotContext
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CurioUnequipEvent$Type = ($CurioUnequipEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CurioUnequipEvent_ = $CurioUnequipEvent$Type;
}}
declare module "packages/top/theillusivec4/curios/common/$CuriosHelper" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$ICurio, $ICurio$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$ICuriosHelper, $ICuriosHelper$Type} from "packages/top/theillusivec4/curios/api/type/util/$ICuriosHelper"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$TriConsumer, $TriConsumer$Type} from "packages/org/apache/logging/log4j/util/$TriConsumer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IItemHandlerModifiable, $IItemHandlerModifiable$Type} from "packages/net/minecraftforge/items/$IItemHandlerModifiable"
import {$ICuriosItemHandler, $ICuriosItemHandler$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICuriosItemHandler"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$CuriosHelper$SlotAttributeWrapper, $CuriosHelper$SlotAttributeWrapper$Type} from "packages/top/theillusivec4/curios/common/$CuriosHelper$SlotAttributeWrapper"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$SlotResult, $SlotResult$Type} from "packages/top/theillusivec4/curios/api/$SlotResult"
import {$ImmutableTriple, $ImmutableTriple$Type} from "packages/org/apache/commons/lang3/tuple/$ImmutableTriple"

export class $CuriosHelper implements $ICuriosHelper {

constructor()

public "setBrokenCurioConsumer"(arg0: $TriConsumer$Type<(string), (integer), ($LivingEntity$Type)>): void
public "setBrokenCurioConsumer"(arg0: $Consumer$Type<($SlotContext$Type)>): void
public "getAttributeModifiers"(arg0: string, arg1: $ItemStack$Type): $Multimap<($Attribute), ($AttributeModifier)>
public "getAttributeModifiers"(arg0: $SlotContext$Type, arg1: $UUID$Type, arg2: $ItemStack$Type): $Multimap<($Attribute), ($AttributeModifier)>
/**
 * 
 * @deprecated
 */
public static "getOrCreateSlotAttribute"(arg0: string): $CuriosHelper$SlotAttributeWrapper
public "getCurioTags"(arg0: $Item$Type): $Set<(string)>
public "getCurio"(arg0: $ItemStack$Type): $LazyOptional<($ICurio)>
public "getCuriosHandler"(arg0: $LivingEntity$Type): $LazyOptional<($ICuriosItemHandler)>
public "findEquippedCurio"(arg0: $Predicate$Type<($ItemStack$Type)>, arg1: $LivingEntity$Type): $Optional<($ImmutableTriple<(string), (integer), ($ItemStack)>)>
public "findEquippedCurio"(arg0: $Item$Type, arg1: $LivingEntity$Type): $Optional<($ImmutableTriple<(string), (integer), ($ItemStack)>)>
public "addModifier"(arg0: $ItemStack$Type, arg1: $Attribute$Type, arg2: string, arg3: $UUID$Type, arg4: double, arg5: $AttributeModifier$Operation$Type, arg6: string): void
public "findFirstCurio"(arg0: $LivingEntity$Type, arg1: $Item$Type): $Optional<($SlotResult)>
public "findFirstCurio"(arg0: $LivingEntity$Type, arg1: $Predicate$Type<($ItemStack$Type)>): $Optional<($SlotResult)>
public "onBrokenCurio"(arg0: $SlotContext$Type): void
public "onBrokenCurio"(arg0: string, arg1: integer, arg2: $LivingEntity$Type): void
public "findCurios"(arg0: $LivingEntity$Type, ...arg1: (string)[]): $List<($SlotResult)>
public "findCurios"(arg0: $LivingEntity$Type, arg1: $Predicate$Type<($ItemStack$Type)>): $List<($SlotResult)>
public "findCurios"(arg0: $LivingEntity$Type, arg1: $Item$Type): $List<($SlotResult)>
public "setEquippedCurio"(arg0: $LivingEntity$Type, arg1: string, arg2: integer, arg3: $ItemStack$Type): void
public "findCurio"(arg0: $LivingEntity$Type, arg1: string, arg2: integer): $Optional<($SlotResult)>
public "isStackValid"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): boolean
public "addSlotModifier"(arg0: $Multimap$Type<($Attribute$Type), ($AttributeModifier$Type)>, arg1: string, arg2: $UUID$Type, arg3: double, arg4: $AttributeModifier$Operation$Type): void
public "addSlotModifier"(arg0: $ItemStack$Type, arg1: string, arg2: string, arg3: $UUID$Type, arg4: double, arg5: $AttributeModifier$Operation$Type, arg6: string): void
public "getEquippedCurios"(arg0: $LivingEntity$Type): $LazyOptional<($IItemHandlerModifiable)>
set "brokenCurioConsumer"(value: $TriConsumer$Type<(string), (integer), ($LivingEntity$Type)>)
set "brokenCurioConsumer"(value: $Consumer$Type<($SlotContext$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosHelper$Type = ($CuriosHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosHelper_ = $CuriosHelper$Type;
}}
declare module "packages/top/theillusivec4/curios/api/$SlotResult" {
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $SlotResult extends $Record {

constructor(slotContext: $SlotContext$Type, stack: $ItemStack$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "stack"(): $ItemStack
public "slotContext"(): $SlotContext
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotResult$Type = ($SlotResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotResult_ = $SlotResult$Type;
}}
declare module "packages/top/theillusivec4/curios/api/$SlotPredicate$Builder" {
import {$MinMaxBounds$Ints, $MinMaxBounds$Ints$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Ints"
import {$SlotPredicate, $SlotPredicate$Type} from "packages/top/theillusivec4/curios/api/$SlotPredicate"

export class $SlotPredicate$Builder {


public static "slot"(): $SlotPredicate$Builder
public "of"(...arg0: (string)[]): $SlotPredicate$Builder
public "build"(): $SlotPredicate
public "withIndex"(arg0: $MinMaxBounds$Ints$Type): $SlotPredicate$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotPredicate$Builder$Type = ($SlotPredicate$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotPredicate$Builder_ = $SlotPredicate$Builder$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/server/sync/$SPacketSyncStack$HandlerType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $SPacketSyncStack$HandlerType extends $Enum<($SPacketSyncStack$HandlerType)> {
static readonly "EQUIPMENT": $SPacketSyncStack$HandlerType
static readonly "COSMETIC": $SPacketSyncStack$HandlerType


public static "values"(): ($SPacketSyncStack$HandlerType)[]
public static "valueOf"(arg0: string): $SPacketSyncStack$HandlerType
public static "fromValue"(arg0: integer): $SPacketSyncStack$HandlerType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SPacketSyncStack$HandlerType$Type = (("equipment") | ("cosmetic")) | ($SPacketSyncStack$HandlerType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SPacketSyncStack$HandlerType_ = $SPacketSyncStack$HandlerType$Type;
}}
declare module "packages/top/theillusivec4/curios/api/type/data/$ISlotData" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ICurio$DropRule, $ICurio$DropRule$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio$DropRule"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ICondition, $ICondition$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition"

export interface $ISlotData {

 "replace"(arg0: boolean): $ISlotData
 "size"(arg0: integer): $ISlotData
 "order"(arg0: integer): $ISlotData
 "dropRule"(arg0: $ICurio$DropRule$Type): $ISlotData
 "addCondition"(arg0: $ICondition$Type): $ISlotData
 "renderToggle"(arg0: boolean): $ISlotData
 "useNativeGui"(arg0: boolean): $ISlotData
 "operation"(arg0: $AttributeModifier$Operation$Type): $ISlotData
 "serialize"(): $JsonObject
 "addCosmetic"(arg0: boolean): $ISlotData
 "icon"(arg0: $ResourceLocation$Type): $ISlotData
 "addValidator"(arg0: $ResourceLocation$Type): $ISlotData
}

export namespace $ISlotData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISlotData$Type = ($ISlotData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISlotData_ = $ISlotData$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/server/$SPacketGrabbedItem" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $SPacketGrabbedItem {

constructor(arg0: $ItemStack$Type)

public static "decode"(arg0: $FriendlyByteBuf$Type): $SPacketGrabbedItem
public static "encode"(arg0: $SPacketGrabbedItem$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $SPacketGrabbedItem$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SPacketGrabbedItem$Type = ($SPacketGrabbedItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SPacketGrabbedItem_ = $SPacketGrabbedItem$Type;
}}
declare module "packages/top/theillusivec4/curios/common/slottype/$LegacySlotManager" {
import {$InterModComms$IMCMessage, $InterModComms$IMCMessage$Type} from "packages/net/minecraftforge/fml/$InterModComms$IMCMessage"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$SlotType$Builder, $SlotType$Builder$Type} from "packages/top/theillusivec4/curios/common/slottype/$SlotType$Builder"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LegacySlotManager {

constructor()

public static "getImcBuilders"(): $Map<(string), ($SlotType$Builder)>
public static "getIdsToMods"(): $Map<(string), ($Set<(string)>)>
public static "buildImcSlotTypes"(arg0: $Stream$Type<($InterModComms$IMCMessage$Type)>, arg1: $Stream$Type<($InterModComms$IMCMessage$Type)>): void
get "imcBuilders"(): $Map<(string), ($SlotType$Builder)>
get "idsToMods"(): $Map<(string), ($Set<(string)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LegacySlotManager$Type = ($LegacySlotManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LegacySlotManager_ = $LegacySlotManager$Type;
}}
declare module "packages/top/theillusivec4/curios/api/type/inventory/$IDynamicStackHandler" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockContainerJS, $BlockContainerJS$Type} from "packages/dev/latvian/mods/kubejs/level/$BlockContainerJS"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$IItemHandlerModifiable, $IItemHandlerModifiable$Type} from "packages/net/minecraftforge/items/$IItemHandlerModifiable"
import {$IItemHandler, $IItemHandler$Type} from "packages/net/minecraftforge/items/$IItemHandler"

export interface $IDynamicStackHandler extends $IItemHandlerModifiable {

 "grow"(arg0: integer): void
 "getSlots"(): integer
 "getStackInSlot"(arg0: integer): $ItemStack
 "setStackInSlot"(arg0: integer, arg1: $ItemStack$Type): void
 "shrink"(arg0: integer): void
 "setPreviousStackInSlot"(arg0: integer, arg1: $ItemStack$Type): void
 "getPreviousStackInSlot"(arg0: integer): $ItemStack
 "deserializeNBT"(arg0: $CompoundTag$Type): void
 "serializeNBT"(): $CompoundTag
 "kjs$self"(): $IItemHandler
 "getBlock"(level: $Level$Type): $BlockContainerJS
 "insertItem"(arg0: integer, arg1: $ItemStack$Type, arg2: boolean): $ItemStack
 "getSlotLimit"(arg0: integer): integer
 "extractItem"(arg0: integer, arg1: integer, arg2: boolean): $ItemStack
 "isItemValid"(arg0: integer, arg1: $ItemStack$Type): boolean
 "getSlots"(): integer
 "getStackInSlot"(i: integer): $ItemStack
 "insertItem"(i: integer, itemStack: $ItemStack$Type, b: boolean): $ItemStack
 "isMutable"(): boolean
 "extractItem"(i: integer, i1: integer, b: boolean): $ItemStack
 "isItemValid"(i: integer, itemStack: $ItemStack$Type): boolean
 "setStackInSlot"(slot: integer, stack: $ItemStack$Type): void
 "getSlotLimit"(i: integer): integer
 "insertItem"(stack: $ItemStack$Type, simulate: boolean): $ItemStack
 "setChanged"(): void
 "asContainer"(): $Container
 "countNonEmpty"(ingredient: $Ingredient$Type): integer
 "countNonEmpty"(): integer
 "getAllItems"(): $List<($ItemStack)>
 "getHeight"(): integer
 "find"(ingredient: $Ingredient$Type): integer
 "find"(): integer
 "getWidth"(): integer
 "clear"(): void
 "clear"(ingredient: $Ingredient$Type): void
 "count"(ingredient: $Ingredient$Type): integer
 "count"(): integer
 "isEmpty"(): boolean
}

export namespace $IDynamicStackHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IDynamicStackHandler$Type = ($IDynamicStackHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IDynamicStackHandler_ = $IDynamicStackHandler$Type;
}}
declare module "packages/top/theillusivec4/curios/api/$CuriosApi" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$ICurio, $ICurio$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio"
import {$IIconHelper, $IIconHelper$Type} from "packages/top/theillusivec4/curios/api/type/util/$IIconHelper"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ICuriosItemHandler, $ICuriosItemHandler$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICuriosItemHandler"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ICurioItem, $ICurioItem$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurioItem"
import {$SlotResult, $SlotResult$Type} from "packages/top/theillusivec4/curios/api/$SlotResult"
import {$ICuriosHelper, $ICuriosHelper$Type} from "packages/top/theillusivec4/curios/api/type/util/$ICuriosHelper"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$ISlotHelper, $ISlotHelper$Type} from "packages/top/theillusivec4/curios/api/type/util/$ISlotHelper"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ISlotType, $ISlotType$Type} from "packages/top/theillusivec4/curios/api/type/$ISlotType"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$ICapabilityProvider, $ICapabilityProvider$Type} from "packages/net/minecraftforge/common/capabilities/$ICapabilityProvider"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CuriosApi {
static readonly "MODID": string

constructor()

/**
 * 
 * @deprecated
 */
public static "getSlot"(arg0: string): $Optional<($ISlotType)>
public static "getSlot"(arg0: string, arg1: boolean): $Optional<($ISlotType)>
public static "getSlot"(arg0: string, arg1: $Level$Type): $Optional<($ISlotType)>
public static "registerCurio"(arg0: $Item$Type, arg1: $ICurioItem$Type): void
public static "getSlotIcon"(arg0: string): $ResourceLocation
public static "getCurioPredicates"(): $Map<($ResourceLocation), ($Predicate<($SlotResult)>)>
/**
 * 
 * @deprecated
 */
public static "getIconHelper"(): $IIconHelper
/**
 * 
 * @deprecated
 */
public static "getPlayerSlots"(): $Map<(string), ($ISlotType)>
public static "getPlayerSlots"(arg0: $Level$Type): $Map<(string), ($ISlotType)>
public static "getPlayerSlots"(arg0: boolean): $Map<(string), ($ISlotType)>
public static "getPlayerSlots"(arg0: $Player$Type): $Map<(string), ($ISlotType)>
public static "getCurioPredicate"(arg0: $ResourceLocation$Type): $Optional<($Predicate<($SlotResult)>)>
public static "getSlotUuid"(arg0: $SlotContext$Type): $UUID
/**
 * 
 * @deprecated
 */
public static "setIconHelper"(arg0: $IIconHelper$Type): void
/**
 * 
 * @deprecated
 */
public static "setCuriosHelper"(arg0: $ICuriosHelper$Type): void
/**
 * 
 * @deprecated
 */
public static "getSlotHelper"(): $ISlotHelper
/**
 * 
 * @deprecated
 */
public static "setSlotHelper"(arg0: $ISlotHelper$Type): void
/**
 * 
 * @deprecated
 */
public static "getItemStackSlots"(arg0: $ItemStack$Type): $Map<(string), ($ISlotType)>
public static "getItemStackSlots"(arg0: $ItemStack$Type, arg1: $LivingEntity$Type): $Map<(string), ($ISlotType)>
public static "getItemStackSlots"(arg0: $ItemStack$Type, arg1: boolean): $Map<(string), ($ISlotType)>
public static "getItemStackSlots"(arg0: $ItemStack$Type, arg1: $Level$Type): $Map<(string), ($ISlotType)>
/**
 * 
 * @deprecated
 */
public static "getSlots"(): $Map<(string), ($ISlotType)>
public static "getSlots"(arg0: $Level$Type): $Map<(string), ($ISlotType)>
public static "getSlots"(arg0: boolean): $Map<(string), ($ISlotType)>
public static "getAttributeModifiers"(arg0: $SlotContext$Type, arg1: $UUID$Type, arg2: $ItemStack$Type): $Multimap<($Attribute), ($AttributeModifier)>
public static "getEntitySlots"(arg0: $LivingEntity$Type): $Map<(string), ($ISlotType)>
public static "getEntitySlots"(arg0: $EntityType$Type<(any)>, arg1: boolean): $Map<(string), ($ISlotType)>
/**
 * 
 * @deprecated
 */
public static "getEntitySlots"(arg0: $EntityType$Type<(any)>): $Map<(string), ($ISlotType)>
public static "getEntitySlots"(arg0: $EntityType$Type<(any)>, arg1: $Level$Type): $Map<(string), ($ISlotType)>
public static "getCuriosInventory"(arg0: $LivingEntity$Type): $LazyOptional<($ICuriosItemHandler)>
/**
 * 
 * @deprecated
 */
public static "getCuriosHelper"(): $ICuriosHelper
public static "registerCurioPredicate"(arg0: $ResourceLocation$Type, arg1: $Predicate$Type<($SlotResult$Type)>): void
public static "broadcastCurioBreakEvent"(arg0: $SlotContext$Type): void
public static "createCurioProvider"(arg0: $ICurio$Type): $ICapabilityProvider
public static "testCurioPredicates"(arg0: $Set$Type<($ResourceLocation$Type)>, arg1: $SlotResult$Type): boolean
public static "getCurio"(arg0: $ItemStack$Type): $LazyOptional<($ICurio)>
public static "addModifier"(arg0: $ItemStack$Type, arg1: $Attribute$Type, arg2: string, arg3: $UUID$Type, arg4: double, arg5: $AttributeModifier$Operation$Type, arg6: string): void
public static "isStackValid"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): boolean
public static "addSlotModifier"(arg0: $Multimap$Type<($Attribute$Type), ($AttributeModifier$Type)>, arg1: string, arg2: $UUID$Type, arg3: double, arg4: $AttributeModifier$Operation$Type): void
public static "addSlotModifier"(arg0: $ItemStack$Type, arg1: string, arg2: string, arg3: $UUID$Type, arg4: double, arg5: $AttributeModifier$Operation$Type, arg6: string): void
get "curioPredicates"(): $Map<($ResourceLocation), ($Predicate<($SlotResult)>)>
get "iconHelper"(): $IIconHelper
get "playerSlots"(): $Map<(string), ($ISlotType)>
set "iconHelper"(value: $IIconHelper$Type)
set "curiosHelper"(value: $ICuriosHelper$Type)
get "slotHelper"(): $ISlotHelper
set "slotHelper"(value: $ISlotHelper$Type)
get "slots"(): $Map<(string), ($ISlotType)>
get "curiosHelper"(): $ICuriosHelper
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosApi$Type = ($CuriosApi);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosApi_ = $CuriosApi$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/client/$CPacketDestroy" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $CPacketDestroy {

constructor()

public static "decode"(arg0: $FriendlyByteBuf$Type): $CPacketDestroy
public static "encode"(arg0: $CPacketDestroy$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $CPacketDestroy$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CPacketDestroy$Type = ($CPacketDestroy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CPacketDestroy_ = $CPacketDestroy$Type;
}}
declare module "packages/top/theillusivec4/curios/common/$CuriosConfig$Common" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"

export class $CuriosConfig$Common {
 "slots": $ForgeConfigSpec$ConfigValue<($List<(any)>)>

constructor(arg0: $ForgeConfigSpec$Builder$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosConfig$Common$Type = ($CuriosConfig$Common);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosConfig$Common_ = $CuriosConfig$Common$Type;
}}
declare module "packages/top/theillusivec4/curios/common/inventory/container/$CuriosContainerProvider" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export class $CuriosContainerProvider implements $MenuProvider {

constructor()

public "getDisplayName"(): $Component
public "createMenu"(arg0: integer, arg1: $Inventory$Type, arg2: $Player$Type): $AbstractContainerMenu
get "displayName"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosContainerProvider$Type = ($CuriosContainerProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosContainerProvider_ = $CuriosContainerProvider$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/client/$CPacketToggleCosmetics" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $CPacketToggleCosmetics {

constructor(arg0: integer)

public static "decode"(arg0: $FriendlyByteBuf$Type): $CPacketToggleCosmetics
public static "encode"(arg0: $CPacketToggleCosmetics$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $CPacketToggleCosmetics$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CPacketToggleCosmetics$Type = ($CPacketToggleCosmetics);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CPacketToggleCosmetics_ = $CPacketToggleCosmetics$Type;
}}
declare module "packages/top/theillusivec4/curios/client/gui/$PageButton" {
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$CuriosScreenV2, $CuriosScreenV2$Type} from "packages/top/theillusivec4/curios/client/gui/$CuriosScreenV2"
import {$PageButton$Type, $PageButton$Type$Type} from "packages/top/theillusivec4/curios/client/gui/$PageButton$Type"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $PageButton extends $Button {
static readonly "SMALL_WIDTH": integer
static readonly "DEFAULT_WIDTH": integer
static readonly "DEFAULT_HEIGHT": integer
 "onPress": $Button$OnPress
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: $CuriosScreenV2$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $PageButton$Type$Type)

public "m_87963_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageButton$Type = ($PageButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageButton_ = $PageButton$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/client/$CPacketToggleRender" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $CPacketToggleRender {

constructor(arg0: string, arg1: integer)

public static "decode"(arg0: $FriendlyByteBuf$Type): $CPacketToggleRender
public static "encode"(arg0: $CPacketToggleRender$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $CPacketToggleRender$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CPacketToggleRender$Type = ($CPacketToggleRender);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CPacketToggleRender_ = $CPacketToggleRender$Type;
}}
declare module "packages/top/theillusivec4/curios/$Curios" {
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"

export class $Curios {
static readonly "MODID": string
static readonly "LOGGER": $Logger

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Curios$Type = ($Curios);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Curios_ = $Curios$Type;
}}
declare module "packages/top/theillusivec4/caelus/api/$CaelusApi" {
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$CaelusApi$TriState, $CaelusApi$TriState$Type} from "packages/top/theillusivec4/caelus/api/$CaelusApi$TriState"

export class $CaelusApi {

constructor()

public static "getInstance"(): $CaelusApi
public "getElytraModifier"(): $AttributeModifier
public "getFlightAttribute"(): $Attribute
/**
 * 
 * @deprecated
 */
public "canFly"(arg0: $LivingEntity$Type): boolean
public "canFallFly"(arg0: $LivingEntity$Type): $CaelusApi$TriState
public "getModId"(): string
get "instance"(): $CaelusApi
get "elytraModifier"(): $AttributeModifier
get "flightAttribute"(): $Attribute
get "modId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CaelusApi$Type = ($CaelusApi);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CaelusApi_ = $CaelusApi$Type;
}}
declare module "packages/top/theillusivec4/curios/common/data/$EntitiesData" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$IEntitiesData, $IEntitiesData$Type} from "packages/top/theillusivec4/curios/api/type/data/$IEntitiesData"
import {$ICondition, $ICondition$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition"

export class $EntitiesData implements $IEntitiesData {

constructor()

public "addCondition"(arg0: $ICondition$Type): $EntitiesData
public "serialize"(): $JsonObject
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntitiesData$Type = ($EntitiesData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntitiesData_ = $EntitiesData$Type;
}}
declare module "packages/top/theillusivec4/curios/common/data/$CuriosSlotManager" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$SimpleJsonResourceReloadListener, $SimpleJsonResourceReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimpleJsonResourceReloadListener"
import {$ListTag, $ListTag$Type} from "packages/net/minecraft/nbt/$ListTag"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ISlotType, $ISlotType$Type} from "packages/top/theillusivec4/curios/api/type/$ISlotType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$SlotType$Builder, $SlotType$Builder$Type} from "packages/top/theillusivec4/curios/common/slottype/$SlotType$Builder"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CuriosSlotManager extends $SimpleJsonResourceReloadListener {
static "SERVER": $CuriosSlotManager
static "CLIENT": $CuriosSlotManager

constructor()
constructor(arg0: $ICondition$IContext$Type)

public "getSlot"(arg0: string): $Optional<($ISlotType)>
public static "fromJson"(arg0: $SlotType$Builder$Type, arg1: $JsonObject$Type): void
public static "getSyncPacket"(): $ListTag
public "setIcons"(arg0: $Map$Type<(string), ($ResourceLocation$Type)>): void
public static "applySyncPacket"(arg0: $ListTag$Type): void
public "getModsFromSlots"(): $Map<(string), ($Set<(string)>)>
public "getIcon"(arg0: string): $ResourceLocation
public "getSlots"(): $Map<(string), ($ISlotType)>
public "getIcons"(): $Map<(string), ($ResourceLocation)>
public static "fromConfig"(arg0: $Map$Type<(string), ($SlotType$Builder$Type)>): $Set<(string)>
public "getConfigSlots"(): $Set<(string)>
get "syncPacket"(): $ListTag
set "icons"(value: $Map$Type<(string), ($ResourceLocation$Type)>)
get "modsFromSlots"(): $Map<(string), ($Set<(string)>)>
get "slots"(): $Map<(string), ($ISlotType)>
get "icons"(): $Map<(string), ($ResourceLocation)>
get "configSlots"(): $Set<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosSlotManager$Type = ($CuriosSlotManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosSlotManager_ = $CuriosSlotManager$Type;
}}
declare module "packages/top/theillusivec4/curios/api/type/util/$IIconHelper" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

/**
 * 
 * @deprecated
 */
export interface $IIconHelper {

/**
 * 
 * @deprecated
 */
 "getIcon"(arg0: string): $ResourceLocation
/**
 * 
 * @deprecated
 */
 "addIcon"(arg0: string, arg1: $ResourceLocation$Type): void
/**
 * 
 * @deprecated
 */
 "clearIcons"(): void
}

export namespace $IIconHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIconHelper$Type = ($IIconHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIconHelper_ = $IIconHelper$Type;
}}
declare module "packages/top/theillusivec4/curios/client/gui/$CuriosButton" {
import {$ImageButton, $ImageButton$Type} from "packages/net/minecraft/client/gui/components/$ImageButton"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $CuriosButton extends $ImageButton {
readonly "resourceLocation": $ResourceLocation
static readonly "SMALL_WIDTH": integer
static readonly "DEFAULT_WIDTH": integer
static readonly "DEFAULT_HEIGHT": integer
 "onPress": $Button$OnPress
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer


public "renderWidget"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosButton$Type = ($CuriosButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosButton_ = $CuriosButton$Type;
}}
declare module "packages/top/theillusivec4/curios/api/event/$DropRulesEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$LivingEvent, $LivingEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingEvent"
import {$ICurio$DropRule, $ICurio$DropRule$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio$DropRule"
import {$ImmutableList, $ImmutableList$Type} from "packages/com/google/common/collect/$ImmutableList"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$Tuple, $Tuple$Type} from "packages/net/minecraft/util/$Tuple"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ICuriosItemHandler, $ICuriosItemHandler$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICuriosItemHandler"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $DropRulesEvent extends $LivingEvent {

constructor()
constructor(arg0: $LivingEntity$Type, arg1: $ICuriosItemHandler$Type, arg2: $DamageSource$Type, arg3: integer, arg4: boolean)

public "getSource"(): $DamageSource
public "isRecentlyHit"(): boolean
public "addOverride"(arg0: $Predicate$Type<($ItemStack$Type)>, arg1: $ICurio$DropRule$Type): void
public "getCurioHandler"(): $ICuriosItemHandler
public "getLootingLevel"(): integer
public "getOverrides"(): $ImmutableList<($Tuple<($Predicate<($ItemStack)>), ($ICurio$DropRule)>)>
public "getListenerList"(): $ListenerList
get "source"(): $DamageSource
get "recentlyHit"(): boolean
get "curioHandler"(): $ICuriosItemHandler
get "lootingLevel"(): integer
get "overrides"(): $ImmutableList<($Tuple<($Predicate<($ItemStack)>), ($ICurio$DropRule)>)>
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropRulesEvent$Type = ($DropRulesEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropRulesEvent_ = $DropRulesEvent$Type;
}}
declare module "packages/top/theillusivec4/caelus/mixin/util/$MixinHooks" {
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $MixinHooks {

constructor()

public static "canFly"(arg0: $LivingEntity$Type, arg1: boolean, arg2: boolean): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinHooks$Type = ($MixinHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinHooks_ = $MixinHooks$Type;
}}
declare module "packages/top/theillusivec4/curios/common/integration/jei/$CuriosContainerHandler" {
import {$IClickableIngredient, $IClickableIngredient$Type} from "packages/mezz/jei/api/runtime/$IClickableIngredient"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$CuriosScreen, $CuriosScreen$Type} from "packages/top/theillusivec4/curios/client/gui/$CuriosScreen"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IGuiClickableArea, $IGuiClickableArea$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiClickableArea"
import {$IGuiContainerHandler, $IGuiContainerHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiContainerHandler"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"

export class $CuriosContainerHandler implements $IGuiContainerHandler<($CuriosScreen)> {

constructor()

public "getGuiExtraAreas"(arg0: $CuriosScreen$Type): $List<($Rect2i)>
public "getGuiClickableAreas"(arg0: $CuriosScreen$Type, arg1: double, arg2: double): $Collection<($IGuiClickableArea)>
public "getClickableIngredientUnderMouse"(arg0: $CuriosScreen$Type, arg1: double, arg2: double): $Optional<($IClickableIngredient<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosContainerHandler$Type = ($CuriosContainerHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosContainerHandler_ = $CuriosContainerHandler$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/client/$CPacketScroll" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $CPacketScroll {

constructor(arg0: integer, arg1: integer)

public static "decode"(arg0: $FriendlyByteBuf$Type): $CPacketScroll
public static "encode"(arg0: $CPacketScroll$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $CPacketScroll$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CPacketScroll$Type = ($CPacketScroll);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CPacketScroll_ = $CPacketScroll$Type;
}}
declare module "packages/top/theillusivec4/curios/common/data/$SlotData" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ICurio$DropRule, $ICurio$DropRule$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio$DropRule"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$ISlotData, $ISlotData$Type} from "packages/top/theillusivec4/curios/api/type/data/$ISlotData"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ICondition, $ICondition$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition"

export class $SlotData implements $ISlotData {

constructor()

public "size"(arg0: integer): $SlotData
public "dropRule"(arg0: $ICurio$DropRule$Type): $SlotData
public "addCondition"(arg0: $ICondition$Type): $SlotData
public "operation"(arg0: $AttributeModifier$Operation$Type): $SlotData
public "serialize"(): $JsonObject
public "addCosmetic"(arg0: boolean): $SlotData
public "addValidator"(arg0: $ResourceLocation$Type): $ISlotData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotData$Type = ($SlotData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotData_ = $SlotData$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/server/sync/$SPacketSyncData" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$ListTag, $ListTag$Type} from "packages/net/minecraft/nbt/$ListTag"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $SPacketSyncData {

constructor(arg0: $ListTag$Type, arg1: $ListTag$Type)

public static "decode"(arg0: $FriendlyByteBuf$Type): $SPacketSyncData
public static "encode"(arg0: $SPacketSyncData$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $SPacketSyncData$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SPacketSyncData$Type = ($SPacketSyncData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SPacketSyncData_ = $SPacketSyncData$Type;
}}
declare module "packages/top/theillusivec4/caelus/common/network/$CaelusNetwork" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CaelusNetwork {

constructor()

public static "setup"(): void
public static "sendFlightC2S"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CaelusNetwork$Type = ($CaelusNetwork);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CaelusNetwork_ = $CaelusNetwork$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/server/sync/$SPacketSyncStack" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$SPacketSyncStack$HandlerType, $SPacketSyncStack$HandlerType$Type} from "packages/top/theillusivec4/curios/common/network/server/sync/$SPacketSyncStack$HandlerType"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $SPacketSyncStack {

constructor(arg0: integer, arg1: string, arg2: integer, arg3: $ItemStack$Type, arg4: $SPacketSyncStack$HandlerType$Type, arg5: $CompoundTag$Type)

public static "decode"(arg0: $FriendlyByteBuf$Type): $SPacketSyncStack
public static "encode"(arg0: $SPacketSyncStack$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $SPacketSyncStack$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SPacketSyncStack$Type = ($SPacketSyncStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SPacketSyncStack_ = $SPacketSyncStack$Type;
}}
declare module "packages/top/theillusivec4/curios/common/inventory/$CurioStacksHandler" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IDynamicStackHandler, $IDynamicStackHandler$Type} from "packages/top/theillusivec4/curios/api/type/inventory/$IDynamicStackHandler"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ICuriosItemHandler, $ICuriosItemHandler$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICuriosItemHandler"
import {$ICurio$DropRule, $ICurio$DropRule$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio$DropRule"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$ICurioStacksHandler, $ICurioStacksHandler$Type} from "packages/top/theillusivec4/curios/api/type/inventory/$ICurioStacksHandler"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CurioStacksHandler implements $ICurioStacksHandler {

constructor(arg0: $ICuriosItemHandler$Type, arg1: string)
constructor(arg0: $ICuriosItemHandler$Type, arg1: string, arg2: integer, arg3: boolean, arg4: boolean, arg5: boolean, arg6: $ICurio$DropRule$Type)

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "getModifiers"(): $Map<($UUID), ($AttributeModifier)>
public "update"(): void
public "grow"(arg0: integer): void
public "getIdentifier"(): string
public "clearModifiers"(): void
public "removeModifier"(arg0: $UUID$Type): void
public "isVisible"(): boolean
public "copyModifiers"(arg0: $ICurioStacksHandler$Type): void
public "getSyncTag"(): $CompoundTag
public "getCachedModifiers"(): $Set<($AttributeModifier)>
public "getSizeShift"(): integer
public "applySyncTag"(arg0: $CompoundTag$Type): void
public "canToggleRendering"(): boolean
public "hasCosmetic"(): boolean
public "getSlots"(): integer
public "getDropRule"(): $ICurio$DropRule
public "clearCachedModifiers"(): void
public "getModifiersByOperation"(arg0: $AttributeModifier$Operation$Type): $Collection<($AttributeModifier)>
public "addPermanentModifier"(arg0: $AttributeModifier$Type): void
public "getPermanentModifiers"(): $Set<($AttributeModifier)>
public "addTransientModifier"(arg0: $AttributeModifier$Type): void
public "getRenders"(): $NonNullList<(boolean)>
public "getCosmeticStacks"(): $IDynamicStackHandler
public "getStacks"(): $IDynamicStackHandler
public "shrink"(arg0: integer): void
public "deserializeNBT"(arg0: $CompoundTag$Type): void
public "serializeNBT"(): $CompoundTag
get "modifiers"(): $Map<($UUID), ($AttributeModifier)>
get "identifier"(): string
get "visible"(): boolean
get "syncTag"(): $CompoundTag
get "cachedModifiers"(): $Set<($AttributeModifier)>
get "sizeShift"(): integer
get "slots"(): integer
get "dropRule"(): $ICurio$DropRule
get "permanentModifiers"(): $Set<($AttributeModifier)>
get "renders"(): $NonNullList<(boolean)>
get "cosmeticStacks"(): $IDynamicStackHandler
get "stacks"(): $IDynamicStackHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CurioStacksHandler$Type = ($CurioStacksHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CurioStacksHandler_ = $CurioStacksHandler$Type;
}}
declare module "packages/top/theillusivec4/curios/common/$CuriosConfig$Server" {
import {$ForgeConfigSpec$BooleanValue, $ForgeConfigSpec$BooleanValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$BooleanValue"
import {$ForgeConfigSpec$EnumValue, $ForgeConfigSpec$EnumValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$EnumValue"
import {$ForgeConfigSpec$IntValue, $ForgeConfigSpec$IntValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$IntValue"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"
import {$CuriosConfig$KeepCurios, $CuriosConfig$KeepCurios$Type} from "packages/top/theillusivec4/curios/common/$CuriosConfig$KeepCurios"

export class $CuriosConfig$Server {
 "keepCurios": $ForgeConfigSpec$EnumValue<($CuriosConfig$KeepCurios)>
 "enableLegacyMenu": $ForgeConfigSpec$BooleanValue
 "minimumColumns": $ForgeConfigSpec$IntValue
 "maxSlotsPerPage": $ForgeConfigSpec$IntValue

constructor(arg0: $ForgeConfigSpec$Builder$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosConfig$Server$Type = ($CuriosConfig$Server);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosConfig$Server_ = $CuriosConfig$Server$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/$NetworkHandler" {
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $NetworkHandler {
static "INSTANCE": $SimpleChannel

constructor()

public static "register"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkHandler$Type = ($NetworkHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkHandler_ = $NetworkHandler$Type;
}}
declare module "packages/top/theillusivec4/curios/api/type/inventory/$ICurioStacksHandler" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IDynamicStackHandler, $IDynamicStackHandler$Type} from "packages/top/theillusivec4/curios/api/type/inventory/$IDynamicStackHandler"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ICurio$DropRule, $ICurio$DropRule$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio$DropRule"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $ICurioStacksHandler {

 "getModifiers"(): $Map<($UUID), ($AttributeModifier)>
 "update"(): void
/**
 * 
 * @deprecated
 */
 "grow"(arg0: integer): void
 "getIdentifier"(): string
 "clearModifiers"(): void
 "removeModifier"(arg0: $UUID$Type): void
 "isVisible"(): boolean
 "copyModifiers"(arg0: $ICurioStacksHandler$Type): void
 "getSyncTag"(): $CompoundTag
 "getCachedModifiers"(): $Set<($AttributeModifier)>
/**
 * 
 * @deprecated
 */
 "getSizeShift"(): integer
 "applySyncTag"(arg0: $CompoundTag$Type): void
 "canToggleRendering"(): boolean
 "hasCosmetic"(): boolean
 "getSlots"(): integer
 "getDropRule"(): $ICurio$DropRule
 "clearCachedModifiers"(): void
 "getModifiersByOperation"(arg0: $AttributeModifier$Operation$Type): $Collection<($AttributeModifier)>
 "addPermanentModifier"(arg0: $AttributeModifier$Type): void
 "getPermanentModifiers"(): $Set<($AttributeModifier)>
 "addTransientModifier"(arg0: $AttributeModifier$Type): void
 "getRenders"(): $NonNullList<(boolean)>
 "getCosmeticStacks"(): $IDynamicStackHandler
 "getStacks"(): $IDynamicStackHandler
/**
 * 
 * @deprecated
 */
 "shrink"(arg0: integer): void
 "deserializeNBT"(arg0: $CompoundTag$Type): void
 "serializeNBT"(): $CompoundTag
}

export namespace $ICurioStacksHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICurioStacksHandler$Type = ($ICurioStacksHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICurioStacksHandler_ = $ICurioStacksHandler$Type;
}}
declare module "packages/top/theillusivec4/caelus/common/network/$CPacketFlight" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $CPacketFlight {

constructor()

public static "decode"(arg0: $FriendlyByteBuf$Type): $CPacketFlight
public static "encode"(arg0: $CPacketFlight$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $CPacketFlight$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CPacketFlight$Type = ($CPacketFlight);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CPacketFlight_ = $CPacketFlight$Type;
}}
declare module "packages/top/theillusivec4/curios/common/inventory/$DynamicStackHandler" {
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$IDynamicStackHandler, $IDynamicStackHandler$Type} from "packages/top/theillusivec4/curios/api/type/inventory/$IDynamicStackHandler"
import {$ItemStackHandler, $ItemStackHandler$Type} from "packages/net/minecraftforge/items/$ItemStackHandler"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $DynamicStackHandler extends $ItemStackHandler implements $IDynamicStackHandler {

constructor(arg0: integer, arg1: $Function$Type<(integer), ($SlotContext$Type)>)

public "grow"(arg0: integer): void
public "extractItem"(arg0: integer, arg1: integer, arg2: boolean): $ItemStack
public "isItemValid"(arg0: integer, arg1: $ItemStack$Type): boolean
public "shrink"(arg0: integer): void
public "setPreviousStackInSlot"(arg0: integer, arg1: $ItemStack$Type): void
public "getPreviousStackInSlot"(arg0: integer): $ItemStack
public "getSlots"(): integer
public "getStackInSlot"(arg0: integer): $ItemStack
public "setStackInSlot"(arg0: integer, arg1: $ItemStack$Type): void
public "deserializeNBT"(arg0: $CompoundTag$Type): void
public "serializeNBT"(): $CompoundTag
get "slots"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicStackHandler$Type = ($DynamicStackHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicStackHandler_ = $DynamicStackHandler$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/client/$CPacketPage" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $CPacketPage {

constructor(arg0: integer, arg1: boolean)

public static "decode"(arg0: $FriendlyByteBuf$Type): $CPacketPage
public static "encode"(arg0: $CPacketPage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $CPacketPage$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CPacketPage$Type = ($CPacketPage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CPacketPage_ = $CPacketPage$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/server/$SPacketSetIcons" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $SPacketSetIcons {

constructor(arg0: $Map$Type<(string), ($ResourceLocation$Type)>)

public static "decode"(arg0: $FriendlyByteBuf$Type): $SPacketSetIcons
public static "encode"(arg0: $SPacketSetIcons$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $SPacketSetIcons$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SPacketSetIcons$Type = ($SPacketSetIcons);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SPacketSetIcons_ = $SPacketSetIcons$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/client/$CPacketOpenVanilla" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $CPacketOpenVanilla {

constructor(arg0: $ItemStack$Type)

public static "decode"(arg0: $FriendlyByteBuf$Type): $CPacketOpenVanilla
public static "encode"(arg0: $CPacketOpenVanilla$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $CPacketOpenVanilla$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CPacketOpenVanilla$Type = ($CPacketOpenVanilla);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CPacketOpenVanilla_ = $CPacketOpenVanilla$Type;
}}
declare module "packages/top/theillusivec4/curios/server/command/$CuriosCommand" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandBuildContext, $CommandBuildContext$Type} from "packages/net/minecraft/commands/$CommandBuildContext"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export class $CuriosCommand {

constructor()

public static "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>, arg1: $CommandBuildContext$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosCommand$Type = ($CuriosCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosCommand_ = $CuriosCommand$Type;
}}
declare module "packages/top/theillusivec4/curios/api/event/$SlotModifiersUpdatedEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$LivingEvent, $LivingEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingEvent"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $SlotModifiersUpdatedEvent extends $LivingEvent {

constructor()
constructor(arg0: $LivingEntity$Type, arg1: $Set$Type<(string)>)

public "getTypes"(): $Set<(string)>
public "getListenerList"(): $ListenerList
get "types"(): $Set<(string)>
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotModifiersUpdatedEvent$Type = ($SlotModifiersUpdatedEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotModifiersUpdatedEvent_ = $SlotModifiersUpdatedEvent$Type;
}}
declare module "packages/top/theillusivec4/curios/api/type/capability/$ICurio$SoundInfo" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $ICurio$SoundInfo extends $Record {

constructor(soundEvent: $SoundEvent$Type, volume: float, pitch: float)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
/**
 * 
 * @deprecated
 */
public "getSoundEvent"(): $SoundEvent
public "pitch"(): float
public "volume"(): float
/**
 * 
 * @deprecated
 */
public "getVolume"(): float
/**
 * 
 * @deprecated
 */
public "getPitch"(): float
public "soundEvent"(): $SoundEvent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICurio$SoundInfo$Type = ($ICurio$SoundInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICurio$SoundInfo_ = $ICurio$SoundInfo$Type;
}}
declare module "packages/top/theillusivec4/curios/common/data/$CuriosEntityManager" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$SimpleJsonResourceReloadListener, $SimpleJsonResourceReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimpleJsonResourceReloadListener"
import {$ListTag, $ListTag$Type} from "packages/net/minecraft/nbt/$ListTag"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ISlotType, $ISlotType$Type} from "packages/top/theillusivec4/curios/api/type/$ISlotType"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CuriosEntityManager extends $SimpleJsonResourceReloadListener {
static "SERVER": $CuriosEntityManager
static "CLIENT": $CuriosEntityManager

constructor()
constructor(arg0: $ICondition$IContext$Type)

public static "getSyncPacket"(): $ListTag
public static "applySyncPacket"(arg0: $ListTag$Type): void
public "getModsFromSlots"(): $Map<(string), ($Set<(string)>)>
public "getEntitySlots"(arg0: $EntityType$Type<(any)>): $Map<(string), ($ISlotType)>
get "syncPacket"(): $ListTag
get "modsFromSlots"(): $Map<(string), ($Set<(string)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosEntityManager$Type = ($CuriosEntityManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosEntityManager_ = $CuriosEntityManager$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/server/sync/$SPacketSyncRender" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $SPacketSyncRender {

constructor(arg0: integer, arg1: string, arg2: integer, arg3: boolean)

public static "decode"(arg0: $FriendlyByteBuf$Type): $SPacketSyncRender
public static "encode"(arg0: $SPacketSyncRender$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $SPacketSyncRender$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SPacketSyncRender$Type = ($SPacketSyncRender);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SPacketSyncRender_ = $SPacketSyncRender$Type;
}}
declare module "packages/top/theillusivec4/caelus/common/$CaelusApiImpl" {
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$CaelusApi, $CaelusApi$Type} from "packages/top/theillusivec4/caelus/api/$CaelusApi"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$CaelusApi$TriState, $CaelusApi$TriState$Type} from "packages/top/theillusivec4/caelus/api/$CaelusApi$TriState"

export class $CaelusApiImpl extends $CaelusApi {
static readonly "INSTANCE": $CaelusApi
static readonly "MOD_ID": string
static readonly "ATTRIBUTES": $DeferredRegister<($Attribute)>

constructor()

public "getElytraModifier"(): $AttributeModifier
public "getFlightAttribute"(): $Attribute
public "canFly"(arg0: $LivingEntity$Type): boolean
public "canFallFly"(arg0: $LivingEntity$Type): $CaelusApi$TriState
public "getModId"(): string
get "elytraModifier"(): $AttributeModifier
get "flightAttribute"(): $Attribute
get "modId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CaelusApiImpl$Type = ($CaelusApiImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CaelusApiImpl_ = $CaelusApiImpl$Type;
}}
declare module "packages/top/theillusivec4/curios/api/type/data/$IEntitiesData" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$ICondition, $ICondition$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition"

export interface $IEntitiesData {

 "replace"(arg0: boolean): $IEntitiesData
 "addCondition"(arg0: $ICondition$Type): $IEntitiesData
 "addPlayer"(): $IEntitiesData
 "addSlots"(...arg0: (string)[]): $IEntitiesData
 "serialize"(): $JsonObject
 "addEntities"(...arg0: ($EntityType$Type<(any)>)[]): $IEntitiesData
}

export namespace $IEntitiesData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IEntitiesData$Type = ($IEntitiesData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IEntitiesData_ = $IEntitiesData$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/client/$ClientPacketHandler" {
import {$SPacketQuickMove, $SPacketQuickMove$Type} from "packages/top/theillusivec4/curios/common/network/server/$SPacketQuickMove"

export class $ClientPacketHandler {

constructor()

public static "handlePacket"(arg0: $SPacketQuickMove$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientPacketHandler$Type = ($ClientPacketHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientPacketHandler_ = $ClientPacketHandler$Type;
}}
declare module "packages/top/theillusivec4/curios/server/command/$CuriosSelectorOptions" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CuriosSelectorOptions {

constructor()

public static "register"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosSelectorOptions$Type = ($CuriosSelectorOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosSelectorOptions_ = $CuriosSelectorOptions$Type;
}}
declare module "packages/top/theillusivec4/caelus/api/$RenderCapeEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PlayerEvent, $PlayerEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent"

export class $RenderCapeEvent extends $PlayerEvent {

constructor()
constructor(arg0: $Player$Type)

public "isCancelable"(): boolean
public "getListenerList"(): $ListenerList
get "cancelable"(): boolean
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderCapeEvent$Type = ($RenderCapeEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderCapeEvent_ = $RenderCapeEvent$Type;
}}
declare module "packages/top/theillusivec4/curios/api/type/$ISlotType" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ICurio$DropRule, $ICurio$DropRule$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio$DropRule"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $ISlotType extends $Comparable<($ISlotType)> {

/**
 * 
 * @deprecated
 */
 "getPriority"(): integer
 "getSize"(): integer
/**
 * 
 * @deprecated
 */
 "isLocked"(): boolean
 "getIdentifier"(): string
/**
 * 
 * @deprecated
 */
 "isVisible"(): boolean
 "getValidators"(): $Set<($ResourceLocation)>
 "writeNbt"(): $CompoundTag
 "useNativeGui"(): boolean
 "getOrder"(): integer
 "canToggleRendering"(): boolean
 "hasCosmetic"(): boolean
 "getIcon"(): $ResourceLocation
 "getDropRule"(): $ICurio$DropRule
 "compareTo"(arg0: $ISlotType$Type): integer
}

export namespace $ISlotType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISlotType$Type = ($ISlotType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISlotType_ = $ISlotType$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/server/sync/$SPacketSyncCurios" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ICurioStacksHandler, $ICurioStacksHandler$Type} from "packages/top/theillusivec4/curios/api/type/inventory/$ICurioStacksHandler"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $SPacketSyncCurios {

constructor(arg0: integer, arg1: $Map$Type<(string), ($ICurioStacksHandler$Type)>)
constructor(arg0: $Map$Type<(string), ($CompoundTag$Type)>, arg1: integer)

public static "decode"(arg0: $FriendlyByteBuf$Type): $SPacketSyncCurios
public static "encode"(arg0: $SPacketSyncCurios$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $SPacketSyncCurios$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SPacketSyncCurios$Type = ($SPacketSyncCurios);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SPacketSyncCurios_ = $SPacketSyncCurios$Type;
}}
declare module "packages/top/theillusivec4/curios/common/$CuriosConfig" {
import {$CuriosConfig$Server, $CuriosConfig$Server$Type} from "packages/top/theillusivec4/curios/common/$CuriosConfig$Server"
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$CuriosConfig$Common, $CuriosConfig$Common$Type} from "packages/top/theillusivec4/curios/common/$CuriosConfig$Common"

export class $CuriosConfig {
static readonly "SERVER_SPEC": $ForgeConfigSpec
static readonly "SERVER": $CuriosConfig$Server
static readonly "COMMON_SPEC": $ForgeConfigSpec
static readonly "COMMON": $CuriosConfig$Common

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosConfig$Type = ($CuriosConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosConfig_ = $CuriosConfig$Type;
}}
declare module "packages/top/theillusivec4/curios/api/event/$CurioDropsEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$LivingEvent, $LivingEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingEvent"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$ICuriosItemHandler, $ICuriosItemHandler$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICuriosItemHandler"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $CurioDropsEvent extends $LivingEvent {

constructor(arg0: $LivingEntity$Type, arg1: $ICuriosItemHandler$Type, arg2: $DamageSource$Type, arg3: $Collection$Type<($ItemEntity$Type)>, arg4: integer, arg5: boolean)
constructor()

public "getSource"(): $DamageSource
public "isRecentlyHit"(): boolean
public "isCancelable"(): boolean
public "getCurioHandler"(): $ICuriosItemHandler
public "getDrops"(): $Collection<($ItemEntity)>
public "getLootingLevel"(): integer
public "getListenerList"(): $ListenerList
get "source"(): $DamageSource
get "recentlyHit"(): boolean
get "cancelable"(): boolean
get "curioHandler"(): $ICuriosItemHandler
get "drops"(): $Collection<($ItemEntity)>
get "lootingLevel"(): integer
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CurioDropsEvent$Type = ($CurioDropsEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CurioDropsEvent_ = $CurioDropsEvent$Type;
}}
declare module "packages/top/theillusivec4/curios/common/$CuriosConfig$KeepCurios" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $CuriosConfig$KeepCurios extends $Enum<($CuriosConfig$KeepCurios)> {
static readonly "ON": $CuriosConfig$KeepCurios
static readonly "DEFAULT": $CuriosConfig$KeepCurios
static readonly "OFF": $CuriosConfig$KeepCurios


public static "values"(): ($CuriosConfig$KeepCurios)[]
public static "valueOf"(arg0: string): $CuriosConfig$KeepCurios
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosConfig$KeepCurios$Type = (("default") | ("off") | ("on")) | ($CuriosConfig$KeepCurios);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosConfig$KeepCurios_ = $CuriosConfig$KeepCurios$Type;
}}
declare module "packages/top/theillusivec4/curios/api/$SlotContext" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $SlotContext extends $Record {

constructor(identifier: string, entity: $LivingEntity$Type, index: integer, cosmetic: boolean, visible: boolean)

public "index"(): integer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
/**
 * 
 * @deprecated
 */
public "getIndex"(): integer
public "visible"(): boolean
/**
 * 
 * @deprecated
 */
public "getIdentifier"(): string
/**
 * 
 * @deprecated
 */
public "getWearer"(): $LivingEntity
public "identifier"(): string
public "entity"(): $LivingEntity
public "cosmetic"(): boolean
get "wearer"(): $LivingEntity
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotContext$Type = ($SlotContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotContext_ = $SlotContext$Type;
}}
declare module "packages/top/theillusivec4/caelus/api/$CaelusApi$TriState" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $CaelusApi$TriState extends $Enum<($CaelusApi$TriState)> {
static readonly "ALLOW": $CaelusApi$TriState
static readonly "DEFAULT": $CaelusApi$TriState
static readonly "DENY": $CaelusApi$TriState


public static "values"(): ($CaelusApi$TriState)[]
public static "valueOf"(arg0: string): $CaelusApi$TriState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CaelusApi$TriState$Type = (("allow") | ("default") | ("deny")) | ($CaelusApi$TriState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CaelusApi$TriState_ = $CaelusApi$TriState$Type;
}}
declare module "packages/top/theillusivec4/curios/common/util/$EquipCurioTrigger$Instance" {
import {$LocationPredicate, $LocationPredicate$Type} from "packages/net/minecraft/advancements/critereon/$LocationPredicate"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ItemPredicate, $ItemPredicate$Type} from "packages/net/minecraft/advancements/critereon/$ItemPredicate"
import {$AbstractCriterionTriggerInstance, $AbstractCriterionTriggerInstance$Type} from "packages/net/minecraft/advancements/critereon/$AbstractCriterionTriggerInstance"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$SerializationContext, $SerializationContext$Type} from "packages/net/minecraft/advancements/critereon/$SerializationContext"
import {$ContextAwarePredicate, $ContextAwarePredicate$Type} from "packages/net/minecraft/advancements/critereon/$ContextAwarePredicate"
import {$SlotPredicate, $SlotPredicate$Type} from "packages/top/theillusivec4/curios/api/$SlotPredicate"

export class $EquipCurioTrigger$Instance extends $AbstractCriterionTriggerInstance {

constructor(arg0: $ContextAwarePredicate$Type, arg1: $ItemPredicate$Type, arg2: $LocationPredicate$Type, arg3: $SlotPredicate$Type)

public "getCriterion"(): $ResourceLocation
public "serializeToJson"(arg0: $SerializationContext$Type): $JsonObject
get "criterion"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EquipCurioTrigger$Instance$Type = ($EquipCurioTrigger$Instance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EquipCurioTrigger$Instance_ = $EquipCurioTrigger$Instance$Type;
}}
declare module "packages/top/theillusivec4/curios/api/type/util/$ISlotHelper" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ISlotType, $ISlotType$Type} from "packages/top/theillusivec4/curios/api/type/$ISlotType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$SortedMap, $SortedMap$Type} from "packages/java/util/$SortedMap"
import {$ICurioStacksHandler, $ICurioStacksHandler$Type} from "packages/top/theillusivec4/curios/api/type/inventory/$ICurioStacksHandler"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

/**
 * 
 * @deprecated
 */
export interface $ISlotHelper {

/**
 * 
 * @deprecated
 */
 "clear"(): void
/**
 * 
 * @deprecated
 */
 "addSlotType"(arg0: $ISlotType$Type): void
/**
 * 
 * @deprecated
 */
 "setSlotsForType"(arg0: string, arg1: $LivingEntity$Type, arg2: integer): void
/**
 * 
 * @deprecated
 */
 "unlockSlotType"(arg0: string, arg1: $LivingEntity$Type): void
/**
 * 
 * @deprecated
 */
 "getSlotTypes"(arg0: $LivingEntity$Type): $Collection<($ISlotType)>
/**
 * 
 * @deprecated
 */
 "getSlotTypes"(): $Collection<($ISlotType)>
/**
 * 
 * @deprecated
 */
 "createSlots"(arg0: $LivingEntity$Type): $SortedMap<($ISlotType), ($ICurioStacksHandler)>
/**
 * 
 * @deprecated
 */
 "createSlots"(): $SortedMap<($ISlotType), ($ICurioStacksHandler)>
/**
 * 
 * @deprecated
 */
 "getSlotTypeIds"(): $Set<(string)>
/**
 * 
 * @deprecated
 */
 "shrinkSlotType"(arg0: string, arg1: integer, arg2: $LivingEntity$Type): void
/**
 * 
 * @deprecated
 */
 "shrinkSlotType"(arg0: string, arg1: $LivingEntity$Type): void
/**
 * 
 * @deprecated
 */
 "getSlotsForType"(arg0: $LivingEntity$Type, arg1: string): integer
/**
 * 
 * @deprecated
 */
 "growSlotType"(arg0: string, arg1: integer, arg2: $LivingEntity$Type): void
/**
 * 
 * @deprecated
 */
 "growSlotType"(arg0: string, arg1: $LivingEntity$Type): void
/**
 * 
 * @deprecated
 */
 "lockSlotType"(arg0: string, arg1: $LivingEntity$Type): void
/**
 * 
 * @deprecated
 */
 "getSlotType"(arg0: string): $Optional<($ISlotType)>
}

export namespace $ISlotHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISlotHelper$Type = ($ISlotHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISlotHelper_ = $ISlotHelper$Type;
}}
declare module "packages/top/theillusivec4/curios/common/slottype/$SlotType$Builder" {
import {$SlotType, $SlotType$Type} from "packages/top/theillusivec4/curios/common/slottype/$SlotType"
import {$ICurio$DropRule, $ICurio$DropRule$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio$DropRule"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $SlotType$Builder {

constructor(arg0: string)

public "size"(arg0: integer, arg1: string, arg2: boolean): $SlotType$Builder
public "size"(arg0: integer): $SlotType$Builder
public "size"(arg0: integer, arg1: string): $SlotType$Builder
public "size"(arg0: integer, arg1: boolean): $SlotType$Builder
public "apply"(arg0: $SlotType$Builder$Type): void
public "order"(arg0: integer, arg1: boolean): $SlotType$Builder
public "order"(arg0: integer): $SlotType$Builder
public "build"(): $SlotType
public "validator"(arg0: $ResourceLocation$Type): $SlotType$Builder
public "dropRule"(arg0: $ICurio$DropRule$Type): $SlotType$Builder
public "dropRule"(arg0: string): $SlotType$Builder
public "renderToggle"(arg0: boolean, arg1: boolean): $SlotType$Builder
public "renderToggle"(arg0: boolean): $SlotType$Builder
public "useNativeGui"(arg0: boolean, arg1: boolean): $SlotType$Builder
public "useNativeGui"(arg0: boolean): $SlotType$Builder
public "hasCosmetic"(arg0: boolean, arg1: boolean): $SlotType$Builder
public "hasCosmetic"(arg0: boolean): $SlotType$Builder
public "icon"(arg0: $ResourceLocation$Type): $SlotType$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotType$Builder$Type = ($SlotType$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotType$Builder_ = $SlotType$Builder$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/server/sync/$SPacketSyncModifiers" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ICurioStacksHandler, $ICurioStacksHandler$Type} from "packages/top/theillusivec4/curios/api/type/inventory/$ICurioStacksHandler"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $SPacketSyncModifiers {

constructor(arg0: integer, arg1: $Set$Type<($ICurioStacksHandler$Type)>)
constructor(arg0: $Map$Type<(string), ($CompoundTag$Type)>, arg1: integer)

public static "decode"(arg0: $FriendlyByteBuf$Type): $SPacketSyncModifiers
public static "encode"(arg0: $SPacketSyncModifiers$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $SPacketSyncModifiers$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SPacketSyncModifiers$Type = ($SPacketSyncModifiers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SPacketSyncModifiers_ = $SPacketSyncModifiers$Type;
}}
declare module "packages/top/theillusivec4/curios/common/capability/$ItemizedCurioCapability" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ICurio, $ICurio$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$ICurio$SoundInfo, $ICurio$SoundInfo$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio$SoundInfo"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$EnderMan, $EnderMan$Type} from "packages/net/minecraft/world/entity/monster/$EnderMan"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$ICurio$DropRule, $ICurio$DropRule$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio$DropRule"
import {$ICurioItem, $ICurioItem$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurioItem"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"

export class $ItemizedCurioCapability implements $ICurio {

constructor(arg0: $ICurioItem$Type, arg1: $ItemStack$Type)

public "getStack"(): $ItemStack
public "onEquip"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): void
public "getAttributesTooltip"(arg0: $List$Type<($Component$Type)>): $List<($Component)>
public "makesPiglinsNeutral"(arg0: $SlotContext$Type): boolean
public "canWalkOnPowderedSnow"(arg0: $SlotContext$Type): boolean
public "getAttributeModifiers"(arg0: $SlotContext$Type, arg1: $UUID$Type): $Multimap<($Attribute), ($AttributeModifier)>
public "curioTick"(arg0: $SlotContext$Type): void
public "canUnequip"(arg0: $SlotContext$Type): boolean
public "curioBreak"(arg0: $SlotContext$Type): void
public "getDropRule"(arg0: $SlotContext$Type, arg1: $DamageSource$Type, arg2: integer, arg3: boolean): $ICurio$DropRule
public "canEquipFromUse"(arg0: $SlotContext$Type): boolean
public "onEquipFromUse"(arg0: $SlotContext$Type): void
public "getEquipSound"(arg0: $SlotContext$Type): $ICurio$SoundInfo
public "getSlotsTooltip"(arg0: $List$Type<($Component$Type)>): $List<($Component)>
public "readSyncData"(arg0: $SlotContext$Type, arg1: $CompoundTag$Type): void
public "onUnequip"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): void
public "writeSyncData"(arg0: $SlotContext$Type): $CompoundTag
public "canSync"(arg0: $SlotContext$Type): boolean
public "getLootingLevel"(arg0: $SlotContext$Type, arg1: $DamageSource$Type, arg2: $LivingEntity$Type, arg3: integer): integer
public "canEquip"(arg0: $SlotContext$Type): boolean
public "isEnderMask"(arg0: $SlotContext$Type, arg1: $EnderMan$Type): boolean
public "getFortuneLevel"(arg0: $SlotContext$Type, arg1: $LootContext$Type): integer
/**
 * 
 * @deprecated
 */
public "onEquip"(arg0: string, arg1: integer, arg2: $LivingEntity$Type): void
/**
 * 
 * @deprecated
 */
public "showAttributesTooltip"(arg0: string): boolean
/**
 * 
 * @deprecated
 */
public "playRightClickEquipSound"(arg0: $LivingEntity$Type): void
/**
 * 
 * @deprecated
 */
public "getAttributeModifiers"(arg0: string): $Multimap<($Attribute), ($AttributeModifier)>
/**
 * 
 * @deprecated
 */
public "curioTick"(arg0: string, arg1: integer, arg2: $LivingEntity$Type): void
/**
 * 
 * @deprecated
 */
public "canUnequip"(arg0: string, arg1: $LivingEntity$Type): boolean
/**
 * 
 * @deprecated
 */
public "curioBreak"(arg0: $ItemStack$Type, arg1: $LivingEntity$Type): void
/**
 * 
 * @deprecated
 */
public "getDropRule"(arg0: $LivingEntity$Type): $ICurio$DropRule
/**
 * 
 * @deprecated
 */
public "getFortuneBonus"(arg0: string, arg1: $LivingEntity$Type, arg2: $ItemStack$Type, arg3: integer): integer
/**
 * 
 * @deprecated
 */
public "getLootingBonus"(arg0: string, arg1: $LivingEntity$Type, arg2: $ItemStack$Type, arg3: integer): integer
/**
 * 
 * @deprecated
 */
public "readSyncData"(arg0: $CompoundTag$Type): void
/**
 * 
 * @deprecated
 */
public "onUnequip"(arg0: string, arg1: integer, arg2: $LivingEntity$Type): void
/**
 * 
 * @deprecated
 */
public "canRightClickEquip"(): boolean
/**
 * 
 * @deprecated
 */
public "getTagsTooltip"(arg0: $List$Type<($Component$Type)>): $List<($Component)>
/**
 * 
 * @deprecated
 */
public "curioAnimate"(arg0: string, arg1: integer, arg2: $LivingEntity$Type): void
/**
 * 
 * @deprecated
 */
public "writeSyncData"(): $CompoundTag
/**
 * 
 * @deprecated
 */
public "canSync"(arg0: string, arg1: integer, arg2: $LivingEntity$Type): boolean
/**
 * 
 * @deprecated
 */
public "canEquip"(arg0: string, arg1: $LivingEntity$Type): boolean
public static "playBreakAnimation"(arg0: $ItemStack$Type, arg1: $LivingEntity$Type): void
get "stack"(): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemizedCurioCapability$Type = ($ItemizedCurioCapability);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemizedCurioCapability_ = $ItemizedCurioCapability$Type;
}}
declare module "packages/top/theillusivec4/curios/client/$CuriosClientConfig" {
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$CuriosClientConfig$Client, $CuriosClientConfig$Client$Type} from "packages/top/theillusivec4/curios/client/$CuriosClientConfig$Client"

export class $CuriosClientConfig {
static readonly "CLIENT_SPEC": $ForgeConfigSpec
static readonly "CLIENT": $CuriosClientConfig$Client

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosClientConfig$Type = ($CuriosClientConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosClientConfig_ = $CuriosClientConfig$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/server/$SPacketBreak" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $SPacketBreak {

constructor(arg0: integer, arg1: string, arg2: integer)

public static "decode"(arg0: $FriendlyByteBuf$Type): $SPacketBreak
public static "encode"(arg0: $SPacketBreak$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $SPacketBreak$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SPacketBreak$Type = ($SPacketBreak);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SPacketBreak_ = $SPacketBreak$Type;
}}
declare module "packages/top/theillusivec4/curios/client/$CuriosClientConfig$Client" {
import {$ForgeConfigSpec$BooleanValue, $ForgeConfigSpec$BooleanValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$BooleanValue"
import {$ForgeConfigSpec$EnumValue, $ForgeConfigSpec$EnumValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$EnumValue"
import {$ForgeConfigSpec$IntValue, $ForgeConfigSpec$IntValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$IntValue"
import {$CuriosClientConfig$Client$ButtonCorner, $CuriosClientConfig$Client$ButtonCorner$Type} from "packages/top/theillusivec4/curios/client/$CuriosClientConfig$Client$ButtonCorner"

export class $CuriosClientConfig$Client {
readonly "renderCurios": $ForgeConfigSpec$BooleanValue
readonly "enableButton": $ForgeConfigSpec$BooleanValue
readonly "buttonXOffset": $ForgeConfigSpec$IntValue
readonly "buttonYOffset": $ForgeConfigSpec$IntValue
readonly "creativeButtonXOffset": $ForgeConfigSpec$IntValue
readonly "creativeButtonYOffset": $ForgeConfigSpec$IntValue
readonly "buttonCorner": $ForgeConfigSpec$EnumValue<($CuriosClientConfig$Client$ButtonCorner)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosClientConfig$Client$Type = ($CuriosClientConfig$Client);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosClientConfig$Client_ = $CuriosClientConfig$Client$Type;
}}
declare module "packages/top/theillusivec4/curios/common/integration/jei/$CuriosJeiPlugin" {
import {$IGuiHandlerRegistration, $IGuiHandlerRegistration$Type} from "packages/mezz/jei/api/registration/$IGuiHandlerRegistration"
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
import {$IAdvancedRegistration, $IAdvancedRegistration$Type} from "packages/mezz/jei/api/registration/$IAdvancedRegistration"
import {$IVanillaCategoryExtensionRegistration, $IVanillaCategoryExtensionRegistration$Type} from "packages/mezz/jei/api/registration/$IVanillaCategoryExtensionRegistration"
import {$IRecipeTransferRegistration, $IRecipeTransferRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeTransferRegistration"
import {$IRecipeRegistration, $IRecipeRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IJeiRuntime, $IJeiRuntime$Type} from "packages/mezz/jei/api/runtime/$IJeiRuntime"
import {$IRecipeCatalystRegistration, $IRecipeCatalystRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCatalystRegistration"
import {$IModPlugin, $IModPlugin$Type} from "packages/mezz/jei/api/$IModPlugin"
import {$IRuntimeRegistration, $IRuntimeRegistration$Type} from "packages/mezz/jei/api/registration/$IRuntimeRegistration"
import {$IRecipeCategoryRegistration, $IRecipeCategoryRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCategoryRegistration"
import {$IModIngredientRegistration, $IModIngredientRegistration$Type} from "packages/mezz/jei/api/registration/$IModIngredientRegistration"
import {$ISubtypeRegistration, $ISubtypeRegistration$Type} from "packages/mezz/jei/api/registration/$ISubtypeRegistration"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export class $CuriosJeiPlugin implements $IModPlugin {

constructor()

public "registerGuiHandlers"(arg0: $IGuiHandlerRegistration$Type): void
public "getPluginUid"(): $ResourceLocation
public "registerItemSubtypes"(arg0: $ISubtypeRegistration$Type): void
public "registerVanillaCategoryExtensions"(arg0: $IVanillaCategoryExtensionRegistration$Type): void
public "registerFluidSubtypes"<T>(arg0: $ISubtypeRegistration$Type, arg1: $IPlatformFluidHelper$Type<(T)>): void
public "onConfigManagerAvailable"(arg0: $IJeiConfigManager$Type): void
public "onRuntimeUnavailable"(): void
public "registerIngredients"(arg0: $IModIngredientRegistration$Type): void
public "registerRecipeTransferHandlers"(arg0: $IRecipeTransferRegistration$Type): void
public "registerRecipeCatalysts"(arg0: $IRecipeCatalystRegistration$Type): void
public "registerRecipes"(arg0: $IRecipeRegistration$Type): void
public "registerAdvanced"(arg0: $IAdvancedRegistration$Type): void
public "onRuntimeAvailable"(arg0: $IJeiRuntime$Type): void
public "registerCategories"(arg0: $IRecipeCategoryRegistration$Type): void
public "registerRuntime"(arg0: $IRuntimeRegistration$Type): void
get "pluginUid"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosJeiPlugin$Type = ($CuriosJeiPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosJeiPlugin_ = $CuriosJeiPlugin$Type;
}}
declare module "packages/top/theillusivec4/curios/api/$CuriosCapability" {
import {$ICurio, $ICurio$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio"
import {$ICuriosItemHandler, $ICuriosItemHandler$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICuriosItemHandler"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"

export class $CuriosCapability {
static readonly "INVENTORY": $Capability<($ICuriosItemHandler)>
static readonly "ITEM": $Capability<($ICurio)>
static readonly "ID_INVENTORY": $ResourceLocation
static readonly "ID_ITEM": $ResourceLocation

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosCapability$Type = ($CuriosCapability);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosCapability_ = $CuriosCapability$Type;
}}
declare module "packages/top/theillusivec4/curios/common/network/server/$SPacketScroll" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $SPacketScroll {

constructor(arg0: integer, arg1: integer)

public static "decode"(arg0: $FriendlyByteBuf$Type): $SPacketScroll
public static "encode"(arg0: $SPacketScroll$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $SPacketScroll$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SPacketScroll$Type = ($SPacketScroll);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SPacketScroll_ = $SPacketScroll$Type;
}}
declare module "packages/top/theillusivec4/curios/api/type/capability/$ICuriosItemHandler" {
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Tag, $Tag$Type} from "packages/net/minecraft/nbt/$Tag"
import {$ListTag, $ListTag$Type} from "packages/net/minecraft/nbt/$ListTag"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IItemHandlerModifiable, $IItemHandlerModifiable$Type} from "packages/net/minecraftforge/items/$IItemHandlerModifiable"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"
import {$ICurioStacksHandler, $ICurioStacksHandler$Type} from "packages/top/theillusivec4/curios/api/type/inventory/$ICurioStacksHandler"
import {$Tuple, $Tuple$Type} from "packages/net/minecraft/util/$Tuple"
import {$SlotResult, $SlotResult$Type} from "packages/top/theillusivec4/curios/api/$SlotResult"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $ICuriosItemHandler {

 "getModifiers"(): $Multimap<(string), ($AttributeModifier)>
 "reset"(): void
 "getUpdatingInventories"(): $Set<($ICurioStacksHandler)>
 "addTransientSlotModifier"(arg0: string, arg1: $UUID$Type, arg2: string, arg3: double, arg4: $AttributeModifier$Operation$Type): void
 "addTransientSlotModifiers"(arg0: $Multimap$Type<(string), ($AttributeModifier$Type)>): void
 "addPermanentSlotModifier"(arg0: string, arg1: $UUID$Type, arg2: string, arg3: double, arg4: $AttributeModifier$Operation$Type): void
 "addPermanentSlotModifiers"(arg0: $Multimap$Type<(string), ($AttributeModifier$Type)>): void
 "handleInvalidStacks"(): void
 "clearCachedSlotModifiers"(): void
/**
 * 
 * @deprecated
 */
 "setEnchantmentBonuses"(arg0: $Tuple$Type<(integer), (integer)>): void
 "removeSlotModifiers"(arg0: $Multimap$Type<(string), ($AttributeModifier$Type)>): void
 "getWearer"(): $LivingEntity
 "getVisibleSlots"(): integer
 "readTag"(arg0: $Tag$Type): void
 "getSlots"(): integer
/**
 * 
 * @deprecated
 */
 "getFortuneBonus"(): integer
/**
 * 
 * @deprecated
 */
 "getLootingBonus"(): integer
 "getLootingLevel"(arg0: $DamageSource$Type, arg1: $LivingEntity$Type, arg2: integer): integer
 "getStacksHandler"(arg0: string): $Optional<($ICurioStacksHandler)>
 "getCurios"(): $Map<(string), ($ICurioStacksHandler)>
 "isEquipped"(arg0: $Item$Type): boolean
 "isEquipped"(arg0: $Predicate$Type<($ItemStack$Type)>): boolean
 "clearSlotModifiers"(): void
/**
 * 
 * @deprecated
 */
 "getLockedSlots"(): $Set<(string)>
 "removeSlotModifier"(arg0: string, arg1: $UUID$Type): void
 "saveInventory"(arg0: boolean): $ListTag
/**
 * 
 * @deprecated
 */
 "processSlots"(): void
 "setCurios"(arg0: $Map$Type<(string), ($ICurioStacksHandler$Type)>): void
 "loseInvalidStack"(arg0: $ItemStack$Type): void
 "writeTag"(): $Tag
 "loadInventory"(arg0: $ListTag$Type): void
 "getFortuneLevel"(arg0: $LootContext$Type): integer
/**
 * 
 * @deprecated
 */
 "unlockSlotType"(arg0: string, arg1: integer, arg2: boolean, arg3: boolean): void
/**
 * 
 * @deprecated
 */
 "shrinkSlotType"(arg0: string, arg1: integer): void
/**
 * 
 * @deprecated
 */
 "growSlotType"(arg0: string, arg1: integer): void
/**
 * 
 * @deprecated
 */
 "lockSlotType"(arg0: string): void
 "findFirstCurio"(arg0: $Item$Type): $Optional<($SlotResult)>
 "findFirstCurio"(arg0: $Predicate$Type<($ItemStack$Type)>): $Optional<($SlotResult)>
 "findCurios"(arg0: $Predicate$Type<($ItemStack$Type)>): $List<($SlotResult)>
 "findCurios"(arg0: $Item$Type): $List<($SlotResult)>
 "findCurios"(...arg0: (string)[]): $List<($SlotResult)>
 "setEquippedCurio"(arg0: string, arg1: integer, arg2: $ItemStack$Type): void
 "findCurio"(arg0: string, arg1: integer): $Optional<($SlotResult)>
 "getEquippedCurios"(): $IItemHandlerModifiable
}

export namespace $ICuriosItemHandler {
const LOGGER: $Logger
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICuriosItemHandler$Type = ($ICuriosItemHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICuriosItemHandler_ = $ICuriosItemHandler$Type;
}}
declare module "packages/top/theillusivec4/curios/client/gui/$CuriosScreenV2" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$RecipeBookComponent, $RecipeBookComponent$Type} from "packages/net/minecraft/client/gui/screens/recipebook/$RecipeBookComponent"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$RecipeUpdateListener, $RecipeUpdateListener$Type} from "packages/net/minecraft/client/gui/screens/recipebook/$RecipeUpdateListener"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$ICuriosScreen, $ICuriosScreen$Type} from "packages/top/theillusivec4/curios/api/client/$ICuriosScreen"
import {$EffectRenderingInventoryScreen, $EffectRenderingInventoryScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$EffectRenderingInventoryScreen"
import {$List, $List$Type} from "packages/java/util/$List"
import {$CuriosContainerV2, $CuriosContainerV2$Type} from "packages/top/theillusivec4/curios/common/inventory/container/$CuriosContainerV2"
import {$Tuple, $Tuple$Type} from "packages/net/minecraft/util/$Tuple"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export class $CuriosScreenV2 extends $EffectRenderingInventoryScreen<($CuriosContainerV2)> implements $RecipeUpdateListener, $ICuriosScreen {
 "widthTooNarrow": boolean
 "panelWidth": integer
static readonly "INVENTORY_LOCATION": $ResourceLocation
static readonly "SLOT_ITEM_BLIT_OFFSET": integer
 "imageWidth": integer
 "hoveredSlot": $Slot
 "leftPos": integer
 "topPos": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $CuriosContainerV2$Type, arg1: $Inventory$Type, arg2: $Component$Type)

public static "getButtonOffset"(arg0: boolean): $Tuple<(integer), (integer)>
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "m_7856_"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "updateRenderButtons"(): void
public "m_181908_"(): void
public "getRecipeBookComponent"(): $RecipeBookComponent
public "recipesUpdated"(): void
get "recipeBookComponent"(): $RecipeBookComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosScreenV2$Type = ($CuriosScreenV2);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosScreenV2_ = $CuriosScreenV2$Type;
}}
declare module "packages/top/theillusivec4/curios/mixin/$CuriosImplMixinHooks" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$ICurio, $ICurio$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ICuriosItemHandler, $ICuriosItemHandler$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICuriosItemHandler"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ICurioItem, $ICurioItem$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurioItem"
import {$ISlotType, $ISlotType$Type} from "packages/top/theillusivec4/curios/api/type/$ISlotType"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$SlotResult, $SlotResult$Type} from "packages/top/theillusivec4/curios/api/$SlotResult"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CuriosImplMixinHooks {

constructor()

public static "registerCurio"(arg0: $Item$Type, arg1: $ICurioItem$Type): void
public static "getCurioPredicates"(): $Map<($ResourceLocation), ($Predicate<($SlotResult)>)>
public static "getCurioPredicate"(arg0: $ResourceLocation$Type): $Optional<($Predicate<($SlotResult)>)>
public static "getItemStackSlots"(arg0: $ItemStack$Type, arg1: $LivingEntity$Type): $Map<(string), ($ISlotType)>
public static "getItemStackSlots"(arg0: $ItemStack$Type, arg1: boolean): $Map<(string), ($ISlotType)>
public static "getSlots"(arg0: boolean): $Map<(string), ($ISlotType)>
public static "getAttributeModifiers"(arg0: $SlotContext$Type, arg1: $UUID$Type, arg2: $ItemStack$Type): $Multimap<($Attribute), ($AttributeModifier)>
public static "getEntitySlots"(arg0: $EntityType$Type<(any)>, arg1: boolean): $Map<(string), ($ISlotType)>
public static "getCuriosInventory"(arg0: $LivingEntity$Type): $LazyOptional<($ICuriosItemHandler)>
public static "getCurioFromRegistry"(arg0: $Item$Type): $Optional<($ICurioItem)>
public static "registerCurioPredicate"(arg0: $ResourceLocation$Type, arg1: $Predicate$Type<($SlotResult$Type)>): void
public static "broadcastCurioBreakEvent"(arg0: $SlotContext$Type): void
public static "testCurioPredicates"(arg0: $Set$Type<($ResourceLocation$Type)>, arg1: $SlotResult$Type): boolean
public static "getUuid"(arg0: $SlotContext$Type): $UUID
public static "getCurio"(arg0: $ItemStack$Type): $LazyOptional<($ICurio)>
public static "addModifier"(arg0: $ItemStack$Type, arg1: $Attribute$Type, arg2: string, arg3: $UUID$Type, arg4: double, arg5: $AttributeModifier$Operation$Type, arg6: string): void
public static "isStackValid"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): boolean
public static "addSlotModifier"(arg0: $ItemStack$Type, arg1: string, arg2: string, arg3: $UUID$Type, arg4: double, arg5: $AttributeModifier$Operation$Type, arg6: string): void
public static "addSlotModifier"(arg0: $Multimap$Type<($Attribute$Type), ($AttributeModifier$Type)>, arg1: string, arg2: $UUID$Type, arg3: double, arg4: $AttributeModifier$Operation$Type): void
get "curioPredicates"(): $Map<($ResourceLocation), ($Predicate<($SlotResult)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosImplMixinHooks$Type = ($CuriosImplMixinHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosImplMixinHooks_ = $CuriosImplMixinHooks$Type;
}}
declare module "packages/top/theillusivec4/curios/api/$SlotTypeMessage$Builder" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$SlotTypeMessage, $SlotTypeMessage$Type} from "packages/top/theillusivec4/curios/api/$SlotTypeMessage"

/**
 * 
 * @deprecated
 */
export class $SlotTypeMessage$Builder {

/**
 * 
 * @deprecated
 */
constructor(arg0: string)

/**
 * 
 * @deprecated
 */
public "priority"(arg0: integer): $SlotTypeMessage$Builder
/**
 * 
 * @deprecated
 */
public "lock"(): $SlotTypeMessage$Builder
/**
 * 
 * @deprecated
 */
public "size"(arg0: integer): $SlotTypeMessage$Builder
/**
 * 
 * @deprecated
 */
public "build"(): $SlotTypeMessage
/**
 * 
 * @deprecated
 */
public "hide"(): $SlotTypeMessage$Builder
/**
 * 
 * @deprecated
 */
public "cosmetic"(): $SlotTypeMessage$Builder
/**
 * 
 * @deprecated
 */
public "icon"(arg0: $ResourceLocation$Type): $SlotTypeMessage$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotTypeMessage$Builder$Type = ($SlotTypeMessage$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotTypeMessage$Builder_ = $SlotTypeMessage$Builder$Type;
}}
declare module "packages/top/theillusivec4/curios/api/type/capability/$ICurio$DropRule" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ICurio$DropRule extends $Enum<($ICurio$DropRule)> {
static readonly "DEFAULT": $ICurio$DropRule
static readonly "ALWAYS_DROP": $ICurio$DropRule
static readonly "ALWAYS_KEEP": $ICurio$DropRule
static readonly "DESTROY": $ICurio$DropRule


public static "values"(): ($ICurio$DropRule)[]
public static "valueOf"(arg0: string): $ICurio$DropRule
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICurio$DropRule$Type = (("always_drop") | ("default") | ("always_keep") | ("destroy")) | ($ICurio$DropRule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICurio$DropRule_ = $ICurio$DropRule$Type;
}}
declare module "packages/top/theillusivec4/curios/api/$CuriosTriggers" {
import {$ItemPredicate$Builder, $ItemPredicate$Builder$Type} from "packages/net/minecraft/advancements/critereon/$ItemPredicate$Builder"
import {$CuriosTriggers$EquipBuilder, $CuriosTriggers$EquipBuilder$Type} from "packages/top/theillusivec4/curios/api/$CuriosTriggers$EquipBuilder"
import {$LocationPredicate$Builder, $LocationPredicate$Builder$Type} from "packages/net/minecraft/advancements/critereon/$LocationPredicate$Builder"
import {$CriterionTriggerInstance, $CriterionTriggerInstance$Type} from "packages/net/minecraft/advancements/$CriterionTriggerInstance"

export class $CuriosTriggers {

constructor()

public static "equip"(): $CuriosTriggers$EquipBuilder
/**
 * 
 * @deprecated
 */
public static "equip"(arg0: $ItemPredicate$Builder$Type): $CriterionTriggerInstance
/**
 * 
 * @deprecated
 */
public static "equipAtLocation"(arg0: $ItemPredicate$Builder$Type, arg1: $LocationPredicate$Builder$Type): $CriterionTriggerInstance
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosTriggers$Type = ($CuriosTriggers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosTriggers_ = $CuriosTriggers$Type;
}}
declare module "packages/top/theillusivec4/curios/mixin/$CuriosUtilMixinHooks" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $CuriosUtilMixinHooks {

constructor()

public static "containsTag"(arg0: $Player$Type, arg1: $TagKey$Type<($Item$Type)>): boolean
public static "isFreezeImmune"(arg0: $LivingEntity$Type): boolean
public static "containsStack"(arg0: $Player$Type, arg1: $ItemStack$Type): boolean
public static "mergeCuriosInventory"(arg0: $CompoundTag$Type, arg1: $Entity$Type): $CompoundTag
public static "canWalkOnPowderSnow"(arg0: $LivingEntity$Type): boolean
public static "getFortuneLevel"(arg0: $LootContext$Type): integer
public static "getFortuneLevel"(arg0: $Player$Type): integer
public static "canNeutralizePiglins"(arg0: $LivingEntity$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosUtilMixinHooks$Type = ($CuriosUtilMixinHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosUtilMixinHooks_ = $CuriosUtilMixinHooks$Type;
}}
declare module "packages/top/theillusivec4/curios/api/type/capability/$ICurio" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$ICurio$SoundInfo, $ICurio$SoundInfo$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio$SoundInfo"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$EnderMan, $EnderMan$Type} from "packages/net/minecraft/world/entity/monster/$EnderMan"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$ICurio$DropRule, $ICurio$DropRule$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio$DropRule"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"

export interface $ICurio {

 "getStack"(): $ItemStack
/**
 * 
 * @deprecated
 */
 "onEquip"(arg0: string, arg1: integer, arg2: $LivingEntity$Type): void
 "onEquip"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): void
 "getAttributesTooltip"(arg0: $List$Type<($Component$Type)>): $List<($Component)>
/**
 * 
 * @deprecated
 */
 "showAttributesTooltip"(arg0: string): boolean
/**
 * 
 * @deprecated
 */
 "playRightClickEquipSound"(arg0: $LivingEntity$Type): void
 "makesPiglinsNeutral"(arg0: $SlotContext$Type): boolean
 "canWalkOnPowderedSnow"(arg0: $SlotContext$Type): boolean
/**
 * 
 * @deprecated
 */
 "getAttributeModifiers"(arg0: string): $Multimap<($Attribute), ($AttributeModifier)>
 "getAttributeModifiers"(arg0: $SlotContext$Type, arg1: $UUID$Type): $Multimap<($Attribute), ($AttributeModifier)>
/**
 * 
 * @deprecated
 */
 "curioTick"(arg0: string, arg1: integer, arg2: $LivingEntity$Type): void
 "curioTick"(arg0: $SlotContext$Type): void
/**
 * 
 * @deprecated
 */
 "canUnequip"(arg0: string, arg1: $LivingEntity$Type): boolean
 "canUnequip"(arg0: $SlotContext$Type): boolean
 "curioBreak"(arg0: $SlotContext$Type): void
/**
 * 
 * @deprecated
 */
 "curioBreak"(arg0: $ItemStack$Type, arg1: $LivingEntity$Type): void
/**
 * 
 * @deprecated
 */
 "getDropRule"(arg0: $LivingEntity$Type): $ICurio$DropRule
 "getDropRule"(arg0: $SlotContext$Type, arg1: $DamageSource$Type, arg2: integer, arg3: boolean): $ICurio$DropRule
/**
 * 
 * @deprecated
 */
 "getFortuneBonus"(arg0: string, arg1: $LivingEntity$Type, arg2: $ItemStack$Type, arg3: integer): integer
/**
 * 
 * @deprecated
 */
 "getLootingBonus"(arg0: string, arg1: $LivingEntity$Type, arg2: $ItemStack$Type, arg3: integer): integer
 "canEquipFromUse"(arg0: $SlotContext$Type): boolean
 "onEquipFromUse"(arg0: $SlotContext$Type): void
 "getEquipSound"(arg0: $SlotContext$Type): $ICurio$SoundInfo
 "getSlotsTooltip"(arg0: $List$Type<($Component$Type)>): $List<($Component)>
/**
 * 
 * @deprecated
 */
 "readSyncData"(arg0: $CompoundTag$Type): void
 "readSyncData"(arg0: $SlotContext$Type, arg1: $CompoundTag$Type): void
/**
 * 
 * @deprecated
 */
 "onUnequip"(arg0: string, arg1: integer, arg2: $LivingEntity$Type): void
 "onUnequip"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): void
/**
 * 
 * @deprecated
 */
 "canRightClickEquip"(): boolean
/**
 * 
 * @deprecated
 */
 "getTagsTooltip"(arg0: $List$Type<($Component$Type)>): $List<($Component)>
/**
 * 
 * @deprecated
 */
 "curioAnimate"(arg0: string, arg1: integer, arg2: $LivingEntity$Type): void
/**
 * 
 * @deprecated
 */
 "writeSyncData"(): $CompoundTag
 "writeSyncData"(arg0: $SlotContext$Type): $CompoundTag
 "canSync"(arg0: $SlotContext$Type): boolean
/**
 * 
 * @deprecated
 */
 "canSync"(arg0: string, arg1: integer, arg2: $LivingEntity$Type): boolean
 "getLootingLevel"(arg0: $SlotContext$Type, arg1: $DamageSource$Type, arg2: $LivingEntity$Type, arg3: integer): integer
/**
 * 
 * @deprecated
 */
 "canEquip"(arg0: string, arg1: $LivingEntity$Type): boolean
 "canEquip"(arg0: $SlotContext$Type): boolean
 "isEnderMask"(arg0: $SlotContext$Type, arg1: $EnderMan$Type): boolean
 "getFortuneLevel"(arg0: $SlotContext$Type, arg1: $LootContext$Type): integer

(): $ItemStack
}

export namespace $ICurio {
function playBreakAnimation(arg0: $ItemStack$Type, arg1: $LivingEntity$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICurio$Type = ($ICurio);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICurio_ = $ICurio$Type;
}}
declare module "packages/top/theillusivec4/curios/client/gui/$RenderButton" {
import {$ImageButton, $ImageButton$Type} from "packages/net/minecraft/client/gui/components/$ImageButton"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"
import {$CurioSlot, $CurioSlot$Type} from "packages/top/theillusivec4/curios/common/inventory/$CurioSlot"

export class $RenderButton extends $ImageButton {
readonly "resourceLocation": $ResourceLocation
static readonly "SMALL_WIDTH": integer
static readonly "DEFAULT_WIDTH": integer
static readonly "DEFAULT_HEIGHT": integer
 "onPress": $Button$OnPress
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: $CurioSlot$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $ResourceLocation$Type, arg9: $Button$OnPress$Type)

public "renderButtonOverlay"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "renderWidget"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderButton$Type = ($RenderButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderButton_ = $RenderButton$Type;
}}
declare module "packages/top/theillusivec4/curios/api/event/$CurioChangeEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$LivingEvent, $LivingEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingEvent"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $CurioChangeEvent extends $LivingEvent {

constructor()
constructor(arg0: $LivingEntity$Type, arg1: string, arg2: integer, arg3: $ItemStack$Type, arg4: $ItemStack$Type)

public "getFrom"(): $ItemStack
public "getIdentifier"(): string
public "getSlotIndex"(): integer
public "getTo"(): $ItemStack
public "getListenerList"(): $ListenerList
get "from"(): $ItemStack
get "identifier"(): string
get "slotIndex"(): integer
get "to"(): $ItemStack
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CurioChangeEvent$Type = ($CurioChangeEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CurioChangeEvent_ = $CurioChangeEvent$Type;
}}
declare module "packages/top/theillusivec4/curios/common/util/$EquipCurioTrigger" {
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$EquipCurioTrigger$Instance, $EquipCurioTrigger$Instance$Type} from "packages/top/theillusivec4/curios/common/util/$EquipCurioTrigger$Instance"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$SimpleCriterionTrigger, $SimpleCriterionTrigger$Type} from "packages/net/minecraft/advancements/critereon/$SimpleCriterionTrigger"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$DeserializationContext, $DeserializationContext$Type} from "packages/net/minecraft/advancements/critereon/$DeserializationContext"
import {$ContextAwarePredicate, $ContextAwarePredicate$Type} from "packages/net/minecraft/advancements/critereon/$ContextAwarePredicate"

export class $EquipCurioTrigger extends $SimpleCriterionTrigger<($EquipCurioTrigger$Instance)> {
static readonly "ID": $ResourceLocation
static readonly "INSTANCE": $EquipCurioTrigger


public "trigger"(arg0: $SlotContext$Type, arg1: $ServerPlayer$Type, arg2: $ItemStack$Type, arg3: $ServerLevel$Type, arg4: double, arg5: double, arg6: double): void
public "trigger"(arg0: $ServerPlayer$Type, arg1: $ItemStack$Type, arg2: $ServerLevel$Type, arg3: double, arg4: double, arg5: double): void
public "createInstance"(arg0: $JsonObject$Type, arg1: $ContextAwarePredicate$Type, arg2: $DeserializationContext$Type): $EquipCurioTrigger$Instance
public "getId"(): $ResourceLocation
get "id"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EquipCurioTrigger$Type = ($EquipCurioTrigger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EquipCurioTrigger_ = $EquipCurioTrigger$Type;
}}
declare module "packages/top/theillusivec4/curios/api/$SlotPredicate" {
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$MinMaxBounds$Ints, $MinMaxBounds$Ints$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Ints"

export class $SlotPredicate {
static readonly "ANY": $SlotPredicate

constructor()
constructor(arg0: $Set$Type<(string)>, arg1: $MinMaxBounds$Ints$Type)

public "matches"(arg0: $SlotContext$Type): boolean
public static "fromJson"(arg0: $JsonElement$Type): $SlotPredicate
public "serializeToJson"(): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotPredicate$Type = ($SlotPredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotPredicate_ = $SlotPredicate$Type;
}}
declare module "packages/top/theillusivec4/curios/client/$ClientEventHandler" {
import {$InputEvent$Key, $InputEvent$Key$Type} from "packages/net/minecraftforge/client/event/$InputEvent$Key"
import {$TickEvent$ClientTickEvent, $TickEvent$ClientTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ClientTickEvent"
import {$ItemTooltipEvent, $ItemTooltipEvent$Type} from "packages/net/minecraftforge/event/entity/player/$ItemTooltipEvent"

export class $ClientEventHandler {

constructor()

public "onKeyInput"(arg0: $InputEvent$Key$Type): void
public "onTooltip"(arg0: $ItemTooltipEvent$Type): void
public "onClientTick"(arg0: $TickEvent$ClientTickEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientEventHandler$Type = ($ClientEventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientEventHandler_ = $ClientEventHandler$Type;
}}
declare module "packages/top/theillusivec4/curios/client/gui/$GuiEventHandler" {
import {$ScreenEvent$Init$Post, $ScreenEvent$Init$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$Init$Post"
import {$ScreenEvent$Render$Pre, $ScreenEvent$Render$Pre$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$Render$Pre"
import {$ScreenEvent$MouseButtonPressed$Pre, $ScreenEvent$MouseButtonPressed$Pre$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$MouseButtonPressed$Pre"

export class $GuiEventHandler {

constructor()

public "onMouseClick"(arg0: $ScreenEvent$MouseButtonPressed$Pre$Type): void
public "onInventoryGuiInit"(arg0: $ScreenEvent$Init$Post$Type): void
public "onInventoryGuiDrawBackground"(arg0: $ScreenEvent$Render$Pre$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiEventHandler$Type = ($GuiEventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiEventHandler_ = $GuiEventHandler$Type;
}}
declare module "packages/top/theillusivec4/curios/api/$SlotTypePreset" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$SlotTypeMessage$Builder, $SlotTypeMessage$Builder$Type} from "packages/top/theillusivec4/curios/api/$SlotTypeMessage$Builder"

/**
 * 
 * @deprecated
 */
export class $SlotTypePreset extends $Enum<($SlotTypePreset)> {
static readonly "HEAD": $SlotTypePreset
static readonly "NECKLACE": $SlotTypePreset
static readonly "BACK": $SlotTypePreset
static readonly "BODY": $SlotTypePreset
static readonly "BRACELET": $SlotTypePreset
static readonly "HANDS": $SlotTypePreset
static readonly "RING": $SlotTypePreset
static readonly "BELT": $SlotTypePreset
static readonly "CHARM": $SlotTypePreset
static readonly "CURIO": $SlotTypePreset


public static "values"(): ($SlotTypePreset)[]
public static "valueOf"(arg0: string): $SlotTypePreset
/**
 * 
 * @deprecated
 */
public "getIdentifier"(): string
/**
 * 
 * @deprecated
 */
public static "findPreset"(arg0: string): $Optional<($SlotTypePreset)>
/**
 * 
 * @deprecated
 */
public "getMessageBuilder"(): $SlotTypeMessage$Builder
get "identifier"(): string
get "messageBuilder"(): $SlotTypeMessage$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotTypePreset$Type = (("head") | ("hands") | ("charm") | ("ring") | ("belt") | ("back") | ("necklace") | ("bracelet") | ("body") | ("curio")) | ($SlotTypePreset);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotTypePreset_ = $SlotTypePreset$Type;
}}
declare module "packages/top/theillusivec4/curios/common/inventory/$CosmeticCurioSlot" {
import {$IDynamicStackHandler, $IDynamicStackHandler$Type} from "packages/top/theillusivec4/curios/api/type/inventory/$IDynamicStackHandler"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$CurioSlot, $CurioSlot$Type} from "packages/top/theillusivec4/curios/common/inventory/$CurioSlot"

export class $CosmeticCurioSlot extends $CurioSlot {
readonly "container": $Container
 "index": integer
 "x": integer
 "y": integer

constructor(arg0: $Player$Type, arg1: $IDynamicStackHandler$Type, arg2: integer, arg3: string, arg4: integer, arg5: integer)

public "getRenderStatus"(): boolean
public "getSlotName"(): string
get "renderStatus"(): boolean
get "slotName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CosmeticCurioSlot$Type = ($CosmeticCurioSlot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CosmeticCurioSlot_ = $CosmeticCurioSlot$Type;
}}
declare module "packages/top/theillusivec4/curios/common/inventory/container/$CuriosContainer" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$RecipeBookType, $RecipeBookType$Type} from "packages/net/minecraft/world/inventory/$RecipeBookType"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ICuriosItemHandler, $ICuriosItemHandler$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICuriosItemHandler"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$InventoryMenu, $InventoryMenu$Type} from "packages/net/minecraft/world/inventory/$InventoryMenu"
import {$ICuriosMenu, $ICuriosMenu$Type} from "packages/top/theillusivec4/curios/api/type/$ICuriosMenu"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"
import {$StackedContents, $StackedContents$Type} from "packages/net/minecraft/world/entity/player/$StackedContents"

export class $CuriosContainer extends $InventoryMenu implements $ICuriosMenu {
readonly "curiosHandler": $LazyOptional<($ICuriosItemHandler)>
readonly "player": $Player
static readonly "CONTAINER_ID": integer
static readonly "RESULT_SLOT": integer
static readonly "CRAFT_SLOT_START": integer
static readonly "CRAFT_SLOT_END": integer
static readonly "ARMOR_SLOT_START": integer
static readonly "ARMOR_SLOT_END": integer
static readonly "INV_SLOT_START": integer
static readonly "INV_SLOT_END": integer
static readonly "USE_ROW_SLOT_START": integer
static readonly "USE_ROW_SLOT_END": integer
static readonly "SHIELD_SLOT": integer
static readonly "BLOCK_ATLAS": $ResourceLocation
static readonly "EMPTY_ARMOR_SLOT_HELMET": $ResourceLocation
static readonly "EMPTY_ARMOR_SLOT_CHESTPLATE": $ResourceLocation
static readonly "EMPTY_ARMOR_SLOT_LEGGINGS": $ResourceLocation
static readonly "EMPTY_ARMOR_SLOT_BOOTS": $ResourceLocation
static readonly "EMPTY_ARMOR_SLOT_SHIELD": $ResourceLocation
readonly "active": boolean
readonly "owner": $Player
static readonly "SLOT_CLICKED_OUTSIDE": integer
static readonly "QUICKCRAFT_TYPE_CHARITABLE": integer
static readonly "QUICKCRAFT_TYPE_GREEDY": integer
static readonly "QUICKCRAFT_TYPE_CLONE": integer
static readonly "QUICKCRAFT_HEADER_START": integer
static readonly "QUICKCRAFT_HEADER_CONTINUE": integer
static readonly "QUICKCRAFT_HEADER_END": integer
static readonly "CARRIED_SLOT_SIZE": integer
readonly "lastSlots": $NonNullList<($ItemStack)>
readonly "slots": $NonNullList<($Slot)>
 "remoteSlots": $NonNullList<($ItemStack)>
 "containerId": integer

constructor(arg0: integer, arg1: $Inventory$Type, arg2: boolean)
constructor(arg0: integer, arg1: $Inventory$Type)
constructor(arg0: integer, arg1: $Inventory$Type, arg2: $FriendlyByteBuf$Type)

public "resetSlots"(): void
public "scrollToIndex"(arg0: integer): void
public "hasCosmeticColumn"(): boolean
public "stillValid"(arg0: $Player$Type): boolean
public "removed"(arg0: $Player$Type): void
public "scrollTo"(arg0: float): void
public "canScroll"(): boolean
public "recipeMatches"(arg0: $Recipe$Type<(any)>): boolean
public "clearCraftingContent"(): void
public "fillCraftSlotsStackedContents"(arg0: $StackedContents$Type): void
public "getGridHeight"(): integer
public "getRecipeBookType"(): $RecipeBookType
public "getGridWidth"(): integer
public "quickMoveStack"(arg0: $Player$Type, arg1: integer): $ItemStack
public "slotsChanged"(arg0: $Container$Type): void
public "setItem"(arg0: integer, arg1: integer, arg2: $ItemStack$Type): void
get "gridHeight"(): integer
get "recipeBookType"(): $RecipeBookType
get "gridWidth"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosContainer$Type = ($CuriosContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosContainer_ = $CuriosContainer$Type;
}}
declare module "packages/top/theillusivec4/curios/common/inventory/container/$CuriosContainerV2" {
import {$RecipeBookType, $RecipeBookType$Type} from "packages/net/minecraft/world/inventory/$RecipeBookType"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ICuriosItemHandler, $ICuriosItemHandler$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICuriosItemHandler"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$List, $List$Type} from "packages/java/util/$List"
import {$CuriosContainer, $CuriosContainer$Type} from "packages/top/theillusivec4/curios/common/inventory/container/$CuriosContainer"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"
import {$StackedContents, $StackedContents$Type} from "packages/net/minecraft/world/entity/player/$StackedContents"

export class $CuriosContainerV2 extends $CuriosContainer {
readonly "curiosHandler": $ICuriosItemHandler
readonly "player": $Player
 "currentPage": integer
 "totalPages": integer
 "grid": $List<(integer)>
 "hasCosmetics": boolean
 "isViewingCosmetics": boolean
 "panelWidth": integer
static readonly "CONTAINER_ID": integer
static readonly "RESULT_SLOT": integer
static readonly "CRAFT_SLOT_START": integer
static readonly "CRAFT_SLOT_END": integer
static readonly "ARMOR_SLOT_START": integer
static readonly "ARMOR_SLOT_END": integer
static readonly "INV_SLOT_START": integer
static readonly "INV_SLOT_END": integer
static readonly "USE_ROW_SLOT_START": integer
static readonly "USE_ROW_SLOT_END": integer
static readonly "SHIELD_SLOT": integer
static readonly "BLOCK_ATLAS": $ResourceLocation
static readonly "EMPTY_ARMOR_SLOT_HELMET": $ResourceLocation
static readonly "EMPTY_ARMOR_SLOT_CHESTPLATE": $ResourceLocation
static readonly "EMPTY_ARMOR_SLOT_LEGGINGS": $ResourceLocation
static readonly "EMPTY_ARMOR_SLOT_BOOTS": $ResourceLocation
static readonly "EMPTY_ARMOR_SLOT_SHIELD": $ResourceLocation
readonly "active": boolean
readonly "owner": $Player
static readonly "SLOT_CLICKED_OUTSIDE": integer
static readonly "QUICKCRAFT_TYPE_CHARITABLE": integer
static readonly "QUICKCRAFT_TYPE_GREEDY": integer
static readonly "QUICKCRAFT_TYPE_CLONE": integer
static readonly "QUICKCRAFT_HEADER_START": integer
static readonly "QUICKCRAFT_HEADER_CONTINUE": integer
static readonly "QUICKCRAFT_HEADER_END": integer
static readonly "CARRIED_SLOT_SIZE": integer
readonly "lastSlots": $NonNullList<($ItemStack)>
readonly "slots": $NonNullList<($Slot)>
 "remoteSlots": $NonNullList<($ItemStack)>
 "containerId": integer

constructor(arg0: integer, arg1: $Inventory$Type, arg2: $FriendlyByteBuf$Type)
constructor(arg0: integer, arg1: $Inventory$Type)

public "resetSlots"(): void
public "stillValid"(arg0: $Player$Type): boolean
public "removed"(arg0: $Player$Type): void
public "setPage"(arg0: integer): void
public "toggleCosmetics"(): void
public "prevPage"(): void
public "checkQuickMove"(): void
public "nextPage"(): void
public "recipeMatches"(arg0: $Recipe$Type<(any)>): boolean
public "clearCraftingContent"(): void
public "fillCraftSlotsStackedContents"(arg0: $StackedContents$Type): void
public "getGridHeight"(): integer
public "getSize"(): integer
public "getResultSlotIndex"(): integer
public "getRecipeBookType"(): $RecipeBookType
public "shouldMoveToInventory"(arg0: integer): boolean
public "getGridWidth"(): integer
public "quickMoveStack"(arg0: $Player$Type, arg1: integer): $ItemStack
public "slotsChanged"(arg0: $Container$Type): void
public "setItem"(arg0: integer, arg1: integer, arg2: $ItemStack$Type): void
set "page"(value: integer)
get "gridHeight"(): integer
get "size"(): integer
get "resultSlotIndex"(): integer
get "recipeBookType"(): $RecipeBookType
get "gridWidth"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosContainerV2$Type = ($CuriosContainerV2);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosContainerV2_ = $CuriosContainerV2$Type;
}}
declare module "packages/top/theillusivec4/curios/api/type/util/$ICuriosHelper" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$ICurio, $ICurio$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICurio"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$TriConsumer, $TriConsumer$Type} from "packages/org/apache/logging/log4j/util/$TriConsumer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IItemHandlerModifiable, $IItemHandlerModifiable$Type} from "packages/net/minecraftforge/items/$IItemHandlerModifiable"
import {$ICuriosItemHandler, $ICuriosItemHandler$Type} from "packages/top/theillusivec4/curios/api/type/capability/$ICuriosItemHandler"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$SlotResult, $SlotResult$Type} from "packages/top/theillusivec4/curios/api/$SlotResult"
import {$ImmutableTriple, $ImmutableTriple$Type} from "packages/org/apache/commons/lang3/tuple/$ImmutableTriple"

/**
 * 
 * @deprecated
 */
export interface $ICuriosHelper {

/**
 * 
 * @deprecated
 */
 "setBrokenCurioConsumer"(arg0: $TriConsumer$Type<(string), (integer), ($LivingEntity$Type)>): void
/**
 * 
 * @deprecated
 */
 "setBrokenCurioConsumer"(arg0: $Consumer$Type<($SlotContext$Type)>): void
/**
 * 
 * @deprecated
 */
 "getAttributeModifiers"(arg0: string, arg1: $ItemStack$Type): $Multimap<($Attribute), ($AttributeModifier)>
/**
 * 
 * @deprecated
 */
 "getAttributeModifiers"(arg0: $SlotContext$Type, arg1: $UUID$Type, arg2: $ItemStack$Type): $Multimap<($Attribute), ($AttributeModifier)>
/**
 * 
 * @deprecated
 */
 "getCurioTags"(arg0: $Item$Type): $Set<(string)>
/**
 * 
 * @deprecated
 */
 "getCurio"(arg0: $ItemStack$Type): $LazyOptional<($ICurio)>
/**
 * 
 * @deprecated
 */
 "getCuriosHandler"(arg0: $LivingEntity$Type): $LazyOptional<($ICuriosItemHandler)>
/**
 * 
 * @deprecated
 */
 "findEquippedCurio"(arg0: $Item$Type, arg1: $LivingEntity$Type): $Optional<($ImmutableTriple<(string), (integer), ($ItemStack)>)>
/**
 * 
 * @deprecated
 */
 "findEquippedCurio"(arg0: $Predicate$Type<($ItemStack$Type)>, arg1: $LivingEntity$Type): $Optional<($ImmutableTriple<(string), (integer), ($ItemStack)>)>
/**
 * 
 * @deprecated
 */
 "addModifier"(arg0: $ItemStack$Type, arg1: $Attribute$Type, arg2: string, arg3: $UUID$Type, arg4: double, arg5: $AttributeModifier$Operation$Type, arg6: string): void
/**
 * 
 * @deprecated
 */
 "findFirstCurio"(arg0: $LivingEntity$Type, arg1: $Predicate$Type<($ItemStack$Type)>): $Optional<($SlotResult)>
/**
 * 
 * @deprecated
 */
 "findFirstCurio"(arg0: $LivingEntity$Type, arg1: $Item$Type): $Optional<($SlotResult)>
/**
 * 
 * @deprecated
 */
 "onBrokenCurio"(arg0: $SlotContext$Type): void
/**
 * 
 * @deprecated
 */
 "onBrokenCurio"(arg0: string, arg1: integer, arg2: $LivingEntity$Type): void
/**
 * 
 * @deprecated
 */
 "findCurios"(arg0: $LivingEntity$Type, arg1: $Predicate$Type<($ItemStack$Type)>): $List<($SlotResult)>
/**
 * 
 * @deprecated
 */
 "findCurios"(arg0: $LivingEntity$Type, arg1: $Item$Type): $List<($SlotResult)>
/**
 * 
 * @deprecated
 */
 "findCurios"(arg0: $LivingEntity$Type, ...arg1: (string)[]): $List<($SlotResult)>
/**
 * 
 * @deprecated
 */
 "setEquippedCurio"(arg0: $LivingEntity$Type, arg1: string, arg2: integer, arg3: $ItemStack$Type): void
/**
 * 
 * @deprecated
 */
 "findCurio"(arg0: $LivingEntity$Type, arg1: string, arg2: integer): $Optional<($SlotResult)>
/**
 * 
 * @deprecated
 */
 "isStackValid"(arg0: $SlotContext$Type, arg1: $ItemStack$Type): boolean
/**
 * 
 * @deprecated
 */
 "addSlotModifier"(arg0: $ItemStack$Type, arg1: string, arg2: string, arg3: $UUID$Type, arg4: double, arg5: $AttributeModifier$Operation$Type, arg6: string): void
/**
 * 
 * @deprecated
 */
 "addSlotModifier"(arg0: $Multimap$Type<($Attribute$Type), ($AttributeModifier$Type)>, arg1: string, arg2: $UUID$Type, arg3: double, arg4: $AttributeModifier$Operation$Type): void
/**
 * 
 * @deprecated
 */
 "getEquippedCurios"(arg0: $LivingEntity$Type): $LazyOptional<($IItemHandlerModifiable)>
}

export namespace $ICuriosHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICuriosHelper$Type = ($ICuriosHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICuriosHelper_ = $ICuriosHelper$Type;
}}
declare module "packages/top/theillusivec4/curios/client/render/$CuriosLayer" {
import {$RenderLayer, $RenderLayer$Type} from "packages/net/minecraft/client/renderer/entity/layers/$RenderLayer"
import {$RenderLayerParent, $RenderLayerParent$Type} from "packages/net/minecraft/client/renderer/entity/$RenderLayerParent"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$EntityModel, $EntityModel$Type} from "packages/net/minecraft/client/model/$EntityModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $CuriosLayer<T extends $LivingEntity, M extends $EntityModel<(T)>> extends $RenderLayer<(T), (M)> {

constructor(arg0: $RenderLayerParent$Type<(T), (M)>)

public "render"(arg0: $PoseStack$Type, arg1: $MultiBufferSource$Type, arg2: integer, arg3: T, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosLayer$Type<T, M> = ($CuriosLayer<(T), (M)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosLayer_<T, M> = $CuriosLayer$Type<(T), (M)>;
}}
declare module "packages/top/theillusivec4/curios/client/gui/$CuriosScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$RecipeBookComponent, $RecipeBookComponent$Type} from "packages/net/minecraft/client/gui/screens/recipebook/$RecipeBookComponent"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$RecipeUpdateListener, $RecipeUpdateListener$Type} from "packages/net/minecraft/client/gui/screens/recipebook/$RecipeUpdateListener"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$ICuriosScreen, $ICuriosScreen$Type} from "packages/top/theillusivec4/curios/api/client/$ICuriosScreen"
import {$EffectRenderingInventoryScreen, $EffectRenderingInventoryScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$EffectRenderingInventoryScreen"
import {$List, $List$Type} from "packages/java/util/$List"
import {$CuriosContainer, $CuriosContainer$Type} from "packages/top/theillusivec4/curios/common/inventory/container/$CuriosContainer"
import {$Tuple, $Tuple$Type} from "packages/net/minecraft/util/$Tuple"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export class $CuriosScreen extends $EffectRenderingInventoryScreen<($CuriosContainer)> implements $RecipeUpdateListener, $ICuriosScreen {
 "hasScrollBar": boolean
 "widthTooNarrow": boolean
static readonly "INVENTORY_LOCATION": $ResourceLocation
static readonly "SLOT_ITEM_BLIT_OFFSET": integer
 "imageWidth": integer
 "hoveredSlot": $Slot
 "leftPos": integer
 "topPos": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $CuriosContainer$Type, arg1: $Inventory$Type, arg2: $Component$Type)

public static "getButtonOffset"(arg0: boolean): $Tuple<(integer), (integer)>
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "m_7856_"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "updateRenderButtons"(): void
public "m_181908_"(): void
public "getRecipeBookComponent"(): $RecipeBookComponent
public "recipesUpdated"(): void
get "recipeBookComponent"(): $RecipeBookComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosScreen$Type = ($CuriosScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosScreen_ = $CuriosScreen$Type;
}}
declare module "packages/top/theillusivec4/curios/server/$SlotHelper" {
import {$ISlotHelper, $ISlotHelper$Type} from "packages/top/theillusivec4/curios/api/type/util/$ISlotHelper"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ISlotType, $ISlotType$Type} from "packages/top/theillusivec4/curios/api/type/$ISlotType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$SortedMap, $SortedMap$Type} from "packages/java/util/$SortedMap"
import {$ICurioStacksHandler, $ICurioStacksHandler$Type} from "packages/top/theillusivec4/curios/api/type/inventory/$ICurioStacksHandler"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $SlotHelper implements $ISlotHelper {

constructor()

public "clear"(): void
public "addSlotType"(arg0: $ISlotType$Type): void
public "setSlotsForType"(arg0: string, arg1: $LivingEntity$Type, arg2: integer): void
public "unlockSlotType"(arg0: string, arg1: $LivingEntity$Type): void
public "getSlotTypes"(arg0: $LivingEntity$Type): $Collection<($ISlotType)>
public "getSlotTypes"(): $Collection<($ISlotType)>
public "createSlots"(): $SortedMap<($ISlotType), ($ICurioStacksHandler)>
public "createSlots"(arg0: $LivingEntity$Type): $SortedMap<($ISlotType), ($ICurioStacksHandler)>
public "getSlotTypeIds"(): $Set<(string)>
public "shrinkSlotType"(arg0: string, arg1: $LivingEntity$Type): void
public "shrinkSlotType"(arg0: string, arg1: integer, arg2: $LivingEntity$Type): void
public "getSlotsForType"(arg0: $LivingEntity$Type, arg1: string): integer
public "growSlotType"(arg0: string, arg1: integer, arg2: $LivingEntity$Type): void
public "growSlotType"(arg0: string, arg1: $LivingEntity$Type): void
public "lockSlotType"(arg0: string, arg1: $LivingEntity$Type): void
public "getSlotType"(arg0: string): $Optional<($ISlotType)>
get "slotTypes"(): $Collection<($ISlotType)>
get "slotTypeIds"(): $Set<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotHelper$Type = ($SlotHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotHelper_ = $SlotHelper$Type;
}}
declare module "packages/top/theillusivec4/caelus/$Caelus" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Caelus {
static readonly "MOD_ID": string

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Caelus$Type = ($Caelus);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Caelus_ = $Caelus$Type;
}}
