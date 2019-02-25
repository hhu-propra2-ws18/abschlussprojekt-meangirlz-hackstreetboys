package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.mapRequestDto;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeatureDto {
    @SerializedName("center")
    @Expose
    public List<Double> center = null;

    public double getX(){
        return center.get(0);
    }
    public double getY(){
        return center.get(1);
    }
}