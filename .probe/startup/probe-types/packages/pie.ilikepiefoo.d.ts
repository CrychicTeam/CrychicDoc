declare module "packages/pie/ilikepiefoo/compat/jade/builder/$ServerExtensionProviderBuilder" {
import {$JadeProviderBuilder, $JadeProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$JadeProviderBuilder"
import {$GetServerGroupsCallbackJS, $GetServerGroupsCallbackJS$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/callback/$GetServerGroupsCallbackJS"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ServerExtensionProviderBuilder<IN, OUT> extends $JadeProviderBuilder {
 "callback": $Consumer<($GetServerGroupsCallbackJS<(IN), (OUT)>)>

constructor(uniqueIdentifier: $ResourceLocation$Type)

public "callback"(callback: $Consumer$Type<($GetServerGroupsCallbackJS$Type<(IN), (OUT)>)>): $ServerExtensionProviderBuilder<(IN), (OUT)>
public static "doNothing"<IN, OUT>(callback: $GetServerGroupsCallbackJS$Type<(IN), (OUT)>): void
public "getCallback"(): $Consumer<($GetServerGroupsCallbackJS<(IN), (OUT)>)>
public "onGroups"(callback: $Consumer$Type<($GetServerGroupsCallbackJS$Type<(IN), (OUT)>)>): $ServerExtensionProviderBuilder<(IN), (OUT)>
public "groupCallback"(callback: $Consumer$Type<($GetServerGroupsCallbackJS$Type<(IN), (OUT)>)>): $ServerExtensionProviderBuilder<(IN), (OUT)>
public "setCallback"(callback: $Consumer$Type<($GetServerGroupsCallbackJS$Type<(IN), (OUT)>)>): $ServerExtensionProviderBuilder<(IN), (OUT)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerExtensionProviderBuilder$Type<IN, OUT> = ($ServerExtensionProviderBuilder<(IN), (OUT)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerExtensionProviderBuilder_<IN, OUT> = $ServerExtensionProviderBuilder$Type<(IN), (OUT)>;
}}
declare module "packages/pie/ilikepiefoo/events/custom/$ArchEventRegisterEventJS" {
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $ArchEventRegisterEventJS extends $EventJS {
static readonly "LOG": $Logger

constructor()

public "register"(name: string, eventProvider: $Class$Type<(any)>, fieldName: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArchEventRegisterEventJS$Type = ($ArchEventRegisterEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArchEventRegisterEventJS_ = $ArchEventRegisterEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/events/$RegisterItemSubtypeEventJS" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$JEIEventJS, $JEIEventJS$Type} from "packages/pie/ilikepiefoo/compat/jei/events/$JEIEventJS"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ISubtypeRegistration, $ISubtypeRegistration$Type} from "packages/mezz/jei/api/registration/$ISubtypeRegistration"
import {$CustomJSRecipe, $CustomJSRecipe$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomJSRecipe"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $RegisterItemSubtypeEventJS extends $JEIEventJS {
readonly "data": $ISubtypeRegistration
static readonly "customRecipeTypes": $Map<($ResourceLocation), ($RecipeType<($CustomJSRecipe)>)>
static readonly "overriddenRecipeTypes": $Map<($ResourceLocation), ($RecipeType)>
static "JEI_HELPERS": $IJeiHelpers

constructor(data: $ISubtypeRegistration$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegisterItemSubtypeEventJS$Type = ($RegisterItemSubtypeEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegisterItemSubtypeEventJS_ = $RegisterItemSubtypeEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/impl/$CustomServerExtensionProvider" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IServerExtensionProvider, $IServerExtensionProvider$Type} from "packages/snownee/jade/api/view/$IServerExtensionProvider"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$CustomJadeProvider, $CustomJadeProvider$Type} from "packages/pie/ilikepiefoo/compat/jade/impl/$CustomJadeProvider"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"
import {$ServerExtensionProviderBuilder, $ServerExtensionProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$ServerExtensionProviderBuilder"

export class $CustomServerExtensionProvider<IN, OUT> extends $CustomJadeProvider<($ServerExtensionProviderBuilder<(IN), (OUT)>)> implements $IServerExtensionProvider<(IN), (OUT)> {

constructor(builder: $ServerExtensionProviderBuilder$Type<(IN), (OUT)>)

public "getGroups"(serverPlayer: $ServerPlayer$Type, serverLevel: $ServerLevel$Type, arg2: IN, b: boolean): $List<($ViewGroup<(OUT)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomServerExtensionProvider$Type<IN, OUT> = ($CustomServerExtensionProvider<(IN), (OUT)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomServerExtensionProvider_<IN, OUT> = $CustomServerExtensionProvider$Type<(IN), (OUT)>;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/impl/$CustomBlockComponentProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$BlockComponentProviderBuilder, $BlockComponentProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$BlockComponentProviderBuilder"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$CustomToggleableProviderBuilder, $CustomToggleableProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/impl/$CustomToggleableProviderBuilder"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $CustomBlockComponentProvider extends $CustomToggleableProviderBuilder<($BlockComponentProviderBuilder)> implements $IBlockComponentProvider {

constructor(builder: $BlockComponentProviderBuilder$Type)

public "getIcon"(accessor: $BlockAccessor$Type, config: $IPluginConfig$Type, currentIcon: $IElement$Type): $IElement
public "appendTooltip"(iTooltip: $ITooltip$Type, blockAccessor: $BlockAccessor$Type, iPluginConfig: $IPluginConfig$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomBlockComponentProvider$Type = ($CustomBlockComponentProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomBlockComponentProvider_ = $CustomBlockComponentProvider$Type;
}}
declare module "packages/pie/ilikepiefoo/events/$PlayerRespawnEventJS" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$PlayerEventJS, $PlayerEventJS$Type} from "packages/dev/latvian/mods/kubejs/player/$PlayerEventJS"

export class $PlayerRespawnEventJS extends $PlayerEventJS {

constructor(player: $ServerPlayer$Type, conqueredEnd: boolean)

public static "of"(player: $ServerPlayer$Type, conqueredEnd: boolean): $PlayerRespawnEventJS
public "causedByDeath"(): boolean
public "leavingEnd"(): boolean
public "causedByPortal"(): boolean
public "returningFromEnd"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerRespawnEventJS$Type = ($PlayerRespawnEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerRespawnEventJS_ = $PlayerRespawnEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/util/$EventAdapter" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$InvocationHandler, $InvocationHandler$Type} from "packages/java/lang/reflect/$InvocationHandler"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$EventHandler, $EventHandler$Type} from "packages/dev/latvian/mods/kubejs/event/$EventHandler"
import {$Method, $Method$Type} from "packages/java/lang/reflect/$Method"

export class $EventAdapter<T> implements $InvocationHandler {
static readonly "LOG": $Logger
readonly "name": string
readonly "handler": T
readonly "eventClass": $Class<(T)>
readonly "customMethods": $Set<($Method)>
readonly "handlers": ($EventHandler)[]

constructor(eventClass: $Class$Type<(T)>, eventName: string, ...handlers: ($EventHandler$Type)[])

public "invoke"(proxy: any, method: $Method$Type, args: (any)[]): any
public static "invokeDefault"(arg0: any, arg1: $Method$Type, ...arg2: (any)[]): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventAdapter$Type<T> = ($EventAdapter<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventAdapter_<T> = $EventAdapter$Type<(T)>;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/$ITooltipWrapper" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IElement$Align, $IElement$Align$Type} from "packages/snownee/jade/api/ui/$IElement$Align"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IElementHelper, $IElementHelper$Type} from "packages/snownee/jade/api/ui/$IElementHelper"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $ITooltipWrapper {
readonly "tooltip": $ITooltip

constructor(tooltip: $ITooltip$Type)

public "add"(index: integer, component: $Component$Type): void
public "add"(component: $Component$Type, tag: $ResourceLocation$Type): void
public "add"(element: $IElement$Type): void
public "add"(index: integer, component: $Component$Type, tag: $ResourceLocation$Type): void
public "add"(index: integer, elements: $List$Type<($IElement$Type)>): void
public "add"(i: integer, iElement: $IElement$Type): void
public "add"(component: $Component$Type): void
public "remove"(resourceLocation: $ResourceLocation$Type): void
public "get"(i: integer, align: $IElement$Align$Type): $List<($IElement)>
public "get"(resourceLocation: $ResourceLocation$Type): $List<($IElement)>
public "append"(index: integer, elements: $List$Type<($IElement$Type)>): void
public "append"(component: $Component$Type, tag: $ResourceLocation$Type): void
public "append"(element: $IElement$Type): void
public "append"(i: integer, iElement: $IElement$Type): void
public "append"(component: $Component$Type): void
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public static "of"(tooltip: $ITooltip$Type): $ITooltipWrapper
public "addAll"(components: $List$Type<($Component$Type)>): void
public "getMessage"(): string
public "getElementHelper"(): $IElementHelper
public "getTooltip"(): $ITooltip
public "addElements"(elements: $List$Type<($IElement$Type)>): void
get "empty"(): boolean
get "message"(): string
get "elementHelper"(): $IElementHelper
get "tooltip"(): $ITooltip
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITooltipWrapper$Type = ($ITooltipWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITooltipWrapper_ = $ITooltipWrapper$Type;
}}
declare module "packages/pie/ilikepiefoo/events/$PlayerChangeDimensionEventJS" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$PlayerEventJS, $PlayerEventJS$Type} from "packages/dev/latvian/mods/kubejs/player/$PlayerEventJS"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $PlayerChangeDimensionEventJS extends $PlayerEventJS {

constructor(player: $ServerPlayer$Type, oldWorld: $ResourceKey$Type<($Level$Type)>, newWorld: $ResourceKey$Type<($Level$Type)>)

public static "of"(player: $ServerPlayer$Type, oldLevel: $ResourceKey$Type<($Level$Type)>, newLevel: $ResourceKey$Type<($Level$Type)>): $PlayerChangeDimensionEventJS
public "getOldLevel"(): $Level
public "getNewWorldKey"(): $ResourceKey<($Level)>
public "getNewLevel"(): $Level
public "getOldWorldKey"(): $ResourceKey<($Level)>
public "getEntity"(): $Player
get "oldLevel"(): $Level
get "newWorldKey"(): $ResourceKey<($Level)>
get "newLevel"(): $Level
get "oldWorldKey"(): $ResourceKey<($Level)>
get "entity"(): $Player
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerChangeDimensionEventJS$Type = ($PlayerChangeDimensionEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerChangeDimensionEventJS_ = $PlayerChangeDimensionEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/builder/$JadeProviderBuilder" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $JadeProviderBuilder {

constructor(uniqueIdentifier: $ResourceLocation$Type)

public "getDefaultPriority"(): integer
public "setDefaultPriority"(priority: integer): $JadeProviderBuilder
public "getUniqueIdentifier"(): $ResourceLocation
get "defaultPriority"(): integer
set "defaultPriority"(value: integer)
get "uniqueIdentifier"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeProviderBuilder$Type = ($JadeProviderBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeProviderBuilder_ = $JadeProviderBuilder$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/impl/$CustomJSRecipe$CustomRecipeListBuilder" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$CustomJSRecipe, $CustomJSRecipe$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomJSRecipe"

export class $CustomJSRecipe$CustomRecipeListBuilder {

constructor(recipeType: $RecipeType$Type<($CustomJSRecipe$Type)>)

public "add"(recipe: $CustomJSRecipe$Type): $CustomJSRecipe$CustomRecipeListBuilder
public "add"(recipeData: any): $CustomJSRecipe$CustomRecipeListBuilder
public "addAll"(recipeData: $List$Type<(any)>): $CustomJSRecipe$CustomRecipeListBuilder
public "getRecipeType"(): $RecipeType<($CustomJSRecipe)>
public "custom"(recipeData: any): $CustomJSRecipe
public "getRecipes"(): $List<($CustomJSRecipe)>
get "recipeType"(): $RecipeType<($CustomJSRecipe)>
get "recipes"(): $List<($CustomJSRecipe)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomJSRecipe$CustomRecipeListBuilder$Type = ($CustomJSRecipe$CustomRecipeListBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomJSRecipe$CustomRecipeListBuilder_ = $CustomJSRecipe$CustomRecipeListBuilder$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/events/$RegisterFluidSubtypeEventJS" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$JEIEventJS, $JEIEventJS$Type} from "packages/pie/ilikepiefoo/compat/jei/events/$JEIEventJS"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ISubtypeRegistration, $ISubtypeRegistration$Type} from "packages/mezz/jei/api/registration/$ISubtypeRegistration"
import {$CustomJSRecipe, $CustomJSRecipe$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomJSRecipe"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $RegisterFluidSubtypeEventJS extends $JEIEventJS {
readonly "data": $ISubtypeRegistration
static readonly "customRecipeTypes": $Map<($ResourceLocation), ($RecipeType<($CustomJSRecipe)>)>
static readonly "overriddenRecipeTypes": $Map<($ResourceLocation), ($RecipeType)>
static "JEI_HELPERS": $IJeiHelpers

constructor(data: $ISubtypeRegistration$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegisterFluidSubtypeEventJS$Type = ($RegisterFluidSubtypeEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegisterFluidSubtypeEventJS_ = $RegisterFluidSubtypeEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/$JEIDrawableWrapper" {
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"

export class $JEIDrawableWrapper {

constructor()

public static "of"(o: any): $IDrawable
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEIDrawableWrapper$Type = ($JEIDrawableWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEIDrawableWrapper_ = $JEIDrawableWrapper$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/builder/$RecipeCategoryBuilder$IsRecipeHandledByCategory" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecipeCategoryBuilder$IsRecipeHandledByCategory<T> {

 "isHandled"(arg0: T): boolean

(arg0: T): boolean
}

export namespace $RecipeCategoryBuilder$IsRecipeHandledByCategory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeCategoryBuilder$IsRecipeHandledByCategory$Type<T> = ($RecipeCategoryBuilder$IsRecipeHandledByCategory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeCategoryBuilder$IsRecipeHandledByCategory_<T> = $RecipeCategoryBuilder$IsRecipeHandledByCategory$Type<(T)>;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/builder/$BlockComponentProviderBuilder" {
import {$ToggleableProviderBuilder, $ToggleableProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$ToggleableProviderBuilder"
import {$BlockComponentProviderBuilder$IconRetriever, $BlockComponentProviderBuilder$IconRetriever$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$BlockComponentProviderBuilder$IconRetriever"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockComponentProviderBuilder$TooltipRetriever, $BlockComponentProviderBuilder$TooltipRetriever$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$BlockComponentProviderBuilder$TooltipRetriever"

export class $BlockComponentProviderBuilder extends $ToggleableProviderBuilder {

constructor(uniqueIdentifier: $ResourceLocation$Type)

public "tooltip"(tooltipRetriever: $BlockComponentProviderBuilder$TooltipRetriever$Type): $BlockComponentProviderBuilder
public "icon"(iconRetriever: $BlockComponentProviderBuilder$IconRetriever$Type): $BlockComponentProviderBuilder
public "getTooltipRetriever"(): $BlockComponentProviderBuilder$TooltipRetriever
public "getIconRetriever"(): $BlockComponentProviderBuilder$IconRetriever
public "setTooltipRetriever"(tooltipRetriever: $BlockComponentProviderBuilder$TooltipRetriever$Type): $BlockComponentProviderBuilder
public "tooltipRetriever"(tooltipRetriever: $BlockComponentProviderBuilder$TooltipRetriever$Type): $BlockComponentProviderBuilder
public "setIconRetriever"(iconRetriever: $BlockComponentProviderBuilder$IconRetriever$Type): $BlockComponentProviderBuilder
public "iconRetriever"(iconRetriever: $BlockComponentProviderBuilder$IconRetriever$Type): $BlockComponentProviderBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockComponentProviderBuilder$Type = ($BlockComponentProviderBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockComponentProviderBuilder_ = $BlockComponentProviderBuilder$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/events/$RegisterVanillaCategoryExtensionsEventJS" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IVanillaCategoryExtensionRegistration, $IVanillaCategoryExtensionRegistration$Type} from "packages/mezz/jei/api/registration/$IVanillaCategoryExtensionRegistration"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$JEIEventJS, $JEIEventJS$Type} from "packages/pie/ilikepiefoo/compat/jei/events/$JEIEventJS"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CustomJSRecipe, $CustomJSRecipe$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomJSRecipe"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $RegisterVanillaCategoryExtensionsEventJS extends $JEIEventJS {
readonly "data": $IVanillaCategoryExtensionRegistration
static readonly "customRecipeTypes": $Map<($ResourceLocation), ($RecipeType<($CustomJSRecipe)>)>
static readonly "overriddenRecipeTypes": $Map<($ResourceLocation), ($RecipeType)>
static "JEI_HELPERS": $IJeiHelpers

constructor(data: $IVanillaCategoryExtensionRegistration$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegisterVanillaCategoryExtensionsEventJS$Type = ($RegisterVanillaCategoryExtensionsEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegisterVanillaCategoryExtensionsEventJS_ = $RegisterVanillaCategoryExtensionsEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/$WailaClientRegistrationEventJS" {
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
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ProgressView, $ProgressView$Type} from "packages/snownee/jade/api/view/$ProgressView"
import {$IClientExtensionProvider, $IClientExtensionProvider$Type} from "packages/snownee/jade/api/view/$IClientExtensionProvider"
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ClientExtensionProviderBuilder, $ClientExtensionProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$ClientExtensionProviderBuilder"
import {$BlockComponentProviderBuilder, $BlockComponentProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$BlockComponentProviderBuilder"
import {$IWailaClientRegistration, $IWailaClientRegistration$Type} from "packages/snownee/jade/api/$IWailaClientRegistration"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$EnergyView, $EnergyView$Type} from "packages/snownee/jade/api/view/$EnergyView"
import {$EntityComponentProviderBuilder, $EntityComponentProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$EntityComponentProviderBuilder"
import {$FluidView, $FluidView$Type} from "packages/snownee/jade/api/view/$FluidView"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"

export class $WailaClientRegistrationEventJS extends $EventJS {

constructor(registration: $IWailaClientRegistration$Type)

public "block"(location: $ResourceLocation$Type, block: $Class$Type<(any)>): $BlockComponentProviderBuilder
public "addAfterRenderCallback"(callback: $JadeAfterRenderCallback$Type): void
public "addAfterRenderCallback"(priority: integer, callback: $JadeAfterRenderCallback$Type): void
public "addBeforeRenderCallback"(callback: $JadeBeforeRenderCallback$Type): void
public "addBeforeRenderCallback"(priority: integer, callback: $JadeBeforeRenderCallback$Type): void
public "addTooltipCollectedCallback"(callback: $JadeTooltipCollectedCallback$Type): void
public "addTooltipCollectedCallback"(priority: integer, callback: $JadeTooltipCollectedCallback$Type): void
public "addRenderBackgroundCallback"(callback: $JadeRenderBackgroundCallback$Type): void
public "addRenderBackgroundCallback"(priority: integer, callback: $JadeRenderBackgroundCallback$Type): void
public "registerItemStorageClient"(provider: $IClientExtensionProvider$Type<($ItemStack$Type), ($ItemView$Type)>): void
public "registerProgressClient"(provider: $IClientExtensionProvider$Type<($CompoundTag$Type), ($ProgressView$Type)>): void
public "registerBlockComponent"(provider: $IBlockComponentProvider$Type, block: $Class$Type<(any)>): void
public "addRayTraceCallback"(callback: $JadeRayTraceCallback$Type): void
public "addRayTraceCallback"(priority: integer, callback: $JadeRayTraceCallback$Type): void
public "registerFluidStorageClient"(provider: $IClientExtensionProvider$Type<($CompoundTag$Type), ($FluidView$Type)>): void
public "registerEnergyStorageClient"(provider: $IClientExtensionProvider$Type<($CompoundTag$Type), ($EnergyView$Type)>): void
public "addItemModNameCallback"(priority: integer, callback: $JadeItemModNameCallback$Type): void
public "addItemModNameCallback"(callback: $JadeItemModNameCallback$Type): void
public "registerEntityComponent"(provider: $IEntityComponentProvider$Type, entity: $Class$Type<(any)>): void
public "addConfig"(key: $ResourceLocation$Type, defaultValue: boolean): void
public "addConfig"(key: $ResourceLocation$Type, defaultValue: float, min: float, max: float, slider: boolean): void
public "addConfig"(key: $ResourceLocation$Type, defaultValue: integer, min: integer, max: integer, slider: boolean): void
public "addConfig"(key: $ResourceLocation$Type, defaultValue: string, validator: $Predicate$Type<(string)>): void
public "addConfig"(key: $ResourceLocation$Type, defaultValue: $Enum$Type<(any)>): void
public "shouldHide"(state: $BlockState$Type): boolean
public "shouldHide"(target: $Entity$Type): boolean
public "shouldPick"(entity: $Entity$Type): boolean
public "shouldPick"(blockState: $BlockState$Type): boolean
public "blockAccessor"(): $BlockAccessor$Builder
public "addConfigListener"(key: $ResourceLocation$Type, listener: $Consumer$Type<($ResourceLocation$Type)>): void
public "hideTarget"(block: $Block$Type): void
public "hideTarget"(entityType: $EntityType$Type<(any)>): void
public "usePickedResult"(block: $Block$Type): void
public "usePickedResult"(entityType: $EntityType$Type<(any)>): void
public "entityAccessor"(): $EntityAccessor$Builder
public "registerBlockIcon"(provider: $IBlockComponentProvider$Type, block: $Class$Type<(any)>): void
public "registerEntityIcon"(provider: $IEntityComponentProvider$Type, entity: $Class$Type<(any)>): void
public "isServerConnected"(): boolean
public "maybeLowVisionUser"(): boolean
public "setServerData"(tag: $CompoundTag$Type): void
public "getAccessorHandler"(clazz: $Class$Type<(any)>): $Accessor$ClientHandler<($Accessor<(any)>)>
public "getServerData"(): $CompoundTag
public "getBlockCamouflage"(level: $LevelAccessor$Type, pos: $BlockPos$Type): $ItemStack
public "entity"(location: $ResourceLocation$Type, entity: $Class$Type<(any)>): $EntityComponentProviderBuilder
public "registerAccessorHandler"<T extends $Accessor<(any)>>(clazz: $Class$Type<(T)>, handler: $Accessor$ClientHandler$Type<(T)>): void
public "registerCustomEnchantPower"(block: $Block$Type, customEnchantPower: $CustomEnchantPower$Type): void
public "markAsServerFeature"(uid: $ResourceLocation$Type): void
public "markAsClientFeature"(uid: $ResourceLocation$Type): void
public "progress"(location: $ResourceLocation$Type): $ClientExtensionProviderBuilder<($CompoundTag), ($ProgressView)>
public "createPluginConfigScreen"(parent: $Screen$Type, namespace: string): $Screen
public "fluidStorage"(location: $ResourceLocation$Type): $ClientExtensionProviderBuilder<($CompoundTag), ($FluidView)>
public "energyStorage"(location: $ResourceLocation$Type): $ClientExtensionProviderBuilder<($CompoundTag), ($EnergyView)>
public "itemStorage"(location: $ResourceLocation$Type): $ClientExtensionProviderBuilder<($ItemStack), ($ItemView)>
public "isClientFeature"(uid: $ResourceLocation$Type): boolean
public "isShowDetailsPressed"(): boolean
get "serverConnected"(): boolean
set "serverData"(value: $CompoundTag$Type)
get "serverData"(): $CompoundTag
get "showDetailsPressed"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WailaClientRegistrationEventJS$Type = ($WailaClientRegistrationEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WailaClientRegistrationEventJS_ = $WailaClientRegistrationEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/impl/$CustomRecipeCategoryDecorator$TooltipDecorator" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export interface $CustomRecipeCategoryDecorator$TooltipDecorator<R> {

 "decorate"(arg0: $List$Type<($Component$Type)>, arg1: R, arg2: $IRecipeCategory$Type<(R)>, arg3: $IRecipeSlotsView$Type, arg4: double, arg5: double): $List<($Component)>

(arg0: $List$Type<($Component$Type)>, arg1: R, arg2: $IRecipeCategory$Type<(R)>, arg3: $IRecipeSlotsView$Type, arg4: double, arg5: double): $List<($Component)>
}

export namespace $CustomRecipeCategoryDecorator$TooltipDecorator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomRecipeCategoryDecorator$TooltipDecorator$Type<R> = ($CustomRecipeCategoryDecorator$TooltipDecorator<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomRecipeCategoryDecorator$TooltipDecorator_<R> = $CustomRecipeCategoryDecorator$TooltipDecorator$Type<(R)>;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/impl/$CustomClientExtensionProvider" {
import {$IClientExtensionProvider, $IClientExtensionProvider$Type} from "packages/snownee/jade/api/view/$IClientExtensionProvider"
import {$ClientViewGroup, $ClientViewGroup$Type} from "packages/snownee/jade/api/view/$ClientViewGroup"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$ClientExtensionProviderBuilder, $ClientExtensionProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$ClientExtensionProviderBuilder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$CustomJadeProvider, $CustomJadeProvider$Type} from "packages/pie/ilikepiefoo/compat/jade/impl/$CustomJadeProvider"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"

export class $CustomClientExtensionProvider<IN, OUT> extends $CustomJadeProvider<($ClientExtensionProviderBuilder<(IN), (OUT)>)> implements $IClientExtensionProvider<(IN), (OUT)> {

constructor(builder: $ClientExtensionProviderBuilder$Type<(IN), (OUT)>)

public "getClientGroups"(accessor: $Accessor$Type<(any)>, groups: $List$Type<($ViewGroup$Type<(IN)>)>): $List<($ClientViewGroup<(OUT)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomClientExtensionProvider$Type<IN, OUT> = ($CustomClientExtensionProvider<(IN), (OUT)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomClientExtensionProvider_<IN, OUT> = $CustomClientExtensionProvider$Type<(IN), (OUT)>;
}}
declare module "packages/pie/ilikepiefoo/events/$PlayerCloneEventJS" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$PlayerEventJS, $PlayerEventJS$Type} from "packages/dev/latvian/mods/kubejs/player/$PlayerEventJS"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export class $PlayerCloneEventJS extends $PlayerEventJS {

constructor(oldPlayer: $ServerPlayer$Type, newPlayer: $ServerPlayer$Type, conqueredEnd: boolean)

public static "of"(oldPlayer: $ServerPlayer$Type, newPlayer: $ServerPlayer$Type, conqueredEnd: boolean): $PlayerCloneEventJS
public "causedByDeath"(): boolean
public "getOldPlayer"(): $Player
public "getNewPlayer"(): $Player
public "leavingEnd"(): boolean
public "causedByPortal"(): boolean
public "returningFromEnd"(): boolean
get "oldPlayer"(): $Player
get "newPlayer"(): $Player
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerCloneEventJS$Type = ($PlayerCloneEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerCloneEventJS_ = $PlayerCloneEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/events/$RegisterIngredientsEventJS" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$JEIEventJS, $JEIEventJS$Type} from "packages/pie/ilikepiefoo/compat/jei/events/$JEIEventJS"
import {$IModIngredientRegistration, $IModIngredientRegistration$Type} from "packages/mezz/jei/api/registration/$IModIngredientRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CustomJSRecipe, $CustomJSRecipe$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomJSRecipe"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $RegisterIngredientsEventJS extends $JEIEventJS {
readonly "data": $IModIngredientRegistration
static readonly "customRecipeTypes": $Map<($ResourceLocation), ($RecipeType<($CustomJSRecipe)>)>
static readonly "overriddenRecipeTypes": $Map<($ResourceLocation), ($RecipeType)>
static "JEI_HELPERS": $IJeiHelpers

constructor(data: $IModIngredientRegistration$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegisterIngredientsEventJS$Type = ($RegisterIngredientsEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegisterIngredientsEventJS_ = $RegisterIngredientsEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/builder/$RecipeCategoryBuilder$InputHandler" {
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export interface $RecipeCategoryBuilder$InputHandler<T> {

 "handleInput"(arg0: T, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean

(arg0: T, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
}

export namespace $RecipeCategoryBuilder$InputHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeCategoryBuilder$InputHandler$Type<T> = ($RecipeCategoryBuilder$InputHandler<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeCategoryBuilder$InputHandler_<T> = $RecipeCategoryBuilder$InputHandler$Type<(T)>;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/$JEIEvents" {
import {$EventHandler, $EventHandler$Type} from "packages/dev/latvian/mods/kubejs/event/$EventHandler"
import {$EventGroup, $EventGroup$Type} from "packages/dev/latvian/mods/kubejs/event/$EventGroup"

export interface $JEIEvents {

}

export namespace $JEIEvents {
const GROUP: $EventGroup
const ON_RUNTIME_AVAILABLE: $EventHandler
const REGISTER_ADVANCED: $EventHandler
const REGISTER_CATEGORIES: $EventHandler
const REGISTER_FLUID_SUBTYPES: $EventHandler
const REGISTER_GUI_HANDLERS: $EventHandler
const REGISTER_INGREDIENTS: $EventHandler
const REGISTER_ITEM_SUBTYPES: $EventHandler
const REGISTER_RECIPE_CATALYSTS: $EventHandler
const REGISTER_RECIPES: $EventHandler
const REGISTER_RECIPE_TRANSFER_HANDLERS: $EventHandler
const REGISTER_VANILLA_CATEGORY_EXTENSIONS: $EventHandler
function register(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEIEvents$Type = ($JEIEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEIEvents_ = $JEIEvents$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/$WailaCommonRegistrationEventJS" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ServerDataProviderBuilder, $ServerDataProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$ServerDataProviderBuilder"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IWailaCommonRegistration, $IWailaCommonRegistration$Type} from "packages/snownee/jade/api/$IWailaCommonRegistration"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ServerExtensionProviderBuilder, $ServerExtensionProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$ServerExtensionProviderBuilder"

export class $WailaCommonRegistrationEventJS extends $EventJS {

constructor(registration: $IWailaCommonRegistration$Type)

public "progress"<T>(location: $ResourceLocation$Type, highestClass: $Class$Type<(any)>): $ServerExtensionProviderBuilder<(T), ($CompoundTag)>
public "getRegistration"(): $IWailaCommonRegistration
public "fluidStorage"<T>(location: $ResourceLocation$Type, highestClass: $Class$Type<(any)>): $ServerExtensionProviderBuilder<(T), ($CompoundTag)>
public "energyStorage"<T>(location: $ResourceLocation$Type, highestClass: $Class$Type<(any)>): $ServerExtensionProviderBuilder<(T), ($CompoundTag)>
public "itemStorage"<T>(location: $ResourceLocation$Type, highestClass: $Class$Type<(any)>): $ServerExtensionProviderBuilder<(T), ($ItemStack)>
public "blockDataProvider"(location: $ResourceLocation$Type, block: $Class$Type<(any)>): $ServerDataProviderBuilder<($BlockAccessor)>
public "entityDataProvider"(location: $ResourceLocation$Type, entity: $Class$Type<(any)>): $ServerDataProviderBuilder<($EntityAccessor)>
get "registration"(): $IWailaCommonRegistration
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WailaCommonRegistrationEventJS$Type = ($WailaCommonRegistrationEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WailaCommonRegistrationEventJS_ = $WailaCommonRegistrationEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/builder/$RecipeCategoryBuilder" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$RecipeCategoryBuilder$TooltipHandler, $RecipeCategoryBuilder$TooltipHandler$Type} from "packages/pie/ilikepiefoo/compat/jei/builder/$RecipeCategoryBuilder$TooltipHandler"
import {$RecipeCategoryBuilder$GetRegisterName, $RecipeCategoryBuilder$GetRegisterName$Type} from "packages/pie/ilikepiefoo/compat/jei/builder/$RecipeCategoryBuilder$GetRegisterName"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$RecipeCategoryBuilder$InputHandler, $RecipeCategoryBuilder$InputHandler$Type} from "packages/pie/ilikepiefoo/compat/jei/builder/$RecipeCategoryBuilder$InputHandler"
import {$RecipeCategoryBuilder$SetRecipeHandler, $RecipeCategoryBuilder$SetRecipeHandler$Type} from "packages/pie/ilikepiefoo/compat/jei/builder/$RecipeCategoryBuilder$SetRecipeHandler"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$RecipeCategoryBuilder$DrawHandler, $RecipeCategoryBuilder$DrawHandler$Type} from "packages/pie/ilikepiefoo/compat/jei/builder/$RecipeCategoryBuilder$DrawHandler"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$RecipeCategoryBuilder$IsRecipeHandledByCategory, $RecipeCategoryBuilder$IsRecipeHandledByCategory$Type} from "packages/pie/ilikepiefoo/compat/jei/builder/$RecipeCategoryBuilder$IsRecipeHandledByCategory"

export class $RecipeCategoryBuilder<T> {

constructor(recipeType: $RecipeType$Type<(T)>, jeiHelpers: $IJeiHelpers$Type)

public "getIsRecipeHandledByCategory"(): $RecipeCategoryBuilder$IsRecipeHandledByCategory<(T)>
public "getSetRecipeHandler"(): $RecipeCategoryBuilder$SetRecipeHandler<(T)>
public "getCategoryBackground"(): $IDrawable
public "setSetRecipeHandler"(setRecipeHandler: $RecipeCategoryBuilder$SetRecipeHandler$Type<(T)>): $RecipeCategoryBuilder<(T)>
public "setIsRecipeHandledByCategory"(isRecipeHandledByCategory: $RecipeCategoryBuilder$IsRecipeHandledByCategory$Type<(T)>): $RecipeCategoryBuilder<(T)>
public "setWidth"(width: integer): $RecipeCategoryBuilder<(T)>
public "withTooltip"(tooltipHandler: $RecipeCategoryBuilder$TooltipHandler$Type<(T)>): $RecipeCategoryBuilder<(T)>
public "getJeiHelpers"(): $IJeiHelpers
public "getRecipeType"(): $RecipeType<(T)>
public "background"(background: $IDrawable$Type): $RecipeCategoryBuilder<(T)>
public "registryName"(getRegisterName: $RecipeCategoryBuilder$GetRegisterName$Type<(T)>): $RecipeCategoryBuilder<(T)>
public "getWidth"(): integer
public "getHeight"(): integer
public "setTooltipHandler"(tooltipHandler: $RecipeCategoryBuilder$TooltipHandler$Type<(T)>): $RecipeCategoryBuilder<(T)>
public "title"(title: $Component$Type): $RecipeCategoryBuilder<(T)>
public "getInputHandler"(): $RecipeCategoryBuilder$InputHandler<(T)>
public "icon"(icon: $IDrawable$Type): $RecipeCategoryBuilder<(T)>
public "setHeight"(height: integer): $RecipeCategoryBuilder<(T)>
public "onInput"(inputHandler: $RecipeCategoryBuilder$InputHandler$Type<(T)>): $RecipeCategoryBuilder<(T)>
public "getTooltipHandler"(): $RecipeCategoryBuilder$TooltipHandler<(T)>
public "getDrawHandler"(): $RecipeCategoryBuilder$DrawHandler<(T)>
public "getCategoryIcon"(): $IDrawable
public "setDrawHandler"(drawHandler: $RecipeCategoryBuilder$DrawHandler$Type<(T)>): $RecipeCategoryBuilder<(T)>
public "getCategoryTitle"(): $Component
public "handleLookup"(recipeHandler: $RecipeCategoryBuilder$SetRecipeHandler$Type<(T)>): $RecipeCategoryBuilder<(T)>
public "getGetRegisterName"(): $RecipeCategoryBuilder$GetRegisterName<(T)>
public "isRecipeHandled"(isRecipeHandledByCategory: $RecipeCategoryBuilder$IsRecipeHandledByCategory$Type<(T)>): $RecipeCategoryBuilder<(T)>
public "setGetRegisterName"(getRegisterName: $RecipeCategoryBuilder$GetRegisterName$Type<(T)>): $RecipeCategoryBuilder<(T)>
public "setInputHandler"(inputHandler: $RecipeCategoryBuilder$InputHandler$Type<(T)>): $RecipeCategoryBuilder<(T)>
get "isRecipeHandledByCategory"(): $RecipeCategoryBuilder$IsRecipeHandledByCategory<(T)>
get "setRecipeHandler"(): $RecipeCategoryBuilder$SetRecipeHandler<(T)>
get "categoryBackground"(): $IDrawable
set "setRecipeHandler"(value: $RecipeCategoryBuilder$SetRecipeHandler$Type<(T)>)
set "isRecipeHandledByCategory"(value: $RecipeCategoryBuilder$IsRecipeHandledByCategory$Type<(T)>)
set "width"(value: integer)
get "jeiHelpers"(): $IJeiHelpers
get "recipeType"(): $RecipeType<(T)>
get "width"(): integer
get "height"(): integer
set "tooltipHandler"(value: $RecipeCategoryBuilder$TooltipHandler$Type<(T)>)
get "inputHandler"(): $RecipeCategoryBuilder$InputHandler<(T)>
set "height"(value: integer)
get "tooltipHandler"(): $RecipeCategoryBuilder$TooltipHandler<(T)>
get "drawHandler"(): $RecipeCategoryBuilder$DrawHandler<(T)>
get "categoryIcon"(): $IDrawable
set "drawHandler"(value: $RecipeCategoryBuilder$DrawHandler$Type<(T)>)
get "categoryTitle"(): $Component
get "getRegisterName"(): $RecipeCategoryBuilder$GetRegisterName<(T)>
set "getRegisterName"(value: $RecipeCategoryBuilder$GetRegisterName$Type<(T)>)
set "inputHandler"(value: $RecipeCategoryBuilder$InputHandler$Type<(T)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeCategoryBuilder$Type<T> = ($RecipeCategoryBuilder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeCategoryBuilder_<T> = $RecipeCategoryBuilder$Type<(T)>;
}}
declare module "packages/pie/ilikepiefoo/forge/$KubeJSAdditionsForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $KubeJSAdditionsForge {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KubeJSAdditionsForge$Type = ($KubeJSAdditionsForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KubeJSAdditionsForge_ = $KubeJSAdditionsForge$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/impl/$CustomToggleableProviderBuilder" {
import {$ToggleableProviderBuilder, $ToggleableProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$ToggleableProviderBuilder"
import {$IToggleableProvider, $IToggleableProvider$Type} from "packages/snownee/jade/api/$IToggleableProvider"
import {$CustomJadeProvider, $CustomJadeProvider$Type} from "packages/pie/ilikepiefoo/compat/jade/impl/$CustomJadeProvider"

export class $CustomToggleableProviderBuilder<T extends $ToggleableProviderBuilder> extends $CustomJadeProvider<(T)> implements $IToggleableProvider {

constructor(builder: T)

public "isRequired"(): boolean
public "enabledByDefault"(): boolean
get "required"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomToggleableProviderBuilder$Type<T> = ($CustomToggleableProviderBuilder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomToggleableProviderBuilder_<T> = $CustomToggleableProviderBuilder$Type<(T)>;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/events/$RegisterGUIHandlersEventJS" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IGuiHandlerRegistration, $IGuiHandlerRegistration$Type} from "packages/mezz/jei/api/registration/$IGuiHandlerRegistration"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$JEIEventJS, $JEIEventJS$Type} from "packages/pie/ilikepiefoo/compat/jei/events/$JEIEventJS"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CustomJSRecipe, $CustomJSRecipe$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomJSRecipe"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $RegisterGUIHandlersEventJS extends $JEIEventJS {
readonly "data": $IGuiHandlerRegistration
static readonly "customRecipeTypes": $Map<($ResourceLocation), ($RecipeType<($CustomJSRecipe)>)>
static readonly "overriddenRecipeTypes": $Map<($ResourceLocation), ($RecipeType)>
static "JEI_HELPERS": $IJeiHelpers

constructor(data: $IGuiHandlerRegistration$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegisterGUIHandlersEventJS$Type = ($RegisterGUIHandlersEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegisterGUIHandlersEventJS_ = $RegisterGUIHandlersEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/builder/$BlockComponentProviderBuilder$TooltipRetriever" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$ITooltipWrapper, $ITooltipWrapper$Type} from "packages/pie/ilikepiefoo/compat/jade/$ITooltipWrapper"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"

export interface $BlockComponentProviderBuilder$TooltipRetriever {

 "appendTooltip"(arg0: $ITooltipWrapper$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void

(arg0: $ITooltipWrapper$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
}

export namespace $BlockComponentProviderBuilder$TooltipRetriever {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockComponentProviderBuilder$TooltipRetriever$Type = ($BlockComponentProviderBuilder$TooltipRetriever);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockComponentProviderBuilder$TooltipRetriever_ = $BlockComponentProviderBuilder$TooltipRetriever$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/impl/$CustomRecipeCategoryDecorator" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$IRecipeCategoryDecorator, $IRecipeCategoryDecorator$Type} from "packages/mezz/jei/api/recipe/category/extensions/$IRecipeCategoryDecorator"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$CustomRecipeCategoryDecorator$TooltipDecorator, $CustomRecipeCategoryDecorator$TooltipDecorator$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomRecipeCategoryDecorator$TooltipDecorator"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$CustomRecipeCategoryDecorator$DrawDecorator, $CustomRecipeCategoryDecorator$DrawDecorator$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomRecipeCategoryDecorator$DrawDecorator"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export class $CustomRecipeCategoryDecorator<T> extends $Record implements $IRecipeCategoryDecorator<(T)> {

constructor(draw: $CustomRecipeCategoryDecorator$DrawDecorator$Type<(T)>, tooltip: $CustomRecipeCategoryDecorator$TooltipDecorator$Type<(T)>)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "draw"(): $CustomRecipeCategoryDecorator$DrawDecorator<(T)>
public "draw"(recipe: T, recipeCategory: $IRecipeCategory$Type<(T)>, recipeSlotsView: $IRecipeSlotsView$Type, guiGraphics: $GuiGraphics$Type, mouseX: double, mouseY: double): void
public "tooltip"(): $CustomRecipeCategoryDecorator$TooltipDecorator<(T)>
public "decorateExistingTooltips"(tooltips: $List$Type<($Component$Type)>, recipe: T, recipeCategory: $IRecipeCategory$Type<(T)>, recipeSlotsView: $IRecipeSlotsView$Type, mouseX: double, mouseY: double): $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomRecipeCategoryDecorator$Type<T> = ($CustomRecipeCategoryDecorator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomRecipeCategoryDecorator_<T> = $CustomRecipeCategoryDecorator$Type<(T)>;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/builder/$RecipeCategoryBuilder$DrawHandler" {
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export interface $RecipeCategoryBuilder$DrawHandler<T> {

 "draw"(arg0: T, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void

(arg0: T, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
}

export namespace $RecipeCategoryBuilder$DrawHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeCategoryBuilder$DrawHandler$Type<T> = ($RecipeCategoryBuilder$DrawHandler<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeCategoryBuilder$DrawHandler_<T> = $RecipeCategoryBuilder$DrawHandler$Type<(T)>;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/events/$OnRuntimeAvailableEventJS" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$JEIEventJS, $JEIEventJS$Type} from "packages/pie/ilikepiefoo/compat/jei/events/$JEIEventJS"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IJeiRuntime, $IJeiRuntime$Type} from "packages/mezz/jei/api/runtime/$IJeiRuntime"
import {$CustomJSRecipe, $CustomJSRecipe$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomJSRecipe"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $OnRuntimeAvailableEventJS extends $JEIEventJS {
readonly "data": $IJeiRuntime
static readonly "customRecipeTypes": $Map<($ResourceLocation), ($RecipeType<($CustomJSRecipe)>)>
static readonly "overriddenRecipeTypes": $Map<($ResourceLocation), ($RecipeType)>
static "JEI_HELPERS": $IJeiHelpers

constructor(data: $IJeiRuntime$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OnRuntimeAvailableEventJS$Type = ($OnRuntimeAvailableEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OnRuntimeAvailableEventJS_ = $OnRuntimeAvailableEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/$KubeJSAdditions" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $KubeJSAdditions {
static readonly "MOD_ID": string

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KubeJSAdditions$Type = ($KubeJSAdditions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KubeJSAdditions_ = $KubeJSAdditions$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/builder/$ViewGroupBuilder" {
import {$ClientViewGroup, $ClientViewGroup$Type} from "packages/snownee/jade/api/view/$ClientViewGroup"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"

export class $ViewGroupBuilder<OUT> {

constructor()

public "add"(element: OUT): $ViewGroupBuilder<(OUT)>
public "clear"(): $ViewGroupBuilder<(OUT)>
public "addAll"(elements: $List$Type<(OUT)>): $ViewGroupBuilder<(OUT)>
public "addElement"(element: OUT): $ViewGroupBuilder<(OUT)>
public "getElements"(): $List<(OUT)>
public "setElements"(elements: $List$Type<(OUT)>): $ViewGroupBuilder<(OUT)>
public "buildCommon"(): $ViewGroup<(OUT)>
public "addElements"(elements: $List$Type<(OUT)>): $ViewGroupBuilder<(OUT)>
public "buildClient"(): $ClientViewGroup<(OUT)>
get "elements"(): $List<(OUT)>
set "elements"(value: $List$Type<(OUT)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ViewGroupBuilder$Type<OUT> = ($ViewGroupBuilder<(OUT)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ViewGroupBuilder_<OUT> = $ViewGroupBuilder$Type<(OUT)>;
}}
declare module "packages/pie/ilikepiefoo/events/$ProxyEventJS" {
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$Method, $Method$Type} from "packages/java/lang/reflect/$Method"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ProxyEventJS extends $EventJS {
static readonly "LOG": $Logger

constructor(method: $Method$Type, args: (any)[])

public "getReturnType"(): string
public "getGenericReturnType"(): string
public "getParameters"(): $Map<(string), (any)>
public "getMethodName"(): string
public "setResult"(result: any): void
public "getResult"(): any
public "getArgs"(): (any)[]
public "getArg"(index: integer): any
public "hasResult"(): boolean
public "requiresResult"(): boolean
public "getResultOptional"(): $Optional<(any)>
get "returnType"(): string
get "genericReturnType"(): string
get "parameters"(): $Map<(string), (any)>
get "methodName"(): string
set "result"(value: any)
get "result"(): any
get "args"(): (any)[]
get "resultOptional"(): $Optional<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProxyEventJS$Type = ($ProxyEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProxyEventJS_ = $ProxyEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/impl/$CustomJSRecipe" {
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"

export class $CustomJSRecipe {

constructor(recipeData: any, recipeType: $RecipeType$Type<($CustomJSRecipe$Type)>)

public "getData"(): any
public "getRecipeType"(): $RecipeType<($CustomJSRecipe)>
public "getRecipeData"(): any
public "setRecipeData"(recipeData: any): void
get "data"(): any
get "recipeType"(): $RecipeType<($CustomJSRecipe)>
get "recipeData"(): any
set "recipeData"(value: any)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomJSRecipe$Type = ($CustomJSRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomJSRecipe_ = $CustomJSRecipe$Type;
}}
declare module "packages/pie/ilikepiefoo/util/$ReflectionUtils$Pair" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ReflectionUtils$Pair<A, B> {
 "a": A
 "b": B

constructor(a: A, b: B)

public "getA"(): A
public "getB"(): B
get "a"(): A
get "b"(): B
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReflectionUtils$Pair$Type<A, B> = ($ReflectionUtils$Pair<(A), (B)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReflectionUtils$Pair_<A, B> = $ReflectionUtils$Pair$Type<(A), (B)>;
}}
declare module "packages/pie/ilikepiefoo/events/$AdditionalEvents" {
import {$EventHandler, $EventHandler$Type} from "packages/dev/latvian/mods/kubejs/event/$EventHandler"
import {$EventGroup, $EventGroup$Type} from "packages/dev/latvian/mods/kubejs/event/$EventGroup"

export interface $AdditionalEvents {

}

export namespace $AdditionalEvents {
const GROUP: $EventGroup
const ENTITY_ENTER_CHUNK: $EventHandler
const ENTITY_TAME: $EventHandler
const PLAYER_CHANGE_DIMENSION: $EventHandler
const PLAYER_CLONE: $EventHandler
const PLAYER_RESPAWN: $EventHandler
const ARCH_EVENTS: $EventGroup
const ARCH_STARTUP_EVENT_HANDLER: $EventHandler
const ARCH_CLIENT_EVENT_HANDLER: $EventHandler
const ARCH_SERVER_EVENT_HANDLER: $EventHandler
const ARCH_EVENT_REGISTER: $EventHandler
function register(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdditionalEvents$Type = ($AdditionalEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdditionalEvents_ = $AdditionalEvents$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/events/$RegisterCategoriesEventJS" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RecipeCategoryWrapperBuilder, $RecipeCategoryWrapperBuilder$Type} from "packages/pie/ilikepiefoo/compat/jei/builder/$RecipeCategoryWrapperBuilder"
import {$CustomJSRecipe, $CustomJSRecipe$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomJSRecipe"
import {$CustomRecipeCategory, $CustomRecipeCategory$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomRecipeCategory"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$RecipeCategoryBuilder, $RecipeCategoryBuilder$Type} from "packages/pie/ilikepiefoo/compat/jei/builder/$RecipeCategoryBuilder"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$IRecipeCategoryRegistration, $IRecipeCategoryRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCategoryRegistration"
import {$JEIEventJS, $JEIEventJS$Type} from "packages/pie/ilikepiefoo/compat/jei/events/$JEIEventJS"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $RegisterCategoriesEventJS extends $JEIEventJS {
readonly "data": $IRecipeCategoryRegistration
static readonly "customRecipeTypes": $Map<($ResourceLocation), ($RecipeType<($CustomJSRecipe)>)>
static readonly "overriddenRecipeTypes": $Map<($ResourceLocation), ($RecipeType)>
static "JEI_HELPERS": $IJeiHelpers

constructor(data: $IRecipeCategoryRegistration$Type)

public "wrap"<T>(recipeType: $RecipeType$Type<(T)>, existingCategory: $IRecipeCategory$Type<(T)>, categoryConsumer: $Consumer$Type<($RecipeCategoryWrapperBuilder$Type<(T)>)>): $CustomRecipeCategory<(T)>
public "register"<T>(recipeType: $RecipeType$Type<(T)>, categoryConsumer: $Consumer$Type<($RecipeCategoryBuilder$Type<(T)>)>): $CustomRecipeCategory<(T)>
public "custom"(recipeType: $ResourceLocation$Type, categoryConsumer: $Consumer$Type<($RecipeCategoryBuilder$Type<($CustomJSRecipe$Type)>)>): $CustomRecipeCategory<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegisterCategoriesEventJS$Type = ($RegisterCategoriesEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegisterCategoriesEventJS_ = $RegisterCategoriesEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/impl/$CustomEntityComponentProvider" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$CustomToggleableProviderBuilder, $CustomToggleableProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/impl/$CustomToggleableProviderBuilder"
import {$EntityComponentProviderBuilder, $EntityComponentProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$EntityComponentProviderBuilder"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"
import {$IEntityComponentProvider, $IEntityComponentProvider$Type} from "packages/snownee/jade/api/$IEntityComponentProvider"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"

export class $CustomEntityComponentProvider extends $CustomToggleableProviderBuilder<($EntityComponentProviderBuilder)> implements $IEntityComponentProvider {

constructor(builder: $EntityComponentProviderBuilder$Type)

public "getIcon"(accessor: $EntityAccessor$Type, config: $IPluginConfig$Type, currentIcon: $IElement$Type): $IElement
public "appendTooltip"(iTooltip: $ITooltip$Type, blockAccessor: $EntityAccessor$Type, iPluginConfig: $IPluginConfig$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomEntityComponentProvider$Type = ($CustomEntityComponentProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomEntityComponentProvider_ = $CustomEntityComponentProvider$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/$JEIPlugin" {
import {$IGuiHandlerRegistration, $IGuiHandlerRegistration$Type} from "packages/mezz/jei/api/registration/$IGuiHandlerRegistration"
import {$IAdvancedRegistration, $IAdvancedRegistration$Type} from "packages/mezz/jei/api/registration/$IAdvancedRegistration"
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
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

export class $JEIPlugin implements $IModPlugin {

constructor()

public "registerItemSubtypes"(registration: $ISubtypeRegistration$Type): void
public "registerVanillaCategoryExtensions"(registration: $IVanillaCategoryExtensionRegistration$Type): void
public "registerFluidSubtypes"<T>(registration: $ISubtypeRegistration$Type, platformFluidHelper: $IPlatformFluidHelper$Type<(T)>): void
public "registerGuiHandlers"(registration: $IGuiHandlerRegistration$Type): void
public "registerIngredients"(registration: $IModIngredientRegistration$Type): void
public "registerRecipeTransferHandlers"(registration: $IRecipeTransferRegistration$Type): void
public "registerRecipeCatalysts"(registration: $IRecipeCatalystRegistration$Type): void
public "getPluginUid"(): $ResourceLocation
public "registerRecipes"(registration: $IRecipeRegistration$Type): void
public "registerAdvanced"(registration: $IAdvancedRegistration$Type): void
public "onRuntimeAvailable"(jeiRuntime: $IJeiRuntime$Type): void
public "registerCategories"(registration: $IRecipeCategoryRegistration$Type): void
public "onConfigManagerAvailable"(arg0: $IJeiConfigManager$Type): void
public "onRuntimeUnavailable"(): void
public "registerRuntime"(arg0: $IRuntimeRegistration$Type): void
get "pluginUid"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEIPlugin$Type = ($JEIPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEIPlugin_ = $JEIPlugin$Type;
}}
declare module "packages/pie/ilikepiefoo/events/$EntityTameEventJS" {
import {$PlayerEventJS, $PlayerEventJS$Type} from "packages/dev/latvian/mods/kubejs/player/$PlayerEventJS"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Animal, $Animal$Type} from "packages/net/minecraft/world/entity/animal/$Animal"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EntityTameEventJS extends $PlayerEventJS {

constructor(animal: $Animal$Type, player: $Player$Type)

public static "of"(animal: $Animal$Type, player: $Player$Type): $EntityTameEventJS
public "getAnimal"(): $Entity
public "getEntity"(): $Player
get "animal"(): $Entity
get "entity"(): $Player
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityTameEventJS$Type = ($EntityTameEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityTameEventJS_ = $EntityTameEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/builder/callback/$GetClientGroupsCallbackJS" {
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ViewGroupBuilder, $ViewGroupBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$ViewGroupBuilder"
import {$ViewGroup, $ViewGroup$Type} from "packages/snownee/jade/api/view/$ViewGroup"

export class $GetClientGroupsCallbackJS<IN, OUT> {
readonly "accessor": $Accessor<(any)>
readonly "groups": $List<($ViewGroup<(IN)>)>

constructor(accessor: $Accessor$Type<(any)>, groups: $List$Type<($ViewGroup$Type<(IN)>)>)

public "getAccessor"(): $Accessor<(any)>
public "addGroups"(groups: $List$Type<($ViewGroup$Type<(OUT)>)>): $GetClientGroupsCallbackJS<(IN), (OUT)>
public "clearGroups"(): $GetClientGroupsCallbackJS<(IN), (OUT)>
public "addGroup"(groupBuilderConsumer: $Consumer$Type<($ViewGroupBuilder$Type<(OUT)>)>): $GetClientGroupsCallbackJS<(IN), (OUT)>
public "addGroup"(group: $List$Type<(OUT)>): $GetClientGroupsCallbackJS<(IN), (OUT)>
public "addGroup"(group: $ViewGroup$Type<(OUT)>): $GetClientGroupsCallbackJS<(IN), (OUT)>
public "addGroup"(group: $ViewGroupBuilder$Type<(OUT)>): $GetClientGroupsCallbackJS<(IN), (OUT)>
public "getGroups"(): $List<($ViewGroup<(IN)>)>
public "getResultingGroups"(): $List<($ViewGroupBuilder<(OUT)>)>
get "accessor"(): $Accessor<(any)>
get "groups"(): $List<($ViewGroup<(IN)>)>
get "resultingGroups"(): $List<($ViewGroupBuilder<(OUT)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GetClientGroupsCallbackJS$Type<IN, OUT> = ($GetClientGroupsCallbackJS<(IN), (OUT)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GetClientGroupsCallbackJS_<IN, OUT> = $GetClientGroupsCallbackJS$Type<(IN), (OUT)>;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/$JadeEvents" {
import {$EventHandler, $EventHandler$Type} from "packages/dev/latvian/mods/kubejs/event/$EventHandler"
import {$EventGroup, $EventGroup$Type} from "packages/dev/latvian/mods/kubejs/event/$EventGroup"

export interface $JadeEvents {

}

export namespace $JadeEvents {
const GROUP: $EventGroup
const ON_COMMON_REGISTRATION: $EventHandler
const ON_CLIENT_REGISTRATION: $EventHandler
function register(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeEvents$Type = ($JadeEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeEvents_ = $JadeEvents$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/events/$RegisterRecipeTransferHandlersEventJS" {
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$IRecipeTransferRegistration, $IRecipeTransferRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeTransferRegistration"

export class $RegisterRecipeTransferHandlersEventJS extends $EventJS {
readonly "data": $IRecipeTransferRegistration

constructor(data: $IRecipeTransferRegistration$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegisterRecipeTransferHandlersEventJS$Type = ($RegisterRecipeTransferHandlersEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegisterRecipeTransferHandlersEventJS_ = $RegisterRecipeTransferHandlersEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/builder/$RecipeCategoryBuilder$SetRecipeHandler" {
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"

export interface $RecipeCategoryBuilder$SetRecipeHandler<T> {

 "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: T, arg2: $IFocusGroup$Type): void

(arg0: $IRecipeLayoutBuilder$Type, arg1: T, arg2: $IFocusGroup$Type): void
}

export namespace $RecipeCategoryBuilder$SetRecipeHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeCategoryBuilder$SetRecipeHandler$Type<T> = ($RecipeCategoryBuilder$SetRecipeHandler<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeCategoryBuilder$SetRecipeHandler_<T> = $RecipeCategoryBuilder$SetRecipeHandler$Type<(T)>;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/builder/$ServerDataProviderBuilder$AppendServerDataCallback" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"

export interface $ServerDataProviderBuilder$AppendServerDataCallback<ACCESSOR extends $Accessor<(any)>> {

 "appendServerData"(arg0: $CompoundTag$Type, arg1: ACCESSOR): void

(arg0: $CompoundTag$Type, arg1: ACCESSOR): void
}

export namespace $ServerDataProviderBuilder$AppendServerDataCallback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerDataProviderBuilder$AppendServerDataCallback$Type<ACCESSOR> = ($ServerDataProviderBuilder$AppendServerDataCallback<(ACCESSOR)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerDataProviderBuilder$AppendServerDataCallback_<ACCESSOR> = $ServerDataProviderBuilder$AppendServerDataCallback$Type<(ACCESSOR)>;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/builder/$EntityComponentProviderBuilder$TooltipRetriever" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$ITooltipWrapper, $ITooltipWrapper$Type} from "packages/pie/ilikepiefoo/compat/jade/$ITooltipWrapper"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"

export interface $EntityComponentProviderBuilder$TooltipRetriever {

 "appendTooltip"(arg0: $ITooltipWrapper$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void

(arg0: $ITooltipWrapper$Type, arg1: $EntityAccessor$Type, arg2: $IPluginConfig$Type): void
}

export namespace $EntityComponentProviderBuilder$TooltipRetriever {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityComponentProviderBuilder$TooltipRetriever$Type = ($EntityComponentProviderBuilder$TooltipRetriever);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityComponentProviderBuilder$TooltipRetriever_ = $EntityComponentProviderBuilder$TooltipRetriever$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/builder/$BlockComponentProviderBuilder$IconRetriever" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"

export interface $BlockComponentProviderBuilder$IconRetriever {

 "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement

(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
}

export namespace $BlockComponentProviderBuilder$IconRetriever {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockComponentProviderBuilder$IconRetriever$Type = ($BlockComponentProviderBuilder$IconRetriever);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockComponentProviderBuilder$IconRetriever_ = $BlockComponentProviderBuilder$IconRetriever$Type;
}}
declare module "packages/pie/ilikepiefoo/player/$CustomDamageSourceJS" {
import {$DamageType, $DamageType$Type} from "packages/net/minecraft/world/damagesource/$DamageType"
import {$DeathMessageType, $DeathMessageType$Type} from "packages/net/minecraft/world/damagesource/$DeathMessageType"
import {$DamageEffects, $DamageEffects$Type} from "packages/net/minecraft/world/damagesource/$DamageEffects"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$DamageScaling, $DamageScaling$Type} from "packages/net/minecraft/world/damagesource/$DamageScaling"

export class $CustomDamageSourceJS extends $DamageSource {

constructor(damageType: $DamageType$Type)

public static "custom"(id: string, exhaustion: float, effects: $DamageEffects$Type): $CustomDamageSourceJS
public static "custom"(id: string, scaling: $DamageScaling$Type, exhaustion: float, effects: $DamageEffects$Type): $CustomDamageSourceJS
public static "custom"(id: string, scaling: $DamageScaling$Type, exhaustion: float, effects: $DamageEffects$Type, deathMessageType: $DeathMessageType$Type): $CustomDamageSourceJS
public static "custom"(id: string, exhaustion: float): $CustomDamageSourceJS
public static "custom"(id: string): $CustomDamageSourceJS
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomDamageSourceJS$Type = ($CustomDamageSourceJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomDamageSourceJS_ = $CustomDamageSourceJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/builder/$ToggleableProviderBuilder" {
import {$JadeProviderBuilder, $JadeProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$JadeProviderBuilder"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ToggleableProviderBuilder extends $JadeProviderBuilder {

constructor(uniqueIdentifier: $ResourceLocation$Type)

public "isRequired"(): boolean
public "required"(): $ToggleableProviderBuilder
public "setRequired"(isRequired: boolean): $ToggleableProviderBuilder
public "enabledByDefault"(): $ToggleableProviderBuilder
public "isEnabledByDefault"(): boolean
public "setEnabledByDefault"(enabledByDefault: boolean): $ToggleableProviderBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ToggleableProviderBuilder$Type = ($ToggleableProviderBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ToggleableProviderBuilder_ = $ToggleableProviderBuilder$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/builder/callback/$GetServerGroupsCallbackJS" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ViewGroupBuilder, $ViewGroupBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$ViewGroupBuilder"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export class $GetServerGroupsCallbackJS<IN, OUT> {
readonly "player": $ServerPlayer
readonly "world": $ServerLevel
readonly "target": IN
readonly "showDetails": boolean

constructor(player: $ServerPlayer$Type, world: $ServerLevel$Type, target: IN, showDetails: boolean)

public "getTarget"(): IN
public "getLevel"(): $ServerLevel
public "getWorld"(): $ServerLevel
public "showDetails"(): boolean
public "getPlayer"(): $ServerPlayer
public "addGroups"(groups: $List$Type<($ViewGroupBuilder$Type<(OUT)>)>): $GetServerGroupsCallbackJS<(IN), (OUT)>
public "clearGroups"(): $GetServerGroupsCallbackJS<(IN), (OUT)>
public "addGroup"(groupBuilderConsumer: $Consumer$Type<($ViewGroupBuilder$Type<(OUT)>)>): $GetServerGroupsCallbackJS<(IN), (OUT)>
public "addGroup"(group: $List$Type<(OUT)>): $GetServerGroupsCallbackJS<(IN), (OUT)>
public "addGroup"(group: $ViewGroupBuilder$Type<(OUT)>): $GetServerGroupsCallbackJS<(IN), (OUT)>
public "getGroups"(): $List<($ViewGroupBuilder<(OUT)>)>
get "target"(): IN
get "level"(): $ServerLevel
get "world"(): $ServerLevel
get "player"(): $ServerPlayer
get "groups"(): $List<($ViewGroupBuilder<(OUT)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GetServerGroupsCallbackJS$Type<IN, OUT> = ($GetServerGroupsCallbackJS<(IN), (OUT)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GetServerGroupsCallbackJS_<IN, OUT> = $GetServerGroupsCallbackJS$Type<(IN), (OUT)>;
}}
declare module "packages/pie/ilikepiefoo/$EventHandler" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $EventHandler {

constructor()

public static "init"(): void
public static "onPlayerChangeDimension"(player: $ServerPlayer$Type, oldLevel: $ResourceKey$Type<($Level$Type)>, newLevel: $ResourceKey$Type<($Level$Type)>): void
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
declare module "packages/pie/ilikepiefoo/compat/jei/$JEIKubeJSPlugin" {
import {$KubeJSPlugin, $KubeJSPlugin$Type} from "packages/dev/latvian/mods/kubejs/$KubeJSPlugin"
import {$ScriptType, $ScriptType$Type} from "packages/dev/latvian/mods/kubejs/script/$ScriptType"
import {$TypeWrappers, $TypeWrappers$Type} from "packages/dev/latvian/mods/rhino/util/wrap/$TypeWrappers"

export class $JEIKubeJSPlugin extends $KubeJSPlugin {

constructor()

public "registerTypeWrappers"(type: $ScriptType$Type, typeWrappers: $TypeWrappers$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEIKubeJSPlugin$Type = ($JEIKubeJSPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEIKubeJSPlugin_ = $JEIKubeJSPlugin$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/events/$RegisterAdvancedEventJS" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IAdvancedRegistration, $IAdvancedRegistration$Type} from "packages/mezz/jei/api/registration/$IAdvancedRegistration"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$CustomRecipeCategoryDecorator, $CustomRecipeCategoryDecorator$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomRecipeCategoryDecorator"
import {$JEIEventJS, $JEIEventJS$Type} from "packages/pie/ilikepiefoo/compat/jei/events/$JEIEventJS"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CustomRecipeCategoryDecorator$TooltipDecorator, $CustomRecipeCategoryDecorator$TooltipDecorator$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomRecipeCategoryDecorator$TooltipDecorator"
import {$CustomJSRecipe, $CustomJSRecipe$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomJSRecipe"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$CustomRecipeCategoryDecorator$DrawDecorator, $CustomRecipeCategoryDecorator$DrawDecorator$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomRecipeCategoryDecorator$DrawDecorator"

export class $RegisterAdvancedEventJS extends $JEIEventJS {
readonly "data": $IAdvancedRegistration
static readonly "customRecipeTypes": $Map<($ResourceLocation), ($RecipeType<($CustomJSRecipe)>)>
static readonly "overriddenRecipeTypes": $Map<($ResourceLocation), ($RecipeType)>
static "JEI_HELPERS": $IJeiHelpers

constructor(data: $IAdvancedRegistration$Type)

public "categoryDecorator"<T>(drawDecorator: $CustomRecipeCategoryDecorator$DrawDecorator$Type<(T)>, tooltipDecorator: $CustomRecipeCategoryDecorator$TooltipDecorator$Type<(T)>): $CustomRecipeCategoryDecorator<(T)>
public "categoryDecorator"<T>(drawDecorator: $CustomRecipeCategoryDecorator$DrawDecorator$Type<(T)>): $CustomRecipeCategoryDecorator<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegisterAdvancedEventJS$Type = ($RegisterAdvancedEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegisterAdvancedEventJS_ = $RegisterAdvancedEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/impl/$CustomServerDataProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$ServerDataProviderBuilder, $ServerDataProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$ServerDataProviderBuilder"
import {$CustomJadeProvider, $CustomJadeProvider$Type} from "packages/pie/ilikepiefoo/compat/jade/impl/$CustomJadeProvider"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $CustomServerDataProvider<T extends $Accessor<(any)>> extends $CustomJadeProvider<($ServerDataProviderBuilder<(T)>)> implements $IServerDataProvider<(T)> {

constructor(builder: $ServerDataProviderBuilder$Type<(T)>)

public "appendServerData"(compoundTag: $CompoundTag$Type, accessor: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomServerDataProvider$Type<T> = ($CustomServerDataProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomServerDataProvider_<T> = $CustomServerDataProvider$Type<(T)>;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/impl/$CustomRecipeCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$RecipeCategoryBuilder, $RecipeCategoryBuilder$Type} from "packages/pie/ilikepiefoo/compat/jei/builder/$RecipeCategoryBuilder"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $CustomRecipeCategory<T> implements $IRecipeCategory<(T)> {

constructor(builder: $RecipeCategoryBuilder$Type<(T)>)

public "getRecipeType"(): $RecipeType<(T)>
public "draw"(recipe: T, recipeSlotsView: $IRecipeSlotsView$Type, guiGraphics: $GuiGraphics$Type, mouseX: double, mouseY: double): void
public "getWidth"(): integer
public "getHeight"(): integer
public "isHandled"(recipe: T): boolean
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "handleInput"(recipe: T, mouseX: double, mouseY: double, input: $InputConstants$Key$Type): boolean
public "setRecipe"(builder: $IRecipeLayoutBuilder$Type, recipe: T, focuses: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "getTooltipStrings"(recipe: T, recipeSlotsView: $IRecipeSlotsView$Type, mouseX: double, mouseY: double): $List<($Component)>
public "getRegistryName"(recipe: T): $ResourceLocation
get "recipeType"(): $RecipeType<(T)>
get "width"(): integer
get "height"(): integer
get "icon"(): $IDrawable
get "title"(): $Component
get "background"(): $IDrawable
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomRecipeCategory$Type<T> = ($CustomRecipeCategory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomRecipeCategory_<T> = $CustomRecipeCategory$Type<(T)>;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/builder/$ClientExtensionProviderBuilder" {
import {$GetClientGroupsCallbackJS, $GetClientGroupsCallbackJS$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/callback/$GetClientGroupsCallbackJS"
import {$JadeProviderBuilder, $JadeProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$JadeProviderBuilder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ClientExtensionProviderBuilder<IN, OUT> extends $JadeProviderBuilder {

constructor(uniqueIdentifier: $ResourceLocation$Type)

public "callback"(callback: $Consumer$Type<($GetClientGroupsCallbackJS$Type<(IN), (OUT)>)>): $ClientExtensionProviderBuilder<(IN), (OUT)>
public static "doNothing"<IN, OUT>(callback: $GetClientGroupsCallbackJS$Type<(IN), (OUT)>): void
public "getCallback"(): $Consumer<($GetClientGroupsCallbackJS<(IN), (OUT)>)>
public "onGroups"(callback: $Consumer$Type<($GetClientGroupsCallbackJS$Type<(IN), (OUT)>)>): $ClientExtensionProviderBuilder<(IN), (OUT)>
public "groupCallback"(callback: $Consumer$Type<($GetClientGroupsCallbackJS$Type<(IN), (OUT)>)>): $ClientExtensionProviderBuilder<(IN), (OUT)>
public "setCallback"(callback: $Consumer$Type<($GetClientGroupsCallbackJS$Type<(IN), (OUT)>)>): $ClientExtensionProviderBuilder<(IN), (OUT)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientExtensionProviderBuilder$Type<IN, OUT> = ($ClientExtensionProviderBuilder<(IN), (OUT)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientExtensionProviderBuilder_<IN, OUT> = $ClientExtensionProviderBuilder$Type<(IN), (OUT)>;
}}
declare module "packages/pie/ilikepiefoo/events/$EntityEnterChunkEventJS" {
import {$EntityEventJS, $EntityEventJS$Type} from "packages/dev/latvian/mods/kubejs/entity/$EntityEventJS"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EntityEnterChunkEventJS extends $EntityEventJS {

constructor(entity: $Entity$Type, chunkX: integer, chunkY: integer, chunkZ: integer, prevX: integer, prevY: integer, prevZ: integer)

public static "of"(entity: $Entity$Type, chunkX: integer, chunkY: integer, chunkZ: integer, prevX: integer, prevY: integer, prevZ: integer): $EntityEnterChunkEventJS
public "getChunkX"(): integer
public "getChunkZ"(): integer
public "getChunkY"(): integer
public "getEntity"(): $Entity
public "getPrevY"(): integer
public "getPrevZ"(): integer
public "getPrevX"(): integer
get "chunkX"(): integer
get "chunkZ"(): integer
get "chunkY"(): integer
get "entity"(): $Entity
get "prevY"(): integer
get "prevZ"(): integer
get "prevX"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityEnterChunkEventJS$Type = ($EntityEnterChunkEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityEnterChunkEventJS_ = $EntityEnterChunkEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/impl/$CustomJadeProvider" {
import {$JadeProviderBuilder, $JadeProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$JadeProviderBuilder"
import {$IJadeProvider, $IJadeProvider$Type} from "packages/snownee/jade/api/$IJadeProvider"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $CustomJadeProvider<T extends $JadeProviderBuilder> implements $IJadeProvider {

constructor(builder: T)

public "getUid"(): $ResourceLocation
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomJadeProvider$Type<T> = ($CustomJadeProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomJadeProvider_<T> = $CustomJadeProvider$Type<(T)>;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/events/$JEIEventJS" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CustomJSRecipe, $CustomJSRecipe$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomJSRecipe"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $JEIEventJS extends $EventJS {
static readonly "customRecipeTypes": $Map<($ResourceLocation), ($RecipeType<($CustomJSRecipe)>)>
static readonly "overriddenRecipeTypes": $Map<($ResourceLocation), ($RecipeType)>
static "JEI_HELPERS": $IJeiHelpers

constructor()

public static "removeCustomRecipeType"(recipeType: $ResourceLocation$Type): void
public static "getOrCreateCustomRecipeType"(recipeType: $ResourceLocation$Type): $RecipeType<($CustomJSRecipe)>
public static "getCustomRecipeType"(recipeType: $ResourceLocation$Type): $RecipeType<($CustomJSRecipe)>
public static "clearOverriddenRecipeTypes"(): void
public static "clearCustomRecipeTypes"(): void
public static "removeOverriddenRecipeType"(recipeType: $ResourceLocation$Type): void
public static "getOverriddenRecipeType"(recipeType: $ResourceLocation$Type): $RecipeType<(any)>
public static "getOrCreateCustomOverriddenRecipeType"<T>(recipeType: $ResourceLocation$Type, existingRecipeType: $RecipeType$Type<(T)>): $RecipeType<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEIEventJS$Type = ($JEIEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEIEventJS_ = $JEIEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/$AdditionalJadePlugin" {
import {$IWailaClientRegistration, $IWailaClientRegistration$Type} from "packages/snownee/jade/api/$IWailaClientRegistration"
import {$IWailaPlugin, $IWailaPlugin$Type} from "packages/snownee/jade/api/$IWailaPlugin"
import {$IWailaCommonRegistration, $IWailaCommonRegistration$Type} from "packages/snownee/jade/api/$IWailaCommonRegistration"

export class $AdditionalJadePlugin implements $IWailaPlugin {

constructor()

public "register"(registration: $IWailaCommonRegistration$Type): void
public "registerClient"(registration: $IWailaClientRegistration$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdditionalJadePlugin$Type = ($AdditionalJadePlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdditionalJadePlugin_ = $AdditionalJadePlugin$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/builder/$EntityComponentProviderBuilder$IconRetriever" {
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$EntityAccessor, $EntityAccessor$Type} from "packages/snownee/jade/api/$EntityAccessor"

export interface $EntityComponentProviderBuilder$IconRetriever {

 "getIcon"(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement

(arg0: $EntityAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
}

export namespace $EntityComponentProviderBuilder$IconRetriever {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityComponentProviderBuilder$IconRetriever$Type = ($EntityComponentProviderBuilder$IconRetriever);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityComponentProviderBuilder$IconRetriever_ = $EntityComponentProviderBuilder$IconRetriever$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/events/$RegisterRecipeCatalystsEventJS" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$JEIEventJS, $JEIEventJS$Type} from "packages/pie/ilikepiefoo/compat/jei/events/$JEIEventJS"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CustomJSRecipe, $CustomJSRecipe$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomJSRecipe"
import {$IRecipeCatalystRegistration, $IRecipeCatalystRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCatalystRegistration"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $RegisterRecipeCatalystsEventJS extends $JEIEventJS {
readonly "data": $IRecipeCatalystRegistration
static readonly "customRecipeTypes": $Map<($ResourceLocation), ($RecipeType<($CustomJSRecipe)>)>
static readonly "overriddenRecipeTypes": $Map<($ResourceLocation), ($RecipeType)>
static "JEI_HELPERS": $IJeiHelpers

constructor(data: $IRecipeCatalystRegistration$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegisterRecipeCatalystsEventJS$Type = ($RegisterRecipeCatalystsEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegisterRecipeCatalystsEventJS_ = $RegisterRecipeCatalystsEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/util/$ReflectionUtils" {
import {$ReflectionUtils$Pair, $ReflectionUtils$Pair$Type} from "packages/pie/ilikepiefoo/util/$ReflectionUtils$Pair"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $ReflectionUtils {

constructor()

public static "retrieveEventClass"<T>(eventProvider: $Class$Type<(any)>, fieldName: string, eventType: $Class$Type<(T)>): $ReflectionUtils$Pair<($Class<(any)>), (T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReflectionUtils$Type = ($ReflectionUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReflectionUtils_ = $ReflectionUtils$Type;
}}
declare module "packages/pie/ilikepiefoo/$AdditionsPlugin" {
import {$KubeJSPlugin, $KubeJSPlugin$Type} from "packages/dev/latvian/mods/kubejs/$KubeJSPlugin"
import {$BindingsEvent, $BindingsEvent$Type} from "packages/dev/latvian/mods/kubejs/script/$BindingsEvent"
import {$ScriptType, $ScriptType$Type} from "packages/dev/latvian/mods/kubejs/script/$ScriptType"
import {$TypeWrappers, $TypeWrappers$Type} from "packages/dev/latvian/mods/rhino/util/wrap/$TypeWrappers"

export class $AdditionsPlugin extends $KubeJSPlugin {

constructor()

public "registerBindings"(event: $BindingsEvent$Type): void
public "initStartup"(): void
public "registerEvents"(): void
public "registerTypeWrappers"(type: $ScriptType$Type, typeWrappers: $TypeWrappers$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdditionsPlugin$Type = ($AdditionsPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdditionsPlugin_ = $AdditionsPlugin$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/events/$RegisterRecipesEventJS" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$JEIEventJS, $JEIEventJS$Type} from "packages/pie/ilikepiefoo/compat/jei/events/$JEIEventJS"
import {$IRecipeRegistration, $IRecipeRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CustomJSRecipe, $CustomJSRecipe$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomJSRecipe"
import {$CustomJSRecipe$CustomRecipeListBuilder, $CustomJSRecipe$CustomRecipeListBuilder$Type} from "packages/pie/ilikepiefoo/compat/jei/impl/$CustomJSRecipe$CustomRecipeListBuilder"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $RegisterRecipesEventJS extends $JEIEventJS {
readonly "data": $IRecipeRegistration
readonly "customRecipeListBuilders": $List<($CustomJSRecipe$CustomRecipeListBuilder)>
static readonly "customRecipeTypes": $Map<($ResourceLocation), ($RecipeType<($CustomJSRecipe)>)>
static readonly "overriddenRecipeTypes": $Map<($ResourceLocation), ($RecipeType)>
static "JEI_HELPERS": $IJeiHelpers

constructor(data: $IRecipeRegistration$Type)

public "register"<T>(recipeType: $RecipeType$Type<(T)>, recipes: $List$Type<(T)>): void
public "custom"(recipeType: $ResourceLocation$Type): $CustomJSRecipe$CustomRecipeListBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegisterRecipesEventJS$Type = ($RegisterRecipesEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegisterRecipesEventJS_ = $RegisterRecipesEventJS$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/impl/$CustomRecipeCategoryDecorator$DrawDecorator" {
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export interface $CustomRecipeCategoryDecorator$DrawDecorator<R> {

 "decorate"(arg0: R, arg1: $IRecipeCategory$Type<(R)>, arg2: $IRecipeSlotsView$Type, arg3: $GuiGraphics$Type, arg4: double, arg5: double): void

(arg0: R, arg1: $IRecipeCategory$Type<(R)>, arg2: $IRecipeSlotsView$Type, arg3: $GuiGraphics$Type, arg4: double, arg5: double): void
}

export namespace $CustomRecipeCategoryDecorator$DrawDecorator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomRecipeCategoryDecorator$DrawDecorator$Type<R> = ($CustomRecipeCategoryDecorator$DrawDecorator<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomRecipeCategoryDecorator$DrawDecorator_<R> = $CustomRecipeCategoryDecorator$DrawDecorator$Type<(R)>;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/builder/$RecipeCategoryBuilder$GetRegisterName" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $RecipeCategoryBuilder$GetRegisterName<T> {

 "getRegistryName"(arg0: T): $ResourceLocation

(arg0: T): $ResourceLocation
}

export namespace $RecipeCategoryBuilder$GetRegisterName {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeCategoryBuilder$GetRegisterName$Type<T> = ($RecipeCategoryBuilder$GetRegisterName<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeCategoryBuilder$GetRegisterName_<T> = $RecipeCategoryBuilder$GetRegisterName$Type<(T)>;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/builder/$EntityComponentProviderBuilder" {
import {$EntityComponentProviderBuilder$TooltipRetriever, $EntityComponentProviderBuilder$TooltipRetriever$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$EntityComponentProviderBuilder$TooltipRetriever"
import {$EntityComponentProviderBuilder$IconRetriever, $EntityComponentProviderBuilder$IconRetriever$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$EntityComponentProviderBuilder$IconRetriever"
import {$ToggleableProviderBuilder, $ToggleableProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$ToggleableProviderBuilder"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $EntityComponentProviderBuilder extends $ToggleableProviderBuilder {

constructor(uniqueIdentifier: $ResourceLocation$Type)

public "tooltip"(tooltipRetriever: $EntityComponentProviderBuilder$TooltipRetriever$Type): $EntityComponentProviderBuilder
public "icon"(iconRetriever: $EntityComponentProviderBuilder$IconRetriever$Type): $EntityComponentProviderBuilder
public "getTooltipRetriever"(): $EntityComponentProviderBuilder$TooltipRetriever
public "getIconRetriever"(): $EntityComponentProviderBuilder$IconRetriever
public "setTooltipRetriever"(tooltipRetriever: $EntityComponentProviderBuilder$TooltipRetriever$Type): $EntityComponentProviderBuilder
public "tooltipRetriever"(tooltipRetriever: $EntityComponentProviderBuilder$TooltipRetriever$Type): $EntityComponentProviderBuilder
public "setIconRetriever"(iconRetriever: $EntityComponentProviderBuilder$IconRetriever$Type): $EntityComponentProviderBuilder
public "iconRetriever"(iconRetriever: $EntityComponentProviderBuilder$IconRetriever$Type): $EntityComponentProviderBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityComponentProviderBuilder$Type = ($EntityComponentProviderBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityComponentProviderBuilder_ = $EntityComponentProviderBuilder$Type;
}}
declare module "packages/pie/ilikepiefoo/compat/jade/builder/$ServerDataProviderBuilder" {
import {$JadeProviderBuilder, $JadeProviderBuilder$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$JadeProviderBuilder"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Accessor, $Accessor$Type} from "packages/snownee/jade/api/$Accessor"
import {$ServerDataProviderBuilder$AppendServerDataCallback, $ServerDataProviderBuilder$AppendServerDataCallback$Type} from "packages/pie/ilikepiefoo/compat/jade/builder/$ServerDataProviderBuilder$AppendServerDataCallback"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ServerDataProviderBuilder<T extends $Accessor<(any)>> extends $JadeProviderBuilder {

constructor(uniqueIdentifier: $ResourceLocation$Type)

public static "doNothing"<T extends $Accessor<(any)>>(compoundTag: $CompoundTag$Type, accessor: T): void
public "getCallback"(): $ServerDataProviderBuilder$AppendServerDataCallback<(T)>
public "setCallback"(callback: $ServerDataProviderBuilder$AppendServerDataCallback$Type<(T)>): $ServerDataProviderBuilder<(T)>
get "callback"(): $ServerDataProviderBuilder$AppendServerDataCallback<(T)>
set "callback"(value: $ServerDataProviderBuilder$AppendServerDataCallback$Type<(T)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerDataProviderBuilder$Type<T> = ($ServerDataProviderBuilder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerDataProviderBuilder_<T> = $ServerDataProviderBuilder$Type<(T)>;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/builder/$RecipeCategoryWrapperBuilder" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$RecipeCategoryBuilder, $RecipeCategoryBuilder$Type} from "packages/pie/ilikepiefoo/compat/jei/builder/$RecipeCategoryBuilder"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"

export class $RecipeCategoryWrapperBuilder<T> extends $RecipeCategoryBuilder<(T)> {

constructor(recipeType: $RecipeType$Type<(T)>, jeiHelpers: $IJeiHelpers$Type, recipeCategory: $IRecipeCategory$Type<(T)>)

public "getSourceCategory"(): $IRecipeCategory<(T)>
get "sourceCategory"(): $IRecipeCategory<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeCategoryWrapperBuilder$Type<T> = ($RecipeCategoryWrapperBuilder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeCategoryWrapperBuilder_<T> = $RecipeCategoryWrapperBuilder$Type<(T)>;
}}
declare module "packages/pie/ilikepiefoo/compat/jei/builder/$RecipeCategoryBuilder$TooltipHandler" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export interface $RecipeCategoryBuilder$TooltipHandler<T> {

 "getTooltipStrings"(arg0: T, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>

(arg0: T, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
}

export namespace $RecipeCategoryBuilder$TooltipHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeCategoryBuilder$TooltipHandler$Type<T> = ($RecipeCategoryBuilder$TooltipHandler<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeCategoryBuilder$TooltipHandler_<T> = $RecipeCategoryBuilder$TooltipHandler$Type<(T)>;
}}
