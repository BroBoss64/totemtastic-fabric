package broboss64.totemtastic.item;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.config.TotemtasticConfig;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TotemtasticItemGroup {
    public static final ItemGroup TOTEMTASTIC_GROUP = Registry.register(Registries.ITEM_GROUP, new Identifier(Totemtastic.MOD_ID, "totemtastic_group"),
            FabricItemGroup.builder().displayName(Text.literal("Totemtastic"))
                    .icon(() -> new ItemStack(TotemtasticItems.RESURRECTION_TOTEM)).entries((displayContext, entries) -> {
                        entries.add(TotemtasticItems.FLINT_KNIFE);
                        entries.add(TotemtasticItems.GOLD_KNIFE);
                        entries.add(TotemtasticItems.IRON_KNIFE);
                        entries.add(TotemtasticItems.DIAMOND_KNIFE);
                        entries.add(TotemtasticItems.NETHERITE_KNIFE);

                        entries.add(TotemtasticItems.GLASS_VIAL);

                        entries.add(TotemtasticItems.WOOD_TOTEM_SHELL);
                        entries.add(TotemtasticItems.GOLD_TOTEM_SHELL);
                        entries.add(TotemtasticItems.QUARTZ_TOTEM_SHELL);
                        entries.add(TotemtasticItems.LAPIS_TOTEM_SHELL);

                        if (!Totemtastic.CONFIG.crudeResurrectionTotemDisabled || !Totemtastic.CONFIG.resurrectionTotemDisabled) entries.add(TotemtasticItems.RESURRECTION_CORE);
                        if (!Totemtastic.CONFIG.wormholeTotemDisabled || !Totemtastic.CONFIG.recallTotemDisabled) entries.add(TotemtasticItems.TELEPORTATION_CORE);
                        entries.add(TotemtasticItems.UNDYING_CORE);

                        if (!Totemtastic.CONFIG.crudeResurrectionTotemDisabled || !Totemtastic.CONFIG.resurrectionTotemDisabled) entries.add(TotemtasticItems.RESURRECTION_SMITHING_TEMPLATE);
                        if (!Totemtastic.CONFIG.wormholeTotemDisabled || !Totemtastic.CONFIG.recallTotemDisabled) entries.add(TotemtasticItems.TELEPORTATION_SMITHING_TEMPLATE);
                        entries.add(TotemtasticItems.UNDYING_SMITHING_TEMPLATE);

                        if (!Totemtastic.CONFIG.crudeResurrectionTotemDisabled) entries.add(TotemtasticItems.CRUDE_RESURRECTION_TOTEM);
                        if (!Totemtastic.CONFIG.resurrectionTotemDisabled) entries.add(TotemtasticItems.RESURRECTION_TOTEM);
                        if (!Totemtastic.CONFIG.wormholeTotemDisabled) entries.add(TotemtasticItems.WORMHOLE_TOTEM);
                        if (!Totemtastic.CONFIG.recallTotemDisabled) entries.add(TotemtasticItems.RECALL_TOTEM);
                    }).build());

    public static void registerItemGroup() {
    }
}
