package dev.isnow.ffa.utils.type;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public class KillType {
    Material material;
    short data;
    int amount;

    public KillType(Material material, short data, int amount) {
        this.material = material;
        this.data = data;
        this.amount = amount;
    }
}
