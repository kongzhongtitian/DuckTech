package org.quiltmc.users.duckteam.ducktech.items.armor_material;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class DTArmorMaterial {
    public static final ArmorMaterial BASIC_ESSENCE_LEVEL_ONE = new BasicEssenceLevelOne(
    ) {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return 0;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return 0;
        }
    };
}