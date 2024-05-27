package io.abun.wmb.MenuManagementTest;

import io.abun.wmb.MenuManagement.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuRequestServiceTest {

    @Mock
    private MenuRepository menuRepository;
    private MenuService menuService;

    @BeforeEach
    public void setup() {
        menuService = new MenuServiceImpl(menuRepository);
    }

    @Test
    public void testGetNotFound() {
        assertThrows(ResponseStatusException.class, () -> menuService.findById(-2));
    }

    @Test
    public void testGetSuccess() {
        when(menuRepository.findById(1))
                .thenReturn(Optional.ofNullable(MenuEntity.builder().id(1).name("Nasi Goreng").price(15000).build()));

        MenuRequest nasiGoreng = menuService.findById(1);

        assertEquals(1, nasiGoreng.id());
        assertEquals("Nasi Goreng", nasiGoreng.name());
        assertEquals(15000, nasiGoreng.price());
    }

    @Test
    public void testDeleteSuccess() {
        MenuEntity nasgorEntity = MenuEntity.builder()
                .id(1)
                .name("Nasi Goreng")
                .price(15000)
                .build();

        when(menuRepository.findById(1))
                .thenReturn(Optional.ofNullable(nasgorEntity));

        MenuRequest nasiGoreng = menuService.findById(1);

        menuService.removeById(1);

        verify(menuRepository, times(1)).delete(MenuEntity.parse(nasiGoreng));
    }

    @Test
    public void testDeleteFailedMenuNotFound() {
        MenuEntity nasgorEntity = MenuEntity.builder()
                .id(1)
                .name("Nasi Goreng")
                .price(15000)
                .build();

        when(menuRepository.findById(1))
                .thenReturn(Optional.ofNullable(nasgorEntity));

        MenuRequest nasiGoreng = menuService.findById(1);

        assertThrows(AssertionError.class, () -> menuService.removeById(2));

        verify(menuRepository, times(0)).delete(MenuEntity.parse(nasiGoreng));
    }

    @Test
    public void testUpdateNotFound() {
        MenuRequest menuRequestToUpdate = new MenuRequest(2, "Nasi Uduk", 10000);

        MenuEntity nasgorEntity = MenuEntity.builder()
                .id(1)
                .name("Nasi Goreng")
                .price(15000)
                .build();

        when(menuRepository.findById(1))
                .thenReturn(Optional.ofNullable(nasgorEntity));

        MenuRequest nasiGoreng = menuService.findById(1);

        assertThrows(AssertionError.class, () -> menuService.update(menuRequestToUpdate));

        verify(menuRepository, times(0)).delete(MenuEntity.parse(nasiGoreng));
    }


}
