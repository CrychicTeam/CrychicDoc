declare module "packages/portb/biggerstacks/event/$CommonForgeEvents" {
import {$RegisterCommandsEvent, $RegisterCommandsEvent$Type} from "packages/net/minecraftforge/event/$RegisterCommandsEvent"
import {$PlayerEvent$PlayerLoggedInEvent, $PlayerEvent$PlayerLoggedInEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerLoggedInEvent"

export class $CommonForgeEvents {

constructor()

public static "registerCommand"(arg0: $RegisterCommandsEvent$Type): void
public static "warnIfNoRulesetExists"(arg0: $PlayerEvent$PlayerLoggedInEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonForgeEvents$Type = ($CommonForgeEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonForgeEvents_ = $CommonForgeEvents$Type;
}}
declare module "packages/portb/biggerstacks/config/$StackSizeRules" {
import {$RuleSet, $RuleSet$Type} from "packages/portb/configlib/xml/$RuleSet"

export class $StackSizeRules {
static "maxRegisteredItemStackSize": integer

constructor()

public static "getMaxStackSize"(): integer
public static "getRuleSet"(): $RuleSet
public static "setRuleSet"(arg0: $RuleSet$Type): void
get "maxStackSize"(): integer
get "ruleSet"(): $RuleSet
set "ruleSet"(value: $RuleSet$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StackSizeRules$Type = ($StackSizeRules);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StackSizeRules_ = $StackSizeRules$Type;
}}
declare module "packages/portb/biggerstacks/util/$ItemStackSizeHelper" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$CallbackInfoReturnable, $CallbackInfoReturnable$Type} from "packages/org/spongepowered/asm/mixin/injection/callback/$CallbackInfoReturnable"

export class $ItemStackSizeHelper {

constructor()

public static "applyStackSizeToItem"(arg0: $ItemStack$Type, arg1: $CallbackInfoReturnable$Type<(integer)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemStackSizeHelper$Type = ($ItemStackSizeHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemStackSizeHelper_ = $ItemStackSizeHelper$Type;
}}
declare module "packages/portb/biggerstacks/net/$PacketHandler" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $PacketHandler {
static readonly "CHANNEL_NAME": $ResourceLocation
static readonly "INSTANCE": $SimpleChannel

constructor()

public static "register"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketHandler$Type = ($PacketHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketHandler_ = $PacketHandler$Type;
}}
declare module "packages/portb/biggerstacks/$TransformerEngine" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $TransformerEngine implements $IMixinConfigPlugin {

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
export type $TransformerEngine$Type = ($TransformerEngine);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TransformerEngine_ = $TransformerEngine$Type;
}}
declare module "packages/portb/biggerstacks/util/$ModularRoutersHelper" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ModularRoutersHelper {

constructor()

public static "getMaxStackUpgrades"(): integer
get "maxStackUpgrades"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModularRoutersHelper$Type = ($ModularRoutersHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModularRoutersHelper_ = $ModularRoutersHelper$Type;
}}
declare module "packages/portb/biggerstacks/gui/$EditBoxWithADifferentBorderColour" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$EditBox, $EditBox$Type} from "packages/net/minecraft/client/gui/components/$EditBox"

export class $EditBoxWithADifferentBorderColour extends $EditBox {
static readonly "BACKWARDS": integer
static readonly "FORWARDS": integer
static readonly "DEFAULT_TEXT_COLOR": integer
readonly "font": $Font
 "displayPos": integer
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: $Font$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $Component$Type)
constructor(arg0: $Font$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $EditBox$Type, arg6: $Component$Type)

public "renderWidget"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EditBoxWithADifferentBorderColour$Type = ($EditBoxWithADifferentBorderColour);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EditBoxWithADifferentBorderColour_ = $EditBoxWithADifferentBorderColour$Type;
}}
declare module "packages/portb/biggerstacks/net/$GenericTemplateOptionsPacket" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$TemplateOptions, $TemplateOptions$Type} from "packages/portb/configlib/template/$TemplateOptions"

export class $GenericTemplateOptionsPacket extends $TemplateOptions {

constructor(arg0: integer, arg1: integer, arg2: integer)
constructor(arg0: $FriendlyByteBuf$Type)

public "encode"(arg0: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenericTemplateOptionsPacket$Type = ($GenericTemplateOptionsPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenericTemplateOptionsPacket_ = $GenericTemplateOptionsPacket$Type;
}}
declare module "packages/portb/biggerstacks/util/$CallingClassUtil" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CallingClassUtil {

constructor()

public static "getCallerClassName"(): string
get "callerClassName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CallingClassUtil$Type = ($CallingClassUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CallingClassUtil_ = $CallingClassUtil$Type;
}}
declare module "packages/portb/biggerstacks/net/$ServerboundCreateConfigTemplatePacket" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$GenericTemplateOptionsPacket, $GenericTemplateOptionsPacket$Type} from "packages/portb/biggerstacks/net/$GenericTemplateOptionsPacket"

