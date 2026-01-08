package broboss64.totemtastic.util.loot;

import broboss64.totemtastic.util.TotemtasticLootConditions;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.JsonSerializer;

public class IsHardcoreLootCondition implements LootCondition {
    @Override
    public LootConditionType getType() {
        return TotemtasticLootConditions.HARDCORE_ONLY;
    }

    @Override
    public boolean test(LootContext lootContext) {
        ServerWorld world = lootContext.getWorld();
        return world.getLevelProperties().isHardcore();
    }

    public static class Serializer implements JsonSerializer<IsHardcoreLootCondition> {

        @Override
        public void toJson(JsonObject json, IsHardcoreLootCondition object, JsonSerializationContext context) {
        }

        @Override
        public IsHardcoreLootCondition fromJson(JsonObject json, JsonDeserializationContext context) {
            return new IsHardcoreLootCondition();
        }
    }

}
