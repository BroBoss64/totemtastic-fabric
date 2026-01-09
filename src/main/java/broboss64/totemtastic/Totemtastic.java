package broboss64.totemtastic;

import broboss64.totemtastic.config.TotemtasticConfig;
import broboss64.totemtastic.effect.custom.UmbraMortisEffect;
import broboss64.totemtastic.item.TotemtasticItemGroup;
import broboss64.totemtastic.item.TotemtasticItems;
import broboss64.totemtastic.util.ReviveState;
import broboss64.totemtastic.util.TotemtasticLootConditions;
import broboss64.totemtastic.util.TotemtasticLootTableModifiers;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Totemtastic implements ModInitializer {
	public static final String MOD_ID = "totemtastic";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static TotemtasticConfig CONFIG;

    public static final String PLAYER_UUID_KEY = "OwnerUUID";
    public static final String POSITION_KEY = "linkedPosition";
    public static final Identifier CONFIG_SYNC_IDENTIFIER = new Identifier(MOD_ID, "config_sync");

    public static final StatusEffect UMBRA_MORTIS = new UmbraMortisEffect();

	@Override
	public void onInitialize() {
        TotemtasticConfig.HANDLER.load();
        CONFIG = TotemtasticConfig.HANDLER.instance();
        LOGGER.info("Totemtastic loading!");
        if (TotemtasticConfig.HANDLER.instance().devMode) LOGGER.info("Developer mode enabled!");
        //client/server config sync
        ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            PacketByteBuf buf = PacketByteBufs.create();
            //config values to sync
            buf.writeBoolean(CONFIG.crudeResurrectionTotemDisabled);
            buf.writeBoolean(CONFIG.resurrectionTotemDisabled);
            buf.writeBoolean(CONFIG.umbraMortisDisabled);
            buf.writeBoolean(CONFIG.wormholeTotemDisabled);
            buf.writeBoolean(CONFIG.recallTotemDisabled);

            ServerPlayNetworking.send(player, CONFIG_SYNC_IDENTIFIER, buf);
        }));
        //registering stuff
        TotemtasticItemGroup.registerItemGroup();
        TotemtasticItems.registerModItems();
        TotemtasticLootTableModifiers.modifyLootTables();
        Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "umbra_mortis"), UMBRA_MORTIS);
        TotemtasticLootConditions.register();

        //umbra mortis reapplier method
        ServerTickEvents.END_SERVER_TICK.register(minecraftServer -> {
            for (ServerPlayerEntity player : minecraftServer.getPlayerManager().getPlayerList()) {
                ReviveState state = ReviveState.get(player.getServerWorld());
                int reviveCount = state.getReviveCount(player.getUuid());
                if (reviveCount <= 0) continue;
                if (!player.isAlive()) {
                    player.removeStatusEffect(Totemtastic.UMBRA_MORTIS);
                    continue;
                }
                if (player.hasStatusEffect(Totemtastic.UMBRA_MORTIS)) continue;
                player.addStatusEffect(new StatusEffectInstance(Totemtastic.UMBRA_MORTIS, Integer.MAX_VALUE,
                        reviveCount - 1, true, false, false));
            }
        });
	}
}