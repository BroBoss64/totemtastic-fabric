package broboss64.totemtastic.effect.custom;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.util.ReviveState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.world.ServerWorld;

import java.util.UUID;

public class UmbraMortisEffect extends StatusEffect {

    public UmbraMortisEffect() {
        super(StatusEffectCategory.HARMFUL, 0x260808);
    }

    

    private static final UUID healthReductionUUID = UUID.fromString("4b9e0629-0419-4010-92f8-a763b609730b");

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        EntityAttributeInstance maxHealth = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (maxHealth == null) {
            return;
        }
        maxHealth.removeModifier(healthReductionUUID);
        double reduction = -0.2 * (amplifier + 1);
        maxHealth.addPersistentModifier(new EntityAttributeModifier(healthReductionUUID, "Reduced max health",
                reduction, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        float newMaxHealth = (float) maxHealth.getValue();
        if (entity.getHealth() > newMaxHealth) {
            entity.setHealth(newMaxHealth);
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (!entity.getWorld().isClient()) {
            EntityAttributeInstance maxHealth = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                if (maxHealth != null) {
                    maxHealth.removeModifier(healthReductionUUID);
                }
        }
    }
}
