package com.example.stockwise.items;

import com.example.stockwise.items.history.HistoryItem;
import com.example.stockwise.items.item.Item;
import com.example.stockwise.items.itemPending.PendingItem;
import com.example.stockwise.rack.Rack;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(source = "rackId", target = "rack", qualifiedByName = "rackIdToRack")
    Item toItem(PendingItem pendingItem);

    @Named("rackIdToRack")
    default Rack roleToString(Long rackId) {
        return new Rack(rackId, null, null, null);
    }

    @Mapping(source = "id", target = "username", qualifiedByName = "nullUsername")
    HistoryItem toHistoryItem(Item item);

    @Named("nullUsername")
    default String nullUsername(Long id) {
        return "";
    }


}