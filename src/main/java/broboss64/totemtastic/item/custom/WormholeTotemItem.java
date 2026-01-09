package broboss64.totemtastic.item.custom;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.item.TotemtasticItems;
import broboss64.totemtastic.util.TotemtasticUtils;
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
        ItemStack activeHandStack = user.getStackInHand(hand);
        ItemStack mainHandStack = user.getMainHandStack();
        ItemStack offHandStack = user.getOffHandStack();
        if (!world.isClient()) {
            //only triggers if in main hand
            UUID totemUUID = TotemtasticUtils.fetchUUIDFromItemStack(activeHandStack);
            PlayerEntity targetPlayer = TotemtasticUtils.getPlayerEntityFromUUID((ServerWorld) world, totemUUID);
            if (offHandStack.getItem() == TotemtasticItems.BLOOD_VIAL) {
                //handles binding of totem
                UUID bloodVialUUID = TotemtasticUtils.fetchUUIDFromItemStack(offHandStack);
                TotemtasticUtils.bindTotemFromVial(user);
                String boundToName = TotemtasticUtils.getUsernameFromUUID(bloodVialUUID, world.getServer());
                user.sendMessage(Text.literal("Bound to " + boundToName), true);
                return TypedActionResult.consume(activeHandStack);
            } else if (totemUUID == null) {
                //handles null uuid
                user.sendMessage(Text.literal("Not bound!").formatted(Formatting.RED), true);
                user.stopUsingItem();
                return TypedActionResult.fail(activeHandStack);
            } else if (targetPlayer == null) {
                //handles offline player
                user.sendMessage(Text.literal("Target player is offline!").formatted(Formatting.RED), true);
                user.stopUsingItem();
                return TypedActionResult.fail(activeHandStack);
            } else if (!targetPlayer.isAlive() || targetPlayer.isSpectator()) {
                //handles dead players or ones in spectator
                user.sendMessage(Text.literal("Target player is dead!").formatted(Formatting.RED), true);
                user.stopUsingItem();
                return TypedActionResult.fail(activeHandStack);
            } else if (targetPlayer == user) {
                //handles teleporting to yourself
                user.sendMessage(Text.literal("You can't teleport to yourself!").formatted(Formatting.RED), true);
                user.stopUsingItem();
                return TypedActionResult.fail(activeHandStack);
            } else {
                //if no errors, run normally
                user.setCurrentHand(hand);
                return TypedActionResult.consume(activeHandStack);
            }
        }
        return TypedActionResult.consume(activeHandStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        UUID totemUUID = TotemtasticUtils.fetchUUIDFromItemStack(stack);
        if (!world.isClient() && user instanceof  PlayerEntity) {
            PlayerEntity playerUsing = (PlayerEntity) user;
            ServerWorld serverWorld = (ServerWorld) world;
            PlayerEntity targetPlayer = TotemtasticUtils.getPlayerEntityFromUUID((ServerWorld) world, totemUUID);
            if (remainingUseTicks >= 1 && user.getOffHandStack().getItem() != TotemtasticItems.BLOOD_VIAL) {
                //runs every tick
                serverWorld.spawnParticles(ParticleTypes.PORTAL, user.getX(), user.getY() + 1, user.getZ(),
                        2, 0.25, 0.5, 0.25, 0.2);
                if (remainingUseTicks == getMaxUseTime(stack)){
                    //sends messages to both user and target
                    playerUsing.sendMessage(Text.literal("Teleporting to " + targetPlayer.getName().getString() + ".").formatted(Formatting.AQUA));
                    targetPlayer.sendMessage(Text.literal(playerUsing.getName().getString() + " is teleporting to you!").formatted(Formatting.AQUA), true);
                }
                if (targetPlayer != null) {
                    //spawns particles around the targetPlayer
                    ServerWorld targetWorld = (ServerWorld) targetPlayer.getWorld();
                    targetWorld.spawnParticles(ParticleTypes.PORTAL, targetPlayer.getX(), targetPlayer.getY() + 1, targetPlayer.getZ(),
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
        PlayerEntity targetPlayer = TotemtasticUtils.getPlayerEntityFromUUID((ServerWorld) world, targetUUID);
        if (targetPlayer != null) {
            //teleports the user to the target player, and removes 1 totem (even though they only stack to 1)
                ServerPlayerEntity serverUser = (ServerPlayerEntity) user;
                World targetDimension = targetPlayer.getWorld();
                double targetX = targetPlayer.getX();
                double targetY = targetPlayer.getY();
                double targetZ = targetPlayer.getZ();
                serverUser.teleport((ServerWorld) targetDimension, targetX, targetY, targetZ, targetPlayer.getYaw(), targetPlayer.getPitch());
                targetDimension.playSoundFromEntity(null, user, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 0.6f, 1);
                Hand activeHand = user.getActiveHand();
                user.getStackInHand(activeHand).decrement(1);
                user.stopUsingItem();
        } else {
            Totemtastic.LOGGER.error("Tried to teleport to an invalid player!");
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains(Totemtastic.PLAYER_UUID_KEY)) {
            UUID totemUUID = TotemtasticUtils.fetchUUIDFromItemStack(stack);
            MinecraftClient client = MinecraftClient.getInstance();
            PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(totemUUID);
            if (entry != null) {
                tooltip.add(Text.literal("Will warp to " + entry.getProfile().getName()).formatted(Formatting.GRAY));
            } else {
                tooltip.add(Text.literal("Will warp to an offline player").formatted(Formatting.GRAY));
            }

        } else {
            //default tooltip
            tooltip.add(Text.literal("Teleports the user to the bound player on use.").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("Hold a blood vial in the offhand and use to bind.").formatted(Formatting.GRAY));
        }
    }

    public WormholeTotemItem(Settings settings) {
        super(settings);
    }
}
