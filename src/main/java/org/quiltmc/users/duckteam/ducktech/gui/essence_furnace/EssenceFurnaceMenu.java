package org.quiltmc.users.duckteam.ducktech.gui.essence_furnace;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.quiltmc.users.duckteam.ducktech.api.gui.DTBaseMenu;
import org.quiltmc.users.duckteam.ducktech.blocks.blockentity.AdvanceShredderBlockEntity;
import org.quiltmc.users.duckteam.ducktech.blocks.blockentity.EssenceFurnaceBlockEntity;
import org.quiltmc.users.duckteam.ducktech.gui.DTMenu;

public class EssenceFurnaceMenu extends DTBaseMenu {
    public final EssenceFurnaceBlockEntity blockEntity;
    private final ContainerData data;

    public EssenceFurnaceMenu( int containerId, Inventory inv, BlockEntity entity , ContainerData data) {
        super(DTMenu.ESSENCE_FURNACE_MENU.get(),containerId, inv, entity);


        addDataSlots(data);
        addPlayerInventory(inv);
        //Player player = (Player) event.getEntity();
        //player.inventory.add
        addPlayerHotbar(inv);
        blockEntity = (EssenceFurnaceBlockEntity) entity;

        this.data = data;

        IItemHandler itemHandler = blockEntity.itemStackHandler;

        this.addSlot(new SlotItemHandler(itemHandler,0,54,34));
        this.addSlot(new SlotItemHandler(itemHandler,1,104,34){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

    }
    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaleArrowProgress(){
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int arrowPixelSize = 24;

        return maxProgress != 0 &&  progress != 0 ? progress * arrowPixelSize / maxProgress : 0;
    }

    public EssenceFurnaceMenu(int containerId, Inventory inventory, FriendlyByteBuf friendlyByteBuf ){
        this(containerId, inventory, ((EssenceFurnaceBlockEntity) inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos())), new SimpleContainerData(2));
    }

    @Override
    public int setAdditionSlots() {
        return 2;
    }
}
