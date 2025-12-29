package broboss64.totemtastic.item.custom;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.item.TotemtasticItems;
import broboss64.totemtastic.util.TotemtasticItemUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class WormholeTotemItem extends Item {

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 60;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack heldStack = user.getStackInHand(hand);
        if (!world.isClient()) {
            if (hand == Hand.MAIN_HAND) {
                //only triggers if in main hand
                UUID totemUUID = TotemtasticItemUtils.fetchUUIDFromItemStack(user.getMainHandStack());
                if (totemUUID != null && user.getOffHandStack().getItem() != TotemtasticItems.BLOOD_VIAL) {
                    if (user.isSneaking()) {
                        //shows bound player name
                        String boundPlayerName = "This totem will warp to " + TotemtasticItemUtils.getUsernameFromUUID(totemUUID, world.getServer());
                        user.sendMessage(Text.literal(boundPlayerName), true);
                        user.stopUsingItem();
                        return TypedActionResult.success(user.getMainHandStack(), true);
                    } else {
                        //i dont know what this does but its very important
                        user.setCurrentHand(hand);
                    }
                } else if (user.getOffHandStack().getItem() == TotemtasticItems.BLOOD_VIAL) {
                    //binds it
                    TotemtasticItemUtils.bindTotemFromVial(user);
                    user.stopUsingItem();
                } else {
                    //cancels if bound uuid is missing or invalid
                    user.sendMessage(Text.literal("Not bound or invalid player!"), true);
                    user.stopUsingItem();
                    return TypedActionResult.success(heldStack, true);
                }

            } else {
                user.stopUsingItem();
                return TypedActionResult.fail(heldStack);
            }


        }
        return TypedActionResult.fail(heldStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient() && user instanceof  PlayerEntity) {
            UUID totemUUID = TotemtasticItemUtils.fetchUUIDFromItemStack(stack);
            PlayerEntity playerUsing = (PlayerEntity) user;
            ServerWorld serverWorld = (ServerWorld) world;
            PlayerEntity targetPlayer = TotemtasticItemUtils.getPlayerEntityFromUUID((ServerWorld) world, totemUUID);
            if (remainingUseTicks >= 1 && !user.isSneaking() && user.getOffHandStack().getItem() != TotemtasticItems.BLOOD_VIAL) {
                //runs every tick
                serverWorld.spawnParticles(ParticleTypes.PORTAL, user.getX(), user.getY() + 1, user.getZ(),
                        2, 0.25, 0.5, 0.25, 0.2);
                if (targetPlayer != null) {
                    serverWorld.spawnParticles(ParticleTypes.PORTAL, targetPlayer.getX(), targetPlayer.getY() + 1, targetPlayer.getZ(),
                            2, 0.25, 0.5, 0.25, 0.2);
                }

                if (remainingUseTicks == 1) {
                    //runs when finished using
                    warpToPlayer(world, playerUsing, totemUUID);
                    user.stopUsingItem();
                }

            } else {
                user.stopUsingItem();
            }
        }
    }

    private void warpToPlayer(World world, PlayerEntity user, UUID targetUUID) {
        PlayerEntity targetPlayer = TotemtasticItemUtils.getPlayerEntityFromUUID((ServerWorld) world, targetUUID);
        if (targetPlayer != null) {
            if (user == targetPlayer) {
                user.sendMessage(Text.literal("You can't warp to yourself!"), true);
            } else {
                ServerPlayerEntity serverUser = (ServerPlayerEntity) user;
                World targetDimension = targetPlayer.getWorld();
                double targetX = targetPlayer.getX();
                double targetY = targetPlayer.getY();
                double targetZ = targetPlayer.getZ();
                serverUser.teleport((ServerWorld) targetDimension, targetX, targetY, targetZ, targetPlayer.getYaw(), targetPlayer.getPitch());
                targetDimension.playSoundFromEntity(null, targetPlayer, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 0.6f, 1);
                user.getMainHandStack().decrement(1);
            }

        } else {
            user.sendMessage(Text.literal("Invalid Player! Is the target online?"), true);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains(Totemtastic.PLAYER_UUID_KEY)) {
            UUID totemUUID = TotemtasticItemUtils.fetchUUIDFromItemStack(stack);
            MinecraftClient client = MinecraftClient.getInstance();
            PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(totemUUID);
            if (entry != null) {
                tooltip.add(Text.literal("Will warp to " + entry.getProfile().getName()).formatted(Formatting.GRAY));
            } else {
                tooltip.add(Text.literal("Will warp to an Offline Player").formatted(Formatting.GRAY));
            }

        } else {
            tooltip.add(Text.literal("§7Not bound to a player"));
            tooltip.add(Text.literal("§7Hold a blood vial in the offhand and use to bind"));
        }
    }

    public WormholeTotemItem(Settings settings) {
        super(settings);
    }
}
