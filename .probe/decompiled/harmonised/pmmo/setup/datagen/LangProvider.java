package harmonised.pmmo.setup.datagen;

import harmonised.pmmo.core.CoreUtils;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.common.data.LanguageProvider;

public class LangProvider extends LanguageProvider {

    private String locale;

    public static final LangProvider.Translation PERK_BREAK_SPEED = LangProvider.Translation.Builder.start("perk.pmmo.break_speed").addLocale(LangProvider.Locale.EN_US, "Break Speed Modifier").build();

    public static final LangProvider.Translation PERK_BREAK_SPEED_DESC = LangProvider.Translation.Builder.start("perk.pmmo.break_speed.description").addLocale(LangProvider.Locale.EN_US, "Increases how fast you can break blocks when using the correct tool.").build();

    public static final LangProvider.Translation PERK_BREAK_SPEED_STATUS_1 = LangProvider.Translation.Builder.start("perk.pmmo.break_speed.status1").addLocale(LangProvider.Locale.EN_US, "%s speed +%s").build();

    public static final LangProvider.Translation PERK_FIREWORK = LangProvider.Translation.Builder.start("perk.pmmo.fireworks").addLocale(LangProvider.Locale.EN_US, "Firework").build();

    public static final LangProvider.Translation PERK_FIREWORK_DESC = LangProvider.Translation.Builder.start("perk.pmmo.fireworks.description").addLocale(LangProvider.Locale.EN_US, "Spawns a firework colored like the provided skill").build();

    public static final LangProvider.Translation PERK_FIREWORK_STATUS_1 = LangProvider.Translation.Builder.start("perk.pmmo.fireworks.status1").addLocale(LangProvider.Locale.EN_US, "Rocket Color and Skill = %s").build();

    public static final LangProvider.Translation PERK_JUMP_BOOST = LangProvider.Translation.Builder.start("perk.pmmo.jump_boost").addLocale(LangProvider.Locale.EN_US, "Extra Jump Height").build();

    public static final LangProvider.Translation PERK_JUMP_BOOST_DESC = LangProvider.Translation.Builder.start("perk.pmmo.jump_boost.description").addLocale(LangProvider.Locale.EN_US, "Give extra height to your jumps").build();

    public static final LangProvider.Translation PERK_JUMP_BOOST_STATUS_1 = LangProvider.Translation.Builder.start("perk.pmmo.jump_boost.status1").addLocale(LangProvider.Locale.EN_US, "Extra Height: %s").build();

    public static final LangProvider.Translation PERK_BREATH = LangProvider.Translation.Builder.start("perk.pmmo.breath").addLocale(LangProvider.Locale.EN_US, "Breath Refresh").build();

    public static final LangProvider.Translation PERK_BREATH_DESC = LangProvider.Translation.Builder.start("perk.pmmo.breath.description").addLocale(LangProvider.Locale.EN_US, "Restores breath when not on cooldown").build();

    public static final LangProvider.Translation PERK_BREATH_STATUS_1 = LangProvider.Translation.Builder.start("perk.pmmo.breath.status1").addLocale(LangProvider.Locale.EN_US, "Breath Restored: %s").build();

    public static final LangProvider.Translation PERK_BREATH_STATUS_2 = LangProvider.Translation.Builder.start("perk.pmmo.breath.status2").addLocale(LangProvider.Locale.EN_US, "Cooldown: %ss").build();

    public static final LangProvider.Translation PERK_DAMAGE_BOOST = LangProvider.Translation.Builder.start("perk.pmmo.damage_boost").addLocale(LangProvider.Locale.EN_US, "Damage Modifier").build();

    public static final LangProvider.Translation PERK_DAMAGE_BOOST_DESC = LangProvider.Translation.Builder.start("perk.pmmo.damage_boost.description").addLocale(LangProvider.Locale.EN_US, "Increases damage dealt by the applicable items").build();

    public static final LangProvider.Translation PERK_DAMAGE_BOOST_STATUS_1 = LangProvider.Translation.Builder.start("perk.pmmo.damage_boost.status1").addLocale(LangProvider.Locale.EN_US, "Applicable to: ").build();

    public static final LangProvider.Translation PERK_DAMAGE_BOOST_STATUS_2 = LangProvider.Translation.Builder.start("perk.pmmo.damage_boost.status2").addLocale(LangProvider.Locale.EN_US, "Damage Boost: %s%s").build();

    public static final LangProvider.Translation PERK_COMMAND = LangProvider.Translation.Builder.start("perk.pmmo.command").addLocale(LangProvider.Locale.EN_US, "Custom Commands").build();

    public static final LangProvider.Translation PERK_COMMAND_DESC = LangProvider.Translation.Builder.start("perk.pmmo.command.description").addLocale(LangProvider.Locale.EN_US, "Executes a command or function").build();

    public static final LangProvider.Translation PERK_COMMAND_STATUS_1 = LangProvider.Translation.Builder.start("perk.pmmo.comand.status1").addLocale(LangProvider.Locale.EN_US, "%s = /%s").build();

    public static final LangProvider.Translation PERK_EFFECT = LangProvider.Translation.Builder.start("perk.pmmo.effect").addLocale(LangProvider.Locale.EN_US, "Status Effect").build();

    public static final LangProvider.Translation PERK_EFFECT_DESC = LangProvider.Translation.Builder.start("perk.pmmo.effect.description").addLocale(LangProvider.Locale.EN_US, "Grants the player an effect. If the player already has the effect, it pauses the cooldown").build();

    public static final LangProvider.Translation PERK_EFFECT_STATUS_1 = LangProvider.Translation.Builder.start("perk.pmmo.effect.status1").addLocale(LangProvider.Locale.EN_US, "Effect: %s").build();

    public static final LangProvider.Translation PERK_EFFECT_STATUS_2 = LangProvider.Translation.Builder.start("perk.pmmo.effect.status2").addLocale(LangProvider.Locale.EN_US, "Lvl:%s for %ss").build();

    public static final LangProvider.Translation PERK_FALL_SAVE = LangProvider.Translation.Builder.start("perk.pmmo.fall_save").addLocale(LangProvider.Locale.EN_US, "Reduce Fall Damage").build();

    public static final LangProvider.Translation PERK_FALL_SAVE_DESC = LangProvider.Translation.Builder.start("perk.pmmo.fall_save.description").addLocale(LangProvider.Locale.EN_US, "Prevents damage when not on cooldown").build();

    public static final LangProvider.Translation PERK_FALL_SAVE_STATUS_1 = LangProvider.Translation.Builder.start("perk.pmmo.fall_save.status1").addLocale(LangProvider.Locale.EN_US, "Damage Prevented: %s").build();

    public static final LangProvider.Translation PERK_TAME_BOOST = LangProvider.Translation.Builder.start("perk.pmmo.tame_boost").addLocale(LangProvider.Locale.EN_US, "Tamed Animal Boost").build();

    public static final LangProvider.Translation PERK_TAME_BOOST_DESC = LangProvider.Translation.Builder.start("perk.pmmo.tame_boost.description").addLocale(LangProvider.Locale.EN_US, "Increases an animal's attributes when it is tamed, based on your skill").build();

    public static final LangProvider.Translation PERK_TAME_BOOST_STATUS_1 = LangProvider.Translation.Builder.start("perk.pmmo.tame_boost.status1").addLocale(LangProvider.Locale.EN_US, "%s increased by %s").build();

    public static final LangProvider.Translation PERK_ATTRIBUTE = LangProvider.Translation.Builder.start("perk.pmmo.attribute").addLocale(LangProvider.Locale.EN_US, "Modified Player Attributes").build();

    public static final LangProvider.Translation PERK_TEMP_ATTRIBUTE = LangProvider.Translation.Builder.start("perk.pmmo.temp_attribute").addLocale(LangProvider.Locale.EN_US, "Temporary Player Attributes").build();

    public static final LangProvider.Translation PERK_ATTRIBUTE_DESC = LangProvider.Translation.Builder.start("perk.pmmo.attribute.description").addLocale(LangProvider.Locale.EN_US, "Alters the specified player attribute according to their skill level").build();

    public static final LangProvider.Translation PERK_ATTRIBUTE_STATUS_1 = LangProvider.Translation.Builder.start("perk.pmmo.attribute.status1").addLocale(LangProvider.Locale.EN_US, "Specified Attribute: %s").build();

    public static final LangProvider.Translation PERK_ATTRIBUTE_STATUS_2 = LangProvider.Translation.Builder.start("perk.pmmo.attribute.status2").addLocale(LangProvider.Locale.EN_US, "%s from %s").build();

    public static final LangProvider.Translation PERK_ATTRIBUTE_STATUS_3 = LangProvider.Translation.Builder.start("perk.pmmo.attribute.status3").addLocale(LangProvider.Locale.EN_US, "Current Modification: %s").build();

    public static final LangProvider.Translation PERK_VILLAGER = LangProvider.Translation.Builder.start("perk.pmmo.villager_boost").addLocale(LangProvider.Locale.EN_US, "Villager Price Reduction").build();

    public static final LangProvider.Translation PERK_VILLAGER_DESC = LangProvider.Translation.Builder.start("perk.pmmo.villager.description").addLocale(LangProvider.Locale.EN_US, "Periodically lets you reduce the price of villager trades.").build();

    public static final LangProvider.Translation PERK_VILLAGE_STATUS_1 = LangProvider.Translation.Builder.start("perk.pmmo.villager.status_1").addLocale(LangProvider.Locale.EN_US, "Reputation increased by %s with each activation.").build();

    public static final LangProvider.Translation PERK_VILLAGE_FEEDBACK = LangProvider.Translation.Builder.start("perk.pmmo.villager.notice").addLocale(LangProvider.Locale.EN_US, "You have convinced this villager to lower their prices").build();

    public static final LangProvider.Translation SKILL_HEALTH = LangProvider.Translation.Builder.start("pmmo.health").addLocale(LangProvider.Locale.EN_US, "Health").build();

    public static final LangProvider.Translation SKILL_SPEED = LangProvider.Translation.Builder.start("pmmo.speed").addLocale(LangProvider.Locale.HU, "Speed").addLocale(LangProvider.Locale.JA, "Speed").addLocale(LangProvider.Locale.PL, "Prędkość").addLocale(LangProvider.Locale.DE_DE, "Speed").addLocale(LangProvider.Locale.ES_AR, "Velocidad").addLocale(LangProvider.Locale.ES_CL, "Speed").addLocale(LangProvider.Locale.ES_EC, "Speed").addLocale(LangProvider.Locale.ES_ES, "Speed").addLocale(LangProvider.Locale.ES_MX, "Speed").addLocale(LangProvider.Locale.ES_UY, "Speed").addLocale(LangProvider.Locale.ES_VE, "Speed").addLocale(LangProvider.Locale.FR_FR, "Vitesse").addLocale(LangProvider.Locale.HU, "Speed").addLocale(LangProvider.Locale.IT_IT, "Speed").addLocale(LangProvider.Locale.JA, "Speed").addLocale(LangProvider.Locale.KO_KR, "스피드").addLocale(LangProvider.Locale.LT_LT, "Speed").addLocale(LangProvider.Locale.NL_NL, "Speed").addLocale(LangProvider.Locale.PL, "Prędkość").addLocale(LangProvider.Locale.PT_BR, "Velocidade").addLocale(LangProvider.Locale.RU_RU, "Speed").addLocale(LangProvider.Locale.SV_SE, "Speed").addLocale(LangProvider.Locale.UK_UA, "Швидкість").addLocale(LangProvider.Locale.ZH_CN, "速度").addLocale(LangProvider.Locale.ZH_TW, "速度").addLocale(LangProvider.Locale.EN_US, "Speed").build();

    public static final LangProvider.Translation SKILL_DAMAGE = LangProvider.Translation.Builder.start("pmmo.damage").addLocale(LangProvider.Locale.HU, "Damage").addLocale(LangProvider.Locale.JA, "Damage").addLocale(LangProvider.Locale.PL, "Obrażenia").addLocale(LangProvider.Locale.DE_DE, "Damage").addLocale(LangProvider.Locale.ES_AR, "Daño").addLocale(LangProvider.Locale.ES_CL, "Damage").addLocale(LangProvider.Locale.ES_EC, "Damage").addLocale(LangProvider.Locale.ES_ES, "Damage").addLocale(LangProvider.Locale.ES_MX, "Damage").addLocale(LangProvider.Locale.ES_UY, "Damage").addLocale(LangProvider.Locale.ES_VE, "Damage").addLocale(LangProvider.Locale.FR_FR, "Dommage").addLocale(LangProvider.Locale.HU, "Damage").addLocale(LangProvider.Locale.IT_IT, "Damage").addLocale(LangProvider.Locale.JA, "Damage").addLocale(LangProvider.Locale.KO_KR, "데미지").addLocale(LangProvider.Locale.LT_LT, "Damage").addLocale(LangProvider.Locale.NL_NL, "Damage").addLocale(LangProvider.Locale.PL, "Obrażenia").addLocale(LangProvider.Locale.PT_BR, "Dano").addLocale(LangProvider.Locale.RU_RU, "Damage").addLocale(LangProvider.Locale.SV_SE, "Damage").addLocale(LangProvider.Locale.UK_UA, "Пошкодження").addLocale(LangProvider.Locale.ZH_CN, "伤害").addLocale(LangProvider.Locale.ZH_TW, "傷害").addLocale(LangProvider.Locale.EN_US, "Damage").build();

    public static final LangProvider.Translation SKILL_POWER = LangProvider.Translation.Builder.start("pmmo.power").addLocale(LangProvider.Locale.HU, "Power").addLocale(LangProvider.Locale.JA, "Power").addLocale(LangProvider.Locale.PL, "Moc").addLocale(LangProvider.Locale.DE_DE, "Kraft").addLocale(LangProvider.Locale.ES_AR, "Poder").addLocale(LangProvider.Locale.ES_CL, "Poder").addLocale(LangProvider.Locale.ES_EC, "Poder").addLocale(LangProvider.Locale.ES_ES, "Poder").addLocale(LangProvider.Locale.ES_MX, "Poder").addLocale(LangProvider.Locale.ES_UY, "Poder").addLocale(LangProvider.Locale.ES_VE, "Poder").addLocale(LangProvider.Locale.FR_FR, "Puissance").addLocale(LangProvider.Locale.HU, "Power").addLocale(LangProvider.Locale.IT_IT, "Power").addLocale(LangProvider.Locale.JA, "Power").addLocale(LangProvider.Locale.KO_KR, "힘").addLocale(LangProvider.Locale.LT_LT, "Galybė").addLocale(LangProvider.Locale.NL_NL, "Kracht").addLocale(LangProvider.Locale.PL, "Moc").addLocale(LangProvider.Locale.PT_BR, "Força").addLocale(LangProvider.Locale.RU_RU, "Сила").addLocale(LangProvider.Locale.SV_SE, "Power").addLocale(LangProvider.Locale.UK_UA, "Сила").addLocale(LangProvider.Locale.ZH_CN, "力量").addLocale(LangProvider.Locale.ZH_TW, "力量").addLocale(LangProvider.Locale.EN_US, "Power").build();

    public static final LangProvider.Translation SKILL_MINING = LangProvider.Translation.Builder.start("pmmo.mining").addLocale(LangProvider.Locale.HU, "Mining").addLocale(LangProvider.Locale.JA, "Mining").addLocale(LangProvider.Locale.PL, "Górnictwo").addLocale(LangProvider.Locale.DE_DE, "Mining").addLocale(LangProvider.Locale.ES_AR, "Minería").addLocale(LangProvider.Locale.ES_CL, "Minería").addLocale(LangProvider.Locale.ES_EC, "Minería").addLocale(LangProvider.Locale.ES_ES, "Minería").addLocale(LangProvider.Locale.ES_MX, "Minería").addLocale(LangProvider.Locale.ES_UY, "Minería").addLocale(LangProvider.Locale.ES_VE, "Minería").addLocale(LangProvider.Locale.FR_FR, "Minage").addLocale(LangProvider.Locale.HU, "Mining").addLocale(LangProvider.Locale.IT_IT, "Mining").addLocale(LangProvider.Locale.JA, "Mining").addLocale(LangProvider.Locale.KO_KR, "채광").addLocale(LangProvider.Locale.LT_LT, "Kasyba").addLocale(LangProvider.Locale.NL_NL, "Ontginnen").addLocale(LangProvider.Locale.PL, "Górnictwo").addLocale(LangProvider.Locale.PT_BR, "Mineração").addLocale(LangProvider.Locale.RU_RU, "Копание").addLocale(LangProvider.Locale.SV_SE, "Mining").addLocale(LangProvider.Locale.UK_UA, "Копання").addLocale(LangProvider.Locale.ZH_CN, "采矿").addLocale(LangProvider.Locale.ZH_TW, "採礦").addLocale(LangProvider.Locale.EN_US, "Mining").build();

    public static final LangProvider.Translation SKILL_BUILDING = LangProvider.Translation.Builder.start("pmmo.building").addLocale(LangProvider.Locale.HU, "Building").addLocale(LangProvider.Locale.JA, "Building").addLocale(LangProvider.Locale.PL, "Budowanie").addLocale(LangProvider.Locale.DE_DE, "Bauen").addLocale(LangProvider.Locale.ES_AR, "Construcción").addLocale(LangProvider.Locale.ES_CL, "Construcción").addLocale(LangProvider.Locale.ES_EC, "Construcción").addLocale(LangProvider.Locale.ES_ES, "Construcción").addLocale(LangProvider.Locale.ES_MX, "Construcción").addLocale(LangProvider.Locale.ES_UY, "Construcción").addLocale(LangProvider.Locale.ES_VE, "Construcción").addLocale(LangProvider.Locale.FR_FR, "Construction").addLocale(LangProvider.Locale.HU, "Building").addLocale(LangProvider.Locale.IT_IT, "Building").addLocale(LangProvider.Locale.JA, "Building").addLocale(LangProvider.Locale.KO_KR, "건설").addLocale(LangProvider.Locale.LT_LT, "Statymas").addLocale(LangProvider.Locale.NL_NL, "Bouwen").addLocale(LangProvider.Locale.PL, "Budowanie").addLocale(LangProvider.Locale.PT_BR, "Construção").addLocale(LangProvider.Locale.RU_RU, "Строительство").addLocale(LangProvider.Locale.SV_SE, "Building").addLocale(LangProvider.Locale.UK_UA, "Будівництво").addLocale(LangProvider.Locale.ZH_CN, "建造").addLocale(LangProvider.Locale.ZH_TW, "建造").addLocale(LangProvider.Locale.EN_US, "Building").build();

    public static final LangProvider.Translation SKILL_EXCAVATION = LangProvider.Translation.Builder.start("pmmo.excavation").addLocale(LangProvider.Locale.HU, "Excavation").addLocale(LangProvider.Locale.JA, "Excavation").addLocale(LangProvider.Locale.PL, "Wydobywanie").addLocale(LangProvider.Locale.DE_DE, "Buddeln").addLocale(LangProvider.Locale.ES_AR, "Excavación").addLocale(LangProvider.Locale.ES_CL, "Excavación").addLocale(LangProvider.Locale.ES_EC, "Excavación").addLocale(LangProvider.Locale.ES_ES, "Excavación").addLocale(LangProvider.Locale.ES_MX, "Excavación").addLocale(LangProvider.Locale.ES_UY, "Excavación").addLocale(LangProvider.Locale.ES_VE, "Excavación").addLocale(LangProvider.Locale.FR_FR, "Creusage").addLocale(LangProvider.Locale.HU, "Excavation").addLocale(LangProvider.Locale.IT_IT, "Excavation").addLocale(LangProvider.Locale.JA, "Excavation").addLocale(LangProvider.Locale.KO_KR, "굴삭").addLocale(LangProvider.Locale.LT_LT, "Kasinėjimas").addLocale(LangProvider.Locale.NL_NL, "Graven").addLocale(LangProvider.Locale.PL, "Wydobywanie").addLocale(LangProvider.Locale.PT_BR, "Escavação").addLocale(LangProvider.Locale.RU_RU, "Выкапывание").addLocale(LangProvider.Locale.SV_SE, "Excavation").addLocale(LangProvider.Locale.UK_UA, "Викопування").addLocale(LangProvider.Locale.ZH_CN, "挖掘").addLocale(LangProvider.Locale.ZH_TW, "挖掘").addLocale(LangProvider.Locale.EN_US, "Excavation").build();

    public static final LangProvider.Translation SKILL_WOODCUTTING = LangProvider.Translation.Builder.start("pmmo.woodcutting").addLocale(LangProvider.Locale.HU, "Woodcutting").addLocale(LangProvider.Locale.JA, "Woodcutting").addLocale(LangProvider.Locale.PL, "Wydobywanie drewna").addLocale(LangProvider.Locale.DE_DE, "Holzschneiderei").addLocale(LangProvider.Locale.ES_AR, "Leñador").addLocale(LangProvider.Locale.ES_CL, "Leñador").addLocale(LangProvider.Locale.ES_EC, "Leñador").addLocale(LangProvider.Locale.ES_ES, "Leñador").addLocale(LangProvider.Locale.ES_MX, "Leñador").addLocale(LangProvider.Locale.ES_UY, "Leñador").addLocale(LangProvider.Locale.ES_VE, "Leñador").addLocale(LangProvider.Locale.FR_FR, "Bûchage").addLocale(LangProvider.Locale.HU, "Woodcutting").addLocale(LangProvider.Locale.IT_IT, "Woodcutting").addLocale(LangProvider.Locale.JA, "Woodcutting").addLocale(LangProvider.Locale.KO_KR, "벌목").addLocale(LangProvider.Locale.LT_LT, "Medžio kirtimas").addLocale(LangProvider.Locale.NL_NL, "Houthakken").addLocale(LangProvider.Locale.PL, "Wydobywanie drewna").addLocale(LangProvider.Locale.PT_BR, "Lenhador").addLocale(LangProvider.Locale.RU_RU, "Добыча дерева").addLocale(LangProvider.Locale.SV_SE, "Woodcutting").addLocale(LangProvider.Locale.UK_UA, "Видобуток дерева").addLocale(LangProvider.Locale.ZH_CN, "伐木").addLocale(LangProvider.Locale.ZH_TW, "伐木").addLocale(LangProvider.Locale.EN_US, "Woodcutting").build();

