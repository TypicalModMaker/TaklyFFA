package dev.isnow.ffa.utils.type;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public class LimitType {

    Material material;
    short data;

    public LimitType(Material material, short data) {
        this.material = material;
        this.data = data;
    }
}
