declare module "packages/snownee/jade/api/$IWailaClientRegistration" {
import {$JadeItemModNameCallback, $JadeItemModNameCallback$Type} from "packages/snownee/jade/api/callback/$JadeItemModNameCallback"
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$JadeAfterRenderCallback, $JadeAfterRenderCallback$Type} from "packages/snownee/jade/api/callback/$JadeAfterRenderCallback"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$BlockAccessor$Builder, $BlockAccessor$Builder$Type} from "packages/snownee/jade/api/$BlockAccessor$Builder"
import {$JadeRayTraceCallback, $JadeRayTraceCallback$Type} from "packages/snownee/jade/api/callback/$JadeRayTraceCallback"
import {$JadeTooltipCollectedCallback, $JadeTooltipCollectedCallback$Type} from "packages/snownee/jade/api/callback/$JadeTooltipCollectedCallback"
import {$JadeRenderBackgroundCallback, $JadeRenderBackgroundCallback$Type} from "packages/snownee/jade/api/callback/$JadeRenderBackgroundCallback"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$CustomEnchantPower, $CustomEnchantPower$Type} from "packages/snownee/jade/api/platform/$CustomEnchantPower"
import {$ItemView, $ItemView$Type} from "packages/snownee/jade/api/view/$ItemView"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$EntityAccessor$Builder, $EntityAccessor$Builder$Type} from "packages/snownee/jade/api/$EntityAccessor$Builder"
import {$Accessor$ClientHandler, $Accessor$ClientHandler$Type} from "packages/snownee/jade/api/$Accessor$ClientHandler"
import {$JadeBeforeRenderCallback, $JadeBeforeRenderCallback$Type} from "packages/snownee/jade/api/callback/$JadeBeforeRenderCallback"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$PlatformWailaClientRegistration, $PlatformWailaClientRegistration$Type} from "packages/snownee/jade/api/platform/$PlatformWailaClientRegistration"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ProgressView, $ProgressView$Type} from "packages/snownee/jade/api/view/$ProgressView"
import {$IClientExtensionProvider, $IClientExtensionProvider$Type} from "packages/snownee/jade/api/view/$IClientExtensionProvider"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$EnergyView, $EnergyView$Type} from "packages/snownee/jade/api/view/$EnergyView"
import {$FluidView, $FluidView$Type} from "packages/snownee/jade/api/view/$FluidView"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"

export interface $IWailaClientRegistration extends $PlatformWailaClientRegistration {

 "addAfterRenderCallback"(arg0: integer, arg1: $JadeAfterRenderCallback$Type): void
 "addAfterRenderCallback"(arg0: $JadeAfterRenderCallback$Type): void
 "addBeforeRenderCallback"(arg0: integer, arg1: $JadeBeforeRenderCallback$Type): void
 "addBeforeRenderCallback"(arg0: $JadeBeforeRenderCallback$Type): void
 "addTooltipCollectedCallback"(arg0: $JadeTooltipCollectedCallback$Type): void
 "addTooltipCollectedCallback"(arg0: integer, arg1: $JadeTooltipCollectedCallback$Type): void
 "addRenderBackgroundCallback"(arg0: $JadeRenderBackgroundCallback$Type): void
 "addRenderBackgroundCallback"(arg0: integer, arg1: $JadeRenderBackgroundCallback$Type): void
 "registerItemStorageClient"(arg0: $IClientExtensionProvider$Type<($ItemStack$Type), ($ItemView$Type)>): void
 "registerProgressClient"(arg0: $IClientExtensionProvider$Type<($CompoundTag$Type), ($ProgressView$Type)>): void
 "registerBlockComponent"(arg0: $IBlockComponentProvider$Type, arg1: $Class$Type<(any)>): void
 "addRayTraceCallback"(arg0: integer, arg1: $JadeRayTraceCallback$Type): void
 "addRayTraceCallback"(arg0: $JadeRayTraceCallback$Type): void
 "registerFluidStorageClient"(arg0: $IClientExtensionProvider$Type<($CompoundTag$Type), ($FluidView$Type)>): void
 "registerEnergyStorageClient"(arg0: $IClientExtensionProvider$Type<($CompoundTag$Type), ($EnergyView$Type)>): void
 "addItemModNameCallback"(arg0: integer, arg1: $JadeItemModNameCallback$Type): void
 "addItemModNameCallback"(arg0: $JadeItemModNameCallback$Type): void
 "registerEntityComponent"(arg0: $IEntityComponentProvider$Type, arg1: $Class$Type<(any)>): void
 "addConfig"(arg0: $ResourceLocation$Type, arg1: boolean): void
 "addConfig"(arg0: $ResourceLocation$Type, arg1: string, arg2: $Predicate$Type<(string)>): void
 "addConfig"(arg0: $ResourceLocation$Type, arg1: integer, arg2: integer, arg3: integer, arg4: boolean): void
 "addConfig"(arg0: $ResourceLocation$Type, arg1: $Enum$Type<(any)>): void
 "addConfig"(arg0: $ResourceLocation$Type, arg1: float, arg2: float, arg3: float, arg4: boolean): void
 "shouldHide"(arg0: $Entity$Type): boolean
 "shouldHide"(arg0: $BlockState$Type): boolean
 "shouldPick"(arg0: $BlockState$Type): boolean
 "shouldPick"(arg0: $Entity$Type): boolean
 "blockAccessor"(): $BlockAccessor$Builder
 "addConfigListener"(arg0: $ResourceLocation$Type, arg1: $Consumer$Type<($ResourceLocation$Type)>): void
 "hideTarget"(arg0: $EntityType$Type<(any)>): void
 "hideTarget"(arg0: $Block$Type): void
 "usePickedResult"(arg0: $Block$Type): void
 "usePickedResult"(arg0: $EntityType$Type<(any)>): void
 "entityAccessor"(): $EntityAccessor$Builder
 "registerBlockIcon"(arg0: $IBlockComponentProvider$Type, arg1: $Class$Type<(any)>): void
 "registerEntityIcon"(arg0: $IEntityComponentProvider$Type, arg1: $Class$Type<(any)>): void
 "isServerConnected"(): boolean
 "maybeLowVisionUser"(): boolean
 "setServerData"(arg0: $CompoundTag$Type): void
 "getAccessorHandler"(arg0: $Class$Type<(any)>): $Accessor$ClientHandler<($Accessor<(any)>)>
 "getServerData"(): $CompoundTag
 "getBlockCamouflage"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type): $ItemStack
 "registerAccessorHandler"<T extends $Accessor<(any)>>(arg0: $Class$Type<(T)>, arg1: $Accessor$ClientHandler$Type<(T)>): void
 "markAsServerFeature"(arg0: $ResourceLocation$Type): void
 "markAsClientFeature"(arg0: $ResourceLocation$Type): void
 "createPluginConfigScreen"(arg0: $Screen$Type, arg1: string): $Screen
 "isClientFeature"(arg0: $ResourceLocation$Type): boolean
 "isShowDetailsPressed"(): boolean
 "registerCustomEnchantPower"(arg0: $Block$Type, arg1: $CustomEnchantPower$Type): void
}

export namespace $IWailaClientRegistration {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IWailaClientRegistration$Type = ($IWailaClientRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IWailaClientRegistration_ = $IWailaClientRegistration$Type;
}}
declare module "packages/snownee/jade/addon/universal/$EntityItemStorageProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $EntityItemStorageProvider extends $Enum<($EntityItemStorageProvider)> implements $IEntityComponentProvider, $IServerDataProvider<($EntityAccessor)> {
static readonly "INSTANCE": $EntityItemStorageProvider


public static "values"(): ($EntityItemStorageProvider)[]
public static "valueOf"(arg0: string): $EntityItemStorageProvider
public "getUid"(): $ResourceLocation
public "getDefaultPriority"(): integer
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $EntityAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
get "required"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityItemStorageProvider$Type = (("instance")) | ($EntityItemStorageProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityItemStorageProvider_ = $EntityItemStorageProvider$Type;
}}
declare module "packages/snownee/jade/impl/ui/$SlimProgressStyle" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IProgressStyle, $IProgressStyle$Type} from "packages/snownee/jade/api/ui/$IProgressStyle"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $SlimProgressStyle implements $IProgressStyle {
 "color": integer
 "overlay": $IElement

constructor()

public "color"(arg0: integer, arg1: integer): $IProgressStyle
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Component$Type): void
public "overlay"(arg0: $IElement$Type): $IProgressStyle
public "textColor"(arg0: integer): $IProgressStyle
public "vertical"(arg0: boolean): $IProgressStyle
public "color"(arg0: integer): $IProgressStyle
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlimProgressStyle$Type = ($SlimProgressStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlimProgressStyle_ = $SlimProgressStyle$Type;
}}
declare module "packages/snownee/jade/gui/config/value/$SliderOptionValue" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$FloatUnaryOperator, $FloatUnaryOperator$Type} from "packages/it/unimi/dsi/fastutil/floats/$FloatUnaryOperator"
import {$OptionValue, $OptionValue$Type} from "packages/snownee/jade/gui/config/value/$OptionValue"

export class $SliderOptionValue extends $OptionValue<(float)> {
 "serverFeature": boolean

constructor(arg0: string, arg1: float, arg2: $Consumer$Type<(float)>, arg3: float, arg4: float, arg5: $FloatUnaryOperator$Type)

public "setValue"(arg0: float): void
set "value"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SliderOptionValue$Type = ($SliderOptionValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SliderOptionValue_ = $SliderOptionValue$Type;
}}
declare module "packages/snownee/jade/api/config/$IWailaConfig$BossBarOverlapMode" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$SimpleStringRepresentable, $SimpleStringRepresentable$Type} from "packages/snownee/jade/api/$SimpleStringRepresentable"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $IWailaConfig$BossBarOverlapMode extends $Enum<($IWailaConfig$BossBarOverlapMode)> implements $SimpleStringRepresentable {
static readonly "NO_OPERATION": $IWailaConfig$BossBarOverlapMode
static readonly "HIDE_BOSS_BAR": $IWailaConfig$BossBarOverlapMode
static readonly "HIDE_TOOLTIP": $IWailaConfig$BossBarOverlapMode
static readonly "PUSH_DOWN": $IWailaConfig$BossBarOverlapMode


public static "values"(): ($IWailaConfig$BossBarOverlapMode)[]
public static "valueOf"(arg0: string): $IWailaConfig$BossBarOverlapMode
public "getSerializedName"(): string
public static "fromEnumWithMapping"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
public static "keys"(arg0: ($StringRepresentable$Type)[]): $Keyable
public static "fromEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
get "serializedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IWailaConfig$BossBarOverlapMode$Type = (("no_operation") | ("hide_tooltip") | ("hide_boss_bar") | ("push_down")) | ($IWailaConfig$BossBarOverlapMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IWailaConfig$BossBarOverlapMode_ = $IWailaConfig$BossBarOverlapMode$Type;
}}
declare module "packages/snownee/jade/gui/$BaseOptionsScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$OptionsList, $OptionsList$Type} from "packages/snownee/jade/gui/config/$OptionsList"

export class $BaseOptionsScreen extends $Screen {
 "saveButton": $Button
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Screen$Type, arg1: $Component$Type)
constructor(arg0: $Screen$Type, arg1: string)

public "shouldCloseOnEsc"(): boolean
public "onClose"(): void
public "removed"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "tick"(): void
public "getChildAt"(arg0: double, arg1: double): $Optional<($GuiEventListener)>
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "forcePreviewOverlay"(): boolean
public "createOptions"(): $OptionsList
public "addEntryWidget"<T extends ($GuiEventListener) & ($NarratableEntry)>(arg0: T): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseOptionsScreen$Type = ($BaseOptionsScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseOptionsScreen_ = $BaseOptionsScreen$Type;
}}
declare module "packages/snownee/jade/addon/create/$ContraptionItemStorageProvider" {
import {$ClientViewGroup, $ClientViewGroup$Type} from "packages/snownee/jade/api/view/$ClientViewGroup"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$AbstractContraptionEntity, $AbstractContraptionEntity$Type} from "packages/com/simibubi/create/content/contraptions/$AbstractContraptionEntity"
import {$IServerExtensionProvider, $IServerExtensionProvider$Type} from "packages/snownee/jade/api/view/$IServerExtensionProvider"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IClientExtensionProvider, $IClientExtensionProvider$Type} from "packages/snownee/jade/api/view/$IClientExtensionProvider"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$ItemView, $ItemView$Type} from "packages/snownee/jade/api/view/$ItemView"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"

export class $ContraptionItemStorageProvider extends $Enum<($ContraptionItemStorageProvider)> implements $IServerExtensionProvider<($AbstractContraptionEntity), ($ItemStack)>, $IClientExtensionProvider<($ItemStack), ($ItemView)> {
static readonly "INSTANCE": $ContraptionItemStorageProvider


public static "values"(): ($ContraptionItemStorageProvider)[]
public static "valueOf"(arg0: string): $ContraptionItemStorageProvider
public "getUid"(): $ResourceLocation
public "getClientGroups"(arg0: $Accessor$Type<(any)>, arg1: $List$Type<($ViewGroup$Type<($ItemStack$Type)>)>): $List<($ClientViewGroup<($ItemView)>)>
public "getGroups"(arg0: $ServerPlayer$Type, arg1: $ServerLevel$Type, arg2: $AbstractContraptionEntity$Type, arg3: boolean): $List<($ViewGroup<($ItemStack)>)>
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContraptionItemStorageProvider$Type = (("instance")) | ($ContraptionItemStorageProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContraptionItemStorageProvider_ = $ContraptionItemStorageProvider$Type;
}}
declare module "packages/snownee/jade/impl/config/entry/$ConfigEntry" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$OptionsList, $OptionsList$Type} from "packages/snownee/jade/gui/config/$OptionsList"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$OptionValue, $OptionValue$Type} from "packages/snownee/jade/gui/config/value/$OptionValue"

export class $ConfigEntry<T> {
readonly "id": $ResourceLocation

constructor(arg0: $ResourceLocation$Type, arg1: T)

public "getValue"(): T
public "getId"(): $ResourceLocation
public "setValue"(arg0: any): void
public "getDefaultValue"(): T
public "isValidValue"(arg0: any): boolean
public "setSynced"(arg0: boolean): void
public "isSynced"(): boolean
public "addListener"(arg0: $Consumer$Type<($ResourceLocation$Type)>): void
public "notifyChange"(): void
public "createUI"(arg0: $OptionsList$Type, arg1: string): $OptionValue<(any)>
get "value"(): T
get "id"(): $ResourceLocation
set "value"(value: any)
get "defaultValue"(): T
set "synced"(value: boolean)
get "synced"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigEntry$Type<T> = ($ConfigEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigEntry_<T> = $ConfigEntry$Type<(T)>;
}}
declare module "packages/snownee/jade/impl/$EntityAccessorClientHandler" {
import {$Accessor$ClientHandler, $Accessor$ClientHandler$Type} from "packages/snownee/jade/api/$Accessor$ClientHandler"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$IJadeProvider, $IJadeProvider$Type} from "packages/snownee/jade/api/$IJadeProvider"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $EntityAccessorClientHandler implements $Accessor$ClientHandler<($EntityAccessor)> {

constructor()

public "requestData"(arg0: $EntityAccessor$Type): void
public "shouldDisplay"(arg0: $EntityAccessor$Type): boolean
public "getIcon"(arg0: $EntityAccessor$Type): $IElement
public "gatherComponents"(arg0: $EntityAccessor$Type, arg1: $Function$Type<($IJadeProvider$Type), ($ITooltip$Type)>): void
public "shouldRequestData"(arg0: $EntityAccessor$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityAccessorClientHandler$Type = ($EntityAccessorClientHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityAccessorClientHandler_ = $EntityAccessorClientHandler$Type;
}}
declare module "packages/snownee/jade/impl/ui/$HealthElement" {
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$Element, $Element$Type} from "packages/snownee/jade/api/ui/$Element"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $HealthElement extends $Element {

constructor(arg0: float, arg1: float)

public "getMessage"(): string
public "getSize"(): $Vec2
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
get "message"(): string
get "size"(): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HealthElement$Type = ($HealthElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HealthElement_ = $HealthElement$Type;
}}
declare module "packages/snownee/jade/network/$ServerPingPacket" {
import {$PluginConfig, $PluginConfig$Type} from "packages/snownee/jade/impl/config/$PluginConfig"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export class $ServerPingPacket {
readonly "serverConfig": string

constructor(arg0: string)
constructor(arg0: $PluginConfig$Type)

public static "write"(arg0: $ServerPingPacket$Type, arg1: $FriendlyByteBuf$Type): void
public static "read"(arg0: $FriendlyByteBuf$Type): $ServerPingPacket
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerPingPacket$Type = ($ServerPingPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerPingPacket_ = $ServerPingPacket$Type;
}}
declare module "packages/snownee/jade/gui/$HomeConfigScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $HomeConfigScreen extends $Screen {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Screen$Type)

public "onClose"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HomeConfigScreen$Type = ($HomeConfigScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HomeConfigScreen_ = $HomeConfigScreen$Type;
}}
declare module "packages/snownee/jade/addon/core/$ObjectNameProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $ObjectNameProvider extends $Enum<($ObjectNameProvider)> implements $IBlockComponentProvider, $IEntityComponentProvider, $IServerDataProvider<($BlockAccessor)> {
static readonly "INSTANCE": $ObjectNameProvider


public static "values"(): ($ObjectNameProvider)[]
public static "valueOf"(arg0: string): $ObjectNameProvider
public static "getEntityName"(arg0: $Entity$Type): $Component
public "isRequired"(): boolean
public "getUid"(): $ResourceLocation
public "getDefaultPriority"(): integer
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "enabledByDefault"(): boolean
get "required"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectNameProvider$Type = (("instance")) | ($ObjectNameProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectNameProvider_ = $ObjectNameProvider$Type;
}}
declare module "packages/snownee/jade/api/ui/$IElement" {
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$IElement$Align, $IElement$Align$Type} from "packages/snownee/jade/api/ui/$IElement$Align"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $IElement {

 "size"(arg0: $Vec2$Type): $IElement
 "getMessage"(): string
 "message"(arg0: string): $IElement
 "getSize"(): $Vec2
 "tag"(arg0: $ResourceLocation$Type): $IElement
 "getTag"(): $ResourceLocation
 "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
 "align"(arg0: $IElement$Align$Type): $IElement
 "getAlignment"(): $IElement$Align
 "translate"(arg0: $Vec2$Type): $IElement
 "getTranslation"(): $Vec2
 "getCachedMessage"(): string
 "clearCachedMessage"(): $IElement
 "getCachedSize"(): $Vec2
}

export namespace $IElement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IElement$Type = ($IElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IElement_ = $IElement$Type;
}}
declare module "packages/snownee/jade/api/$AccessorImpl" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $AccessorImpl<T extends $HitResult> implements $Accessor<(T)> {

constructor(arg0: $Level$Type, arg1: $Player$Type, arg2: $CompoundTag$Type, arg3: $Supplier$Type<(T)>, arg4: boolean, arg5: boolean)

public "getLevel"(): $Level
public "getPickedResult"(): $ItemStack
public "showDetails"(): boolean
public "isServerConnected"(): boolean
public "getServerData"(): $CompoundTag
public "getPlayer"(): $Player
public "requireVerification"(): void
public "getHitResult"(): T
public "getTarget"(): any
public "getAccessorType"(): $Class<(any)>
public "toNetwork"(arg0: $FriendlyByteBuf$Type): void
public "verifyData"(arg0: $CompoundTag$Type): boolean
get "level"(): $Level
get "pickedResult"(): $ItemStack
get "serverConnected"(): boolean
get "serverData"(): $CompoundTag
get "player"(): $Player
get "hitResult"(): T
get "target"(): any
get "accessorType"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessorImpl$Type<T> = ($AccessorImpl<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessorImpl_<T> = $AccessorImpl$Type<(T)>;
}}
declare module "packages/snownee/jade/api/view/$FluidView" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$JadeFluidObject, $JadeFluidObject$Type} from "packages/snownee/jade/api/fluid/$JadeFluidObject"

export class $FluidView {
static readonly "EMPTY_FLUID": $Component
 "overlay": $IElement
 "current": string
 "max": string
 "ratio": float
 "fluidName": $Component
 "overrideText": $Component

constructor(arg0: $IElement$Type)

public static "readDefault"(arg0: $CompoundTag$Type): $FluidView
public static "writeDefault"(arg0: $JadeFluidObject$Type, arg1: long): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidView$Type = ($FluidView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidView_ = $FluidView$Type;
}}
declare module "packages/snownee/jade/addon/universal/$ItemIterator" {
import {$AtomicLong, $AtomicLong$Type} from "packages/java/util/concurrent/atomic/$AtomicLong"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $ItemIterator<T> {
static readonly "version": $AtomicLong


public "find"(arg0: any): T
public "reset"(): void
public "getVersion"(arg0: T): long
public "getCollectingProgress"(): float
public "populate"(arg0: T): $Stream<($ItemStack)>
public "afterPopulate"(arg0: integer): void
public "isFinished"(): boolean
get "collectingProgress"(): float
get "finished"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemIterator$Type<T> = ($ItemIterator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemIterator_<T> = $ItemIterator$Type<(T)>;
}}
declare module "packages/snownee/jade/overlay/$WailaTickHandler" {
import {$TooltipRenderer, $TooltipRenderer$Type} from "packages/snownee/jade/overlay/$TooltipRenderer"
import {$ProgressTracker, $ProgressTracker$Type} from "packages/snownee/jade/overlay/$ProgressTracker"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $WailaTickHandler {
 "tooltipRenderer": $TooltipRenderer
 "progressTracker": $ProgressTracker

constructor()

public static "instance"(): $WailaTickHandler
public static "narrate"(arg0: $ITooltip$Type, arg1: boolean): void
public "tickClient"(): void
public static "clearLastNarration"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WailaTickHandler$Type = ($WailaTickHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WailaTickHandler_ = $WailaTickHandler$Type;
}}
declare module "packages/snownee/jade/overlay/$DisplayHelper" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Format, $Format$Type} from "packages/java/text/$Format"
import {$DecimalFormat, $DecimalFormat$Type} from "packages/java/text/$DecimalFormat"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IDisplayHelper, $IDisplayHelper$Type} from "packages/snownee/jade/api/ui/$IDisplayHelper"
import {$JadeFluidObject, $JadeFluidObject$Type} from "packages/snownee/jade/api/fluid/$JadeFluidObject"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IconUI, $IconUI$Type} from "packages/snownee/jade/overlay/$IconUI"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export class $DisplayHelper implements $IDisplayHelper {
static readonly "INSTANCE": $DisplayHelper
static "dfCommas": $DecimalFormat
static readonly "dfCommasArray": ($DecimalFormat)[]

constructor()

public static "drawTexturedModalRect"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer): void
public "drawFluid"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: $JadeFluidObject$Type, arg4: float, arg5: float, arg6: long): void
public static "fill"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: integer): void
public "drawBorder"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: integer, arg7: boolean): void
public static "setBetterTextShadow"(arg0: boolean): void
public static "enableBetterTextShadow"(): boolean
public "drawText"(arg0: $GuiGraphics$Type, arg1: $FormattedText$Type, arg2: float, arg3: float, arg4: integer): void
public "drawText"(arg0: $GuiGraphics$Type, arg1: $FormattedCharSequence$Type, arg2: float, arg3: float, arg4: integer): void
public "drawText"(arg0: $GuiGraphics$Type, arg1: string, arg2: float, arg3: float, arg4: integer): void
public "stripColor"(arg0: $Component$Type): $MutableComponent
public "drawItem"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: $ItemStack$Type, arg4: float, arg5: string): void
public static "renderIcon"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: integer, arg4: integer, arg5: $IconUI$Type): void
public "humanReadableNumber"(arg0: double, arg1: string, arg2: boolean): string
public "humanReadableNumber"(arg0: double, arg1: string, arg2: boolean, arg3: $Format$Type): string
public "drawGradientRect"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: integer, arg6: integer, arg7: boolean): void
public "drawGradientRect"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: integer, arg6: integer): void
public "drawGradientProgress"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: integer): void
public static "get"(): $IDisplayHelper
set "betterTextShadow"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DisplayHelper$Type = ($DisplayHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DisplayHelper_ = $DisplayHelper$Type;
}}
declare module "packages/snownee/jade/overlay/$OverlayRenderer" {
import {$TooltipRenderer, $TooltipRenderer$Type} from "packages/snownee/jade/overlay/$TooltipRenderer"
import {$Theme, $Theme$Type} from "packages/snownee/jade/api/theme/$Theme"
import {$MutableObject, $MutableObject$Type} from "packages/org/apache/commons/lang3/mutable/$MutableObject"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $OverlayRenderer {
static readonly "theme": $MutableObject<($Theme)>
static "ticks": float
static "shown": boolean
static "alpha": float

constructor()

public static "drawTooltipBox"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: float, arg6: boolean, arg7: $TooltipRenderer$Type): void
public static "renderOverlay478757"(arg0: $GuiGraphics$Type): void
public static "renderOverlay"(arg0: $TooltipRenderer$Type, arg1: $GuiGraphics$Type): void
public static "blitNineSliced"(arg0: $GuiGraphics$Type, arg1: $ResourceLocation$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: integer, arg11: integer, arg12: integer, arg13: integer): void
public static "shouldShow"(): boolean
public static "shouldShowImmediately"(arg0: $TooltipRenderer$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OverlayRenderer$Type = ($OverlayRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OverlayRenderer_ = $OverlayRenderer$Type;
}}
declare module "packages/snownee/jade/addon/enderio/$EnderIOPlugin" {
import {$IWailaClientRegistration, $IWailaClientRegistration$Type} from "packages/snownee/jade/api/$IWailaClientRegistration"
import {$IWailaPlugin, $IWailaPlugin$Type} from "packages/snownee/jade/api/$IWailaPlugin"
import {$IWailaCommonRegistration, $IWailaCommonRegistration$Type} from "packages/snownee/jade/api/$IWailaCommonRegistration"

export class $EnderIOPlugin implements $IWailaPlugin {
static readonly "ID": string

constructor()

public "registerClient"(arg0: $IWailaClientRegistration$Type): void
public "register"(arg0: $IWailaCommonRegistration$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnderIOPlugin$Type = ($EnderIOPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnderIOPlugin_ = $EnderIOPlugin$Type;
}}
declare module "packages/snownee/jade/gui/config/$OptionsNav" {
import {$ObjectSelectionList, $ObjectSelectionList$Type} from "packages/net/minecraft/client/gui/components/$ObjectSelectionList"
import {$AbstractSelectionList$Entry, $AbstractSelectionList$Entry$Type} from "packages/net/minecraft/client/gui/components/$AbstractSelectionList$Entry"
import {$OptionsList, $OptionsList$Type} from "packages/snownee/jade/gui/config/$OptionsList"
import {$OptionsNav$Entry, $OptionsNav$Entry$Type} from "packages/snownee/jade/gui/config/$OptionsNav$Entry"
import {$OptionsList$Title, $OptionsList$Title$Type} from "packages/snownee/jade/gui/config/$OptionsList$Title"

export class $OptionsNav extends $ObjectSelectionList<($OptionsNav$Entry)> {
 "scrolling": boolean
 "hovered": E

constructor(arg0: $OptionsList$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer)

public "addEntry"(arg0: $OptionsList$Title$Type): void
public "refresh"(): void
public "getRowWidth"(): integer
get "rowWidth"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionsNav$Type = ($OptionsNav);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionsNav_ = $OptionsNav$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$CommandBlockProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $CommandBlockProvider extends $Enum<($CommandBlockProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)> {
static readonly "INSTANCE": $CommandBlockProvider


public static "values"(): ($CommandBlockProvider)[]
public static "valueOf"(arg0: string): $CommandBlockProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommandBlockProvider$Type = (("instance")) | ($CommandBlockProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommandBlockProvider_ = $CommandBlockProvider$Type;
}}
declare module "packages/snownee/jade/addon/deep_resonance/$CrystalProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $CrystalProvider extends $Enum<($CrystalProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)> {
static readonly "INSTANCE": $CrystalProvider


public static "values"(): ($CrystalProvider)[]
public static "valueOf"(arg0: string): $CrystalProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrystalProvider$Type = (("instance")) | ($CrystalProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrystalProvider_ = $CrystalProvider$Type;
}}
declare module "packages/snownee/jade/api/callback/$JadeRenderBackgroundCallback" {
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$JadeBeforeRenderCallback$ColorSetting, $JadeBeforeRenderCallback$ColorSetting$Type} from "packages/snownee/jade/api/callback/$JadeBeforeRenderCallback$ColorSetting"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ITooltipRenderer, $ITooltipRenderer$Type} from "packages/snownee/jade/api/ui/$ITooltipRenderer"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"

export interface $JadeRenderBackgroundCallback {

 "onRender"(arg0: $ITooltipRenderer$Type, arg1: $Rect2i$Type, arg2: $GuiGraphics$Type, arg3: $Accessor$Type<(any)>, arg4: $JadeBeforeRenderCallback$ColorSetting$Type): boolean

(arg0: $ITooltipRenderer$Type, arg1: $Rect2i$Type, arg2: $GuiGraphics$Type, arg3: $Accessor$Type<(any)>, arg4: $JadeBeforeRenderCallback$ColorSetting$Type): boolean
}

export namespace $JadeRenderBackgroundCallback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeRenderBackgroundCallback$Type = ($JadeRenderBackgroundCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeRenderBackgroundCallback_ = $JadeRenderBackgroundCallback$Type;
}}
declare module "packages/snownee/jade/addon/mcjty_lib/$BaseBlockProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $BaseBlockProvider extends $Enum<($BaseBlockProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)> {
static readonly "INSTANCE": $BaseBlockProvider


public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getUid"(): $ResourceLocation
public "getDefaultPriority"(): integer
get "required"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseBlockProvider$Type = (("instance")) | ($BaseBlockProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseBlockProvider_ = $BaseBlockProvider$Type;
}}
declare module "packages/snownee/jade/api/$IEntityComponentProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$IToggleableProvider, $IToggleableProvider$Type} from "packages/snownee/jade/api/$IToggleableProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export interface $IEntityComponentProvider extends $IToggleableProvider {

