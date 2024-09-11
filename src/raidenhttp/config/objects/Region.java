package raidenhttp.config.objects;

// Imports
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Region {
    public String Name;
    public String Title;
    public String Type;
    public String Ip;
    public int Port;

    public Region(String name, String title, String type, String address, int port) {
        this.Name = name;
        this.Title = title;
        this.Type = type;
        this.Ip = address;
        this.Port  = port;
    }
}