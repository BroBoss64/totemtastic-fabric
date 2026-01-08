package broboss64.totemtastic.util;

import broboss64.totemtastic.item.TotemtasticItems;
import broboss64.totemtastic.util.loot.IsHardcoreLootCondition;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

public class TotemtasticLootTableModifiers {
    private static final Identifier WOODLAND_MANSION_CHEST_ID = new Identifier("minecraft", "chests/woodland_mansion");
    private static final Identifier FORTRESS_CHEST_ID = new Identifier("minecraft", "chests/nether_bridge");
    private static final Identifier STRONGHOLD_CORRIDOR_CHEST_ID = new Identifier("minecraft", "chests/stronghold_corridor");
    private static final Identifier TREASURE_BASTION_CHEST_ID = new Identifier("minecraft", "chests/bastion_treasure");
    private static final Identifier ANCIENT_CITY_CHEST_ID = new Identifier("minecraft", "chests/ancient_city");
    private static final Identifier RUINED_PORTAL_CHEST_ID = new Identifier("minecraft", "chests/ruined_portal");

    private static final Identifier EVOKER_ENTITY_ID = new Identifier("minecraft", "entities/evoker");

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, identifier, builder, lootTableSource) ->{
            if (WOODLAND_MANSION_CHEST_ID.equals(identifier)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.4f))
                        .with(ItemEntry.builder(TotemtasticItems.UNDYING_SMITHING_TEMPLATE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                builder.pool(poolBuilder.build());
            }
            if (FORTRESS_CHEST_ID.equals(identifier)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(IsHardcoreLootCondition::new)
                        .conditionally(RandomChanceLootCondition.builder(0.2f))
                        .with(ItemEntry.builder(TotemtasticItems.RESURRECTION_SMITHING_TEMPLATE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                builder.pool(poolBuilder.build());
            }
            if (TREASURE_BASTION_CHEST_ID.equals(identifier)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(IsHardcoreLootCondition::new)
                        .conditionally(RandomChanceLootCondition.builder(0.8f))
                        .with(ItemEntry.builder(TotemtasticItems.RESURRECTION_SMITHING_TEMPLATE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                builder.pool(poolBuilder.build());
            }
            if (FORTRESS_CHEST_ID.equals(identifier)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.1f))
                        .with(ItemEntry.builder(TotemtasticItems.TELEPORTATION_SMITHING_TEMPLATE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                builder.pool(poolBuilder.build());
            }
            if (STRONGHOLD_CORRIDOR_CHEST_ID.equals(identifier)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.2f))
                        .with(ItemEntry.builder(TotemtasticItems.TELEPORTATION_SMITHING_TEMPLATE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                builder.pool(poolBuilder.build());
            }
            if (ANCIENT_CITY_CHEST_ID.equals(identifier)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.4f))
                        .with(ItemEntry.builder(TotemtasticItems.TELEPORTATION_SMITHING_TEMPLATE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                builder.pool(poolBuilder.build());
            }
            if (RUINED_PORTAL_CHEST_ID.equals(identifier)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.1f))
                        .with(ItemEntry.builder(TotemtasticItems.GOLD_KNIFE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                builder.pool(poolBuilder.build());
            }

            if (EVOKER_ENTITY_ID.equals(identifier)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.1f))
                        .with(ItemEntry.builder(TotemtasticItems.UNDYING_SMITHING_TEMPLATE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                builder.pool(poolBuilder.build());
            }
        });
    }
}
