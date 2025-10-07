package org.quiltmc.users.duckteam.ducktech.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import org.quiltmc.users.duckteam.ducktech.DuckTech;
import org.quiltmc.users.duckteam.ducktech.recipe.custom.advanceshredder.AdvanceShredderRecipe;
import org.quiltmc.users.duckteam.ducktech.recipe.custom.essence_conversion_machine.EssenceConversionMachineRecipe;
import org.quiltmc.users.duckteam.ducktech.recipe.custom.shredder.ShredderRecipe;

import java.util.function.Supplier;

public class DTRecipe {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, DuckTech.MODID);

    public static final Supplier<RecipeType<ShredderRecipe>> SHREDDER_RECIPE =
            RECIPE_TYPES.register(
                    "shredder_recipe",
                    // We need the qualifying generic here due to generics being generics.
                    () -> RecipeType.<ShredderRecipe>simple(ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "shredder_recipe"))
            );



    public static final Supplier<RecipeType<AdvanceShredderRecipe>> ADVANCE_SHREDDER_RECIPE =
            RECIPE_TYPES.register(
                    "advance_shredder_recipe",
                    // We need the qualifying generic here due to generics being generics.
                    () -> RecipeType.<AdvanceShredderRecipe>simple(ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "advance_shredder_recipe"))
            );



    public static final Supplier<RecipeType<EssenceConversionMachineRecipe>>ESSENCE_CONVERSION_MACHINE_RECIPE =
            RECIPE_TYPES.register(
                    "essence_conversion_machine_recipe",
                    () -> RecipeType.<EssenceConversionMachineRecipe>simple(ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "essence_conversion_machine_recipe"))
            );
}
