package broboss64.totemtastic.util;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.util.loot.IsHardcoreLootCondition;
import broboss64.totemtastic.util.loot.ResurrectionEnabledLootCondition;
import broboss64.totemtastic.util.loot.TeleportationEnabledLootCondition;
import broboss64.totemtastic.util.loot.UndyingEnabledLootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TotemtasticLootConditions {

    public static final LootConditionType HARDCORE_ONLY =
            Registry.register(Registries.LOOT_CONDITION_TYPE, new Identifier(Totemtastic.MOD_ID, "hardcore_only"),
                    new LootConditionType(new IsHardcoreLootCondition.Serializer()));
    public static final LootConditionType RESURRECTION_ENABLED =
            Registry.register(Registries.LOOT_CONDITION_TYPE, new Identifier(Totemtastic.MOD_ID, "resurrection_enabled"),
                    new LootConditionType(new ResurrectionEnabledLootCondition.Serializer()));
    public static final LootConditionType TELEPORTATION_ENABLED =
            Registry.register(Registries.LOOT_CONDITION_TYPE, new Identifier(Totemtastic.MOD_ID, "teleportation_enabled"),
                    new LootConditionType(new TeleportationEnabledLootCondition.Serializer()));
    public static final LootConditionType UNDYING_ENABLED =
            Registry.register(Registries.LOOT_CONDITION_TYPE, new Identifier(Totemtastic.MOD_ID, "undying_enabled"),
                    new LootConditionType(new UndyingEnabledLootCondition.Serializer()));

    public static void register() {

    }
}
