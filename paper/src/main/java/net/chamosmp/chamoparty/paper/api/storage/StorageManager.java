package net.chamosmp.chamoparty.paper.api.storage;

import net.chamosmp.chamoparty.api.storage.Storage;
import net.chamosmp.chamoparty.paper.core.utils.storage.Saveable;

public interface StorageManager extends Saveable {

    Storage getStorage();

    IStorage getIStorage();

}
