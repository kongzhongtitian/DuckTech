package org.quiltmc.users.duckteam.ducktech.recipe.custom.shredder;

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

public class ShredderRecipeSerializer implements RecipeSerializer<ShredderRecipe> {

    public static final ShredderRecipeSerializer INSTANCE = new ShredderRecipeSerializer();

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "shredder");

    @Override
    public ShredderRecipe fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
        List<CountedIngredient> inputs = new ArrayList<>();

        // 处理输入 - 支持单个输入或输入数组
        if (jsonObject.has("input")) {
            JsonElement inputElement = jsonObject.get("input");
            if (inputElement.isJsonObject()) {
                // 单个输入
                inputs.add(CountedIngredient.fromJson(inputElement.getAsJsonObject()));
            } else if (inputElement.isJsonArray()) {
                // 输入数组
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
            // 限制最多处理3个输出
            int maxOutputs = Math.min(outputsArray.size(), 3);
            for (int i = 0; i < maxOutputs; i++) {
                JsonElement element = outputsArray.get(i);
                outputs.add(ShapedRecipe.itemStackFromJson(element.getAsJsonObject()));
            }
        }

        return new ShredderRecipe(inputs, outputs, recipeId);
    }

    @Override
    public @Nullable ShredderRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        return ShredderRecipe.fromNetwork(recipeId, buffer);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, ShredderRecipe recipe) {
        recipe.toNetwork(buffer);
    }
}
