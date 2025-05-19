import { Component, OnInit } from '@angular/core';
import { InventarioService } from '../../services/inventario.service';

@Component({
selector: 'app-inventario',
templateUrl: './inventario.page.html',
styleUrls: ['./inventario.page.scss'],
standalone: false,
})
export class InventarioPage implements OnInit {
modo: string = '';
repuestos: any[] = [];
reservas: any[] = [];
repuestoSeleccionado: any = null;
reservaSeleccionada: any = null;
skuBusqueda: string = '';
idReservaBusqueda: string = '';


nuevoRepuesto: any = { idUbicacion: null, sku: null, cantidad: null, precio: null, categoria: '', disponible: false, nombre: '' };
liberarRepuestoModel: any = { idUbicacion: null, sku: null, cantidad: null };
nuevaReserva: any = { idVehiculo: null, sku: null, cantidad: null };
liberarReservaId: string = '';

constructor(private inventarioService: InventarioService) {}

ngOnInit() {
this.cargarDatosIniciales();
}

cargarDatosIniciales() {
this.inventarioService.getRepuestos().subscribe({ next: data => this.repuestos = data, error: err => console.error(err) });
this.inventarioService.getReservas().subscribe({ next: data => this.reservas = data, error: err => console.error(err) });
}


verRepuestos() { this.setModo('repuestoVer'); }

buscarRepuestoPorSku() {
const sku = +this.skuBusqueda;
if (sku) {
this.inventarioService.getRepuesto(sku).subscribe({
next: data => this.repuestoSeleccionado = data,
error: () => this.repuestoSeleccionado = null
});
this.setModo('repuestoConsultar');
}
}

enviarNuevoRepuesto() {
if (this.validarRepuesto()) {
this.inventarioService.agregarRepuesto(this.nuevoRepuesto).subscribe({
next: () => { this.cargarDatosIniciales(); this.resetFormRepuesto(); this.setModo(''); },
error: err => console.error(err)
});
}
}

liberarRepuesto() {
const m = this.liberarRepuestoModel;
if (m.idUbicacion && m.sku && m.cantidad > 0) {
this.inventarioService.liberarRepuesto(m.idUbicacion, m.sku, m.cantidad).subscribe({
next: () => { this.cargarDatosIniciales(); this.liberarRepuestoModel = { idUbicacion: null, sku: null, cantidad: null }; this.setModo(''); },
error: err => console.error(err)
});
}
}

validarRepuesto(): boolean {
const r = this.nuevoRepuesto;
return r.idUbicacion && r.sku && r.cantidad > 0 && r.precio > 0 && r.nombre.trim().length > 0;
}

resetFormRepuesto() {
this.nuevoRepuesto = { idUbicacion: null, sku: null, cantidad: null, precio: null, categoria: '', disponible: false, nombre: '' };
}


verReservas() { this.setModo('reservaVer'); }

buscarReservaPorId() {
const id = +this.idReservaBusqueda;
if (id) {
this.inventarioService.getReserva(id).subscribe({ next: data => this.reservaSeleccionada = data, error: () => this.reservaSeleccionada = null });
this.setModo('reservaConsultar');
}
}

enviarNuevaReserva() {
if (this.validarReserva()) {
this.inventarioService.agregarReserva(this.nuevaReserva).subscribe({
next: () => { this.cargarDatosIniciales(); this.resetFormReserva(); this.setModo(''); },
error: err => console.error(err)
});
}
}

eliminarReserva() {
const id = +this.liberarReservaId;
if (id && confirm('¿Eliminar reserva?')) {
this.inventarioService.eliminarReserva(id).subscribe({ next: () => { this.cargarDatosIniciales(); this.liberarReservaId = ''; this.setModo(''); }, error: err => console.error(err) });
}
}

validarReserva(): boolean {
const r = this.nuevaReserva;
return r.idVehiculo && r.sku && r.cantidad > 0;
}

resetFormReserva() {
this.nuevaReserva = { idVehiculo: null, sku: null, cantidad: null };
}

setModo(m: string) {
this.modo = m;
this.repuestoSeleccionado = null;
this.reservaSeleccionada = null;
this.skuBusqueda = '';
this.idReservaBusqueda = '';
}
}