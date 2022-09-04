package io.github.lordanaku.mana_bar_demo_forge.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.lordanaku.anaku_status_bars.api.RenderHudFunctions;
import io.github.lordanaku.anaku_status_bars.screen.hud.RenderHudHelper;
import io.github.lordanaku.anaku_status_bars.utils.Settings;
import io.github.lordanaku.anaku_status_bars.utils.TextureRecords;
import io.github.lordanaku.anaku_status_bars.utils.interfaces.IHudElement;
import io.github.lordanaku.anaku_status_bars.utils.records.HudElementType;
import io.github.lordanaku.mana_bar_demo_forge.ManaBarDemoCore;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.ColorEntry;
import me.shedaniel.clothconfig2.gui.entries.FloatListEntry;
import net.minecraft.network.chat.Component;


public class ManaBarElement implements IHudElement {
    public static final HudElementType MANA = new HudElementType("mana", true, true, true, false,0x007FFF, 1);
    private boolean renderSide = MANA.shouldRender();

    @Override
    public void renderBar(PoseStack poseStack) {
        RenderHudFunctions.drawDefaultBar(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.DEFAULT_BAR);
        RenderHudFunctions.drawProgressBar(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.PROGRESS_BAR, getProgress(),
                Settings.colorSettings.get(MANA.name()), Settings.alphaSettings.get(MANA.name()));
    }

    @Override
    public void renderIcon(PoseStack poseStack) {
        RenderHudFunctions.drawIcon(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), ManaBarDemoCore.MANA_ICON, 81);
    }

    @Override
    public void renderText(PoseStack poseStack) {
        RenderHudFunctions.drawText(poseStack, "test", getSide(), shouldRenderIcon(), RenderHudHelper.getPosYMod(getSide()), Settings.textColorSettings.get(MANA.name()), 81);
    }

    @Override
    public boolean getSide() {
        return renderSide;
    }

    @Override
    public IHudElement setRenderSide(boolean side) {
        this.renderSide = side;
        return this;
    }

    @Override
    public boolean shouldRender() {
        return Settings.shouldRenderSettings.get(MANA.name());
    }

    @Override
    public boolean shouldRenderIcon() {
        return shouldRender() && Settings.shouldRenderIconSettings.get(MANA.name());
    }

    @Override
    public boolean shouldRenderText() {
        return shouldRender() && Settings.shouldRenderTextSettings.get(MANA.name());
    }

    @Override
    public void registerSettings(ConfigCategory mainCategory, ConfigCategory iconCategory, ConfigCategory textCategory, ConfigCategory colorCategory, ConfigCategory textColorSettings, ConfigCategory alphaCategory, ConfigEntryBuilder builder) {
        BooleanListEntry shouldRender = builder.startBooleanToggle(Component.translatable("option.mana_bar_demo_forge.should_render_mana"), Settings.shouldRenderSettings.get(MANA.name()))
                .setDefaultValue(MANA.shouldRender())
                .setSaveConsumer(value -> Settings.shouldRenderSettings.replace(MANA.name(), value))
                .build();
        mainCategory.addEntry(shouldRender);

        BooleanListEntry shouldRenderIcon = builder.startBooleanToggle(Component.translatable("option.mana_bar_demo_forge.should_render_mana_icon"), Settings.shouldRenderIconSettings.get(MANA.name()))
                .setDefaultValue(MANA.shouldRenderIcon())
                .setSaveConsumer(value -> Settings.shouldRenderIconSettings.replace(MANA.name(), value))
                .build();
        iconCategory.addEntry(shouldRenderIcon);

        BooleanListEntry shouldRenderText = builder.startBooleanToggle(Component.translatable("option.mana_bar_demo_forge.should_render_mana_text"), Settings.shouldRenderTextSettings.get(MANA.name()))
                .setDefaultValue(MANA.shouldRenderText())
                .setSaveConsumer(value -> Settings.shouldRenderTextSettings.replace(MANA.name(), value))
                .build();
        textCategory.addEntry(shouldRenderText);

        ColorEntry colorMana = builder.startColorField(Component.translatable("option.mana_bar_demo_forge.color_mana"), Settings.colorSettings.get(MANA.name()))
                .setDefaultValue(MANA.color())
                .setSaveConsumer(value -> Settings.colorSettings.replace(MANA.name(), value))
                .build();
        colorCategory.addEntry(colorMana);

        ColorEntry textColorMana = builder.startColorField(Component.translatable("option.mana_bar_demo_forge.text_color_mana"), Settings.textColorSettings.get(MANA.name()))
                .setDefaultValue(MANA.color())
                .setSaveConsumer(value -> Settings.textColorSettings.replace(MANA.name(), value))
                .build();
        textColorSettings.addEntry(textColorMana);

        FloatListEntry alphaMana = builder.startFloatField(Component.translatable("option.mana_bar_demo_forge.alpha_mana"), Settings.alphaSettings.get(MANA.name()))
                .setDefaultValue(MANA.alpha())
                .setMin(0.0f)
                .setMax(1.0f)
                .setSaveConsumer(newValue -> Settings.alphaSettings.replace(MANA.name(), newValue))
                .build();
        alphaCategory.addEntry(alphaMana);
    }

    @Override
    public String name() {
        return MANA.name();
    }

    private int getProgress() {
        float mana = 10f;
        float maxMana = 20f;
        float ratio = Math.min(1, Math.max(0, mana / maxMana));
        int maxProgress = 81;
        return (int) Math.min(maxProgress, Math.ceil(ratio * maxProgress));
    }
}
