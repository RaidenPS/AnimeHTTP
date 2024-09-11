package raidenhttp.routers.events.region;

// Imports
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public final class QueryAllRegionsEvent extends raidenhttp.routers.events.Event {
    private String regionList;

    public QueryAllRegionsEvent(String regionList) {
        this.regionList = regionList;
    }
}
