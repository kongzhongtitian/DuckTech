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
import org.quiltmc.users.duckteam.ducktech.api.recipes.InputOutputRecipeSerializer;
import org.quiltmc.users.duckteam.ducktech.recipe.CountedIngredient;

import java.util.ArrayList;
import java.util.List;

public class AdvanceShredderRecipeSerializer extends InputOutputRecipeSerializer<AdvanceShredderRecipe> {
    public static final AdvanceShredderRecipeSerializer INSTANCE = new AdvanceShredderRecipeSerializer();

    public AdvanceShredderRecipeSerializer() {
        super(data -> new AdvanceShredderRecipe(data.inputs, data.outputs, data.id, data.processingTime), 2, 3);
    }

    //如果需要自定义处理时间默认值，可以重写该方法
    @Override
    protected int readProcessingTimeFromJson(JsonObject jsonObject) {
        return GsonHelper.getAsInt(jsonObject, "processingTime", 20);
    }
}

