declare module "packages/harmonised/pmmo/config/readers/$ConfigHelper" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"
import {$ConfigHelper$ConfigObject, $ConfigHelper$ConfigObject$Type} from "packages/harmonised/pmmo/config/readers/$ConfigHelper$ConfigObject"
import {$ModConfig$Type, $ModConfig$Type$Type} from "packages/net/minecraftforge/fml/config/$ModConfig$Type"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $ConfigHelper {


public static "register"<T>(arg0: $ModConfig$Type$Type, arg1: $Function$Type<($ForgeConfigSpec$Builder$Type), (T)>, arg2: string): T
public static "register"<T>(arg0: $ModConfig$Type$Type, arg1: $Function$Type<($ForgeConfigSpec$Builder$Type), (T)>): T
public static "defineObject"<T>(arg0: $ForgeConfigSpec$Builder$Type, arg1: string, arg2: $Codec$Type<(T)>, arg3: T): $ConfigHelper$ConfigObject<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigHelper$Type = ($ConfigHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigHelper_ = $ConfigHelper$Type;
}}
declare module "packages/harmonised/pmmo/features/loot_modifiers/$SkillLootConditionPlayer" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LootItemCondition, $LootItemCondition$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemCondition"

export class $SkillLootConditionPlayer implements $LootItemCondition {

constructor(arg0: integer, arg1: integer, arg2: string)

public "test"(arg0: $LootContext$Type): boolean
public "getType"(): $LootItemConditionType
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SkillLootConditionPlayer$Type = ($SkillLootConditionPlayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SkillLootConditionPlayer_ = $SkillLootConditionPlayer$Type;
}}
declare module "packages/harmonised/pmmo/util/$TagUtils" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $TagUtils {

constructor()

public static "getFloat"(arg0: $CompoundTag$Type, arg1: string, arg2: float): float
public static "stateTag"(arg0: $BlockState$Type): $CompoundTag
public static "mergeTags"(arg0: $CompoundTag$Type, arg1: $CompoundTag$Type): $CompoundTag
public static "entityTag"(arg0: $Entity$Type): $CompoundTag
public static "tileTag"(arg0: $BlockEntity$Type): $CompoundTag
public static "getBlockPos"(arg0: $CompoundTag$Type, arg1: string, arg2: $BlockPos$Type): $BlockPos
public static "stackTag"(arg0: $ItemStack$Type): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TagUtils$Type = ($TagUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TagUtils_ = $TagUtils$Type;
}}
declare module "packages/harmonised/pmmo/core/nbt/$BehaviorToPrevious" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$IExtensibleEnum, $IExtensibleEnum$Type} from "packages/net/minecraftforge/common/$IExtensibleEnum"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $BehaviorToPrevious extends $Enum<($BehaviorToPrevious)> implements $StringRepresentable, $IExtensibleEnum {
static readonly "ADD_TO": $BehaviorToPrevious
static readonly "SUB_FROM": $BehaviorToPrevious
static readonly "HIGHEST": $BehaviorToPrevious
static readonly "REPLACE": $BehaviorToPrevious
static readonly "CODEC": $Codec<($BehaviorToPrevious)>


public static "values"(): ($BehaviorToPrevious)[]
public static "valueOf"(arg0: string): $BehaviorToPrevious
public static "create"(arg0: string): $BehaviorToPrevious
public "getSerializedName"(): string
public static "byName"(arg0: string): $BehaviorToPrevious
public static "fromEnumWithMapping"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
public static "keys"(arg0: ($StringRepresentable$Type)[]): $Keyable
public static "fromEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
/**
 * 
 * @deprecated
 */
public "init"(): void
public static "createCodecForExtensibleEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(any), (any)>): $Codec<(E)>
get "serializedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BehaviorToPrevious$Type = (("highest") | ("replace") | ("sub_from") | ("add_to")) | ($BehaviorToPrevious);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BehaviorToPrevious_ = $BehaviorToPrevious$Type;
}}
declare module "packages/harmonised/pmmo/client/gui/component/$SelectionWidget" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$SelectionWidget$SelectionEntry, $SelectionWidget$SelectionEntry$Type} from "packages/harmonised/pmmo/client/gui/component/$SelectionWidget$SelectionEntry"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $SelectionWidget<T extends $SelectionWidget$SelectionEntry<(any)>> extends $AbstractWidget {
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: $Component$Type, arg4: $Consumer$Type<(T)>)

public "stream"(): $Stream<(T)>
public "setEntries"(arg0: $Collection$Type<(T)>): void
public "getSelected"(): T
public "isMouseOver"(arg0: double, arg1: double): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "setSelected"(arg0: T, arg1: boolean): void
public "m_87963_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "getHeight"(): integer
public "isExtended"(): boolean
set "entries"(value: $Collection$Type<(T)>)
get "selected"(): T
get "height"(): integer
get "extended"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SelectionWidget$Type<T> = ($SelectionWidget<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SelectionWidget_<T> = $SelectionWidget$Type<(T)>;
}}
declare module "packages/harmonised/pmmo/network/serverpackets/$SP_SetVeinLimit" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $SP_SetVeinLimit {

constructor(arg0: integer)
constructor(arg0: $FriendlyByteBuf$Type)

public "encode"(arg0: $FriendlyByteBuf$Type): void
public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SP_SetVeinLimit$Type = ($SP_SetVeinLimit);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SP_SetVeinLimit_ = $SP_SetVeinLimit$Type;
}}
declare module "packages/harmonised/pmmo/compat/ftb_quests/$FTBQHandler" {
import {$TaskType, $TaskType$Type} from "packages/dev/ftb/mods/ftbquests/quest/task/$TaskType"
import {$RewardType, $RewardType$Type} from "packages/dev/ftb/mods/ftbquests/quest/reward/$RewardType"

export class $FTBQHandler {
static "SKILL": $TaskType
static "XP_REWARD": $RewardType
static "LEVEL_REWARD": $RewardType

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FTBQHandler$Type = ($FTBQHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FTBQHandler_ = $FTBQHandler$Type;
}}
declare module "packages/harmonised/pmmo/network/clientpackets/$CP_ResetXP" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $CP_ResetXP {

constructor()

public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CP_ResetXP$Type = ($CP_ResetXP);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CP_ResetXP_ = $CP_ResetXP$Type;
}}
declare module "packages/harmonised/pmmo/features/autovalues/$AutoValues" {
import {$ReqType, $ReqType$Type} from "packages/harmonised/pmmo/api/enums/$ReqType"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$ObjectType, $ObjectType$Type} from "packages/harmonised/pmmo/api/enums/$ObjectType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AutoValues {

constructor()

public static "getExperienceAward"(arg0: $EventType$Type, arg1: $ResourceLocation$Type, arg2: $ObjectType$Type): $Map<(string), (long)>
public static "resetCache"(): void
public static "getRequirements"(arg0: $ReqType$Type, arg1: $ResourceLocation$Type, arg2: $ObjectType$Type): $Map<(string), (integer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoValues$Type = ($AutoValues);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoValues_ = $AutoValues$Type;
}}
declare module "packages/harmonised/pmmo/features/anticheese/$CheeseTracker" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$TickEvent$ServerTickEvent, $TickEvent$ServerTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ServerTickEvent"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CheeseTracker {

constructor()

public static "applyAntiCheese"(arg0: $EventType$Type, arg1: $ResourceLocation$Type, arg2: $Player$Type, arg3: $Map$Type<(string), (long)>): void
public static "playerWatcher"(arg0: $TickEvent$ServerTickEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CheeseTracker$Type = ($CheeseTracker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CheeseTracker_ = $CheeseTracker$Type;
}}
declare module "packages/harmonised/pmmo/network/clientpackets/$CP_ClearData" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $CP_ClearData {

constructor()

public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CP_ClearData$Type = ($CP_ClearData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CP_ClearData_ = $CP_ClearData$Type;
}}
declare module "packages/harmonised/pmmo/util/$TagBuilder" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Tag, $Tag$Type} from "packages/net/minecraft/nbt/$Tag"
import {$ListTag, $ListTag$Type} from "packages/net/minecraft/nbt/$ListTag"

export class $TagBuilder {


public static "start"(): $TagBuilder
public "build"(): $CompoundTag
public "withDouble"(arg0: string, arg1: double): $TagBuilder
public "withString"(arg0: string, arg1: string): $TagBuilder
public "withInt"(arg0: string, arg1: integer): $TagBuilder
public "withList"(arg0: string, ...arg1: ($Tag$Type)[]): $TagBuilder
public "withList"(arg0: string, arg1: $ListTag$Type): $TagBuilder
public "withBool"(arg0: string, arg1: boolean): $TagBuilder
public "withFloat"(arg0: string, arg1: float): $TagBuilder
public "withLong"(arg0: string, arg1: long): $TagBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TagBuilder$Type = ($TagBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TagBuilder_ = $TagBuilder$Type;
}}
declare module "packages/harmonised/pmmo/network/serverpackets/$SP_SetVeinShape" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$VeinShapeData$ShapeType, $VeinShapeData$ShapeType$Type} from "packages/harmonised/pmmo/features/veinmining/$VeinShapeData$ShapeType"

export class $SP_SetVeinShape {

constructor(arg0: $VeinShapeData$ShapeType$Type)
constructor(arg0: $FriendlyByteBuf$Type)

public "encode"(arg0: $FriendlyByteBuf$Type): void
public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SP_SetVeinShape$Type = ($SP_SetVeinShape);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SP_SetVeinShape_ = $SP_SetVeinShape$Type;
}}
declare module "packages/harmonised/pmmo/client/gui/component/$PlayerStatsComponent" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$NarratableEntry$NarrationPriority, $NarratableEntry$NarrationPriority$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry$NarrationPriority"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $PlayerStatsComponent extends $AbstractWidget {
static readonly "IMAGE_WIDTH": integer
static readonly "IMAGE_HEIGHT": integer
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor()

public "initVisuals"(): void
public "init"(arg0: integer, arg1: integer, arg2: $Minecraft$Type, arg3: boolean): void
public "tick"(): void
public "narrationPriority"(): $NarratableEntry$NarrationPriority
public "isVisible"(): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "updateScreenPosition"(arg0: integer, arg1: integer): integer
public "m_87963_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "toggleVisibility"(): void
get "visible"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerStatsComponent$Type = ($PlayerStatsComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerStatsComponent_ = $PlayerStatsComponent$Type;
}}
declare module "packages/harmonised/pmmo/client/utils/$ClientUtils" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ClientTooltipComponent, $ClientTooltipComponent$Type} from "packages/net/minecraft/client/gui/screens/inventory/tooltip/$ClientTooltipComponent"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export class $ClientUtils {

constructor()

public static "invalidateUnlocksCache"(): void
public static "ctc"(arg0: $MutableComponent$Type, arg1: integer): $List<($ClientTooltipComponent)>
public static "sendLevelUpUnlocks"(arg0: string, arg1: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientUtils$Type = ($ClientUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientUtils_ = $ClientUtils$Type;
}}
declare module "packages/harmonised/pmmo/client/gui/$VeinRenderer" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $VeinRenderer {

constructor()

public static "drawBoxHighlights"(arg0: $PoseStack$Type, arg1: $Set$Type<($BlockPos$Type)>): void
public static "drawBoxHighlight"(arg0: $PoseStack$Type, arg1: $VertexConsumer$Type, arg2: $BlockPos$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VeinRenderer$Type = ($VeinRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VeinRenderer_ = $VeinRenderer$Type;
}}
declare module "packages/harmonised/pmmo/core/nbt/$Operator" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$IExtensibleEnum, $IExtensibleEnum$Type} from "packages/net/minecraftforge/common/$IExtensibleEnum"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $Operator extends $Enum<($Operator)> implements $StringRepresentable, $IExtensibleEnum {
static readonly "EQUALS": $Operator
static readonly "GREATER_THAN": $Operator
static readonly "LESS_THAN": $Operator
static readonly "GREATER_THAN_OR_EQUAL": $Operator
static readonly "LESS_THAN_OR_EQUAL": $Operator
static readonly "EXISTS": $Operator
static readonly "CODEC": $Codec<($Operator)>


public static "values"(): ($Operator)[]
public static "valueOf"(arg0: string): $Operator
public static "create"(arg0: string): $Operator
public "getSerializedName"(): string
public static "byName"(arg0: string): $Operator
public static "fromEnumWithMapping"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
public static "keys"(arg0: ($StringRepresentable$Type)[]): $Keyable
public static "fromEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
/**
 * 
 * @deprecated
 */
public "init"(): void
public static "createCodecForExtensibleEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(any), (any)>): $Codec<(E)>
get "serializedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Operator$Type = (("greater_than_or_equal") | ("less_than_or_equal") | ("greater_than") | ("equals") | ("less_than") | ("exists")) | ($Operator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Operator_ = $Operator$Type;
}}
declare module "packages/harmonised/pmmo/features/loot_modifiers/$GLMRegistry" {
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$RareDropModifier, $RareDropModifier$Type} from "packages/harmonised/pmmo/features/loot_modifiers/$RareDropModifier"
import {$TreasureLootModifier, $TreasureLootModifier$Type} from "packages/harmonised/pmmo/features/loot_modifiers/$TreasureLootModifier"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $GLMRegistry {
static readonly "GLM": $DeferredRegister<($Codec<(any)>)>
static readonly "CONDITIONS": $DeferredRegister<($LootItemConditionType)>
static readonly "TREASURE": $RegistryObject<($Codec<($TreasureLootModifier)>)>
static readonly "RARE_DROP": $RegistryObject<($Codec<($RareDropModifier)>)>
static readonly "SKILL_PLAYER": $RegistryObject<($LootItemConditionType)>
static readonly "SKILL_KILL": $RegistryObject<($LootItemConditionType)>
static readonly "HIGHEST_SKILL": $RegistryObject<($LootItemConditionType)>
static readonly "VALID_BLOCK": $RegistryObject<($LootItemConditionType)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLMRegistry$Type = ($GLMRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLMRegistry_ = $GLMRegistry$Type;
}}
declare module "packages/harmonised/pmmo/core/$IDataStorage" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $IDataStorage {

 "get"(): $IDataStorage
 "getXpRaw"(arg0: $UUID$Type, arg1: string): long
 "setXpDiff"(arg0: $UUID$Type, arg1: string, arg2: long): boolean
 "getLevelFromXP"(arg0: long): integer
 "setXpMap"(arg0: $UUID$Type, arg1: $Map$Type<(string), (long)>): void
 "setXpRaw"(arg0: $UUID$Type, arg1: string, arg2: long): void
 "getBaseXpForLevel"(arg0: integer): long
 "getXpMap"(arg0: $UUID$Type): $Map<(string), (long)>
 "getPlayerSkillLevel"(arg0: string, arg1: $UUID$Type): integer
 "changePlayerSkillLevel"(arg0: string, arg1: $UUID$Type, arg2: integer): boolean
 "setPlayerSkillLevel"(arg0: string, arg1: $UUID$Type, arg2: integer): void
 "computeLevelsForCache"(): void
}

export namespace $IDataStorage {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IDataStorage$Type = ($IDataStorage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IDataStorage_ = $IDataStorage$Type;
}}
declare module "packages/harmonised/pmmo/client/gui/$GlossarySelectScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $GlossarySelectScreen extends $Screen {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor()

public "renderBackground"(arg0: $GuiGraphics$Type): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlossarySelectScreen$Type = ($GlossarySelectScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlossarySelectScreen_ = $GlossarySelectScreen$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$PistonHandler" {
import {$PistonEvent$Pre, $PistonEvent$Pre$Type} from "packages/net/minecraftforge/event/level/$PistonEvent$Pre"

export class $PistonHandler {

constructor()

public static "handle"(arg0: $PistonEvent$Pre$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PistonHandler$Type = ($PistonHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PistonHandler_ = $PistonHandler$Type;
}}
declare module "packages/harmonised/pmmo/features/veinmining/$VeinMiningLogic" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$VeinShapeData$ShapeType, $VeinShapeData$ShapeType$Type} from "packages/harmonised/pmmo/features/veinmining/$VeinShapeData$ShapeType"

export class $VeinMiningLogic {
static readonly "VEIN_DATA": string
static readonly "CURRENT_CHARGE": string
static readonly "maxBlocksPerPlayer": $Map<($UUID), (integer)>
static readonly "shapePerPlayer": $Map<($UUID), ($VeinShapeData$ShapeType)>

constructor()

public static "getCurrentCharge"(arg0: $Player$Type): integer
public static "applyVein"(arg0: $ServerPlayer$Type, arg1: $BlockPos$Type): void
public static "regenerateVein"(arg0: $ServerPlayer$Type): void
public static "getMaxChargeFromAllItems"(arg0: $Player$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VeinMiningLogic$Type = ($VeinMiningLogic);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VeinMiningLogic_ = $VeinMiningLogic$Type;
}}
declare module "packages/harmonised/pmmo/config/writers/$PackGenerator" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export class $PackGenerator {
static readonly "PACKNAME": string
static "applyOverride": boolean
static "applyDefaults": boolean
static "applyDisabler": boolean
static "applySimple": boolean
static "namespaceFilter": $List<(string)>
static "players": $Set<($ServerPlayer)>

constructor()

public static "generatePack"(arg0: $MinecraftServer$Type): integer
public static "generatePlayerConfigs"(arg0: $MinecraftServer$Type, arg1: $Collection$Type<($ServerPlayer$Type)>): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PackGenerator$Type = ($PackGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PackGenerator_ = $PackGenerator$Type;
}}
declare module "packages/harmonised/pmmo/client/gui/component/$StatScrollWidget" {
import {$GuiEnumGroup, $GuiEnumGroup$Type} from "packages/harmonised/pmmo/client/gui/component/$GuiEnumGroup"
import {$GlossarySelectScreen$SELECTION, $GlossarySelectScreen$SELECTION$Type} from "packages/harmonised/pmmo/client/gui/$GlossarySelectScreen$SELECTION"
import {$NarratableEntry$NarrationPriority, $NarratableEntry$NarrationPriority$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry$NarrationPriority"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ScrollPanel, $ScrollPanel$Type} from "packages/net/minecraftforge/client/gui/widget/$ScrollPanel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$GlossarySelectScreen$OBJECT, $GlossarySelectScreen$OBJECT$Type} from "packages/harmonised/pmmo/client/gui/$GlossarySelectScreen$OBJECT"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$NarrationElementOutput, $NarrationElementOutput$Type} from "packages/net/minecraft/client/gui/narration/$NarrationElementOutput"

export class $StatScrollWidget extends $ScrollPanel {

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ItemStack$Type)
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $Entity$Type)
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer)
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $BlockPos$Type)
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $GlossarySelectScreen$SELECTION$Type, arg5: $GlossarySelectScreen$OBJECT$Type, arg6: string, arg7: $GuiEnumGroup$Type)

public "generateGlossary"(arg0: $GlossarySelectScreen$SELECTION$Type, arg1: $GlossarySelectScreen$OBJECT$Type, arg2: string, arg3: $GuiEnumGroup$Type): void
public "updateNarration"(arg0: $NarrationElementOutput$Type): void
public "narrationPriority"(): $NarratableEntry$NarrationPriority
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StatScrollWidget$Type = ($StatScrollWidget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StatScrollWidget_ = $StatScrollWidget$Type;
}}
declare module "packages/harmonised/pmmo/config/readers/$MergeableCodecDataManager" {
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$DataSource, $DataSource$Type} from "packages/harmonised/pmmo/config/codecs/$DataSource"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$SimplePreparableReloadListener, $SimplePreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimplePreparableReloadListener"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $MergeableCodecDataManager<T extends $DataSource<(T)>, V> extends $SimplePreparableReloadListener<($Map<($ResourceLocation), (T)>)> {

constructor(arg0: string, arg1: $Logger$Type, arg2: $Codec$Type<(T)>, arg3: $Function$Type<($List$Type<(T)>), (T)>, arg4: $Consumer$Type<($Map$Type<($ResourceLocation$Type), (T)>)>, arg5: $Supplier$Type<(T)>, arg6: $ResourceKey$Type<($Registry$Type<(V)>)>)
constructor(arg0: string, arg1: $Logger$Type, arg2: $Codec$Type<(T)>, arg3: $Function$Type<($List$Type<(T)>), (T)>, arg4: $Consumer$Type<($Map$Type<($ResourceLocation$Type), (T)>)>, arg5: $Gson$Type, arg6: $Supplier$Type<(T)>, arg7: $ResourceKey$Type<($Registry$Type<(V)>)>)

public "getData"(arg0: $ResourceLocation$Type): T
public "getData"(): $Map<($ResourceLocation), (T)>
public "getGenericTypeInstance"(): T
public "subscribeAsSyncable"<PACKET>(arg0: $SimpleChannel$Type, arg1: $Function$Type<($Map$Type<($ResourceLocation$Type), (T)>), (PACKET)>): $MergeableCodecDataManager<(T), (V)>
public "clearData"(): void
public "registerOverride"(arg0: $ResourceLocation$Type, arg1: $DataSource$Type<(any)>): void
public "registerDefault"(arg0: $ResourceLocation$Type, arg1: $DataSource$Type<(any)>): void
public "postProcess"(arg0: $RegistryAccess$Type): void
get "data"(): $Map<($ResourceLocation), (T)>
get "genericTypeInstance"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MergeableCodecDataManager$Type<T, V> = ($MergeableCodecDataManager<(T), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MergeableCodecDataManager_<T, V> = $MergeableCodecDataManager$Type<(T), (V)>;
}}
declare module "packages/harmonised/pmmo/storage/$PmmoSavedData" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$SavedData, $SavedData$Type} from "packages/net/minecraft/world/level/saveddata/$SavedData"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDataStorage, $IDataStorage$Type} from "packages/harmonised/pmmo/core/$IDataStorage"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $PmmoSavedData extends $SavedData implements $IDataStorage {

constructor()
constructor(arg0: $CompoundTag$Type)

public "get"(): $IDataStorage
public "getXpRaw"(arg0: $UUID$Type, arg1: string): long
public "setXpDiff"(arg0: $UUID$Type, arg1: string, arg2: long): boolean
public "getLevelFromXP"(arg0: long): integer
public "setXpMap"(arg0: $UUID$Type, arg1: $Map$Type<(string), (long)>): void
public "setXpRaw"(arg0: $UUID$Type, arg1: string, arg2: long): void
public "getBaseXpForLevel"(arg0: integer): long
public "getLevelCache"(): $List<(long)>
public "awardScheduledXP"(arg0: $UUID$Type): void
public "getXpMap"(arg0: $UUID$Type): $Map<(string), (long)>
public "getPlayerSkillLevel"(arg0: string, arg1: $UUID$Type): integer
public "changePlayerSkillLevel"(arg0: string, arg1: $UUID$Type, arg2: integer): boolean
public "setPlayerSkillLevel"(arg0: string, arg1: $UUID$Type, arg2: integer): void
public "save"(arg0: $CompoundTag$Type): $CompoundTag
public "computeLevelsForCache"(): void
get "levelCache"(): $List<(long)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PmmoSavedData$Type = ($PmmoSavedData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PmmoSavedData_ = $PmmoSavedData$Type;
}}
declare module "packages/harmonised/pmmo/compat/crafttweaker/$ExtendedEnums" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ExtendedEnums {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtendedEnums$Type = ($ExtendedEnums);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtendedEnums_ = $ExtendedEnums$Type;
}}
declare module "packages/harmonised/pmmo/core/nbt/$PathReader" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$List, $List$Type} from "packages/java/util/$List"

