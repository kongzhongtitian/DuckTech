package org.quiltmc.users.duckteam.ducktech.recipe.custom.essence_conversion_machine;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.quiltmc.users.duckteam.ducktech.DuckTech;
import org.quiltmc.users.duckteam.ducktech.api.recipes.InputOutputRecipeSerializer;
import org.quiltmc.users.duckteam.ducktech.recipe.CountedIngredient;

import java.util.List;

public class EssenceConversionMachineRecipeSerializer extends InputOutputRecipeSerializer<EssenceConversionMachineRecipe> {
    public static final EssenceConversionMachineRecipeSerializer INSTANCE = new EssenceConversionMachineRecipeSerializer();

    public EssenceConversionMachineRecipeSerializer() {
        super(data -> new EssenceConversionMachineRecipe(data.inputs, data.outputs, data.id, data.processingTime), 2, 1);
    }

}
