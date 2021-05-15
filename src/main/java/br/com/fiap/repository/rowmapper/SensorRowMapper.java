package br.com.fiap.repository.rowmapper;

import br.com.fiap.domain.Sensor;
import br.com.fiap.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Sensor}, with proper type conversions.
 */
@Service
public class SensorRowMapper implements BiFunction<Row, String, Sensor> {

    private final ColumnConverter converter;

    public SensorRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Sensor} stored in the database.
     */
    @Override
    public Sensor apply(Row row, String prefix) {
        Sensor entity = new Sensor();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setVariable(converter.fromRow(row, prefix + "_variable", String.class));
        entity.setUnit(converter.fromRow(row, prefix + "_unit", String.class));
        entity.setValue(converter.fromRow(row, prefix + "_value", String.class));
        return entity;
    }
}
