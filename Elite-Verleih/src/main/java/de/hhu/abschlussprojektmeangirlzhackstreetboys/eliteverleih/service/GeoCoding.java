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

    public double getXOfFirst(MapDto results) {
        return results.features.get(0).getX();
    }

    public double getYOfFirst(MapDto results) {
        return results.features.get(0).getY();
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
    public MapDto geocode(final String locationAddresse) throws IOException {
        System.err.println("0!");
        String url = encode(locationAddresse);
        //InputStream is = invokeService(encode(address));
        System.err.println("1!");
        if (locationAddresse != null) {
                try {
                    ResponseEntity<MapDto> result = rt.getForEntity(url, MapDto.class);
                    System.err.println("2.05!");
                    MapDto res = result.getBody();
                    System.err.println("2.1!  " + result.getStatusCode());
                    return res;
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    System.err.println("3!");
                    return null;
                }
        }
        System.err.println("4!");
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
