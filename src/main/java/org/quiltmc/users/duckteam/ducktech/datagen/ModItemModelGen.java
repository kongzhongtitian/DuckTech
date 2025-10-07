package org.quiltmc.users.duckteam.ducktech.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.quiltmc.users.duckteam.ducktech.DuckTech;

@SuppressWarnings("all")
public class ModItemModelGen extends ItemModelProvider {


    public static final String GENERATED = "item/generated";

    public static final String HANDHELD = "item/handheld";

    public ModItemModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DuckTech.MODID, existingFileHelper);
    }
    @Override
    protected void registerModels() {

    }

    public void itemGeneratedModel(Item item) {
        withExistingParent(itemName( item), GENERATED).texture("layer0",resourceItem(itemName(item)));
    }

    private String itemName(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }

    public ResourceLocation resourceItem(String path) {
        return new ResourceLocation(DuckTech.MODID, "item/" + path);
    }
}