    public static final LangProvider.Translation SKILL_FARMING = LangProvider.Translation.Builder.start("pmmo.farming").addLocale(LangProvider.Locale.HU, "Farming").addLocale(LangProvider.Locale.JA, "Farming").addLocale(LangProvider.Locale.PL, "Rolnictwo").addLocale(LangProvider.Locale.DE_DE, "Gärtnerei").addLocale(LangProvider.Locale.ES_AR, "Agricultura").addLocale(LangProvider.Locale.ES_CL, "Agricultura").addLocale(LangProvider.Locale.ES_EC, "Agricultura").addLocale(LangProvider.Locale.ES_ES, "Agricultura").addLocale(LangProvider.Locale.ES_MX, "Agricultura").addLocale(LangProvider.Locale.ES_UY, "Agricultura").addLocale(LangProvider.Locale.ES_VE, "Agricultura").addLocale(LangProvider.Locale.FR_FR, "Agriculture").addLocale(LangProvider.Locale.HU, "Farming").addLocale(LangProvider.Locale.IT_IT, "Farming").addLocale(LangProvider.Locale.JA, "Farming").addLocale(LangProvider.Locale.KO_KR, "농사").addLocale(LangProvider.Locale.LT_LT, "Ūkininkavimas").addLocale(LangProvider.Locale.NL_NL, "Landbouw").addLocale(LangProvider.Locale.PL, "Rolnictwo").addLocale(LangProvider.Locale.PT_BR, "Fazendeiro").addLocale(LangProvider.Locale.RU_RU, "Фермерство").addLocale(LangProvider.Locale.SV_SE, "Farming").addLocale(LangProvider.Locale.UK_UA, "Фермерство").addLocale(LangProvider.Locale.ZH_CN, "种植").addLocale(LangProvider.Locale.ZH_TW, "種植").addLocale(LangProvider.Locale.EN_US, "Farming").build();

    public static final LangProvider.Translation SKILL_AGILITY = LangProvider.Translation.Builder.start("pmmo.agility").addLocale(LangProvider.Locale.HU, "Agility").addLocale(LangProvider.Locale.JA, "Agility").addLocale(LangProvider.Locale.PL, "Zwinność").addLocale(LangProvider.Locale.DE_DE, "Agilität").addLocale(LangProvider.Locale.ES_AR, "Agilidad").addLocale(LangProvider.Locale.ES_CL, "Agilidad").addLocale(LangProvider.Locale.ES_EC, "Agilidad").addLocale(LangProvider.Locale.ES_ES, "Agilidad").addLocale(LangProvider.Locale.ES_MX, "Agilidad").addLocale(LangProvider.Locale.ES_UY, "Agilidad").addLocale(LangProvider.Locale.ES_VE, "Agilidad").addLocale(LangProvider.Locale.FR_FR, "Agilité").addLocale(LangProvider.Locale.HU, "Agility").addLocale(LangProvider.Locale.IT_IT, "Agilità").addLocale(LangProvider.Locale.JA, "Agility").addLocale(LangProvider.Locale.KO_KR, "몸놀림").addLocale(LangProvider.Locale.LT_LT, "Judrumas").addLocale(LangProvider.Locale.NL_NL, "Behendigheid").addLocale(LangProvider.Locale.PL, "Zwinność").addLocale(LangProvider.Locale.PT_BR, "Agilidade").addLocale(LangProvider.Locale.RU_RU, "Ловкость").addLocale(LangProvider.Locale.SV_SE, "Agility").addLocale(LangProvider.Locale.UK_UA, "Спритність").addLocale(LangProvider.Locale.ZH_CN, "灵敏").addLocale(LangProvider.Locale.ZH_TW, "靈敏").addLocale(LangProvider.Locale.EN_US, "Agility").build();

    public static final LangProvider.Translation SKILL_ENDURANCE = LangProvider.Translation.Builder.start("pmmo.endurance").addLocale(LangProvider.Locale.HU, "Endurance").addLocale(LangProvider.Locale.JA, "Endurance").addLocale(LangProvider.Locale.PL, "Wytrzymałość").addLocale(LangProvider.Locale.DE_DE, "Ausdauer").addLocale(LangProvider.Locale.ES_AR, "Aguante").addLocale(LangProvider.Locale.ES_CL, "Resistencia").addLocale(LangProvider.Locale.ES_EC, "Resistencia").addLocale(LangProvider.Locale.ES_ES, "Resistencia").addLocale(LangProvider.Locale.ES_MX, "Resistencia").addLocale(LangProvider.Locale.ES_UY, "Aguante").addLocale(LangProvider.Locale.ES_VE, "Resistencia").addLocale(LangProvider.Locale.FR_FR, "Endurance").addLocale(LangProvider.Locale.HU, "Endurance").addLocale(LangProvider.Locale.IT_IT, "Endurance").addLocale(LangProvider.Locale.JA, "Endurance").addLocale(LangProvider.Locale.KO_KR, "강인함").addLocale(LangProvider.Locale.LT_LT, "Ištvermė").addLocale(LangProvider.Locale.NL_NL, "Weerstandsvermogen").addLocale(LangProvider.Locale.PL, "Wytrzymałość").addLocale(LangProvider.Locale.PT_BR, "Resistência").addLocale(LangProvider.Locale.RU_RU, "Выносливость").addLocale(LangProvider.Locale.SV_SE, "Endurance").addLocale(LangProvider.Locale.UK_UA, "Витривалість").addLocale(LangProvider.Locale.ZH_CN, "耐力").addLocale(LangProvider.Locale.ZH_TW, "耐力").addLocale(LangProvider.Locale.EN_US, "Endurance").build();

    public static final LangProvider.Translation SKILL_COMBAT = LangProvider.Translation.Builder.start("pmmo.combat").addLocale(LangProvider.Locale.HU, "Combat").addLocale(LangProvider.Locale.JA, "Combat").addLocale(LangProvider.Locale.PL, "Walka").addLocale(LangProvider.Locale.DE_DE, "Kampf").addLocale(LangProvider.Locale.ES_AR, "Combate").addLocale(LangProvider.Locale.ES_CL, "Combate").addLocale(LangProvider.Locale.ES_EC, "Combate").addLocale(LangProvider.Locale.ES_ES, "Combate").addLocale(LangProvider.Locale.ES_MX, "Combate").addLocale(LangProvider.Locale.ES_UY, "Combate").addLocale(LangProvider.Locale.ES_VE, "Combate").addLocale(LangProvider.Locale.FR_FR, "Combat").addLocale(LangProvider.Locale.HU, "Combat").addLocale(LangProvider.Locale.IT_IT, "Combattimento").addLocale(LangProvider.Locale.JA, "Combat").addLocale(LangProvider.Locale.KO_KR, "전투력").addLocale(LangProvider.Locale.LT_LT, "Kovojimas").addLocale(LangProvider.Locale.NL_NL, "Vechten").addLocale(LangProvider.Locale.PL, "Walka").addLocale(LangProvider.Locale.PT_BR, "Combate").addLocale(LangProvider.Locale.RU_RU, "Бой").addLocale(LangProvider.Locale.SV_SE, "Combat").addLocale(LangProvider.Locale.UK_UA, "Бій").addLocale(LangProvider.Locale.ZH_CN, "战斗").addLocale(LangProvider.Locale.ZH_TW, "戰鬥").addLocale(LangProvider.Locale.EN_US, "Combat").build();

    public static final LangProvider.Translation SKILL_ARCHERY = LangProvider.Translation.Builder.start("pmmo.archery").addLocale(LangProvider.Locale.HU, "Archery").addLocale(LangProvider.Locale.JA, "Archery").addLocale(LangProvider.Locale.PL, "Łucznictwo").addLocale(LangProvider.Locale.DE_DE, "Bogenschießen").addLocale(LangProvider.Locale.ES_AR, "Arquería").addLocale(LangProvider.Locale.ES_CL, "Arquería").addLocale(LangProvider.Locale.ES_EC, "Arquería").addLocale(LangProvider.Locale.ES_ES, "Arquería").addLocale(LangProvider.Locale.ES_MX, "Arquería").addLocale(LangProvider.Locale.ES_UY, "Arquería").addLocale(LangProvider.Locale.ES_VE, "Arquería").addLocale(LangProvider.Locale.FR_FR, "Archerie").addLocale(LangProvider.Locale.HU, "Archery").addLocale(LangProvider.Locale.IT_IT, "Archery").addLocale(LangProvider.Locale.JA, "Archery").addLocale(LangProvider.Locale.KO_KR, "활솜씨").addLocale(LangProvider.Locale.LT_LT, "Šaudymas iš lanko").addLocale(LangProvider.Locale.NL_NL, "Boogschieten").addLocale(LangProvider.Locale.PL, "Łucznictwo").addLocale(LangProvider.Locale.PT_BR, "Arquearia").addLocale(LangProvider.Locale.RU_RU, "Стрельба из лука").addLocale(LangProvider.Locale.SV_SE, "Archery").addLocale(LangProvider.Locale.UK_UA, "Стрільба з лука").addLocale(LangProvider.Locale.ZH_CN, "箭术").addLocale(LangProvider.Locale.ZH_TW, "箭術").addLocale(LangProvider.Locale.EN_US, "Archery").build();

    public static final LangProvider.Translation SKILL_SMITHING = LangProvider.Translation.Builder.start("pmmo.smithing").addLocale(LangProvider.Locale.HU, "Smithing").addLocale(LangProvider.Locale.JA, "Smithing").addLocale(LangProvider.Locale.PL, "Kowalstwo").addLocale(LangProvider.Locale.DE_DE, "Schmiederei").addLocale(LangProvider.Locale.ES_AR, "Metalúrgica").addLocale(LangProvider.Locale.ES_CL, "Metalúrgica").addLocale(LangProvider.Locale.ES_EC, "Metalúrgica").addLocale(LangProvider.Locale.ES_ES, "Metalúrgica").addLocale(LangProvider.Locale.ES_MX, "Metalúrgica").addLocale(LangProvider.Locale.ES_UY, "Metalúrgica").addLocale(LangProvider.Locale.ES_VE, "Metalúrgica").addLocale(LangProvider.Locale.FR_FR, "Forgeage").addLocale(LangProvider.Locale.HU, "Smithing").addLocale(LangProvider.Locale.IT_IT, "Smithing").addLocale(LangProvider.Locale.JA, "Smithing").addLocale(LangProvider.Locale.KO_KR, "대장장이").addLocale(LangProvider.Locale.LT_LT, "Kalvininkystė").addLocale(LangProvider.Locale.NL_NL, "Smeden").addLocale(LangProvider.Locale.PL, "Kowalstwo").addLocale(LangProvider.Locale.PT_BR, "Metalurgia").addLocale(LangProvider.Locale.RU_RU, "Кузнечное дело").addLocale(LangProvider.Locale.SV_SE, "Smithing").addLocale(LangProvider.Locale.UK_UA, "Ковальська справа").addLocale(LangProvider.Locale.ZH_CN, "巧匠").addLocale(LangProvider.Locale.ZH_TW, "巧匠").addLocale(LangProvider.Locale.EN_US, "Smithing").build();

    public static final LangProvider.Translation SKILL_FLYING = LangProvider.Translation.Builder.start("pmmo.flying").addLocale(LangProvider.Locale.HU, "Flying").addLocale(LangProvider.Locale.JA, "Flying").addLocale(LangProvider.Locale.PL, "Latanie").addLocale(LangProvider.Locale.DE_DE, "Fliegen").addLocale(LangProvider.Locale.ES_AR, "Vuelo").addLocale(LangProvider.Locale.ES_CL, "Vuelo").addLocale(LangProvider.Locale.ES_EC, "Vuelo").addLocale(LangProvider.Locale.ES_ES, "Vuelo").addLocale(LangProvider.Locale.ES_MX, "Vuelo").addLocale(LangProvider.Locale.ES_UY, "Vuelo").addLocale(LangProvider.Locale.ES_VE, "Vuelo").addLocale(LangProvider.Locale.FR_FR, "Vol").addLocale(LangProvider.Locale.HU, "Flying").addLocale(LangProvider.Locale.IT_IT, "Flying").addLocale(LangProvider.Locale.JA, "Flying").addLocale(LangProvider.Locale.KO_KR, "활공").addLocale(LangProvider.Locale.LT_LT, "Skraidymas").addLocale(LangProvider.Locale.NL_NL, "Vliegen").addLocale(LangProvider.Locale.PL, "Latanie").addLocale(LangProvider.Locale.PT_BR, "Voô").addLocale(LangProvider.Locale.RU_RU, "Полёт").addLocale(LangProvider.Locale.SV_SE, "Flying").addLocale(LangProvider.Locale.UK_UA, "Політ").addLocale(LangProvider.Locale.ZH_CN, "飞翔").addLocale(LangProvider.Locale.ZH_TW, "飛翔").addLocale(LangProvider.Locale.EN_US, "Flying").build();

    public static final LangProvider.Translation SKILL_SWIMMING = LangProvider.Translation.Builder.start("pmmo.swimming").addLocale(LangProvider.Locale.HU, "Swimming").addLocale(LangProvider.Locale.JA, "Swimming").addLocale(LangProvider.Locale.PL, "Pływanie").addLocale(LangProvider.Locale.DE_DE, "Schwimmen").addLocale(LangProvider.Locale.ES_AR, "Natación").addLocale(LangProvider.Locale.ES_CL, "Natación").addLocale(LangProvider.Locale.ES_EC, "Natación").addLocale(LangProvider.Locale.ES_ES, "Natación").addLocale(LangProvider.Locale.ES_MX, "Natación").addLocale(LangProvider.Locale.ES_UY, "Natación").addLocale(LangProvider.Locale.ES_VE, "Natación").addLocale(LangProvider.Locale.FR_FR, "Nage").addLocale(LangProvider.Locale.HU, "Swimming").addLocale(LangProvider.Locale.IT_IT, "Swimming").addLocale(LangProvider.Locale.JA, "Swimming").addLocale(LangProvider.Locale.KO_KR, "수영").addLocale(LangProvider.Locale.LT_LT, "Plaukimas").addLocale(LangProvider.Locale.NL_NL, "Zwemmen").addLocale(LangProvider.Locale.PL, "Pływanie").addLocale(LangProvider.Locale.PT_BR, "Natação").addLocale(LangProvider.Locale.RU_RU, "Плавание").addLocale(LangProvider.Locale.SV_SE, "Swimming").addLocale(LangProvider.Locale.UK_UA, "Плавання").addLocale(LangProvider.Locale.ZH_CN, "游泳").addLocale(LangProvider.Locale.ZH_TW, "游泳").addLocale(LangProvider.Locale.EN_US, "Swimming").build();

    public static final LangProvider.Translation SKILL_FISHING = LangProvider.Translation.Builder.start("pmmo.fishing").addLocale(LangProvider.Locale.HU, "Fishing").addLocale(LangProvider.Locale.JA, "Fishing").addLocale(LangProvider.Locale.PL, "Łowienie").addLocale(LangProvider.Locale.DE_DE, "Fischerei").addLocale(LangProvider.Locale.ES_AR, "Pesca").addLocale(LangProvider.Locale.ES_CL, "Pesca").addLocale(LangProvider.Locale.ES_EC, "Pesca").addLocale(LangProvider.Locale.ES_ES, "Pesca").addLocale(LangProvider.Locale.ES_MX, "Pesca").addLocale(LangProvider.Locale.ES_UY, "Pesca").addLocale(LangProvider.Locale.ES_VE, "Pesca").addLocale(LangProvider.Locale.FR_FR, "Pêche").addLocale(LangProvider.Locale.HU, "Fishing").addLocale(LangProvider.Locale.IT_IT, "Fishing").addLocale(LangProvider.Locale.JA, "Fishing").addLocale(LangProvider.Locale.KO_KR, "낚시").addLocale(LangProvider.Locale.LT_LT, "Žvejyba").addLocale(LangProvider.Locale.NL_NL, "Vissen").addLocale(LangProvider.Locale.PL, "Łowienie").addLocale(LangProvider.Locale.PT_BR, "Pescaria").addLocale(LangProvider.Locale.RU_RU, "Рыболовное дело").addLocale(LangProvider.Locale.SV_SE, "Fishing").addLocale(LangProvider.Locale.UK_UA, "Рибальська справа").addLocale(LangProvider.Locale.ZH_CN, "垂钓").addLocale(LangProvider.Locale.ZH_TW, "垂釣").addLocale(LangProvider.Locale.EN_US, "Fishing").build();

    public static final LangProvider.Translation SKILL_CRAFTING = LangProvider.Translation.Builder.start("pmmo.crafting").addLocale(LangProvider.Locale.HU, "Crafting").addLocale(LangProvider.Locale.JA, "Crafting").addLocale(LangProvider.Locale.PL, "Tworzenie").addLocale(LangProvider.Locale.DE_DE, "Herstellung").addLocale(LangProvider.Locale.ES_AR, "Crafteo").addLocale(LangProvider.Locale.ES_CL, "Crafteo").addLocale(LangProvider.Locale.ES_EC, "Crafteo").addLocale(LangProvider.Locale.ES_ES, "Crafteo").addLocale(LangProvider.Locale.ES_MX, "Crafteo").addLocale(LangProvider.Locale.ES_UY, "Crafteo").addLocale(LangProvider.Locale.ES_VE, "Crafteo").addLocale(LangProvider.Locale.FR_FR, "Artisanat").addLocale(LangProvider.Locale.HU, "Crafting").addLocale(LangProvider.Locale.IT_IT, "Crafting").addLocale(LangProvider.Locale.JA, "Crafting").addLocale(LangProvider.Locale.KO_KR, "제작").addLocale(LangProvider.Locale.LT_LT, "Rankdarbimas").addLocale(LangProvider.Locale.NL_NL, "Vervaardiging").addLocale(LangProvider.Locale.PL, "Tworzenie").addLocale(LangProvider.Locale.PT_BR, "Criação").addLocale(LangProvider.Locale.RU_RU, "Создание предметов").addLocale(LangProvider.Locale.SV_SE, "Crafting").addLocale(LangProvider.Locale.UK_UA, "Крафтинг").addLocale(LangProvider.Locale.ZH_CN, "制造").addLocale(LangProvider.Locale.ZH_TW, "製造").addLocale(LangProvider.Locale.EN_US, "Crafting").build();

    public static final LangProvider.Translation SKILL_MAGIC = LangProvider.Translation.Builder.start("pmmo.magic").addLocale(LangProvider.Locale.HU, "Magic").addLocale(LangProvider.Locale.JA, "Magic").addLocale(LangProvider.Locale.PL, "Magia").addLocale(LangProvider.Locale.DE_DE, "Magie").addLocale(LangProvider.Locale.ES_AR, "Magia").addLocale(LangProvider.Locale.ES_CL, "Magia").addLocale(LangProvider.Locale.ES_EC, "Magia").addLocale(LangProvider.Locale.ES_ES, "Magia").addLocale(LangProvider.Locale.ES_MX, "Magia").addLocale(LangProvider.Locale.ES_UY, "Magia").addLocale(LangProvider.Locale.ES_VE, "Magia").addLocale(LangProvider.Locale.FR_FR, "Magie").addLocale(LangProvider.Locale.HU, "Magic").addLocale(LangProvider.Locale.IT_IT, "Magia").addLocale(LangProvider.Locale.JA, "Magic").addLocale(LangProvider.Locale.KO_KR, "마법").addLocale(LangProvider.Locale.LT_LT, "Magija").addLocale(LangProvider.Locale.NL_NL, "Toverij").addLocale(LangProvider.Locale.PL, "Magia").addLocale(LangProvider.Locale.PT_BR, "Magia").addLocale(LangProvider.Locale.RU_RU, "Колдовство").addLocale(LangProvider.Locale.SV_SE, "Magic").addLocale(LangProvider.Locale.UK_UA, "Чаклунство").addLocale(LangProvider.Locale.ZH_CN, "魔法").addLocale(LangProvider.Locale.ZH_TW, "魔法").addLocale(LangProvider.Locale.EN_US, "Magic").build();

    public static final LangProvider.Translation SKILL_GUNSLINGING = LangProvider.Translation.Builder.start("pmmo.gunslinging").addLocale(LangProvider.Locale.UK_UA, "Стрільба зі зброї").addLocale(LangProvider.Locale.FR_FR, "Fusillade").addLocale(LangProvider.Locale.EN_US, "Gunslinging").build();

    public static final LangProvider.Translation SKILL_SLAYER = LangProvider.Translation.Builder.start("pmmo.slayer").addLocale(LangProvider.Locale.HU, "Slayer").addLocale(LangProvider.Locale.JA, "Slayer").addLocale(LangProvider.Locale.PL, "Zabójca").addLocale(LangProvider.Locale.DE_DE, "Niederstrecker").addLocale(LangProvider.Locale.ES_AR, "Asesino").addLocale(LangProvider.Locale.ES_CL, "Asesino").addLocale(LangProvider.Locale.ES_EC, "Asesino").addLocale(LangProvider.Locale.ES_ES, "Asesino").addLocale(LangProvider.Locale.ES_MX, "Asesino").addLocale(LangProvider.Locale.ES_UY, "Asesino").addLocale(LangProvider.Locale.ES_VE, "Asesino").addLocale(LangProvider.Locale.FR_FR, "Tueur").addLocale(LangProvider.Locale.HU, "Slayer").addLocale(LangProvider.Locale.IT_IT, "Slayer").addLocale(LangProvider.Locale.JA, "Slayer").addLocale(LangProvider.Locale.KO_KR, "도살자").addLocale(LangProvider.Locale.LT_LT, "Žudymas").addLocale(LangProvider.Locale.NL_NL, "Moordenaar").addLocale(LangProvider.Locale.PL, "Zabójca").addLocale(LangProvider.Locale.PT_BR, "Matador").addLocale(LangProvider.Locale.RU_RU, "Убийца").addLocale(LangProvider.Locale.SV_SE, "Slayer").addLocale(LangProvider.Locale.UK_UA, "Вбивця").addLocale(LangProvider.Locale.ZH_CN, "屠夫").addLocale(LangProvider.Locale.ZH_TW, "屠夫").addLocale(LangProvider.Locale.EN_US, "Slayer").build();

