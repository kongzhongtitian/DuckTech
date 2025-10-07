package org.quiltmc.users.duckteam.ducktech.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.users.duckteam.ducktech.blocks.FacingBlock;
import org.quiltmc.users.duckteam.ducktech.blocks.blockentity.EssenceConversionMachineBlockEntity;

public class EssenceConversionMachine extends FacingBlock implements EntityBlock {
    public EssenceConversionMachine(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new EssenceConversionMachineBlockEntity(p_153215_, p_153216_);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return level.isClientSide() ? null : (level1, pos, state, entity) -> ((EssenceConversionMachineBlockEntity)entity).tick(level1, pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if ( !state.is(newState.getBlock())){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof EssenceConversionMachineBlockEntity processorBlockEntity) {
                processorBlockEntity.drops();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof EssenceConversionMachineBlockEntity EssenceConversionMachineBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) player, EssenceConversionMachineBlockEntity , pos);

                return InteractionResult.SUCCESS;
            }else {
                throw  new IllegalStateException("our container provider is missing");
            }
        }

        return super.use( state, level, pos, player, hand, hitResult);
    }
}
