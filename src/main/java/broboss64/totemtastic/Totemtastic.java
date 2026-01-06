package broboss64.totemtastic;

import broboss64.totemtastic.effect.custom.UmbraMortisEffect;
import broboss64.totemtastic.item.TotemtasticItems;
import broboss64.totemtastic.item.custom.KnifeItem;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Totemtastic implements ModInitializer {
	public static final String MOD_ID = "totemtastic";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final String PLAYER_UUID_KEY = "OwnerUUID";
    public static final String POSITION_KEY = "linkedPosition";

    public static final StatusEffect UMBRA_MORTIS = new UmbraMortisEffect();

	@Override
	public void onInitialize() {
        LOGGER.info("Totemtastic loading!");

        TotemtasticItems.registerModItems();
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(TotemtasticItems::addItemsToTab);

        Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "umbra_mortis"), UMBRA_MORTIS);

        //uncomment when fixed
        /*AttackEntityCallback.EVENT.register((playerEntity, world, hand, entity, entityHitResult) -> {
            if (!(entity instanceof LivingEntity target)) return ActionResult.PASS;
            if (playerEntity.getMainHandStack().getItem() instanceof KnifeItem) {
                target.damage(new DamageSource(playerEntity.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(KnifeItem.STABBING)), (float) playerEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
                double dx = target.getX() - playerEntity.getX();
                double dz = target.getZ() - playerEntity.getZ();
                double distance = Math.sqrt(dx*dx+dz*dz);
                if (distance != 0) {
                    dx /= distance;
                    dz /= distance;
                }
                double strength = playerEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
                target.addVelocity(dx * strength, 0.1, dz * strength);
                target.velocityModified = true;
                return ActionResult.PASS;
            }
            return ActionResult.PASS;
        });*/
	}
}