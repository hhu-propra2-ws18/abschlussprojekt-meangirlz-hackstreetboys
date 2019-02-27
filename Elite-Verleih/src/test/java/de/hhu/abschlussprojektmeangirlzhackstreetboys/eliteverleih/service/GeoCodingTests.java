package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.mapRequestDto.MapDto;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.mapRequestDto.MapLocationDto;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

public class GeoCodingTests {

    GeoCoding geoCoder = new GeoCoding();

    private MapDto setup(){
        MapDto map = setupEmpty();
        MapLocationDto mapL = new MapLocationDto();
        mapL.center = new ArrayList<Double>();
        mapL.center.add(1.57);
        mapL.center.add(50.89);
        map.features.add(mapL);
        return map;
    }

    private MapDto setupEmpty(){
        MapDto map = new MapDto();
        map.features = new ArrayList<MapLocationDto>();
        return map;
    }

    private MapDto setupNull(){
        MapDto map = new MapDto();
        return map;
    }

    @Test
    public void integrationTest_geoCode_erhalteXVom() {
        MapDto alCoords;
        alCoords = geoCoder.geocode("Düsseldorf");
        Assertions.assertThat(geoCoder.erhalteXVomErsten(alCoords)).isEqualTo(-10.18005);
    }

    @Test
    public void integrationTest_geoCode_erhalteYVom() {
        MapDto alCoords;
        alCoords = geoCoder.geocode("Düsseldorf");
        Assertions.assertThat(geoCoder.erhalteYVomErsten(alCoords)).isEqualTo(-54.927086);
    }

    @Test
    public void integrationTest_geoCode_erhalteXVom_Null() {
        MapDto alCoords = null;
        Assertions.assertThat(geoCoder.erhalteXVomErsten(alCoords)).isEqualTo(0);
    }

    @Test
    public void integrationTest_geoCode_erhalteYVom_Null() {
        MapDto alCoords = null;
        Assertions.assertThat(geoCoder.erhalteYVomErsten(alCoords)).isEqualTo(0);
    }

    @Test
    public void integrationTest_geoCode_erhalteErstesY() {
        Assertions.assertThat(geoCoder.erhalteErstesY("Düsseldorf")).isEqualTo(-54.927086);
    }

    @Test
    public void integrationTest_geoCode_erhalteErstesX() throws IOException {
        Assertions.assertThat(geoCoder.erhalteErstesX("Düsseldorf")).isEqualTo(-10.18005);
    }

    @Test
    public void urlEncodierung_korrekteEncodierung(){
        Assertions.assertThat(geoCoder.urlEncodierung("{[(\"%§?! ,.;")).isEqualTo("%7B%5B%28%22%25%C2%A7%3F%21+%2C.%3B");
    }

    @Test
    public void urlErstellung_korrekteErstellung(){
        Assertions.assertThat(geoCoder.encode("Bissen, Luxembourg"))
            .isEqualTo("https://api.mapbox.com/geocoding/v5/mapbox.places/" + "Bissen%2C+Luxembourg"
                + ".json?access_token=sk.eyJ1IjoiZXhhbHciLCJhIjoiY2pza2N2ZGxvMHpyNDQzcWV3dmRmcmMybiJ9."
                + "2ItINTqQyxq_vg2JC_Yz9Q");
    }

    @Test
    public void erhalteVomErsten_korrekteXKoordinaten(){
        MapDto map = setup();
        Assertions.assertThat(geoCoder.erhalteXVomErsten(map)).isEqualTo(50.89);
    }

    @Test
    public void erhalteVomErsten_korrekteYKoordinaten(){
        MapDto map = setup();
        Assertions.assertThat(geoCoder.erhalteYVomErsten(map)).isEqualTo(1.57);
    }

    @Test
    public void erhalteVomErsten_korrekteXKoordinaten_NullSetup(){
        MapDto map = setupNull();
        Assertions.assertThat(geoCoder.erhalteXVomErsten(map)).isEqualTo(0);
    }

    @Test
    public void erhalteVomErsten_korrekteYKoordinaten_NullSetup(){
        MapDto map = setupNull();
        Assertions.assertThat(geoCoder.erhalteYVomErsten(map)).isEqualTo(0);
    }

    @Test
    public void erhalteVomErsten_korrekteXKoordinaten_LeeresSetup(){
        MapDto map = setupEmpty();
        Assertions.assertThat(geoCoder.erhalteXVomErsten(map)).isEqualTo(0);
    }

    @Test
    public void erhalteVomErsten_korrekteYKoordinaten_LeeresSetup(){
        MapDto map = setupEmpty();
        Assertions.assertThat(geoCoder.erhalteYVomErsten(map)).isEqualTo(0);
    }
}
