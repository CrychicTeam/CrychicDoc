import {$ItemModelPropertiesEventJS, $ItemModelPropertiesEventJS$Type} from "packages/dev/latvian/mods/kubejs/item/$ItemModelPropertiesEventJS"
import {$registerHeatEvent, $registerHeatEvent$Type} from "packages/com/xiaohunao/createheatjs/event/$registerHeatEvent"
import {$EntityCapEventJS, $EntityCapEventJS$Type} from "packages/com/prunoideae/powerfuljs/events/$EntityCapEventJS"
import {$RegisterSpawnPlacementsEventJS, $RegisterSpawnPlacementsEventJS$Type} from "packages/net/liopyu/entityjs/events/$RegisterSpawnPlacementsEventJS"
import {$DynamicItemStackEventJS, $DynamicItemStackEventJS$Type} from "packages/com/prunoideae/powerfuljs/events/forge/$DynamicItemStackEventJS"
import {$CreativeTabEvent, $CreativeTabEvent$Type} from "packages/dev/latvian/mods/kubejs/item/creativetab/$CreativeTabEvent"
import {$SpellSelectionEventJS, $SpellSelectionEventJS$Type} from "packages/com/squoshi/irons_spells_js/events/$SpellSelectionEventJS"
import {$ItemModificationEventJS, $ItemModificationEventJS$Type} from "packages/dev/latvian/mods/kubejs/item/$ItemModificationEventJS"
import {$SpecialSpoutHandlerEvent, $SpecialSpoutHandlerEvent$Type} from "packages/dev/latvian/mods/kubejs/create/events/$SpecialSpoutHandlerEvent"
import {$PotionBrewingRegisterEvent, $PotionBrewingRegisterEvent$Type} from "packages/com/almostreliable/morejs/features/potion/$PotionBrewingRegisterEvent"
import {$BlockEntityCapEventJS, $BlockEntityCapEventJS$Type} from "packages/com/prunoideae/powerfuljs/events/$BlockEntityCapEventJS"
import {$ArchEventRegisterEventJS, $ArchEventRegisterEventJS$Type} from "packages/pie/ilikepiefoo/events/custom/$ArchEventRegisterEventJS"
import {$CustomActionEventJS, $CustomActionEventJS$Type} from "packages/snownee/lychee/compat/kubejs/$CustomActionEventJS"
import {$RemoveWorldgenEventJS, $RemoveWorldgenEventJS$Type} from "packages/dev/latvian/mods/kubejs/level/gen/$RemoveWorldgenEventJS"
import {$ItemToolTierRegistryEventJS, $ItemToolTierRegistryEventJS$Type} from "packages/dev/latvian/mods/kubejs/item/custom/$ItemToolTierRegistryEventJS"
import {$ModifyAttributeEventJS, $ModifyAttributeEventJS$Type} from "packages/net/liopyu/entityjs/events/$ModifyAttributeEventJS"
import {$ClientInitEventJS, $ClientInitEventJS$Type} from "packages/dev/latvian/mods/kubejs/client/$ClientInitEventJS"
import {$BoilerHeaterHandlerEvent, $BoilerHeaterHandlerEvent$Type} from "packages/dev/latvian/mods/kubejs/create/events/$BoilerHeaterHandlerEvent"
import {$WailaCommonRegistrationEventJS, $WailaCommonRegistrationEventJS$Type} from "packages/pie/ilikepiefoo/compat/jade/$WailaCommonRegistrationEventJS"
import {$AddWorldgenEventJS, $AddWorldgenEventJS$Type} from "packages/dev/latvian/mods/kubejs/level/gen/$AddWorldgenEventJS"
import {$DynamicEntityEventJS, $DynamicEntityEventJS$Type} from "packages/com/prunoideae/powerfuljs/events/forge/$DynamicEntityEventJS"
import {$CustomConditionEventJS, $CustomConditionEventJS$Type} from "packages/snownee/lychee/compat/kubejs/$CustomConditionEventJS"
import {$RegistryEventJS, $RegistryEventJS$Type} from "packages/dev/latvian/mods/kubejs/registry/$RegistryEventJS"
import {$StartupEventJS, $StartupEventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$StartupEventJS"
import {$BlockModificationEventJS, $BlockModificationEventJS$Type} from "packages/dev/latvian/mods/kubejs/block/$BlockModificationEventJS"
import {$SpecialFluidHandlerEvent, $SpecialFluidHandlerEvent$Type} from "packages/dev/latvian/mods/kubejs/create/events/$SpecialFluidHandlerEvent"
import {$ItemArmorTierRegistryEventJS, $ItemArmorTierRegistryEventJS$Type} from "packages/dev/latvian/mods/kubejs/item/custom/$ItemArmorTierRegistryEventJS"
import {$DynamicBEEventJS, $DynamicBEEventJS$Type} from "packages/com/prunoideae/powerfuljs/events/forge/$DynamicBEEventJS"
import {$EntityModificationEventJS, $EntityModificationEventJS$Type} from "packages/net/liopyu/entityjs/events/$EntityModificationEventJS"
import {$ProxyEventJS, $ProxyEventJS$Type} from "packages/pie/ilikepiefoo/events/$ProxyEventJS"
import {$RecipeSchemaRegistryEventJS, $RecipeSchemaRegistryEventJS$Type} from "packages/dev/latvian/mods/kubejs/recipe/$RecipeSchemaRegistryEventJS"

declare global {
export namespace CreateHeatJS {
function registerHeatEvent(handler: (event: $registerHeatEvent) => void): void
}
export namespace MoreJSEvents {
function registerPotionBrewing(handler: (event: $PotionBrewingRegisterEvent) => void): void
}
export namespace ArchEvents {
function registry(handler: (event: $ArchEventRegisterEventJS) => void): void
function handleStartup(extra: string, handler: (event: $ProxyEventJS) => void): void
}
export namespace WorldgenEvents {
function add(handler: (event: $AddWorldgenEventJS) => void): void
function remove(handler: (event: $RemoveWorldgenEventJS) => void): void
}
export namespace LycheeEvents {
function customAction(extra: string, handler: (event: $CustomActionEventJS) => void): void
function customAction(handler: (event: $CustomActionEventJS) => void): void
function customCondition(extra: string, handler: (event: $CustomConditionEventJS) => void): void
function customCondition(handler: (event: $CustomConditionEventJS) => void): void
}
export namespace EntityJSEvents {
function modifyEntity(handler: (event: $EntityModificationEventJS) => void): void
function attributes(handler: (event: $ModifyAttributeEventJS) => void): void
function spawnPlacement(handler: (event: $RegisterSpawnPlacementsEventJS) => void): void
}
export namespace PlayerEvents {
function spellSelection(handler: (event: $SpellSelectionEventJS) => void): void
}
export namespace CapabilityEvents {
function dynamicItem(handler: (event: $DynamicItemStackEventJS) => void): void
function blockEntity(handler: (event: $BlockEntityCapEventJS) => void): void
function dynamicEntity(handler: (event: $DynamicEntityEventJS) => void): void
function dynamicBE(handler: (event: $DynamicBEEventJS) => void): void
function entity(handler: (event: $EntityCapEventJS) => void): void
}
export namespace JadeEvents {
function onCommonRegistration(handler: (event: $WailaCommonRegistrationEventJS) => void): void
}
export namespace ItemEvents {
function modelProperties(handler: (event: $ItemModelPropertiesEventJS) => void): void
function modification(handler: (event: $ItemModificationEventJS) => void): void
function toolTierRegistry(handler: (event: $ItemToolTierRegistryEventJS) => void): void
function armorTierRegistry(handler: (event: $ItemArmorTierRegistryEventJS) => void): void
}
export namespace CreateEvents {
function boilerHeatHandler(handler: (event: $BoilerHeaterHandlerEvent) => void): void
function pipeFluidEffect(handler: (event: $SpecialFluidHandlerEvent) => void): void
function spoutHandler(handler: (event: $SpecialSpoutHandlerEvent) => void): void
}
export namespace ClientEvents {
function init(handler: (event: $ClientInitEventJS) => void): void
}
export namespace StartupEvents {
function init(handler: (event: $StartupEventJS) => void): void
function registry(extra: string, handler: (event: $RegistryEventJS<(any)>) => void): void
function modifyCreativeTab(extra: string, handler: (event: $CreativeTabEvent) => void): void
function recipeSchemaRegistry(handler: (event: $RecipeSchemaRegistryEventJS) => void): void
function postInit(handler: (event: $StartupEventJS) => void): void
}
export namespace BlockEvents {
function modification(handler: (event: $BlockModificationEventJS) => void): void
}
}