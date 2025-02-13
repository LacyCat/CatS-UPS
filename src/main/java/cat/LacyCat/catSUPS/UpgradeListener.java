package cat.LacyCat.catSUPS;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class UpgradeListener implements Listener {

    private final CatSUPS plugin;
    private final UpgradeInventory upgradeInventory;
    private final NamespacedKey upgradeLevelKey;

    public UpgradeListener(CatSUPS plugin, UpgradeInventory upgradeInventory) {
        this.plugin = plugin;
        this.upgradeInventory = upgradeInventory;
        this.upgradeLevelKey = new NamespacedKey(plugin, "upgrade_level");
    }

    // 📌 GUI 클릭 이벤트 처리
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();

        // 강화 GUI 확인
        if (inventory == null || !event.getView().getTitle().equals("⚔ 아이템 강화")) return;

        event.setCancelled(true); // 기본 클릭 방지

        if (event.getSlot() == 23) { // 강화 버튼 클릭
            ItemStack weapon = inventory.getItem(14);
            if (weapon != null && weapon.getType() != Material.AIR) {
                upgradeWeapon(player, weapon);
            } else {
                player.sendMessage(ChatColor.RED + "⚠ 강화할 아이템을 넣어주세요!");
            }
        }
    }

    // 📌 강화 시스템 적용
    private void upgradeWeapon(Player player, ItemStack weapon) {
        ItemMeta meta = weapon.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer data = meta.getPersistentDataContainer();
        int upgradeLevel = data.getOrDefault(upgradeLevelKey, PersistentDataType.INTEGER, 0);

        // 강화 확률 계산
        double successRate = Math.pow(0.8, upgradeLevel);
        if (Math.random() > successRate) {
            player.sendMessage(ChatColor.RED + "❌ 강화에 실패했습니다...");
            return;
        }

        // 강화 성공 처리
        upgradeLevel++;
        data.set(upgradeLevelKey, PersistentDataType.INTEGER, upgradeLevel);

        // 📌 공격력 증가 (1.2배)
        double baseDamage = 7.0; // 네더라이트 검 기본 공격력 (임의 설정)
        double newDamage = baseDamage * Math.pow(1.2, upgradeLevel);

        // 📌 공격 쿨타임 감소 (0.8배)
        double baseAttackSpeed = 1.6; // 네더라이트 검 기본 공격 속도
        double newAttackSpeed = baseAttackSpeed / Math.pow(0.8, upgradeLevel);

        // 📌 새로운 AttributeModifier 추가 방식
        NamespacedKey damageKey = new NamespacedKey(plugin, "attack_damage");
        NamespacedKey speedKey = new NamespacedKey(plugin, "attack_speed");

        AttributeModifier damageModifier = new AttributeModifier(damageKey, newDamage - baseDamage, Operation.ADD_NUMBER);
        AttributeModifier speedModifier = new AttributeModifier(speedKey, newAttackSpeed - baseAttackSpeed, Operation.ADD_NUMBER);

        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier);
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, speedModifier);

        // 📌 강화 레벨 설명 추가
        meta.setDisplayName(ChatColor.GOLD + "+ " + upgradeLevel + "강 " + ChatColor.RESET + weapon.getType().name());
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "⚔ 공격력: " + ChatColor.RED + String.format("%.1f", newDamage),
                ChatColor.GRAY + "⚡ 공격 속도: " + ChatColor.AQUA + String.format("%.2f", newAttackSpeed),
                ChatColor.GRAY + "🔄 내구도 복구 완료!"
        ));

        // 📌 내구도 풀로 회복
        weapon.setDurability((short) 0);

        weapon.setItemMeta(meta);
        player.sendMessage(ChatColor.GREEN + "✅ 강화 성공! (+ " + upgradeLevel + "강)");

        // 강화 후 인벤토리 업데이트
        player.getOpenInventory().getTopInventory().setItem(14, weapon);
    }

    // 📌 인벤토리 닫기 이벤트 (강화 슬롯 비우기)
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals("⚔ 아이템 강화")) {
            Inventory inventory = event.getInventory();
            ItemStack weapon = inventory.getItem(14);
            if (weapon != null && weapon.getType() != Material.AIR) {
                event.getPlayer().getInventory().addItem(weapon);
            }
        }
    }
}
