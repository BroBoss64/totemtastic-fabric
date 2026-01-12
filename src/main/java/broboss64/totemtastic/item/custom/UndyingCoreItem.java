package broboss64.totemtastic.item.custom;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.config.TotemtasticClientSyncedConfig;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UndyingCoreItem extends Item {
    public UndyingCoreItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        if (world.isClient()) return;
        if (Totemtastic.CONFIG.craftableTotemOfUndying) return;
        //returns the items used to craft, so the player doesn't lose anything if they try to craft it
        //i cant disable recipes from being registered in code so i have to do this bad workaround
        ItemStack returnGold = new ItemStack(Items.GOLD_INGOT, 4);
        ItemStack returnEchoShard = new ItemStack(Items.ECHO_SHARD, 4);
        ItemStack returnEmerald = new ItemStack(Items.EMERALD, 1);
        stack.decrement(1);
        player.sendMessage(Text.translatable("tooltip.totemtastic.item_disabled").formatted(Formatting.RED));
        player.giveItemStack(returnGold);
        player.giveItemStack(returnEchoShard);
        player.giveItemStack(returnEmerald);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (TotemtasticClientSyncedConfig.craftableTotemOfUndying) {
            tooltip.add(Text.translatable("item.totemtastic.undying_core.desc").formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.translatable("tooltip.totemtastic.item_disabled").formatted(Formatting.RED));
        }
    }
}