 "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
 "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
 "isRequired"(): boolean
 "enabledByDefault"(): boolean
 "getUid"(): $ResourceLocation
 "getDefaultPriority"(): integer
}

export namespace $IEntityComponentProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IEntityComponentProvider$Type = ($IEntityComponentProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IEntityComponentProvider_ = $IEntityComponentProvider$Type;
}}
declare module "packages/snownee/jade/util/$CommonProxy" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IJadeProvider, $IJadeProvider$Type} from "packages/snownee/jade/api/$IJadeProvider"
import {$PaintingVariant, $PaintingVariant$Type} from "packages/net/minecraft/world/entity/decoration/$PaintingVariant"
import {$ItemCollector, $ItemCollector$Type} from "packages/snownee/jade/addon/universal/$ItemCollector"
import {$FluidStack, $FluidStack$Type} from "packages/net/minecraftforge/fluids/$FluidStack"
import {$Cache, $Cache$Type} from "packages/com/google/common/cache/$Cache"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$VillagerProfession, $VillagerProfession$Type} from "packages/net/minecraft/world/entity/npc/$VillagerProfession"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$File, $File$Type} from "packages/java/io/$File"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$JadeFluidObject, $JadeFluidObject$Type} from "packages/snownee/jade/api/fluid/$JadeFluidObject"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$EntityHitResult, $EntityHitResult$Type} from "packages/net/minecraft/world/phys/$EntityHitResult"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $CommonProxy {
static readonly "NETWORK": $SimpleChannel
static readonly "BLOCKED_UIDS": $Set<($ResourceLocation)>

constructor()

public static "getId"(arg0: $PaintingVariant$Type): $ResourceLocation
public static "getId"(arg0: $Block$Type): $ResourceLocation
public static "getId"(arg0: $BlockEntityType$Type<(any)>): $ResourceLocation
public static "getId"(arg0: $EntityType$Type<(any)>): $ResourceLocation
public static "isDevEnv"(): boolean
public static "wrapFluidStorage"(arg0: any, arg1: $Player$Type): $List<($ViewGroup<($CompoundTag)>)>
public static "isShears"(arg0: $ItemStack$Type): boolean
public static "wrapEnergyStorage"(arg0: any, arg1: $Player$Type): $List<($ViewGroup<($CompoundTag)>)>
public static "getModIdFromItem"(arg0: $ItemStack$Type): string
public static "storageGroup"(arg0: any, arg1: $Accessor$Type<(any)>): $List<($ViewGroup<($ItemStack)>)>
public static "containerGroup"(arg0: $Container$Type, arg1: $Accessor$Type<(any)>): $List<($ViewGroup<($ItemStack)>)>
public static "getProfressionName"(arg0: $VillagerProfession$Type): $MutableComponent
public static "toFluidStack"(arg0: $JadeFluidObject$Type): $FluidStack
public static "getFluidName"(arg0: $JadeFluidObject$Type): $Component
public static "isBlockedUid"(arg0: $IJadeProvider$Type): boolean
public static "getPartEntity"(arg0: $Entity$Type, arg1: integer): $Entity
public static "getPartEntityIndex"(arg0: $Entity$Type): integer
public static "isPhysicallyClient"(): boolean
public static "isCorrectToolForDrops"(arg0: $BlockState$Type, arg1: $Player$Type): boolean
public static "getEnchantPowerBonus"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): float
public static "isBoss"(arg0: $Entity$Type): boolean
public static "getConfigDirectory"(): $File
public static "isModLoaded"(arg0: string): boolean
public static "getBlockPickedResult"(arg0: $BlockState$Type, arg1: $Player$Type, arg2: $BlockHitResult$Type): $ItemStack
public static "handleNetworkingError"(arg0: $Throwable$Type): void
public static "createItemCollector"(arg0: any, arg1: $Cache$Type<(any), ($ItemCollector$Type<(any)>)>): $ItemCollector<(any)>
public static "getEntityPickedResult"(arg0: $Entity$Type, arg1: $Player$Type, arg2: $EntityHitResult$Type): $ItemStack
public static "wrapPartEntityParent"(arg0: $Entity$Type): $Entity
public static "getPlatformIdentifier"(): string
public static "showOrHideFromServer"(arg0: $Collection$Type<($ServerPlayer$Type)>, arg1: boolean): integer
public static "getLastKnownUsername"(arg0: $UUID$Type): string
public static "isMultipartEntity"(arg0: $Entity$Type): boolean
public static "isShearable"(arg0: $BlockState$Type): boolean
get "devEnv"(): boolean
get "physicallyClient"(): boolean
get "configDirectory"(): $File
get "platformIdentifier"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonProxy$Type = ($CommonProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonProxy_ = $CommonProxy$Type;
}}
declare module "packages/snownee/jade/impl/$WailaCommonRegistration" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IJadeProvider, $IJadeProvider$Type} from "packages/snownee/jade/api/$IJadeProvider"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$IServerExtensionProvider, $IServerExtensionProvider$Type} from "packages/snownee/jade/api/view/$IServerExtensionProvider"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$HierarchyLookup, $HierarchyLookup$Type} from "packages/snownee/jade/impl/$HierarchyLookup"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PriorityStore, $PriorityStore$Type} from "packages/snownee/jade/impl/$PriorityStore"
import {$IWailaCommonRegistration, $IWailaCommonRegistration$Type} from "packages/snownee/jade/api/$IWailaCommonRegistration"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $WailaCommonRegistration implements $IWailaCommonRegistration {
static readonly "INSTANCE": $WailaCommonRegistration
readonly "blockDataProviders": $HierarchyLookup<($IServerDataProvider<($BlockAccessor)>)>
readonly "entityDataProviders": $HierarchyLookup<($IServerDataProvider<($EntityAccessor)>)>
readonly "priorities": $PriorityStore<($ResourceLocation), ($IJadeProvider)>
readonly "itemStorageProviders": $HierarchyLookup<($IServerExtensionProvider<(any), ($ItemStack)>)>
readonly "fluidStorageProviders": $HierarchyLookup<($IServerExtensionProvider<(any), ($CompoundTag)>)>
readonly "energyStorageProviders": $HierarchyLookup<($IServerExtensionProvider<(any), ($CompoundTag)>)>
readonly "progressProviders": $HierarchyLookup<($IServerExtensionProvider<(any), ($CompoundTag)>)>


public "loadComplete"(): void
public "registerItemStorage"<T>(arg0: $IServerExtensionProvider$Type<(T), ($ItemStack$Type)>, arg1: $Class$Type<(any)>): void
public "registerFluidStorage"<T>(arg0: $IServerExtensionProvider$Type<(T), ($CompoundTag$Type)>, arg1: $Class$Type<(any)>): void
public "registerEntityDataProvider"(arg0: $IServerDataProvider$Type<($EntityAccessor$Type)>, arg1: $Class$Type<(any)>): void
public "registerBlockDataProvider"(arg0: $IServerDataProvider$Type<($BlockAccessor$Type)>, arg1: $Class$Type<(any)>): void
public "registerEnergyStorage"<T>(arg0: $IServerExtensionProvider$Type<(T), ($CompoundTag$Type)>, arg1: $Class$Type<(any)>): void
public "registerProgress"<T>(arg0: $IServerExtensionProvider$Type<(T), ($CompoundTag$Type)>, arg1: $Class$Type<(any)>): void
public "getBlockNBTProviders"(arg0: $BlockEntity$Type): $List<($IServerDataProvider<($BlockAccessor)>)>
public "getEntityNBTProviders"(arg0: $Entity$Type): $List<($IServerDataProvider<($EntityAccessor)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WailaCommonRegistration$Type = ($WailaCommonRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WailaCommonRegistration_ = $WailaCommonRegistration$Type;
}}
declare module "packages/snownee/jade/api/callback/$JadeRayTraceCallback" {
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"

export interface $JadeRayTraceCallback {

 "onRayTrace"(arg0: $HitResult$Type, arg1: $Accessor$Type<(any)>, arg2: $Accessor$Type<(any)>): $Accessor<(any)>

(arg0: $HitResult$Type, arg1: $Accessor$Type<(any)>, arg2: $Accessor$Type<(any)>): $Accessor<(any)>
}

export namespace $JadeRayTraceCallback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeRayTraceCallback$Type = ($JadeRayTraceCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeRayTraceCallback_ = $JadeRayTraceCallback$Type;
}}
declare module "packages/snownee/jade/addon/mcjty_lib/$McjtyLibPlugin" {
import {$IWailaClientRegistration, $IWailaClientRegistration$Type} from "packages/snownee/jade/api/$IWailaClientRegistration"
import {$IWailaPlugin, $IWailaPlugin$Type} from "packages/snownee/jade/api/$IWailaPlugin"
import {$IWailaCommonRegistration, $IWailaCommonRegistration$Type} from "packages/snownee/jade/api/$IWailaCommonRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $McjtyLibPlugin implements $IWailaPlugin {
static readonly "ID": string
static readonly "GENERAL": $ResourceLocation

constructor()

public "register"(arg0: $IWailaCommonRegistration$Type): void
public "registerClient"(arg0: $IWailaClientRegistration$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $McjtyLibPlugin$Type = ($McjtyLibPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $McjtyLibPlugin_ = $McjtyLibPlugin$Type;
}}
declare module "packages/snownee/jade/api/platform/$CustomEnchantPower" {
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $CustomEnchantPower {

 "getEnchantPowerBonus"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): float

(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): float
}

export namespace $CustomEnchantPower {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomEnchantPower$Type = ($CustomEnchantPower);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomEnchantPower_ = $CustomEnchantPower$Type;
}}
declare module "packages/snownee/jade/util/$JadeCodecs" {
import {$TargetBlocklist, $TargetBlocklist$Type} from "packages/snownee/jade/api/config/$TargetBlocklist"
import {$OptionalInt, $OptionalInt$Type} from "packages/java/util/$OptionalInt"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $JadeCodecs {
static readonly "OPTIONAL_INT": $Codec<($OptionalInt)>
static readonly "TARGET_BLOCKLIST_CODEC": $Codec<($TargetBlocklist)>

constructor()

public static "createFromEmptyMap"<T>(arg0: $Codec$Type<(T)>): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeCodecs$Type = ($JadeCodecs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeCodecs_ = $JadeCodecs$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$MobBreedingProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $MobBreedingProvider extends $Enum<($MobBreedingProvider)> implements $IEntityComponentProvider, $IServerDataProvider<($EntityAccessor)> {
static readonly "INSTANCE": $MobBreedingProvider


public static "values"(): ($MobBreedingProvider)[]
public static "valueOf"(arg0: string): $MobBreedingProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $EntityAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobBreedingProvider$Type = (("instance")) | ($MobBreedingProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobBreedingProvider_ = $MobBreedingProvider$Type;
}}
declare module "packages/snownee/jade/addon/create/$ContraptionFluidStorageProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ClientViewGroup, $ClientViewGroup$Type} from "packages/snownee/jade/api/view/$ClientViewGroup"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$AbstractContraptionEntity, $AbstractContraptionEntity$Type} from "packages/com/simibubi/create/content/contraptions/$AbstractContraptionEntity"
import {$IServerExtensionProvider, $IServerExtensionProvider$Type} from "packages/snownee/jade/api/view/$IServerExtensionProvider"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IClientExtensionProvider, $IClientExtensionProvider$Type} from "packages/snownee/jade/api/view/$IClientExtensionProvider"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$FluidView, $FluidView$Type} from "packages/snownee/jade/api/view/$FluidView"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"

export class $ContraptionFluidStorageProvider extends $Enum<($ContraptionFluidStorageProvider)> implements $IServerExtensionProvider<($AbstractContraptionEntity), ($CompoundTag)>, $IClientExtensionProvider<($CompoundTag), ($FluidView)> {
static readonly "INSTANCE": $ContraptionFluidStorageProvider


public static "values"(): ($ContraptionFluidStorageProvider)[]
public static "valueOf"(arg0: string): $ContraptionFluidStorageProvider
public "getUid"(): $ResourceLocation
public "getClientGroups"(arg0: $Accessor$Type<(any)>, arg1: $List$Type<($ViewGroup$Type<($CompoundTag$Type)>)>): $List<($ClientViewGroup<($FluidView)>)>
public "getGroups"(arg0: $ServerPlayer$Type, arg1: $ServerLevel$Type, arg2: $AbstractContraptionEntity$Type, arg3: boolean): $List<($ViewGroup<($CompoundTag)>)>
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContraptionFluidStorageProvider$Type = (("instance")) | ($ContraptionFluidStorageProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContraptionFluidStorageProvider_ = $ContraptionFluidStorageProvider$Type;
}}
declare module "packages/snownee/jade/api/config/$IWailaConfig$FluidMode" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$ClipContext$Fluid, $ClipContext$Fluid$Type} from "packages/net/minecraft/world/level/$ClipContext$Fluid"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$SimpleStringRepresentable, $SimpleStringRepresentable$Type} from "packages/snownee/jade/api/$SimpleStringRepresentable"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $IWailaConfig$FluidMode extends $Enum<($IWailaConfig$FluidMode)> implements $SimpleStringRepresentable {
static readonly "NONE": $IWailaConfig$FluidMode
static readonly "ANY": $IWailaConfig$FluidMode
static readonly "SOURCE_ONLY": $IWailaConfig$FluidMode
readonly "ctx": $ClipContext$Fluid


public static "values"(): ($IWailaConfig$FluidMode)[]
public static "valueOf"(arg0: string): $IWailaConfig$FluidMode
public "getSerializedName"(): string
public static "fromEnumWithMapping"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
public static "keys"(arg0: ($StringRepresentable$Type)[]): $Keyable
public static "fromEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
get "serializedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IWailaConfig$FluidMode$Type = (("source_only") | ("none") | ("any")) | ($IWailaConfig$FluidMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IWailaConfig$FluidMode_ = $IWailaConfig$FluidMode$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$PaintingProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $PaintingProvider extends $Enum<($PaintingProvider)> implements $IEntityComponentProvider {
static readonly "INSTANCE": $PaintingProvider


public static "values"(): ($PaintingProvider)[]
public static "valueOf"(arg0: string): $PaintingProvider
public "getUid"(): $ResourceLocation
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PaintingProvider$Type = (("instance")) | ($PaintingProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PaintingProvider_ = $PaintingProvider$Type;
}}
declare module "packages/snownee/jade/api/ui/$ITooltipRenderer" {
import {$Tooltip, $Tooltip$Type} from "packages/snownee/jade/impl/$Tooltip"
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"

export interface $ITooltipRenderer {

 "getSize"(): $Vec2
 "setSize"(arg0: $Vec2$Type): void
 "getPosition"(): $Rect2i
 "getRealRect"(): $Rect2i
 "getRealScale"(): float
 "recalculateSize"(): void
/**
 * 
 * @deprecated
 */
 "setPadding"(arg0: integer, arg1: float): void
 "setPadding"(arg0: integer, arg1: integer): void
 "getPadding"(arg0: integer): integer
 "getIcon"(): $IElement
 "getTooltip"(): $Tooltip
 "setIcon"(arg0: $IElement$Type): void
 "hasIcon"(): boolean
 "recalculateRealRect"(): void
}

export namespace $ITooltipRenderer {
const TOP: integer
const RIGHT: integer
const BOTTOM: integer
const LEFT: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITooltipRenderer$Type = ($ITooltipRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITooltipRenderer_ = $ITooltipRenderer$Type;
}}
declare module "packages/snownee/jade/api/theme/$IThemeHelper" {
import {$Theme, $Theme$Type} from "packages/snownee/jade/api/theme/$Theme"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export interface $IThemeHelper {

 "info"(arg0: any): $MutableComponent
 "failure"(arg0: any): $MutableComponent
 "success"(arg0: any): $MutableComponent
 "seconds"(arg0: integer): $MutableComponent
 "warning"(arg0: any): $MutableComponent
 "getTheme"(arg0: $ResourceLocation$Type): $Theme
 "danger"(arg0: any): $MutableComponent
 "getNormalColor"(): integer
 "isLightColorScheme"(): boolean
 "getThemes"(): $Collection<($Theme)>
 "theme"(): $Theme
 "title"(arg0: any): $MutableComponent
}

export namespace $IThemeHelper {
function get(): $IThemeHelper
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IThemeHelper$Type = ($IThemeHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IThemeHelper_ = $IThemeHelper$Type;
}}
declare module "packages/snownee/jade/api/config/$IPluginConfig" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IToggleableProvider, $IToggleableProvider$Type} from "packages/snownee/jade/api/$IToggleableProvider"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $IPluginConfig {

 "get"(arg0: $IToggleableProvider$Type): boolean
 "get"(arg0: $ResourceLocation$Type): boolean
 "getInt"(arg0: $ResourceLocation$Type): integer
 "getFloat"(arg0: $ResourceLocation$Type): float
 "getString"(arg0: $ResourceLocation$Type): string
 "getKeys"(arg0: string): $Set<($ResourceLocation)>
 "getKeys"(): $Set<($ResourceLocation)>
 "getEnum"<T extends $Enum<(T)>>(arg0: $ResourceLocation$Type): T
}

export namespace $IPluginConfig {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPluginConfig$Type = ($IPluginConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPluginConfig_ = $IPluginConfig$Type;
}}
declare module "packages/snownee/jade/api/$IServerDataProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$IJadeProvider, $IJadeProvider$Type} from "packages/snownee/jade/api/$IJadeProvider"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $IServerDataProvider<T extends $Accessor<(any)>> extends $IJadeProvider {

 "appendServerData"(arg0: $CompoundTag$Type, arg1: T): void
 "getUid"(): $ResourceLocation
 "getDefaultPriority"(): integer
}

export namespace $IServerDataProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IServerDataProvider$Type<T> = ($IServerDataProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IServerDataProvider_<T> = $IServerDataProvider$Type<(T)>;
}}
declare module "packages/snownee/jade/api/view/$ItemView" {
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"

export class $ItemView {
 "item": $ItemStack
 "text": string

constructor(arg0: $ItemStack$Type)
constructor(arg0: $ItemStack$Type, arg1: string)

/**
 * 
 * @deprecated
 */
public static "compacted"(arg0: $Stream$Type<($ItemStack$Type)>, arg1: integer): $ViewGroup<($ItemStack)>
public static "groupOf"(arg0: $Container$Type, arg1: $Accessor$Type<(any)>): $List<($ViewGroup<($ItemStack)>)>
public static "groupOf"(arg0: any, arg1: $Accessor$Type<(any)>): $List<($ViewGroup<($ItemStack)>)>
/**
 * 
 * @deprecated
 */
public static "fromContainer"(arg0: $Container$Type, arg1: integer, arg2: integer): $ViewGroup<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemView$Type = ($ItemView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemView_ = $ItemView$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$ArmorStandProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $ArmorStandProvider extends $Enum<($ArmorStandProvider)> implements $IEntityComponentProvider {
static readonly "INSTANCE": $ArmorStandProvider


public static "values"(): ($ArmorStandProvider)[]
public static "valueOf"(arg0: string): $ArmorStandProvider
public "getUid"(): $ResourceLocation
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArmorStandProvider$Type = (("instance")) | ($ArmorStandProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArmorStandProvider_ = $ArmorStandProvider$Type;
}}
declare module "packages/snownee/jade/impl/config/entry/$IntConfigEntry" {
import {$ConfigEntry, $ConfigEntry$Type} from "packages/snownee/jade/impl/config/entry/$ConfigEntry"
import {$OptionsList, $OptionsList$Type} from "packages/snownee/jade/gui/config/$OptionsList"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$OptionValue, $OptionValue$Type} from "packages/snownee/jade/gui/config/value/$OptionValue"

export class $IntConfigEntry extends $ConfigEntry<(integer)> {
readonly "id": $ResourceLocation

constructor(arg0: $ResourceLocation$Type, arg1: integer, arg2: integer, arg3: integer, arg4: boolean)

public "setValue"(arg0: any): void
public "isValidValue"(arg0: any): boolean
public "createUI"(arg0: $OptionsList$Type, arg1: string): $OptionValue<(any)>
set "value"(value: any)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntConfigEntry$Type = ($IntConfigEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntConfigEntry_ = $IntConfigEntry$Type;
}}
declare module "packages/snownee/jade/$Internals" {
import {$IDisplayHelper, $IDisplayHelper$Type} from "packages/snownee/jade/api/ui/$IDisplayHelper"
import {$IWailaConfig, $IWailaConfig$Type} from "packages/snownee/jade/api/config/$IWailaConfig"
import {$IThemeHelper, $IThemeHelper$Type} from "packages/snownee/jade/api/theme/$IThemeHelper"
import {$IElementHelper, $IElementHelper$Type} from "packages/snownee/jade/api/ui/$IElementHelper"

export class $Internals {

constructor()

public static "getElementHelper"(): $IElementHelper
public static "getWailaConfig"(): $IWailaConfig
public static "getThemeHelper"(): $IThemeHelper
public static "getDisplayHelper"(): $IDisplayHelper
get "elementHelper"(): $IElementHelper
get "wailaConfig"(): $IWailaConfig
get "themeHelper"(): $IThemeHelper
get "displayHelper"(): $IDisplayHelper
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Internals$Type = ($Internals);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Internals_ = $Internals$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$BeehiveProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $BeehiveProvider extends $Enum<($BeehiveProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)> {
static readonly "INSTANCE": $BeehiveProvider


public static "values"(): ($BeehiveProvider)[]
public static "valueOf"(arg0: string): $BeehiveProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BeehiveProvider$Type = (("instance")) | ($BeehiveProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BeehiveProvider_ = $BeehiveProvider$Type;
}}
declare module "packages/snownee/jade/api/view/$ViewGroup" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ViewGroup<T> {
readonly "views": $List<(T)>
 "id": string

constructor(arg0: $List$Type<(T)>)

public static "read"<T>(arg0: $CompoundTag$Type, arg1: $Function$Type<($CompoundTag$Type), (T)>): $ViewGroup<(T)>
public "save"(arg0: $CompoundTag$Type, arg1: $Function$Type<(T), ($CompoundTag$Type)>): void
public "getExtraData"(): $CompoundTag
public static "saveList"<T>(arg0: $CompoundTag$Type, arg1: string, arg2: $List$Type<($ViewGroup$Type<(T)>)>, arg3: $Function$Type<(T), ($CompoundTag$Type)>): boolean
public "setProgress"(arg0: float): void
public static "readList"<T>(arg0: $CompoundTag$Type, arg1: string, arg2: $Function$Type<($CompoundTag$Type), (T)>): $List<($ViewGroup<(T)>)>
get "extraData"(): $CompoundTag
set "progress"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ViewGroup$Type<T> = ($ViewGroup<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ViewGroup_<T> = $ViewGroup$Type<(T)>;
}}
declare module "packages/snownee/jade/api/$TooltipPosition" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $TooltipPosition {
static readonly "HEAD": integer
static readonly "BODY": integer
static readonly "TAIL": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TooltipPosition$Type = ($TooltipPosition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TooltipPosition_ = $TooltipPosition$Type;
}}
declare module "packages/snownee/jade/impl/$HierarchyLookup" {
import {$IJadeProvider, $IJadeProvider$Type} from "packages/snownee/jade/api/$IJadeProvider"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PriorityStore, $PriorityStore$Type} from "packages/snownee/jade/impl/$PriorityStore"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $HierarchyLookup<T extends $IJadeProvider> {

constructor(arg0: $Class$Type<(any)>)
constructor(arg0: $Class$Type<(any)>, arg1: boolean)

public "get"(arg0: any): $List<(T)>
public "get"(arg0: $Class$Type<(any)>): $List<(T)>
public "register"(arg0: $Class$Type<(any)>, arg1: T): void
public "invalidate"(): void
public "loadComplete"(arg0: $PriorityStore$Type<($ResourceLocation$Type), ($IJadeProvider$Type)>): void
public "getObjects"(): $Multimap<($Class<(any)>), (T)>
get "objects"(): $Multimap<($Class<(any)>), (T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HierarchyLookup$Type<T> = ($HierarchyLookup<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HierarchyLookup_<T> = $HierarchyLookup$Type<(T)>;
}}
declare module "packages/snownee/jade/api/$ITooltip" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IElement$Align, $IElement$Align$Type} from "packages/snownee/jade/api/ui/$IElement$Align"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IElementHelper, $IElementHelper$Type} from "packages/snownee/jade/api/ui/$IElementHelper"

export interface $ITooltip {

 "add"(arg0: integer, arg1: $Component$Type): void
 "add"(arg0: integer, arg1: $Component$Type, arg2: $ResourceLocation$Type): void
 "add"(arg0: $IElement$Type): void
 "add"(arg0: integer, arg1: $List$Type<($IElement$Type)>): void
 "add"(arg0: $List$Type<($IElement$Type)>): void
 "add"(arg0: integer, arg1: $IElement$Type): void
 "add"(arg0: $Component$Type): void
 "add"(arg0: $Component$Type, arg1: $ResourceLocation$Type): void
 "remove"(arg0: $ResourceLocation$Type): void
 "get"(arg0: integer, arg1: $IElement$Align$Type): $List<($IElement)>
 "get"(arg0: $ResourceLocation$Type): $List<($IElement)>
 "append"(arg0: $IElement$Type): void
 "append"(arg0: $Component$Type, arg1: $ResourceLocation$Type): void
 "append"(arg0: integer, arg1: $List$Type<($IElement$Type)>): void
 "append"(arg0: $Component$Type): void
 "append"(arg0: integer, arg1: $IElement$Type): void
 "clear"(): void
 "isEmpty"(): boolean
 "size"(): integer
 "addAll"(arg0: $List$Type<($Component$Type)>): void
 "getMessage"(): string
 "getElementHelper"(): $IElementHelper
}

export namespace $ITooltip {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITooltip$Type = ($ITooltip);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITooltip_ = $ITooltip$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$ItemDisplayProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $ItemDisplayProvider extends $Enum<($ItemDisplayProvider)> implements $IEntityComponentProvider {
static readonly "INSTANCE": $ItemDisplayProvider


public static "values"(): ($ItemDisplayProvider)[]
public static "valueOf"(arg0: string): $ItemDisplayProvider
public "isRequired"(): boolean
public "getUid"(): $ResourceLocation
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "required"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemDisplayProvider$Type = (("instance")) | ($ItemDisplayProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemDisplayProvider_ = $ItemDisplayProvider$Type;
}}
declare module "packages/snownee/jade/addon/universal/$EntityProgressProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $EntityProgressProvider extends $Enum<($EntityProgressProvider)> implements $IEntityComponentProvider, $IServerDataProvider<($EntityAccessor)> {
static readonly "INSTANCE": $EntityProgressProvider


public static "values"(): ($EntityProgressProvider)[]
public static "valueOf"(arg0: string): $EntityProgressProvider
public "isRequired"(): boolean
public "getUid"(): $ResourceLocation
public "getDefaultPriority"(): integer
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $EntityAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "enabledByDefault"(): boolean
get "required"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityProgressProvider$Type = (("instance")) | ($EntityProgressProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityProgressProvider_ = $EntityProgressProvider$Type;
}}
declare module "packages/snownee/jade/impl/$EntityAccessorImpl$Builder" {
import {$EntityAccessor$Builder, $EntityAccessor$Builder$Type} from "packages/snownee/jade/api/$EntityAccessor$Builder"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$EntityHitResult, $EntityHitResult$Type} from "packages/net/minecraft/world/phys/$EntityHitResult"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EntityAccessorImpl$Builder implements $EntityAccessor$Builder {
 "showDetails": boolean

constructor()

public "from"(arg0: $EntityAccessor$Type): $EntityAccessorImpl$Builder
public "build"(): $EntityAccessor
public "player"(arg0: $Player$Type): $EntityAccessorImpl$Builder
public "serverData"(arg0: $CompoundTag$Type): $EntityAccessorImpl$Builder
public "showDetails"(arg0: boolean): $EntityAccessorImpl$Builder
public "requireVerification"(): $EntityAccessor$Builder
public "hit"(arg0: $EntityHitResult$Type): $EntityAccessor$Builder
public "entity"(arg0: $Entity$Type): $EntityAccessor$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityAccessorImpl$Builder$Type = ($EntityAccessorImpl$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityAccessorImpl$Builder_ = $EntityAccessorImpl$Builder$Type;
}}
declare module "packages/snownee/jade/gui/config/value/$OptionValue" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$OptionsList$Entry, $OptionsList$Entry$Type} from "packages/snownee/jade/gui/config/$OptionsList$Entry"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$NarrationElementOutput, $NarrationElementOutput$Type} from "packages/net/minecraft/client/gui/narration/$NarrationElementOutput"

export class $OptionValue<T> extends $OptionsList$Entry {
 "serverFeature": boolean

constructor(arg0: string, arg1: $Consumer$Type<(T)>)

public "parent"(arg0: $OptionsList$Entry$Type): $OptionsList$Entry
public "save"(): void
public "setValue"(arg0: T): void
public "isValidValue"(): boolean
public "getX"(): integer
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: boolean, arg9: float): void
public "updateNarration"(arg0: $NarrationElementOutput$Type): void
public "getTitle"(): $Component
public "getTextWidth"(): integer
public "getTextX"(arg0: integer): integer
public "appendDescription"(arg0: string): void
set "value"(value: T)
get "validValue"(): boolean
get "x"(): integer
get "title"(): $Component
get "textWidth"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionValue$Type<T> = ($OptionValue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionValue_<T> = $OptionValue$Type<(T)>;
}}
declare module "packages/snownee/jade/addon/harvest/$SimpleToolHandler" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$ToolHandler, $ToolHandler$Type} from "packages/snownee/jade/addon/harvest/$ToolHandler"

export class $SimpleToolHandler implements $ToolHandler {
readonly "blocks": $Set<($Block)>

constructor(arg0: string, arg1: $TagKey$Type<($Block$Type)>, ...arg2: ($Item$Type)[])

public "getName"(): string
public "test"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): $ItemStack
public "getTools"(): $List<($ItemStack)>
public "matchesBlock"(arg0: $BlockState$Type): boolean
get "name"(): string
get "tools"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleToolHandler$Type = ($SimpleToolHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleToolHandler_ = $SimpleToolHandler$Type;
}}
declare module "packages/snownee/jade/api/callback/$JadeItemModNameCallback" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $JadeItemModNameCallback {

 "gatherItemModName"(arg0: $ItemStack$Type): string

(arg0: $ItemStack$Type): string
}

