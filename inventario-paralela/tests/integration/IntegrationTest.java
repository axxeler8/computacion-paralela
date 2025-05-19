package com.empresa.tests;
import org.junit.jupiter.api.*;
import java.rmi.registry.LocateRegistry;
import com.empresa.inventario.*;
import java.rmi.Naming;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {
  static InventarioService svc;
  @BeforeAll static void startServer() throws Exception {
    LocateRegistry.createRegistry(1099);
    new InventarioServiceImpl();
    svc=(InventarioService)Naming.lookup("rmi://localhost:1099/InventarioService");
  }
  @Test void testListar() throws Exception { List<Repuesto> l=svc.listarTodos(); assertFalse(l.isEmpty()); }
  @Test void testFlujo() throws Exception {
    Repuesto r=svc.consultarRepuesto(1003);
    int orig=r.getStock();
    boolean ok=svc.reservarRepuesto(1003,2);
    assertTrue(ok);
    Repuesto r2=svc.consultarRepuesto(1003);
    assertEquals(orig-2, r2.getStock());
  }
}