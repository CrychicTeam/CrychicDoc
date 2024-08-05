declare module "packages/fuzs/overflowingbars/mixin/$ModMixinConfigPlugin" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ModMixinConfigPlugin implements $IMixinConfigPlugin {

constructor()

public "onLoad"(arg0: string): void
public "postApply"(arg0: string, arg1: $ClassNode$Type, arg2: string, arg3: $IMixinInfo$Type): void
public "getMixins"(): $List<(string)>
public "getRefMapperConfig"(): string
public "shouldApplyMixin"(arg0: string, arg1: string): boolean
public "preApply"(arg0: string, arg1: $ClassNode$Type, arg2: string, arg3: $IMixinInfo$Type): void
public "acceptTargets"(arg0: $Set$Type<(string)>, arg1: $Set$Type<(string)>): void
get "mixins"(): $List<(string)>
get "refMapperConfig"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModMixinConfigPlugin$Type = ($ModMixinConfigPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModMixinConfigPlugin_ = $ModMixinConfigPlugin$Type;
}}
declare module "packages/fuzs/overflowingbars/client/handler/$ArmorBarRenderer" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ProfilerFiller, $ProfilerFiller$Type} from "packages/net/minecraft/util/profiling/$ProfilerFiller"
import {$ClientConfig$ArmorRowConfig, $ClientConfig$ArmorRowConfig$Type} from "packages/fuzs/overflowingbars/config/$ClientConfig$ArmorRowConfig"

