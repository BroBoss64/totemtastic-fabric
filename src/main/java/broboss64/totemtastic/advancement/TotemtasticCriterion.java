package broboss64.totemtastic.advancement;

import net.minecraft.advancement.criterion.Criteria;

public class TotemtasticCriterion {
    public static final ResurrectPlayerCrudeCriterion RESURRECT_PLAYER_CRUDE_CRITERION = Criteria.register(new ResurrectPlayerCrudeCriterion());
    public static final ResurrectPlayerCriterion RESURRECT_PLAYER_CRITERION = Criteria.register(new ResurrectPlayerCriterion());

    public static void register() {

    }
}
