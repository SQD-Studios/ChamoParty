package net.chamosmp.chamoparty.paper.inventory.inventories;

import net.chamosmp.chamoparty.paper.api.enums.Options;
import net.chamosmp.chamoparty.paper.core.utils.builder.ItemBuilder;
import net.chamosmp.chamoparty.paper.core.utils.inventory.ItemButton;
import net.chamosmp.chamoparty.paper.core.utils.inventory.PaginateInventory;
import net.chamosmp.chamoparty.paper.inventory.Inventory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class InventoryConfig extends PaginateInventory<Options> {

    public InventoryConfig() {
        super("§8ChamoParty - Config %p%/%mp%", 54);
    }

    @Override
    public ItemStack buildItem(Options object) {

        boolean isToggle = object.isToggle();
        ItemBuilder builder = new ItemBuilder(isToggle ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK,
                "§f" + object.getName());
        builder.addLine("§f§oStatus: §f" + yesNo(isToggle));
        builder.addLine("§f§oDescription:");
        object.getDescriptions().forEach(desc -> builder.addLine(" §7" + desc));
        if (isToggle)
            builder.glow();

        return builder.build();
    }

    private String yesNo(boolean b) {
        return b ? "§aEnable" : "§cDisabled";
    }

    @Override
    public void onClick(Options object, ItemButton button) {

        object.toggle(this.plugin);

        int slot = button.getSlot();
        boolean isToggle = object.isToggle();

        ItemBuilder builder = new ItemBuilder(isToggle ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK,
                "§f" + object.getName());
        builder.addLine("§f§oStatus: §f" + yesNo(isToggle));
        builder.addLine("§f§oDescription:");
        object.getDescriptions().forEach(desc -> builder.addLine(" §7" + desc));
        if (isToggle)
            builder.glow();

        this.inventory.setItem(slot, builder.build());
    }

    @Override
    public List<Options> preOpenInventory() {
        return Arrays.asList(Options.values());
    }

    @Override
    public void postOpenInventory() {
        // TODO Auto-generated method stub

    }

    @Override
    public Inventory clone() {
        return new InventoryConfig();
    }

}
