package cat.LacyCat.catSUPS;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

public class CatSUPS extends JavaPlugin {
    private UpgradeInventory upgradeInventory;

    @Override
    public void onEnable() {
        upgradeInventory = new UpgradeInventory(this);

        // 이벤트 등록
        Bukkit.getPluginManager().registerEvents(new UpgradeListener(this, upgradeInventory), this);

        // 명령어 등록
        getCommand("upgrade").setExecutor(new UpgradeCommand());
    }

    @Override
    public void onDisable() {
        // 플러그인 종료 시 수행할 작업
    }
}