package org.quiltmc.users.duckteam.ducktech.config;

import org.quiltmc.users.duckteam.ducktech.DuckTech;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DTConfig {
    public static boolean SWITCH_SOUND;

    // 配置文件路径
    private static final Path CONFIG_PATH = Paths.get("config", "ducktech-config.toml");
    private static boolean initialized = false;

    public static void init() {
        if (initialized) {
            return; // 避免重复初始化
        }
        // 静态初始化块 - 在类加载时执行
        boolean switchSound = true;

        try {
            // 确保配置目录存在
            Files.createDirectories(CONFIG_PATH.getParent());

            // 如果配置文件不存在，创建默认配置
            if (!Files.exists(CONFIG_PATH)) {
                createDefaultConfig();
            }

            // 读取并解析 TOML 文件
            String configContent = Files.readString(CONFIG_PATH);
            TomlParseResult result = Toml.parse(configContent);

            if (result.hasErrors()) {
                DuckTech.LOGGER.error("TOML 配置解析错误:");
                result.errors().forEach(error ->
                        DuckTech.LOGGER.error(error.toString())
                );
            } else {
                if (result.getBoolean("switch_sound") == null) {
                    switchSound = result.getBoolean("switch_sound");
                } else {
                    switchSound = true;
                }
            }

        } catch (IOException e) {
            DuckTech.LOGGER.error("无法读取配置文件: " + e.getMessage());
            createDefaultConfig(); // 尝试创建默认配置
        } catch (Exception e) {
            DuckTech.LOGGER.error("配置初始化错误: " + e.getMessage());
        }

        SWITCH_SOUND = switchSound;
    }

    /**
     * 创建默认配置文件
     */
    private static void createDefaultConfig() {
        try {
            String defaultConfig = """
                switch_sound = true
                """;

            Files.writeString(CONFIG_PATH, defaultConfig);
            DuckTech.LOGGER.info("已创建默认配置文件: " + CONFIG_PATH);

        } catch (IOException e) {
            DuckTech.LOGGER.info("无法创建配置文件: " + e.getMessage());
        }
    }
}
