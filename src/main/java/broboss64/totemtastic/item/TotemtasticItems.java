package broboss64.totemtastic.item;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.item.custom.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class TotemtasticItems {
    //totem shells
    public static final Item WOOD_TOTEM_SHELL = registerItem("wooden_totem_shell", new TotemShellItem(new FabricItemSettings().maxCount(1)));
    public static final Item GOLD_TOTEM_SHELL = registerItem("golden_totem_shell", new TotemShellItem(new FabricItemSettings().maxCount(1)));
    public static final Item QUARTZ_TOTEM_SHELL = registerItem("quartz_totem_shell", new TotemShellItem(new FabricItemSettings().maxCount(1)));
    public static final Item LAPIS_TOTEM_SHELL = registerItem("lapis_totem_shell", new TotemShellItem(new FabricItemSettings().maxCount(1)));
    //totem cores
    public static final Item RESURRECTION_CORE = registerItem("resurrection_core", new ResurrectionCoreItem(new FabricItemSettings()));
    public static final Item TELEPORTATION_CORE = registerItem("teleportation_core", new TeleportationCoreItem(new FabricItemSettings()));
    public static final Item UNDYING_CORE = registerItem("undying_core", new UndyingCoreItem(new FabricItemSettings()));
    //totem items
    public static final Item CRUDE_RESURRECTION_TOTEM = registerItem("crude_resurrection_totem", new Item(new FabricItemSettings().maxCount(1)));
    public static final Item RESURRECTION_TOTEM = registerItem("resurrection_totem", new ResurrectionTotemItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE)));
    public static final Item WORMHOLE_TOTEM = registerItem("wormhole_totem", new WormholeTotemItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE)));
    public static final Item RECALL_TOTEM = registerItem("recall_totem", new RecallTotemItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE)));
    //misc
    public static final Item IRON_KNIFE = registerItem("iron_knife", new KnifeItem(new FabricItemSettings().maxCount(1)));
    public static final Item GLASS_VIAL = registerItem("glass_vial", new Item(new FabricItemSettings().maxCount(16)));
    public static final Item BLOOD_VIAL = registerItem("blood_vial", new BloodVialItem(new FabricItemSettings().maxCount(1)));
    public static final Item HWUMBOID = registerItem("hwumboid", new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC)));

    public static void addItemsToTab(FabricItemGroupEntries entries) {
        entries.add(WOOD_TOTEM_SHELL);
        entries.add(GOLD_TOTEM_SHELL);
        entries.add(QUARTZ_TOTEM_SHELL);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Totemtastic.MOD_ID, name), item);
    }

    public static void registerModItems() {

    }

}
