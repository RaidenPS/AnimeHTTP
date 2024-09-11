package raidenhttp.utils.structures;

// Imports
import com.google.gson.JsonElement;

public class StructureGetFPRequest {
    public String device_id;
    public String seed_id;
    public String seed_time;
    public String platform;
    public String device_fp;
    public String app_name;
    public JsonElement ext_fields;
}