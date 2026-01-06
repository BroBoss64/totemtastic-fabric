package broboss64.totemtastic.item.custom;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.util.TotemtasticUtils;
import net.minecraft.client.item.TooltipContext;
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
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RecallTotemItem extends Item {

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
                //only runs if in main hand
                if (user.isSneaking()) {
                    //links the totem to the players current position
                    TotemtasticUtils.createPositionLinkedTotem(user.getBlockPos(), world, user);
                    int targetX = (int) user.getX();
                    int targetY = (int) user.getY();
                    int targetZ = (int) user.getZ();
                    //informs the player of the change
                    user.sendMessage(Text.literal("Bound to X: " + targetX + ", Y: " + targetY + ", Z: " + targetZ));
                    user.stopUsingItem();
                    return TypedActionResult.success(heldStack, true);
                } else {
                    //still dont know what this does
                    user.setCurrentHand(hand);
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
        if (!world.isClient() && user instanceof PlayerEntity) {
            ServerWorld serverWorld = (ServerWorld) world;
            ServerWorld targetWorld = TotemtasticUtils.fetchDimensionFromItemStack(stack, world.getServer());
            BlockPos targetBlock = TotemtasticUtils.fetchBlockPosFromItemStack(stack);
            if (remainingUseTicks >= 1 && !user.isSneaking()) {
                //runs every tick that the player is using the item
                serverWorld.spawnParticles(ParticleTypes.PORTAL, user.getX(), user.getY() + 1, user.getZ(),
                        2, 0.25, 0.5, 0.25, 0.3);
                if (targetBlock != null && targetWorld != null) {
                    targetWorld.spawnParticles(ParticleTypes.PORTAL, targetBlock.getX() + 0.5, targetBlock.getY() + 1, targetBlock.getZ() + 0.5,
                            2, 0.25, 0.5, 0.25, 0.3);
                }
                if (remainingUseTicks == 1) {
                    //runs when finished using
                    returnHome(world, (PlayerEntity) user, stack);
                    user.stopUsingItem();
                }
            } else {
                user.stopUsingItem();
            }
        }
    }

    private void returnHome(World world, PlayerEntity user, ItemStack stack) {
        if (!world.isClient()) {
            BlockPos targetPos = TotemtasticUtils.fetchBlockPosFromItemStack(stack);
            ServerWorld targetWorld = TotemtasticUtils.fetchDimensionFromItemStack(stack, world.getServer());
            ServerPlayerEntity serverUser = (ServerPlayerEntity) user;
            if (targetWorld != null && targetPos != null) {
                serverUser.teleport(targetWorld, targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, user.getYaw(), user.getPitch());
                targetWorld.playSoundFromEntity(null, user, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 0.5f, 1);
                user.getMainHandStack().decrement(1);
            } else {
                user.sendMessage(Text.literal("Not bound or invalid position!"), true);
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains(Totemtastic.POSITION_KEY)) {
            NbtCompound tooltipCoords = nbt.getCompound(Totemtastic.POSITION_KEY);
            tooltip.add(Text.literal("§7X: " +
                    tooltipCoords.getInt("x") + ", Y: " +
                    tooltipCoords.getInt("y") + ", Z: " +
                    tooltipCoords.getInt("z")));
            tooltip.add(Text.literal("Dimension: " + tooltipCoords.getString("dimension")));
        } else {
            tooltip.add(Text.literal("§7No Position Linked"));
            tooltip.add(Text.literal("§7Sneak and use to link"));
        }
    }

    public RecallTotemItem(Settings settings) {
        super(settings);
    }
}
