package study.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import study.sns.domain.exception.AppException;
import study.sns.domain.exception.ErrorCode;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final StringRedisTemplate stringRedisTemplate;

    public Boolean sendAuthEmail(String email) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            String authCode = makeCode();

            stringRedisTemplate.delete(email + "_authCode");
            ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
            stringStringValueOperations.set(email + "_authCode", authCode,
                    60 * 6, TimeUnit.SECONDS);   // 6분간 저장

            message.addRecipients(Message.RecipientType.TO, email);
            message.setSubject("Our Story 회원가입 이메일 인증");

            String msgg = "";
            msgg += "<div style='margin:100px;'>";
            msgg += "<h1>Our Story 입니다.</h1><br/>";
            msgg += "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
            msgg += "<p>감사합니다!<p><br/>";
            msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
            msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
            msgg += "<div style='font-size:130%'>";
            msgg += "CODE : <strong>";
            msgg += authCode;
            msgg += "</strong><div><br/></div>";
            message.setText(msgg, "utf-8", "html");//내용
            message.setFrom(new InternetAddress("chb20050@gmail.com.com", "Out Story"));//보내는 사람

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean checkEmailAuth(String email, String emailAuth) {
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        if (stringStringValueOperations.get(email + "_authCode") != null &&
                stringStringValueOperations.get(email + "_authCode").equals(emailAuth)) {
            return true;
        }
        return false;
    }

    private String makeCode() {
        String code = "";
        Random random = new Random();

        // 인증 번호 8자리
        for (int i = 0 ; i < 8 ; i ++) {
            int temp = random.nextInt(3);

            if (temp == 0) code += (char) (random.nextInt(26) + 'A');
            else if (temp == 1) code += (char) (random.nextInt(26) + 'a');
            else code += random.nextInt(10);
        }

        return code;
    }
}