export class $ArmorBarRenderer {

constructor()

public static "renderToughnessBar"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $Player$Type, arg4: $ProfilerFiller$Type, arg5: boolean, arg6: boolean): void
public static "renderArmorBar"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $Player$Type, arg4: $ProfilerFiller$Type, arg5: boolean): void
public static "renderArmorBar"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: boolean, arg6: boolean, arg7: $ClientConfig$ArmorRowConfig$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArmorBarRenderer$Type = ($ArmorBarRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArmorBarRenderer_ = $ArmorBarRenderer$Type;
}}
declare module "packages/fuzs/overflowingbars/config/$ClientConfig" {
import {$ValueCallback, $ValueCallback$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ValueCallback"
import {$ClientConfig$RowCountConfig, $ClientConfig$RowCountConfig$Type} from "packages/fuzs/overflowingbars/config/$ClientConfig$RowCountConfig"
import {$ConfigCore, $ConfigCore$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigCore"
import {$ClientConfig$ToughnessRowConfig, $ClientConfig$ToughnessRowConfig$Type} from "packages/fuzs/overflowingbars/config/$ClientConfig$ToughnessRowConfig"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"
import {$ClientConfig$IconRowConfig, $ClientConfig$IconRowConfig$Type} from "packages/fuzs/overflowingbars/config/$ClientConfig$IconRowConfig"
import {$ClientConfig$ArmorRowConfig, $ClientConfig$ArmorRowConfig$Type} from "packages/fuzs/overflowingbars/config/$ClientConfig$ArmorRowConfig"

export class $ClientConfig implements $ConfigCore {
 "health": $ClientConfig$IconRowConfig
 "armor": $ClientConfig$ArmorRowConfig
 "toughness": $ClientConfig$ToughnessRowConfig
 "rowCount": $ClientConfig$RowCountConfig
 "moveChatAboveArmor": boolean
 "moveExperienceAboveBar": boolean

constructor()

public "addToBuilder"(arg0: $ForgeConfigSpec$Builder$Type, arg1: $ValueCallback$Type): void
public "afterConfigReload"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientConfig$Type = ($ClientConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientConfig_ = $ClientConfig$Type;
}}
declare module "packages/fuzs/overflowingbars/config/$ClientConfig$ArmorRowConfig" {
import {$ClientConfig$IconRowConfig, $ClientConfig$IconRowConfig$Type} from "packages/fuzs/overflowingbars/config/$ClientConfig$IconRowConfig"

export class $ClientConfig$ArmorRowConfig extends $ClientConfig$IconRowConfig {
 "skipEmptyArmorPoints": boolean
 "allowLayers": boolean
 "allowCount": boolean
 "colorizeFirstRow": boolean
 "inverseColoring": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientConfig$ArmorRowConfig$Type = ($ClientConfig$ArmorRowConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientConfig$ArmorRowConfig_ = $ClientConfig$ArmorRowConfig$Type;
}}
declare module "packages/fuzs/overflowingbars/client/handler/$BarOverlayRenderer" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $BarOverlayRenderer {

constructor()

public static "renderHealthLevelBars"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $Minecraft$Type, arg4: integer, arg5: boolean): void
public static "renderToughnessLevelBar"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $Minecraft$Type, arg4: integer, arg5: boolean, arg6: boolean, arg7: boolean): void
public static "renderArmorLevelBar"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $Minecraft$Type, arg4: integer, arg5: boolean, arg6: boolean): void
public static "resetRenderState"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BarOverlayRenderer$Type = ($BarOverlayRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BarOverlayRenderer_ = $BarOverlayRenderer$Type;
}}
declare module "packages/fuzs/overflowingbars/client/helper/$ChatOffsetHelper" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export class $ChatOffsetHelper {

constructor()

public static "armorRow"(arg0: $Player$Type): boolean
public static "twoHealthRows"(arg0: $Player$Type): boolean
public static "toughnessRow"(arg0: $Player$Type): boolean
public static "getChatOffsetY"(): integer
get "chatOffsetY"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChatOffsetHelper$Type = ($ChatOffsetHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChatOffsetHelper_ = $ChatOffsetHelper$Type;
}}
declare module "packages/fuzs/overflowingbars/config/$ClientConfig$RowCountConfig" {
import {$ValueCallback, $ValueCallback$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ValueCallback"
import {$ConfigCore, $ConfigCore$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigCore"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"

export class $ClientConfig$RowCountConfig implements $ConfigCore {
 "rowCountColor": $ChatFormatting
 "forceFontRenderer": boolean
 "countFullRowsOnly": boolean
 "alwaysRenderRowCount": boolean
 "rowCountX": boolean

constructor()

public "addToBuilder"(arg0: $ForgeConfigSpec$Builder$Type, arg1: $ValueCallback$Type): void
public "afterConfigReload"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientConfig$RowCountConfig$Type = ($ClientConfig$RowCountConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientConfig$RowCountConfig_ = $ClientConfig$RowCountConfig$Type;
}}
declare module "packages/fuzs/overflowingbars/client/$OverflowingBarsClient" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$ClientTooltipComponentsContext, $ClientTooltipComponentsContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ClientTooltipComponentsContext"
import {$LayerDefinitionsContext, $LayerDefinitionsContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$LayerDefinitionsContext"
import {$BlockColor, $BlockColor$Type} from "packages/net/minecraft/client/color/block/$BlockColor"
import {$AdditionalModelsContext, $AdditionalModelsContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$AdditionalModelsContext"
import {$ClientModConstructor, $ClientModConstructor$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/$ClientModConstructor"
import {$ContentRegistrationFlags, $ContentRegistrationFlags$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ContentRegistrationFlags"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$SearchRegistryContext, $SearchRegistryContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$SearchRegistryContext"
import {$ItemDecorationContext, $ItemDecorationContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ItemDecorationContext"
import {$DynamicModifyBakingResultContext, $DynamicModifyBakingResultContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$DynamicModifyBakingResultContext"
import {$SkullRenderersContext, $SkullRenderersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$SkullRenderersContext"
import {$CoreShadersContext, $CoreShadersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$CoreShadersContext"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ColorProvidersContext, $ColorProvidersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ColorProvidersContext"
import {$BlockEntityRenderersContext, $BlockEntityRenderersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$BlockEntityRenderersContext"
import {$DynamicBakingCompletedContext, $DynamicBakingCompletedContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$DynamicBakingCompletedContext"
import {$BuiltinModelItemRendererContext, $BuiltinModelItemRendererContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$BuiltinModelItemRendererContext"
import {$EntityRenderersContext, $EntityRenderersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$EntityRenderersContext"
import {$RenderTypesContext, $RenderTypesContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$RenderTypesContext"
import {$ItemColor, $ItemColor$Type} from "packages/net/minecraft/client/color/item/$ItemColor"
import {$PackRepositorySourcesContext, $PackRepositorySourcesContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$PackRepositorySourcesContext"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$KeyMappingsContext, $KeyMappingsContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$KeyMappingsContext"
import {$ParticleProvidersContext, $ParticleProvidersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ParticleProvidersContext"
import {$ModLifecycleContext, $ModLifecycleContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$ModLifecycleContext"
import {$AddReloadListenersContext, $AddReloadListenersContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$AddReloadListenersContext"
import {$EntitySpectatorShaderContext, $EntitySpectatorShaderContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$EntitySpectatorShaderContext"
import {$LivingEntityRenderLayersContext, $LivingEntityRenderLayersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$LivingEntityRenderLayersContext"
import {$ItemModelPropertiesContext, $ItemModelPropertiesContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ItemModelPropertiesContext"

export class $OverflowingBarsClient implements $ClientModConstructor {

constructor()

public "onConstructMod"(): void
public static "construct"(arg0: string, arg1: $Supplier$Type<($ClientModConstructor$Type)>): void
/**
 * 
 * @deprecated
 */
public static "construct"(arg0: string, arg1: $Supplier$Type<($ClientModConstructor$Type)>, ...arg2: ($ContentRegistrationFlags$Type)[]): void
public "onRegisterParticleProviders"(arg0: $ParticleProvidersContext$Type): void
/**
 * 
 * @deprecated
 */
public "onBakingCompleted"(arg0: $DynamicBakingCompletedContext$Type): void
/**
 * 
 * @deprecated
 */
public "onModifyBakingResult"(arg0: $DynamicModifyBakingResultContext$Type): void
public "onRegisterBuiltinModelItemRenderers"(arg0: $BuiltinModelItemRendererContext$Type): void
public "onRegisterResourcePackReloadListeners"(arg0: $AddReloadListenersContext$Type): void
/**
 * 
 * @deprecated
 */
public "onClientSetup"(arg0: $ModLifecycleContext$Type): void
public "onClientSetup"(): void
public "onRegisterFluidRenderTypes"(arg0: $RenderTypesContext$Type<($Fluid$Type)>): void
public "onRegisterEntitySpectatorShaders"(arg0: $EntitySpectatorShaderContext$Type): void
public "onRegisterLivingEntityRenderLayers"(arg0: $LivingEntityRenderLayersContext$Type): void
public "onRegisterEntityRenderers"(arg0: $EntityRenderersContext$Type): void
public "onRegisterLayerDefinitions"(arg0: $LayerDefinitionsContext$Type): void
public "onRegisterItemModelProperties"(arg0: $ItemModelPropertiesContext$Type): void
public "onRegisterBlockColorProviders"(arg0: $ColorProvidersContext$Type<($Block$Type), ($BlockColor$Type)>): void
public "onRegisterCoreShaders"(arg0: $CoreShadersContext$Type): void
public "onRegisterItemDecorations"(arg0: $ItemDecorationContext$Type): void
/**
 * 
 * @deprecated
 */
public "onRegisterSearchTrees"(arg0: $SearchRegistryContext$Type): void
public "onRegisterSkullRenderers"(arg0: $SkullRenderersContext$Type): void
public "onAddResourcePackFinders"(arg0: $PackRepositorySourcesContext$Type): void
public "onRegisterItemColorProviders"(arg0: $ColorProvidersContext$Type<($Item$Type), ($ItemColor$Type)>): void
public "onRegisterBlockEntityRenderers"(arg0: $BlockEntityRenderersContext$Type): void
public "onRegisterClientTooltipComponents"(arg0: $ClientTooltipComponentsContext$Type): void
public "onRegisterBlockRenderTypes"(arg0: $RenderTypesContext$Type<($Block$Type)>): void
public "onRegisterKeyMappings"(arg0: $KeyMappingsContext$Type): void
public "onRegisterAdditionalModels"(arg0: $AdditionalModelsContext$Type): void
public "getContentRegistrationFlags"(): ($ContentRegistrationFlags)[]
public "getPairingIdentifier"(): $ResourceLocation
get "contentRegistrationFlags"(): ($ContentRegistrationFlags)[]
get "pairingIdentifier"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OverflowingBarsClient$Type = ($OverflowingBarsClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OverflowingBarsClient_ = $OverflowingBarsClient$Type;
}}
declare module "packages/fuzs/overflowingbars/config/$ClientConfig$IconRowConfig" {
import {$ValueCallback, $ValueCallback$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ValueCallback"
import {$ConfigCore, $ConfigCore$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigCore"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"

export class $ClientConfig$IconRowConfig implements $ConfigCore {
 "allowLayers": boolean
 "allowCount": boolean
 "colorizeFirstRow": boolean
 "inverseColoring": boolean

constructor()

public "addToBuilder"(arg0: $ForgeConfigSpec$Builder$Type, arg1: $ValueCallback$Type): void
public "afterConfigReload"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientConfig$IconRowConfig$Type = ($ClientConfig$IconRowConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientConfig$IconRowConfig_ = $ClientConfig$IconRowConfig$Type;
}}
declare module "packages/fuzs/overflowingbars/config/$ClientConfig$ToughnessRowConfig" {
import {$ClientConfig$ArmorRowConfig, $ClientConfig$ArmorRowConfig$Type} from "packages/fuzs/overflowingbars/config/$ClientConfig$ArmorRowConfig"

export class $ClientConfig$ToughnessRowConfig extends $ClientConfig$ArmorRowConfig {
 "armorToughnessBar": boolean
 "toughnessBarRowShift": integer
 "leftSide": boolean
 "skipEmptyArmorPoints": boolean
 "allowLayers": boolean
 "allowCount": boolean
 "colorizeFirstRow": boolean
 "inverseColoring": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientConfig$ToughnessRowConfig$Type = ($ClientConfig$ToughnessRowConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientConfig$ToughnessRowConfig_ = $ClientConfig$ToughnessRowConfig$Type;
}}
declare module "packages/fuzs/overflowingbars/client/$OverflowingBarsForgeClient" {
import {$RegisterGuiOverlaysEvent, $RegisterGuiOverlaysEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterGuiOverlaysEvent"
import {$FMLConstructModEvent, $FMLConstructModEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLConstructModEvent"

export class $OverflowingBarsForgeClient {

constructor()

public static "onRegisterGuiOverlays"(arg0: $RegisterGuiOverlaysEvent$Type): void
public static "onConstructMod"(arg0: $FMLConstructModEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OverflowingBarsForgeClient$Type = ($OverflowingBarsForgeClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OverflowingBarsForgeClient_ = $OverflowingBarsForgeClient$Type;
}}
declare module "packages/fuzs/overflowingbars/client/handler/$HealthBarRenderer" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ProfilerFiller, $ProfilerFiller$Type} from "packages/net/minecraft/util/profiling/$ProfilerFiller"

export class $HealthBarRenderer {
static readonly "INSTANCE": $HealthBarRenderer

constructor()

public "onStartTick"(arg0: $Minecraft$Type): void
public "renderPlayerHealth"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $Player$Type, arg4: $ProfilerFiller$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HealthBarRenderer$Type = ($HealthBarRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HealthBarRenderer_ = $HealthBarRenderer$Type;
}}
declare module "packages/fuzs/overflowingbars/client/handler/$GuiOverlayHandler" {
import {$RenderGuiOverlayEvent$Pre, $RenderGuiOverlayEvent$Pre$Type} from "packages/net/minecraftforge/client/event/$RenderGuiOverlayEvent$Pre"
import {$IGuiOverlay, $IGuiOverlay$Type} from "packages/net/minecraftforge/client/gui/overlay/$IGuiOverlay"

export class $GuiOverlayHandler {
static readonly "TOUGHNESS_LEVEL": $IGuiOverlay

constructor()

public static "onBeforeRenderGuiOverlay"(arg0: $RenderGuiOverlayEvent$Pre$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiOverlayHandler$Type = ($GuiOverlayHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiOverlayHandler_ = $GuiOverlayHandler$Type;
}}
declare module "packages/fuzs/overflowingbars/$OverflowingBarsForge" {
import {$FMLConstructModEvent, $FMLConstructModEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLConstructModEvent"

export class $OverflowingBarsForge {

constructor()

public static "onConstructMod"(arg0: $FMLConstructModEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OverflowingBarsForge$Type = ($OverflowingBarsForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OverflowingBarsForge_ = $OverflowingBarsForge$Type;
}}
declare module "packages/fuzs/overflowingbars/client/handler/$RowCountRenderer" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $RowCountRenderer {

constructor()

public static "drawBarRowCount"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: boolean, arg5: $Font$Type): void
public static "drawBarRowCount"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: boolean, arg5: integer, arg6: $Font$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RowCountRenderer$Type = ($RowCountRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RowCountRenderer_ = $RowCountRenderer$Type;
}}
declare module "packages/fuzs/overflowingbars/$OverflowingBars" {
import {$FlammableBlocksContext, $FlammableBlocksContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$FlammableBlocksContext"
import {$CreativeModeTabContext, $CreativeModeTabContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$CreativeModeTabContext"
import {$BlockInteractionsContext, $BlockInteractionsContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$BlockInteractionsContext"
import {$BiomeModificationsContext, $BiomeModificationsContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$BiomeModificationsContext"
import {$ContentRegistrationFlags, $ContentRegistrationFlags$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ContentRegistrationFlags"
import {$BuildCreativeModeTabContentsContext, $BuildCreativeModeTabContentsContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$BuildCreativeModeTabContentsContext"
import {$ConfigHolder, $ConfigHolder$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigHolder"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$EntityAttributesCreateContext, $EntityAttributesCreateContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$EntityAttributesCreateContext"
import {$PackRepositorySourcesContext, $PackRepositorySourcesContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$PackRepositorySourcesContext"
import {$EntityAttributesModifyContext, $EntityAttributesModifyContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$EntityAttributesModifyContext"
import {$SpawnPlacementsContext, $SpawnPlacementsContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$SpawnPlacementsContext"
import {$FuelBurnTimesContext, $FuelBurnTimesContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$FuelBurnTimesContext"
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"
import {$ModLifecycleContext, $ModLifecycleContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$ModLifecycleContext"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AddReloadListenersContext, $AddReloadListenersContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$AddReloadListenersContext"
import {$ModConstructor, $ModConstructor$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ModConstructor"

export class $OverflowingBars implements $ModConstructor {
static readonly "MOD_ID": string
static readonly "MOD_NAME": string
static readonly "LOGGER": $Logger
static readonly "CONFIG": $ConfigHolder

constructor()

public static "id"(arg0: string): $ResourceLocation
public static "construct"(arg0: string, arg1: $Supplier$Type<($ModConstructor$Type)>): void
/**
 * 
 * @deprecated
 */
public static "construct"(arg0: string, arg1: $Supplier$Type<($ModConstructor$Type)>, ...arg2: ($ContentRegistrationFlags$Type)[]): void
public "onConstructMod"(): void
public "onBuildCreativeModeTabContents"(arg0: $BuildCreativeModeTabContentsContext$Type): void
public "onCommonSetup"(): void
/**
 * 
 * @deprecated
 */
public "onCommonSetup"(arg0: $ModLifecycleContext$Type): void
public "onAddDataPackFinders"(arg0: $PackRepositorySourcesContext$Type): void
public "onRegisterCreativeModeTabs"(arg0: $CreativeModeTabContext$Type): void
public "onRegisterFuelBurnTimes"(arg0: $FuelBurnTimesContext$Type): void
public "onRegisterSpawnPlacements"(arg0: $SpawnPlacementsContext$Type): void
public "onRegisterFlammableBlocks"(arg0: $FlammableBlocksContext$Type): void
public "onRegisterBlockInteractions"(arg0: $BlockInteractionsContext$Type): void
public "onRegisterDataPackReloadListeners"(arg0: $AddReloadListenersContext$Type): void
public "onEntityAttributeModification"(arg0: $EntityAttributesModifyContext$Type): void
public "onEntityAttributeCreation"(arg0: $EntityAttributesCreateContext$Type): void
public "onRegisterBiomeModifications"(arg0: $BiomeModificationsContext$Type): void
public "getContentRegistrationFlags"(): ($ContentRegistrationFlags)[]
public "getPairingIdentifier"(): $ResourceLocation
get "contentRegistrationFlags"(): ($ContentRegistrationFlags)[]
get "pairingIdentifier"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OverflowingBars$Type = ($OverflowingBars);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OverflowingBars_ = $OverflowingBars$Type;
}}
