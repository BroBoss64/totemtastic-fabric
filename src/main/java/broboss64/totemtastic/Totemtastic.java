package broboss64.totemtastic;

import broboss64.totemtastic.item.TotemtasticItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Totemtastic implements ModInitializer {
	public static final String MOD_ID = "totemtastic";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final String PLAYER_UUID_KEY = "OwnerUUID";

	@Override
	public void onInitialize() {
        TotemtasticItems.registerModItems();

		LOGGER.info("Totemtastic loading!");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(TotemtasticItems::addItemsToTab);
	}
}