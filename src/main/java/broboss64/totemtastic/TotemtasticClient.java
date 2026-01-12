package broboss64.totemtastic;

import broboss64.totemtastic.config.TotemtasticClientSyncedConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class TotemtasticClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(Totemtastic.CONFIG_SYNC_IDENTIFIER, (client, handler, buf, sender) -> {
            boolean crudeResurrectionTotemDisabled = buf.readBoolean();
            boolean resurrectionTotemDisabled = buf.readBoolean();
            boolean umbraMortisDisabled = buf.readBoolean();
            boolean craftableTotemOfUndying = buf.readBoolean();
            boolean wormholeTotemDisabled = buf.readBoolean();
            boolean recallTotemDisabled = buf.readBoolean();

            client.execute(() -> {
                Totemtastic.LOGGER.info("Syncing config with server");
                TotemtasticClientSyncedConfig.crudeResurrectionTotemDisabled = crudeResurrectionTotemDisabled;
                if (Totemtastic.CONFIG.devMode) Totemtastic.LOGGER.info(String.valueOf(TotemtasticClientSyncedConfig.crudeResurrectionTotemDisabled));
                TotemtasticClientSyncedConfig.resurrectionTotemDisabled = resurrectionTotemDisabled;
                if (Totemtastic.CONFIG.devMode) Totemtastic.LOGGER.info(String.valueOf(TotemtasticClientSyncedConfig.resurrectionTotemDisabled));
                TotemtasticClientSyncedConfig.umbraMortisDisabled = umbraMortisDisabled;
                if (Totemtastic.CONFIG.devMode) Totemtastic.LOGGER.info(String.valueOf(TotemtasticClientSyncedConfig.umbraMortisDisabled));
                TotemtasticClientSyncedConfig.craftableTotemOfUndying = craftableTotemOfUndying;
                if (Totemtastic.CONFIG.devMode) Totemtastic.LOGGER.info(String.valueOf(TotemtasticClientSyncedConfig.craftableTotemOfUndying));
                TotemtasticClientSyncedConfig.wormholeTotemDisabled = wormholeTotemDisabled;
                if (Totemtastic.CONFIG.devMode) Totemtastic.LOGGER.info(String.valueOf(TotemtasticClientSyncedConfig.wormholeTotemDisabled));
                TotemtasticClientSyncedConfig.recallTotemDisabled = recallTotemDisabled;
                if (Totemtastic.CONFIG.devMode) Totemtastic.LOGGER.info(String.valueOf(TotemtasticClientSyncedConfig.recallTotemDisabled));
            });
        });
    }
}
