package broboss64.totemtastic.util.loot;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.util.TotemtasticLootConditions;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.JsonSerializer;

public class UndyingEnabledLootCondition implements LootCondition {
    @Override
    public LootConditionType getType() {
        return TotemtasticLootConditions.UNDYING_ENABLED;
    }

    @Override
    public boolean test(LootContext lootContext) {
        return Totemtastic.CONFIG.craftableTotemOfUndying;
    }

    public static class Serializer implements JsonSerializer<UndyingEnabledLootCondition> {

        @Override
        public void toJson(JsonObject json, UndyingEnabledLootCondition object, JsonSerializationContext context) {
        }

        @Override
        public UndyingEnabledLootCondition fromJson(JsonObject json, JsonDeserializationContext context) {
            return new UndyingEnabledLootCondition();
        }
    }

}
