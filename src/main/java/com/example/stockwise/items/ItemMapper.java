package com.example.stockwise.items;

import com.example.stockwise.items.item.Item;
import com.example.stockwise.items.itemPending.PendingItem;
import com.example.stockwise.rack.Rack;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface ItemMapper {

    @Mapping(source = "rackId", target = "rack", qualifiedByName = "rackIdToRack")
    Item toItem(PendingItem pendingItem);

    @Named("rackIdToRack")
    default Rack roleToString(Long rackId) {
        Rack rack = new Rack();
        rack.setId(rackId);
        return rack;
    }

}
