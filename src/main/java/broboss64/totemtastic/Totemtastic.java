package broboss64.totemtastic;

import broboss64.totemtastic.effect.custom.UmbraMortisEffect;
import broboss64.totemtastic.item.TotemtasticItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
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
	}
}