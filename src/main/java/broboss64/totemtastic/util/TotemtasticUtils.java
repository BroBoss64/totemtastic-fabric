package broboss64.totemtastic.util;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.item.TotemtasticItems;
import broboss64.totemtastic.item.custom.TotemShellItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class TotemtasticUtils {

    public static ItemStack createBoundItemStack(Item item, Integer count, UUID itemUUID) {
        if (count == null || count == 0) {
            count = 1;
        }
        ItemStack newStack = new ItemStack(item, count);
        NbtCompound nbt = newStack.getOrCreateNbt();
        nbt.putString(Totemtastic.PLAYER_UUID_KEY, itemUUID.toString());
        return newStack;
    }

    public static void createPositionLinkedTotem(BlockPos blockPos, World world, PlayerEntity player) {
        //gets the dimension id from world
        RegistryKey<World> worldKey = world.getRegistryKey();
        Identifier dimID = worldKey.getValue();
        //adds the nbt to the totem ONLY CALL WHEN TOTEM IS IN MAIN HAND
        Hand hand = player.getActiveHand();
        ItemStack totem = player.getStackInHand(hand);
        NbtCompound nbt = totem.getOrCreateNbt();
        NbtCompound coordsTag = new NbtCompound();
        //puts the nbt onto the item
        coordsTag.putInt("x", blockPos.getX());
        coordsTag.putInt("y", blockPos.getY());
        coordsTag.putInt("z", blockPos.getZ());
        coordsTag.putString("dimension", dimID.toString());
        nbt.put(Totemtastic.POSITION_KEY, coordsTag);
        player.setStackInHand(hand, totem);
        world.playSoundFromEntity(null, player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1, 1);
    }

    public static BlockPos fetchBlockPosFromItemStack(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains(Totemtastic.POSITION_KEY)) {
            NbtCompound coordsTag = nbt.getCompound(Totemtastic.POSITION_KEY);
            int x = coordsTag.getInt("x");
            int y = coordsTag.getInt("y");
            int z = coordsTag.getInt("z");
            return new BlockPos(x, y, z);
        }else {
            return null;
        }

    }

    public static ServerWorld fetchDimensionFromItemStack(ItemStack stack, MinecraftServer server) {
        //ONLY CALL THIS ON SERVER SIDE
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains(Totemtastic.POSITION_KEY)) {
            NbtCompound coordsTag = nbt.getCompound(Totemtastic.POSITION_KEY);
            String dimString = coordsTag.getString("dimension");
            Identifier dimID = new Identifier(dimString);
            RegistryKey<World> worldKey = RegistryKey.of(RegistryKeys.WORLD, dimID);
            return server.getWorld(worldKey);
        }else {
            return null;
        }

    }

    public static UUID fetchUUIDFromItemStack(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains(Totemtastic.PLAYER_UUID_KEY)) {
            try {
                return UUID.fromString(nbt.getString(Totemtastic.PLAYER_UUID_KEY));
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    public static String getUsernameFromUUID(UUID playerUUID, MinecraftServer server) {
        //ONLY CALL ON SERVER SIDE
        ServerPlayerEntity player = server.getPlayerManager().getPlayer(playerUUID);
        if (player != null) {
            return player.getName().getString();
        } else {
            return "Offline Player (" + playerUUID.toString() + ")";
        }
    }
    public static PlayerEntity getPlayerEntityFromUUID(ServerWorld world, UUID uuid) {
        //ONLY CALL ON SERVER SIDE
        return world.getServer().getPlayerManager().getPlayer(uuid);
    }

    public static void bindTotemFromVial(PlayerEntity player) {
        if (!player.getWorld().isClient()) {
            Hand hand = player.getActiveHand();
            ItemStack mainHandStack = player.getMainHandStack();
            ItemStack offHandStack = player.getOffHandStack();
            if (offHandStack.getItem() == TotemtasticItems.BLOOD_VIAL) {
                    //safeguard to make sure we never modify the wrong item stack
                    //gets the UUID from the blood vial and sets the main hand item to the new tagged item
                    UUID bloodVialUUID = fetchUUIDFromItemStack(offHandStack);
                    ItemStack taggedStack = createBoundItemStack(mainHandStack.getItem(), 1, bloodVialUUID);
                    player.getInventory().offHand.set(0, new ItemStack(TotemtasticItems.GLASS_VIAL));
                    player.getInventory().setStack(player.getInventory().selectedSlot, taggedStack);
                    player.getWorld().playSoundFromEntity(null, player, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.PLAYERS, 1, 1);
            } else {
                Totemtastic.LOGGER.error("Offhand item is not a blood vial!");
            }
        }
    }
}