    public static final LangProvider.Translation SKILL_FLETCHING = LangProvider.Translation.Builder.start("pmmo.fletching").addLocale(LangProvider.Locale.HU, "Fletching").addLocale(LangProvider.Locale.JA, "Fletching").addLocale(LangProvider.Locale.PL, "Łucznictwo").addLocale(LangProvider.Locale.DE_DE, "Befiederung").addLocale(LangProvider.Locale.ES_AR, "Estabilización").addLocale(LangProvider.Locale.ES_CL, "Estabilización").addLocale(LangProvider.Locale.ES_EC, "Estabilización").addLocale(LangProvider.Locale.ES_ES, "Estabilización").addLocale(LangProvider.Locale.ES_MX, "Estabilización").addLocale(LangProvider.Locale.ES_UY, "Estabilización").addLocale(LangProvider.Locale.ES_VE, "Estabilización").addLocale(LangProvider.Locale.FR_FR, "Archerie").addLocale(LangProvider.Locale.HU, "Fletching").addLocale(LangProvider.Locale.IT_IT, "Fletching").addLocale(LangProvider.Locale.JA, "Fletching").addLocale(LangProvider.Locale.KO_KR, "화살제작").addLocale(LangProvider.Locale.LT_LT, "Flečingas").addLocale(LangProvider.Locale.NL_NL, "Fletchen").addLocale(LangProvider.Locale.PL, "Łucznictwo").addLocale(LangProvider.Locale.PT_BR, "Balístico").addLocale(LangProvider.Locale.RU_RU, "Оперение").addLocale(LangProvider.Locale.SV_SE, "Fletching").addLocale(LangProvider.Locale.UK_UA, "Оперення").addLocale(LangProvider.Locale.ZH_CN, "射箭").addLocale(LangProvider.Locale.ZH_TW, "射箭").addLocale(LangProvider.Locale.EN_US, "Fletching").build();

    public static final LangProvider.Translation SKILL_TAMING = LangProvider.Translation.Builder.start("pmmo.taming").addLocale(LangProvider.Locale.HU, "Taming").addLocale(LangProvider.Locale.JA, "Taming").addLocale(LangProvider.Locale.PL, "Tresowanie").addLocale(LangProvider.Locale.DE_DE, "Zähmung").addLocale(LangProvider.Locale.ES_AR, "Domadura").addLocale(LangProvider.Locale.ES_CL, "Domadura").addLocale(LangProvider.Locale.ES_EC, "Domadura").addLocale(LangProvider.Locale.ES_ES, "Domadura").addLocale(LangProvider.Locale.ES_MX, "Domadura").addLocale(LangProvider.Locale.ES_UY, "Domadura").addLocale(LangProvider.Locale.ES_VE, "Domadura").addLocale(LangProvider.Locale.FR_FR, "Apprivoisement").addLocale(LangProvider.Locale.HU, "Taming").addLocale(LangProvider.Locale.IT_IT, "Taming").addLocale(LangProvider.Locale.JA, "Taming").addLocale(LangProvider.Locale.KO_KR, "목축").addLocale(LangProvider.Locale.LT_LT, "Taming").addLocale(LangProvider.Locale.NL_NL, "Temmen").addLocale(LangProvider.Locale.PL, "Tresowanie").addLocale(LangProvider.Locale.PT_BR, "Domador").addLocale(LangProvider.Locale.RU_RU, "Приручение").addLocale(LangProvider.Locale.SV_SE, "Taming").addLocale(LangProvider.Locale.UK_UA, "Приручення").addLocale(LangProvider.Locale.ZH_CN, "驯服").addLocale(LangProvider.Locale.ZH_TW, "馴服").addLocale(LangProvider.Locale.EN_US, "Taming").build();

    public static final LangProvider.Translation SKILL_HUNTER = LangProvider.Translation.Builder.start("pmmo.hunter").addLocale(LangProvider.Locale.HU, "Hunter").addLocale(LangProvider.Locale.JA, "Hunter").addLocale(LangProvider.Locale.PL, "Łowca").addLocale(LangProvider.Locale.DE_DE, "Jäger").addLocale(LangProvider.Locale.ES_AR, "Cazador").addLocale(LangProvider.Locale.ES_CL, "Cazador").addLocale(LangProvider.Locale.ES_EC, "Cazador").addLocale(LangProvider.Locale.ES_ES, "Cazador").addLocale(LangProvider.Locale.ES_MX, "Cazador").addLocale(LangProvider.Locale.ES_UY, "Cazador").addLocale(LangProvider.Locale.ES_VE, "Cazador").addLocale(LangProvider.Locale.FR_FR, "Chasseur").addLocale(LangProvider.Locale.HU, "Hunter").addLocale(LangProvider.Locale.IT_IT, "Cacciatore").addLocale(LangProvider.Locale.JA, "Hunter").addLocale(LangProvider.Locale.KO_KR, "사냥").addLocale(LangProvider.Locale.LT_LT, "Hunter").addLocale(LangProvider.Locale.NL_NL, "Jager").addLocale(LangProvider.Locale.PL, "Łowca").addLocale(LangProvider.Locale.PT_BR, "Caçador").addLocale(LangProvider.Locale.RU_RU, "Охота").addLocale(LangProvider.Locale.SV_SE, "Hunter").addLocale(LangProvider.Locale.UK_UA, "Полювання").addLocale(LangProvider.Locale.ZH_CN, "狩猎").addLocale(LangProvider.Locale.ZH_TW, "狩獵").addLocale(LangProvider.Locale.EN_US, "Hunter").build();

    public static final LangProvider.Translation SKILL_ENGINEERING = LangProvider.Translation.Builder.start("pmmo.engineering").addLocale(LangProvider.Locale.HU, "Engineering").addLocale(LangProvider.Locale.JA, "Engineering").addLocale(LangProvider.Locale.PL, "Inżynieria").addLocale(LangProvider.Locale.DE_DE, "Ingenieurswesen").addLocale(LangProvider.Locale.ES_AR, "Ingeniería").addLocale(LangProvider.Locale.ES_CL, "Ingeniería").addLocale(LangProvider.Locale.ES_EC, "Ingeniería").addLocale(LangProvider.Locale.ES_ES, "Ingeniería").addLocale(LangProvider.Locale.ES_MX, "Ingeniería").addLocale(LangProvider.Locale.ES_UY, "Ingeniería").addLocale(LangProvider.Locale.ES_VE, "Ingeniería").addLocale(LangProvider.Locale.FR_FR, "Ingénierie").addLocale(LangProvider.Locale.HU, "Engineering").addLocale(LangProvider.Locale.IT_IT, "Ingegniere").addLocale(LangProvider.Locale.JA, "Engineering").addLocale(LangProvider.Locale.KO_KR, "엔지니어링").addLocale(LangProvider.Locale.LT_LT, "Engineering").addLocale(LangProvider.Locale.NL_NL, "Ingenieurswerk").addLocale(LangProvider.Locale.PL, "Inżynieria").addLocale(LangProvider.Locale.PT_BR, "Engenheiro").addLocale(LangProvider.Locale.RU_RU, "Инженерное дело").addLocale(LangProvider.Locale.SV_SE, "Engineering").addLocale(LangProvider.Locale.UK_UA, "Інженерна справа").addLocale(LangProvider.Locale.ZH_CN, "工程").addLocale(LangProvider.Locale.ZH_TW, "工程").addLocale(LangProvider.Locale.EN_US, "Engineering").build();

    public static final LangProvider.Translation SKILL_BLOOD_MAGIC = LangProvider.Translation.Builder.start("pmmo.blood_magic").addLocale(LangProvider.Locale.HU, "Blood Magic").addLocale(LangProvider.Locale.JA, "Blood Magic").addLocale(LangProvider.Locale.PL, "Magia Krwi").addLocale(LangProvider.Locale.DE_DE, "Blut Magie").addLocale(LangProvider.Locale.ES_AR, "Magia Sangrienta").addLocale(LangProvider.Locale.ES_CL, "Magia Sangrienta").addLocale(LangProvider.Locale.ES_EC, "Magia Sangrienta").addLocale(LangProvider.Locale.ES_ES, "Magia Sangrienta").addLocale(LangProvider.Locale.ES_MX, "Magia Sangrienta").addLocale(LangProvider.Locale.ES_UY, "Magia Sangrienta").addLocale(LangProvider.Locale.ES_VE, "Magia Sangrienta").addLocale(LangProvider.Locale.FR_FR, "Magie du sang").addLocale(LangProvider.Locale.HU, "Blood Magic").addLocale(LangProvider.Locale.IT_IT, "Blood Magic").addLocale(LangProvider.Locale.JA, "Blood Magic").addLocale(LangProvider.Locale.KO_KR, "피 마법").addLocale(LangProvider.Locale.LT_LT, "Blood Magic").addLocale(LangProvider.Locale.NL_NL, "Bloedmagie").addLocale(LangProvider.Locale.PL, "Magia Krwi").addLocale(LangProvider.Locale.PT_BR, "Magia do Sangue").addLocale(LangProvider.Locale.RU_RU, "Blood Magic").addLocale(LangProvider.Locale.SV_SE, "Blood Magic").addLocale(LangProvider.Locale.UK_UA, "Магія крові").addLocale(LangProvider.Locale.ZH_CN, "腥红魔法").addLocale(LangProvider.Locale.ZH_TW, "腥紅魔法").addLocale(LangProvider.Locale.EN_US, "Blood Magic").build();

    public static final LangProvider.Translation SKILL_ASTRAL_MAGIC = LangProvider.Translation.Builder.start("pmmo.astral_magic").addLocale(LangProvider.Locale.HU, "Astral Magic").addLocale(LangProvider.Locale.JA, "Astral Magic").addLocale(LangProvider.Locale.PL, "Magia astralna").addLocale(LangProvider.Locale.DE_DE, "Astrale Zauberei").addLocale(LangProvider.Locale.ES_AR, "Magia Astral").addLocale(LangProvider.Locale.ES_CL, "Magia Astral").addLocale(LangProvider.Locale.ES_EC, "Magia Astral").addLocale(LangProvider.Locale.ES_ES, "Magia Astral").addLocale(LangProvider.Locale.ES_MX, "Magia Astral").addLocale(LangProvider.Locale.ES_UY, "Magia Astral").addLocale(LangProvider.Locale.ES_VE, "Magia Astral").addLocale(LangProvider.Locale.FR_FR, "Magie astrale").addLocale(LangProvider.Locale.HU, "Astral Magic").addLocale(LangProvider.Locale.IT_IT, "Astral Magic").addLocale(LangProvider.Locale.JA, "Astral Magic").addLocale(LangProvider.Locale.KO_KR, "정신 마법").addLocale(LangProvider.Locale.LT_LT, "Astral Magic").addLocale(LangProvider.Locale.NL_NL, "Sterrenmagie").addLocale(LangProvider.Locale.PL, "Magia astralna").addLocale(LangProvider.Locale.PT_BR, "Magia Astral").addLocale(LangProvider.Locale.RU_RU, "Astral Magic").addLocale(LangProvider.Locale.SV_SE, "Astral Magic").addLocale(LangProvider.Locale.UK_UA, "Астральна магія").addLocale(LangProvider.Locale.ZH_CN, "星空魔法").addLocale(LangProvider.Locale.ZH_TW, "星空魔法").addLocale(LangProvider.Locale.EN_US, "Astral Magic").build();

    public static final LangProvider.Translation SKILL_GOOD_MAGIC = LangProvider.Translation.Builder.start("pmmo.good_magic").addLocale(LangProvider.Locale.HU, "Good Magic").addLocale(LangProvider.Locale.JA, "Good Magic").addLocale(LangProvider.Locale.PL, "Biała Magia").addLocale(LangProvider.Locale.DE_DE, "Gute Zauberei").addLocale(LangProvider.Locale.ES_AR, "Magia del Bien").addLocale(LangProvider.Locale.ES_CL, "Magia del Bien").addLocale(LangProvider.Locale.ES_EC, "Magia del Bien").addLocale(LangProvider.Locale.ES_ES, "Magia del Bien").addLocale(LangProvider.Locale.ES_MX, "Magia del Bien").addLocale(LangProvider.Locale.ES_UY, "Magia del Bien").addLocale(LangProvider.Locale.ES_VE, "Magia del Bien").addLocale(LangProvider.Locale.FR_FR, "Bonne magie").addLocale(LangProvider.Locale.HU, "Good Magic").addLocale(LangProvider.Locale.IT_IT, "Good Magic").addLocale(LangProvider.Locale.JA, "Good Magic").addLocale(LangProvider.Locale.KO_KR, "백마법").addLocale(LangProvider.Locale.LT_LT, "Good Magic").addLocale(LangProvider.Locale.NL_NL, "Goede Magie").addLocale(LangProvider.Locale.PL, "Biała Magia").addLocale(LangProvider.Locale.PT_BR, "Magia Boa").addLocale(LangProvider.Locale.RU_RU, "Good Magic").addLocale(LangProvider.Locale.SV_SE, "Good Magic").addLocale(LangProvider.Locale.UK_UA, "Магія добра ").addLocale(LangProvider.Locale.ZH_CN, "良善魔法").addLocale(LangProvider.Locale.ZH_TW, "良善魔法").addLocale(LangProvider.Locale.EN_US, "Good Magic").build();

    public static final LangProvider.Translation SKILL_EVIL_MAGIC = LangProvider.Translation.Builder.start("pmmo.evil_magic").addLocale(LangProvider.Locale.HU, "Evil Magic").addLocale(LangProvider.Locale.JA, "Evil Magic").addLocale(LangProvider.Locale.PL, "Czarna Magia").addLocale(LangProvider.Locale.DE_DE, "Böse Zauberei").addLocale(LangProvider.Locale.ES_AR, "Magia Malévola").addLocale(LangProvider.Locale.ES_CL, "Magia Malévola").addLocale(LangProvider.Locale.ES_EC, "Magia Malévola").addLocale(LangProvider.Locale.ES_ES, "Magia Malévola").addLocale(LangProvider.Locale.ES_MX, "Magia Malévola").addLocale(LangProvider.Locale.ES_UY, "Magia Malévola").addLocale(LangProvider.Locale.ES_VE, "Magia Malévola").addLocale(LangProvider.Locale.FR_FR, "Magie maléfique").addLocale(LangProvider.Locale.HU, "Evil Magic").addLocale(LangProvider.Locale.IT_IT, "Evil Magic").addLocale(LangProvider.Locale.JA, "Evil Magic").addLocale(LangProvider.Locale.KO_KR, "흑마법").addLocale(LangProvider.Locale.LT_LT, "Evil Magic").addLocale(LangProvider.Locale.NL_NL, "Slechte Magie").addLocale(LangProvider.Locale.PL, "Czarna Magia").addLocale(LangProvider.Locale.PT_BR, "Magia Malígna").addLocale(LangProvider.Locale.RU_RU, "Evil Magic").addLocale(LangProvider.Locale.SV_SE, "Evil Magic").addLocale(LangProvider.Locale.UK_UA, "Магія Зла").addLocale(LangProvider.Locale.ZH_CN, "恶毒魔法").addLocale(LangProvider.Locale.ZH_TW, "惡毒魔法").addLocale(LangProvider.Locale.EN_US, "Evil Magic").build();

    public static final LangProvider.Translation SKILL_ARCANE_MAGIC = LangProvider.Translation.Builder.start("pmmo.arcane_magic").addLocale(LangProvider.Locale.HU, "Arcane Magic").addLocale(LangProvider.Locale.JA, "Arcane Magic").addLocale(LangProvider.Locale.PL, "Magia Tajemna").addLocale(LangProvider.Locale.DE_DE, "Arkane Zauberei").addLocale(LangProvider.Locale.ES_AR, "Magia Arcana").addLocale(LangProvider.Locale.ES_CL, "Magia Arcana").addLocale(LangProvider.Locale.ES_EC, "Magia Arcana").addLocale(LangProvider.Locale.ES_ES, "Magia Arcana").addLocale(LangProvider.Locale.ES_MX, "Magia Arcana").addLocale(LangProvider.Locale.ES_UY, "Magia Arcana").addLocale(LangProvider.Locale.ES_VE, "Magia Arcana").addLocale(LangProvider.Locale.FR_FR, "Magie des arcanes").addLocale(LangProvider.Locale.HU, "Arcane Magic").addLocale(LangProvider.Locale.IT_IT, "Arcane Magic").addLocale(LangProvider.Locale.JA, "Arcane Magic").addLocale(LangProvider.Locale.KO_KR, "비전 마법").addLocale(LangProvider.Locale.LT_LT, "Arcane Magic").addLocale(LangProvider.Locale.NL_NL, "Geheime Magie").addLocale(LangProvider.Locale.PL, "Magia Tajemna").addLocale(LangProvider.Locale.PT_BR, "Magia Arcana").addLocale(LangProvider.Locale.RU_RU, "Arcane Magic").addLocale(LangProvider.Locale.SV_SE, "Arcane Magic").addLocale(LangProvider.Locale.UK_UA, "Таємнича магія").addLocale(LangProvider.Locale.ZH_CN, "奥术魔法").addLocale(LangProvider.Locale.ZH_TW, "奧術魔法").addLocale(LangProvider.Locale.EN_US, "Arcane Magic").build();

    public static final LangProvider.Translation SKILL_ELEMENTAL = LangProvider.Translation.Builder.start("pmmo.elemental").addLocale(LangProvider.Locale.HU, "Elemental").addLocale(LangProvider.Locale.JA, "Elemental").addLocale(LangProvider.Locale.PL, "Podstawowa").addLocale(LangProvider.Locale.DE_DE, "Elemental").addLocale(LangProvider.Locale.ES_AR, "Elemental").addLocale(LangProvider.Locale.ES_CL, "Elemental").addLocale(LangProvider.Locale.ES_EC, "Elemental").addLocale(LangProvider.Locale.ES_ES, "Elemental").addLocale(LangProvider.Locale.ES_MX, "Elemental").addLocale(LangProvider.Locale.ES_UY, "Elemental").addLocale(LangProvider.Locale.ES_VE, "Elemental").addLocale(LangProvider.Locale.FR_FR, "Élémentaire").addLocale(LangProvider.Locale.HU, "Elemental").addLocale(LangProvider.Locale.IT_IT, "Elemental").addLocale(LangProvider.Locale.JA, "Elemental").addLocale(LangProvider.Locale.KO_KR, "원소").addLocale(LangProvider.Locale.LT_LT, "Elemental").addLocale(LangProvider.Locale.NL_NL, "Elementale").addLocale(LangProvider.Locale.PL, "Podstawowa").addLocale(LangProvider.Locale.PT_BR, "Elemental").addLocale(LangProvider.Locale.RU_RU, "Elemental").addLocale(LangProvider.Locale.SV_SE, "Elemental").addLocale(LangProvider.Locale.UK_UA, "Стихійний").addLocale(LangProvider.Locale.ZH_CN, "元素").addLocale(LangProvider.Locale.ZH_TW, "元素").addLocale(LangProvider.Locale.EN_US, "Elemental").build();

    public static final LangProvider.Translation SKILL_EARTH = LangProvider.Translation.Builder.start("pmmo.earth").addLocale(LangProvider.Locale.HU, "Earth").addLocale(LangProvider.Locale.JA, "Earth").addLocale(LangProvider.Locale.PL, "Ziemia").addLocale(LangProvider.Locale.DE_DE, "Erde").addLocale(LangProvider.Locale.ES_AR, "Tierra").addLocale(LangProvider.Locale.ES_CL, "Tierra").addLocale(LangProvider.Locale.ES_EC, "Tierra").addLocale(LangProvider.Locale.ES_ES, "Tierra").addLocale(LangProvider.Locale.ES_MX, "Tierra").addLocale(LangProvider.Locale.ES_UY, "Tierra").addLocale(LangProvider.Locale.ES_VE, "Tierra").addLocale(LangProvider.Locale.FR_FR, "Terre").addLocale(LangProvider.Locale.HU, "Earth").addLocale(LangProvider.Locale.IT_IT, "Earth").addLocale(LangProvider.Locale.JA, "Earth").addLocale(LangProvider.Locale.KO_KR, "흑").addLocale(LangProvider.Locale.LT_LT, "Earth").addLocale(LangProvider.Locale.NL_NL, "Aarde").addLocale(LangProvider.Locale.PL, "Ziemia").addLocale(LangProvider.Locale.PT_BR, "Terra").addLocale(LangProvider.Locale.RU_RU, "Earth").addLocale(LangProvider.Locale.SV_SE, "Earth").addLocale(LangProvider.Locale.UK_UA, "Земля").addLocale(LangProvider.Locale.ZH_CN, "大地").addLocale(LangProvider.Locale.ZH_TW, "大地").addLocale(LangProvider.Locale.EN_US, "Earth").build();

    public static final LangProvider.Translation SKILL_WATER = LangProvider.Translation.Builder.start("pmmo.water").addLocale(LangProvider.Locale.HU, "Water").addLocale(LangProvider.Locale.JA, "Water").addLocale(LangProvider.Locale.PL, "Woda").addLocale(LangProvider.Locale.DE_DE, "Wasser").addLocale(LangProvider.Locale.ES_AR, "Agua").addLocale(LangProvider.Locale.ES_CL, "Agua").addLocale(LangProvider.Locale.ES_EC, "Agua").addLocale(LangProvider.Locale.ES_ES, "Agua").addLocale(LangProvider.Locale.ES_MX, "Agua").addLocale(LangProvider.Locale.ES_UY, "Agua").addLocale(LangProvider.Locale.ES_VE, "Agua").addLocale(LangProvider.Locale.FR_FR, "Eau").addLocale(LangProvider.Locale.HU, "Water").addLocale(LangProvider.Locale.IT_IT, "Water").addLocale(LangProvider.Locale.JA, "Water").addLocale(LangProvider.Locale.KO_KR, "물").addLocale(LangProvider.Locale.LT_LT, "Water").addLocale(LangProvider.Locale.NL_NL, "Water").addLocale(LangProvider.Locale.PL, "Woda").addLocale(LangProvider.Locale.PT_BR, "Água").addLocale(LangProvider.Locale.RU_RU, "Water").addLocale(LangProvider.Locale.SV_SE, "Water").addLocale(LangProvider.Locale.UK_UA, "Вода").addLocale(LangProvider.Locale.ZH_CN, "净水").addLocale(LangProvider.Locale.ZH_TW, "淨水").addLocale(LangProvider.Locale.EN_US, "Water").build();

