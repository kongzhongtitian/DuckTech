package org.quiltmc.users.duckteam.ducktech.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.users.duckteam.ducktech.blocks.FacingBlock;
import org.quiltmc.users.duckteam.ducktech.blocks.blockentity.VoidEssenceCollectorBlockEntity;

public class VoidEssenceCollector extends FacingBlock implements EntityBlock {
    public VoidEssenceCollector(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new VoidEssenceCollectorBlockEntity(p_153215_, p_153216_);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level1, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return level1.isClientSide ? null :(level, pos, state, blockEntity) -> ((VoidEssenceCollectorBlockEntity)blockEntity).tick(level, pos, state, (VoidEssenceCollectorBlockEntity)blockEntity);
    }
}
