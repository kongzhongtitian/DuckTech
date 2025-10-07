package org.quiltmc.users.duckteam.ducktech.blocks.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.quiltmc.users.duckteam.ducktech.blocks.DTBlockEntity;
import org.quiltmc.users.duckteam.ducktech.recipe.CountedIngredient;
import org.quiltmc.users.duckteam.ducktech.recipe.DTRecipe;
import org.quiltmc.users.duckteam.ducktech.recipe.custom.shredder.ShredderRecipe;
import org.quiltmc.users.duckteam.ducktech.sounds.DTSounds;

import java.util.ArrayList;
import java.util.List;

public class ShredderBlockEntity extends BlockEntity {
    private int processingTime = 40; // 增加处理时间
    private int currentTime = 0;
    private boolean isProcessing = false;

    // 用于存储正在处理的配方和相关物品
    private ShredderRecipe currentRecipe;
    private List<ItemEntity> matchedEntities;

    public ShredderBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(DTBlockEntity.SHREDDER_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public void tick(Level level, BlockPos pos, BlockState state, ShredderBlockEntity entity) {
        if (level.isClientSide) return;

        if (isProcessing) {
            currentTime++;

            if (currentTime >= processingTime) {
                completeProcessing(level, pos);
                resetProcessing();
            }
            return;
        }

        // 检查并开始处理
        checkAndStartProcessing(level, pos);
    }

    private void checkAndStartProcessing(Level level, BlockPos pos) {
        List<ItemEntity> itemEntities = getItemEntitiesAbove(level, pos);

        if (itemEntities.isEmpty()) return;

        // 获取所有可用的粉碎机配方
        List<ShredderRecipe> recipes = level.getRecipeManager()
                .getAllRecipesFor(DTRecipe.SHREDDER_RECIPE.get());

        // 尝试匹配每个配方
        for (ShredderRecipe recipe : recipes) {
            List<ItemEntity> matchedEntities = tryMatchRecipe(itemEntities, recipe);
            if (!matchedEntities.isEmpty()) {
                // 找到匹配的配方，开始处理
                if (!level.isClientSide()) {
                    level.playSound(null, pos,
                            DTSounds.ZAOYIN.get(),
                            SoundSource.BLOCKS,
                            1.0F,
                            1.0F);
                }
                startProcessing(recipe, matchedEntities);
                return;
            }
        }
    }

    // 尝试匹配配方，返回匹配的物品实体列表
// 尝试匹配配方，返回匹配的物品实体列表
    private List<ItemEntity> tryMatchRecipe(List<ItemEntity> itemEntities, ShredderRecipe recipe) {
        List<CountedIngredient> ingredients = recipe.getInputs();
        List<ItemEntity> matchedEntities = new ArrayList<>();
        boolean[] usedEntities = new boolean[itemEntities.size()];

        // 为每个配方成分寻找匹配的物品
        for (CountedIngredient countedIngredient : ingredients) {
            boolean found = false;

            for (int i = 0; i < itemEntities.size(); i++) {
                if (!usedEntities[i]) {
                    ItemEntity entity = itemEntities.get(i);
                    if (countedIngredient.test(entity.getItem())) {
                        matchedEntities.add(entity);
                        usedEntities[i] = true;
                        found = true;
                        break;
                    }
                }
            }

            // 如果没有找到匹配的物品，则此配方不匹配
            if (!found) {
                return new ArrayList<>(); // 返回空列表表示不匹配
            }
        }

        return matchedEntities;
    }


    private void startProcessing(ShredderRecipe recipe, List<ItemEntity> entities) {
        isProcessing = true;
        currentTime = 0;
        currentRecipe = recipe;
        matchedEntities = new ArrayList<>(entities);
    }

    private void completeProcessing(Level level, BlockPos pos) {
        if (currentRecipe == null || matchedEntities == null) return;

        List<CountedIngredient> inputs = currentRecipe.getInputs();

        // 消耗输入物品（根据配方指定的数量）
        for (int i = 0; i < matchedEntities.size(); i++) {
            ItemEntity entity = matchedEntities.get(i);
            if (entity != null && entity.isAlive()) {
                ItemStack stack = entity.getItem();
                int countToConsume = inputs.get(i).count(); // 使用配方中指定的数量
                stack.shrink(countToConsume);

                if (stack.isEmpty()) {
                    entity.discard();
                } else {
                    entity.setItem(stack);
                }
            }
        }

        // 生成输出物品
        spawnOutputs(level, pos, currentRecipe);
    }

    private void spawnOutputs(Level level, BlockPos pos, ShredderRecipe recipe) {
        List<ItemStack> outputs = recipe.getOutput();

        for (ItemStack output : outputs) {
            if (!output.isEmpty()) {

                // 在粉碎机上方生成物品
                if (!output.isEmpty()) {
                    // 在方块侧面或下方生成输出，避免卡住
                    BlockPos spawnPos = findSpawnPosition(level, pos);
                    ItemEntity outputEntity = new ItemEntity(
                            level,
                            spawnPos.getX() + 0.5,
                            spawnPos.getY() + 0.5,
                            spawnPos.getZ() + 0.5,
                            output.copy()
                    );
                    outputEntity.setDeltaMovement(0, 0.1, 0); // 给一点速度避免卡住

                    // 给物品一些随机运动
                    outputEntity.setDeltaMovement(
                            (level.random.nextFloat() - 0.5) * 0.1,
                            0.2,
                            (level.random.nextFloat() - 0.5) * 0.1
                    );

                    level.addFreshEntity(outputEntity);
                }
            }
        }
    }

    private BlockPos findSpawnPosition(Level level, BlockPos pos) {
        // 优先在下方生成
        BlockPos belowPos = pos.below();
        if (level.isEmptyBlock(belowPos)) {
            return belowPos;
        }

        // 其次在侧面生成
        BlockPos[] sidePositions = {
                pos.north(), pos.south(), pos.east(), pos.west()
        };

        for (BlockPos sidePos : sidePositions) {
            if (level.isEmptyBlock(sidePos)) {
                return sidePos;
            }
        }

        // 最后在原位置上方生成
        return pos.above();
    }

    private void resetProcessing() {
        isProcessing = false;
        currentTime = 0;
        currentRecipe = null;
        matchedEntities = null;
    }

    private static List<ItemEntity> getItemEntitiesAbove(Level level, BlockPos pos) {
        AABB detectionArea = new AABB(
                pos.getX(), pos.getY() + 1, pos.getZ(),
                pos.getX() + 1, pos.getY() + 1.2, pos.getZ() + 1
        );
        return level.getEntitiesOfClass(ItemEntity.class, detectionArea);
    }

    private static boolean hasItem(Level level, BlockPos pos) {
        return !getItemEntitiesAbove(level, pos).isEmpty();
    }



    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
    }
}
