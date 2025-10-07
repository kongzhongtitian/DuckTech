package org.quiltmc.users.duckteam.ducktech.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.quiltmc.users.duckteam.ducktech.DuckTech;
import org.quiltmc.users.duckteam.ducktech.blocks.DTBlocks;
import org.quiltmc.users.duckteam.ducktech.recipe.DTRecipe;
import org.quiltmc.users.duckteam.ducktech.recipe.custom.advanceshredder.AdvanceShredderRecipe;

import java.util.List;


@JeiPlugin
public class AdvanceShredderRecipeJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new AdvanceShredderRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<AdvanceShredderRecipe> allRecipesFor = recipeManager.getAllRecipesFor(DTRecipe.ADVANCE_SHREDDER_RECIPE.get());

        registration.addRecipes(AdvanceShredderRecipeCategory.RECIPE_TYPE, allRecipesFor);
    }

//    @Override
//    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
//        registration.addRecipeClickArea(.class, 70, 30, 25, 22,
//                ShredderRecipeCategory.RECIPE_TYPE);
//    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(DTBlocks.ADVANCE_SHREDDER.get()) , AdvanceShredderRecipeCategory.RECIPE_TYPE);
    }
}
