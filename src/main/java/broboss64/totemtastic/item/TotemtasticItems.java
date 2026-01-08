package broboss64.totemtastic.item;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.item.custom.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.List;

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
    //smithing templates
    public static final Item RESURRECTION_SMITHING_TEMPLATE = registerItem("resurrection_smithing_template", new SmithingTemplateItem(Text.literal("Quartz Totem Shell"), Text.literal("Resurrection Core"), Text.literal("Totem of Resurrection Forging"), Text.literal("Quartz Totem Shell"), Text.literal("Resurrection Core"), List.of(new Identifier(Totemtastic.MOD_ID, "item/empty_slot_totem_shell")), List.of(new Identifier(Totemtastic.MOD_ID, "item/empty_slot_totem_core"))));
    public static final Item TELEPORTATION_SMITHING_TEMPLATE = registerItem("teleportation_smithing_template", new SmithingTemplateItem(Text.literal("Lapis/Golden Totem Shell"), Text.literal("Teleportation Core"), Text.literal("Wormhole/Recall Totem Forging"), Text.literal("Lapis/Golden Totem Shell"), Text.literal("Teleportation Core"), List.of(new Identifier(Totemtastic.MOD_ID, "item/empty_slot_totem_shell")), List.of(new Identifier(Totemtastic.MOD_ID, "item/empty_slot_totem_core"))));
    public static final Item UNDYING_SMITHING_TEMPLATE = registerItem("undying_smithing_template", new SmithingTemplateItem(Text.literal("Golden Totem Shell"), Text.literal("Undying Core"), Text.literal("Totem of Undying Forging"), Text.literal("Golden Totem Shell"), Text.literal("Undying Core"), List.of(new Identifier(Totemtastic.MOD_ID, "item/empty_slot_totem_shell")), List.of(new Identifier(Totemtastic.MOD_ID, "item/empty_slot_totem_core"))));
    //totem items
    public static final Item CRUDE_RESURRECTION_TOTEM = registerItem("crude_resurrection_totem", new CrudeResurrectionTotemItem(new FabricItemSettings().maxCount(1)));
    public static final Item RESURRECTION_TOTEM = registerItem("resurrection_totem", new ResurrectionTotemItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE)));
    public static final Item WORMHOLE_TOTEM = registerItem("wormhole_totem", new WormholeTotemItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE)));
    public static final Item RECALL_TOTEM = registerItem("recall_totem", new RecallTotemItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE)));
    //kives
    public static final Item FLINT_KNIFE = registerItem("flint_knife", new KnifeItem(new FabricItemSettings().maxCount(1).maxDamage(59), 1.5f, -2.0f));
    public static final Item IRON_KNIFE = registerItem("iron_knife", new KnifeItem(new FabricItemSettings().maxCount(1).maxDamage(250), 3.0f, -2.0f));
    public static final Item DIAMOND_KNIFE = registerItem("diamond_knife", new KnifeItem(new FabricItemSettings().maxCount(1).maxDamage(1561), 5.0f, -2.0f));
    public static final Item NETHERITE_KNIFE = registerItem("netherite_knife", new KnifeItem(new FabricItemSettings().maxCount(1).maxDamage(2031), 6.0f, -2.0f));
    public static final Item GOLD_KNIFE = registerItem("gold_knife", new KnifeItem(new FabricItemSettings().maxCount(1).maxDamage(32), 2.0f, -2.0f));
    //misc
    public static final Item GLASS_VIAL = registerItem("glass_vial", new Item(new FabricItemSettings().maxCount(16)));
    public static final Item BLOOD_VIAL = registerItem("blood_vial", new BloodVialItem(new FabricItemSettings().maxCount(1)));
    public static final Item HWUMBOID = registerItem("hwumboid", new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Totemtastic.MOD_ID, name), item);
    }

    public static void registerModItems() {}
}
