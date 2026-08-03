package net.chamosmp.chamoparty.paper.core.utils.inventory;

import net.chamosmp.chamoparty.core.utils.inventory.InventoryResult;
import net.chamosmp.chamoparty.core.utils.inventory.Pagination;
import net.chamosmp.chamoparty.exceptions.InventoryOpenException;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.inventory.Inventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class PaginateInventory<T> extends Inventory {

    protected List<T> collections;
    protected final String inventoryName;
    protected final int inventorySize;
    protected int paginationSize = 45;
    protected int nextSlot = 50;
    protected int previousSlot = 48;
    protected int defaultSlot = 0;
    protected boolean isReverse = false;

    /**
     *
     * @param inventoryName
     * @param inventorySize
     */
    public PaginateInventory(String inventoryName, int inventorySize) {
        super();
        this.inventoryName = inventoryName;
        this.inventorySize = inventorySize;
    }

    @Override
    public InventoryResult openInventory(ChamoPartyPlugin main, Player player, int page, Object... args)
            throws InventoryOpenException {

        if (defaultSlot > inventorySize || nextSlot > inventorySize || previousSlot > inventorySize
                || paginationSize > inventorySize)
            throw new InventoryOpenException("An error occurred with slot management!");

        collections = preOpenInventory();

        if (collections == null)
            throw new InventoryOpenException("Collection is null");

        super.createInventory(inventoryName.replace("%mp%", String.valueOf(getMaxPage(collections))).replace("%p%",
                String.valueOf(page)), inventorySize);

        Pagination<T> pagination = new Pagination<>();
        AtomicInteger slot = new AtomicInteger(defaultSlot);

        List<T> tmpList = isReverse ? pagination.paginateReverse(collections, paginationSize, page)
                : pagination.paginate(collections, paginationSize, page);

        tmpList.forEach(tmpItem -> {
            ItemButton button = addItem(slot.getAndIncrement(), buildItem(tmpItem));
            button.setClick((event) -> onClick(tmpItem, button));
        });

        if (getPage() != 1)
            addItem(previousSlot, Material.ARROW, "§f§ §7Previous page")
                    .setClick(event -> {
                        try {
                            createInventory(this.plugin, player, getId(), getPage() - 1, args);
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException(e);
                        }
                    });
        if (getPage() != getMaxPage(collections))
            addItem(nextSlot, Material.ARROW, "§f§ §7Next Page")
                    .setClick(event -> {
                        try {
                            createInventory(this.plugin, player, getId(), getPage() + 1, args);
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException(e);
                        }
                    });

        postOpenInventory();

        return InventoryResult.SUCCESS;
    }

    /**
     *
     * @param object
     * @return
     */
    public abstract ItemStack buildItem(T object);

    /**
     *
     * @param object
     * @param button
     */
    public abstract void onClick(T object, ItemButton button);

    /**
     *
     * @return
     */
    public abstract List<T> preOpenInventory();

    /**
     * Called after create inventory
     */
    public abstract void postOpenInventory();
}
