import {$TargetEntityCastData, $TargetEntityCastData$Type} from "packages/io/redspace/ironsspellbooks/capabilities/magic/$TargetEntityCastData"
import {$Tesselator, $Tesselator$Type} from "packages/com/mojang/blaze3d/vertex/$Tesselator"
import {$ForgeCapabilities, $ForgeCapabilities$Type} from "packages/net/minecraftforge/common/capabilities/$ForgeCapabilities"
import {$Structures, $Structures$Type} from "packages/net/minecraft/data/worldgen/$Structures"
import {$NBTIOWrapper, $NBTIOWrapper$Type} from "packages/dev/latvian/mods/kubejs/util/$NBTIOWrapper"
import {$KMath, $KMath$Type} from "packages/dev/latvian/mods/kubejs/bindings/$KMath"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Potions, $Potions$Type} from "packages/net/minecraft/world/item/alchemy/$Potions"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$ColorWrapper, $ColorWrapper$Type} from "packages/dev/latvian/mods/rhino/mod/wrapper/$ColorWrapper"
import {$PlatformWrapper, $PlatformWrapper$Type} from "packages/dev/latvian/mods/kubejs/script/$PlatformWrapper"
import {$NBTUtils, $NBTUtils$Type} from "packages/dev/latvian/mods/rhino/mod/util/$NBTUtils"
import {$SchoolRegistry, $SchoolRegistry$Type} from "packages/io/redspace/ironsspellbooks/api/registry/$SchoolRegistry"
import {$SpellRarity, $SpellRarity$Type} from "packages/io/redspace/ironsspellbooks/api/spells/$SpellRarity"
import {$LootEntryWrapper, $LootEntryWrapper$Type} from "packages/com/almostreliable/lootjs/kube/$LootEntryWrapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$FTBQuestsKubeJSWrapper, $FTBQuestsKubeJSWrapper$Type} from "packages/dev/ftb/mods/ftbxmodcompat/ftbquests/kubejs/$FTBQuestsKubeJSWrapper"
import {$Reference, $Reference$Type} from "packages/snownee/lychee/core/$Reference"
import {$CapabilitiesForge, $CapabilitiesForge$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/$CapabilitiesForge"
import {$LootContextType, $LootContextType$Type} from "packages/com/almostreliable/lootjs/core/$LootContextType"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$NotificationBuilder, $NotificationBuilder$Type} from "packages/dev/latvian/mods/kubejs/util/$NotificationBuilder"
import {$Blocks, $Blocks$Type} from "packages/net/minecraft/world/level/block/$Blocks"
import {$BlockWrapper, $BlockWrapper$Type} from "packages/dev/latvian/mods/kubejs/bindings/$BlockWrapper"
import {$EntityJSUtils, $EntityJSUtils$Type} from "packages/net/liopyu/entityjs/util/$EntityJSUtils"
import {$OutputItem, $OutputItem$Type} from "packages/dev/latvian/mods/kubejs/item/$OutputItem"
import {$AlchemistCauldronKubeJSRecipes$AlchemistCauldronRecipeBuilder, $AlchemistCauldronKubeJSRecipes$AlchemistCauldronRecipeBuilder$Type} from "packages/com/squoshi/irons_spells_js/util/$AlchemistCauldronKubeJSRecipes$AlchemistCauldronRecipeBuilder"
import {$ForgeEventWrapper, $ForgeEventWrapper$Type} from "packages/dev/latvian/mods/kubejs/forge/$ForgeEventWrapper"
import {$CapabilitiesCurios, $CapabilitiesCurios$Type} from "packages/com/prunoideae/powerfuljs/capabilities/forge/mods/curios/$CapabilitiesCurios"
import {$Result, $Result$Type} from "packages/fr/frinn/custommachinery/common/integration/kubejs/function/$Result"
import {$ICurioRenderer, $ICurioRenderer$Type} from "packages/top/theillusivec4/curios/api/client/$ICurioRenderer"
import {$Items, $Items$Type} from "packages/net/minecraft/world/item/$Items"
import {$TextWrapper, $TextWrapper$Type} from "packages/dev/latvian/mods/kubejs/bindings/$TextWrapper"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$AnimationHolder, $AnimationHolder$Type} from "packages/io/redspace/ironsspellbooks/api/util/$AnimationHolder"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$Stats, $Stats$Type} from "packages/net/minecraft/stats/$Stats"
import {$LootContextParams, $LootContextParams$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParams"
import {$Math, $Math$Type} from "packages/java/lang/$Math"
import {$ItemFilter, $ItemFilter$Type} from "packages/com/almostreliable/lootjs/filters/$ItemFilter"
import {$Utils, $Utils$Type} from "packages/io/redspace/ironsspellbooks/api/util/$Utils"
import {$JavaWrapper, $JavaWrapper$Type} from "packages/dev/latvian/mods/kubejs/bindings/$JavaWrapper"
import {$ItemWrapper, $ItemWrapper$Type} from "packages/dev/latvian/mods/kubejs/bindings/$ItemWrapper"
import {$Collectors, $Collectors$Type} from "packages/java/util/stream/$Collectors"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"
import {$RotationAxis, $RotationAxis$Type} from "packages/dev/latvian/mods/kubejs/util/$RotationAxis"
import {$Feature, $Feature$Type} from "packages/net/minecraft/world/level/levelgen/feature/$Feature"
import {$Painter, $Painter$Type} from "packages/dev/latvian/mods/kubejs/client/painter/$Painter"
import {$CustomMachineUpgradeJSBuilder$JSRecipeModifierBuilder, $CustomMachineUpgradeJSBuilder$JSRecipeModifierBuilder$Type} from "packages/fr/frinn/custommachinery/common/integration/kubejs/$CustomMachineUpgradeJSBuilder$JSRecipeModifierBuilder"
import {$AbstractSpellWrapper, $AbstractSpellWrapper$Type} from "packages/com/squoshi/irons_spells_js/spell/$AbstractSpellWrapper"
import {$FluidAmounts, $FluidAmounts$Type} from "packages/dev/latvian/mods/kubejs/util/$FluidAmounts"
import {$UpdateClient, $UpdateClient$Type} from "packages/io/redspace/ironsspellbooks/api/util/$UpdateClient"
import {$MoreJSBinding, $MoreJSBinding$Type} from "packages/com/almostreliable/morejs/$MoreJSBinding"
import {$UtilsWrapper, $UtilsWrapper$Type} from "packages/dev/latvian/mods/kubejs/bindings/$UtilsWrapper"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$IngredientForgeHelper, $IngredientForgeHelper$Type} from "packages/dev/latvian/mods/kubejs/platform/forge/$IngredientForgeHelper"
import {$FluidWrapper, $FluidWrapper$Type} from "packages/dev/latvian/mods/kubejs/fluid/$FluidWrapper"
import {$InputItem, $InputItem$Type} from "packages/dev/latvian/mods/kubejs/item/$InputItem"
import {$CuriosCapability, $CuriosCapability$Type} from "packages/top/theillusivec4/curios/api/$CuriosCapability"
import {$EnchantmentInstance, $EnchantmentInstance$Type} from "packages/net/minecraft/world/item/enchantment/$EnchantmentInstance"
import {$SpellRegistry, $SpellRegistry$Type} from "packages/io/redspace/ironsspellbooks/api/registry/$SpellRegistry"
import {$ParticleHelper, $ParticleHelper$Type} from "packages/io/redspace/ironsspellbooks/util/$ParticleHelper"
import {$BlockStateProperties, $BlockStateProperties$Type} from "packages/net/minecraft/world/level/block/state/properties/$BlockStateProperties"
import {$ForgeItemFilter, $ForgeItemFilter$Type} from "packages/com/almostreliable/lootjs/forge/filters/$ForgeItemFilter"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LycheeLootContextParams, $LycheeLootContextParams$Type} from "packages/snownee/lychee/$LycheeLootContextParams"
import {$ItemTags, $ItemTags$Type} from "packages/net/minecraft/tags/$ItemTags"
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$PotionRegistry, $PotionRegistry$Type} from "packages/io/redspace/ironsspellbooks/registries/$PotionRegistry"
import {$TradeItem, $TradeItem$Type} from "packages/com/almostreliable/morejs/features/villager/$TradeItem"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$Vector4f, $Vector4f$Type} from "packages/org/joml/$Vector4f"
import {$UUIDWrapper, $UUIDWrapper$Type} from "packages/dev/latvian/mods/rhino/mod/wrapper/$UUIDWrapper"
import {$AABBWrapper, $AABBWrapper$Type} from "packages/dev/latvian/mods/rhino/mod/wrapper/$AABBWrapper"
import {$MachineJS, $MachineJS$Type} from "packages/fr/frinn/custommachinery/common/integration/kubejs/function/$MachineJS"
import {$RenderObjectManager, $RenderObjectManager$Type} from "packages/me/fengming/renderjs/core/$RenderObjectManager"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$VillagerUtils, $VillagerUtils$Type} from "packages/com/almostreliable/morejs/features/villager/$VillagerUtils"
import {$BlockStatePredicate, $BlockStatePredicate$Type} from "packages/dev/latvian/mods/kubejs/block/state/$BlockStatePredicate"
import {$SpellData, $SpellData$Type} from "packages/io/redspace/ironsspellbooks/api/spells/$SpellData"
import {$IngredientWrapper, $IngredientWrapper$Type} from "packages/dev/latvian/mods/kubejs/bindings/$IngredientWrapper"
import {$CastType, $CastType$Type} from "packages/io/redspace/ironsspellbooks/api/spells/$CastType"
import {$Duration, $Duration$Type} from "packages/java/time/$Duration"
import {$RenderSystem, $RenderSystem$Type} from "packages/com/mojang/blaze3d/systems/$RenderSystem"
import {$ConsoleJS, $ConsoleJS$Type} from "packages/dev/latvian/mods/kubejs/util/$ConsoleJS"
import {$IntervalJS, $IntervalJS$Type} from "packages/com/almostreliable/lootjs/kube/wrapper/$IntervalJS"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$CustomDamageSourceJS, $CustomDamageSourceJS$Type} from "packages/pie/ilikepiefoo/player/$CustomDamageSourceJS"
import {$SoundType, $SoundType$Type} from "packages/net/minecraft/world/level/block/$SoundType"
import {$IDisplayInfo$TooltipPredicate, $IDisplayInfo$TooltipPredicate$Type} from "packages/fr/frinn/custommachinery/api/integration/jei/$IDisplayInfo$TooltipPredicate"
import {$JsonIO, $JsonIO$Type} from "packages/dev/latvian/mods/kubejs/util/$JsonIO"
import {$DirectionWrapper, $DirectionWrapper$Type} from "packages/dev/latvian/mods/rhino/mod/wrapper/$DirectionWrapper"

