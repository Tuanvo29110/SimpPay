package org.simpmc.simppay.service.database;

import com.j256.ormlite.dao.Dao;
import org.simpmc.simppay.data.PaymentType;
import org.simpmc.simppay.database.Database;
import org.simpmc.simppay.database.dto.PaymentRecord;
import org.simpmc.simppay.database.entities.BankingPayment;
import org.simpmc.simppay.database.entities.CardPayment;
import org.simpmc.simppay.database.entities.SPPlayer;
import org.simpmc.simppay.model.Payment;

import java.util.List;
import java.util.UUID;

public class PaymentLogService {
    private final Dao<BankingPayment, UUID> bankDao;
    private final Dao<CardPayment, UUID> cardDao;

    public PaymentLogService(Database database) {
        this.bankDao = database.getBankDao();
        this.cardDao = database.getCardDao();
    }

    // NOTE: Please convert Payment to BankingPayment or CardPayment to interact with database. We use ORMLite :D
    // TODO: Please, someone make a PR to optimize this code, i know it is trash, better when using proper SQL Query with JOIN and SUM :D
    public boolean todaysPaymentExists(UUID playerId) {
        try {
            List<BankingPayment> bankingPayments = bankDao.queryBuilder()
                    .where()
                    .eq("player_uuid", playerId)
                    .query();
            List<CardPayment> cardPayments = cardDao.queryBuilder()
                    .where()
                    .eq("player_uuid", playerId)
                    .query();
            return !bankingPayments.isEmpty() || !cardPayments.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<PaymentRecord> getPaymentsByPlayer(SPPlayer playerId) {
        try {
            List<PaymentRecord> payments = new java.util.ArrayList<>(bankDao.queryBuilder()
                    .where()
                    .eq("player_uuid", playerId)
                    .query()
                    .stream()
                    .map(PaymentRecord::fromBank)
                    .toList());

            payments.addAll(cardDao.queryBuilder()
                    .where()
                    .eq("player_uuid", playerId)
                    .query()
                    .stream()
                    .map(PaymentRecord::fromCard)
                    .toList());

            return payments;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public Double getPlayerTotalAmount(SPPlayer playerId) {
        try {
            List<BankingPayment> bankingPayments = bankDao.queryBuilder()
                    .where()
                    .eq("player_uuid", playerId)
                    .query();
            List<CardPayment> cardPayments = cardDao.queryBuilder()
                    .where()
                    .eq("player_uuid", playerId)
                    .query();
            double bankingTotal = bankingPayments.stream()
                    .mapToDouble(BankingPayment::getAmount)
                    .sum();
            double cardTotal = cardPayments.stream()
                    .mapToDouble(CardPayment::getAmount)
                    .sum();
            return bankingTotal + cardTotal;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public long getEntireServerAmount() {
        try {
            List<BankingPayment> bankingPayments = bankDao.queryForAll();
            List<CardPayment> cardPayments = cardDao.queryForAll();
            double bankingTotal = bankingPayments.stream()
                    .mapToDouble(BankingPayment::getAmount)
                    .sum();
            double cardTotal = cardPayments.stream()
                    .mapToDouble(CardPayment::getAmount)
                    .sum();
            return (long) (bankingTotal + cardTotal);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public void removePayment(Payment payment) {
        if (payment.getPaymentType() == PaymentType.BANKING) {
            removeBankingPayment(payment.getPaymentID());
        }
        if (payment.getPaymentType() == PaymentType.CARD) {
            removeCardPayment(payment.getPaymentID());
        }
        throw new IllegalArgumentException("Invalid payment type: " + payment.getPaymentType());
    }

    public void addPayment(Payment payment) {
        if (payment.getPaymentType() == PaymentType.BANKING) {
            addBankingPayment(new BankingPayment(payment));
            return;
        }
        if (payment.getPaymentType() == PaymentType.CARD) {
            addCardPayment(new CardPayment(payment));
            return;
        }
        throw new IllegalArgumentException("Invalid payment type: " + payment.getPaymentType());
    }

    private void addBankingPayment(BankingPayment payment) {
        try {
            bankDao.createOrUpdate(payment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCardPayment(CardPayment payment) {
        try {
            cardDao.createOrUpdate(payment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeBankingPayment(UUID paymentID) {
        try {

            BankingPayment payment = bankDao.queryForId(paymentID);
            bankDao.delete(payment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeCardPayment(UUID paymentID) {
        try {
            CardPayment payment = cardDao.queryForId(paymentID);
            cardDao.delete(payment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
