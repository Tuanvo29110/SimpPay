package org.simpmc.simppay.commands.root;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LongArgument;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.commands.sub.banking.CancelCommand;
import org.simpmc.simppay.config.ConfigManager;
import org.simpmc.simppay.config.types.BankingConfig;
import org.simpmc.simppay.config.types.MessageConfig;
import org.simpmc.simppay.data.PaymentStatus;
import org.simpmc.simppay.model.Payment;
import org.simpmc.simppay.model.detail.BankingDetail;
import org.simpmc.simppay.model.detail.PaymentDetail;
import org.simpmc.simppay.util.MessageUtil;
import org.simpmc.simppay.util.SoundUtil;
import org.simpmc.simppay.util.qrcode.MapQR;

import java.util.UUID;

@SuppressWarnings("unboxing") // ignore unboxing create NPE
public class BankingCommand {
    public BankingCommand() {
        new CommandAPICommand("banking")
                .withPermission("simppay.banking")
                .withAliases("bank")
                .withSubcommands(
                        CancelCommand.commandCreate()
                )
                .withArguments(
                        new LongArgument("amount")
                )
                .executesPlayer((player, args) -> {
                    // start a new banking session
                    MessageConfig messageConfig = ConfigManager.getInstance().getConfig(MessageConfig.class);
                    BankingConfig bankingConfig = ConfigManager.getInstance().getConfig(BankingConfig.class);
                    SPPlugin plugin = SPPlugin.getInstance();
                    // check min amount
                    if ((long) args.get("amount") < bankingConfig.minBanking) {
                        MessageUtil.sendMessage(player, messageConfig.invalidAmount.replace("{amount}", String.valueOf(bankingConfig.minBanking)));
                        SoundUtil.sendSound(player, messageConfig.soundEffect.get(PaymentStatus.FAILED).toSound());
                        return;
                    }
                    // amount must be diviable by 1000
                    if ((Long) args.get("amount") % 1000 != 0) {
                        MessageUtil.sendMessage(player, messageConfig.mustDivisibleBy1000);
                        SoundUtil.sendSound(player, messageConfig.soundEffect.get(PaymentStatus.FAILED).toSound());
                        return;
                    }
                    if (plugin.getPaymentService().getPlayerBankingSessionPayment().containsKey(player.getUniqueId())) {
                        // resend qr map if player is in banking session
                        MessageUtil.sendMessage(player, messageConfig.existBankingSession);
                        byte[] qrMap = plugin.getPaymentService().getPlayerBankQRCode().get(player.getUniqueId());
                        MapQR.sendPacketQRMap(qrMap, player);
                        return;
                    }
                    UUID uuid = UUID.randomUUID(); // payment uuid is randomized

                    PaymentDetail detail = BankingDetail.builder()
                            .amount((Long) args.get("amount"))
                            .build();

                    Payment payment = new Payment(uuid, player.getUniqueId(), detail);

                    PaymentStatus status = SPPlugin.getInstance().getPaymentService().sendBank(payment);
                    if (status == PaymentStatus.EXIST) {
                        MessageUtil.sendMessage(player, messageConfig.unknownErrror);
                        SoundUtil.sendSound(player, messageConfig.soundEffect.get(PaymentStatus.PENDING).toSound());
                        return;
                    }
                    if (status == PaymentStatus.FAILED) {
                        MessageUtil.sendMessage(player, messageConfig.failedCard);
                        SoundUtil.sendSound(player, messageConfig.soundEffect.get(PaymentStatus.FAILED).toSound());
                        return;
                    }
                    plugin.getPaymentService().getPlayerBankingSessionPayment().put(player.getUniqueId(), uuid);

                })
                .register();
    }
}
