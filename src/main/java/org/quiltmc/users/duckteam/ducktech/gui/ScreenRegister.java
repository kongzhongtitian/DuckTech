package org.quiltmc.users.duckteam.ducktech.gui;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.quiltmc.users.duckteam.ducktech.DuckTech;
import org.quiltmc.users.duckteam.ducktech.gui.advance_shredder.AdvanceShredderScreen;
import org.quiltmc.users.duckteam.ducktech.gui.essence_conversion_machine.EssenceConversionMachineScreen;
import org.quiltmc.users.duckteam.ducktech.gui.essence_furnace.EssenceFurnaceScreen;
import org.quiltmc.users.duckteam.ducktech.gui.levitation.LevitationMachineScreen;

@Mod.EventBusSubscriber(modid = DuckTech.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ScreenRegister {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(DTMenu.ADVANCE_SHREDDER_MENU.get(), AdvanceShredderScreen::new);
            MenuScreens.register(DTMenu.LEVITATION_MACHINE_MENU.get(), LevitationMachineScreen::new);
            MenuScreens.register(DTMenu.ESSENCE_FURNACE_MENU.get(), EssenceFurnaceScreen::new);
            MenuScreens.register(DTMenu.ESSENCE_CONVERSION_MACHINE_MENU.get(), EssenceConversionMachineScreen::new);
        });
    }
}