export namespace $JadeItemModNameCallback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeItemModNameCallback$Type = ($JadeItemModNameCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeItemModNameCallback_ = $JadeItemModNameCallback$Type;
}}
declare module "packages/snownee/jade/addon/$JadeAddons" {
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export class $JadeAddons {
static readonly "ID": string
static readonly "NAME": string
static readonly "LOGGER": $Logger

constructor()

public static "seconds"(arg0: integer): $MutableComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeAddons$Type = ($JadeAddons);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeAddons_ = $JadeAddons$Type;
}}
declare module "packages/snownee/jade/impl/template/$TemplateBlockComponentProvider" {
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$TemplateComponentProvider, $TemplateComponentProvider$Type} from "packages/snownee/jade/impl/template/$TemplateComponentProvider"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $TemplateBlockComponentProvider extends $TemplateComponentProvider<($BlockAccessor)> implements $IBlockComponentProvider {

constructor(arg0: $ResourceLocation$Type)
constructor(arg0: $ResourceLocation$Type, arg1: boolean, arg2: boolean, arg3: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TemplateBlockComponentProvider$Type = ($TemplateBlockComponentProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TemplateBlockComponentProvider_ = $TemplateBlockComponentProvider$Type;
}}
declare module "packages/snownee/jade/network/$RequestEntityPacket" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"

export class $RequestEntityPacket {
 "accessor": $EntityAccessor
 "buffer": $FriendlyByteBuf

constructor(arg0: $EntityAccessor$Type)
constructor(arg0: $FriendlyByteBuf$Type)

public static "write"(arg0: $RequestEntityPacket$Type, arg1: $FriendlyByteBuf$Type): void
public static "read"(arg0: $FriendlyByteBuf$Type): $RequestEntityPacket
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RequestEntityPacket$Type = ($RequestEntityPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RequestEntityPacket_ = $RequestEntityPacket$Type;
}}
declare module "packages/snownee/jade/impl/template/$TemplateServerDataProvider" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $TemplateServerDataProvider<T extends $Accessor<(any)>> implements $IServerDataProvider<(T)> {


public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: T): void
public "setDataFunction"(arg0: $BiConsumer$Type<($CompoundTag$Type), (T)>): void
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
set "dataFunction"(value: $BiConsumer$Type<($CompoundTag$Type), (T)>)
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TemplateServerDataProvider$Type<T> = ($TemplateServerDataProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TemplateServerDataProvider_<T> = $TemplateServerDataProvider$Type<(T)>;
}}
declare module "packages/snownee/jade/api/ui/$IElementHelper" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IProgressStyle, $IProgressStyle$Type} from "packages/snownee/jade/api/ui/$IProgressStyle"
import {$IBoxElement, $IBoxElement$Type} from "packages/snownee/jade/api/ui/$IBoxElement"
import {$IBoxStyle, $IBoxStyle$Type} from "packages/snownee/jade/api/ui/$IBoxStyle"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$JadeFluidObject, $JadeFluidObject$Type} from "packages/snownee/jade/api/fluid/$JadeFluidObject"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export interface $IElementHelper {

 "box"(arg0: $ITooltip$Type, arg1: $IBoxStyle$Type): $IBoxElement
 "text"(arg0: $Component$Type): $IElement
 "item"(arg0: $ItemStack$Type, arg1: float, arg2: string): $IElement
 "item"(arg0: $ItemStack$Type, arg1: float): $IElement
 "item"(arg0: $ItemStack$Type): $IElement
 "tooltip"(): $ITooltip
 "progress"(arg0: float, arg1: $Component$Type, arg2: $IProgressStyle$Type, arg3: $IBoxStyle$Type, arg4: boolean): $IElement
 "smallItem"(arg0: $ItemStack$Type): $IElement
 "spacer"(arg0: integer, arg1: integer): $IElement
 "progressStyle"(): $IProgressStyle
 "fluid"(arg0: $JadeFluidObject$Type): $IElement
}

export namespace $IElementHelper {
function get(): $IElementHelper
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IElementHelper$Type = ($IElementHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IElementHelper_ = $IElementHelper$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$ItemTooltipProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $ItemTooltipProvider extends $Enum<($ItemTooltipProvider)> implements $IEntityComponentProvider {
static readonly "INSTANCE": $ItemTooltipProvider


public static "values"(): ($ItemTooltipProvider)[]
public static "valueOf"(arg0: string): $ItemTooltipProvider
public "getUid"(): $ResourceLocation
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemTooltipProvider$Type = (("instance")) | ($ItemTooltipProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemTooltipProvider_ = $ItemTooltipProvider$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$BlockStatesProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $BlockStatesProvider extends $Enum<($BlockStatesProvider)> implements $IBlockComponentProvider {
static readonly "INSTANCE": $BlockStatesProvider


public static "values"(): ($BlockStatesProvider)[]
public static "valueOf"(arg0: string): $BlockStatesProvider
public "getUid"(): $ResourceLocation
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
get "required"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStatesProvider$Type = (("instance")) | ($BlockStatesProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStatesProvider_ = $BlockStatesProvider$Type;
}}
declare module "packages/snownee/jade/util/$JadeForgeUtils" {
import {$IFluidHandler, $IFluidHandler$Type} from "packages/net/minecraftforge/fluids/capability/$IFluidHandler"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$List, $List$Type} from "packages/java/util/$List"
import {$FluidStack, $FluidStack$Type} from "packages/net/minecraftforge/fluids/$FluidStack"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ItemIterator, $ItemIterator$Type} from "packages/snownee/jade/addon/universal/$ItemIterator"
import {$IItemHandler, $IItemHandler$Type} from "packages/net/minecraftforge/items/$IItemHandler"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"

export class $JadeForgeUtils {


public static "fromFluidHandler"(arg0: $IFluidHandler$Type): $List<($ViewGroup<($CompoundTag)>)>
public static "fromItemHandler"(arg0: $IItemHandler$Type, arg1: integer, arg2: $Function$Type<(any), ($IItemHandler$Type)>): $ItemIterator<(any)>
/**
 * 
 * @deprecated
 */
public static "fromItemHandler"(arg0: $IItemHandler$Type, arg1: integer, arg2: integer): $ViewGroup<($ItemStack)>
public static "fromItemHandler"(arg0: $IItemHandler$Type, arg1: integer): $ItemIterator<(any)>
public static "fromFluidStack"(arg0: $FluidStack$Type, arg1: long): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeForgeUtils$Type = ($JadeForgeUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeForgeUtils_ = $JadeForgeUtils$Type;
}}
declare module "packages/snownee/jade/impl/ui/$HorizontalLineElement" {
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$Element, $Element$Type} from "packages/snownee/jade/api/ui/$Element"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $HorizontalLineElement extends $Element {
 "color": integer

constructor()

public "getSize"(): $Vec2
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
get "size"(): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HorizontalLineElement$Type = ($HorizontalLineElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HorizontalLineElement_ = $HorizontalLineElement$Type;
}}
declare module "packages/snownee/jade/addon/harvest/$ToolHandler" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $ToolHandler {

 "getName"(): string
 "test"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): $ItemStack
 "getTools"(): $List<($ItemStack)>
}

export namespace $ToolHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ToolHandler$Type = ($ToolHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ToolHandler_ = $ToolHandler$Type;
}}
declare module "packages/snownee/jade/$Jade" {
import {$WailaConfig, $WailaConfig$Type} from "packages/snownee/jade/impl/config/$WailaConfig"
import {$JsonConfig, $JsonConfig$Type} from "packages/snownee/jade/util/$JsonConfig"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $Jade {
static readonly "MODID": string
static readonly "LOGGER": $Logger
static readonly "CONFIG": $JsonConfig<($WailaConfig)>
static "MAX_DISTANCE_SQR": integer
static "FROZEN": boolean

constructor()

public static "loadComplete"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Jade$Type = ($Jade);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Jade_ = $Jade$Type;
}}
declare module "packages/snownee/jade/api/platform/$PlatformWailaClientRegistration" {
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$CustomEnchantPower, $CustomEnchantPower$Type} from "packages/snownee/jade/api/platform/$CustomEnchantPower"

export interface $PlatformWailaClientRegistration {

 "registerCustomEnchantPower"(arg0: $Block$Type, arg1: $CustomEnchantPower$Type): void

(arg0: $Block$Type, arg1: $CustomEnchantPower$Type): void
}

export namespace $PlatformWailaClientRegistration {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformWailaClientRegistration$Type = ($PlatformWailaClientRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformWailaClientRegistration_ = $PlatformWailaClientRegistration$Type;
}}
declare module "packages/snownee/jade/api/config/$IWailaConfig$IConfigGeneral" {
import {$IWailaConfig$TTSMode, $IWailaConfig$TTSMode$Type} from "packages/snownee/jade/api/config/$IWailaConfig$TTSMode"
import {$IWailaConfig$DisplayMode, $IWailaConfig$DisplayMode$Type} from "packages/snownee/jade/api/config/$IWailaConfig$DisplayMode"
import {$IWailaConfig$BossBarOverlapMode, $IWailaConfig$BossBarOverlapMode$Type} from "packages/snownee/jade/api/config/$IWailaConfig$BossBarOverlapMode"
import {$IWailaConfig$FluidMode, $IWailaConfig$FluidMode$Type} from "packages/snownee/jade/api/config/$IWailaConfig$FluidMode"

export interface $IWailaConfig$IConfigGeneral {

 "isDebug"(): boolean
 "setDisplayEntities"(arg0: boolean): void
 "setDisplayBosses"(arg0: boolean): void
 "setHideFromTabList"(arg0: boolean): void
 "setDebug"(arg0: boolean): void
 "setDisplayBlocks"(arg0: boolean): void
 "getDisplayBlocks"(): boolean
 "setHideFromDebug"(arg0: boolean): void
 "setTTSMode"(arg0: $IWailaConfig$TTSMode$Type): void
 "getDisplayFluids"(): $IWailaConfig$FluidMode
 "setReachDistance"(arg0: float): void
 "getDisplayEntities"(): boolean
 "getDisplayBosses"(): boolean
 "getReachDistance"(): float
 "setDisplayFluids"(arg0: $IWailaConfig$FluidMode$Type): void
 "setDisplayFluids"(arg0: boolean): void
 "getDisplayMode"(): $IWailaConfig$DisplayMode
 "setDisplayTooltip"(arg0: boolean): void
 "getTTSMode"(): $IWailaConfig$TTSMode
 "toggleTTS"(): void
 "getBossBarOverlapMode"(): $IWailaConfig$BossBarOverlapMode
 "getBuiltinCamouflage"(): boolean
 "shouldDisplayFluids"(): boolean
 "shouldEnableTextToSpeech"(): boolean
 "showItemModNameTooltip"(): boolean
 "shouldDisplayTooltip"(): boolean
 "setBuiltinCamouflage"(arg0: boolean): void
 "setItemModNameTooltip"(arg0: boolean): void
 "setBossBarOverlapMode"(arg0: $IWailaConfig$BossBarOverlapMode$Type): void
 "shouldHideFromDebug"(): boolean
 "shouldHideFromTabList"(): boolean
 "setDisplayMode"(arg0: $IWailaConfig$DisplayMode$Type): void
}

export namespace $IWailaConfig$IConfigGeneral {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IWailaConfig$IConfigGeneral$Type = ($IWailaConfig$IConfigGeneral);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IWailaConfig$IConfigGeneral_ = $IWailaConfig$IConfigGeneral$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$RedstoneProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $RedstoneProvider extends $Enum<($RedstoneProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)> {
static readonly "INSTANCE": $RedstoneProvider


public static "values"(): ($RedstoneProvider)[]
public static "valueOf"(arg0: string): $RedstoneProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RedstoneProvider$Type = (("instance")) | ($RedstoneProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RedstoneProvider_ = $RedstoneProvider$Type;
}}
declare module "packages/snownee/jade/impl/config/$WailaConfig$ConfigGeneral$ConfigDisplay" {
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$IWailaConfig$DisplayMode, $IWailaConfig$DisplayMode$Type} from "packages/snownee/jade/api/config/$IWailaConfig$DisplayMode"

export class $WailaConfig$ConfigGeneral$ConfigDisplay {
static readonly "CODEC": $MapCodec<($WailaConfig$ConfigGeneral$ConfigDisplay)>

constructor(arg0: boolean, arg1: boolean, arg2: boolean, arg3: boolean, arg4: $IWailaConfig$DisplayMode$Type)
constructor()

public "setDisplayEntities"(arg0: boolean): void
public "setDisplayBosses"(arg0: boolean): void
public "setDisplayBlocks"(arg0: boolean): void
public "getDisplayBlocks"(): boolean
public "getDisplayEntities"(): boolean
public "getDisplayBosses"(): boolean
public "getDisplayMode"(): $IWailaConfig$DisplayMode
public "setDisplayTooltip"(arg0: boolean): void
public "shouldDisplayTooltip"(): boolean
public "setDisplayMode"(arg0: $IWailaConfig$DisplayMode$Type): void
set "displayEntities"(value: boolean)
set "displayBosses"(value: boolean)
set "displayBlocks"(value: boolean)
get "displayBlocks"(): boolean
get "displayEntities"(): boolean
get "displayBosses"(): boolean
get "displayMode"(): $IWailaConfig$DisplayMode
set "displayTooltip"(value: boolean)
set "displayMode"(value: $IWailaConfig$DisplayMode$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WailaConfig$ConfigGeneral$ConfigDisplay$Type = ($WailaConfig$ConfigGeneral$ConfigDisplay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WailaConfig$ConfigGeneral$ConfigDisplay_ = $WailaConfig$ConfigGeneral$ConfigDisplay$Type;
}}
declare module "packages/snownee/jade/impl/config/entry/$StringConfigEntry" {
import {$ConfigEntry, $ConfigEntry$Type} from "packages/snownee/jade/impl/config/entry/$ConfigEntry"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$OptionsList, $OptionsList$Type} from "packages/snownee/jade/gui/config/$OptionsList"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$OptionValue, $OptionValue$Type} from "packages/snownee/jade/gui/config/value/$OptionValue"

export class $StringConfigEntry extends $ConfigEntry<(string)> {
readonly "id": $ResourceLocation

constructor(arg0: $ResourceLocation$Type, arg1: string, arg2: $Predicate$Type<(string)>)

public "isValidValue"(arg0: any): boolean
public "createUI"(arg0: $OptionsList$Type, arg1: string): $OptionValue<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringConfigEntry$Type = ($StringConfigEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringConfigEntry_ = $StringConfigEntry$Type;
}}
declare module "packages/snownee/jade/impl/ui/$ScaledTextElement" {
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$TextElement, $TextElement$Type} from "packages/snownee/jade/impl/ui/$TextElement"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ScaledTextElement extends $TextElement {
readonly "scale": float
readonly "text": $FormattedText

constructor(arg0: $Component$Type, arg1: float)

public "getMessage"(): string
public "getSize"(): $Vec2
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
get "message"(): string
get "size"(): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScaledTextElement$Type = ($ScaledTextElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScaledTextElement_ = $ScaledTextElement$Type;
}}
declare module "packages/snownee/jade/overlay/$ProgressTracker" {
import {$ProgressTracker$TrackInfo, $ProgressTracker$TrackInfo$Type} from "packages/snownee/jade/overlay/$ProgressTracker$TrackInfo"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ProgressTracker {

constructor()

public "clear"(): void
public "tick"(): void
public "createInfo"(arg0: $ResourceLocation$Type, arg1: float, arg2: boolean, arg3: float): $ProgressTracker$TrackInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProgressTracker$Type = ($ProgressTracker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProgressTracker_ = $ProgressTracker$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$AnimalOwnerProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $AnimalOwnerProvider extends $Enum<($AnimalOwnerProvider)> implements $IEntityComponentProvider, $IServerDataProvider<($EntityAccessor)> {
static readonly "INSTANCE": $AnimalOwnerProvider


public static "values"(): ($AnimalOwnerProvider)[]
public static "valueOf"(arg0: string): $AnimalOwnerProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $EntityAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimalOwnerProvider$Type = (("instance")) | ($AnimalOwnerProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimalOwnerProvider_ = $AnimalOwnerProvider$Type;
}}
declare module "packages/snownee/jade/api/config/$IWailaConfig$IconMode" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$SimpleStringRepresentable, $SimpleStringRepresentable$Type} from "packages/snownee/jade/api/$SimpleStringRepresentable"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $IWailaConfig$IconMode extends $Enum<($IWailaConfig$IconMode)> implements $SimpleStringRepresentable {
static readonly "TOP": $IWailaConfig$IconMode
static readonly "CENTERED": $IWailaConfig$IconMode
static readonly "HIDE": $IWailaConfig$IconMode


public static "values"(): ($IWailaConfig$IconMode)[]
public static "valueOf"(arg0: string): $IWailaConfig$IconMode
public "getSerializedName"(): string
public static "fromEnumWithMapping"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
public static "keys"(arg0: ($StringRepresentable$Type)[]): $Keyable
public static "fromEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
get "serializedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IWailaConfig$IconMode$Type = (("hide") | ("top") | ("centered")) | ($IWailaConfig$IconMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IWailaConfig$IconMode_ = $IWailaConfig$IconMode$Type;
}}
declare module "packages/snownee/jade/impl/ui/$ProgressStyle" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IProgressStyle, $IProgressStyle$Type} from "packages/snownee/jade/api/ui/$IProgressStyle"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ProgressStyle implements $IProgressStyle {
 "autoTextColor": boolean
 "color": integer
 "color2": integer
 "textColor": integer
 "vertical": boolean
 "overlay": $IElement
/**
 * 
 * @deprecated
 */
 "glowText": boolean
 "shadow": boolean

constructor()

public "color"(arg0: integer, arg1: integer): $IProgressStyle
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Component$Type): void
public "overlay"(arg0: $IElement$Type): $IProgressStyle
public "textColor"(arg0: integer): $IProgressStyle
public "vertical"(arg0: boolean): $IProgressStyle
public "glowText"(arg0: boolean): $IProgressStyle
public "color"(arg0: integer): $IProgressStyle
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProgressStyle$Type = ($ProgressStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProgressStyle_ = $ProgressStyle$Type;
}}
declare module "packages/snownee/jade/gui/$JadeFont" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $JadeFont {

 "jade$setGlintStrength"(arg0: float, arg1: float): void
 "jade$setGlint"(arg0: float, arg1: float): void
}

export namespace $JadeFont {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeFont$Type = ($JadeFont);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeFont_ = $JadeFont$Type;
}}
declare module "packages/snownee/jade/impl/ui/$ProgressElement" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IProgressStyle, $IProgressStyle$Type} from "packages/snownee/jade/api/ui/$IProgressStyle"
import {$IBoxStyle, $IBoxStyle$Type} from "packages/snownee/jade/api/ui/$IBoxStyle"
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$Element, $Element$Type} from "packages/snownee/jade/api/ui/$Element"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ProgressElement extends $Element {

constructor(arg0: float, arg1: $Component$Type, arg2: $IProgressStyle$Type, arg3: $IBoxStyle$Type, arg4: boolean)

public "getMessage"(): string
public "getSize"(): $Vec2
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
get "message"(): string
get "size"(): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProgressElement$Type = ($ProgressElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProgressElement_ = $ProgressElement$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$VanillaPlugin" {
import {$IWailaClientRegistration, $IWailaClientRegistration$Type} from "packages/snownee/jade/api/$IWailaClientRegistration"
import {$IWailaPlugin, $IWailaPlugin$Type} from "packages/snownee/jade/api/$IWailaPlugin"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IWailaCommonRegistration, $IWailaCommonRegistration$Type} from "packages/snownee/jade/api/$IWailaCommonRegistration"

export class $VanillaPlugin implements $IWailaPlugin {
static "CLIENT_REGISTRATION": $IWailaClientRegistration

constructor()

public "register"(arg0: $IWailaCommonRegistration$Type): void
public "registerClient"(arg0: $IWailaClientRegistration$Type): void
public static "getCorrespondingNormalChest"(arg0: $BlockState$Type): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VanillaPlugin$Type = ($VanillaPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VanillaPlugin_ = $VanillaPlugin$Type;
}}
declare module "packages/snownee/jade/api/config/$IWailaConfig$IConfigFormatting" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"

export interface $IWailaConfig$IConfigFormatting {

 "registryName"(arg0: string): $Component
/**
 * 
 * @deprecated
 */
 "title"(arg0: any): $Component
 "getModName"(): string
 "setModName"(arg0: string): void
}

export namespace $IWailaConfig$IConfigFormatting {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IWailaConfig$IConfigFormatting$Type = ($IWailaConfig$IConfigFormatting);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IWailaConfig$IConfigFormatting_ = $IWailaConfig$IConfigFormatting$Type;
}}
declare module "packages/snownee/jade/addon/lootr/$LootrInventoryProvider" {
import {$ClientViewGroup, $ClientViewGroup$Type} from "packages/snownee/jade/api/view/$ClientViewGroup"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$IServerExtensionProvider, $IServerExtensionProvider$Type} from "packages/snownee/jade/api/view/$IServerExtensionProvider"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IClientExtensionProvider, $IClientExtensionProvider$Type} from "packages/snownee/jade/api/view/$IClientExtensionProvider"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$ItemView, $ItemView$Type} from "packages/snownee/jade/api/view/$ItemView"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"

export class $LootrInventoryProvider extends $Enum<($LootrInventoryProvider)> implements $IServerExtensionProvider<(any), ($ItemStack)>, $IClientExtensionProvider<($ItemStack), ($ItemView)> {
static readonly "INSTANCE": $LootrInventoryProvider


public static "values"(): ($LootrInventoryProvider)[]
public static "valueOf"(arg0: string): $LootrInventoryProvider
public "getUid"(): $ResourceLocation
public "getClientGroups"(arg0: $Accessor$Type<(any)>, arg1: $List$Type<($ViewGroup$Type<($ItemStack$Type)>)>): $List<($ClientViewGroup<($ItemView)>)>
public "getGroups"(arg0: $ServerPlayer$Type, arg1: $ServerLevel$Type, arg2: any, arg3: boolean): $List<($ViewGroup<($ItemStack)>)>
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrInventoryProvider$Type = (("instance")) | ($LootrInventoryProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrInventoryProvider_ = $LootrInventoryProvider$Type;
}}
declare module "packages/snownee/jade/addon/create/$HideBoilerHandlerProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ClientViewGroup, $ClientViewGroup$Type} from "packages/snownee/jade/api/view/$ClientViewGroup"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$IServerExtensionProvider, $IServerExtensionProvider$Type} from "packages/snownee/jade/api/view/$IServerExtensionProvider"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IClientExtensionProvider, $IClientExtensionProvider$Type} from "packages/snownee/jade/api/view/$IClientExtensionProvider"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$List, $List$Type} from "packages/java/util/$List"
import {$FluidTankBlockEntity, $FluidTankBlockEntity$Type} from "packages/com/simibubi/create/content/fluids/tank/$FluidTankBlockEntity"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$FluidView, $FluidView$Type} from "packages/snownee/jade/api/view/$FluidView"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"

