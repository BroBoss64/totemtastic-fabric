package broboss64.totemtastic.util.loot;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.util.TotemtasticLootConditions;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.JsonSerializer;

public class ResurrectionEnabledLootCondition implements LootCondition {
    @Override
    public LootConditionType getType() {
        return TotemtasticLootConditions.RESURRECTION_ENABLED;
    }

    @Override
    public boolean test(LootContext lootContext) {
        return !Totemtastic.CONFIG.resurrectionTotemDisabled;
    }

    public static class Serializer implements JsonSerializer<ResurrectionEnabledLootCondition> {

        @Override
        public void toJson(JsonObject json, ResurrectionEnabledLootCondition object, JsonSerializationContext context) {
        }

        @Override
        public ResurrectionEnabledLootCondition fromJson(JsonObject json, JsonDeserializationContext context) {
            return new ResurrectionEnabledLootCondition();
        }
    }

}