declare global {
const CustomMachine: typeof $MachineJS
const LootType: typeof $LootContextType
const Platform: typeof $PlatformWrapper
const OutputItem: typeof $OutputItem
const RotationAxis: typeof $RotationAxis
const Feature: typeof $Feature
const ISSUpdateClient: typeof $UpdateClient
const Result: typeof $Result
const AlchemistCauldronRecipeBuilder: typeof $AlchemistCauldronKubeJSRecipes$AlchemistCauldronRecipeBuilder
const InteractionResult: typeof $InteractionResult
const ForgeModEvents: $ForgeEventWrapper
const Painter: $Painter
const Items: typeof $Items
const MINUTE: double
const BlockPos: typeof $BlockPos
const SpellRegistry: typeof $SpellRegistry
const Client: $Minecraft
const IronsSpellsParticleHelper: typeof $ParticleHelper
const SoundType: typeof $SoundType
const Collectors: typeof $Collectors
const Player: typeof $Player
const Optional: typeof $Optional
const Fluid: typeof $FluidWrapper
const SchoolRegistry: typeof $SchoolRegistry
const CuriosCapabilities: typeof $CuriosCapability
const ISSUtils: typeof $Utils
const LycheeReference: typeof $Reference
const Duration: typeof $Duration
const ISSAnimationHolder: typeof $AnimationHolder
const LootContextParams: typeof $LootContextParams
const Matrix4f: typeof $Matrix4f
const SpellRarity: typeof $SpellRarity
const LootEntry: typeof $LootEntryWrapper
const SpellData: typeof $SpellData
const KMath: typeof $KMath
const Stats: typeof $Stats
const Block: typeof $BlockWrapper
const TradeItem: typeof $TradeItem
const Interval: $IntervalJS
const JavaMath: typeof $Math
const TargetEntityCastData: typeof $TargetEntityCastData
const LycheeLootContextParams: typeof $LycheeLootContextParams
const HOUR: double
const MoreJS: typeof $MoreJSBinding
const global: $HashMap<(any), (any)>
const VillagerUtils: typeof $VillagerUtils
const CastType: typeof $CastType
const Tesselator: typeof $Tesselator
const IngredientHelper: $IngredientForgeHelper
const RenderSystem: typeof $RenderSystem
const Vec4f: typeof $Vector4f
const Notification: typeof $NotificationBuilder
const Potions: typeof $Potions
const RenderObjectManager: typeof $RenderObjectManager
const Matrix3f: typeof $Matrix3f
const CMRecipeModifierBuilder: typeof $CustomMachineUpgradeJSBuilder$JSRecipeModifierBuilder
const ResourceLocation: typeof $ResourceLocation
const Structures: typeof $Structures
const BlockProperties: typeof $BlockStateProperties
const Component: typeof $TextWrapper
const console: $ConsoleJS
const Java: $JavaWrapper
const JsonIO: typeof $JsonIO
const Vec3i: typeof $Vec3i
const Blocks: typeof $Blocks
const DamageSource: typeof $CustomDamageSourceJS
const ItemTags: typeof $ItemTags
const ForgeCapabilities: typeof $ForgeCapabilities
const FTBQuests: $FTBQuestsKubeJSWrapper
const Quaternionf: typeof $Quaternionf
const Text: typeof $TextWrapper
const Vec3f: typeof $Vector3f
const Vec3d: typeof $Vec3
const EnchantmentInstance: typeof $EnchantmentInstance
const ForgeEvents: $ForgeEventWrapper
const InputItem: typeof $InputItem
const ISSPotionRegistry: typeof $PotionRegistry
const SECOND: double
export import NBT = $NBTUtils
export import Facing = $DirectionWrapper
export import TooltipPredicate = $IDisplayInfo$TooltipPredicate
export import Color = $ColorWrapper
export import ForgeItemFilter = $ForgeItemFilter
export import NBTIO = $NBTIOWrapper
export import CapabilityBuilder = $CapabilitiesForge
export import Direction = $DirectionWrapper
export import Spell = $AbstractSpellWrapper
export import Comparator = $Comparator
export import EntityJSUtils = $EntityJSUtils
export import Item = $ItemWrapper
export import ItemFilter = $ItemFilter
export import Utils = $UtilsWrapper
export import Ingredient = $IngredientWrapper
export import CuriosRenderer = $ICurioRenderer
export import CuriosCapabilityBuilder = $CapabilitiesCurios
export import BlockStatePredicate = $BlockStatePredicate
export import UUID = $UUIDWrapper
export import AABB = $AABBWrapper
export import FluidAmounts = $FluidAmounts
}