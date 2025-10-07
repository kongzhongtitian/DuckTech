package org.quiltmc.users.duckteam.ducktech.recipe.custom.essence_conversion_machine;

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

public class EssenceConversionMachineRecipeSerializer implements RecipeSerializer<EssenceConversionMachineRecipe> {

    public static final EssenceConversionMachineRecipeSerializer INSTANCE = new EssenceConversionMachineRecipeSerializer();
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "essence_conversion_machine");

    @Override
    public EssenceConversionMachineRecipe fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
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
            int maxOutputs = Math.min(outputsArray.size(), 1);
            for (int i = 0; i < maxOutputs; i++) {
                JsonElement element = outputsArray.get(i);
                outputs.add(ShapedRecipe.itemStackFromJson(element.getAsJsonObject()));
            }
        }

        //默认为20t
        int processingTime = GsonHelper.getAsInt(jsonObject, "processingTime", 60);

        return new EssenceConversionMachineRecipe(inputs, outputs, recipeId, processingTime);
    }

    @Override
    public @Nullable EssenceConversionMachineRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        return EssenceConversionMachineRecipe.fromNetwork(recipeId, buffer);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, EssenceConversionMachineRecipe recipe) {
        recipe.toNetwork(buffer);
    }
}
