package broboss64.totemtastic.config;

import broboss64.totemtastic.Totemtastic;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class TotemtasticConfig {
    //this is the server side config, should almost always call this on the server, and use TotemtasticClientSyncedConfig on client
    public static final ConfigClassHandler<TotemtasticConfig> HANDLER =
            ConfigClassHandler.createBuilder(TotemtasticConfig.class).id(new Identifier(Totemtastic.MOD_ID, "config"))
                    .serializer(totemtasticConfigConfigClassHandler -> GsonConfigSerializerBuilder.create(totemtasticConfigConfigClassHandler)
                            .setPath(FabricLoader.getInstance().getConfigDir().resolve("totemtastic.json5")).setJson5(true).build()).build();

    @SerialEntry(comment = "Disables the Crude Totem of Resurrection")
    public boolean crudeResurrectionTotemDisabled = false;
    @SerialEntry(comment = "Disables the Totem of Resurrection")
    public boolean resurrectionTotemDisabled = false;
    @SerialEntry(comment = "Disables the Umbra Mortis status effect")
    public boolean umbraMortisDisabled = false;

    @SerialEntry(comment = "Controls if the Undying Smithing Template generates, and whether you can craft the Undying Core.")
    public boolean craftableTotemOfUndying = true;

    @SerialEntry(comment = "Disables the Wormhole Totem")
    public boolean wormholeTotemDisabled = false;
    @SerialEntry(comment = "Consumes Wormhole Totem on use")
    public boolean wormholeTotemConsumed = true;

    @SerialEntry(comment = "Disables the Recall Totem")
    public boolean recallTotemDisabled = false;
    @SerialEntry(comment = "Consumes Recall Totem on use")
    public boolean recallTotemConsumed = true;

    @SerialEntry(comment = "Enables extra logging, probably shouldn't enable.")
    public boolean devMode = false;
}
