package broboss64.totemtastic.item.custom;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.item.TotemtasticItems;
import broboss64.totemtastic.util.TotemtasticItemUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class BloodVialItem extends Item {

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
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains(Totemtastic.PLAYER_UUID_KEY)) {
            UUID totemUUID = TotemtasticItemUtils.fetchUUIDFromItemStack(stack);
            MinecraftClient client = MinecraftClient.getInstance();
            PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(totemUUID);
            if (entry != null) {
                tooltip.add(Text.literal(entry.getProfile().getName()).formatted(Formatting.GRAY));
            } else {
                tooltip.add(Text.literal("Offline Player").formatted(Formatting.GRAY));
            }

        } else {
            tooltip.add(Text.literal("Not bound to a player").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("Hold a blood vial in the offhand and use to bind").formatted(Formatting.GRAY));
        }
    }

    public BloodVialItem(Settings settings) {
        super(settings);
    }
}
