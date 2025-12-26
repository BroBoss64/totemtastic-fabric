package broboss64.totemtastic.item.custom;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.item.TotemtasticItems;
import broboss64.totemtastic.util.TotemtasticItemUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ResurrectionTotemItem extends Item {

    private static final double MAX_USE_REACH = Math.sqrt(ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE);
    private double angle = 0;
    private double radius = 1;

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
        if (!world.isClient() && this.getHitResult(user).getType() != HitResult.Type.BLOCK) {
            UUID totemUUID = TotemtasticItemUtils.fetchUUIDFromItemStack(user.getMainHandStack());
            if (totemUUID != null) {
                String playerMessage = "This totem will revive " + TotemtasticItemUtils.getUsernameFromUUID(totemUUID, world.getServer());
                user.sendMessage(Text.literal(playerMessage), true);
                user.stopUsingItem();
            } else {
                String invalidUUIDMessage = "Not bound to a player or invalid player";
                user.sendMessage(Text.literal(invalidUUIDMessage), true);
                user.stopUsingItem();
            }
            return TypedActionResult.success(user.getMainHandStack(), true);
        }
        return TypedActionResult.fail(user.getMainHandStack());
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity playerEntity = context.getPlayer();
        if (playerEntity != null && this.getHitResult(playerEntity).getType() == HitResult.Type.BLOCK && playerEntity.getOffHandStack().getItem() != TotemtasticItems.BLOOD_VIAL && !playerEntity.isSneaking()) {
            playerEntity.setCurrentHand(context.getHand());
        } else if (playerEntity.getOffHandStack().getItem() == TotemtasticItems.BLOOD_VIAL) {
            ItemStack mainHandStack = playerEntity.getMainHandStack();
            ItemStack offhandItemStack = playerEntity.getOffHandStack();
            UUID bloodVialUUID = TotemtasticItemUtils.fetchUUIDFromItemStack(playerEntity.getOffHandStack());
            ItemStack modifiedStack = TotemtasticItemUtils.createBoundItemStack(TotemtasticItems.RESURRECTION_TOTEM, 1, bloodVialUUID);
            playerEntity.setStackInHand(Hand.OFF_HAND, new ItemStack(TotemtasticItems.GLASS_VIAL));
            playerEntity.setStackInHand(Hand.MAIN_HAND, modifiedStack);

        }
        return ActionResult.CONSUME;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient()) {
            UUID deadPlayerUUID = TotemtasticItemUtils.fetchUUIDFromItemStack(stack);
            PlayerEntity playerToRevive = TotemtasticItemUtils.getPlayerEntityFromUUID((ServerWorld) world, deadPlayerUUID);
            if (!playerToRevive.isSpectator() && user instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) user;
                player.sendMessage(Text.literal("Player is not dead or not in spectator mode!"), true);
                player.stopUsingItem();
            } else if (remainingUseTicks >= 0 && user instanceof PlayerEntity playerEntity) {
                HitResult hitResult = this.getHitResult(user);
                spiralParticles(hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z, (ServerWorld) world);

                if (hitResult instanceof BlockHitResult blockHitResult && hitResult.getType() == HitResult.Type.BLOCK) {
                    if (remainingUseTicks == 1) {
                        revivePlayer(world, deadPlayerUUID, user, hitResult);
                        user.stopUsingItem();
                    }
                } else {
                    user.stopUsingItem();
                }
            } else {
                user.stopUsingItem();
            }

        }

    }

    private HitResult getHitResult(LivingEntity user) {
        return ProjectileUtil.getCollision(user, entity -> !entity.isSpectator() && entity.canHit(), MAX_USE_REACH);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("§7Brings a player back to life after dying."));
        tooltip.add(Text.literal("§7Hold a blood vial in the offhand and use to bind the totem."));
    }

    private void revivePlayer(World world, UUID deadPlayerUUID, LivingEntity itemUser, HitResult hitResult) {
        if (!world.isClient()) {
            MinecraftServer server = world.getServer();
            ServerPlayerEntity playerToRevive = (ServerPlayerEntity) TotemtasticItemUtils.getPlayerEntityFromUUID((ServerWorld) world, deadPlayerUUID);
            double posX = hitResult.getPos().x;
            double posY = hitResult.getPos().y;
            double posZ = hitResult.getPos().z;
            playerToRevive.requestTeleport(posX, posY, posZ);
            playerToRevive.changeGameMode(GameMode.SURVIVAL);
            playerToRevive.setHealth(1);
            world.playSoundFromEntity(null, playerToRevive, SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1, 1);
            itemUser.damage(world.getDamageSources().magic(), 20);
        }
    }

    private void spiralParticles(Double posX, Double posY, Double posZ, ServerWorld world) {
       if (!world.isClient()) {
           double angleIncrement = 0.1;
           angle += angleIncrement;

           double x = posX + radius * Math.cos(angle);
           double y = posY;
           double z = posZ + radius * Math.sin(angle);
           world.spawnParticles(ParticleTypes.END_ROD, x, y, z, 1, 0, 0.2, 0, 0);
       }
    }


    public ResurrectionTotemItem(Settings settings) {
        super(settings);
    }
}