export class $PathReader {

constructor()

public static "getNBTValues"(arg0: string, arg1: $CompoundTag$Type): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PathReader$Type = ($PathReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PathReader_ = $PathReader$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$FurnaceHandler" {
import {$FurnaceBurnEvent, $FurnaceBurnEvent$Type} from "packages/harmonised/pmmo/api/events/$FurnaceBurnEvent"

export class $FurnaceHandler {

constructor()

public static "handle"(arg0: $FurnaceBurnEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FurnaceHandler$Type = ($FurnaceHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FurnaceHandler_ = $FurnaceHandler$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$AnvilRepairHandler" {
import {$AnvilRepairEvent, $AnvilRepairEvent$Type} from "packages/net/minecraftforge/event/entity/player/$AnvilRepairEvent"

export class $AnvilRepairHandler {

constructor()

public static "handle"(arg0: $AnvilRepairEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnvilRepairHandler$Type = ($AnvilRepairHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnvilRepairHandler_ = $AnvilRepairHandler$Type;
}}
declare module "packages/harmonised/pmmo/compat/crafttweaker/$CTPerkFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $CTPerkFunction {

}

export namespace $CTPerkFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CTPerkFunction$Type = ($CTPerkFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CTPerkFunction_ = $CTPerkFunction$Type;
}}
declare module "packages/harmonised/pmmo/config/codecs/$ObjectData" {
import {$VeinData, $VeinData$Type} from "packages/harmonised/pmmo/config/codecs/$VeinData"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$DataSource, $DataSource$Type} from "packages/harmonised/pmmo/config/codecs/$DataSource"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$ReqType, $ReqType$Type} from "packages/harmonised/pmmo/api/enums/$ReqType"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ModifierDataType, $ModifierDataType$Type} from "packages/harmonised/pmmo/api/enums/$ModifierDataType"
import {$CodecTypes$SalvageData, $CodecTypes$SalvageData$Type} from "packages/harmonised/pmmo/config/codecs/$CodecTypes$SalvageData"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$LogicEntry, $LogicEntry$Type} from "packages/harmonised/pmmo/core/nbt/$LogicEntry"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ObjectData extends $Record implements $DataSource<($ObjectData)> {
static readonly "CODEC": $Codec<($ObjectData)>

constructor()
constructor(override: boolean, tagValues: $Set$Type<(string)>, reqs: $Map$Type<($ReqType$Type), ($Map$Type<(string), (integer)>)>, nbtReqs: $Map$Type<($ReqType$Type), ($List$Type<($LogicEntry$Type)>)>, negativeEffects: $Map$Type<($ResourceLocation$Type), (integer)>, xpValues: $Map$Type<($EventType$Type), ($Map$Type<(string), (long)>)>, damageXpValues: $Map$Type<($EventType$Type), ($Map$Type<(string), ($Map$Type<(string), (long)>)>)>, nbtXpValues: $Map$Type<($EventType$Type), ($List$Type<($LogicEntry$Type)>)>, bonuses: $Map$Type<($ModifierDataType$Type), ($Map$Type<(string), (double)>)>, nbtBonuses: $Map$Type<($ModifierDataType$Type), ($List$Type<($LogicEntry$Type)>)>, salvage: $Map$Type<($ResourceLocation$Type), ($CodecTypes$SalvageData$Type)>, veinData: $VeinData$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "combine"(arg0: $ObjectData$Type): $ObjectData
public "override"(): boolean
public "salvage"(): $Map<($ResourceLocation), ($CodecTypes$SalvageData)>
public "getTagValues"(): $Set<(string)>
public "xpValues"(): $Map<($EventType), ($Map<(string), (long)>)>
public "getBonuses"(arg0: $ModifierDataType$Type, arg1: $CompoundTag$Type): $Map<(string), (double)>
public "getXpValues"(arg0: $EventType$Type, arg1: $CompoundTag$Type): $Map<(string), (long)>
public "getReqs"(arg0: $ReqType$Type, arg1: $CompoundTag$Type): $Map<(string), (integer)>
public "nbtBonuses"(): $Map<($ModifierDataType), ($List<($LogicEntry)>)>
public "negativeEffects"(): $Map<($ResourceLocation), (integer)>
public "nbtXpValues"(): $Map<($EventType), ($List<($LogicEntry)>)>
public "nbtReqs"(): $Map<($ReqType), ($List<($LogicEntry)>)>
public "tagValues"(): $Set<(string)>
public "getNegativeEffect"(): $Map<($ResourceLocation), (integer)>
public "setNegativeEffects"(arg0: $Map$Type<($ResourceLocation$Type), (integer)>): void
public "setBonuses"(arg0: $ModifierDataType$Type, arg1: $Map$Type<(string), (double)>): void
public "setXpValues"(arg0: $EventType$Type, arg1: $Map$Type<(string), (long)>): void
public "setReqs"(arg0: $ReqType$Type, arg1: $Map$Type<(string), (integer)>): void
public "bonuses"(): $Map<($ModifierDataType), ($Map<(string), (double)>)>
public "damageXpValues"(): $Map<($EventType), ($Map<(string), ($Map<(string), (long)>)>)>
public "isUnconfigured"(): boolean
public "veinData"(): $VeinData
public "reqs"(): $Map<($ReqType), ($Map<(string), (integer)>)>
public "setPositiveEffects"(arg0: $Map$Type<($ResourceLocation$Type), (integer)>): void
public "getPositiveEffect"(): $Map<($ResourceLocation), (integer)>
public static "clearEmptyValues"<K, V>(arg0: $Map$Type<(K), (V)>): $HashMap<(K), (V)>
get "negativeEffect"(): $Map<($ResourceLocation), (integer)>
get "unconfigured"(): boolean
set "positiveEffects"(value: $Map$Type<($ResourceLocation$Type), (integer)>)
get "positiveEffect"(): $Map<($ResourceLocation), (integer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectData$Type = ($ObjectData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectData_ = $ObjectData$Type;
}}
declare module "packages/harmonised/pmmo/api/$APIUtils$SalvageBuilder" {
import {$CodecTypes$SalvageData, $CodecTypes$SalvageData$Type} from "packages/harmonised/pmmo/config/codecs/$CodecTypes$SalvageData"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $APIUtils$SalvageBuilder {


public static "start"(): $APIUtils$SalvageBuilder
public "build"(): $CodecTypes$SalvageData
public "setSalvageMax"(arg0: integer): $APIUtils$SalvageBuilder
public "setLevelReq"(arg0: $Map$Type<(string), (integer)>): $APIUtils$SalvageBuilder
public "setXpAward"(arg0: $Map$Type<(string), (long)>): $APIUtils$SalvageBuilder
public "setChancePerLevel"(arg0: $Map$Type<(string), (double)>): $APIUtils$SalvageBuilder
public "setMaxChance"(arg0: double): $APIUtils$SalvageBuilder
public "setBaseChance"(arg0: double): $APIUtils$SalvageBuilder
set "salvageMax"(value: integer)
set "levelReq"(value: $Map$Type<(string), (integer)>)
set "xpAward"(value: $Map$Type<(string), (long)>)
set "chancePerLevel"(value: $Map$Type<(string), (double)>)
set "maxChance"(value: double)
set "baseChance"(value: double)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $APIUtils$SalvageBuilder$Type = ($APIUtils$SalvageBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $APIUtils$SalvageBuilder_ = $APIUtils$SalvageBuilder$Type;
}}
declare module "packages/harmonised/pmmo/features/loot_modifiers/$SkillUpTrigger$TriggerInstance" {
import {$AbstractCriterionTriggerInstance, $AbstractCriterionTriggerInstance$Type} from "packages/net/minecraft/advancements/critereon/$AbstractCriterionTriggerInstance"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ContextAwarePredicate, $ContextAwarePredicate$Type} from "packages/net/minecraft/advancements/critereon/$ContextAwarePredicate"

export class $SkillUpTrigger$TriggerInstance extends $AbstractCriterionTriggerInstance {

constructor(arg0: $ResourceLocation$Type, arg1: $ContextAwarePredicate$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SkillUpTrigger$TriggerInstance$Type = ($SkillUpTrigger$TriggerInstance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SkillUpTrigger$TriggerInstance_ = $SkillUpTrigger$TriggerInstance$Type;
}}
declare module "packages/harmonised/pmmo/core/perks/$FeaturePerks" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$LivingDeathEvent, $LivingDeathEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingDeathEvent"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PlayerEvent$PlayerRespawnEvent, $PlayerEvent$PlayerRespawnEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerRespawnEvent"
import {$Perk, $Perk$Type} from "packages/harmonised/pmmo/api/perks/$Perk"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"

export class $FeaturePerks {
static readonly "ATTRIBUTE": $Perk
static readonly "TEMP_ATTRIBUTE": $Perk
static "EFFECT_SETTER": $BiFunction<($Player), ($CompoundTag), ($CompoundTag)>
static readonly "EFFECT": $Perk
static readonly "JUMP_CLIENT": $Perk
static readonly "JUMP_SERVER": $Perk
static readonly "BREATH": $Perk
static readonly "DAMAGE_REDUCE": $Perk
static readonly "APPLICABLE_TO": string
static readonly "DAMAGE_BOOST": $Perk
static readonly "RUN_COMMAND": $Perk
static readonly "VILLAGER_TRADING": $Perk

constructor()

public static "restoreAttributesOnSpawn"(arg0: $PlayerEvent$PlayerRespawnEvent$Type): void
public static "saveAttributesOnDeath"(arg0: $LivingDeathEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FeaturePerks$Type = ($FeaturePerks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FeaturePerks_ = $FeaturePerks$Type;
}}
declare module "packages/harmonised/pmmo/features/autovalues/$AutoValueConfig$UtensilTypes" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $AutoValueConfig$UtensilTypes extends $Enum<($AutoValueConfig$UtensilTypes)> {
static readonly "SWORD": $AutoValueConfig$UtensilTypes
static readonly "PICKAXE": $AutoValueConfig$UtensilTypes
static readonly "AXE": $AutoValueConfig$UtensilTypes
static readonly "SHOVEL": $AutoValueConfig$UtensilTypes
static readonly "HOE": $AutoValueConfig$UtensilTypes


public static "values"(): ($AutoValueConfig$UtensilTypes)[]
public static "valueOf"(arg0: string): $AutoValueConfig$UtensilTypes
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoValueConfig$UtensilTypes$Type = (("sword") | ("pickaxe") | ("shovel") | ("axe") | ("hoe")) | ($AutoValueConfig$UtensilTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoValueConfig$UtensilTypes_ = $AutoValueConfig$UtensilTypes$Type;
}}
declare module "packages/harmonised/pmmo/config/codecs/$SkillData" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $SkillData extends $Record {
static "CODEC": $Codec<($SkillData)>

constructor(color: $Optional$Type<(integer)>, afkExempt: $Optional$Type<(boolean)>, displayGroupName: $Optional$Type<(boolean)>, useTotalLevels: $Optional$Type<(boolean)>, groupedSkills: $Optional$Type<($Map$Type<(string), (double)>)>, maxLevel: $Optional$Type<(integer)>, icon: $Optional$Type<($ResourceLocation$Type)>, iconSize: $Optional$Type<(integer)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "color"(): $Optional<(integer)>
public "getIcon"(): $ResourceLocation
public "afkExempt"(): $Optional<(boolean)>
public "displayGroupName"(): $Optional<(boolean)>
public "useTotalLevels"(): $Optional<(boolean)>
public "iconSize"(): $Optional<(integer)>
public "getMaxLevel"(): integer
public "getDisplayGroupName"(): boolean
public "getColor"(): integer
public "icon"(): $Optional<($ResourceLocation)>
public "getGroupBonus"(arg0: double): $Map<(string), (double)>
public "getGroupReq"(arg0: integer): $Map<(string), (integer)>
public "isSkillGroup"(): boolean
public "getGroupXP"(arg0: long): $Map<(string), (long)>
public "groupedSkills"(): $Optional<($Map<(string), (double)>)>
public "getIconSize"(): integer
public "getAfkExempt"(): boolean
public "getUseTotalLevels"(): boolean
public "maxLevel"(): $Optional<(integer)>
public "getGroup"(): $Map<(string), (double)>
get "skillGroup"(): boolean
get "group"(): $Map<(string), (double)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SkillData$Type = ($SkillData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SkillData_ = $SkillData$Type;
}}
declare module "packages/harmonised/pmmo/features/fireworks/$PMMOFireworkEntity" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$EntityDataAccessor, $EntityDataAccessor$Type} from "packages/net/minecraft/network/syncher/$EntityDataAccessor"
import {$FireworkRocketEntity, $FireworkRocketEntity$Type} from "packages/net/minecraft/world/entity/projectile/$FireworkRocketEntity"
import {$Entity$RemovalReason, $Entity$RemovalReason$Type} from "packages/net/minecraft/world/entity/$Entity$RemovalReason"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$EntityDimensions, $EntityDimensions$Type} from "packages/net/minecraft/world/entity/$EntityDimensions"
import {$EntityInLevelCallback, $EntityInLevelCallback$Type} from "packages/net/minecraft/world/level/entity/$EntityInLevelCallback"
import {$OptionalInt, $OptionalInt$Type} from "packages/java/util/$OptionalInt"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $PMMOFireworkEntity extends $FireworkRocketEntity {
static readonly "DATA_ID_FIREWORKS_ITEM": $EntityDataAccessor<($ItemStack)>
static readonly "DATA_ATTACHED_TO_TARGET": $EntityDataAccessor<($OptionalInt)>
 "life": integer
 "lifetime": integer
 "ownerUUID": $UUID
 "cachedOwner": $Entity
 "leftOwner": boolean
 "hasBeenShot": boolean
static readonly "ID_TAG": string
static readonly "PASSENGERS_TAG": string
static readonly "BOARDING_COOLDOWN": integer
static readonly "TOTAL_AIR_SUPPLY": integer
static readonly "MAX_ENTITY_TAG_COUNT": integer
static readonly "DELTA_AFFECTED_BY_BLOCKS_BELOW_0_2": float
static readonly "DELTA_AFFECTED_BY_BLOCKS_BELOW_0_5": double
static readonly "DELTA_AFFECTED_BY_BLOCKS_BELOW_1_0": double
static readonly "BREATHING_DISTANCE_BELOW_EYES": float
static readonly "BASE_TICKS_REQUIRED_TO_FREEZE": integer
static readonly "FREEZE_HURT_FREQUENCY": integer
static readonly "UUID_TAG": string
 "blocksBuilding": boolean
 "level": $Level
 "xo": double
 "yo": double
 "zo": double
 "blockPosition": $BlockPos
 "yRot": float
 "xRot": float
 "yRotO": float
 "xRotO": float
 "horizontalCollision": boolean
 "verticalCollision": boolean
 "verticalCollisionBelow": boolean
 "minorHorizontalCollision": boolean
 "hurtMarked": boolean
 "removalReason": $Entity$RemovalReason
static readonly "DEFAULT_BB_WIDTH": float
static readonly "DEFAULT_BB_HEIGHT": float
 "walkDistO": float
 "walkDist": float
 "moveDist": float
 "flyDist": float
 "fallDistance": float
 "xOld": double
 "yOld": double
 "zOld": double
 "noPhysics": boolean
 "age": integer
 "wasTouchingWater": boolean
 "wasEyeInWater": boolean
 "invulnerableTime": integer
 "levelCallback": $EntityInLevelCallback
 "noCulling": boolean
 "hasImpulse": boolean
 "portalCooldown": integer
 "isInsidePortal": boolean
 "dimensions": $EntityDimensions
 "eyeHeight": float
 "isInPowderSnow": boolean
 "wasInPowderSnow": boolean
 "wasOnFire": boolean
 "mainSupportingBlockPos": $Optional<($BlockPos)>

constructor(arg0: $Level$Type, arg1: double, arg2: double, arg3: double, arg4: $ItemStack$Type)

public "explode"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PMMOFireworkEntity$Type = ($PMMOFireworkEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PMMOFireworkEntity_ = $PMMOFireworkEntity$Type;
}}
declare module "packages/harmonised/pmmo/setup/datagen/$ItemTagProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$TagsProvider$TagLookup, $TagsProvider$TagLookup$Type} from "packages/net/minecraft/data/tags/$TagsProvider$TagLookup"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$ItemTagsProvider, $ItemTagsProvider$Type} from "packages/net/minecraft/data/tags/$ItemTagsProvider"

export class $ItemTagProvider extends $ItemTagsProvider {

constructor(arg0: $PackOutput$Type, arg1: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, arg2: $CompletableFuture$Type<($TagsProvider$TagLookup$Type<($Block$Type)>)>, arg3: $ExistingFileHelper$Type)

public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemTagProvider$Type = ($ItemTagProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemTagProvider_ = $ItemTagProvider$Type;
}}
declare module "packages/harmonised/pmmo/features/party/$PartyUtils" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$List, $List$Type} from "packages/java/util/$List"

export class $PartyUtils {

constructor()

public static "removeFromParty"(arg0: $Player$Type): void
public static "getPartyMembersInRange"(arg0: $ServerPlayer$Type): $List<($ServerPlayer)>
public static "createParty"(arg0: $Player$Type): void
public static "isInParty"(arg0: $Player$Type): boolean
public static "inviteToParty"(arg0: $Player$Type, arg1: $Player$Type): void
public static "declineInvite"(arg0: $UUID$Type): boolean
public static "uninviteToParty"(arg0: $Player$Type, arg1: $Player$Type): void
public static "acceptInvite"(arg0: $Player$Type, arg1: $UUID$Type): void
public static "getPartyMembers"(arg0: $ServerPlayer$Type): $List<($ServerPlayer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PartyUtils$Type = ($PartyUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PartyUtils_ = $PartyUtils$Type;
}}
declare module "packages/harmonised/pmmo/features/autovalues/$AutoBlock" {
import {$ReqType, $ReqType$Type} from "packages/harmonised/pmmo/api/enums/$ReqType"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AutoBlock {
static readonly "REQTYPES": ($ReqType)[]
static readonly "EVENTTYPES": ($EventType)[]

constructor()

public static "processReqs"(arg0: $ReqType$Type, arg1: $ResourceLocation$Type): $Map<(string), (integer)>
public static "processXpGains"(arg0: $EventType$Type, arg1: $ResourceLocation$Type): $Map<(string), (long)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoBlock$Type = ($AutoBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoBlock_ = $AutoBlock$Type;
}}
declare module "packages/harmonised/pmmo/core/$CoreUtils" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$Style, $Style$Type} from "packages/net/minecraft/network/chat/$Style"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CoreUtils {

constructor()

public static "processSkillGroupXP"(arg0: $Map$Type<(string), (long)>): $Map<(string), (long)>
public static "processSkillGroupReqs"(arg0: $Map$Type<(string), (integer)>): $Map<(string), (integer)>
public static "applyXpModifiers"(arg0: $Map$Type<(string), (long)>, arg1: $Map$Type<(string), (double)>): $Map<(string), (long)>
public static "getSkillStyle"(arg0: string): $Style
public static "getSkillStyle"(arg0: string, arg1: double): $Style
public static "setTransparency"(arg0: integer, arg1: double): integer
public static "processSkillGroupBonus"(arg0: $Map$Type<(string), (double)>): $Map<(string), (double)>
public static "deserializeAwardMap"(arg0: $CompoundTag$Type): $Map<(string), (long)>
public static "mergeXpMapsWithSummateCondition"(arg0: $Map$Type<(string), (long)>, arg1: $Map$Type<(string), (long)>): $Map<(string), (long)>
public static "getSkillColor"(arg0: string): integer
public static "getEffects"(arg0: $Map$Type<($ResourceLocation$Type), (integer)>, arg1: boolean): $List<($MobEffectInstance)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoreUtils$Type = ($CoreUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoreUtils_ = $CoreUtils$Type;
}}
declare module "packages/harmonised/pmmo/api/perks/$Perk$Builder" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$TriFunction, $TriFunction$Type} from "packages/org/apache/commons/lang3/function/$TriFunction"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Perk, $Perk$Type} from "packages/harmonised/pmmo/api/perks/$Perk"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export class $Perk$Builder {


public "build"(): $Perk
public "setStart"(arg0: $BiFunction$Type<($Player$Type), ($CompoundTag$Type), ($CompoundTag$Type)>): $Perk$Builder
public "setDescription"(arg0: $MutableComponent$Type): $Perk$Builder
public "setTick"(arg0: $TriFunction$Type<($Player$Type), ($CompoundTag$Type), (integer), ($CompoundTag$Type)>): $Perk$Builder
public "setStop"(arg0: $BiFunction$Type<($Player$Type), ($CompoundTag$Type), ($CompoundTag$Type)>): $Perk$Builder
public "addConditions"(arg0: $BiPredicate$Type<($Player$Type), ($CompoundTag$Type)>): $Perk$Builder
public "setStatus"(arg0: $BiFunction$Type<($Player$Type), ($CompoundTag$Type), ($List$Type<($MutableComponent$Type)>)>): $Perk$Builder
public "addDefaults"(arg0: $CompoundTag$Type): $Perk$Builder
set "start"(value: $BiFunction$Type<($Player$Type), ($CompoundTag$Type), ($CompoundTag$Type)>)
set "description"(value: $MutableComponent$Type)
set "tick"(value: $TriFunction$Type<($Player$Type), ($CompoundTag$Type), (integer), ($CompoundTag$Type)>)
set "stop"(value: $BiFunction$Type<($Player$Type), ($CompoundTag$Type), ($CompoundTag$Type)>)
set "status"(value: $BiFunction$Type<($Player$Type), ($CompoundTag$Type), ($List$Type<($MutableComponent$Type)>)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Perk$Builder$Type = ($Perk$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Perk$Builder_ = $Perk$Builder$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$PlayerTickHandler" {
import {$TickEvent$PlayerTickEvent, $TickEvent$PlayerTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$PlayerTickEvent"

export class $PlayerTickHandler {

constructor()

public static "handle"(arg0: $TickEvent$PlayerTickEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerTickHandler$Type = ($PlayerTickHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerTickHandler_ = $PlayerTickHandler$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$TameHandler" {
import {$AnimalTameEvent, $AnimalTameEvent$Type} from "packages/net/minecraftforge/event/entity/living/$AnimalTameEvent"

export class $TameHandler {

constructor()

public static "handle"(arg0: $AnimalTameEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TameHandler$Type = ($TameHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TameHandler_ = $TameHandler$Type;
}}
declare module "packages/harmonised/pmmo/features/veinmining/$VeinShapeData" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$VeinShapeData$ShapeType, $VeinShapeData$ShapeType$Type} from "packages/harmonised/pmmo/features/veinmining/$VeinShapeData$ShapeType"

export class $VeinShapeData {

constructor(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: integer, arg3: $VeinShapeData$ShapeType$Type, arg4: $Direction$Type)

public "getVein"(): $Set<($BlockPos)>
get "vein"(): $Set<($BlockPos)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VeinShapeData$Type = ($VeinShapeData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VeinShapeData_ = $VeinShapeData$Type;
}}
declare module "packages/harmonised/pmmo/features/veinmining/capability/$VeinProvider" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$VeinHandler, $VeinHandler$Type} from "packages/harmonised/pmmo/features/veinmining/capability/$VeinHandler"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ICapabilitySerializable, $ICapabilitySerializable$Type} from "packages/net/minecraftforge/common/capabilities/$ICapabilitySerializable"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"
import {$DoubleTag, $DoubleTag$Type} from "packages/net/minecraft/nbt/$DoubleTag"

export class $VeinProvider implements $ICapabilitySerializable<($DoubleTag)> {
static readonly "VEIN_CAP_ID": $ResourceLocation
static readonly "VEIN_CAP": $Capability<($VeinHandler)>

constructor()

public "getCapability"<T>(arg0: $Capability$Type<(T)>, arg1: $Direction$Type): $LazyOptional<(T)>
public "deserializeNBT"(arg0: $DoubleTag$Type): void
public "serializeNBT"(): $DoubleTag
public "getCapability"<T>(arg0: $Capability$Type<(T)>): $LazyOptional<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VeinProvider$Type = ($VeinProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VeinProvider_ = $VeinProvider$Type;
}}
declare module "packages/harmonised/pmmo/storage/$ChunkDataProvider" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ICapabilitySerializable, $ICapabilitySerializable$Type} from "packages/net/minecraftforge/common/capabilities/$ICapabilitySerializable"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"
import {$IChunkData, $IChunkData$Type} from "packages/harmonised/pmmo/storage/$IChunkData"

export class $ChunkDataProvider implements $ICapabilitySerializable<($CompoundTag)> {
static readonly "CHUNK_CAP_ID": $ResourceLocation
static readonly "CHUNK_CAP": $Capability<($IChunkData)>

constructor()

public "getCapability"<T>(arg0: $Capability$Type<(T)>, arg1: $Direction$Type): $LazyOptional<(T)>
public "deserializeNBT"(arg0: $CompoundTag$Type): void
public "serializeNBT"(): $CompoundTag
public "getCapability"<T>(arg0: $Capability$Type<(T)>): $LazyOptional<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkDataProvider$Type = ($ChunkDataProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkDataProvider_ = $ChunkDataProvider$Type;
}}
declare module "packages/harmonised/pmmo/registry/$EventTriggerRegistry" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"

export class $EventTriggerRegistry {

constructor()

public "registerListener"(arg0: $ResourceLocation$Type, arg1: $EventType$Type, arg2: $BiFunction$Type<(any), ($CompoundTag$Type), ($CompoundTag$Type)>): void
public "executeEventListeners"(arg0: $EventType$Type, arg1: $Event$Type, arg2: $CompoundTag$Type): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventTriggerRegistry$Type = ($EventTriggerRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventTriggerRegistry_ = $EventTriggerRegistry$Type;
}}
declare module "packages/harmonised/pmmo/client/gui/component/$GuiEnumGroup" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $GuiEnumGroup {

 "getName"(): string

(): string
}

export namespace $GuiEnumGroup {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiEnumGroup$Type = ($GuiEnumGroup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiEnumGroup_ = $GuiEnumGroup$Type;
}}
declare module "packages/harmonised/pmmo/setup/datagen/$DamageTagProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$DamageType, $DamageType$Type} from "packages/net/minecraft/world/damagesource/$DamageType"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$TagsProvider, $TagsProvider$Type} from "packages/net/minecraft/data/tags/$TagsProvider"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"

export class $DamageTagProvider extends $TagsProvider<($DamageType)> {

constructor(arg0: $PackOutput$Type, arg1: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, arg2: $ExistingFileHelper$Type)

public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DamageTagProvider$Type = ($DamageTagProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DamageTagProvider_ = $DamageTagProvider$Type;
}}
declare module "packages/harmonised/pmmo/core/nbt/$LogicEntry$Case" {
import {$LogicEntry$Criteria, $LogicEntry$Criteria$Type} from "packages/harmonised/pmmo/core/nbt/$LogicEntry$Criteria"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $LogicEntry$Case extends $Record {
static readonly "CODEC": $Codec<($LogicEntry$Case)>

constructor(paths: $List$Type<(string)>, criteria: $List$Type<($LogicEntry$Criteria$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "paths"(): $List<(string)>
public "criteria"(): $List<($LogicEntry$Criteria)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LogicEntry$Case$Type = ($LogicEntry$Case);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LogicEntry$Case_ = $LogicEntry$Case$Type;
}}
declare module "packages/harmonised/pmmo/network/clientpackets/$CP_SetOtherExperience" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CP_SetOtherExperience {

constructor(arg0: $Map$Type<(string), (long)>)
constructor(arg0: $FriendlyByteBuf$Type)

public "toBytes"(arg0: $FriendlyByteBuf$Type): void
public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CP_SetOtherExperience$Type = ($CP_SetOtherExperience);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CP_SetOtherExperience_ = $CP_SetOtherExperience$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$DamageDealtHandler" {
import {$LivingDamageEvent, $LivingDamageEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingDamageEvent"
import {$LivingAttackEvent, $LivingAttackEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingAttackEvent"

export class $DamageDealtHandler {

constructor()

public static "handle"(arg0: $LivingDamageEvent$Type): void
public static "handle"(arg0: $LivingAttackEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DamageDealtHandler$Type = ($DamageDealtHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DamageDealtHandler_ = $DamageDealtHandler$Type;
}}
declare module "packages/harmonised/pmmo/features/autovalues/$AutoItem" {
import {$ReqType, $ReqType$Type} from "packages/harmonised/pmmo/api/enums/$ReqType"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AutoItem {
static readonly "REQTYPES": ($ReqType)[]
static readonly "EVENTTYPES": ($EventType)[]

constructor()

public static "processReqs"(arg0: $ReqType$Type, arg1: $ResourceLocation$Type): $Map<(string), (integer)>
public static "processXpGains"(arg0: $EventType$Type, arg1: $ResourceLocation$Type): $Map<(string), (long)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoItem$Type = ($AutoItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoItem_ = $AutoItem$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$ExplosionHandler" {
import {$ExplosionEvent$Detonate, $ExplosionEvent$Detonate$Type} from "packages/net/minecraftforge/event/level/$ExplosionEvent$Detonate"

export class $ExplosionHandler {

constructor()

public static "handle"(arg0: $ExplosionEvent$Detonate$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExplosionHandler$Type = ($ExplosionHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExplosionHandler_ = $ExplosionHandler$Type;
}}
declare module "packages/harmonised/pmmo/client/utils/$DP" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $DP {

constructor()

public static "dprefix"(arg0: long): string
public static "dpSoft"(arg0: float): string
public static "dpSoft"(arg0: double): string
public static "dp"(arg0: double): string
public static "dp"(arg0: float): string
public static "dpCustom"(arg0: double, arg1: integer): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DP$Type = ($DP);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DP_ = $DP$Type;
}}
declare module "packages/harmonised/pmmo/config/codecs/$PlayerData" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ReqType, $ReqType$Type} from "packages/harmonised/pmmo/api/enums/$ReqType"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ModifierDataType, $ModifierDataType$Type} from "packages/harmonised/pmmo/api/enums/$ModifierDataType"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$DataSource, $DataSource$Type} from "packages/harmonised/pmmo/config/codecs/$DataSource"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $PlayerData extends $Record implements $DataSource<($PlayerData)> {
static readonly "CODEC": $Codec<($PlayerData)>

constructor()
constructor(override: boolean, ignoreReq: boolean, bonuses: $Map$Type<(string), (double)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "combine"(arg0: $PlayerData$Type): $PlayerData
public "override"(): boolean
public "ignoreReq"(): boolean
public "getBonuses"(arg0: $ModifierDataType$Type, arg1: $CompoundTag$Type): $Map<(string), (double)>
public "setBonuses"(arg0: $ModifierDataType$Type, arg1: $Map$Type<(string), (double)>): void
public "bonuses"(): $Map<(string), (double)>
public "isUnconfigured"(): boolean
public "mergeWithPlayerBonuses"(arg0: $Map$Type<(string), (double)>): $Map<(string), (double)>
public "getTagValues"(): $Set<(string)>
public "getXpValues"(arg0: $EventType$Type, arg1: $CompoundTag$Type): $Map<(string), (long)>
public "getReqs"(arg0: $ReqType$Type, arg1: $CompoundTag$Type): $Map<(string), (integer)>
public "getNegativeEffect"(): $Map<($ResourceLocation), (integer)>
public "setNegativeEffects"(arg0: $Map$Type<($ResourceLocation$Type), (integer)>): void
public "setXpValues"(arg0: $EventType$Type, arg1: $Map$Type<(string), (long)>): void
public "setReqs"(arg0: $ReqType$Type, arg1: $Map$Type<(string), (integer)>): void
public "setPositiveEffects"(arg0: $Map$Type<($ResourceLocation$Type), (integer)>): void
public "getPositiveEffect"(): $Map<($ResourceLocation), (integer)>
public static "clearEmptyValues"<K, V>(arg0: $Map$Type<(K), (V)>): $HashMap<(K), (V)>
get "unconfigured"(): boolean
get "tagValues"(): $Set<(string)>
get "negativeEffect"(): $Map<($ResourceLocation), (integer)>
set "negativeEffects"(value: $Map$Type<($ResourceLocation$Type), (integer)>)
set "positiveEffects"(value: $Map$Type<($ResourceLocation$Type), (integer)>)
get "positiveEffect"(): $Map<($ResourceLocation), (integer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerData$Type = ($PlayerData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerData_ = $PlayerData$Type;
}}
declare module "packages/harmonised/pmmo/features/loot_modifiers/$TreasureLootModifier" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$LootModifier, $LootModifier$Type} from "packages/net/minecraftforge/common/loot/$LootModifier"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LootItemCondition, $LootItemCondition$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemCondition"
import {$Dynamic, $Dynamic$Type} from "packages/com/mojang/serialization/$Dynamic"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $TreasureLootModifier extends $LootModifier {
static readonly "CODEC": $Codec<($TreasureLootModifier)>
 "drop": $ItemStack
 "chance": double
 "perLevel": boolean
 "skill": string

constructor(arg0: ($LootItemCondition$Type)[], arg1: $ResourceLocation$Type, arg2: integer, arg3: double)
constructor(arg0: ($LootItemCondition$Type)[], arg1: $ResourceLocation$Type, arg2: integer, arg3: double, arg4: $Optional$Type<(boolean)>, arg5: $Optional$Type<(string)>)

public "codec"(): $Codec<(any)>
public static "getJson"<U>(arg0: $Dynamic$Type<(any)>): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TreasureLootModifier$Type = ($TreasureLootModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TreasureLootModifier_ = $TreasureLootModifier$Type;
}}
declare module "packages/harmonised/pmmo/commands/$CmdNodeParty" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$ArgumentBuilder, $ArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$ArgumentBuilder"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"
import {$CommandContext, $CommandContext$Type} from "packages/com/mojang/brigadier/context/$CommandContext"

export class $CmdNodeParty {

constructor()

public static "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>): $ArgumentBuilder<($CommandSourceStack), (any)>
public static "partyCreate"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>): integer
public static "partyLeave"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>): integer
public static "listParty"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>): integer
public static "partyDecline"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>): integer
public static "partyInvite"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>): integer
public static "partyAccept"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>): integer
public static "partyUninvite"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CmdNodeParty$Type = ($CmdNodeParty);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CmdNodeParty_ = $CmdNodeParty$Type;
}}
declare module "packages/harmonised/pmmo/api/perks/$Perk" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Perk$Builder, $Perk$Builder$Type} from "packages/harmonised/pmmo/api/perks/$Perk$Builder"
import {$TriFunction, $TriFunction$Type} from "packages/org/apache/commons/lang3/function/$TriFunction"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export class $Perk extends $Record {
static readonly "VALID_CONTEXT": $BiPredicate<($Player), ($CompoundTag)>

constructor(conditions: $BiPredicate$Type<($Player$Type), ($CompoundTag$Type)>, propertyDefaults: $CompoundTag$Type, start: $BiFunction$Type<($Player$Type), ($CompoundTag$Type), ($CompoundTag$Type)>, tick: $TriFunction$Type<($Player$Type), ($CompoundTag$Type), (integer), ($CompoundTag$Type)>, stop: $BiFunction$Type<($Player$Type), ($CompoundTag$Type), ($CompoundTag$Type)>, description: $MutableComponent$Type, status: $BiFunction$Type<($Player$Type), ($CompoundTag$Type), ($List$Type<($MutableComponent$Type)>)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "start"(): $BiFunction<($Player), ($CompoundTag), ($CompoundTag)>
public "start"(arg0: $Player$Type, arg1: $CompoundTag$Type): $CompoundTag
public static "begin"(): $Perk$Builder
public static "empty"(): $Perk
public "status"(): $BiFunction<($Player), ($CompoundTag), ($List<($MutableComponent)>)>
public "stop"(): $BiFunction<($Player), ($CompoundTag), ($CompoundTag)>
public "stop"(arg0: $Player$Type, arg1: $CompoundTag$Type): $CompoundTag
public "tick"(): $TriFunction<($Player), ($CompoundTag), (integer), ($CompoundTag)>
public "tick"(arg0: $Player$Type, arg1: $CompoundTag$Type, arg2: integer): $CompoundTag
public "description"(): $MutableComponent
public "propertyDefaults"(): $CompoundTag
public "canActivate"(arg0: $Player$Type, arg1: $CompoundTag$Type): boolean
public "conditions"(): $BiPredicate<($Player), ($CompoundTag)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Perk$Type = ($Perk);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Perk_ = $Perk$Type;
}}
declare module "packages/harmonised/pmmo/network/$Networking" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export class $Networking {

constructor()

public static "registerMessages"(): void
public static "sendToClient"(arg0: any, arg1: $ServerPlayer$Type): void
public static "sendToServer"(arg0: any): void
public static "registerDataSyncPackets"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Networking$Type = ($Networking);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Networking_ = $Networking$Type;
}}
declare module "packages/harmonised/pmmo/api/enums/$ObjectType" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$IExtensibleEnum, $IExtensibleEnum$Type} from "packages/net/minecraftforge/common/$IExtensibleEnum"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $ObjectType extends $Enum<($ObjectType)> implements $StringRepresentable, $IExtensibleEnum {
static readonly "ITEM": $ObjectType
static readonly "BLOCK": $ObjectType
static readonly "ENTITY": $ObjectType
static readonly "DIMENSION": $ObjectType
static readonly "BIOME": $ObjectType
static readonly "ENCHANTMENT": $ObjectType
static readonly "EFFECT": $ObjectType
static readonly "PLAYER": $ObjectType
static readonly "CODEC": $Codec<($ObjectType)>


public static "values"(): ($ObjectType)[]
public static "valueOf"(arg0: string): $ObjectType
public static "create"(arg0: string): $ObjectType
public "getSerializedName"(): string
public static "byName"(arg0: string): $ObjectType
public static "fromEnumWithMapping"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
public static "keys"(arg0: ($StringRepresentable$Type)[]): $Keyable
public static "fromEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
/**
 * 
 * @deprecated
 */
public "init"(): void
public static "createCodecForExtensibleEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(any), (any)>): $Codec<(E)>
get "serializedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectType$Type = (("item") | ("biome") | ("enchantment") | ("effect") | ("block") | ("dimension") | ("entity") | ("player")) | ($ObjectType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectType_ = $ObjectType$Type;
}}
declare module "packages/harmonised/pmmo/api/events/$XpEvent" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$PlayerEvent, $PlayerEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent"

export class $XpEvent extends $PlayerEvent {
 "skill": string
 "amountAwarded": long

constructor(arg0: $ServerPlayer$Type, arg1: string, arg2: long, arg3: long, arg4: $CompoundTag$Type)
constructor()

public "getContext"(): $CompoundTag
public "isCancelable"(): boolean
public "isLevelUp"(): boolean
public "endLevel"(): integer
public "startLevel"(): integer
public "getListenerList"(): $ListenerList
get "context"(): $CompoundTag
get "cancelable"(): boolean
get "levelUp"(): boolean
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $XpEvent$Type = ($XpEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $XpEvent_ = $XpEvent$Type;
}}
declare module "packages/harmonised/pmmo/setup/$ClientSetup" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$RegisterGuiOverlaysEvent, $RegisterGuiOverlaysEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterGuiOverlaysEvent"
import {$RegisterKeyMappingsEvent, $RegisterKeyMappingsEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterKeyMappingsEvent"

export class $ClientSetup {
static readonly "SHOW_VEIN": $KeyMapping
static readonly "ADD_VEIN": $KeyMapping
static readonly "SUB_VEIN": $KeyMapping
static readonly "CYCLE_VEIN": $KeyMapping
static readonly "SHOW_LIST": $KeyMapping
static readonly "VEIN_KEY": $KeyMapping
static readonly "OPEN_MENU": $KeyMapping

constructor()

public static "init"(arg0: $RegisterKeyMappingsEvent$Type): void
public static "registerOverlay"(arg0: $RegisterGuiOverlaysEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientSetup$Type = ($ClientSetup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientSetup_ = $ClientSetup$Type;
}}
declare module "packages/harmonised/pmmo/api/enums/$PerkSide" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $PerkSide extends $Enum<($PerkSide)> {
static readonly "CLIENT": $PerkSide
static readonly "SERVER": $PerkSide
static readonly "BOTH": $PerkSide


public static "values"(): ($PerkSide)[]
public static "valueOf"(arg0: string): $PerkSide
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PerkSide$Type = (("server") | ("client") | ("both")) | ($PerkSide);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PerkSide_ = $PerkSide$Type;
}}
declare module "packages/harmonised/pmmo/config/readers/$CoreLoader" {
import {$LocationData, $LocationData$Type} from "packages/harmonised/pmmo/config/codecs/$LocationData"
import {$EnhancementsData, $EnhancementsData$Type} from "packages/harmonised/pmmo/config/codecs/$EnhancementsData"
import {$MergeableCodecDataManager, $MergeableCodecDataManager$Type} from "packages/harmonised/pmmo/config/readers/$MergeableCodecDataManager"
import {$ExecutableListener, $ExecutableListener$Type} from "packages/harmonised/pmmo/config/readers/$ExecutableListener"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$DataSource, $DataSource$Type} from "packages/harmonised/pmmo/config/codecs/$DataSource"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$ObjectData, $ObjectData$Type} from "packages/harmonised/pmmo/config/codecs/$ObjectData"
import {$ModifierDataType, $ModifierDataType$Type} from "packages/harmonised/pmmo/api/enums/$ModifierDataType"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$TagsUpdatedEvent, $TagsUpdatedEvent$Type} from "packages/net/minecraftforge/event/$TagsUpdatedEvent"
import {$ObjectType, $ObjectType$Type} from "packages/harmonised/pmmo/api/enums/$ObjectType"
import {$PlayerData, $PlayerData$Type} from "packages/harmonised/pmmo/config/codecs/$PlayerData"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CoreLoader {
static readonly "RELOADER": $ExecutableListener
readonly "ITEM_LOADER": $MergeableCodecDataManager<($ObjectData), ($Item)>
readonly "BLOCK_LOADER": $MergeableCodecDataManager<($ObjectData), ($Block)>
readonly "ENTITY_LOADER": $MergeableCodecDataManager<($ObjectData), ($EntityType<(any)>)>
readonly "BIOME_LOADER": $MergeableCodecDataManager<($LocationData), ($Biome)>
readonly "DIMENSION_LOADER": $MergeableCodecDataManager<($LocationData), ($Level)>
readonly "PLAYER_LOADER": $MergeableCodecDataManager<($PlayerData), ($Player)>
readonly "ENCHANTMENT_LOADER": $MergeableCodecDataManager<($EnhancementsData), ($Enchantment)>
readonly "EFFECT_LOADER": $MergeableCodecDataManager<($EnhancementsData), ($MobEffect)>

constructor()

public "getLoader"(arg0: $ModifierDataType$Type): $MergeableCodecDataManager<(any), (any)>
public "getLoader"(arg0: $ObjectType$Type): $MergeableCodecDataManager<(any), (any)>
public static "onTagLoad"(arg0: $TagsUpdatedEvent$Type): void
public "applyData"<T extends $DataSource<(T)>>(arg0: $ObjectType$Type, arg1: $Map$Type<($ResourceLocation$Type), (T)>): void
public "resetData"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoreLoader$Type = ($CoreLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoreLoader_ = $CoreLoader$Type;
}}
declare module "packages/harmonised/pmmo/client/gui/component/$PMMOButton" {
import {$ImageButton, $ImageButton$Type} from "packages/net/minecraft/client/gui/components/$ImageButton"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $PMMOButton extends $ImageButton {
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

constructor(arg0: $Screen$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PMMOButton$Type = ($PMMOButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PMMOButton_ = $PMMOButton$Type;
}}
declare module "packages/harmonised/pmmo/features/veinmining/$VeinShapeData$ShapeType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $VeinShapeData$ShapeType extends $Enum<($VeinShapeData$ShapeType)> {
static readonly "AOE": $VeinShapeData$ShapeType
static readonly "TUNNEL": $VeinShapeData$ShapeType
static readonly "BIG_TUNNEL": $VeinShapeData$ShapeType


public static "values"(): ($VeinShapeData$ShapeType)[]
public static "valueOf"(arg0: string): $VeinShapeData$ShapeType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VeinShapeData$ShapeType$Type = (("aoe") | ("big_tunnel") | ("tunnel")) | ($VeinShapeData$ShapeType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VeinShapeData$ShapeType_ = $VeinShapeData$ShapeType$Type;
}}
declare module "packages/harmonised/pmmo/api/enums/$ReqType" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$IExtensibleEnum, $IExtensibleEnum$Type} from "packages/net/minecraftforge/common/$IExtensibleEnum"
import {$GuiEnumGroup, $GuiEnumGroup$Type} from "packages/harmonised/pmmo/client/gui/component/$GuiEnumGroup"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$LangProvider$Translation, $LangProvider$Translation$Type} from "packages/harmonised/pmmo/setup/datagen/$LangProvider$Translation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $ReqType extends $Enum<($ReqType)> implements $StringRepresentable, $IExtensibleEnum, $GuiEnumGroup {
static readonly "WEAR": $ReqType
static readonly "USE_ENCHANTMENT": $ReqType
static readonly "TOOL": $ReqType
static readonly "WEAPON": $ReqType
static readonly "USE": $ReqType
static readonly "PLACE": $ReqType
static readonly "BREAK": $ReqType
static readonly "KILL": $ReqType
static readonly "TRAVEL": $ReqType
static readonly "RIDE": $ReqType
static readonly "TAME": $ReqType
static readonly "BREED": $ReqType
static readonly "INTERACT": $ReqType
static readonly "ENTITY_INTERACT": $ReqType
readonly "itemApplicable": boolean
readonly "blockApplicable": boolean
readonly "entityApplicable": boolean
readonly "defaultSkill": string
readonly "tooltipTranslation": $LangProvider$Translation
static readonly "ITEM_APPLICABLE_EVENTS": ($ReqType)[]
static readonly "BLOCK_APPLICABLE_EVENTS": ($ReqType)[]
static readonly "ENTITY_APPLICABLE_EVENTS": ($ReqType)[]
static readonly "BLOCKITEM_APPLICABLE_EVENTS": ($ReqType)[]
static readonly "CODEC": $Codec<($ReqType)>


public "getName"(): string
public static "values"(): ($ReqType)[]
public static "valueOf"(arg0: string): $ReqType
public static "create"(arg0: string, arg1: boolean, arg2: boolean, arg3: boolean, arg4: string, arg5: $LangProvider$Translation$Type): $ReqType
public "getSerializedName"(): string
public static "byName"(arg0: string): $ReqType
public static "fromEnumWithMapping"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
public static "keys"(arg0: ($StringRepresentable$Type)[]): $Keyable
public static "fromEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
/**
 * 
 * @deprecated
 */
public "init"(): void
public static "createCodecForExtensibleEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(any), (any)>): $Codec<(E)>
get "name"(): string
get "serializedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReqType$Type = (("break") | ("use") | ("entity_interact") | ("wear") | ("interact") | ("kill") | ("ride") | ("tool") | ("breed") | ("weapon") | ("tame") | ("use_enchantment") | ("place") | ("travel")) | ($ReqType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReqType_ = $ReqType$Type;
}}
declare module "packages/harmonised/pmmo/api/events/$FurnaceBurnEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $FurnaceBurnEvent extends $Event {

