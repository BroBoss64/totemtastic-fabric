package broboss64.totemtastic.util;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.util.loot.IsHardcoreLootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TotemtasticLootConditions {

    public static final LootConditionType HARDCORE_ONLY =
            Registry.register(Registries.LOOT_CONDITION_TYPE, new Identifier(Totemtastic.MOD_ID, "hardcore_only"),
                    new LootConditionType(new IsHardcoreLootCondition.Serializer()));

    public static void register() {

    }
}
