package raidenhttp.utils.structures;

public class StructureGrandByTicketRequest {
    public static class Device {
        public String device_id;
        public String device_name;
        public String client;
        public String device_model;
    }

    public String action_ticket;
    public Device device = new Device();
    public String way;
}