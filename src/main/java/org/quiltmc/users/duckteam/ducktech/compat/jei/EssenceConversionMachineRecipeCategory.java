package org.quiltmc.users.duckteam.ducktech.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.users.duckteam.ducktech.DuckTech;
import org.quiltmc.users.duckteam.ducktech.blocks.DTBlocks;
import org.quiltmc.users.duckteam.ducktech.recipe.CountedIngredient;
import org.quiltmc.users.duckteam.ducktech.recipe.custom.essence_conversion_machine.EssenceConversionMachineRecipe;

import java.util.List;


public class EssenceConversionMachineRecipeCategory implements IRecipeCategory<EssenceConversionMachineRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "essence_conversion_machine");

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "textures/gui/shredder.png");

    public static final ResourceLocation SLOT = ResourceLocation.fromNamespaceAndPath(DuckTech.MODID, "textures/gui/slot.png");

    public static final RecipeType<EssenceConversionMachineRecipe> RECIPE_TYPE =
            new RecipeType<>(UID, EssenceConversionMachineRecipe.class);

    private final IDrawableBuilder background;
    private final IDrawable icon;
    private final IDrawableBuilder slot;

    public EssenceConversionMachineRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(TEXTURE, 0, 0, 176, 94);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,  new ItemStack(DTBlocks.ESSENCE_CONVERSION_MACHINE.get()));
        this.slot = guiHelper.drawableBuilder(SLOT, 0, 0, 18, 18);

        background.setTextureSize(176,94);
        slot.setTextureSize(18,18);
    }

    @Override
    public RecipeType<EssenceConversionMachineRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.ducktech.essence_conversion_machine");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    @SuppressWarnings("all")
    public @Nullable IDrawable getBackground() {
        return background.build();
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder iRecipeLayoutBuilder, EssenceConversionMachineRecipe shredderRecipe, IFocusGroup iFocusGroup) {
        NonNullList<Ingredient> ingredients = shredderRecipe.getIngredients();
        List<CountedIngredient> inputs = shredderRecipe.getInputs();

        if (ingredients.size() == 1){
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 44,34).addItemStack(inputs.get(0).createItemStack()).setBackground(slot.build(),-1,-1);
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 65,34).addItemStack(Items.AIR.getDefaultInstance()).setBackground(slot.build(),-1,-1);
        }else if (ingredients.size() == 2){
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 44,34).addItemStack(inputs.get(0).createItemStack()).setBackground(slot.build(),-1,-1);
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 65,34).addItemStack(inputs.get(1).createItemStack()).setBackground(slot.build(),-1,-1);
        }



        // 获取所有输出
        List<ItemStack> outputs = shredderRecipe.getOutput();


        // 根据输出数量动态布局
            // 单个输出 - 居中显示
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 108, 34).setBackground(slot.build(),-1,-1)
                    .addItemStack(outputs.get(0));




        }

    @Override
    public void draw(EssenceConversionMachineRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

        // 绘制文字
        Component text = Component.translatable("block.ducktech.essence_conversion_machine");

        int processingTime = recipe.getProcessingTime();
        double s = (double) processingTime / 20;

        guiGraphics.drawString(
                Minecraft.getInstance().font,
                text,
                8,  // x坐标
                6,  // y坐标
                0x404040, // 颜色 (灰色)
                false // 是否有阴影
        );
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                Component.translatable("jei.tooltip.time").append(": " + s +" s"),
                120,  // x坐标
                70,  // y坐标
                0x404040, // 颜色 (灰色)
                false // 是否有阴影
        );
    }
}
