declare module "packages/fr/lucreeper74/createmetallurgy/compat/jei/category/elements/$FoundryTopElement" {
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $FoundryTopElement implements $IDrawable {

constructor()

public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "getWidth"(): integer
public "getHeight"(): integer
public "draw"(arg0: $GuiGraphics$Type): void
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoundryTopElement$Type = ($FoundryTopElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoundryTopElement_ = $FoundryTopElement$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/registries/$CMBlockEntityTypes" {
import {$CastingTableBlockEntity, $CastingTableBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/table/$CastingTableBlockEntity"
import {$CastingBasinBlockEntity, $CastingBasinBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/basin/$CastingBasinBlockEntity"
import {$FoundryMixerBlockEntity, $FoundryMixerBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/foundry_mixer/$FoundryMixerBlockEntity"
import {$BeltGrinderBlockEntity, $BeltGrinderBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/belt_grinder/$BeltGrinderBlockEntity"
import {$GlassedFoundryLidBlockEntity, $GlassedFoundryLidBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/glassed_foundry_lid/$GlassedFoundryLidBlockEntity"
import {$LightBulbBlockEntity, $LightBulbBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/$LightBulbBlockEntity"
import {$BlockEntityEntry, $BlockEntityEntry$Type} from "packages/com/tterrag/registrate/util/entry/$BlockEntityEntry"
import {$FoundryLidBlockEntity, $FoundryLidBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/foundry_lid/$FoundryLidBlockEntity"
import {$FoundryBasinBlockEntity, $FoundryBasinBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/foundry_basin/$FoundryBasinBlockEntity"

export class $CMBlockEntityTypes {
static readonly "FOUNDRY_BASIN": $BlockEntityEntry<($FoundryBasinBlockEntity)>
static readonly "CASTING_BASIN": $BlockEntityEntry<($CastingBasinBlockEntity)>
static readonly "CASTING_TABLE": $BlockEntityEntry<($CastingTableBlockEntity)>
static readonly "FOUNDRY_LID": $BlockEntityEntry<($FoundryLidBlockEntity)>
static readonly "GLASSED_ALLOYER_TOP": $BlockEntityEntry<($GlassedFoundryLidBlockEntity)>
static readonly "FOUNDRY_MIXER": $BlockEntityEntry<($FoundryMixerBlockEntity)>
static readonly "BELT_GRINDER": $BlockEntityEntry<($BeltGrinderBlockEntity)>
static readonly "LIGHT_BULB": $BlockEntityEntry<($LightBulbBlockEntity)>

constructor()

public static "register"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMBlockEntityTypes$Type = ($CMBlockEntityTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMBlockEntityTypes_ = $CMBlockEntityTypes$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/data/$CMDatagen" {
import {$GatherDataEvent, $GatherDataEvent$Type} from "packages/net/minecraftforge/data/event/$GatherDataEvent"

export class $CMDatagen {

constructor()

public static "gatherData"(arg0: $GatherDataEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMDatagen$Type = ($CMDatagen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMDatagen_ = $CMDatagen$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/utils/$CMLang" {
import {$FluidStack, $FluidStack$Type} from "packages/net/minecraftforge/fluids/$FluidStack"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$LangBuilder, $LangBuilder$Type} from "packages/com/simibubi/create/foundation/utility/$LangBuilder"

export class $CMLang {

constructor()

public static "builder"(): $LangBuilder
public static "number"(arg0: double): $LangBuilder
public static "text"(arg0: string): $LangBuilder
public static "asId"(arg0: string): string
public static "resolveBuilders"(arg0: (any)[]): (any)[]
public static "translate"(arg0: string, ...arg1: (any)[]): $LangBuilder
public static "translateDirect"(arg0: string, ...arg1: (any)[]): $MutableComponent
public static "fluidName"(arg0: $FluidStack$Type): $LangBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMLang$Type = ($CMLang);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMLang_ = $CMLang$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/casting/table/$CastingTableBlock" {
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CastingBlockEntity, $CastingBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/$CastingBlockEntity"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$CastingBlock, $CastingBlock$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/$CastingBlock"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export class $CastingTableBlock extends $CastingBlock {
static readonly "FACING": $DirectionProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getBlockEntityClass"(): $Class<($CastingBlockEntity)>
public "getBlockEntityType"(): $BlockEntityType<(any)>
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public static "onRemove"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type): void
get "blockEntityClass"(): $Class<($CastingBlockEntity)>
get "blockEntityType"(): $BlockEntityType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingTableBlock$Type = ($CastingTableBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingTableBlock_ = $CastingTableBlock$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/foundry_mixer/$AlloyingRecipe" {
import {$BasinRecipe, $BasinRecipe$Type} from "packages/com/simibubi/create/content/processing/basin/$BasinRecipe"
import {$ProcessingRecipeBuilder$ProcessingRecipeParams, $ProcessingRecipeBuilder$ProcessingRecipeParams$Type} from "packages/com/simibubi/create/content/processing/recipe/$ProcessingRecipeBuilder$ProcessingRecipeParams"

export class $AlloyingRecipe extends $BasinRecipe {

constructor(arg0: $ProcessingRecipeBuilder$ProcessingRecipeParams$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AlloyingRecipe$Type = ($AlloyingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AlloyingRecipe_ = $AlloyingRecipe$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/ponders/$FoundryScenes" {
import {$SceneBuildingUtil, $SceneBuildingUtil$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuildingUtil"
import {$SceneBuilder, $SceneBuilder$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuilder"

export class $FoundryScenes {

constructor()

public static "foundryBasin"(arg0: $SceneBuilder$Type, arg1: $SceneBuildingUtil$Type): void
public static "alloying"(arg0: $SceneBuilder$Type, arg1: $SceneBuildingUtil$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoundryScenes$Type = ($FoundryScenes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoundryScenes_ = $FoundryScenes$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/compat/jei/category/$MeltingCategory" {
import {$BasinRecipe, $BasinRecipe$Type} from "packages/com/simibubi/create/content/processing/basin/$BasinRecipe"
import {$BasinCategory, $BasinCategory$Type} from "packages/com/simibubi/create/compat/jei/category/$BasinCategory"
import {$CreateRecipeCategory$Info, $CreateRecipeCategory$Info$Type} from "packages/com/simibubi/create/compat/jei/category/$CreateRecipeCategory$Info"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export class $MeltingCategory extends $BasinCategory {

constructor(arg0: $CreateRecipeCategory$Info$Type<($BasinRecipe$Type)>)

public "draw"(arg0: $BasinRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MeltingCategory$Type = ($MeltingCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MeltingCategory_ = $MeltingCategory$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/utils/$TintedVertexBuilder" {
import {$VertexFormatElement, $VertexFormatElement$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormatElement"
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$PoseStack$Pose, $PoseStack$Pose$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack$Pose"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$BakedQuad, $BakedQuad$Type} from "packages/net/minecraft/client/renderer/block/model/$BakedQuad"

export class $TintedVertexBuilder implements $VertexConsumer {

constructor(arg0: $VertexConsumer$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer)

public "vertex"(arg0: double, arg1: double, arg2: double): $VertexConsumer
public "uv"(arg0: float, arg1: float): $VertexConsumer
public "color"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $VertexConsumer
public "normal"(arg0: float, arg1: float, arg2: float): $VertexConsumer
public "overlayCoords"(arg0: integer, arg1: integer): $VertexConsumer
public "uv2"(arg0: integer, arg1: integer): $VertexConsumer
public "defaultColor"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "unsetDefaultColor"(): void
public "endVertex"(): void
public "overlayCoords"(arg0: integer): $VertexConsumer
public "uv2"(arg0: integer): $VertexConsumer
public "vertex"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: integer, arg10: integer, arg11: float, arg12: float, arg13: float): void
public "putBulkData"(arg0: $PoseStack$Pose$Type, arg1: $BakedQuad$Type, arg2: float, arg3: float, arg4: float, arg5: integer, arg6: integer): void
public "putBulkData"(arg0: $PoseStack$Pose$Type, arg1: $BakedQuad$Type, arg2: (float)[], arg3: float, arg4: float, arg5: float, arg6: float, arg7: (integer)[], arg8: integer, arg9: boolean): void
public "putBulkData"(arg0: $PoseStack$Pose$Type, arg1: $BakedQuad$Type, arg2: (float)[], arg3: float, arg4: float, arg5: float, arg6: (integer)[], arg7: integer, arg8: boolean): void
public "color"(arg0: integer): $VertexConsumer
public "normal"(arg0: $Matrix3f$Type, arg1: float, arg2: float, arg3: float): $VertexConsumer
public "vertex"(arg0: $Matrix4f$Type, arg1: float, arg2: float, arg3: float): $VertexConsumer
public "color"(arg0: float, arg1: float, arg2: float, arg3: float): $VertexConsumer
public "putBulkData"(arg0: $PoseStack$Pose$Type, arg1: $BakedQuad$Type, arg2: float, arg3: float, arg4: float, arg5: float, arg6: integer, arg7: integer, arg8: boolean): void
public "applyBakedLighting"(arg0: integer, arg1: $ByteBuffer$Type): integer
public "applyBakedNormals"(arg0: $Vector3f$Type, arg1: $ByteBuffer$Type, arg2: $Matrix3f$Type): void
public "misc"(arg0: $VertexFormatElement$Type, ...arg1: (integer)[]): $VertexConsumer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TintedVertexBuilder$Type = ($TintedVertexBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TintedVertexBuilder_ = $TintedVertexBuilder$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/casting/basin/$CastingBasinBlockEntity" {
import {$IFluidHandler, $IFluidHandler$Type} from "packages/net/minecraftforge/fluids/capability/$IFluidHandler"
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$SmartFluidTankBehaviour, $SmartFluidTankBehaviour$Type} from "packages/com/simibubi/create/foundation/blockEntity/behaviour/fluid/$SmartFluidTankBehaviour"
import {$IHaveGoggleInformation, $IHaveGoggleInformation$Type} from "packages/com/simibubi/create/content/equipment/goggles/$IHaveGoggleInformation"
import {$BlockEntityBehaviour, $BlockEntityBehaviour$Type} from "packages/com/simibubi/create/foundation/blockEntity/behaviour/$BlockEntityBehaviour"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$IItemHandlerModifiable, $IItemHandlerModifiable$Type} from "packages/net/minecraftforge/items/$IItemHandlerModifiable"
import {$CastingBlockEntity, $CastingBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/$CastingBlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$SmartInventory, $SmartInventory$Type} from "packages/com/simibubi/create/foundation/item/$SmartInventory"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $CastingBasinBlockEntity extends $CastingBlockEntity implements $IHaveGoggleInformation {
 "itemCapability": $LazyOptional<($IItemHandlerModifiable)>
 "inputTank": $SmartFluidTankBehaviour
 "inv": $SmartInventory
 "moldInv": $SmartInventory
 "running": boolean
 "processingTick": integer
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockPos$Type, arg2: $BlockState$Type)

public "addBehaviours"(arg0: $List$Type<($BlockEntityBehaviour$Type)>): void
public "addToGoggleTooltip"(arg0: $List$Type<($Component$Type)>, arg1: boolean): boolean
public "containedFluidTooltip"(arg0: $List$Type<($Component$Type)>, arg1: boolean, arg2: $LazyOptional$Type<($IFluidHandler$Type)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingBasinBlockEntity$Type = ($CastingBasinBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingBasinBlockEntity_ = $CastingBasinBlockEntity$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/registries/$CMArmInteract$BeltGrinderType" {
import {$ArmInteractionPointType, $ArmInteractionPointType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$ArmInteractionPointType"
import {$ArmInteractionPoint, $ArmInteractionPoint$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$ArmInteractionPoint"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $CMArmInteract$BeltGrinderType extends $ArmInteractionPointType {

constructor(arg0: $ResourceLocation$Type)

public "canCreatePoint"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): boolean
public "createPoint"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): $ArmInteractionPoint
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMArmInteract$BeltGrinderType$Type = ($CMArmInteract$BeltGrinderType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMArmInteract$BeltGrinderType_ = $CMArmInteract$BeltGrinderType$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/compat/jei/$CreateMetallurgyJEI" {
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

export class $CreateMetallurgyJEI implements $IModPlugin {

constructor()

public "registerGuiHandlers"(arg0: $IGuiHandlerRegistration$Type): void
public "registerRecipeCatalysts"(arg0: $IRecipeCatalystRegistration$Type): void
public "getPluginUid"(): $ResourceLocation
public "registerRecipes"(arg0: $IRecipeRegistration$Type): void
public "registerCategories"(arg0: $IRecipeCategoryRegistration$Type): void
public "registerItemSubtypes"(arg0: $ISubtypeRegistration$Type): void
public "registerVanillaCategoryExtensions"(arg0: $IVanillaCategoryExtensionRegistration$Type): void
public "registerFluidSubtypes"<T>(arg0: $ISubtypeRegistration$Type, arg1: $IPlatformFluidHelper$Type<(T)>): void
public "onConfigManagerAvailable"(arg0: $IJeiConfigManager$Type): void
public "onRuntimeUnavailable"(): void
public "registerIngredients"(arg0: $IModIngredientRegistration$Type): void
public "registerRecipeTransferHandlers"(arg0: $IRecipeTransferRegistration$Type): void
public "registerAdvanced"(arg0: $IAdvancedRegistration$Type): void
public "onRuntimeAvailable"(arg0: $IJeiRuntime$Type): void
public "registerRuntime"(arg0: $IRuntimeRegistration$Type): void
get "pluginUid"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreateMetallurgyJEI$Type = ($CreateMetallurgyJEI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreateMetallurgyJEI_ = $CreateMetallurgyJEI$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/$CreateMetallurgy" {
import {$CreateRegistrate, $CreateRegistrate$Type} from "packages/com/simibubi/create/foundation/data/$CreateRegistrate"
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"
import {$NetworkHandler, $NetworkHandler$Type} from "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/network/$NetworkHandler"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $CreateMetallurgy {
static readonly "MOD_ID": string
static readonly "REGISTRATE": $CreateRegistrate
static readonly "LOGGER": $Logger
static readonly "NETWORK_HANDLER": $NetworkHandler

constructor()

public static "genRL"(arg0: string): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreateMetallurgy$Type = ($CreateMetallurgy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreateMetallurgy_ = $CreateMetallurgy$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/compat/jei/category/$AlloyingCategory" {
import {$BasinRecipe, $BasinRecipe$Type} from "packages/com/simibubi/create/content/processing/basin/$BasinRecipe"
import {$BasinCategory, $BasinCategory$Type} from "packages/com/simibubi/create/compat/jei/category/$BasinCategory"
import {$CreateRecipeCategory$Info, $CreateRecipeCategory$Info$Type} from "packages/com/simibubi/create/compat/jei/category/$CreateRecipeCategory$Info"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export class $AlloyingCategory extends $BasinCategory {

constructor(arg0: $CreateRecipeCategory$Info$Type<($BasinRecipe$Type)>)

public "draw"(arg0: $BasinRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AlloyingCategory$Type = ($AlloyingCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AlloyingCategory_ = $AlloyingCategory$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/compat/jei/category/elements/$CastingInTableElement" {
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $CastingInTableElement implements $IDrawable {

constructor()

public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "getWidth"(): integer
public "getHeight"(): integer
public "draw"(arg0: $GuiGraphics$Type): void
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingInTableElement$Type = ($CastingInTableElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingInTableElement_ = $CastingInTableElement$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/casting/$CastingBlockRenderer" {
import {$CastingBlockEntity, $CastingBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/$CastingBlockEntity"
import {$BlockEntityRendererProvider$Context, $BlockEntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider$Context"
import {$SmartBlockEntityRenderer, $SmartBlockEntityRenderer$Type} from "packages/com/simibubi/create/foundation/blockEntity/renderer/$SmartBlockEntityRenderer"

export class $CastingBlockRenderer extends $SmartBlockEntityRenderer<($CastingBlockEntity)> {

constructor(arg0: $BlockEntityRendererProvider$Context$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingBlockRenderer$Type = ($CastingBlockRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingBlockRenderer_ = $CastingBlockRenderer$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/belt_grinder/$BeltGrinderFilterSlot" {
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ValueBoxTransform, $ValueBoxTransform$Type} from "packages/com/simibubi/create/foundation/blockEntity/behaviour/$ValueBoxTransform"

export class $BeltGrinderFilterSlot extends $ValueBoxTransform {

constructor()

public "rotate"(arg0: $BlockState$Type, arg1: $PoseStack$Type): void
public "getLocalOffset"(arg0: $BlockState$Type): $Vec3
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BeltGrinderFilterSlot$Type = ($BeltGrinderFilterSlot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BeltGrinderFilterSlot_ = $BeltGrinderFilterSlot$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/casting/recipe/$CastingRecipeSerializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$FluidIngredient, $FluidIngredient$Type} from "packages/com/simibubi/create/foundation/fluid/$FluidIngredient"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$CastingRecipe, $CastingRecipe$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/recipe/$CastingRecipe"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$ProcessingOutput, $ProcessingOutput$Type} from "packages/com/simibubi/create/content/processing/recipe/$ProcessingOutput"

export class $CastingRecipeSerializer implements $RecipeSerializer<($CastingRecipe)> {

constructor()

public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type): $CastingRecipe
public "fromNetwork"(arg0: $ResourceLocation$Type, arg1: $FriendlyByteBuf$Type): $CastingRecipe
public "toNetwork"(arg0: $FriendlyByteBuf$Type, arg1: $CastingRecipe$Type): void
public "createRecipe"(arg0: $ResourceLocation$Type, arg1: $Ingredient$Type, arg2: $FluidIngredient$Type, arg3: integer, arg4: boolean, arg5: $ProcessingOutput$Type): $CastingRecipe
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type, arg2: $ICondition$IContext$Type): $CastingRecipe
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingRecipeSerializer$Type = ($CastingRecipeSerializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingRecipeSerializer_ = $CastingRecipeSerializer$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/registries/$CMArmInteract$CastingBasinType" {
import {$ArmInteractionPointType, $ArmInteractionPointType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$ArmInteractionPointType"
import {$ArmInteractionPoint, $ArmInteractionPoint$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$ArmInteractionPoint"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $CMArmInteract$CastingBasinType extends $ArmInteractionPointType {

constructor(arg0: $ResourceLocation$Type)

public "canCreatePoint"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): boolean
public "createPoint"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): $ArmInteractionPoint
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMArmInteract$CastingBasinType$Type = ($CMArmInteract$CastingBasinType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMArmInteract$CastingBasinType_ = $CMArmInteract$CastingBasinType$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/network/$NetworkHandler$Address" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $NetworkHandler$Address {
static readonly "EMPTY": $NetworkHandler$Address


public "hashCode"(): integer
public static "of"(arg0: $ItemStack$Type): $NetworkHandler$Address
public "getStack"(): $ItemStack
get "stack"(): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkHandler$Address$Type = ($NetworkHandler$Address);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkHandler$Address_ = $NetworkHandler$Address$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/glassed_foundry_lid/$GlassedFoundryLidBlock" {
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$IBE, $IBE$Type} from "packages/com/simibubi/create/foundation/block/$IBE"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$GlassedFoundryLidBlockEntity, $GlassedFoundryLidBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/glassed_foundry_lid/$GlassedFoundryLidBlockEntity"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IWrenchable, $IWrenchable$Type} from "packages/com/simibubi/create/content/equipment/wrench/$IWrenchable"
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"
import {$GameEventListener, $GameEventListener$Type} from "packages/net/minecraft/world/level/gameevent/$GameEventListener"

export class $GlassedFoundryLidBlock extends $Block implements $IBE<($GlassedFoundryLidBlockEntity)>, $IWrenchable {
static readonly "FACING": $DirectionProperty
static readonly "UNDER_FOUNDRY_MIXER": $BooleanProperty
static readonly "OPEN": $BooleanProperty
static readonly "POWERED": $BooleanProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getStateForPlacement"(arg0: $BlockPlaceContext$Type): $BlockState
public "getBlockEntityClass"(): $Class<($GlassedFoundryLidBlockEntity)>
public "getBlockEntityType"(): $BlockEntityType<(any)>
public "neighborChanged"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Block$Type, arg4: $BlockPos$Type, arg5: boolean): void
public "onPlace"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type, arg4: boolean): void
public "use"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type, arg4: $InteractionHand$Type, arg5: $BlockHitResult$Type): $InteractionResult
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public "getTicker"<S extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(S)>): $BlockEntityTicker<(S)>
public "getBlockEntity"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type): $GlassedFoundryLidBlockEntity
public "getBlockEntityOptional"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type): $Optional<($GlassedFoundryLidBlockEntity)>
public "onBlockEntityUse"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $Function$Type<($GlassedFoundryLidBlockEntity$Type), ($InteractionResult$Type)>): $InteractionResult
public "withBlockEntityDo"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $Consumer$Type<($GlassedFoundryLidBlockEntity$Type)>): void
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
public static "onRemove"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type): void
public "updateAfterWrenched"(arg0: $BlockState$Type, arg1: $UseOnContext$Type): $BlockState
public "getRotatedBlockState"(arg0: $BlockState$Type, arg1: $Direction$Type): $BlockState
public "playRotateSound"(arg0: $Level$Type, arg1: $BlockPos$Type): void
public "playRemoveSound"(arg0: $Level$Type, arg1: $BlockPos$Type): void
public "onSneakWrenched"(arg0: $BlockState$Type, arg1: $UseOnContext$Type): $InteractionResult
public "onWrenched"(arg0: $BlockState$Type, arg1: $UseOnContext$Type): $InteractionResult
public "getListener"<T extends $BlockEntity>(arg0: $ServerLevel$Type, arg1: T): $GameEventListener
get "blockEntityClass"(): $Class<($GlassedFoundryLidBlockEntity)>
get "blockEntityType"(): $BlockEntityType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlassedFoundryLidBlock$Type = ($GlassedFoundryLidBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlassedFoundryLidBlock_ = $GlassedFoundryLidBlock$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/data/recipes/$CMRecipeProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$RecipeProvider, $RecipeProvider$Type} from "packages/net/minecraft/data/recipes/$RecipeProvider"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"

export class $CMRecipeProvider extends $RecipeProvider {

constructor(arg0: $PackOutput$Type)

public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMRecipeProvider$Type = ($CMRecipeProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMRecipeProvider_ = $CMRecipeProvider$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/$LightBulbBlock" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$SimpleWaterloggedBlock, $SimpleWaterloggedBlock$Type} from "packages/net/minecraft/world/level/block/$SimpleWaterloggedBlock"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$LightBulbBlockEntity, $LightBulbBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/$LightBulbBlockEntity"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$IBE, $IBE$Type} from "packages/com/simibubi/create/foundation/block/$IBE"
import {$PathComputationType, $PathComputationType$Type} from "packages/net/minecraft/world/level/pathfinder/$PathComputationType"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$DyeColor, $DyeColor$Type} from "packages/net/minecraft/world/item/$DyeColor"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$WrenchableDirectionalBlock, $WrenchableDirectionalBlock$Type} from "packages/com/simibubi/create/foundation/block/$WrenchableDirectionalBlock"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"
import {$GameEventListener, $GameEventListener$Type} from "packages/net/minecraft/world/level/gameevent/$GameEventListener"

export class $LightBulbBlock extends $WrenchableDirectionalBlock implements $IBE<($LightBulbBlockEntity)>, $SimpleWaterloggedBlock {
static readonly "LEVEL": $IntegerProperty
static readonly "WATERLOGGED": $BooleanProperty
static readonly "FACING": $DirectionProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type, arg1: $DyeColor$Type)

public "getStateForPlacement"(arg0: $BlockPlaceContext$Type): $BlockState
public "getBlockEntityClass"(): $Class<($LightBulbBlockEntity)>
public "getBlockEntityType"(): $BlockEntityType<(any)>
public "getColor"(): $DyeColor
public "canConnectRedstone"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Direction$Type): boolean
public "isPathfindable"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $PathComputationType$Type): boolean
public "neighborChanged"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Block$Type, arg4: $BlockPos$Type, arg5: boolean): void
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
public "isSignalSource"(arg0: $BlockState$Type): boolean
public "getFluidState"(arg0: $BlockState$Type): $FluidState
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public "transmit"(arg0: $Level$Type, arg1: $BlockPos$Type): void
public "getTicker"<S extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(S)>): $BlockEntityTicker<(S)>
public "getBlockEntity"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type): $LightBulbBlockEntity
public "getBlockEntityOptional"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type): $Optional<($LightBulbBlockEntity)>
public "onBlockEntityUse"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $Function$Type<($LightBulbBlockEntity$Type), ($InteractionResult$Type)>): $InteractionResult
public "withBlockEntityDo"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $Consumer$Type<($LightBulbBlockEntity$Type)>): void
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
public static "onRemove"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type): void
public "getPickupSound"(): $Optional<($SoundEvent)>
public "canPlaceLiquid"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Fluid$Type): boolean
public "placeLiquid"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $FluidState$Type): boolean
public "pickupBlock"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): $ItemStack
public "getListener"<T extends $BlockEntity>(arg0: $ServerLevel$Type, arg1: T): $GameEventListener
public "getPickupSound"(arg0: $BlockState$Type): $Optional<($SoundEvent)>
get "blockEntityClass"(): $Class<($LightBulbBlockEntity)>
get "blockEntityType"(): $BlockEntityType<(any)>
get "color"(): $DyeColor
get "pickupSound"(): $Optional<($SoundEvent)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightBulbBlock$Type = ($LightBulbBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightBulbBlock_ = $LightBulbBlock$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/utils/$CastingItemRenderTypeBuffer" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$MultiBufferSource$BufferSource, $MultiBufferSource$BufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource$BufferSource"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CastingItemRenderTypeBuffer implements $MultiBufferSource {

constructor(arg0: $MultiBufferSource$Type, arg1: integer, arg2: integer)

public "getBuffer"(arg0: $RenderType$Type): $VertexConsumer
public static "immediateWithBuffers"(layerBuffers: $Map$Type<(any), (any)>, fallbackBuffer: $BufferBuilder$Type): $MultiBufferSource$BufferSource
public static "immediate"(arg0: $BufferBuilder$Type): $MultiBufferSource$BufferSource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingItemRenderTypeBuffer$Type = ($CastingItemRenderTypeBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingItemRenderTypeBuffer_ = $CastingItemRenderTypeBuffer$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/glassed_foundry_lid/$GlassedFoundryLidGenerator" {
import {$RegistrateBlockstateProvider, $RegistrateBlockstateProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateBlockstateProvider"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$DataGenContext, $DataGenContext$Type} from "packages/com/tterrag/registrate/providers/$DataGenContext"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$SpecialBlockStateGen, $SpecialBlockStateGen$Type} from "packages/com/simibubi/create/foundation/data/$SpecialBlockStateGen"
import {$ModelFile, $ModelFile$Type} from "packages/net/minecraftforge/client/model/generators/$ModelFile"

export class $GlassedFoundryLidGenerator extends $SpecialBlockStateGen {

constructor()

public "getModel"<T extends $Block>(arg0: $DataGenContext$Type<($Block$Type), (T)>, arg1: $RegistrateBlockstateProvider$Type, arg2: $BlockState$Type): $ModelFile
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlassedFoundryLidGenerator$Type = ($GlassedFoundryLidGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlassedFoundryLidGenerator_ = $GlassedFoundryLidGenerator$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/casting/$CastingWithSpout" {
import {$SpoutBlockEntity, $SpoutBlockEntity$Type} from "packages/com/simibubi/create/content/fluids/spout/$SpoutBlockEntity"
import {$FluidStack, $FluidStack$Type} from "packages/net/minecraftforge/fluids/$FluidStack"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockSpoutingBehaviour, $BlockSpoutingBehaviour$Type} from "packages/com/simibubi/create/api/behaviour/$BlockSpoutingBehaviour"

export class $CastingWithSpout extends $BlockSpoutingBehaviour {

constructor()

public "fillBlock"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $SpoutBlockEntity$Type, arg3: $FluidStack$Type, arg4: boolean): integer
public static "registerDefaults"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingWithSpout$Type = ($CastingWithSpout);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingWithSpout_ = $CastingWithSpout$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/registries/$CMSpriteShifts" {
import {$SpriteShiftEntry, $SpriteShiftEntry$Type} from "packages/com/simibubi/create/foundation/block/render/$SpriteShiftEntry"

export class $CMSpriteShifts {
static readonly "SAND_PAPER_BELT": $SpriteShiftEntry
static readonly "RED_SAND_PAPER_BELT": $SpriteShiftEntry

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMSpriteShifts$Type = ($CMSpriteShifts);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMSpriteShifts_ = $CMSpriteShifts$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/ponders/$LightBulbScenes" {
import {$SceneBuildingUtil, $SceneBuildingUtil$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuildingUtil"
import {$SceneBuilder, $SceneBuilder$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuilder"

export class $LightBulbScenes {

constructor()

public static "lightBulbScenes"(arg0: $SceneBuilder$Type, arg1: $SceneBuildingUtil$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightBulbScenes$Type = ($LightBulbScenes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightBulbScenes_ = $LightBulbScenes$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/casting/recipe/$CastingRecipeSerializer$CastingBasinRecipeSerializer" {
import {$FluidIngredient, $FluidIngredient$Type} from "packages/com/simibubi/create/foundation/fluid/$FluidIngredient"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$CastingRecipeSerializer, $CastingRecipeSerializer$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/recipe/$CastingRecipeSerializer"
import {$CastingRecipe, $CastingRecipe$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/recipe/$CastingRecipe"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$ProcessingOutput, $ProcessingOutput$Type} from "packages/com/simibubi/create/content/processing/recipe/$ProcessingOutput"

export class $CastingRecipeSerializer$CastingBasinRecipeSerializer extends $CastingRecipeSerializer {

constructor()

public "createRecipe"(arg0: $ResourceLocation$Type, arg1: $Ingredient$Type, arg2: $FluidIngredient$Type, arg3: integer, arg4: boolean, arg5: $ProcessingOutput$Type): $CastingRecipe
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingRecipeSerializer$CastingBasinRecipeSerializer$Type = ($CastingRecipeSerializer$CastingBasinRecipeSerializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingRecipeSerializer$CastingBasinRecipeSerializer_ = $CastingRecipeSerializer$CastingBasinRecipeSerializer$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/glassed_foundry_lid/$GlassedFoundryLidBlockEntity" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $GlassedFoundryLidBlockEntity extends $BlockEntity {
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockPos$Type, arg2: $BlockState$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlassedFoundryLidBlockEntity$Type = ($GlassedFoundryLidBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlassedFoundryLidBlockEntity_ = $GlassedFoundryLidBlockEntity$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/registries/$CMArmInteract$FoundryBasinType" {
import {$ArmInteractionPointType, $ArmInteractionPointType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$ArmInteractionPointType"
import {$ArmInteractionPoint, $ArmInteractionPoint$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$ArmInteractionPoint"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $CMArmInteract$FoundryBasinType extends $ArmInteractionPointType {

constructor(arg0: $ResourceLocation$Type)

public "canCreatePoint"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): boolean
public "createPoint"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): $ArmInteractionPoint
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMArmInteract$FoundryBasinType$Type = ($CMArmInteract$FoundryBasinType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMArmInteract$FoundryBasinType_ = $CMArmInteract$FoundryBasinType$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/foundry_basin/$FoundryBasinBlock" {
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$IWrenchable, $IWrenchable$Type} from "packages/com/simibubi/create/content/equipment/wrench/$IWrenchable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BasinBlock, $BasinBlock$Type} from "packages/com/simibubi/create/content/processing/basin/$BasinBlock"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $FoundryBasinBlock extends $BasinBlock implements $IWrenchable {
static readonly "FACING": $DirectionProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type)

public "updateEntityAfterFallOn"(arg0: $BlockGetter$Type, arg1: $Entity$Type): void
public "getBlockEntityType"(): $BlockEntityType<(any)>
public "onWrenched"(arg0: $BlockState$Type, arg1: $UseOnContext$Type): $InteractionResult
public static "onRemove"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type): void
get "blockEntityType"(): $BlockEntityType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoundryBasinBlock$Type = ($FoundryBasinBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoundryBasinBlock_ = $FoundryBasinBlock$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/data/recipes/$GrindingRecipeGen" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CMProcessingRecipesGen, $CMProcessingRecipesGen$Type} from "packages/fr/lucreeper74/createmetallurgy/data/recipes/$CMProcessingRecipesGen"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"

export class $GrindingRecipeGen extends $CMProcessingRecipesGen {

constructor(arg0: $PackOutput$Type)

public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GrindingRecipeGen$Type = ($GrindingRecipeGen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GrindingRecipeGen_ = $GrindingRecipeGen$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/network/$Network" {
import {$INetworkNode, $INetworkNode$Type} from "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/network/$INetworkNode"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $Network {
readonly "nodes": $Set<($INetworkNode)>

constructor(arg0: $Level$Type)

public "removeNode"(arg0: $INetworkNode$Type): void
public "addNode"(arg0: $INetworkNode$Type): void
public "transmit"(arg0: $INetworkNode$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Network$Type = ($Network);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Network_ = $Network$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/casting/table/$CastingTableMovementBehaviour" {
import {$ContraptionMatrices, $ContraptionMatrices$Type} from "packages/com/simibubi/create/content/contraptions/render/$ContraptionMatrices"
import {$MaterialManager, $MaterialManager$Type} from "packages/com/jozufozu/flywheel/api/$MaterialManager"
import {$VirtualRenderWorld, $VirtualRenderWorld$Type} from "packages/com/jozufozu/flywheel/core/virtual/$VirtualRenderWorld"
import {$ItemStackHandler, $ItemStackHandler$Type} from "packages/net/minecraftforge/items/$ItemStackHandler"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$MovementContext, $MovementContext$Type} from "packages/com/simibubi/create/content/contraptions/behaviour/$MovementContext"
import {$ActorInstance, $ActorInstance$Type} from "packages/com/simibubi/create/content/contraptions/render/$ActorInstance"
import {$MovementBehaviour, $MovementBehaviour$Type} from "packages/com/simibubi/create/content/contraptions/behaviour/$MovementBehaviour"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CastingTableMovementBehaviour implements $MovementBehaviour {

constructor()

public "tick"(arg0: $MovementContext$Type): void
public "renderAsNormalBlockEntity"(): boolean
public "getOrReadInventory"(arg0: $MovementContext$Type): $Map<(string), ($ItemStackHandler)>
public "isActive"(arg0: $MovementContext$Type): boolean
public "createInstance"(arg0: $MaterialManager$Type, arg1: $VirtualRenderWorld$Type, arg2: $MovementContext$Type): $ActorInstance
public "cancelStall"(arg0: $MovementContext$Type): void
public "startMoving"(arg0: $MovementContext$Type): void
public "canBeDisabledVia"(arg0: $MovementContext$Type): $ItemStack
public "visitNewPosition"(arg0: $MovementContext$Type, arg1: $BlockPos$Type): void
public "writeExtraData"(arg0: $MovementContext$Type): void
public "onSpeedChanged"(arg0: $MovementContext$Type, arg1: $Vec3$Type, arg2: $Vec3$Type): void
public "stopMoving"(arg0: $MovementContext$Type): void
public "renderInContraption"(arg0: $MovementContext$Type, arg1: $VirtualRenderWorld$Type, arg2: $ContraptionMatrices$Type, arg3: $MultiBufferSource$Type): void
public "mustTickWhileDisabled"(): boolean
public "getActiveAreaOffset"(arg0: $MovementContext$Type): $Vec3
public "hasSpecialInstancedRendering"(): boolean
public "onDisabledByControls"(arg0: $MovementContext$Type): void
public "dropItem"(arg0: $MovementContext$Type, arg1: $ItemStack$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingTableMovementBehaviour$Type = ($CastingTableMovementBehaviour);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingTableMovementBehaviour_ = $CastingTableMovementBehaviour$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/compat/jei/category/animations/$AnimatedFoundryMixer" {
import {$ILightingSettings, $ILightingSettings$Type} from "packages/com/simibubi/create/foundation/gui/$ILightingSettings"
import {$AnimatedKinetics, $AnimatedKinetics$Type} from "packages/com/simibubi/create/compat/jei/category/animations/$AnimatedKinetics"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $AnimatedFoundryMixer extends $AnimatedKinetics {
 "offset": integer
static readonly "DEFAULT_LIGHTING": $ILightingSettings

constructor()

public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimatedFoundryMixer$Type = ($AnimatedFoundryMixer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimatedFoundryMixer_ = $AnimatedFoundryMixer$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/registries/$CMPartialModels" {
import {$PartialModel, $PartialModel$Type} from "packages/com/jozufozu/flywheel/core/$PartialModel"
import {$DyeColor, $DyeColor$Type} from "packages/net/minecraft/world/item/$DyeColor"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CMPartialModels {
static readonly "FOUNDRY_MIXER_POLE": $PartialModel
static readonly "FOUNDRY_MIXER_HEAD": $PartialModel
static readonly "SHAFTLESS_STONE_COGWHEEL": $PartialModel
static readonly "THERMOMETER_GAUGE": $PartialModel
static readonly "GRINDER_BELT": $PartialModel
static readonly "BULB_INNER_GLOW": $PartialModel
static readonly "BULB_TUBES": $Map<($DyeColor), ($PartialModel)>
static readonly "BULB_TUBES_GLOW": $Map<($DyeColor), ($PartialModel)>

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMPartialModels$Type = ($CMPartialModels);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMPartialModels_ = $CMPartialModels$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/data/recipes/$CMProcessingRecipesGen" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$DataGenerator, $DataGenerator$Type} from "packages/net/minecraft/data/$DataGenerator"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$CMRecipeProvider, $CMRecipeProvider$Type} from "packages/fr/lucreeper74/createmetallurgy/data/recipes/$CMRecipeProvider"

export class $CMProcessingRecipesGen extends $CMRecipeProvider {

constructor(arg0: $PackOutput$Type)

public static "registerAll"(arg0: $DataGenerator$Type, arg1: $PackOutput$Type): void
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMProcessingRecipesGen$Type = ($CMProcessingRecipesGen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMProcessingRecipesGen_ = $CMProcessingRecipesGen$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/compat/jei/category/$CastingInTableCategory" {
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$CreateRecipeCategory, $CreateRecipeCategory$Type} from "packages/com/simibubi/create/compat/jei/category/$CreateRecipeCategory"
import {$CreateRecipeCategory$Info, $CreateRecipeCategory$Info$Type} from "packages/com/simibubi/create/compat/jei/category/$CreateRecipeCategory$Info"
import {$CastingTableRecipe, $CastingTableRecipe$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/recipe/$CastingTableRecipe"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export class $CastingInTableCategory extends $CreateRecipeCategory<($CastingTableRecipe)> {

constructor(arg0: $CreateRecipeCategory$Info$Type<($CastingTableRecipe$Type)>)

public "draw"(arg0: $CastingTableRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $CastingTableRecipe$Type, arg2: $IFocusGroup$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingInTableCategory$Type = ($CastingInTableCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingInTableCategory_ = $CastingInTableCategory$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/network/address/$NetworkAddressRenderer" {
import {$SmartBlockEntity, $SmartBlockEntity$Type} from "packages/com/simibubi/create/foundation/blockEntity/$SmartBlockEntity"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"

export class $NetworkAddressRenderer {

constructor()

public static "tick"(): void
public static "renderOnBlockEntity"(arg0: $SmartBlockEntity$Type, arg1: $PoseStack$Type, arg2: $MultiBufferSource$Type, arg3: integer, arg4: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkAddressRenderer$Type = ($NetworkAddressRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkAddressRenderer_ = $NetworkAddressRenderer$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/belt_grinder/$GrindingRecipe" {
import {$ProcessingRecipe, $ProcessingRecipe$Type} from "packages/com/simibubi/create/content/processing/recipe/$ProcessingRecipe"
import {$RecipeWrapper, $RecipeWrapper$Type} from "packages/net/minecraftforge/items/wrapper/$RecipeWrapper"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ProcessingRecipeBuilder$ProcessingRecipeParams, $ProcessingRecipeBuilder$ProcessingRecipeParams$Type} from "packages/com/simibubi/create/content/processing/recipe/$ProcessingRecipeBuilder$ProcessingRecipeParams"

export class $GrindingRecipe extends $ProcessingRecipe<($RecipeWrapper)> {

constructor(arg0: $ProcessingRecipeBuilder$ProcessingRecipeParams$Type)

public "matches"(arg0: $RecipeWrapper$Type, arg1: $Level$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GrindingRecipe$Type = ($GrindingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GrindingRecipe_ = $GrindingRecipe$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/foundry_mixer/$FoundryMixerRenderer" {
import {$BlockEntityRendererProvider$Context, $BlockEntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider$Context"
import {$SuperByteBufferCache$Compartment, $SuperByteBufferCache$Compartment$Type} from "packages/com/simibubi/create/foundation/render/$SuperByteBufferCache$Compartment"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$MechanicalMixerRenderer, $MechanicalMixerRenderer$Type} from "packages/com/simibubi/create/content/kinetics/mixer/$MechanicalMixerRenderer"

export class $FoundryMixerRenderer extends $MechanicalMixerRenderer {
static readonly "KINETIC_BLOCK": $SuperByteBufferCache$Compartment<($BlockState)>
static "rainbowMode": boolean

constructor(arg0: $BlockEntityRendererProvider$Context$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoundryMixerRenderer$Type = ($FoundryMixerRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoundryMixerRenderer_ = $FoundryMixerRenderer$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/casting/recipe/$CastingBasinRecipe" {
import {$FluidIngredient, $FluidIngredient$Type} from "packages/com/simibubi/create/foundation/fluid/$FluidIngredient"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$CastingRecipe, $CastingRecipe$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/recipe/$CastingRecipe"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ProcessingOutput, $ProcessingOutput$Type} from "packages/com/simibubi/create/content/processing/recipe/$ProcessingOutput"

export class $CastingBasinRecipe extends $CastingRecipe {

constructor(arg0: $ResourceLocation$Type, arg1: $Ingredient$Type, arg2: $FluidIngredient$Type, arg3: integer, arg4: boolean, arg5: $ProcessingOutput$Type)

public "getSerializer"(): $RecipeSerializer<(any)>
get "serializer"(): $RecipeSerializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingBasinRecipe$Type = ($CastingBasinRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingBasinRecipe_ = $CastingBasinRecipe$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/$LightBulbRenderer" {
import {$SafeBlockEntityRenderer, $SafeBlockEntityRenderer$Type} from "packages/com/simibubi/create/foundation/blockEntity/renderer/$SafeBlockEntityRenderer"
import {$BlockEntityRendererProvider$Context, $BlockEntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider$Context"
import {$LightBulbBlockEntity, $LightBulbBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/$LightBulbBlockEntity"

export class $LightBulbRenderer extends $SafeBlockEntityRenderer<($LightBulbBlockEntity)> {

constructor(arg0: $BlockEntityRendererProvider$Context$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightBulbRenderer$Type = ($LightBulbRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightBulbRenderer_ = $LightBulbRenderer$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/registries/$CMPonders" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CMPonders {

constructor()

public static "register"(): void
public static "registerLang"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMPonders$Type = ($CMPonders);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMPonders_ = $CMPonders$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/compat/jei/category/animations/$AnimatedBeltGrinder" {
import {$ILightingSettings, $ILightingSettings$Type} from "packages/com/simibubi/create/foundation/gui/$ILightingSettings"
import {$AnimatedKinetics, $AnimatedKinetics$Type} from "packages/com/simibubi/create/compat/jei/category/animations/$AnimatedKinetics"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $AnimatedBeltGrinder extends $AnimatedKinetics {
 "offset": integer
static readonly "DEFAULT_LIGHTING": $ILightingSettings

constructor()

public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimatedBeltGrinder$Type = ($AnimatedBeltGrinder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimatedBeltGrinder_ = $AnimatedBeltGrinder$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/casting/$CastingBlockEntity" {
import {$IFluidHandler, $IFluidHandler$Type} from "packages/net/minecraftforge/fluids/capability/$IFluidHandler"
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$SmartFluidTankBehaviour, $SmartFluidTankBehaviour$Type} from "packages/com/simibubi/create/foundation/blockEntity/behaviour/fluid/$SmartFluidTankBehaviour"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IItemHandlerModifiable, $IItemHandlerModifiable$Type} from "packages/net/minecraftforge/items/$IItemHandlerModifiable"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$SmartBlockEntity, $SmartBlockEntity$Type} from "packages/com/simibubi/create/foundation/blockEntity/$SmartBlockEntity"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$SmartInventory, $SmartInventory$Type} from "packages/com/simibubi/create/foundation/item/$SmartInventory"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $CastingBlockEntity extends $SmartBlockEntity {
 "itemCapability": $LazyOptional<($IItemHandlerModifiable)>
 "inputTank": $SmartFluidTankBehaviour
 "inv": $SmartInventory
 "moldInv": $SmartInventory
 "running": boolean
 "processingTick": integer
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockPos$Type, arg2: $BlockState$Type)

public "write"(arg0: $CompoundTag$Type, arg1: boolean): void
public "destroy"(): void
public "tick"(): void
public "process"(): void
public "readOnlyItems"(arg0: $CompoundTag$Type): void
public "getMatchingRecipes"(): $List<($Recipe<(any)>)>
public "getCapability"<T>(arg0: $Capability$Type<(T)>, arg1: $Direction$Type): $LazyOptional<(T)>
public "startProcess"(): void
public "processFailed"(): void
public static "isInAirCurrent"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockEntity$Type): boolean
public "getFluidTank"(): $IFluidHandler
get "matchingRecipes"(): $List<($Recipe<(any)>)>
get "fluidTank"(): $IFluidHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingBlockEntity$Type = ($CastingBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingBlockEntity_ = $CastingBlockEntity$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/registries/$CMCreativeTabs" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $CMCreativeTabs {
static readonly "MAIN_CREATIVE_TAB": $RegistryObject<($CreativeModeTab)>

constructor()

public static "register"(arg0: $IEventBus$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMCreativeTabs$Type = ($CMCreativeTabs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMCreativeTabs_ = $CMCreativeTabs$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/$EventsHandler" {
import {$ItemTooltipEvent, $ItemTooltipEvent$Type} from "packages/net/minecraftforge/event/entity/player/$ItemTooltipEvent"

export class $EventsHandler {

constructor()

public static "addToItemTooltip"(arg0: $ItemTooltipEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventsHandler$Type = ($EventsHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventsHandler_ = $EventsHandler$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/foundry_lid/$FoundryLidBlock" {
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$IBE, $IBE$Type} from "packages/com/simibubi/create/foundation/block/$IBE"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IWrenchable, $IWrenchable$Type} from "packages/com/simibubi/create/content/equipment/wrench/$IWrenchable"
import {$FoundryLidBlockEntity, $FoundryLidBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/foundry_lid/$FoundryLidBlockEntity"
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"
import {$GameEventListener, $GameEventListener$Type} from "packages/net/minecraft/world/level/gameevent/$GameEventListener"

export class $FoundryLidBlock extends $Block implements $IBE<($FoundryLidBlockEntity)>, $IWrenchable {
static readonly "FACING": $DirectionProperty
static readonly "ON_FOUNDRY_BASIN": $BooleanProperty
static readonly "OPEN": $BooleanProperty
static readonly "POWERED": $BooleanProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getStateForPlacement"(arg0: $BlockPlaceContext$Type): $BlockState
public "getBlockEntityClass"(): $Class<($FoundryLidBlockEntity)>
public "getBlockEntityType"(): $BlockEntityType<(any)>
public "neighborChanged"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Block$Type, arg4: $BlockPos$Type, arg5: boolean): void
public "onPlace"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type, arg4: boolean): void
public "use"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type, arg4: $InteractionHand$Type, arg5: $BlockHitResult$Type): $InteractionResult
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public "getTicker"<S extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(S)>): $BlockEntityTicker<(S)>
public "getBlockEntity"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type): $FoundryLidBlockEntity
public "getBlockEntityOptional"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type): $Optional<($FoundryLidBlockEntity)>
public "onBlockEntityUse"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $Function$Type<($FoundryLidBlockEntity$Type), ($InteractionResult$Type)>): $InteractionResult
public "withBlockEntityDo"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $Consumer$Type<($FoundryLidBlockEntity$Type)>): void
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
public static "onRemove"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type): void
public "updateAfterWrenched"(arg0: $BlockState$Type, arg1: $UseOnContext$Type): $BlockState
public "getRotatedBlockState"(arg0: $BlockState$Type, arg1: $Direction$Type): $BlockState
public "playRotateSound"(arg0: $Level$Type, arg1: $BlockPos$Type): void
public "playRemoveSound"(arg0: $Level$Type, arg1: $BlockPos$Type): void
public "onSneakWrenched"(arg0: $BlockState$Type, arg1: $UseOnContext$Type): $InteractionResult
public "onWrenched"(arg0: $BlockState$Type, arg1: $UseOnContext$Type): $InteractionResult
public "getListener"<T extends $BlockEntity>(arg0: $ServerLevel$Type, arg1: T): $GameEventListener
get "blockEntityClass"(): $Class<($FoundryLidBlockEntity)>
get "blockEntityType"(): $BlockEntityType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoundryLidBlock$Type = ($FoundryLidBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoundryLidBlock_ = $FoundryLidBlock$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/worldgen/$PlacedFeatures" {
import {$PlacedFeature, $PlacedFeature$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacedFeature"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$PlacementModifier, $PlacementModifier$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementModifier"
import {$BootstapContext, $BootstapContext$Type} from "packages/net/minecraft/data/worldgen/$BootstapContext"

export class $PlacedFeatures {
static readonly "WOLFRAMIE_ORE_PLACED_KEY": $ResourceKey<($PlacedFeature)>

constructor()

public static "rareOrePlacement"(arg0: integer, arg1: $PlacementModifier$Type): $List<($PlacementModifier)>
public static "bootstrap"(arg0: $BootstapContext$Type<($PlacedFeature$Type)>): void
public static "commonOrePlacement"(arg0: integer, arg1: $PlacementModifier$Type): $List<($PlacementModifier)>
public static "orePlacement"(arg0: $PlacementModifier$Type, arg1: $PlacementModifier$Type): $List<($PlacementModifier)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlacedFeatures$Type = ($PlacedFeatures);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlacedFeatures_ = $PlacedFeatures$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/worldgen/$BiomeModifiers" {
import {$BiomeModifier, $BiomeModifier$Type} from "packages/net/minecraftforge/common/world/$BiomeModifier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BootstapContext, $BootstapContext$Type} from "packages/net/minecraft/data/worldgen/$BootstapContext"

export class $BiomeModifiers {
static readonly "ADD_NETHER_WOLFRAMITE_ORE": $ResourceKey<($BiomeModifier)>

constructor()

public static "bootstrap"(arg0: $BootstapContext$Type<($BiomeModifier$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeModifiers$Type = ($BiomeModifiers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeModifiers_ = $BiomeModifiers$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/$CreateMetallurgyClient" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"

export class $CreateMetallurgyClient {

constructor()

public static "clientInit"(arg0: $FMLClientSetupEvent$Type): void
public static "loadClient"(arg0: $IEventBus$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreateMetallurgyClient$Type = ($CreateMetallurgyClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreateMetallurgyClient_ = $CreateMetallurgyClient$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/belt_grinder/$BeltGrinderGenerator" {
import {$RegistrateBlockstateProvider, $RegistrateBlockstateProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateBlockstateProvider"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$DataGenContext, $DataGenContext$Type} from "packages/com/tterrag/registrate/providers/$DataGenContext"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$SpecialBlockStateGen, $SpecialBlockStateGen$Type} from "packages/com/simibubi/create/foundation/data/$SpecialBlockStateGen"
import {$ModelFile, $ModelFile$Type} from "packages/net/minecraftforge/client/model/generators/$ModelFile"

export class $BeltGrinderGenerator extends $SpecialBlockStateGen {

constructor()

public "getModel"<T extends $Block>(arg0: $DataGenContext$Type<($Block$Type), (T)>, arg1: $RegistrateBlockstateProvider$Type, arg2: $BlockState$Type): $ModelFile
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BeltGrinderGenerator$Type = ($BeltGrinderGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BeltGrinderGenerator_ = $BeltGrinderGenerator$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/compat/jei/category/elements/$CastingInBasinElement" {
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $CastingInBasinElement implements $IDrawable {

constructor()

public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "getWidth"(): integer
public "getHeight"(): integer
public "draw"(arg0: $GuiGraphics$Type): void
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingInBasinElement$Type = ($CastingInBasinElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingInBasinElement_ = $CastingInBasinElement$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/belt_grinder/$BeltGrinderRenderer" {
import {$SafeBlockEntityRenderer, $SafeBlockEntityRenderer$Type} from "packages/com/simibubi/create/foundation/blockEntity/renderer/$SafeBlockEntityRenderer"
import {$BlockEntityRendererProvider$Context, $BlockEntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider$Context"
import {$BeltGrinderBlockEntity, $BeltGrinderBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/belt_grinder/$BeltGrinderBlockEntity"

export class $BeltGrinderRenderer extends $SafeBlockEntityRenderer<($BeltGrinderBlockEntity)> {

constructor(arg0: $BlockEntityRendererProvider$Context$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BeltGrinderRenderer$Type = ($BeltGrinderRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BeltGrinderRenderer_ = $BeltGrinderRenderer$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/data/$CMGenEntriesProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$RegistrySetBuilder, $RegistrySetBuilder$Type} from "packages/net/minecraft/core/$RegistrySetBuilder"
import {$DatapackBuiltinEntriesProvider, $DatapackBuiltinEntriesProvider$Type} from "packages/net/minecraftforge/common/data/$DatapackBuiltinEntriesProvider"

export class $CMGenEntriesProvider extends $DatapackBuiltinEntriesProvider {
static readonly "BUILDER": $RegistrySetBuilder

constructor(arg0: $PackOutput$Type, arg1: $CompletableFuture$Type<($HolderLookup$Provider$Type)>)

public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMGenEntriesProvider$Type = ($CMGenEntriesProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMGenEntriesProvider_ = $CMGenEntriesProvider$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/registries/$CMFluids" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$FluidEntry, $FluidEntry$Type} from "packages/com/tterrag/registrate/util/entry/$FluidEntry"
import {$ForgeFlowingFluid$Flowing, $ForgeFlowingFluid$Flowing$Type} from "packages/net/minecraftforge/fluids/$ForgeFlowingFluid$Flowing"

export class $CMFluids {
static readonly "MOLTEN_IRON_STILL_RL": $ResourceLocation
static readonly "MOLTEN_IRON_FLOW_RL": $ResourceLocation
static readonly "MOLTEN_IRON": $FluidEntry<($ForgeFlowingFluid$Flowing)>
static readonly "MOLTEN_GOLD_STILL_RL": $ResourceLocation
static readonly "MOLTEN_GOLD_FLOW_RL": $ResourceLocation
static readonly "MOLTEN_GOLD": $FluidEntry<($ForgeFlowingFluid$Flowing)>
static readonly "MOLTEN_COPPER_STILL_RL": $ResourceLocation
static readonly "MOLTEN_COPPER_FLOW_RL": $ResourceLocation
static readonly "MOLTEN_COPPER": $FluidEntry<($ForgeFlowingFluid$Flowing)>
static readonly "MOLTEN_ZINC_STILL_RL": $ResourceLocation
static readonly "MOLTEN_ZINC_FLOW_RL": $ResourceLocation
static readonly "MOLTEN_ZINC": $FluidEntry<($ForgeFlowingFluid$Flowing)>
static readonly "MOLTEN_BRASS_STILL_RL": $ResourceLocation
static readonly "MOLTEN_BRASS_FLOW_RL": $ResourceLocation
static readonly "MOLTEN_BRASS": $FluidEntry<($ForgeFlowingFluid$Flowing)>
static readonly "MOLTEN_TUNGSTEN_STILL_RL": $ResourceLocation
static readonly "MOLTEN_TUNGSTEN_FLOW_RL": $ResourceLocation
static readonly "MOLTEN_TUNGSTEN": $FluidEntry<($ForgeFlowingFluid$Flowing)>
static readonly "MOLTEN_STEEL_STILL_RL": $ResourceLocation
static readonly "MOLTEN_STEEL_FLOW_RL": $ResourceLocation
static readonly "MOLTEN_STEEL": $FluidEntry<($ForgeFlowingFluid$Flowing)>

constructor()

public static "register"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMFluids$Type = ($CMFluids);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMFluids_ = $CMFluids$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/events/$CMClientEvents" {
import {$TickEvent$ClientTickEvent, $TickEvent$ClientTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ClientTickEvent"

export class $CMClientEvents {

constructor()

public static "onTick"(arg0: $TickEvent$ClientTickEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMClientEvents$Type = ($CMClientEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMClientEvents_ = $CMClientEvents$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/$LightBulbBlockEntity" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$DyeColor, $DyeColor$Type} from "packages/net/minecraft/world/item/$DyeColor"
import {$SmartBlockEntity, $SmartBlockEntity$Type} from "packages/com/simibubi/create/foundation/blockEntity/$SmartBlockEntity"
import {$LerpedFloat, $LerpedFloat$Type} from "packages/com/simibubi/create/foundation/utility/animation/$LerpedFloat"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockEntityBehaviour, $BlockEntityBehaviour$Type} from "packages/com/simibubi/create/foundation/blockEntity/behaviour/$BlockEntityBehaviour"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $LightBulbBlockEntity extends $SmartBlockEntity {
 "glow": $LerpedFloat
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockPos$Type, arg2: $BlockState$Type)

public "initialize"(): void
public "write"(arg0: $CompoundTag$Type, arg1: boolean): void
public "tick"(): void
public "addBehavioursDeferred"(arg0: $List$Type<($BlockEntityBehaviour$Type)>): void
public "getSignal"(): integer
public "addBehaviours"(arg0: $List$Type<($BlockEntityBehaviour$Type)>): void
public "getColor"(): $DyeColor
public "setSignal"(arg0: integer): void
public "getReceivedSignal"(): integer
public "transmit"(arg0: integer): void
public "setBlockState"(arg0: $BlockState$Type): void
get "signal"(): integer
get "color"(): $DyeColor
set "signal"(value: integer)
get "receivedSignal"(): integer
set "blockState"(value: $BlockState$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightBulbBlockEntity$Type = ($LightBulbBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightBulbBlockEntity_ = $LightBulbBlockEntity$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/belt_grinder/$BeltGrinderBlock" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IBE, $IBE$Type} from "packages/com/simibubi/create/foundation/block/$IBE"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BeltGrinderBlockEntity, $BeltGrinderBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/belt_grinder/$BeltGrinderBlockEntity"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$HorizontalKineticBlock, $HorizontalKineticBlock$Type} from "packages/com/simibubi/create/content/kinetics/base/$HorizontalKineticBlock"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"
import {$GameEventListener, $GameEventListener$Type} from "packages/net/minecraft/world/level/gameevent/$GameEventListener"

export class $BeltGrinderBlock extends $HorizontalKineticBlock implements $IBE<($BeltGrinderBlockEntity)> {
static readonly "HORIZONTAL_FACING": $Property<($Direction)>
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getBlockEntityClass"(): $Class<($BeltGrinderBlockEntity)>
public "updateEntityAfterFallOn"(arg0: $BlockGetter$Type, arg1: $Entity$Type): void
public "getBlockEntityType"(): $BlockEntityType<(any)>
public "use"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type, arg4: $InteractionHand$Type, arg5: $BlockHitResult$Type): $InteractionResult
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public "entityInside"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Entity$Type): void
public "hasShaftTowards"(arg0: $LevelReader$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Direction$Type): boolean
public "getRotationAxis"(arg0: $BlockState$Type): $Direction$Axis
public "getTicker"<S extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(S)>): $BlockEntityTicker<(S)>
public "getBlockEntity"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type): $BeltGrinderBlockEntity
public "getBlockEntityOptional"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type): $Optional<($BeltGrinderBlockEntity)>
public "onBlockEntityUse"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $Function$Type<($BeltGrinderBlockEntity$Type), ($InteractionResult$Type)>): $InteractionResult
public "withBlockEntityDo"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $Consumer$Type<($BeltGrinderBlockEntity$Type)>): void
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
public static "onRemove"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type): void
public "getListener"<T extends $BlockEntity>(arg0: $ServerLevel$Type, arg1: T): $GameEventListener
get "blockEntityClass"(): $Class<($BeltGrinderBlockEntity)>
get "blockEntityType"(): $BlockEntityType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BeltGrinderBlock$Type = ($BeltGrinderBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BeltGrinderBlock_ = $BeltGrinderBlock$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/utils/$CMDyeHelper" {
import {$DyeHelper, $DyeHelper$Type} from "packages/com/simibubi/create/foundation/utility/$DyeHelper"
import {$DyeColor, $DyeColor$Type} from "packages/net/minecraft/world/item/$DyeColor"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$Couple, $Couple$Type} from "packages/com/simibubi/create/foundation/utility/$Couple"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CMDyeHelper extends $DyeHelper {
static readonly "DYE_TABLE": $Map<($DyeColor), ($Couple<(integer)>)>

constructor()

public static "getGlassOfDye"(arg0: $DyeColor$Type): $ItemLike
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMDyeHelper$Type = ($CMDyeHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMDyeHelper_ = $CMDyeHelper$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/foundry_basin/$FoundryBasinBlockEntity" {
import {$BasinInventory, $BasinInventory$Type} from "packages/com/simibubi/create/content/processing/basin/$BasinInventory"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$SmartFluidTankBehaviour, $SmartFluidTankBehaviour$Type} from "packages/com/simibubi/create/foundation/blockEntity/behaviour/fluid/$SmartFluidTankBehaviour"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BasinBlockEntity, $BasinBlockEntity$Type} from "packages/com/simibubi/create/content/processing/basin/$BasinBlockEntity"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $FoundryBasinBlockEntity extends $BasinBlockEntity {
 "inputInventory": $BasinInventory
 "inputTank": $SmartFluidTankBehaviour
static readonly "OUTPUT_ANIMATION_TIME": integer
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockPos$Type, arg2: $BlockState$Type)

public "addToGoggleTooltip"(arg0: $List$Type<($Component$Type)>, arg1: boolean): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoundryBasinBlockEntity$Type = ($FoundryBasinBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoundryBasinBlockEntity_ = $FoundryBasinBlockEntity$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/worldgen/$ConfiguredFeatures" {
import {$ConfiguredFeature, $ConfiguredFeature$Type} from "packages/net/minecraft/world/level/levelgen/feature/$ConfiguredFeature"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BootstapContext, $BootstapContext$Type} from "packages/net/minecraft/data/worldgen/$BootstapContext"

export class $ConfiguredFeatures {
static readonly "WOLFRAMIE_ORE_KEY": $ResourceKey<($ConfiguredFeature<(any), (any)>)>

constructor()

public static "bootstrap"(arg0: $BootstapContext$Type<($ConfiguredFeature$Type<(any), (any)>)>): void
public static "registerKey"(arg0: string): $ResourceKey<($ConfiguredFeature<(any), (any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfiguredFeatures$Type = ($ConfiguredFeatures);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfiguredFeatures_ = $ConfiguredFeatures$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/$LightBulbAddressSlot" {
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ValueBoxTransform, $ValueBoxTransform$Type} from "packages/com/simibubi/create/foundation/blockEntity/behaviour/$ValueBoxTransform"

export class $LightBulbAddressSlot extends $ValueBoxTransform {

constructor()

public "rotate"(arg0: $BlockState$Type, arg1: $PoseStack$Type): void
public "getLocalOffset"(arg0: $BlockState$Type): $Vec3
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightBulbAddressSlot$Type = ($LightBulbAddressSlot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightBulbAddressSlot_ = $LightBulbAddressSlot$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/utils/$ColoredFluidRenderer" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$FluidStack, $FluidStack$Type} from "packages/net/minecraftforge/fluids/$FluidStack"
import {$FluidRenderer, $FluidRenderer$Type} from "packages/com/simibubi/create/foundation/fluid/$FluidRenderer"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"

export class $ColoredFluidRenderer extends $FluidRenderer {

constructor()

public static "RGBAtoColor"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): integer
public static "renderFluidBox"(arg0: $FluidStack$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: $VertexConsumer$Type, arg8: $PoseStack$Type, arg9: integer, arg10: integer, arg11: boolean): void
public static "renderFluidBox"(arg0: $FluidStack$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: $MultiBufferSource$Type, arg8: $PoseStack$Type, arg9: integer, arg10: integer, arg11: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColoredFluidRenderer$Type = ($ColoredFluidRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColoredFluidRenderer_ = $ColoredFluidRenderer$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/casting/table/$CastingTableBlockEntity" {
import {$IFluidHandler, $IFluidHandler$Type} from "packages/net/minecraftforge/fluids/capability/$IFluidHandler"
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$SmartFluidTankBehaviour, $SmartFluidTankBehaviour$Type} from "packages/com/simibubi/create/foundation/blockEntity/behaviour/fluid/$SmartFluidTankBehaviour"
import {$IHaveGoggleInformation, $IHaveGoggleInformation$Type} from "packages/com/simibubi/create/content/equipment/goggles/$IHaveGoggleInformation"
import {$BlockEntityBehaviour, $BlockEntityBehaviour$Type} from "packages/com/simibubi/create/foundation/blockEntity/behaviour/$BlockEntityBehaviour"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$IItemHandlerModifiable, $IItemHandlerModifiable$Type} from "packages/net/minecraftforge/items/$IItemHandlerModifiable"
import {$CastingBlockEntity, $CastingBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/$CastingBlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$SmartInventory, $SmartInventory$Type} from "packages/com/simibubi/create/foundation/item/$SmartInventory"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $CastingTableBlockEntity extends $CastingBlockEntity implements $IHaveGoggleInformation {
 "itemCapability": $LazyOptional<($IItemHandlerModifiable)>
 "inputTank": $SmartFluidTankBehaviour
 "inv": $SmartInventory
 "moldInv": $SmartInventory
 "running": boolean
 "processingTick": integer
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockPos$Type, arg2: $BlockState$Type)

public "addBehaviours"(arg0: $List$Type<($BlockEntityBehaviour$Type)>): void
public "addToGoggleTooltip"(arg0: $List$Type<($Component$Type)>, arg1: boolean): boolean
public "containedFluidTooltip"(arg0: $List$Type<($Component$Type)>, arg1: boolean, arg2: $LazyOptional$Type<($IFluidHandler$Type)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingTableBlockEntity$Type = ($CastingTableBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingTableBlockEntity_ = $CastingTableBlockEntity$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/casting/basin/$CastingBasinBlock" {
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CastingBlockEntity, $CastingBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/$CastingBlockEntity"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$CastingBlock, $CastingBlock$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/$CastingBlock"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export class $CastingBasinBlock extends $CastingBlock {
static readonly "FACING": $DirectionProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getBlockEntityClass"(): $Class<($CastingBlockEntity)>
public "getBlockEntityType"(): $BlockEntityType<(any)>
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public static "onRemove"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type): void
get "blockEntityClass"(): $Class<($CastingBlockEntity)>
get "blockEntityType"(): $BlockEntityType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingBasinBlock$Type = ($CastingBasinBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingBasinBlock_ = $CastingBasinBlock$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/foundry_lid/$FoundryLidGenerator" {
import {$RegistrateBlockstateProvider, $RegistrateBlockstateProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateBlockstateProvider"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$DataGenContext, $DataGenContext$Type} from "packages/com/tterrag/registrate/providers/$DataGenContext"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$SpecialBlockStateGen, $SpecialBlockStateGen$Type} from "packages/com/simibubi/create/foundation/data/$SpecialBlockStateGen"
import {$ModelFile, $ModelFile$Type} from "packages/net/minecraftforge/client/model/generators/$ModelFile"

export class $FoundryLidGenerator extends $SpecialBlockStateGen {

constructor()

public "getModel"<T extends $Block>(arg0: $DataGenContext$Type<($Block$Type), (T)>, arg1: $RegistrateBlockstateProvider$Type, arg2: $BlockState$Type): $ModelFile
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoundryLidGenerator$Type = ($FoundryLidGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoundryLidGenerator_ = $FoundryLidGenerator$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/network/address/$NetworkAddressBehaviour" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$INetworkNode, $INetworkNode$Type} from "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/network/$INetworkNode"
import {$IntSupplier, $IntSupplier$Type} from "packages/java/util/function/$IntSupplier"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockEntityBehaviour, $BlockEntityBehaviour$Type} from "packages/com/simibubi/create/foundation/blockEntity/behaviour/$BlockEntityBehaviour"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ClipboardCloneable, $ClipboardCloneable$Type} from "packages/com/simibubi/create/content/equipment/clipboard/$ClipboardCloneable"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ValueBoxTransform, $ValueBoxTransform$Type} from "packages/com/simibubi/create/foundation/blockEntity/behaviour/$ValueBoxTransform"
import {$SmartBlockEntity, $SmartBlockEntity$Type} from "packages/com/simibubi/create/foundation/blockEntity/$SmartBlockEntity"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$IntConsumer, $IntConsumer$Type} from "packages/java/util/function/$IntConsumer"
import {$NetworkHandler$Address, $NetworkHandler$Address$Type} from "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/network/$NetworkHandler$Address"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BehaviourType, $BehaviourType$Type} from "packages/com/simibubi/create/foundation/blockEntity/behaviour/$BehaviourType"

export class $NetworkAddressBehaviour extends $BlockEntityBehaviour implements $INetworkNode, $ClipboardCloneable {
static readonly "TYPE": $BehaviourType<($NetworkAddressBehaviour)>
 "blockEntity": $SmartBlockEntity

constructor(arg0: $SmartBlockEntity$Type, arg1: $ValueBoxTransform$Type)

public "initialize"(): void
public "getLocation"(): $BlockPos
public "write"(arg0: $CompoundTag$Type, arg1: boolean): void
public "read"(arg0: $CompoundTag$Type, arg1: boolean): void
public "isAlive"(): boolean
public "getType"(): $BehaviourType<(any)>
public "getAddress"(): $NetworkHandler$Address
public "unload"(): void
public "isSafeNBT"(): boolean
public "getTransmittedSignal"(): integer
public "testHit"(arg0: $Vec3$Type): boolean
public static "networkNode"(arg0: $SmartBlockEntity$Type, arg1: $ValueBoxTransform$Type, arg2: $IntConsumer$Type, arg3: $IntSupplier$Type): $NetworkAddressBehaviour
public "writeToClipboard"(arg0: $CompoundTag$Type, arg1: $Direction$Type): boolean
public "notifySignalChange"(): void
public "setAddress"(arg0: $ItemStack$Type): void
public "setReceivedSignal"(arg0: integer): void
public "getClipboardKey"(): string
public "readFromClipboard"(arg0: $CompoundTag$Type, arg1: $Player$Type, arg2: $Direction$Type, arg3: boolean): boolean
get "location"(): $BlockPos
get "alive"(): boolean
get "type"(): $BehaviourType<(any)>
get "address"(): $NetworkHandler$Address
get "safeNBT"(): boolean
get "transmittedSignal"(): integer
set "address"(value: $ItemStack$Type)
set "receivedSignal"(value: integer)
get "clipboardKey"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkAddressBehaviour$Type = ($NetworkAddressBehaviour);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkAddressBehaviour_ = $NetworkAddressBehaviour$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/registries/$CMBlocks" {
import {$BlockEntry, $BlockEntry$Type} from "packages/com/tterrag/registrate/util/entry/$BlockEntry"
import {$BeltGrinderBlock, $BeltGrinderBlock$Type} from "packages/fr/lucreeper74/createmetallurgy/content/belt_grinder/$BeltGrinderBlock"
import {$DyedBlockList, $DyedBlockList$Type} from "packages/com/simibubi/create/foundation/block/$DyedBlockList"
import {$FoundryLidBlock, $FoundryLidBlock$Type} from "packages/fr/lucreeper74/createmetallurgy/content/foundry_lid/$FoundryLidBlock"
import {$FoundryMixerBlock, $FoundryMixerBlock$Type} from "packages/fr/lucreeper74/createmetallurgy/content/foundry_mixer/$FoundryMixerBlock"
import {$GlassedFoundryLidBlock, $GlassedFoundryLidBlock$Type} from "packages/fr/lucreeper74/createmetallurgy/content/glassed_foundry_lid/$GlassedFoundryLidBlock"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$CastingBasinBlock, $CastingBasinBlock$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/basin/$CastingBasinBlock"
import {$LightBulbBlock, $LightBulbBlock$Type} from "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/$LightBulbBlock"
import {$FoundryBasinBlock, $FoundryBasinBlock$Type} from "packages/fr/lucreeper74/createmetallurgy/content/foundry_basin/$FoundryBasinBlock"
import {$CastingTableBlock, $CastingTableBlock$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/table/$CastingTableBlock"

export class $CMBlocks {
static readonly "RAW_WOLFRAMITE_BLOCK": $BlockEntry<($Block)>
static readonly "TUNGSTEN_BLOCK": $BlockEntry<($Block)>
static readonly "WOLFRAMITE_ORE": $BlockEntry<($Block)>
static readonly "COKE_BLOCK": $BlockEntry<($Block)>
static readonly "STEEL_BLOCK": $BlockEntry<($Block)>
static readonly "REFRACTORY_MORTAR": $BlockEntry<($Block)>
static readonly "FOUNDRY_BASIN_BLOCK": $BlockEntry<($FoundryBasinBlock)>
static readonly "CASTING_BASIN_BLOCK": $BlockEntry<($CastingBasinBlock)>
static readonly "CASTING_TABLE_BLOCK": $BlockEntry<($CastingTableBlock)>
static readonly "FOUNDRY_LID_BLOCK": $BlockEntry<($FoundryLidBlock)>
static readonly "GLASSED_FOUNDRY_LID_BLOCK": $BlockEntry<($GlassedFoundryLidBlock)>
static readonly "FOUNDRY_MIXER_BLOCK": $BlockEntry<($FoundryMixerBlock)>
static readonly "BELT_GRINDER_BLOCK": $BlockEntry<($BeltGrinderBlock)>
static readonly "LIGHT_BULBS": $DyedBlockList<($LightBulbBlock)>

constructor()

public static "register"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMBlocks$Type = ($CMBlocks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMBlocks_ = $CMBlocks$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/foundry_basin/$FoundryBasinRenderer" {
import {$BlockEntityRendererProvider$Context, $BlockEntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider$Context"
import {$BasinRenderer, $BasinRenderer$Type} from "packages/com/simibubi/create/content/processing/basin/$BasinRenderer"

export class $FoundryBasinRenderer extends $BasinRenderer {

constructor(arg0: $BlockEntityRendererProvider$Context$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoundryBasinRenderer$Type = ($FoundryBasinRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoundryBasinRenderer_ = $FoundryBasinRenderer$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/registries/$CMArmInteract$CastingTableType" {
import {$ArmInteractionPointType, $ArmInteractionPointType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$ArmInteractionPointType"
import {$ArmInteractionPoint, $ArmInteractionPoint$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$ArmInteractionPoint"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $CMArmInteract$CastingTableType extends $ArmInteractionPointType {

constructor(arg0: $ResourceLocation$Type)

public "canCreatePoint"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): boolean
public "createPoint"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): $ArmInteractionPoint
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMArmInteract$CastingTableType$Type = ($CMArmInteract$CastingTableType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMArmInteract$CastingTableType_ = $CMArmInteract$CastingTableType$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/casting/$CastingBlock" {
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$Mirror, $Mirror$Type} from "packages/net/minecraft/world/level/block/$Mirror"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IBE, $IBE$Type} from "packages/com/simibubi/create/foundation/block/$IBE"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$CastingBlockEntity, $CastingBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/$CastingBlockEntity"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$IWrenchable, $IWrenchable$Type} from "packages/com/simibubi/create/content/equipment/wrench/$IWrenchable"
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"
import {$GameEventListener, $GameEventListener$Type} from "packages/net/minecraft/world/level/gameevent/$GameEventListener"

export class $CastingBlock extends $Block implements $IBE<($CastingBlockEntity)>, $IWrenchable {
static readonly "FACING": $DirectionProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getStateForPlacement"(arg0: $BlockPlaceContext$Type): $BlockState
public "use"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type, arg4: $InteractionHand$Type, arg5: $BlockHitResult$Type): $InteractionResult
public "onRemove"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type, arg4: boolean): void
public "rotate"(arg0: $BlockState$Type, arg1: $Rotation$Type): $BlockState
public "mirror"(arg0: $BlockState$Type, arg1: $Mirror$Type): $BlockState
public "getTicker"<S extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(S)>): $BlockEntityTicker<(S)>
public "getBlockEntity"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type): $CastingBlockEntity
public "getBlockEntityOptional"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type): $Optional<($CastingBlockEntity)>
public "getBlockEntityClass"(): $Class<($CastingBlockEntity)>
public "onBlockEntityUse"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $Function$Type<($CastingBlockEntity$Type), ($InteractionResult$Type)>): $InteractionResult
public "withBlockEntityDo"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $Consumer$Type<($CastingBlockEntity$Type)>): void
public "getBlockEntityType"(): $BlockEntityType<(any)>
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
public static "onRemove"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type): void
public "updateAfterWrenched"(arg0: $BlockState$Type, arg1: $UseOnContext$Type): $BlockState
public "getRotatedBlockState"(arg0: $BlockState$Type, arg1: $Direction$Type): $BlockState
public "playRotateSound"(arg0: $Level$Type, arg1: $BlockPos$Type): void
public "playRemoveSound"(arg0: $Level$Type, arg1: $BlockPos$Type): void
public "onSneakWrenched"(arg0: $BlockState$Type, arg1: $UseOnContext$Type): $InteractionResult
public "onWrenched"(arg0: $BlockState$Type, arg1: $UseOnContext$Type): $InteractionResult
public "getListener"<T extends $BlockEntity>(arg0: $ServerLevel$Type, arg1: T): $GameEventListener
get "blockEntityClass"(): $Class<($CastingBlockEntity)>
get "blockEntityType"(): $BlockEntityType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingBlock$Type = ($CastingBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingBlock_ = $CastingBlock$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/foundry_lid/$MeltingRecipe" {
import {$BasinRecipe, $BasinRecipe$Type} from "packages/com/simibubi/create/content/processing/basin/$BasinRecipe"
import {$ProcessingRecipeBuilder$ProcessingRecipeParams, $ProcessingRecipeBuilder$ProcessingRecipeParams$Type} from "packages/com/simibubi/create/content/processing/recipe/$ProcessingRecipeBuilder$ProcessingRecipeParams"

export class $MeltingRecipe extends $BasinRecipe {

constructor(arg0: $ProcessingRecipeBuilder$ProcessingRecipeParams$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MeltingRecipe$Type = ($MeltingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MeltingRecipe_ = $MeltingRecipe$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/network/$NetworkHandler" {
import {$INetworkNode, $INetworkNode$Type} from "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/network/$INetworkNode"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$NetworkHandler$Address, $NetworkHandler$Address$Type} from "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/network/$NetworkHandler$Address"
import {$Network, $Network$Type} from "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/network/$Network"

export class $NetworkHandler {

constructor()

public "getNetOf"(arg0: $Level$Type, arg1: $INetworkNode$Type): $Network
public "addNetwork"(arg0: $Level$Type, arg1: $NetworkHandler$Address$Type): $Network
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
declare module "packages/fr/lucreeper74/createmetallurgy/ponders/$CastingScenes" {
import {$SceneBuildingUtil, $SceneBuildingUtil$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuildingUtil"
import {$SceneBuilder, $SceneBuilder$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuilder"

export class $CastingScenes {

constructor()

public static "castingBlocks"(arg0: $SceneBuilder$Type, arg1: $SceneBuildingUtil$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingScenes$Type = ($CastingScenes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingScenes_ = $CastingScenes$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/casting/recipe/$CastingRecipe" {
import {$InputReplacement, $InputReplacement$Type} from "packages/dev/latvian/mods/kubejs/recipe/$InputReplacement"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$RecipeSchema, $RecipeSchema$Type} from "packages/dev/latvian/mods/kubejs/recipe/schema/$RecipeSchema"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$ProcessingOutput, $ProcessingOutput$Type} from "packages/com/simibubi/create/content/processing/recipe/$ProcessingOutput"
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$CastingBlockEntity, $CastingBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/$CastingBlockEntity"
import {$FluidIngredient, $FluidIngredient$Type} from "packages/com/simibubi/create/foundation/fluid/$FluidIngredient"
import {$SmartInventory, $SmartInventory$Type} from "packages/com/simibubi/create/foundation/item/$SmartInventory"
import {$ReplacementMatch, $ReplacementMatch$Type} from "packages/dev/latvian/mods/kubejs/recipe/$ReplacementMatch"
import {$OutputReplacement, $OutputReplacement$Type} from "packages/dev/latvian/mods/kubejs/recipe/$OutputReplacement"

export class $CastingRecipe implements $Recipe<($SmartInventory)> {

constructor(arg0: $ResourceLocation$Type, arg1: $Ingredient$Type, arg2: $FluidIngredient$Type, arg3: integer, arg4: boolean, arg5: $ProcessingOutput$Type)

public "matches"(arg0: $SmartInventory$Type, arg1: $Level$Type): boolean
public static "match"(arg0: $CastingBlockEntity$Type, arg1: $Recipe$Type<(any)>): boolean
public "getResultItem"(arg0: $RegistryAccess$Type): $ItemStack
public "getIngredient"(): $Ingredient
public "getProcessingOutput"(): $ProcessingOutput
public "canCraftInDimensions"(arg0: integer, arg1: integer): boolean
public "getSerializer"(): $RecipeSerializer<(any)>
public "assemble"(arg0: $SmartInventory$Type, arg1: $RegistryAccess$Type): $ItemStack
public "getFluidIngredient"(): $FluidIngredient
public "isMoldConsumed"(): boolean
public "getId"(): $ResourceLocation
public "getProcessingDuration"(): integer
public "getRemainingItems"(arg0: $SmartInventory$Type): $NonNullList<($ItemStack)>
public "getIngredients"(): $NonNullList<($Ingredient)>
public "getToastSymbol"(): $ItemStack
public "isIncomplete"(): boolean
public "showNotification"(): boolean
public "isSpecial"(): boolean
public "getType"(): $ResourceLocation
public "replaceOutput"(match: $ReplacementMatch$Type, arg1: $OutputReplacement$Type): boolean
public "setGroup"(group: string): void
public "hasInput"(match: $ReplacementMatch$Type): boolean
public "getOrCreateId"(): $ResourceLocation
public "getSchema"(): $RecipeSchema
public "replaceInput"(match: $ReplacementMatch$Type, arg1: $InputReplacement$Type): boolean
public "hasOutput"(match: $ReplacementMatch$Type): boolean
public "getGroup"(): string
public "getMod"(): string
get "ingredient"(): $Ingredient
get "processingOutput"(): $ProcessingOutput
get "serializer"(): $RecipeSerializer<(any)>
get "fluidIngredient"(): $FluidIngredient
get "moldConsumed"(): boolean
get "id"(): $ResourceLocation
get "processingDuration"(): integer
get "ingredients"(): $NonNullList<($Ingredient)>
get "toastSymbol"(): $ItemStack
get "incomplete"(): boolean
get "special"(): boolean
get "type"(): $ResourceLocation
set "group"(value: string)
get "orCreateId"(): $ResourceLocation
get "schema"(): $RecipeSchema
get "group"(): string
get "mod"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingRecipe$Type = ($CastingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingRecipe_ = $CastingRecipe$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/foundry_lid/$FoundryLidBlockEntity" {
import {$LerpedFloat, $LerpedFloat$Type} from "packages/com/simibubi/create/foundation/utility/animation/$LerpedFloat"
import {$BasinOperatingBlockEntity, $BasinOperatingBlockEntity$Type} from "packages/com/simibubi/create/content/processing/basin/$BasinOperatingBlockEntity"
import {$DeferralBehaviour, $DeferralBehaviour$Type} from "packages/com/simibubi/create/foundation/blockEntity/behaviour/simple/$DeferralBehaviour"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$SequencedGearshiftBlockEntity$SequenceContext, $SequencedGearshiftBlockEntity$SequenceContext$Type} from "packages/com/simibubi/create/content/kinetics/transmission/sequencer/$SequencedGearshiftBlockEntity$SequenceContext"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $FoundryLidBlockEntity extends $BasinOperatingBlockEntity {
 "processingTime": integer
 "running": boolean
 "gauge": $LerpedFloat
 "basinChecker": $DeferralBehaviour
 "basinRemoved": boolean
 "network": long
 "source": $BlockPos
 "networkDirty": boolean
 "updateSpeed": boolean
 "preventSpeedUpdate": integer
 "sequenceContext": $SequencedGearshiftBlockEntity$SequenceContext
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockPos$Type, arg2: $BlockState$Type)

public "tick"(): void
public "startProcessingBasin"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoundryLidBlockEntity$Type = ($FoundryLidBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoundryLidBlockEntity_ = $FoundryLidBlockEntity$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/compat/jei/category/$GrindingCategory" {
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$CreateRecipeCategory, $CreateRecipeCategory$Type} from "packages/com/simibubi/create/compat/jei/category/$CreateRecipeCategory"
import {$GrindingRecipe, $GrindingRecipe$Type} from "packages/fr/lucreeper74/createmetallurgy/content/belt_grinder/$GrindingRecipe"
import {$CreateRecipeCategory$Info, $CreateRecipeCategory$Info$Type} from "packages/com/simibubi/create/compat/jei/category/$CreateRecipeCategory$Info"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export class $GrindingCategory extends $CreateRecipeCategory<($GrindingRecipe)> {

constructor(arg0: $CreateRecipeCategory$Info$Type<($GrindingRecipe$Type)>)

public "draw"(arg0: $GrindingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $GrindingRecipe$Type, arg2: $IFocusGroup$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GrindingCategory$Type = ($GrindingCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GrindingCategory_ = $GrindingCategory$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/belt_grinder/$BeltGrinderBlockEntity" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$ProcessingInventory, $ProcessingInventory$Type} from "packages/com/simibubi/create/content/processing/recipe/$ProcessingInventory"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockEntityBehaviour, $BlockEntityBehaviour$Type} from "packages/com/simibubi/create/foundation/blockEntity/behaviour/$BlockEntityBehaviour"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$KineticBlockEntity, $KineticBlockEntity$Type} from "packages/com/simibubi/create/content/kinetics/base/$KineticBlockEntity"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"
import {$List, $List$Type} from "packages/java/util/$List"
import {$SequencedGearshiftBlockEntity$SequenceContext, $SequencedGearshiftBlockEntity$SequenceContext$Type} from "packages/com/simibubi/create/content/kinetics/transmission/sequencer/$SequencedGearshiftBlockEntity$SequenceContext"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"

export class $BeltGrinderBlockEntity extends $KineticBlockEntity {
 "inv": $ProcessingInventory
 "processingTick": integer
 "network": long
 "source": $BlockPos
 "networkDirty": boolean
 "updateSpeed": boolean
 "preventSpeedUpdate": integer
 "sequenceContext": $SequencedGearshiftBlockEntity$SequenceContext
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockPos$Type, arg2: $BlockState$Type)

public "write"(arg0: $CompoundTag$Type, arg1: boolean): void
public "destroy"(): void
public "tick"(): void
public "addBehaviours"(arg0: $List$Type<($BlockEntityBehaviour$Type)>): void
public "insertItem"(arg0: $ItemEntity$Type): void
public "getCapability"<T>(arg0: $Capability$Type<(T)>, arg1: $Direction$Type): $LazyOptional<(T)>
public "tickAudio"(): void
public "getItemMovementVec"(): $Vec3
get "itemMovementVec"(): $Vec3
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BeltGrinderBlockEntity$Type = ($BeltGrinderBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BeltGrinderBlockEntity_ = $BeltGrinderBlockEntity$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/casting/recipe/$CastingTableRecipe" {
import {$FluidIngredient, $FluidIngredient$Type} from "packages/com/simibubi/create/foundation/fluid/$FluidIngredient"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$CastingRecipe, $CastingRecipe$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/recipe/$CastingRecipe"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ProcessingOutput, $ProcessingOutput$Type} from "packages/com/simibubi/create/content/processing/recipe/$ProcessingOutput"

export class $CastingTableRecipe extends $CastingRecipe {

constructor(arg0: $ResourceLocation$Type, arg1: $Ingredient$Type, arg2: $FluidIngredient$Type, arg3: integer, arg4: boolean, arg5: $ProcessingOutput$Type)

public "getSerializer"(): $RecipeSerializer<(any)>
get "serializer"(): $RecipeSerializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingTableRecipe$Type = ($CastingTableRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingTableRecipe_ = $CastingTableRecipe$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/registries/$CMArmInteract" {
import {$CMArmInteract$CastingTableType, $CMArmInteract$CastingTableType$Type} from "packages/fr/lucreeper74/createmetallurgy/registries/$CMArmInteract$CastingTableType"
import {$AllArmInteractionPointTypes$BasinType, $AllArmInteractionPointTypes$BasinType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$AllArmInteractionPointTypes$BasinType"
import {$CMArmInteract$BeltGrinderType, $CMArmInteract$BeltGrinderType$Type} from "packages/fr/lucreeper74/createmetallurgy/registries/$CMArmInteract$BeltGrinderType"
import {$AllArmInteractionPointTypes$BeltType, $AllArmInteractionPointTypes$BeltType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$AllArmInteractionPointTypes$BeltType"
import {$AllArmInteractionPointTypes$MillstoneType, $AllArmInteractionPointTypes$MillstoneType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$AllArmInteractionPointTypes$MillstoneType"
import {$AllArmInteractionPointTypes$CrushingWheelsType, $AllArmInteractionPointTypes$CrushingWheelsType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$AllArmInteractionPointTypes$CrushingWheelsType"
import {$AllArmInteractionPointTypes$FunnelType, $AllArmInteractionPointTypes$FunnelType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$AllArmInteractionPointTypes$FunnelType"
import {$AllArmInteractionPointTypes$CrafterType, $AllArmInteractionPointTypes$CrafterType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$AllArmInteractionPointTypes$CrafterType"
import {$AllArmInteractionPointTypes$BlazeBurnerType, $AllArmInteractionPointTypes$BlazeBurnerType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$AllArmInteractionPointTypes$BlazeBurnerType"
import {$AllArmInteractionPointTypes$DeployerType, $AllArmInteractionPointTypes$DeployerType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$AllArmInteractionPointTypes$DeployerType"
import {$AllArmInteractionPointTypes$ComposterType, $AllArmInteractionPointTypes$ComposterType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$AllArmInteractionPointTypes$ComposterType"
import {$AllArmInteractionPointTypes, $AllArmInteractionPointTypes$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$AllArmInteractionPointTypes"
import {$CMArmInteract$CastingBasinType, $CMArmInteract$CastingBasinType$Type} from "packages/fr/lucreeper74/createmetallurgy/registries/$CMArmInteract$CastingBasinType"
import {$CMArmInteract$FoundryBasinType, $CMArmInteract$FoundryBasinType$Type} from "packages/fr/lucreeper74/createmetallurgy/registries/$CMArmInteract$FoundryBasinType"
import {$AllArmInteractionPointTypes$JukeboxType, $AllArmInteractionPointTypes$JukeboxType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$AllArmInteractionPointTypes$JukeboxType"
import {$AllArmInteractionPointTypes$SawType, $AllArmInteractionPointTypes$SawType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$AllArmInteractionPointTypes$SawType"
import {$AllArmInteractionPointTypes$CampfireType, $AllArmInteractionPointTypes$CampfireType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$AllArmInteractionPointTypes$CampfireType"
import {$AllArmInteractionPointTypes$RespawnAnchorType, $AllArmInteractionPointTypes$RespawnAnchorType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$AllArmInteractionPointTypes$RespawnAnchorType"
import {$AllArmInteractionPointTypes$ChuteType, $AllArmInteractionPointTypes$ChuteType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$AllArmInteractionPointTypes$ChuteType"
import {$AllArmInteractionPointTypes$DepotType, $AllArmInteractionPointTypes$DepotType$Type} from "packages/com/simibubi/create/content/kinetics/mechanicalArm/$AllArmInteractionPointTypes$DepotType"

export class $CMArmInteract extends $AllArmInteractionPointTypes {
static readonly "FOUNDRY_BASIN": $CMArmInteract$FoundryBasinType
static readonly "CASTING_BASIN": $CMArmInteract$CastingBasinType
static readonly "CASTING_TABLE": $CMArmInteract$CastingTableType
static readonly "BELT_GRINDER": $CMArmInteract$BeltGrinderType
static readonly "BASIN": $AllArmInteractionPointTypes$BasinType
static readonly "BELT": $AllArmInteractionPointTypes$BeltType
static readonly "BLAZE_BURNER": $AllArmInteractionPointTypes$BlazeBurnerType
static readonly "CHUTE": $AllArmInteractionPointTypes$ChuteType
static readonly "CRAFTER": $AllArmInteractionPointTypes$CrafterType
static readonly "CRUSHING_WHEELS": $AllArmInteractionPointTypes$CrushingWheelsType
static readonly "DEPLOYER": $AllArmInteractionPointTypes$DeployerType
static readonly "DEPOT": $AllArmInteractionPointTypes$DepotType
static readonly "FUNNEL": $AllArmInteractionPointTypes$FunnelType
static readonly "MILLSTONE": $AllArmInteractionPointTypes$MillstoneType
static readonly "SAW": $AllArmInteractionPointTypes$SawType
static readonly "CAMPFIRE": $AllArmInteractionPointTypes$CampfireType
static readonly "COMPOSTER": $AllArmInteractionPointTypes$ComposterType
static readonly "JUKEBOX": $AllArmInteractionPointTypes$JukeboxType
static readonly "RESPAWN_ANCHOR": $AllArmInteractionPointTypes$RespawnAnchorType

constructor()

public static "register"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMArmInteract$Type = ($CMArmInteract);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMArmInteract_ = $CMArmInteract$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/registries/$CMItems" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$CombustibleItem, $CombustibleItem$Type} from "packages/com/simibubi/create/foundation/item/$CombustibleItem"
import {$ItemEntry, $ItemEntry$Type} from "packages/com/tterrag/registrate/util/entry/$ItemEntry"

export class $CMItems {
static readonly "TUNGSTEN_INGOT": $ItemEntry<($Item)>
static readonly "TUNGSTEN_SHEET": $ItemEntry<($Item)>
static readonly "TUNGSTEN_NUGGET": $ItemEntry<($Item)>
static readonly "TUNGSTEN_WIRE": $ItemEntry<($Item)>
static readonly "RAW_WOLFRAMITE": $ItemEntry<($Item)>
static readonly "CRUSHED_RAW_WOLFRAMITE": $ItemEntry<($Item)>
static readonly "DIRTY_WOLFRAMITE_DUST": $ItemEntry<($Item)>
static readonly "WOLFRAMITE_DUST": $ItemEntry<($Item)>
static readonly "DIRTY_GOLD_DUST": $ItemEntry<($Item)>
static readonly "GOLD_DUST": $ItemEntry<($Item)>
static readonly "DIRTY_IRON_DUST": $ItemEntry<($Item)>
static readonly "IRON_DUST": $ItemEntry<($Item)>
static readonly "DIRTY_COPPER_DUST": $ItemEntry<($Item)>
static readonly "COPPER_DUST": $ItemEntry<($Item)>
static readonly "DIRTY_ZINC_DUST": $ItemEntry<($Item)>
static readonly "ZINC_DUST": $ItemEntry<($Item)>
static readonly "GRAPHITE_BLANK_MOLD": $ItemEntry<($Item)>
static readonly "GRAPHITE_INGOT_MOLD": $ItemEntry<($Item)>
static readonly "GRAPHITE_NUGGET_MOLD": $ItemEntry<($Item)>
static readonly "GRAPHITE_PLATE_MOLD": $ItemEntry<($Item)>
static readonly "COKE": $ItemEntry<($CombustibleItem)>
static readonly "GRAPHITE": $ItemEntry<($Item)>
static readonly "STEEL_INGOT": $ItemEntry<($Item)>
static readonly "STURDY_WHISK": $ItemEntry<($Item)>
static readonly "TUNGSTEN_WIRE_SPOOL": $ItemEntry<($Item)>
static readonly "SANDPAPER_BELT": $ItemEntry<($Item)>

constructor()

public static "register"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMItems$Type = ($CMItems);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMItems_ = $CMItems$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/foundry_lid/$FoundryLidRenderer" {
import {$SafeBlockEntityRenderer, $SafeBlockEntityRenderer$Type} from "packages/com/simibubi/create/foundation/blockEntity/renderer/$SafeBlockEntityRenderer"
import {$BlockEntityRendererProvider$Context, $BlockEntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider$Context"
import {$FoundryLidBlockEntity, $FoundryLidBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/foundry_lid/$FoundryLidBlockEntity"

export class $FoundryLidRenderer extends $SafeBlockEntityRenderer<($FoundryLidBlockEntity)> {

constructor(arg0: $BlockEntityRendererProvider$Context$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoundryLidRenderer$Type = ($FoundryLidRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoundryLidRenderer_ = $FoundryLidRenderer$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/casting/recipe/$CastingRecipeSerializer$CastingTableRecipeSerializer" {
import {$FluidIngredient, $FluidIngredient$Type} from "packages/com/simibubi/create/foundation/fluid/$FluidIngredient"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$CastingRecipeSerializer, $CastingRecipeSerializer$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/recipe/$CastingRecipeSerializer"
import {$CastingRecipe, $CastingRecipe$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/recipe/$CastingRecipe"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$ProcessingOutput, $ProcessingOutput$Type} from "packages/com/simibubi/create/content/processing/recipe/$ProcessingOutput"

export class $CastingRecipeSerializer$CastingTableRecipeSerializer extends $CastingRecipeSerializer {

constructor()

public "createRecipe"(arg0: $ResourceLocation$Type, arg1: $Ingredient$Type, arg2: $FluidIngredient$Type, arg3: integer, arg4: boolean, arg5: $ProcessingOutput$Type): $CastingRecipe
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingRecipeSerializer$CastingTableRecipeSerializer$Type = ($CastingRecipeSerializer$CastingTableRecipeSerializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingRecipeSerializer$CastingTableRecipeSerializer_ = $CastingRecipeSerializer$CastingTableRecipeSerializer$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/foundry_mixer/$FoundryMixerBlockEntity" {
import {$DeferralBehaviour, $DeferralBehaviour$Type} from "packages/com/simibubi/create/foundation/blockEntity/behaviour/simple/$DeferralBehaviour"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$SequencedGearshiftBlockEntity$SequenceContext, $SequencedGearshiftBlockEntity$SequenceContext$Type} from "packages/com/simibubi/create/content/kinetics/transmission/sequencer/$SequencedGearshiftBlockEntity$SequenceContext"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$MechanicalMixerBlockEntity, $MechanicalMixerBlockEntity$Type} from "packages/com/simibubi/create/content/kinetics/mixer/$MechanicalMixerBlockEntity"

export class $FoundryMixerBlockEntity extends $MechanicalMixerBlockEntity {
 "runningTicks": integer
 "processingTicks": integer
 "running": boolean
 "basinChecker": $DeferralBehaviour
 "basinRemoved": boolean
 "network": long
 "source": $BlockPos
 "networkDirty": boolean
 "updateSpeed": boolean
 "preventSpeedUpdate": integer
 "sequenceContext": $SequencedGearshiftBlockEntity$SequenceContext
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockPos$Type, arg2: $BlockState$Type)

public "tick"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoundryMixerBlockEntity$Type = ($FoundryMixerBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoundryMixerBlockEntity_ = $FoundryMixerBlockEntity$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/compat/jei/category/$PolishingWithGrinderCategory" {
import {$SandPaperPolishingRecipe, $SandPaperPolishingRecipe$Type} from "packages/com/simibubi/create/content/equipment/sandPaper/$SandPaperPolishingRecipe"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$CreateRecipeCategory, $CreateRecipeCategory$Type} from "packages/com/simibubi/create/compat/jei/category/$CreateRecipeCategory"
import {$CreateRecipeCategory$Info, $CreateRecipeCategory$Info$Type} from "packages/com/simibubi/create/compat/jei/category/$CreateRecipeCategory$Info"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export class $PolishingWithGrinderCategory extends $CreateRecipeCategory<($SandPaperPolishingRecipe)> {

constructor(arg0: $CreateRecipeCategory$Info$Type<($SandPaperPolishingRecipe$Type)>)

public "draw"(arg0: $SandPaperPolishingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $SandPaperPolishingRecipe$Type, arg2: $IFocusGroup$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolishingWithGrinderCategory$Type = ($PolishingWithGrinderCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolishingWithGrinderCategory_ = $PolishingWithGrinderCategory$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/network/$INetworkNode" {
import {$NetworkHandler$Address, $NetworkHandler$Address$Type} from "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/network/$NetworkHandler$Address"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $INetworkNode {

 "getLocation"(): $BlockPos
 "isAlive"(): boolean
 "getAddress"(): $NetworkHandler$Address
 "getTransmittedSignal"(): integer
 "setReceivedSignal"(arg0: integer): void
}

export namespace $INetworkNode {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $INetworkNode$Type = ($INetworkNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $INetworkNode_ = $INetworkNode$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/data/lang/$CMLangGen" {
import {$RegistrateLangProvider, $RegistrateLangProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateLangProvider"

export class $CMLangGen {

constructor()

public static "generate"(arg0: $RegistrateLangProvider$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMLangGen$Type = ($CMLangGen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMLangGen_ = $CMLangGen$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/registries/$CMDamageTypes" {
import {$DamageType, $DamageType$Type} from "packages/net/minecraft/world/damagesource/$DamageType"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BootstapContext, $BootstapContext$Type} from "packages/net/minecraft/data/worldgen/$BootstapContext"

export class $CMDamageTypes {
static readonly "GRINDER": $ResourceKey<($DamageType)>

constructor()

public static "bootstrap"(arg0: $BootstapContext$Type<($DamageType$Type)>): void
public static "grinder"(arg0: $Level$Type): $DamageSource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMDamageTypes$Type = ($CMDamageTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMDamageTypes_ = $CMDamageTypes$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/compat/jei/category/$CastingInBasinCategory" {
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$CastingBasinRecipe, $CastingBasinRecipe$Type} from "packages/fr/lucreeper74/createmetallurgy/content/casting/recipe/$CastingBasinRecipe"
import {$CreateRecipeCategory, $CreateRecipeCategory$Type} from "packages/com/simibubi/create/compat/jei/category/$CreateRecipeCategory"
import {$CreateRecipeCategory$Info, $CreateRecipeCategory$Info$Type} from "packages/com/simibubi/create/compat/jei/category/$CreateRecipeCategory$Info"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export class $CastingInBasinCategory extends $CreateRecipeCategory<($CastingBasinRecipe)> {

constructor(arg0: $CreateRecipeCategory$Info$Type<($CastingBasinRecipe$Type)>)

public "draw"(arg0: $CastingBasinRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $CastingBasinRecipe$Type, arg2: $IFocusGroup$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingInBasinCategory$Type = ($CastingInBasinCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingInBasinCategory_ = $CastingInBasinCategory$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/belt_grinder/$BeltGrinderInstance" {
import {$MaterialManager, $MaterialManager$Type} from "packages/com/jozufozu/flywheel/api/$MaterialManager"
import {$BeltGrinderBlockEntity, $BeltGrinderBlockEntity$Type} from "packages/fr/lucreeper74/createmetallurgy/content/belt_grinder/$BeltGrinderBlockEntity"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$SingleRotatingInstance, $SingleRotatingInstance$Type} from "packages/com/simibubi/create/content/kinetics/base/$SingleRotatingInstance"

export class $BeltGrinderInstance extends $SingleRotatingInstance<($BeltGrinderBlockEntity)> {
readonly "world": $Level

constructor(arg0: $MaterialManager$Type, arg1: $BeltGrinderBlockEntity$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BeltGrinderInstance$Type = ($BeltGrinderInstance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BeltGrinderInstance_ = $BeltGrinderInstance$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/foundry_mixer/$FoundryMixerBlock" {
import {$ICogWheel, $ICogWheel$Type} from "packages/com/simibubi/create/content/kinetics/simpleRelays/$ICogWheel"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$MechanicalMixerBlock, $MechanicalMixerBlock$Type} from "packages/com/simibubi/create/content/kinetics/mixer/$MechanicalMixerBlock"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $FoundryMixerBlock extends $MechanicalMixerBlock implements $ICogWheel {
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getBlockEntityType"(): $BlockEntityType<(any)>
public "canSurvive"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type): boolean
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public static "isSmallCogItem"(arg0: $ItemStack$Type): boolean
public static "isDedicatedCogItem"(arg0: $ItemStack$Type): boolean
public static "isLargeCogItem"(arg0: $ItemStack$Type): boolean
public static "isSmallCog"(arg0: $BlockState$Type): boolean
public static "isSmallCog"(arg0: $Block$Type): boolean
public static "isLargeCog"(arg0: $BlockState$Type): boolean
public static "isLargeCog"(arg0: $Block$Type): boolean
public static "isDedicatedCogWheel"(arg0: $Block$Type): boolean
public static "onRemove"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type): void
get "blockEntityType"(): $BlockEntityType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoundryMixerBlock$Type = ($FoundryMixerBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoundryMixerBlock_ = $FoundryMixerBlock$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/registries/$CMRecipeTypes" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IRecipeTypeInfo, $IRecipeTypeInfo$Type} from "packages/com/simibubi/create/foundation/recipe/$IRecipeTypeInfo"

export class $CMRecipeTypes extends $Enum<($CMRecipeTypes)> implements $IRecipeTypeInfo {
static readonly "MELTING": $CMRecipeTypes
static readonly "ALLOYING": $CMRecipeTypes
static readonly "GRINDING": $CMRecipeTypes
static readonly "CASTING_IN_BASIN": $CMRecipeTypes
static readonly "CASTING_IN_TABLE": $CMRecipeTypes


public static "values"(): ($CMRecipeTypes)[]
public static "valueOf"(arg0: string): $CMRecipeTypes
public static "register"(arg0: $IEventBus$Type): void
public "getId"(): $ResourceLocation
public "getType"<T extends $RecipeType<(any)>>(): T
public "getSerializer"<T extends $RecipeSerializer<(any)>>(): T
get "id"(): $ResourceLocation
get "type"(): T
get "serializer"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CMRecipeTypes$Type = (("casting_in_basin") | ("alloying") | ("casting_in_table") | ("grinding") | ("melting")) | ($CMRecipeTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CMRecipeTypes_ = $CMRecipeTypes$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/light_bulb/network/address/$NetworkAddressHandler" {
import {$PlayerInteractEvent$RightClickBlock, $PlayerInteractEvent$RightClickBlock$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$RightClickBlock"

export class $NetworkAddressHandler {

constructor()

public static "onBlockActivated"(arg0: $PlayerInteractEvent$RightClickBlock$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkAddressHandler$Type = ($NetworkAddressHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkAddressHandler_ = $NetworkAddressHandler$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/foundry_mixer/$FoundryMixerInstance" {
import {$EncasedCogInstance, $EncasedCogInstance$Type} from "packages/com/simibubi/create/content/kinetics/simpleRelays/encased/$EncasedCogInstance"
import {$DynamicInstance, $DynamicInstance$Type} from "packages/com/jozufozu/flywheel/api/instance/$DynamicInstance"
import {$MaterialManager, $MaterialManager$Type} from "packages/com/jozufozu/flywheel/api/$MaterialManager"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$MechanicalMixerBlockEntity, $MechanicalMixerBlockEntity$Type} from "packages/com/simibubi/create/content/kinetics/mixer/$MechanicalMixerBlockEntity"

export class $FoundryMixerInstance extends $EncasedCogInstance implements $DynamicInstance {
readonly "world": $Level

constructor(arg0: $MaterialManager$Type, arg1: $MechanicalMixerBlockEntity$Type)

public "remove"(): void
public "updateLight"(): void
public "beginFrame"(): void
public "decreaseFramerateWithDistance"(): boolean
public "getWorldPosition"(): $BlockPos
get "worldPosition"(): $BlockPos
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoundryMixerInstance$Type = ($FoundryMixerInstance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoundryMixerInstance_ = $FoundryMixerInstance$Type;
}}
declare module "packages/fr/lucreeper74/createmetallurgy/content/casting/basin/$CastingBasinMovementBehaviour" {
import {$ContraptionMatrices, $ContraptionMatrices$Type} from "packages/com/simibubi/create/content/contraptions/render/$ContraptionMatrices"
import {$MaterialManager, $MaterialManager$Type} from "packages/com/jozufozu/flywheel/api/$MaterialManager"
import {$VirtualRenderWorld, $VirtualRenderWorld$Type} from "packages/com/jozufozu/flywheel/core/virtual/$VirtualRenderWorld"
import {$ItemStackHandler, $ItemStackHandler$Type} from "packages/net/minecraftforge/items/$ItemStackHandler"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$MovementContext, $MovementContext$Type} from "packages/com/simibubi/create/content/contraptions/behaviour/$MovementContext"
import {$ActorInstance, $ActorInstance$Type} from "packages/com/simibubi/create/content/contraptions/render/$ActorInstance"
import {$MovementBehaviour, $MovementBehaviour$Type} from "packages/com/simibubi/create/content/contraptions/behaviour/$MovementBehaviour"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CastingBasinMovementBehaviour implements $MovementBehaviour {

constructor()

public "tick"(arg0: $MovementContext$Type): void
public "renderAsNormalBlockEntity"(): boolean
public "getOrReadInventory"(arg0: $MovementContext$Type): $Map<(string), ($ItemStackHandler)>
public "isActive"(arg0: $MovementContext$Type): boolean
public "createInstance"(arg0: $MaterialManager$Type, arg1: $VirtualRenderWorld$Type, arg2: $MovementContext$Type): $ActorInstance
public "cancelStall"(arg0: $MovementContext$Type): void
public "startMoving"(arg0: $MovementContext$Type): void
public "canBeDisabledVia"(arg0: $MovementContext$Type): $ItemStack
public "visitNewPosition"(arg0: $MovementContext$Type, arg1: $BlockPos$Type): void
public "writeExtraData"(arg0: $MovementContext$Type): void
public "onSpeedChanged"(arg0: $MovementContext$Type, arg1: $Vec3$Type, arg2: $Vec3$Type): void
public "stopMoving"(arg0: $MovementContext$Type): void
public "renderInContraption"(arg0: $MovementContext$Type, arg1: $VirtualRenderWorld$Type, arg2: $ContraptionMatrices$Type, arg3: $MultiBufferSource$Type): void
public "mustTickWhileDisabled"(): boolean
public "getActiveAreaOffset"(arg0: $MovementContext$Type): $Vec3
public "hasSpecialInstancedRendering"(): boolean
public "onDisabledByControls"(arg0: $MovementContext$Type): void
public "dropItem"(arg0: $MovementContext$Type, arg1: $ItemStack$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CastingBasinMovementBehaviour$Type = ($CastingBasinMovementBehaviour);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CastingBasinMovementBehaviour_ = $CastingBasinMovementBehaviour$Type;
}}
