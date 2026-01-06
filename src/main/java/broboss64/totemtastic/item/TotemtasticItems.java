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
    public static final Item CRUDE_RESURRECTION_TOTEM = registerItem("crude_resurrection_totem", new CrudeResurrectionTotemItem(new FabricItemSettings().maxCount(1)));
    public static final Item RESURRECTION_TOTEM = registerItem("resurrection_totem", new ResurrectionTotemItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE)));
    public static final Item WORMHOLE_TOTEM = registerItem("wormhole_totem", new WormholeTotemItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE)));
    public static final Item RECALL_TOTEM = registerItem("recall_totem", new RecallTotemItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE)));
    //misc
    public static final Item FLINT_KNIFE = registerItem("flint_knife", new KnifeItem(new FabricItemSettings().maxCount(1).maxDamage(59), 1.5f, -2.0f));
    public static final Item IRON_KNIFE = registerItem("iron_knife", new KnifeItem(new FabricItemSettings().maxCount(1).maxDamage(250), 3.0f, -2.0f));
    public static final Item DIAMOND_KNIFE = registerItem("diamond_knife", new KnifeItem(new FabricItemSettings().maxCount(1).maxDamage(1561), 5.0f, -2.0f));
    public static final Item NETHERITE_KNIFE = registerItem("netherite_knife", new KnifeItem(new FabricItemSettings().maxCount(1).maxDamage(2031), 6.0f, -2.0f));
    public static final Item GOLD_KNIFE = registerItem("gold_knife", new KnifeItem(new FabricItemSettings().maxCount(1).maxDamage(32), 2.0f, -2.0f));
    public static final Item GLASS_VIAL = registerItem("glass_vial", new Item(new FabricItemSettings().maxCount(16)));
    public static final Item BLOOD_VIAL = registerItem("blood_vial", new BloodVialItem(new FabricItemSettings().maxCount(1)));
    public static final Item HWUMBOID = registerItem("hwumboid", new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC)));

    public static void addItemsToTab(FabricItemGroupEntries entries) {
        entries.add(GLASS_VIAL);
        //knives
        entries.add(FLINT_KNIFE);
        entries.add(GOLD_KNIFE);
        entries.add(IRON_KNIFE);
        entries.add(DIAMOND_KNIFE);
        entries.add(NETHERITE_KNIFE);
        //totem components
        entries.add(WOOD_TOTEM_SHELL);
        entries.add(GOLD_TOTEM_SHELL);
        entries.add(QUARTZ_TOTEM_SHELL);
        entries.add(LAPIS_TOTEM_SHELL);
        entries.add(RESURRECTION_CORE);
        entries.add(TELEPORTATION_CORE);
        entries.add(UNDYING_CORE);
        //totems
        entries.add(CRUDE_RESURRECTION_TOTEM);
        entries.add(RESURRECTION_TOTEM);
        entries.add(WORMHOLE_TOTEM);
        entries.add(RECALL_TOTEM);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Totemtastic.MOD_ID, name), item);
    }

    public static void registerModItems() {

    }

}
