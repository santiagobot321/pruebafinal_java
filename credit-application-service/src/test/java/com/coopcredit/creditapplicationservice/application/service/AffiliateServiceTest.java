package com.coopcredit.creditapplicationservice.application.service;

import com.coopcredit.creditapplicationservice.application.port.out.AffiliatePersistencePort;
import com.coopcredit.creditapplicationservice.domain.exception.AffiliateNotFoundException;
import com.coopcredit.creditapplicationservice.domain.model.Affiliate;
import com.coopcredit.creditapplicationservice.domain.model.AffiliateStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AffiliateServiceTest {

    @Mock
    private AffiliatePersistencePort affiliatePersistencePort;

    @InjectMocks
    private AffiliateService affiliateService;

    private Affiliate testAffiliate;

    @BeforeEach
    void setUp() {
        testAffiliate = new Affiliate(
                1L,
                "123456789",
                "John Doe",
                new BigDecimal("5000.00"),
                LocalDate.of(2023, 1, 1),
                AffiliateStatus.ACTIVO
        );
    }

    @Test
    void registerAffiliate_shouldReturnSavedAffiliate() {
        when(affiliatePersistencePort.saveAffiliate(any(Affiliate.class))).thenReturn(testAffiliate);

        Affiliate result = affiliateService.registerAffiliate(testAffiliate);

        assertNotNull(result);
        assertEquals(testAffiliate.getDocument(), result.getDocument());
        verify(affiliatePersistencePort, times(1)).saveAffiliate(testAffiliate);
    }

    @Test
    void updateAffiliate_shouldUpdateExistingAffiliate() {
        Affiliate updatedDetails = new Affiliate(
                null, // ID is ignored in update
                "123456789",
                "Jane Doe",
                new BigDecimal("6000.00"),
                LocalDate.of(2023, 1, 1),
                AffiliateStatus.INACTIVO
        );

        when(affiliatePersistencePort.findAffiliateById(1L)).thenReturn(Optional.of(testAffiliate));
        when(affiliatePersistencePort.saveAffiliate(any(Affiliate.class))).thenReturn(testAffiliate);

        Affiliate result = affiliateService.updateAffiliate(1L, updatedDetails);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals(new BigDecimal("6000.00"), result.getSalary());
        assertEquals(AffiliateStatus.INACTIVO, result.getStatus());
        verify(affiliatePersistencePort, times(1)).findAffiliateById(1L);
        verify(affiliatePersistencePort, times(1)).saveAffiliate(testAffiliate);
    }

    @Test
    void updateAffiliate_shouldThrowExceptionIfNotFound() {
        Affiliate updatedDetails = new Affiliate();
        when(affiliatePersistencePort.findAffiliateById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> affiliateService.updateAffiliate(99L, updatedDetails));
        verify(affiliatePersistencePort, times(1)).findAffiliateById(99L);
        verify(affiliatePersistencePort, never()).saveAffiliate(any(Affiliate.class));
    }

    @Test
    void getAffiliateById_shouldReturnAffiliateIfExists() {
        when(affiliatePersistencePort.findAffiliateById(1L)).thenReturn(Optional.of(testAffiliate));

        Optional<Affiliate> result = affiliateService.getAffiliateById(1L);

        assertTrue(result.isPresent());
        assertEquals(testAffiliate.getDocument(), result.get().getDocument());
        verify(affiliatePersistencePort, times(1)).findAffiliateById(1L);
    }

    @Test
    void getAffiliateById_shouldReturnEmptyIfNotFound() {
        when(affiliatePersistencePort.findAffiliateById(99L)).thenReturn(Optional.empty());

        Optional<Affiliate> result = affiliateService.getAffiliateById(99L);

        assertFalse(result.isPresent());
        verify(affiliatePersistencePort, times(1)).findAffiliateById(99L);
    }

    @Test
    void getAffiliateByDocument_shouldReturnAffiliateIfExists() {
        when(affiliatePersistencePort.findAffiliateByDocument("123456789")).thenReturn(Optional.of(testAffiliate));

        Optional<Affiliate> result = affiliateService.getAffiliateByDocument("123456789");

        assertTrue(result.isPresent());
        assertEquals(testAffiliate.getId(), result.get().getId());
        verify(affiliatePersistencePort, times(1)).findAffiliateByDocument("123456789");
    }

    @Test
    void getAffiliateByDocument_shouldReturnEmptyIfNotFound() {
        when(affiliatePersistencePort.findAffiliateByDocument("nonexistent")).thenReturn(Optional.empty());

        Optional<Affiliate> result = affiliateService.getAffiliateByDocument("nonexistent");

        assertFalse(result.isPresent());
        verify(affiliatePersistencePort, times(1)).findAffiliateByDocument("nonexistent");
    }
}
