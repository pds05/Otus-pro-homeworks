package ru.otus.java.pro.homeworks.pattern2.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemsService  implements AppService<Item> {
    private AppDao<Item> dao;

    public ItemsService(AppDao<Item> dao) {
        this.dao = dao;
    }

    @Override
    public List<Item> createItems(int total) {
        List<Item> result = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            Item item = new Item(null, "item" + i, BigDecimal.valueOf(i % 2 == 0 ? 10 * i : 100 * i));
            item = dao.create(item);
            result.add(item);
        }
        System.out.println("Created: " + result);
        return result;
    }

    @Override
    public void increasePrice(List<Long> itemIds, int order) {
        for (Long itemId : itemIds) {
            Item item = dao.find(itemId);
            item.setPrice(item.getPrice().multiply(new BigDecimal(order)));
            dao.update(item);
        }
    }

    @Override
    public AppDao<Item> getDao() {
        return dao;
    }
}
