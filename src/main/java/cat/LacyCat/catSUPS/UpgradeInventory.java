package cat.LacyCat.catSUPS;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UpgradeInventory {

    private final CatSUPS plugin;

    public UpgradeInventory(CatSUPS plugin) {
        this.plugin = plugin;
    }

    // 📌 강화 GUI 생성
    public static Inventory createUpgradeGUI() {
        Inventory gui = Bukkit.createInventory(null, 27, "⚔ 아이템 강화");

        // 흰색 스테인드 글라스 배경
        ItemStack filler = createItem(Material.WHITE_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < 27; i++) {
            gui.setItem(i, filler);
        }

        // 강화 슬롯 (14번 칸) → 빈 칸
        gui.setItem(14, null);

        // 강화 버튼 (23번 칸) → 초록색 양털
        gui.setItem(23, createItem(Material.LIME_WOOL, ChatColor.GREEN + "✅ 아이템 강화"));

        return gui;
    }

    // 📌 GUI 아이템 생성 함수
    private static ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    // 📌 플레이어에게 강화 GUI 열기
    public void openUpgradeGUI(Player player) {
        player.openInventory(createUpgradeGUI());
    }
}
