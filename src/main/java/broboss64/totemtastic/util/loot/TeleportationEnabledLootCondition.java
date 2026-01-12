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

public class TeleportationEnabledLootCondition implements LootCondition {
    @Override
    public LootConditionType getType() {
        return TotemtasticLootConditions.TELEPORTATION_ENABLED;
    }

    @Override
    public boolean test(LootContext lootContext) {
        if (Totemtastic.CONFIG.recallTotemDisabled && Totemtastic.CONFIG.wormholeTotemDisabled) {
            return false;
        } else return true;
    }

    public static class Serializer implements JsonSerializer<TeleportationEnabledLootCondition> {

        @Override
        public void toJson(JsonObject json, TeleportationEnabledLootCondition object, JsonSerializationContext context) {
        }

        @Override
        public TeleportationEnabledLootCondition fromJson(JsonObject json, JsonDeserializationContext context) {
            return new TeleportationEnabledLootCondition();
        }
    }

}
