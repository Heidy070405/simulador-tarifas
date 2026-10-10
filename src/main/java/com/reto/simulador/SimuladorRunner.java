package com.reto.simulador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SimuladorRunner implements CommandLineRunner {

    @Autowired
    private TarifaService tarifaService;

    @Override
    public void run(String... args) {
        System.out.println("=== SIMULADOR DE TARIFAS Y LIQUIDACIÓN INSTITUCIONAL ===");

        imprimir("Hogar Pérez",         100, false);
        imprimir("Colegio San José",    100, true);
        imprimir("Universidad Central", 250, true);
        imprimir("Comercio La Esquina", 40,  false);
    }

    private void imprimir(String cliente, int consumo, boolean institucional) {
        var l = tarifaService.liquidar(consumo, institucional);
        System.out.println("\nCliente:    " + cliente
                + (institucional ? " (Institucional)" : " (Regular)"));
        System.out.println("Consumo:    " + consumo + " unidades");
        System.out.println("Subtotal:   $" + l.subtotal());
        System.out.println("Descuento:  -$" + l.descuento());
        System.out.println("IVA:        $" + l.iva());
        System.out.println("TOTAL:      $" + l.total());
    }
}