export class $ServerboundCreateConfigTemplatePacket extends $GenericTemplateOptionsPacket {

constructor(arg0: integer, arg1: integer, arg2: integer)
constructor(arg0: $FriendlyByteBuf$Type)

public "encode"(arg0: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerboundCreateConfigTemplatePacket$Type = ($ServerboundCreateConfigTemplatePacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerboundCreateConfigTemplatePacket_ = $ServerboundCreateConfigTemplatePacket$Type;
}}
declare module "packages/portb/biggerstacks/net/$ClientboundRulesHandshakePacket" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$RuleSet, $RuleSet$Type} from "packages/portb/configlib/xml/$RuleSet"

export class $ClientboundRulesHandshakePacket {

constructor(arg0: $FriendlyByteBuf$Type)
constructor()

public "encode"(arg0: $FriendlyByteBuf$Type): void
public "getRules"(): $RuleSet
get "rules"(): $RuleSet
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientboundRulesHandshakePacket$Type = ($ClientboundRulesHandshakePacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientboundRulesHandshakePacket_ = $ClientboundRulesHandshakePacket$Type;
}}
declare module "packages/portb/biggerstacks/event/$ServerLifecycleHandler" {
import {$ServerStoppingEvent, $ServerStoppingEvent$Type} from "packages/net/minecraftforge/event/server/$ServerStoppingEvent"

export class $ServerLifecycleHandler {

constructor()

public "serverStopping"(arg0: $ServerStoppingEvent$Type): void
public "ensureStopped"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerLifecycleHandler$Type = ($ServerLifecycleHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerLifecycleHandler_ = $ServerLifecycleHandler$Type;
}}
declare module "packages/portb/biggerstacks/event/$CommonModEvents" {
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"

export class $CommonModEvents {

constructor()

public static "serverStarting"(arg0: $FMLCommonSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonModEvents$Type = ($CommonModEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonModEvents_ = $CommonModEvents$Type;
}}
declare module "packages/portb/biggerstacks/event/$ServerEvents" {
import {$ServerAboutToStartEvent, $ServerAboutToStartEvent$Type} from "packages/net/minecraftforge/event/server/$ServerAboutToStartEvent"
import {$ServerStoppingEvent, $ServerStoppingEvent$Type} from "packages/net/minecraftforge/event/server/$ServerStoppingEvent"

export class $ServerEvents {

constructor()

public static "serverStopping"(arg0: $ServerStoppingEvent$Type): void
public static "serverStarting"(arg0: $ServerAboutToStartEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerEvents$Type = ($ServerEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerEvents_ = $ServerEvents$Type;
}}
declare module "packages/portb/biggerstacks/gui/$ConfigureScreen" {
import {$ClientboundConfigureScreenOpenPacket, $ClientboundConfigureScreenOpenPacket$Type} from "packages/portb/biggerstacks/net/$ClientboundConfigureScreenOpenPacket"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ConfigureScreen extends $Screen {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering


public static "open"(arg0: $ClientboundConfigureScreenOpenPacket$Type): void
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "renderBackground"(arg0: $GuiGraphics$Type): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "isPauseScreen"(): boolean
get "pauseScreen"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigureScreen$Type = ($ConfigureScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigureScreen_ = $ConfigureScreen$Type;
}}
declare module "packages/portb/biggerstacks/event/$ClientEvents" {
import {$ClientPlayerNetworkEvent$LoggingOut, $ClientPlayerNetworkEvent$LoggingOut$Type} from "packages/net/minecraftforge/client/event/$ClientPlayerNetworkEvent$LoggingOut"
import {$ItemTooltipEvent, $ItemTooltipEvent$Type} from "packages/net/minecraftforge/event/entity/player/$ItemTooltipEvent"

export class $ClientEvents {

constructor()

public static "showExactItemStackCount"(arg0: $ItemTooltipEvent$Type): void
public static "forgetRuleset"(arg0: $ClientPlayerNetworkEvent$LoggingOut$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientEvents$Type = ($ClientEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientEvents_ = $ClientEvents$Type;
}}
declare module "packages/portb/biggerstacks/util/$ConfigCommand" {
import {$RegisterCommandsEvent, $RegisterCommandsEvent$Type} from "packages/net/minecraftforge/event/$RegisterCommandsEvent"
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"

export class $ConfigCommand {
static readonly "LOGGER": $Logger

constructor()

public static "register"(arg0: $RegisterCommandsEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigCommand$Type = ($ConfigCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigCommand_ = $ConfigCommand$Type;
}}
declare module "packages/portb/biggerstacks/mixin/vanilla/stacksize/$ItemStackAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ItemStackAccessor {

