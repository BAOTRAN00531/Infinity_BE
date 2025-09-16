//package com.example.infinityweb_be.service.order.momo;
//
//import com.example.infinityweb_be.domain.Order;
//import com.example.infinityweb_be.domain.dto.order.CreateOrderRequest;
//import com.example.infinityweb_be.domain.dto.order.OrderResponse;
//import com.example.infinityweb_be.domain.dto.order.PaymentMethod;
//import com.example.infinityweb_be.domain.dto.order.momo.MomoPaymentRequest;
//import com.example.infinityweb_be.domain.dto.order.momo.MomoResult;
//import com.example.infinityweb_be.service.order.OrderService;
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class MomoService {
//
//    private final OrderService orderService;
//
//    @Value("${momo.accessKey}")
//    private String accessKey;
//
//    @Value("${momo.secretKey}")
//    private String secretKey;
//
//    @Value("${momo.partnerCode}")
//    private String partnerCode;
//
//    @Value("${momo.returnUrl}")
//    private String returnUrl;
//
//    @Value("${momo.notifyUrl}")
//    private String notifyUrl;
//
//    @Value("${momo.endpoint}")
//    private String endpoint;
//
//    @Value("${momo.create-url}")
//    private String createUrl;
//
//    public String buildRedirectUrl(Order order) {
//        String requestId = UUID.randomUUID().toString();
//        String orderId = order.getOrderCode();
//
//        Map<String, Object> body = new LinkedHashMap<>();
//        body.put("partnerCode", partnerCode);
//        body.put("accessKey", accessKey);
//        body.put("requestId", requestId);
//        body.put("amount", order.getTotalAmount().stripTrailingZeros().toPlainString());
//        body.put("orderId", orderId);
//        body.put("orderInfo", "Thanh toán khóa học");
//        body.put("redirectUrl", returnUrl + "?orderId=" + orderId);
//        body.put("ipnUrl", notifyUrl);
//        body.put("extraData", "");
//        body.put("requestType", "captureWallet");
//        body.put("lang", "vi");
//
//        // ✅ Dữ liệu để tạo chữ ký
//        String rawHash = String.format(
//                "accessKey=%s&amount=%s&extraData=&ipnUrl=%s&orderId=%s&orderInfo=Thanh toán khóa học&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=captureWallet",
//                accessKey,
//                order.getTotalAmount().stripTrailingZeros().toPlainString(),
//                notifyUrl,
//                orderId,
//                partnerCode,
//                returnUrl + "?orderId=" + orderId,
//                requestId
//        );
//
//        String signature = hmacSHA256(rawHash, secretKey);
//        body.put("signature", signature);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, headers);
//
//        log.info("[MoMo] Gửi request đến URL: {}", endpoint + "/create");
//        log.debug("[MoMo] Payload gửi đi: {}", body);
//
//        try {
//            RestTemplate restTemplate = new RestTemplate();
//            Map<String, Object> response = restTemplate.postForObject(endpoint + "/create", req, Map.class);
//
//            log.debug("[MoMo] Phản hồi: {}", response);
//
//            if (response != null && response.containsKey("payUrl")) {
//                return (String) response.get("payUrl");
//            } else {
//                log.error("[MoMo] Không có payUrl trong phản hồi MoMo: {}", response);
//                throw new RuntimeException("Lỗi gọi MoMo: Không có URL thanh toán MoMo trong phản hồi");
//            }
//        } catch (Exception e) {
//            log.error("[MoMo] Lỗi HTTP: {}", e.getMessage());
//            throw new RuntimeException("Lỗi gọi MoMo: " + e.getMessage());
//        }
//    }
//
//
//
//    private String hmacSHA256(String data, String key) {
//        try {
//            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//            Mac mac = Mac.getInstance("HmacSHA256");
//            mac.init(secretKeySpec);
//            byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
//            return bytesToHex(bytes);
//        } catch (Exception e) {
//            throw new RuntimeException("Cannot generate HMAC", e);
//        }
//    }
//
//    private String bytesToHex(byte[] bytes) {
//        StringBuilder result = new StringBuilder();
//        for (byte b : bytes)
//            result.append(String.format("%02x", b));
//        return result.toString();
//    }
//
//    public boolean verifySignature(MomoResult result) {
//        String rawData = String.format(
//                "accessKey=%s&amount=%s&extraData=%s&message=%s&orderId=%s&orderInfo=%s&orderType=%s&partnerCode=%s&payType=%s&requestId=%s&responseTime=%s&resultCode=%s&transId=%s",
//                accessKey,
//                result.getAmount(),
//                result.getExtraData(),
//                result.getMessage(),
//                result.getOrderId(),
//                result.getOrderInfo(),
//                result.getOrderType(),
//                result.getPartnerCode(),
//                result.getPayType(),
//                result.getRequestId(),
//                result.getResponseTime(),
//                result.getResultCode(),
//                result.getTransId()
//        );
//
//        String expected = hmacSHA256(rawData, secretKey);
//        return expected.equals(result.getSignature());
//    }
//
//    public String createMomoPayment(MomoPaymentRequest request) {
//        OrderResponse orderResponse = orderService.createOrder(
//                new CreateOrderRequest(
//                        request.getUserId(),
//                        PaymentMethod.MOMO,
//                        request.getCourseId()
//                )
//        );
//
//        Order order = orderService.findByOrderCode(orderResponse.getOrderCode())
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        return buildRedirectUrl(order);
//    }
//}
