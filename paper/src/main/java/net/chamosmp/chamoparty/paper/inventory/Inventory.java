package net.chamosmp.chamoparty.paper.inventory;

import net.chamosmp.chamoparty.core.utils.ColorUtils;
import net.chamosmp.chamoparty.core.utils.inventory.InventoryResult;
import net.chamosmp.chamoparty.exceptions.InventoryOpenException;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.core.utils.Utils;
import net.chamosmp.chamoparty.paper.core.utils.builder.ItemBuilder;
import net.chamosmp.chamoparty.paper.core.utils.inventory.ItemButton;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class Inventory extends Utils implements Cloneable {

    protected int id;
    protected ChamoPartyPlugin plugin;
    protected Map<Integer, ItemButton> items = new HashMap<Integer, ItemButton>();
    protected Player player;
    protected int page;
    protected Object[] args;
    protected org.bukkit.inventory.Inventory inventory;
    protected String guiName;
    protected Component title;
    protected boolean openAsync = false;

    /**
     * Id de l'inventaire
     *
     * @param id
     * @return
     */
    public Inventory setId(int id) {
        this.id = id;
        return this;
    }

    public int getId() {
        return id;
    }

    /**
     * Permet de cr§er l'inventaire
     *
     * @param name
     * @param size
     * @return this
     */
    protected Inventory createInventory(String name, int size) {
        this.guiName = name;
        this.title = ColorUtils.parse(name);
        this.inventory = Bukkit.createInventory(null, size, title);
        return this;
    }

    /**
     * Create default inventory with default size and name
     */
    private void createDefaultInventory() {
        if (this.inventory == null)
            this.inventory = Bukkit.createInventory(null, 54, ColorUtils.parse("§cDefault Inventory"));
    }

    /**
     * Ajout d'un item
     *
     * @param slot
     * @param item
     * @return
     */
    public ItemButton addItem(int slot, Material material, String name) {
        return addItem(slot, new ItemBuilder(material, name).build());
    }

    /**
     *
     * @param slot
     * @param item
     * @return
     */
    public ItemButton addItem(int slot, ItemStack item) {
        // Pour §viter les erreurs, on cr§e un inventaire
        createDefaultInventory();

        ItemButton button = new ItemButton(slot);
        this.items.put(slot, button);

        if (this.openAsync)
            runAsync(plugin, () -> this.inventory.setItem(slot, item));
        else
            this.inventory.setItem(slot, item);

        return button;
    }

    /**
     * Permet de r§cup§rer tous les items
     *
     * @return
     */
    public Map<Integer, ItemButton> getItems() {
        return items;
    }

    /**
     * Permet de r§cup§rer le joueur
     *
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Permet de r§cup§rer la page
     *
     * @return the page
     */
    public int getPage() {
        return page;
    }

    /**
     * @return the inventory
     */
    public org.bukkit.inventory.Inventory getInventory() {
        return inventory;
    }

    public Component getGuiComponent() {
        return title;
    }

    protected InventoryResult preOpenInventory(ChamoPartyPlugin main, Player player, int page, Object... args)
            throws InventoryOpenException {

        this.page = page;
        this.args = args;
        this.player = player;
        this.plugin = main;

        return openInventory(main, player, page, args);
    }

    public abstract InventoryResult openInventory(ChamoPartyPlugin main, Player player, int page, Object... args)
            throws InventoryOpenException;

    /**
     *
     * @param event
     * @param plugin
     * @param player
     */
    protected void onClose(InventoryCloseEvent event, ChamoPartyPlugin plugin, Player player) {
    }

    /**
     *
     * @param event
     * @param plugin
     * @param player
     */
    protected void onDrag(InventoryDragEvent event, ChamoPartyPlugin plugin, Player player) {
    }

    @Override
    protected Inventory clone() {
        try {
            return getClass().getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
