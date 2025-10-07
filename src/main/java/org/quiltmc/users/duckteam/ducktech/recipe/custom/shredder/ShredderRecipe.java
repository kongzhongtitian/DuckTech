package org.quiltmc.users.duckteam.ducktech.recipe.custom.shredder;

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
import org.quiltmc.users.duckteam.ducktech.recipe.CountedIngredient;
import org.quiltmc.users.duckteam.ducktech.recipe.DTRecipe;
import org.quiltmc.users.duckteam.ducktech.recipe.DTRecipeSerializers;

import java.util.ArrayList;
import java.util.List;

public class ShredderRecipe implements Recipe<SimpleContainer> {

    private final List<CountedIngredient> inputs; // 支持数量的输入物品 (最多2个)
    private final List<ItemStack> output;//多个输出物品 (最多3个)
    private final ResourceLocation id;

    public ShredderRecipe(List<CountedIngredient> inputs, List<ItemStack> output, ResourceLocation id) {
        // 限制输入最多为2个
        this.inputs = new ArrayList<>();
        for (int i = 0; i < Math.min(inputs.size(), 2); i++) {
            this.inputs.add(inputs.get(i));
        }

        // 限制输出最多为3个
        this.output = new ArrayList<>();
        for (int i = 0; i < Math.min(output.size(), 3); i++) {
            this.output.add(output.get(i));
        }
        this.id = id;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide()) return false;

        // 检查容器大小是否与输入数量匹配
        if (container.getContainerSize() < inputs.size()) {
            return false;
        }

        // 检查每个输入是否匹配对应的槽位（包括数量）
        for (int i = 0; i < inputs.size(); i++) {
            ItemStack stack = container.getItem(i);
            if (!inputs.get(i).test(stack)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return output.isEmpty() ? ItemStack.EMPTY : output.get(0).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.isEmpty() ? ItemStack.EMPTY : output.get(0).copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DTRecipeSerializers.SHREDDER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return DTRecipe.SHREDDER_RECIPE.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for (CountedIngredient input : inputs) {
            ingredients.add(input.ingredient());
        }
        return ingredients;
    }

    public List<CountedIngredient> getInputs() {
        return new ArrayList<>(inputs); // 返回副本以防止外部修改
    }

    public List<ItemStack> getOutput() {
        return new ArrayList<>(output); // 返回副本以防止外部修改
    }

    // 网络序列化支持
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeVarInt(inputs.size());
        for (CountedIngredient input : inputs) {
            input.ingredient().toNetwork(buffer);
            buffer.writeVarInt(input.count());
        }

        buffer.writeVarInt(output.size());
        for (ItemStack stack : output) {
            buffer.writeItem(stack);
        }
    }

    public static ShredderRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        int inputCount = buffer.readVarInt();
        List<CountedIngredient> inputs = new ArrayList<>();
        for (int i = 0; i < inputCount; i++) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            int count = buffer.readVarInt();
            inputs.add(new CountedIngredient(ingredient, count));
        }

        int outputCount = buffer.readVarInt();
        List<ItemStack> outputs = new ArrayList<>();
        for (int i = 0; i < outputCount; i++) {
            outputs.add(buffer.readItem());
        }

        return new ShredderRecipe(inputs, outputs, id);
    }
}
