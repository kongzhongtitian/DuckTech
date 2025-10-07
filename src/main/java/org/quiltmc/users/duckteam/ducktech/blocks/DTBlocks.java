package org.quiltmc.users.duckteam.ducktech.blocks;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;
import org.quiltmc.users.duckteam.ducktech.DuckTech;
import org.quiltmc.users.duckteam.ducktech.blocks.blockentity.VoidHopperBlockEntity;
import org.quiltmc.users.duckteam.ducktech.blocks.custom.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.quiltmc.users.duckteam.ducktech.items.DTItems;

import java.util.function.Supplier;

public class DTBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DuckTech.MODID);

    //深板岩矿石
    private static final BlockBehaviour.Properties METAL_DEEPSLATE_ORE_PROPERTIES =
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .strength(5.0F, 6.0F)
                    .requiresCorrectToolForDrops();

    public static final RegistryObject<Block> DEEPSLATE_LEAD_ORE = registerBlock("deepslate_lead_ore",
            () -> new Block(METAL_DEEPSLATE_ORE_PROPERTIES));

    public static final RegistryObject<Block> DEEPSLATE_ALUMINUM_ORE = registerBlock("deepslate_aluminum_ore",
            () -> new Block(METAL_DEEPSLATE_ORE_PROPERTIES));

    public static final RegistryObject<Block> DEEPSLATE_TIN_ORE = registerBlock("deepslate_tin_ore",
            () -> new Block(METAL_DEEPSLATE_ORE_PROPERTIES));

    public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE = registerBlock("deepslate_silver_ore",
            () -> new Block(METAL_DEEPSLATE_ORE_PROPERTIES));


    //材料
    private static final BlockBehaviour.Properties METAL_BLOCK_PROPERTIES =
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .strength(5.0F, 6.0F)
                    .requiresCorrectToolForDrops();

    public static final RegistryObject<Block> LEAD_BLOCK = registerBlock("lead_block",
            () -> new Block(METAL_BLOCK_PROPERTIES));

    public static final RegistryObject<Block> ALUMINUM_BLOCK = registerBlock("aluminum_block",
            () -> new Block(METAL_BLOCK_PROPERTIES));

    public static final RegistryObject<Block> TIN_BLOCK = registerBlock("tin_block",
            () -> new Block(METAL_BLOCK_PROPERTIES));

    public static final RegistryObject<Block> SILVER_BLOCK = registerBlock("silver_block",
            () -> new Block(METAL_BLOCK_PROPERTIES));

    public static final RegistryObject<Block> BRONZE_BLOCK = registerBlock("bronze_block",
            () -> new Block(METAL_BLOCK_PROPERTIES));


    //矿石
    private static final BlockBehaviour.Properties METAL_ORE_PROPERTIES =
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .strength(5.0F, 6.0F)
                    .requiresCorrectToolForDrops();

    public static final RegistryObject<Block> LEAD_ORE = registerBlock("lead_ore",
            () -> new Block(METAL_ORE_PROPERTIES));

    public static final RegistryObject<Block> ALUMINUM_ORE = registerBlock("aluminum_ore",
            () -> new Block(METAL_ORE_PROPERTIES));

    public static final RegistryObject<Block> TIN_ORE = registerBlock("tin_ore",
            () -> new Block(METAL_ORE_PROPERTIES));

    public static final RegistryObject<Block> SILVER_ORE = registerBlock("silver_ore",
            () -> new Block(METAL_ORE_PROPERTIES));


    //raw
    private static final BlockBehaviour.Properties METAL_RAW_BLOCK_PROPERTIES =
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .strength(5.0F, 6.0F)
                    .requiresCorrectToolForDrops();

    public static final RegistryObject<Block> RAW_LEAD_BLOCK = registerBlock("raw_lead_block",
            () -> new Block(METAL_RAW_BLOCK_PROPERTIES));

    public static final RegistryObject<Block> RAW_ALUMINUM_BLOCK = registerBlock("raw_aluminum_block",
            () -> new Block(METAL_RAW_BLOCK_PROPERTIES));

    public static final RegistryObject<Block> RAW_TIN_BLOCK = registerBlock("raw_tin_block",
            () -> new Block(METAL_RAW_BLOCK_PROPERTIES));

    public static final RegistryObject<Block> RAW_SILVER_BLOCK = registerBlock("raw_silver_block",
            () -> new Block(METAL_RAW_BLOCK_PROPERTIES));


    //杂项方块
    private static final BlockBehaviour.Properties METAL_OTHER_BLOCK_PROPERTIES =
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.METAL)
                    .strength(5.0F, 6.0F);

    public static final RegistryObject<Block> MACHINE_CASING = registerBlock("machine_casing",
            () -> new Block(METAL_OTHER_BLOCK_PROPERTIES));
    public static final RegistryObject<Block> BRONZE_MACHINE_CASING = registerBlock("bronze_machine_casing",
            () -> new Block(METAL_OTHER_BLOCK_PROPERTIES));


    //机器
    public static final RegistryObject<Block> SHREDDER = registerBlock("shredder"  ,() -> new Shredder(BlockBehaviour.Properties.of()));

    public static final RegistryObject<Block> ADVANCE_SHREDDER = registerBlock("advance_shredder"  ,() -> new AdvanceShredder(BlockBehaviour.Properties.of()));

    public static final RegistryObject<Block> LEVITATION_MACHINE = registerBlock("levitation_machine"  ,() -> new LevitationMachine(BlockBehaviour.Properties.of()));

    public static final RegistryObject<Block> VOID_ESSENCE_COLLECTOR = registerBlock("void_essence_collector", () -> new VoidEssenceCollector(BlockBehaviour.Properties.of()));

    public static final RegistryObject<Block> AIR_ESSENCE_COLLECTOR = registerBlock("air_essence_collector", () -> new AirEssenceCollector(BlockBehaviour.Properties.of()));

    public static final RegistryObject<Block> VOID_HOPPER = registerBlock("void_hopper", () -> new VoidHopper(BlockBehaviour.Properties.of()));

    public static final RegistryObject<Block> ESSENCE_FURNACE = registerBlock("essence_furnace", () -> new EssenceFurnace(BlockBehaviour.Properties.of()));

    public static final RegistryObject<Block> ESSENCE_CONVERSION_MACHINE = registerBlock("essence_conversion_machine", () -> new EssenceConversionMachine(BlockBehaviour.Properties.of()));

    public static RegistryObject<Block> registerSimpleBlock(String name, BlockBehaviour.Properties properties) {
        RegistryObject<Block> block = BLOCKS.register(name, () -> new Block(properties));
        DTItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    public static RegistryObject<Block> registerSimpleBlock(String name, BlockBehaviour.Properties properties, Item.Properties itemProperties) {
        RegistryObject<Block> block = BLOCKS.register(name, () -> new Block(properties));
        DTItems.ITEMS.register(name, () -> new BlockItem(block.get(), itemProperties));
        return block;
    }

    public static RegistryObject<Block> registerBlock(String name, Supplier<Block> blockSupplier) {
        RegistryObject<Block> block = BLOCKS.register(name, blockSupplier);
        DTItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }
}