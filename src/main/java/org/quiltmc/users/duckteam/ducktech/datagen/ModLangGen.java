package org.quiltmc.users.duckteam.ducktech.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.quiltmc.users.duckteam.ducktech.DuckTech;

public class ModLangGen extends LanguageProvider {
    public ModLangGen(PackOutput output, String locale) {
        super(output, DuckTech.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
    }

    public void addVillager(VillagerProfession villagerProfession, String name) {
        add("entity.minecraft.villager."+ ForgeRegistries.VILLAGER_PROFESSIONS.getKey(villagerProfession).toLanguageKey(), name);
    }
}
