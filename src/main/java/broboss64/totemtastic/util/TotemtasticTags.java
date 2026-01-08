package broboss64.totemtastic.util;

import broboss64.totemtastic.Totemtastic;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class TotemtasticTags {

    public static class Items {
        public static final TagKey<Item> KNIVES = createTag("knives");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(Totemtastic.MOD_ID, name));
        }
    }

}