 "accessSetCount"(arg0: integer): void

(arg0: integer): void
}

export namespace $ItemStackAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemStackAccessor$Type = ($ItemStackAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemStackAccessor_ = $ItemStackAccessor$Type;
}}
declare module "packages/portb/biggerstacks/config/$ClientConfig" {
import {$ForgeConfigSpec$BooleanValue, $ForgeConfigSpec$BooleanValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$BooleanValue"
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"

export class $ClientConfig {
static readonly "SPEC": $ForgeConfigSpec
static readonly "enableNumberShortening": $ForgeConfigSpec$BooleanValue

constructor()

public static "getNumberColour"(): $ChatFormatting
get "numberColour"(): $ChatFormatting
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
declare module "packages/portb/biggerstacks/config/$ServerConfig" {
import {$ForgeConfigSpec$BooleanValue, $ForgeConfigSpec$BooleanValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$BooleanValue"
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"

export class $ServerConfig {
static readonly "SERVER_INSTANCE": $ServerConfig
static readonly "LOCAL_INSTANCE": $ServerConfig
readonly "SPEC": $ForgeConfigSpec
readonly "increaseTransferRate": $ForgeConfigSpec$BooleanValue


public static "get"(): $ServerConfig
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerConfig$Type = ($ServerConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerConfig_ = $ServerConfig$Type;
}}
declare module "packages/portb/biggerstacks/util/$StackSizeHelper" {
import {$CallbackInfoReturnable, $CallbackInfoReturnable$Type} from "packages/org/spongepowered/asm/mixin/injection/callback/$CallbackInfoReturnable"

export class $StackSizeHelper {

constructor()

public static "getNewStackSize"(): integer
public static "increaseTransferRate"(arg0: integer): integer
public static "scaleSlotLimit"(arg0: integer): integer
public static "scaleSlotLimit"(arg0: $CallbackInfoReturnable$Type<(integer)>): void
public static "scaleTransferRate"(arg0: integer, arg1: boolean): integer
public static "scaleTransferRate"(arg0: $CallbackInfoReturnable$Type<(integer)>, arg1: boolean): void
get "newStackSize"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StackSizeHelper$Type = ($StackSizeHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StackSizeHelper_ = $StackSizeHelper$Type;
}}
declare module "packages/portb/biggerstacks/$Constants" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$DecimalFormat, $DecimalFormat$Type} from "packages/java/text/$DecimalFormat"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $Constants {
static readonly "ONE_BILLION": integer
static readonly "ONE_MILLION": integer
static readonly "ONE_THOUSAND": integer
static readonly "MOD_ID": string
static readonly "RULESET_FILE_NAME": string
static readonly "RULESET_FILE": $Path
static readonly "CONFIG_GUI_BG": $ResourceLocation
static readonly "TOOLTIP_NUMBER_FORMAT": $DecimalFormat
static readonly "CHANGE_STACK_SIZE_COMMAND_PERMISSION_LEVEL": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Constants$Type = ($Constants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Constants_ = $Constants$Type;
}}
declare module "packages/portb/biggerstacks/net/$ClientboundConfigureScreenOpenPacket" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$GenericTemplateOptionsPacket, $GenericTemplateOptionsPacket$Type} from "packages/portb/biggerstacks/net/$GenericTemplateOptionsPacket"

export class $ClientboundConfigureScreenOpenPacket extends $GenericTemplateOptionsPacket {

constructor(arg0: boolean, arg1: integer, arg2: integer, arg3: integer)
constructor(arg0: $FriendlyByteBuf$Type)

public "encode"(arg0: $FriendlyByteBuf$Type): void
public "isAlreadyUsingCustomFile"(): boolean
get "alreadyUsingCustomFile"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientboundConfigureScreenOpenPacket$Type = ($ClientboundConfigureScreenOpenPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientboundConfigureScreenOpenPacket_ = $ClientboundConfigureScreenOpenPacket$Type;
}}
declare module "packages/portb/biggerstacks/net/$ClientboundRulesUpdatePacket" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$RuleSet, $RuleSet$Type} from "packages/portb/configlib/xml/$RuleSet"

export class $ClientboundRulesUpdatePacket {

constructor(arg0: $RuleSet$Type)
constructor(arg0: $FriendlyByteBuf$Type)

public "encode"(arg0: $FriendlyByteBuf$Type): void
public "getRules"(): $RuleSet
get "rules"(): $RuleSet
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientboundRulesUpdatePacket$Type = ($ClientboundRulesUpdatePacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientboundRulesUpdatePacket_ = $ClientboundRulesUpdatePacket$Type;
}}
declare module "packages/portb/biggerstacks/$BiggerStacks" {
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"

export class $BiggerStacks {
static readonly "LOGGER": $Logger

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiggerStacks$Type = ($BiggerStacks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiggerStacks_ = $BiggerStacks$Type;
}}
