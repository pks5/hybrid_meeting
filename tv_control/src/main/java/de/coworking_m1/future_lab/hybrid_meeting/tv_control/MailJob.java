package de.coworking_m1.future_lab.hybrid_meeting.tv_control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailJob {

    @Autowired
    private ReadInboundEmailService emailService;
    
    @Scheduled(fixedDelay = 1000)
    public void checkMail(){
        log.info("Running scheduler");

        emailService.readInboundEmails();
    }
}
