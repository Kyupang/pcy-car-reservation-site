package reservationsystem.reservationcar.service;


import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Balance;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.StorageType;
import net.nurigo.sdk.message.request.MessageListRequest;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.MessageListResponse;
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private final DefaultMessageService messageService;

<<<<<<< HEAD
    public SmsService() {
        // 반드시 계정 내 등록된 유효한 API 키, API Secret Key를 입력해주셔야 합니다!
        this.messageService = NurigoApp.INSTANCE.initialize("NCSUOSOZHFQ9DNVW","PY1WROKIFUZVF7CK1DLLZFZYCUQBBSYY", "https://api.coolsms.co.kr");
=======
    @Value("${sms.api.key}")
    private String apiKey;

    @Value("${sms.api.secret}")
    private String apiSecret;

    public SmsService() {
        // 반드시 계정 내 등록된 유효한 API 키, API Secret Key를 입력해주셔야 합니다!
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey,apiSecret, "https://api.coolsms.co.kr");
>>>>>>> 63d6fc3860df9f039783037c1b2909d5e77ffd2c
    }

    /**
     * 메시지 목록을 가져오는 메서드
     */
    public MessageListResponse getMessageList() {
        MessageListRequest request = new MessageListRequest();
        MessageListResponse response = this.messageService.getMessageList(request);
        return response;
    }

    /**
     * 단일 메시지 발송 메서드
     */
    public SingleMessageSentResponse sendManager(String from, String register) {
        Message message = new Message();
        message.setFrom(from);
        message.setTo("01045279904");
        message.setText(register + "님 에게 차량 예약신청이 들어왔습니다! ");
        return this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    public SingleMessageSentResponse sendRegister(String to) {
        Message message = new Message();
        message.setFrom("01022428885");
        message.setTo(to);
        message.setText("차량 예약이 승인 되었습니다.\n"
                + "게시판에 운행 전, 후 사진을 업로드 부탁드립니다.");
        return this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    /**
     * MMS 발송 메서드
     */
    public SingleMessageSentResponse sendMmsByResourcePath(String from, String to, String text, String resourcePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        File file = resource.getFile();
        String imageId = this.messageService.uploadFile(file, StorageType.MMS, null);

        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setText(text);
        message.setImageId(imageId);

        return this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    /**
     * 여러 메시지 발송 메서드
     */
    public MultipleDetailMessageSentResponse sendMany(ArrayList<Message> messageList) {
        try {
            return this.messageService.send(messageList, false, true);
        } catch (NurigoMessageNotReceivedException exception) {
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    /**
     * 예약 메시지 발송 메서드
     */
    public MultipleDetailMessageSentResponse sendScheduledMessages(ArrayList<Message> messageList, String scheduleTime) {
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(scheduleTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(localDateTime);
            Instant instant = localDateTime.toInstant(zoneOffset);
            return this.messageService.send(messageList, instant);
        } catch (NurigoMessageNotReceivedException exception) {
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    /**
     * 잔액 조회 메서드
     */
    public Balance getBalance() {
        return this.messageService.getBalance();
    }
}