    public static final LangProvider.Translation SKILL_AIR = LangProvider.Translation.Builder.start("pmmo.air").addLocale(LangProvider.Locale.HU, "Air").addLocale(LangProvider.Locale.JA, "Air").addLocale(LangProvider.Locale.PL, "Powietrze").addLocale(LangProvider.Locale.DE_DE, "Luft").addLocale(LangProvider.Locale.ES_AR, "Aire").addLocale(LangProvider.Locale.ES_CL, "Aire").addLocale(LangProvider.Locale.ES_EC, "Aire").addLocale(LangProvider.Locale.ES_ES, "Aire").addLocale(LangProvider.Locale.ES_MX, "Aire").addLocale(LangProvider.Locale.ES_UY, "Aire").addLocale(LangProvider.Locale.ES_VE, "Aire").addLocale(LangProvider.Locale.FR_FR, "Air").addLocale(LangProvider.Locale.HU, "Air").addLocale(LangProvider.Locale.IT_IT, "Air").addLocale(LangProvider.Locale.JA, "Air").addLocale(LangProvider.Locale.KO_KR, "공기").addLocale(LangProvider.Locale.LT_LT, "Air").addLocale(LangProvider.Locale.NL_NL, "Lucht").addLocale(LangProvider.Locale.PL, "Powietrze").addLocale(LangProvider.Locale.PT_BR, "Ar").addLocale(LangProvider.Locale.RU_RU, "Air").addLocale(LangProvider.Locale.SV_SE, "Air").addLocale(LangProvider.Locale.UK_UA, "Повітря").addLocale(LangProvider.Locale.ZH_CN, "空气").addLocale(LangProvider.Locale.ZH_TW, "空氣").addLocale(LangProvider.Locale.EN_US, "Air").build();

    public static final LangProvider.Translation SKILL_FIRE = LangProvider.Translation.Builder.start("pmmo.fire").addLocale(LangProvider.Locale.HU, "Fire").addLocale(LangProvider.Locale.JA, "Fire").addLocale(LangProvider.Locale.PL, "Ogień").addLocale(LangProvider.Locale.DE_DE, "Feuer").addLocale(LangProvider.Locale.ES_AR, "Fuego").addLocale(LangProvider.Locale.ES_CL, "Fuego").addLocale(LangProvider.Locale.ES_EC, "Fuego").addLocale(LangProvider.Locale.ES_ES, "Fuego").addLocale(LangProvider.Locale.ES_MX, "Fuego").addLocale(LangProvider.Locale.ES_UY, "Fuego").addLocale(LangProvider.Locale.ES_VE, "Fuego").addLocale(LangProvider.Locale.FR_FR, "Feu").addLocale(LangProvider.Locale.HU, "Fire").addLocale(LangProvider.Locale.IT_IT, "Fire").addLocale(LangProvider.Locale.JA, "Fire").addLocale(LangProvider.Locale.KO_KR, "불").addLocale(LangProvider.Locale.LT_LT, "Fire").addLocale(LangProvider.Locale.NL_NL, "Vuur").addLocale(LangProvider.Locale.PL, "Ogień").addLocale(LangProvider.Locale.PT_BR, "Fogo").addLocale(LangProvider.Locale.RU_RU, "Fire").addLocale(LangProvider.Locale.SV_SE, "Fire").addLocale(LangProvider.Locale.UK_UA, "Вогонь").addLocale(LangProvider.Locale.ZH_CN, "火焰").addLocale(LangProvider.Locale.ZH_TW, "火焰").addLocale(LangProvider.Locale.EN_US, "Fire").build();

    public static final LangProvider.Translation SKILL_LIGHTNING = LangProvider.Translation.Builder.start("pmmo.lightning").addLocale(LangProvider.Locale.HU, "Lightning").addLocale(LangProvider.Locale.JA, "Lightning").addLocale(LangProvider.Locale.PL, "Błyskawice").addLocale(LangProvider.Locale.DE_DE, "Blitz").addLocale(LangProvider.Locale.ES_AR, "Electricidad").addLocale(LangProvider.Locale.ES_CL, "Electricidad").addLocale(LangProvider.Locale.ES_EC, "Electricidad").addLocale(LangProvider.Locale.ES_ES, "Electricidad").addLocale(LangProvider.Locale.ES_MX, "Electricidad").addLocale(LangProvider.Locale.ES_UY, "Electricidad").addLocale(LangProvider.Locale.ES_VE, "Electricidad").addLocale(LangProvider.Locale.FR_FR, "Foudre").addLocale(LangProvider.Locale.HU, "Lightning").addLocale(LangProvider.Locale.IT_IT, "Lightning").addLocale(LangProvider.Locale.JA, "Lightning").addLocale(LangProvider.Locale.KO_KR, "번개").addLocale(LangProvider.Locale.LT_LT, "Lightning").addLocale(LangProvider.Locale.NL_NL, "Bliksem").addLocale(LangProvider.Locale.PL, "Błyskawice").addLocale(LangProvider.Locale.PT_BR, "Relâmpago").addLocale(LangProvider.Locale.RU_RU, "Lightning").addLocale(LangProvider.Locale.SV_SE, "Lightning").addLocale(LangProvider.Locale.UK_UA, "Блискавка").addLocale(LangProvider.Locale.ZH_CN, "闪电").addLocale(LangProvider.Locale.ZH_TW, "閃電").addLocale(LangProvider.Locale.EN_US, "Lightning").build();

    public static final LangProvider.Translation SKILL_VOID = LangProvider.Translation.Builder.start("pmmo.void").addLocale(LangProvider.Locale.HU, "Void").addLocale(LangProvider.Locale.JA, "Void").addLocale(LangProvider.Locale.PL, "Unieważnianie").addLocale(LangProvider.Locale.DE_DE, "Leere").addLocale(LangProvider.Locale.ES_AR, "Vacío").addLocale(LangProvider.Locale.ES_CL, "Vacío").addLocale(LangProvider.Locale.ES_EC, "Vacío").addLocale(LangProvider.Locale.ES_ES, "Vacío").addLocale(LangProvider.Locale.ES_MX, "Vacío").addLocale(LangProvider.Locale.ES_UY, "Vacío").addLocale(LangProvider.Locale.ES_VE, "Vacío").addLocale(LangProvider.Locale.FR_FR, "Vide").addLocale(LangProvider.Locale.HU, "Void").addLocale(LangProvider.Locale.IT_IT, "Void").addLocale(LangProvider.Locale.JA, "Void").addLocale(LangProvider.Locale.KO_KR, "공허").addLocale(LangProvider.Locale.LT_LT, "Void").addLocale(LangProvider.Locale.NL_NL, "Leegte").addLocale(LangProvider.Locale.PL, "Unieważnianie").addLocale(LangProvider.Locale.PT_BR, "Vazio").addLocale(LangProvider.Locale.RU_RU, "Void").addLocale(LangProvider.Locale.SV_SE, "Void").addLocale(LangProvider.Locale.UK_UA, "Пустота").addLocale(LangProvider.Locale.ZH_CN, "虚空").addLocale(LangProvider.Locale.ZH_TW, "虛空").addLocale(LangProvider.Locale.EN_US, "Void").build();

    public static final LangProvider.Translation SKILL_THAUMATIC = LangProvider.Translation.Builder.start("pmmo.thaumatic").addLocale(LangProvider.Locale.HU, "Thaumatic").addLocale(LangProvider.Locale.JA, "Thaumatic").addLocale(LangProvider.Locale.PL, "Traumatyczna").addLocale(LangProvider.Locale.DE_DE, "Thaumatisch").addLocale(LangProvider.Locale.ES_AR, "Taumático").addLocale(LangProvider.Locale.ES_CL, "Taumático").addLocale(LangProvider.Locale.ES_EC, "Taumático").addLocale(LangProvider.Locale.ES_ES, "Taumático").addLocale(LangProvider.Locale.ES_MX, "Taumático").addLocale(LangProvider.Locale.ES_UY, "Taumático").addLocale(LangProvider.Locale.ES_VE, "Taumático").addLocale(LangProvider.Locale.FR_FR, "Thaumatique").addLocale(LangProvider.Locale.HU, "Thaumatic").addLocale(LangProvider.Locale.IT_IT, "Thaumatic").addLocale(LangProvider.Locale.JA, "Thaumatic").addLocale(LangProvider.Locale.KO_KR, "기적").addLocale(LangProvider.Locale.LT_LT, "Thaumatic").addLocale(LangProvider.Locale.NL_NL, "Thaumatische").addLocale(LangProvider.Locale.PL, "Traumatyczna").addLocale(LangProvider.Locale.PT_BR, "Tectônica").addLocale(LangProvider.Locale.RU_RU, "Thaumatic").addLocale(LangProvider.Locale.SV_SE, "Thaumatic").addLocale(LangProvider.Locale.UK_UA, "Thaumatic").addLocale(LangProvider.Locale.ZH_CN, "奇术").addLocale(LangProvider.Locale.ZH_TW, "奇術").addLocale(LangProvider.Locale.EN_US, "Thaumatic").build();

    public static final LangProvider.Translation SKILL_SUMMONING = LangProvider.Translation.Builder.start("pmmo.summoning").addLocale(LangProvider.Locale.HU, "Summoning").addLocale(LangProvider.Locale.JA, "Summoning").addLocale(LangProvider.Locale.PL, "Przywołanie").addLocale(LangProvider.Locale.DE_DE, "Beschwörung").addLocale(LangProvider.Locale.ES_AR, "Convocación").addLocale(LangProvider.Locale.ES_CL, "Convocación").addLocale(LangProvider.Locale.ES_EC, "Convocación").addLocale(LangProvider.Locale.ES_ES, "Convocación").addLocale(LangProvider.Locale.ES_MX, "Convocación").addLocale(LangProvider.Locale.ES_UY, "Convocación").addLocale(LangProvider.Locale.ES_VE, "Convocación").addLocale(LangProvider.Locale.FR_FR, "Invocation").addLocale(LangProvider.Locale.HU, "Summoning").addLocale(LangProvider.Locale.IT_IT, "Summoning").addLocale(LangProvider.Locale.JA, "Summoning").addLocale(LangProvider.Locale.KO_KR, "소환술").addLocale(LangProvider.Locale.LT_LT, "Summoning").addLocale(LangProvider.Locale.NL_NL, "Oproepen").addLocale(LangProvider.Locale.PL, "Przywołanie").addLocale(LangProvider.Locale.PT_BR, "Invocação").addLocale(LangProvider.Locale.RU_RU, "Summoning").addLocale(LangProvider.Locale.SV_SE, "Summoning").addLocale(LangProvider.Locale.UK_UA, "Прикликання").addLocale(LangProvider.Locale.ZH_CN, "召唤").addLocale(LangProvider.Locale.ZH_TW, "召喚").addLocale(LangProvider.Locale.EN_US, "Summoning").build();

    public static final LangProvider.Translation SKILL_INVENTION = LangProvider.Translation.Builder.start("pmmo.invention").addLocale(LangProvider.Locale.HU, "Invention").addLocale(LangProvider.Locale.JA, "Invention").addLocale(LangProvider.Locale.PL, "Tworzenie").addLocale(LangProvider.Locale.DE_DE, "Erfindung").addLocale(LangProvider.Locale.ES_AR, "Invención").addLocale(LangProvider.Locale.ES_CL, "Invención").addLocale(LangProvider.Locale.ES_EC, "Invención").addLocale(LangProvider.Locale.ES_ES, "Invención").addLocale(LangProvider.Locale.ES_MX, "Invención").addLocale(LangProvider.Locale.ES_UY, "Invención").addLocale(LangProvider.Locale.ES_VE, "Invención").addLocale(LangProvider.Locale.FR_FR, "Invention").addLocale(LangProvider.Locale.HU, "Invention").addLocale(LangProvider.Locale.IT_IT, "Invention").addLocale(LangProvider.Locale.JA, "Invention").addLocale(LangProvider.Locale.KO_KR, "발명").addLocale(LangProvider.Locale.LT_LT, "Invention").addLocale(LangProvider.Locale.NL_NL, "Uitvinden").addLocale(LangProvider.Locale.PL, "Tworzenie").addLocale(LangProvider.Locale.PT_BR, "Invenção").addLocale(LangProvider.Locale.RU_RU, "Invention").addLocale(LangProvider.Locale.SV_SE, "Invention").addLocale(LangProvider.Locale.UK_UA, "Винахід").addLocale(LangProvider.Locale.ZH_CN, "发明").addLocale(LangProvider.Locale.ZH_TW, "發明").addLocale(LangProvider.Locale.EN_US, "Invention").build();

    public static final LangProvider.Translation SKILL_RUNECRAFTING = LangProvider.Translation.Builder.start("pmmo.runecrafting").addLocale(LangProvider.Locale.HU, "Runecrafting").addLocale(LangProvider.Locale.JA, "Runecrafting").addLocale(LangProvider.Locale.PL, "Tworzenie run").addLocale(LangProvider.Locale.DE_DE, "Runenherstellung").addLocale(LangProvider.Locale.ES_AR, "Crafteo de Runas").addLocale(LangProvider.Locale.ES_CL, "Crafteo de Runas").addLocale(LangProvider.Locale.ES_EC, "Crafteo de Runas").addLocale(LangProvider.Locale.ES_ES, "Crafteo de Runas").addLocale(LangProvider.Locale.ES_MX, "Crafteo de Runas").addLocale(LangProvider.Locale.ES_UY, "Crafteo de Runas").addLocale(LangProvider.Locale.ES_VE, "Crafteo de Runas").addLocale(LangProvider.Locale.FR_FR, "Fabrication de runes").addLocale(LangProvider.Locale.HU, "Runecrafting").addLocale(LangProvider.Locale.IT_IT, "Runecrafting").addLocale(LangProvider.Locale.JA, "Runecrafting").addLocale(LangProvider.Locale.KO_KR, "룬제작").addLocale(LangProvider.Locale.LT_LT, "Runecrafting").addLocale(LangProvider.Locale.NL_NL, "Runenmaken").addLocale(LangProvider.Locale.PL, "Tworzenie run").addLocale(LangProvider.Locale.PT_BR, "Criação de Runas").addLocale(LangProvider.Locale.RU_RU, "Runecrafting").addLocale(LangProvider.Locale.SV_SE, "Runecrafting").addLocale(LangProvider.Locale.UK_UA, "Виготовлення рун").addLocale(LangProvider.Locale.ZH_CN, "制符").addLocale(LangProvider.Locale.ZH_TW, "製符").addLocale(LangProvider.Locale.EN_US, "Runecrafting").build();

    public static final LangProvider.Translation SKILL_PRAYER = LangProvider.Translation.Builder.start("pmmo.prayer").addLocale(LangProvider.Locale.HU, "Prayer").addLocale(LangProvider.Locale.JA, "Prayer").addLocale(LangProvider.Locale.PL, "Kapłan").addLocale(LangProvider.Locale.DE_DE, "Prediger").addLocale(LangProvider.Locale.ES_AR, "Cura").addLocale(LangProvider.Locale.ES_CL, "Cura").addLocale(LangProvider.Locale.ES_EC, "Cura").addLocale(LangProvider.Locale.ES_ES, "Cura").addLocale(LangProvider.Locale.ES_MX, "Cura").addLocale(LangProvider.Locale.ES_UY, "Cura").addLocale(LangProvider.Locale.ES_VE, "Cura").addLocale(LangProvider.Locale.FR_FR, "Prière").addLocale(LangProvider.Locale.HU, "Prayer").addLocale(LangProvider.Locale.IT_IT, "Prayer").addLocale(LangProvider.Locale.JA, "Prayer").addLocale(LangProvider.Locale.KO_KR, "기도").addLocale(LangProvider.Locale.LT_LT, "Prayer").addLocale(LangProvider.Locale.NL_NL, "Bidden").addLocale(LangProvider.Locale.PL, "Kapłan").addLocale(LangProvider.Locale.PT_BR, "Orador").addLocale(LangProvider.Locale.RU_RU, "Prayer").addLocale(LangProvider.Locale.SV_SE, "Prayer").addLocale(LangProvider.Locale.UK_UA, "Молитва").addLocale(LangProvider.Locale.ZH_CN, "祈祷").addLocale(LangProvider.Locale.ZH_TW, "祈禱").addLocale(LangProvider.Locale.EN_US, "Prayer").build();

    public static final LangProvider.Translation SKILL_COOKING = LangProvider.Translation.Builder.start("pmmo.cooking").addLocale(LangProvider.Locale.HU, "Cooking").addLocale(LangProvider.Locale.JA, "Cooking").addLocale(LangProvider.Locale.PL, "Gotowanie").addLocale(LangProvider.Locale.DE_DE, "Kochen").addLocale(LangProvider.Locale.ES_AR, "Cocina").addLocale(LangProvider.Locale.ES_CL, "Cocina").addLocale(LangProvider.Locale.ES_EC, "Cocina").addLocale(LangProvider.Locale.ES_ES, "Cocina").addLocale(LangProvider.Locale.ES_MX, "Cocina").addLocale(LangProvider.Locale.ES_UY, "Cocina").addLocale(LangProvider.Locale.ES_VE, "Cocina").addLocale(LangProvider.Locale.FR_FR, "Cuisine").addLocale(LangProvider.Locale.HU, "Cooking").addLocale(LangProvider.Locale.IT_IT, "Cooking").addLocale(LangProvider.Locale.JA, "Cooking").addLocale(LangProvider.Locale.KO_KR, "요리").addLocale(LangProvider.Locale.LT_LT, "Cooking").addLocale(LangProvider.Locale.NL_NL, "Koken").addLocale(LangProvider.Locale.PL, "Gotowanie").addLocale(LangProvider.Locale.PT_BR, "Cozinhar").addLocale(LangProvider.Locale.RU_RU, "Cooking").addLocale(LangProvider.Locale.SV_SE, "Cooking").addLocale(LangProvider.Locale.UK_UA, "Кулінарія").addLocale(LangProvider.Locale.ZH_CN, "烹饪").addLocale(LangProvider.Locale.ZH_TW, "烹飪").addLocale(LangProvider.Locale.EN_US, "Cooking").build();

    public static final LangProvider.Translation SKILL_FIREMAKING = LangProvider.Translation.Builder.start("pmmo.firemaking").addLocale(LangProvider.Locale.HU, "Firemaking").addLocale(LangProvider.Locale.JA, "Firemaking").addLocale(LangProvider.Locale.PL, "Rozpalanie ognia").addLocale(LangProvider.Locale.DE_DE, "Feuermacher").addLocale(LangProvider.Locale.ES_AR, "Soplete").addLocale(LangProvider.Locale.ES_CL, "Soplete").addLocale(LangProvider.Locale.ES_EC, "Soplete").addLocale(LangProvider.Locale.ES_ES, "Soplete").addLocale(LangProvider.Locale.ES_MX, "Soplete").addLocale(LangProvider.Locale.ES_UY, "Soplete").addLocale(LangProvider.Locale.ES_VE, "Soplete").addLocale(LangProvider.Locale.FR_FR, "Fabrication du feu").addLocale(LangProvider.Locale.HU, "Firemaking").addLocale(LangProvider.Locale.IT_IT, "Firemaking").addLocale(LangProvider.Locale.JA, "Firemaking").addLocale(LangProvider.Locale.KO_KR, "불피우기").addLocale(LangProvider.Locale.LT_LT, "Firemaking").addLocale(LangProvider.Locale.NL_NL, "Vuurmaken").addLocale(LangProvider.Locale.PL, "Rozpalanie ognia").addLocale(LangProvider.Locale.PT_BR, "Domador do Fogo").addLocale(LangProvider.Locale.RU_RU, "Firemaking").addLocale(LangProvider.Locale.SV_SE, "Firemaking").addLocale(LangProvider.Locale.UK_UA, "Розпалювання вогню").addLocale(LangProvider.Locale.ZH_CN, "炼火").addLocale(LangProvider.Locale.ZH_TW, "煉火").addLocale(LangProvider.Locale.EN_US, "Firemaking").build();

    public static final LangProvider.Translation SKILL_AFKING = LangProvider.Translation.Builder.start("pmmo.afking").addLocale(LangProvider.Locale.HU, "Afking").addLocale(LangProvider.Locale.JA, "Afking").addLocale(LangProvider.Locale.PL, "Nieaktywność").addLocale(LangProvider.Locale.DE_DE, "Afking").addLocale(LangProvider.Locale.ES_AR, "Afkero").addLocale(LangProvider.Locale.ES_CL, "Afkero").addLocale(LangProvider.Locale.ES_EC, "Afkero").addLocale(LangProvider.Locale.ES_ES, "Afkero").addLocale(LangProvider.Locale.ES_MX, "Afkero").addLocale(LangProvider.Locale.ES_UY, "Afkero").addLocale(LangProvider.Locale.ES_VE, "Afkero").addLocale(LangProvider.Locale.FR_FR, "AFKing").addLocale(LangProvider.Locale.HU, "Afking").addLocale(LangProvider.Locale.IT_IT, "Afking").addLocale(LangProvider.Locale.JA, "Afking").addLocale(LangProvider.Locale.KO_KR, "잠수").addLocale(LangProvider.Locale.LT_LT, "Afking").addLocale(LangProvider.Locale.NL_NL, "Afking").addLocale(LangProvider.Locale.PL, "Nieaktywność").addLocale(LangProvider.Locale.PT_BR, "Inativo").addLocale(LangProvider.Locale.RU_RU, "Afking").addLocale(LangProvider.Locale.SV_SE, "Afking").addLocale(LangProvider.Locale.UK_UA, "Відсутність").addLocale(LangProvider.Locale.ZH_CN, "挂机").addLocale(LangProvider.Locale.ZH_TW, "掛機").addLocale(LangProvider.Locale.EN_US, "Afking").build();

