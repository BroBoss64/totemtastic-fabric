package broboss64.totemtastic.item.custom;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.item.TotemtasticItems;
import broboss64.totemtastic.util.ReviveState;
import broboss64.totemtastic.util.TotemtasticUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class CrudeResurrectionTotemItem extends Item {

    private static final double MAX_USE_REACH = Math.sqrt(ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE);


    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 100;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack mainHandStack = user.getStackInHand(hand);
        if (hand == Hand.OFF_HAND) return TypedActionResult.fail(mainHandStack);
        if (this.getHitResult(user).getType() == HitResult.Type.MISS) {
            //only runs if player isn't looking at the air
                if (user.getOffHandStack().getItem() == TotemtasticItems.BLOOD_VIAL) {
                    if (!world.isClient()) {
                        //handles binding of totem
                        UUID bloodVialUUID = TotemtasticUtils.fetchUUIDFromItemStack(user.getOffHandStack());
                        TotemtasticUtils.bindTotemFromVial(user);
                        String boundToName = TotemtasticUtils.getUsernameFromUUID(bloodVialUUID, world.getServer());
                        user.sendMessage(Text.literal("Bound to " + boundToName), true);
                        return TypedActionResult.consume(mainHandStack);
                    }
                } else {
                    return TypedActionResult.pass(mainHandStack);
                }
        }
        if (this.getHitResult(user).getType() == HitResult.Type.BLOCK) {
            //only runs if player is looking at a block
            if (!world.isClient()) {
                UUID targetUUID = TotemtasticUtils.fetchUUIDFromItemStack(mainHandStack);
                if (user.getOffHandStack().getItem() == TotemtasticItems.BLOOD_VIAL) {
                    //handles binding of totem
                    UUID bloodVialUUID = TotemtasticUtils.fetchUUIDFromItemStack(user.getOffHandStack());
                    TotemtasticUtils.bindTotemFromVial(user);
                    String boundToName = TotemtasticUtils.getUsernameFromUUID(bloodVialUUID, world.getServer());
                    user.sendMessage(Text.literal("Bound to " + boundToName), true);
                    return TypedActionResult.success(mainHandStack);
                } else if (targetUUID == null) {
                    //handles null UUID
                    user.sendMessage(Text.literal("Not Bound!").formatted(Formatting.RED), true);
                    return TypedActionResult.fail(mainHandStack);
                } else {
                    PlayerEntity targetPlayer = TotemtasticUtils.getPlayerEntityFromUUID((ServerWorld) world, targetUUID);
                    if (targetPlayer == null) {
                        //if PlayerEntity doesn't exist, cancel usage
                        user.sendMessage(Text.literal("Player is offline!").formatted(Formatting.RED), true);
                        return TypedActionResult.fail(mainHandStack);
                    } else if (!targetPlayer.isSpectator()) {
                        //if player isn't in spectator mode, cancel usage
                        user.sendMessage(Text.literal("Player is not dead!").formatted(Formatting.RED), true);
                        return TypedActionResult.fail(mainHandStack);
                    } else {
                        //only runs if PlayerEntity is valid, and is in spectator mode
                        user.setCurrentHand(hand);
                        return TypedActionResult.consume(mainHandStack);
                    }
                }
            } else return TypedActionResult.consume(mainHandStack);
        }
        //if player isn't looking at a block or the air, do nothing.
        return TypedActionResult.pass(mainHandStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        HitResult hitResult = this.getHitResult(user);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            //only runs if the player is still looking at a block
            if (user instanceof PlayerEntity && !world.isClient()) {
                //only runs on the server side, and if the user is a player
                UUID deadPlayerUUID = TotemtasticUtils.fetchUUIDFromItemStack(stack);
                if (remainingUseTicks >= 1) {
                    //runs every tick to spawn particles
                    ((ServerWorld) world).spawnParticles(ParticleTypes.END_ROD, hitResult.getPos().x, hitResult.getPos().y + 1, hitResult.getPos().z, 1, 0.1, 0.5, 0.1, 0);
                    if (remainingUseTicks == 1) {
                        //runs the revive method
                        user.getMainHandStack().decrement(1);
                        revivePlayer(world, deadPlayerUUID, user, hitResult);
                        user.stopUsingItem();
                    }
                }
            }
        } else user.stopUsingItem();
    }


private HitResult getHitResult(LivingEntity user) {
    //lets the usageTick method know the properties of the block it's used on
    return ProjectileUtil.getCollision(user, entity -> !entity.isSpectator() && entity.canHit(), MAX_USE_REACH);
}

@Override
public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    MinecraftClient client = MinecraftClient.getInstance();
    ClientWorld clientWorld = client.world;
    if (clientWorld.getLevelProperties().isHardcore()) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains(Totemtastic.PLAYER_UUID_KEY)) {
            UUID totemUUID = TotemtasticUtils.fetchUUIDFromItemStack(stack);
            PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(totemUUID);
            if (entry != null) {
                tooltip.add(Text.literal("Will resurrect " + entry.getProfile().getName()).formatted(Formatting.GRAY));
                tooltip.add(Text.literal("Will deal 10 hearts of damage to the user.").formatted(Formatting.DARK_RED));
            } else {
                tooltip.add(Text.literal("Bound to an offline player.").formatted(Formatting.GRAY));
                tooltip.add(Text.literal("Will deal 10 hearts of damage to the user.").formatted(Formatting.DARK_RED));
            }

        } else {
            tooltip.add(Text.literal("Will resurrect the bound player.").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("Applies a stacking 20% max health reduction to the resurrected player.").formatted(Formatting.DARK_RED));
            tooltip.add(Text.literal("Hold a blood vial in the offhand and use to bind.").formatted(Formatting.GRAY));
        }
    } else {
        tooltip.add(Text.literal("This item only works in hardcore mode!").formatted(Formatting.RED));
    }
}
private void revivePlayer(World world, UUID deadPlayerUUID, LivingEntity itemUser, HitResult hitResult) {
    if (!world.isClient()) {
        ReviveState state = ReviveState.get((ServerWorld) world);
        int currentReviveCount = state.getReviveCount(deadPlayerUUID);
        ServerPlayerEntity playerToRevive = (ServerPlayerEntity) TotemtasticUtils.getPlayerEntityFromUUID((ServerWorld) world, deadPlayerUUID);
        World targetDimension = itemUser.getWorld();
        double targetX = hitResult.getPos().x;
        double targetY = hitResult.getPos().y;
        double targetZ = hitResult.getPos().z;
        //revives player at the position of the block
        playerToRevive.teleport((ServerWorld) targetDimension, targetX, targetY, targetZ, 0, 0);
        playerToRevive.changeGameMode(GameMode.SURVIVAL);
        playerToRevive.setHealth(1);
        playerToRevive.removeStatusEffect(Totemtastic.UMBRA_MORTIS);
        state.setReviveCount(deadPlayerUUID,currentReviveCount + 1);
        int newReviveCount = state.getReviveCount(deadPlayerUUID);
        playerToRevive.sendMessage(Text.literal(String.valueOf(newReviveCount)));
        ((ServerWorld) world).spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, targetX, targetY + 1, targetZ, 100, 0, 0, 0, 1);
        world.playSoundFromEntity(null, playerToRevive, SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1, 1);
        //damages the user, killing them unless they have absorption or bonus health
        itemUser.damage(world.getDamageSources().magic(), 20);
    }
}


public CrudeResurrectionTotemItem(Settings settings) {
    super(settings);
}
}
