package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.mapRequestDto;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MapDto {

    //@SerializedName("type")
    //@Expose
    //public String type;
    //@SerializedName("query")
    //@Expose
    //public List<String> query = null;
    @SerializedName("features")
    @Expose
    public List<FeatureDto> features = null;
    //@SerializedName("attribution")
    //@Expose
    //public String attribution;

}