    public static final LangProvider.Translation SKILL_TRADING = LangProvider.Translation.Builder.start("pmmo.trading").addLocale(LangProvider.Locale.HU, "Trading").addLocale(LangProvider.Locale.JA, "Trading").addLocale(LangProvider.Locale.PL, "Handel").addLocale(LangProvider.Locale.DE_DE, "Handeln").addLocale(LangProvider.Locale.ES_AR, "Comercio").addLocale(LangProvider.Locale.ES_CL, "Comercio").addLocale(LangProvider.Locale.ES_EC, "Comercio").addLocale(LangProvider.Locale.ES_ES, "Comercio").addLocale(LangProvider.Locale.ES_MX, "Comercio").addLocale(LangProvider.Locale.ES_UY, "Comercio").addLocale(LangProvider.Locale.ES_VE, "Comercio").addLocale(LangProvider.Locale.FR_FR, "Commerce").addLocale(LangProvider.Locale.HU, "Trading").addLocale(LangProvider.Locale.IT_IT, "Trading").addLocale(LangProvider.Locale.JA, "Trading").addLocale(LangProvider.Locale.KO_KR, "거래").addLocale(LangProvider.Locale.LT_LT, "Trading").addLocale(LangProvider.Locale.NL_NL, "Handelen").addLocale(LangProvider.Locale.PL, "Handel").addLocale(LangProvider.Locale.PT_BR, "Mercador").addLocale(LangProvider.Locale.RU_RU, "Trading").addLocale(LangProvider.Locale.SV_SE, "Trading").addLocale(LangProvider.Locale.UK_UA, "Торгівля").addLocale(LangProvider.Locale.ZH_CN, "贸易").addLocale(LangProvider.Locale.ZH_TW, "貿易").addLocale(LangProvider.Locale.EN_US, "Trading").build();

    public static final LangProvider.Translation SKILL_SAILING = LangProvider.Translation.Builder.start("pmmo.sailing").addLocale(LangProvider.Locale.HU, "Sailing").addLocale(LangProvider.Locale.JA, "Sailing").addLocale(LangProvider.Locale.PL, "Żeglarstwo").addLocale(LangProvider.Locale.DE_DE, "Segeln").addLocale(LangProvider.Locale.ES_AR, "Navegación").addLocale(LangProvider.Locale.ES_CL, "Navegación").addLocale(LangProvider.Locale.ES_EC, "Navegación").addLocale(LangProvider.Locale.ES_ES, "Navegación").addLocale(LangProvider.Locale.ES_MX, "Navegación").addLocale(LangProvider.Locale.ES_UY, "Navegación").addLocale(LangProvider.Locale.ES_VE, "Navegación").addLocale(LangProvider.Locale.FR_FR, "Navigation").addLocale(LangProvider.Locale.HU, "Sailing").addLocale(LangProvider.Locale.IT_IT, "Sailing").addLocale(LangProvider.Locale.JA, "Sailing").addLocale(LangProvider.Locale.KO_KR, "항해").addLocale(LangProvider.Locale.LT_LT, "Sailing").addLocale(LangProvider.Locale.NL_NL, "Zeilen").addLocale(LangProvider.Locale.PL, "Żeglarstwo").addLocale(LangProvider.Locale.PT_BR, "Navegador").addLocale(LangProvider.Locale.RU_RU, "Sailing").addLocale(LangProvider.Locale.SV_SE, "Sailing").addLocale(LangProvider.Locale.UK_UA, "Мореплавство").addLocale(LangProvider.Locale.ZH_CN, "航海").addLocale(LangProvider.Locale.ZH_TW, "航海").addLocale(LangProvider.Locale.EN_US, "Sailing").build();

    public static final LangProvider.Translation SKILL_ALCHEMY = LangProvider.Translation.Builder.start("pmmo.alchemy").addLocale(LangProvider.Locale.HU, "Alchemy").addLocale(LangProvider.Locale.JA, "Alchemy").addLocale(LangProvider.Locale.PL, "Alchemia").addLocale(LangProvider.Locale.DE_DE, "Alchemie").addLocale(LangProvider.Locale.ES_AR, "Alquimia").addLocale(LangProvider.Locale.ES_CL, "Alquimia").addLocale(LangProvider.Locale.ES_EC, "Alquimia").addLocale(LangProvider.Locale.ES_ES, "Alquimia").addLocale(LangProvider.Locale.ES_MX, "Alquimia").addLocale(LangProvider.Locale.ES_UY, "Alquimia").addLocale(LangProvider.Locale.ES_VE, "Alquimia").addLocale(LangProvider.Locale.FR_FR, "Alchimie").addLocale(LangProvider.Locale.HU, "Alchemy").addLocale(LangProvider.Locale.IT_IT, "Alchemy").addLocale(LangProvider.Locale.JA, "Alchemy").addLocale(LangProvider.Locale.KO_KR, "연금술").addLocale(LangProvider.Locale.LT_LT, "Alchemy").addLocale(LangProvider.Locale.NL_NL, "Alchemy").addLocale(LangProvider.Locale.PL, "Alchemia").addLocale(LangProvider.Locale.PT_BR, "Alquimia").addLocale(LangProvider.Locale.RU_RU, "Alchemy").addLocale(LangProvider.Locale.SV_SE, "Alchemy").addLocale(LangProvider.Locale.UK_UA, "Алхімія").addLocale(LangProvider.Locale.ZH_CN, "炼金").addLocale(LangProvider.Locale.ZH_TW, "煉金").addLocale(LangProvider.Locale.EN_US, "Alchemy").build();

    public static final LangProvider.Translation SKILL_CONSTRUCTION = LangProvider.Translation.Builder.start("pmmo.construction").addLocale(LangProvider.Locale.HU, "Construction").addLocale(LangProvider.Locale.JA, "Construction").addLocale(LangProvider.Locale.PL, "Budowa").addLocale(LangProvider.Locale.DE_DE, "Konstruktion").addLocale(LangProvider.Locale.ES_AR, "Edificación").addLocale(LangProvider.Locale.ES_CL, "Edificación").addLocale(LangProvider.Locale.ES_EC, "Edificación").addLocale(LangProvider.Locale.ES_ES, "Edificación").addLocale(LangProvider.Locale.ES_MX, "Edificación").addLocale(LangProvider.Locale.ES_UY, "Edificación").addLocale(LangProvider.Locale.ES_VE, "Edificación").addLocale(LangProvider.Locale.FR_FR, "Construction").addLocale(LangProvider.Locale.HU, "Construction").addLocale(LangProvider.Locale.IT_IT, "Construction").addLocale(LangProvider.Locale.JA, "Construction").addLocale(LangProvider.Locale.KO_KR, "건축").addLocale(LangProvider.Locale.LT_LT, "Construction").addLocale(LangProvider.Locale.NL_NL, "Construction").addLocale(LangProvider.Locale.PL, "Budowa").addLocale(LangProvider.Locale.PT_BR, "Construção").addLocale(LangProvider.Locale.RU_RU, "Construction").addLocale(LangProvider.Locale.SV_SE, "Construction").addLocale(LangProvider.Locale.UK_UA, "Будівництво").addLocale(LangProvider.Locale.ZH_CN, "建造").addLocale(LangProvider.Locale.ZH_TW, "建造").addLocale(LangProvider.Locale.EN_US, "Construction").build();

    public static final LangProvider.Translation SKILL_LEATHERWORKING = LangProvider.Translation.Builder.start("pmmo.leatherworking").addLocale(LangProvider.Locale.HU, "Leatherworking").addLocale(LangProvider.Locale.JA, "Leatherworking").addLocale(LangProvider.Locale.PL, "Obróbka skóry").addLocale(LangProvider.Locale.DE_DE, "Lederverarbeitung").addLocale(LangProvider.Locale.ES_AR, "Curtiduría").addLocale(LangProvider.Locale.ES_CL, "Curtiduría").addLocale(LangProvider.Locale.ES_EC, "Curtiduría").addLocale(LangProvider.Locale.ES_ES, "Curtiduría").addLocale(LangProvider.Locale.ES_MX, "Curtiduría").addLocale(LangProvider.Locale.ES_UY, "Curtiduría").addLocale(LangProvider.Locale.ES_VE, "Curtiduría").addLocale(LangProvider.Locale.FR_FR, "Travail du cuir").addLocale(LangProvider.Locale.HU, "Leatherworking").addLocale(LangProvider.Locale.IT_IT, "Leatherworking").addLocale(LangProvider.Locale.JA, "Leatherworking").addLocale(LangProvider.Locale.KO_KR, "가죽마감").addLocale(LangProvider.Locale.LT_LT, "Leatherworking").addLocale(LangProvider.Locale.NL_NL, "Leatherworking").addLocale(LangProvider.Locale.PL, "Obróbka skóry").addLocale(LangProvider.Locale.PT_BR, "Coureiro").addLocale(LangProvider.Locale.RU_RU, "Leatherworking").addLocale(LangProvider.Locale.SV_SE, "Leatherworking").addLocale(LangProvider.Locale.UK_UA, "Обробка шкіри").addLocale(LangProvider.Locale.ZH_CN, "制皮").addLocale(LangProvider.Locale.ZH_TW, "製皮").addLocale(LangProvider.Locale.EN_US, "Leatherworking").build();

    public static final LangProvider.Translation SKILL_EXPLORATION = LangProvider.Translation.Builder.start("pmmo.exploration").addLocale(LangProvider.Locale.HU, "Exploration").addLocale(LangProvider.Locale.JA, "Exploration").addLocale(LangProvider.Locale.PL, "Eksploracja").addLocale(LangProvider.Locale.DE_DE, "Erkundung").addLocale(LangProvider.Locale.ES_AR, "Exploración").addLocale(LangProvider.Locale.ES_CL, "Exploración").addLocale(LangProvider.Locale.ES_EC, "Exploración").addLocale(LangProvider.Locale.ES_ES, "Exploración").addLocale(LangProvider.Locale.ES_MX, "Exploración").addLocale(LangProvider.Locale.ES_UY, "Exploración").addLocale(LangProvider.Locale.ES_VE, "Exploración").addLocale(LangProvider.Locale.FR_FR, "Exploration").addLocale(LangProvider.Locale.HU, "Exploration").addLocale(LangProvider.Locale.IT_IT, "Exploration").addLocale(LangProvider.Locale.JA, "Exploration").addLocale(LangProvider.Locale.KO_KR, "탐험").addLocale(LangProvider.Locale.LT_LT, "Exploration").addLocale(LangProvider.Locale.NL_NL, "Exploration").addLocale(LangProvider.Locale.PL, "Eksploracja").addLocale(LangProvider.Locale.PT_BR, "Exploração").addLocale(LangProvider.Locale.RU_RU, "Exploration").addLocale(LangProvider.Locale.SV_SE, "Exploration").addLocale(LangProvider.Locale.UK_UA, "Розвідка").addLocale(LangProvider.Locale.ZH_CN, "Exploration").addLocale(LangProvider.Locale.ZH_TW, "探索").addLocale(LangProvider.Locale.EN_US, "Exploration").build();

    public static final LangProvider.Translation SKILL_CHARISMA = LangProvider.Translation.Builder.start("pmmo.charisma").addLocale(LangProvider.Locale.EN_US, "Charisma").build();

    public static final LangProvider.Translation ENUM_ANVIL_REPAIR = LangProvider.Translation.Builder.start("pmmo.enum.ANVIL_REPAIR").addLocale(LangProvider.Locale.EN_US, "Anvil Repair").build();

    public static final LangProvider.Translation ENUM_BLOCK_BREAK = LangProvider.Translation.Builder.start("pmmo.enum.BLOCK_BREAK").addLocale(LangProvider.Locale.EN_US, "Break Block").build();

    public static final LangProvider.Translation ENUM_BREAK_SPEED = LangProvider.Translation.Builder.start("pmmo.enum.BREAK_SPEED").addLocale(LangProvider.Locale.EN_US, "Break Speed").build();

    public static final LangProvider.Translation ENUM_BLOCK_PLACE = LangProvider.Translation.Builder.start("pmmo.enum.BLOCK_PLACE").addLocale(LangProvider.Locale.EN_US, "Place Block").build();

    public static final LangProvider.Translation ENUM_BREATH_CHANGE = LangProvider.Translation.Builder.start("pmmo.enum.BREATH_CHANGE").addLocale(LangProvider.Locale.EN_US, "Breath Change").build();

    public static final LangProvider.Translation ENUM_BREED = LangProvider.Translation.Builder.start("pmmo.enum.BREED").addLocale(LangProvider.Locale.EN_US, "Breed").build();

    public static final LangProvider.Translation ENUM_BREW = LangProvider.Translation.Builder.start("pmmo.enum.BREW").addLocale(LangProvider.Locale.EN_US, "Brew").build();

    public static final LangProvider.Translation ENUM_CRAFT = LangProvider.Translation.Builder.start("pmmo.enum.CRAFT").addLocale(LangProvider.Locale.EN_US, "Craft").build();

    public static final LangProvider.Translation ENUM_CONSUME = LangProvider.Translation.Builder.start("pmmo.enum.CONSUME").addLocale(LangProvider.Locale.EN_US, "Eat/Drink").build();

    public static final LangProvider.Translation ENUM_CROUCH = LangProvider.Translation.Builder.start("pmmo.enum.CROUCH").addLocale(LangProvider.Locale.EN_US, "Crouch").build();

    public static final LangProvider.Translation ENUM_RECEIVE_DAMAGE = LangProvider.Translation.Builder.start("pmmo.enum.RECEIVE_DAMAGE").addLocale(LangProvider.Locale.EN_US, "Receive Damage (Unspecified)").build();

    public static final LangProvider.Translation ENUM_FROM_MOBS = LangProvider.Translation.Builder.start("pmmo.enum.FROM_MOBS").addLocale(LangProvider.Locale.EN_US, "Receive Mob Damage").build();

    public static final LangProvider.Translation ENUM_FROM_PLAYERS = LangProvider.Translation.Builder.start("pmmo.enum.FROM_PLAYERS").addLocale(LangProvider.Locale.EN_US, "Receive Player Damage").build();

    public static final LangProvider.Translation ENUM_FROM_ANIMALS = LangProvider.Translation.Builder.start("pmmo.enum.FROM_ANIMALS").addLocale(LangProvider.Locale.EN_US, "Receive Animal Damage").build();

    public static final LangProvider.Translation ENUM_FROM_PROJECTILES = LangProvider.Translation.Builder.start("pmmo.enum.FROM_PROJECTILES").addLocale(LangProvider.Locale.EN_US, "Receive Projectile Damage").build();

    public static final LangProvider.Translation ENUM_FROM_MAGIC = LangProvider.Translation.Builder.start("pmmo.enum.FROM_MAGIC").addLocale(LangProvider.Locale.EN_US, "Receive Magic Damage").build();

    public static final LangProvider.Translation ENUM_FROM_ENVIRONMENT = LangProvider.Translation.Builder.start("pmmo.enum.FROM_ENVIRONMENT").addLocale(LangProvider.Locale.EN_US, "Receive Environmental Damage").build();

    public static final LangProvider.Translation ENUM_FROM_IMPACT = LangProvider.Translation.Builder.start("pmmo.enum.FROM_IMPACT").addLocale(LangProvider.Locale.EN_US, "Receive Impact Damage").build();

    public static final LangProvider.Translation ENUM_DEAL_MELEE_DAMAGE = LangProvider.Translation.Builder.start("pmmo.enum.DEAL_MELEE_DAMAGE").addLocale(LangProvider.Locale.EN_US, "Deal Melee Damage (Unspecified)").build();

    public static final LangProvider.Translation ENUM_MELEE_TO_MOBS = LangProvider.Translation.Builder.start("pmmo.enum.MELEE_TO_MOBS").addLocale(LangProvider.Locale.EN_US, "Deal Melee Damage to Mobs").build();

    public static final LangProvider.Translation ENUM_MELEE_TO_PLAYERS = LangProvider.Translation.Builder.start("pmmo.enum.MELEE_TO_PLAYERS").addLocale(LangProvider.Locale.EN_US, "Deal Melee Damage to Players").build();

    public static final LangProvider.Translation ENUM_MELEE_TO_ANIMALS = LangProvider.Translation.Builder.start("pmmo.enum.MELEE_TO_ANIMALS").addLocale(LangProvider.Locale.EN_US, "Deal Melee Damage to Animals").build();

    public static final LangProvider.Translation ENUM_DEAL_RANGED_DAMAGE = LangProvider.Translation.Builder.start("pmmo.enum.DEAL_RANGED_DAMAGE").addLocale(LangProvider.Locale.EN_US, "Deal Ranged Damage (Unspecified)").build();

    public static final LangProvider.Translation ENUM_RANGED_TO_MOBS = LangProvider.Translation.Builder.start("pmmo.enum.RANGED_TO_MOBS").addLocale(LangProvider.Locale.EN_US, "Deal Ranged Damage to Mobs").build();

    public static final LangProvider.Translation ENUM_RANGED_TO_PLAYERS = LangProvider.Translation.Builder.start("pmmo.enum.RANGED_TO_PLAYERS").addLocale(LangProvider.Locale.EN_US, "Deal Ranged Damage to Players").build();

    public static final LangProvider.Translation ENUM_RANGED_TO_ANIMALS = LangProvider.Translation.Builder.start("pmmo.enum.RANGED_TO_ANIMALS").addLocale(LangProvider.Locale.EN_US, "Deal Ranged Damage to Animals").build();

    public static final LangProvider.Translation ENUM_DEATH = LangProvider.Translation.Builder.start("pmmo.enum.DEATH").addLocale(LangProvider.Locale.EN_US, "Death").build();

    public static final LangProvider.Translation ENUM_ENCHANT = LangProvider.Translation.Builder.start("pmmo.enum.ENCHANT").addLocale(LangProvider.Locale.EN_US, "Enchant").build();

    public static final LangProvider.Translation ENUM_FISH = LangProvider.Translation.Builder.start("pmmo.enum.FISH").addLocale(LangProvider.Locale.EN_US, "Fish").build();

    public static final LangProvider.Translation ENUM_SMELT = LangProvider.Translation.Builder.start("pmmo.enum.SMELT").addLocale(LangProvider.Locale.EN_US, "Smelt/Cook").build();

    public static final LangProvider.Translation ENUM_GROW = LangProvider.Translation.Builder.start("pmmo.enum.GROW").addLocale(LangProvider.Locale.EN_US, "Grow").build();

    public static final LangProvider.Translation ENUM_HEALTH_CHANGE = LangProvider.Translation.Builder.start("pmmo.enum.HEALTH_CHANGE").addLocale(LangProvider.Locale.EN_US, "Health Change").build();

    public static final LangProvider.Translation ENUM_JUMP = LangProvider.Translation.Builder.start("pmmo.enum.JUMP").addLocale(LangProvider.Locale.EN_US, "Jump").build();

    public static final LangProvider.Translation ENUM_SPRINT_JUMP = LangProvider.Translation.Builder.start("pmmo.enum.SPRINT_JUMP").addLocale(LangProvider.Locale.EN_US, "Sprint Jump").build();

    public static final LangProvider.Translation ENUM_CROUCH_JUMP = LangProvider.Translation.Builder.start("pmmo.enum.CROUCH_JUMP").addLocale(LangProvider.Locale.EN_US, "Crouch Jump").build();

    public static final LangProvider.Translation ENUM_WORLD_CONNECT = LangProvider.Translation.Builder.start("pmmo.enum.WORLD_CONNECT").addLocale(LangProvider.Locale.EN_US, "World Connect").build();

    public static final LangProvider.Translation ENUM_WORLD_DISCONNECT = LangProvider.Translation.Builder.start("pmmo.enum.WORLD_DISCONNECT").addLocale(LangProvider.Locale.EN_US, "World Disconnect").build();

    public static final LangProvider.Translation ENUM_HIT_BLOCK = LangProvider.Translation.Builder.start("pmmo.enum.HIT_BLOCK").addLocale(LangProvider.Locale.EN_US, "Hit Block").build();

    public static final LangProvider.Translation ENUM_ACTIVATE_BLOCK = LangProvider.Translation.Builder.start("pmmo.enum.ACTIVATE_BLOCK").addLocale(LangProvider.Locale.EN_US, "Activate Block").build();

    public static final LangProvider.Translation ENUM_ACTIVATE_ITEM = LangProvider.Translation.Builder.start("pmmo.enum.ACTIVATE_ITEM").addLocale(LangProvider.Locale.EN_US, "Activate Item").build();

    public static final LangProvider.Translation ENUM_ENTITY = LangProvider.Translation.Builder.start("pmmo.enum.ENTITY").addLocale(LangProvider.Locale.EN_US, "Interact with Entity").build();

    public static final LangProvider.Translation ENUM_EFFECT = LangProvider.Translation.Builder.start("pmmo.enum.EFFECT").addLocale(LangProvider.Locale.EN_US, "Active Effect").build();

    public static final LangProvider.Translation ENUM_RESPAWN = LangProvider.Translation.Builder.start("pmmo.enum.RESPAWN").addLocale(LangProvider.Locale.EN_US, "Respawn").build();

    public static final LangProvider.Translation ENUM_RIDING = LangProvider.Translation.Builder.start("pmmo.enum.RIDING").addLocale(LangProvider.Locale.EN_US, "Riding").build();

    public static final LangProvider.Translation ENUM_SHIELD_BLOCK = LangProvider.Translation.Builder.start("pmmo.enum.SHIELD_BLOCK").addLocale(LangProvider.Locale.EN_US, "Block with Shield").build();

    public static final LangProvider.Translation ENUM_SKILL_UP = LangProvider.Translation.Builder.start("pmmo.enum.SKILL_UP").addLocale(LangProvider.Locale.EN_US, "Level Up").build();

    public static final LangProvider.Translation ENUM_SLEEP = LangProvider.Translation.Builder.start("pmmo.enum.SLEEP").addLocale(LangProvider.Locale.EN_US, "Sleep").build();

