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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.users.duckteam.ducktech.blocks.DTBlockEntity;
import org.quiltmc.users.duckteam.ducktech.gui.essence_conversion_machine.EssenceConversionMachineMenu;
import org.quiltmc.users.duckteam.ducktech.recipe.CountedIngredient;
import org.quiltmc.users.duckteam.ducktech.recipe.DTRecipe;
import org.quiltmc.users.duckteam.ducktech.recipe.custom.advanceshredder.AdvanceShredderRecipe;
import org.quiltmc.users.duckteam.ducktech.recipe.custom.essence_conversion_machine.EssenceConversionMachineRecipe;
import org.quiltmc.users.duckteam.ducktech.recipe.custom.shredder.ShredderRecipe;
import org.quiltmc.users.duckteam.ducktech.sounds.DTSounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EssenceConversionMachineBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemStackHandler = new ItemStackHandler(5){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide)level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            super.onContentsChanged(slot);
        }
    };

    public static final int INPUT_SLOT_1 = 0;
    public static final int INPUT_SLOT_2 = 1;
    public static final int OUTPUT_SLOT_1 = 4;
    public static final int OUTPUT_SLOT_2 = 3;
    public static final int OUTPUT_SLOT_3 = 2;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 20;

    public EssenceConversionMachineBlockEntity(BlockPos pos, BlockState state) {
        super(DTBlockEntity.ESSENCE_CONVERSION_MACHINE_BLOCK_ENTITY.get(), pos, state);

        data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case  0 -> EssenceConversionMachineBlockEntity.this.progress;
                    case  1 -> EssenceConversionMachineBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0:
                        EssenceConversionMachineBlockEntity.this.progress = value;
                        break;
                    case 1:
                        EssenceConversionMachineBlockEntity.this.maxProgress = value;
                        break;
                }
            }
            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inv.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
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

        // 首先尝试高级粉碎机配方
        Optional<EssenceConversionMachineRecipe> advanceRecipe = getCurrentAdvanceRecipe();

        if (advanceRecipe.isPresent()) {
            // 高级配方使用配方中定义的处理时间
            this.maxProgress = advanceRecipe.get().getProcessingTime() > 0 ?
                    advanceRecipe.get().getProcessingTime() : 20;
            this.data.set(1, this.maxProgress);

            progress++;
            this.data.set(0, this.progress);
            setChanged();

            if (progress >= maxProgress) {
                craftAdvanceItem(advanceRecipe.get());
                resetProgress();
                if (!level.isClientSide()) {
                    level.playSound(null, pos,
                            DTSounds.ZAOYIN.get(),
                            SoundSource.BLOCKS,
                            1.0F,
                            1.0F);
                }
            }
        }else {
            resetProgress();
        }
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void craftAdvanceItem(EssenceConversionMachineRecipe recipe) {

        List<CountedIngredient> inputs = recipe.getInputs();
        List<ItemStack> inputStacks = new ArrayList<>();
        inputStacks.add(itemStackHandler.getStackInSlot(INPUT_SLOT_1));
        inputStacks.add(itemStackHandler.getStackInSlot(INPUT_SLOT_2));

        // 为每个输入材料找到匹配的槽位并消耗
        for (CountedIngredient ingredient : inputs) {
            for (int i = 0; i < inputStacks.size(); i++) {
                ItemStack stack = inputStacks.get(i);
                if (ingredient.test(stack) && stack.getCount() >= ingredient.count()) {
                    stack.shrink(ingredient.count());
                    itemStackHandler.setStackInSlot(i, stack);
                    inputStacks.set(i, stack); // 更新列表中的堆栈
                    break;
                }
            }
        }


        List<ItemStack> outputs = recipe.getOutput();
        List<ItemStack> outputStacks = new ArrayList<>();
        outputStacks.add(itemStackHandler.getStackInSlot(OUTPUT_SLOT_1));
        outputStacks.add(itemStackHandler.getStackInSlot(OUTPUT_SLOT_2));
        outputStacks.add(itemStackHandler.getStackInSlot(OUTPUT_SLOT_3));

        for (ItemStack result : outputs) {
            ItemStack resultCopy = result.copy();

            // 尝试将输出分配到所有输出槽
            for (int i = 0; i < outputStacks.size() && !resultCopy.isEmpty(); i++) {
                ItemStack outputSlot = outputStacks.get(i);

                if (outputSlot.isEmpty()) {
                    // 空槽位，直接放入
                    itemStackHandler.setStackInSlot(OUTPUT_SLOT_3 + i, resultCopy);
                    outputStacks.set(i, resultCopy);
                    resultCopy = ItemStack.EMPTY;
                } else if (itemsMatch(outputSlot, resultCopy) &&
                        outputSlot.getCount() + resultCopy.getCount() <= outputSlot.getMaxStackSize()) {
                    // 相同物品且可以合并
                    outputSlot.grow(resultCopy.getCount());
                    itemStackHandler.setStackInSlot(OUTPUT_SLOT_3 + i, outputSlot);
                    outputStacks.set(i, outputSlot);
                    resultCopy = ItemStack.EMPTY;
                }
            }

            // 如果还有剩余物品，尝试分配到其他槽位
            if (!resultCopy.isEmpty()) {
                for (int i = 0; i < outputStacks.size() && !resultCopy.isEmpty(); i++) {
                    ItemStack outputSlot = outputStacks.get(i);
                    if (outputSlot.isEmpty()) {
                        itemStackHandler.setStackInSlot(OUTPUT_SLOT_3 + i, resultCopy);
                        outputStacks.set(i, resultCopy);
                        resultCopy = ItemStack.EMPTY;
                    }
                }
            }
        }
    }

    private void craftBasicItem(ShredderRecipe recipe) {
        // 消耗输入物品（无视顺序）
        List<CountedIngredient> inputs = recipe.getInputs();
        List<ItemStack> inputStacks = new ArrayList<>();
        inputStacks.add(itemStackHandler.getStackInSlot(INPUT_SLOT_1));
        inputStacks.add(itemStackHandler.getStackInSlot(INPUT_SLOT_2));

        // 为每个输入材料找到匹配的槽位并消耗
        for (CountedIngredient ingredient : inputs) {
            for (int i = 0; i < inputStacks.size(); i++) {
                ItemStack stack = inputStacks.get(i);
                if (ingredient.test(stack) && stack.getCount() >= ingredient.count()) {
                    stack.shrink(ingredient.count());
                    itemStackHandler.setStackInSlot(i, stack);
                    inputStacks.set(i, stack); // 更新列表中的堆栈
                    break;
                }
            }
        }

        // 生成输出物品（分配到所有输出槽）
        List<ItemStack> outputs = recipe.getOutput();
        List<ItemStack> outputStacks = new ArrayList<>();
        outputStacks.add(itemStackHandler.getStackInSlot(OUTPUT_SLOT_1));
        outputStacks.add(itemStackHandler.getStackInSlot(OUTPUT_SLOT_2));
        outputStacks.add(itemStackHandler.getStackInSlot(OUTPUT_SLOT_3));

        for (ItemStack result : outputs) {
            ItemStack resultCopy = result.copy();

            // 尝试将输出分配到所有输出槽
            for (int i = 0; i < outputStacks.size() && !resultCopy.isEmpty(); i++) {
                ItemStack outputSlot = outputStacks.get(i);

                if (outputSlot.isEmpty()) {
                    // 空槽位，直接放入
                    itemStackHandler.setStackInSlot(OUTPUT_SLOT_3 - i, resultCopy);
                    outputStacks.set(i, resultCopy);
                    resultCopy = ItemStack.EMPTY;
                } else if (itemsMatch(outputSlot, resultCopy) &&
                        outputSlot.getCount() + resultCopy.getCount() <= outputSlot.getMaxStackSize()) {
                    // 相同物品且可以合并
                    outputSlot.grow(resultCopy.getCount());
                    itemStackHandler.setStackInSlot(OUTPUT_SLOT_3 - i, outputSlot);
                    outputStacks.set(i, outputSlot);
                    resultCopy = ItemStack.EMPTY;
                }
            }

            // 如果还有剩余物品，尝试分配到其他槽位
            if (!resultCopy.isEmpty()) {
                for (int i = 0; i < outputStacks.size() && !resultCopy.isEmpty(); i++) {
                    ItemStack outputSlot = outputStacks.get(i);
                    if (outputSlot.isEmpty()) {
                        itemStackHandler.setStackInSlot(OUTPUT_SLOT_3 - i, resultCopy);
                        outputStacks.set(i, resultCopy);
                        resultCopy = ItemStack.EMPTY;
                    }
                }
            }
        }
    }

    private void resetProgress() {
        progress = 0;
        maxProgress = 20;
        this.data.set(0, progress);
        this.data.set(1, maxProgress);
        setChanged();
    }

    public boolean hasAdvanceRecipe(Optional<EssenceConversionMachineRecipe> recipe) {
        if (recipe.isEmpty()) return false;

        // 无视顺序
        List<CountedIngredient> inputs = recipe.get().getInputs();
        List<ItemStack> availableInputs = new ArrayList<>();
        availableInputs.add(itemStackHandler.getStackInSlot(INPUT_SLOT_1));
        availableInputs.add(itemStackHandler.getStackInSlot(INPUT_SLOT_2));


        List<ItemStack> simulatedInputs = new ArrayList<>();
        for (ItemStack stack : availableInputs) {
            simulatedInputs.add(stack.copy());
        }

        // 无视顺序
        for (CountedIngredient ingredient : inputs) {
            boolean found = false;
            for (int i = 0; i < simulatedInputs.size(); i++) {
                ItemStack inputStack = simulatedInputs.get(i);
                if (ingredient.test(inputStack) && inputStack.getCount() >= ingredient.count()) {
                    // 模拟消耗
                    inputStack.shrink(ingredient.count());
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }


        List<ItemStack> outputs = recipe.get().getOutput();
        List<ItemStack> outputSlots = new ArrayList<>();
        outputSlots.add(itemStackHandler.getStackInSlot(OUTPUT_SLOT_1));

        // 复制一份用于模拟输出
        List<ItemStack> simulatedOutputs = new ArrayList<>();
        for (ItemStack stack : outputSlots) {
            simulatedOutputs.add(stack.copy());
        }

        // 检查所有输出物品是否可以放入
        for (ItemStack result : outputs) {
            ItemStack resultCopy = result.copy();
            boolean placed = false;

            // 首先尝试合并到已有堆栈
            for (int i = 0; i < simulatedOutputs.size(); i++) {
                ItemStack outputSlot = simulatedOutputs.get(i);
                if (!outputSlot.isEmpty() && itemsMatch(outputSlot, resultCopy)) {
                    int space = outputSlot.getMaxStackSize() - outputSlot.getCount();
                    if (space >= resultCopy.getCount()) {
                        outputSlot.grow(resultCopy.getCount());
                        placed = true;
                        break;
                    } else if (space > 0) {
                        resultCopy.shrink(space);
                        outputSlot.grow(space);
                    }
                }
            }

            // 然后尝试放入空槽
            if (!placed) {
                for (int i = 0; i < simulatedOutputs.size(); i++) {
                    if (simulatedOutputs.get(i).isEmpty()) {
                        simulatedOutputs.set(i, resultCopy);
                        placed = true;
                        break;
                    }
                }
            }

            if (!placed) {
                return false;
            }
        }

        return true;
    }

    public boolean hasBasicRecipe(Optional<ShredderRecipe> recipe) {
        if (recipe.isEmpty()) return false;

        // 检查输入
        List<CountedIngredient> inputs = recipe.get().getInputs();
        List<ItemStack> availableInputs = new ArrayList<>();
        availableInputs.add(itemStackHandler.getStackInSlot(INPUT_SLOT_1));
        availableInputs.add(itemStackHandler.getStackInSlot(INPUT_SLOT_2));

        // 复制一份用于模拟消耗
        List<ItemStack> simulatedInputs = new ArrayList<>();
        for (ItemStack stack : availableInputs) {
            simulatedInputs.add(stack.copy());
        }

        // 检查所有输入材料是否都满足
        for (CountedIngredient ingredient : inputs) {
            boolean found = false;
            for (int i = 0; i < simulatedInputs.size(); i++) {
                ItemStack inputStack = simulatedInputs.get(i);
                if (ingredient.test(inputStack) && inputStack.getCount() >= ingredient.count()) {
                    // 模拟消耗
                    inputStack.shrink(ingredient.count());
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }

        // 检查输出
        List<ItemStack> outputs = recipe.get().getOutput();
        List<ItemStack> outputSlots = new ArrayList<>();
        outputSlots.add(itemStackHandler.getStackInSlot(OUTPUT_SLOT_1));


        List<ItemStack> simulatedOutputs = new ArrayList<>();
        for (ItemStack stack : outputSlots) {
            simulatedOutputs.add(stack.copy());
        }

        // 检查所有输出物品是否可以放入
        for (ItemStack result : outputs) {
            ItemStack resultCopy = result.copy();
            boolean placed = false;

            // 首先尝试合并到已有堆栈
            for (int i = 0; i < simulatedOutputs.size(); i++) {
                ItemStack outputSlot = simulatedOutputs.get(i);
                if (!outputSlot.isEmpty() && itemsMatch(outputSlot, resultCopy)) {
                    int space = outputSlot.getMaxStackSize() - outputSlot.getCount();
                    if (space >= resultCopy.getCount()) {
                        outputSlot.grow(resultCopy.getCount());
                        placed = true;
                        break;
                    } else if (space > 0) {
                        resultCopy.shrink(space);
                        outputSlot.grow(space);
                    }
                }
            }

            // 然后尝试放入空槽
            if (!placed) {
                for (int i = 0; i < simulatedOutputs.size(); i++) {
                    if (simulatedOutputs.get(i).isEmpty()) {
                        simulatedOutputs.set(i, resultCopy);
                        placed = true;
                        break;
                    }
                }
            }

            if (!placed) {
                return false;
            }
        }

        return true;
    }


    private boolean itemsMatch(ItemStack stack1, ItemStack stack2) {
        return ItemStack.isSameItem(stack1, stack2) &&
                ItemStack.isSameItemSameTags(stack1, stack2);
    }

    private Optional<EssenceConversionMachineRecipe> getCurrentAdvanceRecipe() {
        if (level == null) return Optional.empty();


        NonNullList<ItemStack> inputItems = NonNullList.create();
        inputItems.add(itemStackHandler.getStackInSlot(INPUT_SLOT_1));
        inputItems.add(itemStackHandler.getStackInSlot(INPUT_SLOT_2));


        SimpleContainer inventory = new SimpleContainer(inputItems.toArray(new ItemStack[0]));


        List<EssenceConversionMachineRecipe> recipes = level.getRecipeManager().getAllRecipesFor(DTRecipe.ESSENCE_CONVERSION_MACHINE_RECIPE.get());
        for (EssenceConversionMachineRecipe recipe : recipes) {
            if (hasAdvanceRecipe(Optional.of(recipe))) {
                return Optional.of(recipe);
            }
        }

        return Optional.empty();
    }

    private Optional<ShredderRecipe> getCurrentBasicRecipe() {
        if (level == null) return Optional.empty();


        NonNullList<ItemStack> inputItems = NonNullList.create();
        inputItems.add(itemStackHandler.getStackInSlot(INPUT_SLOT_1));
        inputItems.add(itemStackHandler.getStackInSlot(INPUT_SLOT_2));


        SimpleContainer inventory = new SimpleContainer(inputItems.toArray(new ItemStack[0]));


        List<ShredderRecipe> recipes = level.getRecipeManager().getAllRecipesFor(DTRecipe.SHREDDER_RECIPE.get());
        for (ShredderRecipe recipe : recipes) {
            if (hasBasicRecipe(Optional.of(recipe))) {
                return Optional.of(recipe);
            }
        }

        return Optional.empty();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                // 如果没有指定方向，允许完全访问
                return LazyOptional.of(() -> itemStackHandler).cast();
            }

            // 只允许从上方和侧面输入到输入槽
            if (side == Direction.UP || side.getAxis().isHorizontal()) {
                // 创建一个仅允许访问输入槽的 handler
                return LazyOptional.of(() -> new IItemHandler() {
                    @Override
                    public int getSlots() {
                        return itemStackHandler.getSlots();
                    }

                    @Override
                    public ItemStack getStackInSlot(int slot) {
                        // 只能查看输入槽
                        if (slot == INPUT_SLOT_1 || slot == INPUT_SLOT_2) {
                            return itemStackHandler.getStackInSlot(slot);
                        }
                        return ItemStack.EMPTY;
                    }

                    @Override
                    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                        // 只能向输入槽插入物品
                        if (slot == INPUT_SLOT_1 || slot == INPUT_SLOT_2) {
                            return itemStackHandler.insertItem(slot, stack, simulate);
                        }
                        return stack;
                    }

                    @Override
                    public ItemStack extractItem(int slot, int amount, boolean simulate) {
                        // 不允许通过这个handler提取物品
                        return ItemStack.EMPTY;
                    }

                    @Override
                    public int getSlotLimit(int slot) {
                        return itemStackHandler.getSlotLimit(slot);
                    }

                    @Override
                    public boolean isItemValid(int slot, ItemStack stack) {
                        // 只有输入槽可以放入物品
                        if (slot == INPUT_SLOT_1 || slot == INPUT_SLOT_2) {
                            return itemStackHandler.isItemValid(slot, stack);
                        }
                        return false;
                    }
                }).cast();
            }

            // 从下方只能提取输出物品
            if (side == Direction.DOWN) {
                return LazyOptional.of(() -> new IItemHandler() {
                    @Override
                    public int getSlots() {
                        return itemStackHandler.getSlots();
                    }

                    @Override
                    public ItemStack getStackInSlot(int slot) {
                        // 只能查看输出槽
                        if (slot == OUTPUT_SLOT_1 ) {
                            return itemStackHandler.getStackInSlot(slot);
                        }
                        return ItemStack.EMPTY;
                    }

                    @Override
                    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                        // 不允许插入物品
                        return stack;
                    }

                    @Override
                    public ItemStack extractItem(int slot, int amount, boolean simulate) {
                        // 只能从输出槽提取物品
                        if (slot == OUTPUT_SLOT_1 ) {
                            return itemStackHandler.extractItem(slot, amount, simulate);
                        }
                        return ItemStack.EMPTY;
                    }

                    @Override
                    public int getSlotLimit(int slot) {
                        return itemStackHandler.getSlotLimit(slot);
                    }

                    @Override
                    public boolean isItemValid(int slot, ItemStack stack) {
                        // 输出槽不接受外部输入
                        return false;
                    }
                }).cast();
            }
        }

        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemStackHandler.serializeNBT());
        tag.putInt("progress", progress);
        tag.putInt("maxProgress", maxProgress);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemStackHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("progress");
        maxProgress = tag.getInt("maxProgress");
    }
}