package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.mapRequestDto.MapDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GeoCoding implements Serializable {

    RestTemplate rt = new RestTemplate();

    public GeoCoding() {
    }

    /**
     * Gibt die X-Koordinaten/Breitengrade anhand einer Adresse (String) an.
     *
     * @param locationAddresse reele Adresse, am besten nur ein Wort für Stadt, Strasse, Land, etc..
     * @return double Koordinate der X-Achse/des Breitengrad
     */
    public double erhalteErstesX(String locationAddresse) {
        return erhalteXVomErsten(geocode(locationAddresse));
    }

    /**
     * Gibt die Y-Koordinaten/Längengrade anhand einer Adresse (String) an.
     *
     * @param locationAddresse reele Adresse, am besten nur ein Wort für Stadt, Strasse, Land, etc..
     * @return double Koordinate der Y-Achse/des Längengrad
     */
    public double erhalteErstesY(String locationAddresse) {
        return erhalteYVomErsten(geocode(locationAddresse));
    }

    /**
     * Gibt die X-Koordinaten/Breitengrade anhand eines MapDto an.
     *
     * @param results MapDto das mindenstens ein MapLocationDto enthällt
     * @return double Koordinate der X-Achse/des Breitengrad
     */
    public double erhalteXVomErsten(MapDto results) {
        if (results != null) {
            if (results.features != null) {
                if (results.features.size() > 0) {
                    return results.features.get(0).getX();
                }
            }
        }
        return 0;
    }

    /**
     * Gibt die Y-Koordinaten/Längengrade anhand eines MapDto an.
     *
     * @param results MapDto das mindenstens ein MapLocationDto enthällt
     * @return double Koordinate der Y-Achse/des Längengrad
     */
    public double erhalteYVomErsten(MapDto results) {
        if (results != null) {
            if (results.features != null) {
                if (results.features.size() > 0) {
                    return results.features.get(0).getY();
                }
            }
        }
        return 0;
    }


    /**
     * Baut die mapBox-geoCoding-Url mit dem momentanen Suchbegriff und gibt sie als String zurück, die Url die
     * aufgerufen werden muss damit die json-ResultatObjekte für den momentanen Suchbegriff 'locationAddresse'
     * empfangen werden können.
     *
     * @param locationAddresse reele Adresse, am besten nur ein Wort für Stadt, Strasse, Land, etc..
     * @return Url zum Empfangen der json-ResultatObjekte
     */
    public String encode(final String locationAddresse) {
        return "https://api.mapbox.com/geocoding/v5/mapbox.places/" + urlEncodierung(locationAddresse) + ".json?access_token"
            + "=sk.eyJ1IjoiZXhhbHciLCJhIjoiY2pza2N2ZGxvMHpyNDQzcWV3dmRmcmMybiJ9.2ItINTqQyxq_vg2JC_Yz9Q";
    }

    /**
     * Nimmt eine Addresse als String und versucht das entsprechende MapDto vom MapBoxServer zu empfangen.
     *
     * @param locationAddresse reele Adresse, am besten nur ein Wort für Stadt, Strasse, Land, etc..
     * @return MapDto, enthällt alle MapLocationDto, mit möglich-gemeinten LocationKoordinaten
     * @throws IOException wenn json Datei nicht geparsed werden kann
     */
    public MapDto geocode(final String locationAddresse) {
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
     * Url-encodierung einer StringVariablen in UTF-8 zum Einfügen in Url's.
     *
     * @param value String zum umwandeln
     * @return UTF-8 encodierter String
     */
    public String urlEncodierung(final String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
