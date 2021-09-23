package net.vadamdev.kbeditor;

import net.vadamdev.kbeditor.utils.FileUtils;
import net.vadamdev.kbeditor.utils.KBInfo;
import net.vadamdev.viaapi.VIPlugin;
import net.vadamdev.viaapi.startup.APIVersion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Main extends VIPlugin {
    public static Main instance;

    private KBInfo kbInfo;

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        for(FileUtils value : FileUtils.values()) saveResource(value.getFilename(), value.getFilename());

        setupKBInfo();

        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    private void setupKBInfo() {
        FileConfiguration config = FileUtils.CONFIG.getConfig();

        ConfigurationSection melee = config.getConfigurationSection("melee");
        ConfigurationSection speed = config.getConfigurationSection("speed");

        kbInfo = new KBInfo((float) melee.getDouble("horizontal-multiplier"), (float) melee.getDouble("vertical-multiplier"), (float) speed.getDouble("horizontal-multiplier"), (float) speed.getDouble("vertical-multiplier"));
    }

    public KBInfo getKbInfo() {
        return kbInfo;
    }

    @Override
    public APIVersion getAPIVersion() {
        return APIVersion.V2_4_2;
    }
}