export class $HideBoilerHandlerProvider extends $Enum<($HideBoilerHandlerProvider)> implements $IServerExtensionProvider<($FluidTankBlockEntity), ($CompoundTag)>, $IClientExtensionProvider<($CompoundTag), ($FluidView)> {
static readonly "INSTANCE": $HideBoilerHandlerProvider


public static "values"(): ($HideBoilerHandlerProvider)[]
public static "valueOf"(arg0: string): $HideBoilerHandlerProvider
public "getUid"(): $ResourceLocation
public "getClientGroups"(arg0: $Accessor$Type<(any)>, arg1: $List$Type<($ViewGroup$Type<($CompoundTag$Type)>)>): $List<($ClientViewGroup<($FluidView)>)>
public "getGroups"(arg0: $ServerPlayer$Type, arg1: $ServerLevel$Type, arg2: $FluidTankBlockEntity$Type, arg3: boolean): $List<($ViewGroup<($CompoundTag)>)>
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HideBoilerHandlerProvider$Type = (("instance")) | ($HideBoilerHandlerProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HideBoilerHandlerProvider_ = $HideBoilerHandlerProvider$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$ZombieVillagerProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $ZombieVillagerProvider extends $Enum<($ZombieVillagerProvider)> implements $IEntityComponentProvider, $IServerDataProvider<($EntityAccessor)> {
static readonly "INSTANCE": $ZombieVillagerProvider


public static "values"(): ($ZombieVillagerProvider)[]
public static "valueOf"(arg0: string): $ZombieVillagerProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $EntityAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ZombieVillagerProvider$Type = (("instance")) | ($ZombieVillagerProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ZombieVillagerProvider_ = $ZombieVillagerProvider$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$MobGrowthProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $MobGrowthProvider extends $Enum<($MobGrowthProvider)> implements $IEntityComponentProvider, $IServerDataProvider<($EntityAccessor)> {
static readonly "INSTANCE": $MobGrowthProvider


public static "values"(): ($MobGrowthProvider)[]
public static "valueOf"(arg0: string): $MobGrowthProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $EntityAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobGrowthProvider$Type = (("instance")) | ($MobGrowthProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobGrowthProvider_ = $MobGrowthProvider$Type;
}}
declare module "packages/snownee/jade/compat/$JEICompat" {
import {$IGuiHandlerRegistration, $IGuiHandlerRegistration$Type} from "packages/mezz/jei/api/registration/$IGuiHandlerRegistration"
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
import {$IAdvancedRegistration, $IAdvancedRegistration$Type} from "packages/mezz/jei/api/registration/$IAdvancedRegistration"
import {$IVanillaCategoryExtensionRegistration, $IVanillaCategoryExtensionRegistration$Type} from "packages/mezz/jei/api/registration/$IVanillaCategoryExtensionRegistration"
import {$IRecipeRegistration, $IRecipeRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeRegistration"
import {$IRecipeTransferRegistration, $IRecipeTransferRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeTransferRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IJeiRuntime, $IJeiRuntime$Type} from "packages/mezz/jei/api/runtime/$IJeiRuntime"
import {$IRecipeCatalystRegistration, $IRecipeCatalystRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCatalystRegistration"
import {$IModPlugin, $IModPlugin$Type} from "packages/mezz/jei/api/$IModPlugin"
import {$IRuntimeRegistration, $IRuntimeRegistration$Type} from "packages/mezz/jei/api/registration/$IRuntimeRegistration"
import {$IRecipeCategoryRegistration, $IRecipeCategoryRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCategoryRegistration"
import {$IModIngredientRegistration, $IModIngredientRegistration$Type} from "packages/mezz/jei/api/registration/$IModIngredientRegistration"
import {$ISubtypeRegistration, $ISubtypeRegistration$Type} from "packages/mezz/jei/api/registration/$ISubtypeRegistration"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export class $JEICompat implements $IModPlugin {
static readonly "ID": $ResourceLocation

constructor()

public static "onKeyPressed"(arg0: integer): void
public "getPluginUid"(): $ResourceLocation
public "registerRecipes"(arg0: $IRecipeRegistration$Type): void
public "onRuntimeAvailable"(arg0: $IJeiRuntime$Type): void
public "registerItemSubtypes"(arg0: $ISubtypeRegistration$Type): void
public "registerVanillaCategoryExtensions"(arg0: $IVanillaCategoryExtensionRegistration$Type): void
public "registerFluidSubtypes"<T>(arg0: $ISubtypeRegistration$Type, arg1: $IPlatformFluidHelper$Type<(T)>): void
public "onConfigManagerAvailable"(arg0: $IJeiConfigManager$Type): void
public "registerGuiHandlers"(arg0: $IGuiHandlerRegistration$Type): void
public "onRuntimeUnavailable"(): void
public "registerIngredients"(arg0: $IModIngredientRegistration$Type): void
public "registerRecipeTransferHandlers"(arg0: $IRecipeTransferRegistration$Type): void
public "registerRecipeCatalysts"(arg0: $IRecipeCatalystRegistration$Type): void
public "registerAdvanced"(arg0: $IAdvancedRegistration$Type): void
public "registerCategories"(arg0: $IRecipeCategoryRegistration$Type): void
public "registerRuntime"(arg0: $IRuntimeRegistration$Type): void
get "pluginUid"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEICompat$Type = ($JEICompat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEICompat_ = $JEICompat$Type;
}}
declare module "packages/snownee/jade/api/view/$EnergyView" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"

export class $EnergyView {
 "current": string
 "max": string
 "ratio": float
 "overrideText": $Component

constructor()

public static "of"(arg0: long, arg1: long): $CompoundTag
public static "read"(arg0: $CompoundTag$Type, arg1: string): $EnergyView
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnergyView$Type = ($EnergyView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnergyView_ = $EnergyView$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$FallingBlockProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $FallingBlockProvider extends $Enum<($FallingBlockProvider)> implements $IEntityComponentProvider {
static readonly "INSTANCE": $FallingBlockProvider


public static "values"(): ($FallingBlockProvider)[]
public static "valueOf"(arg0: string): $FallingBlockProvider
public "isRequired"(): boolean
public "getUid"(): $ResourceLocation
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "required"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FallingBlockProvider$Type = (("instance")) | ($FallingBlockProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FallingBlockProvider_ = $FallingBlockProvider$Type;
}}
declare module "packages/snownee/jade/impl/$Tooltip" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IElement$Align, $IElement$Align$Type} from "packages/snownee/jade/api/ui/$IElement$Align"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$Tooltip$Line, $Tooltip$Line$Type} from "packages/snownee/jade/impl/$Tooltip$Line"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IElementHelper, $IElementHelper$Type} from "packages/snownee/jade/api/ui/$IElementHelper"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $Tooltip implements $ITooltip {
readonly "lines": $List<($Tooltip$Line)>
 "sneakyDetails": boolean

constructor()

public "add"(arg0: integer, arg1: $IElement$Type): void
public "remove"(arg0: $ResourceLocation$Type): void
public "get"(arg0: integer, arg1: $IElement$Align$Type): $List<($IElement)>
public "get"(arg0: $ResourceLocation$Type): $List<($IElement)>
public "append"(arg0: integer, arg1: $IElement$Type): void
public "clear"(): void
public "size"(): integer
public "getMessage"(): string
public static "drawBorder"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: $IElement$Type): void
public "getElementHelper"(): $IElementHelper
public "add"(arg0: integer, arg1: $Component$Type): void
public "add"(arg0: integer, arg1: $Component$Type, arg2: $ResourceLocation$Type): void
public "add"(arg0: $IElement$Type): void
public "add"(arg0: integer, arg1: $List$Type<($IElement$Type)>): void
public "add"(arg0: $List$Type<($IElement$Type)>): void
public "add"(arg0: $Component$Type): void
public "add"(arg0: $Component$Type, arg1: $ResourceLocation$Type): void
public "append"(arg0: $IElement$Type): void
public "append"(arg0: $Component$Type, arg1: $ResourceLocation$Type): void
public "append"(arg0: integer, arg1: $List$Type<($IElement$Type)>): void
public "append"(arg0: $Component$Type): void
public "isEmpty"(): boolean
public "addAll"(arg0: $List$Type<($Component$Type)>): void
get "message"(): string
get "elementHelper"(): $IElementHelper
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Tooltip$Type = ($Tooltip);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Tooltip_ = $Tooltip$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$StatusEffectsProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $StatusEffectsProvider extends $Enum<($StatusEffectsProvider)> implements $IEntityComponentProvider, $IServerDataProvider<($EntityAccessor)> {
static readonly "INSTANCE": $StatusEffectsProvider


public static "values"(): ($StatusEffectsProvider)[]
public static "valueOf"(arg0: string): $StatusEffectsProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $EntityAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public static "getEffectName"(arg0: $MobEffectInstance$Type): $Component
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StatusEffectsProvider$Type = (("instance")) | ($StatusEffectsProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StatusEffectsProvider_ = $StatusEffectsProvider$Type;
}}
declare module "packages/snownee/jade/impl/theme/$ThemeSerializer" {
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$Theme, $Theme$Type} from "packages/snownee/jade/api/theme/$Theme"

export class $ThemeSerializer implements $JsonDeserializer<($Theme)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThemeSerializer$Type = ($ThemeSerializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThemeSerializer_ = $ThemeSerializer$Type;
}}
declare module "packages/snownee/jade/addon/create/$CreatePlugin" {
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$IWailaClientRegistration, $IWailaClientRegistration$Type} from "packages/snownee/jade/api/$IWailaClientRegistration"
import {$IWailaPlugin, $IWailaPlugin$Type} from "packages/snownee/jade/api/$IWailaPlugin"
import {$IWailaCommonRegistration, $IWailaCommonRegistration$Type} from "packages/snownee/jade/api/$IWailaCommonRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $CreatePlugin implements $IWailaPlugin {
static readonly "ID": string
static readonly "CRAFTING_BLUEPRINT": $ResourceLocation
static readonly "PLACARD": $ResourceLocation
static readonly "BLAZE_BURNER": $ResourceLocation
static readonly "CONTRAPTION_INVENTORY": $ResourceLocation
static readonly "CONTRAPTION_EXACT_BLOCK": $ResourceLocation
static readonly "FILTER": $ResourceLocation
static readonly "HIDE_BOILER_TANKS": $ResourceLocation
static readonly "BACKTANK_CAPACITY": $ResourceLocation
static readonly "GOGGLES": $ResourceLocation
static readonly "REQUIRES_GOGGLES": $ResourceLocation
static readonly "GOGGLES_DETAILED": $ResourceLocation

constructor()

public "register"(arg0: $IWailaCommonRegistration$Type): void
public "override"(arg0: $HitResult$Type, arg1: $Accessor$Type<(any)>, arg2: $Accessor$Type<(any)>): $Accessor<(any)>
public "registerClient"(arg0: $IWailaClientRegistration$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreatePlugin$Type = ($CreatePlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreatePlugin_ = $CreatePlugin$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$LecternProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $LecternProvider extends $Enum<($LecternProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)> {
static readonly "INSTANCE": $LecternProvider


public static "values"(): ($LecternProvider)[]
public static "valueOf"(arg0: string): $LecternProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LecternProvider$Type = (("instance")) | ($LecternProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LecternProvider_ = $LecternProvider$Type;
}}
declare module "packages/snownee/jade/command/$JadeServerCommand" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export class $JadeServerCommand {

constructor()

public static "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeServerCommand$Type = ($JadeServerCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeServerCommand_ = $JadeServerCommand$Type;
}}
declare module "packages/snownee/jade/addon/deep_resonance/$DeepResonancePlugin" {
import {$IWailaClientRegistration, $IWailaClientRegistration$Type} from "packages/snownee/jade/api/$IWailaClientRegistration"
import {$IWailaPlugin, $IWailaPlugin$Type} from "packages/snownee/jade/api/$IWailaPlugin"
import {$IWailaCommonRegistration, $IWailaCommonRegistration$Type} from "packages/snownee/jade/api/$IWailaCommonRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $DeepResonancePlugin implements $IWailaPlugin {
static readonly "ID": string
static readonly "CRYSTAL": $ResourceLocation
static readonly "GENERATOR_PART": $ResourceLocation

constructor()

public "register"(arg0: $IWailaCommonRegistration$Type): void
public "registerClient"(arg0: $IWailaClientRegistration$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeepResonancePlugin$Type = ($DeepResonancePlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeepResonancePlugin_ = $DeepResonancePlugin$Type;
}}
declare module "packages/snownee/jade/api/view/$ClientViewGroup" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $ClientViewGroup<T> {
readonly "views": $List<(T)>
 "title": $Component
 "bgColor": integer
 "progressColor": integer
 "progress": float
 "extraData": $CompoundTag

constructor(arg0: $List$Type<(T)>)

public static "map"<IN, OUT>(arg0: $List$Type<($ViewGroup$Type<(IN)>)>, arg1: $Function$Type<(IN), (OUT)>, arg2: $BiConsumer$Type<($ViewGroup$Type<(IN)>), ($ClientViewGroup$Type<(OUT)>)>): $List<($ClientViewGroup<(OUT)>)>
public "renderHeader"(arg0: $ITooltip$Type): void
public static "tooltip"<T>(arg0: $ITooltip$Type, arg1: $List$Type<($ClientViewGroup$Type<(T)>)>, arg2: boolean, arg3: $BiConsumer$Type<($ITooltip$Type), ($ClientViewGroup$Type<(T)>)>): void
public "shouldRenderGroup"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientViewGroup$Type<T> = ($ClientViewGroup<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientViewGroup_<T> = $ClientViewGroup$Type<(T)>;
}}
declare module "packages/snownee/jade/api/config/$IWailaConfig" {
import {$IWailaConfig$IConfigFormatting, $IWailaConfig$IConfigFormatting$Type} from "packages/snownee/jade/api/config/$IWailaConfig$IConfigFormatting"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$IWailaConfig$IConfigOverlay, $IWailaConfig$IConfigOverlay$Type} from "packages/snownee/jade/api/config/$IWailaConfig$IConfigOverlay"
import {$IWailaConfig$IConfigGeneral, $IWailaConfig$IConfigGeneral$Type} from "packages/snownee/jade/api/config/$IWailaConfig$IConfigGeneral"

export interface $IWailaConfig {

 "getFormatting"(): $IWailaConfig$IConfigFormatting
 "getPlugin"(): $IPluginConfig
 "getOverlay"(): $IWailaConfig$IConfigOverlay
 "getGeneral"(): $IWailaConfig$IConfigGeneral
}

export namespace $IWailaConfig {
function get(): $IWailaConfig
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IWailaConfig$Type = ($IWailaConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IWailaConfig_ = $IWailaConfig$Type;
}}
declare module "packages/snownee/jade/api/$Accessor$ClientHandler" {
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$IJadeProvider, $IJadeProvider$Type} from "packages/snownee/jade/api/$IJadeProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export interface $Accessor$ClientHandler<T extends $Accessor<(any)>> {

 "requestData"(arg0: T): void
 "shouldDisplay"(arg0: T): boolean
 "getIcon"(arg0: T): $IElement
 "gatherComponents"(arg0: T, arg1: $Function$Type<($IJadeProvider$Type), ($ITooltip$Type)>): void
 "shouldRequestData"(arg0: T): boolean
}

export namespace $Accessor$ClientHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Accessor$ClientHandler$Type<T> = ($Accessor$ClientHandler<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Accessor$ClientHandler_<T> = $Accessor$ClientHandler$Type<(T)>;
}}
declare module "packages/snownee/jade/api/ui/$IElement$Align" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $IElement$Align extends $Enum<($IElement$Align)> {
static readonly "LEFT": $IElement$Align
static readonly "RIGHT": $IElement$Align


public static "values"(): ($IElement$Align)[]
public static "valueOf"(arg0: string): $IElement$Align
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IElement$Align$Type = (("left") | ("right")) | ($IElement$Align);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IElement$Align_ = $IElement$Align$Type;
}}
declare module "packages/snownee/jade/overlay/$DatapackBlockManager" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $DatapackBlockManager {

constructor()

public static "override"(arg0: $HitResult$Type, arg1: $Accessor$Type<(any)>, arg2: $Accessor$Type<(any)>): $Accessor<(any)>
public static "onEntityJoin"(arg0: $Entity$Type): void
public static "onEntityLeave"(arg0: $Entity$Type): void
public static "getFakeBlock"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DatapackBlockManager$Type = ($DatapackBlockManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DatapackBlockManager_ = $DatapackBlockManager$Type;
}}
declare module "packages/snownee/jade/api/$WailaPlugin" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $WailaPlugin extends $Annotation {

 "value"(): string
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $WailaPlugin {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WailaPlugin$Type = ($WailaPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WailaPlugin_ = $WailaPlugin$Type;
}}
declare module "packages/snownee/jade/api/$Identifiers" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $Identifiers {
static readonly "PACKET_RECEIVE_DATA": $ResourceLocation
static readonly "PACKET_SERVER_PING": $ResourceLocation
static readonly "PACKET_REQUEST_ENTITY": $ResourceLocation
static readonly "PACKET_REQUEST_TILE": $ResourceLocation
static readonly "PACKET_SHOW_OVERLAY": $ResourceLocation
static readonly "CORE_OBJECT_NAME": $ResourceLocation
static readonly "CORE_REGISTRY_NAME": $ResourceLocation
static readonly "CORE_MOD_NAME": $ResourceLocation
static readonly "CORE_DISTANCE": $ResourceLocation
static readonly "CORE_COORDINATES": $ResourceLocation
static readonly "CORE_REL_COORDINATES": $ResourceLocation
static readonly "CORE_BLOCK_FACE": $ResourceLocation
static readonly "UNIVERSAL_ITEM_STORAGE": $ResourceLocation
static readonly "UNIVERSAL_FLUID_STORAGE": $ResourceLocation
static readonly "UNIVERSAL_FLUID_STORAGE_DETAILED": $ResourceLocation
static readonly "UNIVERSAL_ENERGY_STORAGE": $ResourceLocation
static readonly "UNIVERSAL_ENERGY_STORAGE_DETAILED": $ResourceLocation
static readonly "UNIVERSAL_PROGRESS": $ResourceLocation
static readonly "MC_ITEM_STORAGE_DETAILED_AMOUNT": $ResourceLocation
static readonly "MC_ITEM_STORAGE_NORMAL_AMOUNT": $ResourceLocation
static readonly "MC_ITEM_STORAGE_SHOW_NAME_AMOUNT": $ResourceLocation
static readonly "MC_ITEM_STORAGE_ITEMS_PER_LINE": $ResourceLocation
static readonly "MC_ANIMAL_OWNER": $ResourceLocation
static readonly "MC_ARMOR_STAND": $ResourceLocation
static readonly "MC_BEEHIVE": $ResourceLocation
static readonly "MC_BLOCK_STATES": $ResourceLocation
static readonly "MC_BREWING_STAND": $ResourceLocation
static readonly "MC_CAMPFIRE": $ResourceLocation
static readonly "MC_CHICKEN_EGG": $ResourceLocation
static readonly "MC_CHISELED_BOOKSHELF": $ResourceLocation
static readonly "MC_COMMAND_BLOCK": $ResourceLocation
static readonly "MC_CROP_PROGRESS": $ResourceLocation
static readonly "MC_ENCHANTMENT_POWER": $ResourceLocation
static readonly "MC_ENTITY_ARMOR": $ResourceLocation
static readonly "MC_ENTITY_ARMOR_MAX_FOR_RENDER": $ResourceLocation
static readonly "MC_ENTITY_HEALTH": $ResourceLocation
static readonly "MC_ENTITY_HEALTH_MAX_FOR_RENDER": $ResourceLocation
static readonly "MC_ENTITY_HEALTH_ICONS_PER_LINE": $ResourceLocation
static readonly "MC_ENTITY_HEALTH_SHOW_FRACTIONS": $ResourceLocation
static readonly "MC_FALLING_BLOCK": $ResourceLocation
static readonly "MC_FURNACE": $ResourceLocation
static readonly "MC_HARVEST_TOOL": $ResourceLocation
static readonly "MC_HARVEST_TOOL_NEW_LINE": $ResourceLocation
static readonly "MC_EFFECTIVE_TOOL": $ResourceLocation
static readonly "MC_SHOW_UNBREAKABLE": $ResourceLocation
static readonly "MC_HARVEST_TOOL_CREATIVE": $ResourceLocation
static readonly "MC_HORSE_STATS": $ResourceLocation
static readonly "MC_ITEM_BER": $ResourceLocation
static readonly "MC_ITEM_FRAME": $ResourceLocation
static readonly "MC_ITEM_TOOLTIP": $ResourceLocation
static readonly "MC_JUKEBOX": $ResourceLocation
static readonly "MC_LECTERN": $ResourceLocation
static readonly "MC_MOB_BREEDING": $ResourceLocation
static readonly "MC_MOB_GROWTH": $ResourceLocation
static readonly "MC_MOB_SPAWNER": $ResourceLocation
static readonly "MC_NOTE_BLOCK": $ResourceLocation
static readonly "MC_PAINTING": $ResourceLocation
static readonly "MC_PLAYER_HEAD": $ResourceLocation
static readonly "MC_POTION_EFFECTS": $ResourceLocation
static readonly "MC_REDSTONE": $ResourceLocation
static readonly "MC_TNT_STABILITY": $ResourceLocation
static readonly "MC_TOTAL_ENCHANTMENT_POWER": $ResourceLocation
static readonly "MC_VILLAGER_PROFESSION": $ResourceLocation
static readonly "MC_ITEM_DISPLAY": $ResourceLocation
static readonly "MC_BLOCK_DISPLAY": $ResourceLocation
static readonly "MC_BREAKING_PROGRESS": $ResourceLocation
static readonly "MC_ZOMBIE_VILLAGER": $ResourceLocation

constructor()

public static "JADE"(arg0: string): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Identifiers$Type = ($Identifiers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Identifiers_ = $Identifiers$Type;
}}
declare module "packages/snownee/jade/impl/theme/$ThemeHelper" {
import {$SimpleJsonResourceReloadListener, $SimpleJsonResourceReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimpleJsonResourceReloadListener"
import {$Theme, $Theme$Type} from "packages/snownee/jade/api/theme/$Theme"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Style, $Style$Type} from "packages/net/minecraft/network/chat/$Style"
import {$IThemeHelper, $IThemeHelper$Type} from "packages/snownee/jade/api/theme/$IThemeHelper"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export class $ThemeHelper extends $SimpleJsonResourceReloadListener implements $IThemeHelper {
static readonly "INSTANCE": $ThemeHelper
static readonly "ID": $ResourceLocation

constructor()

public "info"(arg0: any): $MutableComponent
public "failure"(arg0: any): $MutableComponent
public "success"(arg0: any): $MutableComponent
public "seconds"(arg0: integer): $MutableComponent
public "warning"(arg0: any): $MutableComponent
public "getTheme"(arg0: $ResourceLocation$Type): $Theme
public static "colorStyle"(arg0: integer): $Style
public "danger"(arg0: any): $MutableComponent
public "getThemes"(): $Collection<($Theme)>
public "theme"(): $Theme
public "title"(arg0: any): $MutableComponent
public static "get"(): $IThemeHelper
public "getNormalColor"(): integer
public "isLightColorScheme"(): boolean
get "themes"(): $Collection<($Theme)>
get "normalColor"(): integer
get "lightColorScheme"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThemeHelper$Type = ($ThemeHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThemeHelper_ = $ThemeHelper$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$NoteBlockProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $NoteBlockProvider extends $Enum<($NoteBlockProvider)> implements $IBlockComponentProvider {
static readonly "INSTANCE": $NoteBlockProvider


public static "values"(): ($NoteBlockProvider)[]
public static "valueOf"(arg0: string): $NoteBlockProvider
public "getUid"(): $ResourceLocation
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NoteBlockProvider$Type = (("instance")) | ($NoteBlockProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NoteBlockProvider_ = $NoteBlockProvider$Type;
}}
declare module "packages/snownee/jade/gui/$WailaConfigScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$OptionsList$Entry, $OptionsList$Entry$Type} from "packages/snownee/jade/gui/config/$OptionsList$Entry"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$OptionsList, $OptionsList$Type} from "packages/snownee/jade/gui/config/$OptionsList"
import {$BaseOptionsScreen, $BaseOptionsScreen$Type} from "packages/snownee/jade/gui/$BaseOptionsScreen"

export class $WailaConfigScreen extends $BaseOptionsScreen {
 "saveButton": $Button
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Screen$Type)

public static "editBlocklist"(arg0: $OptionsList$Entry$Type, arg1: string, arg2: $Runnable$Type): $OptionsList$Entry
public "createOptions"(): $OptionsList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WailaConfigScreen$Type = ($WailaConfigScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WailaConfigScreen_ = $WailaConfigScreen$Type;
}}
declare module "packages/snownee/jade/overlay/$IconUI" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $IconUI extends $Enum<($IconUI)> {
static readonly "HEART": $IconUI
static readonly "HALF_HEART": $IconUI
static readonly "EMPTY_HEART": $IconUI
static readonly "ARMOR": $IconUI
static readonly "HALF_ARMOR": $IconUI
static readonly "EMPTY_ARMOR": $IconUI
static readonly "EXPERIENCE_BUBBLE": $IconUI
readonly "u": integer
readonly "v": integer
readonly "su": integer
readonly "sv": integer
readonly "bu": integer
readonly "bv": integer
readonly "bsu": integer
readonly "bsv": integer


public static "values"(): ($IconUI)[]
public static "valueOf"(arg0: string): $IconUI
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IconUI$Type = (("armor") | ("half_armor") | ("experience_bubble") | ("half_heart") | ("empty_heart") | ("heart") | ("empty_armor")) | ($IconUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IconUI_ = $IconUI$Type;
}}
declare module "packages/snownee/jade/network/$ShowOverlayPacket" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export class $ShowOverlayPacket {
readonly "show": boolean

constructor(arg0: boolean)

public static "write"(arg0: $ShowOverlayPacket$Type, arg1: $FriendlyByteBuf$Type): void
public static "read"(arg0: $FriendlyByteBuf$Type): $ShowOverlayPacket
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShowOverlayPacket$Type = ($ShowOverlayPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShowOverlayPacket_ = $ShowOverlayPacket$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$ChiseledBookshelfProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $ChiseledBookshelfProvider extends $Enum<($ChiseledBookshelfProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)> {
static readonly "INSTANCE": $ChiseledBookshelfProvider


public static "values"(): ($ChiseledBookshelfProvider)[]
public static "valueOf"(arg0: string): $ChiseledBookshelfProvider
public "getUid"(): $ResourceLocation
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "getDefaultPriority"(): integer
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
get "required"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChiseledBookshelfProvider$Type = (("instance")) | ($ChiseledBookshelfProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChiseledBookshelfProvider_ = $ChiseledBookshelfProvider$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$TNTStabilityProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $TNTStabilityProvider extends $Enum<($TNTStabilityProvider)> implements $IBlockComponentProvider {
static readonly "INSTANCE": $TNTStabilityProvider


public static "values"(): ($TNTStabilityProvider)[]
public static "valueOf"(arg0: string): $TNTStabilityProvider
public "getUid"(): $ResourceLocation
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TNTStabilityProvider$Type = (("instance")) | ($TNTStabilityProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TNTStabilityProvider_ = $TNTStabilityProvider$Type;
}}
declare module "packages/snownee/jade/addon/universal/$EntityEnergyStorageProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $EntityEnergyStorageProvider extends $Enum<($EntityEnergyStorageProvider)> implements $IEntityComponentProvider, $IServerDataProvider<($EntityAccessor)> {
static readonly "INSTANCE": $EntityEnergyStorageProvider


public static "values"(): ($EntityEnergyStorageProvider)[]
public static "valueOf"(arg0: string): $EntityEnergyStorageProvider
public "getUid"(): $ResourceLocation
public "getDefaultPriority"(): integer
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $EntityAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
get "required"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityEnergyStorageProvider$Type = (("instance")) | ($EntityEnergyStorageProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityEnergyStorageProvider_ = $EntityEnergyStorageProvider$Type;
}}
declare module "packages/snownee/jade/api/callback/$JadeTooltipCollectedCallback" {
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export interface $JadeTooltipCollectedCallback {

 "onTooltipCollected"(arg0: $ITooltip$Type, arg1: $Accessor$Type<(any)>): void

(arg0: $ITooltip$Type, arg1: $Accessor$Type<(any)>): void
}

export namespace $JadeTooltipCollectedCallback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeTooltipCollectedCallback$Type = ($JadeTooltipCollectedCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeTooltipCollectedCallback_ = $JadeTooltipCollectedCallback$Type;
}}
declare module "packages/snownee/jade/impl/$CallbackContainer" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"

export class $CallbackContainer<T> {

constructor()

public "add"(arg0: integer, arg1: T): void
public "sort"(): void
public "call"(arg0: $Consumer$Type<(T)>): void
public "callbacks"(): $List<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CallbackContainer$Type<T> = ($CallbackContainer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CallbackContainer_<T> = $CallbackContainer$Type<(T)>;
}}
declare module "packages/snownee/jade/overlay/$ProgressTracker$TrackInfo" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ProgressTracker$TrackInfo {

constructor()

public "tick"(arg0: float): float
public "getWidth"(): float
get "width"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProgressTracker$TrackInfo$Type = ($ProgressTracker$TrackInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProgressTracker$TrackInfo_ = $ProgressTracker$TrackInfo$Type;
}}
declare module "packages/snownee/jade/impl/ui/$TextElement" {
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$Element, $Element$Type} from "packages/snownee/jade/api/ui/$Element"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $TextElement extends $Element {
readonly "text": $FormattedText

constructor(arg0: $Component$Type)
constructor(arg0: $FormattedText$Type)

public "getMessage"(): string
public "getSize"(): $Vec2
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
get "message"(): string
get "size"(): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextElement$Type = ($TextElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextElement_ = $TextElement$Type;
}}
declare module "packages/snownee/jade/impl/$EntityAccessorImpl" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$EntityAccessorImpl$Builder, $EntityAccessorImpl$Builder$Type} from "packages/snownee/jade/impl/$EntityAccessorImpl$Builder"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$EntityHitResult, $EntityHitResult$Type} from "packages/net/minecraft/world/phys/$EntityHitResult"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$AccessorImpl, $AccessorImpl$Type} from "packages/snownee/jade/api/$AccessorImpl"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EntityAccessorImpl extends $AccessorImpl<($EntityHitResult)> implements $EntityAccessor {

constructor(arg0: $EntityAccessorImpl$Builder$Type)

public "getTarget"(): any
public static "fromNetwork"(arg0: $FriendlyByteBuf$Type, arg1: $ServerPlayer$Type): $EntityAccessor
public "getPickedResult"(): $ItemStack
public static "handleRequest"(arg0: $FriendlyByteBuf$Type, arg1: $ServerPlayer$Type, arg2: $Consumer$Type<($Runnable$Type)>, arg3: $Consumer$Type<($CompoundTag$Type)>): void
public "toNetwork"(arg0: $FriendlyByteBuf$Type): void
public "getEntity"(): $Entity
public "getRawEntity"(): $Entity
public "verifyData"(arg0: $CompoundTag$Type): boolean
public "getAccessorType"(): $Class<(any)>
get "target"(): any
get "pickedResult"(): $ItemStack
get "entity"(): $Entity
get "rawEntity"(): $Entity
get "accessorType"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityAccessorImpl$Type = ($EntityAccessorImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityAccessorImpl_ = $EntityAccessorImpl$Type;
}}
declare module "packages/snownee/jade/gui/$PluginsConfigScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$OptionsList, $OptionsList$Type} from "packages/snownee/jade/gui/config/$OptionsList"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BaseOptionsScreen, $BaseOptionsScreen$Type} from "packages/snownee/jade/gui/$BaseOptionsScreen"

export class $PluginsConfigScreen extends $BaseOptionsScreen {
 "saveButton": $Button
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Screen$Type)

public static "createPluginConfigScreen"(arg0: $Screen$Type, arg1: string, arg2: boolean): $Screen
public "createOptions"(): $OptionsList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PluginsConfigScreen$Type = ($PluginsConfigScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PluginsConfigScreen_ = $PluginsConfigScreen$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$CampfireProvider" {
import {$ClientViewGroup, $ClientViewGroup$Type} from "packages/snownee/jade/api/view/$ClientViewGroup"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$IServerExtensionProvider, $IServerExtensionProvider$Type} from "packages/snownee/jade/api/view/$IServerExtensionProvider"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IClientExtensionProvider, $IClientExtensionProvider$Type} from "packages/snownee/jade/api/view/$IClientExtensionProvider"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$ItemView, $ItemView$Type} from "packages/snownee/jade/api/view/$ItemView"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"

export class $CampfireProvider extends $Enum<($CampfireProvider)> implements $IServerExtensionProvider<(any), ($ItemStack)>, $IClientExtensionProvider<($ItemStack), ($ItemView)> {
static readonly "INSTANCE": $CampfireProvider


public static "values"(): ($CampfireProvider)[]
public static "valueOf"(arg0: string): $CampfireProvider
public "getUid"(): $ResourceLocation
public "getClientGroups"(arg0: $Accessor$Type<(any)>, arg1: $List$Type<($ViewGroup$Type<($ItemStack$Type)>)>): $List<($ClientViewGroup<($ItemView)>)>
public "getGroups"(arg0: $ServerPlayer$Type, arg1: $ServerLevel$Type, arg2: any, arg3: boolean): $List<($ViewGroup<($ItemStack)>)>
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CampfireProvider$Type = (("instance")) | ($CampfireProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CampfireProvider_ = $CampfireProvider$Type;
}}
declare module "packages/snownee/jade/addon/universal/$EnergyStorageProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$ClientViewGroup, $ClientViewGroup$Type} from "packages/snownee/jade/api/view/$ClientViewGroup"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IServerExtensionProvider, $IServerExtensionProvider$Type} from "packages/snownee/jade/api/view/$IServerExtensionProvider"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IClientExtensionProvider, $IClientExtensionProvider$Type} from "packages/snownee/jade/api/view/$IClientExtensionProvider"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$EnergyView, $EnergyView$Type} from "packages/snownee/jade/api/view/$EnergyView"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $EnergyStorageProvider extends $Enum<($EnergyStorageProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)>, $IServerExtensionProvider<(any), ($CompoundTag)>, $IClientExtensionProvider<($CompoundTag), ($EnergyView)> {
static readonly "INSTANCE": $EnergyStorageProvider


public static "values"(): ($EnergyStorageProvider)[]
public static "append"(arg0: $ITooltip$Type, arg1: $Accessor$Type<(any)>, arg2: $IPluginConfig$Type): void
public static "valueOf"(arg0: string): $EnergyStorageProvider
public "getUid"(): $ResourceLocation
public "getDefaultPriority"(): integer
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getClientGroups"(arg0: $Accessor$Type<(any)>, arg1: $List$Type<($ViewGroup$Type<($CompoundTag$Type)>)>): $List<($ClientViewGroup<($EnergyView)>)>
public static "putData"(arg0: $Accessor$Type<(any)>): void
public "getGroups"(arg0: $ServerPlayer$Type, arg1: $ServerLevel$Type, arg2: any, arg3: boolean): $List<($ViewGroup<($CompoundTag)>)>
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
get "required"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnergyStorageProvider$Type = (("instance")) | ($EnergyStorageProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnergyStorageProvider_ = $EnergyStorageProvider$Type;
}}
declare module "packages/snownee/jade/addon/universal/$UniversalPlugin" {
import {$IWailaClientRegistration, $IWailaClientRegistration$Type} from "packages/snownee/jade/api/$IWailaClientRegistration"
import {$IWailaPlugin, $IWailaPlugin$Type} from "packages/snownee/jade/api/$IWailaPlugin"
import {$IWailaCommonRegistration, $IWailaCommonRegistration$Type} from "packages/snownee/jade/api/$IWailaCommonRegistration"

export class $UniversalPlugin implements $IWailaPlugin {

constructor()

public "register"(arg0: $IWailaCommonRegistration$Type): void
public "registerClient"(arg0: $IWailaClientRegistration$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UniversalPlugin$Type = ($UniversalPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UniversalPlugin_ = $UniversalPlugin$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$EntityHealthProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $EntityHealthProvider extends $Enum<($EntityHealthProvider)> implements $IEntityComponentProvider {
static readonly "INSTANCE": $EntityHealthProvider


public static "values"(): ($EntityHealthProvider)[]
public static "valueOf"(arg0: string): $EntityHealthProvider
public "getUid"(): $ResourceLocation
public "getDefaultPriority"(): integer
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
get "required"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityHealthProvider$Type = (("instance")) | ($EntityHealthProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityHealthProvider_ = $EntityHealthProvider$Type;
}}
declare module "packages/snownee/jade/impl/$PriorityStore" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ImmutableList, $ImmutableList$Type} from "packages/com/google/common/collect/$ImmutableList"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $PriorityStore<K, V> {

constructor(arg0: $ToIntFunction$Type<(V)>, arg1: $Function$Type<(V), (K)>)

public "put"(arg0: V): void
public "put"(arg0: V, arg1: integer): void
public "sort"(arg0: $Set$Type<(K)>): void
public "byKey"(arg0: K): integer
public "configurable"(arg0: string, arg1: $Codec$Type<(K)>): void
public "setSortingFunction"(arg0: $BiFunction$Type<($PriorityStore$Type<(K), (V)>), ($Collection$Type<(K)>), ($List$Type<(K)>)>): void
public "byValue"(arg0: V): integer
public "getSortedList"(): $ImmutableList<(K)>
set "sortingFunction"(value: $BiFunction$Type<($PriorityStore$Type<(K), (V)>), ($Collection$Type<(K)>), ($List$Type<(K)>)>)
get "sortedList"(): $ImmutableList<(K)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PriorityStore$Type<K, V> = ($PriorityStore<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PriorityStore_<K, V> = $PriorityStore$Type<(K), (V)>;
}}
declare module "packages/snownee/jade/addon/vanilla/$EntityArmorProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $EntityArmorProvider extends $Enum<($EntityArmorProvider)> implements $IEntityComponentProvider {
static readonly "INSTANCE": $EntityArmorProvider


public static "values"(): ($EntityArmorProvider)[]
public static "valueOf"(arg0: string): $EntityArmorProvider
public "getUid"(): $ResourceLocation
public "getDefaultPriority"(): integer
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
get "required"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityArmorProvider$Type = (("instance")) | ($EntityArmorProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityArmorProvider_ = $EntityArmorProvider$Type;
}}
declare module "packages/snownee/jade/impl/ui/$SubTextElement" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$Element, $Element$Type} from "packages/snownee/jade/api/ui/$Element"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $SubTextElement extends $Element {

constructor(arg0: $Component$Type)

public "getSize"(): $Vec2
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
get "size"(): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SubTextElement$Type = ($SubTextElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SubTextElement_ = $SubTextElement$Type;
}}
declare module "packages/snownee/jade/api/ui/$IDisplayHelper" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Format, $Format$Type} from "packages/java/text/$Format"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export interface $IDisplayHelper {

 "drawBorder"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: integer, arg7: boolean): void
 "drawText"(arg0: $GuiGraphics$Type, arg1: string, arg2: float, arg3: float, arg4: integer): void
 "drawText"(arg0: $GuiGraphics$Type, arg1: $FormattedCharSequence$Type, arg2: float, arg3: float, arg4: integer): void
 "drawText"(arg0: $GuiGraphics$Type, arg1: $FormattedText$Type, arg2: float, arg3: float, arg4: integer): void
 "stripColor"(arg0: $Component$Type): $MutableComponent
 "drawItem"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: $ItemStack$Type, arg4: float, arg5: string): void
 "humanReadableNumber"(arg0: double, arg1: string, arg2: boolean, arg3: $Format$Type): string
 "humanReadableNumber"(arg0: double, arg1: string, arg2: boolean): string
 "drawGradientRect"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: integer, arg6: integer): void
}

export namespace $IDisplayHelper {
function get(): $IDisplayHelper
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IDisplayHelper$Type = ($IDisplayHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IDisplayHelper_ = $IDisplayHelper$Type;
}}
declare module "packages/snownee/jade/util/$DumpGenerator" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $DumpGenerator {

constructor()

public static "generateInfoDump"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DumpGenerator$Type = ($DumpGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DumpGenerator_ = $DumpGenerator$Type;
}}
declare module "packages/snownee/jade/addon/harvest/$ShearsToolHandler" {
import {$SpecialToolHandler, $SpecialToolHandler$Type} from "packages/snownee/jade/addon/harvest/$SpecialToolHandler"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $ShearsToolHandler extends $SpecialToolHandler {
readonly "blocks": $Set<($Block)>

constructor()

public "test"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShearsToolHandler$Type = ($ShearsToolHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShearsToolHandler_ = $ShearsToolHandler$Type;
}}
declare module "packages/snownee/jade/util/$WailaExceptionHandler" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$IJadeProvider, $IJadeProvider$Type} from "packages/snownee/jade/api/$IJadeProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $WailaExceptionHandler {

constructor()

public static "handleErr"(arg0: $Throwable$Type, arg1: $IJadeProvider$Type, arg2: $ITooltip$Type, arg3: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WailaExceptionHandler$Type = ($WailaExceptionHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WailaExceptionHandler_ = $WailaExceptionHandler$Type;
}}
declare module "packages/snownee/jade/api/fluid/$JadeFluidObject" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"

export class $JadeFluidObject {


public "isEmpty"(): boolean
public static "of"(arg0: $Fluid$Type, arg1: long): $JadeFluidObject
public static "of"(arg0: $Fluid$Type, arg1: long, arg2: $CompoundTag$Type): $JadeFluidObject
public static "of"(arg0: $Fluid$Type): $JadeFluidObject
public static "empty"(): $JadeFluidObject
public "getType"(): $Fluid
public "getTag"(): $CompoundTag
public static "bucketVolume"(): long
public "getAmount"(): long
public static "blockVolume"(): long
get "type"(): $Fluid
get "tag"(): $CompoundTag
get "amount"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeFluidObject$Type = ($JadeFluidObject);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeFluidObject_ = $JadeFluidObject$Type;
}}
declare module "packages/snownee/jade/addon/create/$BacktankProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $BacktankProvider extends $Enum<($BacktankProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)> {
static readonly "INSTANCE": $BacktankProvider


public static "values"(): ($BacktankProvider)[]
public static "valueOf"(arg0: string): $BacktankProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BacktankProvider$Type = (("instance")) | ($BacktankProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BacktankProvider_ = $BacktankProvider$Type;
}}
declare module "packages/snownee/jade/gui/config/$BelowOrAboveListEntryTooltipPositioner" {
import {$OptionsList$Entry, $OptionsList$Entry$Type} from "packages/snownee/jade/gui/config/$OptionsList$Entry"
import {$OptionsList, $OptionsList$Type} from "packages/snownee/jade/gui/config/$OptionsList"
import {$Vector2ic, $Vector2ic$Type} from "packages/org/joml/$Vector2ic"
import {$ClientTooltipPositioner, $ClientTooltipPositioner$Type} from "packages/net/minecraft/client/gui/screens/inventory/tooltip/$ClientTooltipPositioner"

export class $BelowOrAboveListEntryTooltipPositioner implements $ClientTooltipPositioner {

constructor(arg0: $OptionsList$Type, arg1: $OptionsList$Entry$Type)

public "positionTooltip"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): $Vector2ic
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BelowOrAboveListEntryTooltipPositioner$Type = ($BelowOrAboveListEntryTooltipPositioner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BelowOrAboveListEntryTooltipPositioner_ = $BelowOrAboveListEntryTooltipPositioner$Type;
}}
declare module "packages/snownee/jade/util/$JsonConfig" {
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $JsonConfig<T> {
static readonly "GSON": $Gson

constructor(arg0: string, arg1: $Codec$Type<(T)>, arg2: $Runnable$Type, arg3: $Supplier$Type<(T)>)
constructor(arg0: string, arg1: $Codec$Type<(T)>, arg2: $Runnable$Type)

public "get"(): T
public "write"(arg0: T, arg1: boolean): void
public "save"(): void
public "getFile"(): $File
public "invalidate"(): void
get "file"(): $File
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonConfig$Type<T> = ($JsonConfig<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonConfig_<T> = $JsonConfig$Type<(T)>;
}}
declare module "packages/snownee/jade/addon/$JadeAddonsClient" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $JadeAddonsClient {

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeAddonsClient$Type = ($JadeAddonsClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeAddonsClient_ = $JadeAddonsClient$Type;
}}
declare module "packages/snownee/jade/util/$FluidTextHelper" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $FluidTextHelper {

constructor()

public static "getUnicodeMillibuckets"(arg0: long, arg1: boolean): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidTextHelper$Type = ($FluidTextHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidTextHelper_ = $FluidTextHelper$Type;
}}
declare module "packages/snownee/jade/impl/$Tooltip$Line" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$IElement$Align, $IElement$Align$Type} from "packages/snownee/jade/api/ui/$IElement$Align"
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $Tooltip$Line {

constructor()

public "getAlignedElements"(arg0: $IElement$Align$Type): $List<($IElement)>
public "getSize"(): $Vec2
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
get "size"(): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Tooltip$Line$Type = ($Tooltip$Line);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Tooltip$Line_ = $Tooltip$Line$Type;
}}
declare module "packages/snownee/jade/api/$BlockAccessor$Builder" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $BlockAccessor$Builder {

