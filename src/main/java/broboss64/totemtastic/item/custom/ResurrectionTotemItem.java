package broboss64.totemtastic.item.custom;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.item.TotemtasticItems;
import broboss64.totemtastic.util.ReviveState;
import broboss64.totemtastic.util.TotemtasticUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ResurrectionTotemItem extends Item {

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
        ItemStack heldStack = user.getStackInHand(hand);
        if (!world.isClient() && this.getHitResult(user).getType() != HitResult.Type.BLOCK) {
            if (user.getOffHandStack().getItem() == TotemtasticItems.BLOOD_VIAL) {
                TotemtasticUtils.bindTotemFromVial(user);
                user.stopUsingItem();
            } else {
                user.stopUsingItem();
                return TypedActionResult.fail(heldStack);
            }

        }
        return TypedActionResult.fail(heldStack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient()) {
            PlayerEntity player = context.getPlayer();
            UUID targetUUID = TotemtasticUtils.fetchUUIDFromItemStack(context.getStack());
            PlayerEntity targetPlayer = TotemtasticUtils.getPlayerEntityFromUUID((ServerWorld) context.getWorld(), targetUUID);
            if (targetUUID == null) {
                player.stopUsingItem();
                player.sendMessage(Text.literal("Not bound or invalid player!"), true);
                return ActionResult.FAIL;
            } else if (!targetPlayer.isSpectator() || targetPlayer == null) {
                player.stopUsingItem();
                player.sendMessage(Text.literal("Player is not dead or offline!"), true);
                return ActionResult.FAIL;
            } else if (this.getHitResult(player).getType() == HitResult.Type.BLOCK && player.getOffHandStack().getItem() != TotemtasticItems.BLOOD_VIAL) {
                player.setCurrentHand(context.getHand());
            } else {
                player.stopUsingItem();
                return ActionResult.FAIL;
            }
            return ActionResult.FAIL;
        }
        return ActionResult.FAIL;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient() && user instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) user;
            UUID deadPlayerUUID = TotemtasticUtils.fetchUUIDFromItemStack(stack);
            PlayerEntity playerToRevive = TotemtasticUtils.getPlayerEntityFromUUID((ServerWorld) world, deadPlayerUUID);
            if (playerToRevive == null) {
                //handles if the uuid is null
                player.sendMessage(Text.literal("Not bound or invalid player!"), true);
                player.stopUsingItem();
            } else if (!playerToRevive.isSpectator() && playerToRevive != null) {
                //runs if player isnt in spectator
                player.sendMessage(Text.literal("Player is not dead or not in spectator mode!"), true);
                player.stopUsingItem();
            } else if (remainingUseTicks >= 1) {
                HitResult hitResult = this.getHitResult(user);
                ((ServerWorld) world).spawnParticles(ParticleTypes.END_ROD, hitResult.getPos().x, hitResult.getPos().y + 1, hitResult.getPos().z, 1, 0.1, 0.5, 0.1, 0);
                if (hitResult instanceof BlockHitResult && hitResult.getType() == HitResult.Type.BLOCK) {
                    if (remainingUseTicks == 1) {
                        revivePlayer(world, deadPlayerUUID, user, hitResult);
                        user.getMainHandStack().decrement(1);
                        user.stopUsingItem();
                    }
                } else {
                    user.stopUsingItem();
                }
            }

        } else {
            user.stopUsingItem();
        }

    }


private HitResult getHitResult(LivingEntity user) {
    //lets the usageTick method know the properties of the block it's used on
    return ProjectileUtil.getCollision(user, entity -> !entity.isSpectator() && entity.canHit(), MAX_USE_REACH);
}

@Override
public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    NbtCompound nbt = stack.getNbt();
    if (nbt != null && nbt.contains(Totemtastic.PLAYER_UUID_KEY)) {
        UUID totemUUID = TotemtasticUtils.fetchUUIDFromItemStack(stack);
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(totemUUID);
        if (entry != null) {
            tooltip.add(Text.literal("Will resurrect " + entry.getProfile().getName()).formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.literal("Will resurrect an Offline Player").formatted(Formatting.GRAY));
        }

    } else {
        tooltip.add(Text.literal("Will resurrect the bound player, and deals 10 hearts of damage to user.").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Will also remove all stacks of Umbra Mortis from the revived player").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Not Bound to a player").formatted(Formatting.GRAY));
    }
}
private void revivePlayer(World world, UUID deadPlayerUUID, LivingEntity itemUser, HitResult hitResult) {
    if (!world.isClient()) {
        MinecraftServer server = world.getServer();
        ReviveState state = ReviveState.get((ServerWorld) world);
        ServerPlayerEntity playerToRevive = (ServerPlayerEntity) TotemtasticUtils.getPlayerEntityFromUUID((ServerWorld) world, deadPlayerUUID);
        World targetDimension = itemUser.getWorld();
        double targetX = hitResult.getPos().x;
        double targetY = hitResult.getPos().y;
        double targetZ = hitResult.getPos().z;
        //revives player at the position of the block
        playerToRevive.teleport((ServerWorld) targetDimension, targetX, targetY, targetZ, 0, 0);
        playerToRevive.changeGameMode(GameMode.SURVIVAL);
        playerToRevive.setHealth(1);
        state.setReviveCount(deadPlayerUUID, 0);
        int currentReviveCount = state.getReviveCount(deadPlayerUUID);
        playerToRevive.sendMessage(Text.literal(String.valueOf(currentReviveCount)));
        world.playSoundFromEntity(null, playerToRevive, SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1, 1);
        ((ServerWorld) world).spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, targetX, targetY + 1, targetZ, 100, 0, 0, 0, 1);
        //damages the user, killing them unless they have absorption or bonus health
        itemUser.damage(world.getDamageSources().magic(), 20);
    }
}


public ResurrectionTotemItem(Settings settings) {
    super(settings);
}
}
