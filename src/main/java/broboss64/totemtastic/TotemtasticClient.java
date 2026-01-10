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
            boolean wormholeTotemDisabled = buf.readBoolean();
            boolean recallTotemDisabled = buf.readBoolean();

            client.execute(() -> {
                Totemtastic.LOGGER.info("Syncing config with server");
                TotemtasticClientSyncedConfig.crudeResurrectionTotemDisabled = crudeResurrectionTotemDisabled;
                Totemtastic.LOGGER.info(String.valueOf(TotemtasticClientSyncedConfig.crudeResurrectionTotemDisabled));
                TotemtasticClientSyncedConfig.resurrectionTotemDisabled = resurrectionTotemDisabled;
                Totemtastic.LOGGER.info(String.valueOf(TotemtasticClientSyncedConfig.resurrectionTotemDisabled));
                TotemtasticClientSyncedConfig.umbraMortisDisabled = umbraMortisDisabled;
                Totemtastic.LOGGER.info(String.valueOf(TotemtasticClientSyncedConfig.umbraMortisDisabled));
                TotemtasticClientSyncedConfig.wormholeTotemDisabled = wormholeTotemDisabled;
                Totemtastic.LOGGER.info(String.valueOf(TotemtasticClientSyncedConfig.wormholeTotemDisabled));
                TotemtasticClientSyncedConfig.recallTotemDisabled = recallTotemDisabled;
                Totemtastic.LOGGER.info(String.valueOf(TotemtasticClientSyncedConfig.recallTotemDisabled));
            });
        });
    }
}
