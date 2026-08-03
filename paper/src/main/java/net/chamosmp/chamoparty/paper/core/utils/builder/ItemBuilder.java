package net.chamosmp.chamoparty.paper.core.utils.builder;

import net.chamosmp.chamoparty.core.utils.ColorUtils;
import net.chamosmp.chamoparty.paper.core.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBuilder extends Utils implements Cloneable {

    private ItemStack item;
    private final Material material;
    private ItemMeta meta;
    private final int data;
    private final int amount;
    private String name;
    private List<Component> lore;
    private List<ItemFlag> flags;
    private Map<Enchantment, Integer> enchantments;

    /**
     *
     * @param material
     * @param data
     * @param amount
     * @param name
     * @param lore
     * @param flags
     * @param enchantments
     */
    public ItemBuilder(Material material, int data, int amount, String name, List<String> lore, List<ItemFlag> flags,
                       Map<Enchantment, Integer> enchantments) {
        super();
        this.material = material;
        this.data = data;
        this.amount = amount;
        this.name = name;
        this.lore = ColorUtils.parse(lore);
        this.flags = flags;
        this.enchantments = enchantments;
    }

    /**
     *
     * @param material
     * @param name
     */
    public ItemBuilder(Material material, String name) {
        this(material, 0, 1, name, null, null, null);
    }


    /**
     * add enchant
     *
     * @param enchantment
     * @param value
     * @return
     */
    public ItemBuilder addEnchant(Enchantment enchantment, int value) {
        if (enchantments == null)
            enchantments = new HashMap<>();
        enchantments.put(enchantment, value);
        return this;
    }

    /**
     *
     * @param flag
     * @return
     */
    public ItemBuilder setFlag(ItemFlag flag) {
        if (flags == null)
            flags = new ArrayList<>();
        this.flags.add(flag);
        return this;
    }

    /**
     *
     * @param format
     * @param args
     * @return
     */
    public ItemBuilder addLine(String format) {
        if (lore == null)
            lore = new ArrayList<>();
        lore.add(ColorUtils.parse(format));
        return this;
    }

    /**
     *
     * @param name
     * @return
     */
    public ItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     *
     * @return
     */
    public ItemBuilder glow() {
        addEnchant(material != Material.BOW ? Enchantment.INFINITY : Enchantment.LUCK_OF_THE_SEA, 10);
        setFlag(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemStack build() {
        item = new ItemStack(material, amount);

        if (meta == null)
            meta = item.getItemMeta();

        Damageable damageable = (Damageable) meta;
        damageable.setDamage((short) amount);

        if (flags != null)
            flags.forEach(flag -> meta.addItemFlags(flag));

        if (name != null)
            meta.displayName(ColorUtils.parse(name));

        if (lore != null)
            meta.lore(lore);

        if (enchantments != null)
            enchantments.forEach((e, l) -> meta.addEnchant(e, l, true));

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Clone
     */
    public ItemBuilder clone() {
        try {
            return (ItemBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

}
