package broboss64.totemtastic.advancement;

import broboss64.totemtastic.Totemtastic;
import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ResurrectPlayerCriterion extends AbstractCriterion {
    public static final Identifier ID = new Identifier(Totemtastic.MOD_ID, "resurrect_player");

    @Override
    protected AbstractCriterionConditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new Conditions(ID, LootContextPredicate.EMPTY);
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, conditions -> true);
    }

    public static class Conditions extends AbstractCriterionConditions {
        public Conditions(Identifier id, LootContextPredicate playerPredicate) {
            super(id, playerPredicate);
        }
    }
}
