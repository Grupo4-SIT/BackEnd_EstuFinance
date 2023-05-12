package com.estufinance.serverfinanceapp.model.domain;

import java.math.BigDecimal;
import java.util.Optional;

import com.estufinance.serverfinanceapp.model.entity.enums.PagoInteres;
import com.estufinance.serverfinanceapp.model.entity.enums.Periodicidad;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Tasa {

    private BigDecimal interes;
    private PagoInteres formaPago;
    private Periodicidad periodicidad;

    public String getPlainSeparateBySemicolon() {
        return new StringBuilder()
                .append(String.valueOf(interes)).append(";")
                .append(formaPago.toString()).append(";")
                .append(periodicidad.toString())
                .toString();
    }

    public static Optional<Tasa> getFormattedTasa(String tasa) {

        String[] tasaFields = tasa.split(";");
        Tasa tasaFormatted = null;

        if (tasaFields.length == 3) {

            final BigDecimal interes = BigDecimal.valueOf(Double.valueOf(tasaFields[0]));
            final PagoInteres formaPago = PagoInteres.valueOf(tasaFields[1]);
            final Periodicidad periodicidad = Periodicidad.valueOf(tasaFields[2]);

            tasaFormatted = Tasa.builder()
                    .interes(interes)
                    .formaPago(formaPago)
                    .periodicidad(periodicidad)
                    .build();
        }

        return Optional.ofNullable(tasaFormatted);
    }

}
