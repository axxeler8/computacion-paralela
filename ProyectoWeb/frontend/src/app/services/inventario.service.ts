import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InventarioService {
  private apiUrl = 'http://localhost:8080/api/inventario';

  constructor(private http: HttpClient) { }

  
  getRepuestos(): Observable<any> {
    return this.http.get(`${this.apiUrl}/repuestos`);
  }

  getRepuesto(sku: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/repuestos/${sku}`);
  }

  agregarRepuesto(repuesto: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/repuestos`, repuesto);
  }

  liberarRepuesto(idUbicacion: number, sku: number, cantidad: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/repuestos/${idUbicacion}/${sku}/liberar?cantidad=${cantidad}`, {});
  }

  
  getReservas(): Observable<any> {
    return this.http.get(`${this.apiUrl}/reservas`);
  }

  getReserva(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/reservas/${id}`);
  }

  agregarReserva(reserva: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/reservas`, reserva);
  }

  eliminarReserva(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/reservas/${id}`);
  }
}