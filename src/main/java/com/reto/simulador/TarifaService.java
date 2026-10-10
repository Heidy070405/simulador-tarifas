package com.reto.simulador;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TarifaService {

    @Value("${tarifa.valor-unitario}")
    private BigDecimal valorUnitario;

    @Value("${tarifa.cargo-fijo}")
    private BigDecimal cargoFijo;

    @Value("${tarifa.descuento-institucional}")
    private BigDecimal descuentoInstitucional;

    @Value("${tarifa.iva}")
    private BigDecimal iva;

    public record Liquidacion(BigDecimal subtotal, BigDecimal descuento,
                              BigDecimal iva, BigDecimal total) {}

    public BigDecimal calcularSubtotal(int consumo) {
        return valorUnitario.multiply(BigDecimal.valueOf(consumo))
                .add(cargoFijo);
    }

    public BigDecimal calcularDescuento(BigDecimal subtotal, boolean institucional) {
        return institucional
                ? subtotal.multiply(descuentoInstitucional)
                : BigDecimal.ZERO;
    }

    public BigDecimal calcularIva(BigDecimal baseGravable) {
        return baseGravable.multiply(iva);
    }

    public Liquidacion liquidar(int consumo, boolean institucional) {
        if (consumo < 0) {
            throw new IllegalArgumentException("El consumo no puede ser negativo");
        }
        BigDecimal subtotal  = calcularSubtotal(consumo);
        BigDecimal descuento = calcularDescuento(subtotal, institucional);
        BigDecimal base      = subtotal.subtract(descuento);
        BigDecimal valorIva  = calcularIva(base);
        BigDecimal total     = base.add(valorIva);

        return new Liquidacion(
                redondear(subtotal), redondear(descuento),
                redondear(valorIva), redondear(total));
    }

    private BigDecimal redondear(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }
}