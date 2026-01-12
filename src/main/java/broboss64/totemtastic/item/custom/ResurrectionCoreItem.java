package broboss64.totemtastic.item.custom;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.config.TotemtasticClientSyncedConfig;
import broboss64.totemtastic.item.TotemtasticItems;
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

public class ResurrectionCoreItem extends Item {
    public ResurrectionCoreItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        if (world.isClient()) return;
        if (!Totemtastic.CONFIG.resurrectionTotemDisabled || !Totemtastic.CONFIG.crudeResurrectionTotemDisabled) return;
        //returns the items used to craft, so the player doesn't lose anything if they try to craft it
        //i cant disable recipes from being registered in code so i have to do this bad workaround
        ItemStack returnCopper = new ItemStack(Items.COPPER_INGOT, 4);
        ItemStack returnRedstone = new ItemStack(Items.REDSTONE, 4);
        ItemStack returnGlassVial = new ItemStack(TotemtasticItems.GLASS_VIAL, 1);
        stack.decrement(1);
        player.sendMessage(Text.translatable("tooltip.totemtastic.item_disabled").formatted(Formatting.RED));
        player.giveItemStack(returnCopper);
        player.giveItemStack(returnRedstone);
        player.giveItemStack(returnGlassVial);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (!TotemtasticClientSyncedConfig.resurrectionTotemDisabled || !TotemtasticClientSyncedConfig.crudeResurrectionTotemDisabled) {
            tooltip.add(Text.translatable("item.totemtastic.resurrection_core.desc").formatted(Formatting.GRAY));
        } else tooltip.add(Text.translatable("tooltip.totemtastic.item_disabled").formatted(Formatting.RED));
    }
}
