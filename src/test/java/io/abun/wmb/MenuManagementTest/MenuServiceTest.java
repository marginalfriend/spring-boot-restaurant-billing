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
import static org.junit.jupiter.api.Assumptions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

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

        Menu nasiGoreng = menuService.findById(1);

        assertEquals(1, nasiGoreng.id());
        assertEquals("Nasi Goreng", nasiGoreng.name());
        assertEquals(15000, nasiGoreng.price());
    }
}
