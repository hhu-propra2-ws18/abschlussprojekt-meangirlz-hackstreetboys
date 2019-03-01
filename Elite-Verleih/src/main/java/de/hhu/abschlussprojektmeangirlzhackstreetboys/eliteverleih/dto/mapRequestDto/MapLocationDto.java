package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.mapRequestDto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MapLocationDto {
    @SerializedName("center")
    @Expose
    public List<Double> center = null;

    public double getX() {
        return center.get(1);
    }

    public double getY() {
        return center.get(0);
    }
}