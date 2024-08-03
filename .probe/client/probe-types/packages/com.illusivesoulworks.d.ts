declare module "packages/com/illusivesoulworks/polymorph/common/impl/$RecipePair" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$IRecipePair, $IRecipePair$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IRecipePair"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $RecipePair extends $Record implements $IRecipePair {

constructor(resourceLocation: $ResourceLocation$Type, output: $ItemStack$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $IRecipePair$Type): integer
public "output"(): $ItemStack
public "getResourceLocation"(): $ResourceLocation
public "resourceLocation"(): $ResourceLocation
public "getOutput"(): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipePair$Type = ($RecipePair);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipePair_ = $RecipePair$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/capability/$StackRecipeData" {
import {$IStackRecipeData, $IStackRecipeData$Type} from "packages/com/illusivesoulworks/polymorph/api/common/capability/$IStackRecipeData"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$AbstractRecipeData, $AbstractRecipeData$Type} from "packages/com/illusivesoulworks/polymorph/common/capability/$AbstractRecipeData"

export class $StackRecipeData extends $AbstractRecipeData<($ItemStack)> implements $IStackRecipeData {

constructor(arg0: $ItemStack$Type)

public "getListeners"(): $Set<($ServerPlayer)>
get "listeners"(): $Set<($ServerPlayer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StackRecipeData$Type = ($StackRecipeData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StackRecipeData_ = $StackRecipeData$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/network/server/$SPacketBlockEntityRecipeSync" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $SPacketBlockEntityRecipeSync extends $Record {

constructor(blockPos: $BlockPos$Type, selected: $ResourceLocation$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "decode"(arg0: $FriendlyByteBuf$Type): $SPacketBlockEntityRecipeSync
public static "encode"(arg0: $SPacketBlockEntityRecipeSync$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $SPacketBlockEntityRecipeSync$Type): void
public "selected"(): $ResourceLocation
public "getSelected"(): $ResourceLocation
public "getBlockPos"(): $BlockPos
public "blockPos"(): $BlockPos
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SPacketBlockEntityRecipeSync$Type = ($SPacketBlockEntityRecipeSync);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SPacketBlockEntityRecipeSync_ = $SPacketBlockEntityRecipeSync$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/impl/$PolymorphCommon" {
import {$IPolymorphCommon$IContainer2ItemStack, $IPolymorphCommon$IContainer2ItemStack$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphCommon$IContainer2ItemStack"
import {$IPolymorphCommon, $IPolymorphCommon$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphCommon"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IPolymorphCommon$IContainer2BlockEntity, $IPolymorphCommon$IContainer2BlockEntity$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphCommon$IContainer2BlockEntity"
import {$IPolymorphCommon$IBlockEntity2RecipeData, $IPolymorphCommon$IBlockEntity2RecipeData$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphCommon$IBlockEntity2RecipeData"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$IPolymorphCommon$IItemStack2RecipeData, $IPolymorphCommon$IItemStack2RecipeData$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphCommon$IItemStack2RecipeData"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$IPolymorphPacketDistributor, $IPolymorphPacketDistributor$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphPacketDistributor"

export class $PolymorphCommon implements $IPolymorphCommon {

constructor()

public static "get"(): $IPolymorphCommon
public "registerContainer2BlockEntity"(arg0: $IPolymorphCommon$IContainer2BlockEntity$Type): void
public "registerBlockEntity2RecipeData"(arg0: $IPolymorphCommon$IBlockEntity2RecipeData$Type): void
public "setServer"(arg0: $MinecraftServer$Type): void
public "getServer"(): $Optional<($MinecraftServer)>
public "tryCreateRecipeData"(arg0: $ItemStack$Type): $Optional<(any)>
public "tryCreateRecipeData"(arg0: $BlockEntity$Type): $Optional<(any)>
public "registerItemStack2RecipeData"(arg0: $IPolymorphCommon$IItemStack2RecipeData$Type): void
public "registerContainer2ItemStack"(arg0: $IPolymorphCommon$IContainer2ItemStack$Type): void
public "getRecipeDataFromBlockEntity"(arg0: $AbstractContainerMenu$Type): $Optional<(any)>
public "getRecipeDataFromItemStack"(arg0: $AbstractContainerMenu$Type): $Optional<(any)>
public "getRecipeData"(arg0: $BlockEntity$Type): $Optional<(any)>
public "getRecipeData"(arg0: $ItemStack$Type): $Optional<(any)>
public "getRecipeData"(arg0: $Player$Type): $Optional<(any)>
public "getPacketDistributor"(): $IPolymorphPacketDistributor
set "server"(value: $MinecraftServer$Type)
get "server"(): $Optional<($MinecraftServer)>
get "packetDistributor"(): $IPolymorphPacketDistributor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolymorphCommon$Type = ($PolymorphCommon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolymorphCommon_ = $PolymorphCommon$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/integration/$PolymorphIntegrations" {
import {$AbstractCompatibilityModule, $AbstractCompatibilityModule$Type} from "packages/com/illusivesoulworks/polymorph/common/integration/$AbstractCompatibilityModule"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $PolymorphIntegrations {

constructor()

public static "get"(): $Set<($AbstractCompatibilityModule)>
public static "init"(): void
public static "setup"(): void
public static "isActive"(arg0: string): boolean
public static "clientSetup"(): void
public static "loadConfig"(): void
public static "disable"(arg0: string): void
public static "openContainer"(arg0: $AbstractContainerMenu$Type, arg1: $ServerPlayer$Type): void
public static "selectRecipe"(arg0: $BlockEntity$Type, arg1: $AbstractContainerMenu$Type, arg2: $Recipe$Type<(any)>): void
public static "selectRecipe"(arg0: $AbstractContainerMenu$Type, arg1: $Recipe$Type<(any)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolymorphIntegrations$Type = ($PolymorphIntegrations);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolymorphIntegrations_ = $PolymorphIntegrations$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/client/widget/$SelectionWidget" {
import {$FocusNavigationEvent, $FocusNavigationEvent$Type} from "packages/net/minecraft/client/gui/navigation/$FocusNavigationEvent"
import {$OutputWidget, $OutputWidget$Type} from "packages/com/illusivesoulworks/polymorph/api/client/widget/$OutputWidget"
import {$IRecipePair, $IRecipePair$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IRecipePair"
import {$ScreenRectangle, $ScreenRectangle$Type} from "packages/net/minecraft/client/gui/navigation/$ScreenRectangle"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ComponentPath, $ComponentPath$Type} from "packages/net/minecraft/client/gui/$ComponentPath"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"

export class $SelectionWidget implements $Renderable, $GuiEventListener {

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $Consumer$Type<($ResourceLocation$Type)>, arg5: $AbstractContainerScreen$Type<(any)>)

public "isActive"(): boolean
public "renderTooltip"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "setActive"(arg0: boolean): void
public "setFocused"(arg0: boolean): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "isFocused"(): boolean
public "getOutputWidgets"(): $List<($OutputWidget)>
public "setRecipeList"(arg0: $Set$Type<($IRecipePair$Type)>): void
public "setOffsets"(arg0: integer, arg1: integer): void
public "highlightButton"(arg0: $ResourceLocation$Type): void
public "setPosition"(arg0: integer, arg1: integer): void
public "getCurrentFocusPath"(): $ComponentPath
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "nextFocusPath"(arg0: $FocusNavigationEvent$Type): $ComponentPath
public "isMouseOver"(arg0: double, arg1: double): boolean
public "getRectangle"(): $ScreenRectangle
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "keyReleased"(arg0: integer, arg1: integer, arg2: integer): boolean
public "mouseMoved"(arg0: double, arg1: double): void
public "getTabOrderGroup"(): integer
get "active"(): boolean
set "active"(value: boolean)
set "focused"(value: boolean)
get "focused"(): boolean
get "outputWidgets"(): $List<($OutputWidget)>
set "recipeList"(value: $Set$Type<($IRecipePair$Type)>)
get "currentFocusPath"(): $ComponentPath
get "rectangle"(): $ScreenRectangle
get "tabOrderGroup"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SelectionWidget$Type = ($SelectionWidget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SelectionWidget_ = $SelectionWidget$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/network/client/$CPacketPlayerRecipeSelection" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $CPacketPlayerRecipeSelection extends $Record {

constructor(recipe: $ResourceLocation$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "decode"(arg0: $FriendlyByteBuf$Type): $CPacketPlayerRecipeSelection
public static "encode"(arg0: $CPacketPlayerRecipeSelection$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $CPacketPlayerRecipeSelection$Type, arg1: $ServerPlayer$Type): void
public "recipe"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CPacketPlayerRecipeSelection$Type = ($CPacketPlayerRecipeSelection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CPacketPlayerRecipeSelection_ = $CPacketPlayerRecipeSelection$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/client/recipe/widget/$PlayerRecipesWidget" {
import {$AbstractRecipesWidget, $AbstractRecipesWidget$Type} from "packages/com/illusivesoulworks/polymorph/api/client/widget/$AbstractRecipesWidget"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $PlayerRecipesWidget extends $AbstractRecipesWidget {
static readonly "WIDGETS": $ResourceLocation
static readonly "BUTTON_X_OFFSET": integer
static readonly "BUTTON_Y_OFFSET": integer
static readonly "WIDGET_X_OFFSET": integer
static readonly "WIDGET_Y_OFFSET": integer

constructor(arg0: $AbstractContainerScreen$Type<(any)>, arg1: $Slot$Type)

public "getOutputSlot"(): $Slot
public "selectRecipe"(arg0: $ResourceLocation$Type): void
get "outputSlot"(): $Slot
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerRecipesWidget$Type = ($PlayerRecipesWidget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerRecipesWidget_ = $PlayerRecipesWidget$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/network/client/$CPacketStackRecipeSelection" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $CPacketStackRecipeSelection extends $Record {

constructor(recipe: $ResourceLocation$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "decode"(arg0: $FriendlyByteBuf$Type): $CPacketStackRecipeSelection
public static "encode"(arg0: $CPacketStackRecipeSelection$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $CPacketStackRecipeSelection$Type, arg1: $ServerPlayer$Type): void
public "recipe"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CPacketStackRecipeSelection$Type = ($CPacketStackRecipeSelection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CPacketStackRecipeSelection_ = $CPacketStackRecipeSelection$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/client/widget/$AbstractRecipesWidget" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IRecipesWidget, $IRecipesWidget$Type} from "packages/com/illusivesoulworks/polymorph/api/client/base/$IRecipesWidget"
import {$IRecipePair, $IRecipePair$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IRecipePair"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"
import {$SelectionWidget, $SelectionWidget$Type} from "packages/com/illusivesoulworks/polymorph/api/client/widget/$SelectionWidget"

export class $AbstractRecipesWidget implements $IRecipesWidget {
static readonly "WIDGETS": $ResourceLocation
static readonly "BUTTON_X_OFFSET": integer
static readonly "BUTTON_Y_OFFSET": integer
static readonly "WIDGET_X_OFFSET": integer
static readonly "WIDGET_Y_OFFSET": integer

constructor(arg0: $AbstractContainerScreen$Type<(any)>, arg1: integer, arg2: integer)
constructor(arg0: $AbstractContainerScreen$Type<(any)>)

public "setRecipesList"(arg0: $Set$Type<($IRecipePair$Type)>, arg1: $ResourceLocation$Type): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "getXPos"(): integer
public "getYPos"(): integer
public "getSelectionWidget"(): $SelectionWidget
public "highlightRecipe"(arg0: $ResourceLocation$Type): void
public "initChildWidgets"(): void
public "selectRecipe"(arg0: $ResourceLocation$Type): void
public "getOutputSlot"(): $Slot
get "xPos"(): integer
get "yPos"(): integer
get "selectionWidget"(): $SelectionWidget
get "outputSlot"(): $Slot
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractRecipesWidget$Type = ($AbstractRecipesWidget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractRecipesWidget_ = $AbstractRecipesWidget$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/network/server/$ClientPacketHandler" {
import {$SPacketRecipesList, $SPacketRecipesList$Type} from "packages/com/illusivesoulworks/polymorph/common/network/server/$SPacketRecipesList"
import {$SPacketBlockEntityRecipeSync, $SPacketBlockEntityRecipeSync$Type} from "packages/com/illusivesoulworks/polymorph/common/network/server/$SPacketBlockEntityRecipeSync"
import {$SPacketPlayerRecipeSync, $SPacketPlayerRecipeSync$Type} from "packages/com/illusivesoulworks/polymorph/common/network/server/$SPacketPlayerRecipeSync"
import {$SPacketHighlightRecipe, $SPacketHighlightRecipe$Type} from "packages/com/illusivesoulworks/polymorph/common/network/server/$SPacketHighlightRecipe"

export class $ClientPacketHandler {

constructor()

public static "handle"(arg0: $SPacketHighlightRecipe$Type): void
public static "handle"(arg0: $SPacketRecipesList$Type): void
public static "handle"(arg0: $SPacketBlockEntityRecipeSync$Type): void
public static "handle"(arg0: $SPacketPlayerRecipeSync$Type): void
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
declare module "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphCommon$IContainer2BlockEntity" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"

export interface $IPolymorphCommon$IContainer2BlockEntity {

 "getBlockEntity"(arg0: $AbstractContainerMenu$Type): $BlockEntity

(arg0: $AbstractContainerMenu$Type): $BlockEntity
}

export namespace $IPolymorphCommon$IContainer2BlockEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPolymorphCommon$IContainer2BlockEntity$Type = ($IPolymorphCommon$IContainer2BlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPolymorphCommon$IContainer2BlockEntity_ = $IPolymorphCommon$IContainer2BlockEntity$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/$PolymorphApi" {
import {$IPolymorphClient, $IPolymorphClient$Type} from "packages/com/illusivesoulworks/polymorph/api/client/base/$IPolymorphClient"
import {$IPolymorphCommon, $IPolymorphCommon$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphCommon"

export class $PolymorphApi {
static readonly "MOD_ID": string

constructor()

public static "common"(): $IPolymorphCommon
public static "client"(): $IPolymorphClient
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolymorphApi$Type = ($PolymorphApi);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolymorphApi_ = $PolymorphApi$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/platform/$ForgeClientPlatform" {
import {$IClientPlatform, $IClientPlatform$Type} from "packages/com/illusivesoulworks/polymorph/platform/services/$IClientPlatform"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $ForgeClientPlatform implements $IClientPlatform {

constructor()

public "getScreenLeft"(arg0: $AbstractContainerScreen$Type<(any)>): integer
public "getScreenTop"(arg0: $AbstractContainerScreen$Type<(any)>): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeClientPlatform$Type = ($ForgeClientPlatform);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeClientPlatform_ = $ForgeClientPlatform$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/$PolymorphForgeCapabilities" {
import {$IBlockEntityRecipeData, $IBlockEntityRecipeData$Type} from "packages/com/illusivesoulworks/polymorph/api/common/capability/$IBlockEntityRecipeData"
import {$IStackRecipeData, $IStackRecipeData$Type} from "packages/com/illusivesoulworks/polymorph/api/common/capability/$IStackRecipeData"
import {$IPlayerRecipeData, $IPlayerRecipeData$Type} from "packages/com/illusivesoulworks/polymorph/api/common/capability/$IPlayerRecipeData"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"

export class $PolymorphForgeCapabilities {
static readonly "PLAYER_RECIPE_DATA": $Capability<($IPlayerRecipeData)>
static readonly "BLOCK_ENTITY_RECIPE_DATA": $Capability<($IBlockEntityRecipeData)>
static readonly "STACK_RECIPE_DATA": $Capability<($IStackRecipeData)>
static readonly "PLAYER_RECIPE_DATA_ID": $ResourceLocation
static readonly "BLOCK_ENTITY_RECIPE_DATA_ID": $ResourceLocation
static readonly "STACK_RECIPE_DATA_ID": $ResourceLocation

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolymorphForgeCapabilities$Type = ($PolymorphForgeCapabilities);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolymorphForgeCapabilities_ = $PolymorphForgeCapabilities$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/mixin/core/$AccessorAbstractFurnaceBlockEntity" {
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $AccessorAbstractFurnaceBlockEntity {

 "getItems"(): $NonNullList<($ItemStack)>

(): $NonNullList<($ItemStack)>
}

export namespace $AccessorAbstractFurnaceBlockEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessorAbstractFurnaceBlockEntity$Type = ($AccessorAbstractFurnaceBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessorAbstractFurnaceBlockEntity_ = $AccessorAbstractFurnaceBlockEntity$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/client/widget/$OpenSelectionButton" {
import {$ImageButton, $ImageButton$Type} from "packages/net/minecraft/client/gui/components/$ImageButton"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $OpenSelectionButton extends $ImageButton {
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

constructor(arg0: $AbstractContainerScreen$Type<(any)>, arg1: integer, arg2: integer, arg3: $Button$OnPress$Type)

public "setOffsets"(arg0: integer, arg1: integer): void
public "renderWidget"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OpenSelectionButton$Type = ($OpenSelectionButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OpenSelectionButton_ = $OpenSelectionButton$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/client/base/$IRecipesWidget" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IRecipePair, $IRecipePair$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IRecipePair"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$SelectionWidget, $SelectionWidget$Type} from "packages/com/illusivesoulworks/polymorph/api/client/widget/$SelectionWidget"

export interface $IRecipesWidget {

 "setRecipesList"(arg0: $Set$Type<($IRecipePair$Type)>, arg1: $ResourceLocation$Type): void
 "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
 "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
 "getXPos"(): integer
 "getYPos"(): integer
 "getSelectionWidget"(): $SelectionWidget
 "highlightRecipe"(arg0: $ResourceLocation$Type): void
 "getOutputSlot"(): $Slot
 "initChildWidgets"(): void
 "selectRecipe"(arg0: $ResourceLocation$Type): void
}

export namespace $IRecipesWidget {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipesWidget$Type = ($IRecipesWidget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipesWidget_ = $IRecipesWidget$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/capability/$PlayerRecipeData" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$IPlayerRecipeData, $IPlayerRecipeData$Type} from "packages/com/illusivesoulworks/polymorph/api/common/capability/$IPlayerRecipeData"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$AbstractRecipeData, $AbstractRecipeData$Type} from "packages/com/illusivesoulworks/polymorph/common/capability/$AbstractRecipeData"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export class $PlayerRecipeData extends $AbstractRecipeData<($Player)> implements $IPlayerRecipeData {

constructor(arg0: $Player$Type)

public "getListeners"(): $Set<($ServerPlayer)>
public "sendRecipesListToListeners"(arg0: boolean): void
public "selectRecipe"(arg0: $Recipe$Type<(any)>): void
public "getRecipe"<T extends $Recipe<(C)>, C extends $Container>(arg0: $RecipeType$Type<(T)>, arg1: C, arg2: $Level$Type, arg3: $List$Type<(T)>): $Optional<(T)>
public "setContainerMenu"(arg0: $AbstractContainerMenu$Type): void
public "getContainerMenu"(): $AbstractContainerMenu
get "listeners"(): $Set<($ServerPlayer)>
set "containerMenu"(value: $AbstractContainerMenu$Type)
get "containerMenu"(): $AbstractContainerMenu
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerRecipeData$Type = ($PlayerRecipeData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerRecipeData_ = $PlayerRecipeData$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/$PolymorphCommonEvents" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $PolymorphCommonEvents {

constructor()

public static "openContainer"(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type): void
public static "playerDisconnected"(arg0: $ServerPlayer$Type): void
public static "levelTick"(arg0: $Level$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolymorphCommonEvents$Type = ($PolymorphCommonEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolymorphCommonEvents_ = $PolymorphCommonEvents$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/client/base/$ITickingRecipesWidget" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ITickingRecipesWidget {

 "tick"(): void

(): void
}

export namespace $ITickingRecipesWidget {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITickingRecipesWidget$Type = ($ITickingRecipesWidget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITickingRecipesWidget_ = $ITickingRecipesWidget$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphCommon$IItemStack2RecipeData" {
import {$IStackRecipeData, $IStackRecipeData$Type} from "packages/com/illusivesoulworks/polymorph/api/common/capability/$IStackRecipeData"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $IPolymorphCommon$IItemStack2RecipeData {

 "createRecipeData"(arg0: $ItemStack$Type): $IStackRecipeData

(arg0: $ItemStack$Type): $IStackRecipeData
}

export namespace $IPolymorphCommon$IItemStack2RecipeData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPolymorphCommon$IItemStack2RecipeData$Type = ($IPolymorphCommon$IItemStack2RecipeData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPolymorphCommon$IItemStack2RecipeData_ = $IPolymorphCommon$IItemStack2RecipeData$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/mixin/core/$AccessorSmithingTrimRecipe" {
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"

export interface $AccessorSmithingTrimRecipe {

 "getBase"(): $Ingredient
 "getAddition"(): $Ingredient
 "getTemplate"(): $Ingredient
}

export namespace $AccessorSmithingTrimRecipe {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessorSmithingTrimRecipe$Type = ($AccessorSmithingTrimRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessorSmithingTrimRecipe_ = $AccessorSmithingTrimRecipe$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/server/wrapper/$IngredientWrapper" {
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"

export class $IngredientWrapper {

constructor(arg0: $Ingredient$Type)

public "matches"(arg0: $IngredientWrapper$Type): boolean
public "getIngredient"(): $Ingredient
get "ingredient"(): $Ingredient
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientWrapper$Type = ($IngredientWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientWrapper_ = $IngredientWrapper$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphPacketDistributor" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$SortedSet, $SortedSet$Type} from "packages/java/util/$SortedSet"
import {$IRecipePair, $IRecipePair$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IRecipePair"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $IPolymorphPacketDistributor {

 "sendRecipesListS2C"(arg0: $ServerPlayer$Type): void
 "sendRecipesListS2C"(arg0: $ServerPlayer$Type, arg1: $SortedSet$Type<($IRecipePair$Type)>): void
 "sendRecipesListS2C"(arg0: $ServerPlayer$Type, arg1: $SortedSet$Type<($IRecipePair$Type)>, arg2: $ResourceLocation$Type): void
 "sendPlayerSyncS2C"(arg0: $ServerPlayer$Type, arg1: $SortedSet$Type<($IRecipePair$Type)>, arg2: $ResourceLocation$Type): void
 "sendPlayerRecipeSelectionC2S"(arg0: $ResourceLocation$Type): void
 "sendPersistentRecipeSelectionC2S"(arg0: $ResourceLocation$Type): void
 "sendStackRecipeSelectionC2S"(arg0: $ResourceLocation$Type): void
 "sendHighlightRecipeS2C"(arg0: $ServerPlayer$Type, arg1: $ResourceLocation$Type): void
 "sendBlockEntitySyncS2C"(arg0: $BlockPos$Type, arg1: $ResourceLocation$Type): void
 "sendBlockEntityListenerC2S"(arg0: boolean): void
}

export namespace $IPolymorphPacketDistributor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPolymorphPacketDistributor$Type = ($IPolymorphPacketDistributor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPolymorphPacketDistributor_ = $IPolymorphPacketDistributor$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/client/impl/$PolymorphClient" {
import {$IPolymorphClient$IRecipesWidgetFactory, $IPolymorphClient$IRecipesWidgetFactory$Type} from "packages/com/illusivesoulworks/polymorph/api/client/base/$IPolymorphClient$IRecipesWidgetFactory"
import {$IPolymorphClient, $IPolymorphClient$Type} from "packages/com/illusivesoulworks/polymorph/api/client/base/$IPolymorphClient"
import {$IRecipesWidget, $IRecipesWidget$Type} from "packages/com/illusivesoulworks/polymorph/api/client/base/$IRecipesWidget"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $PolymorphClient implements $IPolymorphClient {

constructor()

public static "get"(): $IPolymorphClient
public static "setup"(): void
public "registerWidget"(arg0: $IPolymorphClient$IRecipesWidgetFactory$Type): void
public "findCraftingResultSlot"(arg0: $AbstractContainerScreen$Type<(any)>): $Optional<($Slot)>
public "getWidget"(arg0: $AbstractContainerScreen$Type<(any)>): $Optional<($IRecipesWidget)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolymorphClient$Type = ($PolymorphClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolymorphClient_ = $PolymorphClient$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/network/server/$SPacketRecipesList" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$SortedSet, $SortedSet$Type} from "packages/java/util/$SortedSet"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$IRecipePair, $IRecipePair$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IRecipePair"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $SPacketRecipesList extends $Record {

constructor(recipeList: $SortedSet$Type<($IRecipePair$Type)>, selected: $ResourceLocation$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "decode"(arg0: $FriendlyByteBuf$Type): $SPacketRecipesList
public static "encode"(arg0: $SPacketRecipesList$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $SPacketRecipesList$Type): void
public "selected"(): $ResourceLocation
public "getSelected"(): $ResourceLocation
public "recipeList"(): $SortedSet<($IRecipePair)>
public "getRecipeList"(): $SortedSet<($IRecipePair)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SPacketRecipesList$Type = ($SPacketRecipesList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SPacketRecipesList_ = $SPacketRecipesList$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/capability/$AbstractHighlightedRecipeData" {
import {$SortedSet, $SortedSet$Type} from "packages/java/util/$SortedSet"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$IRecipePair, $IRecipePair$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IRecipePair"
import {$AbstractBlockEntityRecipeData, $AbstractBlockEntityRecipeData$Type} from "packages/com/illusivesoulworks/polymorph/common/capability/$AbstractBlockEntityRecipeData"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $AbstractHighlightedRecipeData<E extends $BlockEntity> extends $AbstractBlockEntityRecipeData<(E)> {

constructor(arg0: E)

public "getPacketData"(): $Pair<($SortedSet<($IRecipePair)>), ($ResourceLocation)>
public "selectRecipe"(arg0: $Recipe$Type<(any)>): void
get "packetData"(): $Pair<($SortedSet<($IRecipePair)>), ($ResourceLocation)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractHighlightedRecipeData$Type<E> = ($AbstractHighlightedRecipeData<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractHighlightedRecipeData_<E> = $AbstractHighlightedRecipeData$Type<(E)>;
}}
declare module "packages/com/illusivesoulworks/polymorph/mixin/core/$AccessorSmithingTransformRecipe" {
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"

export interface $AccessorSmithingTransformRecipe {

 "getBase"(): $Ingredient
 "getAddition"(): $Ingredient
 "getTemplate"(): $Ingredient
}

export namespace $AccessorSmithingTransformRecipe {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessorSmithingTransformRecipe$Type = ($AccessorSmithingTransformRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessorSmithingTransformRecipe_ = $AccessorSmithingTransformRecipe$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/$PolymorphForgePacketDistributor" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$SortedSet, $SortedSet$Type} from "packages/java/util/$SortedSet"
import {$IRecipePair, $IRecipePair$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IRecipePair"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$IPolymorphPacketDistributor, $IPolymorphPacketDistributor$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphPacketDistributor"

export class $PolymorphForgePacketDistributor implements $IPolymorphPacketDistributor {

constructor()

public "sendRecipesListS2C"(arg0: $ServerPlayer$Type): void
public "sendRecipesListS2C"(arg0: $ServerPlayer$Type, arg1: $SortedSet$Type<($IRecipePair$Type)>): void
public "sendRecipesListS2C"(arg0: $ServerPlayer$Type, arg1: $SortedSet$Type<($IRecipePair$Type)>, arg2: $ResourceLocation$Type): void
public "sendPlayerSyncS2C"(arg0: $ServerPlayer$Type, arg1: $SortedSet$Type<($IRecipePair$Type)>, arg2: $ResourceLocation$Type): void
public "sendPlayerRecipeSelectionC2S"(arg0: $ResourceLocation$Type): void
public "sendPersistentRecipeSelectionC2S"(arg0: $ResourceLocation$Type): void
public "sendStackRecipeSelectionC2S"(arg0: $ResourceLocation$Type): void
public "sendHighlightRecipeS2C"(arg0: $ServerPlayer$Type, arg1: $ResourceLocation$Type): void
public "sendBlockEntitySyncS2C"(arg0: $BlockPos$Type, arg1: $ResourceLocation$Type): void
public "sendBlockEntityListenerC2S"(arg0: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolymorphForgePacketDistributor$Type = ($PolymorphForgePacketDistributor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolymorphForgePacketDistributor_ = $PolymorphForgePacketDistributor$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/network/server/$SPacketPlayerRecipeSync" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$SortedSet, $SortedSet$Type} from "packages/java/util/$SortedSet"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$IRecipePair, $IRecipePair$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IRecipePair"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $SPacketPlayerRecipeSync extends $Record {

constructor(recipeList: $SortedSet$Type<($IRecipePair$Type)>, selected: $ResourceLocation$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "decode"(arg0: $FriendlyByteBuf$Type): $SPacketPlayerRecipeSync
public static "encode"(arg0: $SPacketPlayerRecipeSync$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $SPacketPlayerRecipeSync$Type): void
public "selected"(): $ResourceLocation
public "getSelected"(): $ResourceLocation
public "recipeList"(): $SortedSet<($IRecipePair)>
public "getRecipeList"(): $SortedSet<($IRecipePair)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SPacketPlayerRecipeSync$Type = ($SPacketPlayerRecipeSync);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SPacketPlayerRecipeSync_ = $SPacketPlayerRecipeSync$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/capability/$AbstractRecipeData" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$IRecipePair, $IRecipePair$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IRecipePair"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$IRecipeData, $IRecipeData$Type} from "packages/com/illusivesoulworks/polymorph/api/common/capability/$IRecipeData"
import {$SortedSet, $SortedSet$Type} from "packages/java/util/$SortedSet"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export class $AbstractRecipeData<E> implements $IRecipeData<(E)> {

constructor(arg0: E)

public "isEmpty"(arg0: $Container$Type): boolean
public "getOwner"(): E
public "isFailing"(): boolean
public "setFailing"(arg0: boolean): void
public "writeNBT"(): $CompoundTag
public "getSelectedRecipe"(): $Optional<(any)>
public "readNBT"(arg0: $CompoundTag$Type): void
public "setRecipesList"(arg0: $SortedSet$Type<($IRecipePair$Type)>): void
public "getRecipesList"(): $SortedSet<($IRecipePair)>
public "setSelectedRecipe"(arg0: $Recipe$Type<(any)>): void
public "getListeners"(): $Set<($ServerPlayer)>
public "sendRecipesListToListeners"(arg0: boolean): void
public "getPacketData"(): $Pair<($SortedSet<($IRecipePair)>), ($ResourceLocation)>
public "selectRecipe"(arg0: $Recipe$Type<(any)>): void
public "getRecipe"<T extends $Recipe<(C)>, C extends $Container>(arg0: $RecipeType$Type<(T)>, arg1: C, arg2: $Level$Type, arg3: $List$Type<(T)>): $Optional<(T)>
public "getLastRecipe"(): $Optional<(any)>
public "getLoadedRecipe"(): $Optional<($ResourceLocation)>
get "owner"(): E
get "failing"(): boolean
set "failing"(value: boolean)
get "selectedRecipe"(): $Optional<(any)>
set "recipesList"(value: $SortedSet$Type<($IRecipePair$Type)>)
get "recipesList"(): $SortedSet<($IRecipePair)>
set "selectedRecipe"(value: $Recipe$Type<(any)>)
get "listeners"(): $Set<($ServerPlayer)>
get "packetData"(): $Pair<($SortedSet<($IRecipePair)>), ($ResourceLocation)>
get "lastRecipe"(): $Optional<(any)>
get "loadedRecipe"(): $Optional<($ResourceLocation)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractRecipeData$Type<E> = ($AbstractRecipeData<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractRecipeData_<E> = $AbstractRecipeData$Type<(E)>;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/integration/fastfurnace/$FastFurnaceModule" {
import {$AbstractCompatibilityModule, $AbstractCompatibilityModule$Type} from "packages/com/illusivesoulworks/polymorph/common/integration/$AbstractCompatibilityModule"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $FastFurnaceModule extends $AbstractCompatibilityModule {

constructor()

public "selectRecipe"(arg0: $BlockEntity$Type, arg1: $Recipe$Type<(any)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastFurnaceModule$Type = ($FastFurnaceModule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastFurnaceModule_ = $FastFurnaceModule$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/integration/$AbstractCompatibilityModule" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $AbstractCompatibilityModule {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractCompatibilityModule$Type = ($AbstractCompatibilityModule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractCompatibilityModule_ = $AbstractCompatibilityModule$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/server/wrapper/$SmithingRecipeWrapper" {
import {$RecipeWrapper, $RecipeWrapper$Type} from "packages/com/illusivesoulworks/polymorph/server/wrapper/$RecipeWrapper"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $SmithingRecipeWrapper extends $RecipeWrapper {

constructor(arg0: $Recipe$Type<(any)>)

public "conflicts"(arg0: $RecipeWrapper$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SmithingRecipeWrapper$Type = ($SmithingRecipeWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SmithingRecipeWrapper_ = $SmithingRecipeWrapper$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/platform/services/$IPlatform" {
import {$PolymorphIntegrations$Loader, $PolymorphIntegrations$Loader$Type} from "packages/com/illusivesoulworks/polymorph/common/integration/$PolymorphIntegrations$Loader"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IPolymorphPacketDistributor, $IPolymorphPacketDistributor$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphPacketDistributor"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export interface $IPlatform {

 "getLoader"(): $PolymorphIntegrations$Loader
 "isModFileLoaded"(arg0: string): boolean
 "getConfigDir"(): $Path
 "isShaped"(arg0: $Recipe$Type<(any)>): boolean
 "getGameDir"(): $Path
 "getRecipeData"(arg0: $ItemStack$Type): $Optional<(any)>
 "getRecipeData"(arg0: $BlockEntity$Type): $Optional<(any)>
 "getRecipeData"(arg0: $Player$Type): $Optional<(any)>
 "isSameShape"(arg0: $Recipe$Type<(any)>, arg1: $Recipe$Type<(any)>): boolean
 "isModLoaded"(arg0: string): boolean
 "getPacketDistributor"(): $IPolymorphPacketDistributor
}

export namespace $IPlatform {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlatform$Type = ($IPlatform);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlatform_ = $IPlatform$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/capability/$PolymorphCapabilities" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $PolymorphCapabilities {

constructor()

public static "getRecipeData"(arg0: $ItemStack$Type): $Optional<(any)>
public static "getRecipeData"(arg0: $BlockEntity$Type): $Optional<(any)>
public static "getRecipeData"(arg0: $Player$Type): $Optional<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolymorphCapabilities$Type = ($PolymorphCapabilities);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolymorphCapabilities_ = $PolymorphCapabilities$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/client/$ClientEventsListener" {
import {$ScreenEvent$Init$Post, $ScreenEvent$Init$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$Init$Post"
import {$ScreenEvent$Render$Post, $ScreenEvent$Render$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$Render$Post"
import {$TickEvent$ClientTickEvent, $TickEvent$ClientTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ClientTickEvent"
import {$ScreenEvent$MouseButtonPressed$Pre, $ScreenEvent$MouseButtonPressed$Pre$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$MouseButtonPressed$Pre"

export class $ClientEventsListener {

constructor()

public "tick"(arg0: $TickEvent$ClientTickEvent$Type): void
public "initScreen"(arg0: $ScreenEvent$Init$Post$Type): void
public "render"(arg0: $ScreenEvent$Render$Post$Type): void
public "mouseClick"(arg0: $ScreenEvent$MouseButtonPressed$Pre$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientEventsListener$Type = ($ClientEventsListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientEventsListener_ = $ClientEventsListener$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/$PolymorphConstants" {
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"

export class $PolymorphConstants {
static readonly "MOD_ID": string
static readonly "MOD_NAME": string
static readonly "LOG": $Logger

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolymorphConstants$Type = ($PolymorphConstants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolymorphConstants_ = $PolymorphConstants$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/capability/$AbstractBlockEntityRecipeData" {
import {$IBlockEntityRecipeData, $IBlockEntityRecipeData$Type} from "packages/com/illusivesoulworks/polymorph/api/common/capability/$IBlockEntityRecipeData"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$SortedSet, $SortedSet$Type} from "packages/java/util/$SortedSet"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IRecipePair, $IRecipePair$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IRecipePair"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$AbstractRecipeData, $AbstractRecipeData$Type} from "packages/com/illusivesoulworks/polymorph/common/capability/$AbstractRecipeData"

export class $AbstractBlockEntityRecipeData<E extends $BlockEntity> extends $AbstractRecipeData<($BlockEntity)> implements $IBlockEntityRecipeData {

constructor(arg0: E)

public "isEmpty"(): boolean
public "isEmpty"(arg0: $Container$Type): boolean
public "getOwner"(): E
public "tick"(): void
public "removeListener"(arg0: $ServerPlayer$Type): void
public "getListeners"(): $Set<($ServerPlayer)>
public "addListener"(arg0: $ServerPlayer$Type): void
public "getPacketData"(): $Pair<($SortedSet<($IRecipePair)>), ($ResourceLocation)>
get "empty"(): boolean
get "owner"(): E
get "listeners"(): $Set<($ServerPlayer)>
get "packetData"(): $Pair<($SortedSet<($IRecipePair)>), ($ResourceLocation)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractBlockEntityRecipeData$Type<E> = ($AbstractBlockEntityRecipeData<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractBlockEntityRecipeData_<E> = $AbstractBlockEntityRecipeData$Type<(E)>;
}}
declare module "packages/com/illusivesoulworks/polymorph/client/recipe/$RecipesWidget" {
import {$SortedSet, $SortedSet$Type} from "packages/java/util/$SortedSet"
import {$IRecipesWidget, $IRecipesWidget$Type} from "packages/com/illusivesoulworks/polymorph/api/client/base/$IRecipesWidget"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IRecipePair, $IRecipePair$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IRecipePair"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $RecipesWidget {

constructor()

public static "get"(): $Optional<($IRecipesWidget)>
public static "clear"(): void
public static "create"(arg0: $AbstractContainerScreen$Type<(any)>): void
public static "enqueueRecipesList"(arg0: $SortedSet$Type<($IRecipePair$Type)>, arg1: $ResourceLocation$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipesWidget$Type = ($RecipesWidget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipesWidget_ = $RecipesWidget$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/capability/$FurnaceRecipeData" {
import {$AbstractFurnaceBlockEntity, $AbstractFurnaceBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$AbstractFurnaceBlockEntity"
import {$AbstractHighlightedRecipeData, $AbstractHighlightedRecipeData$Type} from "packages/com/illusivesoulworks/polymorph/common/capability/$AbstractHighlightedRecipeData"

export class $FurnaceRecipeData extends $AbstractHighlightedRecipeData<($AbstractFurnaceBlockEntity)> {

constructor(arg0: $AbstractFurnaceBlockEntity$Type)

public "isEmpty"(): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FurnaceRecipeData$Type = ($FurnaceRecipeData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FurnaceRecipeData_ = $FurnaceRecipeData$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/client/$PolymorphClientEvents" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $PolymorphClientEvents {

constructor()

public static "tick"(): void
public static "initScreen"(arg0: $Screen$Type): void
public static "render"(arg0: $Screen$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float): void
public static "mouseClick"(arg0: $Screen$Type, arg1: double, arg2: double, arg3: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolymorphClientEvents$Type = ($PolymorphClientEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolymorphClientEvents_ = $PolymorphClientEvents$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/mixin/core/$AccessorInventoryMenu" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$CraftingContainer, $CraftingContainer$Type} from "packages/net/minecraft/world/inventory/$CraftingContainer"
import {$ResultContainer, $ResultContainer$Type} from "packages/net/minecraft/world/inventory/$ResultContainer"

export interface $AccessorInventoryMenu {

 "getOwner"(): $Player
 "getResultSlots"(): $ResultContainer
 "getCraftSlots"(): $CraftingContainer
}

export namespace $AccessorInventoryMenu {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessorInventoryMenu$Type = ($AccessorInventoryMenu);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessorInventoryMenu_ = $AccessorInventoryMenu$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/common/capability/$IPlayerRecipeData" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$IRecipePair, $IRecipePair$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IRecipePair"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$IRecipeData, $IRecipeData$Type} from "packages/com/illusivesoulworks/polymorph/api/common/capability/$IRecipeData"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$SortedSet, $SortedSet$Type} from "packages/java/util/$SortedSet"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export interface $IPlayerRecipeData extends $IRecipeData<($Player)> {

 "setContainerMenu"(arg0: $AbstractContainerMenu$Type): void
 "getContainerMenu"(): $AbstractContainerMenu
 "isEmpty"(arg0: $Container$Type): boolean
 "getOwner"(): $Player
 "isFailing"(): boolean
 "setFailing"(arg0: boolean): void
 "writeNBT"(): $CompoundTag
 "getSelectedRecipe"(): $Optional<(any)>
 "readNBT"(arg0: $CompoundTag$Type): void
 "setRecipesList"(arg0: $SortedSet$Type<($IRecipePair$Type)>): void
 "getRecipesList"(): $SortedSet<($IRecipePair)>
 "setSelectedRecipe"(arg0: $Recipe$Type<(any)>): void
 "getListeners"(): $Set<($ServerPlayer)>
 "sendRecipesListToListeners"(arg0: boolean): void
 "getPacketData"(): $Pair<($SortedSet<($IRecipePair)>), ($ResourceLocation)>
 "selectRecipe"(arg0: $Recipe$Type<(any)>): void
 "getRecipe"<T extends $Recipe<(C)>, C extends $Container>(arg0: $RecipeType$Type<(T)>, arg1: C, arg2: $Level$Type, arg3: $List$Type<(T)>): $Optional<(T)>
}

export namespace $IPlayerRecipeData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlayerRecipeData$Type = ($IPlayerRecipeData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlayerRecipeData_ = $IPlayerRecipeData$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/server/wrapper/$CraftingRecipeWrapper" {
import {$RecipeWrapper, $RecipeWrapper$Type} from "packages/com/illusivesoulworks/polymorph/server/wrapper/$RecipeWrapper"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $CraftingRecipeWrapper extends $RecipeWrapper {

constructor(arg0: $Recipe$Type<(any)>)

public "isShaped"(): boolean
public "conflicts"(arg0: $RecipeWrapper$Type): boolean
get "shaped"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingRecipeWrapper$Type = ($CraftingRecipeWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingRecipeWrapper_ = $CraftingRecipeWrapper$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/client/widget/$OutputWidget" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$IRecipePair, $IRecipePair$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IRecipePair"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $OutputWidget extends $AbstractWidget {
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: $IRecipePair$Type)

public "setHighlighted"(arg0: boolean): void
public "m_87963_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "getWidth"(): integer
public "getResourceLocation"(): $ResourceLocation
public "getOutput"(): $ItemStack
set "highlighted"(value: boolean)
get "width"(): integer
get "resourceLocation"(): $ResourceLocation
get "output"(): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OutputWidget$Type = ($OutputWidget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OutputWidget_ = $OutputWidget$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/common/base/$IRecipePair" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $IRecipePair extends $Comparable<($IRecipePair)> {

 "getResourceLocation"(): $ResourceLocation
 "getOutput"(): $ItemStack
 "compareTo"(arg0: $IRecipePair$Type): integer
}

export namespace $IRecipePair {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipePair$Type = ($IRecipePair);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipePair_ = $IRecipePair$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/common/capability/$IStackRecipeData" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$IRecipePair, $IRecipePair$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IRecipePair"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$IRecipeData, $IRecipeData$Type} from "packages/com/illusivesoulworks/polymorph/api/common/capability/$IRecipeData"
import {$SortedSet, $SortedSet$Type} from "packages/java/util/$SortedSet"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export interface $IStackRecipeData extends $IRecipeData<($ItemStack)> {

 "isEmpty"(arg0: $Container$Type): boolean
 "getOwner"(): $ItemStack
 "isFailing"(): boolean
 "setFailing"(arg0: boolean): void
 "writeNBT"(): $CompoundTag
 "getSelectedRecipe"(): $Optional<(any)>
 "readNBT"(arg0: $CompoundTag$Type): void
 "setRecipesList"(arg0: $SortedSet$Type<($IRecipePair$Type)>): void
 "getRecipesList"(): $SortedSet<($IRecipePair)>
 "setSelectedRecipe"(arg0: $Recipe$Type<(any)>): void
 "getListeners"(): $Set<($ServerPlayer)>
 "sendRecipesListToListeners"(arg0: boolean): void
 "getPacketData"(): $Pair<($SortedSet<($IRecipePair)>), ($ResourceLocation)>
 "selectRecipe"(arg0: $Recipe$Type<(any)>): void
 "getRecipe"<T extends $Recipe<(C)>, C extends $Container>(arg0: $RecipeType$Type<(T)>, arg1: C, arg2: $Level$Type, arg3: $List$Type<(T)>): $Optional<(T)>
}

export namespace $IStackRecipeData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IStackRecipeData$Type = ($IStackRecipeData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IStackRecipeData_ = $IStackRecipeData$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/common/capability/$IBlockEntityRecipeData" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$IRecipePair, $IRecipePair$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IRecipePair"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$IRecipeData, $IRecipeData$Type} from "packages/com/illusivesoulworks/polymorph/api/common/capability/$IRecipeData"
import {$SortedSet, $SortedSet$Type} from "packages/java/util/$SortedSet"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export interface $IBlockEntityRecipeData extends $IRecipeData<($BlockEntity)> {

 "tick"(): void
 "removeListener"(arg0: $ServerPlayer$Type): void
 "addListener"(arg0: $ServerPlayer$Type): void
 "isEmpty"(arg0: $Container$Type): boolean
 "getOwner"(): $BlockEntity
 "isFailing"(): boolean
 "setFailing"(arg0: boolean): void
 "writeNBT"(): $CompoundTag
 "getSelectedRecipe"(): $Optional<(any)>
 "readNBT"(arg0: $CompoundTag$Type): void
 "setRecipesList"(arg0: $SortedSet$Type<($IRecipePair$Type)>): void
 "getRecipesList"(): $SortedSet<($IRecipePair)>
 "setSelectedRecipe"(arg0: $Recipe$Type<(any)>): void
 "getListeners"(): $Set<($ServerPlayer)>
 "sendRecipesListToListeners"(arg0: boolean): void
 "getPacketData"(): $Pair<($SortedSet<($IRecipePair)>), ($ResourceLocation)>
 "selectRecipe"(arg0: $Recipe$Type<(any)>): void
 "getRecipe"<T extends $Recipe<(C)>, C extends $Container>(arg0: $RecipeType$Type<(T)>, arg1: C, arg2: $Level$Type, arg3: $List$Type<(T)>): $Optional<(T)>
}

export namespace $IBlockEntityRecipeData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IBlockEntityRecipeData$Type = ($IBlockEntityRecipeData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IBlockEntityRecipeData_ = $IBlockEntityRecipeData$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/integration/fastbench/$FastBenchModule" {
import {$AbstractCompatibilityModule, $AbstractCompatibilityModule$Type} from "packages/com/illusivesoulworks/polymorph/common/integration/$AbstractCompatibilityModule"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $FastBenchModule extends $AbstractCompatibilityModule {

constructor()

public "selectRecipe"(arg0: $AbstractContainerMenu$Type, arg1: $Recipe$Type<(any)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastBenchModule$Type = ($FastBenchModule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastBenchModule_ = $FastBenchModule$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphCommon$IContainer2ItemStack" {
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $IPolymorphCommon$IContainer2ItemStack {

 "getItemStack"(arg0: $AbstractContainerMenu$Type): $ItemStack

(arg0: $AbstractContainerMenu$Type): $ItemStack
}

export namespace $IPolymorphCommon$IContainer2ItemStack {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPolymorphCommon$IContainer2ItemStack$Type = ($IPolymorphCommon$IContainer2ItemStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPolymorphCommon$IContainer2ItemStack_ = $IPolymorphCommon$IContainer2ItemStack$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/client/base/$IPolymorphClient" {
import {$IPolymorphClient$IRecipesWidgetFactory, $IPolymorphClient$IRecipesWidgetFactory$Type} from "packages/com/illusivesoulworks/polymorph/api/client/base/$IPolymorphClient$IRecipesWidgetFactory"
import {$IRecipesWidget, $IRecipesWidget$Type} from "packages/com/illusivesoulworks/polymorph/api/client/base/$IRecipesWidget"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export interface $IPolymorphClient {

 "registerWidget"(arg0: $IPolymorphClient$IRecipesWidgetFactory$Type): void
 "findCraftingResultSlot"(arg0: $AbstractContainerScreen$Type<(any)>): $Optional<($Slot)>
 "getWidget"(arg0: $AbstractContainerScreen$Type<(any)>): $Optional<($IRecipesWidget)>
}

export namespace $IPolymorphClient {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPolymorphClient$Type = ($IPolymorphClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPolymorphClient_ = $IPolymorphClient$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/$CommonEventsListener" {
import {$PlayerContainerEvent$Open, $PlayerContainerEvent$Open$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerContainerEvent$Open"
import {$RegisterCommandsEvent, $RegisterCommandsEvent$Type} from "packages/net/minecraftforge/event/$RegisterCommandsEvent"
import {$ServerAboutToStartEvent, $ServerAboutToStartEvent$Type} from "packages/net/minecraftforge/event/server/$ServerAboutToStartEvent"
import {$PlayerEvent$PlayerLoggedOutEvent, $PlayerEvent$PlayerLoggedOutEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerLoggedOutEvent"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$AttachCapabilitiesEvent, $AttachCapabilitiesEvent$Type} from "packages/net/minecraftforge/event/$AttachCapabilitiesEvent"
import {$RegisterCapabilitiesEvent, $RegisterCapabilitiesEvent$Type} from "packages/net/minecraftforge/common/capabilities/$RegisterCapabilitiesEvent"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$TickEvent$LevelTickEvent, $TickEvent$LevelTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$LevelTickEvent"
import {$ServerStoppedEvent, $ServerStoppedEvent$Type} from "packages/net/minecraftforge/event/server/$ServerStoppedEvent"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $CommonEventsListener {

constructor()

public "registerCommands"(arg0: $RegisterCommandsEvent$Type): void
public "serverStopped"(arg0: $ServerStoppedEvent$Type): void
public "serverAboutToStart"(arg0: $ServerAboutToStartEvent$Type): void
public "attachCapabilitiesStack"(arg0: $AttachCapabilitiesEvent$Type<($ItemStack$Type)>): void
public "attachCapabilitiesPlayer"(arg0: $AttachCapabilitiesEvent$Type<($Entity$Type)>): void
public "playerLoggedOut"(arg0: $PlayerEvent$PlayerLoggedOutEvent$Type): void
public "openContainer"(arg0: $PlayerContainerEvent$Open$Type): void
public "attachCapabilities"(arg0: $AttachCapabilitiesEvent$Type<($BlockEntity$Type)>): void
public "levelTick"(arg0: $TickEvent$LevelTickEvent$Type): void
public "registerCapabilities"(arg0: $RegisterCapabilitiesEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonEventsListener$Type = ($CommonEventsListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonEventsListener_ = $CommonEventsListener$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/network/server/$SPacketHighlightRecipe" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $SPacketHighlightRecipe extends $Record {

constructor(recipe: $ResourceLocation$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "decode"(arg0: $FriendlyByteBuf$Type): $SPacketHighlightRecipe
public static "encode"(arg0: $SPacketHighlightRecipe$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $SPacketHighlightRecipe$Type): void
public "recipe"(): $ResourceLocation
public "getRecipe"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SPacketHighlightRecipe$Type = ($SPacketHighlightRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SPacketHighlightRecipe_ = $SPacketHighlightRecipe$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/client/recipe/widget/$FurnaceRecipesWidget" {
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$PersistentRecipesWidget, $PersistentRecipesWidget$Type} from "packages/com/illusivesoulworks/polymorph/client/recipe/widget/$PersistentRecipesWidget"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $FurnaceRecipesWidget extends $PersistentRecipesWidget {
static readonly "WIDGETS": $ResourceLocation
static readonly "BUTTON_X_OFFSET": integer
static readonly "BUTTON_Y_OFFSET": integer
static readonly "WIDGET_X_OFFSET": integer
static readonly "WIDGET_Y_OFFSET": integer

constructor(arg0: $AbstractContainerScreen$Type<(any)>)

public "getOutputSlot"(): $Slot
get "outputSlot"(): $Slot
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FurnaceRecipesWidget$Type = ($FurnaceRecipesWidget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FurnaceRecipesWidget_ = $FurnaceRecipesWidget$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/platform/$ForgePlatform" {
import {$PolymorphIntegrations$Loader, $PolymorphIntegrations$Loader$Type} from "packages/com/illusivesoulworks/polymorph/common/integration/$PolymorphIntegrations$Loader"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$IPlatform, $IPlatform$Type} from "packages/com/illusivesoulworks/polymorph/platform/services/$IPlatform"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IPolymorphPacketDistributor, $IPolymorphPacketDistributor$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphPacketDistributor"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $ForgePlatform implements $IPlatform {

constructor()

public "getLoader"(): $PolymorphIntegrations$Loader
public "isModFileLoaded"(arg0: string): boolean
public "getConfigDir"(): $Path
public "isShaped"(arg0: $Recipe$Type<(any)>): boolean
public "getGameDir"(): $Path
public "getRecipeData"(arg0: $BlockEntity$Type): $Optional<(any)>
public "getRecipeData"(arg0: $Player$Type): $Optional<(any)>
public "getRecipeData"(arg0: $ItemStack$Type): $Optional<(any)>
public "isSameShape"(arg0: $Recipe$Type<(any)>, arg1: $Recipe$Type<(any)>): boolean
public "isModLoaded"(arg0: string): boolean
public "getPacketDistributor"(): $IPolymorphPacketDistributor
get "loader"(): $PolymorphIntegrations$Loader
get "configDir"(): $Path
get "gameDir"(): $Path
get "packetDistributor"(): $IPolymorphPacketDistributor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgePlatform$Type = ($ForgePlatform);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgePlatform_ = $ForgePlatform$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphCommon$IBlockEntity2RecipeData" {
import {$IBlockEntityRecipeData, $IBlockEntityRecipeData$Type} from "packages/com/illusivesoulworks/polymorph/api/common/capability/$IBlockEntityRecipeData"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"

export interface $IPolymorphCommon$IBlockEntity2RecipeData {

 "createRecipeData"(arg0: $BlockEntity$Type): $IBlockEntityRecipeData

(arg0: $BlockEntity$Type): $IBlockEntityRecipeData
}

export namespace $IPolymorphCommon$IBlockEntity2RecipeData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPolymorphCommon$IBlockEntity2RecipeData$Type = ($IPolymorphCommon$IBlockEntity2RecipeData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPolymorphCommon$IBlockEntity2RecipeData_ = $IPolymorphCommon$IBlockEntity2RecipeData$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphCommon" {
import {$IPolymorphCommon$IBlockEntity2RecipeData, $IPolymorphCommon$IBlockEntity2RecipeData$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphCommon$IBlockEntity2RecipeData"
import {$IPolymorphCommon$IContainer2ItemStack, $IPolymorphCommon$IContainer2ItemStack$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphCommon$IContainer2ItemStack"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$IPolymorphCommon$IItemStack2RecipeData, $IPolymorphCommon$IItemStack2RecipeData$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphCommon$IItemStack2RecipeData"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IPolymorphPacketDistributor, $IPolymorphPacketDistributor$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphPacketDistributor"
import {$IPolymorphCommon$IContainer2BlockEntity, $IPolymorphCommon$IContainer2BlockEntity$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IPolymorphCommon$IContainer2BlockEntity"

export interface $IPolymorphCommon {

 "registerContainer2BlockEntity"(arg0: $IPolymorphCommon$IContainer2BlockEntity$Type): void
 "registerBlockEntity2RecipeData"(arg0: $IPolymorphCommon$IBlockEntity2RecipeData$Type): void
 "setServer"(arg0: $MinecraftServer$Type): void
 "getServer"(): $Optional<($MinecraftServer)>
 "tryCreateRecipeData"(arg0: $BlockEntity$Type): $Optional<(any)>
 "tryCreateRecipeData"(arg0: $ItemStack$Type): $Optional<(any)>
 "registerItemStack2RecipeData"(arg0: $IPolymorphCommon$IItemStack2RecipeData$Type): void
 "registerContainer2ItemStack"(arg0: $IPolymorphCommon$IContainer2ItemStack$Type): void
 "getRecipeDataFromBlockEntity"(arg0: $AbstractContainerMenu$Type): $Optional<(any)>
 "getRecipeDataFromItemStack"(arg0: $AbstractContainerMenu$Type): $Optional<(any)>
 "getRecipeData"(arg0: $ItemStack$Type): $Optional<(any)>
 "getRecipeData"(arg0: $BlockEntity$Type): $Optional<(any)>
 "getRecipeData"(arg0: $Player$Type): $Optional<(any)>
 "getPacketDistributor"(): $IPolymorphPacketDistributor
}

export namespace $IPolymorphCommon {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPolymorphCommon$Type = ($IPolymorphCommon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPolymorphCommon_ = $IPolymorphCommon$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/platform/services/$IClientPlatform" {
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export interface $IClientPlatform {

 "getScreenLeft"(arg0: $AbstractContainerScreen$Type<(any)>): integer
 "getScreenTop"(arg0: $AbstractContainerScreen$Type<(any)>): integer
}

export namespace $IClientPlatform {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IClientPlatform$Type = ($IClientPlatform);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IClientPlatform_ = $IClientPlatform$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/server/$PolymorphCommands" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export class $PolymorphCommands {

constructor()

public static "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolymorphCommands$Type = ($PolymorphCommands);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolymorphCommands_ = $PolymorphCommands$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/platform/services/$IIntegrationPlatform" {
import {$AbstractCompatibilityModule, $AbstractCompatibilityModule$Type} from "packages/com/illusivesoulworks/polymorph/common/integration/$AbstractCompatibilityModule"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $IIntegrationPlatform {

 "createCompatibilityModules"(): $Map<(string), ($Supplier<($Supplier<($AbstractCompatibilityModule)>)>)>

(): $Map<(string), ($Supplier<($Supplier<($AbstractCompatibilityModule)>)>)>
}

export namespace $IIntegrationPlatform {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIntegrationPlatform$Type = ($IIntegrationPlatform);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIntegrationPlatform_ = $IIntegrationPlatform$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/mixin/$IntegratedMixinPlugin" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$IMixinErrorHandler$ErrorAction, $IMixinErrorHandler$ErrorAction$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinErrorHandler$ErrorAction"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$IMixinErrorHandler, $IMixinErrorHandler$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinErrorHandler"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IMixinConfig, $IMixinConfig$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfig"

export class $IntegratedMixinPlugin implements $IMixinConfigPlugin, $IMixinErrorHandler {

constructor()

public "onLoad"(arg0: string): void
public "postApply"(arg0: string, arg1: $ClassNode$Type, arg2: string, arg3: $IMixinInfo$Type): void
public "onPrepareError"(arg0: $IMixinConfig$Type, arg1: $Throwable$Type, arg2: $IMixinInfo$Type, arg3: $IMixinErrorHandler$ErrorAction$Type): $IMixinErrorHandler$ErrorAction
public "onApplyError"(arg0: string, arg1: $Throwable$Type, arg2: $IMixinInfo$Type, arg3: $IMixinErrorHandler$ErrorAction$Type): $IMixinErrorHandler$ErrorAction
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
export type $IntegratedMixinPlugin$Type = ($IntegratedMixinPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegratedMixinPlugin_ = $IntegratedMixinPlugin$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/client/base/$IPolymorphClient$IRecipesWidgetFactory" {
import {$IRecipesWidget, $IRecipesWidget$Type} from "packages/com/illusivesoulworks/polymorph/api/client/base/$IRecipesWidget"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export interface $IPolymorphClient$IRecipesWidgetFactory {

 "createWidget"(arg0: $AbstractContainerScreen$Type<(any)>): $IRecipesWidget

(arg0: $AbstractContainerScreen$Type<(any)>): $IRecipesWidget
}

export namespace $IPolymorphClient$IRecipesWidgetFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPolymorphClient$IRecipesWidgetFactory$Type = ($IPolymorphClient$IRecipesWidgetFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPolymorphClient$IRecipesWidgetFactory_ = $IPolymorphClient$IRecipesWidgetFactory$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/platform/$Services" {
import {$IPlatform, $IPlatform$Type} from "packages/com/illusivesoulworks/polymorph/platform/services/$IPlatform"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$IClientPlatform, $IClientPlatform$Type} from "packages/com/illusivesoulworks/polymorph/platform/services/$IClientPlatform"
import {$IIntegrationPlatform, $IIntegrationPlatform$Type} from "packages/com/illusivesoulworks/polymorph/platform/services/$IIntegrationPlatform"

export class $Services {
static readonly "INTEGRATION_PLATFORM": $IIntegrationPlatform
static readonly "CLIENT_PLATFORM": $IClientPlatform
static readonly "PLATFORM": $IPlatform

constructor()

public static "load"<T>(arg0: $Class$Type<(T)>): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Services$Type = ($Services);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Services_ = $Services$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/network/client/$CPacketPersistentRecipeSelection" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $CPacketPersistentRecipeSelection extends $Record {

constructor(recipe: $ResourceLocation$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "decode"(arg0: $FriendlyByteBuf$Type): $CPacketPersistentRecipeSelection
public static "encode"(arg0: $CPacketPersistentRecipeSelection$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $CPacketPersistentRecipeSelection$Type, arg1: $ServerPlayer$Type): void
public "recipe"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CPacketPersistentRecipeSelection$Type = ($CPacketPersistentRecipeSelection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CPacketPersistentRecipeSelection_ = $CPacketPersistentRecipeSelection$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/network/client/$CPacketBlockEntityListener" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export class $CPacketBlockEntityListener extends $Record {

constructor(add: boolean)

public "add"(): boolean
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "decode"(arg0: $FriendlyByteBuf$Type): $CPacketBlockEntityListener
public static "encode"(arg0: $CPacketBlockEntityListener$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $CPacketBlockEntityListener$Type, arg1: $ServerPlayer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CPacketBlockEntityListener$Type = ($CPacketBlockEntityListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CPacketBlockEntityListener_ = $CPacketBlockEntityListener$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/server/wrapper/$RecipeWrapper" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$IngredientWrapper, $IngredientWrapper$Type} from "packages/com/illusivesoulworks/polymorph/server/wrapper/$IngredientWrapper"

export class $RecipeWrapper {

constructor(arg0: $Recipe$Type<(any)>)

public "getId"(): $ResourceLocation
public "getIngredients"(): $List<($IngredientWrapper)>
public "getRecipe"(): $Recipe<(any)>
public "conflicts"(arg0: $RecipeWrapper$Type): boolean
get "id"(): $ResourceLocation
get "ingredients"(): $List<($IngredientWrapper)>
get "recipe"(): $Recipe<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeWrapper$Type = ($RecipeWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeWrapper_ = $RecipeWrapper$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/api/common/capability/$IRecipeData" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$IRecipePair, $IRecipePair$Type} from "packages/com/illusivesoulworks/polymorph/api/common/base/$IRecipePair"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$SortedSet, $SortedSet$Type} from "packages/java/util/$SortedSet"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export interface $IRecipeData<E> {

 "isEmpty"(arg0: $Container$Type): boolean
 "getOwner"(): E
 "isFailing"(): boolean
 "setFailing"(arg0: boolean): void
 "writeNBT"(): $CompoundTag
 "getSelectedRecipe"(): $Optional<(any)>
 "readNBT"(arg0: $CompoundTag$Type): void
 "setRecipesList"(arg0: $SortedSet$Type<($IRecipePair$Type)>): void
 "getRecipesList"(): $SortedSet<($IRecipePair)>
 "setSelectedRecipe"(arg0: $Recipe$Type<(any)>): void
 "getListeners"(): $Set<($ServerPlayer)>
 "sendRecipesListToListeners"(arg0: boolean): void
 "getPacketData"(): $Pair<($SortedSet<($IRecipePair)>), ($ResourceLocation)>
 "selectRecipe"(arg0: $Recipe$Type<(any)>): void
 "getRecipe"<T extends $Recipe<(C)>, C extends $Container>(arg0: $RecipeType$Type<(T)>, arg1: C, arg2: $Level$Type, arg3: $List$Type<(T)>): $Optional<(T)>
}

export namespace $IRecipeData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeData$Type<E> = ($IRecipeData<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeData_<E> = $IRecipeData$Type<(E)>;
}}
declare module "packages/com/illusivesoulworks/polymorph/mixin/core/$AccessorCraftingMenu" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$CraftingContainer, $CraftingContainer$Type} from "packages/net/minecraft/world/inventory/$CraftingContainer"
import {$ResultContainer, $ResultContainer$Type} from "packages/net/minecraft/world/inventory/$ResultContainer"

export interface $AccessorCraftingMenu {

 "getPlayer"(): $Player
 "getResultSlots"(): $ResultContainer
 "getCraftSlots"(): $CraftingContainer
}

export namespace $AccessorCraftingMenu {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessorCraftingMenu$Type = ($AccessorCraftingMenu);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessorCraftingMenu_ = $AccessorCraftingMenu$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/$PolymorphCommonMod" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PolymorphCommonMod {

constructor()

public static "init"(): void
public static "setup"(): void
public static "clientSetup"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolymorphCommonMod$Type = ($PolymorphCommonMod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolymorphCommonMod_ = $PolymorphCommonMod$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/client/recipe/widget/$PersistentRecipesWidget" {
import {$AbstractRecipesWidget, $AbstractRecipesWidget$Type} from "packages/com/illusivesoulworks/polymorph/api/client/widget/$AbstractRecipesWidget"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $PersistentRecipesWidget extends $AbstractRecipesWidget {
static readonly "WIDGETS": $ResourceLocation
static readonly "BUTTON_X_OFFSET": integer
static readonly "BUTTON_Y_OFFSET": integer
static readonly "WIDGET_X_OFFSET": integer
static readonly "WIDGET_Y_OFFSET": integer

constructor(arg0: $AbstractContainerScreen$Type<(any)>)

public "selectRecipe"(arg0: $ResourceLocation$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PersistentRecipesWidget$Type = ($PersistentRecipesWidget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PersistentRecipesWidget_ = $PersistentRecipesWidget$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/$PolymorphForgeNetwork" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $PolymorphForgeNetwork {

constructor()

public static "get"(): $SimpleChannel
public static "setup"(): void
public static "registerC2S"<M>(arg0: $Class$Type<(M)>, arg1: $BiConsumer$Type<(M), ($FriendlyByteBuf$Type)>, arg2: $Function$Type<($FriendlyByteBuf$Type), (M)>, arg3: $BiConsumer$Type<(M), ($ServerPlayer$Type)>): void
public static "registerS2C"<M>(arg0: $Class$Type<(M)>, arg1: $BiConsumer$Type<(M), ($FriendlyByteBuf$Type)>, arg2: $Function$Type<($FriendlyByteBuf$Type), (M)>, arg3: $Consumer$Type<(M)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolymorphForgeNetwork$Type = ($PolymorphForgeNetwork);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolymorphForgeNetwork_ = $PolymorphForgeNetwork$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/$PolymorphForgeMod" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PolymorphForgeMod {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolymorphForgeMod$Type = ($PolymorphForgeMod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolymorphForgeMod_ = $PolymorphForgeMod$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/platform/$ForgeIntegrationPlatform" {
import {$AbstractCompatibilityModule, $AbstractCompatibilityModule$Type} from "packages/com/illusivesoulworks/polymorph/common/integration/$AbstractCompatibilityModule"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$IIntegrationPlatform, $IIntegrationPlatform$Type} from "packages/com/illusivesoulworks/polymorph/platform/services/$IIntegrationPlatform"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ForgeIntegrationPlatform implements $IIntegrationPlatform {

constructor()

public "createCompatibilityModules"(): $Map<(string), ($Supplier<($Supplier<($AbstractCompatibilityModule)>)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeIntegrationPlatform$Type = ($ForgeIntegrationPlatform);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeIntegrationPlatform_ = $ForgeIntegrationPlatform$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/util/$BlockEntityTicker" {
import {$IBlockEntityRecipeData, $IBlockEntityRecipeData$Type} from "packages/com/illusivesoulworks/polymorph/api/common/capability/$IBlockEntityRecipeData"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export class $BlockEntityTicker {

constructor()

public static "add"(arg0: $ServerPlayer$Type, arg1: $IBlockEntityRecipeData$Type): void
public static "remove"(arg0: $ServerPlayer$Type): void
public static "clear"(): void
public static "tick"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityTicker$Type = ($BlockEntityTicker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityTicker_ = $BlockEntityTicker$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/integration/$PolymorphIntegrations$Loader" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $PolymorphIntegrations$Loader extends $Enum<($PolymorphIntegrations$Loader)> {
static readonly "FABRIC": $PolymorphIntegrations$Loader
static readonly "FORGE": $PolymorphIntegrations$Loader


public static "values"(): ($PolymorphIntegrations$Loader)[]
public static "valueOf"(arg0: string): $PolymorphIntegrations$Loader
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolymorphIntegrations$Loader$Type = (("fabric") | ("forge")) | ($PolymorphIntegrations$Loader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolymorphIntegrations$Loader_ = $PolymorphIntegrations$Loader$Type;
}}
declare module "packages/com/illusivesoulworks/polymorph/common/crafting/$RecipeSelection" {
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $RecipeSelection {

constructor()

public static "getPlayerRecipe"<T extends $Recipe<(C)>, C extends $Container>(arg0: $AbstractContainerMenu$Type, arg1: $RecipeType$Type<(T)>, arg2: C, arg3: $Level$Type, arg4: $Player$Type, arg5: $List$Type<(T)>): $Optional<(T)>
public static "getPlayerRecipe"<T extends $Recipe<(C)>, C extends $Container>(arg0: $AbstractContainerMenu$Type, arg1: $RecipeType$Type<(T)>, arg2: C, arg3: $Level$Type, arg4: $Player$Type): $Optional<(T)>
public static "getPlayerRecipe"<T extends $Recipe<(C)>, C extends $Container>(arg0: $AbstractContainerMenu$Type, arg1: $RecipeType$Type<(T)>, arg2: C, arg3: $Level$Type, arg4: $List$Type<($Slot$Type)>): $Optional<(T)>
public static "getBlockEntityRecipe"<T extends $Recipe<(C)>, C extends $Container>(arg0: $RecipeType$Type<(T)>, arg1: C, arg2: $Level$Type, arg3: $BlockEntity$Type): $Optional<(T)>
public static "getStackRecipe"<T extends $Recipe<(C)>, C extends $Container>(arg0: $RecipeType$Type<(T)>, arg1: C, arg2: $Level$Type, arg3: $ItemStack$Type): $Optional<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeSelection$Type = ($RecipeSelection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeSelection_ = $RecipeSelection$Type;
}}
