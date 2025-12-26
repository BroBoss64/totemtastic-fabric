package broboss64.totemtastic.item.custom;

import broboss64.totemtastic.item.TotemtasticItems;
import broboss64.totemtastic.util.TotemtasticItemUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class BloodVialItem extends Item {
    private String playerUUIDString = "";

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack mainHandItemStack = player.getMainHandStack();
        if (!world.isClient() && mainHandItemStack.getItem() == TotemtasticItems.BLOOD_VIAL) {
           UUID holderUUID = TotemtasticItemUtils.fetchUUIDFromItemStack(mainHandItemStack);
           if (holderUUID == null) {
               player.sendMessage(Text.literal("Invalid Player UUID!"), true);
           } else {
               String holderName = TotemtasticItemUtils.getUsernameFromUUID(holderUUID, world.getServer());
               player.sendMessage(Text.literal(holderName), true);
           }
        } else if (player.getOffHandStack().getItem() == TotemtasticItems.BLOOD_VIAL) {
            return TypedActionResult.fail(player.getOffHandStack());
        }
        return TypedActionResult.success(mainHandItemStack, true);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("§7Right Click to see who it is bound to."));
    }

    public BloodVialItem(Settings settings) {
        super(settings);
    }
}
