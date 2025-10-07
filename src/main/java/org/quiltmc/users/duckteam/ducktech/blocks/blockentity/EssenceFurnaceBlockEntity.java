package org.quiltmc.users.duckteam.ducktech.blocks.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.users.duckteam.ducktech.api.block.DTBaseBlockEntity;
import org.quiltmc.users.duckteam.ducktech.blocks.DTBlockEntity;
import org.quiltmc.users.duckteam.ducktech.gui.essence_furnace.EssenceFurnaceMenu;
import org.quiltmc.users.duckteam.ducktech.items.DTItems;

public class EssenceFurnaceBlockEntity extends DTBaseBlockEntity implements MenuProvider {
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 40;

    public EssenceFurnaceBlockEntity( BlockPos p_155229_, BlockState p_155230_) {
        super(DTBlockEntity.ESSENCE_FURNACE_BLOCK_ENTITY.get(), p_155229_, p_155230_);

        setItemStackHandler(2);

        data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case  0 -> EssenceFurnaceBlockEntity.this.progress;
                    case  1 -> EssenceFurnaceBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0:
                        EssenceFurnaceBlockEntity.this.progress = value;
                        break;
                    case 1:
                        EssenceFurnaceBlockEntity.this.maxProgress = value;
                        break;
                }
            }
            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        progress = tag.getInt("progress");
        maxProgress = tag.getInt("maxProgress");

    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putInt("progress", progress);
        tag.putInt("maxProgress", maxProgress);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ducktech.essence_furnace");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new EssenceFurnaceMenu(p_39954_, p_39955_, this, data);
    }

    public void tick(Level level, BlockPos pos, BlockState state ) {
        if (level.isClientSide()) return;

        if (!getItemStackHandler().getStackInSlot(0).isEmpty()) {
            if (getItemStackHandler().getStackInSlot(0).is(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("ducktech", "essence")))) {
                if (progress < maxProgress) {
                    progress++;
                } else {
                    ItemStack outputStack = getItemStackHandler().getStackInSlot(1);
                    ItemStack item = DTItems.BASIC_ESSENCE.get().getDefaultInstance();

                    if (outputStack.isEmpty()) {
                        getItemStackHandler().setStackInSlot(1, item);
                        getItemStackHandler().getStackInSlot(0).shrink(1);
                        progress = 0;
                    } else if (outputStack.getItem() == Items.GOLD_INGOT && outputStack.getCount() < outputStack.getMaxStackSize()) {
                        outputStack.grow(1);
                        getItemStackHandler().getStackInSlot(0).shrink(1);
                        progress = 0;
                    }
                }

                setChanged();
            } else {
                if (progress > 0) {
                    progress = 0;
                    setChanged();
                }
            }
        } else {
            if (progress > 0) {
                progress = 0;
                setChanged();
            }
        }
    }

    private IItemHandlerModifiable getItemStackHandler() {
        return  this.itemStackHandler;
    }
}
