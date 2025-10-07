package org.quiltmc.users.duckteam.ducktech.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.quiltmc.users.duckteam.ducktech.DuckTech;
import org.quiltmc.users.duckteam.ducktech.recipe.custom.advanceshredder.AdvanceShredderRecipe;
import org.quiltmc.users.duckteam.ducktech.recipe.custom.advanceshredder.AdvanceShredderRecipeSerializer;
import org.quiltmc.users.duckteam.ducktech.recipe.custom.essence_conversion_machine.EssenceConversionMachineRecipe;
import org.quiltmc.users.duckteam.ducktech.recipe.custom.essence_conversion_machine.EssenceConversionMachineRecipeSerializer;
import org.quiltmc.users.duckteam.ducktech.recipe.custom.shredder.ShredderRecipe;
import org.quiltmc.users.duckteam.ducktech.recipe.custom.shredder.ShredderRecipeSerializer;

public class DTRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DuckTech.MODID);

    public static final RegistryObject<RecipeSerializer<ShredderRecipe>> SHREDDER_SERIALIZER =
            SERIALIZERS.register("shredder", () -> ShredderRecipeSerializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<AdvanceShredderRecipe>> ADVANCE_SHREDDER_SERIALIZER =
            SERIALIZERS.register("advance_shredder", () -> AdvanceShredderRecipeSerializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<EssenceConversionMachineRecipe>> ESSENCE_CONVERSION_MACHINE_SERIALIZER =
            SERIALIZERS.register("essence_conversion_machine", () -> EssenceConversionMachineRecipeSerializer.INSTANCE);
}
