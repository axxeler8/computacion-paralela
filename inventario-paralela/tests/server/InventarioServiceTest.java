package com.empresa.tests;
import com.empresa.inventario.InventarioServiceImpl;
import com.empresa.inventario.Repuesto;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
public class InventarioServiceTest {
  static InventarioServiceImpl impl;
  @BeforeAll static void init() throws Exception { impl=new InventarioServiceImpl(); }
  @Test void testConsultar(){ Repuesto r=impl.consultarRepuesto(1001); assertNotNull(r); assertEquals(1001,r.getSku()); }
  @Test void testReservar(){ boolean ok=impl.reservarRepuesto(1002,1); assertTrue(ok); }
}