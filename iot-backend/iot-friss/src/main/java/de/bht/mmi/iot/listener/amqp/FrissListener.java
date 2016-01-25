package de.bht.mmi.iot.listener.amqp;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import de.bht.mmi.iot.constants.AmqpConstants;
import de.bht.mmi.iot.model.Measurement;
import de.bht.mmi.iot.repository.MeasurementRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

@Component
public class FrissListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private DynamoDB dynamoDB;

    private static final Logger LOGGER = LoggerFactory.getLogger(FrissListener.class);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(),
            exchange = @Exchange(value = AmqpConstants.FRISS_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = AmqpConstants.ALL_MESSAGE_ROUTING_KEY)
    )
    public void processFriss(@Headers Map<String, String> amqpHeaders, String data) {
        LOGGER.info("Received message with payload: {} and amqpHeaders: {}.", data, amqpHeaders);


/*        TableWriteItems tableWriteItems =
                new TableWriteItems(DbConstants.TABLENAME_MEASUREMENT).withItemsToPut(
                        new Item()
                        .withPrimaryKey(DbConstants.ATTRIBUTE_SENSOR_ID, "abc")
                        .withJSON("document", json)
                );

        dynamoDB.batchWriteItem(tableWriteItems);*/

        Measurement m = new Measurement();
        m.setSensorId("abc");
        m.setTimeOfMeasurement(DateTime.now());
        m.setAcceleration(new double[]{0.3, 0.5});
        measurementRepository.save(Arrays.asList(m));


    }

}