 "from"(arg0: $BlockAccessor$Type): $BlockAccessor$Builder
 "build"(): $BlockAccessor
 "level"(arg0: $Level$Type): $BlockAccessor$Builder
 "hit"(arg0: $BlockHitResult$Type): $BlockAccessor$Builder
 "player"(arg0: $Player$Type): $BlockAccessor$Builder
 "serverData"(arg0: $CompoundTag$Type): $BlockAccessor$Builder
 "showDetails"(arg0: boolean): $BlockAccessor$Builder
 "blockState"(arg0: $BlockState$Type): $BlockAccessor$Builder
 "requireVerification"(): $BlockAccessor$Builder
 "fakeBlock"(arg0: $ItemStack$Type): $BlockAccessor$Builder
 "blockEntity"(arg0: $BlockEntity$Type): $BlockAccessor$Builder
 "blockEntity"(arg0: $Supplier$Type<($BlockEntity$Type)>): $BlockAccessor$Builder
 "serverConnected"(arg0: boolean): $BlockAccessor$Builder
}

export namespace $BlockAccessor$Builder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockAccessor$Builder$Type = ($BlockAccessor$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockAccessor$Builder_ = $BlockAccessor$Builder$Type;
}}
declare module "packages/snownee/jade/addon/deep_resonance/$GeneratorPartProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $GeneratorPartProvider extends $Enum<($GeneratorPartProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)> {
static readonly "INSTANCE": $GeneratorPartProvider


public static "values"(): ($GeneratorPartProvider)[]
public static "valueOf"(arg0: string): $GeneratorPartProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeneratorPartProvider$Type = (("instance")) | ($GeneratorPartProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeneratorPartProvider_ = $GeneratorPartProvider$Type;
}}
declare module "packages/snownee/jade/addon/universal/$FluidStorageProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$ClientViewGroup, $ClientViewGroup$Type} from "packages/snownee/jade/api/view/$ClientViewGroup"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IServerExtensionProvider, $IServerExtensionProvider$Type} from "packages/snownee/jade/api/view/$IServerExtensionProvider"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IClientExtensionProvider, $IClientExtensionProvider$Type} from "packages/snownee/jade/api/view/$IClientExtensionProvider"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$FluidView, $FluidView$Type} from "packages/snownee/jade/api/view/$FluidView"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $FluidStorageProvider extends $Enum<($FluidStorageProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)>, $IServerExtensionProvider<(any), ($CompoundTag)>, $IClientExtensionProvider<($CompoundTag), ($FluidView)> {
static readonly "INSTANCE": $FluidStorageProvider


public static "values"(): ($FluidStorageProvider)[]
public static "append"(arg0: $ITooltip$Type, arg1: $Accessor$Type<(any)>, arg2: $IPluginConfig$Type): void
public static "valueOf"(arg0: string): $FluidStorageProvider
public "getUid"(): $ResourceLocation
public "getDefaultPriority"(): integer
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getClientGroups"(arg0: $Accessor$Type<(any)>, arg1: $List$Type<($ViewGroup$Type<($CompoundTag$Type)>)>): $List<($ClientViewGroup<($FluidView)>)>
public static "putData"(arg0: $Accessor$Type<(any)>): void
public "getGroups"(arg0: $ServerPlayer$Type, arg1: $ServerLevel$Type, arg2: any, arg3: boolean): $List<($ViewGroup<($CompoundTag)>)>
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
get "required"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidStorageProvider$Type = (("instance")) | ($FluidStorageProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidStorageProvider_ = $FluidStorageProvider$Type;
}}
declare module "packages/snownee/jade/api/view/$IClientExtensionProvider" {
import {$ClientViewGroup, $ClientViewGroup$Type} from "packages/snownee/jade/api/view/$ClientViewGroup"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$IJadeProvider, $IJadeProvider$Type} from "packages/snownee/jade/api/$IJadeProvider"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"

export interface $IClientExtensionProvider<IN, OUT> extends $IJadeProvider {

 "getClientGroups"(arg0: $Accessor$Type<(any)>, arg1: $List$Type<($ViewGroup$Type<(IN)>)>): $List<($ClientViewGroup<(OUT)>)>
 "getUid"(): $ResourceLocation
 "getDefaultPriority"(): integer
}

export namespace $IClientExtensionProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IClientExtensionProvider$Type<IN, OUT> = ($IClientExtensionProvider<(IN), (OUT)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IClientExtensionProvider_<IN, OUT> = $IClientExtensionProvider$Type<(IN), (OUT)>;
}}
declare module "packages/snownee/jade/gui/config/$KeybindOptionButton" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$OptionButton, $OptionButton$Type} from "packages/snownee/jade/gui/config/$OptionButton"
import {$OptionsList, $OptionsList$Type} from "packages/snownee/jade/gui/config/$OptionsList"

export class $KeybindOptionButton extends $OptionButton {

constructor(arg0: $OptionsList$Type, arg1: $KeyMapping$Type)

public "refresh"(arg0: $KeyMapping$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeybindOptionButton$Type = ($KeybindOptionButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeybindOptionButton_ = $KeybindOptionButton$Type;
}}
declare module "packages/snownee/jade/addon/create/$PlacardProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $PlacardProvider extends $Enum<($PlacardProvider)> implements $IBlockComponentProvider {
static readonly "INSTANCE": $PlacardProvider


public static "values"(): ($PlacardProvider)[]
public static "valueOf"(arg0: string): $PlacardProvider
public "getUid"(): $ResourceLocation
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlacardProvider$Type = (("instance")) | ($PlacardProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlacardProvider_ = $PlacardProvider$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$ChickenEggProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $ChickenEggProvider extends $Enum<($ChickenEggProvider)> implements $IEntityComponentProvider, $IServerDataProvider<($EntityAccessor)> {
static readonly "INSTANCE": $ChickenEggProvider


public static "values"(): ($ChickenEggProvider)[]
public static "valueOf"(arg0: string): $ChickenEggProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $EntityAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChickenEggProvider$Type = (("instance")) | ($ChickenEggProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChickenEggProvider_ = $ChickenEggProvider$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$ItemBERProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $ItemBERProvider extends $Enum<($ItemBERProvider)> implements $IBlockComponentProvider {
static readonly "INSTANCE": $ItemBERProvider


public static "values"(): ($ItemBERProvider)[]
public static "valueOf"(arg0: string): $ItemBERProvider
public "isRequired"(): boolean
public "getUid"(): $ResourceLocation
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "required"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemBERProvider$Type = (("instance")) | ($ItemBERProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemBERProvider_ = $ItemBERProvider$Type;
}}
declare module "packages/snownee/jade/addon/core/$BlockFaceProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $BlockFaceProvider extends $Enum<($BlockFaceProvider)> implements $IBlockComponentProvider {
static readonly "INSTANCE": $BlockFaceProvider


public static "values"(): ($BlockFaceProvider)[]
public static "valueOf"(arg0: string): $BlockFaceProvider
public "getUid"(): $ResourceLocation
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
get "required"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockFaceProvider$Type = (("instance")) | ($BlockFaceProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockFaceProvider_ = $BlockFaceProvider$Type;
}}
declare module "packages/snownee/jade/addon/general/$GeneralPlugin" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$IWailaClientRegistration, $IWailaClientRegistration$Type} from "packages/snownee/jade/api/$IWailaClientRegistration"
import {$IWailaPlugin, $IWailaPlugin$Type} from "packages/snownee/jade/api/$IWailaPlugin"
import {$IWailaCommonRegistration, $IWailaCommonRegistration$Type} from "packages/snownee/jade/api/$IWailaCommonRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"

export class $GeneralPlugin implements $IWailaPlugin {
static readonly "ID": string
static readonly "EQUIPMENT_REQUIREMENT": $ResourceLocation
static "EQUIPMENT_CHECK_PREDICATE": $BiPredicate<($Player), ($TagKey<($Item)>)>
 "requirementTag": $TagKey<($Item)>

constructor()

public "override"(arg0: $HitResult$Type, arg1: $Accessor$Type<(any)>, arg2: $Accessor$Type<(any)>): $Accessor<(any)>
public "registerClient"(arg0: $IWailaClientRegistration$Type): void
public "register"(arg0: $IWailaCommonRegistration$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeneralPlugin$Type = ($GeneralPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeneralPlugin_ = $GeneralPlugin$Type;
}}
declare module "packages/snownee/jade/util/$ModIdentification" {
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PreparableReloadListener$PreparationBarrier, $PreparableReloadListener$PreparationBarrier$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener$PreparationBarrier"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ResourceManagerReloadListener, $ResourceManagerReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManagerReloadListener"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$ProfilerFiller, $ProfilerFiller$Type} from "packages/net/minecraft/util/profiling/$ProfilerFiller"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ModIdentification implements $ResourceManagerReloadListener {
static readonly "INSTANCE": $ModIdentification

constructor()

public static "invalidateCache"(): void
public "onResourceManagerReload"(arg0: $ResourceManager$Type): void
public static "getModName"(arg0: $ItemStack$Type): string
public static "getModName"(arg0: $Entity$Type): string
public static "getModName"(arg0: string): $Optional<(string)>
public static "getModName"(arg0: $ResourceLocation$Type): string
public static "getModName"(arg0: $Block$Type): string
public "reload"(arg0: $PreparableReloadListener$PreparationBarrier$Type, arg1: $ResourceManager$Type, arg2: $ProfilerFiller$Type, arg3: $ProfilerFiller$Type, arg4: $Executor$Type, arg5: $Executor$Type): $CompletableFuture<(void)>
public "getName"(): string
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModIdentification$Type = ($ModIdentification);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModIdentification_ = $ModIdentification$Type;
}}
declare module "packages/snownee/jade/api/ui/$BoxStyle" {
import {$IBoxStyle, $IBoxStyle$Type} from "packages/snownee/jade/api/ui/$IBoxStyle"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $BoxStyle implements $IBoxStyle {
static readonly "DEFAULT": $BoxStyle
 "bgColor": integer
 "borderColor": integer
 "borderWidth": float
 "roundCorner": boolean
 "progressColor": integer
 "progress": float

constructor()

public "tag"(arg0: $ResourceLocation$Type): void
public "borderWidth"(): float
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BoxStyle$Type = ($BoxStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BoxStyle_ = $BoxStyle$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$MobSpawnerProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$BaseSpawner, $BaseSpawner$Type} from "packages/net/minecraft/world/level/$BaseSpawner"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"

export class $MobSpawnerProvider extends $Enum<($MobSpawnerProvider)> implements $IBlockComponentProvider, $IEntityComponentProvider {
static readonly "INSTANCE": $MobSpawnerProvider


public static "values"(): ($MobSpawnerProvider)[]
public static "valueOf"(arg0: string): $MobSpawnerProvider
public "getUid"(): $ResourceLocation
public "getDefaultPriority"(): integer
public static "appendTooltip"(arg0: $ITooltip$Type, arg1: $Accessor$Type<(any)>, arg2: $BaseSpawner$Type, arg3: $BlockPos$Type, arg4: $MutableComponent$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
get "required"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobSpawnerProvider$Type = (("instance")) | ($MobSpawnerProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobSpawnerProvider_ = $MobSpawnerProvider$Type;
}}
declare module "packages/snownee/jade/impl/template/$TemplateComponentProvider" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$IToggleableProvider, $IToggleableProvider$Type} from "packages/snownee/jade/api/$IToggleableProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $TemplateComponentProvider<T extends $Accessor<(any)>> implements $IToggleableProvider {


public "isRequired"(): boolean
public "getUid"(): $ResourceLocation
public "getIcon"(arg0: T, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
public "appendTooltip"(arg0: $ITooltip$Type, arg1: T, arg2: $IPluginConfig$Type): void
public "setIconFunction"(arg0: $BiFunction$Type<(T), ($IElement$Type), ($IElement$Type)>): void
public "setTooltipFunction"(arg0: $BiConsumer$Type<($ITooltip$Type), (T)>): void
get "required"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
set "iconFunction"(value: $BiFunction$Type<(T), ($IElement$Type), ($IElement$Type)>)
set "tooltipFunction"(value: $BiConsumer$Type<($ITooltip$Type), (T)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TemplateComponentProvider$Type<T> = ($TemplateComponentProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TemplateComponentProvider_<T> = $TemplateComponentProvider$Type<(T)>;
}}
declare module "packages/snownee/jade/addon/core/$DistanceProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$DecimalFormat, $DecimalFormat$Type} from "packages/java/text/$DecimalFormat"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"

export class $DistanceProvider extends $Enum<($DistanceProvider)> implements $IBlockComponentProvider, $IEntityComponentProvider {
static readonly "INSTANCE": $DistanceProvider
static readonly "fmt": $DecimalFormat


public static "values"(): ($DistanceProvider)[]
public "append"(arg0: $ITooltip$Type, arg1: $Accessor$Type<(any)>, arg2: $BlockPos$Type, arg3: $IPluginConfig$Type): void
public static "valueOf"(arg0: string): $DistanceProvider
public static "distance"(arg0: $Accessor$Type<(any)>): string
public static "display"(arg0: integer, arg1: integer): $Component
public "isRequired"(): boolean
public static "narrate"(arg0: integer): string
public "getUid"(): $ResourceLocation
public "getDefaultPriority"(): integer
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public static "xyz"(arg0: $ITooltip$Type, arg1: $Vec3i$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "enabledByDefault"(): boolean
get "required"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DistanceProvider$Type = (("instance")) | ($DistanceProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DistanceProvider_ = $DistanceProvider$Type;
}}
declare module "packages/snownee/jade/impl/config/entry/$EnumConfigEntry" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ConfigEntry, $ConfigEntry$Type} from "packages/snownee/jade/impl/config/entry/$ConfigEntry"
import {$OptionsList, $OptionsList$Type} from "packages/snownee/jade/gui/config/$OptionsList"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$OptionValue, $OptionValue$Type} from "packages/snownee/jade/gui/config/value/$OptionValue"

export class $EnumConfigEntry<E extends $Enum<(E)>> extends $ConfigEntry<(E)> {
readonly "id": $ResourceLocation

constructor(arg0: $ResourceLocation$Type, arg1: E)

public "setValue"(arg0: any): void
public "isValidValue"(arg0: any): boolean
public "createUI"(arg0: $OptionsList$Type, arg1: string): $OptionValue<(any)>
set "value"(value: any)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumConfigEntry$Type<E> = ($EnumConfigEntry<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumConfigEntry_<E> = $EnumConfigEntry$Type<(E)>;
}}
declare module "packages/snownee/jade/api/$BlockAccessor" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $BlockAccessor extends $Accessor<($BlockHitResult)> {

 "getPosition"(): $BlockPos
 "getBlock"(): $Block
 "getSide"(): $Direction
 "getBlockEntity"(): $BlockEntity
 "getAccessorType"(): $Class<(any)>
 "getBlockState"(): $BlockState
 "getFakeBlock"(): $ItemStack
 "isFakeBlock"(): boolean
 "getTarget"(): any
 "getLevel"(): $Level
 "getPickedResult"(): $ItemStack
 "showDetails"(): boolean
 "toNetwork"(arg0: $FriendlyByteBuf$Type): void
 "isServerConnected"(): boolean
 "getServerData"(): $CompoundTag
 "getPlayer"(): $Player
 "verifyData"(arg0: $CompoundTag$Type): boolean
 "getHitResult"(): $BlockHitResult
}

export namespace $BlockAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockAccessor$Type = ($BlockAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockAccessor_ = $BlockAccessor$Type;
}}
declare module "packages/snownee/jade/api/$IWailaPlugin" {
import {$IWailaClientRegistration, $IWailaClientRegistration$Type} from "packages/snownee/jade/api/$IWailaClientRegistration"
import {$IWailaCommonRegistration, $IWailaCommonRegistration$Type} from "packages/snownee/jade/api/$IWailaCommonRegistration"

export interface $IWailaPlugin {

 "register"(arg0: $IWailaCommonRegistration$Type): void
 "registerClient"(arg0: $IWailaClientRegistration$Type): void
}

export namespace $IWailaPlugin {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IWailaPlugin$Type = ($IWailaPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IWailaPlugin_ = $IWailaPlugin$Type;
}}
declare module "packages/snownee/jade/addon/create/$GogglesProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $GogglesProvider implements $IBlockComponentProvider {

constructor()

public "getUid"(): $ResourceLocation
public "enabledByDefault"(): boolean
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GogglesProvider$Type = ($GogglesProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GogglesProvider_ = $GogglesProvider$Type;
}}
declare module "packages/snownee/jade/api/$IToggleableProvider" {
import {$IJadeProvider, $IJadeProvider$Type} from "packages/snownee/jade/api/$IJadeProvider"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $IToggleableProvider extends $IJadeProvider {

 "isRequired"(): boolean
 "enabledByDefault"(): boolean
 "getUid"(): $ResourceLocation
 "getDefaultPriority"(): integer

(): boolean
}

export namespace $IToggleableProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IToggleableProvider$Type = ($IToggleableProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IToggleableProvider_ = $IToggleableProvider$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$FurnaceProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $FurnaceProvider extends $Enum<($FurnaceProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)> {
static readonly "INSTANCE": $FurnaceProvider


public static "values"(): ($FurnaceProvider)[]
public static "valueOf"(arg0: string): $FurnaceProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FurnaceProvider$Type = (("instance")) | ($FurnaceProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FurnaceProvider_ = $FurnaceProvider$Type;
}}
declare module "packages/snownee/jade/addon/harvest/$SpecialToolHandler" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$ToolHandler, $ToolHandler$Type} from "packages/snownee/jade/addon/harvest/$ToolHandler"

export class $SpecialToolHandler implements $ToolHandler {
readonly "blocks": $Set<($Block)>

constructor(arg0: string, arg1: $ItemStack$Type)

public "getName"(): string
public "test"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): $ItemStack
public "getTools"(): $List<($ItemStack)>
get "name"(): string
get "tools"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpecialToolHandler$Type = ($SpecialToolHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpecialToolHandler_ = $SpecialToolHandler$Type;
}}
declare module "packages/snownee/jade/addon/create/$BlazeBurnerProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $BlazeBurnerProvider extends $Enum<($BlazeBurnerProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)> {
static readonly "INSTANCE": $BlazeBurnerProvider


public static "values"(): ($BlazeBurnerProvider)[]
public static "valueOf"(arg0: string): $BlazeBurnerProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlazeBurnerProvider$Type = (("instance")) | ($BlazeBurnerProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlazeBurnerProvider_ = $BlazeBurnerProvider$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$EnchantmentPowerProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $EnchantmentPowerProvider extends $Enum<($EnchantmentPowerProvider)> implements $IBlockComponentProvider {
static readonly "INSTANCE": $EnchantmentPowerProvider


public static "values"(): ($EnchantmentPowerProvider)[]
public static "valueOf"(arg0: string): $EnchantmentPowerProvider
public "getUid"(): $ResourceLocation
public "getDefaultPriority"(): integer
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
get "required"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnchantmentPowerProvider$Type = (("instance")) | ($EnchantmentPowerProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnchantmentPowerProvider_ = $EnchantmentPowerProvider$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$BlockDisplayProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $BlockDisplayProvider extends $Enum<($BlockDisplayProvider)> implements $IEntityComponentProvider {
static readonly "INSTANCE": $BlockDisplayProvider


public static "values"(): ($BlockDisplayProvider)[]
public static "valueOf"(arg0: string): $BlockDisplayProvider
public "isRequired"(): boolean
public "getUid"(): $ResourceLocation
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "required"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockDisplayProvider$Type = (("instance")) | ($BlockDisplayProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockDisplayProvider_ = $BlockDisplayProvider$Type;
}}
declare module "packages/snownee/jade/addon/universal/$ItemCollector" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ItemIterator, $ItemIterator$Type} from "packages/snownee/jade/addon/universal/$ItemIterator"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"

export class $ItemCollector<T> {
static readonly "MAX_SIZE": integer
static readonly "EMPTY": $ItemCollector<(any)>
 "version": long
 "lastTimeFinished": long
 "mergedResult": $List<($ViewGroup<($ItemStack)>)>

constructor(arg0: $ItemIterator$Type<(T)>)

public "update"(arg0: any, arg1: long): $List<($ViewGroup<($ItemStack)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemCollector$Type<T> = ($ItemCollector<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemCollector_<T> = $ItemCollector$Type<(T)>;
}}
declare module "packages/snownee/jade/api/view/$IServerExtensionProvider" {
import {$IJadeProvider, $IJadeProvider$Type} from "packages/snownee/jade/api/$IJadeProvider"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"

export interface $IServerExtensionProvider<IN, OUT> extends $IJadeProvider {

 "getGroups"(arg0: $ServerPlayer$Type, arg1: $ServerLevel$Type, arg2: IN, arg3: boolean): $List<($ViewGroup<(OUT)>)>
 "getUid"(): $ResourceLocation
 "getDefaultPriority"(): integer
}

export namespace $IServerExtensionProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IServerExtensionProvider$Type<IN, OUT> = ($IServerExtensionProvider<(IN), (OUT)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IServerExtensionProvider_<IN, OUT> = $IServerExtensionProvider$Type<(IN), (OUT)>;
}}
declare module "packages/snownee/jade/network/$RequestTilePacket" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"

export class $RequestTilePacket {
 "accessor": $BlockAccessor
 "buffer": $FriendlyByteBuf

constructor(arg0: $BlockAccessor$Type)
constructor(arg0: $FriendlyByteBuf$Type)

public static "write"(arg0: $RequestTilePacket$Type, arg1: $FriendlyByteBuf$Type): void
public static "read"(arg0: $FriendlyByteBuf$Type): $RequestTilePacket
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RequestTilePacket$Type = ($RequestTilePacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RequestTilePacket_ = $RequestTilePacket$Type;
}}
declare module "packages/snownee/jade/impl/ui/$FluidStackElement" {
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$Element, $Element$Type} from "packages/snownee/jade/api/ui/$Element"
import {$JadeFluidObject, $JadeFluidObject$Type} from "packages/snownee/jade/api/fluid/$JadeFluidObject"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $FluidStackElement extends $Element {

constructor(arg0: $JadeFluidObject$Type)

public "getMessage"(): string
public "getSize"(): $Vec2
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
get "message"(): string
get "size"(): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidStackElement$Type = ($FluidStackElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidStackElement_ = $FluidStackElement$Type;
}}
declare module "packages/snownee/jade/gui/config/$OptionsNav$Entry" {
import {$ObjectSelectionList$Entry, $ObjectSelectionList$Entry$Type} from "packages/net/minecraft/client/gui/components/$ObjectSelectionList$Entry"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$OptionsNav, $OptionsNav$Type} from "packages/snownee/jade/gui/config/$OptionsNav"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$OptionsList$Title, $OptionsList$Title$Type} from "packages/snownee/jade/gui/config/$OptionsList$Title"

export class $OptionsNav$Entry extends $ObjectSelectionList$Entry<($OptionsNav$Entry)> {

constructor(arg0: $OptionsNav$Type, arg1: $OptionsList$Title$Type)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: boolean, arg9: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "getNarration"(): $Component
get "narration"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionsNav$Entry$Type = ($OptionsNav$Entry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionsNav$Entry_ = $OptionsNav$Entry$Type;
}}
declare module "packages/snownee/jade/addon/universal/$ItemStorageProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$ClientViewGroup, $ClientViewGroup$Type} from "packages/snownee/jade/api/view/$ClientViewGroup"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$ItemCollector, $ItemCollector$Type} from "packages/snownee/jade/addon/universal/$ItemCollector"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IServerExtensionProvider, $IServerExtensionProvider$Type} from "packages/snownee/jade/api/view/$IServerExtensionProvider"
import {$Cache, $Cache$Type} from "packages/com/google/common/cache/$Cache"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IClientExtensionProvider, $IClientExtensionProvider$Type} from "packages/snownee/jade/api/view/$IClientExtensionProvider"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ItemView, $ItemView$Type} from "packages/snownee/jade/api/view/$ItemView"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $ItemStorageProvider extends $Enum<($ItemStorageProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)>, $IServerExtensionProvider<(any), ($ItemStack)>, $IClientExtensionProvider<($ItemStack), ($ItemView)> {
static readonly "INSTANCE": $ItemStorageProvider
readonly "targetCache": $Cache<(any), ($ItemCollector<(any)>)>
readonly "containerCache": $Cache<(any), ($ItemCollector<(any)>)>


public static "values"(): ($ItemStorageProvider)[]
public static "append"(arg0: $ITooltip$Type, arg1: $Accessor$Type<(any)>, arg2: $IPluginConfig$Type): void
public static "valueOf"(arg0: string): $ItemStorageProvider
public "getUid"(): $ResourceLocation
public "getDefaultPriority"(): integer
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getClientGroups"(arg0: $Accessor$Type<(any)>, arg1: $List$Type<($ViewGroup$Type<($ItemStack$Type)>)>): $List<($ClientViewGroup<($ItemView)>)>
public static "putData"(arg0: $Accessor$Type<(any)>): void
public "getGroups"(arg0: $ServerPlayer$Type, arg1: $ServerLevel$Type, arg2: any, arg3: boolean): $List<($ViewGroup<($ItemStack)>)>
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
get "required"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemStorageProvider$Type = (("instance")) | ($ItemStorageProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemStorageProvider_ = $ItemStorageProvider$Type;
}}
declare module "packages/snownee/jade/impl/config/entry/$FloatConfigEntry" {
import {$ConfigEntry, $ConfigEntry$Type} from "packages/snownee/jade/impl/config/entry/$ConfigEntry"
import {$OptionsList, $OptionsList$Type} from "packages/snownee/jade/gui/config/$OptionsList"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$OptionValue, $OptionValue$Type} from "packages/snownee/jade/gui/config/value/$OptionValue"

export class $FloatConfigEntry extends $ConfigEntry<(float)> {
readonly "id": $ResourceLocation

constructor(arg0: $ResourceLocation$Type, arg1: float, arg2: float, arg3: float, arg4: boolean)

public "setValue"(arg0: any): void
public "isValidValue"(arg0: any): boolean
public "createUI"(arg0: $OptionsList$Type, arg1: string): $OptionValue<(any)>
set "value"(value: any)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FloatConfigEntry$Type = ($FloatConfigEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FloatConfigEntry_ = $FloatConfigEntry$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$PlayerHeadProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $PlayerHeadProvider extends $Enum<($PlayerHeadProvider)> implements $IBlockComponentProvider {
static readonly "INSTANCE": $PlayerHeadProvider


public static "values"(): ($PlayerHeadProvider)[]
public static "valueOf"(arg0: string): $PlayerHeadProvider
public "getUid"(): $ResourceLocation
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerHeadProvider$Type = (("instance")) | ($PlayerHeadProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerHeadProvider_ = $PlayerHeadProvider$Type;
}}
declare module "packages/snownee/jade/addon/universal/$ProgressProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $ProgressProvider extends $Enum<($ProgressProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)> {
static readonly "INSTANCE": $ProgressProvider


public static "values"(): ($ProgressProvider)[]
public static "append"(arg0: $ITooltip$Type, arg1: $Accessor$Type<(any)>, arg2: $IPluginConfig$Type): void
public static "valueOf"(arg0: string): $ProgressProvider
public "isRequired"(): boolean
public "getUid"(): $ResourceLocation
public "getDefaultPriority"(): integer
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public static "putData"(arg0: $Accessor$Type<(any)>): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "enabledByDefault"(): boolean
get "required"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProgressProvider$Type = (("instance")) | ($ProgressProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProgressProvider_ = $ProgressProvider$Type;
}}
declare module "packages/snownee/jade/gui/config/value/$InputOptionValue" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$OptionValue, $OptionValue$Type} from "packages/snownee/jade/gui/config/value/$OptionValue"

export class $InputOptionValue<T> extends $OptionValue<(T)> {
static readonly "INTEGER": $Predicate<(string)>
static readonly "FLOAT": $Predicate<(string)>
 "serverFeature": boolean

constructor(arg0: $Runnable$Type, arg1: string, arg2: T, arg3: $Consumer$Type<(T)>, arg4: $Predicate$Type<(string)>)

public "setValue"(arg0: T): void
public "isValidValue"(): boolean
set "value"(value: T)
get "validValue"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputOptionValue$Type<T> = ($InputOptionValue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputOptionValue_<T> = $InputOptionValue$Type<(T)>;
}}
declare module "packages/snownee/jade/impl/config/$PluginConfig" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$File, $File$Type} from "packages/java/io/$File"
import {$ConfigEntry, $ConfigEntry$Type} from "packages/snownee/jade/impl/config/entry/$ConfigEntry"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IToggleableProvider, $IToggleableProvider$Type} from "packages/snownee/jade/api/$IToggleableProvider"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $PluginConfig implements $IPluginConfig {
static readonly "INSTANCE": $PluginConfig
static readonly "CLIENT_FILE": string
static readonly "SERVER_FILE": string


public "get"(arg0: $IToggleableProvider$Type): boolean
public "get"(arg0: $ResourceLocation$Type): boolean
public "getInt"(arg0: $ResourceLocation$Type): integer
public "getFloat"(arg0: $ResourceLocation$Type): float
public "set"(arg0: $ResourceLocation$Type, arg1: any): boolean
public "containsKey"(arg0: $ResourceLocation$Type): boolean
public "save"(): void
public "getFile"(): $File
public "getEntry"(arg0: $ResourceLocation$Type): $ConfigEntry<(any)>
public "getString"(arg0: $ResourceLocation$Type): string
public "getKeys"(arg0: string): $Set<($ResourceLocation)>
public "getKeys"(): $Set<($ResourceLocation)>
public "reload"(): void
public "getEnum"<T extends $Enum<(T)>>(arg0: $ResourceLocation$Type): T
public "addConfig"(arg0: $ConfigEntry$Type<(any)>): void
public "getServerConfigs"(): string
public "applyServerConfigs"(arg0: $JsonObject$Type): void
public "addConfigListener"(arg0: $ResourceLocation$Type, arg1: $Consumer$Type<($ResourceLocation$Type)>): void
public "getNamespaces"(): $List<(string)>
get "file"(): $File
get "keys"(): $Set<($ResourceLocation)>
get "serverConfigs"(): string
get "namespaces"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PluginConfig$Type = ($PluginConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PluginConfig_ = $PluginConfig$Type;
}}
declare module "packages/snownee/jade/overlay/$TooltipRenderer" {
import {$Tooltip, $Tooltip$Type} from "packages/snownee/jade/impl/$Tooltip"
import {$Theme, $Theme$Type} from "packages/snownee/jade/api/theme/$Theme"
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"
import {$ITooltipRenderer, $ITooltipRenderer$Type} from "packages/snownee/jade/api/ui/$ITooltipRenderer"

export class $TooltipRenderer implements $ITooltipRenderer {

constructor(arg0: $Tooltip$Type, arg1: boolean)

public "getSize"(): $Vec2
public "setSize"(arg0: $Vec2$Type): void
public "getPosition"(): $Rect2i
public "getRealRect"(): $Rect2i
public "getRealScale"(): float
public "recalculateSize"(): void
public "draw"(arg0: $GuiGraphics$Type): void
public "setPadding"(arg0: integer, arg1: integer): void
public "getPadding"(arg0: integer): integer
public "getIcon"(): $IElement
public "getTooltip"(): $Tooltip
public "setPaddingFromTheme"(arg0: $Theme$Type): void
public "setIcon"(arg0: $IElement$Type): void
public "hasIcon"(): boolean
public "recalculateRealRect"(): void
/**
 * 
 * @deprecated
 */
public "setPadding"(arg0: integer, arg1: float): void
get "size"(): $Vec2
set "size"(value: $Vec2$Type)
get "position"(): $Rect2i
get "realRect"(): $Rect2i
get "realScale"(): float
get "icon"(): $IElement
get "tooltip"(): $Tooltip
set "paddingFromTheme"(value: $Theme$Type)
set "icon"(value: $IElement$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TooltipRenderer$Type = ($TooltipRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TooltipRenderer_ = $TooltipRenderer$Type;
}}
declare module "packages/snownee/jade/api/callback/$JadeAfterRenderCallback" {
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export interface $JadeAfterRenderCallback {

 "afterRender"(arg0: $ITooltip$Type, arg1: $Rect2i$Type, arg2: $GuiGraphics$Type, arg3: $Accessor$Type<(any)>): void

(arg0: $ITooltip$Type, arg1: $Rect2i$Type, arg2: $GuiGraphics$Type, arg3: $Accessor$Type<(any)>): void
}

export namespace $JadeAfterRenderCallback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeAfterRenderCallback$Type = ($JadeAfterRenderCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeAfterRenderCallback_ = $JadeAfterRenderCallback$Type;
}}
declare module "packages/snownee/jade/api/$IWailaCommonRegistration" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$IServerExtensionProvider, $IServerExtensionProvider$Type} from "packages/snownee/jade/api/view/$IServerExtensionProvider"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export interface $IWailaCommonRegistration {

