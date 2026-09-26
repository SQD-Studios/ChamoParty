package net.chamosmp.chamoparty.paper.api.storage;

import net.chamosmp.chamoparty.api.storage.Storage;

public interface StorageManager extends Saveable {

    Storage getStorage();

    IStorage getIStorage();

}
