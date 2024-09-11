package raidenhttp.utils.structures;

// Imports
import lombok.Builder;

@Builder
public class StructureLoginTokenRequest {
    public String uid;
    public String token;
}
