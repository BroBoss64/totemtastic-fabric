package broboss64.totemtastic.item.custom;

import broboss64.totemtastic.Totemtastic;
import broboss64.totemtastic.item.TotemtasticItems;
import broboss64.totemtastic.util.TotemtasticItemUtils;
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
import net.minecraft.world.World;

public class KnifeItem extends Item {
    public static final RegistryKey<DamageType> STAB_SELF =
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Totemtastic.MOD_ID, "stab_self"));
    public static final RegistryKey<DamageType> STABBING =
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Totemtastic.MOD_ID, "stabbing"));

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
            ItemStack bloodVial = TotemtasticItemUtils.createBoundItemStack(
                    TotemtasticItems.BLOOD_VIAL, 1, player.getUuid());
            player.giveItemStack(bloodVial);
            player.setStackInHand(Hand.OFF_HAND, new ItemStack(offHandItem.getItem(), offHandItem.getCount() - 1));
            world.playSoundFromEntity(null, player, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 1, 1);
        }
    }

    public KnifeItem(Settings settings) {
        super(settings);
    }
}
