package io.abun.wmb.TransactionService;

import io.abun.wmb.Constants.ConstantVariables;
import io.abun.wmb.TransactionService.dto.payment.PaymentCustomerRequest;
import io.abun.wmb.TransactionService.dto.payment.PaymentDetailRequest;
import io.abun.wmb.TransactionService.dto.payment.PaymentItemDetailRequest;
import io.abun.wmb.TransactionService.dto.payment.PaymentRequest;
import io.abun.wmb.TransactionService.interfaces.PaymentRepository;
import io.abun.wmb.TransactionService.interfaces.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final RestClient restClient;
    private final String SECRET_KEY;
    private final String BASE_URL_SNAP;

    @Autowired
    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            RestClient restClient,
            @Value("${midtrans.api.key}")
            String SECRET_KEY,
            @Value("${midtrans.api.snap-url}")
            String BASE_URL_SNAP) {
        this.paymentRepository = paymentRepository;
        this.restClient = restClient;
        this.SECRET_KEY = SECRET_KEY;
        this.BASE_URL_SNAP = BASE_URL_SNAP;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PaymentEntity create(BillEntity bill) {
        Integer amount = bill.getBillDetails()
                .stream().map(detail -> detail.getMenu().getPrice() * detail.getQuantity()).reduce(0, Integer::sum);

        PaymentDetailRequest paymentDetailRequest = new PaymentDetailRequest(
                amount,
                bill.getId().toString()
        );

        List<PaymentItemDetailRequest> paymentItemDetailRequests = bill.getBillDetails().stream().map(item -> {
            return new PaymentItemDetailRequest(
                    item.getQuantity(),
                    item.getMenu().getPrice(),
                    item.getMenu().getName());
        }).toList();

        PaymentCustomerRequest paymentCustomerRequest = bill.getCustomer() != null ? new PaymentCustomerRequest(bill.getCustomer().getName()) : new PaymentCustomerRequest("Guest");

        PaymentRequest paymentRequest = new PaymentRequest(
                paymentDetailRequest,
                paymentItemDetailRequests,
                ConstantVariables.SUPPORTED_PAYMENT_METHOD,
                paymentCustomerRequest
        );

        ResponseEntity<Map<String, String>> responseFromMidtrans = restClient.post()
                .uri(BASE_URL_SNAP)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + SECRET_KEY)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        Map<String, String> responseBodyFromMidtrans = responseFromMidtrans.getBody();

        if (responseBodyFromMidtrans == null) {
            return null;
        }

        return paymentRepository.saveAndFlush(PaymentEntity.builder()
                .token(responseBodyFromMidtrans.get("token"))
                .redirectUrl(responseBodyFromMidtrans.get("redirect_url"))
                .transactionStatus("ordered")
                .build());
    }
}
