package broboss64.totemtastic.item.custom;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.item.TotemtasticItems;
import broboss64.totemtastic.util.TotemtasticUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class KnifeItem extends Item {
    public static final RegistryKey<DamageType> STAB_SELF =
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Totemtastic.MOD_ID, "stab_self"));
    /*public static final RegistryKey<DamageType> STABBING =
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Totemtastic.MOD_ID, "stabbing"));*/

    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack mainHandItem = player.getMainHandStack();
        if (!world.isClient) {
           DamageSource damageSource = new DamageSource(world.getRegistryManager()
                   .get(RegistryKeys.DAMAGE_TYPE).entryOf(STAB_SELF));

           player.damage(damageSource, 4);
           world.playSoundFromEntity(null, player, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, SoundCategory.PLAYERS, 1, 1);
           fillBloodVial(world, player, hand);
       }
       return TypedActionResult.success(mainHandItem, true);
    }

    private void fillBloodVial(World world, PlayerEntity player, Hand hand) {
        ItemStack offHandItem = player.getOffHandStack();
        if (offHandItem.getItem() == TotemtasticItems.GLASS_VIAL) {
            ItemStack bloodVial = TotemtasticUtils.createBoundItemStack(
                    TotemtasticItems.BLOOD_VIAL, 1, player.getUuid());
            if (offHandItem.getCount() == 1) {
                player.setStackInHand(Hand.OFF_HAND, bloodVial);
            } else {
                player.giveItemStack(bloodVial);
                player.setStackInHand(Hand.OFF_HAND, new ItemStack(offHandItem.getItem(), offHandItem.getCount() - 1));
            }
            world.playSoundFromEntity(null, player, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 1, 1);
        }
    }

    public KnifeItem(Settings settings, float attackDamage, float attackSpeed) {
        super(settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        //adds attack damage bonus
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Knife Damage Modifier",
                attackDamage, EntityAttributeModifier.Operation.ADDITION));
        //sets correct attack speed
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Knife Attack Speed Modifier",
                attackSpeed, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getWorld().isClient() && attacker instanceof PlayerEntity player) {
            stack.damage(1, player, player1 -> player1.sendToolBreakStatus(Hand.MAIN_HAND));
        }
        return true;
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return !miner.isCreative();
    }

    @Override
    public boolean hasRecipeRemainder() {
        return true;
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        if (stack.getDamage() < stack.getMaxDamage()) {
            ItemStack copy = stack.copy();
            copy.setDamage(stack.getDamage() + 1);
            return copy;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
    }
}
