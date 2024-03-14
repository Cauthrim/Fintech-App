package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.account.db.Account;
import com.academy.fintech.pe.core.service.account.db.AccountService;
import com.academy.fintech.pe.core.service.agreement.db.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.AgreementService;
import com.academy.fintech.pe.core.service.product.db.Product;
import com.academy.fintech.pe.core.service.product.db.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AgreementCreationService {
    private final AgreementService agreementService;
    private final ProductService productService;
    private final AccountService accountService;

    @Transactional
    public String createAgreement(Agreement agreement) {
        Product product = productService.findByCode(agreement.getProductCode());

        if (product != null && checkAgreement(agreement, product)) {
            agreement = agreementService.save(agreement);

            Account account = new Account();
            account.setNonIdFields(agreement.getId(), "debit", new BigDecimal(0));
            accountService.save(account);

            account = new Account();
            account.setNonIdFields(agreement.getId(), "overdue", new BigDecimal(0));
            accountService.save(account);

            return agreement.getId();
        }

        return "";
    }

    private boolean checkAgreement(Agreement agreement, Product product) {
        return checkLoanTermBorders(agreement, product)
                && checkPrincipalAmountBorders(agreement, product)
                && checkInterestBorders(agreement, product)
                && checkOriginationAmountBorders(agreement, product);
    }

    private boolean checkLoanTermBorders(Agreement agreement, Product product) {
        int loanTerm = agreement.getTerm();
        return loanTerm >= product.getMinTerm() && loanTerm <= product.getMaxTerm();
    }

    private boolean checkPrincipalAmountBorders(Agreement agreement, Product product) {
        int principalAmount = agreement.getOriginationAmount().add(agreement.getDisbursementAmount()).intValue();
        return principalAmount >= product.getMinPrincipalAmount()
                && principalAmount <= product.getMaxPrincipalAmount();
    }

    private boolean checkInterestBorders(Agreement agreement, Product product) {
        BigDecimal interest = agreement.getInterest();
        return interest.compareTo(product.getMinInterest()) >= 0 && interest.compareTo(product.getMaxInterest()) <= 0;
    }

    private boolean checkOriginationAmountBorders(Agreement agreement, Product product) {
        int originationAmount = agreement.getOriginationAmount().intValue();
        return originationAmount >= product.getMinOriginationAmount()
                && originationAmount <= product.getMaxOriginationAmount();
    }
}
