package com.example.addon.modules;

import com.example.addon.utils.webhook.WebhookHandler;
import com.example.addon.utils.webhook.WebhookContent;
import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.orbit.EventHandler;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public class DiscordSRV extends Module {
    public static final DiscordSRV INSTANCE = new DiscordSRV();

    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
    private final SettingGroup sgFilter = this.settings.createGroup("Filter");

    // General
    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .defaultValue(Mode.Webhook)
        .onChanged(o -> changeMode())
        .build()
    );

    private final Setting<Boolean> deactivateOnLeave = sgGeneral.add(new BoolSetting.Builder()
        .name("deactivate-on-leave")
        .defaultValue(false)
        .build()
    );

    // Bot
    private final Setting<String> botToken = sgGeneral.add(new StringSetting.Builder()
        .name("bot-token")
        .defaultValue("")
        .visible(() -> mode.get() == Mode.Bot)
        .onChanged(o -> requireRebuild = true)
        .build()
    );

    private final Setting<String> targetChannelId = sgGeneral.add(new StringSetting.Builder()
        .name("target-channel-id")
        .defaultValue("")
        .visible(() -> mode.get() == Mode.Bot)
        .onChanged(o -> channel = null)
        .build()
    );

    // Webhook
    private final Setting<String> webhookUrl = sgGeneral.add(new StringSetting.Builder()
        .name("webhook-url")
        .defaultValue("")
        .visible(() -> mode.get() == Mode.Webhook)
        .onChanged(o -> requireRebuild = true)
        .build()
    );
