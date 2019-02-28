package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.AccountDto;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.ReservationDto;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PropayManagerTest {



    PropayManager propayManager;

    static RestTemplate restTemplateOK = mock(RestTemplate.class);
    static RestTemplate restTemplateWeg = mock(RestTemplate.class);

    @BeforeClass
    public static void setupBeforeClass() {
        HttpStatus mockStatusOk = HttpStatus.OK;
        List<ReservationDto> reservationDtoList = new ArrayList<ReservationDto>();
        ReservationDto mockReservationDto = new ReservationDto();
        mockReservationDto.setId(1);
        mockReservationDto.setAmount(2.0);
        reservationDtoList.add(mockReservationDto);
        AccountDto mockAccountDto = new AccountDto();
        mockAccountDto.setAccount("MockTest");
        mockAccountDto.setAmount(100);
        mockAccountDto.setResavations(reservationDtoList);
        ResponseEntity<AccountDto> mockAccountEntity = new ResponseEntity<AccountDto>(mockAccountDto, mockStatusOk);
        ResponseEntity<ReservationDto> mockReservationEntity = new ResponseEntity<ReservationDto>(mockReservationDto, mockStatusOk);
        when(restTemplateOK.getForEntity(anyString(), any(Class.class))).thenReturn(mockAccountEntity);
        when(restTemplateOK.postForEntity(anyString(), any(),  ArgumentMatchers.argThat(new ClassOrSubclassMatcher<>(AccountDto.class)))).thenReturn(mockAccountEntity);
        when(restTemplateOK.postForEntity(anyString(), any(),  ArgumentMatchers.argThat(new ClassOrSubclassMatcher<>(ReservationDto.class)))).thenReturn(mockReservationEntity);

        HttpStatus mockStatusError = HttpStatus.INTERNAL_SERVER_ERROR;
        when(restTemplateWeg.getForEntity(anyString(), any(Class.class))).thenThrow(NullPointerException.class);


    }
    private void ok() {
        propayManager = new PropayManager(restTemplateOK);
    }
    private void propayNichtVerfuegbar() {
        propayManager = new PropayManager(restTemplateWeg);
    }

    @Test
    public void propayManager_GetAccountOK() {
        ok();
        AccountDto test = propayManager.getAccount("MockTest");
        assertEquals("MockTest", test.getAccount());
    }

    @Test
    public void propayManager_guthabenAufladenOK() {
        ok();
        int code = propayManager.guthabenAufladen("MockTest", 10);
        assertEquals(200, code);
    }

    @Test
    public void propayManager_ueberweisenOK() {
        ok();
        int code = propayManager.ueberweisen("MockTest", "test",  10);
        assertEquals(200, code);
    }

    @Test
    public void propayManager_KautionReservierenOK() {
        ok();
        ReservationDto reservationDtoErgebins = propayManager.kautionReserviern("MockTest", "test",  10);
        assertEquals(1, reservationDtoErgebins.getId());
    }

    @Test
    public void propayManager_KautionEinziehenOK() {
        ok();
        int code = propayManager.kautionEinziehen("MockTest", 1);
        assertEquals(200, code);
    }

    @Test
    public void propayManager_KautionFreigebenOK() {
        ok();
        int code = propayManager.kautionFreigeben("MockTest", 1);
        assertEquals(200, code);
    }


    @Test(expected = RuntimeException.class)
    public void propayManager_GetAccountError() {
        propayNichtVerfuegbar();
        AccountDto test = propayManager.getAccount("MockTest");
        //assertEquals("MockTest", test.getAccount());
    }


}
