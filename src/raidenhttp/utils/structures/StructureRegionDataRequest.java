package raidenhttp.utils.structures;

// Protocol buffers
import lombok.Getter;
import raidenhttp.cache.protos.QueryCurrRegionHttpRspOuterClass.QueryCurrRegionHttpRsp;

@Getter
public class StructureRegionDataRequest {
    private final QueryCurrRegionHttpRsp regionQuery;
    private final String base64;

    public StructureRegionDataRequest(QueryCurrRegionHttpRsp prq, String b64) {
        this.regionQuery = prq;
        this.base64 = b64;
    }
}
