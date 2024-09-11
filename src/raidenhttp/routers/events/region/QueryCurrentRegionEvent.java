package raidenhttp.routers.events.region;

// Imports
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public final class QueryCurrentRegionEvent extends raidenhttp.routers.events.Event {
    private String regionInfo;

    public QueryCurrentRegionEvent(String regionInfo) {
        this.regionInfo = regionInfo;
    }
}
