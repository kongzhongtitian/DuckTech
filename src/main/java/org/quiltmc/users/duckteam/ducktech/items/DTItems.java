package org.quiltmc.users.duckteam.ducktech.items;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import org.quiltmc.users.duckteam.ducktech.DuckTech;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DTItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DuckTech.MODID);

    //粉
    public static final RegistryObject<Item> IRON_DUST = registerSimpleItem("iron_dust");
    public static final RegistryObject<Item> GOLDEN_DUST = registerSimpleItem("golden_dust");
    public static final RegistryObject<Item> COPPER_DUST = registerSimpleItem("copper_dust");
    public static final RegistryObject<Item> ALUMINUM_DUST = registerSimpleItem("aluminum_dust");
    public static final RegistryObject<Item> LEAD_DUST = registerSimpleItem("lead_dust");
    public static final RegistryObject<Item> TIN_DUST = registerSimpleItem("tin_dust");
    public static final RegistryObject<Item> SILVER_DUST = registerSimpleItem("silver_dust");
    public static final RegistryObject<Item> BRONZE_DUST = registerSimpleItem("bronze_dust");
    public static final RegistryObject<Item> DIAMOND_DUST = registerSimpleItem("diamond_dust");

    //精华
    public static final RegistryObject<Item> CRUSHED_ESSENCE = registerSimpleItem("crushed_essence");
    public static final RegistryObject<Item> ADVANCE_CRUSHED_ESSENCE = registerSimpleItem("advance_crushed_essence");
    public static final RegistryObject<Item> AIR_ESSENCE = registerSimpleItem("air_essence");
    public static final RegistryObject<Item> BASIC_ESSENCE = registerSimpleItem("basic_essence");
    public static final RegistryObject<Item> VOID_ESSENCE = registerSimpleItem("void_essence");
    public static final RegistryObject<Item> THERMAL_ESSENCE = registerSimpleItem("thermal_essence");
    public static final RegistryObject<Item> FROZEN_ESSENCE = registerSimpleItem("frozen_essence");
    public static final RegistryObject<Item> DUCK_ESSENCE = registerSimpleItem("duck_essence");

    //齿轮
    public static final RegistryObject<Item> IRON_GEAR = registerSimpleItem("iron_gear");
    public static final RegistryObject<Item> GOLDEN_GEAR = registerSimpleItem("golden_gear");
    public static final RegistryObject<Item> GOLD_PLATED_GEAR = registerSimpleItem("gold_plated_gear");
    public static final RegistryObject<Item> TIN_GEAR = registerSimpleItem("tin_gear");

    //锭
    public static final RegistryObject<Item> LEAD_INGOT = registerSimpleItem("lead_ingot");
    public static final RegistryObject<Item> ALUMINUM_INGOT = registerSimpleItem("aluminum_ingot");
    public static final RegistryObject<Item> TIN_INGOT = registerSimpleItem("tin_ingot");
    public static final RegistryObject<Item> SILVER_INGOT = registerSimpleItem("silver_ingot");
    public static final RegistryObject<Item> BRONZE_INGOT = registerSimpleItem("bronze_ingot");

    //nugget
    public static final RegistryObject<Item> LEAD_NUGGET = registerSimpleItem("lead_nugget");
    public static final RegistryObject<Item> ALUMINUM_NUGGET = registerSimpleItem("aluminum_nugget");
    public static final RegistryObject<Item> TIN_NUGGET = registerSimpleItem("tin_nugget");
    public static final RegistryObject<Item> SILVER_NUGGET = registerSimpleItem("silver_nugget");
    public static final RegistryObject<Item> BRONZE_NUGGET = registerSimpleItem("bronze_nugget");

    //粗
    public static final RegistryObject<Item> RAW_LEAD = registerSimpleItem("raw_lead");
    public static final RegistryObject<Item> RAW_ALUMINUM = registerSimpleItem("raw_aluminum");
    public static final RegistryObject<Item> RAW_TIN = registerSimpleItem("raw_tin");
    public static final RegistryObject<Item> RAW_SILVER = registerSimpleItem("raw_silver");

    //板
    public static final RegistryObject<Item> IRON_PLATE = registerSimpleItem("iron_plate");
    public static final RegistryObject<Item> ALUMINUM_PLATE = registerSimpleItem("aluminum_plate");
    public static final RegistryObject<Item> TIN_PLATE = registerSimpleItem("tin_plate");
    public static final RegistryObject<Item> BRONZE_PLATE = registerSimpleItem("bronze_plate");

    //外壳
    public static final RegistryObject<Item> IRON_HULL = registerSimpleItem("iron_hull");
    public static final RegistryObject<Item> ALUMINUM_HULL = registerSimpleItem("aluminum_hull");
    public static final RegistryObject<Item> TIN_HULL = registerSimpleItem("tin_hull");
    public static final RegistryObject<Item> BRONZE_HULL = registerSimpleItem("bronze_hull");

    //other
    public static final RegistryObject<Item> RUBBER_DUCK = registerSimpleItem("rubber_duck");
    public static final RegistryObject<Item> RUBBER = registerSimpleItem("rubber");
    public static final RegistryObject<Item> YELLOW_RUBBER = registerSimpleItem("yellow_rubber");
    public static final RegistryObject<Item> DUCKTECH = registerSimpleItem("ducktech");

    //耐久
    public static final RegistryObject<Item> FORGE_HAMMER = ITEMS.register("forge_hammer",
            () ->
                    new Item(new Item.Properties().stacksTo(1).durability(64)));
    public static final RegistryObject<Item> SACRIFICIAL_KNIFE = ITEMS.register("sacrificial_knife",
            () ->
                    new Item(new Item.Properties().stacksTo(1).durability(64)));


    public static RegistryObject<Item> registerSimpleItem(String itemName){
        return ITEMS.register(itemName , ()-> new Item(new Item.Properties()));
    }
}
