package com.lmora.saldapp.domain.service;

import com.lmora.saldapp.domain.model.Balance;
import com.lmora.saldapp.domain.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("SettlementCalculator")
public class SettlementCalculatorTest {

    // Helpers
    private Balance balance(long id, String amount){
        return new Balance(id, new BigDecimal(BigInteger.ZERO), new BigDecimal(amount));
    }

    // Inputs Validation
    @Nested
    @DisplayName("input validation")
    class InputValidation {

        @Test
        @DisplayName("throws when list is null")
        @SuppressWarnings("DataFlowIssue")
        void throwsWhenListIsNull() {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> SettlementCalculator.calculate(null))
                    .withMessage("balances can't be null");
        }

        @Test
        @DisplayName("throws when list contains a null element")
        void throwsWhenElementIsNull() {
            List<Balance> balances = new java.util.ArrayList<>();
            balances.add(null);

            assertThatIllegalArgumentException()
                    .isThrownBy(() -> SettlementCalculator.calculate(balances))
                    .withMessageContaining("null");
        }

        @Test
        @DisplayName("throws when balance field is null")
        void throwsWhenBalanceFieldIsNull() {
            Balance invalid = new Balance(1L, null, null);

            assertThatIllegalArgumentException()
                    .isThrownBy(() -> SettlementCalculator.calculate(List.of(invalid)))
                    .withMessageContaining("null");
        }

        @Test
        @DisplayName("throws when integrantId is null")
        void throwsWhenIntegrantIdIsNull() {
            Balance invalid = new Balance(null, new BigDecimal("100"), new BigDecimal("0"));

            assertThatIllegalArgumentException()
                    .isThrownBy(() -> SettlementCalculator.calculate(List.of(invalid)))
                    .withMessageContaining("null");
        }
    }

    // Edge Cases
    @Nested
    @DisplayName("edge cases")
    class EdgeCases {

        @Test
        @DisplayName("returns empty list when input is empty")
        void returnsEmptyWhenInputIsEmpty() {
            List<Transaction> result = SettlementCalculator.calculate(Collections.emptyList());

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("returns empty list when all balances are zero")
        void returnsEmptyWhenAllBalancesAreZero() {
            List<Balance> balances = List.of(
                    balance(1L, "0"),
                    balance(2L, "0")
            );

            assertThat(SettlementCalculator.calculate(balances)).isEmpty();
        }

        @Test
        @DisplayName("returns empty list when there is only one participant with non-zero balance")
        void returnsEmptyWithOnlyOneNonZeroBalance() {
            // without a counterparty, the single non-zero balance can't be settled
            List<Balance> balances = List.of(balance(1L, "-100"));

            assertThat(SettlementCalculator.calculate(balances)).isEmpty();
        }
    }

    // Algorithm  correctness
    @Nested
    @DisplayName("algorithm correctness")
    class AlgorithmCorrectness {

        @Test
        @DisplayName("one debtor and one creditor with equal amounts settle in one transaction")
        void exactPairSettlesInOneTransaction() {
            List<Balance> balances = List.of(
                    balance(1L, "-100"),
                    balance(2L, "100")
            );

            List<Transaction> result = SettlementCalculator.calculate(balances);

            assertThat(result).hasSize(1);
            assertThat(result.getFirst().getFromIntegrantId()).isEqualTo(1L);
            assertThat(result.getFirst().getToIntegrantId()).isEqualTo(2L);
            assertThat(result.getFirst().getAmount()).isEqualByComparingTo("100");
        }

        @Test
        @DisplayName("debtor owes more than creditor is owed — creditor settles first")
        void debtorOwesMoreThanCreditor() {
            List<Balance> balances = List.of(
                    balance(1L, "-100"),
                    balance(2L,  "+60"),
                    balance(3L,  "+40")
            );

            List<Transaction> result = SettlementCalculator.calculate(balances);

            // 3 participantes → máximo 2 transacciones
            assertThat(result).hasSize(2);
            assertTotalAmountMatchesTotalDebt(result, "100");
        }

        @Test
        @DisplayName("creditor is owed more than debtor owes — debtor settles first")
        void creditorIsOwedMoreThanDebtor() {
            List<Balance> balances = List.of(
                    balance(1L, "-40"),
                    balance(2L, "-60"),
                    balance(3L, "+100")
            );

            List<Transaction> result = SettlementCalculator.calculate(balances);

            assertThat(result).hasSize(2);
            assertTotalAmountMatchesTotalDebt(result, "100");
        }

        @Test
        @DisplayName("multiple debtors and creditors resolve correctly")
        void multipleDebtorsAndCreditors() {
            List<Balance> balances = List.of(
                    balance(1L, "-90"),
                    balance(2L, "-60"),
                    balance(3L, "-30"),
                    balance(4L,  "+90"),
                    balance(5L,  "+60"),
                    balance(6L,  "+30")
            );

            List<Transaction> result = SettlementCalculator.calculate(balances);

            // 6 participantes → máximo 5 transacciones
            assertThat(result).hasSizeLessThanOrEqualTo(5);
            assertTotalAmountMatchesTotalDebt(result, "180");
            assertAllAmountsArePositive(result);
        }

        @Test
        @DisplayName("exact pairs cancel in minimum transactions (double elimination)")
        void exactPairsCancelInMinimumTransactions() {
            List<Balance> balances = List.of(
                    balance(1L, "-50"),
                    balance(2L, "-30"),
                    balance(3L,  "+50"),
                    balance(4L,  "+30")
            );

            List<Transaction> result = SettlementCalculator.calculate(balances);

            // pares exactos → 2 transacciones en lugar de 3
            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("all transaction amounts are positive")
        void allAmountsArePositive() {
            List<Balance> balances = List.of(
                    balance(1L, "-200"),
                    balance(2L,  "+80"),
                    balance(3L,  "+70"),
                    balance(4L,  "+50")
            );

            List<Transaction> result = SettlementCalculator.calculate(balances);

            assertAllAmountsArePositive(result);
        }
    }

    // Helpers
    private void assertTotalAmountMatchesTotalDebt(List<Transaction> transactions, String expected) {
        BigDecimal total = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertThat(total).isEqualByComparingTo(new BigDecimal(expected));
    }

    private void assertAllAmountsArePositive(List<Transaction> transactions) {
        assertThat(transactions)
                .allSatisfy(t -> assertThat(t.getAmount())
                        .isGreaterThan(BigDecimal.ZERO));
    }
}