    public static final LangProvider.Translation ENUM_SPRINTING = LangProvider.Translation.Builder.start("pmmo.enum.SPRINTING").addLocale(LangProvider.Locale.EN_US, "Sprinting").build();

    public static final LangProvider.Translation ENUM_SUBMERGED = LangProvider.Translation.Builder.start("pmmo.enum.SUBMERGED").addLocale(LangProvider.Locale.EN_US, "Submerged").build();

    public static final LangProvider.Translation ENUM_SWIMMING = LangProvider.Translation.Builder.start("pmmo.enum.SWIMMING").addLocale(LangProvider.Locale.EN_US, "Swimming (above surface)").build();

    public static final LangProvider.Translation ENUM_DIVING = LangProvider.Translation.Builder.start("pmmo.enum.DIVING").addLocale(LangProvider.Locale.EN_US, "Diving").build();

    public static final LangProvider.Translation ENUM_SURFACING = LangProvider.Translation.Builder.start("pmmo.enum.SURFACING").addLocale(LangProvider.Locale.EN_US, "Surfacing").build();

    public static final LangProvider.Translation ENUM_SWIM_SPRINTING = LangProvider.Translation.Builder.start("pmmo.enum.SWIM_SPRINTING").addLocale(LangProvider.Locale.EN_US, "Fast Swimming").build();

    public static final LangProvider.Translation ENUM_TAMING = LangProvider.Translation.Builder.start("pmmo.enum.TAMING").addLocale(LangProvider.Locale.EN_US, "Taming").build();

    public static final LangProvider.Translation ENUM_VEIN_MINE = LangProvider.Translation.Builder.start("pmmo.enum.VEIN_MINE").addLocale(LangProvider.Locale.EN_US, "Vein Mining").build();

    public static final LangProvider.Translation ENUM_DISABLE_PERK = LangProvider.Translation.Builder.start("pmmo.enum.DISABLE_PERK").addLocale(LangProvider.Locale.EN_US, "Disable Perk").build();

    public static final LangProvider.Translation ENUM_WEAR = LangProvider.Translation.Builder.start("pmmo.enum.WEAR").addLocale(LangProvider.Locale.EN_US, "Wear Item").build();

    public static final LangProvider.Translation ENUM_USE_ENCHANTMENT = LangProvider.Translation.Builder.start("pmmo.enum.USE_ENCHANTMENT").addLocale(LangProvider.Locale.EN_US, "Use Enchantment").build();

    public static final LangProvider.Translation ENUM_TOOL = LangProvider.Translation.Builder.start("pmmo.enum.TOOL").addLocale(LangProvider.Locale.EN_US, "Use as Tool").build();

    public static final LangProvider.Translation ENUM_WEAPON = LangProvider.Translation.Builder.start("pmmo.enum.WEAPON").addLocale(LangProvider.Locale.EN_US, "Use as Weapon").build();

    public static final LangProvider.Translation ENUM_USE = LangProvider.Translation.Builder.start("pmmo.enum.USE").addLocale(LangProvider.Locale.EN_US, "Activate Item Ability").build();

    public static final LangProvider.Translation ENUM_PLACE = LangProvider.Translation.Builder.start("pmmo.enum.PLACE").addLocale(LangProvider.Locale.EN_US, "Place Block").build();

    public static final LangProvider.Translation ENUM_BREAK = LangProvider.Translation.Builder.start("pmmo.enum.BREAK").addLocale(LangProvider.Locale.EN_US, "Break Block").build();

    public static final LangProvider.Translation ENUM_KILL = LangProvider.Translation.Builder.start("pmmo.enum.KILL").addLocale(LangProvider.Locale.EN_US, "Kill Entity").build();

    public static final LangProvider.Translation ENUM_TRAVEL = LangProvider.Translation.Builder.start("pmmo.enum.TRAVEL").addLocale(LangProvider.Locale.EN_US, "Travel to").build();

    public static final LangProvider.Translation ENUM_RIDE = LangProvider.Translation.Builder.start("pmmo.enum.RIDE").addLocale(LangProvider.Locale.EN_US, "Ride/Drive").build();

    public static final LangProvider.Translation ENUM_TAME = LangProvider.Translation.Builder.start("pmmo.enum.TAME").addLocale(LangProvider.Locale.EN_US, "Tame Animal").build();

    public static final LangProvider.Translation ENUM_INTERACT = LangProvider.Translation.Builder.start("pmmo.enum.INTERACT").addLocale(LangProvider.Locale.EN_US, "Interact with Block").build();

    public static final LangProvider.Translation ENUM_ENTITY_INTERACT = LangProvider.Translation.Builder.start("pmmo.enum.ENTITY_INTERACT").addLocale(LangProvider.Locale.EN_US, "Interact with Entity").build();

    public static final LangProvider.Translation ENUM_BIOME = LangProvider.Translation.Builder.start("pmmo.enum.BIOME").addLocale(LangProvider.Locale.EN_US, "Biome").build();

    public static final LangProvider.Translation KEYBIND_CATEGORY = LangProvider.Translation.Builder.start("category.pmmo").addLocale(LangProvider.Locale.HU, "Project MMO").addLocale(LangProvider.Locale.JA, "Project MMO").addLocale(LangProvider.Locale.PL, "Project MMO").addLocale(LangProvider.Locale.DE_DE, "Project MMO").addLocale(LangProvider.Locale.ES_AR, "Project MMO").addLocale(LangProvider.Locale.ES_CL, "Project MMO").addLocale(LangProvider.Locale.ES_EC, "Project MMO").addLocale(LangProvider.Locale.ES_ES, "Project MMO").addLocale(LangProvider.Locale.ES_MX, "Project MMO").addLocale(LangProvider.Locale.ES_UY, "Project MMO").addLocale(LangProvider.Locale.ES_VE, "Project MMO").addLocale(LangProvider.Locale.FR_FR, "Project MMO").addLocale(LangProvider.Locale.HU, "Project MMO").addLocale(LangProvider.Locale.IT_IT, "Project MMO").addLocale(LangProvider.Locale.JA, "Project MMO").addLocale(LangProvider.Locale.KO_KR, "프로젝트 MMO").addLocale(LangProvider.Locale.LT_LT, "Projektas MMO").addLocale(LangProvider.Locale.NL_NL, "Project MMO").addLocale(LangProvider.Locale.PL, "Project MMO").addLocale(LangProvider.Locale.PT_BR, "Projeto MMO").addLocale(LangProvider.Locale.RU_RU, "Project MMO").addLocale(LangProvider.Locale.SV_SE, "Projekt MMO").addLocale(LangProvider.Locale.UK_UA, "Project MMO").addLocale(LangProvider.Locale.ZH_CN, "Project MMO").addLocale(LangProvider.Locale.ZH_TW, "Project MMO").addLocale(LangProvider.Locale.EN_US, "Project MMO").build();

    public static final LangProvider.Translation KEYBIND_SHOWVEIN = LangProvider.Translation.Builder.start("key.pmmo.showVein").addLocale(LangProvider.Locale.EN_US, "Toggle Vein Gauge").build();

    public static final LangProvider.Translation KEYBIND_ADDVEIN = LangProvider.Translation.Builder.start("key.pmmo.addVein").addLocale(LangProvider.Locale.EN_US, "Increase Vein Size").build();

    public static final LangProvider.Translation KEYBIND_SUBVEIN = LangProvider.Translation.Builder.start("key.pmmo.subVein").addLocale(LangProvider.Locale.EN_US, "Decrease Vein Size").build();

    public static final LangProvider.Translation KEYBIND_VEINCYCLE = LangProvider.Translation.Builder.start("key.pmmo.cyclevein").addLocale(LangProvider.Locale.EN_US, "Cycle Vein Mode").build();

    public static final LangProvider.Translation KEYBIND_SHOWLIST = LangProvider.Translation.Builder.start("key.pmmo.showList").addLocale(LangProvider.Locale.EN_US, "Toggle Skill List").build();

    public static final LangProvider.Translation KEYBIND_VEIN = LangProvider.Translation.Builder.start("key.pmmo.vein").addLocale(LangProvider.Locale.HU, "Vein Mine").addLocale(LangProvider.Locale.JA, "鉱脈鉱山").addLocale(LangProvider.Locale.PL, "Wydobywanie żył").addLocale(LangProvider.Locale.DE_DE, "Ader Mine").addLocale(LangProvider.Locale.ES_AR, "Rasgar").addLocale(LangProvider.Locale.ES_CL, "Rasgar").addLocale(LangProvider.Locale.ES_EC, "Rasgar").addLocale(LangProvider.Locale.ES_ES, "Rasgar").addLocale(LangProvider.Locale.ES_MX, "Rasgar").addLocale(LangProvider.Locale.ES_UY, "Rasgar").addLocale(LangProvider.Locale.ES_VE, "Rasgar").addLocale(LangProvider.Locale.FR_FR, "Minage en veine").addLocale(LangProvider.Locale.HU, "Vein Mine").addLocale(LangProvider.Locale.IT_IT, "Vein Mine").addLocale(LangProvider.Locale.JA, "鉱脈鉱山").addLocale(LangProvider.Locale.KO_KR, "채광 피로도").addLocale(LangProvider.Locale.LT_LT, "Vein Mine").addLocale(LangProvider.Locale.NL_NL, "Vein Mine").addLocale(LangProvider.Locale.PL, "Wydobywanie żył").addLocale(LangProvider.Locale.PT_BR, "minerador de veio").addLocale(LangProvider.Locale.RU_RU, "Вскопать жилу").addLocale(LangProvider.Locale.SV_SE, "ven mine").addLocale(LangProvider.Locale.UK_UA, "Вскопати жилу").addLocale(LangProvider.Locale.ZH_CN, "连锁挖矿").addLocale(LangProvider.Locale.ZH_TW, "連鎖挖礦").addLocale(LangProvider.Locale.EN_US, "Vein Mine Marker").build();

    public static final LangProvider.Translation KEYBIND_OPENMENU = LangProvider.Translation.Builder.start("key.pmmo.openMenu").addLocale(LangProvider.Locale.EN_US, "Open Glossary").build();

    public static final LangProvider.Translation WELCOME_TEXT = LangProvider.Translation.Builder.start("pmmo.welcomeText").addLocale(LangProvider.Locale.EN_US, "Welcome! Project MMO is more fun with datapacks. download one %s").build();

    public static final LangProvider.Translation CLICK_ME = LangProvider.Translation.Builder.start("pmmo.clickMe").addLocale(LangProvider.Locale.EN_US, "HERE").build();

    public static final LangProvider.Translation VEIN_BLACKLIST = LangProvider.Translation.Builder.start("pmmo.veinBlacklist").addLocale(LangProvider.Locale.EN_US, "Blacklisted blocks from Veining").build();

    public static final LangProvider.Translation VEIN_SHAPE = LangProvider.Translation.Builder.start("pmmo.veinshape").addLocale(LangProvider.Locale.EN_US, "Vein Shape Set To: %s").build();

    public static final LangProvider.Translation LEVEL_UP_HEADER = LangProvider.Translation.Builder.start("pmmo.levelup").addLocale(LangProvider.Locale.EN_US, "You have reached level %s in %s and unlocked:").build();

    public static final LangProvider.Translation REQ_WEAR = LangProvider.Translation.Builder.start("pmmo.toWear").addLocale(LangProvider.Locale.EN_US, "To Wear").build();

    public static final LangProvider.Translation REQ_TOOL = LangProvider.Translation.Builder.start("pmmo.tool").addLocale(LangProvider.Locale.HU, "Tool").addLocale(LangProvider.Locale.JA, "Tool").addLocale(LangProvider.Locale.PL, "Narzędzie").addLocale(LangProvider.Locale.DE_DE, "Werkzeug").addLocale(LangProvider.Locale.ES_AR, "Herramienta").addLocale(LangProvider.Locale.ES_CL, "Herramienta").addLocale(LangProvider.Locale.ES_EC, "Herramienta").addLocale(LangProvider.Locale.ES_ES, "Herramienta").addLocale(LangProvider.Locale.ES_MX, "Herramienta").addLocale(LangProvider.Locale.ES_UY, "Herramienta").addLocale(LangProvider.Locale.ES_VE, "Herramienta").addLocale(LangProvider.Locale.FR_FR, "Outils").addLocale(LangProvider.Locale.HU, "Tool").addLocale(LangProvider.Locale.IT_IT, "Attrezzo").addLocale(LangProvider.Locale.JA, "Tool").addLocale(LangProvider.Locale.KO_KR, "도구").addLocale(LangProvider.Locale.LT_LT, "Įrankis").addLocale(LangProvider.Locale.NL_NL, "Gereedschap").addLocale(LangProvider.Locale.PL, "Narzędzie").addLocale(LangProvider.Locale.PT_BR, "Ferramenta").addLocale(LangProvider.Locale.RU_RU, "Инструмент").addLocale(LangProvider.Locale.SV_SE, "Tool").addLocale(LangProvider.Locale.UK_UA, "Інструмент").addLocale(LangProvider.Locale.ZH_CN, "工具").addLocale(LangProvider.Locale.ZH_TW, "工具").addLocale(LangProvider.Locale.EN_US, "Tool").build();

    public static final LangProvider.Translation REQ_WEAPON = LangProvider.Translation.Builder.start("pmmo.weapon").addLocale(LangProvider.Locale.HU, "Weapon").addLocale(LangProvider.Locale.JA, "Weapon").addLocale(LangProvider.Locale.PL, "Broń").addLocale(LangProvider.Locale.DE_DE, "Waffe").addLocale(LangProvider.Locale.ES_AR, "Arma").addLocale(LangProvider.Locale.ES_CL, "Arma").addLocale(LangProvider.Locale.ES_EC, "Arma").addLocale(LangProvider.Locale.ES_ES, "Arma").addLocale(LangProvider.Locale.ES_MX, "Arma").addLocale(LangProvider.Locale.ES_UY, "Arma").addLocale(LangProvider.Locale.ES_VE, "Arma").addLocale(LangProvider.Locale.FR_FR, "Arme").addLocale(LangProvider.Locale.HU, "Weapon").addLocale(LangProvider.Locale.IT_IT, "Arma").addLocale(LangProvider.Locale.JA, "Weapon").addLocale(LangProvider.Locale.KO_KR, "무기").addLocale(LangProvider.Locale.LT_LT, "Ginklas").addLocale(LangProvider.Locale.NL_NL, "Wapen").addLocale(LangProvider.Locale.PL, "Broń").addLocale(LangProvider.Locale.PT_BR, "Arma").addLocale(LangProvider.Locale.RU_RU, "Оружие").addLocale(LangProvider.Locale.SV_SE, "Weapon").addLocale(LangProvider.Locale.UK_UA, "Зброя").addLocale(LangProvider.Locale.ZH_CN, "武器").addLocale(LangProvider.Locale.ZH_TW, "武器").addLocale(LangProvider.Locale.EN_US, "Weapon").build();

    public static final LangProvider.Translation REQ_USE = LangProvider.Translation.Builder.start("pmmo.use").addLocale(LangProvider.Locale.HU, "Use").addLocale(LangProvider.Locale.JA, "Use").addLocale(LangProvider.Locale.PL, "Wykorzystaj").addLocale(LangProvider.Locale.DE_DE, "Benutzen").addLocale(LangProvider.Locale.ES_AR, "Usar").addLocale(LangProvider.Locale.ES_CL, "Usar").addLocale(LangProvider.Locale.ES_EC, "Usar").addLocale(LangProvider.Locale.ES_ES, "Usar").addLocale(LangProvider.Locale.ES_MX, "Usar").addLocale(LangProvider.Locale.ES_UY, "Usar").addLocale(LangProvider.Locale.ES_VE, "Usar").addLocale(LangProvider.Locale.FR_FR, "Utiliser").addLocale(LangProvider.Locale.HU, "Use").addLocale(LangProvider.Locale.IT_IT, "Usa").addLocale(LangProvider.Locale.JA, "Use").addLocale(LangProvider.Locale.KO_KR, "사용").addLocale(LangProvider.Locale.LT_LT, "Naudoti").addLocale(LangProvider.Locale.NL_NL, "Gebruiken").addLocale(LangProvider.Locale.PL, "Wykorzystaj").addLocale(LangProvider.Locale.PT_BR, "Usar").addLocale(LangProvider.Locale.RU_RU, "Использовать").addLocale(LangProvider.Locale.SV_SE, "Use").addLocale(LangProvider.Locale.UK_UA, "Використати").addLocale(LangProvider.Locale.ZH_CN, "使用需求等级").addLocale(LangProvider.Locale.ZH_TW, "使用需求等級").addLocale(LangProvider.Locale.EN_US, "Use").build();

    public static final LangProvider.Translation REQ_PLACE = LangProvider.Translation.Builder.start("pmmo.place").addLocale(LangProvider.Locale.HU, "Place Down").addLocale(LangProvider.Locale.JA, "Place Down").addLocale(LangProvider.Locale.PL, "Postawić").addLocale(LangProvider.Locale.DE_DE, "Platzieren").addLocale(LangProvider.Locale.ES_AR, "Colocar").addLocale(LangProvider.Locale.ES_CL, "Colocar").addLocale(LangProvider.Locale.ES_EC, "Colocar").addLocale(LangProvider.Locale.ES_ES, "Colocar").addLocale(LangProvider.Locale.ES_MX, "Colocar").addLocale(LangProvider.Locale.ES_UY, "Colocar").addLocale(LangProvider.Locale.ES_VE, "Colocar").addLocale(LangProvider.Locale.FR_FR, "Placer").addLocale(LangProvider.Locale.HU, "Place Down").addLocale(LangProvider.Locale.IT_IT, "Piazza Giu").addLocale(LangProvider.Locale.JA, "Place Down").addLocale(LangProvider.Locale.KO_KR, "놓기").addLocale(LangProvider.Locale.LT_LT, "Statyti").addLocale(LangProvider.Locale.NL_NL, "Plaatsen").addLocale(LangProvider.Locale.PL, "Postawić").addLocale(LangProvider.Locale.PT_BR, "Colocar").addLocale(LangProvider.Locale.RU_RU, "Разместить").addLocale(LangProvider.Locale.SV_SE, "Place Down").addLocale(LangProvider.Locale.UK_UA, "Розмістити").addLocale(LangProvider.Locale.ZH_CN, "放置需求等级").addLocale(LangProvider.Locale.ZH_TW, "放置需求等級").addLocale(LangProvider.Locale.EN_US, "To Place").build();

    public static final LangProvider.Translation REQ_ENCHANT = LangProvider.Translation.Builder.start("pmmo.use_enchant").addLocale(LangProvider.Locale.EN_US, "Use Enchantment").build();

    public static final LangProvider.Translation REQ_INTERACT = LangProvider.Translation.Builder.start("pmmo.req_interact").addLocale(LangProvider.Locale.EN_US, "Interact with Block").build();

    public static final LangProvider.Translation REQ_BREAK = LangProvider.Translation.Builder.start("pmmo.break").addLocale(LangProvider.Locale.HU, "Break").addLocale(LangProvider.Locale.JA, "Break").addLocale(LangProvider.Locale.PL, "Rozwalić").addLocale(LangProvider.Locale.DE_DE, "Zerstören").addLocale(LangProvider.Locale.ES_AR, "Quebrar").addLocale(LangProvider.Locale.ES_CL, "Quebrar").addLocale(LangProvider.Locale.ES_EC, "Quebrar").addLocale(LangProvider.Locale.ES_ES, "Quebrar").addLocale(LangProvider.Locale.ES_MX, "Quebrar").addLocale(LangProvider.Locale.ES_UY, "Quebrar").addLocale(LangProvider.Locale.ES_VE, "Quebrar").addLocale(LangProvider.Locale.FR_FR, "Casser").addLocale(LangProvider.Locale.HU, "Break").addLocale(LangProvider.Locale.IT_IT, "Rompi").addLocale(LangProvider.Locale.JA, "Break").addLocale(LangProvider.Locale.KO_KR, "부수기").addLocale(LangProvider.Locale.LT_LT, "Nugriauti").addLocale(LangProvider.Locale.NL_NL, "Breken").addLocale(LangProvider.Locale.PL, "Rozwalić").addLocale(LangProvider.Locale.PT_BR, "Quebrar").addLocale(LangProvider.Locale.RU_RU, "Сломать").addLocale(LangProvider.Locale.SV_SE, "Break").addLocale(LangProvider.Locale.UK_UA, "Зламати").addLocale(LangProvider.Locale.ZH_CN, "破坏需求等级").addLocale(LangProvider.Locale.ZH_TW, "破壞需求等級").addLocale(LangProvider.Locale.EN_US, "To Break").build();

    public static final LangProvider.Translation XP_VALUE_ANVIL = LangProvider.Translation.Builder.start("pmmo.xpValueAnvil").addLocale(LangProvider.Locale.EN_US, "Anvil Repair Xp Value").build();

    public static final LangProvider.Translation XP_VALUE_BREAK = LangProvider.Translation.Builder.start("pmmo.xpValueBreak").addLocale(LangProvider.Locale.EN_US, "Break Xp Value").build();

    public static final LangProvider.Translation XP_VALUE_CRAFT = LangProvider.Translation.Builder.start("pmmo.xpValueCraft").addLocale(LangProvider.Locale.EN_US, "Craft Xp Value").build();

    public static final LangProvider.Translation XP_VALUE_CONSUME = LangProvider.Translation.Builder.start("pmmo.xpValueConsume").addLocale(LangProvider.Locale.EN_US, "Consume Xp Value").build();

    public static final LangProvider.Translation XP_VALUE_DEAL_DAMAGE = LangProvider.Translation.Builder.start("pmmo.xpValueDealDamage").addLocale(LangProvider.Locale.EN_US, "Deal Damage with Item Xp").build();

    public static final LangProvider.Translation XP_VALUE_ENCHANT = LangProvider.Translation.Builder.start("pmmo.xpValueEnchant").addLocale(LangProvider.Locale.EN_US, "Enchant Xp Value").build();

