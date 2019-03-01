package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.maprequestdto;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MapDto {

    @SerializedName("features")
    @Expose
    public List<MapLocationDto> features = null;

}