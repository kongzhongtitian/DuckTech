package org.quiltmc.users.duckteam.ducktech;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quiltmc.users.duckteam.ducktech.blocks.*;
import org.quiltmc.users.duckteam.ducktech.gui.DTMenu;
import org.quiltmc.users.duckteam.ducktech.items.*;
import org.quiltmc.users.duckteam.ducktech.recipe.*;
import org.quiltmc.users.duckteam.ducktech.sounds.DTSounds;

@Mod(DuckTech.MOD_ID)
public class DuckTech {
    public static final String MODID = "ducktech";
    public static final String MOD_ID = MODID;

    private static final Logger LOGGER = LogManager.getLogger();

    public DuckTech(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        LOGGER.info("|----l  |    |  /----  |  / --------- |----  /----  |");
        LOGGER.info("|     | |    | |     l | /      |     |     |     l |");
        LOGGER.info("|     | |    | |       |/       |     |     |       |");
        LOGGER.info("|     | |    | |       |        |     |---- |       |----、");
        LOGGER.info("|     | |    | |       |l       |     |     |       |    |");
        LOGGER.info("|     | |    | |     / | l      |     |     |     / |    |");
        LOGGER.info("|----/  l----|  l----  |  l     |     |----  l----  |    |");
        LOGGER.info("DuckTech Version:1.-1.x");
        DTCreativeTab.CREATIVE_TABS.register(modEventBus);
        DTSounds.SOUND_EVENTS.register(modEventBus);
        DTBlocks.BLOCKS.register(modEventBus);
        DTItems.ITEMS.register(modEventBus);
        DTBlockEntity.BLOCK_ENTITY_TYPES.register(modEventBus);
        DTRecipe.RECIPE_TYPES.register(modEventBus);
        DTRecipeSerializers.SERIALIZERS.register(modEventBus);
        DTMenu.MENUS.register(modEventBus);
        LOGGER.info("DuckTech Has Loaded");
    }
}
