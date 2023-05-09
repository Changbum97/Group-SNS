package study.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import study.sns.domain.entity.User;
import study.sns.domain.exception.AppException;
import study.sns.domain.exception.ErrorCode;
import study.sns.repository.UserRepository;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final StringRedisTemplate stringRedisTemplate;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public Boolean sendAuthEmail(String email) {

        try {
            String authCode = makeCode();

            stringRedisTemplate.delete(email + "_authCode");
            ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
            stringStringValueOperations.set(email + "_authCode", authCode,
                    60 * 6, TimeUnit.SECONDS);   // 6분간 저장

            sendEmail(email, "Our Story 회원가입 이메일 인증", "회원가입 인증 코드입니다.", "CODE", authCode);
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

    public Boolean sendId(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        try {
            sendEmail(email, "Our Story 아이디 찾기", "회원님의 아이디 입니다.", "ID", user.getLoginId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Boolean sendPw(String email, String loginId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!user.getLoginId().equals(loginId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        String newPw = makeCode();

        try {
            sendEmail(email, "Our Story 비밀번호 찾기", "회원님의 새 비밀번호 입니다.", "PW", newPw);
        } catch (Exception e) {
            return false;
        }

        user.setNewPassword(encoder.encode(newPw));
        return true;
    }

    private void sendEmail(String email, String title, String subTitle, String key, String value) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject(title);

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1>Our Story 입니다.</h1><br/>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana; width: 50%';>";
        msgg += "<h3 style='color:blue;'>" + subTitle + "</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += key + " : <strong>";
        msgg += value;
        msgg += "</strong><div><br/></div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("chb20050@gmail.com.com", "Out Story"));    // 보내는 사람

        mailSender.send(message);
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
