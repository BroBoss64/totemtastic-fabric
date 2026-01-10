package broboss64.totemtastic.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeleportationCoreItem extends Item {
    public TeleportationCoreItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.totemtastic.teleportation_core.desc").formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
