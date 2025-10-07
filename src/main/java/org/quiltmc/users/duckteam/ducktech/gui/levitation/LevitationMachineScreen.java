package org.quiltmc.users.duckteam.ducktech.gui.levitation;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.quiltmc.users.duckteam.ducktech.DuckTech;

public class LevitationMachineScreen extends AbstractContainerScreen<LevitationMachineMenu> {
    public static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(DuckTech.MODID,
            "textures/screen/levitation.png");

    public static final ResourceLocation SLOT = ResourceLocation.fromNamespaceAndPath(DuckTech.MODID,
            "textures/gui/slot.png");


    public LevitationMachineScreen(LevitationMachineMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);


    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float p_97788_, int p_97789_, int p_97790_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, 256, 256);

        if (this.getMenu().getTime() > 0){
            guiGraphics.drawString(this.font, Component.literal("time:").append((int)(Math.ceil((double)(30 - this.getMenu().getTime() / 20))) +"s"), x + 75, y + 60, 0x404040 , false);

        }
    }
}