    public static final LangProvider.Translation XP_VALUE_FISH = LangProvider.Translation.Builder.start("pmmo.xpValueFish").addLocale(LangProvider.Locale.EN_US, "Fishing Loot Xp Value").build();

    public static final LangProvider.Translation XP_VALUE_SMELT = LangProvider.Translation.Builder.start("pmmo.xpValueSmelt").addLocale(LangProvider.Locale.EN_US, "Smelt Xp Value").build();

    public static final LangProvider.Translation XP_VALUE_TRADE_GIVE = LangProvider.Translation.Builder.start("pmmo.xpValueTradeGive").addLocale(LangProvider.Locale.EN_US, "Given in Trade Xp Value").build();

    public static final LangProvider.Translation XP_VALUE_TRADE_GET = LangProvider.Translation.Builder.start("pmmo.xpValueTradeGet").addLocale(LangProvider.Locale.EN_US, "Received in Trade Xp Value").build();

    public static final LangProvider.Translation XP_VALUE_HIT_BLOCK = LangProvider.Translation.Builder.start("pmmo.xpValueHitBlock").addLocale(LangProvider.Locale.EN_US, "Hit Block Xp Value").build();

    public static final LangProvider.Translation XP_VALUE_ACTIVATE_BLOCK = LangProvider.Translation.Builder.start("pmmo.xpValueActivateBlock").addLocale(LangProvider.Locale.EN_US, "Activate Block Xp Value").build();

    public static final LangProvider.Translation XP_VALUE_USE = LangProvider.Translation.Builder.start("pmmo.xpValueUse").addLocale(LangProvider.Locale.EN_US, "Use Item Xp Value").build();

    public static final LangProvider.Translation XP_VALUE_BREW = LangProvider.Translation.Builder.start("pmmo.xpValueBrew").addLocale(LangProvider.Locale.EN_US, "Brew Xp Value").build();

    public static final LangProvider.Translation XP_VALUE_GROW = LangProvider.Translation.Builder.start("pmmo.xpValueGrow").addLocale(LangProvider.Locale.EN_US, "Grow Xp Value").build();

    public static final LangProvider.Translation XP_VALUE_PLACE = LangProvider.Translation.Builder.start("pmmo.xpValuePlace").addLocale(LangProvider.Locale.EN_US, "Place Xp Value").build();

    public static final LangProvider.Translation BOOST_HELD = LangProvider.Translation.Builder.start("pmmo.itemXpBoostHeld").addLocale(LangProvider.Locale.EN_US, "Xp Boost In Hand").build();

    public static final LangProvider.Translation BOOST_WORN = LangProvider.Translation.Builder.start("pmmo.itemXpBoostWorn").addLocale(LangProvider.Locale.EN_US, "Xp Boost Worn").build();

    public static final LangProvider.Translation VEIN_TOOLTIP = LangProvider.Translation.Builder.start("pmmo.veintooltip").addLocale(LangProvider.Locale.EN_US, "Vein Mining").build();

    public static final LangProvider.Translation VEIN_DATA = LangProvider.Translation.Builder.start("pmmo.veindata").addLocale(LangProvider.Locale.EN_US, "Charge Cap %1$s, recharges %2$s/s").build();

    public static final LangProvider.Translation VEIN_BREAK = LangProvider.Translation.Builder.start("pmmo.veinbreak").addLocale(LangProvider.Locale.EN_US, "Cost to break as block: %s").build();

    public static final LangProvider.Translation OPEN_GLOSSARY = LangProvider.Translation.Builder.start("pmmo.gui.stat_screen.open_glossary").addLocale(LangProvider.Locale.EN_US, "Open Glossary").build();

    public static final LangProvider.Translation EVENT_HEADER = LangProvider.Translation.Builder.start("pmmo.event_header").addLocale(LangProvider.Locale.EN_US, "XP Award Events").build();

    public static final LangProvider.Translation REQ_HEADER = LangProvider.Translation.Builder.start("pmmo.req_header").addLocale(LangProvider.Locale.EN_US, "Requirements").build();

    public static final LangProvider.Translation REQ_EFFECTS_HEADER = LangProvider.Translation.Builder.start("pmmo.req_effects_header").addLocale(LangProvider.Locale.EN_US, "Negative Effects for unmet Reqs").build();

    public static final LangProvider.Translation MODIFIER_HEADER = LangProvider.Translation.Builder.start("pmmo.modifier_header").addLocale(LangProvider.Locale.EN_US, "XP Modifiers").build();

    public static final LangProvider.Translation SALVAGE_HEADER = LangProvider.Translation.Builder.start("pmmo.salvage_header").addLocale(LangProvider.Locale.EN_US, "Salvage").build();

    public static final LangProvider.Translation SALVAGE_LEVEL_REQ = LangProvider.Translation.Builder.start("pmmo.salvage_levelreq").addLocale(LangProvider.Locale.EN_US, "Required level to obtain").build();

    public static final LangProvider.Translation SALVAGE_CHANCE = LangProvider.Translation.Builder.start("pmmo.salvage_chance").addLocale(LangProvider.Locale.EN_US, "Chance: %1$s / %2$s").build();

    public static final LangProvider.Translation SALVAGE_MAX = LangProvider.Translation.Builder.start("pmmo.salvage_max").addLocale(LangProvider.Locale.EN_US, "Max Obtainable: %1$s").build();

    public static final LangProvider.Translation SALVAGE_CHANCE_MOD = LangProvider.Translation.Builder.start("pmmo.salvage_chance_modifier").addLocale(LangProvider.Locale.EN_US, "Chance boost based on level").build();

    public static final LangProvider.Translation SALVAGE_XP_AWARD = LangProvider.Translation.Builder.start("pmmo.salvage_xpAward_header").addLocale(LangProvider.Locale.EN_US, "Xp awarded on success").build();

    public static final LangProvider.Translation VEIN_HEADER = LangProvider.Translation.Builder.start("pmmo.vein_header").addLocale(LangProvider.Locale.EN_US, "Vein Mining Attributes").build();

    public static final LangProvider.Translation VEIN_RATE = LangProvider.Translation.Builder.start("pmmo.veindata_rate").addLocale(LangProvider.Locale.EN_US, "Vein Recharge Rate Per Second: %1$s").build();

    public static final LangProvider.Translation VEIN_CAP = LangProvider.Translation.Builder.start("pmmo.veindata_cap").addLocale(LangProvider.Locale.EN_US, "Vein Capacity Added By Item: %1$s").build();

    public static final LangProvider.Translation VEIN_CONSUME = LangProvider.Translation.Builder.start("pmmo.veindata_consume").addLocale(LangProvider.Locale.EN_US, "Vein Consumed on Break: %1$s").build();

    public static final LangProvider.Translation PLAYER_HEADER = LangProvider.Translation.Builder.start("pmmo.playerspecific_header").addLocale(LangProvider.Locale.EN_US, "Player-Specific Settings").build();

    public static final LangProvider.Translation PLAYER_IGNORE_REQ = LangProvider.Translation.Builder.start("pmmo.playerspecific.ignorereq").addLocale(LangProvider.Locale.EN_US, "Ignore Reqs: %1$s").build();

    public static final LangProvider.Translation PLAYER_BONUSES = LangProvider.Translation.Builder.start("pmmo.playerspecific.bonus").addLocale(LangProvider.Locale.EN_US, "Player Bonuses:").build();

    public static final LangProvider.Translation SKILL_LIST_HEADER = LangProvider.Translation.Builder.start("pmmo.skilllist_header").addLocale(LangProvider.Locale.EN_US, "Player Skills").build();

    public static final LangProvider.Translation DIMENSION_HEADER = LangProvider.Translation.Builder.start("pmmo.dimension_header").addLocale(LangProvider.Locale.EN_US, "Dimension: %1$s").build();

    public static final LangProvider.Translation VEIN_BLACKLIST_HEADER = LangProvider.Translation.Builder.start("pmmo.vein_blacklist_header").addLocale(LangProvider.Locale.EN_US, "Vein Blacklisted Blocks").build();

    public static final LangProvider.Translation MOB_MODIFIER_HEADER = LangProvider.Translation.Builder.start("pmmo.mob_modifier_header").addLocale(LangProvider.Locale.EN_US, "Mob Modifiers").build();

    public static final LangProvider.Translation BIOME_HEADER = LangProvider.Translation.Builder.start("pmmo.biome_header").addLocale(LangProvider.Locale.EN_US, "Mob Modifiers").build();

    public static final LangProvider.Translation BIOME_EFFECT_NEG = LangProvider.Translation.Builder.start("pmmo.biome_negative").addLocale(LangProvider.Locale.EN_US, "Penalty Effects").build();

    public static final LangProvider.Translation BIOME_EFFECT_POS = LangProvider.Translation.Builder.start("pmmo.biome_positive").addLocale(LangProvider.Locale.EN_US, "Bonus Effects").build();

    public static final LangProvider.Translation ADDON_AFFECTED_ATTRIBUTE = LangProvider.Translation.Builder.start("pmmo.gui.statscroll.addon_affected").addLocale(LangProvider.Locale.EN_US, "This Property is Dynamically Defined").build();

    public static final LangProvider.Translation GLOSSARY_DEFAULT_SECTION = LangProvider.Translation.Builder.start("pmmo.gui.glossary.default_section").addLocale(LangProvider.Locale.EN_US, "Choose Section").build();

    public static final LangProvider.Translation GLOSSARY_SECTION_REQ = LangProvider.Translation.Builder.start("pmmo.gui.glossary.section.req").addLocale(LangProvider.Locale.EN_US, "Requirements").build();

    public static final LangProvider.Translation GLOSSARY_SECTION_XP = LangProvider.Translation.Builder.start("pmmo.gui.glossary.section.xp_sources").addLocale(LangProvider.Locale.EN_US, "XP Sources").build();

    public static final LangProvider.Translation GLOSSARY_SECTION_BONUS = LangProvider.Translation.Builder.start("pmmo.gui.glossary.section.bonuses").addLocale(LangProvider.Locale.EN_US, "Bonuses").build();

    public static final LangProvider.Translation GLOSSARY_SECTION_SALVAGE = LangProvider.Translation.Builder.start("pmmo.gui.glossary.section.salvage").addLocale(LangProvider.Locale.EN_US, "Salvage").build();

    public static final LangProvider.Translation GLOSSARY_SECTION_VEIN = LangProvider.Translation.Builder.start("pmmo.gui.glossary.section.vein").addLocale(LangProvider.Locale.EN_US, "Vein Mining").build();

    public static final LangProvider.Translation GLOSSARY_SECTION_MOB = LangProvider.Translation.Builder.start("pmmo.gui.glossary.section.mobscaling").addLocale(LangProvider.Locale.EN_US, "Mob Scaling").build();

    public static final LangProvider.Translation GLOSSARY_SECTION_PERKS = LangProvider.Translation.Builder.start("pmmo.gui.glossary.section.perks").addLocale(LangProvider.Locale.EN_US, "Perks").build();

    public static final LangProvider.Translation GLOSSARY_DEFAULT_OBJECT = LangProvider.Translation.Builder.start("pmmo.gui.glossary.default_object").addLocale(LangProvider.Locale.EN_US, "All Content").build();

    public static final LangProvider.Translation GLOSSARY_OBJECT_ITEMS = LangProvider.Translation.Builder.start("pmmo.gui.glossary.object.items").addLocale(LangProvider.Locale.EN_US, "Items").build();

    public static final LangProvider.Translation GLOSSARY_OBJECT_BLOCKS = LangProvider.Translation.Builder.start("pmmo.gui.glossary.object.blocks").addLocale(LangProvider.Locale.EN_US, "Blocks").build();

    public static final LangProvider.Translation GLOSSARY_OBJECT_ENTITIES = LangProvider.Translation.Builder.start("pmmo.gui.glossary.object.entities").addLocale(LangProvider.Locale.EN_US, "Animals/Mobs").build();

    public static final LangProvider.Translation GLOSSARY_OBJECT_DIMENSIONS = LangProvider.Translation.Builder.start("pmmo.gui.glossary.object.dimensions").addLocale(LangProvider.Locale.EN_US, "Dimensions").build();

    public static final LangProvider.Translation GLOSSARY_OBJECT_BIOMES = LangProvider.Translation.Builder.start("pmmo.gui.glossary.object.biomes").addLocale(LangProvider.Locale.EN_US, "Biomes").build();

    public static final LangProvider.Translation GLOSSARY_OBJECT_ENCHANTS = LangProvider.Translation.Builder.start("pmmo.gui.glossary.object.enchantments").addLocale(LangProvider.Locale.EN_US, "Enchantments").build();

    public static final LangProvider.Translation GLOSSARY_OBJECT_EFFECTS = LangProvider.Translation.Builder.start("pmmo.gui.glossary.object.effects").addLocale(LangProvider.Locale.EN_US, "Effects").build();

    public static final LangProvider.Translation GLOSSARY_OBJECT_PERKS = LangProvider.Translation.Builder.start("pmmo.gui.glossary.object.perks").addLocale(LangProvider.Locale.EN_US, "Perks").build();

    public static final LangProvider.Translation GLOSSARY_DEFAULT_SKILL = LangProvider.Translation.Builder.start("pmmo.gui.glossary.default_skill").addLocale(LangProvider.Locale.EN_US, "All Skills").build();

    public static final LangProvider.Translation GLOSSARY_DEFAULT_ENUM = LangProvider.Translation.Builder.start("pmmo.gui.glossary.default_enum").addLocale(LangProvider.Locale.EN_US, "All Event/Req/Type").build();

    public static final LangProvider.Translation GLOSSARY_VIEW_BUTTON = LangProvider.Translation.Builder.start("pmmo.gui.glossary.view_button").addLocale(LangProvider.Locale.EN_US, "View Info").build();

    public static final LangProvider.Translation FOUND_TREASURE = LangProvider.Translation.Builder.start("pmmo.youFoundTreasure").addLocale(LangProvider.Locale.EN_US, "You Found Treasure!").build();

    public static final LangProvider.Translation LEVELED_UP = LangProvider.Translation.Builder.start("pmmo.leveled_up").addLocale(LangProvider.Locale.EN_US, "You leveled up to %s in %s").build();

    public static final LangProvider.Translation PERK_BREATH_REFRESH = LangProvider.Translation.Builder.start("pmmo.perks.breathrefresh").addLocale(LangProvider.Locale.EN_US, "Your skill extended your breath").build();

    public static final LangProvider.Translation VEIN_LIMIT = LangProvider.Translation.Builder.start("pmmo.veinLimit").addLocale(LangProvider.Locale.EN_US, "Vein Limit: %1$s").build();

    public static final LangProvider.Translation VEIN_CHARGE = LangProvider.Translation.Builder.start("pmmo.veinCharge").addLocale(LangProvider.Locale.EN_US, "Vein Ability: %1$s/%2$s").build();

    public static final LangProvider.Translation SET_LEVEL = LangProvider.Translation.Builder.start("pmmo.setLevel").addLocale(LangProvider.Locale.EN_US, "%1$s has been set to level %2$s for %3$s").build();

    public static final LangProvider.Translation SET_XP = LangProvider.Translation.Builder.start("pmmo.setXp").addLocale(LangProvider.Locale.EN_US, "%1$s has been set to %2$sxp for %3$s").build();

    public static final LangProvider.Translation ADD_LEVEL = LangProvider.Translation.Builder.start("pmmo.addLevel").addLocale(LangProvider.Locale.EN_US, "%1$s has been changed by %2$s levels for %3$s").build();

    public static final LangProvider.Translation ADD_XP = LangProvider.Translation.Builder.start("pmmo.addXp").addLocale(LangProvider.Locale.EN_US, "%1$s has been changed by %2$sxp for %3$s").build();

    public static final LangProvider.Translation PARTY_ALREADY_IN = LangProvider.Translation.Builder.start("pmmo.youAreAlreadyInAParty").addLocale(LangProvider.Locale.EN_US, "You are already in a Party").build();

    public static final LangProvider.Translation PARTY_CREATED = LangProvider.Translation.Builder.start("pmmo.partyCreated").addLocale(LangProvider.Locale.EN_US, "You have created a Party").build();

    public static final LangProvider.Translation PARTY_LEFT = LangProvider.Translation.Builder.start("pmmo.youLeftTheParty").addLocale(LangProvider.Locale.EN_US, "You have left the Party").build();

    public static final LangProvider.Translation PARTY_NOT_IN = LangProvider.Translation.Builder.start("pmmo.youAreNotInAParty").addLocale(LangProvider.Locale.EN_US, "You are not in a Party").build();

    public static final LangProvider.Translation PARTY_INVITE = LangProvider.Translation.Builder.start("pmmo.youHaveInvitedAPlayerToYourParty").addLocale(LangProvider.Locale.EN_US, "You invited %1$s to your Party").build();

    public static final LangProvider.Translation PARTY_MEMBER_TOTAL = LangProvider.Translation.Builder.start("pmmo.totalMembers").addLocale(LangProvider.Locale.EN_US, "Total Members: %1$s").build();

    public static final LangProvider.Translation PARTY_MEMBER_LIST = LangProvider.Translation.Builder.start("pmmo.partyMemberListEntry").addLocale(LangProvider.Locale.EN_US, "%1$s").build();

    public static final LangProvider.Translation PARTY_DECLINE = LangProvider.Translation.Builder.start("pmmo.youHaveDeclinedPartyInvitation").addLocale(LangProvider.Locale.EN_US, "You have declined the Party invitation").build();

    public static final LangProvider.Translation PARTY_NO_INVITES = LangProvider.Translation.Builder.start("pmmo.youAreNotInvitedToAnyParty").addLocale(LangProvider.Locale.EN_US, "You have no pending Party invitations").build();

    public static final LangProvider.Translation PARTY_JOINED = LangProvider.Translation.Builder.start("pmmo.youJoinedAParty").addLocale(LangProvider.Locale.EN_US, "You have joined the Party!").build();

    public static final LangProvider.Translation PARTY_RESCIND_INVITE = LangProvider.Translation.Builder.start("pmmo.msg.rescindInvite").addLocale(LangProvider.Locale.EN_US, "You have removed the invite for %1$s").build();

    public static final LangProvider.Translation PARTY_ACCEPT = LangProvider.Translation.Builder.start("pmmo.msg.accept").addLocale(LangProvider.Locale.EN_US, "Accept").build();

    public static final LangProvider.Translation PARTY_DECLINE_INVITE = LangProvider.Translation.Builder.start("pmmo.msg.decline").addLocale(LangProvider.Locale.EN_US, "Decline").build();

    public static final LangProvider.Translation PARTY_PLAYER_INVITED = LangProvider.Translation.Builder.start("pmmo.playerInvitedYouToAParty").addLocale(LangProvider.Locale.EN_US, "%1$s invited you to their Party, %2$s|%3$s").build();

    public static final LangProvider.Translation PACK_BEGIN = LangProvider.Translation.Builder.start("pmmo.cmd.pack.begin").addLocale(LangProvider.Locale.EN_US, "Starting new genData.  Override, defaults, players, simplify, and disabler settings have all been set to false.").build();

    public static final LangProvider.Translation PACK_OVERRIDE = LangProvider.Translation.Builder.start("pmmo.cmd.pack.override").addLocale(LangProvider.Locale.EN_US, "Generated files will now include override set to true").build();

    public static final LangProvider.Translation PACK_DEFAULTS = LangProvider.Translation.Builder.start("pmmo.cmd.pack.defaults").addLocale(LangProvider.Locale.EN_US, "Generated files will now include existing settings and AutoValues.").build();

    public static final LangProvider.Translation PACK_DISABLER = LangProvider.Translation.Builder.start("pmmo.cmd.pack.disabler").addLocale(LangProvider.Locale.EN_US, "Generated pack will now disable all packs loaded before it.").build();

    public static final LangProvider.Translation PACK_PLAYERS = LangProvider.Translation.Builder.start("pmmo.cmd.pack.players").addLocale(LangProvider.Locale.EN_US, "Generated pack will now include the selected player files").build();

    public static final LangProvider.Translation PACK_SIMPLE = LangProvider.Translation.Builder.start("pmmo.cmd.pack.simple").addLocale(LangProvider.Locale.EN_US, "Generated pack will exclude unused properties.").build();

    public static final LangProvider.Translation PACK_FILTER = LangProvider.Translation.Builder.start("pmmo.cmd.pack.filter").addLocale(LangProvider.Locale.EN_US, "Generated pack will only include files from this namespace").build();

    public static final LangProvider.Translation DENIAL_WEAR = LangProvider.Translation.Builder.start("pmmo.msg.denial.wear").addLocale(LangProvider.Locale.EN_US, "You are not skilled enough to wear %1$s").build();

    public static final LangProvider.Translation DENIAL_USE_ENCHANT = LangProvider.Translation.Builder.start("pmmo.msg.denial.use_enchantment").addLocale(LangProvider.Locale.EN_US, "You are not skilled enough to use %1$s with %2$s enchantment").build();

    public static final LangProvider.Translation DENIAL_TOOL = LangProvider.Translation.Builder.start("pmmo.msg.denial.tool").addLocale(LangProvider.Locale.EN_US, "You are not skilled enough to use %1$s as tool").build();

    public static final LangProvider.Translation DENIAL_WEAPON = LangProvider.Translation.Builder.start("pmmo.msg.denial.weapon").addLocale(LangProvider.Locale.EN_US, "You are not skilled enough to use %1$s as a weapon").build();

    public static final LangProvider.Translation DENIAL_USE = LangProvider.Translation.Builder.start("pmmo.msg.denial.use").addLocale(LangProvider.Locale.EN_US, "You are not skilled enough to use %1$s").build();

    public static final LangProvider.Translation DENIAL_PLACE = LangProvider.Translation.Builder.start("pmmo.msg.denial.place").addLocale(LangProvider.Locale.EN_US, "You are not skilled enough to place %1$s").build();

    public static final LangProvider.Translation DENIAL_BREAK = LangProvider.Translation.Builder.start("pmmo.msg.denial.break").addLocale(LangProvider.Locale.EN_US, "You are not skilled enough to break %1$s").build();

    public static final LangProvider.Translation DENIAL_BIOME = LangProvider.Translation.Builder.start("pmmo.msg.denial.biome").addLocale(LangProvider.Locale.EN_US, "You are not skilled enough to survive in %1$s").build();