 "registerItemStorage"<T>(arg0: $IServerExtensionProvider$Type<(T), ($ItemStack$Type)>, arg1: $Class$Type<(any)>): void
 "registerFluidStorage"<T>(arg0: $IServerExtensionProvider$Type<(T), ($CompoundTag$Type)>, arg1: $Class$Type<(any)>): void
 "registerEntityDataProvider"(arg0: $IServerDataProvider$Type<($EntityAccessor$Type)>, arg1: $Class$Type<(any)>): void
 "registerBlockDataProvider"(arg0: $IServerDataProvider$Type<($BlockAccessor$Type)>, arg1: $Class$Type<(any)>): void
 "registerEnergyStorage"<T>(arg0: $IServerExtensionProvider$Type<(T), ($CompoundTag$Type)>, arg1: $Class$Type<(any)>): void
 "registerProgress"<T>(arg0: $IServerExtensionProvider$Type<(T), ($CompoundTag$Type)>, arg1: $Class$Type<(any)>): void
}

export namespace $IWailaCommonRegistration {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IWailaCommonRegistration$Type = ($IWailaCommonRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IWailaCommonRegistration_ = $IWailaCommonRegistration$Type;
}}
declare module "packages/snownee/jade/$JadeCommonConfig" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"

export class $JadeCommonConfig {
static "bypassLockedContainer": boolean

constructor()

public static "shouldShowCustomName"(arg0: $BlockEntity$Type): boolean
public static "shouldIgnoreTE"(arg0: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeCommonConfig$Type = ($JadeCommonConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeCommonConfig_ = $JadeCommonConfig$Type;
}}
declare module "packages/snownee/jade/impl/$BlockAccessorImpl" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$AccessorImpl, $AccessorImpl$Type} from "packages/snownee/jade/api/$AccessorImpl"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $BlockAccessorImpl extends $AccessorImpl<($BlockHitResult)> implements $BlockAccessor {


public "getTarget"(): any
public "getPosition"(): $BlockPos
public "getBlock"(): $Block
public static "fromNetwork"(arg0: $FriendlyByteBuf$Type, arg1: $ServerPlayer$Type): $BlockAccessor
public "getPickedResult"(): $ItemStack
public "getSide"(): $Direction
public "getBlockEntity"(): $BlockEntity
public static "handleRequest"(arg0: $FriendlyByteBuf$Type, arg1: $ServerPlayer$Type, arg2: $Consumer$Type<($Runnable$Type)>, arg3: $Consumer$Type<($CompoundTag$Type)>): void
public "toNetwork"(arg0: $FriendlyByteBuf$Type): void
public "getBlockState"(): $BlockState
public "getFakeBlock"(): $ItemStack
public "isFakeBlock"(): boolean
public "setFakeBlock"(arg0: $ItemStack$Type): void
public "verifyData"(arg0: $CompoundTag$Type): boolean
public "getAccessorType"(): $Class<(any)>
get "target"(): any
get "position"(): $BlockPos
get "block"(): $Block
get "pickedResult"(): $ItemStack
get "side"(): $Direction
get "blockEntity"(): $BlockEntity
get "blockState"(): $BlockState
get "fakeBlock"(): $ItemStack
get "fakeBlock"(): boolean
set "fakeBlock"(value: $ItemStack$Type)
get "accessorType"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockAccessorImpl$Type = ($BlockAccessorImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockAccessorImpl_ = $BlockAccessorImpl$Type;
}}
declare module "packages/snownee/jade/impl/ui/$ProgressArrowElement" {
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$Element, $Element$Type} from "packages/snownee/jade/api/ui/$Element"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ProgressArrowElement extends $Element {

constructor(arg0: float)

public "getSize"(): $Vec2
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
get "size"(): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProgressArrowElement$Type = ($ProgressArrowElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProgressArrowElement_ = $ProgressArrowElement$Type;
}}
declare module "packages/snownee/jade/api/config/$IWailaConfig$IConfigOverlay" {
import {$Theme, $Theme$Type} from "packages/snownee/jade/api/theme/$Theme"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IWailaConfig$IconMode, $IWailaConfig$IconMode$Type} from "packages/snownee/jade/api/config/$IWailaConfig$IconMode"

export interface $IWailaConfig$IConfigOverlay {

 "setSquare"(arg0: boolean): void
 "getTheme"(): $Theme
 "getOverlayPosY"(): float
 "getAnchorY"(): float
 "setFlipMainHand"(arg0: boolean): void
 "getFlipMainHand"(): boolean
 "getOverlayPosX"(): float
 "setOverlayPosY"(arg0: float): void
 "setAnchorY"(arg0: float): void
 "tryFlip"(arg0: float): float
 "setAnchorX"(arg0: float): void
 "setOverlayScale"(arg0: float): void
 "setOverlayPosX"(arg0: float): void
 "getAnchorX"(): float
 "getOverlayScale"(): float
 "getIconMode"(): $IWailaConfig$IconMode
 "setIconMode"(arg0: $IWailaConfig$IconMode$Type): void
 "shouldShowIcon"(): boolean
 "getAnimation"(): boolean
 "applyTheme"(arg0: $ResourceLocation$Type): void
 "getSquare"(): boolean
 "getThemes"(): $Collection<($Theme)>
 "getAlpha"(): float
 "setAlpha"(arg0: float): void
 "getDisappearingDelay"(): float
 "setDisappearingDelay"(arg0: float): void
 "getAutoScaleThreshold"(): float
 "setAnimation"(arg0: boolean): void
}

export namespace $IWailaConfig$IConfigOverlay {
function applyAlpha(arg0: integer, arg1: float): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IWailaConfig$IConfigOverlay$Type = ($IWailaConfig$IConfigOverlay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IWailaConfig$IConfigOverlay_ = $IWailaConfig$IConfigOverlay$Type;
}}
declare module "packages/snownee/jade/impl/template/$TemplateBlockServerDataProvider" {
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$TemplateServerDataProvider, $TemplateServerDataProvider$Type} from "packages/snownee/jade/impl/template/$TemplateServerDataProvider"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $TemplateBlockServerDataProvider extends $TemplateServerDataProvider<($BlockAccessor)> {

constructor(arg0: $ResourceLocation$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TemplateBlockServerDataProvider$Type = ($TemplateBlockServerDataProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TemplateBlockServerDataProvider_ = $TemplateBlockServerDataProvider$Type;
}}
declare module "packages/snownee/jade/impl/$WailaClientRegistration" {
import {$JadeItemModNameCallback, $JadeItemModNameCallback$Type} from "packages/snownee/jade/api/callback/$JadeItemModNameCallback"
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$JsonConfig, $JsonConfig$Type} from "packages/snownee/jade/util/$JsonConfig"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$JadeAfterRenderCallback, $JadeAfterRenderCallback$Type} from "packages/snownee/jade/api/callback/$JadeAfterRenderCallback"
import {$ImmutableSet, $ImmutableSet$Type} from "packages/com/google/common/collect/$ImmutableSet"
import {$TargetBlocklist, $TargetBlocklist$Type} from "packages/snownee/jade/api/config/$TargetBlocklist"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$BlockAccessor$Builder, $BlockAccessor$Builder$Type} from "packages/snownee/jade/api/$BlockAccessor$Builder"
import {$HierarchyLookup, $HierarchyLookup$Type} from "packages/snownee/jade/impl/$HierarchyLookup"
import {$JadeRayTraceCallback, $JadeRayTraceCallback$Type} from "packages/snownee/jade/api/callback/$JadeRayTraceCallback"
import {$List, $List$Type} from "packages/java/util/$List"
import {$JadeTooltipCollectedCallback, $JadeTooltipCollectedCallback$Type} from "packages/snownee/jade/api/callback/$JadeTooltipCollectedCallback"
import {$CallbackContainer, $CallbackContainer$Type} from "packages/snownee/jade/impl/$CallbackContainer"
import {$JadeRenderBackgroundCallback, $JadeRenderBackgroundCallback$Type} from "packages/snownee/jade/api/callback/$JadeRenderBackgroundCallback"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$CustomEnchantPower, $CustomEnchantPower$Type} from "packages/snownee/jade/api/platform/$CustomEnchantPower"
import {$ItemView, $ItemView$Type} from "packages/snownee/jade/api/view/$ItemView"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$Accessor$ClientHandler, $Accessor$ClientHandler$Type} from "packages/snownee/jade/api/$Accessor$ClientHandler"
import {$EntityAccessor$Builder, $EntityAccessor$Builder$Type} from "packages/snownee/jade/api/$EntityAccessor$Builder"
import {$JadeBeforeRenderCallback, $JadeBeforeRenderCallback$Type} from "packages/snownee/jade/api/callback/$JadeBeforeRenderCallback"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ProgressView, $ProgressView$Type} from "packages/snownee/jade/api/view/$ProgressView"
import {$IClientExtensionProvider, $IClientExtensionProvider$Type} from "packages/snownee/jade/api/view/$IClientExtensionProvider"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IWailaClientRegistration, $IWailaClientRegistration$Type} from "packages/snownee/jade/api/$IWailaClientRegistration"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$EnergyView, $EnergyView$Type} from "packages/snownee/jade/api/view/$EnergyView"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$FluidView, $FluidView$Type} from "packages/snownee/jade/api/view/$FluidView"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $WailaClientRegistration implements $IWailaClientRegistration {
static readonly "INSTANCE": $WailaClientRegistration
readonly "blockIconProviders": $HierarchyLookup<($IBlockComponentProvider)>
readonly "blockComponentProviders": $HierarchyLookup<($IBlockComponentProvider)>
readonly "entityIconProviders": $HierarchyLookup<($IEntityComponentProvider)>
readonly "entityComponentProviders": $HierarchyLookup<($IEntityComponentProvider)>
readonly "hideBlocks": $Set<($Block)>
 "hideBlocksReloadable": $ImmutableSet<($Block)>
readonly "hideEntities": $Set<($EntityType<(any)>)>
 "hideEntitiesReloadable": $ImmutableSet<($EntityType<(any)>)>
readonly "pickBlocks": $Set<($Block)>
readonly "pickEntities": $Set<($EntityType<(any)>)>
readonly "afterRenderCallback": $CallbackContainer<($JadeAfterRenderCallback)>
readonly "beforeRenderCallback": $CallbackContainer<($JadeBeforeRenderCallback)>
readonly "rayTraceCallback": $CallbackContainer<($JadeRayTraceCallback)>
readonly "tooltipCollectedCallback": $CallbackContainer<($JadeTooltipCollectedCallback)>
readonly "itemModNameCallback": $CallbackContainer<($JadeItemModNameCallback)>
readonly "renderBackgroundCallback": $CallbackContainer<($JadeRenderBackgroundCallback)>
readonly "customEnchantPowers": $Map<($Block), ($CustomEnchantPower)>
readonly "itemStorageProviders": $Map<($ResourceLocation), ($IClientExtensionProvider<($ItemStack), ($ItemView)>)>
readonly "fluidStorageProviders": $Map<($ResourceLocation), ($IClientExtensionProvider<($CompoundTag), ($FluidView)>)>
readonly "energyStorageProviders": $Map<($ResourceLocation), ($IClientExtensionProvider<($CompoundTag), ($EnergyView)>)>
readonly "progressProviders": $Map<($ResourceLocation), ($IClientExtensionProvider<($CompoundTag), ($ProgressView)>)>
readonly "clientFeatures": $Set<($ResourceLocation)>
readonly "accessorHandlers": $Map<($Class<($Accessor<(any)>)>), ($Accessor$ClientHandler<($Accessor<(any)>)>)>


public static "instance"(): $WailaClientRegistration
public "loadComplete"(): void
public "addAfterRenderCallback"(arg0: integer, arg1: $JadeAfterRenderCallback$Type): void
public "addBeforeRenderCallback"(arg0: integer, arg1: $JadeBeforeRenderCallback$Type): void
public "addTooltipCollectedCallback"(arg0: integer, arg1: $JadeTooltipCollectedCallback$Type): void
public "addRenderBackgroundCallback"(arg0: integer, arg1: $JadeRenderBackgroundCallback$Type): void
public "registerItemStorageClient"(arg0: $IClientExtensionProvider$Type<($ItemStack$Type), ($ItemView$Type)>): void
public "registerProgressClient"(arg0: $IClientExtensionProvider$Type<($CompoundTag$Type), ($ProgressView$Type)>): void
public "registerBlockComponent"(arg0: $IBlockComponentProvider$Type, arg1: $Class$Type<(any)>): void
public "addRayTraceCallback"(arg0: integer, arg1: $JadeRayTraceCallback$Type): void
public "registerFluidStorageClient"(arg0: $IClientExtensionProvider$Type<($CompoundTag$Type), ($FluidView$Type)>): void
public "registerEnergyStorageClient"(arg0: $IClientExtensionProvider$Type<($CompoundTag$Type), ($EnergyView$Type)>): void
public "addItemModNameCallback"(arg0: integer, arg1: $JadeItemModNameCallback$Type): void
public "registerEntityComponent"(arg0: $IEntityComponentProvider$Type, arg1: $Class$Type<(any)>): void
public "addConfig"(arg0: $ResourceLocation$Type, arg1: boolean): void
public "addConfig"(arg0: $ResourceLocation$Type, arg1: $Enum$Type<(any)>): void
public "addConfig"(arg0: $ResourceLocation$Type, arg1: string, arg2: $Predicate$Type<(string)>): void
public "addConfig"(arg0: $ResourceLocation$Type, arg1: integer, arg2: integer, arg3: integer, arg4: boolean): void
public "addConfig"(arg0: $ResourceLocation$Type, arg1: float, arg2: float, arg3: float, arg4: boolean): void
public "reloadBlocklists"(): void
public "shouldHide"(arg0: $BlockState$Type): boolean
public "shouldHide"(arg0: $Entity$Type): boolean
public "shouldPick"(arg0: $Entity$Type): boolean
public "shouldPick"(arg0: $BlockState$Type): boolean
public "blockAccessor"(): $BlockAccessor$Builder
public "addConfigListener"(arg0: $ResourceLocation$Type, arg1: $Consumer$Type<($ResourceLocation$Type)>): void
public "hideTarget"(arg0: $EntityType$Type<(any)>): void
public "hideTarget"(arg0: $Block$Type): void
public "usePickedResult"(arg0: $Block$Type): void
public "usePickedResult"(arg0: $EntityType$Type<(any)>): void
public "entityAccessor"(): $EntityAccessor$Builder
public "registerBlockIcon"(arg0: $IBlockComponentProvider$Type, arg1: $Class$Type<(any)>): void
public "registerEntityIcon"(arg0: $IEntityComponentProvider$Type, arg1: $Class$Type<(any)>): void
public "isServerConnected"(): boolean
public "maybeLowVisionUser"(): boolean
public "setServerData"(arg0: $CompoundTag$Type): void
public "getAccessorHandler"(arg0: $Class$Type<(any)>): $Accessor$ClientHandler<($Accessor<(any)>)>
public "getServerData"(): $CompoundTag
public "getBlockCamouflage"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type): $ItemStack
public "registerAccessorHandler"<T extends $Accessor<(any)>>(arg0: $Class$Type<(T)>, arg1: $Accessor$ClientHandler$Type<(T)>): void
public "registerCustomEnchantPower"(arg0: $Block$Type, arg1: $CustomEnchantPower$Type): void
public "markAsServerFeature"(arg0: $ResourceLocation$Type): void
public "markAsClientFeature"(arg0: $ResourceLocation$Type): void
public "createPluginConfigScreen"(arg0: $Screen$Type, arg1: string): $Screen
public static "createBlockBlocklist"(): $JsonConfig<($TargetBlocklist)>
public "getBlockIconProviders"(arg0: $Block$Type, arg1: $Predicate$Type<($IBlockComponentProvider$Type)>): $List<($IBlockComponentProvider)>
public "getEntityIconProviders"(arg0: $Entity$Type, arg1: $Predicate$Type<($IEntityComponentProvider$Type)>): $List<($IEntityComponentProvider)>
public static "createEntityBlocklist"(): $JsonConfig<($TargetBlocklist)>
public "getEntityProviders"(arg0: $Entity$Type, arg1: $Predicate$Type<($IEntityComponentProvider$Type)>): $List<($IEntityComponentProvider)>
public "getBlockProviders"(arg0: $Block$Type, arg1: $Predicate$Type<($IBlockComponentProvider$Type)>): $List<($IBlockComponentProvider)>
public "isClientFeature"(arg0: $ResourceLocation$Type): boolean
public "isShowDetailsPressed"(): boolean
public "addAfterRenderCallback"(arg0: $JadeAfterRenderCallback$Type): void
public "addBeforeRenderCallback"(arg0: $JadeBeforeRenderCallback$Type): void
public "addTooltipCollectedCallback"(arg0: $JadeTooltipCollectedCallback$Type): void
public "addRenderBackgroundCallback"(arg0: $JadeRenderBackgroundCallback$Type): void
public "addRayTraceCallback"(arg0: $JadeRayTraceCallback$Type): void
public "addItemModNameCallback"(arg0: $JadeItemModNameCallback$Type): void
get "serverConnected"(): boolean
set "serverData"(value: $CompoundTag$Type)
get "serverData"(): $CompoundTag
get "showDetailsPressed"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WailaClientRegistration$Type = ($WailaClientRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WailaClientRegistration_ = $WailaClientRegistration$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$VillagerProfessionProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $VillagerProfessionProvider extends $Enum<($VillagerProfessionProvider)> implements $IEntityComponentProvider {
static readonly "INSTANCE": $VillagerProfessionProvider


public static "values"(): ($VillagerProfessionProvider)[]
public static "valueOf"(arg0: string): $VillagerProfessionProvider
public "getUid"(): $ResourceLocation
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VillagerProfessionProvider$Type = (("instance")) | ($VillagerProfessionProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VillagerProfessionProvider_ = $VillagerProfessionProvider$Type;
}}
declare module "packages/snownee/jade/impl/ui/$ArmorElement" {
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$Element, $Element$Type} from "packages/snownee/jade/api/ui/$Element"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ArmorElement extends $Element {

constructor(arg0: float)

public "getMessage"(): string
public "getSize"(): $Vec2
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
get "message"(): string
get "size"(): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArmorElement$Type = ($ArmorElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArmorElement_ = $ArmorElement$Type;
}}
declare module "packages/snownee/jade/api/$Accessor" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $Accessor<T extends $HitResult> {

 "getTarget"(): any
 "getLevel"(): $Level
 "getPickedResult"(): $ItemStack
 "getAccessorType"(): $Class<(any)>
 "showDetails"(): boolean
 "toNetwork"(arg0: $FriendlyByteBuf$Type): void
 "isServerConnected"(): boolean
 "getServerData"(): $CompoundTag
 "getPlayer"(): $Player
 "verifyData"(arg0: $CompoundTag$Type): boolean
 "getHitResult"(): T
}

export namespace $Accessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Accessor$Type<T> = ($Accessor<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Accessor_<T> = $Accessor$Type<(T)>;
}}
declare module "packages/snownee/jade/api/theme/$Theme" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $Theme {
static readonly "DARK": $Theme
 "id": $ResourceLocation
 "backgroundColor": integer
readonly "borderColor": (integer)[]
 "titleColor": integer
 "normalColor": integer
 "infoColor": integer
 "successColor": integer
 "warningColor": integer
 "dangerColor": integer
 "failureColor": integer
 "boxBorderColor": integer
 "itemAmountColor": integer
 "textShadow": boolean
 "backgroundTexture": $ResourceLocation
 "backgroundTextureUV": (integer)[]
 "backgroundTexture_withIcon": $ResourceLocation
 "backgroundTextureUV_withIcon": (integer)[]
readonly "padding": (integer)[]
 "squareBorder": boolean
 "opacity": float
 "bottomProgressOffset": (integer)[]
 "bottomProgressNormalColor": integer
 "bottomProgressFailureColor": integer
 "lightColorScheme": boolean
 "hidden": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Theme$Type = ($Theme);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Theme_ = $Theme$Type;
}}
declare module "packages/snownee/jade/api/callback/$JadeBeforeRenderCallback" {
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$JadeBeforeRenderCallback$ColorSetting, $JadeBeforeRenderCallback$ColorSetting$Type} from "packages/snownee/jade/api/callback/$JadeBeforeRenderCallback$ColorSetting"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export interface $JadeBeforeRenderCallback {

 "beforeRender"(arg0: $ITooltip$Type, arg1: $Rect2i$Type, arg2: $GuiGraphics$Type, arg3: $Accessor$Type<(any)>, arg4: $JadeBeforeRenderCallback$ColorSetting$Type): boolean

(arg0: $ITooltip$Type, arg1: $Rect2i$Type, arg2: $GuiGraphics$Type, arg3: $Accessor$Type<(any)>, arg4: $JadeBeforeRenderCallback$ColorSetting$Type): boolean
}

