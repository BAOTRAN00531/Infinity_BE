package com.example.infinityweb_be.service.order.VNPAY;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPayService {

    @Value("${vnpay.tmnCode}")
    private String vnp_TmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;

    @Value("${vnpay.payUrl}")
    private String vnp_Url;

    @Value("${vnpay.returnUrl}")
    private String vnp_ReturnUrl;

    public String createPaymentUrl(String orderId, long amount, HttpServletRequest request) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo = "Thanh toan don hang " + orderId;
        String vnp_OrderType = "other";
        String vnp_Locale = "vn";
        String vnp_CurrCode = "VND";

        String vnp_TxnRef = orderId;
        String vnp_IpAddr = request.getRemoteAddr();

        long amountInCents = amount * 100;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amountInCents));
        vnp_Params.put("vnp_CurrCode", vnp_CurrCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        // Thêm thời gian tạo giao dịch
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String vnp_CreateDate = LocalDateTime.now().format(formatter);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        // Sắp xếp key theo thứ tự alphabet
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String value = vnp_Params.get(fieldName);
            if (value != null && !value.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
                query.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
            }
        }

        // Bỏ dấu & cuối cùng
        hashData.setLength(hashData.length() - 1);
        query.setLength(query.length() - 1);

        // Tạo chữ ký SHA256
        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        return vnp_Url + "?" + query.toString();
    }

    private String hmacSHA512(String data, String key) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(secretKeySpec);
            byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(bytes);  // Dùng lại function bạn đã viết cho MoMo
        } catch (Exception e) {
            throw new RuntimeException("Cannot generate HMAC SHA512", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes)
            result.append(String.format("%02x", b));
        return result.toString();
    }

}