constructor()
constructor(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $BlockPos$Type)

public "getLevel"(): $Level
public "getInput"(): $ItemStack
public "isCancelable"(): boolean
public "getPos"(): $BlockPos
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "level"(): $Level
get "input"(): $ItemStack
get "cancelable"(): boolean
get "pos"(): $BlockPos
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FurnaceBurnEvent$Type = ($FurnaceBurnEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FurnaceBurnEvent_ = $FurnaceBurnEvent$Type;
}}
declare module "packages/harmonised/pmmo/util/$Messenger" {
import {$ReqType, $ReqType$Type} from "packages/harmonised/pmmo/api/enums/$ReqType"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export class $Messenger {

constructor()

public static "sendDenialMsg"(arg0: $ReqType$Type, arg1: $Player$Type, ...arg2: (any)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Messenger$Type = ($Messenger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Messenger_ = $Messenger$Type;
}}
declare module "packages/harmonised/pmmo/setup/$CommonSetup" {
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$ModConfigEvent$Reloading, $ModConfigEvent$Reloading$Type} from "packages/net/minecraftforge/fml/event/config/$ModConfigEvent$Reloading"
import {$RegisterCommandsEvent, $RegisterCommandsEvent$Type} from "packages/net/minecraftforge/event/$RegisterCommandsEvent"
import {$GatherDataEvent, $GatherDataEvent$Type} from "packages/net/minecraftforge/data/event/$GatherDataEvent"
import {$AttachCapabilitiesEvent, $AttachCapabilitiesEvent$Type} from "packages/net/minecraftforge/event/$AttachCapabilitiesEvent"
import {$RegisterCapabilitiesEvent, $RegisterCapabilitiesEvent$Type} from "packages/net/minecraftforge/common/capabilities/$RegisterCapabilitiesEvent"
import {$AddReloadListenerEvent, $AddReloadListenerEvent$Type} from "packages/net/minecraftforge/event/$AddReloadListenerEvent"
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"
import {$ServerStartingEvent, $ServerStartingEvent$Type} from "packages/net/minecraftforge/event/server/$ServerStartingEvent"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $CommonSetup {

constructor()

public static "init"(arg0: $FMLCommonSetupEvent$Type): void
public static "onCommandRegister"(arg0: $RegisterCommandsEvent$Type): void
public static "gatherData"(arg0: $GatherDataEvent$Type): void
public static "onCapabilityAttach"(arg0: $AttachCapabilitiesEvent$Type<($LevelChunk$Type)>): void
public static "onServerStartup"(arg0: $ServerStartingEvent$Type): void
public static "onConfigReload"(arg0: $ModConfigEvent$Reloading$Type): void
public static "onCapabilityRegister"(arg0: $RegisterCapabilitiesEvent$Type): void
public static "onAddReloadListeners"(arg0: $AddReloadListenerEvent$Type): void
public static "onPlayerCapabilityAttach"(arg0: $AttachCapabilitiesEvent$Type<($Entity$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonSetup$Type = ($CommonSetup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonSetup_ = $CommonSetup$Type;
}}
declare module "packages/harmonised/pmmo/client/events/$ClientTickHandler$GainEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"

export class $ClientTickHandler$GainEntry {
 "duration": integer

constructor(arg0: string, arg1: long)

public "toString"(): string
public "display"(): $Component
public "downTick"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTickHandler$GainEntry$Type = ($ClientTickHandler$GainEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTickHandler$GainEntry_ = $ClientTickHandler$GainEntry$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$DamageReceivedHandler" {
import {$LivingHurtEvent, $LivingHurtEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingHurtEvent"

export class $DamageReceivedHandler {

constructor()

public static "handle"(arg0: $LivingHurtEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DamageReceivedHandler$Type = ($DamageReceivedHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DamageReceivedHandler_ = $DamageReceivedHandler$Type;
}}
declare module "packages/harmonised/pmmo/$ProjectMMO" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ProjectMMO {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProjectMMO$Type = ($ProjectMMO);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProjectMMO_ = $ProjectMMO$Type;
}}
declare module "packages/harmonised/pmmo/commands/$CmdNodeStore" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$ArgumentBuilder, $ArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$ArgumentBuilder"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"
import {$CommandContext, $CommandContext$Type} from "packages/com/mojang/brigadier/context/$CommandContext"

export class $CmdNodeStore {

constructor()

public static "store"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>): integer
public static "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>): $ArgumentBuilder<($CommandSourceStack), (any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CmdNodeStore$Type = ($CmdNodeStore);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CmdNodeStore_ = $CmdNodeStore$Type;
}}
declare module "packages/harmonised/pmmo/features/loot_modifiers/$RareDropModifier" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$LootModifier, $LootModifier$Type} from "packages/net/minecraftforge/common/loot/$LootModifier"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LootItemCondition, $LootItemCondition$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemCondition"
import {$Dynamic, $Dynamic$Type} from "packages/com/mojang/serialization/$Dynamic"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $RareDropModifier extends $LootModifier {
static readonly "CODEC": $Codec<($RareDropModifier)>
 "drop": $ItemStack
 "chance": double
 "perLevel": boolean
 "skill": string

constructor(arg0: ($LootItemCondition$Type)[], arg1: $ResourceLocation$Type, arg2: integer, arg3: double)
constructor(arg0: ($LootItemCondition$Type)[], arg1: $ResourceLocation$Type, arg2: integer, arg3: double, arg4: $Optional$Type<(boolean)>, arg5: $Optional$Type<(string)>)

public "codec"(): $Codec<(any)>
public static "getJson"<U>(arg0: $Dynamic$Type<(any)>): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RareDropModifier$Type = ($RareDropModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RareDropModifier_ = $RareDropModifier$Type;
}}
declare module "packages/harmonised/pmmo/events/$EventHandler" {
import {$PlayerEvent$ItemCraftedEvent, $PlayerEvent$ItemCraftedEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$ItemCraftedEvent"
import {$BabyEntitySpawnEvent, $BabyEntitySpawnEvent$Type} from "packages/net/minecraftforge/event/entity/living/$BabyEntitySpawnEvent"
import {$SleepFinishedTimeEvent, $SleepFinishedTimeEvent$Type} from "packages/net/minecraftforge/event/level/$SleepFinishedTimeEvent"
import {$LivingDeathEvent, $LivingDeathEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingDeathEvent"
import {$LivingEvent$LivingJumpEvent, $LivingEvent$LivingJumpEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingEvent$LivingJumpEvent"
import {$EntityTravelToDimensionEvent, $EntityTravelToDimensionEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityTravelToDimensionEvent"
import {$AnimalTameEvent, $AnimalTameEvent$Type} from "packages/net/minecraftforge/event/entity/living/$AnimalTameEvent"
import {$FurnaceBurnEvent, $FurnaceBurnEvent$Type} from "packages/harmonised/pmmo/api/events/$FurnaceBurnEvent"
import {$PlayerEvent$PlayerLoggedOutEvent, $PlayerEvent$PlayerLoggedOutEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerLoggedOutEvent"
import {$TradeWithVillagerEvent, $TradeWithVillagerEvent$Type} from "packages/net/minecraftforge/event/entity/player/$TradeWithVillagerEvent"
import {$EnchantEvent, $EnchantEvent$Type} from "packages/harmonised/pmmo/api/events/$EnchantEvent"
import {$LivingHurtEvent, $LivingHurtEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingHurtEvent"
import {$ShieldBlockEvent, $ShieldBlockEvent$Type} from "packages/net/minecraftforge/event/entity/living/$ShieldBlockEvent"
import {$PlayerEvent$BreakSpeed, $PlayerEvent$BreakSpeed$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$BreakSpeed"
import {$ItemFishedEvent, $ItemFishedEvent$Type} from "packages/net/minecraftforge/event/entity/player/$ItemFishedEvent"
import {$PlayerInteractEvent$RightClickItem, $PlayerInteractEvent$RightClickItem$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$RightClickItem"
import {$BlockEvent$CropGrowEvent$Post, $BlockEvent$CropGrowEvent$Post$Type} from "packages/net/minecraftforge/event/level/$BlockEvent$CropGrowEvent$Post"
import {$LivingDamageEvent, $LivingDamageEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingDamageEvent"
import {$EntityMountEvent, $EntityMountEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityMountEvent"
import {$PistonEvent$Pre, $PistonEvent$Pre$Type} from "packages/net/minecraftforge/event/level/$PistonEvent$Pre"
import {$BlockEvent$BreakEvent, $BlockEvent$BreakEvent$Type} from "packages/net/minecraftforge/event/level/$BlockEvent$BreakEvent"
import {$BlockEvent$EntityPlaceEvent, $BlockEvent$EntityPlaceEvent$Type} from "packages/net/minecraftforge/event/level/$BlockEvent$EntityPlaceEvent"
import {$PlayerEvent$PlayerLoggedInEvent, $PlayerEvent$PlayerLoggedInEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerLoggedInEvent"
import {$LivingAttackEvent, $LivingAttackEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingAttackEvent"
import {$PlayerBrewedPotionEvent, $PlayerBrewedPotionEvent$Type} from "packages/net/minecraftforge/event/brewing/$PlayerBrewedPotionEvent"
import {$PlayerInteractEvent$RightClickBlock, $PlayerInteractEvent$RightClickBlock$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$RightClickBlock"
import {$ExplosionEvent$Detonate, $ExplosionEvent$Detonate$Type} from "packages/net/minecraftforge/event/level/$ExplosionEvent$Detonate"
import {$TickEvent$PlayerTickEvent, $TickEvent$PlayerTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$PlayerTickEvent"
import {$AnvilRepairEvent, $AnvilRepairEvent$Type} from "packages/net/minecraftforge/event/entity/player/$AnvilRepairEvent"
import {$PlayerInteractEvent$EntityInteract, $PlayerInteractEvent$EntityInteract$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$EntityInteract"
import {$PlayerEvent$PlayerChangeGameModeEvent, $PlayerEvent$PlayerChangeGameModeEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerChangeGameModeEvent"
import {$PlayerInteractEvent$LeftClickBlock, $PlayerInteractEvent$LeftClickBlock$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$LeftClickBlock"
import {$PlayerEvent$PlayerRespawnEvent, $PlayerEvent$PlayerRespawnEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerRespawnEvent"
import {$LivingEntityUseItemEvent$Finish, $LivingEntityUseItemEvent$Finish$Type} from "packages/net/minecraftforge/event/entity/living/$LivingEntityUseItemEvent$Finish"
import {$TickEvent$LevelTickEvent, $TickEvent$LevelTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$LevelTickEvent"

export class $EventHandler {

constructor()

public static "onPlayerTick"(arg0: $TickEvent$PlayerTickEvent$Type): void
public static "onBlockBreak"(arg0: $BlockEvent$BreakEvent$Type): void
public static "onPlayerLeave"(arg0: $PlayerEvent$PlayerLoggedOutEvent$Type): void
public static "onBlockPlace"(arg0: $BlockEvent$EntityPlaceEvent$Type): void
public static "onShieldBlock"(arg0: $ShieldBlockEvent$Type): void
public static "onRespawn"(arg0: $PlayerEvent$PlayerRespawnEvent$Type): void
public static "onPlayerJoin"(arg0: $PlayerEvent$PlayerLoggedInEvent$Type): void
public static "onGamemodeChange"(arg0: $PlayerEvent$PlayerChangeGameModeEvent$Type): void
public static "onDealDamage"(arg0: $LivingDamageEvent$Type): void
public static "onDealDamage"(arg0: $LivingAttackEvent$Type): void
public static "onFish"(arg0: $ItemFishedEvent$Type): void
public static "onCraft"(arg0: $PlayerEvent$ItemCraftedEvent$Type): void
public static "onEntityInteract"(arg0: $PlayerInteractEvent$EntityInteract$Type): void
public static "onBreed"(arg0: $BabyEntitySpawnEvent$Type): void
public static "onTame"(arg0: $AnimalTameEvent$Type): void
public static "onJump"(arg0: $LivingEvent$LivingJumpEvent$Type): void
public static "onCropGrow"(arg0: $BlockEvent$CropGrowEvent$Post$Type): void
public static "onPistonMove"(arg0: $PistonEvent$Pre$Type): void
public static "onSleep"(arg0: $SleepFinishedTimeEvent$Type): void
public static "tickPerks"(arg0: $TickEvent$LevelTickEvent$Type): void
public static "onBreakSpeed"(arg0: $PlayerEvent$BreakSpeed$Type): void
public static "filterExplosions"(arg0: $ExplosionEvent$Detonate$Type): void
public static "onPotionCollect"(arg0: $PlayerBrewedPotionEvent$Type): void
public static "onBlockActivate"(arg0: $PlayerInteractEvent$RightClickBlock$Type): void
public static "onItemActivate"(arg0: $PlayerInteractEvent$RightClickItem$Type): void
public static "onFoodEat"(arg0: $LivingEntityUseItemEvent$Finish$Type): void
public static "onEnchant"(arg0: $EnchantEvent$Type): void
public static "onAnvilRepar"(arg0: $AnvilRepairEvent$Type): void
public static "onMount"(arg0: $EntityMountEvent$Type): void
public static "onPlayerDimTravel"(arg0: $EntityTravelToDimensionEvent$Type): void
public static "onTrade"(arg0: $TradeWithVillagerEvent$Type): void
public static "onSmelt"(arg0: $FurnaceBurnEvent$Type): void
public static "onBlockHit"(arg0: $PlayerInteractEvent$LeftClickBlock$Type): void
public static "onDamage"(arg0: $LivingHurtEvent$Type): void
public static "onDeath"(arg0: $LivingDeathEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventHandler$Type = ($EventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventHandler_ = $EventHandler$Type;
}}
declare module "packages/harmonised/pmmo/config/codecs/$VeinData" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ReqType, $ReqType$Type} from "packages/harmonised/pmmo/api/enums/$ReqType"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ModifierDataType, $ModifierDataType$Type} from "packages/harmonised/pmmo/api/enums/$ModifierDataType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$DataSource, $DataSource$Type} from "packages/harmonised/pmmo/config/codecs/$DataSource"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $VeinData implements $DataSource<($VeinData)> {
 "chargeCap": $Optional<(integer)>
 "chargeRate": $Optional<(double)>
 "consumeAmount": $Optional<(integer)>
static readonly "VEIN_DATA_CODEC": $Codec<($VeinData)>
static "EMPTY": $VeinData

constructor(arg0: $Optional$Type<(integer)>, arg1: $Optional$Type<(double)>, arg2: $Optional$Type<(integer)>)

public "toString"(): string
public "combine"(arg0: $VeinData$Type): $VeinData
public "replaceWith"(arg0: $VeinData$Type): void
public "isUnconfigured"(): boolean
public "getTagValues"(): $Set<(string)>
public "getBonuses"(arg0: $ModifierDataType$Type, arg1: $CompoundTag$Type): $Map<(string), (double)>
public "getXpValues"(arg0: $EventType$Type, arg1: $CompoundTag$Type): $Map<(string), (long)>
public "getReqs"(arg0: $ReqType$Type, arg1: $CompoundTag$Type): $Map<(string), (integer)>
public "getNegativeEffect"(): $Map<($ResourceLocation), (integer)>
public "setNegativeEffects"(arg0: $Map$Type<($ResourceLocation$Type), (integer)>): void
public "setBonuses"(arg0: $ModifierDataType$Type, arg1: $Map$Type<(string), (double)>): void
public "setXpValues"(arg0: $EventType$Type, arg1: $Map$Type<(string), (long)>): void
public "setReqs"(arg0: $ReqType$Type, arg1: $Map$Type<(string), (integer)>): void
public "setPositiveEffects"(arg0: $Map$Type<($ResourceLocation$Type), (integer)>): void
public "getPositiveEffect"(): $Map<($ResourceLocation), (integer)>
public static "clearEmptyValues"<K, V>(arg0: $Map$Type<(K), (V)>): $HashMap<(K), (V)>
get "unconfigured"(): boolean
get "tagValues"(): $Set<(string)>
get "negativeEffect"(): $Map<($ResourceLocation), (integer)>
set "negativeEffects"(value: $Map$Type<($ResourceLocation$Type), (integer)>)
set "positiveEffects"(value: $Map$Type<($ResourceLocation$Type), (integer)>)
get "positiveEffect"(): $Map<($ResourceLocation), (integer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VeinData$Type = ($VeinData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VeinData_ = $VeinData$Type;
}}
declare module "packages/harmonised/pmmo/config/codecs/$CodecTypes$SalvageData" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CodecTypes$SalvageData extends $Record {

constructor(chancePerLevel: $Map$Type<(string), (double)>, levelReq: $Map$Type<(string), (integer)>, xpAward: $Map$Type<(string), (long)>, salvageMax: integer, baseChance: double, maxChance: double)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "combine"(arg0: $CodecTypes$SalvageData$Type, arg1: $CodecTypes$SalvageData$Type, arg2: boolean, arg3: boolean): $CodecTypes$SalvageData
public "levelReq"(): $Map<(string), (integer)>
public "chancePerLevel"(): $Map<(string), (double)>
public "salvageMax"(): integer
public "baseChance"(): double
public "maxChance"(): double
public "xpAward"(): $Map<(string), (long)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CodecTypes$SalvageData$Type = ($CodecTypes$SalvageData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CodecTypes$SalvageData_ = $CodecTypes$SalvageData$Type;
}}
declare module "packages/harmonised/pmmo/client/events/$WorldRenderHandler" {
import {$RenderLevelStageEvent, $RenderLevelStageEvent$Type} from "packages/net/minecraftforge/client/event/$RenderLevelStageEvent"

export class $WorldRenderHandler {

constructor()

public static "onWorldRender"(arg0: $RenderLevelStageEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldRenderHandler$Type = ($WorldRenderHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldRenderHandler_ = $WorldRenderHandler$Type;
}}
declare module "packages/harmonised/pmmo/setup/datagen/$LangProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$LanguageProvider, $LanguageProvider$Type} from "packages/net/minecraftforge/common/data/$LanguageProvider"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$LangProvider$Translation, $LangProvider$Translation$Type} from "packages/harmonised/pmmo/setup/datagen/$LangProvider$Translation"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export class $LangProvider extends $LanguageProvider {
static readonly "PERK_BREAK_SPEED": $LangProvider$Translation
static readonly "PERK_BREAK_SPEED_DESC": $LangProvider$Translation
static readonly "PERK_BREAK_SPEED_STATUS_1": $LangProvider$Translation
static readonly "PERK_FIREWORK": $LangProvider$Translation
static readonly "PERK_FIREWORK_DESC": $LangProvider$Translation
static readonly "PERK_FIREWORK_STATUS_1": $LangProvider$Translation
static readonly "PERK_JUMP_BOOST": $LangProvider$Translation
static readonly "PERK_JUMP_BOOST_DESC": $LangProvider$Translation
static readonly "PERK_JUMP_BOOST_STATUS_1": $LangProvider$Translation
static readonly "PERK_BREATH": $LangProvider$Translation
static readonly "PERK_BREATH_DESC": $LangProvider$Translation
static readonly "PERK_BREATH_STATUS_1": $LangProvider$Translation
static readonly "PERK_BREATH_STATUS_2": $LangProvider$Translation
static readonly "PERK_DAMAGE_BOOST": $LangProvider$Translation
static readonly "PERK_DAMAGE_BOOST_DESC": $LangProvider$Translation
static readonly "PERK_DAMAGE_BOOST_STATUS_1": $LangProvider$Translation
static readonly "PERK_DAMAGE_BOOST_STATUS_2": $LangProvider$Translation
static readonly "PERK_COMMAND": $LangProvider$Translation
static readonly "PERK_COMMAND_DESC": $LangProvider$Translation
static readonly "PERK_COMMAND_STATUS_1": $LangProvider$Translation
static readonly "PERK_EFFECT": $LangProvider$Translation
static readonly "PERK_EFFECT_DESC": $LangProvider$Translation
static readonly "PERK_EFFECT_STATUS_1": $LangProvider$Translation
static readonly "PERK_EFFECT_STATUS_2": $LangProvider$Translation
static readonly "PERK_FALL_SAVE": $LangProvider$Translation
static readonly "PERK_FALL_SAVE_DESC": $LangProvider$Translation
static readonly "PERK_FALL_SAVE_STATUS_1": $LangProvider$Translation
static readonly "PERK_TAME_BOOST": $LangProvider$Translation
static readonly "PERK_TAME_BOOST_DESC": $LangProvider$Translation
static readonly "PERK_TAME_BOOST_STATUS_1": $LangProvider$Translation
static readonly "PERK_ATTRIBUTE": $LangProvider$Translation
static readonly "PERK_TEMP_ATTRIBUTE": $LangProvider$Translation
static readonly "PERK_ATTRIBUTE_DESC": $LangProvider$Translation
static readonly "PERK_ATTRIBUTE_STATUS_1": $LangProvider$Translation
static readonly "PERK_ATTRIBUTE_STATUS_2": $LangProvider$Translation
static readonly "PERK_ATTRIBUTE_STATUS_3": $LangProvider$Translation
static readonly "PERK_VILLAGER": $LangProvider$Translation
static readonly "PERK_VILLAGER_DESC": $LangProvider$Translation
static readonly "PERK_VILLAGE_STATUS_1": $LangProvider$Translation
static readonly "PERK_VILLAGE_FEEDBACK": $LangProvider$Translation
static readonly "SKILL_HEALTH": $LangProvider$Translation
static readonly "SKILL_SPEED": $LangProvider$Translation
static readonly "SKILL_DAMAGE": $LangProvider$Translation
static readonly "SKILL_POWER": $LangProvider$Translation
static readonly "SKILL_MINING": $LangProvider$Translation
static readonly "SKILL_BUILDING": $LangProvider$Translation
static readonly "SKILL_EXCAVATION": $LangProvider$Translation
static readonly "SKILL_WOODCUTTING": $LangProvider$Translation
static readonly "SKILL_FARMING": $LangProvider$Translation
static readonly "SKILL_AGILITY": $LangProvider$Translation
static readonly "SKILL_ENDURANCE": $LangProvider$Translation
static readonly "SKILL_COMBAT": $LangProvider$Translation
static readonly "SKILL_ARCHERY": $LangProvider$Translation
static readonly "SKILL_SMITHING": $LangProvider$Translation
static readonly "SKILL_FLYING": $LangProvider$Translation
static readonly "SKILL_SWIMMING": $LangProvider$Translation
static readonly "SKILL_FISHING": $LangProvider$Translation
static readonly "SKILL_CRAFTING": $LangProvider$Translation
static readonly "SKILL_MAGIC": $LangProvider$Translation
static readonly "SKILL_GUNSLINGING": $LangProvider$Translation
static readonly "SKILL_SLAYER": $LangProvider$Translation
static readonly "SKILL_FLETCHING": $LangProvider$Translation
static readonly "SKILL_TAMING": $LangProvider$Translation
static readonly "SKILL_HUNTER": $LangProvider$Translation
static readonly "SKILL_ENGINEERING": $LangProvider$Translation
static readonly "SKILL_BLOOD_MAGIC": $LangProvider$Translation
static readonly "SKILL_ASTRAL_MAGIC": $LangProvider$Translation
static readonly "SKILL_GOOD_MAGIC": $LangProvider$Translation
static readonly "SKILL_EVIL_MAGIC": $LangProvider$Translation
static readonly "SKILL_ARCANE_MAGIC": $LangProvider$Translation
static readonly "SKILL_ELEMENTAL": $LangProvider$Translation
static readonly "SKILL_EARTH": $LangProvider$Translation
static readonly "SKILL_WATER": $LangProvider$Translation
static readonly "SKILL_AIR": $LangProvider$Translation
static readonly "SKILL_FIRE": $LangProvider$Translation
static readonly "SKILL_LIGHTNING": $LangProvider$Translation
static readonly "SKILL_VOID": $LangProvider$Translation
static readonly "SKILL_THAUMATIC": $LangProvider$Translation
static readonly "SKILL_SUMMONING": $LangProvider$Translation
static readonly "SKILL_INVENTION": $LangProvider$Translation
static readonly "SKILL_RUNECRAFTING": $LangProvider$Translation
static readonly "SKILL_PRAYER": $LangProvider$Translation
static readonly "SKILL_COOKING": $LangProvider$Translation
static readonly "SKILL_FIREMAKING": $LangProvider$Translation
static readonly "SKILL_AFKING": $LangProvider$Translation
static readonly "SKILL_TRADING": $LangProvider$Translation
static readonly "SKILL_SAILING": $LangProvider$Translation
static readonly "SKILL_ALCHEMY": $LangProvider$Translation
static readonly "SKILL_CONSTRUCTION": $LangProvider$Translation
static readonly "SKILL_LEATHERWORKING": $LangProvider$Translation
static readonly "SKILL_EXPLORATION": $LangProvider$Translation
static readonly "SKILL_CHARISMA": $LangProvider$Translation
static readonly "ENUM_ANVIL_REPAIR": $LangProvider$Translation
static readonly "ENUM_BLOCK_BREAK": $LangProvider$Translation
static readonly "ENUM_BREAK_SPEED": $LangProvider$Translation
static readonly "ENUM_BLOCK_PLACE": $LangProvider$Translation
static readonly "ENUM_BREATH_CHANGE": $LangProvider$Translation
static readonly "ENUM_BREED": $LangProvider$Translation
static readonly "ENUM_BREW": $LangProvider$Translation
static readonly "ENUM_CRAFT": $LangProvider$Translation
static readonly "ENUM_CONSUME": $LangProvider$Translation
static readonly "ENUM_CROUCH": $LangProvider$Translation
static readonly "ENUM_RECEIVE_DAMAGE": $LangProvider$Translation
static readonly "ENUM_FROM_MOBS": $LangProvider$Translation
static readonly "ENUM_FROM_PLAYERS": $LangProvider$Translation
static readonly "ENUM_FROM_ANIMALS": $LangProvider$Translation
static readonly "ENUM_FROM_PROJECTILES": $LangProvider$Translation
static readonly "ENUM_FROM_MAGIC": $LangProvider$Translation
static readonly "ENUM_FROM_ENVIRONMENT": $LangProvider$Translation
static readonly "ENUM_FROM_IMPACT": $LangProvider$Translation
static readonly "ENUM_DEAL_MELEE_DAMAGE": $LangProvider$Translation
static readonly "ENUM_MELEE_TO_MOBS": $LangProvider$Translation
static readonly "ENUM_MELEE_TO_PLAYERS": $LangProvider$Translation
static readonly "ENUM_MELEE_TO_ANIMALS": $LangProvider$Translation
static readonly "ENUM_DEAL_RANGED_DAMAGE": $LangProvider$Translation
static readonly "ENUM_RANGED_TO_MOBS": $LangProvider$Translation
static readonly "ENUM_RANGED_TO_PLAYERS": $LangProvider$Translation
static readonly "ENUM_RANGED_TO_ANIMALS": $LangProvider$Translation
static readonly "ENUM_DEATH": $LangProvider$Translation
static readonly "ENUM_ENCHANT": $LangProvider$Translation
static readonly "ENUM_FISH": $LangProvider$Translation
static readonly "ENUM_SMELT": $LangProvider$Translation
static readonly "ENUM_GROW": $LangProvider$Translation
static readonly "ENUM_HEALTH_CHANGE": $LangProvider$Translation
static readonly "ENUM_JUMP": $LangProvider$Translation
static readonly "ENUM_SPRINT_JUMP": $LangProvider$Translation
static readonly "ENUM_CROUCH_JUMP": $LangProvider$Translation
static readonly "ENUM_WORLD_CONNECT": $LangProvider$Translation
static readonly "ENUM_WORLD_DISCONNECT": $LangProvider$Translation
static readonly "ENUM_HIT_BLOCK": $LangProvider$Translation
static readonly "ENUM_ACTIVATE_BLOCK": $LangProvider$Translation
static readonly "ENUM_ACTIVATE_ITEM": $LangProvider$Translation
static readonly "ENUM_ENTITY": $LangProvider$Translation
static readonly "ENUM_EFFECT": $LangProvider$Translation
static readonly "ENUM_RESPAWN": $LangProvider$Translation
static readonly "ENUM_RIDING": $LangProvider$Translation
static readonly "ENUM_SHIELD_BLOCK": $LangProvider$Translation
static readonly "ENUM_SKILL_UP": $LangProvider$Translation
static readonly "ENUM_SLEEP": $LangProvider$Translation
static readonly "ENUM_SPRINTING": $LangProvider$Translation
static readonly "ENUM_SUBMERGED": $LangProvider$Translation
static readonly "ENUM_SWIMMING": $LangProvider$Translation
static readonly "ENUM_DIVING": $LangProvider$Translation
static readonly "ENUM_SURFACING": $LangProvider$Translation
static readonly "ENUM_SWIM_SPRINTING": $LangProvider$Translation
static readonly "ENUM_TAMING": $LangProvider$Translation
static readonly "ENUM_VEIN_MINE": $LangProvider$Translation
static readonly "ENUM_DISABLE_PERK": $LangProvider$Translation
static readonly "ENUM_WEAR": $LangProvider$Translation
static readonly "ENUM_USE_ENCHANTMENT": $LangProvider$Translation
static readonly "ENUM_TOOL": $LangProvider$Translation
static readonly "ENUM_WEAPON": $LangProvider$Translation
static readonly "ENUM_USE": $LangProvider$Translation
static readonly "ENUM_PLACE": $LangProvider$Translation
static readonly "ENUM_BREAK": $LangProvider$Translation
static readonly "ENUM_KILL": $LangProvider$Translation
static readonly "ENUM_TRAVEL": $LangProvider$Translation
static readonly "ENUM_RIDE": $LangProvider$Translation
static readonly "ENUM_TAME": $LangProvider$Translation
static readonly "ENUM_INTERACT": $LangProvider$Translation
static readonly "ENUM_ENTITY_INTERACT": $LangProvider$Translation
static readonly "ENUM_BIOME": $LangProvider$Translation
static readonly "KEYBIND_CATEGORY": $LangProvider$Translation
static readonly "KEYBIND_SHOWVEIN": $LangProvider$Translation
static readonly "KEYBIND_ADDVEIN": $LangProvider$Translation
static readonly "KEYBIND_SUBVEIN": $LangProvider$Translation
static readonly "KEYBIND_VEINCYCLE": $LangProvider$Translation
static readonly "KEYBIND_SHOWLIST": $LangProvider$Translation
static readonly "KEYBIND_VEIN": $LangProvider$Translation
static readonly "KEYBIND_OPENMENU": $LangProvider$Translation
static readonly "WELCOME_TEXT": $LangProvider$Translation
static readonly "CLICK_ME": $LangProvider$Translation
static readonly "VEIN_BLACKLIST": $LangProvider$Translation
static readonly "VEIN_SHAPE": $LangProvider$Translation
static readonly "LEVEL_UP_HEADER": $LangProvider$Translation
static readonly "REQ_WEAR": $LangProvider$Translation
static readonly "REQ_TOOL": $LangProvider$Translation
static readonly "REQ_WEAPON": $LangProvider$Translation
static readonly "REQ_USE": $LangProvider$Translation
static readonly "REQ_PLACE": $LangProvider$Translation
static readonly "REQ_ENCHANT": $LangProvider$Translation
static readonly "REQ_INTERACT": $LangProvider$Translation
static readonly "REQ_BREAK": $LangProvider$Translation
static readonly "XP_VALUE_ANVIL": $LangProvider$Translation
static readonly "XP_VALUE_BREAK": $LangProvider$Translation
static readonly "XP_VALUE_CRAFT": $LangProvider$Translation
static readonly "XP_VALUE_CONSUME": $LangProvider$Translation
static readonly "XP_VALUE_DEAL_DAMAGE": $LangProvider$Translation
static readonly "XP_VALUE_ENCHANT": $LangProvider$Translation
static readonly "XP_VALUE_FISH": $LangProvider$Translation
static readonly "XP_VALUE_SMELT": $LangProvider$Translation
static readonly "XP_VALUE_TRADE_GIVE": $LangProvider$Translation
static readonly "XP_VALUE_TRADE_GET": $LangProvider$Translation
static readonly "XP_VALUE_HIT_BLOCK": $LangProvider$Translation
static readonly "XP_VALUE_ACTIVATE_BLOCK": $LangProvider$Translation
static readonly "XP_VALUE_USE": $LangProvider$Translation
static readonly "XP_VALUE_BREW": $LangProvider$Translation
static readonly "XP_VALUE_GROW": $LangProvider$Translation
static readonly "XP_VALUE_PLACE": $LangProvider$Translation
static readonly "BOOST_HELD": $LangProvider$Translation
static readonly "BOOST_WORN": $LangProvider$Translation
static readonly "VEIN_TOOLTIP": $LangProvider$Translation
static readonly "VEIN_DATA": $LangProvider$Translation
static readonly "VEIN_BREAK": $LangProvider$Translation
static readonly "OPEN_GLOSSARY": $LangProvider$Translation
static readonly "EVENT_HEADER": $LangProvider$Translation
static readonly "REQ_HEADER": $LangProvider$Translation
static readonly "REQ_EFFECTS_HEADER": $LangProvider$Translation
static readonly "MODIFIER_HEADER": $LangProvider$Translation
static readonly "SALVAGE_HEADER": $LangProvider$Translation
static readonly "SALVAGE_LEVEL_REQ": $LangProvider$Translation
static readonly "SALVAGE_CHANCE": $LangProvider$Translation
static readonly "SALVAGE_MAX": $LangProvider$Translation
static readonly "SALVAGE_CHANCE_MOD": $LangProvider$Translation
static readonly "SALVAGE_XP_AWARD": $LangProvider$Translation
static readonly "VEIN_HEADER": $LangProvider$Translation
static readonly "VEIN_RATE": $LangProvider$Translation
static readonly "VEIN_CAP": $LangProvider$Translation
static readonly "VEIN_CONSUME": $LangProvider$Translation
static readonly "PLAYER_HEADER": $LangProvider$Translation
static readonly "PLAYER_IGNORE_REQ": $LangProvider$Translation
static readonly "PLAYER_BONUSES": $LangProvider$Translation
static readonly "SKILL_LIST_HEADER": $LangProvider$Translation
static readonly "DIMENSION_HEADER": $LangProvider$Translation
static readonly "VEIN_BLACKLIST_HEADER": $LangProvider$Translation
static readonly "MOB_MODIFIER_HEADER": $LangProvider$Translation
static readonly "BIOME_HEADER": $LangProvider$Translation
static readonly "BIOME_EFFECT_NEG": $LangProvider$Translation
static readonly "BIOME_EFFECT_POS": $LangProvider$Translation
static readonly "ADDON_AFFECTED_ATTRIBUTE": $LangProvider$Translation
static readonly "GLOSSARY_DEFAULT_SECTION": $LangProvider$Translation
static readonly "GLOSSARY_SECTION_REQ": $LangProvider$Translation
static readonly "GLOSSARY_SECTION_XP": $LangProvider$Translation
static readonly "GLOSSARY_SECTION_BONUS": $LangProvider$Translation
static readonly "GLOSSARY_SECTION_SALVAGE": $LangProvider$Translation
static readonly "GLOSSARY_SECTION_VEIN": $LangProvider$Translation
static readonly "GLOSSARY_SECTION_MOB": $LangProvider$Translation
static readonly "GLOSSARY_SECTION_PERKS": $LangProvider$Translation
static readonly "GLOSSARY_DEFAULT_OBJECT": $LangProvider$Translation
static readonly "GLOSSARY_OBJECT_ITEMS": $LangProvider$Translation
static readonly "GLOSSARY_OBJECT_BLOCKS": $LangProvider$Translation
static readonly "GLOSSARY_OBJECT_ENTITIES": $LangProvider$Translation
static readonly "GLOSSARY_OBJECT_DIMENSIONS": $LangProvider$Translation
static readonly "GLOSSARY_OBJECT_BIOMES": $LangProvider$Translation
static readonly "GLOSSARY_OBJECT_ENCHANTS": $LangProvider$Translation
static readonly "GLOSSARY_OBJECT_EFFECTS": $LangProvider$Translation
static readonly "GLOSSARY_OBJECT_PERKS": $LangProvider$Translation
static readonly "GLOSSARY_DEFAULT_SKILL": $LangProvider$Translation
static readonly "GLOSSARY_DEFAULT_ENUM": $LangProvider$Translation
static readonly "GLOSSARY_VIEW_BUTTON": $LangProvider$Translation
static readonly "FOUND_TREASURE": $LangProvider$Translation
static readonly "LEVELED_UP": $LangProvider$Translation
static readonly "PERK_BREATH_REFRESH": $LangProvider$Translation
static readonly "VEIN_LIMIT": $LangProvider$Translation
static readonly "VEIN_CHARGE": $LangProvider$Translation
static readonly "SET_LEVEL": $LangProvider$Translation
static readonly "SET_XP": $LangProvider$Translation
static readonly "ADD_LEVEL": $LangProvider$Translation
static readonly "ADD_XP": $LangProvider$Translation
static readonly "PARTY_ALREADY_IN": $LangProvider$Translation
static readonly "PARTY_CREATED": $LangProvider$Translation
static readonly "PARTY_LEFT": $LangProvider$Translation
static readonly "PARTY_NOT_IN": $LangProvider$Translation
static readonly "PARTY_INVITE": $LangProvider$Translation
static readonly "PARTY_MEMBER_TOTAL": $LangProvider$Translation
static readonly "PARTY_MEMBER_LIST": $LangProvider$Translation
static readonly "PARTY_DECLINE": $LangProvider$Translation
static readonly "PARTY_NO_INVITES": $LangProvider$Translation
static readonly "PARTY_JOINED": $LangProvider$Translation
static readonly "PARTY_RESCIND_INVITE": $LangProvider$Translation
static readonly "PARTY_ACCEPT": $LangProvider$Translation
static readonly "PARTY_DECLINE_INVITE": $LangProvider$Translation
static readonly "PARTY_PLAYER_INVITED": $LangProvider$Translation
static readonly "PACK_BEGIN": $LangProvider$Translation
static readonly "PACK_OVERRIDE": $LangProvider$Translation
static readonly "PACK_DEFAULTS": $LangProvider$Translation
static readonly "PACK_DISABLER": $LangProvider$Translation
static readonly "PACK_PLAYERS": $LangProvider$Translation
static readonly "PACK_SIMPLE": $LangProvider$Translation
static readonly "PACK_FILTER": $LangProvider$Translation
static readonly "DENIAL_WEAR": $LangProvider$Translation
static readonly "DENIAL_USE_ENCHANT": $LangProvider$Translation
static readonly "DENIAL_TOOL": $LangProvider$Translation
static readonly "DENIAL_WEAPON": $LangProvider$Translation
static readonly "DENIAL_USE": $LangProvider$Translation
static readonly "DENIAL_PLACE": $LangProvider$Translation
static readonly "DENIAL_BREAK": $LangProvider$Translation
static readonly "DENIAL_BIOME": $LangProvider$Translation
static readonly "DENIAL_KILL": $LangProvider$Translation
static readonly "DENIAL_TRAVEL": $LangProvider$Translation
static readonly "DENIAL_RIDE": $LangProvider$Translation
static readonly "DENIAL_TAME": $LangProvider$Translation
static readonly "DENIAL_ENTITY_INTERACT": $LangProvider$Translation
static readonly "DENIAL_SALVAGE": $LangProvider$Translation
static readonly "DENIAL_INTERACT": $LangProvider$Translation
static readonly "SALVAGE_TUTORIAL_HEADER": $LangProvider$Translation
static readonly "SALVAGE_TUTORIAL_USAGE": $LangProvider$Translation
static readonly "FTBQ_XP_TITLE": $LangProvider$Translation
static readonly "FTBQ_XP_SKILL": $LangProvider$Translation
static readonly "FTBQ_XP_AMOUNT": $LangProvider$Translation
static readonly "FTBQ_LVL_TITLE": $LangProvider$Translation
static readonly "FTBQ_LVL_SKILL": $LangProvider$Translation
static readonly "FTBQ_LVL_AMOUNT": $LangProvider$Translation
static readonly "FTBQ_SKILL_TITLE": $LangProvider$Translation
static readonly "FTBQ_SKILL_SKILL": $LangProvider$Translation
static readonly "FTBQ_SKILL_LEVEL": $LangProvider$Translation

constructor(arg0: $PackOutput$Type, arg1: string)

public static "skill"(arg0: string): $MutableComponent
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LangProvider$Type = ($LangProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LangProvider_ = $LangProvider$Type;
}}
declare module "packages/harmonised/pmmo/features/loot_modifiers/$SkillUpTrigger" {
import {$SkillUpTrigger$TriggerInstance, $SkillUpTrigger$TriggerInstance$Type} from "packages/harmonised/pmmo/features/loot_modifiers/$SkillUpTrigger$TriggerInstance"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$SimpleCriterionTrigger, $SimpleCriterionTrigger$Type} from "packages/net/minecraft/advancements/critereon/$SimpleCriterionTrigger"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $SkillUpTrigger extends $SimpleCriterionTrigger<($SkillUpTrigger$TriggerInstance)> {
static readonly "SKILL_UP": $SkillUpTrigger

constructor()

public "trigger"(arg0: $ServerPlayer$Type): void
public "getId"(): $ResourceLocation
get "id"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SkillUpTrigger$Type = ($SkillUpTrigger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SkillUpTrigger_ = $SkillUpTrigger$Type;
}}
declare module "packages/harmonised/pmmo/core/nbt/$NBTUtils" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LogicEntry, $LogicEntry$Type} from "packages/harmonised/pmmo/core/nbt/$LogicEntry"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $NBTUtils {

constructor()

public static "getBonuses"(arg0: $List$Type<($LogicEntry$Type)>, arg1: $CompoundTag$Type): $Map<(string), (double)>
public static "getExperienceAward"(arg0: $List$Type<($LogicEntry$Type)>, arg1: $CompoundTag$Type): $Map<(string), (long)>
public static "getRequirement"(arg0: $List$Type<($LogicEntry$Type)>, arg1: $CompoundTag$Type): $Map<(string), (integer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NBTUtils$Type = ($NBTUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NBTUtils_ = $NBTUtils$Type;
}}
declare module "packages/harmonised/pmmo/compat/ftb_quests/$SkillTask" {
import {$Quest, $Quest$Type} from "packages/dev/ftb/mods/ftbquests/quest/$Quest"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ConfigGroup, $ConfigGroup$Type} from "packages/dev/ftb/mods/ftblibrary/config/$ConfigGroup"
import {$Task, $Task$Type} from "packages/dev/ftb/mods/ftbquests/quest/task/$Task"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$TaskType, $TaskType$Type} from "packages/dev/ftb/mods/ftbquests/quest/task/$TaskType"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$TeamData, $TeamData$Type} from "packages/dev/ftb/mods/ftbquests/quest/$TeamData"

export class $SkillTask extends $Task {
static "SKILL": $TaskType
 "skill": string
 "requiredLevel": integer
readonly "id": long

constructor(arg0: long, arg1: $Quest$Type)

public "getType"(): $TaskType
public "readData"(arg0: $CompoundTag$Type): void
public "submitTask"(arg0: $TeamData$Type, arg1: $ServerPlayer$Type, arg2: $ItemStack$Type): void
public "getAltTitle"(): $Component
public "autoSubmitOnPlayerTick"(): integer
public "getMaxProgress"(): long
public "writeNetData"(arg0: $FriendlyByteBuf$Type): void
public "writeData"(arg0: $CompoundTag$Type): void
public "fillConfigGroup"(arg0: $ConfigGroup$Type): void
public "readNetData"(arg0: $FriendlyByteBuf$Type): void
get "type"(): $TaskType
get "altTitle"(): $Component
get "maxProgress"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SkillTask$Type = ($SkillTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SkillTask_ = $SkillTask$Type;
}}
declare module "packages/harmonised/pmmo/config/writers/$AutoValueWriter" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $AutoValueWriter {

constructor()

public static "dumpObjectConfigToFile"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoValueWriter$Type = ($AutoValueWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoValueWriter_ = $AutoValueWriter$Type;
}}
declare module "packages/harmonised/pmmo/api/enums/$EventType" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$IExtensibleEnum, $IExtensibleEnum$Type} from "packages/net/minecraftforge/common/$IExtensibleEnum"
import {$GuiEnumGroup, $GuiEnumGroup$Type} from "packages/harmonised/pmmo/client/gui/component/$GuiEnumGroup"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$LangProvider$Translation, $LangProvider$Translation$Type} from "packages/harmonised/pmmo/setup/datagen/$LangProvider$Translation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $EventType extends $Enum<($EventType)> implements $StringRepresentable, $IExtensibleEnum, $GuiEnumGroup {
static readonly "ANVIL_REPAIR": $EventType
static readonly "BLOCK_BREAK": $EventType
static readonly "BREAK_SPEED": $EventType
static readonly "BLOCK_PLACE": $EventType
static readonly "BREATH_CHANGE": $EventType
static readonly "BREED": $EventType
static readonly "BREW": $EventType
static readonly "CONSUME": $EventType
static readonly "CRAFT": $EventType
static readonly "CROUCH": $EventType
static readonly "RECEIVE_DAMAGE": $EventType
static readonly "DEAL_DAMAGE": $EventType
static readonly "DEATH": $EventType
static readonly "ENCHANT": $EventType
static readonly "EFFECT": $EventType
static readonly "FISH": $EventType
static readonly "SMELT": $EventType
static readonly "GROW": $EventType
static readonly "GIVEN_AS_TRADE": $EventType
/**
 * 
 * @deprecated
 */
static readonly "HEALTH_CHANGE": $EventType
static readonly "HEALTH_INCREASE": $EventType
static readonly "HEALTH_DECREASE": $EventType
static readonly "JUMP": $EventType
static readonly "SPRINT_JUMP": $EventType
static readonly "CROUCH_JUMP": $EventType
static readonly "HIT_BLOCK": $EventType
static readonly "ACTIVATE_BLOCK": $EventType
static readonly "ACTIVATE_ITEM": $EventType
static readonly "ENTITY": $EventType
static readonly "RECEIVED_AS_TRADE": $EventType
static readonly "RIDING": $EventType
static readonly "SHIELD_BLOCK": $EventType
static readonly "SKILL_UP": $EventType
static readonly "SPRINTING": $EventType
static readonly "SUBMERGED": $EventType
static readonly "SWIMMING": $EventType
static readonly "DIVING": $EventType
static readonly "SURFACING": $EventType
static readonly "SWIM_SPRINTING": $EventType
static readonly "TAMING": $EventType
readonly "itemApplicable": boolean
readonly "blockApplicable": boolean
readonly "entityApplicable": boolean
readonly "autoValueSkill": string
readonly "tooltipTranslation": $LangProvider$Translation
static readonly "ITEM_APPLICABLE_EVENTS": ($EventType)[]
static readonly "BLOCK_APPLICABLE_EVENTS": ($EventType)[]
static readonly "ENTITY_APPLICABLE_EVENTS": ($EventType)[]
static readonly "BLOCKITEM_APPLICABLE_EVENTS": ($EventType)[]
static readonly "CODEC": $Codec<($EventType)>


public "getName"(): string
public static "values"(): ($EventType)[]
public static "valueOf"(arg0: string): $EventType
public static "create"(arg0: string, arg1: boolean, arg2: boolean, arg3: boolean, arg4: string, arg5: $LangProvider$Translation$Type): $EventType
public "getSerializedName"(): string
public static "byName"(arg0: string): $EventType
public static "fromEnumWithMapping"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
public static "keys"(arg0: ($StringRepresentable$Type)[]): $Keyable
public static "fromEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
/**
 * 
 * @deprecated
 */
public "init"(): void
public static "createCodecForExtensibleEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(any), (any)>): $Codec<(E)>
get "name"(): string
get "serializedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventType$Type = (("health_increase") | ("shield_block") | ("death") | ("health_change") | ("craft") | ("receive_damage") | ("consume") | ("riding") | ("sprint_jump") | ("hit_block") | ("crouch") | ("activate_item") | ("swimming") | ("swim_sprinting") | ("diving") | ("smelt") | ("activate_block") | ("anvil_repair") | ("received_as_trade") | ("jump") | ("submerged") | ("grow") | ("given_as_trade") | ("health_decrease") | ("block_place") | ("surfacing") | ("brew") | ("breed") | ("sprinting") | ("break_speed") | ("block_break") | ("enchant") | ("effect") | ("fish") | ("deal_damage") | ("breath_change") | ("skill_up") | ("crouch_jump") | ("entity") | ("taming")) | ($EventType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventType_ = $EventType$Type;
}}
declare module "packages/harmonised/pmmo/commands/$CmdNodeAdmin" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$ArgumentBuilder, $ArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$ArgumentBuilder"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"
import {$CommandContext, $CommandContext$Type} from "packages/com/mojang/brigadier/context/$CommandContext"

export class $CmdNodeAdmin {

constructor()

public static "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>): $ArgumentBuilder<($CommandSourceStack), (any)>
public static "adminSetOrAdd"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>, arg1: boolean): integer
public static "exemptAdmin"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>): integer
public static "adminBonuses"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>): integer
public static "displayPlayer"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>): integer
public static "adminClear"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CmdNodeAdmin$Type = ($CmdNodeAdmin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CmdNodeAdmin_ = $CmdNodeAdmin$Type;
}}
declare module "packages/harmonised/pmmo/client/events/$KeyPressHandler" {
import {$InputEvent$Key, $InputEvent$Key$Type} from "packages/net/minecraftforge/client/event/$InputEvent$Key"

export class $KeyPressHandler {

constructor()

public static "keyPressEvent"(arg0: $InputEvent$Key$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyPressHandler$Type = ($KeyPressHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyPressHandler_ = $KeyPressHandler$Type;
}}
declare module "packages/harmonised/pmmo/features/autovalues/$AutoEntity" {
import {$ReqType, $ReqType$Type} from "packages/harmonised/pmmo/api/enums/$ReqType"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AutoEntity {
static readonly "EVENTTYPES": ($EventType)[]

constructor()

public static "processReqs"(arg0: $ReqType$Type, arg1: $ResourceLocation$Type): $Map<(string), (integer)>
public static "processXpGains"(arg0: $EventType$Type, arg1: $ResourceLocation$Type): $Map<(string), (long)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoEntity$Type = ($AutoEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoEntity_ = $AutoEntity$Type;
}}
declare module "packages/harmonised/pmmo/network/clientpackets/$CP_UpdateLevelCache" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $CP_UpdateLevelCache {

constructor(arg0: $List$Type<(long)>)
constructor(arg0: $FriendlyByteBuf$Type)

public "toBytes"(arg0: $FriendlyByteBuf$Type): void
public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CP_UpdateLevelCache$Type = ($CP_UpdateLevelCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CP_UpdateLevelCache_ = $CP_UpdateLevelCache$Type;
}}
declare module "packages/harmonised/pmmo/api/$APIUtils" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$PerkSide, $PerkSide$Type} from "packages/harmonised/pmmo/api/enums/$PerkSide"
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$APIUtils$SalvageBuilder, $APIUtils$SalvageBuilder$Type} from "packages/harmonised/pmmo/api/$APIUtils$SalvageBuilder"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Perk, $Perk$Type} from "packages/harmonised/pmmo/api/perks/$Perk"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$LogicalSide, $LogicalSide$Type} from "packages/net/minecraftforge/fml/$LogicalSide"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ReqType, $ReqType$Type} from "packages/harmonised/pmmo/api/enums/$ReqType"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ModifierDataType, $ModifierDataType$Type} from "packages/harmonised/pmmo/api/enums/$ModifierDataType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$ObjectType, $ObjectType$Type} from "packages/harmonised/pmmo/api/enums/$ObjectType"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $APIUtils {
static readonly "MOB_HEALTH": string
static readonly "MOB_SPEED": string
static readonly "MOB_DAMAGE": string
static readonly "IS_CANCELLED": string
static readonly "DENY_ITEM_USE": string
static readonly "DENY_BLOCK_USE": string
static readonly "PER_LEVEL": string
static readonly "MAX_BOOST": string
static readonly "RATIO": string
static readonly "MODIFIER": string
static readonly "MIN_LEVEL": string
static readonly "MAX_LEVEL": string
static readonly "MILESTONES": string
static readonly "MODULUS": string
static readonly "CHANCE": string
static readonly "COOLDOWN": string
static readonly "DURATION": string
static readonly "TARGET": string
static readonly "ENTITY_ID": string
static readonly "BLOCK_POS": string
static readonly "SKILLNAME": string
static readonly "SKILL_LEVEL": string
static readonly "BREAK_SPEED_INPUT_VALUE": string
static readonly "BREAK_SPEED_OUTPUT_VALUE": string
static readonly "DAMAGE_TYPE_IN": string
static readonly "DAMAGE_TYPE": string
static readonly "DAMAGE_IN": string
static readonly "DAMAGE_OUT": string
static readonly "ATTRIBUTE": string
static readonly "JUMP_OUT": string
static readonly "STACK": string
static readonly "PLAYER_ID": string
static readonly "ENCHANT_LEVEL": string
static readonly "ENCHANT_NAME": string
static readonly "AMBIENT": string
static readonly "VISIBLE": string
static readonly "SHOW_ICON": string
static readonly "EFFECTS": string
static readonly "MULTIPLICATIVE": string
static readonly "BASE": string
static readonly "CHANCE_SUCCESS_MSG": string
static readonly "SERIALIZED_AWARD_MAP": string

constructor()

public static "getLevel"(arg0: string, arg1: $Player$Type): integer
public static "setLevel"(arg0: string, arg1: $Player$Type, arg2: integer): void
public static "registerPerk"(arg0: $ResourceLocation$Type, arg1: $Perk$Type, arg2: $PerkSide$Type): void
public static "registerListener"(arg0: $ResourceLocation$Type, arg1: $EventType$Type, arg2: $BiFunction$Type<(any), ($CompoundTag$Type), ($CompoundTag$Type)>): void
public static "getXpAwardMap"(arg0: $ItemStack$Type, arg1: $EventType$Type, arg2: $LogicalSide$Type, arg3: $Player$Type): $Map<(string), (long)>
public static "getXpAwardMap"(arg0: $Entity$Type, arg1: $EventType$Type, arg2: $LogicalSide$Type, arg3: $Player$Type): $Map<(string), (long)>
public static "getXpAwardMap"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $EventType$Type, arg3: $Player$Type): $Map<(string), (long)>
public static "getXpAwardMap"(arg0: $ObjectType$Type, arg1: $EventType$Type, arg2: $ResourceLocation$Type, arg3: $LogicalSide$Type, arg4: $Player$Type): $Map<(string), (long)>
public static "getAllLevels"(arg0: $Player$Type): $Map<(string), (integer)>
public static "getRawXpMap"(arg0: $Player$Type): $Map<(string), (long)>
public static "addLevel"(arg0: string, arg1: $Player$Type, arg2: integer): boolean
public static "registerXpAward"(arg0: $ObjectType$Type, arg1: $ResourceLocation$Type, arg2: $EventType$Type, arg3: $Map$Type<(string), (long)>, arg4: boolean): void
public static "registerSalvage"(arg0: $ResourceLocation$Type, arg1: $Map$Type<($ResourceLocation$Type), ($APIUtils$SalvageBuilder$Type)>, arg2: boolean): void
public static "registerVeinData"(arg0: $ObjectType$Type, arg1: $ResourceLocation$Type, arg2: $Optional$Type<(integer)>, arg3: $Optional$Type<(double)>, arg4: $Optional$Type<(integer)>, arg5: boolean): void
public static "getRequirementMap"(arg0: $BlockPos$Type, arg1: $Level$Type, arg2: $ReqType$Type): $Map<(string), (integer)>
public static "getRequirementMap"(arg0: $Entity$Type, arg1: $ReqType$Type, arg2: $LogicalSide$Type): $Map<(string), (integer)>
public static "getRequirementMap"(arg0: $ItemStack$Type, arg1: $ReqType$Type, arg2: $LogicalSide$Type): $Map<(string), (integer)>
public static "getRequirementMap"(arg0: $ObjectType$Type, arg1: $ResourceLocation$Type, arg2: $ReqType$Type, arg3: $LogicalSide$Type): $Map<(string), (integer)>
public static "registerBonus"(arg0: $ObjectType$Type, arg1: $ResourceLocation$Type, arg2: $ModifierDataType$Type, arg3: $Map$Type<(string), (double)>, arg4: boolean): void
public static "serializeAwardMap"(arg0: $Map$Type<(string), (long)>): $CompoundTag
public static "addXp"(arg0: string, arg1: $Player$Type, arg2: long): boolean
public static "registerBreakPredicate"(arg0: $ResourceLocation$Type, arg1: $ReqType$Type, arg2: $BiPredicate$Type<($Player$Type), ($BlockEntity$Type)>): void
public static "registerItemXpGainTooltipData"(arg0: $ResourceLocation$Type, arg1: $EventType$Type, arg2: $Function$Type<($ItemStack$Type), ($Map$Type<(string), (long)>)>): void
public static "registerBlockXpGainTooltipData"(arg0: $ResourceLocation$Type, arg1: $EventType$Type, arg2: $Function$Type<($BlockEntity$Type), ($Map$Type<(string), (long)>)>): void
public static "registerItemRequirementTooltipData"(arg0: $ResourceLocation$Type, arg1: $ReqType$Type, arg2: $Function$Type<($ItemStack$Type), ($Map$Type<(string), (integer)>)>): void
public static "registerEntityPredicate"(arg0: $ResourceLocation$Type, arg1: $ReqType$Type, arg2: $BiPredicate$Type<($Player$Type), ($Entity$Type)>): void
public static "registerEntityXpGainTooltipData"(arg0: $ResourceLocation$Type, arg1: $EventType$Type, arg2: $Function$Type<($Entity$Type), ($Map$Type<(string), (long)>)>): void
public static "registerLevelProvider"(arg0: $BiFunction$Type<(string), (integer), (integer)>, arg1: $FMLCommonSetupEvent$Type): void
public static "registerBlockRequirementTooltipData"(arg0: $ResourceLocation$Type, arg1: $ReqType$Type, arg2: $Function$Type<($BlockEntity$Type), ($Map$Type<(string), (integer)>)>): void
public static "registerEntityRequirementTooltipData"(arg0: $ResourceLocation$Type, arg1: $ReqType$Type, arg2: $Function$Type<($Entity$Type), ($Map$Type<(string), (integer)>)>): void
public static "setXp"(arg0: string, arg1: $Player$Type, arg2: long): void
public static "getXp"(arg0: string, arg1: $Player$Type): long
public static "registerDamageXpAward"(arg0: $ObjectType$Type, arg1: $ResourceLocation$Type, arg2: boolean, arg3: string, arg4: $Map$Type<(string), (long)>, arg5: boolean): void
public static "registerRequirement"(arg0: $ObjectType$Type, arg1: $ResourceLocation$Type, arg2: $ReqType$Type, arg3: $Map$Type<(string), (integer)>, arg4: boolean): void
public static "registerPositiveEffect"(arg0: $ObjectType$Type, arg1: $ResourceLocation$Type, arg2: $Map$Type<($ResourceLocation$Type), (integer)>, arg3: boolean): void
public static "registerMobModifier"(arg0: $ObjectType$Type, arg1: $ResourceLocation$Type, arg2: $Map$Type<($ResourceLocation$Type), ($Map$Type<(string), (double)>)>, arg3: boolean): void
public static "registerItemBonusData"(arg0: $ResourceLocation$Type, arg1: $ModifierDataType$Type, arg2: $Function$Type<($ItemStack$Type), ($Map$Type<(string), (double)>)>): void
public static "registerNegativeEffect"(arg0: $ObjectType$Type, arg1: $ResourceLocation$Type, arg2: $Map$Type<($ResourceLocation$Type), (integer)>, arg3: boolean): void
public static "registerActionPredicate"(arg0: $ResourceLocation$Type, arg1: $ReqType$Type, arg2: $BiPredicate$Type<($Player$Type), ($ItemStack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $APIUtils$Type = ($APIUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $APIUtils_ = $APIUtils$Type;
}}
declare module "packages/harmonised/pmmo/config/readers/$ConfigHelper$ConfigObject" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $ConfigHelper$ConfigObject<T> implements $Supplier<(T)> {


public "get"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigHelper$ConfigObject$Type<T> = ($ConfigHelper$ConfigObject<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigHelper$ConfigObject_<T> = $ConfigHelper$ConfigObject$Type<(T)>;
}}
declare module "packages/harmonised/pmmo/events/impl/$TradeHandler" {
import {$TradeWithVillagerEvent, $TradeWithVillagerEvent$Type} from "packages/net/minecraftforge/event/entity/player/$TradeWithVillagerEvent"

export class $TradeHandler {

constructor()

public static "handle"(arg0: $TradeWithVillagerEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TradeHandler$Type = ($TradeHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TradeHandler_ = $TradeHandler$Type;
}}
declare module "packages/harmonised/pmmo/network/serverpackets/$SP_UpdateVeinTarget" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $SP_UpdateVeinTarget {

constructor(arg0: $BlockPos$Type)
constructor(arg0: $FriendlyByteBuf$Type)

public "toBytes"(arg0: $FriendlyByteBuf$Type): void
public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SP_UpdateVeinTarget$Type = ($SP_UpdateVeinTarget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SP_UpdateVeinTarget_ = $SP_UpdateVeinTarget$Type;
}}
declare module "packages/harmonised/pmmo/features/anticheese/$CheeseTracker$Setting" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$CheeseTracker$Setting$Builder, $CheeseTracker$Setting$Builder$Type} from "packages/harmonised/pmmo/features/anticheese/$CheeseTracker$Setting$Builder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CheeseTracker$Setting extends $Record {
static readonly "SOURCE": string
static readonly "MIN_TIME_TO_APPLY": string
static readonly "REDUCTION": string
static readonly "COOLOFF": string
static readonly "TOLERANCE_PERCENT": string
static readonly "TOLERANCE_FLAT": string
static readonly "RETENTION": string
static readonly "STRICT": string
static readonly "CODEC": $Codec<($CheeseTracker$Setting)>

constructor(source: $List$Type<(string)>, minTime: integer, retention: integer, toleranceFlat: integer, reduction: double, cooloff: integer, tolerancePercent: double, strictTolerance: boolean)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "source"(): $List<(string)>
public static "build"(): $CheeseTracker$Setting$Builder
public "retention"(): integer
public "reduction"(): double
public "strictTolerance"(): boolean
public "tolerancePercent"(): double
public "toleranceFlat"(): integer
public "applyAFK"(arg0: $EventType$Type, arg1: $ResourceLocation$Type, arg2: $Player$Type, arg3: $Map$Type<(string), (long)>): void
public "applyNormalization"(arg0: $EventType$Type, arg1: $ResourceLocation$Type, arg2: $Player$Type, arg3: $Map$Type<(string), (long)>): void
public "applyDiminuation"(arg0: $EventType$Type, arg1: $ResourceLocation$Type, arg2: $Player$Type, arg3: $Map$Type<(string), (long)>): void
public "cooloff"(): integer
public "minTime"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CheeseTracker$Setting$Type = ($CheeseTracker$Setting);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CheeseTracker$Setting_ = $CheeseTracker$Setting$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$PlaceHandler" {
import {$BlockEvent$EntityPlaceEvent, $BlockEvent$EntityPlaceEvent$Type} from "packages/net/minecraftforge/event/level/$BlockEvent$EntityPlaceEvent"

export class $PlaceHandler {

constructor()

public static "handle"(arg0: $BlockEvent$EntityPlaceEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceHandler$Type = ($PlaceHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceHandler_ = $PlaceHandler$Type;
}}
declare module "packages/harmonised/pmmo/features/loot_modifiers/$SkillLootConditionHighestSkill" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LootItemCondition, $LootItemCondition$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemCondition"

export class $SkillLootConditionHighestSkill implements $LootItemCondition {

constructor(arg0: string, arg1: $List$Type<(string)>)

public "test"(arg0: $LootContext$Type): boolean
public "getType"(): $LootItemConditionType
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SkillLootConditionHighestSkill$Type = ($SkillLootConditionHighestSkill);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SkillLootConditionHighestSkill_ = $SkillLootConditionHighestSkill$Type;
}}
declare module "packages/harmonised/pmmo/client/gui/$TutorialOverlayGUI" {
import {$IGuiOverlay, $IGuiOverlay$Type} from "packages/net/minecraftforge/client/gui/overlay/$IGuiOverlay"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ForgeGui, $ForgeGui$Type} from "packages/net/minecraftforge/client/gui/overlay/$ForgeGui"

export class $TutorialOverlayGUI implements $IGuiOverlay {

constructor()

public "render"(arg0: $ForgeGui$Type, arg1: $GuiGraphics$Type, arg2: float, arg3: integer, arg4: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TutorialOverlayGUI$Type = ($TutorialOverlayGUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TutorialOverlayGUI_ = $TutorialOverlayGUI$Type;
}}
declare module "packages/harmonised/pmmo/features/fireworks/$FireworkHandler" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Perk, $Perk$Type} from "packages/harmonised/pmmo/api/perks/$Perk"

export class $FireworkHandler {
static readonly "FIREWORK_SKILL": string
static readonly "FIREWORK": $Perk

constructor()

public static "spawnRocket"(arg0: $Level$Type, arg1: $Vec3$Type, arg2: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FireworkHandler$Type = ($FireworkHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FireworkHandler_ = $FireworkHandler$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$FoodEatHandler" {
import {$LivingEntityUseItemEvent$Finish, $LivingEntityUseItemEvent$Finish$Type} from "packages/net/minecraftforge/event/entity/living/$LivingEntityUseItemEvent$Finish"

export class $FoodEatHandler {

constructor()

public static "handle"(arg0: $LivingEntityUseItemEvent$Finish$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoodEatHandler$Type = ($FoodEatHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoodEatHandler_ = $FoodEatHandler$Type;
}}
declare module "packages/harmonised/pmmo/compat/crafttweaker/$CTDescriptionFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $CTDescriptionFunction {

}

export namespace $CTDescriptionFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CTDescriptionFunction$Type = ($CTDescriptionFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CTDescriptionFunction_ = $CTDescriptionFunction$Type;
}}
declare module "packages/harmonised/pmmo/client/gui/$GlossarySelectScreen$OBJECT" {
import {$SelectionWidget$SelectionEntry, $SelectionWidget$SelectionEntry$Type} from "packages/harmonised/pmmo/client/gui/component/$SelectionWidget$SelectionEntry"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$List, $List$Type} from "packages/java/util/$List"

export class $GlossarySelectScreen$OBJECT extends $Enum<($GlossarySelectScreen$OBJECT)> {
static readonly "ITEMS": $GlossarySelectScreen$OBJECT
static readonly "BLOCKS": $GlossarySelectScreen$OBJECT
static readonly "ENTITY": $GlossarySelectScreen$OBJECT
static readonly "DIMENSIONS": $GlossarySelectScreen$OBJECT
static readonly "BIOMES": $GlossarySelectScreen$OBJECT
static readonly "ENCHANTS": $GlossarySelectScreen$OBJECT
static readonly "EFFECTS": $GlossarySelectScreen$OBJECT
static readonly "PERKS": $GlossarySelectScreen$OBJECT
static readonly "CHOICE_LIST": $List<($SelectionWidget$SelectionEntry<($GlossarySelectScreen$OBJECT)>)>


public static "values"(): ($GlossarySelectScreen$OBJECT)[]
public static "valueOf"(arg0: string): $GlossarySelectScreen$OBJECT
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlossarySelectScreen$OBJECT$Type = (("enchants") | ("effects") | ("biomes") | ("blocks") | ("perks") | ("items") | ("entity") | ("dimensions")) | ($GlossarySelectScreen$OBJECT);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlossarySelectScreen$OBJECT_ = $GlossarySelectScreen$OBJECT$Type;
}}
declare module "packages/harmonised/pmmo/config/$GlobalsConfig" {
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$TomlConfigHelper$ConfigObject, $TomlConfigHelper$ConfigObject$Type} from "packages/harmonised/pmmo/config/readers/$TomlConfigHelper$ConfigObject"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $GlobalsConfig {
static "SERVER_CONFIG": $ForgeConfigSpec
static "PATHS": $TomlConfigHelper$ConfigObject<($Map<(string), (string)>)>
static "CONSTANTS": $TomlConfigHelper$ConfigObject<($Map<(string), (string)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlobalsConfig$Type = ($GlobalsConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlobalsConfig_ = $GlobalsConfig$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$FishHandler" {
import {$ItemFishedEvent, $ItemFishedEvent$Type} from "packages/net/minecraftforge/event/entity/player/$ItemFishedEvent"

export class $FishHandler {

constructor()

public static "handle"(arg0: $ItemFishedEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FishHandler$Type = ($FishHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FishHandler_ = $FishHandler$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$SleepHandler" {
import {$SleepFinishedTimeEvent, $SleepFinishedTimeEvent$Type} from "packages/net/minecraftforge/event/level/$SleepFinishedTimeEvent"

export class $SleepHandler {

constructor()

public static "handle"(arg0: $SleepFinishedTimeEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SleepHandler$Type = ($SleepHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SleepHandler_ = $SleepHandler$Type;
}}
declare module "packages/harmonised/pmmo/features/veinmining/capability/$VeinHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $VeinHandler {

constructor()

public "getCharge"(): double
public "setCharge"(arg0: double): void
get "charge"(): double
set "charge"(value: double)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VeinHandler$Type = ($VeinHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VeinHandler_ = $VeinHandler$Type;
}}
declare module "packages/harmonised/pmmo/compat/ftb_quests/$XpReward" {
import {$Quest, $Quest$Type} from "packages/dev/ftb/mods/ftbquests/quest/$Quest"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ConfigGroup, $ConfigGroup$Type} from "packages/dev/ftb/mods/ftblibrary/config/$ConfigGroup"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Reward, $Reward$Type} from "packages/dev/ftb/mods/ftbquests/quest/reward/$Reward"
import {$RewardType, $RewardType$Type} from "packages/dev/ftb/mods/ftbquests/quest/reward/$RewardType"

export class $XpReward extends $Reward {
static "XP_REWARD": $RewardType
 "skill": string
 "amount": long
 "ignoreBonuses": boolean
readonly "id": long

constructor(arg0: long, arg1: $Quest$Type)

public "getType"(): $RewardType
public "claim"(arg0: $ServerPlayer$Type, arg1: boolean): void
public "readData"(arg0: $CompoundTag$Type): void
public "getAltTitle"(): $Component
public "writeNetData"(arg0: $FriendlyByteBuf$Type): void
public "writeData"(arg0: $CompoundTag$Type): void
public "fillConfigGroup"(arg0: $ConfigGroup$Type): void
public "readNetData"(arg0: $FriendlyByteBuf$Type): void
get "type"(): $RewardType
get "altTitle"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $XpReward$Type = ($XpReward);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $XpReward_ = $XpReward$Type;
}}
declare module "packages/harmonised/pmmo/client/gui/$StatsScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$GuiEnumGroup, $GuiEnumGroup$Type} from "packages/harmonised/pmmo/client/gui/component/$GuiEnumGroup"
import {$GlossarySelectScreen$SELECTION, $GlossarySelectScreen$SELECTION$Type} from "packages/harmonised/pmmo/client/gui/$GlossarySelectScreen$SELECTION"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GlossarySelectScreen$OBJECT, $GlossarySelectScreen$OBJECT$Type} from "packages/harmonised/pmmo/client/gui/$GlossarySelectScreen$OBJECT"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $StatsScreen extends $Screen {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $GlossarySelectScreen$SELECTION$Type, arg1: $GlossarySelectScreen$OBJECT$Type, arg2: string, arg3: $GuiEnumGroup$Type)
constructor(arg0: $Entity$Type)
constructor(arg0: $BlockPos$Type)
constructor(arg0: $ItemStack$Type)
constructor()

public "renderBackground"(arg0: $GuiGraphics$Type): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StatsScreen$Type = ($StatsScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StatsScreen_ = $StatsScreen$Type;
}}
declare module "packages/harmonised/pmmo/api/events/$EnchantEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$EnchantmentInstance, $EnchantmentInstance$Type} from "packages/net/minecraft/world/item/enchantment/$EnchantmentInstance"
import {$PlayerEvent, $PlayerEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $EnchantEvent extends $PlayerEvent {

constructor()
constructor(arg0: $Player$Type, arg1: $ItemStack$Type, arg2: $EnchantmentInstance$Type)

public "getItem"(): $ItemStack
public "isCancelable"(): boolean
public "getEnchantment"(): $EnchantmentInstance
public "getListenerList"(): $ListenerList
get "item"(): $ItemStack
get "cancelable"(): boolean
get "enchantment"(): $EnchantmentInstance
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnchantEvent$Type = ($EnchantEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnchantEvent_ = $EnchantEvent$Type;
}}
declare module "packages/harmonised/pmmo/client/events/$TooltipHandler" {
import {$ItemTooltipEvent, $ItemTooltipEvent$Type} from "packages/net/minecraftforge/event/entity/player/$ItemTooltipEvent"

export class $TooltipHandler {
static "tooltipOn": boolean

constructor()

public static "onTooltip"(arg0: $ItemTooltipEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TooltipHandler$Type = ($TooltipHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TooltipHandler_ = $TooltipHandler$Type;
}}
declare module "packages/harmonised/pmmo/client/gui/$XPOverlayGUI" {
import {$IGuiOverlay, $IGuiOverlay$Type} from "packages/net/minecraftforge/client/gui/overlay/$IGuiOverlay"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ForgeGui, $ForgeGui$Type} from "packages/net/minecraftforge/client/gui/overlay/$ForgeGui"

export class $XPOverlayGUI implements $IGuiOverlay {

constructor()

public "render"(arg0: $ForgeGui$Type, arg1: $GuiGraphics$Type, arg2: float, arg3: integer, arg4: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $XPOverlayGUI$Type = ($XPOverlayGUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $XPOverlayGUI_ = $XPOverlayGUI$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$BreakHandler" {
import {$BlockEvent$BreakEvent, $BlockEvent$BreakEvent$Type} from "packages/net/minecraftforge/event/level/$BlockEvent$BreakEvent"

export class $BreakHandler {

constructor()

public static "handle"(arg0: $BlockEvent$BreakEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BreakHandler$Type = ($BreakHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BreakHandler_ = $BreakHandler$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$BreakSpeedHandler" {
import {$PlayerEvent$BreakSpeed, $PlayerEvent$BreakSpeed$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$BreakSpeed"

export class $BreakSpeedHandler {

constructor()

public static "handle"(arg0: $PlayerEvent$BreakSpeed$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BreakSpeedHandler$Type = ($BreakSpeedHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BreakSpeedHandler_ = $BreakSpeedHandler$Type;
}}
declare module "packages/harmonised/pmmo/features/anticheese/$AntiCheeseConfig" {
import {$ForgeConfigSpec$BooleanValue, $ForgeConfigSpec$BooleanValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$BooleanValue"
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$TomlConfigHelper$ConfigObject, $TomlConfigHelper$ConfigObject$Type} from "packages/harmonised/pmmo/config/readers/$TomlConfigHelper$ConfigObject"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$CheeseTracker$Setting, $CheeseTracker$Setting$Type} from "packages/harmonised/pmmo/features/anticheese/$CheeseTracker$Setting"

export class $AntiCheeseConfig {
static "SERVER_CONFIG": $ForgeConfigSpec
static "AFK_CAN_SUBTRACT": $ForgeConfigSpec$BooleanValue
static "SETTINGS_AFK": $TomlConfigHelper$ConfigObject<($Map<($EventType), ($CheeseTracker$Setting)>)>
static "SETTINGS_DIMINISHING": $TomlConfigHelper$ConfigObject<($Map<($EventType), ($CheeseTracker$Setting)>)>
static "SETTINGS_NORMALIZED": $TomlConfigHelper$ConfigObject<($Map<($EventType), ($CheeseTracker$Setting)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AntiCheeseConfig$Type = ($AntiCheeseConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AntiCheeseConfig_ = $AntiCheeseConfig$Type;
}}
declare module "packages/harmonised/pmmo/setup/datagen/$GLMProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$GlobalLootModifierProvider, $GlobalLootModifierProvider$Type} from "packages/net/minecraftforge/common/data/$GlobalLootModifierProvider"

export class $GLMProvider extends $GlobalLootModifierProvider {

constructor(arg0: $PackOutput$Type)

public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLMProvider$Type = ($GLMProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLMProvider_ = $GLMProvider$Type;
}}
declare module "packages/harmonised/pmmo/network/clientpackets/$CP_SyncData" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ObjectType, $ObjectType$Type} from "packages/harmonised/pmmo/api/enums/$ObjectType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CP_SyncData extends $Record {

constructor(type: $ObjectType$Type, data: $Map$Type<($ResourceLocation$Type), (any)>)

public "type"(): $ObjectType
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "decode"(arg0: $FriendlyByteBuf$Type): $CP_SyncData
public "encode"(arg0: $FriendlyByteBuf$Type): void
public "data"(): $Map<($ResourceLocation), (any)>
public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CP_SyncData$Type = ($CP_SyncData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CP_SyncData_ = $CP_SyncData$Type;
}}
declare module "packages/harmonised/pmmo/core/perks/$PerksImpl" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Perk, $Perk$Type} from "packages/harmonised/pmmo/api/perks/$Perk"

export class $PerksImpl {
static "BREAK_SPEED": $Perk
static readonly "ANIMAL_ID": string
static readonly "TAME_BOOST": $Perk

constructor()

public static "getDefaults"(): $CompoundTag
get "defaults"(): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PerksImpl$Type = ($PerksImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PerksImpl_ = $PerksImpl$Type;
}}
declare module "packages/harmonised/pmmo/commands/$CmdPmmoRoot$Setting" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $CmdPmmoRoot$Setting extends $Enum<($CmdPmmoRoot$Setting)> {
static readonly "RESET": $CmdPmmoRoot$Setting
static readonly "DEFAULT": $CmdPmmoRoot$Setting
static readonly "OVERRIDE": $CmdPmmoRoot$Setting
static readonly "DISABLER": $CmdPmmoRoot$Setting
static readonly "PLAYER": $CmdPmmoRoot$Setting
static readonly "SIMPLIFY": $CmdPmmoRoot$Setting
static readonly "FILTER": $CmdPmmoRoot$Setting


public static "values"(): ($CmdPmmoRoot$Setting)[]
public static "valueOf"(arg0: string): $CmdPmmoRoot$Setting
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CmdPmmoRoot$Setting$Type = (("filter") | ("default") | ("reset") | ("override") | ("simplify") | ("disabler") | ("player")) | ($CmdPmmoRoot$Setting);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CmdPmmoRoot$Setting_ = $CmdPmmoRoot$Setting$Type;
}}
declare module "packages/harmonised/pmmo/client/gui/$GlossarySelectScreen$SELECTION" {
import {$SelectionWidget$SelectionEntry, $SelectionWidget$SelectionEntry$Type} from "packages/harmonised/pmmo/client/gui/component/$SelectionWidget$SelectionEntry"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$List, $List$Type} from "packages/java/util/$List"

export class $GlossarySelectScreen$SELECTION extends $Enum<($GlossarySelectScreen$SELECTION)> {
static readonly "REQS": $GlossarySelectScreen$SELECTION
static readonly "XP": $GlossarySelectScreen$SELECTION
static readonly "BONUS": $GlossarySelectScreen$SELECTION
static readonly "SALVAGE": $GlossarySelectScreen$SELECTION
static readonly "VEIN": $GlossarySelectScreen$SELECTION
static readonly "MOB_SCALING": $GlossarySelectScreen$SELECTION
static readonly "PERKS": $GlossarySelectScreen$SELECTION
static readonly "CHOICE_LIST": $List<($SelectionWidget$SelectionEntry<($GlossarySelectScreen$SELECTION)>)>


public static "values"(): ($GlossarySelectScreen$SELECTION)[]
public static "valueOf"(arg0: string): $GlossarySelectScreen$SELECTION
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlossarySelectScreen$SELECTION$Type = (("reqs") | ("vein") | ("bonus") | ("xp") | ("mob_scaling") | ("salvage") | ("perks")) | ($GlossarySelectScreen$SELECTION);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlossarySelectScreen$SELECTION_ = $GlossarySelectScreen$SELECTION$Type;
}}
declare module "packages/harmonised/pmmo/client/gui/$PlayerStatsScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$PlayerStatsComponent, $PlayerStatsComponent$Type} from "packages/harmonised/pmmo/client/gui/component/$PlayerStatsComponent"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$InventoryMenu, $InventoryMenu$Type} from "packages/net/minecraft/world/inventory/$InventoryMenu"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$EffectRenderingInventoryScreen, $EffectRenderingInventoryScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$EffectRenderingInventoryScreen"
import {$List, $List$Type} from "packages/java/util/$List"

export class $PlayerStatsScreen extends $EffectRenderingInventoryScreen<($InventoryMenu)> {
static readonly "PLAYER_STATS_LOCATION": $ResourceLocation
 "xMouse": float
 "yMouse": float
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

constructor(arg0: $Player$Type)

public "getPlayerStatsComponent"(): $PlayerStatsComponent
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
get "playerStatsComponent"(): $PlayerStatsComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerStatsScreen$Type = ($PlayerStatsScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerStatsScreen_ = $PlayerStatsScreen$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$BreedHandler" {
import {$BabyEntitySpawnEvent, $BabyEntitySpawnEvent$Type} from "packages/net/minecraftforge/event/entity/living/$BabyEntitySpawnEvent"

export class $BreedHandler {

constructor()

public static "handle"(arg0: $BabyEntitySpawnEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BreedHandler$Type = ($BreedHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BreedHandler_ = $BreedHandler$Type;
}}
declare module "packages/harmonised/pmmo/api/enums/$ModifierDataType" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$IExtensibleEnum, $IExtensibleEnum$Type} from "packages/net/minecraftforge/common/$IExtensibleEnum"
import {$GuiEnumGroup, $GuiEnumGroup$Type} from "packages/harmonised/pmmo/client/gui/component/$GuiEnumGroup"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$LangProvider$Translation, $LangProvider$Translation$Type} from "packages/harmonised/pmmo/setup/datagen/$LangProvider$Translation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $ModifierDataType extends $Enum<($ModifierDataType)> implements $StringRepresentable, $IExtensibleEnum, $GuiEnumGroup {
static readonly "BIOME": $ModifierDataType
static readonly "HELD": $ModifierDataType
static readonly "WORN": $ModifierDataType
static readonly "DIMENSION": $ModifierDataType
readonly "tooltip": $LangProvider$Translation
static readonly "CODEC": $Codec<($ModifierDataType)>


public "getName"(): string
public static "values"(): ($ModifierDataType)[]
public static "valueOf"(arg0: string): $ModifierDataType
public static "create"(arg0: string, arg1: $LangProvider$Translation$Type): $ModifierDataType
public "getSerializedName"(): string
public static "byName"(arg0: string): $ModifierDataType
public static "fromEnumWithMapping"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
public static "keys"(arg0: ($StringRepresentable$Type)[]): $Keyable
public static "fromEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
/**
 * 
 * @deprecated
 */
public "init"(): void
public static "createCodecForExtensibleEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(any), (any)>): $Codec<(E)>
get "name"(): string
get "serializedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifierDataType$Type = (("worn") | ("held") | ("biome") | ("dimension")) | ($ModifierDataType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifierDataType_ = $ModifierDataType$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$EntityInteractHandler" {
import {$PlayerInteractEvent$EntityInteract, $PlayerInteractEvent$EntityInteract$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$EntityInteract"

export class $EntityInteractHandler {

constructor()

public static "handle"(arg0: $PlayerInteractEvent$EntityInteract$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityInteractHandler$Type = ($EntityInteractHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityInteractHandler_ = $EntityInteractHandler$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$EnchantHandler" {
import {$EnchantEvent, $EnchantEvent$Type} from "packages/harmonised/pmmo/api/events/$EnchantEvent"

export class $EnchantHandler {

constructor()

public static "handle"(arg0: $EnchantEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnchantHandler$Type = ($EnchantHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnchantHandler_ = $EnchantHandler$Type;
}}
declare module "packages/harmonised/pmmo/config/$SkillsConfig" {
import {$SkillData, $SkillData$Type} from "packages/harmonised/pmmo/config/codecs/$SkillData"
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$TomlConfigHelper$ConfigObject, $TomlConfigHelper$ConfigObject$Type} from "packages/harmonised/pmmo/config/readers/$TomlConfigHelper$ConfigObject"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $SkillsConfig {
static "SERVER_CONFIG": $ForgeConfigSpec
static "SKILLS": $TomlConfigHelper$ConfigObject<($Map<(string), ($SkillData)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SkillsConfig$Type = ($SkillsConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SkillsConfig_ = $SkillsConfig$Type;
}}
declare module "packages/harmonised/pmmo/core/nbt/$LogicEntry$Criteria" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Operator, $Operator$Type} from "packages/harmonised/pmmo/core/nbt/$Operator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LogicEntry$Criteria extends $Record {
static readonly "CODEC": $Codec<($LogicEntry$Criteria)>

constructor(operator: $Operator$Type, comparators: $Optional$Type<($List$Type<(string)>)>, skillMap: $Map$Type<(string), (double)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "operator"(): $Operator
public "skillMap"(): $Map<(string), (double)>
public "comparators"(): $Optional<($List<(string)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LogicEntry$Criteria$Type = ($LogicEntry$Criteria);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LogicEntry$Criteria_ = $LogicEntry$Criteria$Type;
}}
declare module "packages/harmonised/pmmo/client/utils/$DataMirror" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDataStorage, $IDataStorage$Type} from "packages/harmonised/pmmo/core/$IDataStorage"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $DataMirror implements $IDataStorage {

constructor()

public "get"(): $IDataStorage
public "me"(arg0: $UUID$Type): boolean
public "getXpRaw"(arg0: $UUID$Type, arg1: string): long
public "getLevelFromXP"(arg0: long): integer
public "setXpMap"(arg0: $UUID$Type, arg1: $Map$Type<(string), (long)>): void
public "setXpRaw"(arg0: $UUID$Type, arg1: string, arg2: long): void
public "getBaseXpForLevel"(arg0: integer): long
public "getScheduledXp"(arg0: string): long
public "setLevelCache"(arg0: $List$Type<(long)>): void
public "getXpMap"(arg0: $UUID$Type): $Map<(string), (long)>
public "getPlayerSkillLevel"(arg0: string, arg1: $UUID$Type): integer
public "getXpWithPercentToNextLevel"(arg0: long): double
public "setXpDiff"(arg0: $UUID$Type, arg1: string, arg2: long): boolean
public "changePlayerSkillLevel"(arg0: string, arg1: $UUID$Type, arg2: integer): boolean
public "setPlayerSkillLevel"(arg0: string, arg1: $UUID$Type, arg2: integer): void
public "computeLevelsForCache"(): void
set "levelCache"(value: $List$Type<(long)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataMirror$Type = ($DataMirror);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataMirror_ = $DataMirror$Type;
}}
declare module "packages/harmonised/pmmo/client/gui/component/$SelectionWidget$SelectionEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$ComponentPath, $ComponentPath$Type} from "packages/net/minecraft/client/gui/$ComponentPath"
import {$FocusNavigationEvent, $FocusNavigationEvent$Type} from "packages/net/minecraft/client/gui/navigation/$FocusNavigationEvent"
import {$ScreenRectangle, $ScreenRectangle$Type} from "packages/net/minecraft/client/gui/navigation/$ScreenRectangle"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $SelectionWidget$SelectionEntry<T> implements $GuiEventListener {
readonly "message": $Component
 "reference": T

constructor(arg0: $Component$Type, arg1: T)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: boolean, arg5: integer, arg6: float): void
public "setFocused"(arg0: boolean): void
public "isFocused"(): boolean
public "getCurrentFocusPath"(): $ComponentPath
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "nextFocusPath"(arg0: $FocusNavigationEvent$Type): $ComponentPath
public "isMouseOver"(arg0: double, arg1: double): boolean
public "getRectangle"(): $ScreenRectangle
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "keyReleased"(arg0: integer, arg1: integer, arg2: integer): boolean
public "mouseMoved"(arg0: double, arg1: double): void
public "getTabOrderGroup"(): integer
set "focused"(value: boolean)
get "focused"(): boolean
get "currentFocusPath"(): $ComponentPath
get "rectangle"(): $ScreenRectangle
get "tabOrderGroup"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SelectionWidget$SelectionEntry$Type<T> = ($SelectionWidget$SelectionEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SelectionWidget$SelectionEntry_<T> = $SelectionWidget$SelectionEntry$Type<(T)>;
}}
declare module "packages/harmonised/pmmo/core/perks/$PerkRegistration" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PerkRegistration {

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PerkRegistration$Type = ($PerkRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PerkRegistration_ = $PerkRegistration$Type;
}}
declare module "packages/harmonised/pmmo/features/autovalues/$AutoValueConfig$AttributeKey" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AutoValueConfig$AttributeKey extends $Enum<($AutoValueConfig$AttributeKey)> {
static readonly "DUR": $AutoValueConfig$AttributeKey
static readonly "TIER": $AutoValueConfig$AttributeKey
static readonly "DMG": $AutoValueConfig$AttributeKey
static readonly "SPD": $AutoValueConfig$AttributeKey
static readonly "DIG": $AutoValueConfig$AttributeKey
static readonly "AMR": $AutoValueConfig$AttributeKey
static readonly "KBR": $AutoValueConfig$AttributeKey
static readonly "TUF": $AutoValueConfig$AttributeKey
static readonly "HEALTH": $AutoValueConfig$AttributeKey
static readonly "SPEED": $AutoValueConfig$AttributeKey
static readonly "DEFAULT_ITEM_MAP": $Map<(string), (double)>
static "DEFAULT_ARMOR_MAP": $Map<(string), (double)>
static "DEFAULT_ENTITY_MAP": $Map<(string), (double)>


public static "values"(): ($AutoValueConfig$AttributeKey)[]
public static "valueOf"(arg0: string): $AutoValueConfig$AttributeKey
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoValueConfig$AttributeKey$Type = (("dur") | ("dig") | ("tier") | ("tuf") | ("spd") | ("amr") | ("health") | ("kbr") | ("speed") | ("dmg")) | ($AutoValueConfig$AttributeKey);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoValueConfig$AttributeKey_ = $AutoValueConfig$AttributeKey$Type;
}}
declare module "packages/harmonised/pmmo/registry/$PerkRegistry" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Perk, $Perk$Type} from "packages/harmonised/pmmo/api/perks/$Perk"
import {$TickEvent$LevelTickEvent, $TickEvent$LevelTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$LevelTickEvent"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export class $PerkRegistry {

constructor()

public "getDescription"(arg0: $ResourceLocation$Type): $MutableComponent
public "getStatusLines"(arg0: $ResourceLocation$Type, arg1: $Player$Type, arg2: $CompoundTag$Type): $List<($MutableComponent)>
public "registerPerk"(arg0: $ResourceLocation$Type, arg1: $Perk$Type): void
public "isPerkCooledDown"(arg0: $Player$Type, arg1: $CompoundTag$Type): boolean
public "executePerkTicks"(arg0: $TickEvent$LevelTickEvent$Type): void
public "executePerk"(arg0: $EventType$Type, arg1: $Player$Type, arg2: $CompoundTag$Type): $CompoundTag
public "registerClientClone"(arg0: $ResourceLocation$Type, arg1: $Perk$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PerkRegistry$Type = ($PerkRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PerkRegistry_ = $PerkRegistry$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$MountHandler" {
import {$EntityMountEvent, $EntityMountEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityMountEvent"

export class $MountHandler {

constructor()

public static "handle"(arg0: $EntityMountEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MountHandler$Type = ($MountHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MountHandler_ = $MountHandler$Type;
}}
declare module "packages/harmonised/pmmo/features/loot_modifiers/$ValidBlockCondition" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LootItemCondition, $LootItemCondition$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemCondition"

export class $ValidBlockCondition implements $LootItemCondition {
 "tag": $TagKey<($Block)>
 "block": $Block

constructor(arg0: $TagKey$Type<($Block$Type)>)
constructor(arg0: $Block$Type)

public "test"(arg0: $LootContext$Type): boolean
public "getType"(): $LootItemConditionType
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValidBlockCondition$Type = ($ValidBlockCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValidBlockCondition_ = $ValidBlockCondition$Type;
}}
declare module "packages/harmonised/pmmo/util/$MsLoggy$LOG_CODE" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $MsLoggy$LOG_CODE extends $Enum<($MsLoggy$LOG_CODE)> {
static readonly "API": $MsLoggy$LOG_CODE
static readonly "AUTO_VALUES": $MsLoggy$LOG_CODE
static readonly "CHUNK": $MsLoggy$LOG_CODE
static readonly "DATA": $MsLoggy$LOG_CODE
static readonly "EVENT": $MsLoggy$LOG_CODE
static readonly "FEATURE": $MsLoggy$LOG_CODE
static readonly "PERKS": $MsLoggy$LOG_CODE
static readonly "GUI": $MsLoggy$LOG_CODE
static readonly "LOADING": $MsLoggy$LOG_CODE
static readonly "NETWORK": $MsLoggy$LOG_CODE
static readonly "XP": $MsLoggy$LOG_CODE
static readonly "NONE": $MsLoggy$LOG_CODE
 "code": string


public static "values"(): ($MsLoggy$LOG_CODE)[]
public static "valueOf"(arg0: string): $MsLoggy$LOG_CODE
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MsLoggy$LOG_CODE$Type = (("auto_values") | ("data") | ("feature") | ("xp") | ("chunk") | ("gui") | ("api") | ("none") | ("event") | ("perks") | ("loading") | ("network")) | ($MsLoggy$LOG_CODE);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MsLoggy$LOG_CODE_ = $MsLoggy$LOG_CODE$Type;
}}
declare module "packages/harmonised/pmmo/features/anticheese/$CheeseTracker$Setting$Builder" {
import {$CheeseTracker$Setting, $CheeseTracker$Setting$Type} from "packages/harmonised/pmmo/features/anticheese/$CheeseTracker$Setting"

export class $CheeseTracker$Setting$Builder {


public "source"(...arg0: (string)[]): $CheeseTracker$Setting$Builder
public "source"(arg0: string): $CheeseTracker$Setting$Builder
public "build"(): $CheeseTracker$Setting
public "retention"(arg0: integer): $CheeseTracker$Setting$Builder
public "reduction"(arg0: double): $CheeseTracker$Setting$Builder
public "tolerance"(arg0: integer): $CheeseTracker$Setting$Builder
public "tolerance"(arg0: double): $CheeseTracker$Setting$Builder
public "cooloff"(arg0: integer): $CheeseTracker$Setting$Builder
public "minTime"(arg0: integer): $CheeseTracker$Setting$Builder
public "setStrictness"(arg0: boolean): $CheeseTracker$Setting$Builder
set "strictness"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CheeseTracker$Setting$Builder$Type = ($CheeseTracker$Setting$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CheeseTracker$Setting$Builder_ = $CheeseTracker$Setting$Builder$Type;
}}
declare module "packages/harmonised/pmmo/client/gui/$IndicatorsOverlayGUI" {
import {$IGuiOverlay, $IGuiOverlay$Type} from "packages/net/minecraftforge/client/gui/overlay/$IGuiOverlay"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ForgeGui, $ForgeGui$Type} from "packages/net/minecraftforge/client/gui/overlay/$ForgeGui"

export class $IndicatorsOverlayGUI implements $IGuiOverlay {

constructor()

public "render"(arg0: $ForgeGui$Type, arg1: $GuiGraphics$Type, arg2: float, arg3: integer, arg4: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IndicatorsOverlayGUI$Type = ($IndicatorsOverlayGUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IndicatorsOverlayGUI_ = $IndicatorsOverlayGUI$Type;
}}
declare module "packages/harmonised/pmmo/storage/$ChunkDataHandler" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$IChunkData, $IChunkData$Type} from "packages/harmonised/pmmo/storage/$IChunkData"

export class $ChunkDataHandler implements $IChunkData {

constructor()

public "getMap"(): $Map<($BlockPos), ($UUID)>
public "setMap"(arg0: $Map$Type<($BlockPos$Type), ($UUID$Type)>): void
public "checkPos"(arg0: $BlockPos$Type): $UUID
public "setBreaker"(arg0: $BlockPos$Type, arg1: $UUID$Type): void
public "getBreaker"(arg0: $BlockPos$Type): $UUID
public "addPos"(arg0: $BlockPos$Type, arg1: $UUID$Type): void
public "playerMatchesPos"(arg0: $Player$Type, arg1: $BlockPos$Type): boolean
public "delPos"(arg0: $BlockPos$Type): void
get "map"(): $Map<($BlockPos), ($UUID)>
set "map"(value: $Map$Type<($BlockPos$Type), ($UUID$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkDataHandler$Type = ($ChunkDataHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkDataHandler_ = $ChunkDataHandler$Type;
}}
declare module "packages/harmonised/pmmo/network/serverpackets/$SP_OtherExpRequest" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $SP_OtherExpRequest {

constructor(arg0: $UUID$Type)
constructor(arg0: $FriendlyByteBuf$Type)

public "toBytes"(arg0: $FriendlyByteBuf$Type): void
public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SP_OtherExpRequest$Type = ($SP_OtherExpRequest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SP_OtherExpRequest_ = $SP_OtherExpRequest$Type;
}}
declare module "packages/harmonised/pmmo/compat/ftb_quests/$LevelReward" {
import {$Quest, $Quest$Type} from "packages/dev/ftb/mods/ftbquests/quest/$Quest"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ConfigGroup, $ConfigGroup$Type} from "packages/dev/ftb/mods/ftblibrary/config/$ConfigGroup"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Reward, $Reward$Type} from "packages/dev/ftb/mods/ftbquests/quest/reward/$Reward"
import {$RewardType, $RewardType$Type} from "packages/dev/ftb/mods/ftbquests/quest/reward/$RewardType"

export class $LevelReward extends $Reward {
static "LEVEL_REWARD": $RewardType
 "skill": string
 "amount": integer
 "ignoreBonuses": boolean
readonly "id": long

constructor(arg0: long, arg1: $Quest$Type)

public "getType"(): $RewardType
public "claim"(arg0: $ServerPlayer$Type, arg1: boolean): void
public "readData"(arg0: $CompoundTag$Type): void
public "getAltTitle"(): $Component
public "writeNetData"(arg0: $FriendlyByteBuf$Type): void
public "writeData"(arg0: $CompoundTag$Type): void
public "fillConfigGroup"(arg0: $ConfigGroup$Type): void
public "readNetData"(arg0: $FriendlyByteBuf$Type): void
get "type"(): $RewardType
get "altTitle"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LevelReward$Type = ($LevelReward);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LevelReward_ = $LevelReward$Type;
}}
declare module "packages/harmonised/pmmo/features/loot_modifiers/$SkillLootConditionKill" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LootItemCondition, $LootItemCondition$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemCondition"

export class $SkillLootConditionKill implements $LootItemCondition {

constructor(arg0: integer, arg1: integer, arg2: string)

public "test"(arg0: $LootContext$Type): boolean
public "getType"(): $LootItemConditionType
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SkillLootConditionKill$Type = ($SkillLootConditionKill);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SkillLootConditionKill_ = $SkillLootConditionKill$Type;
}}
declare module "packages/harmonised/pmmo/config/codecs/$LocationData" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$DataSource, $DataSource$Type} from "packages/harmonised/pmmo/config/codecs/$DataSource"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$ReqType, $ReqType$Type} from "packages/harmonised/pmmo/api/enums/$ReqType"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ModifierDataType, $ModifierDataType$Type} from "packages/harmonised/pmmo/api/enums/$ModifierDataType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LocationData extends $Record implements $DataSource<($LocationData)> {
static readonly "CODEC": $Codec<($LocationData)>

constructor()
constructor(override: boolean, tagValues: $Set$Type<(string)>, bonusMap: $Map$Type<($ModifierDataType$Type), ($Map$Type<(string), (double)>)>, positive: $Map$Type<($ResourceLocation$Type), (integer)>, negative: $Map$Type<($ResourceLocation$Type), (integer)>, veinBlacklist: $List$Type<($ResourceLocation$Type)>, travelReq: $Map$Type<(string), (integer)>, mobModifiers: $Map$Type<($ResourceLocation$Type), ($Map$Type<(string), (double)>)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "combine"(arg0: $LocationData$Type): $LocationData
public "override"(): boolean
public "negative"(): $Map<($ResourceLocation), (integer)>
public "positive"(): $Map<($ResourceLocation), (integer)>
public "getTagValues"(): $Set<(string)>
public "getBonuses"(arg0: $ModifierDataType$Type, arg1: $CompoundTag$Type): $Map<(string), (double)>
public "getReqs"(arg0: $ReqType$Type, arg1: $CompoundTag$Type): $Map<(string), (integer)>
public "tagValues"(): $Set<(string)>
public "getNegativeEffect"(): $Map<($ResourceLocation), (integer)>
public "setNegativeEffects"(arg0: $Map$Type<($ResourceLocation$Type), (integer)>): void
public "setBonuses"(arg0: $ModifierDataType$Type, arg1: $Map$Type<(string), (double)>): void
public "setReqs"(arg0: $ReqType$Type, arg1: $Map$Type<(string), (integer)>): void
public "setPositiveEffects"(arg0: $Map$Type<($ResourceLocation$Type), (integer)>): void
public "getPositiveEffect"(): $Map<($ResourceLocation), (integer)>
public "travelReq"(): $Map<(string), (integer)>
public "bonusMap"(): $Map<($ModifierDataType), ($Map<(string), (double)>)>
public "isUnconfigured"(): boolean
public "veinBlacklist"(): $List<($ResourceLocation)>
public "mobModifiers"(): $Map<($ResourceLocation), ($Map<(string), (double)>)>
public "getXpValues"(arg0: $EventType$Type, arg1: $CompoundTag$Type): $Map<(string), (long)>
public "setXpValues"(arg0: $EventType$Type, arg1: $Map$Type<(string), (long)>): void
public static "clearEmptyValues"<K, V>(arg0: $Map$Type<(K), (V)>): $HashMap<(K), (V)>
get "negativeEffect"(): $Map<($ResourceLocation), (integer)>
set "negativeEffects"(value: $Map$Type<($ResourceLocation$Type), (integer)>)
set "positiveEffects"(value: $Map$Type<($ResourceLocation$Type), (integer)>)
get "positiveEffect"(): $Map<($ResourceLocation), (integer)>
get "unconfigured"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocationData$Type = ($LocationData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocationData_ = $LocationData$Type;
}}
declare module "packages/harmonised/pmmo/features/penalties/$EffectManager" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Core, $Core$Type} from "packages/harmonised/pmmo/core/$Core"

export class $EffectManager {

constructor()

public static "applyEffects"(arg0: $Core$Type, arg1: $Player$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EffectManager$Type = ($EffectManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EffectManager_ = $EffectManager$Type;
}}
declare module "packages/harmonised/pmmo/util/$RegistryUtil" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $RegistryUtil {

constructor()

public static "getId"(arg0: $EntityType$Type<(any)>): $ResourceLocation
public static "getId"(arg0: $Entity$Type): $ResourceLocation
public static "getId"(arg0: $SoundEvent$Type): $ResourceLocation
public static "getId"(arg0: $Enchantment$Type): $ResourceLocation
public static "getId"(arg0: $MobEffect$Type): $ResourceLocation
public static "getId"(arg0: $DamageSource$Type): $ResourceLocation
public static "getId"(arg0: $ItemStack$Type): $ResourceLocation
public static "getId"(arg0: $Item$Type): $ResourceLocation
public static "getId"(arg0: $BlockState$Type): $ResourceLocation
public static "getId"(arg0: $Block$Type): $ResourceLocation
public static "getId"(arg0: $Holder$Type<($Biome$Type)>): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryUtil$Type = ($RegistryUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryUtil_ = $RegistryUtil$Type;
}}
declare module "packages/harmonised/pmmo/core/$Core" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$PerkRegistry, $PerkRegistry$Type} from "packages/harmonised/pmmo/registry/$PerkRegistry"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$ObjectType, $ObjectType$Type} from "packages/harmonised/pmmo/api/enums/$ObjectType"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$TooltipRegistry, $TooltipRegistry$Type} from "packages/harmonised/pmmo/registry/$TooltipRegistry"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$CoreLoader, $CoreLoader$Type} from "packages/harmonised/pmmo/config/readers/$CoreLoader"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$LevelRegistry, $LevelRegistry$Type} from "packages/harmonised/pmmo/registry/$LevelRegistry"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$PredicateRegistry, $PredicateRegistry$Type} from "packages/harmonised/pmmo/registry/$PredicateRegistry"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$LogicalSide, $LogicalSide$Type} from "packages/net/minecraftforge/fml/$LogicalSide"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$ReqType, $ReqType$Type} from "packages/harmonised/pmmo/api/enums/$ReqType"
import {$ModifierDataType, $ModifierDataType$Type} from "packages/harmonised/pmmo/api/enums/$ModifierDataType"
import {$IDataStorage, $IDataStorage$Type} from "packages/harmonised/pmmo/core/$IDataStorage"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$EventTriggerRegistry, $EventTriggerRegistry$Type} from "packages/harmonised/pmmo/registry/$EventTriggerRegistry"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Core {


public static "get"(arg0: $LogicalSide$Type): $Core
public static "get"(arg0: $Level$Type): $Core
public "getLoader"(): $CoreLoader
public "getData"(): $IDataStorage
public "getCommonXpAwardData"(arg0: $Map$Type<(string), (long)>, arg1: $EventType$Type, arg2: $ResourceLocation$Type, arg3: $Player$Type, arg4: $ObjectType$Type, arg5: $CompoundTag$Type): $Map<(string), (long)>
public "getExperienceAwards"(arg0: $EventType$Type, arg1: $Entity$Type, arg2: $Player$Type, arg3: $CompoundTag$Type): $Map<(string), (long)>
public "getExperienceAwards"(arg0: $EventType$Type, arg1: $ItemStack$Type, arg2: $Player$Type, arg3: $CompoundTag$Type): $Map<(string), (long)>
public "getExperienceAwards"(arg0: $MobEffectInstance$Type, arg1: $Player$Type, arg2: $CompoundTag$Type): $Map<(string), (long)>
public "getExperienceAwards"(arg0: $EventType$Type, arg1: $BlockPos$Type, arg2: $Level$Type, arg3: $Player$Type, arg4: $CompoundTag$Type): $Map<(string), (long)>
public "getObjectModifierMap"(arg0: $ObjectType$Type, arg1: $ResourceLocation$Type, arg2: $ModifierDataType$Type, arg3: $CompoundTag$Type): $Map<(string), (double)>
public "getLevelProvider"(): $LevelRegistry
public "getTooltipRegistry"(): $TooltipRegistry
public "resetDataForReload"(): void
public "isActionPermitted"(arg0: $ReqType$Type, arg1: $ItemStack$Type, arg2: $Player$Type): boolean
public "isActionPermitted"(arg0: $ReqType$Type, arg1: $BlockPos$Type, arg2: $Player$Type): boolean
public "isActionPermitted"(arg0: $ReqType$Type, arg1: $Entity$Type, arg2: $Player$Type): boolean
public "isActionPermitted"(arg0: $ReqType$Type, arg1: $Holder$Type<($Biome$Type)>, arg2: $Player$Type): boolean
public "isActionPermitted"(arg0: $ReqType$Type, arg1: $ResourceKey$Type<($Level$Type)>, arg2: $Player$Type): boolean
public "getSalvage"(arg0: $ServerPlayer$Type): void
public "doesPlayerMeetReq"(arg0: $UUID$Type, arg1: $Map$Type<(string), (integer)>): boolean
public "getEnchantmentReqs"(arg0: $ResourceLocation$Type, arg1: integer): $Map<(string), (integer)>
public "getObjectSkillMap"(arg0: $ObjectType$Type, arg1: $ResourceLocation$Type, arg2: $ReqType$Type, arg3: $CompoundTag$Type): $Map<(string), (integer)>
public "setMarkedPos"(arg0: $UUID$Type, arg1: $BlockPos$Type): void
public "getBlockConsume"(arg0: $Block$Type): integer
public "getMarkedPos"(arg0: $UUID$Type): $BlockPos
public "getSide"(): $LogicalSide
public "getEnchantReqs"(arg0: $ItemStack$Type): $Map<(string), (integer)>
public "getCommonReqData"(arg0: $Map$Type<(string), (integer)>, arg1: $ObjectType$Type, arg2: $ResourceLocation$Type, arg3: $ReqType$Type, arg4: $CompoundTag$Type): $Map<(string), (integer)>
public "getReqMap"(arg0: $ReqType$Type, arg1: $ItemStack$Type, arg2: boolean): $Map<(string), (integer)>
public "getReqMap"(arg0: $ReqType$Type, arg1: $Entity$Type): $Map<(string), (integer)>
public "getReqMap"(arg0: $ReqType$Type, arg1: $BlockPos$Type, arg2: $Level$Type): $Map<(string), (integer)>
public "awardXP"(arg0: $List$Type<($ServerPlayer$Type)>, arg1: $Map$Type<(string), (long)>): void
public "getPerkRegistry"(): $PerkRegistry
public "getObjectExperienceMap"(arg0: $ObjectType$Type, arg1: $ResourceLocation$Type, arg2: $EventType$Type, arg3: $CompoundTag$Type): $Map<(string), (long)>
public "getEventTriggerRegistry"(): $EventTriggerRegistry
public "getPredicateRegistry"(): $PredicateRegistry
public "getConsolidatedModifierMap"(arg0: $Player$Type): $Map<(string), (double)>
get "loader"(): $CoreLoader
get "data"(): $IDataStorage
get "levelProvider"(): $LevelRegistry
get "tooltipRegistry"(): $TooltipRegistry
get "side"(): $LogicalSide
get "perkRegistry"(): $PerkRegistry
get "eventTriggerRegistry"(): $EventTriggerRegistry
get "predicateRegistry"(): $PredicateRegistry
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Core$Type = ($Core);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Core_ = $Core$Type;
}}
declare module "packages/harmonised/pmmo/client/utils/$VeinTracker" {
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$VeinShapeData$ShapeType, $VeinShapeData$ShapeType$Type} from "packages/harmonised/pmmo/features/veinmining/$VeinShapeData$ShapeType"

export class $VeinTracker {
static "currentTarget": $BlockPos
static "currentCharge": double
static "mode": $VeinShapeData$ShapeType

constructor()

public static "setTarget"(arg0: $BlockPos$Type): void
public static "isLookingAtVeinTarget"(arg0: $HitResult$Type): boolean
public static "getCurrentCharge"(): integer
public static "nextMode"(): void
public static "getVein"(): $Set<($BlockPos)>
public static "updateVein"(arg0: $Player$Type): void
set "target"(value: $BlockPos$Type)
get "currentCharge"(): integer
get "vein"(): $Set<($BlockPos)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VeinTracker$Type = ($VeinTracker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VeinTracker_ = $VeinTracker$Type;
}}
declare module "packages/harmonised/pmmo/storage/$IChunkData" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $IChunkData {

 "getMap"(): $Map<($BlockPos), ($UUID)>
 "setMap"(arg0: $Map$Type<($BlockPos$Type), ($UUID$Type)>): void
 "checkPos"(arg0: $BlockPos$Type): $UUID
 "setBreaker"(arg0: $BlockPos$Type, arg1: $UUID$Type): void
 "getBreaker"(arg0: $BlockPos$Type): $UUID
 "addPos"(arg0: $BlockPos$Type, arg1: $UUID$Type): void
 "playerMatchesPos"(arg0: $Player$Type, arg1: $BlockPos$Type): boolean
 "delPos"(arg0: $BlockPos$Type): void
}

export namespace $IChunkData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IChunkData$Type = ($IChunkData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IChunkData_ = $IChunkData$Type;
}}
declare module "packages/harmonised/pmmo/util/$Reference" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$DamageType, $DamageType$Type} from "packages/net/minecraft/world/damagesource/$DamageType"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $Reference {
static readonly "MOD_ID": string
static readonly "NIL": $UUID
static readonly "API_MAP_SERIALIZER_KEY": string
static readonly "API_MAP_SERIALIZER_VALUE": string
static readonly "CREATIVE_REACH_ATTRIBUTE": $UUID
static readonly "MOB_TAG": $TagKey<($EntityType<(any)>)>
static readonly "TAMABLE_TAG": $TagKey<($EntityType<(any)>)>
static readonly "BREEDABLE_TAG": $TagKey<($EntityType<(any)>)>
static readonly "RIDEABLE_TAG": $TagKey<($EntityType<(any)>)>
static readonly "NO_XP_DAMAGE_DEALT": $TagKey<($EntityType<(any)>)>
static readonly "CROPS": $TagKey<($Block)>
static readonly "CASCADING_BREAKABLES": $TagKey<($Block)>
static readonly "MINABLE_AXE": $TagKey<($Block)>
static readonly "MINABLE_HOE": $TagKey<($Block)>
static readonly "MINABLE_SHOVEL": $TagKey<($Block)>
static readonly "BREWABLES": $TagKey<($Item)>
static readonly "SMELTABLES": $TagKey<($Item)>
static readonly "FROM_ENVIRONMENT": $TagKey<($DamageType)>
static readonly "FROM_IMPACT": $TagKey<($DamageType)>
static readonly "FROM_MAGIC": $TagKey<($DamageType)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Reference$Type = ($Reference);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Reference_ = $Reference$Type;
}}
declare module "packages/harmonised/pmmo/client/events/$ScreenHandler" {
import {$ScreenEvent$Init$Post, $ScreenEvent$Init$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$Init$Post"
import {$ScreenEvent$Render$Post, $ScreenEvent$Render$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$Render$Post"

export class $ScreenHandler {

constructor()

public static "onScreenInit"(arg0: $ScreenEvent$Init$Post$Type): void
public static "onScreenRender"(arg0: $ScreenEvent$Render$Post$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenHandler$Type = ($ScreenHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenHandler_ = $ScreenHandler$Type;
}}
declare module "packages/harmonised/pmmo/registry/$PredicateRegistry" {
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$ReqType, $ReqType$Type} from "packages/harmonised/pmmo/api/enums/$ReqType"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $PredicateRegistry {

constructor()

public "predicateExists"(arg0: $ResourceLocation$Type, arg1: $ReqType$Type): boolean
public "checkPredicateReq"(arg0: $Player$Type, arg1: $BlockEntity$Type, arg2: $ReqType$Type): boolean
public "checkPredicateReq"(arg0: $Player$Type, arg1: $Entity$Type, arg2: $ReqType$Type): boolean
public "checkPredicateReq"(arg0: $Player$Type, arg1: $ItemStack$Type, arg2: $ReqType$Type): boolean
public "registerPredicate"(arg0: $ResourceLocation$Type, arg1: $ReqType$Type, arg2: $BiPredicate$Type<($Player$Type), ($ItemStack$Type)>): void
public "registerBreakPredicate"(arg0: $ResourceLocation$Type, arg1: $ReqType$Type, arg2: $BiPredicate$Type<($Player$Type), ($BlockEntity$Type)>): void
public "registerEntityPredicate"(arg0: $ResourceLocation$Type, arg1: $ReqType$Type, arg2: $BiPredicate$Type<($Player$Type), ($Entity$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PredicateRegistry$Type = ($PredicateRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PredicateRegistry_ = $PredicateRegistry$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$CraftHandler" {
import {$PlayerEvent$ItemCraftedEvent, $PlayerEvent$ItemCraftedEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$ItemCraftedEvent"

export class $CraftHandler {

constructor()

public static "handle"(arg0: $PlayerEvent$ItemCraftedEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftHandler$Type = ($CraftHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftHandler_ = $CraftHandler$Type;
}}
declare module "packages/harmonised/pmmo/config/codecs/$CodecTypes" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$PrimitiveCodec, $PrimitiveCodec$Type} from "packages/com/mojang/serialization/codecs/$PrimitiveCodec"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$CodecTypes$SalvageData, $CodecTypes$SalvageData$Type} from "packages/harmonised/pmmo/config/codecs/$CodecTypes$SalvageData"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CodecTypes {
static readonly "DOUBLE_CODEC": $Codec<($Map<(string), (double)>)>
static readonly "LONG_CODEC": $Codec<($Map<(string), (long)>)>
static readonly "INTEGER_CODEC": $Codec<($Map<(string), (integer)>)>
static readonly "DAMAGE_XP_CODEC": $Codec<($Map<(string), ($Map<(string), (long)>)>)>
static readonly "SALVAGE_CODEC": $Codec<($CodecTypes$SalvageData)>
static readonly "UUID_CODEC": $PrimitiveCodec<($UUID)>
static readonly "BLOCKPOS_CODEC": $PrimitiveCodec<($BlockPos)>
static readonly "CHUNKPOS_CODEC": $PrimitiveCodec<($ChunkPos)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CodecTypes$Type = ($CodecTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CodecTypes_ = $CodecTypes$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$CropGrowHandler" {
import {$BlockEvent$CropGrowEvent$Post, $BlockEvent$CropGrowEvent$Post$Type} from "packages/net/minecraftforge/event/level/$BlockEvent$CropGrowEvent$Post"

export class $CropGrowHandler {

constructor()

public static "handle"(arg0: $BlockEvent$CropGrowEvent$Post$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CropGrowHandler$Type = ($CropGrowHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CropGrowHandler_ = $CropGrowHandler$Type;
}}
declare module "packages/harmonised/pmmo/compat/crafttweaker/$CTTickFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $CTTickFunction {

}

export namespace $CTTickFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CTTickFunction$Type = ($CTTickFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CTTickFunction_ = $CTTickFunction$Type;
}}
declare module "packages/harmonised/pmmo/setup/datagen/$BlockTagProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$BlockTagsProvider, $BlockTagsProvider$Type} from "packages/net/minecraftforge/common/data/$BlockTagsProvider"

export class $BlockTagProvider extends $BlockTagsProvider {

constructor(arg0: $PackOutput$Type, arg1: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, arg2: $ExistingFileHelper$Type)

public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockTagProvider$Type = ($BlockTagProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockTagProvider_ = $BlockTagProvider$Type;
}}
declare module "packages/harmonised/pmmo/client/events/$ClientTickHandler" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ClientTickHandler$GainEntry, $ClientTickHandler$GainEntry$Type} from "packages/harmonised/pmmo/client/events/$ClientTickHandler$GainEntry"
import {$TickEvent$ClientTickEvent, $TickEvent$ClientTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ClientTickEvent"

export class $ClientTickHandler {
static readonly "xpGains": $List<($ClientTickHandler$GainEntry)>

constructor()

public static "isRefreshTick"(): boolean
public static "tickGUI"(): void
public static "tickDownGainList"(): void
public static "addToGainList"(arg0: string, arg1: long): void
public static "resetTicks"(): void
public static "onClientTick"(arg0: $TickEvent$ClientTickEvent$Type): void
get "refreshTick"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTickHandler$Type = ($ClientTickHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTickHandler_ = $ClientTickHandler$Type;
}}
declare module "packages/harmonised/pmmo/network/clientpackets/$CP_SyncData_ClearXp" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $CP_SyncData_ClearXp {

constructor()

public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CP_SyncData_ClearXp$Type = ($CP_SyncData_ClearXp);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CP_SyncData_ClearXp_ = $CP_SyncData_ClearXp$Type;
}}
declare module "packages/harmonised/pmmo/registry/$TooltipRegistry" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ReqType, $ReqType$Type} from "packages/harmonised/pmmo/api/enums/$ReqType"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ModifierDataType, $ModifierDataType$Type} from "packages/harmonised/pmmo/api/enums/$ModifierDataType"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $TooltipRegistry {

constructor()

public "clearRegistry"(): void
public "bonusTooltipExists"(arg0: $ResourceLocation$Type, arg1: $ModifierDataType$Type): boolean
public "requirementTooltipExists"(arg0: $ResourceLocation$Type, arg1: $ReqType$Type): boolean
public "xpGainTooltipExists"(arg0: $ResourceLocation$Type, arg1: $EventType$Type): boolean
public "getBlockRequirementTooltipData"(arg0: $ResourceLocation$Type, arg1: $ReqType$Type, arg2: $BlockEntity$Type): $Map<(string), (integer)>
public "getBonusTooltipData"(arg0: $ResourceLocation$Type, arg1: $ModifierDataType$Type, arg2: $ItemStack$Type): $Map<(string), (double)>
public "getBlockXpGainTooltipData"(arg0: $ResourceLocation$Type, arg1: $EventType$Type, arg2: $BlockEntity$Type): $Map<(string), (long)>
public "getItemXpGainTooltipData"(arg0: $ResourceLocation$Type, arg1: $EventType$Type, arg2: $ItemStack$Type): $Map<(string), (long)>
public "getItemRequirementTooltipData"(arg0: $ResourceLocation$Type, arg1: $ReqType$Type, arg2: $ItemStack$Type): $Map<(string), (integer)>
public "getEntityRequirementTooltipData"(arg0: $ResourceLocation$Type, arg1: $ReqType$Type, arg2: $Entity$Type): $Map<(string), (integer)>
public "getEntityXpGainTooltipData"(arg0: $ResourceLocation$Type, arg1: $EventType$Type, arg2: $Entity$Type): $Map<(string), (long)>
public "registerItemXpGainTooltipData"(arg0: $ResourceLocation$Type, arg1: $EventType$Type, arg2: $Function$Type<($ItemStack$Type), ($Map$Type<(string), (long)>)>): void
public "registerBlockXpGainTooltipData"(arg0: $ResourceLocation$Type, arg1: $EventType$Type, arg2: $Function$Type<($BlockEntity$Type), ($Map$Type<(string), (long)>)>): void
public "registerItemRequirementTooltipData"(arg0: $ResourceLocation$Type, arg1: $ReqType$Type, arg2: $Function$Type<($ItemStack$Type), ($Map$Type<(string), (integer)>)>): void
public "registerEntityXpGainTooltipData"(arg0: $ResourceLocation$Type, arg1: $EventType$Type, arg2: $Function$Type<($Entity$Type), ($Map$Type<(string), (long)>)>): void
public "registerItemBonusTooltipData"(arg0: $ResourceLocation$Type, arg1: $ModifierDataType$Type, arg2: $Function$Type<($ItemStack$Type), ($Map$Type<(string), (double)>)>): void
public "registerBlockRequirementTooltipData"(arg0: $ResourceLocation$Type, arg1: $ReqType$Type, arg2: $Function$Type<($BlockEntity$Type), ($Map$Type<(string), (integer)>)>): void
public "registerEntityRequirementTooltipData"(arg0: $ResourceLocation$Type, arg1: $ReqType$Type, arg2: $Function$Type<($Entity$Type), ($Map$Type<(string), (integer)>)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TooltipRegistry$Type = ($TooltipRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TooltipRegistry_ = $TooltipRegistry$Type;
}}
declare module "packages/harmonised/pmmo/config/readers/$TomlConfigHelper" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"
import {$TomlConfigHelper$ConfigObject, $TomlConfigHelper$ConfigObject$Type} from "packages/harmonised/pmmo/config/readers/$TomlConfigHelper$ConfigObject"
import {$ModConfig$Type, $ModConfig$Type$Type} from "packages/net/minecraftforge/fml/config/$ModConfig$Type"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $TomlConfigHelper extends $Record {

constructor(builder: $ForgeConfigSpec$Builder$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "builder"(): $ForgeConfigSpec$Builder
public static "register"<T>(arg0: $ModConfig$Type$Type, arg1: $Function$Type<($ForgeConfigSpec$Builder$Type), (T)>, arg2: string): T
public static "register"<T>(arg0: $ModConfig$Type$Type, arg1: $Function$Type<($ForgeConfigSpec$Builder$Type), (T)>): T
public static "defineObject"<T>(arg0: $ForgeConfigSpec$Builder$Type, arg1: string, arg2: $Codec$Type<(T)>, arg3: T): $TomlConfigHelper$ConfigObject<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TomlConfigHelper$Type = ($TomlConfigHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TomlConfigHelper_ = $TomlConfigHelper$Type;
}}
declare module "packages/harmonised/pmmo/network/clientpackets/$CP_UpdateExperience" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $CP_UpdateExperience {

constructor(arg0: string, arg1: long)
constructor(arg0: $FriendlyByteBuf$Type)

public "toBytes"(arg0: $FriendlyByteBuf$Type): void
public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CP_UpdateExperience$Type = ($CP_UpdateExperience);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CP_UpdateExperience_ = $CP_UpdateExperience$Type;
}}
declare module "packages/harmonised/pmmo/setup/datagen/$EntityTagProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$EntityTypeTagsProvider, $EntityTypeTagsProvider$Type} from "packages/net/minecraft/data/tags/$EntityTypeTagsProvider"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"

export class $EntityTagProvider extends $EntityTypeTagsProvider {

constructor(arg0: $PackOutput$Type, arg1: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, arg2: $ExistingFileHelper$Type)

public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityTagProvider$Type = ($EntityTagProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityTagProvider_ = $EntityTagProvider$Type;
}}
declare module "packages/harmonised/pmmo/config/readers/$TomlConfigHelper$ConfigObject" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $TomlConfigHelper$ConfigObject<T> implements $Supplier<(T)> {


public "get"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TomlConfigHelper$ConfigObject$Type<T> = ($TomlConfigHelper$ConfigObject<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TomlConfigHelper$ConfigObject_<T> = $TomlConfigHelper$ConfigObject$Type<(T)>;
}}
declare module "packages/harmonised/pmmo/network/clientpackets/$CP_SyncVein" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $CP_SyncVein {

constructor(arg0: double)
constructor(arg0: $FriendlyByteBuf$Type)

public "encode"(arg0: $FriendlyByteBuf$Type): void
public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CP_SyncVein$Type = ($CP_SyncVein);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CP_SyncVein_ = $CP_SyncVein$Type;
}}
declare module "packages/harmonised/pmmo/features/autovalues/$AutoValueConfig$WearableTypes" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"

export class $AutoValueConfig$WearableTypes extends $Enum<($AutoValueConfig$WearableTypes)> {
static readonly "HEAD": $AutoValueConfig$WearableTypes
static readonly "CHEST": $AutoValueConfig$WearableTypes
static readonly "LEGS": $AutoValueConfig$WearableTypes
static readonly "BOOTS": $AutoValueConfig$WearableTypes
static readonly "WINGS": $AutoValueConfig$WearableTypes


public static "values"(): ($AutoValueConfig$WearableTypes)[]
public static "valueOf"(arg0: string): $AutoValueConfig$WearableTypes
public static "fromSlot"(arg0: $EquipmentSlot$Type, arg1: boolean): $AutoValueConfig$WearableTypes
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoValueConfig$WearableTypes$Type = (("head") | ("chest") | ("wings") | ("legs") | ("boots")) | ($AutoValueConfig$WearableTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoValueConfig$WearableTypes_ = $AutoValueConfig$WearableTypes$Type;
}}
declare module "packages/harmonised/pmmo/config/codecs/$DataSource" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ReqType, $ReqType$Type} from "packages/harmonised/pmmo/api/enums/$ReqType"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ModifierDataType, $ModifierDataType$Type} from "packages/harmonised/pmmo/api/enums/$ModifierDataType"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $DataSource<T> {

 "combine"(arg0: T): T
 "getTagValues"(): $Set<(string)>
 "getBonuses"(arg0: $ModifierDataType$Type, arg1: $CompoundTag$Type): $Map<(string), (double)>
 "getXpValues"(arg0: $EventType$Type, arg1: $CompoundTag$Type): $Map<(string), (long)>
 "getReqs"(arg0: $ReqType$Type, arg1: $CompoundTag$Type): $Map<(string), (integer)>
 "getNegativeEffect"(): $Map<($ResourceLocation), (integer)>
 "setNegativeEffects"(arg0: $Map$Type<($ResourceLocation$Type), (integer)>): void
 "setBonuses"(arg0: $ModifierDataType$Type, arg1: $Map$Type<(string), (double)>): void
 "setXpValues"(arg0: $EventType$Type, arg1: $Map$Type<(string), (long)>): void
 "setReqs"(arg0: $ReqType$Type, arg1: $Map$Type<(string), (integer)>): void
 "setPositiveEffects"(arg0: $Map$Type<($ResourceLocation$Type), (integer)>): void
 "getPositiveEffect"(): $Map<($ResourceLocation), (integer)>
 "isUnconfigured"(): boolean
}

export namespace $DataSource {
function clearEmptyValues<K, V>(arg0: $Map$Type<(K), (V)>): $HashMap<(K), (V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataSource$Type<T> = ($DataSource<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataSource_<T> = $DataSource$Type<(T)>;
}}
declare module "packages/harmonised/pmmo/events/impl/$PotionHandler" {
import {$PlayerBrewedPotionEvent, $PlayerBrewedPotionEvent$Type} from "packages/net/minecraftforge/event/brewing/$PlayerBrewedPotionEvent"

export class $PotionHandler {

constructor()

public static "handle"(arg0: $PlayerBrewedPotionEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PotionHandler$Type = ($PotionHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PotionHandler_ = $PotionHandler$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$JumpHandler" {
import {$LivingEvent$LivingJumpEvent, $LivingEvent$LivingJumpEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingEvent$LivingJumpEvent"

export class $JumpHandler {

constructor()

public static "handle"(arg0: $LivingEvent$LivingJumpEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JumpHandler$Type = ($JumpHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JumpHandler_ = $JumpHandler$Type;
}}
declare module "packages/harmonised/pmmo/setup/datagen/$LangProvider$Translation" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LangProvider$Translation extends $Record {

constructor(key: string, localeMap: $Map$Type<(string), (string)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "key"(): string
public "asComponent"(...arg0: (any)[]): $MutableComponent
public "asComponent"(): $MutableComponent
public "localeMap"(): $Map<(string), (string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LangProvider$Translation$Type = ($LangProvider$Translation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LangProvider$Translation_ = $LangProvider$Translation$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$ShieldBlockHandler" {
import {$ShieldBlockEvent, $ShieldBlockEvent$Type} from "packages/net/minecraftforge/event/entity/living/$ShieldBlockEvent"

export class $ShieldBlockHandler {

constructor()

public static "handle"(arg0: $ShieldBlockEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShieldBlockHandler$Type = ($ShieldBlockHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShieldBlockHandler_ = $ShieldBlockHandler$Type;
}}
declare module "packages/harmonised/pmmo/features/mobscaling/$MobAttributeHandler" {
import {$MobSpawnEvent$FinalizeSpawn, $MobSpawnEvent$FinalizeSpawn$Type} from "packages/net/minecraftforge/event/entity/living/$MobSpawnEvent$FinalizeSpawn"
import {$EntityJoinLevelEvent, $EntityJoinLevelEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityJoinLevelEvent"

export class $MobAttributeHandler {

constructor()

public static "onBossAdd"(arg0: $EntityJoinLevelEvent$Type): void
public static "onMobSpawn"(arg0: $MobSpawnEvent$FinalizeSpawn$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobAttributeHandler$Type = ($MobAttributeHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobAttributeHandler_ = $MobAttributeHandler$Type;
}}
declare module "packages/harmonised/pmmo/util/$Functions" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Functions<X, Y> {

constructor()

public static "mergeMaps"<K, V extends number>(arg0: $Map$Type<(K), (V)>, arg1: $Map$Type<(K), (V)>): $Map<(K), (V)>
public static "mergeMaps"<K, V extends number>(...arg0: ($Map$Type<(K), (V)>)[]): $Map<(K), (V)>
public static "pathPrepend"(arg0: $ResourceLocation$Type, arg1: string): $ResourceLocation
public static "getReliableUUID"(arg0: string): $UUID
public static "memoize"<X, Y>(arg0: $Function$Type<(X), (Y)>): $Function<(X), (Y)>
public static "biPermutation"<T>(arg0: T, arg1: T, arg2: boolean, arg3: boolean, arg4: $BiConsumer$Type<(T), (T)>, arg5: $BiConsumer$Type<(T), (T)>, arg6: $BiConsumer$Type<(T), (T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Functions$Type<X, Y> = ($Functions<(X), (Y)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Functions_<X, Y> = $Functions$Type<(X), (Y)>;
}}
declare module "packages/harmonised/pmmo/compat/curios/$CurioCompat" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $CurioCompat {
static "hasCurio": boolean

constructor()

public static "getItems"(arg0: $Player$Type): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CurioCompat$Type = ($CurioCompat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CurioCompat_ = $CurioCompat$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$PlayerDeathHandler" {
import {$LivingDeathEvent, $LivingDeathEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingDeathEvent"

export class $PlayerDeathHandler {

constructor()

public static "handle"(arg0: $LivingDeathEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerDeathHandler$Type = ($PlayerDeathHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerDeathHandler_ = $PlayerDeathHandler$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$LoginHandler" {
import {$PlayerEvent$PlayerLoggedInEvent, $PlayerEvent$PlayerLoggedInEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerLoggedInEvent"

export class $LoginHandler {

constructor()

public static "handle"(arg0: $PlayerEvent$PlayerLoggedInEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LoginHandler$Type = ($LoginHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LoginHandler_ = $LoginHandler$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$DeathHandler" {
import {$LivingDeathEvent, $LivingDeathEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingDeathEvent"

export class $DeathHandler {

constructor()

public static "handle"(arg0: $LivingDeathEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeathHandler$Type = ($DeathHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeathHandler_ = $DeathHandler$Type;
}}
declare module "packages/harmonised/pmmo/features/autovalues/$AutoValueConfig" {
import {$AutoValueConfig$WearableTypes, $AutoValueConfig$WearableTypes$Type} from "packages/harmonised/pmmo/features/autovalues/$AutoValueConfig$WearableTypes"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$TomlConfigHelper$ConfigObject, $TomlConfigHelper$ConfigObject$Type} from "packages/harmonised/pmmo/config/readers/$TomlConfigHelper$ConfigObject"
import {$AutoValueConfig$AttributeKey, $AutoValueConfig$AttributeKey$Type} from "packages/harmonised/pmmo/features/autovalues/$AutoValueConfig$AttributeKey"
import {$ForgeConfigSpec$DoubleValue, $ForgeConfigSpec$DoubleValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$DoubleValue"
import {$AutoValueConfig$UtensilTypes, $AutoValueConfig$UtensilTypes$Type} from "packages/harmonised/pmmo/features/autovalues/$AutoValueConfig$UtensilTypes"
import {$ReqType, $ReqType$Type} from "packages/harmonised/pmmo/api/enums/$ReqType"
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AutoValueConfig {
static "SERVER_CONFIG": $ForgeConfigSpec
static "ENABLE_AUTO_VALUES": $ForgeConfigSpec$ConfigValue<(boolean)>
static "AXE_OVERRIDE": $TomlConfigHelper$ConfigObject<($Map<(string), (long)>)>
static "HOE_OVERRIDE": $TomlConfigHelper$ConfigObject<($Map<(string), (long)>)>
static "SHOVEL_OVERRIDE": $TomlConfigHelper$ConfigObject<($Map<(string), (long)>)>
static "RARITIES_MODIFIER": $ForgeConfigSpec$DoubleValue
static "BREWABLES_OVERRIDE": $TomlConfigHelper$ConfigObject<($Map<(string), (long)>)>
static "SMELTABLES_OVERRIDE": $TomlConfigHelper$ConfigObject<($Map<(string), (long)>)>
static "ITEM_PENALTIES": $TomlConfigHelper$ConfigObject<($Map<($ResourceLocation), (integer)>)>
static "HARDNESS_MODIFIER": $ForgeConfigSpec$ConfigValue<(double)>
static "ENTITY_ATTRIBUTES": $TomlConfigHelper$ConfigObject<($Map<(string), (double)>)>

constructor()

public static "getUtensilAttribute"(arg0: $AutoValueConfig$UtensilTypes$Type, arg1: $AutoValueConfig$AttributeKey$Type): double
public static "getWearableAttribute"(arg0: $AutoValueConfig$WearableTypes$Type, arg1: $AutoValueConfig$AttributeKey$Type): double
public static "getItemXpAward"(arg0: $EventType$Type): $Map<(string), (long)>
public static "getEntityXpAward"(arg0: $EventType$Type): $Map<(string), (long)>
public static "isReqEnabled"(arg0: $ReqType$Type): boolean
public static "isXpGainEnabled"(arg0: $EventType$Type): boolean
public static "getBlockXpAward"(arg0: $EventType$Type): $Map<(string), (long)>
public static "getToolReq"(arg0: $ItemStack$Type): $Map<(string), (integer)>
public static "getItemReq"(arg0: $ReqType$Type): $Map<(string), (integer)>
public static "getBlockReq"(arg0: $ReqType$Type): $Map<(string), (integer)>
public static "setupServer"(arg0: $ForgeConfigSpec$Builder$Type): void
set "upServer"(value: $ForgeConfigSpec$Builder$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoValueConfig$Type = ($AutoValueConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoValueConfig_ = $AutoValueConfig$Type;
}}
declare module "packages/harmonised/pmmo/core/nbt/$LogicEntry" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$LogicEntry$Case, $LogicEntry$Case$Type} from "packages/harmonised/pmmo/core/nbt/$LogicEntry$Case"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BehaviorToPrevious, $BehaviorToPrevious$Type} from "packages/harmonised/pmmo/core/nbt/$BehaviorToPrevious"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $LogicEntry extends $Record {
static readonly "CODEC": $Codec<($LogicEntry)>

constructor(behavior: $BehaviorToPrevious$Type, addCases: boolean, cases: $List$Type<($LogicEntry$Case$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "cases"(): $List<($LogicEntry$Case)>
public "addCases"(): boolean
public "behavior"(): $BehaviorToPrevious
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LogicEntry$Type = ($LogicEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LogicEntry_ = $LogicEntry$Type;
}}
declare module "packages/harmonised/pmmo/config/$PerksConfig" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$TomlConfigHelper$ConfigObject, $TomlConfigHelper$ConfigObject$Type} from "packages/harmonised/pmmo/config/readers/$TomlConfigHelper$ConfigObject"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $PerksConfig {
static "SERVER_CONFIG": $ForgeConfigSpec
static "PERK_SETTINGS": $TomlConfigHelper$ConfigObject<($Map<($EventType), ($List<($CompoundTag)>)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PerksConfig$Type = ($PerksConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PerksConfig_ = $PerksConfig$Type;
}}
declare module "packages/harmonised/pmmo/core/nbt/$Result" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Operator, $Operator$Type} from "packages/harmonised/pmmo/core/nbt/$Operator"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Result extends $Record {

constructor(operator: $Operator$Type, comparator: string, comparison: string, values: $Map$Type<(string), (double)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "values"(): $Map<(string), (double)>
public "hashCode"(): integer
public "operator"(): $Operator
public "comparator"(): string
public "comparison"(): string
public "compares"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Result$Type = ($Result);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Result_ = $Result$Type;
}}
declare module "packages/harmonised/pmmo/registry/$LevelRegistry" {
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"

export class $LevelRegistry {

constructor()

public "process"(arg0: string, arg1: integer): integer
public "registerLevelProvider"(arg0: $BiFunction$Type<(string), (integer), (integer)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LevelRegistry$Type = ($LevelRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LevelRegistry_ = $LevelRegistry$Type;
}}
declare module "packages/harmonised/pmmo/config/$Config" {
import {$ForgeConfigSpec$IntValue, $ForgeConfigSpec$IntValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$IntValue"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$TomlConfigHelper$ConfigObject, $TomlConfigHelper$ConfigObject$Type} from "packages/harmonised/pmmo/config/readers/$TomlConfigHelper$ConfigObject"
import {$ForgeConfigSpec$BooleanValue, $ForgeConfigSpec$BooleanValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$BooleanValue"
import {$ForgeConfigSpec$DoubleValue, $ForgeConfigSpec$DoubleValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$DoubleValue"
import {$ReqType, $ReqType$Type} from "packages/harmonised/pmmo/api/enums/$ReqType"
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ModifierDataType, $ModifierDataType$Type} from "packages/harmonised/pmmo/api/enums/$ModifierDataType"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$ConfigHelper$ConfigObject, $ConfigHelper$ConfigObject$Type} from "packages/harmonised/pmmo/config/readers/$ConfigHelper$ConfigObject"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Config {
static "CLIENT_CONFIG": $ForgeConfigSpec
static "COMMON_CONFIG": $ForgeConfigSpec
static "SERVER_CONFIG": $ForgeConfigSpec
static "SKILL_LIST_OFFSET_X": $ForgeConfigSpec$ConfigValue<(double)>
static "SKILL_LIST_OFFSET_Y": $ForgeConfigSpec$ConfigValue<(double)>
static "VEIN_GAUGE_OFFSET_X": $ForgeConfigSpec$ConfigValue<(double)>
static "VEIN_GAUGE_OFFSET_Y": $ForgeConfigSpec$ConfigValue<(double)>
static "GAIN_LIST_OFFSET_X": $ForgeConfigSpec$ConfigValue<(double)>
static "GAIN_LIST_OFFSET_Y": $ForgeConfigSpec$ConfigValue<(double)>
static "SKILL_LIST_DISPLAY": $ForgeConfigSpec$ConfigValue<(boolean)>
static "GAIN_LIST_DISPLAY": $ForgeConfigSpec$ConfigValue<(boolean)>
static "VEIN_GAUGE_DISPLAY": $ForgeConfigSpec$ConfigValue<(boolean)>
static "SECTION_HEADER_COLOR": $ForgeConfigSpec$ConfigValue<(integer)>
static "SALVAGE_ITEM_COLOR": $ForgeConfigSpec$ConfigValue<(integer)>
static "GAIN_LIST_LINGER_DURATION": $ForgeConfigSpec$ConfigValue<(integer)>
static "GAIN_BLACKLIST": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static "HIDE_SKILL_BUTTON": $ForgeConfigSpec$BooleanValue
static "SKILL_BUTTON_X": $ForgeConfigSpec$ConfigValue<(integer)>
static "SKILL_BUTTON_Y": $ForgeConfigSpec$ConfigValue<(integer)>
static "SKILLUP_UNLOCKS": $ForgeConfigSpec$BooleanValue
static "HIDE_MET_REQS": $ForgeConfigSpec$BooleanValue
static "VEIN_LIMIT": $ForgeConfigSpec$IntValue
static "BREAK_NERF_HIGHLIGHTS": $ForgeConfigSpec$BooleanValue
static "BLOCK_OWNER_HIGHLIGHTS": $ForgeConfigSpec$BooleanValue
static "SALVAGE_HIGHLIGHTS": $ForgeConfigSpec$BooleanValue
static "INFO_LOGGING": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static "DEBUG_LOGGING": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static "WARN_LOGGING": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static "ERROR_LOGGING": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static "FATAL_LOGGING": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static "CREATIVE_REACH": $ForgeConfigSpec$ConfigValue<(double)>
static "SALVAGE_BLOCK": $ForgeConfigSpec$ConfigValue<(string)>
static "TREASURE_ENABLED": $ForgeConfigSpec$BooleanValue
static "BREWING_TRACKED": $ForgeConfigSpec$BooleanValue
static "MAX_LEVEL": $ForgeConfigSpec$ConfigValue<(integer)>
static "LOSS_ON_DEATH": $ForgeConfigSpec$ConfigValue<(double)>
static "LOSE_LEVELS_ON_DEATH": $ForgeConfigSpec$ConfigValue<(boolean)>
static "LOSE_ONLY_EXCESS": $ForgeConfigSpec$ConfigValue<(boolean)>
static "USE_EXPONENTIAL_FORMULA": $ForgeConfigSpec$ConfigValue<(boolean)>
static "GLOBAL_MODIFIER": $ForgeConfigSpec$ConfigValue<(double)>
static "SKILL_MODIFIERS": $TomlConfigHelper$ConfigObject<($Map<(string), (double)>)>
static "LINEAR_BASE_XP": $ForgeConfigSpec$ConfigValue<(long)>
static "LINEAR_PER_LEVEL": $ForgeConfigSpec$ConfigValue<(double)>
static "EXPONENTIAL_BASE_XP": $ForgeConfigSpec$ConfigValue<(integer)>
static "EXPONENTIAL_POWER_BASE": $ForgeConfigSpec$ConfigValue<(double)>
static "EXPONENTIAL_LEVEL_MOD": $ForgeConfigSpec$ConfigValue<(double)>
static "STATIC_LEVELS": $ConfigHelper$ConfigObject<($List<(long)>)>
static "REUSE_PENALTY": $ForgeConfigSpec$ConfigValue<(double)>
static "SUMMATED_MAPS": $ForgeConfigSpec$ConfigValue<(boolean)>
static "RECEIVE_DAMAGE_XP": $TomlConfigHelper$ConfigObject<($Map<(string), ($Map<(string), (long)>)>)>
static "DEAL_DAMAGE_XP": $TomlConfigHelper$ConfigObject<($Map<(string), ($Map<(string), (long)>)>)>
static "JUMP_XP": $TomlConfigHelper$ConfigObject<($Map<(string), (double)>)>
static "SPRINT_JUMP_XP": $TomlConfigHelper$ConfigObject<($Map<(string), (double)>)>
static "CROUCH_JUMP_XP": $TomlConfigHelper$ConfigObject<($Map<(string), (double)>)>
static "BREATH_CHANGE_XP": $TomlConfigHelper$ConfigObject<($Map<(string), (double)>)>
static "HEALTH_CHANGE_XP": $TomlConfigHelper$ConfigObject<($Map<(string), (double)>)>
static "HEALTH_INCREASE_XP": $TomlConfigHelper$ConfigObject<($Map<(string), (double)>)>
static "HEALTH_DECREASE_XP": $TomlConfigHelper$ConfigObject<($Map<(string), (double)>)>
static "SPRINTING_XP": $TomlConfigHelper$ConfigObject<($Map<(string), (double)>)>
static "SUBMERGED_XP": $TomlConfigHelper$ConfigObject<($Map<(string), (double)>)>
static "SWIMMING_XP": $TomlConfigHelper$ConfigObject<($Map<(string), (double)>)>
static "DIVING_XP": $TomlConfigHelper$ConfigObject<($Map<(string), (double)>)>
static "SURFACING_XP": $TomlConfigHelper$ConfigObject<($Map<(string), (double)>)>
static "SWIM_SPRINTING_XP": $TomlConfigHelper$ConfigObject<($Map<(string), (double)>)>
static "PARTY_RANGE": $ForgeConfigSpec$IntValue
static "PARTY_BONUS": $ForgeConfigSpec$DoubleValue
static "MOB_SCALING_ENABLED": $ForgeConfigSpec$BooleanValue
static "MOB_USE_EXPONENTIAL_FORMULA": $ForgeConfigSpec$ConfigValue<(boolean)>
static "MOB_SCALING_AOE": $ForgeConfigSpec$ConfigValue<(integer)>
static "MOB_SCALING_BASE_LEVEL": $ForgeConfigSpec$ConfigValue<(integer)>
static "MOB_LINEAR_PER_LEVEL": $ForgeConfigSpec$ConfigValue<(double)>
static "MOB_EXPONENTIAL_POWER_BASE": $ForgeConfigSpec$ConfigValue<(double)>
static "MOB_EXPONENTIAL_LEVEL_MOD": $ForgeConfigSpec$ConfigValue<(double)>
static "BOSS_SCALING_RATIO": $ForgeConfigSpec$ConfigValue<(double)>
static "MOB_SCALING": $TomlConfigHelper$ConfigObject<($Map<(string), ($Map<(string), (double)>)>)>
static "VEIN_ENABLED": $ForgeConfigSpec$ConfigValue<(boolean)>
static "REQUIRE_SETTING": $ForgeConfigSpec$ConfigValue<(boolean)>
static "DEFAULT_CONSUME": $ForgeConfigSpec$ConfigValue<(integer)>
static "VEIN_CHARGE_MODIFIER": $ForgeConfigSpec$DoubleValue
static "VEIN_BLACKLIST": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static "BASE_CHARGE_RATE": $ForgeConfigSpec$DoubleValue
static "BASE_CHARGE_CAP": $ForgeConfigSpec$IntValue

constructor()

public static "tooltipXpEnabled"(arg0: $EventType$Type): $ForgeConfigSpec$BooleanValue
public static "tooltipReqEnabled"(arg0: $ReqType$Type): $ForgeConfigSpec$BooleanValue
public static "tooltipBonusEnabled"(arg0: $ModifierDataType$Type): $ForgeConfigSpec$BooleanValue
public static "reqEnabled"(arg0: $ReqType$Type): $ForgeConfigSpec$BooleanValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Config$Type = ($Config);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Config_ = $Config$Type;
}}
declare module "packages/harmonised/pmmo/api/events/$SalvageEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PlayerEvent, $PlayerEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $SalvageEvent extends $PlayerEvent {

constructor()
constructor(arg0: $Player$Type, arg1: $BlockPos$Type)

public "isCancelable"(): boolean
public "getBlockPos"(): $BlockPos
public "getListenerList"(): $ListenerList
get "cancelable"(): boolean
get "blockPos"(): $BlockPos
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SalvageEvent$Type = ($SalvageEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SalvageEvent_ = $SalvageEvent$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$DimensionTravelHandler" {
import {$EntityTravelToDimensionEvent, $EntityTravelToDimensionEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityTravelToDimensionEvent"

export class $DimensionTravelHandler {

constructor()

public static "handle"(arg0: $EntityTravelToDimensionEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DimensionTravelHandler$Type = ($DimensionTravelHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DimensionTravelHandler_ = $DimensionTravelHandler$Type;
}}
declare module "packages/harmonised/pmmo/config/readers/$ExecutableListener" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$SimplePreparableReloadListener, $SimplePreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimplePreparableReloadListener"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $ExecutableListener extends $SimplePreparableReloadListener<(boolean)> {

constructor(arg0: $Runnable$Type)

public "subscribeAsSyncable"<PACKET>(arg0: $SimpleChannel$Type, arg1: $Supplier$Type<(PACKET)>): $ExecutableListener
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExecutableListener$Type = ($ExecutableListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExecutableListener_ = $ExecutableListener$Type;
}}
declare module "packages/harmonised/pmmo/commands/$CmdPmmoRoot" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CmdPmmoRoot$Setting, $CmdPmmoRoot$Setting$Type} from "packages/harmonised/pmmo/commands/$CmdPmmoRoot$Setting"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"
import {$CommandContext, $CommandContext$Type} from "packages/com/mojang/brigadier/context/$CommandContext"

export class $CmdPmmoRoot {

constructor()

public static "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>): void
public static "set"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>, arg1: $CmdPmmoRoot$Setting$Type): integer
public static "help"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>): integer
public static "credits"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CmdPmmoRoot$Type = ($CmdPmmoRoot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CmdPmmoRoot_ = $CmdPmmoRoot$Type;
}}
declare module "packages/harmonised/pmmo/util/$MsLoggy" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$MsLoggy$LOG_CODE, $MsLoggy$LOG_CODE$Type} from "packages/harmonised/pmmo/util/$MsLoggy$LOG_CODE"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $MsLoggy extends $Enum<($MsLoggy)> {
static readonly "INFO": $MsLoggy
static readonly "WARN": $MsLoggy
static readonly "DEBUG": $MsLoggy
static readonly "ERROR": $MsLoggy
static readonly "FATAL": $MsLoggy


public static "values"(): ($MsLoggy)[]
public "log"<K, V>(arg0: $MsLoggy$LOG_CODE$Type, arg1: $Map$Type<(K), (V)>, arg2: string, ...arg3: (any)[]): void
public "log"<T>(arg0: $MsLoggy$LOG_CODE$Type, arg1: $Collection$Type<(T)>, arg2: string, ...arg3: (any)[]): void
public "log"(arg0: $MsLoggy$LOG_CODE$Type, arg1: string, ...arg2: (any)[]): void
public static "valueOf"(arg0: string): $MsLoggy
public "logAndReturn"<VALUE>(arg0: VALUE, arg1: $MsLoggy$LOG_CODE$Type, arg2: string, ...arg3: (any)[]): VALUE
public static "listToString"<T>(arg0: $Collection$Type<(T)>): string
public static "mapToString"(arg0: $Map$Type<(any), (any)>): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MsLoggy$Type = (("warn") | ("debug") | ("error") | ("info") | ("fatal")) | ($MsLoggy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MsLoggy_ = $MsLoggy$Type;
}}
declare module "packages/harmonised/pmmo/config/codecs/$EnhancementsData" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ReqType, $ReqType$Type} from "packages/harmonised/pmmo/api/enums/$ReqType"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ModifierDataType, $ModifierDataType$Type} from "packages/harmonised/pmmo/api/enums/$ModifierDataType"
import {$EventType, $EventType$Type} from "packages/harmonised/pmmo/api/enums/$EventType"
import {$DataSource, $DataSource$Type} from "packages/harmonised/pmmo/config/codecs/$DataSource"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $EnhancementsData extends $Record implements $DataSource<($EnhancementsData)> {
static readonly "CODEC": $Codec<($EnhancementsData)>

constructor()
constructor(override: boolean, skillArray: $Map$Type<(integer), ($Map$Type<(string), (integer)>)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "combine"(arg0: $EnhancementsData$Type): $EnhancementsData
public "override"(): boolean
public "skillArray"(): $Map<(integer), ($Map<(string), (integer)>)>
public "isUnconfigured"(): boolean
public "getTagValues"(): $Set<(string)>
public "getBonuses"(arg0: $ModifierDataType$Type, arg1: $CompoundTag$Type): $Map<(string), (double)>
public "getXpValues"(arg0: $EventType$Type, arg1: $CompoundTag$Type): $Map<(string), (long)>
public "getReqs"(arg0: $ReqType$Type, arg1: $CompoundTag$Type): $Map<(string), (integer)>
public "getNegativeEffect"(): $Map<($ResourceLocation), (integer)>
public "setNegativeEffects"(arg0: $Map$Type<($ResourceLocation$Type), (integer)>): void
public "setBonuses"(arg0: $ModifierDataType$Type, arg1: $Map$Type<(string), (double)>): void
public "setXpValues"(arg0: $EventType$Type, arg1: $Map$Type<(string), (long)>): void
public "setReqs"(arg0: $ReqType$Type, arg1: $Map$Type<(string), (integer)>): void
public "setPositiveEffects"(arg0: $Map$Type<($ResourceLocation$Type), (integer)>): void
public "getPositiveEffect"(): $Map<($ResourceLocation), (integer)>
public static "clearEmptyValues"<K, V>(arg0: $Map$Type<(K), (V)>): $HashMap<(K), (V)>
get "unconfigured"(): boolean
get "tagValues"(): $Set<(string)>
get "negativeEffect"(): $Map<($ResourceLocation), (integer)>
set "negativeEffects"(value: $Map$Type<($ResourceLocation$Type), (integer)>)
set "positiveEffects"(value: $Map$Type<($ResourceLocation$Type), (integer)>)
get "positiveEffect"(): $Map<($ResourceLocation), (integer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnhancementsData$Type = ($EnhancementsData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnhancementsData_ = $EnhancementsData$Type;
}}
declare module "packages/harmonised/pmmo/compat/crafttweaker/$CTPerkPredicate" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $CTPerkPredicate {

}

export namespace $CTPerkPredicate {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CTPerkPredicate$Type = ($CTPerkPredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CTPerkPredicate_ = $CTPerkPredicate$Type;
}}
declare module "packages/harmonised/pmmo/events/impl/$PlayerClickHandler" {
import {$PlayerInteractEvent$LeftClickBlock, $PlayerInteractEvent$LeftClickBlock$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$LeftClickBlock"
import {$PlayerInteractEvent$RightClickBlock, $PlayerInteractEvent$RightClickBlock$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$RightClickBlock"
import {$PlayerInteractEvent$RightClickItem, $PlayerInteractEvent$RightClickItem$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$RightClickItem"

export class $PlayerClickHandler {

constructor()

public static "rightClickBlock"(arg0: $PlayerInteractEvent$RightClickBlock$Type): void
public static "leftClickBlock"(arg0: $PlayerInteractEvent$LeftClickBlock$Type): void
public static "rightClickItem"(arg0: $PlayerInteractEvent$RightClickItem$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerClickHandler$Type = ($PlayerClickHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerClickHandler_ = $PlayerClickHandler$Type;
}}
