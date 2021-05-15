package br.com.fiap.repository.rowmapper;

import br.com.fiap.domain.SubscriptionSensor;
import br.com.fiap.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SubscriptionSensor}, with proper type conversions.
 */
@Service
public class SubscriptionSensorRowMapper implements BiFunction<Row, String, SubscriptionSensor> {

    private final ColumnConverter converter;

    public SubscriptionSensorRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SubscriptionSensor} stored in the database.
     */
    @Override
    public SubscriptionSensor apply(Row row, String prefix) {
        SubscriptionSensor entity = new SubscriptionSensor();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTelegramId(converter.fromRow(row, prefix + "_telegram_id", Integer.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        return entity;
    }
}
