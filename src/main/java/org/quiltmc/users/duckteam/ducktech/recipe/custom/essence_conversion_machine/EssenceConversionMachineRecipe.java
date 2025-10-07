package org.quiltmc.users.duckteam.ducktech.recipe.custom.essence_conversion_machine;

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
import org.quiltmc.users.duckteam.ducktech.recipe.CountedIngredient;
import org.quiltmc.users.duckteam.ducktech.recipe.DTRecipe;
import org.quiltmc.users.duckteam.ducktech.recipe.DTRecipeSerializers;

import java.util.ArrayList;
import java.util.List;

public class EssenceConversionMachineRecipe implements Recipe<SimpleContainer> {

    private final List<CountedIngredient> inputs; // 支持数量的输入物品 (最多2个)
    private final List<ItemStack> output;         // 多个输出物品 (最多3个)
    private final ResourceLocation id;
    private final int processingTime;             // 处理时间（tick）

    public EssenceConversionMachineRecipe(List<CountedIngredient> inputs, List<ItemStack> output, ResourceLocation id, int processingTime) {
        // 限制输入最多为2个
        this.inputs = new ArrayList<>();
        for (int i = 0; i < Math.min(inputs.size(), 2); i++) {
            this.inputs.add(inputs.get(i));
        }

        // 限制输出最多为3个
        this.output = new ArrayList<>();
        for (int i = 0; i < Math.min(output.size(), 1); i++) {
            this.output.add(output.get(i));
        }

        this.id = id;
        this.processingTime = processingTime;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer container, Level level) {
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
        return width * height >= inputs.size();
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
        return DTRecipeSerializers.ESSENCE_CONVERSION_MACHINE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return DTRecipe.ESSENCE_CONVERSION_MACHINE_RECIPE.get();
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

    public int getProcessingTime() {
        return processingTime;
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

        buffer.writeVarInt(processingTime);
    }

    public static EssenceConversionMachineRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
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

        int processingTime = buffer.readVarInt();

        return new EssenceConversionMachineRecipe(inputs, outputs, id, processingTime);
    }
}
