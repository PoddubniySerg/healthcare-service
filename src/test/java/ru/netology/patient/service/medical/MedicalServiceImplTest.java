package ru.netology.patient.service.medical;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.service.alert.SendAlertService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class MedicalServiceImplTest {

    private static PatientInfo patientInfo;

    @BeforeAll
    public static void startTests() {
        patientInfo = new PatientInfo(UUID.randomUUID().toString(),
                "Иван",
                "Петров",
                LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)));
        System.out.println("Start tests");
    }

    @AfterAll
    public static void completeTests() {
        System.out.println("Complete tests");
    }

    @BeforeEach
    public void startTest() {
        System.out.println("Srart test");
    }

    @AfterEach
    public void completeTest() {
        System.out.println("\nComplete test");
    }

    @Test
    public void checkBloodPressureAndSendMessage() {
        //arrange
        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        ;
        PatientInfoFileRepository fileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(fileRepository.getById(Mockito.any())).thenReturn(patientInfo);

        MedicalService medicalService = new MedicalServiceImpl(fileRepository, alertService);

        String expected = String.format("Warning, patient with id: %s, need help", patientInfo.getId());
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        //act
        medicalService.checkBloodPressure(patientInfo.getId(), new BloodPressure(60, 120));
        //assert
        Mockito.verify(alertService, Mockito.times(1)).send(Mockito.any());
        Mockito.verify(alertService).send(argumentCaptor.capture());
        Assertions.assertEquals(expected, argumentCaptor.getValue());
    }

    @Test
    public void checkTemperatureAndSendMessage() {
        //arrange
        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        ;
        PatientInfoFileRepository fileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(fileRepository.getById(Mockito.any())).thenReturn(patientInfo);

        MedicalService medicalService = new MedicalServiceImpl(fileRepository, alertService);

        String expected = String.format("Warning, patient with id: %s, need help", patientInfo.getId());
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        //act
        medicalService.checkTemperature(patientInfo.getId(), new BigDecimal("35.0"));
        //assert
        Mockito.verify(alertService, Mockito.times(1)).send(Mockito.any());
        Mockito.verify(alertService).send(argumentCaptor.capture());
        Assertions.assertEquals(expected, argumentCaptor.getValue());
    }

    @Test
    public void checkNormalTemperatureAndBloodPressure() {
        //arrange
        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        ;
        PatientInfoFileRepository fileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(fileRepository.getById(Mockito.any())).thenReturn(patientInfo);

        MedicalService medicalService = new MedicalServiceImpl(fileRepository, alertService);
        //act
        medicalService.checkTemperature(patientInfo.getId(), new BigDecimal("36.8"));
        medicalService.checkBloodPressure(patientInfo.getId(), new BloodPressure(120, 80));
        //assert
        Mockito.verify(alertService, Mockito.never()).send(Mockito.any());
    }
}