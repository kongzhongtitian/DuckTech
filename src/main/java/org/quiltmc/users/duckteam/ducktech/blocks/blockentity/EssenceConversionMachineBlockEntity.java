package org.quiltmc.users.duckteam.ducktech.blocks.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.users.duckteam.ducktech.api.block.DTBaseProcessingBlockEntity;
import org.quiltmc.users.duckteam.ducktech.api.recipes.InputOutputRecipe;
import org.quiltmc.users.duckteam.ducktech.blocks.DTBlockEntity;
import org.quiltmc.users.duckteam.ducktech.gui.essence_conversion_machine.EssenceConversionMachineMenu;
import org.quiltmc.users.duckteam.ducktech.recipe.CountedIngredient;
import org.quiltmc.users.duckteam.ducktech.recipe.DTRecipe;
import org.quiltmc.users.duckteam.ducktech.recipe.custom.advanceshredder.AdvanceShredderRecipe;
import org.quiltmc.users.duckteam.ducktech.recipe.custom.essence_conversion_machine.EssenceConversionMachineRecipe;
import org.quiltmc.users.duckteam.ducktech.recipe.custom.shredder.ShredderRecipe;
import org.quiltmc.users.duckteam.ducktech.sounds.DTSounds;
import org.quiltmc.users.duckteam.ducktech.utils.RecipeOutputUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EssenceConversionMachineBlockEntity extends DTBaseProcessingBlockEntity implements MenuProvider {
    public static final int INPUT_SLOT_1 = 0;
    public static final int INPUT_SLOT_2 = 1;
    public static final int OUTPUT_SLOT = 2;

    public EssenceConversionMachineBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.ESSENCE_CONVERSION_MACHINE_BLOCK_ENTITY.get(), pos, state);
        this.setItemStackHandler(5);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ducktech.essence_conversion_machine");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new EssenceConversionMachineMenu(containerId, inventory, this, this.data);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        Optional<EssenceConversionMachineRecipe> recipe = getRecipe(DTRecipe.ESSENCE_CONVERSION_MACHINE_RECIPE.get());

        if (recipe.isPresent() && hasRecipe(DTRecipe.ESSENCE_CONVERSION_MACHINE_RECIPE.get())) {
            this.maxProgress = recipe.get().getProcessingTime() > 0 ?
                    recipe.get().getProcessingTime() : 20;
            this.data.set(1, this.maxProgress);

            progress++;
            this.data.set(0, this.progress);
            setChanged();

            if (progress >= maxProgress) {
                craftItem(recipe.get());
                resetProgress();
                if (!level.isClientSide()) {
                    level.playSound(null, pos,
                            DTSounds.ZAOYIN.get(),
                            SoundSource.BLOCKS,
                            1.0F,
                            1.0F);
                }
            }
        } else {
            resetProgress();
        }
    }

    private <T extends InputOutputRecipe> void craftItem(T recipe) {
        RecipeOutputUtil.consumeInputs(recipe, itemStackHandler, List.of(INPUT_SLOT_1, INPUT_SLOT_2));
        RecipeOutputUtil.produceOutputs(recipe.getOutputs(), itemStackHandler, List.of(2));
    }

    private void resetProgress() {
        progress = 0;
        maxProgress = 20;
        this.data.set(0, progress);
        this.data.set(1, maxProgress);
        setChanged();
    }

    private <T extends InputOutputRecipe> boolean hasRecipe(RecipeType<T> recipeType) {
        if (recipeType == null) return false;

        Optional<List<ItemStack>> outputs = RecipeOutputUtil.getOutputs(recipeType, getSlotsItemStack(), level);
        if (outputs.isEmpty()) return false;

        return RecipeOutputUtil.canFitOutputs(outputs.get(), getSlotsOutputItemStack());
    }

    private <T extends InputOutputRecipe> Optional<T> getRecipe(RecipeType<T> recipeType) {
        if (level == null) return Optional.empty();
        return RecipeOutputUtil.getRecipe(recipeType, getSlotsItemStack(), level);
    }

    protected List<ItemStack> getSlotsItemStack() {
        return List.of(
                itemStackHandler.getStackInSlot(INPUT_SLOT_1),
                itemStackHandler.getStackInSlot(INPUT_SLOT_2)
        );
    }

    protected List<ItemStack> getSlotsOutputItemStack() {
        return List.of(
                itemStackHandler.getStackInSlot(OUTPUT_SLOT)
        );
    }
}
