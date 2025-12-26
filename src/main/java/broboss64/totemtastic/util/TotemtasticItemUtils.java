package broboss64.totemtastic.util;

import broboss64.totemtastic.Totemtastic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.UUID;

public class TotemtasticItemUtils {

    public static ItemStack createBoundItemStack(Item item, Integer count, UUID itemUUID) {
        if (count == null || count == 0) {
            count = 1;
        }
        ItemStack newStack = new ItemStack(item, count);
        NbtCompound nbt = newStack.getOrCreateNbt();
        nbt.putString(Totemtastic.PLAYER_UUID_KEY, itemUUID.toString());
        return newStack;
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
        ServerPlayerEntity player = server.getPlayerManager().getPlayer(playerUUID);
        if (player != null) {
            return player.getName().getString();
        } else {
            String fallback = playerUUID.toString() + " (offline player)";
            return fallback;
        }
    }
    public static PlayerEntity getPlayerEntityFromUUID(ServerWorld world, UUID uuid) {
        Entity entity = world.getEntity(uuid);
        if (entity != null) {
            return (PlayerEntity) entity;
        }else {
            return null;
        }
    }
}