export namespace $JadeBeforeRenderCallback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeBeforeRenderCallback$Type = ($JadeBeforeRenderCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeBeforeRenderCallback_ = $JadeBeforeRenderCallback$Type;
}}
declare module "packages/snownee/jade/addon/lootr/$LootrPlugin" {
import {$IWailaClientRegistration, $IWailaClientRegistration$Type} from "packages/snownee/jade/api/$IWailaClientRegistration"
import {$IWailaPlugin, $IWailaPlugin$Type} from "packages/snownee/jade/api/$IWailaPlugin"
import {$IWailaCommonRegistration, $IWailaCommonRegistration$Type} from "packages/snownee/jade/api/$IWailaCommonRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $LootrPlugin implements $IWailaPlugin {
static readonly "ID": string
static readonly "INFO": $ResourceLocation
static readonly "INVENTORY": $ResourceLocation

constructor()

public "register"(arg0: $IWailaCommonRegistration$Type): void
public "registerClient"(arg0: $IWailaClientRegistration$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrPlugin$Type = ($LootrPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrPlugin_ = $LootrPlugin$Type;
}}
declare module "packages/snownee/jade/api/$IBlockComponentProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IToggleableProvider, $IToggleableProvider$Type} from "packages/snownee/jade/api/$IToggleableProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export interface $IBlockComponentProvider extends $IToggleableProvider {

 "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
 "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
 "isRequired"(): boolean
 "enabledByDefault"(): boolean
 "getUid"(): $ResourceLocation
 "getDefaultPriority"(): integer
}

export namespace $IBlockComponentProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IBlockComponentProvider$Type = ($IBlockComponentProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IBlockComponentProvider_ = $IBlockComponentProvider$Type;
}}
declare module "packages/snownee/jade/gui/config/value/$CycleOptionValue" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$CycleButton$Builder, $CycleButton$Builder$Type} from "packages/net/minecraft/client/gui/components/$CycleButton$Builder"
import {$OptionValue, $OptionValue$Type} from "packages/snownee/jade/gui/config/value/$OptionValue"

export class $CycleOptionValue<T> extends $OptionValue<(T)> {
 "serverFeature": boolean

constructor(arg0: string, arg1: $CycleButton$Builder$Type<(T)>, arg2: T, arg3: $Consumer$Type<(T)>)

public "setValue"(arg0: T): void
set "value"(value: T)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CycleOptionValue$Type<T> = ($CycleOptionValue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CycleOptionValue_<T> = $CycleOptionValue$Type<(T)>;
}}
declare module "packages/snownee/jade/impl/ui/$CompoundElement" {
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$Element, $Element$Type} from "packages/snownee/jade/api/ui/$Element"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $CompoundElement extends $Element {
static readonly "EMPTY": $CompoundElement


public static "of"(arg0: $ItemStack$Type): $CompoundElement
public static "of"(arg0: $ItemStack$Type, arg1: float, arg2: string): $CompoundElement
public static "of"(arg0: $ItemStack$Type, arg1: float): $CompoundElement
public "getMessage"(): string
public "getSize"(): $Vec2
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
get "message"(): string
get "size"(): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompoundElement$Type = ($CompoundElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompoundElement_ = $CompoundElement$Type;
}}
declare module "packages/snownee/jade/addon/lootr/$LootrEntityInfoProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $LootrEntityInfoProvider extends $Enum<($LootrEntityInfoProvider)> implements $IEntityComponentProvider, $IServerDataProvider<($EntityAccessor)> {
static readonly "INSTANCE": $LootrEntityInfoProvider


public static "values"(): ($LootrEntityInfoProvider)[]
public static "valueOf"(arg0: string): $LootrEntityInfoProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $EntityAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrEntityInfoProvider$Type = (("instance")) | ($LootrEntityInfoProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrEntityInfoProvider_ = $LootrEntityInfoProvider$Type;
}}
declare module "packages/snownee/jade/impl/ui/$SpacerElement" {
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$Element, $Element$Type} from "packages/snownee/jade/api/ui/$Element"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $SpacerElement extends $Element {

constructor(arg0: $Vec2$Type)

public "getSize"(): $Vec2
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
get "size"(): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpacerElement$Type = ($SpacerElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpacerElement_ = $SpacerElement$Type;
}}
declare module "packages/snownee/jade/impl/ui/$IconElement" {
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$Element, $Element$Type} from "packages/snownee/jade/api/ui/$Element"
import {$IconUI, $IconUI$Type} from "packages/snownee/jade/overlay/$IconUI"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $IconElement extends $Element {

constructor(arg0: $IconUI$Type)

public "getSize"(): $Vec2
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
get "size"(): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IconElement$Type = ($IconElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IconElement_ = $IconElement$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$HorseStatsProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $HorseStatsProvider extends $Enum<($HorseStatsProvider)> implements $IEntityComponentProvider {
static readonly "INSTANCE": $HorseStatsProvider


public static "values"(): ($HorseStatsProvider)[]
public static "valueOf"(arg0: string): $HorseStatsProvider
public "getUid"(): $ResourceLocation
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HorseStatsProvider$Type = (("instance")) | ($HorseStatsProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HorseStatsProvider_ = $HorseStatsProvider$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$JukeboxProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $JukeboxProvider extends $Enum<($JukeboxProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)> {
static readonly "INSTANCE": $JukeboxProvider


public static "values"(): ($JukeboxProvider)[]
public static "valueOf"(arg0: string): $JukeboxProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JukeboxProvider$Type = (("instance")) | ($JukeboxProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JukeboxProvider_ = $JukeboxProvider$Type;
}}
declare module "packages/snownee/jade/addon/core/$ModNameProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $ModNameProvider extends $Enum<($ModNameProvider)> implements $IBlockComponentProvider, $IEntityComponentProvider {
static readonly "INSTANCE": $ModNameProvider


public static "values"(): ($ModNameProvider)[]
public static "valueOf"(arg0: string): $ModNameProvider
public "getUid"(): $ResourceLocation
public "getDefaultPriority"(): integer
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
get "required"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModNameProvider$Type = (("instance")) | ($ModNameProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModNameProvider_ = $ModNameProvider$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$TotalEnchantmentPowerProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $TotalEnchantmentPowerProvider extends $Enum<($TotalEnchantmentPowerProvider)> implements $IBlockComponentProvider {
static readonly "INSTANCE": $TotalEnchantmentPowerProvider


public static "values"(): ($TotalEnchantmentPowerProvider)[]
public static "valueOf"(arg0: string): $TotalEnchantmentPowerProvider
public "getUid"(): $ResourceLocation
public static "getPower"(arg0: $Level$Type, arg1: $BlockPos$Type): float
public "getDefaultPriority"(): integer
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
get "required"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TotalEnchantmentPowerProvider$Type = (("instance")) | ($TotalEnchantmentPowerProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TotalEnchantmentPowerProvider_ = $TotalEnchantmentPowerProvider$Type;
}}
declare module "packages/snownee/jade/api/ui/$IBoxStyle" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $IBoxStyle {

 "tag"(arg0: $ResourceLocation$Type): void
 "borderWidth"(): float
 "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
}

export namespace $IBoxStyle {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IBoxStyle$Type = ($IBoxStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IBoxStyle_ = $IBoxStyle$Type;
}}
declare module "packages/snownee/jade/addon/core/$RegistryNameProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $RegistryNameProvider extends $Enum<($RegistryNameProvider)> implements $IBlockComponentProvider, $IEntityComponentProvider {
static readonly "INSTANCE": $RegistryNameProvider


public static "values"(): ($RegistryNameProvider)[]
public "append"(arg0: $ITooltip$Type, arg1: string, arg2: $IPluginConfig$Type): void
public static "valueOf"(arg0: string): $RegistryNameProvider
public "isRequired"(): boolean
public "getUid"(): $ResourceLocation
public "getDefaultPriority"(): integer
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "enabledByDefault"(): boolean
get "required"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryNameProvider$Type = (("instance")) | ($RegistryNameProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryNameProvider_ = $RegistryNameProvider$Type;
}}
declare module "packages/snownee/jade/api/view/$ProgressView" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IProgressStyle, $IProgressStyle$Type} from "packages/snownee/jade/api/ui/$IProgressStyle"

export class $ProgressView {
 "style": $IProgressStyle
 "progress": float
 "text": $Component

constructor(arg0: $IProgressStyle$Type)

public static "read"(arg0: $CompoundTag$Type): $ProgressView
public static "create"(arg0: float): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProgressView$Type = ($ProgressView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProgressView_ = $ProgressView$Type;
}}
declare module "packages/snownee/jade/api/config/$IWailaConfig$TTSMode" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$SimpleStringRepresentable, $SimpleStringRepresentable$Type} from "packages/snownee/jade/api/$SimpleStringRepresentable"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $IWailaConfig$TTSMode extends $Enum<($IWailaConfig$TTSMode)> implements $SimpleStringRepresentable {
static readonly "TOGGLE": $IWailaConfig$TTSMode
static readonly "PRESS": $IWailaConfig$TTSMode


public static "values"(): ($IWailaConfig$TTSMode)[]
public static "valueOf"(arg0: string): $IWailaConfig$TTSMode
public "getSerializedName"(): string
public static "fromEnumWithMapping"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
public static "keys"(arg0: ($StringRepresentable$Type)[]): $Keyable
public static "fromEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
get "serializedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IWailaConfig$TTSMode$Type = (("toggle") | ("press")) | ($IWailaConfig$TTSMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IWailaConfig$TTSMode_ = $IWailaConfig$TTSMode$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$ItemFrameProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $ItemFrameProvider extends $Enum<($ItemFrameProvider)> implements $IEntityComponentProvider {
static readonly "INSTANCE": $ItemFrameProvider


public static "values"(): ($ItemFrameProvider)[]
public static "valueOf"(arg0: string): $ItemFrameProvider
public "getUid"(): $ResourceLocation
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemFrameProvider$Type = (("instance")) | ($ItemFrameProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemFrameProvider_ = $ItemFrameProvider$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$BrewingStandProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $BrewingStandProvider extends $Enum<($BrewingStandProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)> {
static readonly "INSTANCE": $BrewingStandProvider


public static "values"(): ($BrewingStandProvider)[]
public static "valueOf"(arg0: string): $BrewingStandProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BrewingStandProvider$Type = (("instance")) | ($BrewingStandProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BrewingStandProvider_ = $BrewingStandProvider$Type;
}}
declare module "packages/snownee/jade/$JadeClient" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"

export class $JadeClient {
static "openConfig": $KeyMapping
static "showOverlay": $KeyMapping
static "toggleLiquid": $KeyMapping
static "showDetails": $KeyMapping
static "narrate": $KeyMapping
static "showRecipes": $KeyMapping
static "showUses": $KeyMapping
static "hideModName": boolean

constructor()

public static "format"(arg0: string, ...arg1: (any)[]): $MutableComponent
public static "init"(): void
public static "builtInOverrides"(arg0: $HitResult$Type, arg1: $Accessor$Type<(any)>, arg2: $Accessor$Type<(any)>): $Accessor<(any)>
public static "onGui"(arg0: $Screen$Type): void
public static "drawBreakingProgress"(arg0: $ITooltip$Type, arg1: $Rect2i$Type, arg2: $GuiGraphics$Type, arg3: $Accessor$Type<(any)>): void
public static "onKeyPressed"(arg0: integer): void
public static "onTooltip"(arg0: $List$Type<($Component$Type)>, arg1: $ItemStack$Type, arg2: $TooltipFlag$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeClient$Type = ($JadeClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeClient_ = $JadeClient$Type;
}}
declare module "packages/snownee/jade/api/$SimpleStringRepresentable" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export interface $SimpleStringRepresentable extends $StringRepresentable {

 "getSerializedName"(): string
}

export namespace $SimpleStringRepresentable {
function fromEnumWithMapping<E>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
function keys(arg0: ($StringRepresentable$Type)[]): $Keyable
function fromEnum<E>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleStringRepresentable$Type = ($SimpleStringRepresentable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleStringRepresentable_ = $SimpleStringRepresentable$Type;
}}
declare module "packages/snownee/jade/util/$SmoothChasingValue" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SmoothChasingValue {
 "value": float

constructor()

public "target"(arg0: float): $SmoothChasingValue
public "start"(arg0: float): $SmoothChasingValue
public "set"(arg0: float): $SmoothChasingValue
public "getTarget"(): float
public "tick"(arg0: float): void
public "withSpeed"(arg0: float): $SmoothChasingValue
public "getSpeed"(): float
public "isMoving"(): boolean
get "speed"(): float
get "moving"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SmoothChasingValue$Type = ($SmoothChasingValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SmoothChasingValue_ = $SmoothChasingValue$Type;
}}
declare module "packages/snownee/jade/impl/config/$WailaConfig$ConfigGeneral" {
import {$IWailaConfig$TTSMode, $IWailaConfig$TTSMode$Type} from "packages/snownee/jade/api/config/$IWailaConfig$TTSMode"
import {$WailaConfig$ConfigGeneral$ConfigDisplay, $WailaConfig$ConfigGeneral$ConfigDisplay$Type} from "packages/snownee/jade/impl/config/$WailaConfig$ConfigGeneral$ConfigDisplay"
import {$IWailaConfig$BossBarOverlapMode, $IWailaConfig$BossBarOverlapMode$Type} from "packages/snownee/jade/api/config/$IWailaConfig$BossBarOverlapMode"
import {$IWailaConfig$DisplayMode, $IWailaConfig$DisplayMode$Type} from "packages/snownee/jade/api/config/$IWailaConfig$DisplayMode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IWailaConfig$IConfigGeneral, $IWailaConfig$IConfigGeneral$Type} from "packages/snownee/jade/api/config/$IWailaConfig$IConfigGeneral"
import {$IWailaConfig$FluidMode, $IWailaConfig$FluidMode$Type} from "packages/snownee/jade/api/config/$IWailaConfig$FluidMode"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $WailaConfig$ConfigGeneral implements $IWailaConfig$IConfigGeneral {
static readonly "CODEC": $Codec<($WailaConfig$ConfigGeneral)>
static readonly "itemModNameTooltipDisabledByMods": $List<(string)>
 "hintOverlayToggle": boolean
 "hintNarratorToggle": boolean
 "previewOverlay": boolean

constructor(arg0: boolean, arg1: boolean, arg2: boolean, arg3: $WailaConfig$ConfigGeneral$ConfigDisplay$Type, arg4: boolean, arg5: boolean, arg6: boolean, arg7: $IWailaConfig$TTSMode$Type, arg8: $IWailaConfig$FluidMode$Type, arg9: float, arg10: boolean, arg11: boolean, arg12: $IWailaConfig$BossBarOverlapMode$Type, arg13: boolean)
constructor()

public static "init"(): void
public "isDebug"(): boolean
public "setDisplayEntities"(arg0: boolean): void
public "setDisplayBosses"(arg0: boolean): void
public "setHideFromTabList"(arg0: boolean): void
public "setDebug"(arg0: boolean): void
public "setDisplayBlocks"(arg0: boolean): void
public "getDisplayBlocks"(): boolean
public "setHideFromDebug"(arg0: boolean): void
public "setTTSMode"(arg0: $IWailaConfig$TTSMode$Type): void
public "getDisplayFluids"(): $IWailaConfig$FluidMode
public "setReachDistance"(arg0: float): void
public "getDisplayEntities"(): boolean
public "getDisplayBosses"(): boolean
public "getReachDistance"(): float
public "setDisplayFluids"(arg0: boolean): void
public "setDisplayFluids"(arg0: $IWailaConfig$FluidMode$Type): void
public "getDisplayMode"(): $IWailaConfig$DisplayMode
public "setDisplayTooltip"(arg0: boolean): void
public "getTTSMode"(): $IWailaConfig$TTSMode
public "toggleTTS"(): void
public "getBossBarOverlapMode"(): $IWailaConfig$BossBarOverlapMode
public "getBuiltinCamouflage"(): boolean
public "shouldDisplayFluids"(): boolean
public "shouldEnableTextToSpeech"(): boolean
public "showItemModNameTooltip"(): boolean
public "shouldDisplayTooltip"(): boolean
public "setBuiltinCamouflage"(arg0: boolean): void
public "setItemModNameTooltip"(arg0: boolean): void
public "setBossBarOverlapMode"(arg0: $IWailaConfig$BossBarOverlapMode$Type): void
public "shouldHideFromDebug"(): boolean
public "shouldHideFromTabList"(): boolean
public "setDisplayMode"(arg0: $IWailaConfig$DisplayMode$Type): void
get "debug"(): boolean
set "displayEntities"(value: boolean)
set "displayBosses"(value: boolean)
set "hideFromTabList"(value: boolean)
set "debug"(value: boolean)
set "displayBlocks"(value: boolean)
get "displayBlocks"(): boolean
set "hideFromDebug"(value: boolean)
set "tTSMode"(value: $IWailaConfig$TTSMode$Type)
get "displayFluids"(): $IWailaConfig$FluidMode
set "reachDistance"(value: float)
get "displayEntities"(): boolean
get "displayBosses"(): boolean
get "reachDistance"(): float
set "displayFluids"(value: boolean)
set "displayFluids"(value: $IWailaConfig$FluidMode$Type)
get "displayMode"(): $IWailaConfig$DisplayMode
set "displayTooltip"(value: boolean)
get "tTSMode"(): $IWailaConfig$TTSMode
get "bossBarOverlapMode"(): $IWailaConfig$BossBarOverlapMode
get "builtinCamouflage"(): boolean
set "builtinCamouflage"(value: boolean)
set "itemModNameTooltip"(value: boolean)
set "bossBarOverlapMode"(value: $IWailaConfig$BossBarOverlapMode$Type)
set "displayMode"(value: $IWailaConfig$DisplayMode$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WailaConfig$ConfigGeneral$Type = ($WailaConfig$ConfigGeneral);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WailaConfig$ConfigGeneral_ = $WailaConfig$ConfigGeneral$Type;
}}
declare module "packages/snownee/jade/impl/config/$WailaConfig$ConfigOverlay" {
import {$Theme, $Theme$Type} from "packages/snownee/jade/api/theme/$Theme"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IWailaConfig$IConfigOverlay, $IWailaConfig$IConfigOverlay$Type} from "packages/snownee/jade/api/config/$IWailaConfig$IConfigOverlay"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IWailaConfig$IconMode, $IWailaConfig$IconMode$Type} from "packages/snownee/jade/api/config/$IWailaConfig$IconMode"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $WailaConfig$ConfigOverlay implements $IWailaConfig$IConfigOverlay {
static readonly "CODEC": $Codec<($WailaConfig$ConfigOverlay)>
 "activeTheme": $ResourceLocation
 "themesHash": integer

constructor()
constructor(arg0: $ResourceLocation$Type, arg1: integer, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: boolean, arg8: boolean, arg9: float, arg10: float, arg11: $IWailaConfig$IconMode$Type, arg12: boolean, arg13: float)

public "setSquare"(arg0: boolean): void
public "getTheme"(): $Theme
public "getOverlayPosY"(): float
public "getAnchorY"(): float
public "setFlipMainHand"(arg0: boolean): void
public "getFlipMainHand"(): boolean
public "getOverlayPosX"(): float
public "setOverlayPosY"(arg0: float): void
public "setAnchorY"(arg0: float): void
public "tryFlip"(arg0: float): float
public "setAnchorX"(arg0: float): void
public "setOverlayScale"(arg0: float): void
public "setOverlayPosX"(arg0: float): void
public "getAnchorX"(): float
public "getOverlayScale"(): float
public "getIconMode"(): $IWailaConfig$IconMode
public "setIconMode"(arg0: $IWailaConfig$IconMode$Type): void
public "shouldShowIcon"(): boolean
public "getAnimation"(): boolean
public "applyTheme"(arg0: $ResourceLocation$Type): void
public "getSquare"(): boolean
/**
 * 
 * @deprecated
 */
public "getThemes"(): $Collection<($Theme)>
public "getAlpha"(): float
public "setAlpha"(arg0: float): void
public "getDisappearingDelay"(): float
public "setDisappearingDelay"(arg0: float): void
public "getAutoScaleThreshold"(): float
public "setAnimation"(arg0: boolean): void
public static "applyAlpha"(arg0: integer, arg1: float): integer
set "square"(value: boolean)
get "theme"(): $Theme
get "overlayPosY"(): float
get "anchorY"(): float
set "flipMainHand"(value: boolean)
get "flipMainHand"(): boolean
get "overlayPosX"(): float
set "overlayPosY"(value: float)
set "anchorY"(value: float)
set "anchorX"(value: float)
set "overlayScale"(value: float)
set "overlayPosX"(value: float)
get "anchorX"(): float
get "overlayScale"(): float
get "iconMode"(): $IWailaConfig$IconMode
set "iconMode"(value: $IWailaConfig$IconMode$Type)
get "animation"(): boolean
get "square"(): boolean
get "themes"(): $Collection<($Theme)>
get "alpha"(): float
set "alpha"(value: float)
get "disappearingDelay"(): float
set "disappearingDelay"(value: float)
get "autoScaleThreshold"(): float
set "animation"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WailaConfig$ConfigOverlay$Type = ($WailaConfig$ConfigOverlay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WailaConfig$ConfigOverlay_ = $WailaConfig$ConfigOverlay$Type;
}}
declare module "packages/snownee/jade/impl/$BlockAccessorClientHandler" {
import {$Accessor$ClientHandler, $Accessor$ClientHandler$Type} from "packages/snownee/jade/api/$Accessor$ClientHandler"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$IJadeProvider, $IJadeProvider$Type} from "packages/snownee/jade/api/$IJadeProvider"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $BlockAccessorClientHandler implements $Accessor$ClientHandler<($BlockAccessor)> {

constructor()

public "requestData"(arg0: $BlockAccessor$Type): void
public "shouldDisplay"(arg0: $BlockAccessor$Type): boolean
public "getIcon"(arg0: $BlockAccessor$Type): $IElement
public "gatherComponents"(arg0: $BlockAccessor$Type, arg1: $Function$Type<($IJadeProvider$Type), ($ITooltip$Type)>): void
public "shouldRequestData"(arg0: $BlockAccessor$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockAccessorClientHandler$Type = ($BlockAccessorClientHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockAccessorClientHandler_ = $BlockAccessorClientHandler$Type;
}}
declare module "packages/snownee/jade/addon/core/$CorePlugin" {
import {$IWailaClientRegistration, $IWailaClientRegistration$Type} from "packages/snownee/jade/api/$IWailaClientRegistration"
import {$IWailaPlugin, $IWailaPlugin$Type} from "packages/snownee/jade/api/$IWailaPlugin"
import {$IWailaCommonRegistration, $IWailaCommonRegistration$Type} from "packages/snownee/jade/api/$IWailaCommonRegistration"

export class $CorePlugin implements $IWailaPlugin {

constructor()

public "register"(arg0: $IWailaCommonRegistration$Type): void
public "registerClient"(arg0: $IWailaClientRegistration$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CorePlugin$Type = ($CorePlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CorePlugin_ = $CorePlugin$Type;
}}
declare module "packages/snownee/jade/api/ui/$Element" {
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$IElement$Align, $IElement$Align$Type} from "packages/snownee/jade/api/ui/$IElement$Align"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $Element implements $IElement {

constructor()

public "size"(arg0: $Vec2$Type): $IElement
public "message"(arg0: string): $IElement
public "tag"(arg0: $ResourceLocation$Type): $IElement
public "getTag"(): $ResourceLocation
public "align"(arg0: $IElement$Align$Type): $IElement
public "getAlignment"(): $IElement$Align
public "translate"(arg0: $Vec2$Type): $IElement
public "getTranslation"(): $Vec2
public "getCachedMessage"(): string
public "clearCachedMessage"(): $IElement
public "getCachedSize"(): $Vec2
public "getMessage"(): string
public "getSize"(): $Vec2
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
get "alignment"(): $IElement$Align
get "translation"(): $Vec2
get "cachedMessage"(): string
get "cachedSize"(): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Element$Type = ($Element);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Element_ = $Element$Type;
}}
declare module "packages/snownee/jade/addon/create/$CraftingBlueprintProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $CraftingBlueprintProvider extends $Enum<($CraftingBlueprintProvider)> implements $IEntityComponentProvider {
static readonly "INSTANCE": $CraftingBlueprintProvider
static "RESULT": $Field


public static "values"(): ($CraftingBlueprintProvider)[]
public static "valueOf"(arg0: string): $CraftingBlueprintProvider
public static "getResult"(): $ItemStack
public "getUid"(): $ResourceLocation
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "result"(): $ItemStack
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingBlueprintProvider$Type = (("instance")) | ($CraftingBlueprintProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingBlueprintProvider_ = $CraftingBlueprintProvider$Type;
}}
declare module "packages/snownee/jade/gui/config/$OptionsList$Entry" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$ContainerObjectSelectionList$Entry, $ContainerObjectSelectionList$Entry$Type} from "packages/net/minecraft/client/gui/components/$ContainerObjectSelectionList$Entry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export class $OptionsList$Entry extends $ContainerObjectSelectionList$Entry<($OptionsList$Entry)> {

constructor()

public "parent"(): $OptionsList$Entry
public "parent"(arg0: $OptionsList$Entry$Type): $OptionsList$Entry
public "getDescription"(): string
public static "makeKey"(arg0: string): string
public "addWidget"(arg0: $AbstractWidget$Type, arg1: integer, arg2: integer): void
public "addWidget"(arg0: $AbstractWidget$Type, arg1: integer): void
public "addMessage"(arg0: string): void
public "getMessages"(): $List<(string)>
public "children"(): $List<(any)>
public "addMessageKey"(arg0: string): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: boolean, arg9: float): void
public "narratables"(): $List<(any)>
public static "makeTitle"(arg0: string): $MutableComponent
public "getTextWidth"(): integer
public "getTextX"(arg0: integer): integer
public "getFirstWidget"(): $AbstractWidget
public "setDisabled"(arg0: boolean): void
get "description"(): string
get "messages"(): $List<(string)>
get "textWidth"(): integer
get "firstWidget"(): $AbstractWidget
set "disabled"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionsList$Entry$Type = ($OptionsList$Entry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionsList$Entry_ = $OptionsList$Entry$Type;
}}
declare module "packages/snownee/jade/impl/config/$WailaConfig" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$WailaConfig$ConfigOverlay, $WailaConfig$ConfigOverlay$Type} from "packages/snownee/jade/impl/config/$WailaConfig$ConfigOverlay"
import {$WailaConfig$ConfigGeneral, $WailaConfig$ConfigGeneral$Type} from "packages/snownee/jade/impl/config/$WailaConfig$ConfigGeneral"
import {$WailaConfig$ConfigFormatting, $WailaConfig$ConfigFormatting$Type} from "packages/snownee/jade/impl/config/$WailaConfig$ConfigFormatting"
import {$IWailaConfig, $IWailaConfig$Type} from "packages/snownee/jade/api/config/$IWailaConfig"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $WailaConfig implements $IWailaConfig {
static readonly "CODEC": $Codec<($WailaConfig)>

constructor(arg0: $WailaConfig$ConfigGeneral$Type, arg1: $WailaConfig$ConfigOverlay$Type, arg2: $WailaConfig$ConfigFormatting$Type)

public "getPlugin"(): $IPluginConfig
public "getOverlay"(): $WailaConfig$ConfigOverlay
public "getGeneral"(): $WailaConfig$ConfigGeneral
public static "get"(): $IWailaConfig
get "plugin"(): $IPluginConfig
get "overlay"(): $WailaConfig$ConfigOverlay
get "general"(): $WailaConfig$ConfigGeneral
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WailaConfig$Type = ($WailaConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WailaConfig_ = $WailaConfig$Type;
}}
declare module "packages/snownee/jade/addon/harvest/$HarvestToolProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$ResourceManagerReloadListener, $ResourceManagerReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManagerReloadListener"
import {$Cache, $Cache$Type} from "packages/com/google/common/cache/$Cache"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ProfilerFiller, $ProfilerFiller$Type} from "packages/net/minecraft/util/profiling/$ProfilerFiller"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ImmutableList, $ImmutableList$Type} from "packages/com/google/common/collect/$ImmutableList"
import {$PreparableReloadListener$PreparationBarrier, $PreparableReloadListener$PreparationBarrier$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener$PreparationBarrier"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$ToolHandler, $ToolHandler$Type} from "packages/snownee/jade/addon/harvest/$ToolHandler"

export class $HarvestToolProvider extends $Enum<($HarvestToolProvider)> implements $IBlockComponentProvider, $ResourceManagerReloadListener {
static readonly "INSTANCE": $HarvestToolProvider
static readonly "resultCache": $Cache<($BlockState), ($ImmutableList<($ItemStack)>)>
static readonly "TOOL_HANDLERS": $Map<(string), ($ToolHandler)>


public static "values"(): ($HarvestToolProvider)[]
public static "valueOf"(arg0: string): $HarvestToolProvider
public "getText"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type): $List<($IElement)>
public static "registerHandler"(arg0: $ToolHandler$Type): void
public "getUid"(): $ResourceLocation
public static "getTool"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): $ImmutableList<($ItemStack)>
public "onResourceManagerReload"(arg0: $ResourceManager$Type): void
public "getDefaultPriority"(): integer
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "reload"(arg0: $PreparableReloadListener$PreparationBarrier$Type, arg1: $ResourceManager$Type, arg2: $ProfilerFiller$Type, arg3: $ProfilerFiller$Type, arg4: $Executor$Type, arg5: $Executor$Type): $CompletableFuture<(void)>
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getName"(): string
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
get "required"(): boolean
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HarvestToolProvider$Type = (("instance")) | ($HarvestToolProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HarvestToolProvider_ = $HarvestToolProvider$Type;
}}
declare module "packages/snownee/jade/impl/template/$TemplateEntityComponentProvider" {
import {$TemplateComponentProvider, $TemplateComponentProvider$Type} from "packages/snownee/jade/impl/template/$TemplateComponentProvider"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"

export class $TemplateEntityComponentProvider extends $TemplateComponentProvider<($EntityAccessor)> implements $IEntityComponentProvider {

constructor(arg0: $ResourceLocation$Type)
constructor(arg0: $ResourceLocation$Type, arg1: boolean, arg2: boolean, arg3: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TemplateEntityComponentProvider$Type = ($TemplateEntityComponentProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TemplateEntityComponentProvider_ = $TemplateEntityComponentProvider$Type;
}}
declare module "packages/snownee/jade/gui/config/$OptionButton" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$OptionsList$Entry, $OptionsList$Entry$Type} from "packages/snownee/jade/gui/config/$OptionsList$Entry"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $OptionButton extends $OptionsList$Entry {

constructor(arg0: string, arg1: $Button$Type)
constructor(arg0: $Component$Type, arg1: $Button$Type)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: boolean, arg9: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionButton$Type = ($OptionButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionButton_ = $OptionButton$Type;
}}
declare module "packages/snownee/jade/addon/vanilla/$CropProgressProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $CropProgressProvider extends $Enum<($CropProgressProvider)> implements $IBlockComponentProvider {
static readonly "INSTANCE": $CropProgressProvider


public static "values"(): ($CropProgressProvider)[]
public static "valueOf"(arg0: string): $CropProgressProvider
public "getUid"(): $ResourceLocation
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CropProgressProvider$Type = (("instance")) | ($CropProgressProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CropProgressProvider_ = $CropProgressProvider$Type;
}}
declare module "packages/snownee/jade/gui/config/$OptionsList" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$OptionsList$Entry, $OptionsList$Entry$Type} from "packages/snownee/jade/gui/config/$OptionsList$Entry"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$AbstractSelectionList$Entry, $AbstractSelectionList$Entry$Type} from "packages/net/minecraft/client/gui/components/$AbstractSelectionList$Entry"
import {$BooleanConsumer, $BooleanConsumer$Type} from "packages/it/unimi/dsi/fastutil/booleans/$BooleanConsumer"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$OptionValue, $OptionValue$Type} from "packages/snownee/jade/gui/config/value/$OptionValue"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$FloatUnaryOperator, $FloatUnaryOperator$Type} from "packages/it/unimi/dsi/fastutil/floats/$FloatUnaryOperator"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ContainerObjectSelectionList, $ContainerObjectSelectionList$Type} from "packages/net/minecraft/client/gui/components/$ContainerObjectSelectionList"
import {$CycleButton$Builder, $CycleButton$Builder$Type} from "packages/net/minecraft/client/gui/components/$CycleButton$Builder"
import {$OptionsList$Title, $OptionsList$Title$Type} from "packages/snownee/jade/gui/config/$OptionsList$Title"
import {$BaseOptionsScreen, $BaseOptionsScreen$Type} from "packages/snownee/jade/gui/$BaseOptionsScreen"

