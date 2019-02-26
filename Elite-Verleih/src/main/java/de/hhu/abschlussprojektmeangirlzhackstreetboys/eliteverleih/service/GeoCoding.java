package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.mapRequestDto.MapDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URLEncoder;

public class GeoCoding implements Serializable {

    RestTemplate rt = new RestTemplate();

    public GeoCoding() {
    }

    public double getFirstX(String locationAddresse) {
        return getXOfFirst(geocode(locationAddresse));
    }

    public double getFirstY(String locationAddresse) {
        return getYOfFirst(geocode(locationAddresse));
    }

    public double getXOfFirst(MapDto results) {
        if (results != null){
            if (results.features != null)
                if(results.features.size()>0)
                    return results.features.get(0).getX();
        }
        return 0;
    }

    public double getYOfFirst(MapDto results) {
        if (results != null){
            if (results.features != null)
                if(results.features.size()>0)
                    return results.features.get(0).getY();
        }
        return 0;
    }


    /**
     * baut die mapBox-geoCoding-Url
     *
     * @param locationAddresse
     * @return Url
     */
    public String encode(final String locationAddresse) {
        return "https://api.mapbox.com/geocoding/v5/mapbox.places/" + urlEncode(locationAddresse) +
            ".json?access_token=sk.eyJ1IjoiZXhhbHciLCJhIjoiY2pza2N2ZGxvMHpyNDQzcWV3dmRmcmMybiJ9.2ItINTqQyxq_vg2JC_Yz9Q";
    }

    /**
     * @param locationAddresse
     * @return MapDto
     * @throws IOException
     */
    public MapDto geocode(final String locationAddresse){
        String url = encode(locationAddresse);
        if (locationAddresse != null) {
                try {
                    ResponseEntity<MapDto> result = rt.getForEntity(url, MapDto.class);
                    MapDto res = result.getBody();
                    return res;
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    return null;
                }
        }
        return null;
    }

    /**
     * Url-encodierung einer Variablen
     *
     * @param value
     * @return
     */
    private String urlEncode(final String value) {
        try {
            return "\"" + URLEncoder.encode(value, "UTF-8") + "\"";
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