    public static final LangProvider.Translation DENIAL_KILL = LangProvider.Translation.Builder.start("pmmo.msg.denial.kill").addLocale(LangProvider.Locale.EN_US, "You are not skilled enough to kill %1$s").build();

    public static final LangProvider.Translation DENIAL_TRAVEL = LangProvider.Translation.Builder.start("pmmo.msg.denial.travel").addLocale(LangProvider.Locale.EN_US, "Travel to %1$s requires %2$s").build();

    public static final LangProvider.Translation DENIAL_RIDE = LangProvider.Translation.Builder.start("pmmo.msg.denial.ride").addLocale(LangProvider.Locale.EN_US, "You are not skilled enough to ride %1$s").build();

    public static final LangProvider.Translation DENIAL_TAME = LangProvider.Translation.Builder.start("pmmo.msg.denial.tame").addLocale(LangProvider.Locale.EN_US, "You are not skilled enough to tame %1$s").build();

    public static final LangProvider.Translation DENIAL_ENTITY_INTERACT = LangProvider.Translation.Builder.start("pmmo.msg.denial.entity_interact").addLocale(LangProvider.Locale.EN_US, "You are not skilled enough to interact with %1$s").build();

    public static final LangProvider.Translation DENIAL_SALVAGE = LangProvider.Translation.Builder.start("pmmo.msg.denial.salvage").addLocale(LangProvider.Locale.EN_US, "You cannot salvage %s. Either it cannot be salvaged or you are not skilled enough yet.").build();

    public static final LangProvider.Translation DENIAL_INTERACT = LangProvider.Translation.Builder.start("pmmo.msg.denial.interact").addLocale(LangProvider.Locale.EN_US, "You are not skilled enough to interact with %1$s").build();

    public static final LangProvider.Translation SALVAGE_TUTORIAL_HEADER = LangProvider.Translation.Builder.start("pmmo.client.tutorial.salvage.header").addLocale(LangProvider.Locale.EN_US, "Salvage Block").build();

    public static final LangProvider.Translation SALVAGE_TUTORIAL_USAGE = LangProvider.Translation.Builder.start("pmmo.client.tutorial.salvage.usage").addLocale(LangProvider.Locale.EN_US, "While crouching, right click this block to salvage the items in your hand.").build();

    public static final LangProvider.Translation FTBQ_XP_TITLE = LangProvider.Translation.Builder.start("ftbquests.reward.pmmo.xpreward").addLocale(LangProvider.Locale.HU, "%s %s  Xp Reward").addLocale(LangProvider.Locale.JA, "%s %s  Xp Reward").addLocale(LangProvider.Locale.PL, "Nagroda XP za umijętność").addLocale(LangProvider.Locale.DE_DE, "%s %s  Xp Reward").addLocale(LangProvider.Locale.ES_AR, "Recompensa Exp de Atributo").addLocale(LangProvider.Locale.ES_CL, "%s %s  Xp Reward").addLocale(LangProvider.Locale.ES_EC, "%s %s  Xp Reward").addLocale(LangProvider.Locale.ES_ES, "%s %s  Xp Reward").addLocale(LangProvider.Locale.ES_MX, "%s %s  Xp Reward").addLocale(LangProvider.Locale.ES_UY, "%s %s  Xp Reward").addLocale(LangProvider.Locale.ES_VE, "%s %s  Xp Reward").addLocale(LangProvider.Locale.FR_FR, "Xp de Compétence en Récompense").addLocale(LangProvider.Locale.HU, "%s %s  Xp Reward").addLocale(LangProvider.Locale.IT_IT, "%s %s  Xp Reward").addLocale(LangProvider.Locale.JA, "%s %s  Xp Reward").addLocale(LangProvider.Locale.KO_KR, "스킬 XP 보상").addLocale(LangProvider.Locale.LT_LT, "%s %s  Xp Reward").addLocale(LangProvider.Locale.NL_NL, "%s %s  Xp Reward").addLocale(LangProvider.Locale.PL, "Nagroda XP za umijętność").addLocale(LangProvider.Locale.PT_BR, "Recompensa de Exp de Habilidade").addLocale(LangProvider.Locale.RU_RU, "%s %s  Xp Reward").addLocale(LangProvider.Locale.SV_SE, "%s %s  Xp Reward").addLocale(LangProvider.Locale.UK_UA, "Нагорода за майстерність").addLocale(LangProvider.Locale.ZH_CN, "技能经验奖励").addLocale(LangProvider.Locale.ZH_TW, "獲得技術經驗").addLocale(LangProvider.Locale.EN_US, "%s %s Xp Reward").build();

    public static final LangProvider.Translation FTBQ_XP_SKILL = LangProvider.Translation.Builder.start("ftbquests.reward.pmmo.xpreward.skill").addLocale(LangProvider.Locale.HU, "Skill").addLocale(LangProvider.Locale.JA, "Skill").addLocale(LangProvider.Locale.PL, "Umiejętność").addLocale(LangProvider.Locale.DE_DE, "Skill").addLocale(LangProvider.Locale.ES_AR, "Atributo").addLocale(LangProvider.Locale.ES_CL, "Skill").addLocale(LangProvider.Locale.ES_EC, "Skill").addLocale(LangProvider.Locale.ES_ES, "Skill").addLocale(LangProvider.Locale.ES_MX, "Skill").addLocale(LangProvider.Locale.ES_UY, "Skill").addLocale(LangProvider.Locale.ES_VE, "Skill").addLocale(LangProvider.Locale.FR_FR, "Compétence").addLocale(LangProvider.Locale.HU, "Skill").addLocale(LangProvider.Locale.IT_IT, "Skill").addLocale(LangProvider.Locale.JA, "Skill").addLocale(LangProvider.Locale.KO_KR, "스킬").addLocale(LangProvider.Locale.LT_LT, "Skill").addLocale(LangProvider.Locale.NL_NL, "Skill").addLocale(LangProvider.Locale.PL, "Umiejętność").addLocale(LangProvider.Locale.PT_BR, "Habilidade").addLocale(LangProvider.Locale.RU_RU, "Skill").addLocale(LangProvider.Locale.SV_SE, "Skill").addLocale(LangProvider.Locale.UK_UA, "Майстерність").addLocale(LangProvider.Locale.ZH_CN, "技能").addLocale(LangProvider.Locale.ZH_TW, "技術").addLocale(LangProvider.Locale.EN_US, "Skill").build();

    public static final LangProvider.Translation FTBQ_XP_AMOUNT = LangProvider.Translation.Builder.start("ftbquests.reward.pmmo.xpreward.amount").addLocale(LangProvider.Locale.HU, "Xp Reward").addLocale(LangProvider.Locale.JA, "Xp Reward").addLocale(LangProvider.Locale.PL, "Nagroda XP").addLocale(LangProvider.Locale.DE_DE, "Xp Reward").addLocale(LangProvider.Locale.ES_AR, "Recompensa Exp").addLocale(LangProvider.Locale.ES_CL, "Xp Reward").addLocale(LangProvider.Locale.ES_EC, "Xp Reward").addLocale(LangProvider.Locale.ES_ES, "Xp Reward").addLocale(LangProvider.Locale.ES_MX, "Xp Reward").addLocale(LangProvider.Locale.ES_UY, "Xp Reward").addLocale(LangProvider.Locale.ES_VE, "Xp Reward").addLocale(LangProvider.Locale.FR_FR, "Xp en Récompense").addLocale(LangProvider.Locale.HU, "Xp Reward").addLocale(LangProvider.Locale.IT_IT, "Xp Reward").addLocale(LangProvider.Locale.JA, "Xp Reward").addLocale(LangProvider.Locale.KO_KR, "XP 보상").addLocale(LangProvider.Locale.LT_LT, "Xp Reward").addLocale(LangProvider.Locale.NL_NL, "Xp Reward").addLocale(LangProvider.Locale.PL, "Nagroda XP").addLocale(LangProvider.Locale.PT_BR, "Recompensa de Exp").addLocale(LangProvider.Locale.RU_RU, "Xp Reward").addLocale(LangProvider.Locale.SV_SE, "Xp Reward").addLocale(LangProvider.Locale.UK_UA, "Нагорода за досвід").addLocale(LangProvider.Locale.ZH_CN, "经验奖励").addLocale(LangProvider.Locale.ZH_TW, "獲得經驗").addLocale(LangProvider.Locale.EN_US, "Xp Reward").build();

    public static final LangProvider.Translation FTBQ_LVL_TITLE = LangProvider.Translation.Builder.start("ftbquests.reward.pmmo.levelreward").addLocale(LangProvider.Locale.HU, "Skill Level Reward").addLocale(LangProvider.Locale.JA, "Skill Level Reward").addLocale(LangProvider.Locale.PL, "Nagroda za poziom umiejętności").addLocale(LangProvider.Locale.DE_DE, "Skill Level Reward").addLocale(LangProvider.Locale.ES_AR, "Recompensa por Nivel de Atributo").addLocale(LangProvider.Locale.ES_CL, "Skill Level Reward").addLocale(LangProvider.Locale.ES_EC, "Skill Level Reward").addLocale(LangProvider.Locale.ES_ES, "Skill Level Reward").addLocale(LangProvider.Locale.ES_MX, "Skill Level Reward").addLocale(LangProvider.Locale.ES_UY, "Skill Level Reward").addLocale(LangProvider.Locale.ES_VE, "Skill Level Reward").addLocale(LangProvider.Locale.FR_FR, "Niveau de Compétence en Récompense").addLocale(LangProvider.Locale.HU, "Skill Level Reward").addLocale(LangProvider.Locale.IT_IT, "Skill Level Reward").addLocale(LangProvider.Locale.JA, "Skill Level Reward").addLocale(LangProvider.Locale.KO_KR, "스킬 레벨 보상").addLocale(LangProvider.Locale.LT_LT, "Skill Level Reward").addLocale(LangProvider.Locale.NL_NL, "Skill Level Reward").addLocale(LangProvider.Locale.PL, "Nagroda za poziom umiejętności").addLocale(LangProvider.Locale.PT_BR, "Recompensa de Nível de Habilidade").addLocale(LangProvider.Locale.RU_RU, "Skill Level Reward").addLocale(LangProvider.Locale.SV_SE, "Skill Level Reward").addLocale(LangProvider.Locale.UK_UA, "Нагорода за рівень майстерності").addLocale(LangProvider.Locale.ZH_CN, "技能等级奖励").addLocale(LangProvider.Locale.ZH_TW, "獲得技術等級").addLocale(LangProvider.Locale.EN_US, "%s %s Level Reward").build();

    public static final LangProvider.Translation FTBQ_LVL_SKILL = LangProvider.Translation.Builder.start("ftbquests.reward.pmmo.levelreward.skill").addLocale(LangProvider.Locale.HU, "Skill").addLocale(LangProvider.Locale.JA, "Skill").addLocale(LangProvider.Locale.PL, "Umiejętność").addLocale(LangProvider.Locale.DE_DE, "Skill").addLocale(LangProvider.Locale.ES_AR, "Atributo").addLocale(LangProvider.Locale.ES_CL, "Skill").addLocale(LangProvider.Locale.ES_EC, "Skill").addLocale(LangProvider.Locale.ES_ES, "Skill").addLocale(LangProvider.Locale.ES_MX, "Skill").addLocale(LangProvider.Locale.ES_UY, "Skill").addLocale(LangProvider.Locale.ES_VE, "Skill").addLocale(LangProvider.Locale.FR_FR, "Compétence").addLocale(LangProvider.Locale.HU, "Skill").addLocale(LangProvider.Locale.IT_IT, "Skill").addLocale(LangProvider.Locale.JA, "Skill").addLocale(LangProvider.Locale.KO_KR, "스킬").addLocale(LangProvider.Locale.LT_LT, "Skill").addLocale(LangProvider.Locale.NL_NL, "Skill").addLocale(LangProvider.Locale.PL, "Umiejętność").addLocale(LangProvider.Locale.PT_BR, "Habilidade").addLocale(LangProvider.Locale.RU_RU, "Skill").addLocale(LangProvider.Locale.SV_SE, "Skill").addLocale(LangProvider.Locale.UK_UA, "Майстерність").addLocale(LangProvider.Locale.ZH_CN, "技能").addLocale(LangProvider.Locale.ZH_TW, "技術").addLocale(LangProvider.Locale.EN_US, "Skill").build();

    public static final LangProvider.Translation FTBQ_LVL_AMOUNT = LangProvider.Translation.Builder.start("ftbquests.reward.pmmo.levelreward.amount").addLocale(LangProvider.Locale.HU, "Level Reward").addLocale(LangProvider.Locale.JA, "Level Reward").addLocale(LangProvider.Locale.PL, "Nagroda za poziom").addLocale(LangProvider.Locale.DE_DE, "Level Reward").addLocale(LangProvider.Locale.ES_AR, "Recompensa por Nivel").addLocale(LangProvider.Locale.ES_CL, "Level Reward").addLocale(LangProvider.Locale.ES_EC, "Level Reward").addLocale(LangProvider.Locale.ES_ES, "Level Reward").addLocale(LangProvider.Locale.ES_MX, "Level Reward").addLocale(LangProvider.Locale.ES_UY, "Level Reward").addLocale(LangProvider.Locale.ES_VE, "Level Reward").addLocale(LangProvider.Locale.FR_FR, "Niveau en Récompense").addLocale(LangProvider.Locale.HU, "Level Reward").addLocale(LangProvider.Locale.IT_IT, "Level Reward").addLocale(LangProvider.Locale.JA, "Level Reward").addLocale(LangProvider.Locale.KO_KR, "레벨 보상").addLocale(LangProvider.Locale.LT_LT, "Level Reward").addLocale(LangProvider.Locale.NL_NL, "Level Reward").addLocale(LangProvider.Locale.PL, "Nagroda za poziom").addLocale(LangProvider.Locale.PT_BR, "Recompensa de Nível").addLocale(LangProvider.Locale.RU_RU, "Level Reward").addLocale(LangProvider.Locale.SV_SE, "Level Reward").addLocale(LangProvider.Locale.UK_UA, "Винагорода за рівень").addLocale(LangProvider.Locale.ZH_CN, "等级奖励").addLocale(LangProvider.Locale.ZH_TW, "獲得等級").addLocale(LangProvider.Locale.EN_US, "Level Reward").build();

    public static final LangProvider.Translation FTBQ_SKILL_TITLE = LangProvider.Translation.Builder.start("ftbquests.task.pmmo.skill").addLocale(LangProvider.Locale.HU, "Level %s in %s").addLocale(LangProvider.Locale.JA, "Level %s in %s").addLocale(LangProvider.Locale.PL, "Umiejętność").addLocale(LangProvider.Locale.DE_DE, "Fähigkeit").addLocale(LangProvider.Locale.ES_AR, "Habilidad").addLocale(LangProvider.Locale.ES_UY, "Habilidad").addLocale(LangProvider.Locale.ES_VE, "Habilidad").addLocale(LangProvider.Locale.FR_FR, "Compétence").addLocale(LangProvider.Locale.HU, "Level %s in %s").addLocale(LangProvider.Locale.IT_IT, "Level %s in %s").addLocale(LangProvider.Locale.JA, "Level %s in %s").addLocale(LangProvider.Locale.KO_KR, "스킬").addLocale(LangProvider.Locale.LT_LT, "Level %s in %s").addLocale(LangProvider.Locale.NL_NL, "Level %s in %s").addLocale(LangProvider.Locale.PL, "Umiejętność").addLocale(LangProvider.Locale.PT_BR, "Habilidade").addLocale(LangProvider.Locale.RU_RU, "Level %s in %s").addLocale(LangProvider.Locale.SV_SE, "Level %s in %s").addLocale(LangProvider.Locale.UK_UA, "Майстерність").addLocale(LangProvider.Locale.ZH_CN, "技能").addLocale(LangProvider.Locale.ZH_TW, "技能").addLocale(LangProvider.Locale.EN_US, "Level %s in %s").build();

    public static final LangProvider.Translation FTBQ_SKILL_SKILL = LangProvider.Translation.Builder.start("ftbquests.task.pmmo.skill.skill").addLocale(LangProvider.Locale.HU, "Skill").addLocale(LangProvider.Locale.JA, "Skill").addLocale(LangProvider.Locale.PL, "Umiejętność").addLocale(LangProvider.Locale.DE_DE, "Fähigkeit").addLocale(LangProvider.Locale.ES_AR, "Atributo").addLocale(LangProvider.Locale.ES_CL, "Atributo").addLocale(LangProvider.Locale.ES_EC, "Atributo").addLocale(LangProvider.Locale.ES_ES, "Atributo").addLocale(LangProvider.Locale.ES_MX, "Atributo").addLocale(LangProvider.Locale.ES_UY, "Atributo").addLocale(LangProvider.Locale.ES_VE, "Atributo").addLocale(LangProvider.Locale.FR_FR, "Compétence").addLocale(LangProvider.Locale.HU, "Skill").addLocale(LangProvider.Locale.IT_IT, "Skill").addLocale(LangProvider.Locale.JA, "Skill").addLocale(LangProvider.Locale.KO_KR, "스킬").addLocale(LangProvider.Locale.LT_LT, "Skill").addLocale(LangProvider.Locale.NL_NL, "Skill").addLocale(LangProvider.Locale.PL, "Umiejętność").addLocale(LangProvider.Locale.PT_BR, "Habilidade").addLocale(LangProvider.Locale.RU_RU, "Skill").addLocale(LangProvider.Locale.SV_SE, "Skill").addLocale(LangProvider.Locale.UK_UA, "Майстерність").addLocale(LangProvider.Locale.ZH_CN, "技能").addLocale(LangProvider.Locale.ZH_TW, "技能").addLocale(LangProvider.Locale.EN_US, "Skill").build();

    public static final LangProvider.Translation FTBQ_SKILL_LEVEL = LangProvider.Translation.Builder.start("ftbquests.task.pmmo.skill.requiredLevel").addLocale(LangProvider.Locale.HU, "Required Level").addLocale(LangProvider.Locale.JA, "Required Level").addLocale(LangProvider.Locale.PL, "Wymagany Poziom").addLocale(LangProvider.Locale.DE_DE, "Required Level").addLocale(LangProvider.Locale.ES_AR, "Nivel Requerido").addLocale(LangProvider.Locale.ES_CL, "Nivel Requerido").addLocale(LangProvider.Locale.ES_EC, "Nivel Requerido").addLocale(LangProvider.Locale.ES_ES, "Nivel Requerido").addLocale(LangProvider.Locale.ES_MX, "Nivel Requerido").addLocale(LangProvider.Locale.ES_UY, "Nivel Requerido").addLocale(LangProvider.Locale.ES_VE, "Nivel Requerido").addLocale(LangProvider.Locale.FR_FR, "Niveau requis").addLocale(LangProvider.Locale.HU, "Required Level").addLocale(LangProvider.Locale.IT_IT, "Required Level").addLocale(LangProvider.Locale.JA, "Required Level").addLocale(LangProvider.Locale.KO_KR, "요구 레벨").addLocale(LangProvider.Locale.LT_LT, "Required Level").addLocale(LangProvider.Locale.NL_NL, "Required Level").addLocale(LangProvider.Locale.PL, "Wymagany Poziom").addLocale(LangProvider.Locale.PT_BR, "Nível Exigido").addLocale(LangProvider.Locale.RU_RU, "Required Level").addLocale(LangProvider.Locale.SV_SE, "Required Level").addLocale(LangProvider.Locale.UK_UA, "Необхідний рівень").addLocale(LangProvider.Locale.ZH_CN, "需求等级").addLocale(LangProvider.Locale.ZH_TW, "需求等級").addLocale(LangProvider.Locale.EN_US, "Required Level").build();

    public LangProvider(PackOutput gen, String locale) {
        super(gen, "pmmo", locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        for (Field entry : this.getClass().getDeclaredFields()) {
            if (entry.getType() == LangProvider.Translation.class) {
                try {
                    this.add((LangProvider.Translation) entry.get(LangProvider.class));
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
            }
        }
    }

    private void add(LangProvider.Translation translation) {
        if (translation.localeMap().get(this.locale) != null) {
            this.add(translation.key(), (String) translation.localeMap().get(this.locale));
        }
    }

    public static MutableComponent skill(String skill) {
        return Component.translatable("pmmo." + skill).withStyle(style -> style.withColor(CoreUtils.getSkillColor(skill)));
    }

    public static enum Locale {

        DE_DE("de_de"),
        EN_US("en_us"),
        ES_AR("es_ar"),
        ES_CL("es_cl"),
        ES_EC("es_ec"),
        ES_ES("es_es"),
        ES_MX("ex_mx"),
        ES_UY("es_uy"),
        ES_VE("es_ve"),
        FR_FR("fr_fr"),
        HU("hu"),
        IT_IT("it_it"),
        JA("ja"),
        KO_KR("ko_kr"),
        LT_LT("lt_lt"),
        NL_NL("nl_nl"),
        PL("pl"),
        PT_BR("pt_br"),
        RU_RU("ru_ru"),
        SV_SE("sv_se"),
        UK_UA("uk_ua"),
        ZH_CN("zh_cn"),
        ZH_TW("zh_tw");

        public String str;

        private Locale(String locale) {
            this.str = locale;
        }
    }

    public static record Translation(String key, Map<String, String> localeMap) {

        public MutableComponent asComponent() {
            return Component.translatable(this.key());
        }

        public MutableComponent asComponent(Object... obj) {
            return Component.translatable(this.key(), obj);
        }

        public static class Builder {

            private final String key;

            private Map<String, String> localeMap;

            private Builder(String key) {
                this.key = key;
                this.localeMap = new HashMap();
            }

            public static LangProvider.Translation.Builder start(String key) {
                return new LangProvider.Translation.Builder(key);
            }

            public LangProvider.Translation.Builder addLocale(LangProvider.Locale locale, String translation) {
                this.localeMap.put(locale.str, translation);
                return this;
            }

            public LangProvider.Translation build() {
                return new LangProvider.Translation(this.key, this.localeMap);
            }
        }
    }
}