export class $OptionsList extends $ContainerObjectSelectionList<($OptionsList$Entry)> {
static readonly "OPTION_ON": $Component
static readonly "OPTION_OFF": $Component
readonly "forcePreview": $Set<($OptionsList$Entry)>
 "currentTitle": $OptionsList$Title
 "selectedKey": $KeyMapping
 "scrolling": boolean
 "hovered": E

constructor(arg0: $BaseOptionsScreen$Type, arg1: $Minecraft$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: $Runnable$Type)
constructor(arg0: $BaseOptionsScreen$Type, arg1: $Minecraft$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer)

public "add"<T extends $OptionsList$Entry>(arg0: T): T
public "save"(): void
public "removed"(): void
public "input"<T>(arg0: string, arg1: T, arg2: $Consumer$Type<(T)>, arg3: $Predicate$Type<(string)>): $OptionValue<(T)>
public "input"<T>(arg0: string, arg1: T, arg2: $Consumer$Type<(T)>): $OptionValue<(T)>
public "keybind"(arg0: $KeyMapping$Type): void
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "slider"(arg0: string, arg1: float, arg2: $Consumer$Type<(float)>, arg3: float, arg4: float, arg5: $FloatUnaryOperator$Type): $OptionValue<(float)>
public "slider"(arg0: string, arg1: float, arg2: $Consumer$Type<(float)>): $OptionValue<(float)>
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "m_93485_"(arg0: integer): integer
public "title"(arg0: string): $MutableComponent
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "isFocused"(): boolean
public "choices"<T>(arg0: string, arg1: T, arg2: $List$Type<(T)>, arg3: $Consumer$Type<(T)>, arg4: $Function$Type<(T), ($Component$Type)>): $OptionValue<(T)>
public "choices"(arg0: string, arg1: boolean, arg2: $BooleanConsumer$Type): $OptionValue<(boolean)>
public "choices"(arg0: string, arg1: boolean, arg2: $BooleanConsumer$Type, arg3: $Consumer$Type<($CycleButton$Builder$Type<(boolean)>)>): $OptionValue<(boolean)>
public "choices"<T extends $Enum<(T)>>(arg0: string, arg1: T, arg2: $Consumer$Type<(T)>, arg3: $Consumer$Type<($CycleButton$Builder$Type<(T)>)>): $OptionValue<(T)>
public "choices"<T extends $Enum<(T)>>(arg0: string, arg1: T, arg2: $Consumer$Type<(T)>): $OptionValue<(T)>
public "updateSearch"(arg0: string): void
public "setScrollAmount"(arg0: double): void
public "updateSaveState"(): void
public "getEntryAt"(arg0: double, arg1: double): $OptionsList$Entry
public "m_7610_"(arg0: integer): integer
public "showOnTop"(arg0: $OptionsList$Entry$Type): void
public "getRowWidth"(): integer
public "setDefaultParent"(arg0: $OptionsList$Entry$Type): void
public "resetMappingAndUpdateButtons"(): void
get "focused"(): boolean
set "scrollAmount"(value: double)
get "rowWidth"(): integer
set "defaultParent"(value: $OptionsList$Entry$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionsList$Type = ($OptionsList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionsList_ = $OptionsList$Type;
}}
declare module "packages/snownee/jade/gui/config/$NotUglyEditBox" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$FocusNavigationEvent, $FocusNavigationEvent$Type} from "packages/net/minecraft/client/gui/navigation/$FocusNavigationEvent"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$NarrationElementOutput, $NarrationElementOutput$Type} from "packages/net/minecraft/client/gui/narration/$NarrationElementOutput"
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ComponentPath, $ComponentPath$Type} from "packages/net/minecraft/client/gui/$ComponentPath"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$SoundManager, $SoundManager$Type} from "packages/net/minecraft/client/sounds/$SoundManager"

export class $NotUglyEditBox extends $AbstractWidget implements $Renderable {
 "paddingLeft": integer
 "paddingRight": integer
 "paddingTop": integer
 "responder": $Consumer<(string)>
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: $Font$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $Component$Type)
constructor(arg0: $Font$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $NotUglyEditBox$Type, arg6: $Component$Type)

public "getValue"(): string
public "setValue"(arg0: string): void
public "tick"(): void
public "setFormatter"(arg0: $BiFunction$Type<(string), (integer), ($FormattedCharSequence$Type)>): void
public "setFilter"(arg0: $Predicate$Type<(string)>): void
public "setVisible"(arg0: boolean): void
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "nextFocusPath"(arg0: $FocusNavigationEvent$Type): $ComponentPath
public "isMouseOver"(arg0: double, arg1: double): boolean
public "setFocused"(arg0: boolean): void
public "canConsumeInput"(): boolean
public "isVisible"(): boolean
public "getScreenX"(arg0: integer): integer
public "getCursorPosition"(): integer
public "setEditable"(arg0: boolean): void
public "getInnerWidth"(): integer
public "charTyped"(arg0: character, arg1: integer): boolean
public "setTextColorUneditable"(arg0: integer): void
public "setMaxLength"(arg0: integer): void
public "moveCursorToEnd"(): void
public "insertText"(arg0: string): void
public "setCursorPosition"(arg0: integer): void
public "deleteWords"(arg0: integer): void
public "moveCursor"(arg0: integer): void
public "getWordPosition"(arg0: integer): integer
public "moveCursorTo"(arg0: integer): void
public "moveCursorToStart"(): void
public "deleteChars"(arg0: integer): void
public "m_87963_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "onClick"(arg0: double, arg1: double): void
public "playDownSound"(arg0: $SoundManager$Type): void
public "m_168797_"(arg0: $NarrationElementOutput$Type): void
public "setHint"(arg0: $Component$Type): void
public "setTextColor"(arg0: integer): void
public "setHighlightPos"(arg0: integer): void
public "getHighlighted"(): string
public "setCanLoseFocus"(arg0: boolean): void
public "setSuggestion"(arg0: string): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
get "value"(): string
set "value"(value: string)
set "formatter"(value: $BiFunction$Type<(string), (integer), ($FormattedCharSequence$Type)>)
set "filter"(value: $Predicate$Type<(string)>)
set "visible"(value: boolean)
set "focused"(value: boolean)
get "visible"(): boolean
get "cursorPosition"(): integer
set "editable"(value: boolean)
get "innerWidth"(): integer
set "textColorUneditable"(value: integer)
set "maxLength"(value: integer)
set "cursorPosition"(value: integer)
set "hint"(value: $Component$Type)
set "textColor"(value: integer)
set "highlightPos"(value: integer)
get "highlighted"(): string
set "canLoseFocus"(value: boolean)
set "suggestion"(value: string)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NotUglyEditBox$Type = ($NotUglyEditBox);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NotUglyEditBox_ = $NotUglyEditBox$Type;
}}
declare module "packages/snownee/jade/api/callback/$JadeBeforeRenderCallback$ColorSetting" {
import {$Theme, $Theme$Type} from "packages/snownee/jade/api/theme/$Theme"
import {$MutableObject, $MutableObject$Type} from "packages/org/apache/commons/lang3/mutable/$MutableObject"

export class $JadeBeforeRenderCallback$ColorSetting {
 "alpha": float
 "theme": $MutableObject<($Theme)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeBeforeRenderCallback$ColorSetting$Type = ($JadeBeforeRenderCallback$ColorSetting);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeBeforeRenderCallback$ColorSetting_ = $JadeBeforeRenderCallback$ColorSetting$Type;
}}
declare module "packages/snownee/jade/command/$JadeClientCommand" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export class $JadeClientCommand {

constructor()

public static "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeClientCommand$Type = ($JadeClientCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeClientCommand_ = $JadeClientCommand$Type;
}}
declare module "packages/snownee/jade/impl/$ObjectDataCenter" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"

export class $ObjectDataCenter {
static "rateLimiter": integer
static "timeLastUpdate": long
static "serverConnected": boolean


public static "requestServerData"(): void
public static "get"(): $Accessor<(any)>
public static "set"(arg0: $Accessor$Type<(any)>): void
public static "setServerData"(arg0: $CompoundTag$Type): void
public static "getServerData"(): $CompoundTag
public static "getIcon"(): $IElement
public static "resetTimer"(): void
public static "isTimeElapsed"(arg0: long): boolean
set "serverData"(value: $CompoundTag$Type)
get "serverData"(): $CompoundTag
get "icon"(): $IElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectDataCenter$Type = ($ObjectDataCenter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectDataCenter_ = $ObjectDataCenter$Type;
}}
declare module "packages/snownee/jade/api/config/$IWailaConfig$DisplayMode" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$SimpleStringRepresentable, $SimpleStringRepresentable$Type} from "packages/snownee/jade/api/$SimpleStringRepresentable"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $IWailaConfig$DisplayMode extends $Enum<($IWailaConfig$DisplayMode)> implements $SimpleStringRepresentable {
static readonly "HOLD_KEY": $IWailaConfig$DisplayMode
static readonly "TOGGLE": $IWailaConfig$DisplayMode
static readonly "LITE": $IWailaConfig$DisplayMode


public static "values"(): ($IWailaConfig$DisplayMode)[]
public static "valueOf"(arg0: string): $IWailaConfig$DisplayMode
public "getSerializedName"(): string
public static "fromEnumWithMapping"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
public static "keys"(arg0: ($StringRepresentable$Type)[]): $Keyable
public static "fromEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
get "serializedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IWailaConfig$DisplayMode$Type = (("hold_key") | ("toggle") | ("lite")) | ($IWailaConfig$DisplayMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IWailaConfig$DisplayMode_ = $IWailaConfig$DisplayMode$Type;
}}
declare module "packages/snownee/jade/impl/config/entry/$BooleanConfigEntry" {
import {$ConfigEntry, $ConfigEntry$Type} from "packages/snownee/jade/impl/config/entry/$ConfigEntry"
import {$OptionsList, $OptionsList$Type} from "packages/snownee/jade/gui/config/$OptionsList"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$OptionValue, $OptionValue$Type} from "packages/snownee/jade/gui/config/value/$OptionValue"

export class $BooleanConfigEntry extends $ConfigEntry<(boolean)> {
readonly "id": $ResourceLocation

constructor(arg0: $ResourceLocation$Type, arg1: boolean)

public "isValidValue"(arg0: any): boolean
public "createUI"(arg0: $OptionsList$Type, arg1: string): $OptionValue<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BooleanConfigEntry$Type = ($BooleanConfigEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BooleanConfigEntry_ = $BooleanConfigEntry$Type;
}}
declare module "packages/snownee/jade/impl/ui/$ItemStackElement" {
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$Element, $Element$Type} from "packages/snownee/jade/api/ui/$Element"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ItemStackElement extends $Element {
static readonly "EMPTY": $ItemStackElement


public static "of"(arg0: $ItemStack$Type): $ItemStackElement
public static "of"(arg0: $ItemStack$Type, arg1: float, arg2: string): $ItemStackElement
public static "of"(arg0: $ItemStack$Type, arg1: float): $ItemStackElement
public "getMessage"(): string
public "getSize"(): $Vec2
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
get "message"(): string
get "size"(): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemStackElement$Type = ($ItemStackElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemStackElement_ = $ItemStackElement$Type;
}}
declare module "packages/snownee/jade/impl/config/$WailaConfig$ConfigFormatting" {
import {$IWailaConfig$IConfigFormatting, $IWailaConfig$IConfigFormatting$Type} from "packages/snownee/jade/api/config/$IWailaConfig$IConfigFormatting"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $WailaConfig$ConfigFormatting implements $IWailaConfig$IConfigFormatting {
static readonly "CODEC": $Codec<($WailaConfig$ConfigFormatting)>

constructor(arg0: string)
constructor()

public "registryName"(arg0: string): $Component
/**
 * 
 * @deprecated
 */
public "title"(arg0: any): $Component
public "getModName"(): string
public "setModName"(arg0: string): void
get "modName"(): string
set "modName"(value: string)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WailaConfig$ConfigFormatting$Type = ($WailaConfig$ConfigFormatting);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WailaConfig$ConfigFormatting_ = $WailaConfig$ConfigFormatting$Type;
}}
declare module "packages/snownee/jade/util/$ClientProxy" {
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$ResourceManagerReloadListener, $ResourceManagerReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManagerReloadListener"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$LiquidBlock, $LiquidBlock$Type} from "packages/net/minecraft/world/level/block/$LiquidBlock"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$JadeFluidObject, $JadeFluidObject$Type} from "packages/snownee/jade/api/fluid/$JadeFluidObject"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$RegisterClientCommandsEvent, $RegisterClientCommandsEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterClientCommandsEvent"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"
import {$ToolHandler, $ToolHandler$Type} from "packages/snownee/jade/addon/harvest/$ToolHandler"

export class $ClientProxy {
static "hasJEI": boolean
static "hasREI": boolean
static "hasFastScroll": boolean
static "maybeLowVisionUser": boolean

constructor()

public static "init"(): void
public static "registerCommands"(arg0: $RegisterClientCommandsEvent$Type): void
public static "elementFromLiquid"(arg0: $LiquidBlock$Type): $IElement
public static "onRenderTick"(arg0: $GuiGraphics$Type): void
public static "requestEntityData"(arg0: $EntityAccessor$Type): void
public static "registerKeyBinding"(arg0: string, arg1: integer): $KeyMapping
public static "requestBlockData"(arg0: $BlockAccessor$Type): void
public static "getBossBarRect"(): $Rect2i
public static "getBoundKeyOf"(arg0: $KeyMapping$Type): $InputConstants$Key
public static "getFluidSpriteAndColor"(arg0: $JadeFluidObject$Type, arg1: $BiConsumer$Type<($TextureAtlasSprite$Type), (integer)>): void
public static "registerDetailsKeyBinding"(): $KeyMapping
public static "renderItemDecorationsExtra"(arg0: $GuiGraphics$Type, arg1: $Font$Type, arg2: $ItemStack$Type, arg3: integer, arg4: integer, arg5: string): void
public static "createSwordToolHandler"(): $ToolHandler
public static "shouldRegisterRecipeViewerKeys"(): boolean
public static "shouldShowWithOverlay"(arg0: $Minecraft$Type, arg1: $Screen$Type): boolean
public static "registerReloadListener"(arg0: $ResourceManagerReloadListener$Type): void
public static "getModName"(arg0: string): $Optional<(string)>
public static "isShowDetailsPressed"(): boolean
get "bossBarRect"(): $Rect2i
get "showDetailsPressed"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientProxy$Type = ($ClientProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientProxy_ = $ClientProxy$Type;
}}
declare module "packages/snownee/jade/overlay/$RayTracing" {
import {$EntityHitResult, $EntityHitResult$Type} from "packages/net/minecraft/world/phys/$EntityHitResult"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $RayTracing {
static readonly "INSTANCE": $RayTracing
static "ENTITY_FILTER": $Predicate<($Entity)>


public "getTarget"(): $HitResult
public "fire"(): void
public "getIcon"(): $IElement
public static "wrapBlock"(arg0: $BlockGetter$Type, arg1: $BlockHitResult$Type, arg2: $CollisionContext$Type): $BlockState
public static "isEmptyElement"(arg0: $IElement$Type): boolean
public static "getEntityHitResult"(arg0: $Level$Type, arg1: $Entity$Type, arg2: $Vec3$Type, arg3: $Vec3$Type, arg4: $AABB$Type, arg5: $Predicate$Type<($Entity$Type)>): $EntityHitResult
public "rayTrace"(arg0: $Entity$Type, arg1: double, arg2: float): $HitResult
get "target"(): $HitResult
get "icon"(): $IElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RayTracing$Type = ($RayTracing);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RayTracing_ = $RayTracing$Type;
}}
declare module "packages/snownee/jade/gui/config/$OptionsList$Title" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$OptionsList$Entry, $OptionsList$Entry$Type} from "packages/snownee/jade/gui/config/$OptionsList$Entry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export class $OptionsList$Title extends $OptionsList$Entry {
 "narration": $Component

constructor(arg0: string)
constructor(arg0: $MutableComponent$Type)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: boolean, arg9: float): void
public "narratables"(): $List<(any)>
public "getTitle"(): $MutableComponent
public "getTextWidth"(): integer
public "getTextX"(arg0: integer): integer
get "title"(): $MutableComponent
get "textWidth"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionsList$Title$Type = ($OptionsList$Title);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionsList$Title_ = $OptionsList$Title$Type;
}}
declare module "packages/snownee/jade/impl/template/$TemplateEntityServerDataProvider" {
import {$TemplateServerDataProvider, $TemplateServerDataProvider$Type} from "packages/snownee/jade/impl/template/$TemplateServerDataProvider"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $TemplateEntityServerDataProvider extends $TemplateServerDataProvider<($EntityAccessor)> {

constructor(arg0: $ResourceLocation$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TemplateEntityServerDataProvider$Type = ($TemplateEntityServerDataProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TemplateEntityServerDataProvider_ = $TemplateEntityServerDataProvider$Type;
}}
declare module "packages/snownee/jade/addon/lootr/$LootrInfoProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $LootrInfoProvider extends $Enum<($LootrInfoProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)> {
static readonly "INSTANCE": $LootrInfoProvider


public static "values"(): ($LootrInfoProvider)[]
public static "valueOf"(arg0: string): $LootrInfoProvider
public "getUid"(): $ResourceLocation
public static "appendServerData"(arg0: $CompoundTag$Type, arg1: $UUID$Type): void
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public static "appendTooltip"(arg0: $ITooltip$Type, arg1: $Accessor$Type<(any)>): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrInfoProvider$Type = (("instance")) | ($LootrInfoProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrInfoProvider_ = $LootrInfoProvider$Type;
}}
declare module "packages/snownee/jade/impl/ui/$ElementHelper" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IProgressStyle, $IProgressStyle$Type} from "packages/snownee/jade/api/ui/$IProgressStyle"
import {$IBoxElement, $IBoxElement$Type} from "packages/snownee/jade/api/ui/$IBoxElement"
import {$IBoxStyle, $IBoxStyle$Type} from "packages/snownee/jade/api/ui/$IBoxStyle"
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$JadeFluidObject, $JadeFluidObject$Type} from "packages/snownee/jade/api/fluid/$JadeFluidObject"
import {$IElementHelper, $IElementHelper$Type} from "packages/snownee/jade/api/ui/$IElementHelper"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $ElementHelper implements $IElementHelper {
static readonly "INSTANCE": $ElementHelper
static readonly "SMALL_ITEM_SIZE": $Vec2
static readonly "SMALL_ITEM_OFFSET": $Vec2

constructor()

public "currentUid"(): $ResourceLocation
public "box"(arg0: $ITooltip$Type, arg1: $IBoxStyle$Type): $IBoxElement
public "text"(arg0: $Component$Type): $IElement
public "item"(arg0: $ItemStack$Type): $IElement
public "item"(arg0: $ItemStack$Type, arg1: float): $IElement
public "item"(arg0: $ItemStack$Type, arg1: float, arg2: string): $IElement
public "tooltip"(): $ITooltip
public "progress"(arg0: float, arg1: $Component$Type, arg2: $IProgressStyle$Type, arg3: $IBoxStyle$Type, arg4: boolean): $IElement
public "smallItem"(arg0: $ItemStack$Type): $IElement
public "spacer"(arg0: integer, arg1: integer): $IElement
public "progressStyle"(): $IProgressStyle
public "setCurrentUid"(arg0: $ResourceLocation$Type): void
public "fluid"(arg0: $JadeFluidObject$Type): $IElement
public static "get"(): $IElementHelper
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ElementHelper$Type = ($ElementHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ElementHelper_ = $ElementHelper$Type;
}}
declare module "packages/snownee/jade/addon/create/$FilterProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $FilterProvider extends $Enum<($FilterProvider)> implements $IBlockComponentProvider {
static readonly "INSTANCE": $FilterProvider


public static "values"(): ($FilterProvider)[]
public static "valueOf"(arg0: string): $FilterProvider
public "getUid"(): $ResourceLocation
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FilterProvider$Type = (("instance")) | ($FilterProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FilterProvider_ = $FilterProvider$Type;
}}
declare module "packages/snownee/jade/api/config/$TargetBlocklist" {
import {$List, $List$Type} from "packages/java/util/$List"

export class $TargetBlocklist {
 "__comment": string
 "values": $List<(string)>
 "version": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TargetBlocklist$Type = ($TargetBlocklist);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TargetBlocklist_ = $TargetBlocklist$Type;
}}
declare module "packages/snownee/jade/util/$Color" {
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $Color {
static readonly "CODEC": $Codec<(integer)>


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "valueOf"(arg0: string): $Color
public static "hex"(arg0: string): $Color
public "toInt"(): integer
public "getHex"(): string
public "getHue"(): double
public "getLightness"(): double
public "getSaturation"(): double
public "getRed"(): integer
public "getGreen"(): integer
public "getBlue"(): integer
public static "rgb"(arg0: integer, arg1: integer, arg2: integer): $Color
public static "rgb"(arg0: integer): $Color
public static "rgb"(arg0: integer, arg1: integer, arg2: integer, arg3: double): $Color
public static "hsl"(arg0: double, arg1: double, arg2: double): $Color
public static "hsl"(arg0: double, arg1: double, arg2: double, arg3: double): $Color
public "getOpacity"(): double
get "hue"(): double
get "lightness"(): double
get "saturation"(): double
get "red"(): integer
get "green"(): integer
get "blue"(): integer
get "opacity"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Color$Type = ($Color);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Color_ = $Color$Type;
}}
declare module "packages/snownee/jade/api/$EntityAccessor" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$EntityHitResult, $EntityHitResult$Type} from "packages/net/minecraft/world/phys/$EntityHitResult"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $EntityAccessor extends $Accessor<($EntityHitResult)> {

 "getAccessorType"(): $Class<(any)>
 "getEntity"(): $Entity
 "getRawEntity"(): $Entity
 "getTarget"(): any
 "getLevel"(): $Level
 "getPickedResult"(): $ItemStack
 "showDetails"(): boolean
 "toNetwork"(arg0: $FriendlyByteBuf$Type): void
 "isServerConnected"(): boolean
 "getServerData"(): $CompoundTag
 "getPlayer"(): $Player
 "verifyData"(arg0: $CompoundTag$Type): boolean
 "getHitResult"(): $EntityHitResult
}

export namespace $EntityAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityAccessor$Type = ($EntityAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityAccessor_ = $EntityAccessor$Type;
}}
declare module "packages/snownee/jade/api/$EntityAccessor$Builder" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$EntityHitResult, $EntityHitResult$Type} from "packages/net/minecraft/world/phys/$EntityHitResult"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $EntityAccessor$Builder {

 "from"(arg0: $EntityAccessor$Type): $EntityAccessor$Builder
 "build"(): $EntityAccessor
 "level"(arg0: $Level$Type): $EntityAccessor$Builder
 "hit"(arg0: $Supplier$Type<($EntityHitResult$Type)>): $EntityAccessor$Builder
 "hit"(arg0: $EntityHitResult$Type): $EntityAccessor$Builder
 "player"(arg0: $Player$Type): $EntityAccessor$Builder
 "serverData"(arg0: $CompoundTag$Type): $EntityAccessor$Builder
 "showDetails"(arg0: boolean): $EntityAccessor$Builder
 "entity"(arg0: $Supplier$Type<($Entity$Type)>): $EntityAccessor$Builder
 "entity"(arg0: $Entity$Type): $EntityAccessor$Builder
 "requireVerification"(): $EntityAccessor$Builder
 "serverConnected"(arg0: boolean): $EntityAccessor$Builder
}

export namespace $EntityAccessor$Builder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityAccessor$Builder$Type = ($EntityAccessor$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityAccessor$Builder_ = $EntityAccessor$Builder$Type;
}}
declare module "packages/snownee/jade/api/$IJadeProvider" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $IJadeProvider {

 "getUid"(): $ResourceLocation
 "getDefaultPriority"(): integer

(): $ResourceLocation
}

export namespace $IJadeProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJadeProvider$Type = ($IJadeProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJadeProvider_ = $IJadeProvider$Type;
}}
declare module "packages/snownee/jade/addon/create/$ContraptionExactBlockProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ContraptionExactBlockProvider extends $Enum<($ContraptionExactBlockProvider)> implements $IEntityComponentProvider {
static readonly "INSTANCE": $ContraptionExactBlockProvider


public static "values"(): ($ContraptionExactBlockProvider)[]
public static "valueOf"(arg0: string): $ContraptionExactBlockProvider
public "getUid"(): $ResourceLocation
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "getDefaultPriority"(): integer
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "setHit"(arg0: $Entity$Type, arg1: $Accessor$Type<(any)>): void
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
get "required"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContraptionExactBlockProvider$Type = (("instance")) | ($ContraptionExactBlockProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContraptionExactBlockProvider_ = $ContraptionExactBlockProvider$Type;
}}
declare module "packages/snownee/jade/api/ui/$IBoxElement" {
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$IElement$Align, $IElement$Align$Type} from "packages/snownee/jade/api/ui/$IElement$Align"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ITooltipRenderer, $ITooltipRenderer$Type} from "packages/snownee/jade/api/ui/$ITooltipRenderer"

export interface $IBoxElement extends $IElement {

 "getTooltipRenderer"(): $ITooltipRenderer
 "size"(arg0: $Vec2$Type): $IElement
 "getMessage"(): string
 "message"(arg0: string): $IElement
 "getSize"(): $Vec2
 "tag"(arg0: $ResourceLocation$Type): $IElement
 "getTag"(): $ResourceLocation
 "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
 "align"(arg0: $IElement$Align$Type): $IElement
 "getAlignment"(): $IElement$Align
 "translate"(arg0: $Vec2$Type): $IElement
 "getTranslation"(): $Vec2
 "getCachedMessage"(): string
 "clearCachedMessage"(): $IElement
 "getCachedSize"(): $Vec2
}

export namespace $IBoxElement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IBoxElement$Type = ($IBoxElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IBoxElement_ = $IBoxElement$Type;
}}
declare module "packages/snownee/jade/network/$ReceiveDataPacket" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export class $ReceiveDataPacket {
 "tag": $CompoundTag

constructor(arg0: $CompoundTag$Type)

public static "write"(arg0: $ReceiveDataPacket$Type, arg1: $FriendlyByteBuf$Type): void
public static "read"(arg0: $FriendlyByteBuf$Type): $ReceiveDataPacket
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReceiveDataPacket$Type = ($ReceiveDataPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReceiveDataPacket_ = $ReceiveDataPacket$Type;
}}
declare module "packages/snownee/jade/api/ui/$IProgressStyle" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $IProgressStyle {

 "color"(arg0: integer): $IProgressStyle
 "color"(arg0: integer, arg1: integer): $IProgressStyle
 "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Component$Type): void
 "overlay"(arg0: $IElement$Type): $IProgressStyle
 "textColor"(arg0: integer): $IProgressStyle
 "vertical"(arg0: boolean): $IProgressStyle
}

export namespace $IProgressStyle {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IProgressStyle$Type = ($IProgressStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IProgressStyle_ = $IProgressStyle$Type;
}}
declare module "packages/snownee/jade/addon/universal/$EntityFluidStorageProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $EntityFluidStorageProvider extends $Enum<($EntityFluidStorageProvider)> implements $IEntityComponentProvider, $IServerDataProvider<($EntityAccessor)> {
static readonly "INSTANCE": $EntityFluidStorageProvider


public static "values"(): ($EntityFluidStorageProvider)[]
public static "valueOf"(arg0: string): $EntityFluidStorageProvider
public "getUid"(): $ResourceLocation
public "getDefaultPriority"(): integer
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $EntityAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
get "required"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityFluidStorageProvider$Type = (("instance")) | ($EntityFluidStorageProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityFluidStorageProvider_ = $EntityFluidStorageProvider$Type;
}}
declare module "packages/snownee/jade/impl/ui/$BoxElement" {
import {$Tooltip, $Tooltip$Type} from "packages/snownee/jade/impl/$Tooltip"
import {$IBoxElement, $IBoxElement$Type} from "packages/snownee/jade/api/ui/$IBoxElement"
import {$IBoxStyle, $IBoxStyle$Type} from "packages/snownee/jade/api/ui/$IBoxStyle"
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$Element, $Element$Type} from "packages/snownee/jade/api/ui/$Element"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ITooltipRenderer, $ITooltipRenderer$Type} from "packages/snownee/jade/api/ui/$ITooltipRenderer"

export class $BoxElement extends $Element implements $IBoxElement {

constructor(arg0: $Tooltip$Type, arg1: $IBoxStyle$Type)

public "getMessage"(): string
public "getSize"(): $Vec2
public "tag"(arg0: $ResourceLocation$Type): $IElement
public "render"(arg0: $GuiGraphics$Type, arg1: float, arg2: float, arg3: float, arg4: float): void
public "getTooltipRenderer"(): $ITooltipRenderer
get "message"(): string
get "size"(): $Vec2
get "tooltipRenderer"(): $ITooltipRenderer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BoxElement$Type = ($BoxElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BoxElement_ = $BoxElement$Type;
}}
