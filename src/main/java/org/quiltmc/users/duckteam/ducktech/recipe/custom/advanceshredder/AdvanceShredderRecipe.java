package org.quiltmc.users.duckteam.ducktech.recipe.custom.advanceshredder;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.users.duckteam.ducktech.api.recipes.InputOutputRecipe;
import org.quiltmc.users.duckteam.ducktech.recipe.CountedIngredient;
import org.quiltmc.users.duckteam.ducktech.recipe.DTRecipe;
import org.quiltmc.users.duckteam.ducktech.recipe.DTRecipeSerializers;

import java.util.ArrayList;
import java.util.List;

public class AdvanceShredderRecipe extends InputOutputRecipe {
    private final int processingTime;

    public AdvanceShredderRecipe(List<CountedIngredient> inputs, List<ItemStack> outputs,
                                 ResourceLocation id, int processingTime) {
        super(inputs, outputs, id, 2, 3); // 最多2个输入，最多3个输出
        this.processingTime = processingTime;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DTRecipeSerializers.ADVANCE_SHREDDER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return DTRecipe.ADVANCE_SHREDDER_RECIPE.get();
    }

    public int getProcessingTime() {
        return processingTime;
    }

    //特定于该配方类型的网络序列化方法
    public void toNetwork(FriendlyByteBuf buffer) {
        super.toNetwork(buffer);
        buffer.writeVarInt(processingTime);
    }

    public static AdvanceShredderRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        List<CountedIngredient> inputs = readInputsFromNetwork(buffer);
        List<ItemStack> outputs = readOutputsFromNetwork(buffer);
        int processingTime = buffer.readVarInt();
        return new AdvanceShredderRecipe(inputs, outputs, id, processingTime);
    }
}

