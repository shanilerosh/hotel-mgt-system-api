package com.esoft.hotelmanagementsystem.consumer;

import com.esoft.hotelmanagementsystem.config.EmailUtil;
import com.esoft.hotelmanagementsystem.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ShanilErosh
 */
@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailUtil emailUtil;

        //Email Que Data
        @RabbitListener(queues = {"${queue.name-email}"})
        public void consumeEmailQue(final EmailDto emailDto) {
            emailUtil.sendEmail(emailDto);
        }

}
