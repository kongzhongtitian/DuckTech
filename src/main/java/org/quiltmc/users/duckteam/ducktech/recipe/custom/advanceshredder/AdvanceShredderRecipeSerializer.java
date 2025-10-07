package org.quiltmc.users.duckteam.ducktech.recipe.custom.advanceshredder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.users.duckteam.ducktech.DuckTech;
import org.quiltmc.users.duckteam.ducktech.recipe.CountedIngredient;

import java.util.ArrayList;
import java.util.List;

public class AdvanceShredderRecipeSerializer implements RecipeSerializer<AdvanceShredderRecipe> {

    public static final AdvanceShredderRecipeSerializer INSTANCE = new AdvanceShredderRecipeSerializer();
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "advance_shredder");

    @Override
    public AdvanceShredderRecipe fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
        List<CountedIngredient> inputs = new ArrayList<>();
        if (jsonObject.has("input")) {
            JsonElement inputElement = jsonObject.get("input");
            if (inputElement.isJsonObject()) {
                inputs.add(CountedIngredient.fromJson(inputElement.getAsJsonObject()));
            } else if (inputElement.isJsonArray()) {
                JsonArray inputsArray = inputElement.getAsJsonArray();
                int maxInputs = Math.min(inputsArray.size(), 2);
                for (int i = 0; i < maxInputs; i++) {
                    inputs.add(CountedIngredient.fromJson(inputsArray.get(i).getAsJsonObject()));
                }
            }
        }

        List<ItemStack> outputs = new ArrayList<>();
        if (jsonObject.has("output")) {
            JsonArray outputsArray = GsonHelper.getAsJsonArray(jsonObject, "output");
            int maxOutputs = Math.min(outputsArray.size(), 3);
            for (int i = 0; i < maxOutputs; i++) {
                JsonElement element = outputsArray.get(i);
                outputs.add(ShapedRecipe.itemStackFromJson(element.getAsJsonObject()));
            }
        }

        //默认为20t
        int processingTime = GsonHelper.getAsInt(jsonObject, "processingTime", 20);

        return new AdvanceShredderRecipe(inputs, outputs, recipeId, processingTime);
    }

    @Override
    public @Nullable AdvanceShredderRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        return AdvanceShredderRecipe.fromNetwork(recipeId, buffer);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, AdvanceShredderRecipe recipe) {
        recipe.toNetwork(buffer);
    }
}
