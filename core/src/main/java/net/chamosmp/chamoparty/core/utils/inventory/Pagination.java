package net.chamosmp.chamoparty.core.utils.inventory;

import java.util.ArrayList;
import java.util.List;

public class Pagination<T> {

    public List<T> paginateReverse(List<T> list, int inventorySize, int page) {
        List<T> currentList = new ArrayList<>();
        if (page == 0)
            page = 1;
        int idStart = list.size() - 1 - ((page - 1) * inventorySize);
        int idEnd = idStart - inventorySize;
        if (idEnd < list.size() - inventorySize && list.size() < inventorySize * page)
            idEnd = -1;
        for (int a = idStart; a != idEnd; a--)
            currentList.add(list.get(a));
        return currentList;
    }

    public List<T> paginate(List<T> list, int inventorySize, int page) {
        List<T> currentList = new ArrayList<>();
        if (page == 0)
            page = 1;
        int idStart = ((page - 1)) * inventorySize;
        int idEnd = idStart + inventorySize;
        if (idEnd > list.size())
            idEnd = list.size();
        for (int a = idStart; a != idEnd; a++)
            currentList.add(list.get(a));
        return currentList;
    }

}